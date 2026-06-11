<template>
  <j-select-biz-component :value="value"  :width="1000" v-bind="configs" v-on="$listeners"/>
</template>

<script>
  import JSelectBizComponent from './JSelectBizComponent'

  export default {
    name: 'JSelectLesson',
    components: { JSelectBizComponent },
    props: ['value','url','type'],
    data() {
      return {
        settings: {
          name: '课程',
          displayKey: 'lesson_name',
          returnKeys: ['id', 'id'],
          listUrl: this.url,
          ctype: this.type,
          queryParamCode: 'lesson_name',
          needobj:true,
          isonlineUrl:true,
          queryParamText: '课程名称',
          columns: [
            { title: '课程名称', dataIndex: 'lesson_name', align: 'center', width: '100%', widthRight: '70%' },
          ]
        }
      }
    },
    computed: {
      configs() {
        return Object.assign({ value: this.value }, this.settings, this.$attrs)
      }
    },
    methods:{
    }
  }
</script>

<style lang="less" scoped></style>