import vn.core.buildSrc.Configs

plugins {
    vn.core.plugins.androidLibrary
    vn.core.plugins.androidPublishing
}

android {
    namespace = Configs.Namespace.FINANCE
}

publishing {
    publications {
        create<MavenPublication>(Configs.Artifact.Finance.ARTIFACT_ID) {
            afterEvaluate {
                from(components["all"])
            }
            groupId = Configs.Artifact.Finance.GROUP_ID
            artifactId = Configs.Artifact.Finance.ARTIFACT_ID
            version = Configs.Artifact.Finance.VERSION
        }
    }
}

dependencies {
    implementation(libs.coreDomain)
    implementation(libs.coreData)
}
