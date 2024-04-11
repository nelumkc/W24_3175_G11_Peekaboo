plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.w24_3175_g11_peekaboo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.w24_3175_g11_peekaboo"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //Android Jetpack Navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    //
    implementation("com.google.firebase:firebase-database:20.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")


    //Firestore
    implementation("com.google.firebase:firebase-firestore")

    //Authentication
    implementation("com.google.firebase:firebase-auth")

    //Storage
    implementation("com.google.firebase:firebase-storage")

    implementation("com.google.android.gms:play-services-auth:21.0.0")

    implementation ("com.squareup.picasso:picasso:2.71828")

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    //sending email
    implementation ("com.sun.mail:android-mail:1.6.0")
    implementation ("com.sun.mail:android-activation:1.6.0")
    //Live streaming
    implementation ("com.github.ZEGOCLOUD:zego_uikit_prebuilt_live_streaming_android:+")

    //image slider
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    // dependency for loading image from url
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    //paypal
    implementation("com.paypal.checkout:android-sdk:1.3.0")


}