<template>
  <div class="page-card">
    <h2 class="page-title">菜品管理</h2>

    <div class="toolbar">
      <el-input v-model="query.name" placeholder="输入菜品名称" clearable style="width:220px" @keyup.enter="load" />
      <el-select v-model="query.categoryId" placeholder="选择分类" clearable style="width:180px" @change="load">
        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="success" @click="openAdd">新增菜品</el-button>
    </div>

    <el-table :data="rows" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="图片" width="90"><template #default="{ row }"><el-image v-if="row.image" :src="row.image" style="width:50px;height:50px" fit="cover" /></template></el-table-column>
      <el-table-column prop="name" label="菜品名称" />
      <el-table-column prop="categoryName" label="分类" />
      <el-table-column prop="price" label="价格"><template #default="{ row }">¥{{ row.price }}</template></el-table-column>
      <el-table-column prop="status" label="状态"><template #default="{ row }"><el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="changeStatus(row)" /></template></el-table-column>
      <el-table-column label="操作"><template #default="{ row }"><el-button link type="primary" @click="openEdit(row)">编辑</el-button></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" :page-size="query.pageSize" :total="total" layout="total,prev,pager,next" @current-change="load" />
  </div>

  <el-dialog v-model="visible" :title="editing ? '编辑菜品' : '新增菜品'" width="600px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="菜品名称"><el-input v-model="form.name" /></el-form-item>
      <el-form-item label="分类"><el-select v-model="form.categoryId" placeholder="选择分类"><el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item>
      <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" :precision="2" /></el-form-item>
      <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      <el-form-item label="图片"><el-upload :show-file-list="false" :http-request="uploadImage"><el-button>选择图片</el-button></el-upload><el-image v-if="form.image" :src="form.image" style="width:80px;height:80px;margin-top:8px" /></el-form-item>
      <el-form-item label="口味"><div class="flavors"><div v-for="(item,index) in form.flavors" :key="index" class="flavor-row"><el-input v-model="item.name" placeholder="口味名，如辣度"/><el-input v-model="item.value" placeholder="选项，如不辣/微辣"/><el-button link type="danger" @click="form.flavors.splice(index,1)">删除</el-button></div><el-button link type="primary" @click="form.flavors.push({name:'',value:''})">+ 添加口味</el-button></div></el-form-item>
    </el-form>
    <template #footer><el-button @click="visible=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
  </el-dialog>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { categoryApi, dishApi } from '@/api'

const rows = ref([]); const total = ref(0); const loading = ref(false)
const categories = ref([]); const visible = ref(false); const editing = ref(false)
const query = ref({ page: 1, pageSize: 10, name: '', categoryId: null })
const emptyForm = () => ({ name: '', categoryId: null, price: 0, image: '', description: '', status: 1, flavors: [] })
const form = ref(emptyForm())

const loadCategories = async () => { categories.value = await categoryApi.list(1) }
const load = async () => { loading.value = true; try { const result = await dishApi.page(query.value); rows.value = result?.records || []; total.value = result?.total || 0 } finally { loading.value = false } }
const openAdd = () => { editing.value = false; form.value = emptyForm(); visible.value = true }
const openEdit = async (row) => { editing.value = true; form.value = await dishApi.detail(row.id); form.value.flavors ||= []; visible.value = true }
const save = async () => { await (editing.value ? dishApi.update(form.value) : dishApi.save(form.value)); ElMessage.success('保存成功'); visible.value = false; load() }
const changeStatus = async (row) => { try { await dishApi.changeStatus(row.status, row.id) } catch { row.status = row.status ? 0 : 1 } }
const uploadImage = async ({ file, onSuccess, onError }) => { try { const result = await dishApi.upload(file); form.value.image = result?.url || result; onSuccess(result) } catch (error) { onError(error) } }
onMounted(async () => { await loadCategories(); await load() })
</script>

<style scoped>.flavors{width:100%}.flavor-row{display:flex;gap:8px;margin-bottom:8px}.flavor-row .el-input{flex:1}</style>
