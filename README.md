

# Micro Todo：分治思想赋能的 Todolist 📝

------

![](https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202501141226490.png)

## 介绍

欢迎使用 **micro todo** 🌟，这是一个集成了AI的高级待办事项应用程序，旨在提升您的任务和时间管理效率。该项目利用了包括Spring Boot、Vue、MyBatis Plus和MySQL在内的强大技术栈，提供无缝和高效的用户体验。

## 项目截图

| ![PixPin_2025-01-14_12-46-03](https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202501141250376.png) | ![PixPin_2025-01-14_12-48-13](https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202501141251731.png) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![PixPin_2025-01-14_12-49-37](https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202501141251152.png) | ![PixPin_2025-01-14_12-50-16](https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202501141251435.png) |



## 功能概述

- **AI集成 🤖**：基于您的习惯和偏好提供智能任务建议和时间管理提示。
- **用户友好界面 🎨**：使用Vue.js构建的直观和响应式设计。
- **可扩展后端 🚀**：由Spring Boot驱动，提供高性能和可扩展性。
- **高效数据处理 💾**：使用MyBatis Plus实现无缝的ORM和数据库管理。
- **持久化存储 🗄️**：可靠的数据存储使用MySQL。
- **AI任务拆分 ✂️**：自动将复杂任务拆分为可管理的子任务，提高生产力。

------

## 主要功能

### AI任务拆分功能 ✂️：

- 通过集成 LLM，MicroTodo 可以自动将复杂任务拆分成多个更小的子任务，降低用户的认知负荷，提高任务完成效率。
- **实现**：通过 `OpenAiServiceImpl` 类中的 `getSubTasksDesc` 方法，使用 AI 模型将复杂任务自动拆分成更小的子任务。
- **示例：**从测试代码 `OpenAiServiceImplTest.java` 可以看到，比如"做蛋炒饭"这样的任务会被拆分成多个子任务。

### 智能任务管理系统 📋

- MicroTodo 提供了一套智能的任务管理机制，包括任务的自动序列化、父子关系管理、状态追踪以及版本控制等，让用户可以更加灵活、高效地管理自己的任务。

- 实现方式：通过 `TaskServiceImpl` 类实现智能的任务排序和管理。

- 特点：

  - 自动序列化管理（`sequence` 字段）

  - 父子任务关系管理（`parentId` 字段）

  - 任务状态追踪（`beDeleted` 字段用于标记完成状态）

  - 版本控制（`version` 字段）

###  AI 个性化建议 🤖

- 通过集成 GPT 模型，MicroTodo 可以根据用户的行为习惯，提供个性化的任务管理建议、时间管理优化建议以及任务规划建议等，帮助用户更好地完成任务，提升效率。

- **实现方式：**通过 `IOpenAiService` 接口提供的 `getAnswer` 方法，集成了 GPT 模型来提供：

- **时间管理提示**：基于任务历史数据，提供优化的时间分配策略，帮助用户更有效地利用时间。
- **个性化推荐**：通过分析用户的任务完成模式和时间管理习惯，提供个性化的任务和时间管理建议。例如，如果AI检测到用户经常在晚上拖延任务，可能会建议在上午完成重要任务。

### 现代化前端界面 🎨

- MicroTodo 采用 Vue.js 构建了一套美观、响应式的现代化前端界面，包括导航栏、侧边栏、任务列表、设置页面等，让用户拥有更加流畅、友好的使用体验。

- **实现方式：**使用 Vue.js 构建响应式界面，包含：

  - 导航栏（`NavBar.vue`）

  - 侧边栏（`SideBar.vue`）

  - 任务列表（`TaskList.vue`）

  - 设置页面（`Settings.vue`）

### 多语言国际化支持 🌍

- MicroTodo 支持中英文双语界面，用户可以根据自己的语言偏好自由切换。

- **实现方式：**从项目结构可以看到 `i18n` 目录下有 `zh.js` 和 `en.js`，支持中英文双语界面。

### 用户认证系统 🔐

- MicroTodo 提供了完善的用户认证系统，支持常规登录以及 GitHub OAuth 登录等，并采用安全的密码加密存储机制，保障用户的账号安全。

- **实现方式：**

  - 常规登录（`LoginPage.vue`）

  - GitHub OAuth 登录（`GithubCallback.vue`）

  - 用户管理（`UserController.java`）

  - 安全配置（`SecurityConfig.java`）

### 数据分析功能 📊（开发 ing）：

- **生产力分析**：提供任务完成率、时间花费等数据的详细分析，帮助用户了解他们的工作效率。
- **目标跟踪**：设置和跟踪个人或专业目标，提供可视化的进度报告。

------

## 技术栈

### 前端技术栈

- Vue.js 2.6.14 - 渐进式JavaScript框架
- Vuex 3.6.2 - 状态管理
- Vue Router 3.5.1 - 路由管理
- Element UI 2.15.14 - UI组件库
- Font Awesome 6.5.2 - 图标库
- Remixicon 4.2.0 - 图标库
- Vue CLI - 项目脚手架
- ESLint - 代码检查工具
- Sass - CSS预处理器
- Axios 1.7.2 - HTTP请求库
- Vue Axios 3.5.2 - Axios的Vue集成

### 后端技术栈

- Spring Boot 2.6.13 - 应用开发框架
- Spring Security - 安全框架
- Spring OAuth2 Client - 认证授权
- MyBatis Plus 3.4.1 - ORM框架
- MySQL - 关系型数据库

## 快速开始 🚀

1. **克隆仓库 📥**：

   ```sh
   git clone https://github.com/yourusername/MicroTodo.git
   cd micro-todo
   ```

2. **后端设置 🛠️**：

   - **先决条件**：确保已安装Java 17+ 和 Maven。

   - **配置**：更新 `application.properties` 文件中的MySQL数据库凭证。
   - **构建和运行**：

   ```sh
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **前端设置 💻**：

   - **先决条件**：确保已安装 Node.js 和 npm。
   - **安装依赖**：

   ```sh
   cd frontend
   npm install
   ```

   - **运行应用程序**

   ```sh
   npm run serve
   ```

4. **数据库设置 🗃️**：

   - **初始化数据库**：运行提供的SQL脚本以设置数据库架构。

   - **迁移**：使用MyBatis应用任何待处理的迁移。

## 未来规划 🔮

### 1. 多人协作功能 👥

未来 MicroTodo 将支持多人协同办公，用户可以邀请团队成员共同参与任务，提高团队的协作效率。

### 2. 智能时间管理 ⏰

通过集成先进的 AI 算法，MicroTodo 将能够智能预估任务完成时间，并根据用户的行为数据和任务特点，动态优化用户的任务时间安排，从而进一步提升用户的效率。

## 贡献指南

我们欢迎社区的贡献。请按照以下指南顺利进行贡献：

1. **Fork仓库 🍴**：创建您的分叉并在上面工作。
2. **创建分支 🌿**：为您的分支使用描述性名称（例如 `feature-ai-improvements`）。
3. **进行更改 🛠️**：确保您的代码遵循项目的编码标准。
4. **提交和推送 📤**：编写清晰简洁的提交消息。
5. **拉取请求 📬**：提交一个详细描述更改的拉取请求。

## 许可证

此项目根据MIT许可证授权。有关详细信息，请参见 LICENSE 文件。

## 联系信息

如有任何问题或需要支持，请联系：

- **电子邮件 📧**：suki513737@gmail.com
- **GitHub问题 🐞**： [micro todo Issues](https://github.com/blessonism/MicroTodo/issues)
