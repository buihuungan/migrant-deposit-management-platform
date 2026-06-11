<template>
  <j-select-biz-component :searchobj="searchobj" :width="1000" v-bind="configs" v-on="$listeners"/>
</template>

<script>
  import JSelectBizComponent from './JSelectBizComponent'

  export default {
    name: 'JSelectWare',
    components: { JSelectBizComponent },
    props: ['value','url','type','searchobj','ischildtable'],
    data() {
      return {
        settings: {
          name: '仿真课件',
          displayKey: 'ware_name',
          returnKeys: ['id', 'id'],
          listUrl: this.url,
          ctype: this.type,
          searchobj:this.searchobj,
          queryParamCode: 'ware_name',
          needobj:true,
          isonlineUrl:true,
          ischildtable:this.ischildtable,
          queryParamText: '仿真课件名称',
          columns: [
            { title: '仿真课件名称', dataIndex: 'ware_name', align: 'center', width: '100%', widthRight: '70%' },
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