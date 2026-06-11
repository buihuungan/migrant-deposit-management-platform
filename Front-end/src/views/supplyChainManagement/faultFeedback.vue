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
            <a @click="handleEdit(record, 'detail')">详情</a>
            <a-divider type="vertical" />
            <!-- <a @click="faultDescription(record)">故障描述</a> -->
            <a @click="faultDescription(record)" v-if="arr.indexOf(record.order_name) === -1">故障描述</a>
            <a disabled v-else>已反馈</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <salesModal ref="modalForm" @close-annTask="closeAnnTask" :flowId="flowId"></salesModal>
    <a-modal
      ref="faultDescriptionRef"
      title="故障描述"
      :visible="visible"
      okText="确定"
      cancelText="取消"
      width="40%"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <template>
        <a-textarea placeholder="请输入故障描述" v-model="fault_description" :rows="4" />
      </template>
    </a-modal>
  </div>
</template>

<script>
import salesModal from '../productionManagement/modules/salesModal'
import { getAction, postAction, deleteAction } from '@/api/manage'
import { USER_INFO } from '@/store/mutation-types'
import vue from 'vue'
import { order_postAction, order_getAction, order_putAction } from '@/api/orderApi'
import AddModal from '../processPlanning/modules/addModal.vue'
import { axios } from '../../utils/request'
import { erp_postAction } from '@/api/erpApi.js'
export default {
  components: {
    salesModal
  },
  name: 'shop',
  data() {
    return {
      arr: [],
      order_name: '',
      create_name: '',
      fault_description: '',
      visible: false,
      selectedRowKeys: [],
      flowId: '919e12ea043f412eb25ae8ef75b820b2',
      loading: true,
      columns: [
        { align: 'center', dataIndex: 'process_id', title: '订单编号' },
        { align: 'center', dataIndex: 'order_director', title: '申请人' },
        { align: 'center', dataIndex: 'delivery_party_addr', title: '售达方名称' },
        { align: 'center', dataIndex: 'request_date', title: '订单日期' },
        {
          align: 'center',
          dataIndex: 'order_state',
          title: '订单状态',
          customRender: (text, record, index) => {
            if (text === 0) {
              return '已完成'
            } else if (text === 1) {
              return '未完成'
            } else {
              return '已发货'
            }
          }
        },
        { title: '操作', key: 'action', align: 'center', scopedSlots: { customRender: 'action' } }
      ],
      dataSource: [],
      salesOrderUrl: {
        getColumns: '/online/cgform/api/getColumns/e8c9f23b81d74fc6a292c436d27faa70', // 表名：cict_order   表描述：订单信息
        getData: '/online/cgform/api/getData/e8c9f23b81d74fc6a292c436d27faa70',
        delete: '/online/cgform/api/form/e8c9f23b81d74fc6a292c436d27faa70'
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
        total: 0
      }
    }
  },
  created() {
    this.loadData()
    this.getArr()
  },
  methods: {
    getArr() {
      let params = {
        pageSize: 100,
        pageNum: 1
      }
      erp_postAction('/faultHandling/selectFaultHandling', params).then(res => {
        var arr1 = res.result.records
        this.arr=
          arr1.map(x => {
            return x.orderIdentity
          }
        )
        console.log('this.arr', this.arr)
      })
    },
    //:row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
    loadData() {
      let params = {
        column: 'createTime',
        order: 'desc',
        pageNo: this.ipagination.current,
        pageSize: 443,
        superQueryMatchType: 'and'
      }
      let superQueryParams = []
      if (vue.ls.get(USER_INFO).username != 'admin') {
        superQueryParams.push({ rule: 'eq', type: 'text', val: vue.ls.get(USER_INFO).username, field: 'create_by' })
      }
      superQueryParams.push({ rule: 'eq', type: 'number', val: 1, field: 'audit_status' })
      params.superQueryParams = encodeURI(JSON.stringify(superQueryParams))
      getAction(this.salesOrderUrl.getData, params).then(res => {
        console.log('22222222', res)

        this.dataSource = res.result.records
        this.dataSource = this.dataSource.filter(x => {
          return x.order_state === 0
        })
        this.ipagination.total = this.dataSource.length
        console.log('this.dataSouece', this.dataSource)
        this.loading = false
      })
    },
    newlyBuild() {
      this.$refs.modalForm.getForm({})
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    onClearSelected() {
      this.selectedRowKeys = []
    },

    onPageChange(page, pageSize) {
      this.ipagination.current = page
      // this.loadData()
    },
    closeAnnTask() {
      this.loadData()
    },
    handleEdit(record, message) {
      this.$refs.modalForm.getForm(record, message)
    },
    faultDescription(record) {
      console.log('122222123213123', record)
      this.order_name = record.order_name
      this.create_name = record.order_director
      this.visible = true
    },
    confirm(record) {
      this.handleDelete(record)
    },
    cancel() {
      this.$message.error('已取消')
    },
    handleDelete(record) {
      deleteAction(this.salesOrderUrl.delete + '/' + record.id)
        .then(res => {
          this.$message.success('删除成功')
          this.loadData()
        })
        .catch(err => {
          console.log('err', err)
        })
    },
    handleOk() {
      let params = {
        orderIdentity: this.order_name,
        faultDescription: this.fault_description,
        createBy: this.create_name,
        faultState: '0'
      }
      erp_postAction('/faultHandling/insertFaultHandling', params).then(res => {
        if (res.code === 200) {
          this.$message.success('反馈成功')
          this.visible = false
          this.getArr()
        }
      })
    },

    handleCancel() {
      this.visible = false
    }
  }
}
</script>

<style scoped>
.ant-btn {
  line-height: 1.499;
  position: relative;
  display: inline-block;
  font-weight: 400;
  white-space: nowrap;
  text-align: center;
  background-image: none;
  border: 1px solid transparent;
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.015);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -ms-touch-action: manipulation;
  touch-action: manipulation;
  height: 32px;
  padding: 0 15px;
  font-size: 14px;
  border-radius: 4px;
  color: rgba(0, 0, 0, 0.65);
  background-color: #fff;
  border-color: #d9d9d9;
}
.ant-btn-primary {
  color: #fff;
  background-color: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.12);
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.045);
}
</style>
