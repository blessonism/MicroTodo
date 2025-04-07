import Vue from 'vue'
import VueRouter from 'vue-router'

import HomePage from "@/views/home/HomePage.vue";
import PlanPage from "@/views/plan/PlanPage.vue";
import AboutPage from "@/views/about/AboutPage";
import UserPage from "@/views/user/UserPage.vue";
import LoginPage from "@/views/Login/LoginPage.vue";
import { Message } from "element-ui";
import FAQPage from "@/views/FAQ/FAQPage.vue";
import Settings from '@/views/settings/Settings.vue'
import GithubCallback from '@/views/auth/GithubCallback.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    component: LoginPage,
    meta: { hideNav: true, hideSidebar: true },
  },
  {
  //   path: "/auth/github",
  //   component: GithubCallback,
  //   meta: { hideNav: true, hideSidebar: true },
  // },
  // {
    path: "/auth/github/callback",
    component: GithubCallback,
    meta: { hideNav: true, hideSidebar: true },
  },
  {
    path: "/home",
    component: HomePage,
  },
  {
    path: "/plan",
    component: PlanPage,
  },
  {
    path: "/FAQ",
    component: FAQPage,
  },
  {
    path: "/user",
    component: UserPage,
  },
  {
    path: "/about",
    component: AboutPage,
  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings,
    meta: {
      requiresAuth: true
    }
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  console.log('=== 路由守卫开始 ===');
  console.log('从:', from.path);
  console.log('到:', to.path);
  
  // 检查认证状态
  const sessionAuth = !!sessionStorage.getItem("userInfo");
  const localAuth = !!(localStorage.getItem('token') && localStorage.getItem('user'));
  console.log('Session 认证状态:', sessionAuth);
  console.log('Local 认证状态:', localAuth);
  
  let isAuthenticated = sessionAuth || localAuth;
  console.log('最终认证状态:', isAuthenticated);
  
  // 允许访问的公开路径
  const publicPaths = ['/login', '/register', '/auth/github', '/auth/github/callback'];
  console.log('是否是公开路径:', publicPaths.includes(to.path));
  
  if (!publicPaths.includes(to.path) && !isAuthenticated) {
    console.log('未认证，重定向到登录页面');
    next({ path: "/login" });
    Message({
      message: "请先登录！",
      type: "warning",
    });
  } else {
    // 如果localStorage中有数据但sessionStorage中没有,则同步到sessionStorage
    if (!sessionStorage.getItem("userInfo") && localStorage.getItem('user')) {
      console.log('从 localStorage 同步数据到 sessionStorage');
      const userData = localStorage.getItem('user');
      console.log('用户数据:', userData);
      sessionStorage.setItem("userInfo", userData);
    }
    console.log('允许导航到:', to.path);
    next();
  }
  console.log('=== 路由守卫结束 ===\n');
});

export default router;
