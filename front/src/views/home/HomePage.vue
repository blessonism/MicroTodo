<template>
  <div class="container">
    <el-row
      type="flex"
      class="layout-raw"
      justify="center"
      style="margin-top: 20px"
    >
      <h1>Micro To Do</h1>
    </el-row>
    <el-row type="flex" class="layout-raw" justify="center">
      <div class="wrapper">
        <div class="typing-demo">Small steps Smart progress</div>
      </div>
    </el-row>
    <AddForm class="addform" />
    <TaskList />
  </div>
</template>

<script>
import AddForm from "./AddForm.vue";
import TaskList from "./TaskList.vue";
export default {
  components: {
    AddForm,
    TaskList,
  },
};
</script>

<style scoped>
.layout-raw {
  padding: auto, 0;
}

h1,
p {
  margin-top: 0px;
  margin-bottom: 10px;
  padding: auto, 0;
}

.addform {
  margin: 30px auto;
}

.typing-demo {
  width: 26ch;
  animation: typing 4s steps(26) 1, blink 0.5s step-end infinite alternate;
  white-space: nowrap;
  overflow: hidden;
  border-right: 2px solid;
  font-family: "Courier New", monospace;
  font-size: 1em;
}

@keyframes typing {
  0% {
    width: 0;
  }
  100% {
    width: 26ch;
  }
}

@keyframes blink {
  50% {
    border-color: transparent;
  }
}
</style>
