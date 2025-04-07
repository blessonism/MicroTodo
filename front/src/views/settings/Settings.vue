<template>
  <div class="settings-container">
    <h2 class="settings-title">{{ $t('settings.title') }}</h2>
    <div class="settings-grid">
      <!-- AI Configuration Card -->
      <el-card class="settings-card" shadow="hover">
        <div slot="header" class="card-header">
          <i class="el-icon-cpu"></i>
          <span>{{ $t('settings.ai.title') }}</span>
        </div>
        <el-form label-position="top" :model="aiSettings" class="settings-form">
          <el-form-item :label="$t('settings.ai.model')" class="compact-form-item">
            <el-select v-model="aiSettings.model" :placeholder="$t('settings.ai.model')">
              <el-option label="GPT-4o-mini" value="gpt-4o-mini">
                <span style="float: left">GPT-4o-mini</span>
                <span style="float: right; color: #8492a6; font-size: 12px">{{ $t('settings.ai.modelOptions.basic') }}</span>
              </el-option>
              <el-option label="GPT-4o" value="gpt-4o">
                <span style="float: left">GPT-4</span>
                <span style="float: right; color: #8492a6; font-size: 12px">{{ $t('settings.ai.modelOptions.recommended') }}</span>
              </el-option>
              <el-option label="Claude" value="claude">
                <span style="float: left">Claude</span>
                <span style="float: right; color: #8492a6; font-size: 12px">{{ $t('settings.ai.modelOptions.advanced') }}</span>
              </el-option>
            </el-select>
          </el-form-item>

          <el-divider content-position="left" class="compact-divider">{{ $t('settings.ai.apiSettings') }}</el-divider>

          <el-form-item :label="$t('settings.ai.apiKey')" class="compact-form-item">
            <el-input 
              v-model="aiSettings.openaiKey" 
              type="password" 
              show-password
              placeholder="Enter your OpenAI API key">
              <template slot="prepend">sk-</template>
            </el-input>
          </el-form-item>
          
          <el-collapse v-model="activeCollapse">
            <el-collapse-item :title="$t('settings.ai.advancedSettings')" name="advanced">
              <el-form-item :label="$t('settings.ai.endpoint')" class="compact-form-item">
                <el-input 
                  v-model="aiSettings.apiEndpoint" 
                  placeholder="Enter custom API endpoint (optional)">
                  <i slot="prefix" class="el-icon-link"></i>
                </el-input>
              </el-form-item>

              <el-form-item :label="$t('settings.ai.proxy')" class="compact-form-item">
                <el-input 
                  v-model="aiSettings.proxy" 
                  placeholder="Enter proxy URL (optional)">
                  <i slot="prefix" class="el-icon-connection"></i>
                </el-input>
              </el-form-item>
            </el-collapse-item>
          </el-collapse>
        </el-form>
      </el-card>

      <!-- User Profile Settings Card -->
      <el-card class="settings-card" shadow="hover">
        <div slot="header" class="card-header">
          <i class="el-icon-user"></i>
          <span>{{ $t('settings.user.title') }}</span>
        </div>
        <el-form label-position="top" :model="userSettings" class="settings-form">
          <el-form-item :label="$t('settings.user.avatar')">
            <div class="avatar-uploader">
              <el-avatar 
                :size="50" 
                :src="currentAvatar"
                @error="handleAvatarError"
              ></el-avatar>
              <el-upload
                class="avatar-upload"
                :action="uploadUrl"
                :headers="{}"
                name="file"
                :data="{ userId: userInfo.id }"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :on-error="handleAvatarError"
                :before-upload="beforeAvatarUpload">
                <el-button size="mini" type="primary" plain class="upload-btn">
                  <i class="el-icon-upload"></i> {{ $t('settings.user.changeAvatar') }}
                </el-button>
              </el-upload>
            </div>
          </el-form-item>

          <el-form-item :label="$t('settings.user.displayName')">
            <el-input 
              v-model="userSettings.displayName"
              placeholder="Enter your display name">
              <i slot="prefix" class="el-icon-user"></i>
            </el-input>
          </el-form-item>
          
          <el-divider content-position="left">{{ $t('settings.user.preferences') }}</el-divider>

          <el-form-item :label="$t('settings.user.language')">
            <el-select v-model="userSettings.language" :placeholder="$t('settings.user.language')" @change="handleLanguageChange">
              <el-option label="English" value="en"></el-option>
              <el-option label="中文" value="zh"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('settings.user.notifications.title')">
            <el-checkbox-group v-model="userSettings.notifications">
              <el-checkbox label="email">{{ $t('settings.user.notifications.email') }}</el-checkbox>
              <el-checkbox label="desktop">{{ $t('settings.user.notifications.desktop') }}</el-checkbox>
              <el-checkbox label="sound">{{ $t('settings.user.notifications.sound') }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- Task Split Settings Card -->
      <el-card class="settings-card" shadow="hover">
        <div slot="header" class="card-header">
          <i class="el-icon-set-up"></i>
          <span>{{ $t('settings.taskSplit.title') }}</span>
        </div>
        <el-form label-position="top" :model="splitSettings" class="settings-form">
          <el-form-item :label="$t('settings.taskSplit.granularity.title')">
            <div class="granularity-group">
              <div 
                v-for="option in granularityOptions" 
                :key="option.value"
                class="granularity-option"
                :class="{ 'is-selected': splitSettings.granularity === option.value }"
                @click="splitSettings.granularity = option.value"
              >
                <i :class="option.icon"></i>
                <span>{{ $t(`settings.taskSplit.granularity.${option.value}`) }}</span>
              </div>
            </div>
          </el-form-item>

          <el-divider content-position="left">{{ $t('settings.taskSplit.rules.title') }}</el-divider>

          <div class="checkbox-group">
            <el-checkbox v-model="splitSettings.keepStructure">
              <div class="checkbox-label">
                <i class="el-icon-folder"></i>
                <span>{{ $t('settings.taskSplit.rules.keepStructure') }}</span>
              </div>
            </el-checkbox>
            
            <el-checkbox v-model="splitSettings.autoDescription">
              <div class="checkbox-label">
                <i class="el-icon-document"></i>
                <span>{{ $t('settings.taskSplit.rules.autoDescription') }}</span>
              </div>
            </el-checkbox>
            
            <el-checkbox v-model="splitSettings.timeEstimate">
              <div class="checkbox-label">
                <i class="el-icon-time"></i>
                <span>{{ $t('settings.taskSplit.rules.timeEstimate') }}</span>
              </div>
            </el-checkbox>
          </div>

          <el-divider content-position="left">{{ $t('settings.taskSplit.smart.title') }}</el-divider>

          <div class="checkbox-group">
            <el-checkbox v-model="splitSettings.prioritySuggestion">
              <div class="checkbox-label">
                <i class="el-icon-star-off"></i>
                <span>{{ $t('settings.taskSplit.smart.priority') }}</span>
              </div>
            </el-checkbox>
            
            <el-checkbox v-model="splitSettings.timeSuggestion">
              <div class="checkbox-label">
                <i class="el-icon-timer"></i>
                <span>{{ $t('settings.taskSplit.smart.time') }}</span>
              </div>
            </el-checkbox>
            
            <el-checkbox v-model="splitSettings.dependencySuggestion">
              <div class="checkbox-label">
                <i class="el-icon-share"></i>
                <span>{{ $t('settings.taskSplit.smart.dependency') }}</span>
              </div>
            </el-checkbox>
          </div>
        </el-form>
      </el-card>
    </div>

    <!-- Save Button -->
    <div class="actions">
      <el-button type="primary" @click="saveSettings" icon="el-icon-check" size="small">
        {{ $t('settings.save') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'Settings',
  data() {
    return {
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      activeCollapse: ['advanced'],
      userInfo: JSON.parse(sessionStorage.getItem('userInfo') || '{}'),
      granularityOptions: [
        { value: 'fine', icon: 'el-icon-zoom-in' },
        { value: 'standard', icon: 'el-icon-files' },
        { value: 'coarse', icon: 'el-icon-zoom-out' }
      ],
      aiSettings: {
        model: 'gpt-4o-mini',
        openaiKey: '',
        apiEndpoint: '',
        proxy: '',
      },
      userSettings: {
        avatar: '',
        displayName: '',
        language: localStorage.getItem('language') || 'en',
        notifications: ['email', 'desktop'],
      },
      splitSettings: {
        granularity: 'standard',
        keepStructure: true,
        autoDescription: true,
        timeEstimate: true,
        prioritySuggestion: true,
        timeSuggestion: true,
        dependencySuggestion: true,
      },
      uploadUrl: '/api/user/upload/avatar',
    }
  },
  computed: {
    ...mapState('user', ['displayName', 'avatar']),
    currentAvatar() {
      if (!this.userSettings.avatar) return this.defaultAvatar;
      return this.userSettings.avatar.startsWith('http') ? 
        this.userSettings.avatar : 
        `/uploads/avatars/${this.userSettings.avatar}`;
    }
  },
  methods: {
    handleAvatarSuccess(response) {
      if (response.code === 20031) {
        const avatarPath = response.data;
        // 更新用户信息
        const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
        userInfo.avatar = avatarPath;
        sessionStorage.setItem('userInfo', JSON.stringify(userInfo));
        
        // 更新头像显示
        this.userSettings.avatar = avatarPath;
        this.$store.dispatch('user/updateAvatar', `/uploads/avatars/${avatarPath}`);
        this.$message.success(this.$t('settings.user.avatarUpload.success'));
      } else {
        this.$message.error(response.msg || this.$t('settings.user.avatarUpload.error'));
      }
    },
    handleAvatarError(err, file) {
      console.error('Avatar upload error:', err);
      this.$message.error(this.$t('settings.user.avatarUpload.error'));
    },
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error(this.$t('settings.user.avatarUpload.typeError'));
        return false;
      }
      if (!isLt2M) {
        this.$message.error(this.$t('settings.user.avatarUpload.sizeError'));
        return false;
      }

      // 显示上传中的提示
      this.$message({
        message: this.$t('settings.user.avatarUpload.uploading'),
        type: 'info',
        duration: 2000
      });

      return true;
    },
    handleLanguageChange(lang) {
      this.userSettings.language = lang;
    },
    async saveSettings() {
      try {
        let settingsChanged = false;
        const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
        
        // 1. Save language settings
        const newLang = this.userSettings.language;
        if (newLang !== this.$i18n.locale) {
          this.$i18n.locale = newLang;
          localStorage.setItem('language', newLang);
          settingsChanged = true;
        }
        
        // 2. Save display name
        if (this.userSettings.displayName && this.userSettings.displayName !== this.displayName) {
          try {
            const response = await axios.put(
              `/api/users/${userInfo.id}/displayName`,
              null,
              {
                params: {
                  displayName: this.userSettings.displayName
                }
              }
            );
            
            if (response.data.code === 20031) {
              userInfo.displayName = this.userSettings.displayName;
              sessionStorage.setItem('userInfo', JSON.stringify(userInfo));
              localStorage.setItem('userDisplayName', this.userSettings.displayName);
              this.$store.dispatch('user/updateDisplayName', this.userSettings.displayName);
              settingsChanged = true;
            } else {
              this.$message.error('Failed to update display name');
              this.userSettings.displayName = this.displayName;
              return;
            }
          } catch (error) {
            this.$message.error(`Error updating display name: ${error.message}`);
            this.userSettings.displayName = this.displayName;
            return;
          }
        }

        // 3. Save notification settings
        const savedNotifications = localStorage.getItem('notificationSettings');
        const currentNotifications = JSON.stringify(this.userSettings.notifications);
        if (currentNotifications !== savedNotifications) {
          localStorage.setItem('notificationSettings', currentNotifications);
          settingsChanged = true;
        }

        // 4. Save AI settings
        const savedAISettings = localStorage.getItem('aiSettings');
        const currentAISettings = JSON.stringify(this.aiSettings);
        if (currentAISettings !== savedAISettings) {
          localStorage.setItem('aiSettings', currentAISettings);
          settingsChanged = true;
        }

        // 5. Save task split settings
        const savedSplitSettings = localStorage.getItem('splitSettings');
        const currentSplitSettings = JSON.stringify(this.splitSettings);
        if (currentSplitSettings !== savedSplitSettings) {
          localStorage.setItem('splitSettings', currentSplitSettings);
          settingsChanged = true;
        }

        // Show success message if any settings were changed
        if (settingsChanged) {
          this.$message({
            message: this.$t('settings.saveSuccess') || 'Settings saved successfully!',
            type: 'success',
            duration: 2000
          });
        }
      } catch (error) {
        console.error('Error saving settings:', error);
        this.$message({
          message: `Failed to save settings: ${error.message}`,
          type: 'error',
          duration: 2000
        });
      }
    }
  },
  created() {
    // 1. Load saved language settings
    const savedLanguage = localStorage.getItem('language');
    if (savedLanguage) {
      this.userSettings.language = savedLanguage;
      this.$i18n.locale = savedLanguage;
    }

    // 2. Load saved user info
    const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
    
    // Load display name
    this.userSettings.displayName = userInfo.displayName || '';
      
    // Load avatar
    if (userInfo.avatar) {
      const avatarPath = userInfo.avatar;
      if (avatarPath.startsWith('http')) {
        this.userSettings.avatar = avatarPath;
      } else {
        const filename = decodeURIComponent(avatarPath.trim());
        this.userSettings.avatar = filename;
      }
    } else {
      this.userSettings.avatar = this.defaultAvatar;
    }

    // 3. Load notification settings
    const savedNotifications = localStorage.getItem('notificationSettings');
    if (savedNotifications) {
      this.userSettings.notifications = JSON.parse(savedNotifications);
    }

    // 4. Load AI settings
    const savedAISettings = localStorage.getItem('aiSettings');
    if (savedAISettings) {
      const aiSettings = JSON.parse(savedAISettings);
      this.aiSettings = { ...this.aiSettings, ...aiSettings };
    }

    // 5. Load task split settings
    const savedSplitSettings = localStorage.getItem('splitSettings');
    if (savedSplitSettings) {
      const splitSettings = JSON.parse(savedSplitSettings);
      this.splitSettings = { ...this.splitSettings, ...splitSettings };
    }
      
    // Set upload URL
    if (userInfo.id) {
      this.uploadUrl = `/api/users/${userInfo.id}/avatar/upload`;
    } else {
      console.warn('No user ID found in session storage');
    }
  }
}
</script>

