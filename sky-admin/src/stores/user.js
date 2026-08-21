import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')

  const setToken = (t) => {
    token.value = t
    localStorage.setItem('token', t)
  }

  const clearToken = () => {
    token.value = ''
    localStorage.removeItem('token')
  }

  return { token, setToken, clearToken }
})
