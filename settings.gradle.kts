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
        maven { url = uri("https://storage.zego.im/maven")  }
        maven { url =uri("https://www.jitpack.io") }
    }
}

rootProject.name = "W24_3175_G11_Peekaboo"
include(":app")
 