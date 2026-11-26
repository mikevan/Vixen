import unittest
from summarize import summarize
class SummarizeTest(unittest.TestCase):
 def case(self,**kw):return dict(caseId='a',reviewer='reviewer',evidenceRef='sha256:source',**kw)
 def report(self,metrics=[],cases=[]):return summarize(metrics,{'schema':'vixen-kpi-labels/1','cases':cases})['periods']['all']
 def test_missing_truth_is_not_a_perfect_score(self):
  r=self.report();self.assertIsNone(r['O1.KR1.correct_warning_delivery_rate']['value']);self.assertIsNone(r['O2.KR2.grounded_explanation_rate']['value'])
 def test_notification_submission_does_not_mean_warning_was_seen(self):
  r=self.report([{'runId':'r','sequence':1,'kind':'warning_submitted'}],[self.case(expectedWarning=True)])
  self.assertIsNone(r['O1.KR1.correct_warning_delivery_rate']['value'])
 def test_truth_and_same_clock_drive_rates_and_timing(self):
  r=self.report([], [self.case(expectedWarning=True,warningObserved=True,classificationCorrect=True,clockId='c',evidenceReadyNs=1000000,warningVisibleNs=4000000)])
  self.assertEqual(1,r['O1.KR1.correct_warning_delivery_rate']['value']);self.assertEqual(3,r['O1.KR3.warning_latency']['median'])
 def test_unmapped_clock_and_completed_json_do_not_prove_truth(self):
  metrics=[{'runId':'r','sequence':1,'kind':'ai_attempt_finished','eventKey':'ai','clockId':'other','monotonicNs':4000000,'fields':{'status':'completed'}}]
  r=self.report(metrics,[self.case(aiAttemptId='ai',clockId='c',evidenceReceivedNs=0)])
  self.assertEqual(0,r['O2.KR3.analysis_turnaround']['samples']);self.assertIsNone(r['O2.KR2.grounded_explanation_rate']['value'])
 def test_failed_offline_and_charging_runs_cannot_pass(self):
  r=self.report([], [self.case(offlineRun=True,runSuccessful=False,onBattery=False,enduranceWindowComplete=True,batteryHours=5,workloadId='w')])
  self.assertEqual(0,r['O3.KR1.offline_success_rate']['value']);self.assertEqual(0,r['O3.KR3.field_endurance']['samples'])
 def test_reimporting_identical_data_is_idempotent(self):
  m={'runId':'r','sequence':1,'kind':'warning_submitted'};c=self.case(expectedWarning=True,warningObserved=True,classificationCorrect=True)
  self.assertEqual(summarize([m],{'schema':'vixen-kpi-labels/1','cases':[c]}),summarize([m,m],{'schema':'vixen-kpi-labels/1','cases':[c,c]}))
 def test_conflicting_reimports_fail_instead_of_changing_scores(self):
  m={'runId':'r','sequence':1,'kind':'warning_submitted'}
  with self.assertRaises(ValueError):self.report([m,dict(m,kind='different')])
 def test_actual_duplicate_submissions_are_not_hidden(self):
  a={'runId':'r','sequence':1,'kind':'warning_submitted'};b=dict(a,sequence=2)
  result=summarize([a,b],{'schema':'vixen-kpi-labels/1','cases':[]})
  self.assertEqual(2,result['telemetry']['notificationSubmissions'])
 def test_missing_warning_is_failure_even_without_a_classification_label(self):
  r=self.report([], [self.case(expectedWarning=True,warningObserved=False)])
  self.assertEqual(0,r['O1.KR1.correct_warning_delivery_rate']['value'])
 def test_wrong_warning_cannot_improve_warning_latency(self):
  r=self.report([], [self.case(expectedWarning=True,warningObserved=True,classificationCorrect=False,clockId='c',evidenceReadyNs=0,warningVisibleNs=1000000)])
  self.assertEqual(1,r['O1.KR2.unsupported_warning_fraction']['value']);self.assertEqual(0,r['O1.KR3.warning_latency']['samples'])
if __name__=='__main__':unittest.main()
