import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',          // 访问 /login 时
    component: () => import('@/views/Login.vue')  // 显示登录页
  },
  {
    path: '/',               // 访问根路径时
    component: () => import('@/views/Layout.vue'), // 先显示布局（左侧菜单+顶部栏）
    children: [              // 布局里的内容区，根据子路由切换
      {
        path: '',            // 默认子路由（/ 时显示）
        component: () => import('@/views/order/OrderList.vue')
      },
      {
        path: '/dish',       // /dish 时
        component: () => import('@/views/dish/DishList.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫（保安）
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    // 没登录，扔去登录页
    next('/login')
  } else {
    next() // 放行
  }
})

export default router