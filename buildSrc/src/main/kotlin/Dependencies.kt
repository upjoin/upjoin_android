private const val kotlinVersion = "1.3.72"

object Plugins {
  const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  const val android = "com.android.tools.build:gradle:4.0.1"

  val buildscript = listOf(kotlin, android)
}

object Libs {
  const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
  const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
}

object TestingLibs {

  private const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
  private const val espressoContrib = "androidx.test.espresso:espresso-contrib:3.2.0"

  private const val androidxTestRunner = "androidx.test:runner:1.2.0"
  private const val androidxTestRules = "androidx.test:rules:1.2.0"

  const val junit4 = "junit:junit:4.13"

  val androidTestsDefault = listOf(junit4, androidxTestRunner, androidxTestRules, espressoCore, espressoContrib)
}

object AndroidXLibs {
  private const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha01"
  private const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha01"
  private const val preferenceKtx = "androidx.preference:preference-ktx:1.1.1"
  private const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta8"
  private const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
  private const val activityKtx = "androidx.activity:activity-ktx:1.1.0"
  private const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.0-alpha07"

  private const val livecycleVersion = "2.2.0"
  private const val livecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$livecycleVersion"
  private const val livecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$livecycleVersion"
  private const val livecycleLiveData = "androidx.lifecycle:lifecycle-livedata:$livecycleVersion"
  private const val livecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:$livecycleVersion"

  private const val roomVersion = "2.2.5"
  const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
  const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
  const val roomRxJava2 = "androidx.room:room-rxjava2:$roomVersion"
  const val roomKtx = "androidx.room:room-ktx:$roomVersion"

  val default = listOf(coreKtx, appcompat, preferenceKtx, constraintLayout, swipeRefreshLayout, activityKtx, fragmentKtx,
                            livecycleExtensions, livecycleViewModelKtx, livecycleLiveData, livecycleCommonJava8)
}

object GoogleLibs {
  const val material = "com.google.android.material:material:1.3.0-alpha02"
}

object ThirdLibs {
  private const val glide = "com.github.bumptech.glide:glide:4.11.0" // glide imaging
  private const val glideTransform = "jp.wasabeef:glide-transformations:4.0.0" // for rounded corner images with Glide

  val glideFramework = listOf(glide, glideTransform)

  private const val jacksonVersion = "2.10.3"

  private const val jacksonAnnotiations = "com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion"
  private const val jacksonDatabind = "com.fasterxml.jackson.core:jackson-databind:$jacksonVersion"
  private const val jacksonModuleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion"

  val jacksonFramework = listOf(jacksonAnnotiations, jacksonDatabind, jacksonModuleKotlin)
}
