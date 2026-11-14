# Saved field collection AI evaluation

People must be able to challenge an AI explanation against the evidence it used. This evaluation compares local model answers with preserved records and retains errors for review, keeping the grounds for each finding open to inspection.

The debug instrumentation can evaluate private saved Hunter observations with
the actual cached on-device model. It does not promote a model, retrain weights,
change detector verdicts, or authorize tools. The normal qualification gate and
frozen interpretation suite remain unchanged.

## Reproduced failures and corrections

The original field explanation request omitted its full output schema from the
system message. The installed compact prompt format requires that schema, so the
request failed before inference. FieldShadowTasks now supplies it explicitly;
a regression test renders the request through that exact compact format.

With only that compatibility correction, real field responses confused detector
classification with device identification, called typed reported counts untrusted,
and frequently omitted the supplied count and confidence. The corrected request
anchors explanations to a sentence constructed from the typed report values,
requires those values, and distinguishes captured names and identifiers from
collector-reported classification. A later on-device pass still omitted the count and confidence and took 67
seconds for its first response. That pass was intentionally stopped after two
completed cases. Build 126 uses a shorter, example-guided request with a 256-token
budget. Before displaying an explanation, it checks required detector, count and
confidence values, the reproduced untrusted-value wording, and identifier-matching
claims. Failure preserves raw output but withholds the explanation. These are
narrow report checks, not a general semantic validator. Model prose still requires
semantic review.

A verbose decoded-packet request exceeded its 90-second evaluation deadline.
FieldPacketBrief supplies validated frame counts, loss and source hash without
duplicating decoder paragraphs and frame-index lists. Its generation budget is
256 tokens. Invalid count combinations are rejected before a model request.
The full decoder report and source PCAP must remain preserved separately.

The first bounded packet answer still invented a saved count (66 instead of the
verified 64). Build 127 requires the exact verified packet-count sentence and
rejects numbers absent from the supplied counts. Its decoder rejects changed
count roles as well as new numbers, while preserving the raw model response for
review. This is deliberately conservative and is not a general prose validator.
Only the affected packet case needs rerunning when this packet-only request changes.

## Reproduce privately

Prepare `cache/field-collection-evaluation.json` in the debug app with a `cases`
array. Each case includes its run/receipt identity, Hunter address, session/event,
receipt time, immutable report facts, and a unique case ID. A decoded_capture case
additionally contains the source PCAP SHA-256 and verified kept/seen/dropped,
deauthentication, EAPOL and malformed-frame counts. Do not commit field inputs.

Run BuilderSmokeInstrumentation with `fieldAiEvaluation` set to a unique report
label. The runner refuses to overwrite `files/diagnostics/<label>.jsonl`, checks
that ordinary AiCoordinator calls still reject the unqualified candidate, and
saves each result as it completes. Each row contains the input hash, exact messages,
output schema and token budget, model/weight identity, response or error, and timing.

Review actual prose against source records and native packet decoding. Format
validity is not factual correctness. Numeric case IDs are not unique physical
devices; a single reported count cannot prove persistence. Packet requests cannot
prove successful disconnection. No EAPOL in a partial window cannot prove safety
or that no reconnect happened. Keep before/after outputs and manual review notes.

Build 127 passed 392 unit tests on both full/debug and Play/release variants.
The private nine-case field rerun is separate from general model qualification.

## Reviewed field rerun

The final reviewed set covered eight unique saved event records and one decoded
packet window. All eight event summaries retained their supplied detector, count
and confidence, attributed classification to Hunter, and did not assert ownership,
physical tracking or malicious intent. The corrected packet answer retained all
verified count roles and the incomplete-coverage/no-EAPOL limitations. This result
is specific to these private inputs and does not replace general qualification.

Observed total runtime was about 44-51 seconds per event summary and 82 seconds
for the standalone packet answer. It is not a demonstrated real-time AI workflow.
The weights were not retrained or promoted. The existing 2/12 frozen interpretation
qualification result and ordinary model gate remain unchanged. Private before/after
answers, manual review and integrity manifests are retained outside Git.
