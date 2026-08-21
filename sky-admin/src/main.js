// 1. 引入 Vue 核心库
import { createApp } from 'vue'
// 2. 引入根组件（App.vue 是整个应用的最外层容器）
import App from './App.vue'
// 3. 引入路由（管理页面跳转）
import router from './router'
// 4. 引入状态管理（存用户信息、token）
import { createPinia } from 'pinia'
// 5. 引入 Element Plus UI 组件库
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 创建一个 Vue 应用实例
const app = createApp(App)

// 把“插件”一个个装上
app.use(createPinia())      // 装 Pinia（状态管理）
app.use(router)              // 装路由
app.use(ElementPlus)         // 装 UI 组件库

// 挂载到 index.html 里的 <div id="app"> 上
app.mount('#app')