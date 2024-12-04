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

        manifestPlaceholders["auth0ClientId"] = "\${auth0ClientId}" // <- important backslash!!!
        manifestPlaceholders["auth0Scheme"] = "\${auth0Scheme}" // <- important backslash!!!
        manifestPlaceholders["auth0Domain"] = "\${auth0Domain}" // <- important backslash!!!
        manifestPlaceholders["auth0Scope"] = "\${auth0Scope}" // <- important backslash!!!
        manifestPlaceholders["auth0Audience"] = "\${auth0Audience}" // <- important backslash!!!
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
        buildConfig = true
    }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("androidAuth0") {
                from(components["debug"])
                groupId = "com.github.juckrit"
                artifactId = "androidauth0"
                version = "0.9.0"
            }
        }

        repositories {
            mavenCentral()
        }
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
