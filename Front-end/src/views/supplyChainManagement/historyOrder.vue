<template>
  <div>
    <a-card :bordered="false">
      <div style="margin-bottom: 20px">
        搜索订单：<a-input v-model="orderName" style="width: 200px"></a-input>
        <a-button type="primary" @click="loadData" style="margin-left: 30px">查询</a-button>
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
          <span slot="action" slot-scope="text, record">
            <a @click="getDetail(record)">详情</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <a-modal
      ref="faultHandlingRef"
      title="订单详情"
      :visible="visible"
      okText="确定"
      width="80%"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-row style="margin-bottom: 10px">
        <a-col :span="8">
          <a-space :size="39"><span></span><span></span></a-space>
          订单编号：<a-input disabled style="width: 50%" v-model="record.processId"></a-input>
        </a-col>
        <a-col :span="8">
          <a-space :size="39"><span></span><span></span></a-space>
          订单日期：<a-input disabled style="width: 50%" v-model="record.orderDate"></a-input>
        </a-col>
        <a-col :span="8">
          <a-space :size="24"><span></span><span></span></a-space>
          申请人：<a-input disabled style="width: 50%" v-model="record.orderDirector"></a-input
        ></a-col>
      </a-row>
      <a-row style="margin-bottom: 10px">
        <a-col :span="8">
          申请人联系方式：<a-input disabled style="width: 50%" v-model="record.promoterPhone"></a-input
        ></a-col>
        <a-col :span="8">
          <a-space :size="25"><span></span><span></span></a-space>
          售达方名称：<a-input disabled style="width: 50%" v-model="record.sellerName"></a-input>
        </a-col>
        <a-col :span="8">
          售达方地址：<a-input disabled style="width: 50%" v-model="record.orderDirector"></a-input
        ></a-col>
      </a-row>
      <a-row style="margin-bottom: 10px">
        <a-col :span="8">
          <a-space :size="11"><span></span><span></span></a-space>
          请求交货时间：<a-input disabled style="width: 50%" v-model="record.requestDate"></a-input>
        </a-col>
        <a-col :span="8">
          <a-space :size="11"><span></span><span></span></a-space>
          计划交货时间：<a-input disabled style="width: 50%" v-model="record.planDate"></a-input>
        </a-col>
        <a-col :span="8">
          客户订单号：<a-input disabled style="width: 50%" v-model="record.accountOrder"></a-input
        ></a-col>
      </a-row>
      <a-row style="margin-bottom: 20px">
        <a-col :span="8">
          <a-space :size="25"><span></span><span></span></a-space>
          收货人姓名：<a-input disabled style="width: 50%" v-model="record.receiverName"></a-input>
        </a-col>
        <a-col :span="8">
          收货人联系方式：<a-input disabled style="width: 50%" v-model="record.receiverPhone"></a-input
        ></a-col>
      </a-row>
      <div>
        <a-table ref="table1" size="middle" rowKey="id1" :columns="columns1" :dataSource="record.orderDetailList">
        </a-table>
      </div>
    </a-modal>
  </div>
</template>

<script>
import axios from 'axios'
import { p_postAction } from '@/api/plmApi'
export default {
  data() {
    return {
      record: {},
      orderName: '',
      fault_handling: '',
      visible: false,
      dataSource: [],
      columns: [
        { align: 'center', dataIndex: 'processId', title: '订单编号' },
        { align: 'center', dataIndex: 'orderDetailList[0].prodName', title: '物料描述' },
        { align: 'center', dataIndex: 'orderDetailList[0].prodAmount', title: '数量' },
        { align: 'center', dataIndex: 'orderDate', title: '订单日期' },
        { align: 'center', dataIndex: 'orderDirector', title: '申请人' },
        { align: 'center', dataIndex: 'receiverName', title: '售达方名称' },
        { title: '详情', key: 'action', align: 'center', scopedSlots: { customRender: 'action' } }
      ],
      columns1: [
        { align: 'center', dataIndex: 'prodId', title: '物料号' },
        { align: 'center', dataIndex: 'prodName', title: '物料描述' },
        { align: 'center', dataIndex: 'prodAmount', title: '数量' },
        { align: 'center', dataIndex: 'prodUnit', title: '单位' },
        { align: 'center', dataIndex: 'remark', title: '备注' },
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
      id: '',
      id1: ''
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      let params = {
        pageSize: this.ipagination.pageSize,
          pageNo: this.ipagination.current,
          processId: this.orderName
      }
      p_postAction('/cictOrder/Query', params).then(res => {
        this.dataSource = res.result.records
        this.ipagination.total = res.result.total
      })
    },
    getDetail(record) {
      this.record = record
      this.visible = true
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
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
