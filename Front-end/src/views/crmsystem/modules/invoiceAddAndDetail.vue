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
        <a-button
          type="primary"
          @click.stop='goGack'
          >返回</a-button
        >
      </template>

      <a-form ref="formRef">
        <a-form-item v-bind="formItemLayout" label="发票号码">
          <a-input placeholder="请输入发票号码" v-model="form.invoice_number"  style="width: 80%" :disabled="isDetail"></a-input>
        </a-form-item>

        <a-form-item v-bind="formItemLayout" label="开票金额">
          <a-input placeholder="请输入开票金额" v-model="form.money" style="width: 80%" :disabled="isDetail"></a-input>
        </a-form-item>

        <a-form-item v-bind="formItemLayout" label="开票人员">
          <a-input placeholder="请输入开票人" v-model="form.staff_name" style="width: 80%" :disabled="isDetail"></a-input>
        </a-form-item>
        
        <a-form-item v-bind="formItemLayout" label="发票类型">
          <a-select :default-value="form.type" @change="typeHandleChange" style="width: 40%" :disabled="isDetail">
            <a-select-option value="0"> 普通发票 </a-select-option>
            <a-select-option value="1"> 增值税发票 </a-select-option>
            <a-select-option value="2"> 其他发票 </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item v-bind="formItemLayout" label="开票时间">
          <a-date-picker
            v-model="form.date"
            type="date"
            placeholder="选择开票时间"
            style="width: 40%"
            format="YYYY-MM-DD"
            :disabled="isDetail"
          />
        </a-form-item>

        <a-form-item v-bind="formItemLayout" label="发票图片">
          <a
            v-if="isDetail"
            :href="api.server_url + '/plm/common/download//' + form.invoice_img"
            :download="api.server_url + '/plm/common/download//' + form.invoice_img"
          >
            {{ form.invoice_img }}
          </a>
          <a-upload
            v-else
            name="multipartFile"
            :multiple="false"
            :action="`/plm/common/upload/`"
            @change="imgHandleChange"
            :file-list="invoiceImgList"
          >
            <a-button>
              <a-icon type="upload" /> {{ this.invoiceImgList.length === 0 ? '上传' : '重新上传' }}
            </a-button>
          </a-upload>
        </a-form-item>

        <a-form-item v-bind="formItemLayout" label="备注">
          <a-textarea v-model="form.invoice_remark" placeholder="请输入备注说明" :auto-size="{ minRows: 3, maxRows: 5 }" :disabled="isDetail" style="width: 80%"/>
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
        invoice_number: '',
        date: null,
        type: '0',
        money: '',
        staff_name: '',
        remark: '',
        invoice_img: ''
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
      invoiceImgList: [],
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
      if(this.flag!==3) {
        if (this.form.invoice_img !== '') {
        this.invoiceImgList.push({
          uid: '1',
          name: this.form.invoice_img,
          status: 'done',
          response: 'Server Error 500', // custom error message to show
          url: this.api.server_url + '/plm/common/download//' + this.form.invoice_img,
        })
      }
      }
      
    },
    handleCancel() {
      this.fromReset()
      this.invoiceImgList = []
      this.visible = false
    },
    handleOk() {
     if (this.form.date !== null && this.form.date!=='') {
        this.form.date = moment(this.form.date).format('YYYY-MM-DD')
      }
      if (this.invoiceImgList.length === 0) {
        this.form.invoice_img = ''
      }
      if (this.flag === 1) {
        o_putAction(`/cgform/api/form/${this.onlineId}`, this.form)
          .then((res) => {
            this.$emit('addOk',res.result)
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
            this.$emit('editOk')
          } else {
            Message({
              showClose: true,
              message: '编辑失败!',
              type: 'error',
            })
          }
        })
      }
      this.invoiceImgList = []
      this.fromReset()
      this.visible = false
    },
    typeHandleChange(value) {
      this.form.type = value
    },
    imgHandleChange(info) {
      let flag= false
      let invoiceImgList = [...info.fileList]
      invoiceImgList = invoiceImgList.slice(-1)
      invoiceImgList = invoiceImgList.map((file) => {
        if (file.status === 'done') {
          this.form.invoice_img = file.response.result.dbpath.slice(2)
        }else if (file.status === 'error'){
          Message({
              showClose: true,
              message: '图片上传失败!',
              type: 'error',
            })
            flag = true
        }
        return file
      })
      if(flag) {
        this.invoiceImgList = []
      } else {
        this.invoiceImgList = invoiceImgList
      }
      
    },
    goGack() {
      this.visible=false
    },
    fromReset() {
      this.form = {
        id: '',
        invoice_number: '',
        date: null,
        type: '0',
        money: '',
        staff_name: '',
        remark: '',
        invoice_img: ''
      }
    },
    dateFormat(time) {
      return moment(time).format('YYYY-MM-DD')
    },
  },
  computed: {
    title() {
      return this.flag === 1 ? '添加发票' : this.flag === 2 ? '发票编辑' : '发票详情'
    },
    isDetail() {
      return this.flag === 3
    }
  },
}
</script>

<style lang="less" scoped>
.scanButton {
  margin-left: 20px;
}
</style>