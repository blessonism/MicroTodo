<template>
  <div>
    <el-row type="flex" justify="center">
      <el-col :span="20">
        <el-form :model="task" class="task-form">
          <el-col :span="3.8">
            <Dropdown/>            
          </el-col>
          <el-col :span="18">
            <el-form-item>
              <el-input
                v-model="task.description"
                placeholder="Add new item..."
                class="task-Input"                
                @keyup.enter.native="submitForm"     
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-button type="primary" @click="submitForm" class="addBtn"
                >Add</el-button
              >
            </el-form-item>
          </el-col>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import Dropdown from "../../components/Dropdown.vue";
export default {
  components: {
    Dropdown,
  },
  props: {},
  data() {
    return {
      task: {
        // 默认是顶层任务
        parentId: null,
        description: "",
      },
    };
  },
  methods: {
    ...mapActions("tasks", ["addTask"]),
    submitForm() {
      this.task.description = this.task.description.trim();
      // 空输入提示
      if (!this.task.description) {
        this.$message({
          showClose: true,
          message: "Please enter the task content.",
          type: "warning",
        });
        return;
      }
      // 添加任务
      this.addTask(this.task);
      // 清空输入框
      this.task.description = "";
    },
  },
};
</script>

<style scoped>
.addBtn {
  width: 100%;
}

.task-form {
  display: flex;
  align-items: center;
}

/* 调整 el-input 和 el-button 的高度 */
.el-button {
  height: 50px; /* 设置所需高度 */
  line-height: 48px; /* 确保文本在中间 */
}

.task-Input >>> .el-input__inner {
  height: 50px !important; /* 设置所需高度 */
  line-height: 48px !important; /* 确保文本在中间 */
  padding: 0 15px !important; /* 调整内边距 */
}

.el-button {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 20px; /* 调整内边距 */
}
</style>
