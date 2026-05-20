# VCS Switch

[中文说明](README.zh-CN.md)

An IntelliJ IDEA plugin for switching the active VCS mapping of the current project root between detected version control systems such as Git and SVN.

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

The packaged plugin zip is generated at:

`build/distributions/vcs-switch-0.1.0.zip`

## Compatibility Verification

```powershell
.\gradlew.bat verifyPlugin
```

This project is configured to run the IntelliJ Plugin Verifier against the recommended IDE set for the targeted platform version.

## Install Into Your Existing IDEA

1. Run `.\gradlew.bat buildPlugin`
2. Open IntelliJ IDEA
3. Go to `Settings -> Plugins`
4. Click the gear icon
5. Choose `Install Plugin from Disk...`
6. Select `build/distributions/vcs-switch-0.1.0.zip`
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
