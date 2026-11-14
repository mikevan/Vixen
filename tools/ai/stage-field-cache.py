"""Stage a debug-only manifest for an already installed, unqualified phone model.

Never copies weights or changes qualification. Frozen acceptance data is copied
verbatim for explicit diagnostic evaluation, never admitted to training.
"""
import argparse
import hashlib
import json
from pathlib import Path
import shutil


def main():
    root=Path(__file__).resolve().parents[2]
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--manifest",type=Path,default=root/"tools/ai-training/acceptance/v2/trained-domain-v1/training/phone-package-manifest.json")
    p.add_argument("--examples",type=Path,default=root/"tools/ai-training/acceptance/v2/examples.jsonl")
    p.add_argument("--rubrics",type=Path,default=root/"tools/ai-training/acceptance/v2/rubrics.jsonl")
    p.add_argument("--license",type=Path,required=True,help="Existing retained license for this model's pinned base")
    p.add_argument("--output",type=Path,default=root/"android/app/src/debug/assets/ai/development")
    a=p.parse_args()
    manifest=json.loads(a.manifest.read_text(encoding="utf-8-sig"))
    if manifest.get("releaseEligible") is not False or manifest.get("developmentOnly") is not True:
        p.error("Only an explicitly unqualified development package can use this helper")
    inputs=[(a.examples,"acceptance.jsonl","acceptanceExamplesSha256"),
            (a.rubrics,"acceptance-rubrics.jsonl","acceptanceRubricsSha256")]
    for source,_,key in inputs:
        if hashlib.sha256(source.read_bytes()).hexdigest()!=manifest[key]:
            p.error("Frozen evaluation input differs from the package receipt")
    if not a.license.is_file():p.error("Model base license is missing")
    a.output.mkdir(parents=True,exist_ok=True)
    if list(a.output.glob("*.gguf")):p.error("This cache-only package must not contain duplicate weights")
    existing=a.output/"package.json"
    if existing.exists():
        old=json.loads(existing.read_text(encoding="utf-8-sig"))
        if (old.get("modelId"),old.get("modelVersion"),old.get("sha256")) != (manifest["modelId"],manifest["modelVersion"],manifest["sha256"]):
            p.error("Preserve the existing package; choose another output directory")
    manifest["weightsLocation"]="deviceCache"
    existing.write_text(json.dumps(manifest,indent=2)+"\n",encoding="utf-8")
    for source,name,_ in inputs:shutil.copyfile(source,a.output/name)
    shutil.copyfile(a.license,a.output/"Qwen-LICENSE")
    print("Staged unqualified debug metadata/evaluation fixtures; no weights copied. The matching phone cache is required.")


if __name__=="__main__":main()
