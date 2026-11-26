# Vixen development history: builds 1–150

This is the evidence trail for how the Android tool was created: what changed, what was tried, what failed, and what was actually checked. It brings the Android Controller, LayerScope and flagship Vixen records into one chronology without turning gaps into a story we cannot prove.

Compiled on **November 15, 2026**, through Vixen build 150. This is a development-build index, **not a claim of 150 public releases, 150 retained APKs, or 150 successful field tests**. The GitHub releases listing contained no published Vixen releases at the original builds-1–141 audit; it was not re-audited for this extension. Installation IDs, version names, build numbers and source checkpoints are different pieces of evidence.

## How to read the record

- Every number from 1 through 150 has its own entry. Missing individual notes are explicitly marked. A numbered entry alone does not prove that its APK was built or distributed.
- Original build notes are quoted as historical records. Later source comments and commit messages are identified separately. A grouped checkpoint does not establish which feature changed in each intermediate build.
- “Built,” “installed,” “host-tested,” “phone-tested” and “operator-run” retain their original scopes. A developer's reported diagnosis is a historical account, not an independently established cause. Later reversals are retained.
- Exact version names are listed only where recovered from a version declaration. The old 0.1.x name did not always equal versionCode; the beta naming convention changed later. No missing name or release date has been guessed.
- Sources link to immutable Git commits/files. The source ledger records their Git-blob or commit-message SHA-256. Selected later bench records remain private and are identified by basename and hash; their absence from public Git is disclosed.
- Private field journals, signing material, device serials and physical device addresses are not reproduced. Synthetic fixture addresses may appear in original notes. Historical relative links are resolved to the source checkpoint where possible; unavailable references are labelled.
- The Android controller became LayerScope and was later renamed the flagship Vixen. The ESP32-C5 fox-hunt device historically called Vixen is a different product. Hunter firmware build 93/94 references do **not** fill Android history gaps 93/94. Watch-firmware versions are also excluded.

## The development arc supported by the records

| Period | What the records establish | Limits and reversals |
| --- | --- | --- |
| Initial controller / early decoder work | BLE observations, device diagnostics, logging/export and explicit unavailable fields; Wireshark reached the phone in build 13. | The initial README is a build-10 snapshot; early individual notes are incomplete. Build 12 failed native loading; an initialization timing claim was invalidated. |
| Builds 14–53 | Shared native decoder checks, stability measurement, foreground import, watch/phone presentation, accessibility controls, alerts and evidence explanations. | Small heap fluctuations were not proof of leaks; a sliding-panel design was rejected; some device stalls remained unexplained. |
| Builds 54–92 | Connection feedback, LayerScope identity, responsive navigation, grouped observations, ordinary sensor commands, packet windows and field card analysis. | Several changes were committed as explicit groups; a 59–62 visual experiment was reverted. Temporal association was not proof of successful attack. |
| Builds 100–122 | Tracker defaults, connection-recovery attempts, Builder/reporting, evidence correlation, local model contracts, training and phone runtime work. | Connection changes in 102–108 were reverted in 109. Training completion and faster probes did not qualify the broad model: both domain candidates passed only 2/12 frozen acceptance cases. |
| Builds 123–139 | Durable field observations, bounded explanations, capture slices, portable runtime experiments and stricter field-test fidelity. | Invented events, wrong numerical comparisons, invalid JSON and test-only paths were retained as failures or component results. The test interfaces were removed. |
| Builds 140–141 | A narrower offline signature task was qualified against authored controls and connected to the ordinary field workflow. The operator obtained a correct live report; an EAPOL preservation gap was then fixed in code. | The real run passed bounded interpretation, not complete evidence parity. The initial 141 receipt required a repaired baseline rerun; that rerun and T1 later passed, as recorded below. Synthetic source inputs do not prove actual RF detection, outage, hostile intent, evacuation effectiveness or independent T-Ultra corroboration. |
| Builds 142–149 | Timestamped report history, archive/export, separate AI Reports navigation, plain wording and iterative frame design were deployed. Test 2 exposed a candidate-selection failure on 149. | The failed 149 run preserved the records and packets but never invoked AI. Flat/inset Back designs were revised; build 148 has no separate source commit. |
| Build 150 | Complete same-network pair selection fixed the failure before inference; regression checks and the operator T2 rerun passed their stated criteria. | The report still has no action recommendation. The complete actionable MVP report is not accepted; the model weights were not changed by this repair. |

The 140 scores belong to a **new, narrower output/task contract and model**. They must not be presented as a directly comparable improvement from the old broad 4B prose benchmark. The new authored controls are not independent field incidents.

## Coverage and navigation

All **150 build numbers** are indexed. No individual note was recovered for these **18 numbers**: 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 93, 94, 95, 96, 97, 98, 99, 121. Other entries explicitly identify partial, grouped, inline-comment or local-only evidence rather than claiming a complete release note.

