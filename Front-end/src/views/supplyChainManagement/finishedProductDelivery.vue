<template>
  <div>
    <a-card :bordered="false">
      <div>
        <div class="table-operator" style="margin: 5px 0 10px 2px">
          <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">新增</a-button>
        </div>
        <a-table
          ref="table"
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
        >
          <span slot="action" slot-scope="text, record">
            <!-- <a @click="handleEdit(record, 'detail')">详情</a>
            <a-divider type="vertical" /> -->
            <a :disabled="record.logisticsCode != null" @click="chooseLogisticsProvider(record)">选择物流商</a>
            <a-divider type="vertical" />
            <a @click="shop(record)" v-if="record.orderState == '0'">发货</a>
            <a v-else disabled>已发货</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <a-modal
      title="选择供应商"
      :visible="visible"
      okText="确定"
      cancelText="取消"
      width="80%"
      :destroyOnClose="true"
      @ok="handleOk()"
      @cancel="handleCancel"
    >
      <a-table
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
        style="height: 300px"
        ref="table"
        size="middle"
        :columns="columns1"
        :dataSource="dataSource1"
      >
      </a-table>
    </a-modal>
    <a-modal
      title="新增发货单"
      :visible="addVisible"
      okText="确定"
      cancelText="取消"
      width="30%"
      :destroyOnClose="true"
      @ok="handleOk2()"
      @cancel="handleCancel2"
    >
      <a-form :form="invoiceForm" :label-col="{ span: 6 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="发货方">
          <a-select v-model="invoiceForm.shipper">
            <a-select-option v-for="(item, index) in shipperList" :key="index" :value="item">{{
              item
            }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="收货方">
          <a-select v-model="invoiceForm.consignee">
            <a-select-option v-for="(item, index) in consigneeList" :key="index" :value="item">{{
              item
            }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="产品名称">
          <a-select v-model="invoiceForm.name">
            <a-select-option v-for="(item, index) in productList" :key="index" :value="item">{{
              item
            }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="产品标识">
          <a-select v-model="invoiceForm.identity">
            <a-select-option v-for="(item, index) in productIdentityList" :key="index" :value="item">{{
              item
            }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="发货日期">
          <a-date-picker v-model="invoiceForm.date" style="width: 100%" />
        </a-form-item>
        <a-form-item label="数量">
          <a-input v-model="invoiceForm.number" placeholder="请输入发货数量" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { tm_postAction } from '@/api/templateApi'
import { erp_getAction, erp_postAction } from '@/api/erpApi'
import { putAction } from '@api/manage'
import vue from 'vue'
import { Message } from 'element-ui'
import axios from 'axios'
export default {
  data() {
    return {
      addVisible: false,
      logistics_code: '',
      selectedLogHead: '', //选中行物流商头部
      logHead: '', //确定的物流商头部
      selectedRowKeys: [],
      visible: false,
      shipperList: ['虹信', '武科大'],
      consigneeList: ['中国移动', '中国电信', '中国联通'],
      productList: ['基站天线'],
      productIdentityList: ['O30050150208B3'],
      invoiceForm: {
        shipper: '',
        consignee: '',
        name: '',
        identity: '',
        number: '',
        date: '',
        orderState: '0',
      },
      dataSource1: [
        {
          key: '1',
          identiy: '88.178.113',
          logistics_name: '物流A厂商',
          logistics_rate: '90',
        },
        {
          key: '2',
          identiy: '88.178.114',
          logistics_name: '物流B厂商',
          logistics_rate: '88',
        },
      ],
      columns1: [
        {
          title: '物流商标识编码',
          align: 'center',
          dataIndex: 'identiy',
        },
        {
          title: '物流商名称',
          align: 'center',
          dataIndex: 'logistics_name',
        },
        {
          title: '物流商评分',
          align: 'center',
          dataIndex: 'logistics_rate',
        },
      ],
      columns: [
        {
          title: '物流单号',
          align: 'center',
          width: '20%',
          dataIndex: 'logisticsCode',
        },
        {
          title: '发货方',
          align: 'center',
          width: '8%',
          dataIndex: 'shipper',
        },
        {
          title: '收货方',
          align: 'center',
          width: '8%',
          dataIndex: 'consignee',
        },
        {
          title: '产品名称',
          align: 'center',
          width: '8%',
          dataIndex: 'name',
        },
        {
          title: '产品标识',
          align: 'center',
          width: '16%',
          dataIndex: 'identity',
        },
        {
          title: '发货日期',
          align: 'center',
          width: '8%',
          dataIndex: 'date',
        },
        {
          title: '发货数量',
          align: 'center',
          width: '8%',
          dataIndex: 'number',
        },
        {
          title: '订单状态',
          align: 'center',
          width: '8%',
          dataIndex: 'orderState',
          customRender: (text, record, index) => {
            if (text == '0') {
              return '未发货'
            } else if (text == '1') {
              return '已发货'
            } else {
              return '已收货'
            }
          },
        },
        {
          title: '操作',
          align: 'center',
          width: '16%',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      dataSource: [],
      record: {},
      ipagination: {
        current: 1,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + '共' + total + '条'
        },
        pageSize: 10,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        exampleId: this.$store.state.exampleId
      }
      erp_getAction(
        `/SupplyDelivery/Query`,params
      ).then((res) => {
        res.result.records.forEach((item) => {
          item.date = item.date.substring(0, 10)
        })
        this.dataSource = res.result.records
        this.ipagination.total = res.result.total
      })
    },
    handleAdd() {
      this.addVisible = true
    },
    async handleOk2() {
      console.log('this.invoiceForm', this.invoiceForm)
      this.invoiceForm.exampleId = this.$store.state.exampleId
      erp_postAction('/SupplyDelivery/insert', this.invoiceForm).then((res) => {
        this.loadData()
      })
      // this.invoiceForm.consignee = ''
      this.addVisible = false
    },
    handleCancel2() {
      this.invoiceForm.consignee = ''
      this.addVisible = false
    },
    handleEdit() {},
    shop(record) {
      erp_getAction(`/SupplyDelivery//setSupplySum?number=${record.number}&identity=${record.identity}`).then((res) => {
        if (res.code == 200) {
          erp_getAction(`/SupplyDelivery/updateStatus?id=${record.id}&orderState=1`).then((res) => {
            Message({
              showClose: true,
              message: '发货成功',
              type: 'success',
            })
          })
        } else {
          Message({
            showClose: true,
            message: '产品库存不足或没有此产品库存',
            type: 'error',
          })
        }
        this.loadData()
      })
    },
    chooseLogisticsProvider(record) {
      this.visible = true
      this.record = record
      console.log('this.record', this.record)
    },
    async handleOk() {
      var model = { prefix: '' }
      await putAction('/sys/fillRule/executeRuleByCode/logistics_auto', model).then((res) => {
        if (res.success) {
          this.logistics_code = this.selectedLogHead + '/' + res.result
        }
      })
      erp_getAction(`/SupplyDelivery/addLogisticsCode?id=${this.record.id}&logisticsCode=${this.logistics_code}`).then(
        (res) => {
          this.loadData()
        }
      )
      var logData = {
        identity: '',
        shipper: '',
        consignee: '',
        goods_name: '',
        goods_number: '',
        user_id: '',
        plan_id: '',
        lesson_id: '',
        teaching_mode: '',
      }
      var userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
      var process = JSON.parse(window.localStorage.getItem('process'))
      logData.plan_id = process.planId
      logData.lesson_id = process.lessonId
      logData.teaching_mode = process.teachingMode
      logData.user_id = userid.value
      logData.shipper = this.record.shipper
      logData.consignee = this.record.consignee
      logData.goods_name = this.record.name
      logData.goods_number = this.record.number
      logData.identity = this.logistics_code
      tm_postAction('/cgform/api/form/b274c8de834241408d1193edd54cbb66', logData).then((res) => {
        console.log('生成物流单插入模板成功')
      })
      this.visible = false
      this.selectedRowKeys = []
    },
    handleCancel() {
      this.visible = false
      this.selectedRowKeys = []
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      console.log('selectedRowKeys changed: ', selectedRowKeys, selectedRows)
      this.selectedLogHead = selectedRows[0].identiy
      this.selectedRowKeys = selectedRowKeys
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
