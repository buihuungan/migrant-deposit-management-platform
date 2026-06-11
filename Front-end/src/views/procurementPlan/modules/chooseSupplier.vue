<template>
  <a-modal
    title="选择供应商"
    :visible="visible"
    okText="确定"
    cancelText="取消"
    width="80%"
    :destroyOnClose="true"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :form="form" layout="inline" style="display: flex; justify-content: space-between">
      <a-form-item label="供应商标识编码">
        <jPopup
          ref="modalForm"
          :multi="false"
          :formFlag="true"
          code="supplier_informaton"
          :disabled="disabled"
          @jCallBack="jCallBack"
        ></jPopup>
      </a-form-item>
      <a-form-item label="供应商名称">
        <a-input v-model="formData.supplier_name" />
      </a-form-item>
      <a-form-item label="供应商评分">
        <a-input v-model="formData.supplier_rate" />
      </a-form-item>
      <a-form-item label="供应商提供货物名称">
        <a-input v-model="formData.supplier_goods" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import jPopup from '@comp/jeecg/JPopup'
import { putAction } from '@api/manage'
import {tm_postAction} from '@/api/templateApi'
export default {
  components: { jPopup },
  data() {
    return {
      visible: false,
      dataModel: '',
      disabled: false,
      supplier_infomation: {},
      father_record: {}, //父亲组件传过来的所选项数据
      form: this.$form.createForm(this),
      formData: {},
    }
  },
  methods: {
    openModal(record) {
      record = JSON.parse(JSON.stringify(record))
      console.log('record',record)
      this.visible = true
      this.father_record.order_identity=record.orderIdentity
      this.father_record.order_status = record.orderStatus
      let children = JSON.parse(JSON.stringify(record.children).replace(/createBy/g,"create_by").replace(/createTime/g,"create_time").replace(/purchaseOrderId/g,"purchase_order_id").replace(/sysOrgCode/g,"sys_org_code").replace(/updateBy/g,"update_by").replace(/updateTime/g,"update_time"))
      this.father_record.purchase_order_material_details = children
      console.log('children', children)
      this.father_record.id = record.id
      this.father_record.contract_name = ''
      this.father_record.contract_url = '' 
      console.log("this.father_record aaaaaa",this.father_record);
    },
    handleOk() {
      console.log('this.father_record bbbb', this.father_record)
      var model = { prefix: '' }
      putAction('/sys/fillRule/executeRuleByCode/pn_auto', model).then((res) => {
        if (res.success) {
          console.log('res.result',res.result)
          this.father_record.pn_code = res.result
          for(var i=0;i<this.father_record.purchase_order_material_details.length;i++) {
            this.$set(this.father_record.purchase_order_material_details[i],'pn_code',res.result)
          }
          this.$emit('ok', this.father_record) //返回父组件选完供应商后完整的一条数据
          // this.father_record.purchase_order_material_details[0].pnCode = res.result
        }
      })
      this.visible = false
      this.formData = {}
    },
    handleCancel() {
      this.visible = false
      this.formData = {}
    },
    jCallBack(rows) {
      this.$refs.modalForm.showText = rows[0].identity
      this.father_record.choose_supplier = 2 //修改数据状态为已经选择供应商 默认值为1：为没有选择供应商 2：选择供应商
      this.father_record.supplier_identity = rows[0].identity
      this.father_record.supplier_goods = rows[0].supplier_goods
      this.father_record.supplier_name = rows[0].supplier_name
      this.father_record.supplier_rate = rows[0].supplier_rate
      this.formData = rows[0]
    },
  },
}
</script>

<style>
</style>