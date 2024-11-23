import java.util.Properties

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
    maven {
        val ghUsername = System.getenv("GH_USERNAME") ?: getLocalProperty("GH_USERNAME")
        val ghPassword = System.getenv("GH_TOKEN") ?: getLocalProperty("GH_TOKEN")
        url = uri("https://maven.pkg.github.com/${ghUsername}/REPOSITORY")
        credentials {
            username = ghUsername
            password = ghPassword
        }
    }
}

dependencies {
    implementation(libs.corePlugins)
}

fun getLocalProperty(propertyName: String): String {
    val localProperties = Properties().apply {
        val localPropertiesFile = File(rootDir, "local.properties")
        if (localPropertiesFile.exists()) {
            load(localPropertiesFile.inputStream())
        }
    }

    return localProperties.getProperty(propertyName) ?: run {
        throw NoSuchFieldException("Not defined property: $propertyName")
    }
}
