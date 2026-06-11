<template>
  <a-modal
    title="新增表单"
    :visible="addFormModalVisible"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :form="form">
      <a-form-item label="表单名称">
        <a-input
          placeholder="请输入表单名称"
          v-decorator="[ 'name', validatorRules.name]"
        />
      </a-form-item>
      <a-form-item label="表单编号">
        <a-input
          placeholder="请输入表单编号"
          v-decorator="[ 'encoding', validatorRules.encoding]"
        />
      </a-form-item>
      <a-form-item label="表单图标">
        <a-input
          placeholder="点击右侧按钮选择图标"
          v-decorator="[ 'icon', validatorRules.icon]"
          disabled="disabled"
        >
          <a-icon
            slot="addonAfter"
            type="setting"
            @click="selectIcons"
          />
        </a-input>
      </a-form-item>

      <!--图标选择-->
      <icons
        @choose="handleIconChoose"
        @close="handleIconCancel"
        :iconChooseVisible="iconChooseVisible"
      ></icons>
    </a-form>
  </a-modal>
</template>

<script>
import Vue from 'vue'
import Icons from '../../system/modules/icon/Icons'
import { t_postAction, t_putAction } from '@/api/tempApi'
import { USER_INFO } from "@/store/mutation-types"
import { axios } from '../../../utils/request'

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
      iconChooseVisible: false,
      // model:{}
    }
  },
  computed: {
    validatorRules: function () {
      return {
        name: { rules: [{ required: true, message: '请输入表单名称!' }] },
        encoding: {
          rules: [
            { required: true, message: '请输入表单名称!' },
            {
              pattern: new RegExp('^[a-zA-Z][a-zA-Z0-9_-]{1,30}$'),
              message: '编码必须以字母开头，可包含数字、下划线、横杠。不得超过32个字符！',
            },
            {
              validator: (rule, value, callback) => {
                if (this.formTableData.some((item) => item.encoding === value)) return
                else callback()
              },
              message: '该值不可用，系统中已存在！',
            },
          ],
        },
        icon: { rules: [{ required: true, message: '请选择图标!' }] },
      }
    },
  },
  methods: {
    handleOk(e) {
      // console.log("点击表单新增按钮");
      e.preventDefault()
      this.confirmLoading = true
      this.form.validateFields((err, fieldsValue) => {
        //校验必填项是否有值，自带函数
        console.log("validateFields函数：",fieldsValue)
        if (err) {
          this.$message.error('有必填项未填写！！！')
          return false
        }
        let qs = require('qs')
        let requestData = {
          name: fieldsValue['name'],
          encoding: fieldsValue['encoding'],
          icon: fieldsValue['icon'],
        }
        t_postAction('/admin/desform/create', qs.stringify(requestData)).then((res) => {
          if (res.success) {
            this.$message.success('新增成功')
            this.form.resetFields() //清空新增表单里面数据
            t_postAction('/admin/desform/search', qs.stringify({ encoding: requestData.encoding, deleteFlag: 1 })).then(
              (res) => {
                if (res.success) {
                  let id = res.result.list[0].id
                  // t_putAction('/admin/desform/updateContent',qs.stringify({id:id,content:defineContent}))
                  //   .then(res=>{
                  //     if(!res.success){
                  //       this.$message.console.warning('表单数据初始化失败');
                  //     }
                  //   })
                  console.log('所增表单id为', id)
                }
              }
            )
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
      console.log(value)
      // this.model.icon = value
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