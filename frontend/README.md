# 学生作业管理系统 - 前端

基于 Vue 3 + Element Plus 开发的学生作业管理系统前端。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 构建工具
- **Element Plus** - UI 组件库
- **Vue Router** - 路由管理
- **Axios** - HTTP 客户端

## 功能特性

- 🎯 学生信息管理（增删改查）
- 📚 作业发布管理
- 📝 作业提交与批改
- 📊 数据统计展示
- 🔍 智能搜索筛选
- 🎨 现代化 UI 设计

## 项目结构

```
frontend/
├── src/
│   ├── components/     # 公共组件
│   ├── views/         # 页面组件
│   │   ├── Home.vue           # 首页
│   │   ├── StudentList.vue    # 学生管理
│   │   ├── HomeworkList.vue   # 作业管理
│   │   └── SubmissionList.vue # 提交管理
│   ├── services/      # API 服务
│   │   └── api.js     # 接口封装
│   ├── router/        # 路由配置
│   │   └── index.js
│   ├── App.vue        # 根组件
│   └── main.js        # 入口文件
├── package.json
├── vite.config.js
└── index.html
```

## 安装和运行

### 1. 安装依赖
```bash
cd frontend
npm install
```

### 2. 启动开发服务器
```bash
npm run dev
```

应用将在 http://localhost:3000 启动

### 3. 构建生产版本
```bash
npm run build
```

## 后端 API 要求

前端需要连接后端 Spring Boot 应用，确保后端服务在 http://localhost:8081 运行。

### API 端点
- `GET /api/students` - 获取学生列表
- `POST /api/students` - 创建学生
- `PUT /api/students/{id}` - 更新学生
- `DELETE /api/students/{id}` - 删除学生

- `GET /api/homework` - 获取作业列表
- `POST /api/homework` - 发布作业
- `PUT /api/homework/{id}` - 更新作业
- `DELETE /api/homework/{id}` - 删除作业

- `GET /api/submissions` - 获取提交列表
- `POST /api/submissions` - 提交作业
- `POST /api/submissions/{id}/grade` - 批改作业
- `DELETE /api/submissions/{id}` - 删除提交

## 开发说明

### 代理配置
开发环境下，Vite 配置了代理，将 `/api` 请求转发到后端服务：
```javascript
// vite.config.js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8081',
      changeOrigin: true
    }
  }
}
```

### 组件说明
- **Home.vue** - 首页，展示系统概览和统计数据
- **StudentList.vue** - 学生管理，支持增删改查和搜索
- **HomeworkList.vue** - 作业管理，支持发布作业和状态筛选
- **SubmissionList.vue** - 提交管理，支持作业提交和批改评分

### API 服务
所有后端接口调用都封装在 `src/services/api.js` 中，提供统一的错误处理和请求拦截。

## 浏览器兼容性

- Chrome 60+
- Firefox 60+
- Safari 12+
- Edge 79+

## 许可证

MIT License
