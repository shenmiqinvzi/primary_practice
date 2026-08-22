<template>
  <div class="page-card">
    <h2 class="page-title">分类管理</h2>

    <div class="toolbar">
      <el-select
        v-model="query.type"
        placeholder="全部类型"
        clearable
        style="width: 150px"
        @change="load"
      >
        <el-option label="菜品分类" :value="1" />
        <el-option label="套餐分类" :value="2" />
      </el-select>
      <el-button type="primary" @click="openAdd">新增分类</el-button>
    </div>

    <el-table :data="rows" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="type" label="类型">
        <template #default="{ row }">
          {{ row.type === 1 ? '菜品' : '套餐' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-switch
            v-model="row.status"
            :active-value="1"
            :inactive-value="0"
            @change="toggle(row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.page"
      :page-size="query.pageSize"
      :total="total"
      layout="total,prev,pager,next"
      @current-change="load"
    />
  </div>

  <el-dialog
    v-model="visible"
    :title="editing ? '编辑分类' : '新增分类'"
    width="400px"
  >
    <el-form :model="form" label-width="80px">
      <el-form-item label="名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="类型">
        <el-radio-group v-model="form.type">
          <el-radio :value="1">菜品</el-radio>
          <el-radio :value="2">套餐</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { categoryApi } from '@/api'

const rows = ref([])
const total = ref(0)
const query = ref({ page: 1, pageSize: 10, type: null })
const visible = ref(false)
const editing = ref(false)
const form = ref({})

const load = async () => {
  const result = await categoryApi.page(query.value)
  rows.value = result.records || []
  total.value = result.total || 0
}

const openAdd = () => {
  editing.value = false
  form.value = { name: '', type: 1 }
  visible.value = true
}

const openEdit = (row) => {
  editing.value = true
  form.value = { ...row }
  visible.value = true
}

const save = async () => {
  if (editing.value) {
    await categoryApi.update(form.value)
  } else {
    await categoryApi.save(form.value)
  }

  ElMessage.success('保存成功')
  visible.value = false
  load()
}

const toggle = async (row) => {
  try {
    await categoryApi.changeStatus(row.status, row.id)
  } catch {
    row.status = row.status ? 0 : 1
  }
}

const remove = async (row) => {
  await ElMessageBox.confirm('确认删除该分类？', '提示')
  await categoryApi.remove(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
