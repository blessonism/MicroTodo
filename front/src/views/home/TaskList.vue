<template>
  <div class="container">
    <el-row type="flex" class="layout-row" justify="center">
      <el-col :span="20">
        <el-tree
          highlight-current
          show-checkbox
          draggable
          node-key="id"
          :data="tasks"
          :props="defaultProps"
          :default-expanded-keys="unfinishedTaskList"
          :default-checked-keys="finishedTaskList"
          :expand-on-click-node="false"
          :check-strictly="true"
          :allow-drag="allowDrag"
          :allow-drop="allowDrop"
          :check-on-click-node="true"
          @node-drag-start="handleDragStart"
          @node-drag-end="handleDragEnd"
          @node-drag-enter="handleDragEnter"
          @node-drag-leave="handleDragLeave"
          @node-drop="handleDrop"
          @check-change="handleCheckStateChange"
          @node-click="handleNodeClick"
          ref="listTree"
          class="list-tree"
        >
          <span
            class="custom-tree-node"
            slot-scope="{ node, data }"
            :loading="node.loading"
          >
            <i class="el-icon-rank drag-handle"></i>
            <span
              @click.stop="handleNodeContentClick(node, data)"
              @dblclick="editDialog(updateActions.edit, node)"
              :finished="node.checked"
              class="node-content"
            >
              <span class="description">{{ data.description }}</span>
              <span v-if="getTaskReminderTime(data.id)" class="reminder-time">
                <i class="el-icon-time"></i>
                {{ formatReminderTime(getTaskReminderTime(data.id)) }}
              </span>
            </span>
            <div class="node-actions" @click.stop>          
              <TaskReminder 
                :taskId="data.id"
                :currentReminder.sync="data.reminderTime || ''"
                @update="handleReminderUpdate"
              />
              <i
                class="bx bxs-magic-wand"
                @click.stop="handleEditCommand({ command: updateActions.split, node: node })"
                title="Split task"
              ></i>
              <i
                class="el-icon-delete"
                @click.stop="removeWarning(data.id)"
                title="Delete task"
              ></i>
            </div>
          </span>
        </el-tree>
      </el-col>
    </el-row>
    <!-- 弹窗组件 -->
    <UpdateTaskDialog
      :dialogPayload="dialogPayload"
      :dialogVisible="dialogVisible"
      @closeDialog="controlDialogVisible(false)"
    >
    </UpdateTaskDialog>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from "vuex";
import UpdateTaskDialog from "@/components/UpdateTaskDialog.vue";
import TaskReminder from "@/components/tasks/TaskReminder.vue";

