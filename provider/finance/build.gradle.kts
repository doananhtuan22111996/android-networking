plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
    alias(libs.plugins.androidHilt)
    `maven-publish`
}

android {
    namespace = Configs.Finance.namespace
    compileSdk = Configs.compileSdk

    defaultConfig {
        minSdk = Configs.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Configs.javaVersion
        targetCompatibility = Configs.javaVersion
    }
    kotlinOptions {
        jvmTarget = Configs.jvmTarget
    }
    buildFeatures {
        buildConfig = true
    }
    publishing {
        multipleVariants("all") {
            allVariants()
            withSourcesJar()
        }
    }
}

publishing {
    val ghUsername = System.getenv("GH_USERNAME")
    val ghPassword = System.getenv("GH_TOKEN")
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("${Configs.mavenDomain}/${ghUsername}/android-networking")
            credentials {
                username = ghUsername
                password = ghPassword
            }
        }
    }
    publications {
        create<MavenPublication>("mavenAndroid") {
            afterEvaluate {
                from(components["all"])
            }
            groupId = Configs.Finance.groupId
            artifactId = Configs.Finance.artifactId
            version = Configs.Finance.version
        }
    }
}

dependencies {
    implementation(libs.coreLibxDomain)
    implementation(libs.coreLibxData)
    implementation(libs.bundles.coreAndroidComponents)
    implementation(libs.androidxHilt)
    kapt(libs.androidxHiltCompiler)
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)
    implementation(libs.loggerOkhttp)
    implementation(libs.loggerTimber)
    testImplementation(libs.bundles.testComponents)
    androidTestImplementation(libs.bundles.androidTestComponents)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}