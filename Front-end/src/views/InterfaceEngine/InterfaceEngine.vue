<template>
 <a-card :bordered="false">
    <a-table :columns="columns" :dataSource="dataSource">
      <span slot="action" slot-scope="text, record">
        <a :href="calRoute(record)" target="_blank">查看</a>
      </span>
    </a-table>
  </a-card>
</template>
<script>
import axios from 'axios'
import { toLower } from 'lodash'
import api from '@/api/index'

export default {
  name: 'InterfaceEngine',
  components: {

  },
  data() {
    return {
      dataSource:[],
      columns: [
        {
          title: '接口名',
          align: 'center',
          dataIndex: 'appName',
        },
        {
          title: '实例ID',
          align: 'center',
          dataIndex: 'instanceId',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
    }
  },
  methods: {
    getData(){
      let that=this
      axios({
        baseURL: '/ada-api',
        url: '/ada/list',
        methods: 'GET',
      }).then(res => {
        that.dataSource =res.data.data
        console.log(that.dataSource)
      })
    },
    setData(){
      let data={"code":200,"msg":"操作成功","data":[{"appName":"ERP","instanceId":"erp:172.20.157.48:38072"},{"appName":"MES","instanceId":"mes:172.20.157.23:18462"},{"appName":"WMS","instanceId":"wms:172.20.115.57:27133"},{"appName":"CRM","instanceId":"crm:172.20.124.246:44399"},{"appName":"FAKENODE","instanceId":"fakenode:fakenode:38091"},{"appName":"CMS","instanceId":"cms:cms:8888"},{"appName":"PLM","instanceId":"plm:172.20.115.123:37039"},{"appName":"IDENTITY","instanceId":"identity:identity:38091"},{"appName":"COURSEWAREENGINE","instanceId":"coursewareEngine:172.20.64.87:37192"},{"appName":"ORDER","instanceId":"order:172.20.64.77:41684"}]}
      this.dataSource=data.data;
    },
    calRoute(record){
      return api.server_url + "/" +toLower(record.appName)+"/doc.html#/home"
    }
  },
  created() {
    //this.getData()
    this.setData()    
  },
}
</script>
<style lang="less" scoped>

</style>
