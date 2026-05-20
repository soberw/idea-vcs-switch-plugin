import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
}

group = providers.gradleProperty("group").get()
version = providers.gradleProperty("version").get()

dependencies {
    testImplementation("junit:junit:4.13.2")

    intellijPlatform {
        intellijIdeaCommunity("2025.1.4")
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
            <p>Initial public release.</p>
            <ul>
              <li>Detect project-level Git and SVN repositories.</li>
              <li>Switch the active VCS directory mapping from a toolbar action.</li>
              <li>Notify after the IDEA refresh cycle completes.</li>
            </ul>
        """.trimIndent()
        ideaVersion {
            sinceBuild = "251"
            untilBuild = "251.*"
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
            recommended()
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
