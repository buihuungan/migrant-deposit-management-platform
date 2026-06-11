<template>
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
</template>

<script>
import { tm_postAction } from '@/api/templateApi'
import { putAction } from '@api/manage'
import { o_putAction } from '@/api/onApi'
export default {
  data() {
    return {
      orderInformation: {},
      logistics_code: '',
      selectedLogHead: '', //选中行物流商头部
      logHead: '', //确定的物流商头部
      selectedRowKeys: [],
      visible: false,
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
    }
  },
  methods: {
    openModal(record) {
      this.orderInformation = record
      this.visible = true
    },
    async handleOk() {
      this.logHead = this.selectedLogHead
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
      logData.user_id = userid.value
      var process = JSON.parse(window.localStorage.getItem('process'))
      if(process) {
        logData.plan_id = process.planId
        logData.lesson_id = process.lessonId
        logData.teaching_mode = process.teachingMode
      }
      logData.shipper = this.orderInformation.supplier_name
      logData.consignee = '虹信'
      logData.goods_name = this.orderInformation.purchase_order_material_details[0].name
      logData.goods_number = this.orderInformation.purchase_order_material_details[0].number
      var model = { prefix: '' }
      await putAction('/sys/fillRule/executeRuleByCode/logistics_auto', model).then((res) => {
        if (res.success) {
          this.logistics_code = res.result
        }
      })
      logData.identity = this.logHead + '/' + this.logistics_code
      let deal_record = JSON.parse(JSON.stringify(this.orderInformation))
      this.$delete(deal_record, 'key')
      deal_record.choose_supplier = 3
      let children = JSON.parse(
        JSON.stringify(deal_record.purchase_order_material_details)
          .replace(/pnCode/g, 'pn_code')
          .replace(/createBy/g, 'create_by')
          .replace(/createTime/g, 'create_time')
          .replace(/purchaseOrderId/g, 'purchase_order_id')
          .replace(/sysOrgCode/g, 'sys_org_code')
          .replace(/updateBy/g, 'update_by')
          .replace(/updateTime/g, 'update_time')
      )
      deal_record.purchase_order_material_details = children
      deal_record.logistics_code = logData.identity
      deal_record.example_id = this.$store.state.exampleId
      await o_putAction('/cgform/api/form/597983d3ade2449aa91338e05148332a', deal_record).then((res) => {
        if (res.success) {
          console.log('修改采购订单成功')
          this.$emit('ok', deal_record)
        }
      })
      await tm_postAction('/cgform/api/form/b274c8de834241408d1193edd54cbb66', logData).then((res) => {
        console.log('生成物流单插入模板成功')
      })
      this.selectedRowKeys = []
      this.visible = false
    },
    handleCancel() {
      this.selectedRowKeys = []
      this.visible = false
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      console.log('selectedRowKeys changed: ', selectedRowKeys, selectedRows)
      this.selectedLogHead = selectedRows[0].identiy
      this.selectedRowKeys = selectedRowKeys
    },
  },
}
</script>

<style></style>
