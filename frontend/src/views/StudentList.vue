<template>
  <div class="student-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学生管理</span>
          <el-button type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon>
            添加学生
          </el-button>
        </div>
      </template>

      <!-- 搜索和筛选 -->
      <div class="filter-container">
        <el-input
          v-model="searchText"
          placeholder="搜索学生姓名或学号"
          style="width: 300px; margin-right: 10px;"
          clearable
        />
        <el-button type="primary" @click="loadStudents">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
      </div>

      <!-- 学生表格 -->
      <el-table
        :data="filteredStudents"
        v-loading="loading"
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="studentId" label="学号" width="120" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="className" label="班级" width="200" />
        <el-table-column prop="email" label="邮箱" min-width="200" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
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

    <!-- 添加/编辑学生对话框 -->
    <el-dialog
      :title="isEditing ? '编辑学生' : '添加学生'"
      v-model="showAddDialog"
      width="500px"
    >
      <el-form
        :model="studentForm"
        :rules="rules"
        ref="studentFormRef"
        label-width="80px"
      >
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="studentForm.studentId" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="studentForm.name" />
        </el-form-item>
        <el-form-item label="班级" prop="className">
          <el-input v-model="studentForm.className" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="studentForm.email" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="studentForm.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">
          {{ isEditing ? '更新' : '添加' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { studentApi } from '../services/api.js'

export default {
  name: 'StudentList',
  data() {
    return {
      students: [],
      filteredStudents: [],
      loading: false,
      searchText: '',
      showAddDialog: false,
      isEditing: false,
      studentForm: {
        studentId: '',
        name: '',
        className: '',
        email: '',
        phone: ''
      },
      rules: {
        studentId: [
          { required: true, message: '请输入学号', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        className: [
          { required: true, message: '请输入班级', trigger: 'blur' }
        ]
      }
    }
  },
  async mounted() {
    await this.loadStudents()
  },
  methods: {
    async loadStudents() {
      this.loading = true
      try {
        const response = await studentApi.getAllStudents()
        this.students = response
        this.filteredStudents = response
      } catch (error) {
        console.error('加载学生列表失败:', error)
        this.$message.error('加载学生列表失败')
      } finally {
        this.loading = false
      }
    },

    resetSearch() {
      this.searchText = ''
      this.filteredStudents = this.students
    },

    handleEdit(student) {
      this.isEditing = true
      this.studentForm = { ...student }
      this.showAddDialog = true
    },

    async handleDelete(student) {
      try {
        await this.$confirm('确定要删除这个学生吗？', '提示', {
          type: 'warning'
        })
        
        const success = await studentApi.deleteStudent(student.id)
        if (success) {
          this.$message.success('删除成功')
          await this.loadStudents()
        } else {
          this.$message.error('删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除学生失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.studentFormRef.validate()
        
        let success
        if (this.isEditing) {
          success = await studentApi.updateStudent(this.studentForm.id, this.studentForm)
        } else {
          success = await studentApi.createStudent(this.studentForm)
        }
        
        if (success) {
          this.$message.success(this.isEditing ? '更新成功' : '添加成功')
          this.showAddDialog = false
          await this.loadStudents()
          this.resetForm()
        } else {
          this.$message.error(this.isEditing ? '更新失败' : '添加失败')
        }
      } catch (error) {
        console.error('提交表单失败:', error)
      }
    },

    resetForm() {
      this.studentForm = {
        studentId: '',
        name: '',
        className: '',
        email: '',
        phone: ''
      }
      this.isEditing = false
      if (this.$refs.studentFormRef) {
        this.$refs.studentFormRef.resetFields()
      }
    },

    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString('zh-CN')
    }
  },
  watch: {
    searchText(val) {
      if (!val) {
        this.filteredStudents = this.students
      } else {
        this.filteredStudents = this.students.filter(student =>
          student.name.includes(val) || student.studentId.includes(val)
        )
      }
    }
  }
}
</script>

<style scoped>
.student-list {
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
