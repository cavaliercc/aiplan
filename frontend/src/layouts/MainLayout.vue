<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const searchQuery = ref('')
const menuItems = [
  { path: '/dashboard', title: '仪表板', icon: 'Odometer' },
  { path: '/employees', title: '员工管理', icon: 'User' },
  { path: '/employee-directory', title: '排班计划', icon: 'Calendar' },
  { path: '/stores', title: '门店位置', icon: 'Location' },
]

const activeMenu = computed(() => route.path)

function handleMenuSelect(path: string) {
  router.push(path)
}

function handleCommand(cmd: string) {
  if (cmd === 'logout') {
    // mock logout
  }
}
</script>

<template>
  <div class="flex h-screen bg-gray-50">
    <!-- Sidebar -->
    <aside class="w-60 bg-white border-r border-gray-200 flex flex-col shrink-0">
      <!-- Logo -->
      <div class="h-14 flex items-center px-5 border-b border-gray-100">
        <div class="w-8 h-8 bg-blue-500 rounded-lg flex items-center justify-center mr-3">
          <el-icon :size="18" color="#fff"><Calendar /></el-icon>
        </div>
        <span class="text-base font-semibold text-gray-800">智汇排班</span>
      </div>

      <!-- Department -->
      <div class="px-5 py-3">
        <span class="text-xs text-gray-400">人力资源部</span>
      </div>

      <!-- New Shift Button -->
      <div class="px-4 mb-2">
        <el-button type="primary" class="w-full" @click="router.push('/scheduling')">
          <el-icon class="mr-1"><Plus /></el-icon>
          新增排班
        </el-button>
      </div>

      <!-- Menu -->
      <el-menu
        :default-active="activeMenu"
        class="border-none flex-1"
        @select="handleMenuSelect"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>

      <!-- Bottom user -->
      <div class="px-4 py-3 border-t border-gray-100 flex items-center gap-2">
        <el-avatar :size="32" class="bg-blue-500">管</el-avatar>
        <div class="text-sm">
          <div class="text-gray-700 font-medium">管理员</div>
          <div class="text-gray-400 text-xs">admin@store.com</div>
        </div>
      </div>
    </aside>

    <!-- Main -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- TopBar -->
      <header class="h-14 bg-white border-b border-gray-200 flex items-center justify-between px-6 shrink-0">
        <div class="flex items-center gap-4">
          <h2 class="text-lg font-semibold text-gray-800 m-0">{{ (route.meta as any).title || '仪表板' }}</h2>
        </div>
        <div class="flex items-center gap-4">
          <el-input
            v-model="searchQuery"
            placeholder="搜索员工、门店..."
            class="w-56"
            :prefix-icon="'Search'"
            clearable
            size="default"
          />
          <el-badge :value="3" :max="99">
            <el-button :icon="'Bell'" circle />
          </el-badge>
          <el-button :icon="'Setting'" circle />
          <el-button :icon="'QuestionFilled'" circle />
          <el-dropdown @command="handleCommand">
            <div class="flex items-center gap-2 cursor-pointer">
              <el-avatar :size="32" class="bg-blue-500">张</el-avatar>
              <span class="text-sm text-gray-700">张经理</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="settings">系统设置</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- Content -->
      <main class="flex-1 overflow-auto p-6">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
.el-menu {
  border-right: none !important;
}
.el-menu-item {
  height: 44px;
  line-height: 44px;
  margin: 2px 8px;
  border-radius: 8px;
}
.el-menu-item.is-active {
  background-color: #ecf5ff;
  color: #409eff;
}
</style>
