plugins {
    id("com.android.library") //This is the most important difference between a library module and application main module.
    id("kotlin-android")
    id("maven-publish")
}


android {
    namespace = "com.apprajapati.loadingviews"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
}

afterEvaluate{
    publishing {
        publications {
            create<MavenPublication>("release"){
                from(components["release"])
                groupId = "com.apprajapati"
                artifactId = "loading-view"
                version = "1.0"
            }
        }
    }
}