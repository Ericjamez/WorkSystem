<template>
  <div class="admin-dashboard">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统概览</span>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon" style="background: #409EFF;">
                <el-icon><School /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ stats.collegeCount || 0 }}</div>
                <div class="stat-label">学院数量</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon" style="background: #67C23A;">
                <el-icon><Reading /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ stats.majorCount || 0 }}</div>
                <div class="stat-label">专业数量</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon" style="background: #E6A23C;">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ stats.classCount || 0 }}</div>
                <div class="stat-label">班级数量</div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon" style="background: #F56C6C;">
                <el-icon><Avatar /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ stats.studentCount || 0 }}</div>
                <div class="stat-label">学生数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>快速操作</span>
            </template>
            <div class="quick-actions">
              <el-button type="primary" @click="$router.push('/admin/colleges')">
                <el-icon><School /></el-icon>
                管理学院
              </el-button>
              <el-button type="success" @click="$router.push('/admin/majors')">
                <el-icon><Reading /></el-icon>
                管理专业
              </el-button>
              <el-button type="warning" @click="$router.push('/admin/classes')">
                <el-icon><User /></el-icon>
                管理班级
              </el-button>
              <el-button type="danger" @click="$router.push('/admin/students')">
                <el-icon><Avatar /></el-icon>
                管理学生
              </el-button>
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card>
            <template #header>
              <span>系统信息</span>
            </template>
            <div class="system-info">
              <p><strong>系统版本：</strong>学生作业管理系统 v1.0</p>
              <p><strong>管理员账号：</strong>{{ user.username }}</p>
              <p><strong>登录时间：</strong>{{ loginTime }}</p>
              <p><strong>系统状态：</strong><el-tag type="success">运行正常</el-tag></p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { adminApi } from '../services/api.js'

export default {
  name: 'AdminDashboard',
  data() {
    return {
      user: {},
      stats: {},
      loginTime: new Date().toLocaleString('zh-CN')
    }
  },
  async mounted() {
    this.loadUserInfo()
    await this.loadStats()
  },
  methods: {
    loadUserInfo() {
      const userData = localStorage.getItem('user')
      if (userData) {
        this.user = JSON.parse(userData)
      }
    },
    
    async loadStats() {
      try {
        // 这里可以添加获取统计数据的API调用
        // 暂时使用模拟数据
        this.stats = {
          collegeCount: 3,
          majorCount: 4,
          classCount: 5,
          studentCount: 3
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.stat-card {
  margin-bottom: 0;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
  margin-right: 15px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.quick-actions .el-button {
  justify-content: flex-start;
  padding: 12px 20px;
}

.system-info p {
  margin: 10px 0;
  font-size: 14px;
}
</style>
