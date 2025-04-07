export default {
  sidebar: {
    menu: {
      dashboard: {
        title: 'Dashboard',
        overview: 'Overview',
        about: 'About'
      },
      editor: {
        title: 'Editor',
        magicBuild: 'Magic Build',
        newProjects: 'New Projects',
        uploadNew: 'Upload New'
      },
      setting: {
        title: 'Setting',
        questions: 'Questions',
        logout: 'Logout',
        settings: 'Settings'
      }
    }
  },
  settings: {
    title: 'Settings',
    ai: {
      title: 'AI Configuration',
      model: 'AI Model',
      apiSettings: 'API Settings',
      apiKey: 'OpenAI API Key',
      advancedSettings: 'Advanced API Settings',
      endpoint: 'Custom API Endpoint',
      proxy: 'Proxy Settings',
      modelOptions: {
        basic: 'Basic',
        recommended: 'Recommended',
        advanced: 'Advanced'
      }
    },
    user: {
      title: 'User Profile',
      avatar: 'Avatar',
      changeAvatar: 'Change',
      displayName: 'Display Name',
      preferences: 'Preferences',
      language: 'Language',
      notifications: {
        title: 'Notification Settings',
        email: 'Email Notifications',
        desktop: 'Desktop Notifications',
        sound: 'Sound Notifications'
      },
      avatarUpload: {
        success: 'Your new profile picture looks great! 🎉',
        error: 'Oops! Failed to update your profile picture. Please try again.',
        sizeError: 'The image is too large! Please keep it under 2MB.',
        typeError: 'Please choose an image file (like JPG, PNG).',
        uploading: 'Uploading your new profile picture...'
      }
    },
    taskSplit: {
      title: 'Task Split Configuration',
      granularity: {
        title: 'Split Granularity',
        fine: 'Fine (1-2h/task)',
        standard: 'Standard (2-4h/task)',
        coarse: 'Coarse (4-8h/task)'
      },
      rules: {
        title: 'Split Rules',
        keepStructure: 'Keep original task structure',
        autoDescription: 'Auto-generate subtask descriptions',
        timeEstimate: 'Include time estimates'
      },
      smart: {
        title: 'Smart Features',
        priority: 'Enable priority suggestions',
        time: 'Enable time estimation suggestions',
        dependency: 'Enable dependency analysis'
      }
    },
    save: 'Save Settings',
    saveSuccess: 'Settings saved successfully'
  },
  faq: {
    title: 'Frequently Asked Questions',
    questions: {
      features: {
        question: 'What are the main features of this website?',
        answer: 'The main features of the website include creating, editing, and deleting tasks, setting task priorities and deadlines, and providing intelligent suggestions for task and time management through integrated AI functionality.'
      },
      createTask: {
        question: 'How do I create a new task?',
        answer: 'On the main interface of the website, click the Create New Task button, enter the task name, description, priority, and deadline, then click the Save button.'
      },
      loginIssue: {
        question: 'Why can\'t I log into my account?',
        answer: 'Please check if your username and password are correct. If you still can\'t log in, try resetting your password or contact technical support for help.'
      },
      saveIssue: {
        question: 'Why wasn\'t my task saved?',
        answer: 'It may be due to network connection issues or server failures. Check your network connection and try saving the task again. If the problem persists, contact technical support.'
      },
      privacy: {
        question: 'How do we ensure user data privacy?',
        answer: 'We use encryption technology to protect user data. All user data is stored on secure servers and only authorized personnel can access this data.'
      },
      dataSharing: {
        question: 'Will my data be shared with third parties?',
        answer: 'We do not share user data with any third parties unless we have explicit consent from the user or are required by law.'
      }
    }
  }
} 