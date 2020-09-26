object Versions {

    const val kotlinVersion = "1.4.10"

    object BuildScript {
        // Maintenance & Build Environment
        //const val versions: Lib = "com.github.ben-manes:gradle-versions-plugin:0.20.0"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val android = "com.android.tools.build:gradle:4.0.1"

        // Third
        const val manodermausJUnit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.6.2.0"
    }

    object Kotlin {
        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
    }

    object Testing {
        // JUnit 5
        private const val junitJupiterVersion = "5.6.2"
        //private const val junitPlatformVersion = "1.6.2"
        //private const val junitVintageVersion = "5.6.2"
        const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion"
        const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion"
        const val junitJupiterParams = "org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion"
        //const val junitPlatformCommons: Lib = "org.junit.platform:junit-platform-commons:$junitPlatformVersion"
        //const val junitPlatformEngine: Lib = "org.junit.platform:junit-platform-engine:$junitPlatformVersion"
        //const val junitPlatformLauncher: Lib = "org.junit.platform:junit-platform-launcher:$junitPlatformVersion"
        //const val junitPlatformRunner: Lib = "org.junit.platform:junit-platform-runner:$junitPlatformVersion"
        //const val junitVintageEngine: Lib = "org.junit.vintage:junit-vintage-engine:$junitVintageVersion"

        // Assertions & Testing
        //private const val truthVersion = "0.43"
        //const val truth: Lib = "com.google.truth:truth:$truthVersion"
        //const val truthJava8Extensions: Lib = "com.google.truth.extensions:truth-java8-extension:$truthVersion"
        //const val truthAndroidExtensions: Lib = "androidx.test.ext:truth:1.1.0"

        const val mockitoCore = "org.mockito:mockito-core:3.1.0"
        const val mockitoAndroid = "org.mockito:mockito-android:3.1.0"

        const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"

        const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        const val espressoContrib = "androidx.test.espresso:espresso-contrib:3.3.0"

        //const val androidxTestCore: Lib = "androidx.test:core:1.2.0"
        //const val androidxTestMonitor: Lib = "androidx.test:monitor:1.2.0"
        const val androidxTestRunner = "androidx.test:runner:1.3.0"
        const val androidxTestRules = "androidx.test:rules:1.3.0"

        //private const val spekVersion = "1.2.1"
        //const val spekApi: Lib = "org.jetbrains.spek:spek-api:$spekVersion"
        //const val spekEngine: Lib = "org.jetbrains.spek:spek-junit-platform-engine:$spekVersion"

        const val junit4 = "junit:junit:4.13"

        // Third
        const val mannodermausTestCore = "de.mannodermaus.junit5:android-test-core:1.2.0"
        const val mannodermausTestRunner = "de.mannodermaus.junit5:android-test-runner:1.2.0"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha02"
        const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha02"
        const val preferenceKtx = "androidx.preference:preference-ktx:1.1.1"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.1"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val activityKtx = "androidx.activity:activity-ktx:1.1.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.0-alpha08"

        const val livecycleVersion = "2.2.0"
        const val livecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$livecycleVersion"
        const val livecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$livecycleVersion"
        const val livecycleLiveData = "androidx.lifecycle:lifecycle-livedata:$livecycleVersion"
        const val livecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:$livecycleVersion"

        const val roomVersion = "2.2.5"
        const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
        const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
        const val roomRxJava2 = "androidx.room:room-rxjava2:$roomVersion"
        const val roomKtx = "androidx.room:room-ktx:$roomVersion"

        const val emojiVersion = "1.1.0"
        const val emoji = "androidx.emoji:emoji:$emojiVersion"
        const val emojiAppcompat = "androidx.emoji:emoji-appcompat:$emojiVersion"
    }

    object Google {
        const val material = "com.google.android.material:material:1.3.0-alpha02"
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:4.11.0" // glide imaging
        const val glideTransform = "jp.wasabeef:glide-transformations:4.0.0" // for rounded corner images with Glide
    }

    object Jackson {
        private const val jacksonVersion = "2.10.3"

        const val jacksonAnnotiations = "com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion"
        const val jacksonDatabind = "com.fasterxml.jackson.core:jackson-databind:$jacksonVersion"
        const val jacksonModuleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion"
    }
}