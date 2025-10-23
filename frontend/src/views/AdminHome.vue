<template>
  <div class="admin-home">
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
            <h3>系统管理</h3>
          </div>
          <el-menu-item index="/admin/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/admin/colleges">
            <el-icon><School /></el-icon>
            <span>学院管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/majors">
            <el-icon><Reading /></el-icon>
            <span>专业管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/classes">
            <el-icon><User /></el-icon>
            <span>班级管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/students">
            <el-icon><Avatar /></el-icon>
            <span>学生管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区域 -->
      <el-container>
        <el-header style="background: #67C23A; color: white;">
          <div class="header-content">
            <div class="header-title">
              <h2>学生作业管理系统 - 管理员端</h2>
            </div>
            <div class="header-user">
              <span>欢迎，{{ user.name }}</span>
              <el-tag type="success" style="margin-left: 10px;">管理员</el-tag>
              <el-button 
                type="text" 
                style="color: white; margin-left: 15px;" 
                @click="handleLogout"
              >
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-button>
            </div>
          </div>
        </el-header>
        <el-main>
          <div v-if="$route.path === '/admin/home'">
            <AdminDashboard />
          </div>
          <div v-else-if="$route.path === '/admin/colleges'">
            <CollegeManagement />
          </div>
          <div v-else-if="$route.path === '/admin/majors'">
            <MajorManagement />
          </div>
          <div v-else-if="$route.path === '/admin/classes'">
            <ClassManagement />
          </div>
          <div v-else-if="$route.path === '/admin/students'">
            <StudentManagement />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import AdminDashboard from './AdminDashboard.vue'
import CollegeManagement from './CollegeManagement.vue'
import MajorManagement from './MajorManagement.vue'
import ClassManagement from './ClassManagement.vue'
import StudentManagement from './StudentManagement.vue'

export default {
  name: 'AdminHome',
  components: {
    AdminDashboard,
    CollegeManagement,
    MajorManagement,
    ClassManagement,
    StudentManagement
  },
  data() {
    return {
      user: {}
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      const userData = localStorage.getItem('user')
      if (userData) {
        this.user = JSON.parse(userData)
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
.admin-home {
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
  padding: 0 20px;
  line-height: 60px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header-title h2 {
  margin: 0;
  font-size: 20px;
  font-weight: normal;
}

.header-user {
  display: flex;
  align-items: center;
  font-size: 16px;
}

.el-main {
  padding: 20px;
  background: #f0f2f5;
}
</style>
