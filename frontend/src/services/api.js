import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

// 学生相关 API
export const studentApi = {
  // 获取所有学生
  getAllStudents: () => api.get('/students'),
  
  // 根据ID获取学生
  getStudentById: (id) => api.get(`/students/${id}`),
  
  // 根据学号获取学生
  getStudentByStudentId: (studentId) => api.get(`/students/student-id/${studentId}`),
  
  // 创建学生
  createStudent: (student) => api.post('/students', student),
  
  // 更新学生
  updateStudent: (id, student) => api.put(`/students/${id}`, student),
  
  // 删除学生
  deleteStudent: (id) => api.delete(`/students/${id}`),
  
  // 根据班级获取学生
  getStudentsByClass: (className) => api.get(`/students/class/${className}`)
}

// 作业相关 API
export const homeworkApi = {
  // 获取所有作业
  getAllHomework: () => api.get('/homework'),
  
  // 根据ID获取作业
  getHomeworkById: (id) => api.get(`/homework/${id}`),
  
  // 创建作业
  createHomework: (homework) => api.post('/homework', homework),
  
  // 更新作业
  updateHomework: (id, homework) => api.put(`/homework/${id}`, homework),
  
  // 删除作业
  deleteHomework: (id) => api.delete(`/homework/${id}`),
  
  // 根据课程获取作业
  getHomeworkByCourse: (courseName) => api.get(`/homework/course/${courseName}`),
  
  // 获取有效作业
  getActiveHomework: () => api.get('/homework/active')
}

// 提交相关 API
export const submissionApi = {
  // 获取所有提交
  getAllSubmissions: () => api.get('/submissions'),
  
  // 根据ID获取提交
  getSubmissionById: (id) => api.get(`/submissions/${id}`),
  
  // 根据学生ID获取提交
  getSubmissionsByStudent: (studentId) => api.get(`/submissions/student/${studentId}`),
  
  // 根据作业ID获取提交
  getSubmissionsByHomework: (homeworkId) => api.get(`/submissions/homework/${homeworkId}`),
  
  // 提交作业
  submitHomework: (submission) => api.post('/submissions', submission),
  
  // 更新提交
  updateSubmission: (id, submission) => api.put(`/submissions/${id}`, submission),
  
  // 批改作业
  gradeSubmission: (id, gradeData) => api.post(`/submissions/${id}/grade`, gradeData),
  
  // 删除提交
  deleteSubmission: (id) => api.delete(`/submissions/${id}`),
  
  // 检查是否已提交
  checkSubmission: (studentId, homeworkId) => api.get('/submissions/check-submission', {
    params: { studentId, homeworkId }
  })
}

export default api
