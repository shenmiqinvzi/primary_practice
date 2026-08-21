<template>
  <div style="width:300px;margin:150px auto">
    <h2 style="text-align:center">苍穹外卖后台登录</h2>
    <el-form :model="form" ref="formRef" :rules="rules">
      <el-form-item prop="username">
        <el-input v-model="form.username" placeholder="用户名" />
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" type="password" placeholder="密码" />
      </el-form-item>
      <el-button type="primary" style="width:100%" @click="handleLogin">登录</el-button>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { login as loginApi } from '@/api/employee'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)

const form = ref({ username: '', password: '' })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  try {
    const res = await loginApi(form.value)
    userStore.setToken(res.token)
    router.push('/')
  } catch (e) {
    // 错误已被拦截器弹窗
  }
}
</script>
