# How Vixen uses and trains AI

Vixen aims to help people recognize developing danger through evidence they can preserve and inspect locally. AI is one part of that workflow; capture, exact comparisons, evidence identity, and warning continuity have separate procedural responsibilities.

Read [the current status](CURRENT-AI-STATUS.md) for implementation limits and [the iteration history](iteration-history.md) for successes, mistakes, and decisions. To reproduce the training foundations, begin with [Notella Tutorial 0: environment setup](https://github.com/mikevan/Notella/blob/main/docs/setup.md), then use [the tutorial index](https://github.com/mikevan/Notella/blob/main/docs/index.md).

## Capture first, interpret selected evidence

Hunter records observations and sends evidence to Vixen. T-Ultra is intended to provide an independent record for comparison. Procedural triage maintains a rolling capture window and preserves interesting slices with available packet attachments. The ten-second window describes capture context, not a requirement to run a model every ten seconds.

The intended AI task is to interpret relevant slices against a growing, versioned attack-signature corpus. Partial matches and missing follow-on observations must remain explicit. Device identity, clock alignment, duplicates, packet availability, and exact timing comparisons cannot be inferred from a signature's name.

The application computes supported deterministic policy conditions; the model can explain the checked finding and its evidence. A hypothesis does not establish that an event happened, and a match does not establish who operates a device or what they intend. The ordinary workflow remains subject to its model qualification gate.

## Teach and evaluate separately

Notella provides the reusable ontology, curriculum, training-plan, job, and review tools. We adapt an existing local model using reviewed examples, preserve source and dataset provenance, and compare exact generated answers with the evidence. Optional teacher proposals need explicit review before admission. [Notella's AI workflow](https://github.com/mikevan/Notella/blob/main/docs/how-we-use-ai.md) explains that process; [environment setup](https://github.com/mikevan/Notella/blob/main/docs/setup.md) is the entry point for its tutorials.

Training completion, finite weights, valid JSON, correct references, and a readable explanation are separate checks. None alone establishes factual correctness. We retain wrong and incomplete outputs, keep inspected evaluation answers out of training, and distinguish authored development fixtures from independently reviewed field evidence.

## Prove the ordinary field path

The first target is a preserved local Wi-Fi/BLE outage sequence followed by a drone-signature observation, producing an evidence-grounded warning under an explicitly configured policy. These observations and policy remain unqualified; fresh RF detection and normal warning delivery have not been demonstrated end to end.

Controlled tests must use the same released application and ordinary processing route as field operation. Pre-existing instrumentation may measure it without supplying a separate route to success. The [MVP OKRs](../mvp-okrs.md) define measurements and non-interference requirements.

## Current decision

The current model remains experimental and excluded from ordinary product assessments. Controlled preservation, correlation, and phone-explanation results are component evidence. They do not resolve [SIG-001](model-defects/SIG-001.json), qualify a general model, or establish dependable field warning. The [current status](CURRENT-AI-STATUS.md) records the gaps; the [iteration history](iteration-history.md) explains how we reached that decision.
