<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="brand">
        苍穹外卖
        <small>管理端</small>
      </div>

      <el-menu
        router
        :default-active="$route.path"
        class="menu"
        background-color="#001529"
        text-color="#bfcbd9"
        active-text-color="#fff"
      >
        <el-menu-item index="/dashboard">工作台</el-menu-item>
        <el-menu-item index="/employee">员工管理</el-menu-item>
        <el-menu-item index="/category">分类管理</el-menu-item>
        <el-menu-item index="/dish">菜品管理</el-menu-item>
        <el-menu-item index="/setmeal">套餐管理</el-menu-item>
        <el-menu-item index="/order">订单管理</el-menu-item>
        <el-menu-item index="/report">数据统计</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <span>欢迎回来，管理员</span>
        <el-button link @click="logout">退出登录</el-button>
      </el-header>
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
  router.replace('/login')
}
</script>

<style scoped>
.layout { min-height: 100vh; }
.aside { background: #001529; }
.brand { height: 60px; color: #fff; font-size: 20px; font-weight: 700; display: flex; align-items: center; padding: 0 22px; }
.brand small { font-size: 12px; margin-left: 8px; color: #91a4b7; }
.menu { border: 0; }
.header { background: #fff; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eee; }
</style>
