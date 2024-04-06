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
        maven{
            url=uri("https://jitpack.io") }
    }
}

rootProject.name = "W24_3175_G11_Peekaboo"
include(":app")
 