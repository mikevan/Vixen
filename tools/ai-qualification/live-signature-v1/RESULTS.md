# Live signature interpretation: first comparison

This is a development comparison for the normal offline analysis path. It is not a field demonstration or a qualification certificate. No phone application or dongle scenario was changed by this comparison.

The intended MVP correlates preserved observations quickly enough to support a timely decision. Procedural capture and triage select interesting intervals; a model interprets only their supported evidence. A deauthentication request does not prove an outage, EAPOL does not prove compromise, and Remote ID does not establish an aircraft's presence or intent. This narrower sequence must not be presented as proof of the Wi-Fi/BLE outage evacuation policy.

## Frozen reference and results

Twelve authored cases and their system prompt were frozen before predictions; `protocol.json` records their hashes and decoding settings. They are development references, not independently reviewed field truth. They were not used for optimizer updates in this run.

| Candidate | Exact JSON + pattern + evidence IDs | Result |
| --- | --- | --- |
| Existing Qwen3 0.6B base | 0/12 | Every answer included Markdown fences; several were also semantically wrong. |
| Existing direct semantic adapter | 4/12 | Three positive sequences and the empty input passed. Missing, reversed, late, unaligned, and unrelated-network evidence exposed failures. |

`base.json` and `direct-adapter.json` preserve every answer and elapsed host inference time. Adapter SHA-256: `c7399ebba35040a6b350eafa1802d02b90e2c4ae2715acad37f3f9fa81354c99`.

For example, with deauth and EAPOL but no Remote ID, the adapter answered `disconnect_sequence_then_remote_id`, citing only the two existing events. It also selected that pattern for Remote ID preceding the exchange and for evidence beyond the configured interval. These are semantic failures, not formatting issues. No answer was repaired or counted as successful merely because a guard could reject it.

The host comparison used Transformers FP16, eager attention, greedy decoding, no thinking, and 128 output tokens on an RTX 2070. This is neither the final quantized artifact nor the Android inference path. Host duration does not establish phone latency. No production model gate was changed, no positive-only demonstration was accepted, and no broad model qualification is claimed.

## Reproduce

Use the existing training environment with PyTorch, Transformers and PEFT. Run `evaluate.py --base <local-base-directory> --adapter <local-adapter-directory> --output <new-results-directory>`. It refuses an existing output directory and checks the frozen source hashes. The historical output files came from the same prediction loop before its paths were converted to command-line arguments. Future changes to prompts or scoring require a separately identified run; preserve these failures.

## Remaining MVP work

Wire genuine Remote ID evidence through the ordinary shared protocol, capture and persistence path; integrate automatic background analysis and the normal result display; qualify the final model and phone request path, including missing evidence and timing controls; then freeze the field build before the operator starts the scenario. The current physical harness still runs its previous deauth/AirTag fixture. This record is not an instruction to begin the test.
