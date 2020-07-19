// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {

        Plugins.buildscript.forEach { classpath(it) }

        /*
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
            classpath("com.android.tools.build:gradle:4.0.1")
            classpath("com.google.gms:google-services:4.3.3")
            classpath("de.mannodermaus.gradle.plugins:android-junit5:1.6.2.0")
        */

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {

    repositories {
        google()
        jcenter()
    }
}

/* Groovy: task clean(type: Delete) {
    delete rootProject.buildDir
}*/
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}