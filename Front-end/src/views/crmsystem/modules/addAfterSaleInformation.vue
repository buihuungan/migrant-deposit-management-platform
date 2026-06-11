<template>
  <a-modal
    :title="title"
    :visible="visible"
    okText="确定"
    cancelText="取消"
    width="40%"
    :destroyOnClose="true"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <div class="formbody">
      <a-form :form="form">
        <a-form-item label="选择订单" v-if="openType == '1'">
          <jPopup
            ref="modalForm"
            :multi="false"
            :formFlag="true"
            code="aftersale_message"
            style="width: 210px"
            :disabled="disabled"
            @jCallBack="jCallBack"
          ></jPopup>
        </a-form-item>
        <a-form-item label="订单编码" v-if="openType == '2'">
          <a-input v-model="formData.order_identity" style="width: 210px" :readOnly="true" />
        </a-form-item>
        <!-- <a-form-item label="订单编码" v-if="formData.parts_name != ''">
          <a-input v-model="formData.parts_name" style="width: 210px" :readOnly="true" />
        </a-form-item> -->
        <a-form-item label="订单订购零件" v-if="formData.parts_name && formData.parts_name != ''">
          <a-input v-model="formData.parts_name" style="width: 210px" :readOnly="true" />
        </a-form-item>
        <a-form-item label="供应商名称" v-if="formData.supplier_name && formData.supplier_name != ''">
          <a-input v-model="formData.supplier_name" style="width: 210px" :readOnly="true" />
        </a-form-item>
        <a-form-item label="处理时间">
          <a-date-picker v-model="formData.processing_time" style="width: 210px" />
        </a-form-item>
        <a-form-item label="售后原因">
          <a-input v-model="formData.aftersale_reason" style="width: 210px" placeholder="请输入原因" />
        </a-form-item>
        <a-form-item label="处理状态">
          <a-select
            style="width: 210px"
            placeholder="请选择处理状态"
            @change="handleChange"
            :default-value="formData.processing_state"
          >
            <a-select-option v-for="item in stateOptions" :key="item" :value="item"> {{ item }} </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script>
import axios from 'axios'
import { order_getAction } from '@/api/orderApi'
import { o_getAction, o_postAction, o_putAction } from '@/api/onApi'
import jPopup from '@comp/jeecg/JPopup'

export default {
  components: { jPopup },
  data() {
    return {
      openType: '1', //打开弹框的类型
      onlineId: '597983d3ade2449aa91338e05148332a',
      onlineId2: 'f3a3f37ffa9042378873c8c35f1a571b',
      form: this.$form.createForm(this),
      disabled: false,
      visible: false,
      title: '新增',
      formData: {
        order_identity: '',
        parts_name: '',
        supplier_name: '',
        // processing_state:'',
      },
      stateOptions: ['未处理', '处理中', '处理完成'],
    }
  },
  watch: {
    'formData.order_identity': function (val, oldval) {
      console.log('val,oldval', val, oldval)
      // this.query_parts(val)
    },
  },
  methods: {
    //根据订单查订单零件
    query_parts(order) {
      let params = {
        pageSize: this.pageSize,
        pageNo: this.pageNo,
        column: 'createTime',
        order: 'desc',
        superQueryMatchType: 'and',
      }
      params.superQueryParams = escape(
        JSON.stringify([
          {
            rule: 'eq',
            type: 'text',
            val: order,
            field: 'order_identity',
          },
        ])
      )
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params).then((res) => {
        let id = res.result.records[0].id
        console.log('online结果', res)
        o_getAction(`/cgform/api/form/${this.onlineId}/${id}`).then((res) => {
          console.log('sasaasas', res)
          let name = ''
          res.result.purchase_order_material_details.forEach((element, index) => {
            if (index === 0) {
              name = element.name
            } else {
              name = name + ',' + element.name
            }
          })
          this.formData.parts_name = name
        })
      })
    },
    handleOk() {
      //确定导入数据
      console.log('提交得数据', this.formData)
      if (this.openType == '1') {
        o_postAction(`/cgform/api/form/${this.onlineId2}?tabletype=1`, this.formData)
          .then((res) => {
            // console.log('post结果', res)
            this.visible = false
            this.$message.success('添加成功')
            Object.keys(this.formData).map((key) => (this.formData[key] = ''))
            console.log('post结果', this.formData)
            this.openType = '1'
            this.$emit('ok')
          })
          .catch((err) => {
            console.log(err)
          })
      }else if(this.openType == '2'){
        console.log('2222',this.formData);
        o_putAction(`/cgform/api/form/${this.onlineId2}?tabletype=1`, this.formData)
          .then((res) => {
            // console.log('post结果', res)
            this.visible = false
            this.$message.success('修改成功')
            Object.keys(this.formData).map((key) => (this.formData[key] = ''))
            console.log('post结果', this.formData)
            this.openType = '1'
            this.$emit('ok')
          })
          .catch((err) => {
            console.log(err)
          })
      }
    },
    handleCancel() {
      //取消操作
      // Object.keys(this.formData).forEach(key => this.formData[key]='')
      this.formData = {}
      this.visible = false
      this.openType = '1'
    },
    openModal(opt) {
      this.visible = true
      // console.log('opt,',opt);
      console.log('this.formdata,',this.formData);
      if (opt) {
        this.formData = opt
        // this.formData.processing_state = opt.processing_state
        this.openType = '2'
        this.title = '编辑'
        // this.$refs.modalForm.showText = opt.order_identity
        // this.formData
        console.log('opt,', this.formData)
      }
      // axios
      //   .get('http://10.162.33.222:41684/purchaseDemand/material/3')
      //   .then((res) => {
      //     console.log('res', res)
      //   })
      //   .catch((err) => {
      //     console.log('err', err)
      //   })
    },
    // onChange(date, dateString) {
    //   //   console.log(date, dateString)
    //   this.formData.processing_time = dateString
    // },
    handleChange(value) {
        // console.log(value);
      this.formData.processing_state = value
        // console.log('this.formData.processing_state',this.formData.processing_state);
    },
    jCallBack(rows) {
      console.log('rreee', rows)
      this.$refs.modalForm.showText = rows[0].order_identity
      this.formData.order_identity = rows[0].order_identity
      console.log('this.formData.order_identity', this.formData)
      this.formData.supplier_name = rows[0].supplier_name
      this.query_parts(this.formData.order_identity)
      //   this.father_record.supplier_identity = rows[0].identity
      //   this.father_record.supplier_goods = rows[0].supplier_goods
      //   this.father_record.supplier_name = rows[0].supplier_name
      //   this.father_record.supplier_rate = rows[0].supplier_rate
      //   this.formData = rows[0]
    },
  },
}
</script>

<style scoped>
.formbody {
  margin-top: 5px;
  width: 100%;
}
</style>