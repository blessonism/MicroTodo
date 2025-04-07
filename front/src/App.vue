<template>
  <div
    :class="{
      'sidebar-closed': isSidebarClosed,
      'sidebar-opened': !isSidebarClosed,
    }"
  >
    <el-container>
      <SideBarVue
        v-if="!$route.meta.hideSidebar"
        @toggle="handleSidebarToggle"
      />
      <el-container
        direction="vertical"
        class="main-container"
        :class="{ 'full-width': $route.meta.hideSidebar }"
      >
        <el-header class="header" v-if="!$route.meta.hideNav">
          <NavBarVue></NavBarVue>
        </el-header>
        <el-main
          class="content"
          :class="{ 'full-height': $route.meta.hideSidebar }"
        >
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import NavBarVue from "@/components/NavBar.vue";
import SideBarVue from "@/components/SideBar.vue";

export default {
  components: {
    NavBarVue,
    SideBarVue,
  },
  data() {
    return {
      isSidebarClosed: true,
      tasks: [], // Assuming tasks are fetched from somewhere
      selectedNode: null,
    };
  },
  created() {
    // 监听用户信息变化
    window.addEventListener('storage', this.handleStorageChange);
    
    // 初始化时同步用户信息
    this.syncUserInfo();
  },
  beforeDestroy() {
    // 清理事件监听
    window.removeEventListener('storage', this.handleStorageChange);
  },
  methods: {
    handleSidebarToggle(isClosed) {
      this.isSidebarClosed = isClosed;
    },
    updateSelectedNode(node) {
      this.selectedNode = node;
    },
    handleStorageChange(event) {
      // 当localStorage中的用户信息发生变化时，同步到Vuex
      if (event.key === 'userDisplayName') {
        this.$store.dispatch('user/updateDisplayName', event.newValue || '');
      } else if (event.key === 'userAvatar') {
        this.$store.dispatch('user/updateAvatar', event.newValue || '');
      }
    },
    syncUserInfo() {
      // 从sessionStorage同步用户信息到Vuex
      const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
      if (userInfo.id) {
        const displayName = userInfo.displayName || userInfo.uname;
        this.$store.dispatch('user/updateDisplayName', displayName);
        this.$store.dispatch('user/updateAvatar', userInfo.avatar || '');
      }
    }
  },
};
</script>

<style scoped>
html {
  overflow: hidden;
}

.el-container {
  /* overflow: hidden !important; */
  overflow: visible; /* 或者使用 scroll, visible */
}

.main-container {
  transition: margin-left 0.4s ease;
  width: 100%;
  display: flex;
  flex-direction: column;
  height: 100%; /* Ensure main container fills the height */
}

.sidebar-opened .main-container {
  margin-left: 270px; /* 侧边栏展开时的偏移量 */
}

.sidebar-closed .main-container {
  margin-left: calc(55px + 20px); /* 侧边栏收缩时的偏移量 */
}

.header {
  transition: height 0.4s ease;
}

.content {
  padding: 0px;
  flex: 1;
  overflow: hidden;
}

.full-width {
  margin-left: 0 !important;
}

.full-height {
  padding-top: 0 !important;
}

.full-height .content {
  padding: 0 !important;
}
</style>
