plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kingpowerclick.auth0"
    compileSdk = 35
    testFixtures {
        enable = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
    defaultConfig {
        applicationId = "com.kingpower.dev"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Kingpower dev
//        manifestPlaceholders["auth0ClientId"] = "x2Jt9CMuKlqyPGM7DkypN8BhNno6uEii"
//        manifestPlaceholders["auth0Scheme"] = "https"
//        manifestPlaceholders["auth0Domain"] = "dev.onepass.kpc-dev.com"
//        manifestPlaceholders["auth0Scope"] = "openid profile email offline_access"
//        manifestPlaceholders["auth0Audience"] = "https://www.kingpower.com/"
//        applicationId = "com.kingpower.dev"

        // Firster dev
        manifestPlaceholders["auth0ClientId"] = "YGI5QlEhzwm73vWEAVk8jig8okiHX4DN" // firster
        manifestPlaceholders["auth0Scheme"] = "https"
        manifestPlaceholders["auth0Domain"] = "dev.onepass.kpc-dev.com"
        manifestPlaceholders["auth0Scope"] = "openid profile email offline_access"
        manifestPlaceholders["auth0Audience"] = "f1_api"
        manifestPlaceholders["auth0Organization"] = "f1"
        applicationId = "com.kpc.hisdev"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":auth0"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
