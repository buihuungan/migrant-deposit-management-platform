<template>
  <div>
    <a-card :bordered="false">
      <!-- <div style="margin: 0 8px 8px 0">
        <a-button class="ant-btn ant-btn-primary" type="primary" @click="newlyBuild"
          ><a-icon type="plus" />新建</a-button
        >
      </div> -->
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
import { order_postAction } from '@/api/orderApi'

export default {
  name: 'salesOrderManagement',
  components: { salesModal },
  data() {
    return {
      selectedRowKeys: [],
      flowId: '919e12ea043f412eb25ae8ef75b820b2',
      loading: true,
      columns: [
        { align: 'center', dataIndex: 'process_id', title: '流程编号' },
        { align: 'center', dataIndex: 'order_director', title: '申请人' },
        { align: 'center', dataIndex: 'delivery_party_addr', title: '售达方名称' },
        { align: 'center', dataIndex: 'order_date', title: '订单日期' },
        {
          align: 'center',
          dataIndex: 'is_custormize',
          title: '是否定制',
          customRender: (text, record, index) => {
            if (text === 1) {
              return '是'
            } else {
              return '否'
            }
          },
        },
        { title: '操作', key: 'action', align: 'center', scopedSlots: { customRender: 'action' } },
      ],
      dataSource: [],
      salesOrderUrl: {
        getColumns: '/online/cgform/api/getColumns/e8c9f23b81d74fc6a292c436d27faa70',// 表名：cict_order   表描述：订单信息
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
        res.result.records.forEach((item)=> {
          var newObj = new Object()
          for(let key in item) {
            newObj[key.replace(/([A-Z])/g,"_$1").toLowerCase()] = item[key]
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