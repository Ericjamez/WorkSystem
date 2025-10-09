@echo off
echo ========================================
echo   学生作业管理系统 - 前端启动脚本
echo ========================================
echo.

echo 1. 检查 Node.js 是否安装...
node --version
if %errorlevel% neq 0 (
    echo [错误] Node.js 未安装或未配置环境变量
    echo 请先安装 Node.js: https://nodejs.org/
    pause
    exit /b 1
)

echo.
echo 2. 检查 npm 是否可用...
npm --version
if %errorlevel% neq 0 (
    echo [错误] npm 不可用
    pause
    exit /b 1
)

echo.
echo 3. 安装依赖...
npm install
if %errorlevel% neq 0 (
    echo [错误] 依赖安装失败
    pause
    exit /b 1
)

echo.
echo 4. 启动开发服务器...
echo 前端将在 http://localhost:3000 启动
echo 请确保后端服务在 http://localhost:8081 运行
echo.
npm run dev

pause