<style scoped>
.settings-container {
  max-width: 1200px;
  margin: 12px auto;
  padding: 0 16px;
}

.settings-title {
  color: #303133;
  margin-bottom: 16px;
  font-weight: 600;
  font-size: 20px;
}

.settings-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 10px;
}

.settings-card {
  transition: all 0.3s ease;
  border-radius: 8px;
  height: 100%;
  min-height: 480px;
  display: flex;
  flex-direction: column;
}

.card-header {
  padding: 6px;
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.card-header i {
  margin-right: 8px;
  font-size: 18px;
  color: #409EFF;
}

.settings-form {
  padding: 4px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.el-form-item {
  margin-bottom: 12px;
}

.el-form-item__label {
  padding-bottom: 4px;
  line-height: 20px;
}

.checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin: 12px 0;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
}

.checkbox-label i {
  color: #409EFF;
  font-size: 14px;
}

.el-divider {
  margin: 16px 0;
}

.el-radio-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}

.el-radio-button {
  width: 100%;
}

.el-radio-button__inner {
  width: 100% !important;
  height: 36px !important;
  padding: 0 12px !important;
  text-align: left !important;
  border: 1px solid #DCDFE6 !important;
  border-radius: 4px !important;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: flex !important;
  align-items: center !important;
  font-size: 13px;
  position: relative;
  margin: 0 !important;
}

