plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.francotte.myrecipesstorekmp.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.francotte.myrecipesstorekmp.android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
    }
}

dependencies {
    implementation(projects.shared)
    implementation(projects.core.domain)
    implementation(projects.core.auth)
    implementation(projects.sync)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.detail.api)
    implementation(projects.feature.detail.impl)
    implementation(projects.feature.categories.api)
    implementation(projects.feature.categories.impl)
    implementation(projects.feature.search.api)
    implementation(projects.feature.search.impl)
    implementation(projects.feature.favorites.api)
    implementation(projects.feature.favorites.impl)
    implementation(projects.feature.login.api)
    implementation(projects.feature.login.impl)
    implementation(projects.feature.section.api)
    implementation(projects.feature.section.impl)
    implementation(projects.feature.video.api)
    implementation(projects.feature.video.impl)
    implementation(projects.feature.reset.api)
    implementation(projects.feature.reset.impl)
    implementation(projects.feature.register.api)
    implementation(projects.feature.register.impl)
    implementation(projects.feature.profile.api)
    implementation(projects.feature.profile.impl)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)
}