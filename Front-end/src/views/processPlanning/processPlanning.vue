<template>
  <div>
    <a-card :bordered="false">
      <div>
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">详情</a>
            <a-divider type="vertical" />
            <a @click="handleCustom(record)">需求定制</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <designModal ref="modalForm" @close-annTask="closeAnnTask" :flowId="flowId"></designModal>
    <customModal ref="customForm" @close-annTask="closeAnnTask" @loadData="loadData"></customModal>
  </div>
</template>

<script>
import designModal from './modules/designModal'
import customModal from './modules/customModal'
import { getAction, postAction, deleteAction } from '@/api/manage'

export default {
  name: 'salesOrderManagement',
  components: { designModal, customModal },
  data() {
    return {  
      selectedRowKeys: [],
      flowId: '919e12ea043f412eb25ae8ef75b820b2',
      loading: true,
      columns: [
        { align: 'center', dataIndex: 'process_id', title: '流程编号' },
        { align: 'center', dataIndex: 'order_director', title: '申请人' },
        { align: 'center', dataIndex: 'seller_name', title: '售达方名称' },
        { align: 'center', dataIndex: 'order_date', title: '订单日期' },
        {
          align: 'center',
          dataIndex: 'request_date',
          title: '请求交货日期',
        },
        { title: '操作', key: 'action', align: 'center', scopedSlots: { customRender: 'action' } },
      ],
      dataSource: [],
      designUrl: {
        getData: '/online/cgform/api/getData/e8c9f23b81d74fc6a292c436d27faa70',
      },
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
      let queryParams = {
        column: 'createTime',
        order: 'desc',
        pageNo: this.ipagination.current,
        pageSize: 10,
        superQueryMatchType: 'and',
      }
      queryParams.superQueryParams = escape(
        JSON.stringify([
          { rule: 'eq', type: 'text', val: '1', field: 'is_custormize' },
          { rule: 'eq', type: 'text', val: '0', field: 'custormize_flag' },
        ])
      )
      getAction(this.designUrl.getData, queryParams).then((res) => {
        this.dataSource = res.result.records
        this.ipagination.total = res.result.total
        this.loading = false
      })
    },

    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    closeAnnTask() {
      this.loadData()
    },
    handleEdit(record) {
      this.$refs.modalForm.getForm(record)
    },
    handleCustom(record) {
      console.log(record);
      this.$refs.customForm.openModal(record)
    },
  },
}
</script>

<style scoped>
</style>