.el-radio-button__inner i {
  margin-right: 8px;
  font-size: 14px;
  flex-shrink: 0;
}

.el-radio-button__inner span {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
}

.el-radio-button:not(:first-child)::before {
  display: none !important;
}

.el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background-color: #ecf5ff !important;
  border-color: #409EFF !important;
  color: #409EFF !important;
  box-shadow: none !important;
}

/* 覆盖element-ui的默认样式 */
.el-radio-button:first-child .el-radio-button__inner,
.el-radio-button:last-child .el-radio-button__inner {
  border-radius: 4px !important;
}

.el-radio-button + .el-radio-button {
  margin-left: 0 !important;
}

.actions {
  margin-top: 10px;
  text-align: right;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.avatar-uploader {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.avatar-upload {
  display: inline-block;
}

.upload-btn {
  height: 28px;
  padding: 0 12px;
  font-size: 12px;
}

.el-avatar {
  border-radius: 50%;
  overflow: hidden;
  background-color: #f5f7fa;
}

.el-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.el-collapse-item__header {
  font-size: 13px;
  padding: 0 8px;
}

.el-collapse-item__content {
  padding: 8px;
}

/* 响应式调整 */
@media screen and (max-width: 1200px) {
  .settings-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media screen and (max-width: 768px) {
  .settings-grid {
    grid-template-columns: 1fr;
  }
  
  .settings-container {
    padding: 0 8px;
  }
}

.compact-form-item {
  margin-bottom: 8px;
}

.compact-divider {
  margin: 12px 0;
}

.el-collapse {
  border: none;
}

.el-collapse-item__header {
  font-size: 13px;
  padding: 0 4px;
  height: 32px;
  line-height: 32px;
  border: none;
}

.el-collapse-item__content {
  padding: 4px;
}

.el-collapse-item__wrap {
  border: none;
}

.granularity-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.granularity-option {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 12px;
  height: 36px;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 13px;
}

.granularity-option:hover {
  border-color: #409EFF;
}

.granularity-option.is-selected {
  background-color: #ecf5ff;
  border-color: #409EFF;
  color: #409EFF;
}

.granularity-option i {
  font-size: 14px;
  color: #409EFF;
}

.settings-card /deep/ .el-card__body {
  padding-top: 5px;
  padding-bottom: 5px;
}

</style> 