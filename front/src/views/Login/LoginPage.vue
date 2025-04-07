<template>
  <div class="box">
    <div
      class="content"
      :class="{ 'add-class-content': !isLogin }"
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
    >
      <img
        class="login-img images"
        :class="{
          'add-class-login-img': !isLogin,

          'add-class-login-img-show': isLogin,
        }"
        src="../../assets/login.jpg"
        alt="登录"
      />

      <img
        class="register-img images"
        :class="{ 'add-class-register-img': !isLogin }"
        src="../../assets/register.jpg"
        alt="注册"
      />

      <div class="login-wrapper" :style="wrapperStyle">
        <div class="top-tips">
          <span>Micro Todo</span>

          <span class="top-tips-span" @click="toggleLoginRegister" style>{{
            isLogin ? "Sign up" : "Sign in"
          }}</span>
        </div>

        <h1 class="h1-text">{{ isLogin ? "Sign in" : "Sign up" }}</h1>

        <div class="login-form" v-show="isLogin">
          <div class="user-form form-item">
            <div class="text-tips">
              <span>Account</span>
            </div>

            <input
              type="text"
              label="用户名/邮箱"
              prop="uname"
              v-model="ruleForm.uname"
              placeholder="请输入用户名或邮箱"
            />
          </div>

          <div class="password-form form-item">
            <div class="text-tips">
              <span>Password</span>

              <span class="forgot-password">Forget Password?</span>
            </div>

            <input
              type="password"
              v-model="ruleForm.password"
              placeholder="请输入5-16位密码"
              autocomplete="off"
            />
          </div>

          <div class="button-group">
            <button
              class="btn signin-btn"
              type="primary"
              @click="submitLoginForm('ruleForm')"
              v-loading="loading"
            >
              Sign in
            </button>

            <button
              class="btn github-btn"
              @click="handleGithubLogin"
              v-loading="loading"
            >
              <svg
                height="20"
                viewBox="0 0 16 16"
                width="20"
                class="github-icon"
              >
                <path
                  fill="currentColor"
                  d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"
                ></path>
              </svg>

              GitHub
            </button>
          </div>
        </div>

        <div class="register-form" v-show="!isLogin">
          <div class="user-form form-item">
            <div class="text-tips">
              <span>Username</span>
            </div>

            <input
              type="text"
              v-model="ruleForm.uname"
              placeholder="请输入2-10位用户名"
            />
          </div>

          <div class="user-form form-item">
            <div class="text-tips">
              <span>Email</span>
            </div>

            <input
              type="email"
              v-model="ruleForm.email"
              placeholder="请输入邮箱地址"
            />
          </div>

          <div class="password-form form-item">
            <div class="text-tips">
              <span>Password</span>
            </div>

            <input
              type="password"
              v-model="ruleForm.password"
              placeholder="请输入5-16位密码"
              autocomplete="off"
            />
          </div>

          <div class="password-form form-item">
            <div class="text-tips">
              <span>Confirm Password</span>
            </div>

            <input
              type="password"
              v-model="ruleForm.confirmPassword"
              placeholder="请再次输入密码"
              autocomplete="off"
            />
          </div>

          <div class="button-group">
            <button
              class="btn signup-btn"
              type="primary"
              @click="submitRegisterForm('ruleForm')"
              v-loading="loading"
              style="margin: 0px"
            >
              Sign up
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { Code } from "@/utils/Code";

import { Message } from "element-ui";

