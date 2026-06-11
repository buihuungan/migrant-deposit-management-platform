<template>
  <div>
    <a-card :bordered="false">
      <div>
        <a-form layout="inline">
          <a-form-item label="合同名称">
            <a-input placeholder="请输入名称" v-model="queryParam.username"></a-input>
          </a-form-item>
          <a-form-item style="width: 10%">
            <a-button-group>
              <a-button type="primary" icon="search" @click="loadData(searchData)">查询</a-button>
              <a-button type="primary" icon="reload" @click="loadData()">重置</a-button>
            </a-button-group>
          </a-form-item>
        </a-form>
      </div>
      <div>
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :loading="loading"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record, 'detail')">详情</a>
          </span>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
export default {
  name: 'salesOrderManagement',
  components: {},
  data() {
    return {
      queryParam: {},
      columns:[],
      dataSource:[],
      loading:false
    }
  },
  created() {},
  methods: {},
}
</script>

<style lang="less" scoped>
/deep/ .ant-form-item-control-wrapper {
  width: 250px;
}
</style>