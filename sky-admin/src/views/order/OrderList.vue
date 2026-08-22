<template>
  <div class="page-card">
    <h2 class="page-title">订单管理</h2>
    <div class="toolbar">
      <el-input v-model="query.number" placeholder="订单号" clearable style="width: 200px" />
      <el-input v-model="query.phone" placeholder="手机号" clearable style="width: 160px" />
      <el-select v-model="query.status" placeholder="订单状态" clearable style="width: 140px">
        <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <el-table :data="rows" v-loading="loading" stripe>
      <el-table-column prop="number" label="订单号" min-width="180" />
      <el-table-column prop="consignee" label="收货人" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="amount" label="金额"><template #default="{ row }">¥{{ row.amount }}</template></el-table-column>
      <el-table-column label="状态"><template #default="{ row }"><el-tag>{{ statusName(row.status) }}</el-tag></template></el-table-column>
      <el-table-column prop="orderTime" label="下单时间" min-width="170" />
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDetail(row)">详情</el-button>
          <el-button v-if="row.status === 2" link type="success" @click="action('confirm', row.id)">接单</el-button>
          <el-button v-if="row.status === 2" link type="danger" @click="action('rejection', row.id)">拒单</el-button>
          <el-button v-if="row.status === 3" link type="primary" @click="deliver(row.id)">派送</el-button>
          <el-button v-if="row.status === 4" link type="success" @click="complete(row.id)">完成</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" :page-size="query.pageSize" :total="total" layout="total,prev,pager,next" @current-change="load" />
  </div>

  <el-drawer v-model="drawer" title="订单详情" size="450px">
    <el-descriptions v-if="detail" :column="1" border>
      <el-descriptions-item label="订单号">{{ detail.number }}</el-descriptions-item>
      <el-descriptions-item label="收货人">{{ detail.consignee }} {{ detail.phone }}</el-descriptions-item>
      <el-descriptions-item label="配送地址">{{ detail.address }}</el-descriptions-item>
      <el-descriptions-item label="订单金额">¥{{ detail.amount }}</el-descriptions-item>
      <el-descriptions-item label="订单备注">{{ detail.remark || '无' }}</el-descriptions-item>
    </el-descriptions>
    <h3>商品明细</h3>
    <el-table :data="detail?.orderDetailList || []">
      <el-table-column prop="name" label="商品" />
      <el-table-column prop="number" label="数量" />
      <el-table-column prop="amount" label="小计" />
    </el-table>
  </el-drawer>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { orderApi } from '@/api'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const drawer = ref(false)
const detail = ref(null)
const query = ref({ page: 1, pageSize: 10, number: '', phone: '', status: null })
const statusOptions = [
  { label: '待付款', value: 1 }, { label: '待接单', value: 2 }, { label: '已接单', value: 3 },
  { label: '派送中', value: 4 }, { label: '已完成', value: 5 }, { label: '已取消', value: 6 }
]

const statusName = (status) => statusOptions.find((item) => item.value === status)?.label || '未知'
const load = async () => { loading.value = true; try { const result = await orderApi.page(query.value); rows.value = result?.records || []; total.value = result?.total || 0 } finally { loading.value = false } }
const showDetail = async (row) => { detail.value = await orderApi.detail(row.id); drawer.value = true }
const action = async (name, id) => { const prompt = name === 'rejection' ? await ElMessageBox.prompt('请输入拒单原因', '拒单') : null; await orderApi.action(name, { id, rejectionReason: prompt?.value }); ElMessage.success('操作成功'); load() }
const deliver = async (id) => { await orderApi.delivery(id); ElMessage.success('已开始派送'); load() }
const complete = async (id) => { await orderApi.complete(id); ElMessage.success('订单已完成'); load() }

onMounted(load)
</script>
