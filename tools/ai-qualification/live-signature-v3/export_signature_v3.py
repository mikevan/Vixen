from pathlib import Path
import json,sys,subprocess,torch
from transformers import AutoModelForCausalLM,AutoTokenizer
from peft import PeftModel
root=Path('C:/workspace/TUltra-Project/LayerTime/tools/ai-training/runs/live-signature-v3')
base=Path('C:/workspace/TUltra-Project/LayerTime/tools/ai-training/models/qwen3-0.6b-base')
adapter=root/'store/jobs/5044f9eab863461b8ad7442af325f6f9/adapter'
model=AutoModelForCausalLM.from_pretrained(base,local_files_only=True,dtype=torch.float16,attn_implementation='eager')
model=PeftModel.from_pretrained(model,adapter,local_files_only=True).merge_and_unload()
folder=root/'merged';folder.mkdir(exist_ok=False)
model.save_pretrained(folder,safe_serialization=True)
AutoTokenizer.from_pretrained(base,local_files_only=True).save_pretrained(folder)
subprocess.run([sys.executable,'C:/workspace/TUltra-Project/LayerTime/third_party/llama.cpp/convert_hf_to_gguf.py',str(folder),'--outfile',str(root/'weights-f16.gguf'),'--outtype','f16'],check=True)
print('EXPORTED_F16',flush=True)