export default {
  components: {
    UpdateTaskDialog,
    TaskReminder
  },

  data() {
    return {
      spliting: true,
      dialogVisible: false,
      dialogPayload: {
        action: "",
        editingNodeId: 0,
        editingNodeContent: "",
      },
      defaultProps: {
        children: "subTasks",
        label: "description",
        disabled: (data) => false
      },
      // 本地存储提醒时间
      localReminders: new Map(),
      // 用于节流的计时器
      syncTimer: null,
      updateStateTimer: null, // 添加用于防抖的计时器
      processingNodes: new Set(), // 添加正在处理的节点集合
      reminderCheckTimer: null, // 添加提醒检查定时器引用
    };
  },

  computed: {
    ...mapState("tasks", ["tasks", "updateActions"]),
    ...mapGetters("tasks", ["finishedTaskList", "unfinishedTaskList"]),
  },

  methods: {
    handleNodeClick(node) {
      this.$emit("node-selected", node);
    },
    ...mapActions("tasks", [
      "getTopTasks",
      "deleteTask",
      "updateTaskLocation",
      "updateTaskFinishState",
      "splitTask",
      "updateTaskReminder",
      "checkReminders"
    ]),
    editDialog(command, node) {
      this.dialogPayload.action = command;
      this.dialogPayload.editingNodeId = node.data.id;
      this.dialogPayload.editingNodeContent =
        command === this.updateActions.edit ? node.label : "";
      this.dialogVisible = true;
    },
    controlDialogVisible(visible) {
      this.dialogVisible = visible;
    },
    removeWarning(nodeId) {
      this.$confirm(
        "This operation will delete the task. Do you want to continue?",
        "Hint",
        {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      )
        .then(() => {
          this.deleteTask(nodeId)
            .then(() => {
              this.$message({
                type: "success",
                message: "Successfully deleted!",
              });
            })
            .catch((err) => {
              console.error(err);
              this.$message({
                type: "error",
                message: "Fail to delete!",
              });
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "Cancel delete",
          });
        });
    },
    async handleDrop(dragNode, dropNode, dropType, ev) {
        // 检查拖拽节点和目标节点是否存在
        if (!dragNode || !dragNode.data || !dragNode.data.id) {
            console.error('Invalid drag operation: missing node information');
            return;
        }

        // 构造移动请求体
        const moveRequestBody = {
            movedTaskId: dragNode.data.id,
            targetTaskId: dropNode ? dropNode.data.id : null,
            moveAction: dropType
        };

        try {
            // 只调用一次updateTaskLocation，因为它内部会刷新数据
            await this.$store.dispatch('tasks/updateTaskLocation', moveRequestBody);
        } catch (error) {
            // 只在失败时显示错误提示
            this.$message.error(error.message || '移动任务失败');
        }
    },
    // 使用防抖延迟同步到后端
    debounceSyncToBackend() {
      if (this.syncTimer) {
        clearTimeout(this.syncTimer);
      }
      
      this.syncTimer = setTimeout(() => {
        this.getTopTasks();
      }, 2000); // 2秒后同步
    },
    // 在组件销毁前确保同步完成
    beforeDestroy() {
      if (this.syncTimer) {
        clearTimeout(this.syncTimer);
        this.getTopTasks();
      }
      if (this.reminderCheckTimer) {
        clearInterval(this.reminderCheckTimer);
      }
    },
    handleCheckStateChange(data, checkState) {
      // 如果节点正在处理中，则返回
      if (this.processingNodes.has(data.id)) {
        return;
      }
      
      console.log('Changing task state:', data.id, checkState);
      
      // 将节点添加到处理集合中
      this.processingNodes.add(data.id);
      
      // 清除之前的定时器
      if (this.updateStateTimer) {
        clearTimeout(this.updateStateTimer);
      }
      
      this.updateStateTimer = setTimeout(async () => {
        try {
          const currentNode = this.$refs.listTree.getNode(data.id);
          if (!currentNode) {
            console.warn('Node not found in tree:', data.id);
            return;
          }

          // 更新本地状态
          this.$refs.listTree.setChecked(data.id, checkState, false);

          // 更新后端状态
          await this.updateTaskFinishState({ id: data.id, finish: checkState });

          // 如果是取消勾选父任务，则更新子任务状态
          if (!checkState && data.subTasks && data.subTasks.length > 0) {
            const updateChildTasks = async (tasks) => {
              // 使用 Promise.all 并行处理所有子任务
              await Promise.all(tasks.map(async (task) => {
                try {
                  // 更新子任务状态
                  await this.updateTaskFinishState({ id: task.id, finish: false });
                  this.$refs.listTree.setChecked(task.id, false, false);
                  
                  // 如果子任务还有子任务，递归处理
                  if (task.subTasks && task.subTasks.length > 0) {
                    await updateChildTasks(task.subTasks);
                  }
                } catch (error) {
                  console.error(`Failed to update child task ${task.id}:`, error);
                }
              }));
            };
            
            await updateChildTasks(data.subTasks);
          }

          console.log('Task state updated successfully');
        } catch (error) {
          console.error('Failed to update task state:', error);
          this.$message.error('Failed to update task state');
          // 发生错误时恢复原始状态
          const node = this.$refs.listTree.getNode(data.id);
          if (node) {
            this.$refs.listTree.setChecked(data.id, !checkState, false);
          }
        } finally {
          // 完成后从处理集合中移除节点
          this.processingNodes.delete(data.id);
        }
      }, 300); // 300ms 的防抖延迟
    },
    async handleSplitTask(nodeId, node) {
      node.loading = true;
      await this.splitTask(nodeId);
      node.loading = false;
    },
    async handleEditCommand({ command, node }) {
      switch (command) {
        case this.updateActions.addSub:
          this.editDialog(this.updateActions.addSub, node);
          break;
        case this.updateActions.remove:
          this.removeWarning(node.data.id);
          break;
        case this.updateActions.split:
          node.loading = true;
          await this.splitTask(node.data.id);
          node.loading = false;
          break;
        default:
          break;
      }
    },
    handleDragStart(draggingNode) {
      if (draggingNode && draggingNode.elm) {
        draggingNode.elm.classList.add("dragging");
      }
    },
    handleDragEnd(draggingNode) {
      if (draggingNode && draggingNode.elm) {
        draggingNode.elm.classList.remove("dragging");
      }
    },
    handleDragEnter(dropNode, dragNode, type) {
      if (dropNode && dropNode.elm) {
        dropNode.elm.classList.add("drop-target");
        dropNode.elm.classList.add(`drop-${type}`);
      }
    },
    handleDragLeave(dropNode, dragNode, type) {
      if (dropNode && dropNode.elm) {
        dropNode.elm.classList.remove("drop-target");
        dropNode.elm.classList.remove(`drop-${type}`);
      }
    },
    allowDrag(draggingNode) {
      // 只允许未完成的任务拖拽
      return !draggingNode.data.beDeleted;
    },
    allowDrop(draggingNode, dropNode, type) {
      // 不允许拖拽到已完成的任务内部
      if (dropNode.data.beDeleted && type === "inner") {
        return false;
      }
      
      // 不允许将任务拖拽到自己或自己的子任务中
      if (type === "inner") {
        let currentNode = dropNode;
        while (currentNode.parent) {
          if (currentNode.parent.data.id === draggingNode.data.id) {
            return false;
          }
          currentNode = currentNode.parent;
        }
      }

      // 不允许拖拽到自己的位置
      if (draggingNode.data.id === dropNode.data.id) {
        return false;
      }
      
      return true;
    },
    setCheckedTasks(taskIds) {
      this.$refs["listTree"].setCheckedKeys(taskIds);
    },
    // toggleNode(node) {
    //   node.expanded
    //     ? this.$refs.listTree.store.collapseNode(node)
    //     : this.$refs.listTree.store.expandNode(node);
    // },
    formatReminderTime(reminderTime) {
      if (!reminderTime) return '';
      
      const reminderDate = new Date(reminderTime);
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
    },
    handleReminderUpdate(reminder) {
      console.log('Updating reminder:', reminder);
      
      // 获取当前任务的ID和提醒时间
      const { id, reminderTime } = reminder;
      
      // 发送到后端
      this.updateTaskReminder({
        id,
        reminderTime
      }).then(() => {
        // 成功后重新获取任务列表以确保数据同步
        this.getTopTasks();
      }).catch(err => {
        console.error('Failed to update reminder:', err);
        // 如果后端更新失败，重新获取任务列表
        this.getTopTasks();
      });
    },
    // 获取提醒时间的方法
    getTaskReminderTime(taskId) {
      const findTask = (tasks, id) => {
        for (const task of tasks) {
          if (task.id === id) {
            return task;
          }
          if (task.subTasks && task.subTasks.length > 0) {
            const found = findTask(task.subTasks, id);
            if (found) return found;
          }
        }
        return null;
      };
      
      const task = findTask(this.tasks, taskId);
      return task ? task.reminderTime : null;
    },
    handleNodeContentClick(node, data) {
      // 手动触发复选框状态切换
      const currentState = this.$refs.listTree.getChecked().includes(data.id);
      this.handleCheckStateChange(data, !currentState);
    },
  },

  async created() {
    try {
      await this.getTopTasks();
      
      // 设置已完成任务的选中状态
      this.$nextTick(() => {
        if (this.$refs.listTree) {
          // 先清除所有选中状态
          this.$refs.listTree.setCheckedKeys([]);
          
          // 获取所有已完成任务的ID
          const finishedIds = this.finishedTaskList;
          console.log('Setting finished tasks:', finishedIds);
          
          // 逐个设置已完成任务的选中状态
          finishedIds.forEach(taskId => {
            try {
              this.$refs.listTree.setChecked(taskId, true, false);
              console.log('Set task checked:', taskId);
            } catch (err) {
              console.error('Failed to set task checked:', taskId, err);
            }
          });
        }
      });
      
      // 设置定时检查提醒
      if (this.reminderCheckTimer) {
        clearInterval(this.reminderCheckTimer);
      }
      this.reminderCheckTimer = setInterval(() => {
        this.checkReminders();
      }, 60000); // 每分钟检查一次
    } catch (error) {
      console.error('Failed to initialize tasks:', error);
      this.$message.error('Failed to load tasks');
    }
  },

  mounted() {
    // 设置 body 的样式

    document.body.style.overflow = "visible";
  },
};
</script>

<style scoped>
.container {
  margin: 20px;
}
.list-tree {
  padding: 5px;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5);
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  font-size: 16px;
  padding-right: 8px;
}

