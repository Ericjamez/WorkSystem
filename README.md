# 学生作业管理系统 (Student Homework Management System)

基于 Java Spring Boot + Vue 3 开发的全栈学生作业管理系统

## 功能特性

- 学生信息管理
- 作业发布管理
- 作业提交与批改
- 成绩管理
- RESTful API 接口

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
│   ├── Student.java
│   ├── Homework.java
│   └── HomeworkSubmission.java
├── mapper/          # MyBatis 映射器
│   ├── StudentMapper.java
│   ├── HomeworkMapper.java
│   └── HomeworkSubmissionMapper.java
├── service/         # 业务逻辑层
│   ├── StudentService.java
│   ├── HomeworkService.java
│   └── HomeworkSubmissionService.java
├── controller/      # REST 控制器
│   ├── StudentController.java
│   ├── HomeworkController.java
│   └── HomeworkSubmissionController.java
└── StudentHomeworkSystemApplication.java  # 主应用类
```

### 前端结构
```
frontend/
├── src/
│   ├── views/           # 页面组件
│   │   ├── Home.vue     # 首页
│   │   ├── StudentList.vue      # 学生管理
│   │   ├── HomeworkList.vue     # 作业管理
│   │   └── SubmissionList.vue   # 提交管理
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

### 学生管理
- `GET /api/students` - 获取所有学生
- `GET /api/students/{id}` - 根据ID获取学生
- `POST /api/students` - 创建学生
- `PUT /api/students/{id}` - 更新学生信息
- `DELETE /api/students/{id}` - 删除学生

### 作业管理
- `GET /api/homework` - 获取所有作业
- `GET /api/homework/active` - 获取有效作业
- `POST /api/homework` - 创建作业
- `PUT /api/homework/{id}` - 更新作业
- `DELETE /api/homework/{id}` - 删除作业

### 作业提交管理
- `GET /api/submissions` - 获取所有提交
- `POST /api/submissions` - 提交作业
- `POST /api/submissions/{id}/grade` - 批改作业
- `GET /api/submissions/check-submission` - 检查是否已提交

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
- 3名学生
- 3个作业任务
- 3个作业提交记录

## 前端功能

### 主要页面
- **首页**: 系统概览、统计数据展示、快速导航
- **学生管理**: 学生信息的增删改查、搜索筛选
- **作业管理**: 作业发布、编辑、状态管理、截止时间控制
- **提交管理**: 作业提交、批改评分、评语管理

### 技术特点
- 响应式设计，适配不同屏幕尺寸
- 使用 Element Plus 组件库，界面美观
- Vue Router 实现单页面应用导航
- Axios 封装 RESTful API 调用
- 完整的表单验证和错误处理

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
