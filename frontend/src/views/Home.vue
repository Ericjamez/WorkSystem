<template>
  <div class="home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用学生作业管理系统</span>
        </div>
      </template>
      
      <div class="welcome-content">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-statistic title="学生总数" :value="stats.studentCount" />
          </el-col>
          <el-col :span="8">
            <el-statistic title="作业总数" :value="stats.homeworkCount" />
          </el-col>
          <el-col :span="8">
            <el-statistic title="提交总数" :value="stats.submissionCount" />
          </el-col>
        </el-row>

        <el-divider />

        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>系统功能</span>
              </template>
              <div class="feature-list">
                <el-descriptions :column="1" border>
                  <el-descriptions-item label="学生管理">
                    管理学生基本信息，包括添加、修改、删除学生
                  </el-descriptions-item>
                  <el-descriptions-item label="作业管理">
                    发布和管理作业任务，设置截止时间
                  </el-descriptions-item>
                  <el-descriptions-item label="提交管理">
                    查看学生提交的作业，进行批改和评分
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>快速导航</span>
              </template>
              <div class="quick-nav">
                <el-button 
                  type="primary" 
                  @click="$router.push('/students')"
                  style="width: 100%; margin-bottom: 10px;"
                >
                  <el-icon><User /></el-icon>
                  学生管理
                </el-button>
                <el-button 
                  type="success" 
                  @click="$router.push('/homework')"
                  style="width: 100%; margin-bottom: 10px;"
                >
                  <el-icon><Document /></el-icon>
                  作业管理
                </el-button>
                <el-button 
                  type="warning" 
                  @click="$router.push('/submissions')"
                  style="width: 100%;"
                >
                  <el-icon><EditPen /></el-icon>
                  提交管理
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { studentApi, homeworkApi, submissionApi } from '../services/api.js'

export default {
  name: 'Home',
  data() {
    return {
      stats: {
        studentCount: 0,
        homeworkCount: 0,
        submissionCount: 0
      }
    }
  },
  async mounted() {
    await this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const [students, homework, submissions] = await Promise.all([
          studentApi.getAllStudents(),
          homeworkApi.getAllHomework(),
          submissionApi.getAllSubmissions()
        ])
        
        this.stats.studentCount = students.length
        this.stats.homeworkCount = homework.length
        this.stats.submissionCount = submissions.length
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.$message.error('加载统计数据失败')
      }
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}

.welcome-card {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.welcome-content {
  padding: 20px;
}

.feature-list {
  padding: 10px;
}

.quick-nav {
  padding: 10px;
}

.el-statistic {
  text-align: center;
}
</style>
