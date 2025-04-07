import axios from "axios"

function formatDate(date) {
  const pad = (num) => (num < 10 ? '0' + num : num);

  return date.getFullYear() + '-' +
    pad(date.getMonth() + 1) + '-' +
    pad(date.getDate()) + ' ' +
    pad(date.getHours()) + ':' +
    pad(date.getMinutes()) + ':' +
    pad(date.getSeconds());
}

export default {
  namespaced: true,
  state: {
    backEndUrl: "http://localhost:8088",
    updateActions: {
      addSub: "add",
      edit: "edit",
      remove: "remove",
      split: "split"
    },
    tasks: [
      {
        id: 1,
        label: "Task 1",
        children: [
          {
            id: 4,
            label: "Task 1-1",
            children: [

            ],
          },
        ],
      },
    ],
    notifiedReminders: new Set(),
  },
  getters: {
    finishedTaskList(state) {
      function getfinishedTasks(tasks) {
        return tasks.reduce((finishedTask, task) => {
          // 所有被完成的节点，不论是否有子任务
          if (task.beDeleted) {
            finishedTask.push(task.id);
          }
          // 如果有子任务，递归检查子任务
          if (task.subTasks && task.subTasks.length > 0) {
            finishedTask = finishedTask.concat(getfinishedTasks(task.subTasks));
          }
          return finishedTask;
        }, []);
      }
      return getfinishedTasks(state.tasks);
    },
    unfinishedTaskList(state) {
      function getUnfinishedTasks(tasks) {
        return tasks.reduce((unfinishedTask, task) => {
          // 所有未完成的节点，不论是否有子任务
          if (!task.beDeleted) {
            unfinishedTask.push(task.id);
          }
          // 如果有子任务，递归检查子任务
          if (task.subTasks && task.subTasks.length > 0) {
            unfinishedTask = unfinishedTask.concat(getUnfinishedTasks(task.subTasks));
          }
          return unfinishedTask;
        }, []);
      }
      return getUnfinishedTasks(state.tasks);
    }
  },
  mutations: {
    setTasks(state, newTasks) {
      state.tasks = newTasks
    },

  },
  actions: {
    async getTopTasks(context) {
      let res = await axios({
        method: 'GET',
        url: context.state.backEndUrl + '/tasks'
      }).catch((err) => {
        console.error(err);
      })
      context.commit('setTasks', res.data.data);
    },

    // 仅支持2个参数，一个state，一个修改的参数
    addTask(context, { parentId, description }) {
      // 获取当前时间，并格式化时间
      let nowDatetime = formatDate(new Date())
      // 构建任务
      let task = {
        parentId,
        description,
        startTime: nowDatetime,
        beDeleted: false,
      }
      // 提交数据
      axios({
        method: 'POST',
        url: context.state.backEndUrl + '/tasks',
        data: task
      }).then(() => {
        // 提交完了，重新获取数据
        context.dispatch('getTopTasks');
      }).catch((err) => {
        console.error(err);
      })

    },

    async deleteTask(context, id) {
      await axios({
        method: 'Delete',
        url: context.state.backEndUrl + `/tasks/${id}`,
      }).then(() => {
        // 对页面修改后重新获取数据
        context.dispatch('getTopTasks');
      }).catch((err) => {
        console.error(err);
      })
    },

    async updateTaskContent(context, editedTask) {
      await axios({
        method: 'PUT',
        url: context.state.backEndUrl + '/tasks',
        data: editedTask
      }).catch((err) => {
        console.error(err);
      }).then(() => {
        context.dispatch('getTopTasks');
      })
      
    },

    async splitTask(context, id) {
      console.log(id);
      await axios({
        method: 'POST',
        url: context.state.backEndUrl + `/tasks/split/${id}`,
      }).then(() => {
        // 对页面修改后重新获取数据
        context.dispatch('getTopTasks');
      }).catch((err) => {
        console.error(err);
      })
    },

    updateTaskLocation(context, MoveRequestBody) {
      console.log('Store: Sending move request to backend:', MoveRequestBody);
      
      // 发送请求到后端
      return axios({
        method: 'PUT',
        url: `${context.state.backEndUrl}/tasks/move`,
        data: MoveRequestBody,
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        console.log('Store: Backend response:', response.data);
        // 更新成功后立即获取最新数据
        return context.dispatch('getTopTasks');
      })
      .catch(error => {
        console.error('Store: Failed to update task location:', error.response || error);
        // 发生错误时回滚到服务器状态
        return context.dispatch('getTopTasks').then(() => {
          throw error;
        });
      });
    },

    updateTaskFinishState(context, { id, finish }) {
      console.log('Updating task state:', id, finish);
      return axios({
        method: 'PUT',
        url: `${context.state.backEndUrl}/tasks/${id}/finish`,
        params: { finish },
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(response => {
        console.log('Server response:', response.data);
        // 检查响应是否成功（20031是更新成功的代码）
        if (response.data.code !== 20031) {
          throw new Error(response.data.msg || 'Failed to update task state');
        }
        console.log(id, "状态被修改", finish);
        
        // 直接返回getTopTasks的结果，避免在组件中再次调用
        return context.dispatch('getTopTasks');
      }).catch(err => {
        console.error('Failed to update task state:', err);
        throw err;
      });
    },

    async updateTaskReminder(context, { id, reminderTime }) {
      try {
        // 发送PUT请求更新提醒时间
        await axios({
          method: 'PUT',
          url: `${context.state.backEndUrl}/tasks/${id}/reminder`,
          data: { reminderTime }  // 这里的reminderTime可以是具体时间字符串或null
        });
        
        // 更新成功后重新获取任务列表
        await context.dispatch('getTopTasks');
        return true;
      } catch (error) {
        console.error('Failed to update task reminder:', error);
        return false;
      }
    },

    // 检查提醒时间并触发通知
    checkReminders(context) {
      const checkTaskReminders = async (tasks) => {
        let hasNotificationSent = false; // 标记是否发送过通知
        
        for (const task of tasks) {
          if (task.reminderTime) {
            const reminderDate = new Date(task.reminderTime);
            const now = new Date();
            
            // 生成提醒的唯一标识
            const reminderId = `${task.id}_${task.reminderTime}`;
            
            // 检查是否在提醒时间的前后30秒内，并且没有发送过提醒
            if (Math.abs(reminderDate - now) <= 30000 && !context.state.notifiedReminders.has(reminderId)) {
              // 使用浏览器通知API
              if (Notification.permission === "granted") {
                new Notification("任务提醒", {
                  body: `任务"${task.description}"的提醒时间到了！`,
                  icon: "/favicon.ico"
                });
                
                // 记录已发送的提醒
                context.state.notifiedReminders.add(reminderId);
                hasNotificationSent = true;
                
                // 5分钟后从已通知集合中移除，允许再次提醒（如果用户没有处理）
                setTimeout(() => {
                  context.state.notifiedReminders.delete(reminderId);
                }, 300000);
              }
            }
          }
          
          // 递归检查子任务
          if (task.subTasks && task.subTasks.length > 0) {
            const childHasNotification = await checkTaskReminders(task.subTasks);
            hasNotificationSent = hasNotificationSent || childHasNotification;
          }
        }
        
        return hasNotificationSent;
      };
      
      // 执行检查并在发送通知后刷新任务列表
      return checkTaskReminders(context.state.tasks).then(hasNotification => {
        if (hasNotification) {
          // 如果有通知被发送，刷新任务列表
          return context.dispatch('getTopTasks');
        }
      });
    }
  },
  modules: {
  }
}