"""Existing admitted facts/targets with original generic and relationship instructions; no new evidence."""
import copy,hashlib,json,shutil
from pathlib import Path
from collections import Counter
from grounded_contract import canonical
from domain_training_profile import verify,prompt_hash

def prepare(root,out):
    parent=root/'domain-data/admitted-development-v2';original=root/'domain-data/admitted-development-v1'
    verify(parent);verify(original)
    if out.exists():raise ValueError('Preserve prior view corpus')
    original_rows={r['id']:r for r in [json.loads(l) for l in (original/'train.jsonl').read_text().splitlines()]}
    parent_rows=[json.loads(l) for l in (parent/'train.jsonl').read_text().splitlines()]
    values=[]
    for source in parent_rows:
        for view in ('relationship','generic'):
            row=copy.deepcopy(source);row['id']+='-instruction-'+view
            row['datasetVersion']='blue-team-compact-instruction-views-v3'
            row['instructionView']={'parentId':source['id'],'parentCanonicalSha256':hashlib.sha256(canonical(source).encode()).hexdigest(),
                'view':view,'factsAndTargetChanged':False,'independentEvidence':False}
            if view=='generic':
                prior=original_rows[source['repair']['parentId']]
                row['messages'][0]=copy.deepcopy(prior['messages'][0])
                row['instructionView']['instructionSourceId']=prior['id']
                row['instructionView']['instructionSha256']=hashlib.sha256(prior['messages'][0]['content'].encode()).hexdigest()
            assert row['messages'][1:]==source['messages'][1:]
            values.append(row)
    out.mkdir(parents=True)
    shutil.copy2(parent/'acceptance-exclusions.json',out/'acceptance-exclusions.json')
    manifest=json.loads((parent/'manifest.json').read_text())
    manifest['profile']='blue-team-domain-development-v3-instruction-views'
    manifest['parentManifestSha256']=hashlib.sha256((parent/'manifest.json').read_bytes()).hexdigest()
    manifest['instructionSourceManifestSha256']=hashlib.sha256((original/'manifest.json').read_bytes()).hexdigest()
    manifest['instructionViewPolicy']={'originalTrainingRecords':len(parent_rows),'preparedTrainingViews':len(values),
        'scope':'Two existing instruction forms for the same admitted facts and targets; no new independent examples.',
        'acceptanceUsed':False,'fieldEvidenceUsed':False,'sourceSha256':hashlib.sha256(Path(__file__).read_bytes()).hexdigest()}
    (out/'train.jsonl').write_text(''.join(canonical(r)+'\n' for r in values),encoding='utf-8')
    manifest['splits']['train'].update(sha256=hashlib.sha256((out/'train.jsonl').read_bytes()).hexdigest(),examples=len(values))
    for split in ('validation','test'):shutil.copy2(parent/(split+'.jsonl'),out/(split+'.jsonl'))
    (out/'manifest.json').write_text(json.dumps(manifest,indent=2)+'\n')
    shutil.copy2(Path(__file__),out/'instruction-view-source.py');verify(out);return manifest['instructionViewPolicy']
if __name__=='__main__':
    import argparse
    p=argparse.ArgumentParser();p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    print(json.dumps(prepare(Path(__file__).parent,a.output),indent=2))
