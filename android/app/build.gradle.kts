import java.io.FileInputStream
import java.util.Properties
import java.util.concurrent.TimeUnit

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// Release signing. keystore.properties is gitignored and holds the keystore path and passwords.
// Without it (a machine that does not have the key), release builds are left unsigned instead of failing.
val keystorePropsFile = rootProject.file("keystore.properties")
val keystoreProps = Properties().apply { if (keystorePropsFile.exists()) FileInputStream(keystorePropsFile).use { load(it) } }

android {
    namespace = "com.projectrevivesolutions.layertime.controller"
    compileSdk = 36
    // NDK r28+ links 16 KB-aligned by default. https://developer.android.com/guide/practices/page-sizes
    ndkVersion = "30.0.16248370"

    defaultConfig {
        applicationId = "com.projectrevivesolutions.layerscope"
        minSdk = 26          // Android 8.0. Covers the TCL K24 and most Android-watch OSes.
        if(providers.gradleProperty("ltVulkanRoot").isPresent) minSdk = 28 // Vulkan 1.1 experiment only.
        targetSdk = 36
        // Build 111: Android-compatible summary placeholders, on-device practice regression.
        // Build 110: visual report rules and a reproducible, labeled Builder practice capture.
        versionCode = 138
        buildConfigField("boolean", "LT_ARM_DOTPROD_EXPERIMENT", "false")
        buildConfigField("boolean", "LT_ARM_I8MM_EXPERIMENT", "false")
        versionName = "0.1.138-beta.1"
        // build 88: analytic pipeline slice 1 - workflow wiring files (deauth triage shipped), engine, seven checks + verdict, frame fabricator fixtures, read-only Builder viewer, TRIAGE on event detail
        // build 87: MISSION wired to the Hunter (SetSensors allow-list), presets check their sensors, sensors grouped, EAPOL listed; OPS "Select Sensors" (All Sensors / Only Deauth) is a shortcut into MISSION
        // build 86: watch to cool steel, teal ground, toward the OPS comp; six destinations (OPS, EVENTS, TOOLS, BUILDER, MISSION, SYSTEM)
        // build 83: first release-signed build (PRS upload key)
        // build 82: GET_TEXT through the link FIFO so source MACs stop starving under an event flood; target bound at send time; empty replies logged
        // build 79: Wireshark window in TOOLS - import, display filter, packet list, protocol tree and hex
        // build 78: OPS thinned to face + connection + detection; panels moved to SYSTEM, measure to TOOLS
        // build 77: adaptive shell - five destinations, bottom bar (compact) / left rail (wide), face rolls to a header off OPS
        // build 76: phone face 220->190, landscape console inset to clear the edge handle
        // build 75: phone layout - fixed watch-size face pinned upper-left (portrait) / far-left (landscape), data console extended to fill the rest
        // build 74: detections grouped one row per MAC address, one alert per MAC; LayerTime-Sensors 1.1.2 AirTag grading
        // build 73: target API 36 (Play Store requirement, min 2026-08-31) on AGP 8.11 / Gradle 8.13; edge-to-edge handled with safeDrawing insets

        testInstrumentationRunner = "com.projectrevivesolutions.layertime.controller.BuilderSmokeInstrumentation"

        // One ABI first: the TCL K24 is arm64-v8a. Add more after phone results.
        ndk { abiFilters += "arm64-v8a" }
    }

    flavorDimensions += "distribution"
    productFlavors {
        // full: the sideload and watch build. Keeps the accessibility service (the watch hardware button).
        create("full") { dimension = "distribution" }
        // play: the Google Play build. src/play/AndroidManifest.xml removes the accessibility service so the
        // app is not subject to Google Play accessibility review. Same applicationId and signing key as full,
        // so the two are update-compatible.
        create("play") { dimension = "distribution" }
    }

    signingConfigs {
        create("release") {
            if (keystorePropsFile.exists()) {
                storeFile = file(keystoreProps.getProperty("storeFile"))
                storePassword = keystoreProps.getProperty("storePassword")
                keyAlias = keystoreProps.getProperty("keyAlias")
                keyPassword = keystoreProps.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            // Diagnostic APK: no minification so logs and class names stay readable.
            isMinifyEnabled = false
            applicationIdSuffix = ".diag"
            versionNameSuffix = "-diag"
            // Portable CPU default; stronger CPU instructions require explicit experiment flags.
            val useDotprod=providers.gradleProperty("ltArmDotprod").orNull=="true" || providers.gradleProperty("ltArmI8mm").orNull=="true"
            buildConfigField("boolean", "LT_ARM_DOTPROD_EXPERIMENT", useDotprod.toString())
            val useI8mm=providers.gradleProperty("ltArmI8mm").orNull=="true"
            buildConfigField("boolean", "LT_ARM_I8MM_EXPERIMENT", useI8mm.toString())
            externalNativeBuild { cmake {
                arguments += if(useI8mm) "-DGGML_CPU_ARM_ARCH=armv8.6-a+dotprod+fp16+i8mm"
                    else if(useDotprod) "-DGGML_CPU_ARM_ARCH=armv8.2-a+dotprod+fp16"
                    else "-DGGML_CPU_ARM_ARCH=armv8-a"
                val vulkanRoot=providers.gradleProperty("ltVulkanRoot").orNull
                arguments += "-DLT_AI_VULKAN=${if(vulkanRoot!=null) "ON" else "OFF"}"
                if(vulkanRoot!=null)arguments += "-DLT_VULKAN_BUILD_ROOT=$vulkanRoot"
            } }
        }
        create("beta") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".beta"
            versionNameSuffix = ""
            signingConfig = signingConfigs.getByName("release")
            matchingFallbacks += listOf("debug")
            buildConfigField("boolean", "LT_ARM_DOTPROD_EXPERIMENT", "false")
            buildConfigField("boolean", "LT_ARM_I8MM_EXPERIMENT", "false")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (keystorePropsFile.exists()) signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    // Stream the bundled experimental model without compressing a multi-GB asset.
    androidResources { noCompress += "gguf" }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        // Store native libs uncompressed and 16 KB zip-aligned (AGP 8.5.1+), so Android maps them straight
        // from the APK instead of unpacking a second copy. Google's preferred 16 KB setup:
        // https://developer.android.com/guide/practices/page-sizes
        jniLibs.useLegacyPackaging = false
    }
}

kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }

