plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
    implementation("com.android.tools.build:gradle:3.6.1")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.10.0")
}