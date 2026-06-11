<template>
  <div>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="6">
              <a-form-item label="零部件标识编码：" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
                <a-input placeholder="请输入关键字搜索" v-model="searchIdentity"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item>
                <a-button type="primary" @click="searchQuery">查询</a-button>
                <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <div>
        <a-table
          ref="table"
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
        >
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import axios from 'axios'
import { order_getAction } from '@/api/orderApi'
export default {
  data() {
    return {
      searchIdentity: '',
      dataSource: [
      ],
      columns: [
        { align: 'center', dataIndex: 'identity', title: '标识编码' },
        { align: 'center', dataIndex: 'name', title: '名称' },
        { align: 'center', dataIndex: 'sum', title: '库存' },
      ],
      ipagination: {
        current: 1,
        pageSize: 10,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
    }
  },
  created() { 
    this.loadData()
  },
  methods: {
    loadData() {
      let params = { identity: this.searchIdentity}
      order_getAction('/supplyChainInventory/getTotal' , params).then((res) =>{
          console.log('res',res)
          this.dataSource = res.result
        })
      // order_getAction('/supplyChainInventory/getTotal').then((res) =>{
      //     console.log('res',res)
      //     this.dataSource = res.result
      //   })
    },
    searchReset() { 
      this.searchIdentity = ''
      this.loadData()
     },
    searchQuery() { 
      console.log('searchReset',this.searchIdentity)

      let params = { identity: this.searchIdentity}
      console.log('params',params)
      order_getAction('/supplyChainInventory/getTotal' , params).then((res) =>{
          console.log('res',res)
          this.dataSource = res.result
        })
     },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
  },
}
</script>

<style scoped>
</style>