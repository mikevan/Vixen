# Vixen

People facing surveillance and disruption need ways to recognize danger, preserve evidence, and make informed decisions. The wireless technologies around us can offer insight into risks that would otherwise remain unseen.

Vixen puts that awareness in people's hands. Our aim is to help people understand a developing threat while there is still time to act, even when internet access is unavailable.

## How we pursue that purpose

Hunter captures observations; Vixen preserves and analyzes the evidence; T-Ultra provides an independent record for comparison. Procedural triage identifies interesting changes and preserves the relevant time slices. The intended AI workflow interprets those changes against a growing library of attack signatures, with findings traceable to supporting evidence and uncertainty made visible.

A signature match does not establish who operates a device or what they intend. Useful warning requires both sound evidence and a response early enough to matter. We evaluate those requirements through repeatable scenarios and the same ordinary application workflow used in the field.

## What Vixen provides

Vixen is the flagship Android analysis product in the LayerTime ecosystem. It brings Hunter connectivity, evidence preservation, analysis tools, and experimental offline AI into a portable phone application. Notella is the separate training technology supporting the research.

**Current limits:** the AI remains experimental. Reliable end-to-end outage/drone warning through the ordinary field workflow has not yet been demonstrated. This repository describes an MVP under validation, not a qualified protection system. See [current AI status](docs/ai-development/CURRENT-AI-STATUS.md) and [the MVP objectives](docs/mvp-okrs.md).

## Build and contribute

Formerly LayerScope. Android installation IDs and Kotlin package names are preserved for update compatibility.

Android project: `android/`. Product AI workflow and tracked teaching/evaluation assets: `tools/ai-training/`. Notella remains a separate training product. Native inference is pinned by `third_party/llama.cpp.lock.json`; restore it with `tools/ai/fetch-runtime.ps1`. Wireshark native libraries must be rebuilt/staged using the Android build tooling.

Clone with `git clone --recurse-submodules https://github.com/mikevan/Vixen.git`, or run `git submodule update --init --recursive` in an existing checkout. The protocol reference is the pinned `contracts/` submodule in this repository. Android builds and tests verify its Git pin and clean source; unit tests read this checkout's vectors and invalidate their cache when those vectors change. No sibling Contracts folder or environment override is required. Hunter firmware and watch firmware are independent products. This source extraction is not a device-verified release. Local models, signing keys, private captures, old APK releases and build caches were not copied.

Source ancestry is recorded in `EXTRACTION.json`; original Git history remains in LayerTime. EXTRACTION.json records the initial local preparation; subsequent checkpoints are recorded in Git history.

GitHub Actions builds the diagnostic APK and unsigned Play APK/AAB from source, runs both unit-test variants, and retains results for 14 days. See [Play candidate preparation](docs/play-candidate.md) for device testing, signing and submission requirements.

MVP objectives, KPI definitions, passive instrumentation and non-interference requirements: [MVP OKRs](docs/mvp-okrs.md).
