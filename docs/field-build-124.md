# Field development build 124

Hunter and Vixen are the operating pair; T-Ultra independently verifies common
observations. This build prepares an auditable field experiment. It does not
qualify the existing language model, establish RF agreement, or certify Play
readiness.

## What changed

- Vixen saves the individual Hunter summaries and subsequent text before UI
  grouping, including producer address, session, event ID, count/age saturation,
  connection changes and change-stream gaps. Prior journal runs are not replaced.
- The FIELD RECORD panel shows saved/pending/unsaved record counts and recording
  problems, the latest evidence checks and loss of the live feed. EXPORT FIELD
  RECORD copies a complete written prefix plus its loss/queue metadata.
- Live evidence checks refer to the exact input record. They are the existing
  deterministic event triage, not language-model assessments. Receipt-to-check
  time can be measured without pretending that it includes RF/transport delay.
- Persistence sorting requires fresh, increasing reports for the same Hunter
  record, on a monotonic receipt clock. Replayed/saturated counts, different
  event IDs and wall-clock changes cannot manufacture the observation span.
  The wording no longer treats received reports as proof of physical tracking.
- Pulled raw evidence retains Hunter address/session/event and a digest of
  length-delimited frame payloads. Changing a synthetic PCAP time anchor does
  not change that digest. Another device/session's capture is rejected for
  event-linked triage. Files have unique names and storage failures are shown.
- Decoding and workflow analysis write provenance to the journal. Manual,
  training and Hunter-sourced recordings remain distinguishable.

The journal uses an asynchronous bounded queue and stops appending after a
write error or storage limit. Its default limit is 256 MiB per process run and
1 GiB across retained runs. Omissions are counted and shown. Export before
clearing application data. File close is not a proof of power-loss durability.

## Experimental model

The debug FIELD RECORD panel can explain its latest individual Hunter snapshot
with the explicit EXPERIMENTAL AI action. It freezes the source/session/event,
input facts, and journal reference before generation; changing live input does
not change the running request. Run is manual, Stop cancels, and the model has
a 60-second generation limit. The exact request and raw response are journaled.
A valid response schema does not establish factual correctness. These outputs
require human review and cannot change alerts or checked facts. They are not
approved training examples.

The current first 4B candidate is unchanged and still passed only 2/12 frozen
interpretation cases. Ordinary assessments continue to reject it. The retained
model can be exercised only through explicit debug evaluation.

A private field package may use `weightsLocation: deviceCache` to avoid copying
its 2.5 GB weights into the APK again. This mode is rejected in production
packages. A missing cache produces an unavailable/incompatible package, and the
cached file must still pass the recorded SHA-256 check before loading. A fresh
phone needs the separately prepared model; staging metadata does not install it.

Use `python tools/ai/stage-field-cache.py --license <retained-model-license>`
to copy hash-bound evaluation fixtures and the unqualified manifest. The staged
folder is ignored by Git and normal hosted CI. No model, private capture or
field journal is uploaded by this helper. It neither retrains nor promotes a
candidate. The app retains its existing installation ID so a matching-signed
update can preserve the phone's model cache and evidence.

## Bench sequence

1. Preserve existing diagnostics and card recordings. Reset cards only for the
   intended devices with owner authorization. Stop the run before any reset.
2. Install the matched app/firmware and record their hashes. Keep the watch's
   previous firmware available for restoration. Do not uninstall Vixen or clear
   application data to work around a signing problem.
3. Confirm the named Hunter, its session, SD status and selected detectors. On
   T-Ultra enable Settings → SD Logging and select common detector/radio coverage.
4. Record which time interval belongs to this test and the three producer/run
   identities. Card resets do not synchronize device clocks or receivers.
5. Observe the FIELD RECORD panel during passive monitoring. Pull a known Wi-Fi
   evidence window, inspect its recorded facts and export the journal. A manual
   window pull is not a continuous raw-packet stream.
6. Compare Hunter's log/PCAP files and T-Ultra's `/recon_field_v1.csv` using the
   [field comparison tool](https://github.com/mikevan/LayerTime/tree/main/tools/field).
   Establish an actual clock offset/common interval before interpreting timed
   independent-receiver differences. Keep real reports private.
7. Record RF misses, transport gaps, SD failures, stale inputs, sustained phone
   latency/memory/heat, cancellation and reconnect behaviour. None of these
   device criteria is established merely by host tests or successful builds.

See [the ecosystem field contract](https://github.com/mikevan/LayerTime/blob/main/docs/firmware/field-evidence-contract.md)
and CURRENT-AI-STATUS.md for the unchanged model limitations.
