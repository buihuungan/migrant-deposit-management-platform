<template>
  <a-card>
    <div class="mxgesign">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        childrenColumnName="child"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleAdd(record)">课程设计</a>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <easyflow ref="panel" :org_code="org_code"></easyflow>
  </a-card>
</template>
<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { queryMyDepartTreeList, searchByKeywords } from '@/api/api'
// import MxgraphDesign from './MxgraphDesign'
import easyflow from '../easyflow/ef/panel'
export default {
  name: 'DeptUserInfo',
  mixins: [JeecgListMixin],
  components: {
    // MxgraphDesign,
    easyflow,
  },
  data() {
    return {
      confirmLoading: false,
      // 表头
      columns: [
        {
          title: '机构编码',
          align: 'center',
          dataIndex: 'orgCode',
        },
        {
          title: '机构名称',
          align: 'center',
          dataIndex: 'departName',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 150,
        },
      ],
      record: {},
      org_code: '',
    }
  },
  created() { },
  watch: {
    departIdList: function (value1, value2) {
      this.loadData()
    },
  },
  methods: {
    loadData(arg, isSearch = false) {
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      this.dataSource = []
      this.ipagination.total = 0

      queryMyDepartTreeList().then((res) => {
        if (res.success && res.result) {
          this.dataSource = res.result
          this.ipagination.total = this.dataSource.length
        }
      })
    },
    handleAdd: function (record) {
      this.org_code = record.id
      this.$refs.panel.openModel()
    },
  },
}
</script>
<style scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 3px;
}

/* .ant-card {
    margin-left: -30px;
    margin-right: -30px;
  } */

.table-page-search-wrapper {
  margin-top: -16px;
  margin-bottom: 16px;
}
</style>