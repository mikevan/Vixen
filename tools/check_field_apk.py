"""Reject test interfaces and answer fixtures in an application APK (not the test APK)."""
import argparse
import sys
import zipfile

FORBIDDEN_CLASSES = (
    "LiveHarness", "HarnessProtocol", "LiveHarnessPanel", "DomainAiPracticePanelKt",
    "FieldShadowPanelKt", "FieldShadowExperiment", "BuilderPracticePanelKt",
    "WiresharkDiagActivity", "DomainAiExamples",
)
FORBIDDEN_CONTROLS = (
    "LIVE HUNTER TEST", "EXPERIMENTAL AI FOR LATEST OBSERVATION", "Test trained AI",
    "Test deauth examples", "LOAD PRACTICE ANALYTIC", "TRY THE PRACTICE EXAMPLE",
    "SCREEN-OFF RECEPTION TEST", "LINK MEASURE  //  TEST",
)


def problems(path):
    errors = []
    with zipfile.ZipFile(path) as apk:
        manifest = apk.read("AndroidManifest.xml")
        dex = [apk.read(name) for name in apk.namelist() if name.endswith(".dex")]
        for value in FORBIDDEN_CLASSES + FORBIDDEN_CONTROLS:
            if any(value.encode("utf-8") in data for data in dex):
                errors.append("test interface/code: " + value)
        if any("WiresharkDiagActivity".encode(enc) in manifest for enc in ("utf-8", "utf-16le")):
            errors.append("exported diagnostic activity")
        for name in apk.namelist():
            if name.startswith("assets/") and name.endswith(("acceptance.jsonl", "acceptance-rubrics.jsonl")):
                errors.append("answer fixture: " + name)
    return errors


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("apks", nargs="+")
    args = parser.parse_args()
    failed = False
    for path in args.apks:
        errors = problems(path)
        print(path + (": FAIL" if errors else ": PASS — no built-in test interfaces or acceptance answers"))
        for error in errors:
            print("  " + error)
        failed |= bool(errors)
    return int(failed)


if __name__ == "__main__":
    sys.exit(main())
