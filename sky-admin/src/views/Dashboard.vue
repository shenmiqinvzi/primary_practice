<template>
  <div>
    <h2 class="page-title">工作台</h2>

    <!-- 四张统计卡片：数据来自后端订单统计接口 -->
    <div class="stats">
      <el-card v-for="item in cards" :key="item.label">
        <div class="label">{{ item.label }}</div>
        <strong>{{ item.value }}</strong>
      </el-card>
    </div>

    <div class="page-card shop-card">
      <div><h3>店铺营业状态</h3><p>顾客端会根据这个状态决定是否允许下单。</p></div>
      <!-- v-model 会把开关状态和 open 变量自动同步 -->
      <el-switch v-model="open" active-text="营业中" inactive-text="已打烊" @change="changeStatus" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { orderApi, shopApi } from '@/api'

const open = ref(false)
const cards = ref([
  { label: '待接单', value: 0 },
  { label: '已接单', value: 0 },
  { label: '派送中', value: 0 },
  { label: '店铺状态', value: '读取中' }
])

const load = async () => {
  const [statistics, status] = await Promise.all([orderApi.statistics(), shopApi.status()])
  cards.value[0].value = statistics?.toBeConfirmed ?? 0
  cards.value[1].value = statistics?.confirmed ?? 0
  cards.value[2].value = statistics?.deliveryInProgress ?? 0
  open.value = Number(status) === 1
  cards.value[3].value = open.value ? '营业中' : '已打烊'
}

const changeStatus = async (value) => {
  try {
    await shopApi.setStatus(value ? 1 : 0)
    cards.value[3].value = value ? '营业中' : '已打烊'
    ElMessage.success('店铺状态已更新')
  } catch {
    // 请求失败时恢复原来的开关状态，避免页面显示和后端不一致。
    open.value = !value
  }
}

onMounted(load)
</script>

<style scoped>.stats{display:grid;grid-template-columns:repeat(4,1fr);gap:18px}.label{color:#909399;margin-bottom:12px}.stats strong{font-size:28px}.shop-card{margin-top:20px;display:flex;justify-content:space-between;align-items:center}.shop-card h3{margin:0 0 8px}.shop-card p{color:#909399;margin:0}</style>
