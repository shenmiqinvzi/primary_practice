<template>
  <el-container>
    <!-- 左侧菜单 -->
    <el-aside width="200px">
      <el-menu router>
        <el-menu-item index="/employee">员工管理</el-menu-item>
        <el-menu-item index="/category">分类管理</el-menu-item>
        <el-menu-item index="/dish">菜品管理</el-menu-item>
        <el-menu-item index="/setmeal">套餐管理</el-menu-item>
        <el-menu-item index="/order">订单管理</el-menu-item>
        <el-menu-item index="/report">数据统计</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <!-- 顶部栏 -->
      <el-header>
        <span>苍穹外卖后台</span>
        <el-button @click="logout">退出</el-button>
      </el-header>
      <!-- 内容区（子路由渲染到这里） -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const logout = () => {
  userStore.clearToken()
  router.push('/login')
}
</script>
