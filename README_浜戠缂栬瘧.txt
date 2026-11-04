手机云端编译说明（无需电脑）

这个压缩包已加入 GitHub Actions 云端编译配置。注意：当前应用仍是基础坐标点击器，并非完整的四生产位自动化程序；编译成功只代表基础点击器 APK 生成成功。

手机操作：
1. 登录 GitHub，创建一个 Public repository（公开仓库），例如 DeltaTapHelper。
2. 进入仓库，选择 Add file -> Upload files。
3. 解压本 ZIP，把 DeltaTapHelper 文件夹里面的所有内容（包括 .github 隐藏目录）上传到仓库根目录并 Commit changes。
4. 打开仓库顶部 Actions 标签，选择 Build Android APK。
5. 点 Run workflow，等待任务变成绿色成功。
6. 打开成功的运行记录，在 Artifacts 区域下载 DeltaTapHelper-debug-apk，解压后得到 app-debug.apk。
7. 在 Android 手机上点 APK 安装。只在自己信任的仓库和构建产物上安装。

如果上传页面没有显示 .github 文件夹，说明手机网页端隐藏目录未上传；请用支持显示隐藏文件的文件管理器/网页端方式补齐 .github/workflows/build-apk.yml，否则 Actions 不会出现工作流。

本工程的哈夫币确认仍需人工核对；没有实现自动识别游戏页面、四生产位导航或 8 小时调度。不要将其当作完整自动收菜成品。
