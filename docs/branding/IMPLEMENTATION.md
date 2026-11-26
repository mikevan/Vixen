# Vixen Android icon implementation candidate

Local preparation requested November 15, 2026. Not deployed or approved as final artwork.

The reference is the established PRS Vixen fox wordmark copied unchanged here.
The native vector is an angular interpretation of its fox head and amber/green
palette, not a raster crop or a new Scope/aperture mark. Original artwork and
old resources remain available for comparison.

- Launcher foreground delegates to the shared `vixen_mark` drawable.
- Round/adaptive launchers reuse that mark; API 33 adds a monochrome variant.
- FaceView rasterizes the same vector once for the existing Canvas dial.
- Notifications use a distinct white alpha silhouette, avoiding the colored
  adaptive-icon background in Android's status bar.
- The existing Android launch theme uses the launcher identity; no app package,
  collection, inference or detector identity is renamed.

Open `vixen-icon-review.html` for the reference and mask previews. Device and
small-size visual acceptance remain pending. No phone installation or GitHub
write is part of this preparation. The legacy package identifier remains
`com.projectrevivesolutions.layerscope.diag` to retain the existing data.

Local build 151 assembled successfully and both release-variant unit suites passed. This checks compilation and behavior regressions; it does not establish visual approval on a phone.

The FullDebug candidate APK SHA-256 is `7af4663afc323eec1bc5858fa06a1df970e12fb85c5853b025d2246da53c9a00`. The field APK guard passed: no built-in test interfaces or acceptance-answer assets. Six icon XML resources and local preview/document links passed structural checks. Other existing build-output APKs were not relabeled as this candidate.

## On-device verification for build 152

On November 25, 2026, build 151 still showed LayerScope on the Garmin-style watch face and in the Ops connection strip. Diagnostic build 152 shows VIXEN in both places on the test phone. The Android launcher is labeled Vixen, and the native packet decoder uses Vixen as its report identity. The in-place update uses the existing diagnostic application ID and preserves the build 151 AI assets. The FullDebug build passed 434 unit tests.

The installed APK SHA-256 is `6335c245d7192516c6aca132743f68a89d5db972c8d266c8b8b9d2bae7ef7ecc`.
