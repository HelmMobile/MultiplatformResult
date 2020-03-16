
package cat.helm.result.build

import com.android.build.gradle.BaseExtension
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetPreset
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

private val Project.kotlin: KotlinMultiplatformExtension
    get() = extensions.getByType()

private val Project.android: BaseExtension
    get() = extensions.getByType()

fun Project.standardConfiguration(
    vararg presetNames: String = kotlin.presets.map { it.name }.toTypedArray(),
    isTestModule: Boolean = false
) {
    val targetPresets = kotlin.presets.matching { it.name in presetNames }
    kotlin.buildAllTargets(targetPresets)
    android.configureAndroidApiLevel()

}

private fun KotlinMultiplatformExtension.buildAllTargets(targetPresets: NamedDomainObjectCollection<KotlinTargetPreset<*>>) {
    android {
        publishAllLibraryVariants()
    }
    js {
        browser()
    }

    // Create empty targets for presets with no specific configuration
    targetPresets.forEach {
        if (it.name == "jvmWithJava") return@forEach // Probably don't need this, and it chokes on Android plugin
        if (targets.findByName(it.name) == null) {
            targetFromPreset(it)
        }
    }

    linkAppleSourceSets()
}

private fun KotlinMultiplatformExtension.linkAppleSourceSets() {
    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val appleMain by creating {
            dependsOn(commonMain)
        }
        val appleTest by creating {
            dependsOn(commonTest)
        }
        val apple64Main by creating {
            dependsOn(appleMain)
        }
        val apple64Test by creating {
            dependsOn(appleTest)
        }
        val apple32Main by creating {
            dependsOn(appleMain)
        }
        val apple32Test by creating {
            dependsOn(appleTest)
        }

        // TODO this is just here to make the IDE happy (ish) while we wait for HMPP to improve
        val iosX64Main by getting {
            kotlin.srcDirs(*appleMain.kotlin.srcDirs.toTypedArray())
            kotlin.srcDirs(*apple64Main.kotlin.srcDirs.toTypedArray())
        }

        targets
            .withType<KotlinNativeTarget>()
            .matching { it.konanTarget.family.isAppleFamily }
            .configureEach {
                if (konanTarget.architecture.bitness == 32 || konanTarget.family == Family.WATCHOS) {
                    compilations["main"].defaultSourceSet.dependsOn(apple32Main)
                    compilations["test"].defaultSourceSet.dependsOn(apple32Test)
                } else {
                    compilations["main"].defaultSourceSet.dependsOn(apple64Main)
                    compilations["test"].defaultSourceSet.dependsOn(apple64Test)
                }
            }

    }
}

private fun BaseExtension.configureAndroidApiLevel() {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(15)
    }
}