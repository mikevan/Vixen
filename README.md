# Vixen

Flagship Android analysis product, formerly LayerScope. Android installation IDs and Kotlin package names are preserved for update compatibility.

Android project: `android/`. Product AI workflow and tracked teaching/evaluation assets: `tools/ai-training/`. Notella remains a separate training product. Native inference is pinned by `third_party/llama.cpp.lock.json`; restore it with `tools/ai/fetch-runtime.ps1`. Wireshark native libraries must be rebuilt/staged using the Android build tooling.

Clone with `git clone --recurse-submodules https://github.com/mikevan/Vixen.git`, or run `git submodule update --init --recursive` in an existing checkout. The protocol reference is the pinned `contracts/` submodule in this repository. Android builds and tests verify its Git pin and clean source; unit tests read this checkout's vectors and invalidate their cache when those vectors change. No sibling Contracts folder or environment override is required. Hunter firmware and watch firmware are independent products. This source extraction is not a device-verified release. Local models, signing keys, private captures, old APK releases and build caches were not copied.

Source ancestry is recorded in `EXTRACTION.json`; original Git history remains in LayerTime. EXTRACTION.json records the initial local preparation; subsequent checkpoints are recorded in Git history.

GitHub Actions builds the diagnostic APK and unsigned Play APK/AAB from source, runs both unit-test variants, and retains results for 14 days. See [Play candidate preparation](docs/play-candidate.md) for device testing, signing and submission requirements.
