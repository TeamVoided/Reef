rootProject.name = "Reef"
pluginManagement {
    repositories {
        maven("https://maven.teamvoided.org/releases")
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}
