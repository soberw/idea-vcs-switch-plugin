import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.PublishPluginTask
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask
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
val pluginVerifierIdeVersion = providers.gradleProperty("pluginVerifierIdeVersion").orNull
val pluginArchivePath = providers.gradleProperty("pluginArchivePath")

dependencies {
    testImplementation("junit:junit:4.13.2")

    intellijPlatform {
        // Compile against the oldest supported platform so newer-only APIs cannot leak into the plugin binary.
        intellijIdea("2026.1")
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
            <p>Expanded IntelliJ IDEA compatibility.</p>
            <ul>
              <li>Support IntelliJ IDEA 2026.1 through 2026.3.</li>
              <li>Verify compatibility against IntelliJ Platform builds 261, 262, and 263.</li>
            </ul>
        """.trimIndent()
        ideaVersion {
            sinceBuild = "261"
            untilBuild = "263.*"
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
            if (!pluginVerifierLocalIdePath.isNullOrBlank()) {
                local(file(pluginVerifierLocalIdePath))
            } else if (!pluginVerifierIdeVersion.isNullOrBlank()) {
                create("IU", pluginVerifierIdeVersion)
            } else {
                // Verify one stable release from every supported platform generation.
                create("IU", "2026.1")
                create("IU", "2026.2.5")
                create("IU", "2026.3")
            }
        }
    }
}

kotlin {
    jvmToolchain(21)
}

tasks {
    named<VerifyPluginTask>("verifyPlugin") {
        pluginArchivePath.orNull?.let { archiveFile.set(layout.projectDirectory.file(it)) }
    }

    named<PublishPluginTask>("publishPlugin") {
        pluginArchivePath.orNull?.let { archiveFiles.setFrom(layout.projectDirectory.file(it)) }
    }

    wrapper {
        gradleVersion = "9.5.1"
    }
}
