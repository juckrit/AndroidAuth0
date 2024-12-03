plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.kingpowerclick.android.auth0"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        manifestPlaceholders["Auth0ClientId"] = "\${Auth0ClientId}" // <- important backslash!!!
        manifestPlaceholders["Auth0Scheme"] = "\${Auth0Scheme}" // <- important backslash!!!
        manifestPlaceholders["Auth0Domain"] = "\${Auth0Domain}" // <- important backslash!!!
        manifestPlaceholders["Auth0Scope"] = "\${Auth0Scope}" // <- important backslash!!!
        manifestPlaceholders["Auth0Audience"] = "\${Auth0Audience}" // <- important backslash!!!

//        buildConfigField("String", "Auth0ClientId", "$a")
//        buildConfigField("String", "Auth0Scheme", "$a")
//        buildConfigField("String", "Auth0Domain", "$a}")
//        buildConfigField("String", "Auth0Scope", "$a")
//        buildConfigField("String", "Auth0Audience", "$a")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.auth0)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
