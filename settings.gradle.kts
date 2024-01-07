pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "henkelmax.public"
            url = uri("https://maven.maxhenkel.de/repository/public")
        }
    }
}

rootProject.name = "WalkiTalki1"
include(":app")
 