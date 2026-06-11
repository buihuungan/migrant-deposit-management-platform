<template>
  <div>
    <a-row :gutter="10">
      <a-col :md="leftColMd" :sm="24">
        <a-card title="订单查询" :bordered="false">
          <a-form layout="inline">
            <a-form-item label="标示编码名称">
              <a-input v-model="searchData.orderIdentity" placeholder="请输入标识编码" />
            </a-form-item>
            <a-form-item style="width: 10%">
              <a-button-group>
                <a-button type="primary" icon="search" @click="loadData(searchData)">查询</a-button>
                <a-button type="primary" icon="reload" @click="loadData()">重置</a-button>
              </a-button-group>
            </a-form-item>
          </a-form>
          <div style="margin-top: 3%">
            <div v-if="Object.keys(orderList).length != 0">
              <a-button type="primary" @click="searchOrder" style="margin-bottom: 10px">查看完整订单信息</a-button>
              <a-descriptions title="订单信息" :column="columnFlag" bordered>
                <a-descriptions-item v-for="data in orderList" :key="data.id" :label="data.name">
                  {{ data.value }}
                </a-descriptions-item>
              </a-descriptions>
            </div>
            <a-result v-else status="warning" title="暂无订单信息"> </a-result>
          </div>
        </a-card>
      </a-col>
      <a-col :md="rightColMd" :sm="24">
        <a-card :bordered="false">
          <img loading="lazy" src="../../assets/order.png" alt="" width="550" height="450" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { order_getAction } from '@/api/orderApi'
export default {
  name: 'productTraceability',
  data() {
    return {
      orderList: {},
      searchData: {},
      clickFlag: false,
    }
  },
  computed: {
    leftColMd() {
      return this.clickFlag ? 12 : 24
    },
    rightColMd() {
      return this.clickFlag ? 12 : 0
    },
    columnFlag() {
      return this.clickFlag ? 2 : 3
    },
  },
  methods: {
    loadData(params) {
      if (params) {
        order_getAction('/orderIdentity/search', params)
          .then((res) => {
            this.orderList = res.result.params[0]
            console.log('this.orderList', this.orderList)
          })
          .catch((err) => {
            console.log('err', err)
          })
      } else {
        this.orderList = []
        this.searchData = {}
        this.clickFlag = false
      }
    },
    searchOrder() {
      this.clickFlag = true
    },
  },
}
</script>

<style lang="less" scoped>
/deep/ .ant-form-item-control-wrapper {
  width: 250px;
}
</style>