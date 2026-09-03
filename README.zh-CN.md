# VCS Switch

[English](README.md)
[README 同步清单](README.sync-checklist.md)

一个 IntelliJ IDEA 插件，用于在同一项目根目录下检测到的 Git、SVN 等版本控制系统之间切换当前激活的 VCS 映射。

当前目标兼容范围：IntelliJ IDEA / IntelliJ Platform `2026.2.x`（build `262.*`）。

## 当前范围

- 在项目打开时检测项目根目录下的 Git 和 SVN 标记
- 读取 IDEA 当前对项目根目录使用的 VCS 映射
- 通过工具栏弹窗展示当前检测到的所有 VCS
- 将项目根目录的映射切换到用户选择的 VCS
- 在 IDEA 刷新完成后通知用户切换结果

## 维护者

- Vendor: `soberw`
- Email: `blog_wwwang@163.com`
- Source: [https://github.com/soberw](https://github.com/soberw)

## 构建

请使用本地 JDK 21。若在 Windows 环境下运行 Gradle，必要时先设置 `JAVA_HOME`。

```powershell
$env:JAVA_HOME="C:\path\to\jdk-21"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat build
.\gradlew.bat buildPlugin
```

如果 Gradle 仍然无法自动识别 JDK 21，请先确认 `JAVA_HOME` 已指向一个可用的 JDK 21 安装目录，再执行命令。

生成的插件包位于：

`build/distributions/vcs-switch-0.1.3.zip`

## 兼容性校验

```powershell
.\gradlew.bat verifyPlugin
```

项目已配置 IntelliJ Plugin Verifier，默认只校验当前目标 IDE，这样本地验证更快，也与当前配置的 `2026.2.2` 平台依赖保持一致。

如果本地还没有对应的校验 IDE 缓存，Gradle 可能会额外下载一份独立的 IntelliJ IDEA `2026.2.2` 校验发行包。只要你的本地环境已经配置好，它不会再去下载 JDK 或 Gradle。

如果你希望 Plugin Verifier 直接使用你当前已经安装的 IntelliJ IDEA，而不是下载校验 IDE 包，可以这样执行：

```powershell
.\gradlew.bat verifyPlugin -PpluginVerifierLocalIdePath="D:\path\to\IntelliJ IDEA 2026.2.2"
```

日常本地开发时，也可以在项目根目录创建一个不入库的 `gradle-local.properties` 文件：

```properties
pluginVerifierLocalIdePath=D:\\path\\to\\IntelliJ IDEA 2026.2.2
org.gradle.java.home=D:\\developmentTools\\Java\\jdk\\jdk-21.0.11
org.gradle.java.installations.paths=D:\\developmentTools\\Java\\jdk\\jdk-21.0.11
```

只要这个文件里存在 `pluginVerifierLocalIdePath`，`verifyPlugin` 就会默认优先使用你的本机 IDEA 安装目录。

## 安装到你当前的 IDEA

1. 执行 `.\gradlew.bat buildPlugin`
2. 打开 IntelliJ IDEA
3. 进入 `Settings -> Plugins`
4. 点击右上角齿轮图标
5. 选择 `Install Plugin from Disk...`
6. 选择 `build/distributions/vcs-switch-0.1.3.zip`
7. 重启 IDEA

## 可选的沙盒运行

```powershell
$env:JAVA_HOME="C:\path\to\jdk-21"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat runIde
```

## 手工验证清单

1. 打开一个项目根目录同时包含 `.git` 和 `.svn` 的测试项目
2. 通过工具栏或 `Ctrl+Shift+A` 触发 `Switch Project VCS`
3. 确认弹窗中同时列出了 Git 和 SVN
4. 确认当前 VCS 项带有 `current` 标记
5. 切换到另一种 VCS
6. 确认完成通知正常弹出
7. 打开 `Settings -> Version Control`，确认映射已经变更
8. 重新打开项目，确认上次选择的 VCS 仍然生效

## 发布到 Marketplace 前的准备

在发布到 JetBrains Marketplace 之前，请准备：

1. 与当前 vendor 身份和邮箱一致的 Marketplace vendor profile
2. 用于 `publishPlugin` 的 `PUBLISH_TOKEN` 环境变量
3. 开发者 EULA 或开源许可证选择
4. 若使用开源许可证，则在 Marketplace 页面提供源码链接
5. 如果希望发布签名插件包，则准备签名凭据

## 发布命令

```powershell
$env:PUBLISH_TOKEN="your_marketplace_token"
.\gradlew.bat publishPlugin
```

可选的签名环境变量：

- `CERTIFICATE_CHAIN`
- `PRIVATE_KEY`
- `PRIVATE_KEY_PASSWORD`

## CI/CD 自动发布

JetBrains Marketplace 本身不像 PyPI 那样直接基于源码仓库构建插件。官方支持的自动化路径是：由你的 CI 先构建插件 ZIP，再通过 `publishPlugin` 上传。

当前仓库已补充 `.github/workflows/publish.yml`，支持：

- 手动触发
- 推送 `v*` tag 时触发
- 自动校验 tag 版本与 `gradle.properties` 中的版本号一致
- 自动构建插件
- 自动执行兼容性校验
- 自动上传 ZIP 到 GitHub Actions 制品
- 自动发布到 JetBrains Marketplace

GitHub 仓库需要配置的 secrets：

- `PUBLISH_TOKEN`

如果你后续要把 workflow 扩展为发布签名插件，还可以额外配置：

- `CERTIFICATE_CHAIN`
- `PRIVATE_KEY`
- `PRIVATE_KEY_PASSWORD`

推荐发布流程：

1. 更新 `gradle.properties` 中的版本号
2. 提交并推送代码到 GitHub
3. 创建并推送对应 tag，例如 `v0.1.3`
4. 让 GitHub Actions 自动完成构建、校验和发布

GitHub 侧需要手动做的配置：

1. 打开 `Settings -> Secrets and variables -> Actions`
2. 新增仓库 secret：`PUBLISH_TOKEN`
3. 如果 GitHub 提示需要启用 Actions，先启用 workflow
4. 手动发布时，进入 `Actions -> Publish Plugin -> Run workflow`；自动发布时，直接推送 `v*` tag
