"""Score published OKRs from passive metrics and independent, post-run labels.
Never supplies labels to the app/model. Missing truth stays unmeasured.
"""
import argparse,json,math,statistics
from pathlib import Path

def ratio(values):
    known=[v for v in values if type(v) is bool]
    return dict(value=sum(known)/len(known) if known else None,numerator=sum(known),denominator=len(known),status='measured' if known else 'not_measured')
def distribution(values,unit):
    values=sorted(v for v in values if type(v) in (int,float) and math.isfinite(v) and v>=0)
    return dict(median=statistics.median(values) if values else None,p95=values[math.ceil(.95*len(values))-1] if values else None,samples=len(values),unit=unit,status='measured' if values else 'not_measured')
def duration(case,start,end):
    a=case.get(start);b=case.get(end)
    if type(a) is not int or type(b) is not int or b<a or not case.get('clockId'):return None
    return (b-a)/1000000

def unique(values,identity):
    found={}
    for value in values:
        key=identity(value)
        if key in found and found[key]!=value:raise ValueError('Conflicting duplicate identity: '+str(key))
        found[key]=value
    return list(found.values())

def metric_identity(m):
    if not isinstance(m.get('runId'),str) or not m['runId'] or type(m.get('sequence')) is not int or m['sequence']<=0:
        raise ValueError('Metrics require a stable runId and positive sequence')
    return m['runId'],m['sequence']

def summarize(metrics,labels):
    if labels.get('schema')!='vixen-kpi-labels/1':raise ValueError('Wrong independent-label schema')
    metrics=unique(metrics,metric_identity)
    cases=unique(labels['cases'],lambda c:c['caseId'])
    for c in cases:
        if not c.get('reviewer') or not c.get('evidenceRef'):raise ValueError('Review attribution and evidence reference required')
        for key in ['expectedWarning','warningObserved','classificationCorrect','evidenceIntegrity','explanationGrounded','offlineRun','runSuccessful','recoveredWithoutLossOrDuplicates','onBattery','enduranceWindowComplete']:
            if key in c and c[key] is not None and type(c[key]) is not bool:raise ValueError('Label must be boolean or null: '+key)
    periods={}
    for period in sorted({c.get('period','all') for c in cases} or {'all'}):
        cs=[c for c in cases if c.get('period','all')==period]
        delivered=[False if c.get('warningObserved') is False else c.get('classificationCorrect') if c.get('warningObserved') is True else None for c in cs if c.get('expectedWarning') is True]
        unsupported=[True if c.get('expectedWarning') is False or c.get('classificationCorrect') is False else False if c.get('expectedWarning') is True and c.get('classificationCorrect') is True else None for c in cs if c.get('warningObserved') is True]
        ai=[];invalid_links=0
        for c in cs:
            if c.get('aiAttemptId'):
                ends=[m for m in metrics if m.get('kind')=='ai_attempt_finished' and m.get('eventKey')==c['aiAttemptId'] and m.get('fields',{}).get('status')=='completed']
                if len(ends)==1 and ends[0].get('clockId')==c.get('clockId'):
                    v=duration(dict(c,aiValidatedNs=ends[0]['monotonicNs']),'evidenceReceivedNs','aiValidatedNs')
                    if v is not None and c.get('explanationGrounded') is True:ai.append(v)
                    else:invalid_links+=1
                else:invalid_links+=1
        periods[period]={
          'O1.KR1.correct_warning_delivery_rate':ratio(delivered),
          'O1.KR2.unsupported_warning_fraction':ratio(unsupported),
          'O1.KR3.warning_latency':distribution([duration(c,'evidenceReadyNs','warningVisibleNs') for c in cs if c.get('warningObserved') is True and c.get('classificationCorrect') is True and c.get('expectedWarning') is True],'ms'),
          'O2.KR1.evidence_integrity_rate':ratio([c.get('evidenceIntegrity') for c in cs]),
          'O2.KR2.grounded_explanation_rate':ratio([c.get('explanationGrounded') for c in cs]),
          'O2.KR3.analysis_turnaround':distribution(ai,'ms'),
          'O3.KR1.offline_success_rate':ratio([c.get('runSuccessful') for c in cs if c.get('offlineRun') is True]),
          'O3.KR2.recovery_integrity_rate':ratio([c.get('recoveredWithoutLossOrDuplicates') for c in cs]),
          'O3.KR3.field_endurance':distribution([c.get('batteryHours') for c in cs if c.get('onBattery') is True and c.get('enduranceWindowComplete') is True and c.get('workloadId')],'hours'),
          'quality':{'labeledCases':len(cs),'invalidOrUnlinkedAiTimings':invalid_links}}
        workloads=sorted({c['workloadId'] for c in cs if c.get('onBattery') is True and c.get('enduranceWindowComplete') is True and c.get('workloadId')})
        endurance={w:distribution([c.get('batteryHours') for c in cs if c.get('onBattery') is True and c.get('enduranceWindowComplete') is True and c.get('workloadId')==w],'hours') for w in workloads}
        if len(workloads)>1:periods[period]['O3.KR3.field_endurance']=dict(distribution([],'hours'),byWorkload=endurance,reason='Do not combine unlike workloads')
        else:periods[period]['O3.KR3.field_endurance']['byWorkload']=endurance
    return {'schema':'vixen-okr-report/1','periods':periods,'telemetry':{'records':len(metrics),
      'notificationSubmissions':sum(m.get('kind')=='warning_submitted' for m in metrics),
      'aiUnavailable':sum(m.get('kind')=='ai_attempt_finished' and m.get('fields',{}).get('status')=='unavailable' for m in metrics)},
      'limits':['SDK notification submission is not proof of display or acknowledgement.',
      'Completed caller validation is not semantic truth. Accuracy requires independent review.',
      'Clock IDs must match; source and phone clocks are never silently aligned.',
      'Missing labels and missing durations remain null; report metrics drop/health sidecars alongside trends.',
      'Observer overhead counters measure enqueue/writer time, not a proof of zero effect.']}

if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--metrics',type=Path,nargs='+',required=True);p.add_argument('--labels',type=Path,required=True);p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    if a.output.exists():p.error('Choose a new report path; preserve earlier reports')
    records=[]
    for source in a.metrics:
        for line in source.read_text(encoding='utf-8-sig').splitlines():
            if line.strip():records.append(json.loads(line))
    report=summarize(records,json.loads(a.labels.read_text(encoding='utf-8-sig')))
    a.output.write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8');print(a.output)
