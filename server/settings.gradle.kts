rootProject.name = "share-timer-backend"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("libs:common")
include("libs:db-jpa")
include("libs:storage-redis")
include("libs:web-support")
include("apps:discovery-server")
include("apps:api-gateway")
include("apps:api-service")
include("apps:sync-service")
