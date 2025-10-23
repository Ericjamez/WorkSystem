<template>
  <div class="class-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>班级管理</span>
          <el-button type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon>
            添加班级
          </el-button>
        </div>
      </template>

      <!-- 搜索和筛选 -->
      <div class="filter-container">
        <el-input
          v-model="searchText"
          placeholder="搜索班级名称"
          style="width: 300px; margin-right: 10px;"
          clearable
        />
        <el-select
          v-model="selectedMajorId"
          placeholder="选择专业"
          style="width: 200px; margin-right: 10px;"
          clearable
        >
          <el-option
            v-for="major in majors"
            :key="major.id"
            :label="major.name"
            :value="major.id"
          />
        </el-select>
        <el-button type="primary" @click="loadClasses">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
      </div>

      <!-- 班级表格 -->
      <el-table
        :data="filteredClasses"
        v-loading="loading"
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="班级名称" min-width="200" />
        <el-table-column prop="majorName" label="所属专业" min-width="150" />
        <el-table-column prop="collegeName" label="所属学院" min-width="150" />
        <el-table-column prop="description" label="班级描述" min-width="300" show-overflow-tooltip />
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

    <!-- 添加/编辑班级对话框 -->
    <el-dialog
      :title="isEditing ? '编辑班级' : '添加班级'"
      v-model="showAddDialog"
      width="500px"
    >
      <el-form
        :model="classForm"
        :rules="rules"
        ref="classFormRef"
        label-width="100px"
      >
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="classForm.name" />
        </el-form-item>
        <el-form-item label="所属专业" prop="majorId">
          <el-select v-model="classForm.majorId" placeholder="请选择专业" @change="handleMajorChange">
            <el-option
              v-for="major in majors"
              :key="major.id"
              :label="major.name"
              :value="major.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属学院" prop="collegeName">
          <el-input v-model="classForm.collegeName" disabled />
        </el-form-item>
        <el-form-item label="班级描述" prop="description">
          <el-input
            v-model="classForm.description"
            type="textarea"
            :rows="4"
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
  </div>
</template>

<script>
import { adminApi } from '../services/api.js'

export default {
  name: 'ClassManagement',
  data() {
    return {
      classes: [],
      filteredClasses: [],
      majors: [],
      colleges: [],
      loading: false,
      searchText: '',
      selectedMajorId: null,
      showAddDialog: false,
      isEditing: false,
      classForm: {
        name: '',
        majorId: null,
        collegeName: '',
        description: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入班级名称', trigger: 'blur' }
        ],
        majorId: [
          { required: true, message: '请选择所属专业', trigger: 'change' }
        ]
      }
    }
  },
  async mounted() {
    await this.loadColleges()
    await this.loadMajors()
    await this.loadClasses()
  },
  methods: {
    async loadColleges() {
      try {
        const response = await adminApi.getAllColleges()
        if (response.success) {
          this.colleges = response.colleges || []
        } else {
          this.$message.error('加载学院列表失败')
        }
      } catch (error) {
        console.error('加载学院列表失败:', error)
        this.$message.error('加载学院列表失败')
      }
    },

    async loadMajors() {
      try {
        const response = await adminApi.getAllMajors()
        if (response.success) {
          this.majors = response.majors || []
          // 为专业添加学院名称
          this.majors.forEach(major => {
            const college = this.colleges.find(c => c.id === major.collegeId)
            major.collegeName = college ? college.name : '未知学院'
          })
        } else {
          this.$message.error('加载专业列表失败')
        }
      } catch (error) {
        console.error('加载专业列表失败:', error)
        this.$message.error('加载专业列表失败')
      }
    },

    async loadClasses() {
      this.loading = true
      try {
        const response = await adminApi.getAllClasses()
        if (response.success) {
          this.classes = response.classes || []
          // 为班级添加专业和学院名称
          this.classes.forEach(clazz => {
            const major = this.majors.find(m => m.id === clazz.majorId)
            clazz.majorName = major ? major.name : '未知专业'
            clazz.collegeName = major ? major.collegeName : '未知学院'
          })
          this.filteredClasses = this.classes
        } else {
          this.$message.error('加载班级列表失败')
        }
      } catch (error) {
        console.error('加载班级列表失败:', error)
        this.$message.error('加载班级列表失败')
      } finally {
        this.loading = false
      }
    },

    resetSearch() {
      this.searchText = ''
      this.selectedMajorId = null
      this.filteredClasses = this.classes
    },

    handleEdit(clazz) {
      this.isEditing = true
      this.classForm = { ...clazz }
      // 设置学院名称
      const major = this.majors.find(m => m.id === clazz.majorId)
      this.classForm.collegeName = major ? major.collegeName : ''
      this.showAddDialog = true
    },

    handleMajorChange(majorId) {
      const major = this.majors.find(m => m.id === majorId)
      this.classForm.collegeName = major ? major.collegeName : ''
    },

    async handleDelete(clazz) {
      try {
        await this.$confirm('确定要删除这个班级吗？', '提示', {
          type: 'warning'
        })
        
        const response = await adminApi.deleteClass(clazz.id)
        if (response.success) {
          this.$message.success('删除成功')
          await this.loadClasses()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除班级失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.classFormRef.validate()
        
        let response
        if (this.isEditing) {
          response = await adminApi.updateClass(this.classForm.id, this.classForm)
        } else {
          response = await adminApi.createClass(this.classForm)
        }
        
        if (response.success) {
          this.$message.success(this.isEditing ? '更新成功' : '添加成功')
          this.showAddDialog = false
          await this.loadClasses()
          this.resetForm()
        } else {
          this.$message.error(response.message || (this.isEditing ? '更新失败' : '添加失败'))
        }
      } catch (error) {
        console.error('提交表单失败:', error)
      }
    },

    resetForm() {
      this.classForm = {
        name: '',
        majorId: null,
        collegeName: '',
        description: ''
      }
      this.isEditing = false
      if (this.$refs.classFormRef) {
        this.$refs.classFormRef.resetFields()
      }
    },

    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString('zh-CN')
    }
  },
  watch: {
    searchText(val) {
      this.filteredClasses = this.classes.filter(clazz =>
        clazz.name.includes(val)
      )
    },
    selectedMajorId(val) {
      if (val) {
        this.filteredClasses = this.classes.filter(clazz => clazz.majorId === val)
      } else {
        this.filteredClasses = this.classes
      }
    }
  }
}
</script>

<style scoped>
.class-management {
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
