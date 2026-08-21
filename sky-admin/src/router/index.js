import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    children: [
      { path: '', redirect: '/order' },
      { path: 'employee', component: () => import('@/views/employee/EmployeeList.vue') },
      { path: 'category', component: () => import('@/views/category/CategoryList.vue') },
      { path: 'dish', component: () => import('@/views/dish/DishList.vue') },
      { path: 'setmeal', component: () => import('@/views/setmeal/SetmealList.vue') },
      { path: 'order', component: () => import('@/views/order/OrderList.vue') },
      { path: 'report', component: () => import('@/views/report/ReportPage.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
