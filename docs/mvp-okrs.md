# Vixen MVP objectives and key results

People need warning they can use, evidence they can verify, and tools that remain dependable without internet access. These objectives measure whether Vixen helps people understand developing danger and make informed decisions. Activity counts and successful builds do not establish that outcome.

The first beta must demonstrate Vixen's ordinary field workflow. Trends are computed from preserved observations and independent post-run review, with the same released APK and model used for both controlled tests and field operation.

## Objective 1: Give operators useful warning of a developing event

- **Key Result 1:** Increasing rate of correct warnings delivered through the ordinary interface.
- **Key Result 2:** Decreasing rate of unsupported warnings.
- **Key Result 3:** Decreasing time from sufficient evidence to displayed warning.

## Objective 2: Give operators intelligence they can trust and verify

- **Key Result 1:** Increasing evidence integrity between source records and preserved slices.
- **Key Result 2:** Increasing rate of explanations accurately grounded in the evidence.
- **Key Result 3:** Decreasing time from evidence receipt to validated explanation.

## Objective 3: Remain dependable throughout disconnected field use

- **Key Result 1:** Increasing rate of successful offline operation.
- **Key Result 2:** Increasing recovery rate without lost state or duplicate warnings.
- **Key Result 3:** Increasing operating time per battery charge under a defined workload.

## Measurement definitions

Each KR needs a recorded baseline, target and measurement period. Targets remain unset until agreed; this implementation does not invent thresholds or claim a field pass.

| KR | Definition | Required independent evidence |
|---|---|---|
| O1.KR1 | Correct, observed warnings / labeled opportunities that require warning | Expected outcome, observed warning and classification review |
| O1.KR2 | Unsupported warnings / observed warnings with reviewed ground truth | Independent expected outcome; also report label coverage |
| O1.KR3 | Median and p95 of displayed-warning time minus evidence-ready time | Operator/video/OS observation with an explicit common phone clock |
| O2.KR1 | Reconciled evidence sets / reviewed evidence sets | Source/phone sequence and payload comparison, including gaps and retained slice bounds |
| O2.KR2 | Fully grounded explanations / independently reviewed explanations | Reviewer checks observations, roles, order, timing, uncertainty and unsupported claims |
| O2.KR3 | Median and p95 of caller-validated AI completion minus evidence receipt | Linked AI attempt ID, receipt timestamp and matching clock ID |
| O3.KR1 | Successful disconnected runs / reviewed disconnected runs | Defined completion criteria and observed connectivity state |
| O3.KR2 | Recoveries without lost state or duplicate warnings / reviewed interruptions | Before/after records and observed warning behavior |
| O3.KR3 | Median and p95 operating hours per completed battery window, grouped by workload | Battery-only operation, workload ID and complete endurance window |

A notification accepted by Android is a **submission**, not proof that it was displayed or read. A completed caller validation is not proof of semantic truth. A source's silence is not proof of an outage. Unknown truth, incomplete clocks and absent labels remain **not measured**, never zero failures or 100% success. Charging runs do not establish battery endurance. Short controlled cases do not establish population accuracy.

## Non-interference contract

Recording must not influence the act being recorded. Instrumentation cannot supply observations or model inputs, choose a model, change decisions, affect deduplication, initiate retries, send commands, generate warnings or alter their routing. Business logic never reads KPI state or recording success.

The caller submits a bounded record without waiting for disk. A separate low-priority worker serializes and writes it. A full queue drops metrics rather than applying backpressure. Storage errors and storage limits stop recording without propagating into application behavior. No new service, wake lock, RF scan or network probe is created for measurement. A passive resource sample runs at most once per minute while the application process remains alive.

Zero timing or power impact is not claimed. Memory allocation, queue submission, background writes and passive system queries have physical costs. Record enqueue time, writer elapsed time, dropped/failed records and queue state. Establish an acceptable overhead budget before treating timing measurements as representative. Logger failure/saturation and outcome-invariance tests are necessary checks, not universal proof of physical non-interference.

## Idempotency contract

Every raw record has a stable `(runId, sequence)` identity. Re-importing overlapping exports or identical review labels yields the same report. Conflicting duplicates fail explicitly. Separate real notification submissions retain separate identities, so duplicate warnings cannot be hidden by deduplication. Source packet receipt counts are diagnostic observations; they are not automatically extra scenario opportunities or successful warnings.

Instrumentation-on/off validation must preserve model inputs, model call count, return values, admission decisions, alert selection and retry behavior. The same frozen release is subsequently used for field and controlled-source tests. No harness-only screen, alternate analysis path or qualification bypass is part of the field acceptance criterion.

## Recorded facts and storage

The normal app records process version/clock identity, received event summaries, connection transitions, transport gaps, preserved evidence counts/hashes, procedural triage results, notification submission outcomes, AI attempts and their outcomes, and battery/network/memory samples. AI-unavailable, timeout, cancellation and invalid-result states remain visible. No captured identifiers or model content are copied into KPI fields; the normal private evidence record retains the underlying evidence.

Private `files/kpi-metrics/kpi-<run>.jsonl` contains append-only records. A matching health JSON records offered/written/dropped/failed counts and recorder overhead. Defaults: 512 queued records, 16 MiB per process log and 128 MiB total logs. Existing records are never automatically deleted. Storage exhaustion is an incomplete measurement run, not a successful field run. `clockId` identifies this installation, boot and Android elapsed-realtime clock. Source clocks require an explicit mapping; wall-clock timestamps are for presentation, not silent latency alignment.

Use the existing **EXPORT FIELD RECORD** after a run to export the KPI prefix and health record alongside field evidence. Export itself performs IO, so do not use it as an invisible observer during timing-sensitive tests. Private records are not automatically uploaded or admitted to model training.

## Post-run scoring

Ground truth stays outside Vixen and the model. Start from `tools/metrics/labels.example.json`. Add unique `caseId`, `reviewer`, `evidenceRef`, and `period` for each reviewed opportunity. Use boolean/null labels: `expectedWarning`, `warningObserved`, `classificationCorrect`, `evidenceIntegrity`, `explanationGrounded`, `offlineRun`, `runSuccessful`, and `recoveredWithoutLossOrDuplicates`.

For timing, provide matching `clockId`, `evidenceReadyNs` and `warningVisibleNs`; link `aiAttemptId` and `evidenceReceivedNs` to the recorded completed AI attempt. For endurance, provide `onBattery`, `enduranceWindowComplete`, `batteryHours` and `workloadId`. Leave unverified labels null or absent.

```sh
python tools/metrics/summarize.py --metrics private/kpi-run.jsonl --labels private/review.json --output private/okr-period-01.json
```

Retain the metric health sidecars with every report. Review periods and workload groups consistently. Partial export tails, missing telemetry, unlinked attempts and unreviewed warnings must be reported as coverage limitations. The scoring tool rejects conflicting identities and never treats a submitted notification or syntactically valid model answer as independently verified correctness.

## Verification included

Regression tests cover repeated import idempotency, conflicting identities, real duplicate submissions, missing ground truth, unmapped clocks, charging-run exclusion, queue saturation, disk failure, preservation of caller data, and identical AI calls/inputs/outputs when its metrics writer fails. Wider timing, battery and field non-interference remain measured validation work rather than assumed guarantees.
