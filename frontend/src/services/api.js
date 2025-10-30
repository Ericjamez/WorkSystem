import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
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

// 认证相关 API
export const authApi = {
  // 登录
  login: (credentials) => api.post('/auth/login', credentials),
  
  // 学生注册
  registerStudent: (user) => api.post('/auth/register/student', user),
  
  // 教师注册
  registerTeacher: (user) => api.post('/auth/register/teacher', user)
}

// 用户相关 API
export const userApi = {
  // 获取所有学生
  getAllStudents: () => api.get('/users/students'),
  
  // 获取所有教师
  getAllTeachers: () => api.get('/users/teachers'),
  
  // 根据ID获取用户
  getUserById: (id) => api.get(`/users/${id}`)
}

// 学生相关 API (保持兼容性)
export const studentApi = {
  // 获取所有学生
  getAllStudents: () => api.get('/users/students'),
  
  // 根据ID获取学生
  getStudentById: (id) => api.get(`/users/${id}`),
  
  // 根据学号获取学生
  getStudentByStudentId: (studentId) => api.get(`/students/student-id/${studentId}`),
  
  // 创建学生
  createStudent: (student) => api.post('/auth/register/student', student),
  
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
  
  // 根据教师ID获取作业
  getHomeworkByTeacher: (teacherId) => api.get(`/homework/teacher/${teacherId}`),
  
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
  }),
  
  // 打回作业
  returnSubmission: (id, returnReason) => api.post(`/submissions/${id}/return`, { returnReason }),
  
  // 重新提交作业
  resubmitHomework: (id, content, attachmentPath) => api.post(`/submissions/${id}/resubmit`, {
    content,
    attachmentPath
  })
}

// 私信相关 API
export const messageApi = {
  // 获取用户消息
  getUserMessages: (userId) => api.get(`/messages/user/${userId}`),
  
  // 获取对话
  getConversation: (userId1, userId2) => api.get(`/messages/conversation/${userId1}/${userId2}`),
  
  // 发送消息
  sendMessage: (message) => api.post('/messages/send', message),
  
  // 标记为已读
  markAsRead: (messageId) => api.post(`/messages/mark-read/${messageId}`),
  
  // 获取未读消息数
  getUnreadCount: (userId) => api.get(`/messages/unread-count/${userId}`)
}

// 管理员相关 API
export const adminApi = {
  // 学院管理
  getAllColleges: () => api.get('/admin/colleges'),
  getCollegeById: (id) => api.get(`/admin/colleges/${id}`),
  createCollege: (college) => api.post('/admin/colleges', college),
  updateCollege: (id, college) => api.put(`/admin/colleges/${id}`, college),
  deleteCollege: (id) => api.delete(`/admin/colleges/${id}`),
  
  // 专业管理
  getAllMajors: () => api.get('/admin/majors'),
  getMajorById: (id) => api.get(`/admin/majors/${id}`),
  getMajorsByCollegeId: (collegeId) => api.get(`/admin/majors/college/${collegeId}`),
  createMajor: (major) => api.post('/admin/majors', major),
  updateMajor: (id, major) => api.put(`/admin/majors/${id}`, major),
  deleteMajor: (id) => api.delete(`/admin/majors/${id}`),
  
  // 班级管理
  getAllClasses: () => api.get('/admin/classes'),
  getClassById: (id) => api.get(`/admin/classes/${id}`),
  getClassesByMajorId: (majorId) => api.get(`/admin/classes/major/${majorId}`),
  createClass: (clazz) => api.post('/admin/classes', clazz),
  updateClass: (id, clazz) => api.put(`/admin/classes/${id}`, clazz),
  deleteClass: (id) => api.delete(`/admin/classes/${id}`)
}

export default api
