<template>
  <div class="page-card">
    <h2 class="page-title">员工管理</h2>

    <div class="toolbar">
      <el-input v-model="query.name" placeholder="按姓名搜索" clearable style="width: 220px" @keyup.enter="load" />
      <el-button type="primary" @click="load">搜索</el-button>
      <span class="grow" />
      <el-button type="primary" @click="openAdd">新增员工</el-button>
    </div>

    <el-table :data="rows" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="toggle(row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.page"
      v-model:page-size="query.pageSize"
      :total="total"
      layout="total,prev,pager,next"
      @current-change="load"
    />
  </div>

  <el-dialog v-model="visible" :title="editing ? '编辑员工' : '新增员工'" width="420px">
    <el-form :model="form" label-width="70px">
      <el-form-item label="账号"><el-input v-model="form.username" /></el-form-item>
      <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
      <el-form-item v-if="!editing" label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
      <el-form-item label="手机"><el-input v-model="form.phone" /></el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { employeeApi } from '@/api'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const visible = ref(false)
const editing = ref(false)
const query = ref({ page: 1, pageSize: 10, name: '' })
const form = ref({})

const load = async () => {
  loading.value = true
  try {
    const result = await employeeApi.page(query.value)
    rows.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

const openAdd = () => {
  editing.value = false
  form.value = { username: '', name: '', password: '', phone: '' }
  visible.value = true
}

const openEdit = (row) => {
  editing.value = true
  form.value = { ...row }
  visible.value = true
}

const save = async () => {
  if (editing.value) {
    await employeeApi.update(form.value)
  } else {
    await employeeApi.save(form.value)
  }

  ElMessage.success('保存成功')
  visible.value = false
  load()
}

const toggle = async (row) => {
  try {
    await employeeApi.changeStatus(row.status, row.id)
  } catch {
    row.status = row.status ? 0 : 1
  }
}

onMounted(load)
</script>
