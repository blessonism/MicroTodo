import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from "axios";
import VueAxios from "vue-axios";
import VueRouter from "vue-router";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import "remixicon/fonts/remixicon.css";
import "@fortawesome/fontawesome-free/css/all.css";
import i18n from "./i18n";

Vue.use(ElementUI);

Vue.config.productionTip = false;

Vue.use(VueAxios, axios); // 使用 axios 插件
Vue.use(VueRouter); // 路由插件

// 初始化 store 状态
store.dispatch('user/initializeState');

window.addEventListener("error", (event) => {
  console.error("Global error handler:", event.error);
  if (event.error instanceof URIError) {
    alert("A malformed URI was detected.");
  }
});

new Vue({
  router,
  store,
  i18n,
  render: (h) => h(App),
}).$mount("#app");
