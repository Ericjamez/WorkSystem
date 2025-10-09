# 学生作业管理系统 (Student Homework Management System)

基于 Java Spring Boot + MyBatis + MySQL 开发的学生作业管理系统

## 功能特性

- 学生信息管理
- 作业发布管理
- 作业提交与批改
- 成绩管理
- RESTful API 接口

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **ORM框架**: MyBatis 3.0.3
- **数据库**: MySQL 8.0+
- **构建工具**: Maven
- **Java版本**: 17

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

1. 确保已安装 Java 17 和 Maven
2. 配置好 MySQL 数据库
3. 运行以下命令:
```bash
mvn clean install
mvn spring-boot:run
```

4. 应用将在 http://localhost:8080 启动

## 示例数据

系统已包含示例数据:
- 3名学生
- 3个作业任务
- 3个作业提交记录

## 开发说明

- 使用 Lombok 简化实体类代码
- 使用 MyBatis 注解方式配置 SQL 映射
- 支持中文响应消息
- 包含完整的错误处理机制
