# 学生作业管理系统 (Student Homework Management System)

基于 Java Spring Boot + Vue 3 开发的全栈学生作业管理系统，支持教师端和学生端双登录系统

## 功能特性

### 教师端功能
- 教师登录认证
- 作业发布和管理
- 批改学生作业
- 查看学生提交情况
- 与学生私信沟通

### 学生端功能
- 学生登录认证
- 查看作业列表
- 提交作业
- 查看批改结果
- 与教师私信沟通

### 通用功能
- 用户注册（教师/学生）
- 私信系统
- 角色权限管理

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.0
- **ORM框架**: MyBatis 3.0.3
- **数据库**: MySQL 8.0+
- **构建工具**: Maven
- **Java版本**: 17

### 前端
- **框架**: Vue 3
- **UI组件库**: Element Plus
- **路由**: Vue Router 4
- **HTTP客户端**: Axios
- **构建工具**: Vite
- **开发语言**: JavaScript/TypeScript

## 数据库配置

1. 创建 MySQL 数据库:
```sql
CREATE DATABASE student_homework_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `src/main/resources/application.properties` 中的数据库连接信息:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_homework_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. 运行 `src/main/resources/schema.sql` 创建表结构和初始化数据

## 项目结构

### 后端结构
```
src/main/java/com/example/studenthomeworksystem/
├── entity/          # 实体类
│   ├── User.java                    # 用户实体（教师/学生）
│   ├── Student.java                 # 学生扩展信息
│   ├── Teacher.java                 # 教师扩展信息
│   ├── Homework.java                # 作业实体
│   ├── HomeworkSubmission.java      # 作业提交实体
│   └── PrivateMessage.java          # 私信实体
├── mapper/          # MyBatis 映射器
│   ├── UserMapper.java
│   ├── StudentMapper.java
│   ├── TeacherMapper.java
│   ├── HomeworkMapper.java
│   ├── HomeworkSubmissionMapper.java
│   └── PrivateMessageMapper.java
├── service/         # 业务逻辑层
│   ├── UserService.java
│   ├── StudentService.java
│   ├── TeacherService.java
│   ├── HomeworkService.java
│   ├── HomeworkSubmissionService.java
│   └── PrivateMessageService.java
├── controller/      # REST 控制器
│   ├── AuthController.java          # 认证控制器
│   ├── UserController.java          # 用户管理控制器
│   ├── HomeworkController.java
│   ├── HomeworkSubmissionController.java
│   └── PrivateMessageController.java # 私信控制器
└── StudentHomeworkSystemApplication.java  # 主应用类
```

### 前端结构
```
frontend/
├── src/
│   ├── views/           # 页面组件
│   │   ├── Login.vue            # 登录页面
│   │   ├── StudentHome.vue      # 学生端首页
│   │   ├── TeacherHome.vue      # 教师端首页
│   │   ├── HomeworkList.vue     # 作业管理
│   │   ├── SubmissionList.vue   # 提交管理
│   │   └── Messages.vue         # 私信管理
│   ├── router/          # 路由配置
│   │   └── index.js
│   ├── services/        # API服务
│   │   └── api.js
│   ├── App.vue          # 根组件
│   └── main.js          # 入口文件
├── package.json         # 项目配置
├── vite.config.js       # Vite配置
└── index.html           # HTML模板
```

## API 接口

### 认证管理
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register/student` - 学生注册
- `POST /api/auth/register/teacher` - 教师注册

### 用户管理
- `GET /api/users/students` - 获取所有学生
- `GET /api/users/teachers` - 获取所有教师
- `GET /api/users/{id}` - 根据ID获取用户

### 作业管理
- `GET /api/homework` - 获取所有作业
- `GET /api/homework/teacher/{teacherId}` - 根据教师ID获取作业
- `GET /api/homework/active` - 获取有效作业
- `POST /api/homework` - 创建作业
- `PUT /api/homework/{id}` - 更新作业
- `DELETE /api/homework/{id}` - 删除作业

### 作业提交管理
- `GET /api/submissions` - 获取所有提交
- `POST /api/submissions` - 提交作业
- `POST /api/submissions/{id}/grade` - 批改作业
- `GET /api/submissions/check-submission` - 检查是否已提交

### 私信管理
- `GET /api/messages/user/{userId}` - 获取用户消息
- `GET /api/messages/conversation/{userId1}/{userId2}` - 获取对话
- `POST /api/messages/send` - 发送消息
- `POST /api/messages/mark-read/{messageId}` - 标记为已读
- `GET /api/messages/unread-count/{userId}` - 获取未读消息数

## 运行项目

### 后端运行
1. 确保已安装 Java 17 和 Maven
2. 配置好 MySQL 数据库
3. 运行以下命令:
```bash
mvn clean install
mvn spring-boot:run
```
4. 后端服务将在 http://localhost:8080 启动

### 前端运行
1. 确保已安装 Node.js (推荐 16+)
2. 进入前端目录并安装依赖:
```bash
cd frontend
npm install
```
3. 启动开发服务器:
```bash
npm run dev
```
4. 前端应用将在 http://localhost:3000 启动

### 完整启动流程
1. 启动后端服务 (端口 8080)
2. 启动前端开发服务器 (端口 3000)
3. 访问 http://localhost:3000 使用系统

## 示例数据

系统已包含示例数据:
- 2名教师（张老师、李老师）
- 3名学生（张三、李四、王五）
- 3个作业任务
- 3个作业提交记录
- 示例私信记录

## 前端功能

### 教师端功能
- **教师首页**: 教师工作台、快速导航
- **作业管理**: 作业发布、编辑、状态管理、截止时间控制
- **批改作业**: 查看学生提交、评分、评语管理
- **私信管理**: 与学生沟通、消息通知

### 学生端功能
- **学生首页**: 学生工作台、快速导航
- **作业列表**: 查看作业、提交作业
- **我的提交**: 查看提交记录、批改结果
- **私信管理**: 与教师沟通、消息通知

### 技术特点
- 响应式设计，适配不同屏幕尺寸
- 使用 Element Plus 组件库，界面美观
- Vue Router 实现单页面应用导航和角色权限控制
- Axios 封装 RESTful API 调用
- 完整的表单验证和错误处理
- 实时消息通知系统

## 开发说明

### 后端
- 使用 Lombok 简化实体类代码
- 使用 MyBatis 注解方式配置 SQL 映射
- 支持中文响应消息
- 包含完整的错误处理机制

### 前端
- Vue 3 Composition API 开发
- Element Plus 组件库提供现代化UI
- Vite 构建工具提供快速开发体验
- 支持热重载开发
