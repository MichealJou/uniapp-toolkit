import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

fun properties(key: String) = project.findProperty(key).toString()
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.intellij.platform") version "2.5.0"
    id("org.jetbrains.changelog") version "2.2.0"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create("WS", properties("platformVersion")) // 使用 properties 读取版本号
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        bundledPlugin("JavaScript")
        bundledPlugin("NodeJS")
        bundledPlugin("com.intellij.webcore")
    }
}

intellijPlatform {
    pluginConfiguration {
        name = properties("pluginName")
        id = properties("pluginId")
        description = properties("pluginDescription")
//        changeNotes = changelog.run {
//            getOrNull(properties("pluginVersion"))?.toHTML() ?: getLatest().toHTML()
//        }
        vendor {
            name = properties("pluginVendorName")
            email = properties("pluginVendorEmail")
            url = properties("pluginVendorUrl")
        }

        ideaVersion {
            sinceBuild = properties("pluginSinceBuild")
            untilBuild = properties("pluginUntilBuild")
        }
    }

    buildSearchableOptions = true
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version = properties("pluginVersion")
//        changeNotes = changelog.run {
//            getOrNull(properties("pluginVersion"))?.toHTML() ?: getLatest().toHTML()
//        }
    }

    runIde {
//        autoReloadPlugins = true
    }

    signPlugin {
        certificateChainFile = file("certificates/chain.crt")
        privateKeyFile = file("certificates/private.pem")
        password = System.getenv("PRIVATE_KEY_PASSWORD")
    }

    publishPlugin {
        token = System.getenv("PUBLISH_TOKEN")
    }
}
kotlin {
    jvmToolchain(17)
}