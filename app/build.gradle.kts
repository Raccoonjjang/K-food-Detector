plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.K_Food_Detector.k_fooddetector"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.K_Food_Detector.k_fooddetector"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-ads:21.2.0")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.json:json:20210307")
    implementation("com.google.guava:guava:32.0.1-android")
    implementation("com.google.mlkit:text-recognition-korean:16.0.1")  // ML Kit OCR 라이브러리
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}