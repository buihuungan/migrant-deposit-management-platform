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
      width="40%"
    >
      <template slot="footer" v-if="flag === 3">
        <a-button type="primary" @click.stop="goGack">返回</a-button>
      </template>

      <a-form ref="formRef">
        <a-form-item v-bind="formItemLayout" label="合同ID">
          <a-input placeholder="请输入合同号" v-model="form.contract_no" width="60%"></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="客户名称">
          <a-input placeholder="请输入客户名" v-model="form.username" width="60%" ></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="回款金额" >
          <a-input
            placeholder="请输入回款金额"
            v-model="form.receive_number"
            width="60%"
          ></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="业务员">
          <a-input
            placeholder="请输入业务员姓名"
            v-model="form.salesman_name"
            width="60%"
          ></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="是否逾期">
          <a-select :default-value="form.overdue" @change="stateHandleChange" width="40%">
            <a-select-option value="0"> 未逾期 </a-select-option>
            <a-select-option value="1"> 逾期 </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="备注">
          <a-textarea v-model="form.remark" placeholder="请输入备注说明" :auto-size="{ minRows: 3, maxRows: 5 }" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { o_postAction, o_putAction } from '@/api/onApi'
import { Message } from 'element-ui'
import moment from 'moment'
moment.locale('zh-cn')
import api from '@/api/index'
export default {
  data() {
    return {
      api,
      onlineId: '6eb12b4bf7f74b16bcb535191045bee9',
      visible: false,
      flag: 1,
      form: {
        id: '',
        username: '',
        contract_no: '',
        ma_create_time: null,
        salesman_name: '',
        overdue: '0',
        receive_number: '',
        remark: '',
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
      headers: {},
    }
  },
  methods: {
    show() {
      this.visible = true
    },
    setFlag(flag) {
      this.flag = flag
    },
    setForm(record) {
      this.form = JSON.parse(JSON.stringify(record))
    },
    handleCancel() {
      this.fromReset()
      this.visible = false
    },
    handleOk() {
      
      if (this.flag === 1) {
        // 如果是添加就要手动向表单中添加一个创建时间为当前时刻（要格式化成这种:2021-07-13 16:27:56）
        this.form.input_time = this.dateFormat(Date.now())
        o_postAction(`/cgform/api/form/${this.onlineId}`, this.form)
          .then((res) => {
            if (res.success) {
              Message({
                showClose: true,
                message: '添加成功!',
                type: 'success',
              })
              this.$emit('ok')
            } else {
              Message({
                showClose: true,
                message: '添加失败!',
                type: 'error',
              })
            }
          })
          .catch((err) => {})
      } else {
        o_putAction(`/cgform/api/form/${this.onlineId}`, this.form).then((res) => {
          if (res.success) {
            Message({
              showClose: true,
              message: '编辑成功!',
              type: 'success',
            })
            this.$emit('ok')
          } else {
            Message({
              showClose: true,
              message: '编辑失败!',
              type: 'error',
            })
          }
        })
      }
      this.fromReset()
      this.visible = false
    },
    stateHandleChange(value) {
      this.form.overdue = value
    },
    fromReset() {
      this.form = {
        id: '',
        username: '',
        contract_no: '',
        ma_create_time: null,
        salesman_name: '',
        overdue: '0',
        receive_number: '',
        remark: '',
      }
    },
    dateFormat(time) {
      return moment(time).format('YYYY-MM-DD')
    },
  },
  computed: {
    title() {
      return this.flag === 1 ? '新增回款' : '回款编辑'
    }
  },
}
</script>
<style scoped>

</style>