[1–20](#build-1) · [21–40](#build-21) · [41–60](#build-41) · [61–80](#build-61) · [81–100](#build-81) · [101–120](#build-101) · [121–150](#build-121) · [Grouped original checkpoints](#grouped-original-checkpoints) · [Source ledger](#source-ledger) · [Local audit records](#local-audit-records)

## Build-by-build record

### Build 1

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 2

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 3

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 4

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 5

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 6

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 7

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 8

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 9

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 10

**Individual recorded note** — [S35].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The README explicitly identifies versionCode 10 / versionName 0.1.9. It records the BLE controller, device report, detection logging/export and screen-off foundation; COUNTER/RECON navigation, mission checkboxes and six-section event detail were compiled but not yet device-verified for build 10. Missing raw-frame fields were labelled UNAVAILABLE rather than populated with demonstration values.

### Build 11

**Gap — no individual change note recovered.** The retained build-10 README establishes the early controller phase, but it does not allocate changes to this number. See [S35]. No feature, test result or installation claim is manufactured for this entry.

### Build 12

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 12 failure and fix (for the record)
>
> Build 12 failed to load: `dlopen failed: library "libintl.so" not found: needed by
> libglib-2.0.so`. Android has no gettext, so GLib built its bundled proxy-libintl stub
> (12 KB, needs only libc), which was not packaged. Fix: package libintl.so, and add
> `check_native_closure.sh`, which fails the build unless every DT_NEEDED and every strong
> undefined symbol resolves against the packaged set plus the API-34 platform libraries.

### Build 13

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Measurements on the TCL (build 13)
>
> - APK on device 49,413 KB; native libraries installed 16 files, 129,089 KB
>   (libwireshark.so 123,331 KB); installed total 178,502 KB. Build-host APK 50,589,602 bytes
>   (build 12; build 13 not recorded).
> - System.loadLibrary (ltws + 14 dependencies): 156.9 ms.
> - Decode: first call 12.7 ms; warm median 6.3 ms (min 5.0, max 9.6, 20 runs).
> - Native heap 47,428 KB -> 47,429 KB across 21 decodes. Process total PSS 263,422 KB
>   (native PSS 28,591 KB); no pre-load baseline, so Wireshark's share was not separable.
> - **Not valid:** the reported "epan_init: 0.1 ms". Registering ~1,700 dissectors cannot
>   take 0.1 ms, and the fields decoded, so initialization had already run. The timer sat in
>   Kotlin around a call that only initializes on first use. Working hypothesis (unverified):
>   the report ran twice in one process (for example after unlock), and the second run showed
>   an init that had already happened. Build 14 tests this directly.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> **Verified on the TCL K24 (2026-10-11): on-device Wireshark 4.4.19 decode**
>
> **Build:** 0.1.12-diag, versionCode 13, arm64-v8a. **Device:** TCL T434D, Android 14
> (API 34), ABIs arm64-v8a, armeabi-v7a, armeabi. Source: photos of the diagnostic screen.
>
> **Decoder:** libwireshark/epan **4.4.19**, cross-compiled with NDK r27c (target API 28),
> loaded through JNI. No substitute decoder.
>
> **Field comparison with same-version TShark (`tshark_4.4.19_decode.txt`)**
>
> | Field | TCL (epan 4.4.19, arm64) | TShark 4.4.19 (host) |
> |---|---|---|
> | length | 26 bytes | 26 |
> | wlan.fc.type_subtype | 0x000c | 0x000c |
> | wlan.da | ff:ff:ff:ff:ff:ff | ff:ff:ff:ff:ff:ff |
> | wlan.sa | 00:11:22:33:aa:bb | 00:11:22:33:aa:bb |
> | wlan.bssid | 00:11:22:33:aa:bb | 00:11:22:33:aa:bb |
> | wlan.seq | 7 | 7 |
> | wlan.fixed.reason_code | 0x0007 | 0x0007 |
>
> All six fields match.
>
> **Measurements on the TCL (build 13)**
>
> - APK on device 49,413 KB; native libraries installed 16 files, 129,089 KB
>   (libwireshark.so 123,331 KB); installed total 178,502 KB. Build-host APK 50,589,602 bytes
>   (build 12; build 13 not recorded).
> - System.loadLibrary (ltws + 14 dependencies): 156.9 ms.
> - Decode: first call 12.7 ms; warm median 6.3 ms (min 5.0, max 9.6, 20 runs).
> - Native heap 47,428 KB -> 47,429 KB across 21 decodes. Process total PSS 263,422 KB
>   (native PSS 28,591 KB); no pre-load baseline, so Wireshark's share was not separable.
> - **Not valid:** the reported "epan_init: 0.1 ms". Registering ~1,700 dissectors cannot
>   take 0.1 ms, and the fields decoded, so initialization had already run. The timer sat in
>   Kotlin around a call that only initializes on first use. Working hypothesis (unverified):
>   the report ran twice in one process (for example after unlock), and the second run showed
>   an init that had already happened. Build 14 tests this directly.
>
> **Build 12 failure and fix (for the record)**
>
> Build 12 failed to load: `dlopen failed: library "libintl.so" not found: needed by
> libglib-2.0.so`. Android has no gettext, so GLib built its bundled proxy-libintl stub
> (12 KB, needs only libc), which was not packaged. Fix: package libintl.so, and add
> `check_native_closure.sh`, which fails the build unless every DT_NEEDED and every strong
> undefined symbol resolves against the packaged set plus the API-34 platform libraries.

### Build 14

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 14 changes (decoder core and measurement integrity)
>
> - Decode path moved into plain C (`app/src/main/cpp/ws_core.c`), with a thin JNI layer
>   (`ws_jni.c`), so the host harness runs the exact code the APK runs.
> - epan initialization timed inside native code (CLOCK_MONOTONIC); `epan_init`'s return
>   value is checked; a later call reports `performed_now = 0` with the first call's timing
>   and never re-runs initialization.
> - Missing `frame_data_destroy` added after each decode (per-frame list and table).
>   `epan_dissect_free` already releases the tvb chain (verified in epan/epan.c 4.4.19).
> - Reports carry pid, report number, and activity instance; memory sampled before load.
>
> **Host test of ws_core.c (x86_64, Wireshark 4.4.19), `host_core_test_100k.log`**
>
> - Init: first call performed init (epan_init 86.7 ms, OK); second call did not re-run it.
> - Fields: all six match TShark 4.4.19.
> - 100,000 decodes: 0 output mismatches; RSS 141,160 KB at 10,000 and at 100,000 decodes;
>   malloc heap in use 16,229 -> 16,231 KB. No measurable growth. Mean 468 us per decode.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Verified on the TCL K24 (2026-10-11): build 14 (0.1.13-diag, versionCode 14)
>
> Full log: `tcl_build14_run.log` (UTF-16). Build-host APK 50,640,091 bytes; on device 49,453 KB;
> native libraries 16 files, 129,091 KB; installed total 178,544 KB.
>
> **Run A: fresh process (pid 24877, report #1, activity instance #1)**
> - System.loadLibrary (ltws + 14 dependencies): 162.9 ms.
> - Native-timed initialization: **epan_init 864.8 ms**, returned true; total init 877.0 ms.
> - Fields: 6 of 6 match TShark 4.4.19. Decode: first 7.5 ms; warm median 5.98 ms (min 5.40, max 6.98).
> - Memory (total PSS / native heap): before load 81,557 / 11,952 KB; after load 162,383 / 12,005 KB;
>   after init 220,636 / 41,800 KB; after 21 decodes 221,664 / 43,850 KB. Loading adds about 81 MB of
>   PSS as mapped library pages (native PSS +0.1 MB); epan_init adds about 58 MB PSS and 30 MB native heap.
>   Wireshark's total cost over the app baseline: about 140 MB PSS.
>
> **Run B: same process, new activity instance (pid 24877, report #2, activity instance #2)**
> - Initialization reported "already done earlier in this process; not re-run" (init call #2), with
>   report #1's timings. Load time likewise reported from report #1.
> - This confirms the mechanism behind the invalid build-13 figure (epan_init 0.1 ms): a second report in
>   one process shows an initialization that already happened. In the build-13 code the timed call was
>   the first that could initialize, so 0.1 ms is only possible if an earlier report in the same process
>   ran it. The build-13 run itself recorded no pid, so that specific run cannot be re-examined.
> - Fields 6 of 6. Native heap 45,418 -> 45,419 KB across 21 decodes.
>
> **Run C: fresh process, 20,000-decode stability test (pid 24975)**
> - epan_init 860.4 ms (true), total 862.1 ms; load 146.2 ms. Fields 6 of 6.
> - **0 mismatched outputs in 20,000 decodes.** Mean 7,548 us per decode in the loop (includes result
>   string creation and comparison; the 20-run warm median before the loop was 5,838 us).
> - Samples (decodes: native heap / native PSS / total PSS / RSS, KB):
>   2,000: 43,821 / 24,619 / 222,064 / 302,716; 4,000: 37,653 / 19,467 / 215,172 / 296,276;
>   then rising evenly to 20,000: 37,657 / 19,441 / 219,520 / 300,744.
> - Warm-up: between 2,000 and 4,000 decodes the native heap fell 6.2 MB (allocator returning memory).
>   The in-app "growth from sample 1" figure (-6,164 KB) is therefore misleading; it starts inside warm-up.
> - Steady state, 4,000 to 20,000 decodes: native heap +4 KB and native PSS flat: no native growth.
>   **Total PSS +4,348 KB and RSS +4,468 KB, linear, about 270 bytes per decode, not plateaued at 151 s.**
>   This growth is outside the native heap and is NOT yet attributed. It could be Java garbage awaiting
>   collection (each decode returns a new string) or retained memory; the report has no Java heap
>   breakdown and no forced collection, so the two cannot be told apart from this data.

### Build 15

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 15 (0.1.14): memory investigation, structured fields, PCAP import
>
> **Decode path moved to shared C; host results (x86_64, Wireshark 4.4.19)**
> `ws_core.c` now also provides `ltws_control` (same alloc path, no dissection),
> `ltws_decode_tree` (full protocol tree), and `ltws_decode_pcap`. Host harness
> `ws_core_test.c`, log `host_core_decode_100k.log`:
> - Structured tree: full Wireshark tree with byte offsets. The six key fields resolve at
>   their correct offsets: wlan.fc.type_subtype @0 len1, wlan.sa @10 len6, wlan.da @4 len6,
>   wlan.bssid @16 len6, wlan.fixed.reason_code @24 len2.
> - PCAP import: deauth_fixture.pcap -> 1 frame, type_subtype 0x000c, sa/da, reason 0x0007.
> - Decode loop, 100,000 iterations: 0 mismatches; RSS flat at 141,204 KB from 10k to 100k;
>   malloc in use flat at 16,238 KB. Mean 499 us.
> - Control loop, 100,000 iterations: 0 mismatches; RSS flat at 141,140 KB; heap flat at
>   16,188 KB. Mean 0.4 us.
> - On the host (no JVM), native heap and RSS do not grow for either loop over 100k
>   iterations. This bounds the decoder's native retention at or below the sampling
>   resolution; it does not by itself explain the Android PSS climb, which is measured
>   next with per-category memory on the phone.
>
> **What build 15 measures on the TCL**
> - Per-category memory each sample: Java heap, native heap, code, stack, graphics, other,
>   system, total PSS, plus native-heap-allocated and RSS.
> - A control mode (ltws_mode=control): same JNI byte[] in, String out, no dissection,
>   to separate decoder cost from JNI marshalling and result-string allocation.
> - A baseline taken AFTER warm-up (default 4000 iterations), so warm-up allocator return
>   is not mistaken for the trend.
> - Optional requested GC before each sample (ltws_gc=1): a diagnostic aid, not a guarantee.
>   Normal runs (ltws_gc=0) are preserved so forced collection does not hide production
>   behaviour.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> What build 15 measures on the TCL
>
> - Per-category memory each sample: Java heap, native heap, code, stack, graphics, other,
>   system, total PSS, plus native-heap-allocated and RSS.
> - A control mode (ltws_mode=control): same JNI byte[] in, String out, no dissection,
>   to separate decoder cost from JNI marshalling and result-string allocation.
> - A baseline taken AFTER warm-up (default 4000 iterations), so warm-up allocator return
>   is not mistaken for the trend.
> - Optional requested GC before each sample (ltws_gc=1): a diagnostic aid, not a guarantee.
>   Normal runs (ltws_gc=0) are preserved so forced collection does not hide production
>   behaviour.

### Build 16

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Run monitoring (from build 16 on)
>
> Long phone runs report liveness so "working" can be told from "stalled":
> - App: a `PHASE <name>` line at each phase change and a `HEARTBEAT` line at least every
>   5 s inside long loops (iteration, interval rate, elapsed, ETA), flagged `SLOWING` when
>   the interval rate drops below half the phase's first-interval rate. Shown live on screen.
> - PowerShell run blocks stream those lines as they arrive, warn after 25 s with no app
>   output while the process is alive (possible stall), detect a dead process and save the
>   crash buffer, and enforce a per-run time limit. Logs are written as UTF-8.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Run monitoring verified (build 16)
>
> PHASE lines at load, init, decode, tree, pcap, warmup, stability, and done. HEARTBEAT every
> 5.0 s through the 26 s warm-up and the 137 s stability phase, 143 to 159 iterations/s, no
> SLOWING flag, no stall warning, END reached; 171 s wall time for the whole block.

### Build 17

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Verdict wording (build 17)
>
> Builds 15 and 16 printed "native heap grew" for any increase, including +5 and +7 KB of
> noise. From build 17 the verdict reports growth only above a diagnostic threshold of
> 64 KB and 1 byte per iteration. This is a reporting threshold, not a leak test. The raw
> figures are always printed.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 17 (0.1.16): capture import and decoded fields on the event-detail screen
>
> - Native: `ltws_pcap_open / _next / _error / _file_type / _file_size / _close`, a
>   frame-by-frame reader so the app reports progress and can cancel between frames.
>   Each record set carries frame number, encapsulation, lengths, capture timestamp, file
>   offset, the frame bytes (first 4,096) as hex, and the full Wireshark tree. Tree records
>   gain a sixth column, Wireshark's label text for the node.
> - Native: every dissection now holds one lock, so the diagnostic screen and an import
>   never dissect at the same time (epan is not thread-safe).
> - Each frame is dissected on its own; reassembly and conversation state across frames
>   are not applied in this build.
> - App: section 7 of the event-detail screen, IMPORTED CAPTURE (WIRESHARK), and the same
>   section on its own via DECODE A CAPTURE FILE on the detected-attacks page (for use with
>   no detection open, which is also how it is tested without live attack traffic). The user picks
>   a file with the system picker; every frame is decoded and counted; the first 500 keep
>   their full tree for display. Each frame expands to every field with its byte offset and
>   the frame bytes. The screen states that the file is not linked to the event and that
>   decoding a frame is not a "countered" claim.
> - Progress: on screen (frames, share of file, rate, elapsed, slowing notice, cancel) and
>   in logcat tag LTWS_PCAP (PHASE, HEARTBEAT every 5 s, first five frames' key fields, and
>   a final PCAP DONE / CANCELLED / FAILED line).
> - Test files (synthetic, not captured traffic) from `make_test_captures.py`:
>   `lt_test_5frames.pcap` (deauth, disassociation, beacon SSID "LT-TEST" channel 6, probe
>   request, deauth cut to 20 bytes) and `lt_test_bulk_3000.pcap` (the same five repeated).
>   A unit test checks the five-frame file is byte-identical to what the app writes.
> - The diagnostic report also runs the frame-by-frame reader on the five-frame file and
>   checks frame 1 against the TShark 4.4.19 reference.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 17 on three devices (2026-10-11 and 2026-10-28)
>
> Logs: `tcl_build17_run.log`, `T513V_build17_stall_check.log`, `APPLLP5MAX_build17_run.log`.
> Decoder output on all three: 6 of 6 fixture fields match TShark 4.4.19; the frame-by-frame reader
> decodes all 5 test frames (deauth, disassociation, beacon with SSID, probe request, and the
> truncated deauth marked MALFORMED); 0 mismatched outputs in every loop.
>
> | device | chip | Android | RAM | decodes per second | warm decode median | epan_init |
> |---|---|---|---|---|---|---|
> | TCL K24 (T434D) | | 14 (API 34) | | about 150 | 6.2 ms | 858 to 1,172 ms |
> | TCL T513V | MediaTek MT6835, 8 cores | 15 (API 35) | 3.6 GB | about 700 (until frozen, see below) | | |
> | LOKMAT APPLLP 5 MAX (WP_C17S) | MediaTek MT6765 (Helio P35), 8 cores | 10 (API 29) | 6.0 GB | 200 to 260 | 3.8 ms | 534 ms |
>
> **TCL T513V: the process was frozen by Android (verified)**
> 20,000-decode run held about 700/s to 16,000, slowed to 499/s then 174/s, then went silent with the
> process alive. The stall check showed `isFrozen=true`, `cached=true`, process state 16, both worker
> threads parked in `do_freezer_trap` with 0 CPU over 5 s, and the process in the `background`
> cgroup. Waking the screen did not thaw it because the app was no longer the front activity.
> TCL's `tguard` was also killing cached apps in the same minute. Cause of leaving the foreground
> not established (screen timeout, Home, or the system). Fix in build 18: capture import runs under
> a foreground service; the diagnostic keeps the screen on and logs foreground changes.
>
> **LOKMAT APPLLP 5 MAX**
> - Memory: about 308 MB resident with Wireshark loaded, 5% of RAM. Storage 109 GB free.
> - Native heap allocated over 20,000 decodes: -101 to -135 KB from baseline through 6,000, a step
>   to +301 KB at 8,000, then +113 to +271 KB, ending +115 KB. The last 4,000 decodes added 2 KB.
>   Build 17's start-to-end verdict called this growth; build 18 adds the second-half trend so a
>   single allocator step is told apart from a steady climb. Not settled: a 100,000-decode run is
>   the next measurement.
> - The watch clock was at the firmware build date (June 13) after the factory reset; set by hand.
> - BLE scanning returned nothing until the system Location switch was turned on. Android 10
>   returns no BLE scan results with Location off, whatever permissions the app holds
>   (`WifiService: Location mode is disabled for the device` in the log). Build 18 checks the
>   switch before scanning and tells the operator.
> - Wi-Fi: MediaTek `gen4m` driver (`wlan_drv_gen4m.ko`, chip 0x6765). Monitor mode untested.
> - Screen 640 x 480 at density 160. Build 17 layout: the CLEAR button was cut off in the
>   detection row; everything else fit (screenshots in `screens_watch/`).

### Build 18

**Build-specific account inside a grouped note** — [S01].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

LOKMAT watch layout verified: full-height face at left, sliding away over a stationary panel; CLEAR remained visible. The shared 18–20 note also records foreground capture import, cancellation, and explicit location-state checks.

### Build 19

**Build-specific account inside a grouped note** — [S01].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The panel was made to slide with the face. This was rejected: the panel must remain fixed. The original grouped 18–20 note records the reversal.

### Build 20

**Build-specific account inside a grouped note** — [S01].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The panel became a fixed layer, with a true circular clipped face translating above it. Translation no longer remeasured the layout; corner taps could park the face.

### Build 21

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 21 (0.1.20): the steampunk rim
>
> - Left of the line bisecting the circle vertically the face layer is black; right of it the
>   face is transparent outside the circle, so the data panel shows through the right-hand corners.
> - The rim is a 20/454 brushed-steel band cut into 14 uneven plates by dark seams, two rivets per
>   seam, a mangled outer lip (dents up to 5/454 inward, never more than 2 px outward and never
>   past the face's own circle), hammer dents, scratches, and rust on alternate plates. All vector,
>   seeded with a fixed generator, so no bitmap memory and the same picture every frame.
> - Host preview of the algorithm (Pillow port of the same drawing): `rim_preview.png`.

### Build 22

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 22 (0.1.21): worn metal inside the face, panel text scale, larger lone buttons
>
> - Build 21 verified on the watch (`screens_watch_b21/`): rim, black left half, panel through
>   the right-hand corners, slide and sliver all as designed.
> - Inside the face every stroke keeps its width and gains a shadow stroke, a bright bevel
>   hairline, seeded nicks and highlights, and rivets (ring diagonals, octagon corners).
> - Panel text: the watch declares 160 dpi for both logical and physical density
>   (`dumpsys display`: "density 160 (160.0 x 160.0) dpi"), so the true pitch is unknown to
>   Android and a density-based fix is impossible. The panel scales its text from its own size
>   instead: min(height/340, width/420) clamped to 1.0 to 1.8 (watch 596 x 464 dp -> 1.36;
>   phone portrait -> 1.0), times a remembered A-/A+ multiplier (0.8 to 1.6) shown as
>   TEXT SIZE at the bottom of the main panel. Buttons and padding follow the scale.
> - Buttons that are a panel's only controls render at 44 dp x scale with 13 sp labels.
> - Run blocks from here on write their log as they go and print a status line at least every
>   15 s, including while waiting on the operator.

### Build 23

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 23 (0.1.22)
>
> - Build 22 verified on the watch (`screens_watch_b22/`): text scale 1.4x, large lone buttons,
>   worn strokes; the sub-pixel hairlines and 1.6/454 rivets on the octagons were too faint.
> - COUNTER, RECON, and the NO LAYERWAND bracket are now drawn as weld beads: overlapping
>   steel scallops ("stack of dimes") over a dark heat band, with a thin line of the element's
>   own colour underneath so the cyan and amber still read. Outlines 8/454 longer. No rivets
>   on welded parts.
> - Hairlines and nicks are at least one device pixel; nicks darker.
> - DETECTION EVENTS header 13 sp; TEXT SIZE A-/A+ moved directly under it so it stays
>   reachable when the log is shown.

### Build 24

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 24 (0.1.23)
>
> - The rim is drawn after everything else on the face, so COUNTER, RECON, and the bracket run
>   under the bezel (a crystal held by the rim, above the metrics); a faint glass highlight
>   arc sits just inside the bezel.
> - Capture import never started on the TCL K24 (build 17) or the LOKMAT watch (build 23):
>   the picker opened, no `PHASE copy` ever followed. Build 24 logs every step of that path
>   under tag LTWS_PCAP: IMPORT tapped, picker launch failure if any, PICKER RESULT with the
>   uri or "none", SERVICE start requested, SERVICE onStartCommand, IMPORT start requested.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 24 result: capture import verified on the LOKMAT watch (2026-10-28)
>
> Raw log `APPLLP5MAX_build24_rawlog.log`. The full chain ran: IMPORT tapped -> picker ->
> PICKER RESULT (Downloads document uri) -> PcapImportService -> PHASE copy/init/decode ->
> PCAP DONE. `lt_test_5frames.pcap`: 5 frames, 1 malformed, all key fields correct, 0.1 to 0.3 s.
> `lt_test_bulk_3000.pcap`: 3000 frames, 500 kept, 600 malformed, 64.0 s at a steady 47 frames/s
> (full-tree extraction per frame; the fixture-only diagnostic runs about 200/s on this chip).
> First completed capture import on any device. Not yet tested: an import with the app sent to
> the background mid-run.
> Test-script defect: `adb logcat -d -s TAG:I TAG:E ...` returned nothing on this watch although
> an unfiltered read shows the lines. All run blocks now read the log unfiltered and filter on
> the PC.

### Build 25

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 25 (0.1.24)
>
> - Slide sounds: synthesized pneumatic hiss sweeping down into a latch clank (slide out) and
>   sweeping up into a heavier double latch (slide back); res/raw WAVs, 22 kHz mono, 23 and
>   26 KB, played through SoundPool on the sonification usage. Generator parameters are in the
>   session notes; no third-party audio.
> - Grey text 0xFF8C8C8C -> 0xFFB4B4B4 app-wide.
> - Shorter on-watch text for the capture-import blurb, screen-off test, export, Location
>   message, and the per-frame note.

### Build 26

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 26 (0.1.25): the glowing console
>
> - Console restyled to contrast with the bronze bezel: flat dark blue-black surfaces, one-pixel
>   bright cyan edges with a smooth halo (four overlapping bands of falling alpha) and a
>   cyan-tinted drop shadow, 10 dp rounded corners, glowing section headings. Buttons flat with
>   green or cyan halos. Event rows amber-edged; frame cards green or red by state. Event-detail
>   sections and the capture screen use the same frame. All draw-only; no layout cost.

### Build 27

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 27 (0.1.26)
>
> Build 26 on the watch (`screens_watch_b26/`): edges visible, halo and tinted shadows too faint
> to register, card surface nearly the console colour. Build 27: halo bands 0.55 to 0.05 over
> five widths, an inner glow inside each edge, 1.5 dp edges, surfaces 0xFF13202C on 0xFF05090E,
> heading glow blur 12, and halos on buttons.

### Build 28

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 28 (0.1.27)
>
> Face content (battery arc, title, owl, rings, time, plates, bracket, date) laid out at 86% of
> the circle's diameter and centred, as on the Garmin tactix face, so the bezel frames it with
> air around it. The rim, the left-half mask, and the crystal sheen stay at full scale; the
> COUNTER and RECON tap rectangles scale with the content.

### Build 29

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 29 (0.1.28): face re-layout to the Garmin rhythm
>
> Build 28 (86% uniform scale) kept the old proportions: wide battery arc, small time below the
> owl, rings high and apart, wide plates and bracket. Build 29 re-lays the content (454 box):
> battery a 60-degree stub at the top; title 70; owl 78 at 120 with ear glyphs at 104; rings
> radius 52 at y 212, centres 103 and 351; time 66 at 200 and 262 between the rings; plates
> 118 x 46 at 272 centred under the rings; bracket 200 wide, 328 to 382; date 406.

### Build 30

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 30 (0.1.29)
>
> Build 29 on the watch matched the Garmin rhythm. Build 30: owl 90 at 122 with ear glyphs at
> 52 from centre, plates at 280, bracket 334 to 388, count text 19, date 410.

### Build 31

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 31 (0.1.30): Garmin ring content
>
> - Ring text as on the Garmin face: small glyph and label in the ring colour, large white
>   value, unit or AM/PM under it.
> - SUNRISE / SUNSET: the next event computed on the device from the last known location
>   (USNO Almanac for Computers method, zenith 90.833; unit-tested against Washington DC
>   2026-10-29, Quito at the equinox, and Svalbard polar night). No location -> "SUN --".
>   Phones on Android 12+ do not hold location permission, so they show "--".
> - TEMP: only from a real ambient temperature sensor (TYPE_AMBIENT_TEMPERATURE); with none,
>   N/A. Battery or CPU heat is never shown as air temperature. Fahrenheit for US locales.
> - Log tag LT_RINGS reports whether the sensor and a location fix exist.

### Build 32

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 32 (0.1.31)
>
> Ring stroke 7 -> 9.5, battery stub 9 -> 12, weld bead radius 1.7 -> 2.3 with a wider heat band
> and colour line; owl 90 -> 106 at 126, ear glyphs moved out to 66 from centre.

### Build 33

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 33 (0.1.32)
>
> Face: content box is the full circle and every element is placed so its outer edge sits at
> radius 201 of 227, just inside the bezel's inner edge (205): battery stub radius 193.5,
> rings centred 143 from the centre at y 222, plates at x 55 and 281 (y 290 to 336), bracket
> 278 wide with 31 slant (y 342 to 394). Owl 100 at 134; time 64 at 212 and 272; date 414.
> All strokes inside the circle +25% (rings 11.9, battery 15, welds 8.1 band / 2.9 beads,
> dashes 2.5, wear lines 1.0 to 1.1, glyphs 2.5 px).
> Console: panel and button edges are tube-shaded (dark outer and inner edges, edge colour,
> bright core), 3.6 dp; divider rails 3 dp with vertical shading; primary buttons domed.
> Fonts: SIL OFL fonts bundled (Orbitron, Rajdhani, Exo 2, Share Tech Mono; licences in
> assets/licenses), taken from the Fontsource npm packages and converted WOFF2 -> TTF with
> fontTools. FONT button cycles: SYSTEM, ORBITRON + RAJDHANI (default), ORBITRON, EXO 2,
> RAJDHANI; the choice is remembered.

### Build 34

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 34 (0.1.33)
>
> Face: laid out on the exact coordinates of devices/garmin-tactix/source/HomeView.mc, scaled
> by 0.93 about the centre so the battery arc sits inside the bezel. Text is drawn by ink like
> the Garmin fonts (capital height and bottom row): XTINY 19 px capitals, TINY 27, SMALL 31 px
> digits, NUMBER_MEDIUM 59 px digits. Build 33 sized text by font size, so capitals came out at
> about 60% of the Garmin. Battery: 120 degree arc at r 209, ticks every 4 degrees, icon plus
> BAT n% on rows 42..60. Title on 68..86. Owl 100 centred at 141, ear icons at +/-72, y 118.
> Hours ink 192..251, minutes 258..317. Rings R62 at y 190, x 95 and 359, with bands at y-24..-6,
> 0..31, and 36..54. Buttons [22,262,133,63] and [299,262,133,63]. Recon brackets 326..393, at
> least 220 wide. Date on 401..419. Strokes are the Garmin's pens times 1.7.
> Console: HUD style. Top status strip (link state, event count, sweeping scan glow); panels
> with a gradient surface, a faint grid, and glowing corner brackets; title tabs with a
> pulsing LED, slash accents, and a tick-marked rail; pill buttons (glass or domed, with a
> specular line); disabled buttons stay visible; recessed readouts for counts. Shadows on
> frames and buttons no longer clip their own halo (clip = false).

### Build 35

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 35 (0.1.34)
>
> Console borders doubled: the tube-shaded edge around the console, its panels, and its cards
> is 7.2 dp (was 3.6, now the FrameEdge constant in Theme.kt); corner brackets 6 dp (was 3).
> Inner padding grown by about 4 dp so content clears the thicker edge.

### Build 36

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 36 (0.1.35)
>
> Console borders doubled again: FrameEdge 14.4 dp, corner brackets 12 dp wide and 32 dp long.
> Corner radii grown so the thick edge stays rounded (console 20, panels 18, cards 16 dp), and
> inner padding grown by about 7 dp to clear the edge.

### Build 37

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 37 (0.1.36)
>
> Warm circle, cold console. The dial behind the face metrics is a very faint burnished bronze
> (radial #2B1D10 lit from the upper left to #0E0905 at the bezel) with fine seeded concentric
> polishing arcs and a soft sheen; the steel bezel gets a translucent bronze glaze (#B87333 at
> 18%). Console colder: background #02060F, surface #0B1A2C, edge #6FE6FF, grid lines ice-blue
> at 10%.

### Build 38

**Version declaration recovered:** `0.1.37`. These are checkpoint declarations, not inferred release dates.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 38 (0.1.37)
>
> Bronze one step stronger: dial #3C2916 -> #281B0E -> #140D07, polishing arcs and sheen about
> 60% stronger, bezel glaze 25% (was 18%).

**Inline build note** — [S04].

*Recovered source wording (historical):*

> bronze dial and glaze one step stronger

### Build 39

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 39 (0.1.38)
>
> Rings. Diagnosis on the watch (b38_rings_diag.txt): Android 10, location granted, mode 3,
> network location disabled and no active network, no last known location at all; an MTK
> AMBIENT_TEMPERATURE sensor exists (on-change) and was registered but never delivered a
> reading. Fixes: the sensor's readings are logged (first three, then one a minute, and a line
> if nothing arrives in 10 s), and TEMP shows "--" while waiting (N/A only with no sensor).
> Location: asked for when the app opens; GPS (and network when enabled) is retried, 3 minutes
> on and 5 off, until a fix, then refreshed every 30 minutes; the last fix is saved in prefs so a
> restart keeps it; the ring reads NO FIX while searching and NO LOC without permission.
> ACCESS_FINE_LOCATION is no longer capped at API 30, and COARSE is declared.
> The watch layout rule is now WatchFormFactor (unit tested), tied to the design standard.

### Build 40

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 40 (0.1.39)
>
> Evidence from b39_hw_diag.txt. TEMP: sensorservice's recent-events record has STEP_COUNTER,
> LIGHT, and ACCELEROMETER but never AMBIENT_TEMPERATURE, so the listed MTK ambient sensor has
> never produced a reading; the product listings name no temperature sensor. GPS: the MediaTek
> GNSS stack is real (MNLD hal_gps_start, GNSS HAL wakelock, MtkAgpsNative), but assisted GPS
> fails with no SIM ("IMSI is invalid") and no cell data, so every start is an unassisted cold
> start; no fix arrived in 2 minutes.
> Changes: a temperature sensor silent for 10 s is treated as absent and TEMP shows N/A. The
> receiver keeps searching (up to an hour per attempt) until the first fix, then refreshes for
> 3 minutes every 30. GnssStatus is logged every 30 s (satellites used, in view, best C/N0), and
> the SUN ring's bottom line shows SAT used/seen while there is no position.

### Build 41

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 41 (0.1.40)
>
> GPS evidence (b40_rings_log.txt): the receiver works (22 satellites in view within 30 s) but
> the strongest signal was 21 to 23 dB-Hz, an indoor level, then 0; no fix. A later outdoor try
> also gave no fix (log not yet collected). The LayerWand (T-Dongle-C5) has no GNSS, and the
> watch cannot pair with a phone, so the watch's sources are its own GPS and manual entry.
> Manual position: a POSITION panel in the console takes MGRS (any precision, spaced or not),
> UTM, or decimal latitude and longitude (with N/S/E/W or signs), converts on the watch
> (GridPosition: Snyder transverse Mercator series on WGS84, MGRS AA lettering, band-resolved
> northing, Norway and Svalbard zone exceptions; polar areas refused), and saves it. The SUN
> ring uses whichever is newer, the last GPS fix or the manual entry, and marks a manual
> position MANUAL in amber. GridPositionTest checks 9 reference points from the Python mgrs
> package and PROJ (both hemispheres, equator, 32V, 33X) to 1 m and 0.5 m, plus parsing and
> refusals.

### Build 42

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 42 (0.1.41)
>
> Outdoor GPS (b40_outdoor_log.txt, 13:17 to 14:04): at most 1 satellite in view, mostly 0,
> never above 24.4 dB-Hz, no fix; worse than at the desk. This watch's GNSS reception is not
> usable, so manual entry is its primary position source.
> Position entry: on the watch the system keyboard would not accept the digits after "18S UJ".
> The POSITION panel now has its own HUD keypad (digits, minus, decimal point, ABC for the 24
> grid letters without I and O, SPACE, DEL, CLEAR) typing into a readout well; the system
> keyboard is not used.

### Build 43

**Version declaration recovered:** `0.1.42`. These are checkpoint declarations, not inferred release dates.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 43 (0.1.42)
>
> "PM MANUAL" and "SAT 0/0" on the SUN ring's bottom row ran outside the ring: that row's chord
> is only about 60 px. The bottom row now carries only AM or PM (held to 56 px), and MANUAL, or
> the satellite count while there is no position, sits on a small riveted plate (dark bronze,
> coloured rim, held to 80 px) in place of the sun glyph at the top of the ring, where the
> chord is about 84 px.

**Inline build note** — [S03].

*Recovered source wording (historical):*

> MANUAL and SAT count on a plate inside the SUN ring

### Build 44

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 44 (0.1.43)
>
> Always-on, first part. Found: the MISSION CONFIGURATION sensor checkboxes were never sent
> anywhere (Link 0.1 carries only START with a target, STOP, and CLEAR), and the Recon target
> and checkboxes were Compose state, lost when Android recreated the screen.
> Changes: the target and the checkboxes are saved (MissionPrefs); the page now says the
> selection is saved on the watch and the LayerWand runs all detectors until Link 0.2 can send
> it. The link service raises a high-priority notification with vibration for every new
> detection (DetectionAlerts): new means an event id above the highest already alerted for
> that session, remembered across restarts; more than three at once collapse into one summary;
> silent while the LayerWand reports sleep mode. Tapping an alert opens DETECTED ATTACKS with
> the face slid away. Log tag LT_ALERTS.

### Build 45

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 45 (0.1.44)
>
> Build 44 on the watch: the first detection raised notification 2001 on the Detection alerts
> channel (importance high, vibration 0/400/150/400), but LT_ALERTS logged session -1: the
> LayerWand sent the event before its first STATUS, and the session was only learned from
> STATUS. After a LayerWand restart, event ids begin at 1 again, so events filed under -1 could
> be taken as already alerted, a missed alert. Fixes: the session is taken from HELLO_ACK, which
> precedes every event (clearing the mirror on a change, as STATUS does); an event with an
> unknown session still alerts, tracked in memory only; and if every event id in the alerted
> session is below the last alerted id, the LayerWand restarted with a reused session number and
> de-duplication starts over.

### Build 46

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 46 (0.1.45)
>
> Unplugging the LayerWand did not time the link out on the watch: the app relied only on a
> GATT disconnect callback, which did not arrive, and never watched the Status heartbeat that
> contracts/link.md names as the link-health signal (Status is notified once a second). Added
> LinkHealth (unit tested): STALE after 3 s without Status (the Garmin face's threshold), LOST
> after 8 s, with a scheduled Recon window (schedule 1) extending the allowance by nextReportS.
> LinkClient checks once a second from connect; on LOST it drops the GATT connection itself,
> marks DISCONNECTED, and reconnects with the existing backoff. While STALE, the console strip
> reads LINK STALE in amber and the face's Recon bracket reads LINK STALE with the count greyed,
> as on the Garmin.

### Build 47

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 47 (0.1.46)
>
> Watch buttons, measured with getevent (b46_keys.txt): the green side button sends KEY_BACK on
> mtk-kpd, the red one KEY_POWER (never delivered to apps). The green button becomes the
> LayerTime button through an accessibility service (LayerTimeKeyService) that filters only
> the Back key and watches window changes to know which app is in front; it reads no window
> content. Inside LayerTime the key passes through and slides the face in or out
> (BackHandler). Anywhere else a short press opens LayerTime and a hold of 500 ms performs the
> normal Back, so other apps keep Back. A LAYERTIME BUTTON panel shows whether it is on and
> opens Accessibility settings. Open question to verify on the watch: whether Android 10 lets
> the accessibility service bring LayerTime to the front from the background; fallback is the
> display-over-other-apps permission.
> Reconnect wording: after a lost link (GATT disconnect or the heartbeat watchdog) the face
> reads RECONNECTING with the last count greyed until the link is back, instead of NO LAYERWAND
> once scanning restarts.

### Build 48

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 48 (0.1.47)
>
> Build 47 on the watch: the run block force-stopped the app right after enabling the
> accessibility service, and a force stop disables an app's accessibility services, so the
> button was off (enabled_accessibility_services null, accessibility_enabled 0). Run blocks no
> longer force-stop. With the service on, green in the app and hold-for-Back worked, but a short
> press outside LayerTime did not bring it back. The service decided "LayerTime in front" from
> window-change events; it now reads the activity's own onResume/onPause flag (AppVisibility,
> same process), and logs every green press with that state under LT_BUTTON so the next run
> shows whether the key arrives and whether the activity start is refused.

### Build 49

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 49 (0.1.48)
>
> Link check after build 48 (b48_wand.txt): a freshly started LayerWand advertised and the watch
> connected 4.5 s after boot; the earlier failure (watch scan heard nothing; app had started 33
> scans) was not reproduced and is not yet explained. Not changed in this build.
> The face now rolls off to the left instead of sliding: translation plus a rotation equal to
> the distance travelled over the radius (rolling without slipping), counter-clockwise going
> left, over 380 ms (was a 260 ms slide). The black left half is drawn by a separate layer that
> slides without turning (FaceView drawLeftBlack = false in the sliding layout), and the handle
> that rolls it back is a fixed strip at the screen's left edge, outside the turning layer.

### Build 50

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 50 (0.1.49)
>
> Roll sounds replace the slide sounds (same file names, same SoundPool). Out (0.95 s): gravel
> under a rolling disc for the 380 ms roll, then a heavy metal clunk against a stop at 370 ms.
> In (0.70 s): gravel, then a light damped thud as the face drops into a fitted groove at 385 ms.
> Synthesized with numpy by wireshark-spike/make_face_sounds.py (seeded, reproducible).

### Build 51

**Version declaration recovered:** `0.1.50`. These are checkpoint declarations, not inferred release dates.

**Individual recorded note** — [S01].

*Recovered source wording (historical):*

> Build 51 (0.1.50)
>
> The synthesized gravel (builds 50 and two previews) sounded like static. The roll sounds now
> use a real recording chosen by Michael: one second (2.0 to 3.0 s of his cuts) of a car
> stopping on gravel for rolling out, and one second of a car moving on gravel for rolling back,
> each followed at 0.98 s by the synthesized metal clunk or groove thud. Source and license in
> assets/licenses/SOUND_gravel.txt (Pixabay). The roll is now 980 ms (ROLL_MS, was 380) so the
> thud lands as the face stops.

**Inline build note** — [S06].

*Recovered source wording (historical):*

> real gravel in the roll sounds, roll 980 ms

### Build 52

**Version declaration recovered:** `0.1.51`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S05].

*Recovered source wording (historical):*

> Low-confidence detections are listed without an alert

**Checkpoint commit note** — [S25].

*Recovered source wording (historical):*

> Low-confidence detections no longer buzz the watch
>
> The watch alerted on every detection whatever its confidence, and the LayerWand does not filter Low. LayerTime-Sensors defines Low as worth logging, not worth an alert, and 1.1.0 moves every module-maker Flock prefix (Liteon, Silicon Labs, Espressif, and others found in ordinary laptops) to Low. Without this change, 1.1.0 would have added wrist alerts instead of removing them.
>
> AlertPolicy.worthAnAlert() skips confidence 0 (Low) only. Medium, High, and any value this build does not know still alert, so a future confidence level cannot silence the wrist by accident. Low detections still arrive, show in DETECTED ATTACKS, and count as seen. Build 52, versionName 0.1.51. AlertPolicyTest covers the rule; verified on the LOKMAT watch.

### Build 53

**Version declaration recovered:** `0.1.52`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S07].

*Recovered source wording (historical):*

> tappable signals, QUICK LOOK with Wireshark vendor lookup

**Checkpoint commit note** — [S26].

*Recovered source wording (historical):*

> Tappable signals and QUICK LOOK with Wireshark vendor lookup
>
> The main screen's DETECTION EVENTS list did nothing when tapped, though tapping a signal is the obvious way to investigate it. Every signal now opens ATTACK DETAIL, and BACK returns to the list it came from. The detail screen follows the live event, so its readings update while it is open.
>
> ATTACK DETAIL opened with six cards of mostly UNAVAILABLE fields, because Link 0.1 carries no raw frames. QUICK LOOK is now card 1 and shows only what helps a quick investigation with just the watch and a LayerWand: what it is, who sent it, whether it is getting closer, whether it is still there, the radio, and what to do. Fields with no data are left out.
>
> Wireshark 4.4.19 on the watch exports ws_manuf_lookup, its built-in IEEE registry with the 28- and 36-bit MA-M and MA-S blocks. A new JNI call looks up the vendor of the transmitter address Link 0.1 already sends, off the main thread because loading libwireshark takes seconds. It returns UTF-8 bytes, not a jstring, because NewStringUTF expects modified UTF-8. Bluetooth vendors carry a caveat, because Link 0.1 does not say whether an address is public or random.
>
> The signal trend compares the latest reading with the mean of earlier readings in the last minute and needs three. Changes under 6 dB read as steady, since body position alone moves the signal that much. Card 2 labelled the transmitter address as SSID and claimed no address was available; fixed. SignalAnalysisTest covers address parsing, address kind, the trend, last-heard time, advice for every detector, and vendor parsing. Build 53, versionName 0.1.52, verified on the LOKMAT watch.

### Build 54

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

**Build-specific checkpoint note** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The grouped checkpoint explicitly records the LINK MEASURE panel and a 100-ping bench check with zero losses, 112 ms median and 129 ms p95 round trip at about one metre. This is link transport measurement, not RF detection qualification.

### Build 55

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 56

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 57

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 58

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 59

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

**Reverted experiment range** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Within the explicitly identified 59–62 experiment range, a cable/groove/tab visual treatment was tried and then reverted. The record does not identify the individual variant for each number; build 63 restores the build-58 face.

### Build 60

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

**Reverted experiment range** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Within the explicitly identified 59–62 experiment range, a cable/groove/tab visual treatment was tried and then reverted. The record does not identify the individual variant for each number; build 63 restores the build-58 face.

### Build 61

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

**Reverted experiment range** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Within the explicitly identified 59–62 experiment range, a cable/groove/tab visual treatment was tried and then reverted. The record does not identify the individual variant for each number; build 63 restores the build-58 face.

### Build 62

**Grouped checkpoint only** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups builds 54–63: link measurement, connection notices, press feedback and the salvaged console/face treatment. No complete individual change note was recovered for this number; see the grouped checkpoint below.

**Reverted experiment range** — [S30].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Within the explicitly identified 59–62 experiment range, a cable/groove/tab visual treatment was tried and then reverted. The record does not identify the individual variant for each number; build 63 restores the build-58 face.

### Build 63

**Version declaration recovered:** `0.1.62`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S08].

*Recovered source wording (historical):*

> watch face reverted to build 58 exactly (no cable, no tab); hull, rack (teeth 20% taller), and scratches kept

### Build 64

**Grouped checkpoint only** — [S31].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups watch-controller builds 64–67: full-screen hunt radar, signal-colored step trail, distance band without invented direction, tone and found-state handling. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 65

**Grouped checkpoint only** — [S31].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups watch-controller builds 64–67: full-screen hunt radar, signal-colored step trail, distance band without invented direction, tone and found-state handling. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 66

**Grouped checkpoint only** — [S31].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The checkpoint groups watch-controller builds 64–67: full-screen hunt radar, signal-colored step trail, distance band without invented direction, tone and found-state handling. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 67

**Version declaration recovered:** `0.1.66`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S09].

*Recovered source wording (historical):*

> HUNT step trail counts steps from the accelerometer (the step sensor never reported)

### Build 68

**Grouped checkpoint only** — [S32].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Builds 68–69 were committed together: LayerScope branding, installation-ID migration, Hunter naming, the aperture face and readability changes. Do not interpret the C5 fox-hunt device then named Vixen as the later flagship Android product. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 69

**Version declaration recovered:** `0.1.68`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S11].

