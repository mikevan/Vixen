# Ordinary application field-test contract

The field application and the application under test are the same artifact. A scenario changes Hunter-TestHarness and its quarantined source evidence, not Vixen's screens, model instructions, qualification gates, or decision path. Freeze the APK/model/configuration before a comparison and retain their identities with the results.

## Application cleanup

Build 139 removes the built-in trained-AI practice screen, manual experimental field explanation, synthetic Builder practice loaders, canned deauth checks, link measurement controls, screen-off reception test, and the exported Wireshark diagnostic activity. The previous LIVE HUNTER TEST interface is also absent. These controls are removed from compiled application sources rather than hidden behind a debug switch.

Historical implementations are retained as noncompiled reference text under `tools/reference/test-only-ui`. Frozen model acceptance examples and rubrics, with their loader, belong to `src/androidTest`, not application assets. The separate Android test APK is a developer tool; it is not installed for a field test. Component tests may still exercise research code explicitly, but those results must not be counted as the ordinary field workflow.

## Retained product behavior

Normal Hunter connection, capture/import, analytic authoring, evidence status/export, diagnostics export, and passive KPI recording remain available. Existing observations, collections, model caches, and settings are not deleted by the update. Previous synthetic evidence is preserved with its provenance rather than silently relabelled as field evidence.

The model qualification gate remains unchanged. Removing a manual development-model route does not qualify a model or activate automatic analysis. Hunter-TestHarness's experimental telemetry still needs integration with the ordinary evidence route before it can demonstrate end-to-end warning. This cleanup makes no new MVP success claim.

## Verification

Run both application unit-test variants and build the ordinary APKs. `python tools/check_field_apk.py <app.apk>` rejects the removed interface symbols, control labels, diagnostic activity, and frozen answer assets in compiled APKs. CI checks both full/debug and play/release packages. The guard does not prove semantic accuracy or that all future alternate routes are impossible; code review and end-to-end evidence remain necessary.

Update the installed application in place, preserve its evidence/settings, remove only the separately identified test package, and verify the normal menus and saved evidence afterward. Do not start a scenario, call a model, change Hunter settings, or inject observations merely to verify this cleanup.

## Build 139 verification

Both full/debug and play/release passed 420 unit tests with no failures, errors, or skips. Both application APKs built and passed the packaging guard; the previous APK failed that guard on the removed interfaces and answer assets. The separate test APK compiled with its own acceptance examples and was not installed for the field setup.

The connected phone was updated in place from build 136 to build 139. Its prior saved-data paths and model-cache paths remained present, and saved evidence content was retained. AndroidX profile-installer metadata changed with the APK update, as expected. The normal Builder More/Load menus and field-record display were checked without triggering a scenario, generating an AI explanation, or changing Hunter settings. The old separate test package was removed. Private backup and verification artifacts remain outside this repository.
