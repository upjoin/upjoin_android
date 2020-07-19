# upjoin_android

Add it in your root build.gradle at the end of repositories:

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}

Add project dependencies:

implementation("com.github.upjoin.upjoin_android:upjoin_android_core:0.0.1.2")
implementation("com.github.upjoin.upjoin_android:upjoin_android_actions:0.0.1.2")
implementation("com.github.upjoin.upjoin_android:upjoin_android_repository:0.0.1.2")
...
