plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.hiltandroid)
    alias(libs.plugins.kotlinkapt)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")

}

android {
    namespace = "com.elshan.wallpick"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.elshan.wallpick"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.palette.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(platform(libs.firebase.bom))

    //implementation(libs.firebase.analytics)
    implementation(libs.androidx.compose.material3.material3)


    // Extended Icons
    implementation(libs.androidx.material.icons.extended)

    // Room
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)

    // Coil
    // implementation (libs.coil.compose)
    implementation(libs.coil.compose)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // System ui controller
    implementation(libs.accompanist.systemuicontroller)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    //Datastore
    implementation(libs.androidx.datastore.preferences)

    //lottie
    implementation(libs.lottie.compose)

    implementation(libs.accompanist.permissions)

    implementation(libs.androidx.biometric)

    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.googleid)
    implementation(libs.androidx.appcompat)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.firebase.messaging)



}