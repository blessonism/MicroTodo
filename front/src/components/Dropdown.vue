<template>
  <div
    class="dropdown"
    :class="{ 'show-dropdown': showDropdown }"
    id="dropdown-content"
    v-click-outside="hideDropdown"
  >
    <!-- 添加了 type="button" 属性，明确指定这是一个按钮类型，防止它被当作表单提交按钮处理
    新增了 handleClick 方法来处理点击事件，替代直接调用 toggleDropdown -->
    <button    
      class="dropdown__button"      
      @click="handleClick"  
      id="dropdown-button"
      type="button"
    >
      <i :class="[selectedIcon, 'dropdown__icon']"></i>
      <span class="dropdown__name">{{ selectedName }}</span>

      <div class="dropdown__icons">
        <i class="ri-arrow-down-s-line dropdown__arrow"></i>
        <i class="ri-close-line dropdown__close"></i>
      </div>
    </button>

    <ul class="dropdown__menu">
      <li
        class="dropdown__item"
        @click="selectItem('Ai model', 'ri-user-line')"
      >
        <i class="ri-user-line dropdown__icon"></i>
        <span class="dropdown__name">Ai model</span>
      </li>
      <li
        class="dropdown__item"
        @click="selectItem('Students', 'ri-message-3-line')"
      >
        <i class="ri-message-3-line dropdown__icon"></i>
        <span class="dropdown__name">Students</span>
      </li>

      <li
        class="dropdown__item"
        @click="selectItem('Engineer', 'ri-settings-3-line')"
      >
        <i class="ri-settings-3-line dropdown__icon"></i>
        <span class="dropdown__name">Engineer</span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  directives: {
    'click-outside': {
      bind(el, binding) {
        el.clickOutsideEvent = function(event) {
          // 如果点击的是元素本身或其子元素，不处理
          if (el.contains(event.target)) {
            return;
          }
          // 调用绑定的方法
          binding.value(event);
        };
        document.addEventListener('click', el.clickOutsideEvent);
      },
      unbind(el) {
        document.removeEventListener('click', el.clickOutsideEvent);
      }
    }
  },

  data() {
    return {
      showDropdown: false,
      selectedName: "Default Role",
      selectedIcon: "ri-user-3-line",
    };
  },

  methods: {
    handleClick(event) {
      // 如果是通过 enter 键触发的 click 事件，则不处理
      if (event.detail === 0) {
        return;
      }
      this.toggleDropdown();
    },

    toggleDropdown() {      
      this.showDropdown = !this.showDropdown;
    },

    hideDropdown() {
      this.showDropdown = false;
    },

    selectItem(name, icon) {
      this.selectedName = name;
      this.selectedIcon = icon;
      this.showDropdown = false;
    },
  },

  mounted() {
    document.getElementById("dropdown-button").blur();
  },

  beforeDestroy() {
    // 确保在组件销毁时清理事件监听器
    if (this.$el.clickOutsideEvent) {
      document.removeEventListener('click', this.$el.clickOutsideEvent);
    }
  }
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500&display=swap");

:root {
  --first-color: hsl(225, 72%, 55%);
  --text-color: hsl(225, 52%, 30%);
  --body-color: linear-gradient(180deg, hsl(225, 75%, 92%), hsl(225, 78%, 70%));
  --container-color: hsl(225, 75%, 97%);
  --body-font: "Montserrat", sans-serif;
  --normal-font-size: 1rem;
}

.dropdown {
  position: relative;
  width: max-content;
  transform: translateY(-0.7rem);
  z-index: 2; /* 设置基础的z-index */
}

.dropdown__button,
.dropdown__item {
  display: flex;
  align-items: center;
  column-gap: 0.5rem;
}

.dropdown__button {
  width: 150px; /* 固定宽度 */
  border: none;
  outline: none;
  background-color: var(--container-color);
  padding: 0.95rem;
  border-radius: 0.25rem;
  cursor: pointer;
  z-index: 10;
  white-space: nowrap; /* 禁止文本换行 */
  overflow: hidden; /* 隐藏溢出文本 */
  text-overflow: ellipsis; /* 使用省略号表示溢出文本 */
}

/* 添加新的样式类，用于确保下拉框图标切换时位置不变，动画平滑 */
.dropdown__icon,
.dropdown__icons {
  width: 1.5rem; /* 固定宽度确保水平位置不变 */
  font-size: 1.25rem;
  color: var(--first-color);
  transition: transform 1s ease, opacity 1s ease; /* 平滑切换动画 */
  transform: translateY(0); /* 防止位置变化 */
}

.dropdown__name {
  font-weight: 500;
  font-size: small;
}

.dropdown__icons {
  width: 24px;
  height: 24px;
  display: grid;
  place-items: center;
}

.dropdown__arrow,
.dropdown__close {
  position: absolute;
  transition: opacity 0.1s, transform 0.4s;
}

.dropdown__close {
  opacity: 0;
}

.dropdown__menu {
  background-color: var(--container-color);
  padding: 1rem 1.25rem;
  border-radius: 0.75rem;
  display: grid;
  row-gap: 1.5rem;
  position: absolute;
  width: 100%;
  left: 0;
  top: 3.5rem;
  transform: scale(0.1);
  transform-origin: 10rem -2rem;
  pointer-events: none;
  transition: opacity 0.4s, transform 0.4s;
  opacity: 0;
  z-index: 1000;
  transform: scale(0.95); /* 初始缩小一点 */
  transition: all 0.4s ease; /* 平滑切换所有属性 */
}

.dropdown__item {
  cursor: pointer;
  transition: color 0.3s;
}

.dropdown__item:hover {
  color: var(--first-color);
}

.show-dropdown .dropdown__close {
  opacity: 1;
  transform: rotate(-180deg);
}

.show-dropdown .dropdown__arrow {
  opacity: 0;
  transform: rotate(-180deg);
}

.show-dropdown .dropdown__menu {
  opacity: 1;
  transform: scale(1);
  pointer-events: initial;
  z-index: 10;
}
</style>
