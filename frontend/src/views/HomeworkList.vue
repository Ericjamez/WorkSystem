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
        <el-table-column label="操作" width="200" fixed="right" v-if="isTeacher">
          <template #default="scope">
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
  </div>
</template>

<script>
import { homeworkApi, adminApi } from '../services/api.js'

export default {
  name: 'HomeworkList',
  data() {
    return {
      user: {},
      homework: [],
      filteredHomework: [],
      classList: [],
      loading: false,
      searchText: '',
      showActiveOnly: false,
      showAddDialog: false,
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
        const response = await homeworkApi.getAllHomework()
        this.homework = response
        this.filteredHomework = response
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
          deadline: this.homeworkForm.deadline.toISOString()
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
