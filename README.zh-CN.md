# VCS Switch

[English](README.md)

一个 IntelliJ IDEA 插件，用于在同一项目根目录下检测到的 Git、SVN 等版本控制系统之间切换当前激活的 VCS 映射。

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

`build/distributions/vcs-switch-0.1.0.zip`

## 兼容性校验

```powershell
.\gradlew.bat verifyPlugin
```

项目已配置 IntelliJ Plugin Verifier，会针对目标平台版本推荐的 IDE 集合执行兼容性校验。

## 安装到你当前的 IDEA

1. 执行 `.\gradlew.bat buildPlugin`
2. 打开 IntelliJ IDEA
3. 进入 `Settings -> Plugins`
4. 点击右上角齿轮图标
5. 选择 `Install Plugin from Disk...`
6. 选择 `build/distributions/vcs-switch-0.1.0.zip`
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
