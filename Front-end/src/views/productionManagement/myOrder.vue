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
            <a v-if="record.order_state ===6" disabled>已收货</a>
            <a v-else @click="receipt(record)" >收货</a>
            
            <!-- <a @click="shop(record, 'shop')" v-if="record.order_state">发货</a>
            <a v-else disabled>已发货</a> -->
          </span>
        </a-table>
      </div>
    </a-card>

    <salesModal ref="modalForm" @close-annTask="closeAnnTask" :flowId="flowId"></salesModal>
  </div>
</template>

<script>
import salesModal from './modules/salesModal'
import { getAction, postAction, deleteAction } from '@/api/manage'
import { USER_INFO } from '@/store/mutation-types'
import vue from 'vue'
import { order_postAction, order_getAction, order_putAction } from '@/api/orderApi'
import { o_getAction, o_putAction, o_postAction } from '@api/onApi'
export default {
  name: 'shop',
  components: { salesModal },
  data() {
    return {
      orderInfoId: 'e8c9f23b81d74fc6a292c436d27faa70', //订单信息表id
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
          },
        },
        { title: '操作', key: 'action', align: 'center', scopedSlots: { customRender: 'action' } },
      ],
      dataSource: [],
      salesOrderUrl: {
        getColumns: '/online/cgform/api/getColumns/e8c9f23b81d74fc6a292c436d27faa70', // 表名：cict_order   表描述：订单信息
        getData: '/online/cgform/api/getData/e8c9f23b81d74fc6a292c436d27faa70',
        delete: '/online/cgform/api/form/e8c9f23b81d74fc6a292c436d27faa70',
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
    //:row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
    loadData() {
      // let params = {
      //   column: 'createTime',
      //   order: 'desc',
      //   pageNo: this.ipagination.current,
      //   pageSize: 10,
      //   superQueryMatchType: 'and',
      // }
      // let superQueryParams = []
      // if (vue.ls.get(USER_INFO).username != 'admin') {
      //   superQueryParams.push({ rule: 'eq', type: 'text', val: vue.ls.get(USER_INFO).username, field: 'create_by' })
      // }
      // superQueryParams.push({ rule: 'eq', type: 'number', val: 1, field: 'audit_status' })
      // params.superQueryParams = encodeURI(JSON.stringify(superQueryParams))
      // getAction(this.salesOrderUrl.getData, params).then((res) => {
      //   this.ipagination.total = res.result.total
      //   this.dataSource = res.result.records
      //   this.loading = false
      // })
      let params = {
        pageNum: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        userId: '',
        exampleId : ''
      }
      console.log('vue.ls.get(USER_INFO)',vue.ls.get(USER_INFO))
      params.userId =  vue.ls.get(USER_INFO).id
      params.exampleId = this.$store.state.exampleId
      order_postAction('/cictOrder/getData',params).then( res => {
        this.dataSource = []
        console.log('res',res)
        res.result.records.forEach((item)=> {
          var newObj = new Object()
          for (let key in item) {
            newObj[key.replace(/([A-Z])/g, "_$1").toLowerCase()] = item[key]
          }
          this.dataSource.push(newObj)
        })
        this.ipagination.total = res.result.total
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
      this.loadData()
    },
    closeAnnTask() {
      this.loadData()
    },
    handleEdit(record, message) {
      this.$refs.modalForm.getForm(record, message)
    },
    receipt(record) {
      console.log(record)
      console.log('order_state',record.order_state)
      if( record.order_state == 1 || record.order_state == 0){
        return this.$message.error('当前订单未送检，请前往发货列表进行送检')
      }else if( record.order_state == 2){
        return this.$message.error('当前订单未检测，请前往质量检测进行检测')
      }else if( record.order_state == 3){
        return this.$message.error('当前订单检测不合格，请前往质量检测进行确认')
      }else if( record.order_state == 4){
        return this.$message.error('当前订单未发货，请前往发货列表进行发货')
      }
      this.$elementConfirm('确认收货?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          order_putAction(`/order/finishOrder?orderId=` + record.id)
            .then((res) => {
              if (res.code === 200) {
                var params1= {}
                params1.id = record.id
                params1.create_by = record.create_by
                params1.create_time = record.create_time
                params1.update_by = record.update_by
                params1.update_time = record.update_time
                params1.sys_org_code = record.sys_org_code
                params1.process_id = record.process_id
                params1.order_director = record.order_director
                params1.rec_type = record.rec_type
                params1.order_date = record.order_date
                params1.promoter_phone = record.promoter_phone
                params1.seller_id = record.seller_id
                params1.seller_name = record.seller_name
                params1.delivery_party_name = record.delivery_party_name
                params1.delivery_party_addr = record.delivery_party_addr
                params1.request_date = record.request_date
                params1.plan_date = record.plan_date
                params1.account_order = record.account_order
                params1.receiver_name = record.receiver_name
                params1.receiver_phone = record.receiver_phone
                params1.remark = record.remark
                params1.audit_status = record.audit_status
                params1.is_custormize = record.is_custormize
                params1.custormize_basic = record.custormize_basic
                params1.custormize_color = record.custormize_color
                params1.custormize_shape = record.custormize_shape
                params1.custormize_power = record.custormize_power
                params1.custormize_amount = record.custormize_amount
                params1.custormize_flag = record.custormize_flag
                params1.order_name = record.order_name
                params1.example_id = record.example_id
                params1.order_state = 6
                o_putAction(`/cgform/api/form/` + this.orderInfoId , params1)
                  .then( (res) => {
                    console.log('orderpost接口',res)
                    this.loadData()
                  })
                this.$message.success('收货成功')
              }
            })
            .catch((error) => {
              console.log(error)
            })
        })
        .catch(() => {})
    },
    confirm(record) {
      this.handleDelete(record)
    },
    cancel() {
      this.$message.error('已取消')
    },
    handleDelete(record) {
      deleteAction(this.salesOrderUrl.delete + '/' + record.id)
        .then((res) => {
          this.$message.success('删除成功')
          this.loadData()
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
  },
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