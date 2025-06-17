    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        // Añade el plugin de Compose Compiler (requerido desde Kotlin 2.0)
        id("org.jetbrains.kotlin.plugin.compose")
    }

    android {
        namespace = "com.example.movilesapp"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.example.movilesapp"
            minSdk = 23
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17 // Actualizado a 17
            targetCompatibility = JavaVersion.VERSION_17
        }
        kotlinOptions {
            jvmTarget = "17" // Actualizado a 17
        }
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.4" // Última versión estable
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    dependencies {
        //Dependencias para peticiones y mapas
        implementation("androidx.navigation:navigation-compose:2.5.3")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.google.maps.android:maps-compose:2.11.4")
        implementation("com.google.maps.android:android-maps-utils:3.4.0")
        implementation("com.google.android.gms:play-services-maps:19.2.0")
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        // Core
        implementation("androidx.datastore:datastore-preferences:1.1.1")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
        implementation("androidx.activity:activity-compose:1.8.0")

        // Compose BOM (última versión estable)
        implementation(platform("androidx.compose:compose-bom:2023.10.01"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.material:material-icons-extended")

        // Navigation
        implementation("androidx.navigation:navigation-compose:2.7.5")
        implementation(libs.androidx.datastore)
        implementation(libs.androidx.datastore.android)
        implementation(libs.play.services.ads.api)


        // Testing
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
        androidTestImplementation("androidx.compose.ui:ui-test-junit4")
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")

        //Publicidad
        implementation("com.google.android.gms:play-services-ads:24.4.0")

    }