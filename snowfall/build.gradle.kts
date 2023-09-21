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
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    namespace = "com.apprajapati.snowfall"
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")

}