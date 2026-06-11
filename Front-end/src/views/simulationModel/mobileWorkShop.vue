<template>
  <div class="mobile_work_shop">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="4G车间三维模型" name="first" @click="openModal()">
        <!-- <iframe id="content" src="//111.48.26.196/4GWebGL/ " scrolling="no" style="width:100%;height:900px"></iframe> -->
      </el-tab-pane>
      <el-tab-pane label="5G车间三维模型" name="second">
        <!-- <iframe id="content" src="http://111.48.26.196/5GWebGL/" scrolling="no" style="width:100%;height:1000px"></iframe> -->
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeName: 'first',
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
      if(this.activeName == 'first') {
        window.open("https://ptc.hxct.com/4GWebGL/")
      }
      else if(this.activeName == 'second') {
        window.open("https://ptc.hxct.com/5GWebGL/")
      }
    },
  }
}
</script>

<style scoped>
.mobile_work_shop {
  background: #fff;
  padding: 24px;
}
</style>