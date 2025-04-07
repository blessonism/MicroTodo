module.exports = {
  lintOnSave: false, // 关闭语法检测
  // 开启代理服务器
  devServer: {
    // 代理服务器可以将路由中的指定前缀转发到指定的后端服务器中
    proxy: {
      '/api': {
        target: 'http://localhost:8088',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '' // 移除 /api 前缀
        }
      },      
      '/uploads/avatars': {  // 添加对头像路径的代理
        target: 'http://localhost:8088',
        changeOrigin: true
      },
      '/oauth2': {  // 添加这个配置
        target: 'http://localhost:8088',
        changeOrigin: true
      },
      '/api/auth/github/callback': {
        target: 'http://localhost:8088',
        changeOrigin: true
      },
    }
  }
};
