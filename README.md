# VCS Switch

[中文说明](README.zh-CN.md)
[README Sync Checklist](README.sync-checklist.md)

An IntelliJ IDEA plugin for switching the active VCS mapping of the current project root between detected version control systems such as Git and SVN.

Current target compatibility: IntelliJ IDEA / IntelliJ Platform `2026.2.x` (build `262.*`).

## Current Scope

- Detect Git and SVN markers in the project root when a project opens
- Read the currently active IDEA VCS mapping for the project root
- Show all detected VCS entries from a toolbar action popup
- Switch the project root mapping to the selected VCS
- Notify the user after the IDEA refresh cycle completes

## Maintainer

- Vendor: `soberw`
- Email: `blog_wwwang@163.com`
- Source: [https://github.com/soberw](https://github.com/soberw)

## Build

Use a local JDK 21 installation. On Windows, set `JAVA_HOME` before running Gradle if needed.

```powershell
$env:JAVA_HOME="C:\path\to\jdk-21"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat build
.\gradlew.bat buildPlugin
```

If Gradle cannot find JDK 21 automatically, verify that `JAVA_HOME` points to a JDK 21 installation before running the command.

The packaged plugin zip is generated at:

`build/distributions/vcs-switch-0.1.3.zip`

## Compatibility Verification

```powershell
.\gradlew.bat verifyPlugin
```

This project is configured to run the IntelliJ Plugin Verifier against the current target IDE only, which keeps local verification faster and matches the configured `2026.2.2` platform dependency.

If the required verification IDE artifact is not cached yet, Gradle may download an isolated IntelliJ IDEA `2026.2.2` distribution for verification. It does not need to download JDK or Gradle when your local environment is already configured.

To force Plugin Verifier to use your existing local IntelliJ IDEA installation instead of downloading an IDE artifact, pass a local path:

```powershell
.\gradlew.bat verifyPlugin -PpluginVerifierLocalIdePath="D:\path\to\IntelliJ IDEA 2026.2.2"
```

For daily local development, you can also create an untracked `gradle-local.properties` file in the project root:

```properties
pluginVerifierLocalIdePath=D:\\path\\to\\IntelliJ IDEA 2026.2.2
org.gradle.java.home=D:\\developmentTools\\Java\\jdk\\jdk-21.0.11
org.gradle.java.installations.paths=D:\\developmentTools\\Java\\jdk\\jdk-21.0.11
```

If `pluginVerifierLocalIdePath` is present there, `verifyPlugin` will prefer your local IDE installation automatically.

## Install Into Your Existing IDEA

1. Run `.\gradlew.bat buildPlugin`
2. Open IntelliJ IDEA
3. Go to `Settings -> Plugins`
4. Click the gear icon
5. Choose `Install Plugin from Disk...`
6. Select `build/distributions/vcs-switch-0.1.3.zip`
7. Restart IDEA

## Optional Sandbox Run

```powershell
$env:JAVA_HOME="C:\path\to\jdk-21"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat runIde
```

## Manual Validation Checklist

1. Open a project whose root contains both `.git` and `.svn`
2. Trigger `Switch Project VCS` from the toolbar or `Ctrl+Shift+A`
3. Confirm both Git and SVN are listed
4. Confirm the current VCS is marked as `current`
5. Switch to the other VCS
6. Confirm the completion notification appears
7. Check `Settings -> Version Control` and verify the mapping changed
8. Reopen the project and verify the selected VCS is still active

## Marketplace Publish Prerequisites

Before publishing to JetBrains Marketplace, prepare the following:

1. A Marketplace vendor profile that uses the same vendor identity and email
2. A `PUBLISH_TOKEN` environment variable for `publishPlugin`
3. A Developer EULA or an open-source license choice
4. A source code URL on the Marketplace page if you choose an open-source license
5. Signing credentials if you want to distribute a signed plugin zip

## Marketplace Publish Commands

```powershell
$env:PUBLISH_TOKEN="your_marketplace_token"
.\gradlew.bat publishPlugin
```

Optional signing environment variables:

- `CERTIFICATE_CHAIN`
- `PRIVATE_KEY`
- `PRIVATE_KEY_PASSWORD`

## CI/CD Publishing

JetBrains Marketplace does not build your plugin from source like PyPI. The supported automated path is: your CI builds the plugin ZIP, then uploads it with `publishPlugin`.

This repository includes a GitHub Actions workflow at `.github/workflows/publish.yml` that:

- runs on manual dispatch
- runs on tags matching `v*`
- validates the Git tag version against `gradle.properties`
- builds the plugin
- verifies the plugin
- uploads the ZIP as a GitHub Actions artifact
- publishes it to JetBrains Marketplace

Required GitHub repository secrets:

- `PUBLISH_TOKEN`

Optional signing secrets, if you extend the workflow to publish signed artifacts:

- `CERTIFICATE_CHAIN`
- `PRIVATE_KEY`
- `PRIVATE_KEY_PASSWORD`

Recommended release flow:

1. Update `gradle.properties` version
2. Commit and push to GitHub
3. Create and push a matching tag, for example `v0.1.3`
4. Let GitHub Actions build, verify, and publish automatically

Manual setup on GitHub:

1. Open `Settings -> Secrets and variables -> Actions`
2. Add repository secret `PUBLISH_TOKEN`
3. Open `Actions` and enable workflows if GitHub asks
4. Use `Actions -> Publish Plugin -> Run workflow` for manual releases, or push a `v*` tag for automatic releases
