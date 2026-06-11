<template>
  <div>
    <a-row :gutter="10">
      <a-col :md="leftColMd" :sm="24">
        <a-card title="产品追溯" :bordered="false">
          <a-form layout="inline">
            <a-form-item label="标识编码名称">
              <a-input v-model="searchData.identity" placeholder="请输入标识编码" />
            </a-form-item>
            <a-form-item style="width: 10%">
              <a-button-group>
                <a-button type="primary" icon="search" @click="loadData(searchData)">查询</a-button>
                <a-button type="primary" icon="reload" @click="loadData()">重置</a-button>
              </a-button-group>
            </a-form-item>
          </a-form>
          <div style="margin-top: 3%">
            <div v-if="timeList.length > 0">
              <a-button type="primary" @click="searchProduct" style="margin-bottom: 10px"
                >查看完整产品追溯信息</a-button
              >
              <timeline :timeline-list="timeList" type="product"></timeline>
            </div>
            <a-result v-else status="warning" title="暂无产品信息"> </a-result>
          </div>
        </a-card>
      </a-col>
      <a-col :md="rightColMd" :sm="24">
        <a-card :bordered="false">
          <img loading="lazy" src="../../assets/product.png" alt="" width="570" height="500" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import Timeline from '@/components/Timeline/Timeline'
import { order_getAction } from '@/api/orderApi'
export default {
  name: 'productTraceability',
  components: {
    Timeline,
  },
  data() {
    return {
      timeList: [],
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
  },
  methods: {
    loadData(params) {
      if (params) {
        order_getAction('/productTraceability/SearchProductTraceability', params)
          .then((res) => {
            this.timeList = res.result
          })
          .catch((err) => {
            console.log('err', err)
          })
      } else {
        this.timeList = []
        this.searchData = {}
        this.clickFlag = false
      }
    },
    searchProduct() {
      this.clickFlag = true
    },
  },
}
</script>

<style lang="scss" scoped>
.ant-form-item-control-wrapper {
  width: 250px;
}
</style>