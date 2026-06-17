enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyRecipesStoreKmp"
include(":androidApp")
include(":shared")
include(":core:model")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:data")
include(":core:domain")
include(":core:auth")
include(":sync")
include(":core:designsystem")
include(":core:ui")
include(":core:navigation")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:detail:api")
include(":feature:detail:impl")
include(":feature:categories:api")
include(":feature:categories:impl")
include(":feature:search:api")
include(":feature:search:impl")
include(":feature:favorites:api")
include(":feature:favorites:impl")
include(":feature:login:api")
include(":feature:login:impl")
include(":feature:section:api")
include(":feature:section:impl")
include(":feature:video:api")
include(":feature:video:impl")
include(":feature:reset:api")
include(":feature:reset:impl")
include(":feature:register:api")
include(":feature:register:impl")
include(":feature:profile:api")
include(":feature:profile:impl")