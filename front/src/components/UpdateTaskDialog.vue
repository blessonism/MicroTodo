<template>
  <div>
    <!-- 修改弹窗 -->
    <el-dialog
      :title="dialogPayload.action"
      :visible="dialogVisible"
      @opened="selectText()"
      @close="closeDialog()"
    >
      <el-form>
        <el-form-item label="Task Content">
          <el-input
            ref="myInput"
            v-model="description"
            autocomplete="off"
            @keyup.enter.native="onSubmit()"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog()">Cancel</el-button>
        <el-button type="primary" @click="onSubmit()">OK</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
export default {
  props: ["dialogPayload", "dialogVisible"],

  data() {
    return {
      id: 0,
      description: "",
      action: "",
    };
  },
  computed: {
    ...mapState("tasks", ["updateActions"]),
  },
  watch: {
    // 监视打开对话框时，数据负载的变换
    dialogPayload: {
      deep: true,
      handler(newVal) {
        this.id = newVal.editingNodeId;
        this.description = newVal.editingNodeContent;
        this.action = newVal.action;
      },
    },
  },
  methods: {
    ...mapActions("tasks", ["addTask", "updateTaskContent"]),
    emptyWarning() {
      // 空内容提示
      this.$alert("Please enter your task content.", "Empty Input", {
        confirmButtonText: "OK",
        callback: () => {
          this.$message({
            type: "warning",
            message: `Modification Failure`,
          });
        },
      });
    },
    onSubmit() {
      this.description = this.description.trim();

      if (!this.description) {
        this.emptyWarning();
        return;
      }
      // 注意变量名统一

      switch (this.action) {
        case this.updateActions.addSub:
          // 添加子任务操作
          // 子任务的parentId为当前任务id
          this.addTask({ parentId: this.id, description: this.description });
          break;
        case this.updateActions.edit:
          // 修改当前任务内容
          this.updateTaskContent({
            id: this.id,
            description: this.description,
          });
          break;
        default:
          return;
      }

      // 关闭弹框
      this.closeDialog();
    },

    selectText() {
      // 选中文字
      this.$nextTick(() => {
        this.$refs.myInput.focus();
        const input = this.$refs.myInput.$el.querySelector('input');
        input.setSelectionRange(this.description.length, this.description.length);
      });
    },
    closeDialog() {
      this.$emit("closeDialog");
    },
  },
};
</script>

<style></style>
