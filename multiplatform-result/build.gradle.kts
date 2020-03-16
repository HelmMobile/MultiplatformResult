import cat.helm.result.build.standardConfiguration

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.4-jetbrains-3"


}
apply(from = "../gradle/publish.gradle")
standardConfiguration()

group = "cat.helm.result"
version = "1.0.0"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest{
            dependencies {
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
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
