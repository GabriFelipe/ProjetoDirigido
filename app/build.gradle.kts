plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.compose.compiler)
    }

android {
    namespace = "com.example.projetodirigido"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.projetodirigido"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

}

dependencies {
    val composeBom =
        platform("androidx.compose:compose-bom:2026.06.00")

    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Mantém activity-compose alinhado com activity-ktx do projeto.
    implementation("androidx.activity:activity-compose:1.13.0")

    // Versões controladas pelo Compose BOM.
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0"
    )

    implementation(
        "androidx.datastore:datastore-preferences:1.1.1"
    )


    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}