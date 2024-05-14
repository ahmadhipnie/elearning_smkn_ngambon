plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.elearning_smkn_ngambon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.elearning_smkn_ngambon"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"AIzaSyAMMmErV9OgcKjtHynbh-3nqurPIbRu6jU\"")
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

    buildFeatures {
        buildConfig = true
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
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("io.github.muddz:styleabletoast:2.4.0")
    implementation("dev.chrisbanes.insetter:insetter:0.6.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    // add the dependency for the Google AI client SDK for Android
    implementation("com.google.ai.client.generativeai:generativeai:0.3.0")

    // Required for one-shot operations (to use `ListenableFuture` from Reactive Streams)
    implementation("com.google.guava:guava:31.0.1-jre")

    // Required for streaming operations (to use `Publisher` from Guava Android)
    implementation("org.reactivestreams:reactive-streams:1.0.4")


    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.karumi:dexter:6.0.2")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")


    // Dependensi Gson Converter
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
}