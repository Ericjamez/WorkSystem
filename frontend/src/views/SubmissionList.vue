<template>
  <div class="submission-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>作业提交管理</span>
          <el-button type="primary" @click="showSubmitDialog = true">
            <el-icon><Plus /></el-icon>
            提交作业
          </el-button>
        </div>
      </template>

      <!-- 搜索和筛选 -->
      <div class="filter-container">
        <el-input
          v-model="searchText"
          placeholder="搜索学生姓名或作业标题"
          style="width: 300px; margin-right: 10px;"
          clearable
        />
        <el-select
          v-model="selectedStudent"
          placeholder="选择学生"
          style="width: 200px; margin-right: 10px;"
          clearable
        >
          <el-option
            v-for="student in students"
            :key="student.id"
            :label="student.name"
            :value="student.id"
          />
        </el-select>
        <el-select
          v-model="selectedHomework"
          placeholder="选择作业"
          style="width: 200px; margin-right: 10px;"
          clearable
        >
          <el-option
            v-for="hw in homework"
            :key="hw.id"
            :label="hw.title"
            :value="hw.id"
          />
        </el-select>
        <el-button type="primary" @click="loadSubmissions">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
      </div>

      <!-- 提交表格 -->
      <el-table
        :data="filteredSubmissions"
        v-loading="loading"
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="studentIdNumber" label="学号" width="120" />
        <el-table-column prop="homeworkTitle" label="作业标题" min-width="200" />
        <el-table-column prop="courseName" label="课程名称" width="150" />
        <el-table-column prop="content" label="提交内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="score" label="分数" width="100">
          <template #default="scope">
            <span :style="{ color: scope.row.score ? '#67C23A' : '#909399' }">
              {{ scope.row.score || '未评分' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="teacherComment" label="教师评语" min-width="200" show-overflow-tooltip />
        <el-table-column prop="submitTime" label="提交时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.submitTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              @click="handleGrade(scope.row)"
              v-if="!scope.row.score"
            >
              批改
            </el-button>
            <el-button
              size="small"
              type="warning"
              @click="handleGrade(scope.row)"
              v-else
            >
              修改评分
            </el-button>
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 提交作业对话框 -->
    <el-dialog
      title="提交作业"
      v-model="showSubmitDialog"
      width="600px"
    >
      <el-form
        :model="submitForm"
        :rules="submitRules"
        ref="submitFormRef"
        label-width="100px"
      >
        <el-form-item label="学生" prop="studentId">
          <el-select
            v-model="submitForm.studentId"
            placeholder="选择学生"
            style="width: 100%;"
          >
            <el-option
              v-for="student in students"
              :key="student.id"
              :label="`${student.name} (${student.studentId})`"
              :value="student.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="作业" prop="homeworkId">
          <el-select
            v-model="submitForm.homeworkId"
            placeholder="选择作业"
            style="width: 100%;"
          >
            <el-option
              v-for="hw in activeHomework"
              :key="hw.id"
              :label="`${hw.title} - ${hw.courseName}`"
              :value="hw.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="提交内容" prop="content">
          <el-input
            v-model="submitForm.content"
            type="textarea"
            :rows="6"
            placeholder="请输入作业内容..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSubmitDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">
          提交
        </el-button>
      </template>
    </el-dialog>

    <!-- 批改作业对话框 -->
    <el-dialog
      title="批改作业"
      v-model="showGradeDialog"
      width="600px"
    >
      <el-form
        :model="gradeForm"
        :rules="gradeRules"
        ref="gradeFormRef"
        label-width="100px"
      >
        <el-form-item label="学生">
          <el-input :value="currentSubmission?.studentName" disabled />
        </el-form-item>
        <el-form-item label="作业">
          <el-input :value="currentSubmission?.homeworkTitle" disabled />
        </el-form-item>
        <el-form-item label="提交内容">
          <el-input
            :value="currentSubmission?.content"
            type="textarea"
            :rows="4"
            disabled
          />
        </el-form-item>
        <el-form-item label="分数" prop="score">
          <el-input-number
            v-model="gradeForm.score"
            :min="0"
            :max="currentSubmission?.totalScore || 100"
          />
        </el-form-item>
        <el-form-item label="评语" prop="teacherComment">
          <el-input
            v-model="gradeForm.teacherComment"
            type="textarea"
            :rows="4"
            placeholder="请输入评语..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showGradeDialog = false">取消</el-button>
        <el-button type="primary" @click="handleGradeSubmit">
          提交评分
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { submissionApi, studentApi, homeworkApi } from '../services/api.js'

export default {
  name: 'SubmissionList',
  data() {
    return {
      submissions: [],
      filteredSubmissions: [],
      students: [],
      homework: [],
      activeHomework: [],
      loading: false,
      searchText: '',
      selectedStudent: null,
      selectedHomework: null,
      showSubmitDialog: false,
      showGradeDialog: false,
      currentSubmission: null,
      submitForm: {
        studentId: '',
        homeworkId: '',
        content: ''
      },
      gradeForm: {
        score: null,
        teacherComment: ''
      },
      submitRules: {
        studentId: [
          { required: true, message: '请选择学生', trigger: 'change' }
        ],
        homeworkId: [
          { required: true, message: '请选择作业', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入提交内容', trigger: 'blur' }
        ]
      },
      gradeRules: {
        score: [
          { required: true, message: '请输入分数', trigger: 'blur' }
        ]
      }
    }
  },
  async mounted() {
    await Promise.all([
      this.loadSubmissions(),
      this.loadStudents(),
      this.loadHomework()
    ])
  },
  methods: {
    async loadSubmissions() {
      this.loading = true
      try {
        const response = await submissionApi.getAllSubmissions()
        this.submissions = response
        this.filteredSubmissions = response
      } catch (error) {
        console.error('加载提交列表失败:', error)
        this.$message.error('加载提交列表失败')
      } finally {
        this.loading = false
      }
    },

    async loadStudents() {
      try {
        const response = await studentApi.getAllStudents()
        this.students = response
      } catch (error) {
        console.error('加载学生列表失败:', error)
      }
    },

    async loadHomework() {
      try {
        const [allHomework, activeHomework] = await Promise.all([
          homeworkApi.getAllHomework(),
          homeworkApi.getActiveHomework()
        ])
        this.homework = allHomework
        this.activeHomework = activeHomework
      } catch (error) {
        console.error('加载作业列表失败:', error)
      }
    },

    resetSearch() {
      this.searchText = ''
      this.selectedStudent = null
      this.selectedHomework = null
      this.filteredSubmissions = this.submissions
    },

    handleGrade(submission) {
      this.currentSubmission = submission
      this.gradeForm = {
        score: submission.score || null,
        teacherComment: submission.teacherComment || ''
      }
      this.showGradeDialog = true
    },

    async handleDelete(submission) {
      try {
        await this.$confirm('确定要删除这个提交吗？', '提示', {
          type: 'warning'
        })
        
        const success = await submissionApi.deleteSubmission(submission.id)
        if (success) {
          this.$message.success('删除成功')
          await this.loadSubmissions()
        } else {
          this.$message.error('删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除提交失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.submitFormRef.validate()
        
        const success = await submissionApi.submitHomework(this.submitForm)
        if (success) {
          this.$message.success('提交成功')
          this.showSubmitDialog = false
          await this.loadSubmissions()
          this.resetSubmitForm()
        } else {
          this.$message.error('提交失败，可能已经提交过该作业')
        }
      } catch (error) {
        console.error('提交作业失败:', error)
      }
    },

    async handleGradeSubmit() {
      try {
        await this.$refs.gradeFormRef.validate()
        
        const success = await submissionApi.gradeSubmission(
          this.currentSubmission.id,
          this.gradeForm
        )
        if (success) {
          this.$message.success('评分成功')
          this.showGradeDialog = false
          await this.loadSubmissions()
          this.resetGradeForm()
        } else {
          this.$message.error('评分失败')
        }
      } catch (error) {
        console.error('评分失败:', error)
      }
    },

    resetSubmitForm() {
      this.submitForm = {
        studentId: '',
        homeworkId: '',
        content: ''
      }
      if (this.$refs.submitFormRef) {
        this.$refs.submitFormRef.resetFields()
      }
    },

    resetGradeForm() {
      this.gradeForm = {
        score: null,
        teacherComment: ''
      }
      this.currentSubmission = null
    },

    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString('zh-CN')
    }
  },
  watch: {
    searchText(val) {
      this.filteredSubmissions = this.submissions.filter(sub =>
        sub.studentName.includes(val) || sub.homeworkTitle.includes(val)
      )
    },
    selectedStudent(val) {
      if (val) {
        this.filteredSubmissions = this.submissions.filter(sub => sub.studentId === val)
      } else {
        this.filteredSubmissions = this.submissions
      }
    },
    selectedHomework(val) {
      if (val) {
        this.filteredSubmissions = this.submissions.filter(sub => sub.homeworkId === val)
      } else {
        this.filteredSubmissions = this.submissions
      }
    }
  }
}
</script>

<style scoped>
.submission-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-container {
  margin-bottom: 20px;
}
</style>
