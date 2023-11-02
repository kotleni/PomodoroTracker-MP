plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight")
}

val appPackage = "app.kotleni.pomodoro"

kotlin {
    androidTarget()

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                val voyagerVersion = "1.0.0-rc05"
                implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")

                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")

                val koinVersion = extra["koin.version"] as String
                implementation("io.insert-koin:koin-core:$koinVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")

                implementation("app.cash.sqldelight:android-driver:2.0.0")

                val koinVersion = extra["koin.version"] as String
                implementation("io.insert-koin:koin-android:$koinVersion")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)

                implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = appPackage

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set(appPackage)
        }
    }
}