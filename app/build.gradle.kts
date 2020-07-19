plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    //id("jacoco")
}

android {
    compileSdkVersion(Android.compileSdkVersion)

    defaultConfig {
        applicationId = "de.upjoin.android.demo"
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = Android.versionCode //60
        versionName = Android.appVersion
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
            //minifyEnabled true
            //shrinkResources true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isDebuggable = false

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            //ndk {
            //    setProperty("debugSymbolLevel", "FULL")
            //    debugSymbolLevel = "FULL"
            //}

            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        exclude("README.txt")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

/*sourceSets {
    getByName("main").java.srcDir("src/main/kotlin")
    getByName("test").java.srcDir("src/test/kotlin")
    getByName("androidTest").java.srcDir("src/androidTest/kotlin")
}*/

dependencies {
    implementation(project(":upjoin_android_core"))
    implementation(project(":upjoin_android_actions"))
    implementation(project(":upjoin_android_repository"))

    implementation(Libs.kotlinStdLib)

    AndroidXLibs.default.forEach { implementation(it) }

    implementation(GoogleLibs.material)

    api(AndroidXLibs.roomRuntime)
    kapt(AndroidXLibs.roomCompiler)
    implementation(AndroidXLibs.roomRxJava2)
    implementation(AndroidXLibs.roomKtx)

    ThirdLibs.glideFramework.forEach { implementation(it) }
    ThirdLibs.jacksonFramework.forEach { implementation(it) }

    // ***** TESTING ***** //
    TestingLibs.androidTestsDefault.forEach { androidTestImplementation(it) }
}
