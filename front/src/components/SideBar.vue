<template>
  <nav
    :class="['sidebar', sidebarClass]"
    @mouseleave="hideSidebar"
    @mouseenter="showSidebar"
  >
    <div class="logo_items flex">
      <span class="nav_image">
        <img src="../assets/todo_list.png" alt="logo_img" />
      </span>
      <span class="logo_name"
        ><span class="text-grey">Micro</span>
        <span class="text-blue">todo</span></span
      >
      <i :class="lockIconClass" @click="toggleLock" title="Unlock Sidebar"></i>
      <i class="bx bx-x" id="sidebar-close" @click="toggleSidebar"></i>
    </div>

    <div class="menu_container">
      <div class="menu_items">
        <!-- Dashboard Menu -->
        <ul class="menu_item">
          <div class="menu_title flex">
            <span class="title">{{ $t('sidebar.menu.dashboard.title') }}</span>
            <span class="line"></span>
          </div>
          <li class="item">
            <a href="/home" class="link flex">
              <i class="bx bx-home-alt"></i>
              <span>{{ $t('sidebar.menu.dashboard.overview') }}</span>
            </a>
          </li>
          <li class="item">
            <a href="/about" class="link flex">
              <i class="bx bx-grid-alt"></i>
              <span>{{ $t('sidebar.menu.dashboard.about') }}</span>
            </a>
          </li>
        </ul>
        <!-- Editor Menu -->
        <ul class="menu_item">
          <div class="menu_title flex">
            <span class="title">{{ $t('sidebar.menu.editor.title') }}</span>
            <span class="line"></span>
          </div>
          <li class="item">
            <a href="#" class="link flex">
              <i class="bx bxs-magic-wand"></i>
              <span>{{ $t('sidebar.menu.editor.magicBuild') }}</span>
            </a>
          </li>
          <li class="item">
            <a href="#" class="link flex">
              <i class="bx bx-folder"></i>
              <span>{{ $t('sidebar.menu.editor.newProjects') }}</span>
            </a>
          </li>
          <li class="item">
            <a
              @click="
                handleEditCommand({
                  command: updateActions.upload,
                  node: selectedNode,
                })
              "
              class="link flex"
            >
              <i class="bx bx-cloud-upload"></i>
              <span>{{ $t('sidebar.menu.editor.uploadNew') }}</span>
            </a>
          </li>
        </ul>
        <!-- Setting Menu -->
        <ul class="menu_item">
          <div class="menu_title flex">
            <span class="title">{{ $t('sidebar.menu.setting.title') }}</span>
            <span class="line"></span>
          </div>
          <li class="item">
            <a href="/FAQ" class="link flex">
              <i class="bx bx-flag"></i>
              <span>{{ $t('sidebar.menu.setting.questions') }}</span>
            </a>
          </li>
          <li class="item">
            <a @click="logout" class="link flex">
              <i class="bx bx-award"></i>
              <span>{{ $t('sidebar.menu.setting.logout') }}</span>
            </a>
          </li>
          <li class="item">
            <a href="/settings" class="link flex">
              <i class="bx bx-cog"></i>
              <span>{{ $t('sidebar.menu.setting.settings') }}</span>
            </a>
          </li>
        </ul>
      </div>

      <div class="sidebar_profile flex">
        <a href="/home#/about">
          <span class="nav_image">
            <img src="../assets/todo_list.png" alt="logo_img" />
          </span>
        </a>
        <div class="data_text">
          <span class="name">Suki </span>
          <span class="email"></span>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
import { mapState } from "vuex"; // Ensure mapState is imported

export default {
  data() {
    return {
      sidebarLocked: false,
      sidebarClosed: true,
      windowWidth: window.innerWidth,
      selectedNode: null, // Add this line to keep track of the selected node
    };
  },
  computed: {
    sidebarClass() {
      return {
        locked: this.sidebarLocked,
        close: this.sidebarClosed,
      };
    },
    lockIconClass() {
      return {
        "bx bx-lock-alt": this.sidebarLocked,
        "bx bx-lock-open-alt": !this.sidebarLocked,
      };
    },
    ...mapState("tasks", ["tasks", "updateActions"]), // Map updateActions from Vuex
  },
  methods: {
    logout() {
      // 清理所有存储
      localStorage.clear();
      sessionStorage.clear();
      
      // 重置 Vuex store
      this.$store.dispatch("user/updateDisplayName", "");
      this.$store.dispatch("user/updateAvatar", "");
      
      this.$message.success("已退出登录");
      // 跳转到登录页
      this.$router.push("/login");
    },
    toggleLock() {
      this.sidebarLocked = !this.sidebarLocked;
      if (this.sidebarLocked) {
        this.sidebarClosed = false;
      } else if (this.windowWidth >= 800) {
        this.sidebarClosed = true;
      }
      this.$emit("toggle", this.sidebarClosed);
    },
    toggleSidebar() {
      this.sidebarClosed = !this.sidebarClosed;
      this.$emit("toggle", this.sidebarClosed);
    },
    hideSidebar() {
      if (!this.sidebarLocked && this.windowWidth >= 800) {
        this.sidebarClosed = true;
        this.$emit("toggle", this.sidebarClosed);
      }
    },
    showSidebar() {
      if (!this.sidebarLocked && this.windowWidth >= 800) {
        this.sidebarClosed = false;
        this.$emit("toggle", this.sidebarClosed);
      }
    },
    editDialog(command, node) {
      if (!node.checked) {
        this.$message({
          type: "warning",
          message: "Please select the task to edit or split.",
        });
        return;
      }
      this.dialogPayload.action = command;
      this.dialogPayload.editingNodeId = node.data.id;
      this.dialogPayload.editingNodeContent =
        command === this.updateActions.edit ? node.label : "";
      this.dialogVisible = true;
    },
    async handleEditCommand({ command, node }) {
      if (!node) {
        this.$message({
          type: "warning",
          message: "Please select a task first.",
        });
        return;
      }

      switch (command) {
        case this.updateActions.addSub:
          this.editDialog(this.updateActions.addSub, node);
          break;
        case this.updateActions.remove:
          this.removeWarning(node.data.id);
          break;
        case this.updateActions.split:
          node.loading = true;
          await this.splitTask(node.data.id);
          node.loading = false;
          break;
        case this.updateActions.upload:
          // Logic for uploading new tasks
          this.$message({
            type: "success",
            message: "Upload new task logic here.",
          });
          break;
        default:
          break;
      }
    },
    selectNode(node) {
      this.selectedNode = node;
    },
  },
  mounted() {
    window.addEventListener("resize", () => {
      this.windowWidth = window.innerWidth;
      if (this.windowWidth < 800) {
        this.sidebarClosed = true;
      } else if (!this.sidebarLocked) {
        this.sidebarClosed = false;
      }
      this.$emit("toggle", this.sidebarClosed);
    });
  },
};
</script>

