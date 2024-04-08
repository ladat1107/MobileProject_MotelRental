import android.databinding.tool.processing.ViewBindingErrorMessages

plugins {
    id("com.android.application")
<<<<<<< HEAD
    id("com.google.gms.google-services")
=======
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
>>>>>>> dd91258 (MB06 | Update gradle version 8.3.1)
}

android {
    namespace = "com.motel.mobileproject_motelrental"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.motel.mobileproject_motelrental"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.databinding:databinding-runtime:8.3.1")
<<<<<<< HEAD
    implementation("com.google.firebase:firebase-firestore:24.11.0")
=======
    implementation("com.google.android.gms:play-services-maps:18.2.0")
<<<<<<< HEAD
>>>>>>> dd91258 (MB06 | Update gradle version 8.3.1)
=======
    implementation("com.google.android.libraries.places:places:3.4.0")
>>>>>>> a8daf71 (MB06 | GoogleMap)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation ("com.nex3z:flow-layout:1.3.3")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
}