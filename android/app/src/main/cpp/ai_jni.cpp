#include <jni.h>
#include "llama.h"
#include "prompt_cache.h"
#include <algorithm>
#include <atomic>
#include <memory>
#include <mutex>
#include <stdexcept>
#include <string>
#include <unordered_map>
#include <vector>
#include <chrono>
#include <android/log.h>
#include <iostream>
#include <streambuf>

namespace {
#ifdef LT_AI_VULKAN
// Preserve backend device/driver failures in Android diagnostics; no prompt text is logged.
class AndroidBackendLog final:public std::streambuf {
    std::mutex mutex;std::string line;
    void append(char ch) {
        if(ch=='\n' || line.size()>=3500) {
            if(!line.empty())__android_log_print(ANDROID_LOG_INFO,"LayerTimeAI.Backend","%s",line.c_str());
            line.clear();
        }
        if(ch!='\n')line+=ch;
    }
    int_type overflow(int_type ch) override {
        if(!traits_type::eq_int_type(ch,traits_type::eof())) {std::lock_guard<std::mutex> lock(mutex);append(traits_type::to_char_type(ch));}
        return traits_type::not_eof(ch);
    }
    std::streamsize xsputn(const char* s,std::streamsize n) override {
        std::lock_guard<std::mutex> lock(mutex);for(std::streamsize i=0;i<n;++i)append(s[i]);return n;
    }
    int sync() override {return 0;}
};
#endif
struct Session {
    std::unique_ptr<llama_model, decltype(&llama_model_free)> model{nullptr, llama_model_free};
    std::unique_ptr<llama_context, decltype(&llama_free)> context{nullptr, llama_free};
    std::vector<llama_token> prompt_tokens;
    bool gpu=false;
    std::atomic<int64_t> cancelled_through{0};
    std::mutex run;
};
// A cancelled/failed decode may leave a partial KV sequence. Never reuse it.
struct ContextRun {
    Session& session; bool committed=false;
    ~ContextRun() {
        if(session.context) {
            llama_set_abort_callback(session.context.get(),nullptr,nullptr);
            if(!committed) {
                llama_memory_clear(llama_get_memory(session.context.get()),false);
                session.prompt_tokens.clear();
            }
        }
    }
};
std::mutex handles_mutex;
std::unordered_map<int64_t, std::shared_ptr<Session>> handles;
std::atomic<int64_t> next_handle{1};
std::once_flag backend;

std::shared_ptr<Session> get(int64_t handle) {
    std::lock_guard<std::mutex> lock(handles_mutex);
    auto it=handles.find(handle);
    if(it==handles.end()) throw std::runtime_error("The local model has been closed.");
    return it->second;
}
void fail(JNIEnv* env,const char* message) {env->ThrowNew(env->FindClass("java/lang/IllegalStateException"),message);}
std::string bytes(JNIEnv* env,jbyteArray data) {
    if(!data)throw std::runtime_error("Missing model input.");
    const jsize size=env->GetArrayLength(data);
    if(size>1024*1024)throw std::runtime_error("Model input is too large.");
    std::string out(size,'\0');
    env->GetByteArrayRegion(data,0,size,reinterpret_cast<jbyte*>(out.data()));
    return out;
}
std::vector<llama_token> tokenize(const llama_vocab* vocab,const std::string& text) {
    const int count=llama_tokenize(vocab,text.data(),static_cast<int32_t>(text.size()),nullptr,0,true,true);
    if(count>=0)throw std::runtime_error("Could not measure model input.");
    std::vector<llama_token> tokens(-count);
    const int actual=llama_tokenize(vocab,text.data(),static_cast<int32_t>(text.size()),tokens.data(),static_cast<int32_t>(tokens.size()),true,true);
    if(actual<0)throw std::runtime_error("Could not tokenize model input.");
    tokens.resize(actual);return tokens;
}
struct Run {Session* session;int64_t id;};
bool aborted(void* data) {
    auto* run=static_cast<Run*>(data);
    return run->session->cancelled_through.load()>=run->id;
}
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_projectrevivesolutions_layertime_controller_ai_runtime_NativeLlama_load(JNIEnv* env,jobject,jstring path) {
    try {
        std::call_once(backend,[]{
#ifdef LT_AI_VULKAN
            // Process-lifetime buffer: std::cerr must remain valid through native teardown.
            std::cerr.rdbuf(new AndroidBackendLog());
#endif
            llama_backend_init();
        });
        const char* value=env->GetStringUTFChars(path,nullptr);
        if(!value)throw std::runtime_error("Missing model path.");
        const std::string filename(value);env->ReleaseStringUTFChars(path,value);
        auto session=std::make_shared<Session>();
        auto params=llama_model_default_params();params.n_gpu_layers=0;params.load_mode=LLAMA_LOAD_MODE_MMAP;
#ifdef LT_AI_VULKAN
        session->gpu=llama_supports_gpu_offload();
        params.n_gpu_layers=session->gpu?999:0;
#endif
        __android_log_print(ANDROID_LOG_INFO,"LayerTimeAI","modelBackend=%s",session->gpu?"GPU":"CPU");
        session->model.reset(llama_model_load_from_file(filename.c_str(),params));
        if(!session->model)throw std::runtime_error("The bundled model could not be loaded.");
        const auto id=next_handle.fetch_add(1);
        std::lock_guard<std::mutex> lock(handles_mutex);handles.emplace(id,session);return id;
    } catch(const std::exception& ex){fail(env,ex.what());return 0;}
}

extern "C" JNIEXPORT jint JNICALL
Java_com_projectrevivesolutions_layertime_controller_ai_runtime_NativeLlama_countTokens(JNIEnv* env,jobject,jlong handle,jbyteArray prompt) {
    try {auto session=get(handle);return static_cast<jint>(tokenize(llama_model_get_vocab(session->model.get()),bytes(env,prompt)).size());}
    catch(const std::exception& ex){fail(env,ex.what());return 0;}
}

extern "C" JNIEXPORT void JNICALL
Java_com_projectrevivesolutions_layertime_controller_ai_runtime_NativeLlama_cancel(JNIEnv*,jobject,jlong handle,jlong request) {
    try {
        auto session=get(handle);auto old=session->cancelled_through.load();
        while(old<request && !session->cancelled_through.compare_exchange_weak(old,request)){}
    }catch(...){/* A late cancellation after close is harmless. */}
}

extern "C" JNIEXPORT void JNICALL
Java_com_projectrevivesolutions_layertime_controller_ai_runtime_NativeLlama_close(JNIEnv*,jobject,jlong handle) {
    std::lock_guard<std::mutex> lock(handles_mutex);handles.erase(handle);
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_projectrevivesolutions_layertime_controller_ai_runtime_NativeLlama_generate(
    JNIEnv* env,jobject,jlong handle,jlong request,jbyteArray input,jbyteArray grammar,jint max_tokens,jint context_tokens,jint threads) {
    try {
        auto session=get(handle);std::lock_guard<std::mutex> lock(session->run);
        if(request<1 || max_tokens<1 || max_tokens>4096 || context_tokens<256 || context_tokens>16384)
            throw std::runtime_error("Invalid local inference limits.");
        Run run{session.get(),request};
        if(aborted(&run))throw std::runtime_error("Local inference cancelled.");
        const auto* vocab=llama_model_get_vocab(session->model.get());
        auto tokens=tokenize(vocab,bytes(env,input));
        if(tokens.empty() || tokens.size()+max_tokens>static_cast<size_t>(context_tokens))
            throw std::runtime_error("Model context budget exceeded; input was not truncated.");
        const auto context_start=std::chrono::steady_clock::now();
        ContextRun context_run{*session};
        auto params=llama_context_default_params();
        const int required=static_cast<int>(tokens.size())+max_tokens;
        params.n_ctx=std::min(context_tokens,std::max(2048,(required+255)/256*256));
        params.n_batch=512;params.n_ubatch=512;
        params.n_threads=std::clamp(threads,1,4);params.n_threads_batch=std::clamp(threads,1,6);
        params.offload_kqv=session->gpu;params.no_perf=true;
        if(!session->context || llama_n_ctx(session->context.get())<static_cast<unsigned>(required)) {
            session->prompt_tokens.clear();
            session->context.reset(llama_init_from_model(session->model.get(),params));
        }
        auto* ctx=session->context.get();
        if(!ctx)throw std::runtime_error("Not enough memory to create the local model context.");
        llama_set_n_threads(ctx,params.n_threads,params.n_threads_batch);
        llama_set_abort_callback(ctx,aborted,&run);
        auto reused=ltai::reusable_prompt_prefix(session->prompt_tokens,tokens);
        auto memory=llama_get_memory(ctx);
        if(reused==0 || !llama_memory_seq_rm(memory,0,static_cast<llama_pos>(reused),-1)) {
            reused=0;llama_memory_clear(memory,false);
        }
        const auto prefill_start=std::chrono::steady_clock::now();
        for(size_t i=reused;i<tokens.size();i+=512) {
            if(aborted(&run))throw std::runtime_error("Local inference cancelled.");
            const int n=static_cast<int>(std::min<size_t>(512,tokens.size()-i));
            if(llama_decode(ctx,llama_batch_get_one(tokens.data()+i,n))!=0)
                throw std::runtime_error("Local model prefill failed or was cancelled.");
        }
        session->prompt_tokens=tokens;
        const auto decode_start=std::chrono::steady_clock::now();
        std::unique_ptr<llama_sampler,decltype(&llama_sampler_free)> sampler(
            llama_sampler_chain_init(llama_sampler_chain_default_params()),llama_sampler_free);
        const auto rules=bytes(env,grammar);
        auto* constrained=llama_sampler_init_grammar(vocab,rules.c_str(),"root");
        if(!constrained)throw std::runtime_error("Invalid output grammar.");
        llama_sampler_chain_add(sampler.get(),constrained);
        llama_sampler_chain_add(sampler.get(),llama_sampler_init_greedy());
        std::string output;
        int generated=0;
        for(int i=0;i<max_tokens;++i) {
            if(aborted(&run))throw std::runtime_error("Local inference cancelled.");
            auto token=llama_sampler_sample(sampler.get(),ctx,-1);
            if(llama_vocab_is_eog(vocab,token))break;
            ++generated;
            char small[256];int n=llama_token_to_piece(vocab,token,small,sizeof(small),0,false);
            if(n<0){std::vector<char> large(-n);n=llama_token_to_piece(vocab,token,large.data(),large.size(),0,false);if(n>0)output.append(large.data(),n);}
            else if(n>0)output.append(small,n);
            if(output.size()>1024*1024)throw std::runtime_error("Local model output exceeded its limit.");
            if(i+1<max_tokens && llama_decode(ctx,llama_batch_get_one(&token,1))!=0)
                throw std::runtime_error("Local model decode failed or was cancelled.");
        }
        const auto done=std::chrono::steady_clock::now();
        auto millis=[](auto a,auto b){return static_cast<long long>(std::chrono::duration_cast<std::chrono::milliseconds>(b-a).count());};
        __android_log_print(ANDROID_LOG_INFO,"LayerTimeAI","contextMs=%lld prefillMs=%lld decodeMs=%lld outputTokens=%d contextTokens=%u decodeThreads=%d batchThreads=%d promptTokens=%zu reusedTokens=%zu prefilledTokens=%zu",
            millis(context_start,prefill_start),millis(prefill_start,decode_start),millis(decode_start,done),generated,llama_n_ctx(ctx),params.n_threads,params.n_threads_batch,tokens.size(),reused,tokens.size()-reused);
        jbyteArray result=env->NewByteArray(static_cast<jsize>(output.size()));
        if(result)env->SetByteArrayRegion(result,0,static_cast<jsize>(output.size()),reinterpret_cast<const jbyte*>(output.data()));
        context_run.committed=!env->ExceptionCheck();
        return result;
    }catch(const std::exception& ex){fail(env,ex.what());return nullptr;}
}
