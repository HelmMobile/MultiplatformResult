pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.library" -> useModule("com.android.tools.build:gradle:${requested.version}")
                "binary-compatibility-validator" -> useModule("org.jetbrains.kotlinx:binary-compatibility-validator:${requested.version}")
                "com.jfrog.bintray" -> useModule("com.jfrog.bintray.gradle:gradle-bintray-plugin:${requested.version}")
            }
        }
    }

    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
    }
}
include(":multiplatform-result")
enableFeaturePreview("GRADLE_METADATA")
rootProject.name = "mpp-result"

