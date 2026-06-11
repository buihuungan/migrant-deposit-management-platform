<template>
  <div>
    <a-modal
      :title="title"
      :visible="visible"
      okText="确定"
      cancelText="取消"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
      width="50%"
    >
      <a-form>
        <a-form-item v-bind="formItemLayout" label="id">
          <a-input v-model="form.id" disabled></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="姓名">
          <a-input v-model="form.customerName"></a-input>
        </a-form-item>
        <!--<a-form-item v-bind="formItemLayout" label="营销活动名">
          <a-input v-model="form.activityName" disabled></a-input>
        </a-form-item>-->
        <a-form-item v-bind="formItemLayout" label="性别">
          <a-radio-group name="radioGroup" :default-value="form.customerSex" @change="sexChange(e)">
            <a-radio :value="1"> 男 </a-radio>
            <a-radio :value="0"> 女 </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="电话">
          <a-input placeholder="请输入电话" v-model="form.customerPhone"></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="状态">
          <a-select :default-value="form.customerStatus" style="width: 80px" @change="handleChange">
            <a-select-option value="0"> 正常 </a-select-option>
            <a-select-option value="1"> 失联 </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="地址">
          <a-input placeholder="请输入地址" v-model="form.customerAddress"></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="职业">
          <a-input placeholder="请输入职业" v-model="form.customerJober"></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="其他">
          <a-input placeholder="其他说明" v-model="form.customerElse"></a-input>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      title: '',
      form: {
        activityId: '',
        activityName: '',
        id: '',
        customerName: '',
        customerSex: null,
        customerPhone: '',
        customerStatus: '',
        customerAddress: '',
        customerJober: '',
        customerElse: '',
      },
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      },
    }
  },
  methods: {
    show() {
      this.visible = true
    },
    setTitle(title) {
      this.title = title
    },
    handleCancel() {
      this.form = {
        activityId: '',
        activityName: '',
        id: '',
        customerName: '',
        customerSex: null,
        customerPhone: '',
        customerStatus: '',
        customerAddress: '',
        customerJober: '',
        customerElse: '',
      }
      this.visible = false
    },
    handleOk() {
      this.visible = false
    },
    setForm(record) {
      this.form = record
    }
  },
}
</script>

<style lang="less" scoped>
</style>