<template>
  <el-popover
    placement="bottom"
    width="320"
    trigger="click"
    v-model="visible"
    popper-class="reminder-popover"
  >
    <div class="reminder-content">
      <!-- 快速预设时间 -->
      <div class="quick-presets">
        <div class="preset-title">快速设置</div>
        <div class="preset-buttons">
          <el-button 
            v-for="preset in quickPresets" 
            :key="preset.value"
            class="preset-btn"
            :class="{ active: selectedPreset === preset.value }"
            @click="selectPreset(preset)"
          >
            <div class="preset-info">
              <span class="preset-desc">{{ preset.label }}</span>
              <span class="preset-time">{{ preset.time }}</span>
            </div>
          </el-button>
        </div>
      </div>

      <!-- 分隔线 -->
      <el-divider>或者</el-divider>

      <!-- 自定义时间设置 -->
      <div class="custom-time">
        <div class="custom-title">自定义时间</div>
        <el-date-picker
          v-model="customDate"
          type="date"
          placeholder="选择日期"
          :picker-options="dateOptions"
          @change="handleDateChange"
        />
        <el-time-select
          v-model="customTime"
          :picker-options="timeOptions"
          placeholder="选择时间"
          @change="handleTimeChange"
        />
      </div>

      <!-- 重复提醒设置 -->
      <div class="repeat-setting" v-if="showRepeatOption">
        <div class="repeat-title">
          <span>重复提醒</span>
          <el-switch v-model="enableRepeat" @change="handleRepeatChange" />
        </div>
        <el-select
          v-if="enableRepeat"
          v-model="repeatType"
          placeholder="选择重复方式"
          class="repeat-select"
        >
          <el-option label="每天" value="daily" />
          <el-option label="每周" value="weekly" />
          <el-option label="每月" value="monthly" />
        </el-select>
      </div>

      <!-- 操作按钮 -->
      <div class="reminder-actions">
        <template v-if="currentReminder">
          <el-button size="small" @click="clearReminder" icon="el-icon-delete">清除</el-button>
        </template>
        <div class="button-group">
          <el-button size="small" @click="visible = false">取消</el-button>
          <el-button 
            type="primary" 
            size="small" 
            @click="confirmReminder"
            :disabled="!isValidReminder"
          >确定</el-button>
        </div>
      </div>
    </div>

    <!-- 触发按钮 -->
    <template #reference>
      <div class="reminder-trigger" @mouseenter="showTooltip = true" @mouseleave="showTooltip = false">
        <i :class="['reminder-icon', 'el-icon-alarm-clock', { 'has-reminder': hasReminder }]"></i>
        <transition name="fade">
          <div class="reminder-tooltip" v-if="showTooltip && hasReminder">
            <div class="tooltip-content">
              <i class="el-icon-time"></i>
              <span>{{ formatReminderDisplay }}</span>
            </div>
            <div class="tooltip-arrow"></div>
          </div>
        </transition>
      </div>
    </template>
  </el-popover>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  name: 'TaskReminder',
  
  props: {
    taskId: {
      type: Number,
      required: true
    },
    currentReminder: {
      type: String,
      default: null,
      required: true
    }
  },
  
  data() {
    return {
      visible: false,
      showTooltip: false,
      selectedPreset: null,
      customDate: null,
      customTime: null,
      enableRepeat: false,
      repeatType: 'daily',
      showRepeatOption: false,
      loading: false
    };
  },
  
  computed: {
    hasReminder() {
      return !!this.currentReminder;
    },

    quickPresets() {
      const now = new Date();
      const today = new Date();
      const tomorrow = new Date(today);
      tomorrow.setDate(today.getDate() + 1);

      const formatTimeOnly = (date) => {
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        return `${hours}:${minutes}`;
      };

      return [
        {
          label: '3分钟后',
          time: formatTimeOnly(new Date(now.getTime() + 3 * 60000)),
          value: '3min',
          date: new Date(now.getTime() + 3 * 60000)
        },
        {
          label: '10分钟后',
          time: formatTimeOnly(new Date(now.getTime() + 10 * 60000)),
          value: '10min',
          date: new Date(now.getTime() + 10 * 60000)
        },
        {
          label: '1小时后',
          time: formatTimeOnly(new Date(now.getTime() + 60 * 60000)),
          value: '1hour',
          date: new Date(now.getTime() + 60 * 60000)
        },
        {
          label: '今晚',
          time: '20:00',
          value: 'tonight',
          date: (() => {
            const d = new Date(today);
            d.setHours(20, 0, 0, 0);
            return d;
          })()
        },
        {
          label: '明早',
          time: '09:00',
          value: 'tomorrow_morning',
          date: (() => {
            const d = new Date(tomorrow);
            d.setHours(9, 0, 0, 0);
            return d;
          })()
        },
        {
          label: '明天下午',
          time: '14:00',
          value: 'tomorrow_afternoon',
          date: (() => {
            const d = new Date(tomorrow);
            d.setHours(14, 0, 0, 0);
            return d;
          })()
        }
      ].filter(preset => preset.date > now);
    },

    dateOptions() {
      return {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      };
    },

    timeOptions() {
      return {
        start: '00:00',
        step: '00:30',
        end: '23:30',
        minTime: this.isToday ? this.minTime : undefined
      };
    },

    isToday() {
      if (!this.customDate) return false;
      const today = new Date();
      const selected = new Date(this.customDate);
      return today.toDateString() === selected.toDateString();
    },

    minTime() {
      const now = new Date();
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = Math.ceil(now.getMinutes() / 30) * 30;
      return `${hours}:${minutes.toString().padStart(2, '0')}`;
    },

    isValidReminder() {
      return this.selectedPreset || (this.customDate && this.customTime);
    },

    formatReminderDisplay() {
      if (!this.currentReminder) return '';
      
      const reminderDate = new Date(this.currentReminder);
      const now = new Date();
      const diffMinutes = Math.floor((reminderDate - now) / 60000);
      
      if (diffMinutes < 0) return '已过期';
      if (diffMinutes < 60) return `${diffMinutes}分钟后`;
      
      const diffHours = Math.floor(diffMinutes / 60);
      if (diffHours < 24) return `${diffHours}小时后`;
      
      const today = new Date();
      const tomorrow = new Date(today);
      tomorrow.setDate(today.getDate() + 1);
      
      const formatTime = (date) => {
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        return `${hours}:${minutes}`;
      };
      
      if (reminderDate.toDateString() === today.toDateString()) {
        return `今天 ${formatTime(reminderDate)}`;
      }
      
      if (reminderDate.toDateString() === tomorrow.toDateString()) {
        return `明天 ${formatTime(reminderDate)}`;
      }
      
      const month = (reminderDate.getMonth() + 1).toString().padStart(2, '0');
      const day = reminderDate.getDate().toString().padStart(2, '0');
      return `${month}月${day}日 ${formatTime(reminderDate)}`;
    }
  },
  
  methods: {
    ...mapActions('tasks', ['updateTaskReminder']),

    async confirmReminder() {
      if (!this.isValidReminder) return;

      this.loading = true;
      try {
        let reminderTime;
        if (this.selectedPreset) {
          // 使用预设时间
          const preset = this.quickPresets.find(p => p.value === this.selectedPreset);
          reminderTime = preset.date;
        } else {
          // 使用自定义时间
          const [hours, minutes] = this.customTime.split(':');
          reminderTime = new Date(this.customDate);
          reminderTime.setHours(parseInt(hours), parseInt(minutes), 0);
        }

        // 格式化日期为后端需要的格式
        const formattedTime = this.formatDateForBackend(reminderTime);
        
        // 构建提醒更新的请求参数
        const reminderUpdate = {
          id: this.taskId,
          reminderTime: formattedTime
        };
        
        const success = await this.updateTaskReminder(reminderUpdate);

        if (success) {
          // 通知父组件更新
          this.$emit('update', reminderUpdate);
          this.$message.success('提醒设置成功');
          this.visible = false;
        } else {
          this.$message.error('提醒设置失败');
        }
      } catch (error) {
        console.error('Failed to set reminder:', error);
        this.$message.error('提醒设置失败');
      } finally {
        this.loading = false;
      }
    },

    formatDateForBackend(date) {
      const pad = (num) => String(num).padStart(2, '0');
      return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:00`;
    },

    async clearReminder() {
      this.loading = true;
      try {
        // 先重置所有相关状态
        this.selectedPreset = null;
        this.customDate = null;
        this.customTime = null;
        this.enableRepeat = false;
        this.repeatType = 'daily';
        
        // 发送PUT请求清除提醒时间
        const success = await this.updateTaskReminder({
          id: this.taskId,
          reminderTime: null  // 显式设置为null来清除提醒时间
        });

        if (success) {
          // 通知父组件更新
          this.$emit('update', {
            id: this.taskId,
            reminderTime: null
          });
          
          // 关闭弹窗并显示成功消息
          this.visible = false;
          this.$message.success('提醒已清除');
        } else {
          this.$message.error('清除提醒失败');
        }
      } catch (error) {
        console.error('Failed to clear reminder:', error);
        this.$message.error('清除提醒失败');
      } finally {
        this.loading = false;
      }
    },

    selectPreset(preset) {
      this.selectedPreset = preset.value;
      this.customDate = null;
      this.customTime = null;
    },

    handleDateChange() {
      this.selectedPreset = null;
    },

    handleTimeChange() {
      this.selectedPreset = null;
    },

    handleRepeatChange(value) {
      if (!value) {
        this.repeatType = 'daily';
      }
    }
  },

  mounted() {
    // 请求通知权限
    if (Notification.permission === "default") {
      Notification.requestPermission();
    }
  },

  watch: {
    currentReminder: {
      immediate: true,
      handler(newVal) {
        if (!newVal) {
          // 当提醒被清除时，重置所有状态
          this.selectedPreset = null;
          this.customDate = null;
          this.customTime = null;
          this.enableRepeat = false;
          this.repeatType = 'daily';
        }
      }
    }
  }
};
</script>

<style scoped>
.reminder-icon {
  font-size: 18px;
  padding: 8px;
  cursor: pointer;
  color: #606266;
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.reminder-icon:hover {
  color: #409eff;
}

.reminder-icon.has-reminder {
  color: #e6a23c;
}

.reminder-trigger {
  display: inline-flex;
  align-items: center;
  position: relative;
  z-index: 1;
}

.reminder-tooltip {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: #303133;
  color: #fff;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  margin-bottom: 8px;
  z-index: 2000;
}

:deep(.el-popover) {
  padding: 12px;
  min-width: 200px;
}

.reminder-content {
  padding: 12px;
}

.quick-presets {
  margin-bottom: 16px;
}

.preset-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.custom-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.preset-buttons {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 8px;
  margin-top: 8px;
}

.preset-btn {
  height: 48px;
  padding: 0;
  border: 1px solid #dcdfe6;
  background: #fff;
  transition: all 0.3s;
}

.preset-btn:hover {
  background: #f5f7fa;
}

.preset-btn.active {
  background: #ecf5ff;
  border-color: #409eff;
  color: #409eff;
}

.preset-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  height: 100%;
  padding: 8px;
}

.preset-time {
  font-size: 14px;
  font-weight: 500;
  color: #606266;
}

.preset-desc {
  font-size: 13px;
  color: #909399;
}

.preset-btn.active .preset-time,
.preset-btn.active .preset-desc {
  color: #409eff;
}

:deep(.preset-btn.el-button) {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0;
  width: 100%;
  height: 100%;
}

:deep(.preset-btn.el-button:hover),
:deep(.preset-btn.el-button:focus) {
  color: inherit;
  border-color: #dcdfe6;
  background-color: #f5f7fa;
}

:deep(.preset-btn.el-button.active:hover),
:deep(.preset-btn.el-button.active:focus) {
  color: #409eff;
  border-color: #409eff;
  background-color: #ecf5ff;
}

:deep(.el-date-picker) {
  width: 100%;
}

:deep(.el-time-select) {
  width: 100%;
}

:deep(.el-divider__text) {
  font-size: 12px;
  color: #909399;
}

.reminder-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
  margin-top: 16px;
}

.button-group {
  display: flex;
  gap: 8px;
  margin-left: auto;
}

:deep(.el-button) {
  margin: 0;
}

:deep(.el-button--small) {
  padding: 8px 15px;
  min-width: 68px;
}

.tooltip-content {
  display: flex;
  align-items: center;
  gap: 4px;
}

.tooltip-arrow {
  position: absolute;
  bottom: -4px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-top: 4px solid #303133;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
