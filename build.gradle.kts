// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {

        Plugins.buildscript.forEach { classpath(it) }

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {

    repositories {
        google()
        jcenter()

        apply(plugin = "maven")

        group = "de.upjoin"
    }

}

/*tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}*/