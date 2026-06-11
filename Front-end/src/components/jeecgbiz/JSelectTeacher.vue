<template>
  <j-select-biz-component :searchobj="searchobj"  :width="1000" v-bind="configs" v-on="$listeners"/>
</template>

<script>
  import JSelectBizComponent from './JSelectBizComponent'

  export default {
    name: 'JSelectTeacher',
    components: { JSelectBizComponent },
    props: ['value','url','type','searchobj','ischildtable'],
    data() {
      return {
        settings: {
          name: '教师',
          displayKey: 'teacher_name',
          returnKeys: this.type == 'teacher_lesson' ? ['id', 'teacher_id'] :['id', 'uid'],
          listUrl: this.url,
          ctype: this.type,
          searchobj:this.searchobj,
          queryParamCode: this.type == 'teacher_lesson' ?'teacher_work_no':'work_no',
          needobj:true,
          ischildtable:this.ischildtable,
          isonlineUrl:true,
          queryParamText: '教师工号',
          columns: [
            { title: '教师工号', dataIndex: this.type == 'teacher_lesson' ?'teacher_work_no':'work_no', align: 'center', width: '35%' },
            { title: '教师名', dataIndex: 'teacher_name', align: 'center', width: '75%', widthRight: '70%' },
          ]
        }
      }
    },
    computed: {
      configs() {
        return Object.assign({ value: this.value }, this.settings, this.$attrs)
      }
    },
    watch:{
    },
    methods:{
    }
  }
</script>

<style lang="less" scoped></style>