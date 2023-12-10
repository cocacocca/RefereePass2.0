# 裁判通系统

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 项目简介

裁判通系统是一个基于Java Swing和MySQL数据库的裁判信息管理系统。该系统提供了添加、删除、修改、查询和备份裁判信息的功能。用户通过登录界面输入用户名和密码，成功登录后可以选择相应的功能进行操作。

## 特性

- 提供添加、删除、修改、查询和备份裁判信息的功能。
- 使用Java Swing实现直观的用户界面。
- 数据库连接采用JDBC工具类进行管理。
- 登录功能进行用户身份验证，用户名和密码硬编码在代码中。

## 快速开始

### 安装

1. 克隆项目到本地：
   ```bash
   git clone https://github.com/cocacocca/RefereePass2.0.git
   cd RefereeSystem
   ```

2. 导入项目到IDE中，确保项目依赖的JDBC驱动已配置。

### 示例代码

```java
// 示例代码
public class Main {
    public static void main(String[] args) {
        // 启动登录界面
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
```

## 文档

### 文件结构

```plaintext
- FnFrame
  - ButtonClickListener
    - CommonButtonClickListener.java
  - FnCollection
    - addPackage
      - addUI.java
      - addFn.java
    - backupPackage
      - backupUI.java
    - deletePackage
      - deleteUI.java
      - deleteFn.java
    - selectPackage
      - selectUI.java
      - selectFn.java
    - updatePackage
      - updateUI.java
      - updateFn.java
  - FnFrame.java
- LoginFrame
  - LoginFrame.java
- util
  - JDBCUtils.java
```

### 使用说明

1. 运行 `LoginFrame.java` 启动程序。
2. 输入正确的用户名和密码进行登录。
3. 登录成功后，系统会打开功能选择窗口 (`FnFrame.java`)。
4. 在功能选择窗口中，可以选择添加、删除、修改、查询和备份功能。

**注意：** 数据库连接信息在 `JDBCUtils.java` 中进行配置。确保数据库连接信息正确，以便系统正常运行。

## 贡献

欢迎贡献者！在提交问题或请求更改之前，请阅读贡献指南。

## 版权和许可

该项目使用 MIT 许可证。