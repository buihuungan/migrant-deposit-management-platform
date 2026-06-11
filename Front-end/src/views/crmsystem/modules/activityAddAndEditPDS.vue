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
        <a-form-item v-bind="formItemLayout" label="营销活动名">
          <a-input placeholder="请输入活动名" v-model="form.ma_name" width="80%" :disabled="isDetail"></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="活动负责人">
          <a-input placeholder="请输入负责人" v-model="form.ma_duty_officer" width="80%" :disabled="isDetail"></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="活动预算">
          <a-input placeholder="请输入预算" v-model="form.ma_budget" width="80%" :disabled="isDetail"></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="活动地点">
          <a-input placeholder="请输入活动地点" v-model="form.ma_place" :disabled="isDetail"></a-input>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="开始时间">
          <a-date-picker
            v-model="form.ma_begin_time"
            type="date"
            placeholder="选择开始时间"
            style="width: 80%"
            format="YYYY-MM-DD"
            :disabled="isDetail"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="结束时间">
          <a-date-picker
            v-model="form.ma_end_time"
            type="date"
            placeholder="选择结束时间"
            style="width: 80%"
            format="YYYY-MM-DD"
            :disabled="isDetail"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="活动状态">
          <a-select :default-value="form.ma_status" @change="stateHandleChange" width="60%" :disabled="isDetail">
            <a-select-option value="0"> 未开始 </a-select-option>
            <a-select-option value="1"> 进行中 </a-select-option>
            <a-select-option value="2"> 已结束 </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item v-bind="formItemLayout" label="市场情报">
          <a
            v-if="isDetail"
            :href="api.server_url + '/plm/common/download//' + form.ma_information_file"
            :download="api.server_url + '/plm/common/download//' + form.ma_information_file"
          >
            {{ form.ma_information_file }}
          </a>
          <a-upload
            v-else
            name="multipartFile"
            :multiple="false"
            :action="`/plm/common/upload/`"
            @change="informationHandleChange"
            :file-list="informationFileList"
          >
            <a-button>
              <a-icon type="upload" /> {{ this.informationFileList.length === 0 ? '上传' : '重新上传' }}
            </a-button>
          </a-upload>
        </a-form-item>


        <a-form-item v-bind="formItemLayout" label="活动文件">
          <a
            v-if="isDetail"
            :href="api.server_url + '/plm/common/download//' + form.ma_file"
            :download="api.server_url + '/plm/common/download//' + form.ma_file"
          >
            {{ form.ma_file }}
          </a>
          <a-upload
            v-else
            name="multipartFile"
            :multiple="false"
            :action="`/plm/common/upload/`"
            @change="activityHandleChange"
            :file-list="activityFileList"
          >
            <a-button>
              <a-icon type="upload" /> {{ this.activityFileList.length === 0 ? '上传' : '重新上传' }}
            </a-button>
          </a-upload>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="具体客户">
          <a
            v-if="isDetail"
            :href="api.server_url + '/plm/common/download//' + form.customer_file"
            :download="api.server_url + '/plm/common/download//' + form.customer_file"
          >
            {{ form.customer_file }}
          </a>
          <a-upload
            v-else
            name="multipartFile"
            :multiple="false"
            :action="`/plm/common/upload/`"
            @change="customerHandleChange"
            :file-list="customerFileList"
          >
            <a-button>
              <a-icon type="upload" /> {{ this.customerFileList.length === 0 ? '上传' : '重新上传' }}
            </a-button>
          </a-upload>
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="市场计划">
           <a
            v-if="isDetail"
            :href="api.server_url + '/plm/common/download//' + form.ma_plan_file"
            :download="api.server_url + '/plm/common/download//' + form.ma_plan_file"
          >
            {{ form.ma_plan_file }}
          </a>
          <a-upload
            v-else
            name="multipartFile"
            :multiple="false"
            :action="`/plm/common/upload/`"
            @change="marketPlanHandleChange"
            :file-list="planFileList"
          >
            <a-button> <a-icon type="upload" /> {{ this.planFileList.length === 0 ? '上传' : '重新上传' }} </a-button>
          </a-upload>
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
      onlineId: 'e223ffdd6b2346c7a71d7435393fa4ea',
      visible: false,
      flag: 1,
      form: {
        id: '',
        ma_name: '',
        ma_create_time: null,
        ma_begin_time: null,
        ma_end_time: null,
        ma_status: '0',
        ma_budget: '',
        ma_place: '',
        ma_duty_officer: '',
        ma_information_file: '',
        ma_file: '',
        customer_file: '',
        ma_plan_file: '',
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
      informationFileList: [],
      activityFileList: [],
      customerFileList: [],
      planFileList: [],
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
      if (this.form.ma_begin_time !== '') {
        this.form.ma_begin_time = moment(new Date(this.form.ma_begin_time))
      }
      if (this.form.ma_end_time !== '') {
        this.form.ma_end_time = moment(new Date(this.form.ma_end_time))
      }
      if(this.flag!==3) {
        if (this.form.ma_information_file !== '') {
        this.informationFileList.push({
          uid: '1',
          name: this.form.ma_information_file,
          status: 'done',
          response: 'Server Error 500', // custom error message to show
          url: this.api.server_url + '/plm/common/download//' + this.form.ma_information_file,
        })
      }
      if (this.form.ma_file !== '') {
        this.activityFileList.push({
          uid: '1',
          name: this.form.ma_file,
          status: 'done',
          response: 'Server Error 500', // custom error message to show
          url: this.api.server_url + '/plm/common/download//' + this.form.ma_file,
        })
      }
      if (this.form.customer_file !== '') {
        this.customerFileList.push({
          uid: '1',
          name: this.form.customer_file,
          status: 'done',
          response: 'Server Error 500', // custom error message to show
          url: this.api.server_url + '/plm/common/download//' + this.form.customer_file,
        })
      }
      if (this.form.ma_plan_file !== '') {
        this.planFileList.push({
          uid: '1',
          name: this.form.ma_plan_file,
          status: 'done',
          response: 'Server Error 500', // custom error message to show
          url: this.api.server_url + '/plm/common/download//' + this.form.ma_plan_file,
        })
      }
      }
      
    },
    handleCancel() {
      this.fromReset()
      this.informationFileList = []
      this.activityFileList = []
      this.customerFileList = []
      this.planFileList = []
      this.visible = false
    },
    handleOk() {
      if (this.form.ma_begin_time !== null) {
        this.form.ma_begin_time = this.dateFormat(this.form.ma_begin_time)
      }
      if (this.form.ma_end_time !== null) {
        this.form.ma_end_time = this.dateFormat(this.form.ma_end_time)
      }

      if (this.informationFileList.length === 0) {
        this.form.ma_information_file = ''
      }
      if (this.activityFileList.length === 0) {
        this.form.ma_file = ''
      }
      if (this.customerFileList.length === 0) {
        this.form.customer_file = ''
      }
      if (this.planFileList.length === 0) {
        this.form.ma_plan_file = ''
      }

      if (this.flag === 1) {
        // 如果是添加就要手动向表单中添加一个创建时间为当前时刻（要格式化成这种:2021-07-13 16:27:56）
        this.form.ma_create_time = this.dateFormat(Date.now())
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
      this.informationFileList = []
      this.activityFileList = []
      this.customerFileList = []
      this.planFileList = []
      this.fromReset()
      this.visible = false
    },
    stateHandleChange(value) {
      this.form.ma_status = value
    },
    informationHandleChange(info) {
      let informationFileList = [...info.fileList]
      informationFileList = informationFileList.slice(-1)
      informationFileList = informationFileList.map((file) => {
        if (file.status === 'done') {
          this.form.ma_information_file = file.response.result.dbpath.slice(2)
        }
        return file
      })
      this.informationFileList = informationFileList
    },
    activityHandleChange(info) {
      let activityFileList = [...info.fileList]
      activityFileList = activityFileList.slice(-1)
      activityFileList = activityFileList.map((file) => {
        if (file.status === 'done') {
          this.form.ma_file = file.response.result.dbpath.slice(2)
        }
        return file
      })

      this.activityFileList = activityFileList
    },
    customerHandleChange(info) {
      let customerFileList = [...info.fileList]
      customerFileList = customerFileList.slice(-1)
      customerFileList = customerFileList.map((file) => {
        if (file.status === 'done') {
          this.form.customer_file = file.response.result.dbpath.slice(2)
        }
        return file
      })
      this.customerFileList = customerFileList
    },
    marketPlanHandleChange(info) {
      let planFileList = [...info.fileList]
      planFileList = planFileList.slice(-1)
      planFileList = planFileList.map((file) => {
        if (file.status === 'done') {
          this.form.ma_plan_file = file.response.result.dbpath.slice(2)
        }
        return file
      })
      this.planFileList = planFileList
    },
    goGack() {
      this.visible=false
    },
    fromReset() {
      this.form = {
        id: '',
        ma_name: '',
        ma_create_time: null,
        marketPlan: '',
        ma_duty_officer: '',
        ma_status: '0',
        ma_budget: '',
        ma_place: '',
        ma_begin_time: null,
        ma_end_time: null,
        ma_information_file: '',
        ma_file: '',
        ma_plan_file: '',
      }
    },
    dateFormat(time) {
      return moment(time).format('YYYY-MM-DD')
    },
  },
  computed: {
    title() {
      return this.flag === 1 ? '新增活动' : this.flag === 2 ? '活动编辑' : '活动详情'
    },
    isDetail() {
      return this.flag === 3
    }
  },
}
</script>

<style lang="less" scoped>
</style>