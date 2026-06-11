<template>
  <a-modal title="新增看板" :visible="addFormModalVisible" @ok="handleOk" @cancel="handleCancel">
    <a-form :form="form">
      <a-form-item label="看板名称">
        <a-input placeholder="请输入看板名称" v-decorator="[ 'name', validatorRules.name]"/>
      </a-form-item>
      <a-form-item label="看板描述">
        <a-input placeholder="请输入看板编号" v-decorator="[ 'description', validatorRules.description]"/>
      </a-form-item>
      <!-- <a-form-item label="表单图标">
        <a-input placeholder="点击右侧按钮选择图标" v-decorator="[ 'icon', validatorRules.icon]" disabled="disabled">
          <a-icon slot="addonAfter" type="setting" @click="selectIcons"/>
        </a-input>
      </a-form-item> -->

      <!--图标选择-->
      <icons @choose="handleIconChoose" @close="handleIconCancel" :iconChooseVisible="iconChooseVisible"></icons>
    </a-form>
  </a-modal>
</template>

<script>
import Icons from '../../system/modules/icon/Icons'
import {axios} from "../../../utils/request";
import qs from 'qs'
import {createScreen} from '@api/boardapi'

export default {
  name: 'AddFormMedal',
  components: {
    Icons,
  },
  props: {
    addFormModalVisible: {
      type: Boolean,
      default: false,
    },
    formTableData: {
      type: Array,
    },
    searchData: {
      type: Object,
    },
  },

  data() {
    return {
      visible: false,
      confirmLoading: false,
      form: this.$form.createForm(this),
      iconChooseVisible: false
    }
  },
  computed: {
    validatorRules: function () {
      return {
        name: { rules: [{ required: true, message: '请输入看板名称!' }] },
        description: {
          rules: [
            { required: true, message: '请输入看板描述!' },
            // {
            //   pattern: new RegExp('^[a-zA-Z][a-zA-Z0-9_-]{1,30}$'),
            //   message: '编码必须以字母开头，可包含数字、下划线、横杠。不得超过32个字符！',
            // },
            // {
            //   validator: (rule, value, callback) => {
            //     if (this.formTableData.some((item) => item.encoding === value)) return
            //     else callback()
            //   },
            //   message: '该值不可用，系统中已存在！',
            // },
          ],
        },
        // icon: { rules: [{ required: true, message: '请选择图标!' }] },
      }
    },
  },
  methods: {
    handleOk(e) {
      e.preventDefault()
      this.confirmLoading = true
      this.form.validateFields((err, fieldsValue) => {
        //校验必填项是否有值，自带函数
        if (err) {
          this.$message.error('有必填项未填写！！！')
          return false
        }
        let requestData = {
          name: fieldsValue['name'],
          description: fieldsValue['description'],
          // icon: fieldsValue['icon'],
        }
        createScreen(requestData).then((res) => {
          console.log('新增大屏',res);
          if (res.status===200) {
            this.$message.success('新增成功')
            this.form.resetFields() //清空新增表单里面数据
            this.$emit('loadData', this.searchData)
            this.confirmLoading = false
            this.$emit('update:addFormModalVisible', false)
          } else {
            this.$message.error('新增失败')
            this.$emit('loadData', this.searchData)
            this.confirmLoading = false
            return false
          }
        })
      })
    },
    handleCancel() {
      this.$emit('update:addFormModalVisible', false)
    },
    selectIcons() {
      this.iconChooseVisible = true
    },
    handleIconChoose(value) {
      this.form.setFieldsValue({
        icon: value,
      })
      this.iconChooseVisible = false
    },
    handleIconCancel() {
      this.iconChooseVisible = false
    },
  },
}
</script>

<style scoped>
</style>