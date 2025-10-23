<template>
  <div class="major-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>专业管理</span>
          <el-button type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon>
            添加专业
          </el-button>
        </div>
      </template>

      <!-- 搜索和筛选 -->
      <div class="filter-container">
        <el-input
          v-model="searchText"
          placeholder="搜索专业名称"
          style="width: 300px; margin-right: 10px;"
          clearable
        />
        <el-select
          v-model="selectedCollegeId"
          placeholder="选择学院"
          style="width: 200px; margin-right: 10px;"
          clearable
        >
          <el-option
            v-for="college in colleges"
            :key="college.id"
            :label="college.name"
            :value="college.id"
          />
        </el-select>
        <el-button type="primary" @click="loadMajors">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
      </div>

      <!-- 专业表格 -->
      <el-table
        :data="filteredMajors"
        v-loading="loading"
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="专业名称" min-width="200" />
        <el-table-column prop="collegeName" label="所属学院" min-width="150" />
        <el-table-column prop="description" label="专业描述" min-width="300" show-overflow-tooltip />
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

    <!-- 添加/编辑专业对话框 -->
    <el-dialog
      :title="isEditing ? '编辑专业' : '添加专业'"
      v-model="showAddDialog"
      width="500px"
    >
      <el-form
        :model="majorForm"
        :rules="rules"
        ref="majorFormRef"
        label-width="100px"
      >
        <el-form-item label="专业名称" prop="name">
          <el-input v-model="majorForm.name" />
        </el-form-item>
        <el-form-item label="所属学院" prop="collegeId">
          <el-select v-model="majorForm.collegeId" placeholder="请选择学院">
            <el-option
              v-for="college in colleges"
              :key="college.id"
              :label="college.name"
              :value="college.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="专业描述" prop="description">
          <el-input
            v-model="majorForm.description"
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
  name: 'MajorManagement',
  data() {
    return {
      majors: [],
      filteredMajors: [],
      colleges: [],
      loading: false,
      searchText: '',
      selectedCollegeId: null,
      showAddDialog: false,
      isEditing: false,
      majorForm: {
        name: '',
        collegeId: null,
        description: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入专业名称', trigger: 'blur' }
        ],
        collegeId: [
          { required: true, message: '请选择所属学院', trigger: 'change' }
        ]
      }
    }
  },
  async mounted() {
    await this.loadColleges()
    await this.loadMajors()
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
      this.loading = true
      try {
        const response = await adminApi.getAllMajors()
        if (response.success) {
          this.majors = response.majors || []
          // 为专业添加学院名称
          this.majors.forEach(major => {
            const college = this.colleges.find(c => c.id === major.collegeId)
            major.collegeName = college ? college.name : '未知学院'
          })
          this.filteredMajors = this.majors
        } else {
          this.$message.error('加载专业列表失败')
        }
      } catch (error) {
        console.error('加载专业列表失败:', error)
        this.$message.error('加载专业列表失败')
      } finally {
        this.loading = false
      }
    },

    resetSearch() {
      this.searchText = ''
      this.selectedCollegeId = null
      this.filteredMajors = this.majors
    },

    handleEdit(major) {
      this.isEditing = true
      this.majorForm = { ...major }
      this.showAddDialog = true
    },

    async handleDelete(major) {
      try {
        await this.$confirm('确定要删除这个专业吗？', '提示', {
          type: 'warning'
        })
        
        const response = await adminApi.deleteMajor(major.id)
        if (response.success) {
          this.$message.success('删除成功')
          await this.loadMajors()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除专业失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.majorFormRef.validate()
        
        let response
        if (this.isEditing) {
          response = await adminApi.updateMajor(this.majorForm.id, this.majorForm)
        } else {
          response = await adminApi.createMajor(this.majorForm)
        }
        
        if (response.success) {
          this.$message.success(this.isEditing ? '更新成功' : '添加成功')
          this.showAddDialog = false
          await this.loadMajors()
          this.resetForm()
        } else {
          this.$message.error(response.message || (this.isEditing ? '更新失败' : '添加失败'))
        }
      } catch (error) {
        console.error('提交表单失败:', error)
      }
    },

    resetForm() {
      this.majorForm = {
        name: '',
        collegeId: null,
        description: ''
      }
      this.isEditing = false
      if (this.$refs.majorFormRef) {
        this.$refs.majorFormRef.resetFields()
      }
    },

    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString('zh-CN')
    }
  },
  watch: {
    searchText(val) {
      this.filteredMajors = this.majors.filter(major =>
        major.name.includes(val)
      )
    },
    selectedCollegeId(val) {
      if (val) {
        this.filteredMajors = this.majors.filter(major => major.collegeId === val)
      } else {
        this.filteredMajors = this.majors
      }
    }
  }
}
</script>

<style scoped>
.major-management {
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
