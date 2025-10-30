<template>
  <div class="submission-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isStudent ? '我的提交' : '作业提交管理' }}</span>
          <el-button 
            v-if="isStudent" 
            type="primary" 
            @click="showSubmitDialog = true"
          >
            <el-icon><Plus /></el-icon>
            提交作业
          </el-button>
        </div>
      </template>

      <!-- 搜索和筛选 -->
      <div class="filter-container">
        <el-input
          v-model="searchText"
          :placeholder="isStudent ? '搜索作业标题' : '搜索学生姓名或作业标题'"
          style="width: 300px; margin-right: 10px;"
          clearable
        />
        <el-select
          v-if="isTeacher"
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
              v-if="isTeacher && !scope.row.score"
              size="small"
              type="primary"
              @click="handleGrade(scope.row)"
            >
              批改
            </el-button>
            <el-button
              v-if="isTeacher && scope.row.score"
              size="small"
              type="warning"
              @click="handleGrade(scope.row)"
            >
              修改评分
            </el-button>
            <el-button
              v-if="isTeacher"
              size="small"
              type="danger"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
            <template v-if="isStudent">
              <el-button
                v-if="canResubmit(scope.row)"
                size="small"
                type="warning"
                @click="handleResubmit(scope.row)"
              >
                重新提交
              </el-button>
              <span v-else>-</span>
            </template>
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
        <el-form-item v-if="isTeacher" label="学生" prop="studentId">
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
        <el-form-item v-if="isStudent" label="学生">
          <el-input :value="user.name" disabled />
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
            :rows="4"
            placeholder="请输入作业内容或说明..."
          />
        </el-form-item>
        <el-form-item label="作业附件">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :show-file-list="true"
            :file-list="fileList"
          >
            <el-button type="primary">
              <el-icon><Upload /></el-icon>
              上传文件
            </el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持上传文档、图片、代码等文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
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
        <el-form-item label="作业附件" v-if="currentSubmission?.attachmentPath">
          <div class="attachment-container">
            <el-button
              type="primary"
              size="small"
              @click="downloadAttachment(currentSubmission.attachmentPath)"
            >
              <el-icon><Download /></el-icon>
              下载附件
            </el-button>
            <span class="attachment-name">{{ getFileName(currentSubmission.attachmentPath) }}</span>
          </div>
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

    <!-- 重新提交作业对话框 -->
    <el-dialog
      title="重新提交作业"
      v-model="showResubmitDialog"
      width="600px"
    >
      <el-form
        :model="resubmitForm"
        :rules="resubmitRules"
        ref="resubmitFormRef"
        label-width="100px"
      >
        <el-form-item label="学生">
          <el-input :value="user.name" disabled />
        </el-form-item>
        <el-form-item label="作业">
          <el-input :value="currentSubmission?.homeworkTitle" disabled />
        </el-form-item>
        <el-form-item label="打回原因">
          <el-input
            :value="currentSubmission?.returnReason"
            type="textarea"
            :rows="3"
            disabled
          />
        </el-form-item>
        <el-form-item label="提交内容" prop="content">
          <el-input
            v-model="resubmitForm.content"
            type="textarea"
            :rows="4"
            placeholder="请根据教师反馈修改作业内容..."
          />
        </el-form-item>
        <el-form-item label="作业附件">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :on-success="handleResubmitUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :show-file-list="true"
            :file-list="resubmitFileList"
          >
            <el-button type="primary">
              <el-icon><Upload /></el-icon>
              上传文件
            </el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持上传文档、图片、代码等文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showResubmitDialog = false">取消</el-button>
        <el-button type="warning" @click="handleResubmitSubmit">
          重新提交
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
        user: {},
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
          content: '',
          attachmentPath: ''
        },
        gradeForm: {
          score: null,
          teacherComment: ''
        },
        fileList: [],
        uploadUrl: 'http://localhost:8080/api/upload',
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
        },
        showResubmitDialog: false,
        resubmitForm: {
          content: '',
          attachmentPath: ''
        },
        resubmitFileList: [],
        resubmitRules: {
          content: [
            { required: true, message: '请输入提交内容', trigger: 'blur' }
          ]
        }
      }
    },
  async mounted() {
    this.loadUserInfo()
    await Promise.all([
      this.loadSubmissions(),
      this.loadStudents(),
      this.loadHomework()
    ])
  },
  computed: {
    isTeacher() {
      return this.user.role === 'TEACHER'
    },
    isStudent() {
      return this.user.role === 'STUDENT'
    }
  },
  methods: {
    loadUserInfo() {
      const userData = localStorage.getItem('user')
      if (userData) {
        this.user = JSON.parse(userData)
      }
    },

    async loadSubmissions() {
      this.loading = true
      try {
        let response
        if (this.isStudent) {
          // 学生只能看到自己的提交
          response = await submissionApi.getSubmissionsByStudent(this.user.id)
        } else {
          // 教师可以看到所有提交
          response = await submissionApi.getAllSubmissions()
        }
        
        // 统一处理API响应格式
        if (Array.isArray(response)) {
          // 格式1: 直接返回数组
          this.submissions = response
          this.filteredSubmissions = response
        } else if (response && response.success !== undefined) {
          // 格式2: {success: true, data: [...]}
          if (response.success && Array.isArray(response.data)) {
            this.submissions = response.data
            this.filteredSubmissions = response.data
          } else {
            this.submissions = []
            this.filteredSubmissions = []
            this.$message.error(response.message || '加载提交列表失败')
          }
        } else {
          this.submissions = []
          this.filteredSubmissions = []
          this.$message.error('加载提交列表失败')
        }
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
        
        // 根据用户角色设置学生ID
        const submitData = {
          ...this.submitForm,
          studentId: this.isStudent ? this.user.id : this.submitForm.studentId
        }
        
        const success = await submissionApi.submitHomework(submitData)
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
    },

    // 文件上传相关方法
    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    handleUploadSuccess(response, file) {
      if (response.success) {
        this.submitForm.attachmentPath = response.filePath
        this.$message.success('文件上传成功')
      } else {
        this.$message.error('文件上传失败')
      }
    },

    handleUploadError(error) {
      console.error('文件上传失败:', error)
      this.$message.error('文件上传失败')
    },

    // 下载附件
    downloadAttachment(filePath) {
      if (!filePath) {
        this.$message.error('文件路径不存在')
        return
      }
      
      // 创建下载链接
      const downloadUrl = `http://localhost:8080/api/download?filePath=${encodeURIComponent(filePath)}`
      const link = document.createElement('a')
      link.href = downloadUrl
      link.target = '_blank'
      link.download = this.getFileName(filePath)
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },

    // 获取文件名
    getFileName(filePath) {
      if (!filePath) return ''
      return filePath.split('/').pop() || filePath
    },

    resetSubmitForm() {
      this.submitForm = {
        studentId: '',
        homeworkId: '',
        content: '',
        attachmentPath: ''
      }
      this.fileList = []
      if (this.$refs.submitFormRef) {
        this.$refs.submitFormRef.resetFields()
      }
    },

    // 重新提交作业
    handleResubmit(submission) {
      this.currentSubmission = submission
      this.resubmitForm = {
        content: submission.content || '',
        attachmentPath: submission.attachmentPath || ''
      }
      this.resubmitFileList = []
      this.showResubmitDialog = true
    },

    // 重新提交文件上传成功
    handleResubmitUploadSuccess(response, file) {
      if (response.success) {
        this.resubmitForm.attachmentPath = response.filePath
        this.$message.success('文件上传成功')
      } else {
        this.$message.error('文件上传失败')
      }
    },

    // 提交重新提交
    async handleResubmitSubmit() {
      try {
        await this.$refs.resubmitFormRef.validate()
        
        const success = await submissionApi.resubmitHomework(
          this.currentSubmission.id,
          this.resubmitForm.content,
          this.resubmitForm.attachmentPath
        )
        if (success) {
          this.$message.success('作业重新提交成功')
          this.showResubmitDialog = false
          await this.loadSubmissions()
          this.resetResubmitForm()
        } else {
          this.$message.error('作业重新提交失败')
        }
      } catch (error) {
        console.error('重新提交作业失败:', error)
        this.$message.error('重新提交作业失败')
      }
    },

    // 检查是否可以重新提交
    canResubmit(submission) {
      // 只有有打回原因的作业才能重新提交（即被打回过的作业）
      if (!submission.returnReason) {
        return false
      }
      
      // 检查作业是否已截止
      if (submission.homeworkDeadline) {
        const deadline = new Date(submission.homeworkDeadline)
        const now = new Date()
        return deadline > now
      }
      
      // 如果没有截止时间信息，默认允许重新提交
      return true
    },

    // 重置重新提交表单
    resetResubmitForm() {
      this.resubmitForm = {
        content: '',
        attachmentPath: ''
      }
      this.resubmitFileList = []
      this.currentSubmission = null
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

.attachment-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.attachment-name {
  color: #606266;
  font-size: 14px;
}
</style>