*Recovered source wording (historical):*

> LayerWand is now the Hunter in every string, aperture on the face, DETECTION EVENTS in a HUD panel, 12 sp minimum text

### Build 70

**Version declaration recovered:** `0.1.69`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S13].

*Recovered source wording (historical):*

> buttons and detail tags never get crushed on narrow panels; two-column hunt readings on a sideways phone

**Checkpoint commit note** — [S27].

*Recovered source wording (historical):*

> LayerScope fits phone screens without crushing anything
>
> Build 70 (0.1.69), from the first run on a phone (Galaxy Z Flip6,
> 1080 x 2640, Android 16).
>
> SCAN & CONNECT and DISCONNECT sat in a Row that never wraps. On a narrow
> phone panel DISCONNECT was squeezed to a sliver upright and broke as
> "DISCONNEC / T" sideways. The row is now a FlowRow, so DISCONNECT drops
> to its own line when it does not fit, and every button label is
> single-line and never wraps mid-word.
>
> Event detail rows put the value and its source tag (CALC, DECODED, and
> so on) in a Row. When the pair was a little too wide, the value took the
> width and the tag was squeezed to almost nothing, then wrapped one
> letter per line, which made each row tall and mostly empty. The pair is
> now a FlowRow: the tag sits beside the value when it fits and moves
> under it when it does not, and the tag never wraps.
>
> A sideways phone is about 880 x 360 dp, so the hunt console's single
> column scrolled and hid STEPS and TIME behind STOP HUNT. Screens more
> than twice as wide as tall now move the console left and show the
> readings in two columns; all seven fit. The LOKMAT (640 x 480) keeps the
> approved single-column layout, checked by screenshot.

### Build 71

**Version declaration recovered:** `0.1.70`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S12].

*Recovered source wording (historical):*

> DISCONNECT on the notification, optional swipe-away disconnect, DISCONNECT live after a failure, hull behind the upright hunt screen, phone text starts at 0.85x

**Checkpoint commit note** — [S28].

*Recovered source wording (historical):*

> LayerScope gets real off switches and phone-sized text
>
> Build 71 (0.1.70).
>
> Always-on keeps the link in a foreground service, so closing the app
> never stopped it, and the in-app DISCONNECT was the only off switch. On a
> phone that meant the link kept grabbing the Hunter after the app was
> closed, and the Hunter takes one connection. Three changes:
> - The service notification has a DISCONNECT action that calls the same
>   stop as the in-app button, and tapping the notification opens the app.
> - SWIPE AWAY DISCONNECTS, in CONNECTION, off by default: when on,
>   onTaskRemoved stops the link, so swiping the app away is an explicit
>   off. Default off so a stray swipe never drops the link in the field.
> - The in-app DISCONNECT was disabled in the FAILED state, which is
>   exactly when the service is still running and retrying ("Link
>   failed"). It is now enabled whenever the link is not IDLE.
> Verified on the Z Flip6 by asking Android whether the service runs after
> each step: swipe with the switch off keeps it, notification DISCONNECT
> stops it, swipe with the switch on stops it. All four passed.
>
> The upright hunt screen gets the same salvaged hull as landscape. The
> hull's overlay straps are placed for the landscape console frame and
> landed across 2 S AVG, HEARD, and TIME on this layout, so the upright
> layout draws the hull without the overlay.
>
> The 12 sp minimum came from the watch field test. On a phone, held
> closer and sharper, the same sizes looked oversized. Until the user
> presses A+ or A-, TEXT SIZE now defaults by device: 1.0x on the watch
> layout (WatchFormFactor, checked on the screen's long and short sides so
> orientation does not matter) and 0.85x elsewhere. The LOKMAT still
> reads 1.4x after the update.

### Build 72

**Version declaration recovered:** `0.1.71`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S10].

