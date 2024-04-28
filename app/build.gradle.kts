import android.databinding.tool.processing.ViewBindingErrorMessages

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
    packagingOptions{
        exclude ("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
            java {
                srcDirs("src\\main\\java", "src\\main\\java\\model")
            }
        }
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.databinding:databinding-runtime:8.3.2")
    implementation("com.google.firebase:firebase-firestore:24.11.1")
    implementation (platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("androidx.mediarouter:mediarouter:1.7.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("androidx.activity:activity:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.nex3z:flow-layout:1.3.3")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.hbb20:ccp:2.7.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    // https://mvnrepository.com/artifact/com.sun.mail/android-mail
    implementation ("com.sun.mail:android-mail:1.6.7")
    // https://mvnrepository.com/artifact/com.sun.mail/android-activation
    implementation("com.sun.mail:android-activation:1.6.7")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.exoplayer:exoplayer:2.16.1")
    implementation ("com.firebaseui:firebase-ui-storage:7.1.1")

}