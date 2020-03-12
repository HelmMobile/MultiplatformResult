buildscript {
    repositories {
        maven("http://dl.bintray.com/kotlin/kotlin")
        maven("https://plugins.gradle.org/m2/")
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:3.6.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
    }
}

enableFeaturePreview("GRADLE_METADATA")
rootProject.name = "multiplatform-result"

