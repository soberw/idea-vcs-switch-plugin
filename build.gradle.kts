import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
}

group = providers.gradleProperty("group").get()
version = providers.gradleProperty("version").get()

val localGradleProperties = Properties().apply {
    val localFile = rootProject.file("gradle-local.properties")
    if (localFile.isFile) {
        localFile.inputStream().use(::load)
    }
}
val pluginVerifierLocalIdePath = sequenceOf(
    providers.gradleProperty("pluginVerifierLocalIdePath").orNull,
    System.getenv("PLUGIN_VERIFIER_LOCAL_IDE_PATH"),
    localGradleProperties.getProperty("pluginVerifierLocalIdePath"),
)
    .firstOrNull { !it.isNullOrBlank() }

dependencies {
    testImplementation("junit:junit:4.13.2")

    intellijPlatform {
        intellijIdea("2026.1.3")
        testFramework(TestFrameworkType.Platform)
    }
}

intellijPlatform {
    pluginConfiguration {
        id = "io.github.soberw.vcsswitch"
        name = "VCS Switch"
        version = project.version.toString()
        description = """
            <p>Switch the active VCS mapping of the current project root between the version control systems detected by IntelliJ IDEA.</p>
            <ul>
              <li>Detects Git and SVN markers in the project root.</li>
              <li>Shows the VCS currently mapped by IDEA.</li>
              <li>Lets you switch mappings from a toolbar popup without opening Settings.</li>
            </ul>
        """.trimIndent()
        changeNotes = """
            <p>2026.1 compatibility update.</p>
            <ul>
              <li>Retarget the plugin to IntelliJ IDEA 2026.1.3.</li>
              <li>Publish compatibility for IntelliJ Platform build 261.* (2026.1.x).</li>
            </ul>
        """.trimIndent()
        ideaVersion {
            sinceBuild = "261"
            untilBuild = "261.*"
        }
        vendor {
            name = "soberw"
            email = "blog_wwwang@163.com"
            url = "https://github.com/soberw"
        }
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        channels = providers.gradleProperty("pluginChannel").map { listOf(it) }.orElse(listOf("default"))
        hidden = providers.gradleProperty("pluginHidden").map(String::toBoolean).orElse(false)
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    pluginVerification {
        ides {
            if (pluginVerifierLocalIdePath.isNullOrBlank()) {
                current()
            } else {
                local(file(pluginVerifierLocalIdePath))
            }
        }
    }
}

kotlin {
    jvmToolchain(21)
}

tasks {
    wrapper {
        gradleVersion = "9.5.1"
    }
}
