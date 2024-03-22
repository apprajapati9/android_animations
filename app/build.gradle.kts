plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.apprajapati.myanimations"
    defaultConfig {
        applicationId = "com.apprajapati.myanimations"
        minSdk = 24 // rootProject.ext.minSdk

        compileSdk =  34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled =  false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        dataBinding =  true
    }
}

dependencies {

    implementation(project(":snowfall"))
    implementation(project(":customLoadingViews"))
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation ("androidx.core:core-ktx:1.12.0")

    //For network calls
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //Gson - converterfactor
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    debugImplementation ("com.squareup.leakcanary:leakcanary-android:2.13")
}