plugins {
    id("com.android.library")
    id("kotlin-android")
}


android {
    compileSdk = 34

            defaultConfig {
                minSdk = 24
            }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "com.apprajapati.snowfall"
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$rootProject.kotlinVersion")

}