*Recovered source wording (historical):*

> 16 KB page alignment for libltws and all 14 Wireshark libraries (clears the Android 15+ compatibility warning); NDK r30; native libs stored uncompressed

**Checkpoint commit note** — [S29].

*Recovered source wording (historical):*

> LayerScope 16 KB page alignment (build 72)
>
> Android 15 and newer devices with 16 KB memory pages ran LayerScope in
> compatibility mode, and the Z Flip6 (Android 16) showed the "Android App
> Compatibility" warning on every launch. The cause was native code linked
> for 4 KB pages: our own libltws (NDK 26.1 default) and the 14 Wireshark
> 4.4.19 prebuilt libraries. Google Play blocks updates without 16 KB
> support from February 1, 2027.
>
> libltws: ndkVersion is pinned to NDK r30 (30.0.16248370), which links
> 16 KB aligned by default. The two linker flags
> (-z max-page-size=16384, -z common-page-size=16384) stay in CMakeLists
> as a safety net if the NDK is ever lowered below r28.
>
> Wireshark prebuilts: build_wireshark_arm64.sh now passes both flags to
> every sub-build: the CMake shared and module linker flags, the libffi and
> libgpg-error configure LDFLAGS, the libgcrypt make LDFLAGS, and the Meson
> cross file link args. A new step 11 reads every library with llvm-readelf
> and fails the build unless each LOAD segment is aligned to 0x4000 or
> more. Step 0 now also installs the Python "packaging" module, because the
> GLib Meson build failed without it on a clean WSL Ubuntu 22.04. The set
> was rebuilt with NDK r27c in WSL; all of it is plain C with no shared
> C++ runtime, so building it on r27c while the app uses r30 is safe.
> jniLibs/ and wireshark-prebuilt/ stay gitignored; the script rebuilds
> them.
>
> Packaging: useLegacyPackaging is now false, Google's preferred 16 KB
> setup. Native libraries are stored uncompressed and 16 KB zip aligned,
> and Android maps them straight from the APK instead of unpacking a
> second copy. The APK grows from about 51 MB to about 143 MB, and the
> installed size drops from about 183 MB to about 143 MB. The old comment
> said the libraries were compressed to protect versioned sonames, which
> was never the reason; it is replaced.
>
> WiresharkDiagActivity: with nothing unpacked, the diag screen reported 0
> native libraries. It now counts the libraries inside the APK and says
> whether they are stored uncompressed.
>
> Verified: zipalign -c -P 16 passed. All 16 libraries in the APK have
> every LOAD segment aligned to 0x4000 and stored uncompressed at 16 KB
> zip offsets. RELRO ends on a 16 KB boundary in 15 of them; the 16th,
> AndroidX libandroidx.graphics.path.so (LLD 14.0.7, pulled in by
> Compose), ends RELRO at 0x6000, but its RELRO is exactly its own LOAD
> segment and no writable data shares that page, which Google's rule
> allows. It gets a newer copy with the Compose update for the API 36
> move. The compatibility warning no longer appears on the Flip. The
> Wireshark decode test matched TShark 4.4.19 on 6 of 6 fields with
> END on the Flip (Android 16) and the LOKMAT watch (Android 10).

### Build 73

**Inline build note** — [S14].

*Recovered source wording (historical):*

> target API 36 (Play Store requirement, min 2026-08-31) on AGP 8.11 / Gradle 8.13; edge-to-edge handled with safeDrawing insets

### Build 74

**Inline build note** — [S14].

*Recovered source wording (historical):*

> detections grouped one row per MAC address, one alert per MAC; LayerTime-Sensors 1.1.2 AirTag grading

### Build 75

**Inline build note** — [S14].

*Recovered source wording (historical):*

> phone layout - fixed watch-size face pinned upper-left (portrait) / far-left (landscape), data console extended to fill the rest

### Build 76

**Inline build note** — [S14].

*Recovered source wording (historical):*

> phone face 220->190, landscape console inset to clear the edge handle

### Build 77

**Inline build note** — [S14].

*Recovered source wording (historical):*

> adaptive shell - five destinations, bottom bar (compact) / left rail (wide), face rolls to a header off OPS

### Build 78

**Inline build note** — [S14].

*Recovered source wording (historical):*

> OPS thinned to face + connection + detection; panels moved to SYSTEM, measure to TOOLS

### Build 79

**Inline build note** — [S14].

*Recovered source wording (historical):*

> Wireshark window in TOOLS - import, display filter, packet list, protocol tree and hex

### Build 80

**Grouped checkpoint only** — [S33].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

One explicit batch covers builds 74–89: event grouping, adaptive navigation, evidence transfer, Wireshark/Builder and mission wiring. Features not separately attributed remain batch-level evidence. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 81

**Version declaration recovered:** `0.1.80`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S14].

*Recovered source wording (historical):*

> events export (CSV + JSON) with triage filter (drop LOW noise, keep away-from-owner, flag tracker dwell) and external-dir autosave

### Build 82

**Inline build note** — [S15].

*Recovered source wording (historical):*

> GET_TEXT through the link FIFO so source MACs stop starving under an event flood; target bound at send time; empty replies logged

### Build 83

**Inline build note** — [S15].

*Recovered source wording (historical):*

> first release-signed build (PRS upload key)

### Build 84

**Grouped checkpoint only** — [S33].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

One explicit batch covers builds 74–89: event grouping, adaptive navigation, evidence transfer, Wireshark/Builder and mission wiring. Features not separately attributed remain batch-level evidence. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 85

**Grouped checkpoint only** — [S33].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

One explicit batch covers builds 74–89: event grouping, adaptive navigation, evidence transfer, Wireshark/Builder and mission wiring. Features not separately attributed remain batch-level evidence. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 86

**Inline build note** — [S15].

*Recovered source wording (historical):*

> watch to cool steel, teal ground, toward the OPS comp; six destinations (OPS, EVENTS, TOOLS, BUILDER, MISSION, SYSTEM)

### Build 87

**Inline build note** — [S15].

*Recovered source wording (historical):*

> MISSION wired to the Hunter (SetSensors allow-list), presets check their sensors, sensors grouped, EAPOL listed; OPS "Select Sensors" (All Sensors / Only Deauth) is a shortcut into MISSION

### Build 88

**Inline build note** — [S15].

*Recovered source wording (historical):*

> analytic pipeline slice 1 - workflow wiring files (deauth triage shipped), engine, seven checks + verdict, frame fabricator fixtures, read-only Builder viewer, TRIAGE on event detail

### Build 89

**Version declaration recovered:** `0.1.88`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S15].

*Recovered source wording (historical):*

> Link 0.2 evidence (slice 2) - pull a Wi-Fi detection's frames from the Hunter (GET_FRAMES/GET_FRAME_BYTES, MTU 247), import them, run the triage; protected-network flood now LIKELY ATTACK with a note

### Build 90

**Grouped checkpoint only** — [S34].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The 90–92 batch records evidence hold/re-hold, packet explanations, source SD PCAP windows and pulling/analyzing the Hunter card without a computer. Individual 90/91 allocations are not stated. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 91

**Grouped checkpoint only** — [S34].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The 90–92 batch records evidence hold/re-hold, packet explanations, source SD PCAP windows and pulling/analyzing the Hunter card without a computer. Individual 90/91 allocations are not stated. No complete individual change note was recovered for this number; see the grouped checkpoint below.

### Build 92

**Version declaration recovered:** `0.1.91`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S16].

*Recovered source wording (historical):*

> PULL FROM THE HUNTER (the SD card over the Link, CARD_LIST/CARD_READ, CRC-checked) on HUNTER CARD; Hunters named Hunter-C5-001. build 91: IMPORT HUNTER CARD on EVENTS (the Hunter SD log with repeat and evidence lines, saved frames open in Wireshark). build 90: MISSION evidence hold (SetEvidenceHold, Off/5/10/20/30 s; the Hunter re-holds on a sustained deauth flood); Wireshark window explains every frame type above its card. build 89: Link 0.2 evidence (slice 2) - pull a Wi-Fi detection's frames from the Hunter (GET_FRAMES/GET_FRAME_BYTES, MTU 247), import them, run the triage; protected-network flood now LIKELY ATTACK with a note

### Build 93

**Gap — no individual change note recovered.** Nearby references to Hunter firmware 93/94 concern the separate dongle and are not Android release notes. No feature, test result or installation claim is manufactured for this entry.

### Build 94

**Gap — no individual change note recovered.** Nearby references to Hunter firmware 93/94 concern the separate dongle and are not Android release notes. No feature, test result or installation claim is manufactured for this entry.

### Build 95

**Gap — no individual change note recovered.** Nearby references to Hunter firmware 93/94 concern the separate dongle and are not Android release notes. No feature, test result or installation claim is manufactured for this entry.

### Build 96

**Gap — no individual change note recovered.** Nearby references to Hunter firmware 93/94 concern the separate dongle and are not Android release notes. No feature, test result or installation claim is manufactured for this entry.

### Build 97

**Gap — no individual change note recovered.** Nearby references to Hunter firmware 93/94 concern the separate dongle and are not Android release notes. No feature, test result or installation claim is manufactured for this entry.

### Build 98

**Gap — no individual change note recovered.** Nearby references to Hunter firmware 93/94 concern the separate dongle and are not Android release notes. No feature, test result or installation claim is manufactured for this entry.

### Build 99

**Gap — no individual change note recovered.** Nearby references to Hunter firmware 93/94 concern the separate dongle and are not Android release notes. No feature, test result or installation claim is manufactured for this entry.

### Build 100

**Inline build note** — [S17].

*Recovered source wording (historical):*

> one time on upgrade, the four trackers are unchecked in a saved Mission list (the 2026-11-06 capture showed SetSensors 0x5FFE0, trackers on). build 99: every step of bringing the link up has a 4 s deadline (connect 10 s) and a missed one reconnects naming the step; late callbacks from dropped connections are ignored; the Scan card check retries once; the held Hunter always has its row in HUNTER ACTIONS. Found in the 2026-11-06 capture: service discovery and a notification setup that never answered. build 98: diagnostics: the app log and every Scan card check also go to logcat (tags LayerScope, LayerScope.Scan). build 97: Clear Card erases the Hunter's SD card completely, then lists the card again and says what it holds; nothing on the phone is touched. build 96: trackers (AirTag, Tile, SmartTag, Google Tag) off by default and under Quick Start All Sensors, after a field test where their flood hid every other sensor. build 95: Builder editing (Copy, New, drag steps in from the Tools Palette, move, connect right dot to left dot, Disconnect, Remove, settings, Save with versions, Check against the 7 deauth scenarios, the list of accepted flows with Use for triage); palette and placed tiles are the icon with the name below; ATTACK TIMELINE greyed out. build 94: Builder Tools Palette holds the Tools page tools with their icons (a tap opens the tool); a tool step (Frames behind it, Wireshark) shows the tool icon; workspace text sized in dp (it drew in raw pixels, a third of its size on a dense phone) and larger. build 93: HUNTER ACTIONS replaces CONNECTION (Scan, a row per Hunter with Connect, Disconnect, and Clear Card for Hunters with an SD card); Swipe away disconnects moved to SYSTEM, LOKMAT watch only; OPS Clear also empties the phone's list; PULL FROM THE HUNTER only when the Hunter reports an SD card; Hunters named from the chip in base 32, Hunter-C5-BSV72; hunter_NNNN.log card names. build 92: PULL FROM THE HUNTER (the SD card over the Link, CARD_LIST/CARD_READ, CRC-checked) on HUNTER CARD; Hunters named Hunter-C5-001. build 91: IMPORT HUNTER CARD on EVENTS (the Hunter SD log with repeat and evidence lines, saved frames open in Wireshark). build 90: MISSION evidence hold (SetEvidenceHold, Off/5/10/20/30 s; the Hunter re-holds on a sustained deauth flood); Wireshark window explains every frame type above its card. build 89: Link 0.2 evidence (slice 2) - pull a Wi-Fi detection's frames from the Hunter (GET_FRAMES/GET_FRAME_BYTES, MTU 247), import them, run the triage; protected-network flood now LIKELY ATTACK with a note

**Retained source comment** — [S36].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The source comment explicitly starts the one-time trackers-off migration at build 100 or later. It preserves later operator choices instead of re-enabling noisy trackers at every restart.

### Build 101

**Inline build note** — [S17].

*Recovered source wording (historical):*

> Builder - every check is a Wireshark check you pick from one tile. Drop the Wireshark tile, long-press it, and hold a check to choose it (a popup confirms); the tile then shows that check's name. Detection now gathers the frames itself, so the separate Frames step is gone. The Verdict runs rules you write: each rule sets a verdict and a sentence when its test is met, read top to bottom, first match wins; a test can count checks by how they lean, count identical frames (same type, meaning, source, and destination), or combine those with all, any, and not; the sentence fills blanks like {suspicious}, {counted}, and {top}. The shipped deauth triage is those rules written out, and it still calls all seven test scenarios the same way.

### Build 102

**Inline build note** — [S17].

*Recovered source wording (historical):*

> HUNTER ACTIONS - a scan only lists the Hunters it hears, right away, and never connects during a scan. The SD-card label and Clear Card come from the Hunter you connect to, read from its live link, which is the one connection that reads the card reliably; they appear once it is connected, not during the scan. The old scan-time card probe stalled on Android service discovery (the 2026-11-06 capture showed it timing out twice while the real connect read it in 0.6 s) and is gone.

**Later correction:** the retained build-109 note explicitly reverted the 102–108 connection changes. The claims above describe what was attempted at the time, not a durable verified fix.

### Build 103

**Inline build note** — [S17].

*Recovered source wording (historical):*

> HUNTER ACTIONS - Connect survives a cold service discovery. On a first connection Android's service discovery can be slow or never answer; the connect now settles briefly, gives discovery its own longer window, and on a miss clears the service cache and tries again on the same connection up to four times, instead of dropping the link and rescanning (which also reset the Hunter's advertising). The link health timeout starts after the handshake, so a slow discovery no longer trips a false link-lost. Needed once build 102 removed the scan-time probe that had been warming Android's service cache.

**Later correction:** the retained build-109 note explicitly reverted the 102–108 connection changes. The claims above describe what was attempted at the time, not a durable verified fix.

### Build 104

**Inline build note** — [S17].

*Recovered source wording (historical):*

> HUNTER ACTIONS - Connect is live the moment a Hunter is listed (it no longer waits for the scan to finish), and tapping it ends the scan. Connect also retries the GATT connection itself: if Android brings the radio link up but never reports the GATT connection to the app (the 2026-11-06 capture showed the LE link up in 62 ms with no GATT callback after, because the removed scan-time probe used to warm the stack), it closes and reopens the connection up to four times before giving up.

**Later correction:** the retained build-109 note explicitly reverted the 102–108 connection changes. The claims above describe what was attempted at the time, not a durable verified fix.

### Build 105

**Inline build note** — [S17].

*Recovered source wording (historical):*

> HUNTER ACTIONS - the connect gets past the Android GATT no-callback race. It settles 400 ms after the scan stops before connecting, the first try is a quick direct connect, and tries 2 to 4 use autoConnect (slower but it connects when the stack next sees the Hunter), each with a longer window. The 2026-11-06 both-sides capture showed four direct tries all getting no GATT callback while the Hunter stayed advertising with zero connects.

**Later correction:** the retained build-109 note explicitly reverted the 102–108 connection changes. The claims above describe what was attempted at the time, not a durable verified fix.

### Build 106

**Inline build note** — [S17].

*Recovered source wording (historical):*

> HUNTER ACTIONS - the connect builds a fresh device from the Bluetooth adapter by address (adapter.getRemoteDevice), not the scan result's device object, and connects directly with autoConnect off. The scan-bound device object was why Android kept bringing the radio link up but never reporting the GATT connection; the old card probe, which connected by address this way, always got through. It still settles 400 ms after the scan and retries up to four times, closing and reopening.

**Later correction:** the retained build-109 note explicitly reverted the 102–108 connection changes. The claims above describe what was attempted at the time, not a durable verified fix.

### Build 107

**Inline build note** — [S17].

*Recovered source wording (historical):*

> HUNTER ACTIONS - one clean connect, no retry churn. Each connectGatt registers an Android Bluetooth GATT client, and the build 104 to 106 retry loop leaked them until the stack could not bind any connection (the 2026-11-06 both-sides capture showed a dozen stale client_if, 44 through 128, all chasing the Hunter while the radio link formed every time). The connect is now a single direct connectGatt from a fresh adapter device by address; on a stall it fully closes and the normal reconnect rescans, so only one GATT client is ever open at a time.

**Later correction:** the retained build-109 note explicitly reverted the 102–108 connection changes. The claims above describe what was attempted at the time, not a durable verified fix.

### Build 108

**Inline build note** — [S17].

*Recovered source wording (historical):*

> HUNTER ACTIONS - connect primes like the old card probe. The first cold GATT connection to a freshly advertising Hunter often does not bind on this phone (Android sits in W4_CONN_ST); the probe used to absorb that. The connect now makes a direct connectGatt (autoConnect off) and, if it does not report connected in 6 s, fully closes and tries again up to four times, each with a matching close so no background GATT client is ever left registered. This undoes the autoConnect in builds 104 to 106 that leaked persistent background connects (a dozen zombie client_if camped on the Hunter) and the build 107 over-correction to a single connect with no priming.

**Later correction:** the retained build-109 note explicitly reverted the 102–108 connection changes. The claims above describe what was attempted at the time, not a durable verified fix.

### Build 109

**Version declaration recovered:** `0.1.108`. These are checkpoint declarations, not inferred release dates.

**Inline build note** — [S17].

*Recovered source wording (historical):*

> reverted all of my build 102-108 Bluetooth connect changes. The connect and the scan-time card probe are back to the build 100 code that connected cleanly; removing the probe and then piling on retries and autoConnect was the whole problem (autoConnect leaked persistent background connects that wedged the phone stack). HUNTER ACTIONS and the link behave as they did at build 100. The Builder work (Wireshark check menu, rule-based Verdict, folded Detection) is unchanged and kept.

