<template>
  <div class="login-container">
    <div class="login-form">
      <el-card class="login-card">
        <template #header>
          <div class="login-header">
            <h2>学生作业管理系统</h2>
            <p>请选择登录身份</p>
          </div>
        </template>

        <el-tabs v-model="activeTab" type="border-card">
          <el-tab-pane label="学生登录" name="student">
            <el-form
              :model="studentForm"
              :rules="loginRules"
              ref="studentFormRef"
              label-width="80px"
            >
              <el-form-item label="用户名" prop="username">
                <el-input
                  v-model="studentForm.username"
                  placeholder="请输入用户名"
                  prefix-icon="User"
                />
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="studentForm.password"
                  type="password"
                  placeholder="请输入密码"
                  prefix-icon="Lock"
                  show-password
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  style="width: 100%"
                  @click="handleStudentLogin"
                  :loading="loading"
                >
                  学生登录
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="教师登录" name="teacher">
            <el-form
              :model="teacherForm"
              :rules="loginRules"
              ref="teacherFormRef"
              label-width="80px"
            >
              <el-form-item label="用户名" prop="username">
                <el-input
                  v-model="teacherForm.username"
                  placeholder="请输入用户名"
                  prefix-icon="User"
                />
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="teacherForm.password"
                  type="password"
                  placeholder="请输入密码"
                  prefix-icon="Lock"
                  show-password
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  style="width: 100%"
                  @click="handleTeacherLogin"
                  :loading="loading"
                >
                  教师登录
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>

        <div class="register-link">
          <el-divider>还没有账号？</el-divider>
          <el-button type="text" @click="showRegisterDialog = true">
            立即注册
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 注册对话框 -->
    <el-dialog
      title="用户注册"
      v-model="showRegisterDialog"
      width="500px"
    >
      <el-tabs v-model="registerActiveTab">
        <el-tab-pane label="学生注册" name="student">
          <el-form
            :model="registerStudentForm"
            :rules="registerRules"
            ref="registerStudentFormRef"
            label-width="100px"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="registerStudentForm.username" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="registerStudentForm.password" type="password" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="registerStudentForm.name" />
            </el-form-item>
            <el-form-item label="学号" prop="studentId">
              <el-input v-model="registerStudentForm.studentId" />
            </el-form-item>
            <el-form-item label="班级" prop="className">
              <el-input v-model="registerStudentForm.className" />
            </el-form-item>
            <el-form-item label="专业" prop="major">
              <el-input v-model="registerStudentForm.major" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="registerStudentForm.email" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="教师注册" name="teacher">
          <el-form
            :model="registerTeacherForm"
            :rules="registerRules"
            ref="registerTeacherFormRef"
            label-width="100px"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="registerTeacherForm.username" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="registerTeacherForm.password" type="password" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="registerTeacherForm.name" />
            </el-form-item>
            <el-form-item label="教师编号" prop="teacherId">
              <el-input v-model="registerTeacherForm.teacherId" />
            </el-form-item>
            <el-form-item label="院系" prop="department">
              <el-input v-model="registerTeacherForm.department" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="registerTeacherForm.email" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <el-button @click="showRegisterDialog = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleRegister"
          :loading="registerLoading"
        >
          注册
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { authApi } from '../services/api.js'

export default {
  name: 'Login',
  data() {
    return {
      activeTab: 'student',
      registerActiveTab: 'student',
      loading: false,
      registerLoading: false,
      showRegisterDialog: false,
      studentForm: {
        username: '',
        password: ''
      },
      teacherForm: {
        username: '',
        password: ''
      },
      registerStudentForm: {
        username: '',
        password: '',
        name: '',
        studentId: '',
        className: '',
        major: '',
        email: ''
      },
      registerTeacherForm: {
        username: '',
        password: '',
        name: '',
        teacherId: '',
        department: '',
        email: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        studentId: [
          { required: true, message: '请输入学号', trigger: 'blur' }
        ],
        teacherId: [
          { required: true, message: '请输入教师编号', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async handleStudentLogin() {
      try {
        await this.$refs.studentFormRef.validate()
        this.loading = true
        
        const response = await authApi.login(this.studentForm)
        if (response.success) {
          this.$message.success('学生登录成功')
          // 存储用户信息到本地存储
          localStorage.setItem('user', JSON.stringify(response.user))
          // 跳转到学生首页
          this.$router.push('/student/home')
        } else {
          // 显示详细的错误信息
          this.$message.error(response.message || '用户名或密码错误')
        }
      } catch (error) {
        console.error('登录失败:', error)
        // 显示详细的错误信息
        if (error.response && error.response.data) {
          this.$message.error(error.response.data.message || '登录失败，请检查网络连接')
        } else {
          this.$message.error('登录失败：' + (error.message || '未知错误'))
        }
      } finally {
        this.loading = false
      }
    },

    async handleTeacherLogin() {
      try {
        await this.$refs.teacherFormRef.validate()
        this.loading = true
        
        const response = await authApi.login(this.teacherForm)
        if (response.success) {
          this.$message.success('教师登录成功')
          // 存储用户信息到本地存储
          localStorage.setItem('user', JSON.stringify(response.user))
          // 跳转到教师首页
          this.$router.push('/teacher/home')
        } else {
          // 显示详细的错误信息
          this.$message.error(response.message || '用户名或密码错误')
        }
      } catch (error) {
        console.error('登录失败:', error)
        // 显示详细的错误信息
        if (error.response && error.response.data) {
          this.$message.error(error.response.data.message || '登录失败，请检查网络连接')
        } else {
          this.$message.error('登录失败：' + (error.message || '未知错误'))
        }
      } finally {
        this.loading = false
      }
    },

    async handleRegister() {
      try {
        let formRef = this.registerActiveTab === 'student' 
          ? this.$refs.registerStudentFormRef 
          : this.$refs.registerTeacherFormRef
        
        await formRef.validate()
        this.registerLoading = true
        
        let response
        if (this.registerActiveTab === 'student') {
          response = await authApi.registerStudent(this.registerStudentForm)
        } else {
          response = await authApi.registerTeacher(this.registerTeacherForm)
        }
        
        if (response.success) {
          this.$message.success('注册成功')
          this.showRegisterDialog = false
          this.resetRegisterForms()
        } else {
          this.$message.error(response.message)
        }
      } catch (error) {
        console.error('注册失败:', error)
        this.$message.error('注册失败')
      } finally {
        this.registerLoading = false
      }
    },

    resetRegisterForms() {
      this.registerStudentForm = {
        username: '',
        password: '',
        name: '',
        studentId: '',
        className: '',
        major: '',
        email: ''
      }
      this.registerTeacherForm = {
        username: '',
        password: '',
        name: '',
        teacherId: '',
        department: '',
        email: ''
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-form {
  width: 400px;
}

.login-card {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  color: #409EFF;
}

.login-header h2 {
  margin: 0;
  font-size: 24px;
}

.login-header p {
  margin: 10px 0 0 0;
  color: #666;
  font-size: 14px;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}
</style>
