// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {

        Dependencies.buildscript.forEach { classpath(it) }

        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {

    repositories {
        google()
        mavenCentral()

        //apply(plugin = "maven")

        group = "com.github.upjoin"
    }

}

/*tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}*/