### Build 110

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 110: operator-verifiable practice test
>
> Version code 110 / 0.1.109-diag adds Builder > Practice test. Load practice capture generates a six-packet offline PCAP and sends it through the normal Wireshark importer. It replaces only the current imported recording. Training provenance is carried into findings and displayed in Wireshark and Timeline; no packets are transmitted. The fixture now writes a structured RSN EAPOL-Key descriptor instead of an unspecified all-zero descriptor.
>
> The practice network is 02:00:00:00:00:01; the target client is 02:00:00:00:00:02; the distractor is 02:00:00:00:00:03. Frames 1/2 are disconnects at seconds 1/2, frame 3 is target EAPOL at second 3, frame 4 is distractor EAPOL at second 4, frame 5 is a target disconnect at second 10, and frame 6 is reverse-direction target EAPOL at second 11. The expected episode evidence is [1,2,3] and [5,6]; frame 4 is excluded. Expected counts: two disconnect episodes, two linked episodes, two related EAPOL frames.
>
> Open reference workflow opens a read-only Capture + Wireshark rate + Timeline + report wiring. Copy makes it editable. The report's first rule uses Timeline linked_episodes >= 2 and heading TWO LINKED EPISODES. Change the threshold to 3 and rerun: heading THRESHOLD NOT MET. Change back to 2 and rerun: TWO LINKED EPISODES returns. Inspect packet evidence to verify the distractor was excluded. The panel also explains how to rebuild the same capability from a blank Builder without JSON.
>
> Automated tests cover exact metrics, episode frame references, the 2/3/2 threshold experiment, generated PCAP record layout and RSN descriptors, training provenance, and workflow exchange. Passing JVM tests does not establish that native Wireshark decoding or touch interactions have been verified on the phone; the practice test is designed to exercise those remaining boundaries directly.

**Inline build note** — [S18].

*Recovered source wording (historical):*

> visual report rules and a reproducible, labeled Builder practice capture.

### Build 111

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 111: Run capture crash fix and Flip6 verification
>
> The Flip6 crash log showed ExceptionInInitializerError in Summary caused by Android ICU rejecting an unescaped closing brace in the placeholder regex. Both literal braces are now escaped. Desktop JVM matching had accepted the old pattern, so a device regression was added rather than relying only on those tests.
>
> The Android test runner is now BuilderSmokeInstrumentation (a dependency-free Instrumentation subclass in androidTest). Build/install app-full-debug.apk and app-full-debug-androidTest.apk, then run `adb -s [device serial omitted] shell am instrument -w com.projectrevivesolutions.layerscope.diag.test/com.projectrevivesolutions.layertime.controller.BuilderSmokeInstrumentation`. Its result bundle reports PASS/FAIL for Android template matching, native capture decoding and the 2/3/2 posture test; it is a smoke runner rather than an AndroidX/JUnit runner.
>
> Verified on the connected SM-F741W Flip6: templates PASS; native Wireshark decoded six packets, three EAPOL and zero malformed; exact two-episode/two-related-EAPOL finding PASS; unrelated fourth packet excluded; changing threshold 2 to 3 and back to 2 PASS. Build 111 (0.1.110-diag) was installed over build 110 preserving app data. The actual Builder UI was then used to load the practice recording, open the reference and press Run capture; the app remained running. Full visual/gesture verification of every editor action remains separate.

**Inline build note** — [S18].

*Recovered source wording (historical):*

> Android-compatible summary placeholders, on-device practice regression.

### Build 112

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 112: question-first design iteration
>
> The operator's feedback was that the canvas was unintuitive, deauth packets were not explicitly identified, and the report text was too technical. The first screen now starts with a question: Was a device told to leave a Wi-Fi network? Try the practice example loads the labeled six-packet recording and runs a ready-made analysis without requiring wiring. Custom tools and the canvas remain available through an explicit custom-builder path.
>
> The guided view exposes evidence, tap-to-add Wireshark/Timeline tools, report settings and Run analysis. Tap-to-add only connects an unambiguous single source/report and does not replace existing input connections. Report results appear directly in the Builder. Technical packet statistics and capture limits are expandable.
>
> Target reports preserve separate deauthentication/disassociation frame references and deauth direction. The reading layer groups episodes by network and device and states the MAC address, number of deauth packets and later EAPOL setup messages, followed by potential significance and a threat-posture recommendation. Client-originated deauth is not described as an attack directed at that client. Packet buttons explicitly identify deauth and connection-setup/EAPOL evidence.
>
> The default question report describes disconnect requests and later setup messages; it does not claim an observed successful disconnect/reconnect solely from deauth and EAPOL. The user's suggested explanation is the model for the reading layer: subject, sequence, consequence, action. Password-risk wording is conditional because it depends on network security and a usable captured exchange. Packet capture interpretation reference: https://documentation.meraki.com/Wireless/Troubleshooting_and_Support/Troubleshooting/Analyzing_Wireless_Packet_Captures (deauthentication, association, and successful WPA authentication sections).
>
> Build 112 (0.1.111-diag) was installed over build 111. JVM tests cover guided connections, placeholder assignment, explicit deauth counts, distractor exclusion, direction-sensitive reporting and the question-analysis workflow. On the Flip6, native decoding and the threshold 2/3/2 smoke regression still pass. Final visual review was interrupted by the phone's lock screen; the user was asked to unlock it. This is a first design iteration for operator review, not a claim of audience usability validation.
>
> After the user unlocked the Flip6, the build 112 UI was checked directly: Builder opened on the plain question screen; Try the practice example loaded and ran the recording with one tap; the report displayed client 02:00:00:00:00:02, three deauth packets across two bursts, two later EAPOL messages, the significance paragraph and threat-posture recommendation. Show deauth packets (3) opened Wireshark showing three selected frames; the packet list explicitly labeled frame 1 Deauthentication (0x000c). The phone was returned to the Builder report for user review. This confirms the interaction path and visible text, not audience comprehension; operator feedback remains the next design input.

### Build 113

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 113: action first
>
> The user clarified that a person may have very little time to assess a warning. Report order is now: training marker when applicable, recommended action, one short plain-language reason, expandable observations, then expandable technical details and recording limits. MAC addresses, packet counts and longer risk explanations no longer precede the recommendation.
>
> Installed build 113 (0.1.112-diag) on the Flip6. JVM build/tests and the native Android smoke regression pass. The one-tap practice UI was run and its visible first screen confirmed: Increase your threat posture and review this activity, followed by the short reason, What was observed?, and Technical details and recording limits. Both action and reason are visible together at the phone's current text size. The practice warning remains explicitly marked as training. This order is a presentation change, not an assertion of a predictable time-to-attack from radio observations.

### Build 114

**Grouped record with stated per-build detail** — [S02].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Shared 114–115 navigation work added common Back navigation and frame-rail experiments. The source groups these versions; it does not provide a complete separate build-114 release note.

### Build 115

**Grouped record with stated per-build detail** — [S02].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The shared 114–115 record specifically attributes attached notched Back rail tabs to build 115 (0.1.114-diag), with a brief pulse, haptic tick and optional UI sound; navigation does not wait for the effect.

### Build 116

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 116 and design-thinking review
>
> Back rail tabs were reduced from 142x48 dp to 96x32 dp, attached at the frame's left corners with no 18 dp inset. Reserved vertical space was reduced from 52 to 34 dp at each edge, reclaiming 36 dp for content. Installed build 116 (0.1.115-diag), with JVM build/tests passing; inspected its placement on the Flip6.
>
> The user asked that the next Builder review focus on flow, UI/UX and design-thinking principles. Treat field use and analysis authoring as distinct tasks to investigate with the operator, not assumed interchangeable roles. Start with user goals and observed friction, define the problem, explore alternatives, prototype a small journey, then test task completion and comprehension.
>
> Directly observed on the Flip6: tapping Use this question with my recording led to Build an analysis, New/Save/Practice, an editable name and tool controls. The Evidence section said no recording was loaded and directed the user away to Tools to import it; it did not provide an import action there. Thus a request to run an existing question currently opens authoring controls and leaves the evidence-acquisition step unresolved in place. This is a concrete flow problem, not an inferred preference about colors or styling. The starting question also consumes most of a screen before its actions, and a single deauth question can make the broader Builder capability hard to discover.
>
> Candidate design hypotheses for the next iteration: separate Use an analysis, Build an analysis and Practice entry paths; resolve evidence selection inside the running flow; present one dominant next action per step; keep advanced wiring optional; preserve drafts and a clear return path. These are hypotheses to prototype and evaluate with the user, not a claim that the UX has been validated with refugees or field operators.

### Build 117

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 117: intent-separated journeys
>
> Design hypothesis: an operator running a ready-made analysis should not encounter tool authoring before choosing evidence. The Builder entry now has three explicit paths: Use an analysis, Build an analysis and Practice. Use offers a preset and saved/included workflows, then loads a recording directly in that journey. The recording step exposes empty/loading/failed/ready/empty-file states, cancellation, retry and Run. Results remain action-first; Back from a result returns to the loaded recording, then to analysis choice. Edit a copy is an explicit transition into authoring.
>
> Build keeps the tool editor, offers recording import in place for testing, and retains its own in-memory draft/undo/redo while the operator uses or practices with another analysis. A temporary capture run can now take an explicit workflow without changing the workflow selected for automatic detection triage; Use for triage remains an explicit action.
>
> Installed build 117 (0.1.116-diag). Android build and 284 JVM tests pass. The existing native Flip6 smoke regression passes. Direct device walkthrough: Home > Use > preset > Choose recording opens Android's picker; cancelling returns to the same selected analysis. Imported only the generated test capture through that picker, yielding six available packets and Run analysis. Running produced the action-first report; device Back returned to the recording's Run step. Edit a copy opened authoring; switching to Use and back to Build restored the unsaved copy. The phone was left on the three-intent entry screen.
>
> A generated test-only capture was copied to Download/TRAINING-LayerScope-build117-review.pcap to test this route; the app recognizes the TRAINING- name and labels the report as practice data. A copy is retained at diagnostic-artifacts/builder117/TRAINING-LayerScope-build117-review.pcap. No other user files were selected during the walkthrough. This verifies the intended task path and recovery behavior; comprehension and preference still need operator feedback on this prototype.

### Build 118

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 118 - blocks first
>
> Builder now opens directly into a Scratch-inspired workspace. Evidence, Wireshark, Timeline, and Report categories expose tap-to-add / hold-to-drag blocks. Canvas blocks use category colors and input/output shoulders, retaining typed ports and connections. New, Load, Save, Run are primary controls; recording, undo, redo, and more are secondary. Saved and included analytics live in Load; included analytics open as editable copies. Settings and extra actions open in framed dialogs. Results open separately and return to blocks. New analytic guidance describes the next action rather than showing an internal wiring error.
>
> Verified on the Flip6: workspace, Load menu, practice analytic load, and Run to action-first report. All 284 JVM tests pass. On-device native smoke passes six packets, three EAPOL, zero malformed, threshold 2/3/2, unrelated packet excluded. Build 118 installed preserving data.
>
> Architecture direction: small deterministic atoms remain responsible for observations and immediate findings. Optional local AI assessment/explanation/translation atoms are future work; no model or AI runtime was installed in this build. Device support and offline performance need measurement before choosing a model.

### Build 119

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 119 - first AI toolkit increment
>
> Implemented product-owned model contracts under controller/ai with no Android or framework types in the request/provider API. Tasks include capture review, explanation, analytic authoring, atom definition, and agent definition. Contracts support structured responses, tool definitions, tool call identities, and tokenizer-derived context budgeting. The coordinator serializes inference, propagates cancellation, applies deadlines, checks returned model identity, and rejects unsupported tasks and undeclared tool calls. The orchestration host remains responsible for tool-argument schema validation and dispatch. The installed provider explicitly reports no product model; no fake inference is shipped.
>
> CaptureEvidence indexes the full available capture before paging and correlates episodes across page boundaries. It validates capture/page identities, duplicate frame references, and supplied coverage. CaptureReviewer exposes the current disconnect checks as an explicitly rule-based baseline. Builder Review / AI opens an action-first recording review, supporting-frame links, optional explanations and recording limits, and a prompt field. Prompt authoring stays unavailable until a compatible bundled model exists. Generated analytic wiring is validated before becoming an editable draft; prompt and model provenance survive save, exchange, and copy. Sharing warns that the authoring prompt is included. Existing GATT and Hunter code was not changed by this increment.
>
> Validation: 500-observation tests cover complete paging, cross-page deauth/EAPOL linkage, unrelated targets, zero-based native references, incomplete coverage, malformed observations, and invalid model evidence. Workflow proposal tests cover invalid/orphan/unknown steps, fractional versions, capture-specific address defaults, provenance round trips, tokenizer limits, cancellation and model timeouts. JSON decoding now limits nesting and rejects duplicate keys, invalid numbers, and raw control characters.
>
> Koog 1.3.0 was evaluated in tools/ai-toolkit-evaluation with an isolated Kotlin 2.3.10 project. An actual Koog graph executes three local fixture nodes over 500 test integers without invoking a model. The main app remains Kotlin 2.0.21. This is a JVM feasibility test, not Android embedding or model-quality evidence. No bespoke agent execution engine was added. Android framework integration remains a separate compatibility gate.
>
> Training: the user authorized existing scenarios and licensed sources. tools/ai-training exports 17 synthetic seed pages from seven scenario families, split as 9 train / 3 validation / 5 test with no family overlap. Source provenance, hashes, and label status are recorded; six Python validation tests and the preparation/training dry run pass. A local-only two-step LoRA smoke-training entry point is provided, but its GPU training path has not run. No training libraries, base weights, or trained product model were installed. The workstation has an RTX 2070 with 8192 MiB VRAM; real memory and speed measurements remain necessary. NIST SP 800-153 is registered as a reference candidate; its text is not ingested.
>
> Flip6 checks: updated build installed preserving data; native practice decoding and analytic thresholds pass; capture review checks all six practice frames and identifies two patterns. The first evidence link opens exactly three supporting frames in Wireshark, and hardware Back returns to Builder.
>
> Manual verification: Builder > Load > Load practice analytic > Review / AI > Review disconnect patterns. Expect two patterns. The first has two disconnect requests and one later network access message; its evidence link shows three frames. Practice remains visibly labeled throughout.

### Build 120

**Individual recorded note** — [S02].

*Recovered source wording (historical):*

> Build 120 - field comparison and embedded AI development path
>
> Network identity and temporal order are now separate report fields. The comparison checks device endpoints and BSSID; identical SSIDs do not establish identity, and different BSSIDs may still belong to one roaming network. EAPOL from the same device/access point before a disconnect is displayed as earlier context, never counted as a later reconnect. Broadcast disconnects can match an access point without establishing an affected client. Missing identifiers and missing traffic remain explicit. Wireshark's decoded SSID text is used instead of raw hexadecimal values.
>
> Triage, Timeline, and Builder recording review display comparison, event order, network name, target, and access-point identifiers before expanded explanations. Supporting frame links retain native zero-based references; a separate comparison link includes contextual earlier or different-access-point EAPOL. AI evidence pages carry the same typed context and validate references against it.
>
> The phone's native decoder rechecked both private field recordings: one contained two EAPOL messages preceding a single deauth for the same observed device/BSSID; the other contained 33 deauth frames and no EAPOL. Neither identifies the gaming network, which the user said was neither observed SSID. These recordings do not establish a cause for the reported ping increase. Original recordings and identifiable incident output remain in ignored local diagnostics and were not added to training.
>
> Android embeds Koog 1.3.0 on Kotlin 2.3.10/AGP 8.13.2. Its graph provides the rule-based baseline immediately, then optionally runs qualified local model page assessments. Product contracts remain framework independent. The prompt executor is local-only, with no remote fallback. Phone instrumentation verifies the actual graph and retained baseline without a qualified model.
>
> Pinned llama.cpp is compiled as an additional embedded CPU library alongside Wireshark. The provider implements exact tokenizer budgeting, the pinned non-thinking Qwen3 template, JSON grammar, serialized inference, cancellation, and SHA-256-verified model extraction. Output schema and evidence validation remain the coordinator's responsibility; generic JSON grammar alone does not enforce the task schema. Unqualified smoke weights are debug-only and rejected by the product coordinator.
>
> The first successful local training smoke run used 17 fixture-derived examples split by scenario family: nine train, three validation, five held out. Two LoRA updates took 17.51 seconds, validation took 4.74 seconds, and peak allocated GPU memory was approximately 1.84 GiB. Merged Q8_0 development weights measure 639,446,880 bytes. This verifies the pipeline, not field accuracy. Reviewed defensive examples, independent evaluations, prompt-to-analytic tests, atom/agent tool execution, and product qualification remain necessary. See `tools/ai-training/README.md` for reproduction.
>
> Validation: 327 Android JVM tests and nine Python tests pass, including loss/gradient equivalence and the RTX 2070 attention-kernel comparison. Native phone practice decoding, rule threshold changes, complete recording review, Koog Android execution, and both private field comparison checks pass.
>
> Flip6 embedded-model benchmark: packaged Q8_0 weights loaded/tokenized in 922 ms, the first 59-token-input JSON probe took 11,643 ms, and a subsequent probe after cancellation took 1,641 ms. Cancellation returned in 104 ms and the provider remained reusable. This is a tiny structured-output probe, not a capture-review latency benchmark. Unoptimized debug kernels previously took 34-42 seconds; only the inference-library targets now use portable `-O3` optimization. The final probe passed without a model service, and the unqualified candidate remained rejected by the production coordinator.

### Build 121

**Gap — no individual change note recovered.** The build-122 checkpoint and first-domain-adapter records give surrounding context but do not establish a separate build-121 change list. No feature, test result or installation claim is manufactured for this entry.

### Build 122

**Version declaration recovered:** `0.1.121`. These are checkpoint declarations, not inferred release dates.

**Checkpoint and qualification record** — [S38].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Checkpoint Builder integration, trained AI candidates and faster phone runtime

Preserve the Android Builder/reporting integration, passive client/BSSID and timeline correlation, navigation and editing work, GATT queue/connection diagnostics, Hunter source changes, and embedded local inference interfaces. Pin the external llama.cpp source and include its fetch helper rather than vendoring its build tree.

Build 122 on the Flip6 retains the first trained Qwen3-4B domain candidate in the explicit debug test screen. Runtime changes reduced two selected responses from about 74 seconds each to 23–29 seconds; a repeat measured 25–30 seconds. Cancellation returned in 116 ms and a subsequent token-count request completed. These measurements do not qualify sustained performance, memory improvement or subsequent full generation.

