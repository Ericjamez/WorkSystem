<template>
  <div class="homework-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>作业管理</span>
          <el-button v-if="isTeacher" type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon>
            发布作业
          </el-button>
        </div>
      </template>

      <!-- 搜索和筛选 -->
      <div class="filter-container">
        <el-input
          v-model="searchText"
          placeholder="搜索作业标题或课程名称"
          style="width: 300px; margin-right: 10px;"
          clearable
        />
        <el-button type="primary" @click="loadHomework">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
        <el-button type="success" @click="showActiveOnly = !showActiveOnly">
          <el-icon><Clock /></el-icon>
          {{ showActiveOnly ? '显示全部' : '仅显示有效作业' }}
        </el-button>
      </div>

      <!-- 作业表格 -->
      <el-table
        :data="filteredHomework"
        v-loading="loading"
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="作业标题" min-width="200" />
        <el-table-column prop="courseName" label="课程名称" width="150" />
        <el-table-column prop="description" label="作业描述" min-width="250" show-overflow-tooltip />
        <el-table-column prop="deadline" label="截止时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.deadline) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalScore" label="总分" width="100" />
        <el-table-column prop="createTime" label="发布时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="isActive(scope.row.deadline) ? 'success' : 'danger'">
              {{ isActive(scope.row.deadline) ? '进行中' : '已截止' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="提交情况" width="120" v-if="isTeacher">
          <template #default="scope">
            <el-tag :type="getSubmissionStats(scope.row).type">
              {{ getSubmissionStats(scope.row).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" fixed="right" v-if="isTeacher">
          <template #default="scope">
            <el-button
              size="small"
              type="success"
              @click="handleViewSubmissions(scope.row)"
            >
              查看提交
            </el-button>
            <el-button
              size="small"
              type="primary"
              @click="handleEdit(scope.row)"
            >
              编辑
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

        <!-- 学生端操作列 -->
        <el-table-column label="操作" width="200" fixed="right" v-if="!isTeacher && user.role === 'STUDENT'">
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              @click="handleSubmitHomework(scope.row)"
            >
              提交作业
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑作业对话框 -->
    <el-dialog
      :title="isEditing ? '编辑作业' : '发布作业'"
      v-model="showAddDialog"
      width="600px"
    >
      <el-form
        :model="homeworkForm"
        :rules="rules"
        ref="homeworkFormRef"
        label-width="100px"
      >
        <el-form-item label="作业标题" prop="title">
          <el-input v-model="homeworkForm.title" />
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="homeworkForm.courseName" />
        </el-form-item>
        <el-form-item label="选择班级" prop="classId">
          <el-select v-model="homeworkForm.classId" placeholder="请选择班级" style="width: 100%;">
            <el-option
              v-for="classItem in classList"
              :key="classItem.id"
              :label="classItem.name"
              :value="classItem.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="作业描述" prop="description">
          <el-input
            v-model="homeworkForm.description"
            type="textarea"
            :rows="4"
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
                支持上传文档、图片等文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="截止时间" prop="deadline">
          <el-date-picker
            v-model="homeworkForm.deadline"
            type="datetime"
            placeholder="选择截止时间"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="总分" prop="totalScore">
          <el-input-number
            v-model="homeworkForm.totalScore"
            :min="1"
            :max="200"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">
          {{ isEditing ? '更新' : '发布' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 提交作业对话框（与"我的提交"页面相同） -->
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
        <el-form-item label="学生">
          <el-input :value="user.name" disabled />
        </el-form-item>
        <el-form-item label="作业">
          <el-input :value="getSelectedHomeworkTitle()" disabled />
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
        <el-button type="primary" @click="handleSubmitHomeworkForm">
          提交
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { homeworkApi, adminApi, submissionApi } from '../services/api.js'

export default {
  name: 'HomeworkList',
  data() {
    return {
      user: {},
      homework: [],
      filteredHomework: [],
      classList: [],
      activeHomework: [],
      loading: false,
      searchText: '',
      showActiveOnly: false,
      showAddDialog: false,
      showSubmitDialog: false,
      isEditing: false,
      homeworkForm: {
        title: '',
        courseName: '',
        classId: null,
        description: '',
        deadline: '',
        totalScore: 100,
        attachmentPath: ''
      },
      submitForm: {
        studentId: '',
        homeworkId: '',
        content: '',
        attachmentPath: ''
      },
      fileList: [],
      uploadUrl: 'http://localhost:8080/api/upload',
      rules: {
        title: [
          { required: true, message: '请输入作业标题', trigger: 'blur' }
        ],
        courseName: [
          { required: true, message: '请输入课程名称', trigger: 'blur' }
        ],
        classId: [
          { required: true, message: '请选择班级', trigger: 'change' }
        ],
        description: [
          { required: true, message: '请输入作业描述', trigger: 'blur' }
        ],
        deadline: [
          { required: true, message: '请选择截止时间', trigger: 'change' }
        ],
        totalScore: [
          { required: true, message: '请输入总分', trigger: 'blur' }
        ]
      },
      submitRules: {
        content: [
          { required: true, message: '请输入提交内容', trigger: 'blur' }
        ]
      }
    }
  },
  async mounted() {
    this.loadUserInfo()
    await this.loadHomework()
    await this.loadClasses()
  },
  computed: {
    isTeacher() {
      return this.user.role === 'TEACHER'
    }
  },
  methods: {
    loadUserInfo() {
      const userData = localStorage.getItem('user')
      if (userData) {
        this.user = JSON.parse(userData)
      }
    },

    async loadHomework() {
      this.loading = true
      try {
        let response
        if (this.isTeacher) {
          // 教师只能看到自己发布的作业
          response = await homeworkApi.getHomeworkByTeacher(this.user.id)
          console.log('教师作业响应:', response)
          // 统一处理API响应格式
          if (response && response.success !== undefined) {
            if (response.success && Array.isArray(response.data)) {
              this.homework = response.data || []
            } else {
              this.homework = []
              this.$message.error(response.message || '获取教师作业失败')
            }
          } else if (Array.isArray(response)) {
            // 直接返回数组格式
            this.homework = response
          } else {
            this.homework = []
            this.$message.error('获取教师作业失败')
          }
        } else if (this.user.role === 'STUDENT') {
          // 学生只能看到本专业的作业
          response = await homeworkApi.getAllHomework()
          // 统一处理API响应格式
          let homeworkList = []
          if (response && response.success !== undefined) {
            if (response.success && Array.isArray(response.data)) {
              homeworkList = response.data || []
            } else {
              this.$message.error(response.message || '获取作业列表失败')
            }
          } else if (Array.isArray(response)) {
            homeworkList = response
          } else {
            this.$message.error('获取作业列表失败')
          }
          
          // 过滤出学生本专业的作业
          const studentMajor = this.user.major || '计算机科学与技术' // 默认专业
          this.homework = homeworkList.filter(hw => {
            // 根据课程名称判断是否属于本专业
            return this.isCourseInMajor(hw.courseName, studentMajor)
          })
        } else {
          // 管理员可以看到所有作业
          response = await homeworkApi.getAllHomework()
          // 统一处理API响应格式
          if (response && response.success !== undefined) {
            if (response.success && Array.isArray(response.data)) {
              this.homework = response.data || []
            } else {
              this.homework = []
              this.$message.error(response.message || '获取作业列表失败')
            }
          } else if (Array.isArray(response)) {
            this.homework = response
          } else {
            this.homework = []
            this.$message.error('获取作业列表失败')
          }
        }
        this.filteredHomework = this.homework
      } catch (error) {
        console.error('加载作业列表失败:', error)
        this.$message.error('加载作业列表失败')
      } finally {
        this.loading = false
      }
    },

    resetSearch() {
      this.searchText = ''
      this.showActiveOnly = false
      this.filteredHomework = this.homework
    },

    isActive(deadline) {
      return new Date(deadline) > new Date()
    },

    handleEdit(homework) {
      this.isEditing = true
      this.homeworkForm = { 
        ...homework,
        deadline: new Date(homework.deadline)
      }
      this.showAddDialog = true
    },

    async handleDelete(homework) {
      try {
        await this.$confirm('确定要删除这个作业吗？', '提示', {
          type: 'warning'
        })
        
        const success = await homeworkApi.deleteHomework(homework.id)
        if (success) {
          this.$message.success('删除成功')
          await this.loadHomework()
        } else {
          this.$message.error('删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除作业失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.homeworkFormRef.validate()
        
        // 格式化截止时间
        const formattedData = {
          ...this.homeworkForm,
          deadline: this.homeworkForm.deadline.toISOString(),
          teacherId: this.user.id // 添加教师ID
        }
        
        let success
        if (this.isEditing) {
          success = await homeworkApi.updateHomework(this.homeworkForm.id, formattedData)
        } else {
          success = await homeworkApi.createHomework(formattedData)
        }
        
        if (success) {
          this.$message.success(this.isEditing ? '更新成功' : '发布成功')
          this.showAddDialog = false
          await this.loadHomework()
          this.resetForm()
        } else {
          this.$message.error(this.isEditing ? '更新失败' : '发布失败')
        }
      } catch (error) {
        console.error('提交表单失败:', error)
        this.$message.error('操作失败，请检查网络连接或联系管理员')
      }
    },

    resetForm() {
      this.homeworkForm = {
        title: '',
        courseName: '',
        description: '',
        deadline: '',
        totalScore: 100
      }
      this.isEditing = false
      if (this.$refs.homeworkFormRef) {
        this.$refs.homeworkFormRef.resetFields()
      }
    },

    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString('zh-CN')
    },

    // 加载班级列表
    async loadClasses() {
      try {
        const response = await adminApi.getAllClasses()
        if (response.success) {
          this.classList = response.classes || []
        } else {
          this.$message.error('加载班级列表失败')
        }
      } catch (error) {
        console.error('加载班级列表失败:', error)
        this.$message.error('加载班级列表失败')
      }
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
        this.homeworkForm.attachmentPath = response.filePath
        this.$message.success('文件上传成功')
      } else {
        this.$message.error('文件上传失败')
      }
    },

    handleUploadError(error) {
      console.error('文件上传失败:', error)
      this.$message.error('文件上传失败')
    },

    // 跳转到作业批改页面
    handleGrade(homework) {
      this.$router.push(`/homework-grade/${homework.id}`)
    },

    // 判断课程是否属于学生专业
    isCourseInMajor(courseName, studentMajor) {
      // 课程与专业的映射关系
      const majorCourseMap = {
        '计算机科学与技术': ['Java程序设计', '数据库原理', '计算机科学与技术', '数据结构', '算法设计'],
        '人工智能': ['人工智能', '机器学习', '深度学习', '自然语言处理'],
        '软件工程': ['软件工程', '软件测试', '软件项目管理', '软件架构'],
        '电子信息工程': ['电路原理', '数字电路', '模拟电路', '信号与系统']
      }
      
      // 如果学生专业在映射表中，检查课程是否属于该专业
      if (majorCourseMap[studentMajor]) {
        return majorCourseMap[studentMajor].some(course => 
          courseName.includes(course) || course.includes(courseName)
        )
      }
      
      // 默认情况下，如果专业不在映射表中，显示所有作业
      return true
    },

    // 获取作业提交统计信息（教师端）
    async getSubmissionStats(homework) {
      try {
        // 获取该作业的所有提交
        const response = await submissionApi.getSubmissionsByHomework(homework.id)
        let submissions = []
        
        // 统一处理API响应格式
        if (Array.isArray(response)) {
          submissions = response
        } else if (response && response.success !== undefined && response.data) {
          submissions = response.data
        }
        
        // 计算统计信息
        const totalSubmissions = submissions.length
        const gradedSubmissions = submissions.filter(sub => sub.score).length
        
        // 根据批改情况设置标签类型
        let type = 'info'
        if (totalSubmissions === 0) {
          type = 'danger' // 无人提交
        } else if (gradedSubmissions === totalSubmissions) {
          type = 'success' // 全部已批改
        } else if (gradedSubmissions > 0) {
          type = 'warning' // 部分已批改
        } else {
          type = 'info' // 全部未批改
        }
        
        return {
          text: `${gradedSubmissions}/${totalSubmissions}`,
          type: type
        }
      } catch (error) {
        console.error('获取作业提交统计失败:', error)
        // 出错时返回默认统计
        return {
          text: '0/0',
          type: 'info'
        }
      }
    },

    // 查看作业提交
    handleViewSubmissions(homework) {
      this.$router.push(`/homework-grade/${homework.id}`)
    },

    // 学生提交作业 - 使用与"我的提交"相同的对话框
    async handleSubmitHomework(homework) {
      try {
        // 检查作业是否已截止
        if (!this.isActive(homework.deadline)) {
          this.$message.error('该作业已截止，无法提交')
          return
        }

        // 加载有效作业列表
        await this.loadActiveHomework()
        
        // 设置提交表单数据
        this.submitForm = {
          studentId: this.user.id,
          homeworkId: homework.id,
          content: '',
          attachmentPath: ''
        }
        
        // 显示提交对话框
        this.showSubmitDialog = true
      } catch (error) {
        console.error('准备提交作业失败:', error)
        this.$message.error('准备提交作业失败')
      }
    },

    // 加载有效作业列表
    async loadActiveHomework() {
      try {
        const response = await homeworkApi.getActiveHomework()
        // 统一处理API响应格式
        if (Array.isArray(response)) {
          this.activeHomework = response
        } else if (response && response.success !== undefined) {
          if (response.success && Array.isArray(response.data)) {
            this.activeHomework = response.data
          } else {
            this.activeHomework = []
          }
        } else {
          this.activeHomework = []
        }
      } catch (error) {
        console.error('加载有效作业失败:', error)
        this.activeHomework = []
      }
    },

    // 提交作业表单处理
    async handleSubmitHomeworkForm() {
      try {
        await this.$refs.submitFormRef.validate()
        
        const submitData = {
          ...this.submitForm,
          studentId: this.user.id
        }
        
        const success = await submissionApi.submitHomework(submitData)
        if (success) {
          this.$message.success('作业提交成功')
          this.showSubmitDialog = false
          // 重新加载作业列表以更新状态
          await this.loadHomework()
          this.resetSubmitForm()
        } else {
          this.$message.error('提交失败，可能已经提交过该作业')
        }
      } catch (error) {
        console.error('提交作业失败:', error)
        this.$message.error('提交作业失败')
      }
    },

    // 重置提交表单
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

    // 获取选中的作业标题
    getSelectedHomeworkTitle() {
      const homeworkId = this.submitForm.homeworkId
      if (!homeworkId) return ''
      
      const homework = this.homework.find(hw => hw.id === homeworkId)
      return homework ? `${homework.title} - ${homework.courseName}` : ''
    }
  },
  watch: {
    searchText(val) {
      this.filteredHomework = this.homework.filter(hw =>
        hw.title.includes(val) || hw.courseName.includes(val)
      )
    },
    showActiveOnly(val) {
      if (val) {
        this.filteredHomework = this.homework.filter(hw => 
          this.isActive(hw.deadline)
        )
      } else {
        this.filteredHomework = this.homework
      }
    }
  }
}
</script>

<style scoped>
.homework-list {
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
