<template>
  <div>
    <a-modal
      :title="title"
      :visible="visible"
      :destroyOnClose="true"
      @cancel="handleCancel"
    >
      <a-form>
        <a-form-item
          v-bind="formItemLayout"
          label="标识编码"
        >
          <a-input disabled="true" v-model="form.identity"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="名称"
        >
          <a-input disabled="true" v-model="form.name"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="型号"
        >
          <a-input disabled="true" v-model="form.model"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="单位"
        >
          <a-input disabled="true" v-model="form.unit"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="数量"
        >
          <a-input disabled="true" v-model="form.number"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="供应商标识编码"
        >
          <a-input disabled="true" v-model="form.supplier_identity"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="供应商名称"
        >
          <a-input disabled="true" v-model="form.supplier_name"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="供应商评分"
        >
          <a-input disabled="true" v-model="form.supplier_rate"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="供应商提供零件名称"
        >
          <a-input disabled="true" v-model="form.supplier_goods"></a-input>
        </a-form-item>
        <a-form-item
          v-bind="formItemLayout"
          label="合同名称"
        >
          <a-input disabled="true" v-model="form.contract_name"></a-input>
        </a-form-item>
        <!-- <a-form-item
          v-bind="formItemLayout"
          label="合同下载"
        >
          <a-input disabled="true" v-model="form.contract_url"></a-input>
        </a-form-item> -->
      </a-form>
      <template slot="footer">
        <a-button
          type="primary"
          @click="handleCancel"
        >关闭</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import { o_postAction } from '@/api/onApi'
import { Message } from 'element-ui'
export default {
  data() {
    return {
      onlineId: '597983d3ade2449aa91338e05148332a',
      form: {
        identity: '',
        name: '',
        model: '',
        unit: '',
        number: '',
        supplier_identity: '',
        supplier_name: '',
        supplier_rate: '',
        supplier_goods: '',
      },
      title: '',
      visible: false,
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 7 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      },
    }
  },
  methods: {
    openModal(record) {
      this.title = '详情'
      this.form = record
      this.visible = true
    },
    handleCancel() {
      this.visible = false
    },
  },
}
</script>

<style>
</style>