Preserve the admitted development data, rejected/draft data with their labels, ontology/source provenance, repair and training tools, and exact acceptance outputs with hash-bound semantic judgments. The first candidate completed 1,090 updates. Its correction continuation completed another 682 updates with no skips and finite saved weights. Both passed only 2/12 frozen acceptance cases. The correction improved BLE address-type explanation but regressed captured-instruction explanation, and still missed required Wi-Fi identity/timing findings. It was not packaged or installed. Neither candidate is operationally qualified; ordinary product assessments exclude them.

Validation: 168 Python tests completed, one skipped; 364 Android unit tests passed with no failures or errors; the Android validation build succeeded. Two real-model phone cases and cancellation recovery were measured. Hunter changes are preserved as a development checkpoint; this commit makes no new firmware field-validation claim.

Model weights, private field captures, local build outputs and unrelated research artifacts are excluded. Two synthetic-observation JSONL files exceed GitHub's regular size limit and are stored through Git LFS without changing their bytes. Clones need Git LFS to materialize those data files. The remaining training/evaluation evidence stays in regular Git.

The owner explicitly authorized this commit and normal push in chat. Earlier statements reserving commits to the owner describe the preceding checkpoint workflow. No acceptance thresholds were relaxed, and no acceptance outputs were used as optimizer targets.

See CURRENT-AI-STATUS.md for current limitations, exact evidence locations and the proposed next controlled experiment.

### Build 123

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

A retained local log is named field-build123-validation.log. No complete per-build feature note or standalone release claim was recovered. Do not backdate all build-124 changes to this number.

**Evidence limit:** part of this account comes from a retained local build/bench trace. The linked public status/contract supplies surrounding context; it does not independently certify every local measurement or name this exact build. See the local audit ledger.

### Build 124

**Individual build document** — [S39].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

**Field development build 124**

Hunter and Vixen are the operating pair; T-Ultra independently verifies common
observations. This build prepares an auditable field experiment. It does not
qualify the existing language model, establish RF agreement, or certify Play
readiness.

**What changed**

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

**Experimental model**

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

**Bench sequence**

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

### Build 125

**Development notes plus retained build receipt** — [S40].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Field explanation compatibility correction supplied the complete schema required by the compact prompt. Subsequent answers still confused classification and identity and omitted supplied report values. The retained build-validation receipt records 390 passing tests per variant and unchanged unqualified weights.

### Build 126

**Individual recorded note** — [S40].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Shorter example-guided event explanation, 256 output tokens, and checks for the supplied detector, count and confidence. Invalid answers retain their raw output and are withheld, not repaired into semantic passes. The retained build receipt records 391 passing tests per variant; no weight promotion.

### Build 127

**Individual recorded note** — [S40].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Packet explanation requires the verified count sentence and rejects invented numbers or changed count roles after an answer reported 66 saved packets instead of 64. Both variants passed 392 tests. A private nine-case rerun is limited evidence, not general qualification.

### Build 128

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Runtime prefix-cache experiment. A retained phone cache-check record tested identical output, changed input, cancellation and recovery. It is an unqualified development experiment; cache speed does not establish semantic correctness.

**Evidence limit:** part of this account comes from a retained local build/bench trace. The linked public status/contract supplies surrounding context; it does not independently certify every local measurement or name this exact build. See the local audit ledger.

### Build 129

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Vulkan/GPU diagnostic experiments and repeated build attempts. Retained phone GPU-check logs record failure; no successful portable-field or general model qualification is inferred.

**Evidence limit:** part of this account comes from a retained local build/bench trace. The linked public status/contract supplies surrounding context; it does not independently certify every local measurement or name this exact build. See the local audit ledger.

### Build 130

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

ARM i8mm runtime experiment. Retained phone instrumentation records a cache/cancellation/recovery pass. This is a hardware experiment, not evidence of accuracy across phones. The subsequent MVP restores portable defaults.

**Evidence limit:** part of this account comes from a retained local build/bench trace. The linked public status/contract supplies surrounding context; it does not independently certify every local measurement or name this exact build. See the local audit ledger.

### Build 131

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

LiveEvidenceSlices integration preserves receipt-clock pre/post-trigger context and attaches available packet windows, with producer/session isolation, partial closure and bounded storage. It invokes no AI and does not treat a signature name as an observed drone.

### Build 132

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Controlled positive policy replay omitted the explicit classification and measured gap. This was a component failure, not a normal field-workflow success.

### Build 133

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The model incorrectly claimed that 30,000 ms exceeded 90,000 ms. Failure retained; arithmetic and policy comparison were moved into procedural code rather than accepting the prose.

### Build 134

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The first controlled answer named the intended policy result and evidence/timing relationship, but broke JSON syntax at references. It was not a complete pass.

### Build 135

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

A task-specific structured-output grammar supported two complete component replay passes on the phone: 125.184 and 119.835 seconds. These were controlled preservation/correlation/explanation replays, not normal live sensing/notification proof; the times also exceeded the illustrative 30-second assessment goal.

### Build 136

**Retrospective synthesis from identified records** — [S37].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

A separate harness modal/direct development-model path was exercised. This established transport/components only; it did not prove the normal event list, alerts and qualified coordinator. The later field-fidelity correction explicitly withdraws a normal-workflow interpretation of that demonstration.

### Build 137

**Retrospective synthesis from identified records** — [S42].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Field-fidelity correction removed the live-test modal/control and required a common ordinary field path. The retained build logs identify 137; no ordinary-field MVP pass is credited to this correction alone.

**Evidence limit:** part of this account comes from a retained local build/bench trace. The linked public status/contract supplies surrounding context; it does not independently certify every local measurement or name this exact build. See the local audit ledger.

### Build 138

**Version declaration recovered:** `0.1.138-beta.1`. These are checkpoint declarations, not inferred release dates.

**Retrospective synthesis from identified records** — [S43].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

MVP beta preparation added passive OKR/KPI instrumentation and delivery preparation. It did not itself qualify the old 4B model or establish a normal-field warning. A version declaration/build is not proof that a public GitHub release was published.

### Build 139

**Version declaration recovered:** `0.1.139-beta.1`. These are checkpoint declarations, not inferred release dates.

**Documented implementation and verification** — [S42].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Removed the remaining practice, experimental-explanation, benchmark and diagnostic test interfaces from the application; acceptance answers belong to the separate test APK. Saved data was retained during the update. This app-cleanup milestone did not yet close the normal automatic-analysis/model-admission gap.

### Build 140

**Version declaration recovered:** `0.1.140-beta.1`. These are checkpoint declarations, not inferred release dates.

**Documented implementation and verification** — [S41].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Introduced the ordinary foreground-service bounded signature workflow and a separately qualified 0.6B Q8 model. The compact interpretation contract passed 30/30 validation cases, 50/50 held-out host cases, 50/50 native phone cases and 53/53 through the production coordinator including actual-selector inputs. The task changed and the data are authored synthetic references, so these numbers are not directly comparable to earlier broad prose/4B acceptance scores.

**Operator-run result, with its limits** — [S44].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The first operator attempt had Remote ID disabled: deauth and EAPOL arrived, no model report was triggered, and waiting beyond 120 seconds was not an inference timeout. After the operator enabled Remote ID and ran again, Hunter delivered 8 deauth, 4 EAPOL and 4 Remote ID sightings; the model selected disconnect_sequence_then_remote_id with a 30-second source-clock gap. The report completed 5.645 seconds after Remote ID receipt, Android accepted the notification, and monitoring continued. Forty-eight deauth packet payloads and their hash were verified, but the EAPOL packet window was missing. Interpretation passed; complete evidence parity did not. Independent source-SD readback and real RF/outage/drone intent were not established. The public comparison receipt covers the completed second attempt. The first-attempt account is supported by the retained private journal and the read-only sensor-selection inspection in the operator conversation; its journal hash is listed in the local audit ledger.

### Build 141

**Version declaration recovered:** `0.1.141-beta.1`. These are checkpoint declarations, not inferred release dates.

**Documented implementation and verification** — [S41].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

The normal packet-preservation filter was extended from Wi-Fi deauth alone to Wi-Fi deauth and EAPOL. Both variants passed 427 tests; build 141 was installed with the model and harness firmware unchanged. The prior completed report was retained. A new operator baseline run is still required before claiming the EAPOL packet-transfer repair passed physically.

**Explicit uncompleted field-verification gate** — [S45].

*Editorial synthesis of the cited evidence; not a verbatim quotation:*

Repair receipt: physicalEapolTransferVerified=false; baselineRerunStarted=false. This is an installed, automatically tested fix, not a retroactive pass for the original capture.


**Later operator verification (separate from the original receipt):** the repaired baseline on 141 passed phone-side preservation: 48 deauth and 24 complete EAPOL-Key frames, verified PCAP hashes, the correct 30-second sequence, a posted notification and continued monitoring. The report completed 5,778 ms after first drone-summary receipt. T1 then passed on 141 with irregular timing and duplicate summaries: counts 8/4/4, the same 48/24 packet windows, a 62-second gap, one accepted analysis/notification and a 2,500 ms report delay. Private receipts: `baseline-141/verification.json` and `t1-operator/verdict.json` in the local audit ledger; public account [S56]. These do not establish independent SD parity or real RF detection.

### Build 142

**Source checkpoint and local installation receipt** — [S46], with later wording refinements retained in Git commit `4f18dbb3a45c3ec4610aa98a473f967c41f0a705`.

Added visible report completion times, stable IDs and Hunter sessions, with saved-report history, Archive/Restore and JSON Export. Archive markers remain separate from original inference records. Drone labels and report prose were simplified; the generic repeated warning paragraph was removed from displayed report wording while original saved JSON remained unchanged. Build 142 was installed; three existing reports were checked byte-for-byte. Both variants passed 429 unit tests. Unrelated release work was excluded from commits. This was a normal application feature, not a test-specific interface.

### Build 143

**Source checkpoint and local installation receipt** — [S47].

OPS was reduced to the current assessment with Archive confirmation. A separate AI Reports page gained Active/Archived history. Archiving the newest report no longer promotes an older report into OPS, including after restart. Future assessments retain app version, prompt/schema/grammar, output budget/timeout and richer source facts for later analysis. Model input and inference settings stayed unchanged. Both variants passed 430 tests; installation verification checked 39 existing report/packet files unchanged. No new physical AI pass is assigned to this UI build.

### Build 144

**Source checkpoint and local installation receipt** — [S48].

AI report panels adopted the existing dark instrument housings, cyan headings and recessed actions. Back controls were moved inward and enclosed. Both variants' checks passed; build 144 was installed and 39 original report/packet files matched their earlier hashes. The operator then rejected the redundant outer frame and wasted space. This is a deployed iteration followed by a correction, not the final approved frame design.

### Build 145

**Source checkpoint and retained local build log** — [S49].

Removed the additional route housing and its upper/lower control bands. Back moved into existing content headers; AI Reports used one housing with divided history entries. The build/test run succeeded and installation was observed during this development session. No separate retained installation receipt is asserted for 145; the local build log proves compilation/checks, not installation by itself. The operator said the layout looked better, then requested a small framed Back area.

### Build 146

**Source checkpoint and retained local build log** — [S50].

Wrapped Back in a miniature housing using the content border's artwork. Checks passed and installation was observed in the session; the retained build log is not an independent installation receipt. The operator clarified that Back must be part of the border, rather than a separate box. This approach was superseded.

### Build 147

**Source checkpoint and retained local build log** — [S51].

Replaced the small housing with a flat drawn return joined to the upper-left rails. The app built and was installed in the session. A phone screenshot then showed why the result still failed visually: the flat overlay obscured corner detail and did not share the frame's bevel, texture or lighting. The operator agreed with that diagnosis. This failed visual approach is preserved as an iteration, not described as the approved design.

### Build 148

**Grouped source evidence; no standalone build-148 source checkpoint recovered** — [S52].

The upper-left Back return was rebuilt from the housing's original textured glass and rail sections, retaining the existing top/left rails and adding matching right/lower sections. The local build log records successful checks; deployment as versionCode 148 was observed in the session. The phone was at its lock screen during the observer's final screenshot attempt, so no successful post-deployment visual inspection is claimed from that screenshot. The operator subsequently approved the result and explicitly made it the standard for every frame with Back. These changes were retained with build 149 in commit `3c865116c88bd779c60ab19cdf74018be0804c0b`; a separate 148 commit/APK archive is not asserted.

### Build 149

**Source checkpoint, installation receipt and failed operator Test 2** — [S52], [S53], [S56].

Applied the approved textured border-owned Back design to remaining analytic pages, Builder menus/results and confirmations; removed the standalone Back renderer and duplicate lower controls. Both variants passed 430 tests. Installation verification recorded 40 report/archive/packet files unchanged. The design standard was documented and the operator later authorized commit/push.

**Test 2 failed on frozen Vixen 149.** The harness added unrelated disconnects on B and handshakes on C while retaining A's valid disconnect/handshake sequence and the drone observation 62 seconds after A's first disconnect. Vixen received all five events with counts **8/4/8/4/4**. Four packet windows **48/24/48/24** (144 frames total) were saved, hashes verified, and their A/A/B/C source identities checked. Monitoring continued, but no AI invocation/report was recorded after the reporting deadline.

The procedural selector chose the newest disconnect (B), found no B handshake and stopped before considering A's valid pair. This was a **failure before model inference**, not an observed wrong model answer. The failed run's journal, packet captures and comparison receipt remain separate and private; their local audit hashes are listed below. It is not retroactively passed by build 150.

### Build 150

**Selection repair, regression checks, installation receipt and successful operator rerun** — [S54], [S55], [S56].

Changed candidate selection to rank complete disconnect/handshake pairs on the same network within one producer/session. A newer unmatched network no longer hides a complete older pair. Negative order and outside-window evidence are still eligible; selection does not filter for the expected positive answer. The model weights, prompt, grammar and harness scenario were unchanged.

Regression cases derived from the failed run were first run against the previous selector: **3 of 10 selection tests failed**. After repair, both application variants passed **434 tests**, with no failures/errors/skips. The app was installed and 54 pre-existing report/archive/packet files were checked unchanged. This automatic verification qualified selection logic; it was not itself an end-to-end model pass.

The first attempted run on 150 stopped about 3.4 seconds after Start, before the first 20-second fixture event. The stop's cause was not established. It is an **aborted attempt**, not a model failure or a successful test.

The operator restarted. Read-only observation verified the subsequent complete T2 run: counts **8/4/8/4/4**, correct selection of A's events **1/2** and drone event **5**, a **62,000 ms** source-clock gap, and the raw model pattern `disconnect_sequence_then_remote_id`. The report completed **7,509 ms** after the first drone summary reached Vixen, within the 30-second target. One accepted analysis and one posted notification were recorded despite duplicate summaries. Monitoring continued. Four closed packet windows **48/24/48/24** matched their saved SHA-256 hashes and A/A/B/C source identities; both handshake windows contained complete 95-byte EAPOL-Key bodies. Original drone service data, prepared input and raw output were preserved. The observer read saved records without operating either device during this complete run.

**Scope and remaining gap:** T2 passed its correlation, preservation, notification, duplicate-handling and timing criteria. The report still provides **no action recommendation**, which the operator identified after the rerun. The discussed recommendation was not implemented or qualified. Therefore this is **not acceptance of the complete actionable MVP report**. Independent source-SD parity, physical RF detection, evacuation effectiveness and T-Ultra corroboration were not established by this scenario. The original frozen-149 failure remains a separate result.

## Grouped original checkpoints

These original commit accounts are preserved together so that batch-level details are available without assigning them to unsupported individual version numbers. The quoted validation claims retain their original scope and are not newly reproduced tests.

### Checkpoint 54-63

Source [S30].

> Watch controller builds 54 to 63: press feedback, connection reasons, 3D face, salvaged hull console
>
> Build 54: LINK MEASURE test panel for Link 0.2 Phase 0. Measured 2026-10-30 at about 1 m: PING x100 lost 0, round trip median 112 ms, p95 129 ms.
>
> CONNECTION panel now says why a scan stopped (Bluetooth off, Location off, no LayerWand found, scan error) and what to tap next. With Bluetooth off it used to stay blank. ConnNotice is pure and covered by 9 unit tests. Every button flashes while pressed and gives one 20 ms buzz when the press registers; a disabled button stays dark and silent. VIBRATE permission added.
>
> Face: one light from the upper left for every part, the rim shaded as a raised ring, the dial recessed, a curved crystal, and a thick puck side wall with a contact shadow so it sits on the console. The light and shadow are separate layers that slide but do not turn, so the face rolls under a fixed light. One roll sound both ways; the roll-out sound file is removed.
>
> Salvaged hull: the console and the plate behind the face are one lit height map, rendered once per screen size off the main thread (1,008 ms on the LOKMAT) and cached. Ribbing, invented glyphs, a spine, scorch, a gear rack (teeth 13.2 dp), and roll scratches traced from the real roll path, cut only into the high surfaces. Straps riveted through the cover glass; every crack is clipped to the glass.
>
> Tried and reverted in builds 59 to 62: a haul cable in a groove ground into the bezel, then in the side wall, with a welded tab. At a visible size the groove buried the riveted rim, which is the look. FaceView.kt and FaceLight.kt are restored byte for byte to their build 58 versions. Version 0.1.62, build 63.

### Checkpoint 64-67

Source [S31].

> Vixen fox hunt game
>
> Vixen is a fox hunt. One player hides the fox, a beacon. The hunter
> finds it with a LayerWand and the LayerTime watch. This adds the HUNT
> slice of Link 0.2 (ops 0x08 HUNT_START, 0x09 HUNT_STOP, 0x0A GET_HUNT,
> reply 0x8A HUNT_READING, capability bit 6, Status flag bit 6) and the
> fox firmware.
>
> The fox: a new tdongle_c5_vixen environment in the LayerWand
> platformio.ini, not its own device folder, because it is the same
> T-Dongle-C5 board and would otherwise copy every board file. It only
> advertises over Bluetooth every 100 ms at fixed power, with the service
> UUID 8ac60010-a08b-4851-b89f-e6b39081268e. That UUID is in the LayerTime
> family but is not the link service, so watches never try to connect to
> it and LayerWands never count it as a node. The screen and LED stay dark
> so it can be hidden; a short BOOT press shows its status for 10 seconds.
>
> LayerWand: core LinkHunt holds the codec and HuntTracker, host tested in
> test_link_hunt. HUNT_START is applied on the loop task, never inside a
> Bluetooth callback. It stops the Recon scheduler and scans BLE
> continuously with duplicate filtering off, so every advertisement counts
> as a reading. HUNT_STOP restores whatever Recon mode was running before.
> Target kind 0xF0 means any Vixen fox, matched by service UUID. Hunting a
> real detection by address waits for the Bluetooth byte-order fix.
>
> Watch builds 64 to 67: full-screen hunt radar with a step trail colored
> by signal, a range band with no fake direction (the watch has no
> compass), the status dot, closing or opening, a tone that rises with
> signal, FOUND after the smoothed signal holds above threshold for 2 s,
> and TARGET LOST after 5 s of silence. GET_HUNT polls every 220 ms, one
> request at a time. Build 67 counts steps from the accelerometer, because
> the LOKMAT step sensor never reported a step even with
> ACTIVITY_RECOGNITION granted.

