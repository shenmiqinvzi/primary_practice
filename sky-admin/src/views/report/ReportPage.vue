<template>
  <div>
    <h2 class="page-title">数据统计</h2>

    <div class="toolbar">
      <el-date-picker
        v-model="dates"
        type="daterange"
        value-format="YYYY-MM-DD"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
      />
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="download">导出 Excel</el-button>
    </div>

    <div class="stats">
      <el-card>
        <div>营业额</div>
        <strong>{{ turnover }}</strong>
      </el-card>
      <el-card>
        <div>新增用户</div>
        <strong>{{ users }}</strong>
      </el-card>
      <el-card>
        <div>订单数</div>
        <strong>{{ orders }}</strong>
      </el-card>
    </div>

    <div class="page-card">
      <h3>销量 Top10</h3>
      <el-table :data="top">
        <el-table-column prop="name" label="商品" />
        <el-table-column prop="number" label="销量" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { reportApi } from '@/api'

const dates = ref([])
const turnover = ref('--')
const users = ref('--')
const orders = ref('--')
const top = ref([])

const getParams = () => ({
  begin: dates.value?.[0],
  end: dates.value?.[1]
})

const load = async () => {
  if (!dates.value?.length) return

  const [turnoverData, userData, orderData, topData] = await Promise.all([
    reportApi.turnover(getParams()),
    reportApi.users(getParams()),
    reportApi.orders(getParams()),
    reportApi.top10(getParams())
  ])

  turnover.value = turnoverData?.turnoverList
    ?.reduce((sum, item) => sum + Number(item || 0), 0)
    .toFixed(2) || '0.00'

  users.value = userData?.newUserList
    ?.reduce((sum, item) => sum + Number(item || 0), 0) || 0

  orders.value = orderData?.totalOrderCount ?? 0

  // 后端把商品名称和销量分成两个数组，前端要按下标重新组合。
  top.value = (topData?.nameList || []).map((name, index) => ({
    name,
    number: topData.numberList?.[index] ?? 0
  }))
}

// 导出接口返回文件流，让浏览器直接打开下载地址即可。
const download = () => {
  reportApi.export(getParams()).then(response => {
    const url = URL.createObjectURL(response)
    const link = document.createElement('a')
    link.href = url
    link.download = '订单报表.xlsx'
    link.click()
    URL.revokeObjectURL(url)
  })
}
</script>

<style scoped>
.stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  margin-bottom: 20px;
}

.stats strong {
  display: block;
  margin-top: 10px;
  font-size: 28px;
}
</style>
