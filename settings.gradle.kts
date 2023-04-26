pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven { url = uri("https://maven.deeplink.dev/repository/maven-releases/") }
        maven { url = uri("https://jitpack.io") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven { url = uri("https://maven.deeplink.dev/repository/maven-releases/") }
        maven { url = uri("https://jitpack.io") }
        mavenCentral()
    }
}

rootProject.name = "DeepLinkDemo"
include(":app")
 