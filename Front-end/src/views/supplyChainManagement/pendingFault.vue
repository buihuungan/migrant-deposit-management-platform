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
        >
          <span slot="action" slot-scope="text, record">
            <a @click="faultHandling(record)">故障处理</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <a-modal
      ref="faultHandlingRef"
      title="故障处理意见"
      :visible="visible"
      okText="确定"
      cancelText="取消"
      width="40%"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <template>
        <a-textarea placeholder="请输入故障处理意见" v-model="fault_handling" :rows="4" />
      </template>
    </a-modal>
  </div>
</template>

<script>
import axios from 'axios'
import { erp_postAction } from '@/api/erpApi.js'
export default {
  data() {
    return {
      fault_handling: '',
      visible: false,
      loading: true,
      dataSource: [],
      columns: [
        { align: 'center', dataIndex: 'orderIdentity', title: '订单编号' },
        { align: 'center', dataIndex: 'createBy', title: '创建人' },
        { align: 'center', dataIndex: 'createTime', title: '创建时间' },
        { align: 'center', dataIndex: 'faultDescription', title: '故障描述' },
        {
          align: 'center',
          dataIndex: 'faultState',
          title: '售后状态',
          customRender: (text, record, index) => {
            if (text == 0) {
              return '待处理'
            } else if (text == 1) {
              return '已处理'
            } else if (text == 2) {
              return '未解决'
            } else if (text == 3) {
              return '已解决'
            }
          }
        },
        { title: '操作', key: 'action', align: 'center', scopedSlots: { customRender: 'action' } }
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
        total: 0
      },
      id: ''
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      let params = {
        pageSize: this.ipagination.pageSize,
        pageNum: this.ipagination.current,
        faultState: '0'
      }
      erp_postAction('/faultHandling/selectFaultHandlingByState', params).then(res => {
        console.log('22222222222222', res)
        this.dataSource = res.result.records
        this.ipagination.total = res.result.total
      })
    },
    faultHandling(record) {
      this.id = record.id
      this.visible = true
    },
    handleOk() {
      let params = {
        id: this.id,
        handlingOption: this.fault_handling,
        faultState: '1'
      }
      erp_postAction('/faultHandling/updateFaultHandling', params).then(res => {
        console.log('res111111111', res)
        if (res.code == 200) {
          this.$message.success('故障处理提交成功')
        } else {
          this.$message.error('故障处理提交失败')
        }
        this.visible = false
        this.loadData()
      })
    },
    handleCancel() {
      this.visible = false
    },
    onPageChange() {}
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
