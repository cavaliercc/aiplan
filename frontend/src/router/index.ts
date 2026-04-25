import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/Login.vue'),
      meta: { public: true },
    },
    {
      path: '/',
      component: MainLayout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('../views/Dashboard.vue'),
          meta: { title: '仪表板', icon: 'Odometer' },
        },
        {
          path: 'employees',
          name: 'EmployeeManagement',
          component: () => import('../views/EmployeeManagement.vue'),
          meta: { title: '员工管理', icon: 'User' },
        },
        {
          path: 'employee-directory',
          name: 'EmployeeDirectory',
          component: () => import('../views/EmployeeDirectory.vue'),
          meta: { title: '排班计划', icon: 'Calendar' },
        },
        {
          path: 'stores',
          name: 'StoreDistribution',
          component: () => import('../views/StoreDistribution.vue'),
          meta: { title: '门店位置', icon: 'Location' },
        },
        {
          path: 'scheduling',
          name: 'ShiftScheduling',
          component: () => import('../views/ShiftScheduling.vue'),
          meta: { title: '新增班次', icon: 'Plus' },
        },
      ],
    },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (!to.meta.public && !token) {
    return { name: 'Login' }
  }
  if (to.name === 'Login' && token) {
    return { name: 'Dashboard' }
  }
})

export default router
