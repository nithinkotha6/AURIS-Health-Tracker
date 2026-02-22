plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace  = "com.auris"
    compileSdk = 35

    defaultConfig {
        applicationId   = "com.auris"
        minSdk          = 26
        targetSdk       = 35
        versionCode     = 1
        versionName     = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Room: export schema for migration tracking
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental",    "true")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable        = true
            isMinifyEnabled     = false
        }
        release {
            isMinifyEnabled   = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose     = true
        viewBinding = true  // Keeps legacy Canvas view references compilable
        buildConfig = true  // Required for BuildConfig.DEBUG references
    }
}

dependencies {
    // ── Core ──────────────────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)   // collectAsStateWithLifecycle
    implementation(libs.androidx.activity.compose)

    // ── Compose BOM + UI ──────────────────────────────────────────────
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.material)
    implementation(libs.compose.animation)
    implementation(libs.compose.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)      // Icons.Default.GridView etc.
    debugImplementation(libs.compose.ui.tooling)

    // ── Navigation ────────────────────────────────────────────────────
    implementation(libs.navigation.compose)

    // ── Hilt DI ───────────────────────────────────────────────────────
    implementation(libs.hilt.android)
    debugImplementation(libs.androidx.ui.test.manifest)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // ── Room (entity types in Phase 1-5, active queries in Phase 6) ──
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // ── WorkManager ───────────────────────────────────────────────────
    implementation(libs.work.runtime.ktx)

    // ── DataStore ─────────────────────────────────────────────────────
    implementation(libs.datastore.preferences)

    // ── Guava ─────────────────────────────────────────────────────────
    implementation(libs.guava)

    // ── Serialization + Coroutines ────────────────────────────────────
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // ── Logging ───────────────────────────────────────────────────────
    implementation(libs.timber)

    // ── Phase 7/8  — CameraX + image compressor ─────────────────────
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation(libs.compressor)

    // ── Phase 9   — Hilt WorkManager integration ──────────────────────
    implementation(libs.hilt.work)
    ksp(libs.hilt.compiler)

    // ══ FUTURE PHASE DEPENDENCIES (uncomment when needed) ════════════
    //
    // Phase 6  — SQLCipher
    // implementation(libs.sqlcipher)
    //
    // Phase 10 — Health Connect
    implementation(libs.health.connect)
    //
    // Phase 11 — Vico trend charts
    implementation(libs.vico.compose.m3)
}
