import org.gradle.api.JavaVersion

object Configs {
    const val minSdk = 24
    const val targetSdk = 34
    const val compileSdk = 34
    const val kotlinCompilerExtensionVersion = "1.5.14"
    val javaVersion = JavaVersion.VERSION_21
    val jvmTarget = JavaVersion.VERSION_21.toString()
    const val mavenDomain = "https://maven.pkg.github.com"

    object Finance {
        const val namespace = "vn.core.provider.finance"
        const val groupId = "vn.finance.libs"
        const val artifactId = "networking"
        const val version = "1.0.0"
    }
}


