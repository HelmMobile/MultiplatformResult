plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
}

group = "cat.helm.result"
version = "1.0.0"
kotlin {
    jvm()
    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
    ios()
    linuxX64()
    macosX64()
    mingwX64()
    android()

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting{
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val androidTest by getting{
            dependencies{
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(15)
    }
    sourceSets {
        getByName("main").apply {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}

repositories {
    maven("http://dl.bintray.com/kotlin/kotlin")
    maven("https://plugins.gradle.org/m2/")
    google()
    mavenCentral()
    jcenter()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "cat.helm.result"
            artifactId = "multiplatform-result"
            version = "1.0.0"
        }
    }
}