plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    // ナビゲーションでカスタムNavTypeを指定するのに必要。
    id("kotlin-parcelize")
}

android {
    namespace = "jp.hibeiko.yarnshelf"
    compileSdk = 34

    defaultConfig {
        applicationId = "jp.hibeiko.yarnshelf"
        minSdk = 31
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation("androidx.compose.material3:material3:1.2.1")

    // ViewModelを利用するのに必要。ユニット4「アーキテクチャコンポーネント」より。
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // NavHostを利用するのに必要。ユニット4「Jetpack Composeでのナビゲーション」より。
    implementation(libs.androidx.navigation.compose)
//    implementation(libs.androidx.navigation.runtime.ktx)

    // Roomを利用するのに必要。ユニット６「データの永続化」より。
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // APIを利用するのに必要。ユニット５「インターネットに接続する」より。
    // Retrofit
    implementation(libs.retrofit)
    // Retrofit with Scalar Converter
    // Retrofit は JSON 結果を String として返すことができます。
    // Serialization Converterを使うのでこちらはコメントアウト
//    implementation(libs.converter.scalars)
    // JSON解析をするのに必要。
    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)
    // Retrofit with Kotlin serialization Converter
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)

    //Coilを利用するのに必要。Coil は、基本的に次の 2 つのものを必要とします。
    //
    //読み込んで表示する画像の URL。
    //実際に画像を表示するための AsyncImage コンポーザブル。
    // Coil
    implementation(libs.coil.compose)

    //バーコードスキャンするのに必要。
    // https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner?hl=ja
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")

    // テスト
    testImplementation(libs.junit)

    // コルーチンのテスト
    testImplementation(libs.kotlinx.coroutines.test)

    // UIテスト
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    // Navigationテスト
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.junit)
    // Espresso
    // IntentsはNavigationテストにインポートが推奨、CoreはRoomのテストにインポートが推奨とされていたが、なくても動いた。
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.androidx.espresso.intents)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}