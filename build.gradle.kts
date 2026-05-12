// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "9.2.1" apply false
    id("java")
}
buildscript{
    repositories{
        google()

    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.9.8")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}