### Checkpoint 68-69

Source [S32].

> LayerScope: new name, Hunter naming, hunt screen, and readability
>
> Builds 68 and 69 (0.1.67 and 0.1.68). Both touched the same files before
> either was committed, so they ship as one change.
>
> The app is LayerScope. The package ID changed from
> com.projectrevivesolutions.layertime.controller to
> com.projectrevivesolutions.layerscope now, before any store upload,
> because a store package ID can never change afterward. The cost is a
> one-time reinstall on existing devices: permissions and the green-button
> Accessibility service have to be turned on again. Internal Kotlin package
> and class names stay as they were; nobody sees them, and renaming them
> would touch every file for no visible change.
>
> The owl is the Layers and PRS logo, so LayerScope carries its own mark.
> The launcher icon is the aperture traced to vector paths so it stays
> sharp at every size, and the face shows the same mark rendered from it.
>
> One launcher icon. The Wireshark diag screen had its own LAUNCHER filter
> from the decoder spike, so installs showed two identical owls (labels
> truncate to the same text). The screen stays for development and opens
> through adb.
>
> LayerWand is now the Hunter in every user-visible string, including
> lowercase "wand" in event detail and the Location message. The Bluetooth
> name LT-C5-xxxx stays, because pairing matches on it. HUNT LAYERVIXEN is
> now HUNT VIXEN.
>
> Readability from field testing: the smallest text (8 to 11 sp) was hard
> to read, so the minimum is now 12 sp and every size moved up one step.
> DETECTION EVENTS was never inside a HUD panel, so it sat on bare
> background; it now has the same blue panel as every other section.
>
> Hunt screen: a fighter-jet radar scope bolted onto the same salvaged ship
> console as the main screen, mocked up and approved first. The scope
> overlaps the console frame, casts its shadow on it, and is strapped on;
> straps stop on the frame band so nothing covers a reading. Readings are
> 16 sp labels and 22 sp values in a HUD panel and follow TEXT SIZE; the
> old side column was 12 sp and ignored it. The static scope art and the
> hull are drawn once per screen size off the main thread; only the radar
> redraws with the sweep. HullRelief takes faceTrack=false so this screen
> gets no gear rack or roll scratches.
>
> Reconnect race: a SCAN & CONNECT tap during a reconnect wait left the
> scheduled retry in place, and when it fired it closed the connection the
> tap had just started. start() now cancels the pending retry, stops any
> running scan, and drops a half-open connection first, and a "scan
> already running" error is no longer reported as a failure. Logs showed
> the green side button never triggered reconnects; its service was simply
> off after the package ID change.

### Checkpoint 74-89

Source [S33].

> LayerScope builds 74 to 89 and the Hunter evidence store
>
> One commit for this batch by choice: builds 74 to 89 changed the same
> files (MainActivity.kt, LinkClient.kt, ControllerRepository.kt, C5App.cpp,
> LinkServer.cpp, build.gradle.kts) build after build, so a per-build split
> would have needed hunk staging and could leave commits that do not
> compile. Build 89, which carries all of it, passed its device test
> (Hunter and phone) before this commit.
>
> Sensors pointer: sensors/ moves to LayerTime-Sensors 1.1.2 (AirTag grading,
> EAPOL detector). The firmware no longer needs LAYERTIME_SENSORS_UNRECORDED.
>
> Core and contracts:
> - ReconTarget Eapol = 18 (17 is EarlyWarning in core; DetectorId stays 17).
>   LinkServer ReconStart accepts All..GoogleTag or Eapol and still rejects
>   None and EarlyWarning. enums.json and display names follow.
> - MonitorEventLog eviction and the scheduler interest hold, from the
>   2026-11-01 field test where a busy MAC was evicted and re-created with a
>   new event id, flooding rows and alerts.
> - SetSensors (CommandType 15, capability 0x0080): a u32 allow-list, one bit
>   per ReconTarget, enforced in LayerTimeCore::candidateThunk so Mission
>   can turn off, for example, every Apple tracker. kAllSensorsMask 0x5FFE0.
> - EvidenceStore: a 256-frame ring plus 32 windows of 64 frames in a
>   platform pool (about 2.3 MB of PSRAM on the C5). A window keeps the
>   frames behind one Wi-Fi detection: its transmitter, its BSSID, stations
>   seen talking to that BSSID, and SSID clones, with beacons thinned.
> - LinkEvidence: GET_FRAMES / FRAME_WINDOW / FRAME_INDEX and
>   GET_FRAME_BYTES / FRAME_BYTES (capability 0x0020). GET_FRAME_BYTES
>   streams to the end of the window in one request because a request per
>   frame at the measured 60 ms round trip was about ten times slower.
>   FRAME_INDEX is 14 bytes; a 15/14 mismatch between the C++ and Kotlin
>   sides was caught by the Kotlin test and pinned on both.
> - ReconScheduler.holdChannel keeps the longer of two holds, so a 1.5 s
>   interest hold never cuts the 10 s hold after a deauth detection.
> - New suites test_evidence_store and test_link_evidence; selection,
>   scheduler, core, link server, and core model suites extended.
>   test_sensor_equivalence still fails on its stale pre-extraction golden;
>   regenerating it is its own task.
>
> Hunter (LayerWand T-Dongle-C5):
> - Every Wi-Fi frame goes to the evidence tap before classification, with
>   the trailing 4 bytes dropped: Link 0.2 Phase 0 found 0 of 1,989 frames
>   carried a valid FCS there.
> - Wi-Fi detections open an evidence window; deauth also holds the channel
>   10 s so the window hears the follow-on.
> - Evidence ops are deferred to the loop like hunt ops. Without that the
>   BLE callback answered them UnknownOp and the phone reported firmware
>   that does not keep frames.
> - MTU tracking moved out of the measure-only build, where it was hiding a
>   compile error in the normal build.
> - Sensor allow-list stored in NVS; the Hunter now boots stopped.
>
> LayerScope (Android), builds 74 to 89:
> - Event rows grouped per MAC across detectors, one alert per row per
>   session (EventGrouper, AlertSelector).
> - Adaptive layout from the Material width classes, with the watch layout
>   pinned by test; OPS, EVENTS, TOOLS, BUILDER, MISSION, SYSTEM tabs; the
>   Wireshark window and the attack timeline in TOOLS; the spacecraft
>   visual design and its section 1 assets.
> - Mission: sensors grouped by type, presets that check their members,
>   wired to the Hunter through SetSensors. OPS QUICK START: Select Sensors
>   (All Sensors, Only Deauth), Start and Stop follow the Hunter, a scan
>   mode badge, and the crosshair scan light. Hunt Vixen is its own sensor.
>   Field status Active sensor reads the selection, not the scan step.
> - Analytic pipeline slice 1: a JSON workflow file with a validator, the
>   deauth triage steps with a deterministic verdict, fabricated radiotap
>   fixtures with expected verdicts, and a read-only Builder view. A flood
>   on a network that requires management frame protection is Likely
>   Attack, with a note that it most likely failed.
> - Slice 2: PULL THE HUNTER'S FRAMES fetches a detection's evidence window
>   at MTU 247, writes a pcap, decodes it with Wireshark, and runs triage.
> - Wireshark startup now follows tshark's order: error reporter, process
>   policies, configuration_init, wtap_init, epan_init, epan_load_settings,
>   prefs_apply_all, with config and data folders in the app cache. Before
>   this, settings were never loaded, so the 802.11 decryption context was
>   never created and the first real EAPOL key frame crashed in
>   g_hash_table_lookup on a NULL table. Calling prefs_apply_all alone was
>   tried first and did nothing: it only runs apply callbacks for changed
>   preferences. Reproduced and confirmed on desktop Wireshark before the
>   device test.
> - Release signing template and the play flavor manifest.
>
> docs/vixen: the Hunter and Vixen web flasher with its firmware images.

### Checkpoint 90-92

Source [S34].

