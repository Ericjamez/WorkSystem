<template>
  <div class="college-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学院管理</span>
          <el-button type="primary" @click="showAddDialog = true">
            <el-icon><Plus /></el-icon>
            添加学院
          </el-button>
        </div>
      </template>

      <!-- 搜索和筛选 -->
      <div class="filter-container">
        <el-input
          v-model="searchText"
          placeholder="搜索学院名称"
          style="width: 300px; margin-right: 10px;"
          clearable
        />
        <el-button type="primary" @click="loadColleges">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
      </div>

      <!-- 学院表格 -->
      <el-table
        :data="filteredColleges"
        v-loading="loading"
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="学院名称" min-width="200" />
        <el-table-column prop="description" label="学院描述" min-width="300" show-overflow-tooltip />
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

    <!-- 添加/编辑学院对话框 -->
    <el-dialog
      :title="isEditing ? '编辑学院' : '添加学院'"
      v-model="showAddDialog"
      width="500px"
    >
      <el-form
        :model="collegeForm"
        :rules="rules"
        ref="collegeFormRef"
        label-width="100px"
      >
        <el-form-item label="学院名称" prop="name">
          <el-input v-model="collegeForm.name" />
        </el-form-item>
        <el-form-item label="学院描述" prop="description">
          <el-input
            v-model="collegeForm.description"
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
  name: 'CollegeManagement',
  data() {
    return {
      colleges: [],
      filteredColleges: [],
      loading: false,
      searchText: '',
      showAddDialog: false,
      isEditing: false,
      collegeForm: {
        name: '',
        description: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入学院名称', trigger: 'blur' }
        ]
      }
    }
  },
  async mounted() {
    await this.loadColleges()
  },
  methods: {
    async loadColleges() {
      this.loading = true
      try {
        const response = await adminApi.getAllColleges()
        if (response.success) {
          this.colleges = response.colleges || []
          this.filteredColleges = this.colleges
        } else {
          this.$message.error('加载学院列表失败')
        }
      } catch (error) {
        console.error('加载学院列表失败:', error)
        this.$message.error('加载学院列表失败')
      } finally {
        this.loading = false
      }
    },

    resetSearch() {
      this.searchText = ''
      this.filteredColleges = this.colleges
    },

    handleEdit(college) {
      this.isEditing = true
      this.collegeForm = { ...college }
      this.showAddDialog = true
    },

    async handleDelete(college) {
      try {
        await this.$confirm('确定要删除这个学院吗？', '提示', {
          type: 'warning'
        })
        
        const response = await adminApi.deleteCollege(college.id)
        if (response.success) {
          this.$message.success('删除成功')
          await this.loadColleges()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除学院失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.collegeFormRef.validate()
        
        let response
        if (this.isEditing) {
          response = await adminApi.updateCollege(this.collegeForm.id, this.collegeForm)
        } else {
          response = await adminApi.createCollege(this.collegeForm)
        }
        
        if (response.success) {
          this.$message.success(this.isEditing ? '更新成功' : '添加成功')
          this.showAddDialog = false
          await this.loadColleges()
          this.resetForm()
        } else {
          this.$message.error(response.message || (this.isEditing ? '更新失败' : '添加失败'))
        }
      } catch (error) {
        console.error('提交表单失败:', error)
      }
    },

    resetForm() {
      this.collegeForm = {
        name: '',
        description: ''
      }
      this.isEditing = false
      if (this.$refs.collegeFormRef) {
        this.$refs.collegeFormRef.resetFields()
      }
    },

    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString('zh-CN')
    }
  },
  watch: {
    searchText(val) {
      this.filteredColleges = this.colleges.filter(college =>
        college.name.includes(val)
      )
    }
  }
}
</script>

<style scoped>
.college-management {
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
