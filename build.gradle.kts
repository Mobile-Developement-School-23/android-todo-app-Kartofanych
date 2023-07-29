buildscript {
    dependencies {
        classpath(libs.navigation.safe.args.gradle.plugin)
        classpath(libs.coroutines)
    }
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    alias(libs.plugins.android.library) apply false
}