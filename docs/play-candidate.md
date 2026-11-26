# Vixen Play candidate

The purpose of distribution is to make local evidence analysis accessible on phones people already have. Reaching an app store makes the tools easier to obtain; it does not qualify the model or establish a dependable warning system.

Status: build preparation, not an approved or published Play release.

GitHub Actions builds the existing `playRelease` variant as an APK and Android
App Bundle, alongside a diagnostic `fullDebug` APK and unit tests. The runner
rebuilds Wireshark and the pinned inference runtime from source. It has no
release signing key and does not upload to Play Console. The Play outputs are
unsigned development candidates; the debug APK is installable for initial
checks. No personal models or training-data LFS files are fetched into the app.

Download outputs under Artifacts in a successful Vixen CI run. `pinned` denotes
the product's checked-in dependencies; artifacts labelled with a library name
are integration experiments. Retention is 14 days, so retain the chosen release
candidate and its exact source/pins in durable release storage before expiry.

## Before internal Play testing

- [ ] Record successful CI run, exact product/dependency/runtime commits, AAB
      SHA-256, native build versions and test results.
- [ ] Run the [hardware acceptance checklist](https://github.com/mikevan/LayerTime/blob/main/docs/firmware/hardware-acceptance.md)
      on the intended phone/watch hardware. Test BLE permission denial, reconnect,
      background services, card transfers, capture import, native decoding and
      no-model/local-model behaviour. Record failures and NOT RUN cases.
- [ ] Test installed APKs generated from the bundle on both 4 KB and 16 KB-page
      supported devices. Native ELF alignment alone does not prove bundle
      packaging/installation. Check the JNI/inference libraries as well as the
      Wireshark dependencies. [Android guidance](https://developer.android.com/guide/practices/page-sizes).
- [ ] Verify the final Play merged manifest excludes `LayerTimeKeyService`,
      retains only needed permissions/services, and uses the intended launcher
      name/icon. The checked-in Play manifest removes the accessibility service;
      inspect the final bundle rather than relying only on source intent.
- [ ] Confirm `com.projectrevivesolutions.layerscope` is the intended existing
      application ID and identify its Play listing/signing history. Vixen is the
      product name; changing this ID would create a different application.
- [ ] Choose a versionCode higher than every previously uploaded version and
      reconcile the user-facing versionName. Current source is versionCode 122
      and versionName 0.1.121; it has not been confirmed against Play Console.
- [ ] Confirm the account's current target-API and testing requirements in Play
      Console. Source currently targets API 36; this is not a certification of
      store eligibility. [App setup requirements](https://support.google.com/googleplay/android-developer/answer/9859152).
- [ ] Inventory actual data handling, storage, backups, optional model import,
      capture export and third-party code. Prepare an accurate public privacy
      policy and Data safety answers; do not claim local-only or no-collection
      solely from the product description. Review permission explanations and
      foreground-service declarations against the implemented flows.
- [ ] Review bundled native/software/model licenses, notices and required source
      availability with the release owner. The CI build is not a license review.
- [ ] Prepare listing text/screenshots, hardware requirements, content rating,
      contact/support information and the privacy-policy URL.
- [ ] Use the existing approved upload key and Play App Signing setup for a
      signed `bundlePlayRelease` build in a trusted release environment. Keep
      keys/passwords out of source, artifacts and untrusted pull-request jobs.
      [Play App Signing](https://support.google.com/googleplay/android-developer/answer/9842756).
- [ ] Upload the signed bundle to internal testing, inspect Play validation and
      pre-launch reports, test the Play-delivered installation, then decide
      whether it is ready for wider testing. Publishing requires a separate
      release decision; this workflow never submits anything automatically.

## Build locally

Initialize submodules, restore the pinned inference runtime using
`tools/ai/fetch-runtime.ps1`, and stage Wireshark native dependencies with
`android/wireshark-spike/build_wireshark_arm64.sh` on Linux. Follow the pinned
SDK/NDK/tool versions in `.github/workflows/ci.yml`. Then from `android/`:

```sh
bash gradlew :app:assembleFullDebug :app:testFullDebugUnitTest \
  :app:testPlayReleaseUnitTest :app:assemblePlayRelease :app:bundlePlayRelease
```

With no `keystore.properties`, release outputs are unsigned. An unsigned APK
cannot be installed normally, and an unsigned AAB is not upload-ready.
