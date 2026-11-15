import json,subprocess,time,hashlib
from pathlib import Path
root=Path(__file__).resolve().parent
runner='/mnt/c/workspace/TUltra-Project/LayerTime/tools/ai-training/runtime/llama-host/bin/llama-completion'
model=root.parent/'weights-q8.gguf'
cases=json.loads((root/'cases.json').read_text());system=(root/'system.txt').read_text()
report=root/'quantized-results.json'
if report.exists():raise ValueError('Preserve existing results')
rows=[]
for case in cases:
 prompt='<|im_start|>system\n'+system+'<|im_end|>\n<|im_start|>user\n'+json.dumps(case['input'],sort_keys=True,separators=(',',':'))+'<|im_end|>\n<|im_start|>assistant\n<think>\n\n</think>\n\n'
 file=root/(case['id']+'.prompt');file.write_bytes(prompt.encode())
 start=time.monotonic();result=subprocess.run([runner,'-m',str(model),'-f',str(file),'--grammar-file',str(root/'signature.gbnf'),'-n','64','-c','2048','-t','4','-b','512','-ub','512','--temp','0','--no-display-prompt','--simple-io','-no-cnv'],capture_output=True,timeout=90)
 raw=result.stdout.decode('utf-8');(root/(case['id']+'.stdout')).write_bytes(result.stdout);(root/(case['id']+'.stderr')).write_bytes(result.stderr)
 # The completion executable appends this literal terminal marker after EOS;
 # it is not a model token or a JSON repair. Keep the full stdout separately.
 text=raw.removesuffix(' [end of text]\n\n\n').strip()
 try:correct=result.returncode==0 and json.loads(text)==case['expected']
 except ValueError:correct=False
 rows.append(dict(id=case['id'],output=text,correct=correct,seconds=time.monotonic()-start,promptSha256=hashlib.sha256(prompt.encode()).hexdigest(),exitCode=result.returncode))
 report.write_text(json.dumps(dict(modelSha256=json.loads((root/'frozen.json').read_text())['modelSha256'],total=len(cases),completed=len(rows),correct=sum(r['correct'] for r in rows),results=rows,phoneQualified=False),indent=2))
 print(case['id'],correct,round(rows[-1]['seconds'],2),text,flush=True)
print('TOTAL',sum(r['correct'] for r in rows),'/',len(cases),flush=True)