export default {
  data() {
    const validateEmail = (rule, value, callback) => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (value && !emailRegex.test(value)) {
        callback(new Error("请输入有效的邮箱地址"));
      } else {
        callback();
      }
    };

    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.ruleForm.password) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };

    return {
      isLogin: true,

      ruleForm: {
        uname: "",

        password: "",

        email: "",

        confirmPassword: "",
      },

      rules: {
        uname: [
          { required: true, message: "用户名不能为空！", trigger: "blur" },

          {
            min: 2,

            max: 10,

            message: "用户名长度必须在2到10个字符之间",

            trigger: "blur",
          },
        ],

        password: [
          { required: true, message: "密码不能为空！", trigger: "blur" },

          {
            min: 5,

            max: 16,

            message: "密码长度必须在5到16个字符之间",

            trigger: "blur",
          },
        ],

        email: [
          {
            required: !this.isLogin,
            message: "注册时邮箱不能为空！",
            trigger: "blur",
          },

          { validator: validateEmail, trigger: "blur" },
        ],

        confirmPassword: [
          { required: !this.isLogin, message: "请确认密码！", trigger: "blur" },

          { validator: validateConfirmPassword, trigger: "blur" },
        ],
      },

      loading: false,
    };
  },

  computed: {
    wrapperStyle() {
      return {
        height: this.isLogin ? "70vh" : "80vh",
      };
    },
  },

  methods: {
    toggleLoginRegister() {
      this.isLogin = !this.isLogin;

      this.resetForm();
    },

    resetForm() {
      this.ruleForm = {
        uname: "",

        password: "",

        email: "",

        confirmPassword: "",
      };

      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.resetFields();
      }
    },

    submitLoginForm() {
      const form = this.$refs.ruleForm;

      if (form) {
        const isValid = this.customValidate(form);

        if (isValid) {
          this.handleLogin();
        } else {
          this.$message.error("表单验证失败");
        }
      }
    },

    customValidate(form) {
      let isValid = true;

      if (!this.ruleForm.uname) {
        isValid = false;
      }

      if (
        !this.ruleForm.password ||
        this.ruleForm.password.length < 5 ||
        this.ruleForm.password.length > 16
      ) {
        isValid = false;
      }

      if (!this.isLogin) {
        if (
          !this.ruleForm.email ||
          !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.ruleForm.email)
        ) {
          isValid = false;
        }

        if (this.ruleForm.password !== this.ruleForm.confirmPassword) {
          isValid = false;
        }
      }

      return isValid;
    },

    handleLogin() {
      this.loading = true;
      const loginData = {
        uname: this.ruleForm.uname,
        password: this.ruleForm.password,
      };

      axios
        .post("/api/auth/login", loginData)
        .then((response) => {
          if (response.data.code === Code.GET_OK) {
            const { token, user } = response.data.data;
            
            // 先清理旧的用户信息
            localStorage.clear();
            sessionStorage.clear();
            
            // 保存新的用户信息
            localStorage.setItem("token", token);
            localStorage.setItem("user", JSON.stringify(user));
            sessionStorage.setItem("userInfo", JSON.stringify(user));
            
            // 更新 Vuex store
            this.$store.dispatch("user/updateDisplayName", user.displayName);
            this.$store.dispatch("user/updateAvatar", user.avatar);
            
            this.$message.success("登录成功");
            this.$router.push("/home");
            console.log("登录成功，用户信息：", user);
          } else {
            this.$message.error(response.data.msg || "登录失败");
          }
        })
        .catch((error) => {
          console.error("Login error:", error);
          this.$message.error(error.response?.data?.msg || "登录失败，请稍后重试");
        })
        .finally(() => {
          this.loading = false;
        });
    },

    submitRegisterForm() {
      const form = this.$refs.ruleForm;
      if (form) {
        const isValid = this.customValidate(form);
        if (isValid) {
          this.loading = true;
          const formData = {
            username: this.ruleForm.uname,
            password: this.ruleForm.password,
            email: this.ruleForm.email,
            displayName: this.ruleForm.uname
          };

          axios.post("/api/auth/register", formData)
            .then((response) => {
              if (response.data.code === Code.SAVE_OK) {
                this.$message.success(response.data.msg);
                this.isLogin = true;
                this.resetForm();
              } else {
                this.$message.warning(response.data.msg);
              }
            })
            .catch((error) => {
              this.$message.error(error.response?.data?.msg || error.message);
            })
            .finally(() => {
              this.loading = false;
            });
        } else {
          this.$message.error("表单验证失败");
        }
      }
    },

    async handleGithubLogin() {
       // 生成随机 state
       const state = Math.random().toString(36).substring(2, 15);
      console.log('Generated state:', state);

      // 保存 state 到 localStorage
      localStorage.setItem('github_oauth_state', state);

      try {
        // 可选：同步 state 到后端
        await axios.post('/api/auth/github/state', { state });
        console.log('State successfully synced to backend.');
      } catch (error) {
        console.error('Failed to sync state with backend:', error);
        this.$message.error('无法发起 GitHub 登录，请稍后再试');
        return;
      }

      // 构造 GitHub 授权 URL
      const clientId = 'Ov23ctCzxfKPmtdBWEnn'; // 替换为实际的 GitHub 应用 Client ID
      const redirectUri = encodeURIComponent('http://localhost:8080/auth/github/callback');
      const scope = encodeURIComponent('read:user user:email');
      const githubAuthUrl = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&state=${state}&scope=${scope}`;

      console.log('Redirecting to GitHub OAuth URL:', githubAuthUrl);

      // 跳转到 GitHub 授权页面
      window.location.href = githubAuthUrl;
    },

    handleGithubCallback(code, state) {
      console.log('Processing GitHub callback...');
      console.log('Code:', code);
      console.log('State:', state);
      
      // 验证state
      const savedState = localStorage.getItem('github_oauth_state');
      console.log('Saved state:', savedState);
      
      if (!state || !savedState || state !== savedState) {
        console.error('State validation failed');
        this.$message.error('State验证失败，可能存在安全风险');
        return;
      }
      
      this.loading = true;
      // 清除state
      localStorage.removeItem('github_oauth_state');
      
      // 发送到后端处理
      const callbackUrl = `/api/auth/github/callback?code=${code}&state=${state}`;
      console.log('Sending request to:', callbackUrl);
      
      axios.get(callbackUrl)
        .then(response => {
          console.log('Backend response:', response.data);
          if (response.data.code === 20011) {
            const { token, user } = response.data.data;
            console.log('Received user data:', user);
            
            // 保存登录信息
            localStorage.setItem('token', token);
            localStorage.setItem('user', JSON.stringify(user));
            sessionStorage.setItem('userInfo', JSON.stringify(user));
            
            // 设置axios默认header
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            
            // 显示成功消息
            this.$message.success(response.data.msg || 'GitHub登录成功！');
            
            // 重定向到主页
            this.$router.push('/home');
          } else {
            throw new Error(response.data.msg || '登录失败');
          }
        })
        .catch(error => {
          console.error('GitHub callback error:', error);
          this.$message.error(error.response?.data?.msg || error.message || 'GitHub登录处理失败');
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },

  created() {
    // 检查URL中是否包含GitHub回调的参数
    const code = this.$route.query.code;
    const state = this.$route.query.state;

    if (code && state) {
      this.handleGithubCallback(code, state);
    }
  },

  mounted() {
    // 设置 body 的样式

    document.body.style.overflow = "hidden";
  },

  beforeDestroy() {
    document.body.style.overflow = "auto";
  },
};
</script>

<style scoped>
html {
  overflow: hidden;
}

* {
  margin: 0;

  padding: 0;
}

.box {
  background-color: rgb(51, 32, 108);
}

.box .content {
  width: 90vw;

  height: 85vh;

  background-color: rgb(228, 218, 232);

  border-radius: 40px;

  position: absolute;

  left: 50%;

  top: 50%;

  transform: translate(-50%, -50%);

  display: flex;

  justify-content: center;

  align-items: center;

  overflow: hidden;

  transition: 0.7s;
}

.box .content .images {
  margin-left: -150px;

  margin-right: 100px;

  position: absolute;

  left: 15%;
}

.box .content .login-img {
  width: 45%;

  height: 90%;

  border-radius: 15%;
}

.box .content .register-img {
  width: 45%;

  opacity: 0;

  border-radius: 15%;
}

.box .content .login-wrapper {
  width: 30vw;

  height: 70vh;

  border-radius: 40px;

  -webkit-backdrop-filter: blur(1px);

  backdrop-filter: blur(1px);

  background: rgba(244, 240, 245, 0.6);

  padding: 60px;

  box-sizing: border-box;

  position: absolute;

  right: 10%;

  transition: 0.7s;

  overflow: hidden;
}

.box .content .login-wrapper .top-tips span:nth-child(1) {
  font-size: 25px;

  font-weight: 600;
}

.box .content .login-wrapper .top-tips .top-tips-span {
  font-size: 15px;

  color: rgb(79, 133, 226);

  border-bottom: 1px solid rgb(79, 133, 226);

  float: right;

  line-height: 25px;

  cursor: pointer;
}

.box .content .login-wrapper h1 {
  font-size: 45px;

  margin: 20px 0;
}

.box .content .login-wrapper .other-login {
  width: 60%;

  height: 50px;

  border-radius: 10px;

  display: flex;

  align-items: center;

  justify-content: center;

  margin: 40px auto;

  cursor: pointer;

  transition: background-color 0.3s;
}

.box .content .login-wrapper .other-login img {
  width: 25px;

  height: 25px;

  vertical-align: middle;
}

.box .content .login-wrapper .other-login span {
  vertical-align: middle;
}

.box .content .login-wrapper .login-form,
.box .content .login-wrapper .register-form {
  width: 100%;
}

.box .content .login-wrapper .login-form .form-item .text-tips,
.box .content .login-wrapper .register-form .form-item .text-tips {
  color: rgb(123, 122, 123);

  margin: 5px;
}

.box .content .login-wrapper .login-form .form-item .text-tips .forgot-password,
.box
  .content
  .login-wrapper
  .register-form
  .form-item
  .text-tips
  .forgot-password {
  font-size: 15px;

  color: rgb(79, 133, 226);

  border-bottom: 1px solid rgb(79, 133, 226);

  float: right;

  line-height: 25px;

  cursor: pointer;
}

.box .content .login-wrapper .login-form .form-item input,
.box .content .login-wrapper .register-form .form-item input {
  width: 100%;

  height: 50px;

  margin: 5px 0;

  border-radius: 5px;

  border: 0;

  font-size: 17px;

  padding: 0 20px;

  box-sizing: border-box;
}

.box .content .login-wrapper .login-form .form-item input:focus,
.box .content .login-wrapper .register-form .form-item input:focus {
  outline: none;

  border: 1px solid rgb(79, 133, 226);
}

.box .content .login-wrapper .login-form .btn,
.box .content .login-wrapper .register-form .btn {
  width: 150px;

  height: 50px;

  margin: 20px 0;

  background-color: rgb(59, 58, 59);

  border: 0;

  border-radius: 5px;

  color: #fff;

  font-size: 20px;

  cursor: pointer;
}

.add-class-content {
  background-color: rgb(156, 148, 208) !important;

  transition: 0.1s;
}

.add-class-login-img {
  -webkit-animation: disappear 0.3s forwards;

  animation: disappear 0.3s forwards;

  -webkit-animation-timing-function: ease;

  animation-timing-function: ease;
}

.add-class-login-img-show {
  -webkit-animation-delay: 1s;

  animation-delay: 1s;

  -webkit-animation: appear 1s forwards;

  animation: appear 1s forwards;

  -webkit-animation-timing-function: ease;

  animation-timing-function: ease;
}

.add-class-register-img {
  -webkit-animation: appear 1s forwards;

  animation: appear 1s forwards;

  -webkit-animation-timing-function: ease;

  animation-timing-function: ease;

  -webkit-animation-delay: 0.2s;

  animation-delay: 0.2s;
}

@-webkit-keyframes appear {
  from {
    border-radius: 15%;

    opacity: 0;
  }

  to {
    border-radius: 0%;

    opacity: 1;
  }
}

@keyframes appear {
  from {
    border-radius: 15%;

    opacity: 0;
  }

  to {
    border-radius: 0%;

    opacity: 1;
  }
}

@-webkit-keyframes disappear {
  from {
    opacity: 1;
  }

  to {
    opacity: 0;
  }
}

@keyframes disappear {
  from {
    opacity: 1;
  }

  to {
    opacity: 0;
  }
}

.social-login {
  display: flex;

  justify-content: center;

  gap: 20px;

  margin-bottom: 20px;
}

.other-login {
  display: flex;

  align-items: center;

  cursor: pointer;

  padding: 8px 15px;

  border-radius: 4px;

  transition: background-color 0.3s;
}

.other-login:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.github-login {
  background-color: #24292e;

  color: white;
}

.github-login:hover {
  background-color: #2f363d;
}

.github-login i {
  font-size: 20px;

  margin-right: 8px;
}

.other-login img {
  width: 20px;

  height: 20px;

  margin-right: 8px;
}

.other-login span {
  font-size: 14px;
}

.github-icon {
  margin-right: 10px;
}

.other-login span {
  font-size: 16px;
}

.button-group {
  display: flex;

  justify-content: space-between;

  gap: 20px;

  margin: 20px 0;
}

.btn {
  flex: 1;

  height: 50px;

  border: none;

  border-radius: 8px;

  font-size: 16px;

  font-weight: 500;

  cursor: pointer;

  display: flex;

  align-items: center;

  justify-content: center;

  transition: all 0.3s ease;

  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.signin-btn {
  background: linear-gradient(145deg, #3b3a3b, #4a494a);

  color: #fff;

  border: 1px solid rgba(255, 255, 255, 0.1);
}

.signin-btn:hover {
  background: linear-gradient(145deg, #4a494a, #5a595a);

  transform: translateY(-1px);

  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.github-btn {
  background: linear-gradient(145deg, #24292e, #2f363d);

  color: #fff;

  border: 1px solid rgba(255, 255, 255, 0.1);
}

.github-btn:hover {
  background: linear-gradient(145deg, #2f363d, #3a424a);

  transform: translateY(-1px);

  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.github-icon {
  margin-right: 8px;

  filter: drop-shadow(0 1px 1px rgba(0, 0, 0, 0.2));
}

.btn:active {
  transform: translateY(1px);

  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

/* Update signup button style to match */

.signup-btn {
  background: linear-gradient(145deg, #3b3a3b, #4a494a);

  color: #fff;

  border: 1px solid rgba(255, 255, 255, 0.1);

  width: 100%;
}

.signup-btn:hover {
  background: linear-gradient(145deg, #4a494a, #5a595a);

  transform: translateY(-1px);

  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
