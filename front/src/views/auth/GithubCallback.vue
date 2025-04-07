<template>
  <div class="github-callback">
    <div v-if="loading" class="loading">
      <i class="el-icon-loading"></i>
      <p>处理 GitHub 登录中...</p>
    </div>
    <div v-if="error" class="error">
      <p>{{ error }}</p>
      <el-button type="primary" @click="$router.push('/login')">返回登录</el-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      loading: true,
      error: ''
    };
  },
  async mounted() {
    await this.handleGithubCallback();
  },
  methods: {
    async handleGithubCallback() {
      try {
        // 提取 URL 参数
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');
        const state = urlParams.get('state');

        if (!code || !state) {
          throw new Error('GitHub 回调参数缺失，请重新登录！');
        }

        // 验证 state
        const savedState = localStorage.getItem('github_oauth_state');
        if (!savedState || savedState !== state) {
          localStorage.removeItem('github_oauth_state'); // 清理无效的 state
          throw new Error('State 验证失败，请重新登录！');
        }
        localStorage.removeItem('github_oauth_state');

        // 请求后端处理回调
        const response = await axios.get('/api/auth/github/callback', {
          params: { code, state },
        });

        console.log('后端返回的数据:', response.data);

        // 检查 response.data.data 是否为空
        const data = response.data.data;
        if (!data) {
          throw new Error(response.data.msg || '后端返回数据为空，请联系管理员！');
        }

        const { token, user } = data;

        if (!token || !user) {
          throw new Error('后端返回的 token 或用户信息不完整，请联系管理员！');
        }

        // 先清理旧的用户信息
        localStorage.clear();
        sessionStorage.clear();
        
        // 保存新的用户信息
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
        sessionStorage.setItem('userInfo', JSON.stringify(user));
        
        // 更新 Vuex store
        this.$store.dispatch('user/updateDisplayName', user.displayName);
        this.$store.dispatch('user/updateAvatar', user.avatar);

        console.log('GitHub 登录成功，用户信息:', user);

        this.$message.success('GitHub 登录成功！');
        this.$router.push('/home');
      } catch (error) {
        console.error('GitHub 登录处理失败:', error.message || error);
        this.error = error.message || 'GitHub 登录失败，请稍后重试！';
      } finally {
        this.loading = false;
      }
    }

  }
};
</script>



<style scoped>
.github-callback {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  flex-direction: column;
}

.loading {
  text-align: center;
}

.loading i {
  font-size: 24px;
  margin-bottom: 10px;
}

.error {
  color: red;
  text-align: center;
  margin: 20px;
}

.error button {
  margin-top: 10px;
}
</style> 