<template>
  <j-select-biz-component :searchobj="searchobj"  :width="1000" v-bind="configs" v-on="$listeners"/>
</template>

<script>
  import JSelectBizComponent from './JSelectBizComponent'

  export default {
    name: 'JSelectIdentity',
    components: { JSelectBizComponent },
    props: ['value','url','searchobj'],
    data() {
      return {
        settings: {
          name: '标识编码',
          displayKey: 'identity',
          returnKeys: ['id'],
          listUrl: this.url,
          searchobj:this.searchobj,
          queryParamCode: 'identity',
          needobj:true,
          isonlineUrl:true,
          queryParamText: '标识编码',
          columns: [
            { title: '标识编码', dataIndex: 'identity', align: 'center', width: '35%' },
            { title: '标识信息', dataIndex: 'table_txt', align: 'center', width: '75%', widthRight: '70%' },
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