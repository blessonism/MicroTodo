export default {
  sidebar: {
    menu: {
      dashboard: {
        // title: '仪表盘',
        overview: '主页',
        about: '关于'
      },
      editor: {
        // title: '编辑器',
        magicBuild: '智能构建',
        newProjects: '新建项目',
        uploadNew: '上传新任务'
      },
      setting: {
        // title: '设置',
        questions: '常见问题',
        logout: '退出登录',
        settings: '设置'
      }
    }
  },
  settings: {
    title: '设置',
    ai: {
      title: 'AI 配置',
      model: 'AI 模型',
      apiSettings: 'API 设置',
      apiKey: 'OpenAI API 密钥',
      advancedSettings: '高级 API 设置',
      endpoint: '自定义 API 端点',
      proxy: '代理设置',
      modelOptions: {
        basic: '基础版',
        recommended: '推荐',
        advanced: '高级版'
      }
    },
    user: {
      title: '用户信息',
      avatar: '头像',
      changeAvatar: '更改',
      displayName: '显示名称',
      preferences: '偏好设置',
      language: '语言',
      notifications: {
        title: '通知设置',
        email: '邮件通知',
        desktop: '桌面通知',
        sound: '声音通知'
      },
      avatarUpload: {
        success: '太棒了！新头像已经设置好啦~ 🎉',
        error: '啊哦，头像更新失败了，要不要再试一次？',
        sizeError: '图片有点大哦，请保持在2MB以内~',
        typeError: '请选择图片文件哦（比如JPG、PNG）',
        uploading: '正在上传你的新头像...'
      }
    },
    taskSplit: {
      title: '任务拆分配置',
      granularity: {
        title: '拆分粒度',
        fine: '精细 (1-2小时/任务)',
        standard: '标准 (2-4小时/任务)',
        coarse: '粗略 (4-8小时/任务)'
      },
      rules: {
        title: '拆分规则',
        keepStructure: '保持原有任务结构',
        autoDescription: '自动生成子任务描述',
        timeEstimate: '包含时间估算'
      },
      smart: {
        title: '智能功能',
        priority: '启用优先级建议',
        time: '启用时间估算建议',
        dependency: '启用依赖关系分析'
      }
    },
    save: '保存设置',
    saveSuccess: '设置已保存'
  },
  faq: {
    title: '常见问题',
    questions: {
      features: {
        question: '这个网站有哪些主要功能？',
        answer: '本网站的主要功能包括创建、编辑和删除任务，设置任务优先级和截止日期，并通过集成的AI功能提供智能的任务和时间管理建议。'
      },
      createTask: {
        question: '如何创建新任务？',
        answer: '在网站主界面，点击"创建新任务"按钮，输入任务名称、描述、优先级和截止日期，然后点击保存按钮即可。'
      },
      loginIssue: {
        question: '为什么我无法登录账户？',
        answer: '请检查您的用户名和密码是否正确。如果仍然无法登录，请尝试重置密码或联系技术支持寻求帮助。'
      },
      saveIssue: {
        question: '为什么我的任务没有保存成功？',
        answer: '这可能是由于网络连接问题或服务器故障导致。请检查您的网络连接并重试保存任务。如果问题持续存在，请联系技术支持。'
      },
      privacy: {
        question: '如何确保用户数据隐私？',
        answer: '我们使用加密技术保护用户数据。所有用户数据都存储在安全的服务器上，只有授权人员才能访问这些数据。'
      },
      dataSharing: {
        question: '我的数据会被分享给第三方吗？',
        answer: '除非得到用户的明确同意或法律要求，否则我们不会与任何第三方分享用户数据。'
      }
    }
  }
} 