<template>
  <div>
    <index-page v-if="indexStyle==1"></index-page>
    <index-bdc v-if="indexStyle==2"></index-bdc>
    <index-task v-if="indexStyle==3"></index-task>
    <!-- <div style="width: 100%;text-align: right;margin-top: 20px">
      请选择首页样式：
      <a-radio-group v-model="indexStyle">
        <a-radio :value="1">统计图表</a-radio>
        <a-radio :value="2">统计图表2</a-radio>
        <a-radio :value="3">任务表格</a-radio>
      </a-radio-group>
    </div> -->
  </div>
</template>

<script>
  import IndexChart from './IndexChart'
  import IndexTask from "./IndexTask"
  import IndexBdc from './IndexBdc'
  import IndexPage from './IndexPage'

  export default {
    name: "Analysis",
    components: {
      IndexPage,
      IndexBdc
    },
    data() {
      return {
        indexStyle:2
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>