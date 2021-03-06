object Dependencies {

  val buildscript = listOf(
    Versions.BuildScript.kotlin,
    Versions.BuildScript.android,
    Versions.BuildScript.manodermausJUnit5)

  val androidTestsDefault = listOf(
    Versions.Testing.junit4,
    Versions.Testing.androidxTestRunner,
    Versions.Testing.androidxTestRules,
    Versions.Testing.junitJupiterApi,
    Versions.Testing.junitJupiterParams,
    Versions.Testing.mannodermausTestCore,
    Versions.Testing.mockitoCore,
    Versions.Testing.mockitoAndroid,
    Versions.Testing.mockitoKotlin,
    Versions.Testing.espressoCore,
    Versions.Testing.espressoContrib)

  val kotlinDefault = listOf(
    Versions.Kotlin.kotlinStdLib
  )

  val androidXdefault = listOf(
    Versions.AndroidX.coreKtx,
    Versions.AndroidX.appcompat,
    Versions.AndroidX.preferenceKtx,
    Versions.AndroidX.constraintLayout,
    Versions.AndroidX.swipeRefreshLayout,
    Versions.AndroidX.activityKtx,
    Versions.AndroidX.fragmentKtx,
    Versions.AndroidX.worker,
    Versions.AndroidX.livecycleExtensions,
    Versions.AndroidX.livecycleViewModelKtx,
    Versions.AndroidX.livecycleLiveData,
    Versions.AndroidX.livecycleCommonJava8)

  val androidXnavigation = listOf(
    Versions.AndroidX.navigationFragment,
    Versions.AndroidX.navigationUI)

  val androidXemojis = listOf(
    Versions.AndroidX.emoji,
    Versions.AndroidX.emojiAppcompat
  )

  val material = listOf(
    Versions.Google.material
  )

  val glideFramework = listOf(
    Versions.Glide.glide,
    Versions.Glide.glideTransform
  )

  val jacksonFramework = listOf(
    Versions.Jackson.jacksonAnnotiations,
    Versions.Jackson.jacksonDatabind,
    Versions.Jackson.jacksonModuleKotlin
  )

}
