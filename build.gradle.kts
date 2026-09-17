// 1. Plugins that apply globally (if any)
plugins {
    // You can keep this here if the root needs to recognize it, 
    // but do not add configuration blocks for it.
    application 
}

// 2. Global settings for all subprojects (Optional but clean)
allprojects {
    repositories {
        mavenCentral()
    }
}
