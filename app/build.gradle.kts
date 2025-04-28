plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34  // 使用 SDK 33（更加稳定）

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 21
        targetSdk = 34  // 确保 targetSdk 也是 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")  // 必须添加此项
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.ui:ui:1.1.1")  // Jetpack Compose UI
    implementation("androidx.compose.material3:material3:1.0.0")  // Material Design 3
    implementation("org.tensorflow:tensorflow-lite:2.10.0")
    implementation(libs.androidx.appcompat)  // TensorFlow Lite
    implementation ("androidx.appcompat:appcompat:1.6.1")

    testImplementation("junit:junit:4.13.2")  // 单元测试
    androidTestImplementation("androidx.test.ext:junit:1.1.3")  // Android 测试
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")  // Espresso UI 测试

    androidTestImplementation(platform("androidx.compose:compose-bom:2021.10.00"))  // Jetpack Compose BOM
}
