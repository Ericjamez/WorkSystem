<template>
  <div class="teacher-home">
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
            <h3>教师作业系统</h3>
          </div>
          <el-menu-item index="/teacher/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/teacher/homework">
            <el-icon><Document /></el-icon>
            <span>作业管理</span>
          </el-menu-item>
          <el-menu-item index="/teacher/submissions">
            <el-icon><EditPen /></el-icon>
            <span>批改作业</span>
          </el-menu-item>
          <el-menu-item index="/teacher/messages">
            <el-icon><ChatDotRound /></el-icon>
            <span>私信</span>
            <el-badge v-if="unreadCount > 0" :value="unreadCount" class="badge" />
          </el-menu-item>
          <el-menu-item @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出登录</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区域 -->
      <el-container>
        <el-header style="text-align: center; font-size: 24px; background: #E6A23C; color: white;">
          学生作业管理系统 - 教师端
          <div class="user-info">
            <span>欢迎，{{ user.name }}</span>
            <el-tag type="warning" style="margin-left: 10px;">教师</el-tag>
          </div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { messageApi } from '../services/api.js'

export default {
  name: 'TeacherHome',
  data() {
    return {
      user: {},
      unreadCount: 0
    }
  },
  async mounted() {
    this.loadUserInfo()
    await this.loadUnreadCount()
    // 每30秒刷新未读消息数
    this.refreshInterval = setInterval(this.loadUnreadCount, 30000)
  },
  beforeUnmount() {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval)
    }
  },
  methods: {
    loadUserInfo() {
      const userData = localStorage.getItem('user')
      if (userData) {
        this.user = JSON.parse(userData)
      }
    },
    
    async loadUnreadCount() {
      try {
        if (this.user.id) {
          const response = await messageApi.getUnreadCount(this.user.id)
          if (response.success) {
            this.unreadCount = response.count
          }
        }
      } catch (error) {
        console.error('获取未读消息数失败:', error)
      }
    },
    
    handleLogout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('user')
        this.$router.push('/login')
        this.$message.success('已退出登录')
      })
    }
  }
}
</script>

<style scoped>
.teacher-home {
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
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  font-size: 16px;
}

.el-main {
  padding: 20px;
  background: #f0f2f5;
}

.badge {
  margin-left: 10px;
}
</style>
