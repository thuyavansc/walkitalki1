plugins {
    id("com.android.application")
}

android {
    namespace = "au.com.softclient.walkitalki1"
    compileSdk = 34

    defaultConfig {
        applicationId = "au.com.softclient.walkitalki1"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

//    implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar']) // Include AAR files in the libs folder

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    //implementation((name: "opus", ext: "aar") // Include Opus AAR file
    //api files('libs/opus.aar')

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation(fileTree(mapOf(
//        "dir" to "F:\\CampusNotes\\OnlineCourse\\AndroidProject\\Learn\\WalkiTalki1\\app\\libs",
//        "include" to listOf("*.aar", "*.jar"),
//        "exclude" to listOf()
//    )))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //implementation ("com.microsoft.signalr:signalr:3.0.0")
    implementation ("com.microsoft.signalr:signalr:3.1.4")
    //implementation("com.microsoft.signalr:signalr:7.0.0")
    //implementation("com.microsoft.signalr:signalr:5.0.4")
    //implementation ("com.microsoft.signalr:signalr:5.1.2")

    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")

    //implementation ("net.sourceforge.jopus:jopus:1.1.6")
    //implementation ("com.aocate.media:libopus:0.2.1")
    //implementation ("com.github.jcraft:opus-java:1.2.1")
//    implementation ("org.tipl:opus-java:1.3.1")
//    implementation ("org.concentus:opus-java:1.3.1")

//    implementation ("de.sciss/javax.sound.sampled.spi-0.9")
//    implementation ("org.concentus/opus-java/1.3.1")
    //implementation ("org.xiph.jorbis:jorbis:0.0.17")
    //implementation ("com.github.jcraft:jopus:1.2.1")

    //implementation ("com.github.jcraft:jopus:1.2.1")
    //implementation ("com.github.jcraft:opus-java:1.2.1")
    //implementation ("net.java.dev.jna:jna:5.10.0")
    //implementation ("net.sourceforge.jopus:jopus:1.1.6")
    //implementation ("com.aocate.media:libopus:0.2.1")

//    implementation ("net.sourceforge.jopus:jopus:1.1.6")
//    implementation ("com.aocate.media:libopus:0.2.1")


    //implementation ("com.github.tunabaranurut:tiwiconverter:1.0.2")

//    implementation ("com.google.android.exoplayer:exoplayer-core:2.17.0")
//    implementation ("com.google.android.exoplayer:exoplayer-hls:2.17.0")
//    implementation ("com.google.android.exoplayer:exoplayer-ui:2.17.0")
//    implementation ("com.google.android.exoplayer:exoplayer-dash:2.17.0")

    //implementation("com.fraunhofer.fdk.aac:fdk-aac:2.2.1")

    //implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

//    implementation("club.minnced:opus-java:1.1.1")
//    implementation ("de.maxhenkel.opus4j:opus4j:2.0.2")
//    implementation ("net.java.dev.jna:jna:5.10.0")
//
//    //implementation ("com.tianscar.javasound:javasound-opus:1.2.1")
//    //implementation ("com.github.martoreto:opuscodec:v1.2.1.2")


    //implementation (project(":Opus")) // Replace with the actual library module name



}

