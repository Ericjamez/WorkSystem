<template>
  <div class="student-management">
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
          placeholder="搜索学号、姓名"
          style="width: 300px; margin-right: 10px;"
          clearable
        />
        <el-select
          v-model="selectedClassId"
          placeholder="选择班级"
          style="width: 200px; margin-right: 10px;"
          clearable
        >
          <el-option
            v-for="clazz in classes"
            :key="clazz.id"
            :label="clazz.name"
            :value="clazz.id"
          />
        </el-select>
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
        <el-table-column prop="className" label="班级" width="150" />
        <el-table-column prop="majorName" label="专业" width="150" />
        <el-table-column prop="collegeName" label="学院" width="150" />
        <el-table-column prop="email" label="邮箱" min-width="200" />
        <el-table-column prop="phone" label="电话" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
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
              type="warning"
              @click="handleViewPassword(scope.row)"
            >
              查看密码
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
      :title="isEditing ? '编辑学生信息' : '添加学生'"
      v-model="showAddDialog"
      width="600px"
    >
      <el-form
        :model="studentForm"
        :rules="rules"
        ref="studentFormRef"
        label-width="100px"
      >
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="studentForm.studentId" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="studentForm.name" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="studentForm.email" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="studentForm.phone" />
        </el-form-item>
        <el-form-item label="所属班级" prop="classId">
          <el-select v-model="studentForm.classId" placeholder="请选择班级" @change="handleClassChange">
            <el-option
              v-for="clazz in classes"
              :key="clazz.id"
              :label="clazz.name"
              :value="clazz.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属专业" prop="majorName">
          <el-input v-model="studentForm.majorName" disabled />
        </el-form-item>
        <el-form-item label="所属学院" prop="collegeName">
          <el-input v-model="studentForm.collegeName" disabled />
        </el-form-item>
        <el-form-item label="登录密码" prop="password">
          <el-input 
            v-model="studentForm.password" 
            type="password" 
            placeholder="默认密码：password123"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">
          {{ isEditing ? '更新' : '添加' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 查看密码对话框 -->
    <el-dialog
      title="查看学生密码"
      v-model="showPasswordDialog"
      width="400px"
    >
      <div style="text-align: center; padding: 20px;">
        <p><strong>学生：</strong>{{ selectedStudent.name }}</p>
        <p><strong>学号：</strong>{{ selectedStudent.studentId }}</p>
        <p><strong>登录密码：</strong>{{ selectedStudent.password || 'password123' }}</p>
      </div>
      <template #footer>
        <el-button @click="showPasswordDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { adminApi, studentApi, authApi } from '../services/api.js'

export default {
  name: 'StudentManagement',
  data() {
    return {
      students: [],
      filteredStudents: [],
      classes: [],
      loading: false,
      searchText: '',
      selectedClassId: null,
      showAddDialog: false,
      showPasswordDialog: false,
      isEditing: false,
      selectedStudent: {},
      studentForm: {
        studentId: '',
        name: '',
        email: '',
        phone: '',
        classId: null,
        majorName: '',
        collegeName: '',
        password: 'password123'
      },
      rules: {
        studentId: [
          { required: true, message: '请输入学号', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        classId: [
          { required: true, message: '请选择所属班级', trigger: 'change' }
        ]
      }
    }
  },
  async mounted() {
    await this.loadClasses()
    await this.loadStudents()
  },
  methods: {
    async loadClasses() {
      try {
        const response = await adminApi.getAllClasses()
        if (response.success) {
          this.classes = response.classes || []
        } else {
          this.$message.error('加载班级列表失败')
        }
      } catch (error) {
        console.error('加载班级列表失败:', error)
        this.$message.error('加载班级列表失败')
      }
    },

    async loadStudents() {
      this.loading = true
      try {
        const response = await studentApi.getAllStudents()
        if (response.success) {
          this.students = response.data || []
          // 为学生添加班级、专业、学院信息
          this.students.forEach(student => {
            // 使用API返回的实际数据
            student.className = student.className || '未知班级'
            student.majorName = student.major || '未知专业'
            // 根据专业推断学院
            if (student.major === '计算机科学与技术') {
              student.collegeName = '计算机学院'
            } else if (student.major === '软件工程') {
              student.collegeName = '软件学院'
            } else if (student.major === '电子信息工程') {
              student.collegeName = '电子信息学院'
            } else {
              student.collegeName = '未知学院'
            }
          })
          this.filteredStudents = this.students
        } else {
          this.$message.error('加载学生列表失败')
        }
      } catch (error) {
        console.error('加载学生列表失败:', error)
        this.$message.error('加载学生列表失败')
      } finally {
        this.loading = false
      }
    },

    resetSearch() {
      this.searchText = ''
      this.selectedClassId = null
      this.filteredStudents = this.students
    },

    handleEdit(student) {
      this.isEditing = true
      this.studentForm = { ...student }
      this.showAddDialog = true
    },

    handleViewPassword(student) {
      this.selectedStudent = { ...student }
      this.showPasswordDialog = true
    },

    handleClassChange(classId) {
      const selectedClass = this.classes.find(c => c.id === classId)
      if (selectedClass) {
        this.studentForm.className = selectedClass.name || '未知班级'
        this.studentForm.majorName = selectedClass.majorName || '未知专业'
        this.studentForm.collegeName = selectedClass.collegeName || '未知学院'
      }
    },

    async handleDelete(student) {
      try {
        await this.$confirm('确定要删除这个学生吗？', '提示', {
          type: 'warning'
        })
        
        const response = await studentApi.deleteStudent(student.id)
        if (response) {
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
        
        let response
        if (this.isEditing) {
          // 更新学生时使用正确的数据格式
          const updateData = {
            studentId: this.studentForm.studentId,
            className: this.studentForm.className || '未知班级',
            major: this.studentForm.majorName || '未知专业',
            classId: this.studentForm.classId
          }
          response = await studentApi.updateStudent(this.studentForm.id, updateData)
        } else {
          // 添加学生时使用注册接口
          const registerData = {
            username: this.studentForm.studentId,
            password: this.studentForm.password || 'password123',
            name: this.studentForm.name,
            email: this.studentForm.email,
            phone: this.studentForm.phone,
            studentId: this.studentForm.studentId, // 添加学号字段
            className: this.studentForm.className || '未知班级', // 添加班级名称
            major: this.studentForm.majorName || '未知专业' // 添加专业名称
          }
          response = await authApi.registerStudent(registerData)
        }
        
        if (response && (response.success || response === '学生信息更新成功')) {
          this.$message.success(this.isEditing ? '更新成功' : '添加成功')
          this.showAddDialog = false
          await this.loadStudents()
          this.resetForm()
        } else {
          this.$message.error(response?.message || (this.isEditing ? '更新失败' : '添加失败'))
        }
      } catch (error) {
        console.error('提交表单失败:', error)
        this.$message.error('操作失败')
      }
    },

    resetForm() {
      this.studentForm = {
        studentId: '',
        name: '',
        email: '',
        phone: '',
        classId: null,
        majorName: '',
        collegeName: '',
        password: 'password123'
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
      this.filteredStudents = this.students.filter(student =>
        student.studentId.includes(val) || student.name.includes(val)
      )
    },
    selectedClassId(val) {
      if (val) {
        this.filteredStudents = this.students.filter(student => student.classId === val)
      } else {
        this.filteredStudents = this.students
      }
    }
  }
}
</script>

<style scoped>
.student-management {
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
