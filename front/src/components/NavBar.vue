<template>
  <div>
    <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
    >
      <router-link to="/home">
        <el-menu-item index="1"> Home </el-menu-item>
      </router-link>
      <router-link to="/about">
        <el-menu-item index="2"> About </el-menu-item>
      </router-link>
      <router-link to="/FAQ">
        <el-menu-item index="3"> Q&A </el-menu-item>
      </router-link>
      <router-link to="/plan">
        <el-menu-item index="4"> Plan </el-menu-item>
      </router-link>

      <!-- 添加一个 spacer 来推动最后一个菜单项到右边 -->
      <div class="menu-spacer"></div>

      <el-menu-item index="5" class="avatar-item">
        <el-dropdown trigger="click">
          <div class="avatar-wrapper">
            <!-- <span class="user-name">{{ displayName }}</span> -->
            <el-avatar
              :size="32"
              :src="avatar || defaultAvatar"
              @error="() => true"
            ></el-avatar>
            <i class="el-icon-caret-bottom"></i>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <router-link to="/settings" class="dropdown-link">
                <i class="el-icon-setting"></i>
                <span>Settings</span>
              </router-link>
            </el-dropdown-item>
            <el-dropdown-item>
              <div class="dropdown-link" @click="toggleDarkMode">
                <i :class="isDarkMode ? 'el-icon-sunny' : 'el-icon-moon'"></i>
                <span>{{ isDarkMode ? 'Light Mode' : 'Dark Mode' }}</span>
              </div>
            </el-dropdown-item>
            <el-dropdown-item divided>
              <div class="dropdown-link" @click="handleLogout">
                <i class="el-icon-switch-button"></i>
                <span>Logout</span>
              </div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'NavBar',
  data() {
    return {
      activeIndex: "1",
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    };
  },
  computed: {
    ...mapState('user', ['displayName', 'avatar', 'isDarkMode'])
  },
  watch: {
    // 监听displayName的变化
    displayName: {
      handler(newVal) {
        console.log('NavBar: displayName changed to:', newVal);
      },
      immediate: true
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    handleLogout() {
      // 先清除 localStorage
      localStorage.removeItem('userDisplayName');
      localStorage.removeItem('userAvatar');
      // 再清除 sessionStorage
      sessionStorage.removeItem('userInfo');
      this.$store.dispatch('user/updateDisplayName', '');
      this.$store.dispatch('user/updateAvatar', '');
      this.$router.push('/login');
    },
    toggleDarkMode() {
      this.$store.dispatch('user/toggleDarkMode');
    },
    initUserInfo() {
      const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
      if (userInfo.displayName) {
        this.$store.dispatch('user/updateDisplayName', userInfo.displayName);
      }
      if (userInfo.avatar) {
        const avatarPath = userInfo.avatar;
        const fullAvatarUrl = avatarPath.startsWith('http') ? 
          avatarPath : 
          `/uploads/avatars/${avatarPath}`;
        console.log('NavBar: Setting avatar URL to:', fullAvatarUrl);
        this.$store.dispatch('user/updateAvatar', fullAvatarUrl);
      }
    }
  },
  created() {
    // 初始化时设置主题
    if (this.isDarkMode) {
      document.documentElement.classList.add('dark-mode')
    }
    // 初始化用户信息
    this.initUserInfo();
  }
};
</script>

<style scoped>
.el-menu-demo {
  display: flex;
  justify-content: space-between;
}

.menu-spacer {
  flex-grow: 1;
}

a,
.router-link-active {
  text-decoration: none;
}

.avatar-item {
  padding: 0 20px;
}

.avatar-wrapper {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.avatar-wrapper .el-avatar {
  margin-right: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-size: cover;
  background-position: center;
}

.dropdown-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: inherit;
  text-decoration: none;
  width: 100%;
  padding: 0 4px;
}

.el-dropdown-menu__item {
  padding: 0 16px;
}

.el-dropdown-menu__item i {
  font-size: 16px;
}

.user-name {
  margin-right: 10px;
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 120px;
}
</style>