// Contracts belong to this checkout and are pinned by the parent gitlink.
val productRepository = rootProject.projectDir.parentFile.canonicalFile
val contractsDirectory = productRepository.resolve("contracts")
val verifySharedContracts by tasks.registering {
    group = "verification"
    description = "Verify the pinned, clean Contracts submodule before builds and protocol tests."
    doLast {
        if (!contractsDirectory.resolve(".git").exists() || !contractsDirectory.resolve("vectors/link_frames.json").isFile) {
            throw GradleException("Contracts is missing. From the Vixen root run: git submodule update --init --recursive")
        }
        fun gitAt(directory: File, vararg arguments: String): String {
            val command = listOf("git", "--no-optional-locks", "-c", "safe.directory=${directory.invariantSeparatorsPath}", "-C", directory.absolutePath) + arguments
            val process = ProcessBuilder(command).redirectErrorStream(true).start()
            if (!process.waitFor(30, TimeUnit.SECONDS)) {
                process.destroyForcibly()
                throw GradleException("Contracts Git verification timed out")
            }
            val output = process.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }.trim()
            if (process.exitValue() != 0) throw GradleException("Contracts Git verification failed: $output")
            return output
        }
        if (gitAt(productRepository, "config", "--file", ".gitmodules", "--get", "submodule.shared-contracts.path") != "contracts" ||
            gitAt(productRepository, "config", "--file", ".gitmodules", "--get", "submodule.shared-contracts.url") != "https://github.com/mikevan/LayerTime-Contracts.git") {
            throw GradleException("Contracts submodule metadata differs from the canonical repository")
        }
        val entry = gitAt(productRepository, "ls-files", "--stage", "--", "contracts").split(Regex("\\s+"), limit = 4)
        if (entry.size != 4 || entry[0] != "160000" || entry[2] != "0" || entry[3] != "contracts") {
            throw GradleException("Contracts must be recorded as one pinned Git submodule")
        }
        if (File(gitAt(contractsDirectory, "rev-parse", "--show-toplevel")).canonicalFile != contractsDirectory.canonicalFile ||
            gitAt(contractsDirectory, "rev-parse", "HEAD") != entry[1]) {
            throw GradleException("Contracts checkout differs from its recorded Git commit")
        }
        if (gitAt(contractsDirectory, "status", "--porcelain=v1", "--untracked-files=all").isNotEmpty() ||
            gitAt(contractsDirectory, "ls-files", "--others", "--ignored", "--exclude-standard", "--", "vectors").isNotEmpty()) {
            throw GradleException("Contracts has local changes or extra vector files; restore the pinned source before building")
        }
        logger.lifecycle("Shared Contracts verified at ${entry[1]}")
    }
}
tasks.named("preBuild").configure { dependsOn(verifySharedContracts) }

// Explicit offline audit files must invalidate test caching when their contents change.
tasks.withType<org.gradle.api.tasks.testing.Test>().configureEach {
    dependsOn(verifySharedContracts)
    systemProperty("vixen.contractsDir", contractsDirectory.absolutePath)
    inputs.files(fileTree(contractsDirectory) { include("vectors/**") })
    listOf("LAYERTIME_INTENT_EXAMPLES", "LAYERTIME_INTENT_RESPONSES", "LAYERTIME_QUANTITY_EXAMPLES", "LAYERTIME_QUANTITY_RESPONSES").forEach { key ->
        val auditPath = providers.environmentVariable(key).orNull
        inputs.property(key, auditPath ?: "")
        if (!auditPath.isNullOrBlank()) inputs.file(auditPath)
    }
    listOf("LAYERTIME_INTENT_TEMPLATE", "LAYERTIME_INTENT_PRACTICE_EXPORT", "LAYERTIME_QUANTITY_TEMPLATE").forEach { key ->
        val exportPath = providers.environmentVariable(key).orNull
        inputs.property(key, exportPath ?: "")
        if (!exportPath.isNullOrBlank()) outputs.upToDateWhen { false }
    }
}

dependencies {
    // Embedded orchestration only. Local inference uses our own provider, not a cloud client.
    implementation("ai.koog:agents-core:1.3.0")
    val composeBom = platform("androidx.compose:compose-bom:2024.09.03")
    implementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-service:2.8.6")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    debugImplementation("androidx.compose.ui:ui-tooling")

    testImplementation("junit:junit:4.13.2")
}
