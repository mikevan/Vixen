import argparse, json, hashlib, time, sys
from pathlib import Path
sys.stdout.reconfigure(encoding='utf-8')
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM
from peft import PeftModel
parser=argparse.ArgumentParser()
parser.add_argument('--base',type=Path,required=True)
parser.add_argument('--adapter',type=Path,required=True)
parser.add_argument('--output',type=Path,required=True)
args=parser.parse_args()
source=Path(__file__).resolve().parent
out=args.output
out.mkdir(parents=True,exist_ok=False)
protocol=json.loads((source/'protocol.json').read_text(encoding='utf-8'))
for name,key in [('cases.json','casesSha256'),('system.txt','systemSha256')]:
 if hashlib.sha256((source/name).read_bytes()).hexdigest()!=protocol[key]:
  raise ValueError('Frozen source hash mismatch: '+name)
cases=json.loads((source/'cases.json').read_text(encoding='utf-8'))
system=(source/'system.txt').read_text(encoding='utf-8')
schema=json.loads((source/'schema.json').read_text(encoding='utf-8'))
base=args.base
adapter=args.adapter
tok=AutoTokenizer.from_pretrained(base,local_files_only=True)
model=AutoModelForCausalLM.from_pretrained(base,local_files_only=True,torch_dtype=torch.float16,attn_implementation='eager').to('cuda').eval()
for condition in ['base','direct-adapter']:
 if condition=='direct-adapter': model=PeftModel.from_pretrained(model,adapter,local_files_only=True,is_trainable=False).eval()
 results=[]
 for case in cases:
  prompt=tok.apply_chat_template([{'role':'system','content':system},{'role':'user','content':json.dumps(case['input'],separators=(',',':'))}],tokenize=False,add_generation_prompt=True,enable_thinking=False)
  inputs=tok(prompt,return_tensors='pt',add_special_tokens=False).to('cuda'); start=time.monotonic()
  with torch.inference_mode(): generated=model.generate(**inputs,max_new_tokens=128,do_sample=False,pad_token_id=tok.eos_token_id)
  raw=tok.decode(generated[0,inputs.input_ids.shape[1]:],skip_special_tokens=True)
  try:
   parsed=json.loads(raw); valid=isinstance(parsed,dict) and set(parsed)=={'pattern','evidenceIds'} and parsed['pattern'] in schema['properties']['pattern']['enum'] and isinstance(parsed['evidenceIds'],list) and all(isinstance(x,str) for x in parsed['evidenceIds']) and len(set(parsed['evidenceIds']))==len(parsed['evidenceIds'])
   correct=valid and parsed['pattern']==case['expected']['pattern'] and set(parsed['evidenceIds'])==set(case['expected']['evidenceIds'])
  except (ValueError,TypeError,KeyError): valid=False; correct=False
  row={'id':case['id'],'output':raw,'schemaValid':valid,'correct':correct,'seconds':time.monotonic()-start,'promptSha256':hashlib.sha256(prompt.encode()).hexdigest()};results.append(row)
  (out/(condition+'.json')).write_text(json.dumps({'condition':condition,'results':results,'fieldQualified':False,'finalQuantizedModel':False},indent=2),encoding='utf-8')
  print(condition,case['id'],correct,raw,flush=True)
 print('TOTAL',condition,sum(x['correct'] for x in results),'/',len(results),flush=True)
print('REPORT',out,flush=True)
