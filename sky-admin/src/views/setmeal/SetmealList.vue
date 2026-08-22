<template>
  <div class="page-card">
    <h2 class="page-title">套餐管理</h2>
    <div class="toolbar">
      <el-input v-model="query.name" placeholder="套餐名称" clearable style="width: 220px" @keyup.enter="load" />
      <el-select v-model="query.categoryId" placeholder="套餐分类" clearable style="width: 180px" @change="load">
        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="success" @click="openAdd">新增套餐</el-button>
    </div>

    <el-table :data="rows" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="套餐名称" />
      <el-table-column prop="categoryName" label="分类" />
      <el-table-column prop="price" label="价格"><template #default="{ row }">¥{{ row.price }}</template></el-table-column>
      <el-table-column prop="status" label="状态"><template #default="{ row }"><el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="changeStatus(row)" /></template></el-table-column>
      <el-table-column label="操作"><template #default="{ row }"><el-button link type="primary" @click="openEdit(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template></el-table-column>
    </el-table>

    <el-pagination v-model:current-page="query.page" :page-size="query.pageSize" :total="total" layout="total,prev,pager,next" @current-change="load" />
  </div>

  <el-dialog v-model="visible" :title="editing ? '编辑套餐' : '新增套餐'" width="700px">
    <el-form :model="form" label-width="90px">
      <el-form-item label="套餐名称"><el-input v-model="form.name" /></el-form-item>
      <el-form-item label="分类"><el-select v-model="form.categoryId"><el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item>
      <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" :precision="2" /></el-form-item>
      <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      <el-form-item label="图片"><el-upload :show-file-list="false" :http-request="uploadImage"><el-button>选择图片</el-button></el-upload><el-image v-if="form.image" :src="form.image" style="width:80px;height:80px;margin-top:8px" /></el-form-item>
      <el-form-item label="关联菜品">
        <div class="dish-list">
          <div v-for="item in form.setmealDishes" :key="item.dishId" class="dish-row">
            <el-select v-model="item.dishId" placeholder="选择菜品" @change="fillDish(item)"><el-option v-for="dish in dishes" :key="dish.id" :label="`${dish.name} ¥${dish.price}`" :value="dish.id" /></el-select>
            <el-input-number v-model="item.copies" :min="1" size="small" />
            <el-button link type="danger" @click="removeDish(item.dishId)">移除</el-button>
          </div>
          <el-button link type="primary" @click="addDish">+ 添加菜品</el-button>
        </div>
      </el-form-item>
    </el-form>
    <template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
  </el-dialog>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { categoryApi, dishApi, setmealApi } from '@/api'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const categories = ref([])
const dishes = ref([])
const visible = ref(false)
const editing = ref(false)
const query = ref({ page: 1, pageSize: 10, name: '', categoryId: null })
const emptyForm = () => ({ name: '', categoryId: null, price: 0, image: '', description: '', status: 1, setmealDishes: [] })
const form = ref(emptyForm())

const load = async () => {
  loading.value = true
  try {
    const result = await setmealApi.page(query.value)
    rows.value = result?.records || []
    total.value = result?.total || 0
  } finally {
    loading.value = false
  }
}

const openAdd = () => { editing.value = false; form.value = emptyForm(); visible.value = true }
const openEdit = async (row) => { editing.value = true; form.value = await setmealApi.detail(row.id); form.value.setmealDishes ||= []; visible.value = true }
const save = async () => { await (editing.value ? setmealApi.update(form.value) : setmealApi.save(form.value)); ElMessage.success('保存成功'); visible.value = false; load() }
const changeStatus = async (row) => { try { await setmealApi.changeStatus(row.status, row.id) } catch { row.status = row.status ? 0 : 1 } }
const remove = async (row) => { await ElMessageBox.confirm('确认删除该套餐？', '提示'); await setmealApi.remove([row.id]); ElMessage.success('删除成功'); load() }
const uploadImage = async ({ file, onSuccess, onError }) => { try { const result = await setmealApi.upload(file); form.value.image = result?.url || result; onSuccess(result) } catch (error) { onError(error) } }
const fillDish = (item) => { const dish = dishes.value.find(row => row.id === item.dishId); if (dish) item.name = dish.name; if (dish) item.price = dish.price }
const addDish = () => form.value.setmealDishes.push({ dishId: null, name: '', copies: 1 })
const removeDish = (id) => { form.value.setmealDishes = form.value.setmealDishes.filter((item) => item.dishId !== id) }

onMounted(async () => { categories.value = await categoryApi.list(2); const result = await dishApi.page({ page: 1, pageSize: 1000 }); dishes.value = result?.records || []; await load() })
</script>

<style scoped>
.dish-list { width: 100%; }
.dish-row { display: flex; gap: 12px; align-items: center; margin-bottom: 8px; }
.dish-row span { flex: 1; }
</style>
