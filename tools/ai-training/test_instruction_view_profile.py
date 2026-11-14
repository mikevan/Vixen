import unittest,tempfile,json,hashlib
from pathlib import Path
from instruction_view_profile import prepare
from train_lora import lora_targets
from domain_training_profile import prompt_hash
class InstructionViewProfileTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.temp=tempfile.TemporaryDirectory();cls.addClassCleanup(cls.temp.cleanup)
        cls.root=Path(__file__).parent;cls.out=Path(cls.temp.name)/'views'
        cls.policy=prepare(cls.root,cls.out)
        cls.parent={r['id']:r for r in map(json.loads,(cls.root/'domain-data/admitted-development-v2/train.jsonl').read_text().splitlines())}
        cls.rows=list(map(json.loads,(cls.out/'train.jsonl').read_text().splitlines()))
    def test_variants_do_not_change_facts_or_targets_or_count_as_independent_evidence(self):
        self.assertEqual(2*len(self.parent),len(self.rows))
        for row in self.rows:
            parent=self.parent[row['instructionView']['parentId']]
            self.assertEqual(parent['messages'][1:],row['messages'][1:])
            self.assertFalse(row['instructionView']['independentEvidence'])
        self.assertEqual(len(self.parent),self.policy['originalTrainingRecords'])
    def test_held_out_data_and_exclusion_list_remain_byte_identical(self):
        parent=self.root/'domain-data/admitted-development-v2'
        for name in ['validation.jsonl','test.jsonl','acceptance-exclusions.json']:
            self.assertEqual((parent/name).read_bytes(),(self.out/name).read_bytes())
        blocked=set(json.loads((self.out/'acceptance-exclusions.json').read_text())['promptSha256'])
        self.assertFalse(blocked.intersection(prompt_hash(r) for r in self.rows))
    def test_adapter_scope_does_not_train_embeddings_or_output_head(self):
        self.assertEqual(['q_proj','v_proj'],lora_targets('qv'))
        self.assertEqual(7,len(set(lora_targets('all'))))
        self.assertNotIn('embed_tokens',lora_targets('all'));self.assertNotIn('lm_head',lora_targets('all'))
        with self.assertRaises(ValueError):lora_targets('unknown')
if __name__=='__main__':unittest.main()
