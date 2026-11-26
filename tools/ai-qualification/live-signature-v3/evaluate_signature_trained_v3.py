import json, hashlib, time, sys
from pathlib import Path
sys.stdout.reconfigure(encoding='utf-8')
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM
from peft import PeftModel
source=Path('C:/workspace/Vixen/tools/ai-qualification/live-signature-v1')
out=Path('C:/workspace/TUltra-Project/LayerTime/tools/ci/logs')/('signature-trained-v3-validation-'+time.strftime('%Y%m%dT%H%M%S'))
out.mkdir(parents=True,exist_ok=False)
run=Path('C:/workspace/TUltra-Project/LayerTime/tools/ai-training/runs/live-signature-v3')
prepared=json.loads((run/'prepared.json').read_text())
curriculum=json.loads((run/'store/curricula'/ (prepared['curriculumId']+'.json')).read_text())
cases=[dict(id=e['id'],input=json.loads(e['messages'][-2]['content']),expected=json.loads(e['messages'][-1]['content'])) for e in curriculum['examples'] if e['split']=='validation']
system=curriculum['examples'][0]['messages'][0]['content']
base=Path('C:/workspace/TUltra-Project/LayerTime/tools/ai-training/models/qwen3-0.6b-base')
adapter=run/'store/jobs/5044f9eab863461b8ad7442af325f6f9/adapter'
tok=AutoTokenizer.from_pretrained(base,local_files_only=True)
model=AutoModelForCausalLM.from_pretrained(base,local_files_only=True,torch_dtype=torch.float16,attn_implementation='eager').to('cuda').eval()
for condition in ['direct-adapter']:
 if condition=='direct-adapter': model=PeftModel.from_pretrained(model,adapter,local_files_only=True,is_trainable=False).eval()
 results=[]
 for case in cases:
  prompt=tok.apply_chat_template([{'role':'system','content':system},{'role':'user','content':json.dumps(case['input'],separators=(',',':'))}],tokenize=False,add_generation_prompt=True,enable_thinking=False)
  inputs=tok(prompt,return_tensors='pt',add_special_tokens=False).to('cuda'); start=time.monotonic()
  with torch.inference_mode(): generated=model.generate(**inputs,max_new_tokens=128,do_sample=False,pad_token_id=tok.eos_token_id)
  raw=tok.decode(generated[0,inputs.input_ids.shape[1]:],skip_special_tokens=True)
  try:
   parsed=json.loads(raw); valid=isinstance(parsed,dict) and set(parsed)=={'pattern'} and parsed['pattern'] in curriculum['examples'][0]['outputSchema']
   correct=valid and parsed==case['expected']
  except (ValueError,TypeError,KeyError): valid=False; correct=False
  row={'id':case['id'],'output':raw,'schemaValid':valid,'correct':correct,'seconds':time.monotonic()-start,'promptSha256':hashlib.sha256(prompt.encode()).hexdigest()};results.append(row)
  (out/(condition+'.json')).write_text(json.dumps({'condition':condition,'results':results,'fieldQualified':False,'finalQuantizedModel':False},indent=2),encoding='utf-8')
  print(condition,case['id'],correct,raw,flush=True)
 print('TOTAL',condition,sum(x['correct'] for x in results),'/',len(results),flush=True)
print('REPORT',out,flush=True)
