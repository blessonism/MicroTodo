<template>
  <div>
    <!-- 职能下拉菜单 -->
    <el-select
      v-model="funcMode"
      placeholder="AI Tools"
      class="func-select"
      style="color: white"
    >
      <el-option label="Split Tasks" value="split"></el-option>
      <el-option label="Ask Question" value="ask"></el-option>
      <el-option label="Get Answers" value="ans"></el-option>
      <el-option label="SWOT Analysis" value="swot"></el-option>
    </el-select>

    <!--  -->
  </div>
</template>

<script>
export default {
  name: "DemoComponent",
  data(){
    return {
        funcMode: "",
    }
  }
};
</script>

<style></style>
