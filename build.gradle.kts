import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins{
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Versions.ANDROID_SDK_VERSION)
    defaultConfig {
        targetSdkVersion(Versions.ANDROID_SDK_VERSION)
        minSdkVersion(Versions.ANDROID_MIN_VERSION)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    sourceSets {
        named("main") {
            withConvention(KotlinSourceSet::class) {
                mutableListOf("src/main/kotlin").apply{
                    java.setSrcDirs(this)
                    kotlin.setSrcDirs(this)
                }
            }
            manifest.srcFile("src/main/AndroidManifestKotlin.xml")
        }
    }
}

dependencies {
    implementation(Libs.Kotlin.StdLib)
    implementation(Libs.AndroidX.Core)
    implementation(Libs.AndroidX.AppCompat)
}