<style model>
@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@200;300;400;500;600;700&display=swap");
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Poppins", sans-serif;
}
/* body {
  min-height: 100vh;
  background: #eef5fe;
} */
.flex {
  display: flex;
  align-items: center;
}
.nav_image {
  display: flex;
  min-width: 55px;
  justify-content: center;
}
.nav_image img {
  height: 35px;
  width: 35px;
  border-radius: 50%;
  object-fit: cover;
}

.text-grey {
  color: #333;
}

.text-blue {
  color: #4169eb;
}
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 245px;
  background: #fff;
  padding: 15px 10px;
  box-shadow: 0 0 2px rgba(0, 0, 0, 0.1);
  transition: all 0.4s ease;
  z-index: 1000;
}
.sidebar.close {
  width: calc(55px + 20px);
}
.logo_items {
  gap: 8px;
}
.logo_name {
  font-size: 22px;
  color: #333;
  font-weight: 500;
  transition: all 0.3s ease;
}
.sidebar.close .bx-x,
.sidebar.close .bx-lock-alt,
.sidebar.close .bx-lock-open-alt,
.sidebar.close .logo_name {
  opacity: 0;
  pointer-events: none;
}

.bx-lock-alt,
.bx-x {
  padding: 13px;
  color: #020202;
  font-size: 18px;
  cursor: pointer;
  margin-left: -4px;
  transition: all 0.3s ease;
}
.bx-x {
  display: none;
  color: #333;
}
.sidebar.close .bx-x {
  display: block;
}
.menu_container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-top: 40px;
  overflow-y: auto;
  height: calc(100% - 82px);
}
.menu_container::-webkit-scrollbar {
  display: none;
}
.menu_title {
  position: relative;
  height: 50px;
  width: 55px;
}
.menu_title .title {
  margin-left: 15px;
  transition: all 0.3s ease;
}
.sidebar.close .title {
  opacity: 0;
}
.menu_title .line {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  height: 3px;
  width: 20px;
  border-radius: 25px;
  background: #aaa;
  transition: all 0.3s ease;
}
.menu_title .line {
  opacity: 0;
}
.sidebar.close .line {
  opacity: 1;
}
.item {
  list-style: none;
}
.link {
  text-decoration: none;
  border-radius: 8px;
  margin-bottom: 8px;
  color: #707070;
}
.link:hover {
  color: #fff;
  background-color: #4070f4;
}
.link span {
  white-space: nowrap;
}
.link i {
  height: 50px;
  min-width: 55px;
  display: flex;
  font-size: 22px;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
}
.sidebar_profile {
  padding-top: 15px;
  margin-top: 15px;
  gap: 15px;
  border-top: 2px solid rgba(0, 0, 0, 0.1);
}
.sidebar_profile .name {
  font-size: 18px;
  color: #333;
}
.sidebar_profile .email {
  font-size: 15px;
  color: #333;
}
.navbar {
  max-width: 100%;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  background: #fff;
  padding: 10px 20px;
  border-radius: 0 0 8px 8px;
  justify-content: space-between;
  z-index: 999;
}
#sidebar-open {
  font-size: 30px;
  color: #333;
  cursor: pointer;
  margin-right: 20px;
  display: none;
}
.search_box {
  height: 46px;
  max-width: 500px;
  width: 100%;
  border: 1px solid #aaa;
  outline: none;
  border-radius: 8px;
  padding: 0 15px;
  font-size: 18px;
  color: #333;
}
.navbar img {
  height: 40px;
  width: 40px;
  margin-left: 20px;
}
@media screen and (max-width: 1100px) {
  .navbar {
    left: 0;
    max-width: 100%;
  }
}
@media screen and (max-width: 800px) {
  .sidebar {
    left: 0;
    z-index: 1000;
  }
  .sidebar.close {
    left: -100%;
    font-size: font-weight;
  }
  .bx-x {
    display: block;
  }
  .bx-lock-alt {
    display: none;
  }
  .navbar {
    left: 0;
    max-width: 100%;
    transform: translateX(0%);
  }
  #sidebar-open {
    display: block;
  }
}
</style>
