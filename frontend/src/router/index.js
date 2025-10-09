import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import StudentList from '../views/StudentList.vue'
import HomeworkList from '../views/HomeworkList.vue'
import SubmissionList from '../views/SubmissionList.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/students',
    name: 'StudentList',
    component: StudentList
  },
  {
    path: '/homework',
    name: 'HomeworkList',
    component: HomeworkList
  },
  {
    path: '/submissions',
    name: 'SubmissionList',
    component: SubmissionList
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
