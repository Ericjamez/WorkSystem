<template>
  <div id="app">
    <el-container>
      <!-- 侧边栏导航 -->
      <el-aside width="200px">
        <el-menu
          :default-active="$route.path"
          router
          class="sidebar-menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <div class="logo">
            <h3>作业管理系统</h3>
          </div>
          <el-menu-item index="/">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/students">
            <el-icon><User /></el-icon>
            <span>学生管理</span>
          </el-menu-item>
          <el-menu-item index="/homework">
            <el-icon><Document /></el-icon>
            <span>作业管理</span>
          </el-menu-item>
          <el-menu-item index="/submissions">
            <el-icon><EditPen /></el-icon>
            <span>提交管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区域 -->
      <el-container>
        <el-header style="text-align: center; font-size: 24px; background: #409EFF; color: white;">
          学生作业管理系统
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  height: 100vh;
}

.sidebar-menu {
  height: 100vh;
  border-right: none;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #2b2f3a;
  color: white;
}

.el-header {
  padding: 0;
  line-height: 60px;
}

.el-main {
  padding: 20px;
  background: #f0f2f5;
}
</style>