.bx.bxs-magic-wand {
  cursor: pointer;
  padding: 8px;
}
.node-content {
  margin-right: 10px;
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
}

.description {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.reminder-time {
  font-size: 13px;
  color: #e6a23c;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  white-space: nowrap;
  margin-left: 8px;
  padding: 2px 8px;
  background-color: rgba(230, 162, 60, 0.1);
  border-radius: 4px;
}

.reminder-time i {
  font-size: 14px;
}

.node-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
}

.node-actions i {
  font-size: 16px;
  padding: 8px;
  cursor: pointer;
  color: #606266;
  transition: all 0.3s;
  border-radius: 4px;
}

.node-actions i:hover {
  color: #606266;
  background-color: rgba(96, 98, 102, 0.1);
}

.node-actions i.bx {
  font-size: 18px;
}

span[finished="true"] {
  color: #999;
  pointer-events: none;
  opacity: 0.5;
  text-decoration: line-through;
  text-decoration-thickness: 2px;
}
span[loading="true"] {
  color: #999;
  pointer-events: none;
  opacity: 0.5;
}
div /deep/ .el-tree-node__content {
  height: 43px !important;
}
div /deep/ [role="group"] {
  border-radius: 4px;
  margin-left: 20px; /* Indentation for nested tasks */
  /* box-shadow: 0px 0px 2px rgba(0, 0, 0, 0.5); */
  /* 调整边框 */
}
.dragging {
  opacity: 0.7;
  background-color: rgba(64, 158, 255, 0.1);
  border: 2px dashed #409EFF;
  border-radius: 4px;
}
.drop-target {
  position: relative;
}
.drop-target::before {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  height: 2px;
  background-color: #409EFF;
  z-index: 1;
}
.drop-target.drop-before::before {
  top: 0;
}
.drop-target.drop-after::before {
  bottom: 0;
}
.drop-target.drop-inner {
  background-color: rgba(64, 158, 255, 0.1);
  border: 2px dashed #409EFF;
  border-radius: 4px;
}
.drag-handle {
  cursor: grab;
  margin-right: 10px;
}
.drag-handle:active {
  cursor: grabbing;
}
/* .expand-collapse-button {
  margin-right: 10px;
} */
.node-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
}
.node-actions .el-button {
  padding: 7px;
}
.node-actions i.bx {
  font-size: 18px;
}
.reminder-time {
  margin-left: 10px;
  font-size: 13px;
  color: #e6a23c;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
.reminder-time i {
  font-size: 14px;
}
.preset-btn {
  width: 100%;
  height: 56px;
  padding: 8px;
}

.preset-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  height: 100%;
}

.preset-time {
  font-size: 16px;
  font-weight: 500;
  color: #606266;
}

.preset-desc {
  font-size: 13px;
  color: #909399;
}

.node-actions i.bx.bxs-magic-wand:hover {
  color: #409EFF;
  background-color: rgba(64, 158, 255, 0.1);
}

.node-actions i.el-icon-delete:hover {
  color: #f56c6c;
  background-color: rgba(245, 108, 108, 0.1);
}
</style>
