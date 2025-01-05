import com.diffplug.gradle.spotless.SpotlessExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(mobilex.plugins.androidHilt) apply false
    alias(mobilex.plugins.spotless) apply false
}

allprojects {
    apply {
        plugin(rootProject.mobilex.plugins.spotless.get().pluginId)
    }

    configure<SpotlessExtension> {
        // Configuration for Java files
        java {
            target("**/*.java")
            googleJavaFormat().aosp() // Use Android Open Source Project style
            removeUnusedImports() // Automatically remove unused imports
            trimTrailingWhitespace() // Remove trailing whitespace
        }

        // Configuration for Kotlin files
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt") // Exclude files in the build directory
            ktlint("1.5.0").setEditorConfigPath(rootProject.file(".editorconfig").path) // Use ktlint with version 1.2.1 and custom .editorconfig
            toggleOffOn() // Allow toggling Spotless off and on within code files using comments
            trimTrailingWhitespace()
        }

        // Additional configuration for Kotlin Gradle scripts
        kotlinGradle {
            target("*.gradle.kts")
            ktlint("1.5.0") // Apply ktlint to Gradle Kotlin scripts
        }
    }
}
