# 超话日历查看 Android App

这是安卓 App 源码版本。它不保存微博帖子内容，只在 App 内用 WebView 登录微博，然后按你选中的日期打开个人主页搜索页面。

## 使用方式

1. 用 Android Studio 打开本文件夹：`D:\桌面\weibo-chaohua-android`
2. 等待 Gradle 同步完成。
3. 连接安卓手机，点击 Run 安装。
4. App 里先点“登录微博”，完成微博登录。
5. 填写微博 UID 和“超话名或个人主页搜索词”。
6. 在日历里点某一天，App 会打开当天对应的个人主页搜索页面。

## 重要说明

- App 不保存帖子列表、不导出、不编辑。
- 为了保持微博登录，WebView 会保留微博自己的 Cookie。
- 如果搜索词留空，App 只打开你的个人主页，并把日期复制到剪贴板。
- 默认搜索模板是：

```text
https://weibo.com/u/{uid}?profile_ftype=1&is_all=1&is_search=1&key_word={query}
```

如果微博网页改版，可以在 App 的“设置”里改模板。可用变量：

- `{uid}`：微博 UID
- `{query}`：搜索词
- `{date}`：选中日期，例如 `2026-05-28`
- `{date_cn}`：中文日期，例如 `2026年5月28日`

## 用 GitHub Actions 云打包 APK

电脑不用安装 Android Studio。把这个文件夹上传到 GitHub 仓库后，GitHub 会自动打包 APK。

步骤：

1. 在 GitHub 新建一个仓库，例如 `weibo-chaohua-viewer`。
2. 把 `D:\桌面\weibo-chaohua-android` 里的所有文件上传到仓库。
3. 打开仓库页面的 `Actions`。
4. 点左侧 `Build APK`。
5. 如果没有自动运行，点 `Run workflow`。
6. 运行完成后，打开这次运行记录，在页面底部 `Artifacts` 下载 `weibo-chaohua-viewer-debug-apk`。
7. 解压后得到 `app-debug.apk`，发到安卓手机安装。

如果手机提示“未知来源应用”，允许本次安装即可。

## 本地构建环境

当前电脑命令行里没有检测到 Java 和 Gradle，所以我没有在这里直接打包 APK。安装 Android Studio 后，它会自带 JDK，并能同步这个项目。
