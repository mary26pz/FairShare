// Project-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()  // Ensure Google repository is included
        mavenCentral()
    }
    dependencies {
        // Ensure that the Google services plugin classpath is included here
        classpath("com.google.gms:google-services:4.4.2")  // Firebase plugin
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")  // Hilt Gradle plugin
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false  // <-- Add this line
}