> Hunter 93 and LayerScope 92: field analysis from the Hunter's card
>
> One commit for this batch, as with the last one: Hunter firmware 93 and
> LayerScope builds 90 to 92 changed the same files (C5App.cpp, LinkClient.kt,
> MainActivity.kt) step after step. All of it passed its device test on
> Hunter-C5-001 and the phone before this commit. The goal: analysis in the
> field with only the Hunter and the phone, no computer.
>
> Evidence hold (Mission):
> - COMMAND SetEvidenceHold = 16 (seconds 0 to 30, capability bit 8 0x0100),
>   Settings.evidenceHoldSec, saved in the Hunter's NVS. Mission gets EVIDENCE
>   HOLD buttons Off, 5, 10, 20, and 30 s with a status sentence.
> - Before this a sustained flood never re-held the channel: MonitorEventLog
>   only told its recorder about new records. EventLog gains repeated()
>   (default does nothing), called after a record updates. The Hunter re-holds
>   on a repeat deauth and pushes the evidence window out, so it stays on a
>   flood and resumes sweeping when it stops.
> - A window collects for the hold but never less than 10 s, because collecting
>   costs no radio time; only the channel hold costs the sweep.
>
> Wireshark window:
> - FrameGlossary explains every 802.11 type and subtype Wireshark 4.4.19 names,
>   and the four WPA handshake messages, above each frame card.
>
> SD card evidence:
> - EvidencePcap writes a closed window as a pcap (radiotap identical to
>   LayerScope's, times since boot) to ev_NNNN/event_EEEEE.pcap beside
>   layerwand_NNNN.log. The loop copies the window under the evidence lock and
>   writes the card without it, so the Wi-Fi task never waits on the card. A
>   window revived by a repeat is saved again if it heard more.
> - The log gains repeat lines (at most once per 10 s per event) and evidence
>   lines (path and counts).
> - The card takes one open file (SD.begin max_files 1), so the log is closed
>   around each save and each read and reopened to append.
>
> Card pull:
> - Over USB (bench): "card list" and "card get <name>" on the serial port,
>   base64 lines with a CRC-32. The ESP32-C5 has only a USB serial port, so it
>   cannot appear as a USB drive.
> - Over the Link (field): CARD_LIST 0x0B and CARD_READ 0x0C, replies 0x8B to
>   0x8D, capability bit 9 0x0200. Files are codes, never text, so only the
>   Hunter's own files can be named. CARD_DONE carries the CRC-32 of the whole
>   file, so a resumed pull is checked end to end. The Hunter streams 8 frames
>   per loop pass so the loop never stalls on a large log. LayerScope's HUNTER
>   CARD screen has PULL FROM THE HUNTER, downloads only new or changed files,
>   and opens a detection's frames in the Wireshark window.
>
> Unit names:
> - Every unit says what it is: Hunter-C5-001, Vixen-C5-001. The chip comes from
>   the hardware; the number is given when the unit is flashed (unit set over USB)
>   and kept in NVS. A unit with no number shows its last two address bytes.
>   Replaces LT-C5-xxxx and LT-VIXEN-xxxx.
>
> Tests: new test_evidence_pcap and test_link_card; evidence store, event log,
> scheduler, core, link server, core model, and bring-up suites extended; Kotlin
> EvidenceHoldTest, FrameGlossaryTest, HunterCardLogTest, LinkCardTest. A C++ to
> Kotlin seam check moved a 70 KB file byte for byte. test_sensor_equivalence
> still fails on its stale golden, as before.
>
> Not built: the capture summary paragraph above the Wireshark list.

## Source ledger

The hashes below are SHA-256 of the exact Git blob bytes, or the commit-message output used for a commit source. They are not APK hashes or claims that a test was independently repeated. Git commit identities and original notes remain the primary provenance.

| Source | Original evidence | Content SHA-256 |
| --- | --- | --- |
| [S01] | Vixen `11520899b644` — Original Android decoder/UI build notebook | `1387a1e3552fff995049a964a59f755c14023e49be51297616bad44d9e6ece02` |
| [S02] | Vixen `11520899b644` — Builder behavior and UI verification notebook | `52ea35aec1fa3d5430f5b967991bbf15d4bd202714f5d37ec4530b1dbf4d6191` |
| [S03] | LayerTime `6b14f8d5b640` — Version declaration and inline build notes | `938a2330fbb6ca8e98026ffc9d09e35fb58c77f31df2f46dd4b370ba23ecc113` |
| [S04] | LayerTime `a05ecbe356a7` — Version declaration and inline build notes | `011dcb8d1cea9fe7a965c158c6d31c0db14ceebf9b61a906ad9d01ddf85f6642` |
| [S05] | LayerTime `a6639ad518f2` — Version declaration and inline build notes | `30287415b702e4055d728edf29d28db807419fe96b1bbc7dbc337b3ba3d87688` |
| [S06] | LayerTime `bf43f41207e9` — Version declaration and inline build notes | `ad16ddad0620f27591918f7f81efdf92a8b2e937028d38b667922dda52e89947` |
| [S07] | LayerTime `5943c3a44d2c` — Version declaration and inline build notes | `024020e6b666786f603c813510cbf2564e1b3de2da86979076cf5f6628f1b8aa` |
| [S08] | LayerTime `92525c912109` — Version declaration and inline build notes | `18f33d0d76bea0c5726abeb54ab860048228943292c3f6ace6ca99323e24a8b4` |
| [S09] | LayerTime `9d0ade450adf` — Version declaration and inline build notes | `3dee47a9a8d2d04d6c01410a95814c650e142a4f914006f5e96a4926d9f6d957` |
| [S10] | LayerTime `4700de16e16b` — Version declaration and inline build notes | `f295f33291d4370021f0f8328d292d006df0cbfd42c41802a9d83cb7a9f80278` |
| [S11] | LayerTime `5949abf200a0` — Version declaration and inline build notes | `bc6d88b71eac352adcb66ffc02ec2713a51f84761c89e096168a666ee2124ae8` |
| [S12] | LayerTime `7ac2f1648c06` — Version declaration and inline build notes | `51df4c866e9a659260ceceb21e01ae6da9a8f5ab678e86d8553bcc21a05f58d3` |
| [S13] | LayerTime `d0932f258e01` — Version declaration and inline build notes | `5973d9bf7ae5ed0eea7d7e059eefbbab5f25d12faf414afb75cd188c13258f3a` |
| [S14] | LayerTime `398f9af7909b` — Version declaration and inline build notes | `3210d75a301a083177594ac9bb6d795325af0675754736a67018ba78a743247a` |
| [S15] | LayerTime `e97a7b9bb972` — Version declaration and inline build notes | `45d1931d4eb151a1c328aabd43ad963a8a0955877461ceb0f6a6cbd399407187` |
| [S16] | LayerTime `b30a3f732c8d` — Version declaration and inline build notes | `ed5c3ee2f6c3dee26db0d8b57f2fad27c0eff7f91883eba32c0b4e9dbcd6b095` |
| [S17] | LayerTime `cb1582daecdf` — Version declaration and inline build notes | `6b73c4f8ae48d9adee48ea119b201421ba3c991fe1da2919b8d48dfa253ed109` |
| [S18] | LayerTime `7aec804eaea2` — Version declaration and inline build notes | `260551bcb1ecd242cc6e08d1afa1dcd1efc4aa0f887823cd16789cfd9dbf936b` |
| [S19] | Vixen `e2e8566e9bde` — Version declaration and inline build notes | `260551bcb1ecd242cc6e08d1afa1dcd1efc4aa0f887823cd16789cfd9dbf936b` |
| [S20] | Vixen `f40288d918ff` — Version declaration and inline build notes | `221847dab01ddad81ea66823bd72ebb0dd0df01ac5fa482b413c11fd43563ac0` |
| [S21] | Vixen `10e642a6c687` — Version declaration and inline build notes | `32d8c7de71f5820bcc232ac239732d52ce0234c165d505d2d70509d3bcfcb157` |
| [S22] | Vixen `8ad37a1bdc6e` — Version declaration and inline build notes | `6a214951610c857d55425f5c57ba1b4734bf95b6b83a0df4df61da1a374e30f5` |
| [S23] | Vixen `11520899b644` — Version declaration and inline build notes | `d9d312a65bfe9ccb444ae8fa4e889eea5536ad3dd3bb61f98468c7734200f0ad` |
| [S24] | Vixen `1d9ee3194c4b` — Version declaration and inline build notes | `ea6cacc0ffd330d04370844460637092e50781bfd99fc0ba66f9493453a1cb7d` |
| [S25] | LayerTime `a6639ad518f2` — commit message | `4c2ea9cf836118ba455121d34943f9ce8c97099fc5b568a467696b0252f0114d` |
| [S26] | LayerTime `5943c3a44d2c` — commit message | `7ab98f8e6da9b305a28a6a47b501496b78012597022721eb7a32059eb7bdadee` |
| [S27] | LayerTime `d0932f258e01` — commit message | `a43271d2ac465d3d7119c4fb9322cf706e7a9114cccdf78215e9c917e014e609` |
| [S28] | LayerTime `7ac2f1648c06` — commit message | `41100f55db16cc9d29e9d5cd2ce8210f03bc16bf12d9d98329f7ad3e13daec47` |
| [S29] | LayerTime `4700de16e16b` — commit message | `f24e9b21cf76f2e73f809c78c6099b84a2c743b4ec6e02de46067c267ec4eee3` |
| [S30] | LayerTime `92525c912109` — commit message | `5d96ddbe084b06e9a325d98179fbc4090c9bec6c3bfb0f9613c1163b143c08ca` |
| [S31] | LayerTime `9d0ade450adf` — commit message | `05f1d637129672fc3b54372e841ab44a9929cb11de0459ffba00524a0db6688b` |
| [S32] | LayerTime `5949abf200a0` — commit message | `10f48b49d6ba647fa3b308f251ec04a619dba3eebdd11d676df523246e76bad2` |
| [S33] | LayerTime `e97a7b9bb972` — commit message | `3a16caae31932c4bc3e0a016a7a92185f7bfe596e4153d691f97ff758c47d4e0` |
| [S34] | LayerTime `b30a3f732c8d` — commit message | `b8182c0cb9d1af6da107663e04484316f92054bc29047164517533edaaa36e59` |
| [S35] | LayerTime `a05ecbe356a7` — Initial Android README retained in the first Android checkpoint | `c6c576eb4133dd1af093a6ef12bac61a0e6871880127dd0e16c0469b14f2d44a` |
| [S36] | Vixen `11520899b644` — Tracker-default migration note | `492c99e9f9cfaef9eba0346ce268290ccc66996366f9c1c6548cb91d1c059085` |
| [S37] | Vixen `11520899b644` — AI status and failure record at this checkpoint | `a778e8a9b6a0b7812a7f62071d7d1682306b336ca858b86e948d1a76f2108153` |
| [S38] | Vixen `11520899b644` — Build 122 check-in and training/acceptance evidence | `8117a48d31a022b386085b0a98433dd9ea5532f062848ef349a4636af5b45c35` |
| [S39] | Vixen `11520899b644` — Field build 124 notes | `15990d28958546481717c387e2e6259ac873be011968f9a211a4537e4b94c5a7` |
| [S40] | Vixen `11520899b644` — Saved-field explanation evaluation and corrections | `2c89bdd8a6dd2c180d8bf508dc8c4718faf0c6601260c6c26d494e38244facee` |
| [S41] | Vixen `11520899b644` — Bounded live signature MVP workflow and limits | `c0bd7322031abf52ef821fc95282b0c7bf3a089c1c75444665c9487d0ddc7735` |
| [S42] | Vixen `11520899b644` — Field fidelity and test-interface cleanup contract | `63c67c8cb9aa073da564b8f870ccd2ebe08849a3375e68fbf91aa5428fdb89d5` |
| [S43] | Vixen `11520899b644` — MVP objectives and passive instrumentation | `bd8d7febf6a075babb6ee39b380ed303b3ba12ad7ab4a3404076cac7c10897f6` |
| [S44] | Vixen `11520899b644` — First ordinary operator-run comparison receipt | `b30373b9665dde79399ede41e58931728807995e254823558c572f10b3b076d9` |
| [S45] | Vixen `11520899b644` — Build 141 preservation-fix receipt | `3f54c7ddc43a9d87946ebf7b1383cc83db49eaa5b4fc94b1bc0412526e57aec1` |
| [S46] | Vixen `6ac80c057dd3` — Build 142 version and report-history checkpoint | `93e5c9246d863915826dacfb7f0021328ed6be1c1c4122ce53a2caeb464404bb` |
| [S47] | Vixen `69431deb27f3` — Build 143 version and separated history page | `a7b8b196edc52a76e07651bdb3fe0060b24729b1b6401bd4571b939978dd1a18` |
| [S48] | Vixen `7a2081eeb4b8` — Build 144 frame styling checkpoint | `bbb5f731fe4c2202235440bf2ef7e6ba5750ee1ea4bea45abebf98919c085b38` |
| [S49] | Vixen `a848594a7ae1` — Build 145 removal of redundant route frames | `f1d284ca6bf3d3f754b35613ac7763627295900ad95ae493e0896589ba541158` |
| [S50] | Vixen `22a4ec53a119` — Build 146 inset Back housing | `b3d48054daed352b84241d64ec99a755c23e9addde19dd9926a28f3afddab4d0` |
| [S51] | Vixen `b3d926d13b25` — Build 147 flat border-return iteration | `fa712d31a4f83f729282252701d701133ae480d884b4c68b8246e6a8c8607b8f` |
| [S52] | Vixen `3c865116c88b` — Approved build 148 design and build 149 application coverage | `0ef7bf12b6ae10216d6005d3a6b35fa6ca72f606b25228be778acd93e190f378` |
| [S53] | Vixen `3c865116c88b` — Build 149 version checkpoint | `c358800540c9c2df3f3c1b74ba6406a14430b4db894f109d2af9573a9687824f` |
| [S54] | Vixen `c028fb44092e` — Build 150 complete-pair selection repair | `27e16d09083a1dd7d0e55ccb032dd588cdbfcec24f375fedacbad843d14cb097` |
| [S55] | Vixen `c028fb44092e` — Build 150 verification, original T2 failure, rerun and recommendation gap | `aac4b31182312c53e003ff7675f1b503aca44e18ae58de70b46c90a985df7e82` |
| [S56] | Hunter-TestHarness `5f00861d44da` — T1/T2 scenarios, original failure and successful rerun | `20c189db413426be6a56a6c73935573303238fb9c6445891572246d5a9524ccd` |

## Local audit records

These records were inspected in the retained development workspace. Their raw contents are not published in this document. A log filename alone is not proof of the result; the account above distinguishes observed failure, successful bounded checks and missing per-build feature notes. Private operator captures remain private.

| Retained basename | SHA-256 | Availability |
| --- | --- | --- |
| `field-build123-validation.log` | `a061ead34811e25d7b6a890cdcf9b57ddaf89543c1ba0d8155dad276f127687c` | Retained locally; not a public Git attachment |
| `phone-cache-build128-results.jsonl` | `3739ed16c4179810831b53fa028a69481b52ad656d7dfb6d85c525d9fa872feb` | Retained locally; not a public Git attachment |
| `phone-cache-build129-gpu-checks.log` | `b65646376aa5831224f2156b6fda72def657ad63db0ed0b06a17f3bf79695d7b` | Retained locally; not a public Git attachment |
| `phone-cache-build130-i8mm-checks.log` | `0ba37fdddd07cab0d517dc04e81629962765c6519e4111f1e5f41938a732e83b` | Retained locally; not a public Git attachment |
| `field-fidelity-build137-final.log` | `719b69425d397946580fedfe847369d1818a2b48bd77694611dfea5b76f369bf` | Retained locally; not a public Git attachment |
| `field-ai-evaluation-20261114T052030Z/build125-validation.json` | `772f922cf279aaac601917fde37d62cdb11a261408ec2344a89cd3a7e8b58b44` | Retained locally; not a public Git attachment |
| `field-ai-evaluation-20261114T052030Z/build126-validation.json` | `8ff39959c83cdff08e30d6a2096817ed6d2a6adeaaf3312367aadab53163e17e` | Retained locally; not a public Git attachment |
| `field-ai-evaluation-20261114T052030Z/build127-validation.json` | `489acdc8e923936ea4ac947e9c17d01772f781cb89dc464bfbff395071c7abe7` | Retained locally; not a public Git attachment |
| `live-signature-v3-phone/first-operator-run.jsonl` | `8a8a671edc0bb6a2c7af4d5e8be08e6d11b061c2fd8b23244f10356de85e24ba` | Retained locally; not a public Git attachment |
| `live-signature-v3-phone/baseline-141/verification.json` | `197b808a41e48a3950c53d3d7c7a2780d63f398a3330fe5d4c3e58b850d4a90a` | Retained locally; not a public Git attachment |
| `live-signature-v3-phone/t1-operator/verdict.json` | `005f1617bdc4beb36aa0856538abb02d2f91a1eb16c0ef9b1187779131645854` | Retained locally; not a public Git attachment |
| `vixen142-deploy/verification.json` | `9d5474359577e145b412ccad155678f6cd5da3fec176aac105488e1905026e91` | Retained locally; not a public Git attachment |
| `vixen143-deploy/verification.json` | `4183932799dc04c3404b88b3271d6759af4e6cc4a4b30376521715daa4cf1e65` | Retained locally; not a public Git attachment |
| `vixen144-deploy/verification.json` | `beb0d53e76c562e151046c9969851209675c482d77cb2304f770173250b3b27e` | Retained locally; not a public Git attachment |
| `vixen145-design-build.log` | `92c004f63a706bb17499b751ff2ed42e2963d9a2be813b64e5637d75e936bcd1` | Retained locally; not a public Git attachment |
| `vixen146-design-build.log` | `ba147c5b8a7e64a97b038372b7be6d03bafe9718f73fcc6eb7200987e7629a07` | Retained locally; not a public Git attachment |
| `vixen147-design-build.log` | `d8348b82c0a2227828e6fbec549da3520824d9d5ae9f2ea91b8f288b4cb528d5` | Retained locally; not a public Git attachment |
| `vixen148-design-build.log` | `3672dd3ebfe057c9c5957d8a062805eabef98f3ecdc94120f4e26c7a27834773` | Retained locally; not a public Git attachment |
| `vixen149-deploy/verification.json` | `c9820aaa3c2d63483077bb7e5410e3d730d5df2b2cbd23d64ca2efd7034c4f28` | Retained locally; not a public Git attachment |
| `live-signature-v3-phone/t2-operator/verification.json` | `35112ccf0015c1a2b65768ee33845a5c6aaa94e3794f4be158808d1d8eb51557` | Retained locally; not a public Git attachment |
| `vixen150-deploy/verification.json` | `8f90acf632937a129d044e5f8be1938f180c6e0c2dfb2a469ac352e8d5e514c1` | Retained locally; not a public Git attachment |
| `vixen150-selection-before.log` | `faec845af19f0f15b10a264bb0a53c0701c0ea2aaa3bc2742bb8e5cc1dda56fa` | Retained locally; not a public Git attachment |
| `vixen150-selection-after.log` | `c3043ee6f069d9f45a78e7dba7df9048b8a317810a1059d5691feaed9f6b5b02` | Retained locally; not a public Git attachment |
| `live-signature-v3-phone/t2-rerun-150/verification.json` | `4be9e0a2af1a0dc1944ee6be21d6058d754f89f9b48d5e3a671a262a6a00a43b` | Retained locally; not a public Git attachment |

## Current stopping point

Build 150 is installed. The repaired 141 baseline and T1 passed their bounded criteria. Test 2 failed on frozen 149, then passed its defined criteria on 150 after a procedural selection repair; the original failure is retained. Both variants passed 434 tests for 150. The model and T2 harness scenario were unchanged by the repair. **Action recommendations remain unimplemented, so the complete actionable MVP report has not passed acceptance.** Test 3 remains defined, not executed. No additional physical pass, public release, or independent RF/SD/watch verification is inferred.

[S01]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/android/wireshark-spike/RESULTS.md
[S02]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/docs/ai-development/records/builder-behavior-reporting.md
[S03]: https://github.com/mikevan/LayerTime/blob/6b14f8d5b6400d8902868b2bf33e625b48b1b00c/devices/layertime-controller-android/app/build.gradle.kts
[S04]: https://github.com/mikevan/LayerTime/blob/a05ecbe356a75f852a74b92f354cb47a2b3a8f55/devices/layertime-controller-android/app/build.gradle.kts
[S05]: https://github.com/mikevan/LayerTime/blob/a6639ad518f2d6ef11089701f912e0ac09d9be72/devices/layertime-controller-android/app/build.gradle.kts
[S06]: https://github.com/mikevan/LayerTime/blob/bf43f41207e9d13a279a4a5cae7837c9276b7d37/devices/layertime-controller-android/app/build.gradle.kts
[S07]: https://github.com/mikevan/LayerTime/blob/5943c3a44d2c423a25a0e2f44f758c622f27567a/devices/layertime-controller-android/app/build.gradle.kts
[S08]: https://github.com/mikevan/LayerTime/blob/92525c912109fd188cf336b5b9733f09dc568478/devices/layertime-controller-android/app/build.gradle.kts
[S09]: https://github.com/mikevan/LayerTime/blob/9d0ade450adfa6ae3c28884394456aa8855c0143/devices/layertime-controller-android/app/build.gradle.kts
[S10]: https://github.com/mikevan/LayerTime/blob/4700de16e16b4757c21b09cf07d5d3349049e025/devices/layertime-controller-android/app/build.gradle.kts
[S11]: https://github.com/mikevan/LayerTime/blob/5949abf200a0fcae261d6cc36c4b78420b0e82be/devices/layertime-controller-android/app/build.gradle.kts
[S12]: https://github.com/mikevan/LayerTime/blob/7ac2f1648c0678ed9e290f419f51ff0faa2cbdd9/devices/layertime-controller-android/app/build.gradle.kts
[S13]: https://github.com/mikevan/LayerTime/blob/d0932f258e01754ce8adbea66684d1935fd095dd/devices/layertime-controller-android/app/build.gradle.kts
[S14]: https://github.com/mikevan/LayerTime/blob/398f9af7909bd2765de9ef944b3ea1ac849f4f01/devices/layertime-controller-android/app/build.gradle.kts
[S15]: https://github.com/mikevan/LayerTime/blob/e97a7b9bb9720596ab8bfe1d36ed7a4df1b50e1f/devices/layertime-controller-android/app/build.gradle.kts
[S16]: https://github.com/mikevan/LayerTime/blob/b30a3f732c8daa22b73388e7d2f193205a33ad86/devices/layertime-controller-android/app/build.gradle.kts
[S17]: https://github.com/mikevan/LayerTime/blob/cb1582daecdf9a1229a98e24ded7d36f098f4b83/devices/layertime-controller-android/app/build.gradle.kts
[S18]: https://github.com/mikevan/LayerTime/blob/7aec804eaea2387b67dbfaa378d26466d493a491/devices/layertime-controller-android/app/build.gradle.kts
[S19]: https://github.com/mikevan/Vixen/blob/e2e8566e9bde75de65b434dea065b43044a605fe/android/app/build.gradle.kts
[S20]: https://github.com/mikevan/Vixen/blob/f40288d918ff932ebbae54c60eb6e7b75132252a/android/app/build.gradle.kts
[S21]: https://github.com/mikevan/Vixen/blob/10e642a6c68740c805ebfa441b324dcb49aa74f9/android/app/build.gradle.kts
[S22]: https://github.com/mikevan/Vixen/blob/8ad37a1bdc6e412599539f89e188b9ed4cfbe730/android/app/build.gradle.kts
[S23]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/android/app/build.gradle.kts
[S24]: https://github.com/mikevan/Vixen/blob/1d9ee3194c4bbac615e6f9f394386745782fa3c0/android/app/build.gradle.kts
[S25]: https://github.com/mikevan/LayerTime/commit/a6639ad518f2d6ef11089701f912e0ac09d9be72
[S26]: https://github.com/mikevan/LayerTime/commit/5943c3a44d2c423a25a0e2f44f758c622f27567a
[S27]: https://github.com/mikevan/LayerTime/commit/d0932f258e01754ce8adbea66684d1935fd095dd
[S28]: https://github.com/mikevan/LayerTime/commit/7ac2f1648c0678ed9e290f419f51ff0faa2cbdd9
[S29]: https://github.com/mikevan/LayerTime/commit/4700de16e16b4757c21b09cf07d5d3349049e025
[S30]: https://github.com/mikevan/LayerTime/commit/92525c912109fd188cf336b5b9733f09dc568478
[S31]: https://github.com/mikevan/LayerTime/commit/9d0ade450adfa6ae3c28884394456aa8855c0143
[S32]: https://github.com/mikevan/LayerTime/commit/5949abf200a0fcae261d6cc36c4b78420b0e82be
[S33]: https://github.com/mikevan/LayerTime/commit/e97a7b9bb9720596ab8bfe1d36ed7a4df1b50e1f
[S34]: https://github.com/mikevan/LayerTime/commit/b30a3f732c8daa22b73388e7d2f193205a33ad86
[S35]: https://github.com/mikevan/LayerTime/blob/a05ecbe356a75f852a74b92f354cb47a2b3a8f55/devices/layertime-controller-android/README.md
[S36]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/android/app/src/main/java/com/projectrevivesolutions/layertime/controller/mission/MissionPrefs.kt
[S37]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/docs/ai-development/CURRENT-AI-STATUS.md
[S38]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/docs/ai-development/CHECKIN-BUILD122.md
[S39]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/docs/field-build-124.md
[S40]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/docs/field-ai-evaluation.md
[S41]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/docs/live-signature-mvp.md
[S42]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/docs/field-test-contract.md
[S43]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/docs/mvp-okrs.md
[S44]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/tools/ai-qualification/live-signature-v3/operator-baseline-summary.json
[S45]: https://github.com/mikevan/Vixen/blob/11520899b644252a48c165658bfa00bd3fdd1449/tools/ai-qualification/live-signature-v3/preservation-repair-141.json

[S46]: https://github.com/mikevan/Vixen/blob/6ac80c057dd3a05bb5d038d7b61171928393835d/android/app/build.gradle.kts
[S47]: https://github.com/mikevan/Vixen/blob/69431deb27f33e9022a5c119bf40b9b14690ee0d/android/app/build.gradle.kts
[S48]: https://github.com/mikevan/Vixen/blob/7a2081eeb4b81ff0a883c2af4553e2558aea9d4b/android/app/build.gradle.kts
[S49]: https://github.com/mikevan/Vixen/blob/a848594a7ae17c0fea25f35752a3ac60ac3407ab/android/app/build.gradle.kts
[S50]: https://github.com/mikevan/Vixen/blob/22a4ec53a1193eb3b21b1469d76873f579e16de7/android/app/build.gradle.kts
[S51]: https://github.com/mikevan/Vixen/blob/b3d926d13b2571e3021683610cddc89e9312e2bb/android/app/build.gradle.kts
[S52]: https://github.com/mikevan/Vixen/blob/3c865116c88bd779c60ab19cdf74018be0804c0b/docs/ui-design-language.md
[S53]: https://github.com/mikevan/Vixen/blob/3c865116c88bd779c60ab19cdf74018be0804c0b/android/app/build.gradle.kts
[S54]: https://github.com/mikevan/Vixen/blob/c028fb44092e00f4feadd2f7760d881c540e6f48/android/app/src/main/java/com/projectrevivesolutions/layertime/controller/signatures/LiveSignatureInput.kt
[S55]: https://github.com/mikevan/Vixen/blob/c028fb44092e00f4feadd2f7760d881c540e6f48/docs/live-signature-mvp.md
[S56]: https://github.com/mikevan/Hunter-TestHarness/blob/5f00861d44da00a5fc1d5696ce6070d16ab5a17e/docs/incremental-field-tests.md
