import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import StudentHome from '../views/StudentHome.vue'
import TeacherHome from '../views/TeacherHome.vue'
import AdminHome from '../views/AdminHome.vue'
import HomeworkList from '../views/HomeworkList.vue'
import SubmissionList from '../views/SubmissionList.vue'
import Messages from '../views/Messages.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  // 学生端路由
  {
    path: '/student/home',
    name: 'StudentHome',
    component: StudentHome,
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/student/homework',
    name: 'StudentHomework',
    component: HomeworkList,
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/student/submissions',
    name: 'StudentSubmissions',
    component: SubmissionList,
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/student/messages',
    name: 'StudentMessages',
    component: Messages,
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  // 教师端路由
  {
    path: '/teacher/home',
    name: 'TeacherHome',
    component: TeacherHome,
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/homework',
    name: 'TeacherHomework',
    component: HomeworkList,
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/submissions',
    name: 'TeacherSubmissions',
    component: SubmissionList,
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  {
    path: '/teacher/messages',
    name: 'TeacherMessages',
    component: Messages,
    meta: { requiresAuth: true, role: 'TEACHER' }
  },
  // 管理员端路由
  {
    path: '/admin/home',
    name: 'AdminHome',
    component: AdminHome,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/colleges',
    name: 'AdminColleges',
    component: AdminHome,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/majors',
    name: 'AdminMajors',
    component: AdminHome,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/classes',
    name: 'AdminClasses',
    component: AdminHome,
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/students',
    name: 'AdminStudents',
    component: AdminHome,
    meta: { requiresAuth: true, role: 'ADMIN' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  
  // 检查是否需要认证
  if (to.meta.requiresAuth && !user.id) {
    next('/login')
    return
  }
  
  // 检查角色权限
  if (to.meta.role && user.role !== to.meta.role) {
    // 如果角色不匹配，重定向到对应角色的首页
    if (user.role === 'STUDENT') {
      next('/student/home')
    } else if (user.role === 'TEACHER') {
      next('/teacher/home')
    } else if (user.role === 'ADMIN') {
      next('/admin/home')
    } else {
      next('/login')
    }
    return
  }
  
  next()
})

export default router
