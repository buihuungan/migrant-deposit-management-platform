<template>
  <div>
    <a-modal width="600px" :title="title" :visible="visible" @ok="handleOk" @cancel="handleCancel">
      <a-form>
        <a-row :gutter="15">
          <a-col :span="12">
            <a-form-item label="姓名" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
              <a-input placeholder="请输入姓名" v-model="form.user_name" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="性别" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
              <a-select placeholder="" v-model="form.user_gender">
                <a-select-option value="0">女</a-select-option>
                <a-select-option value="1">男</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="15">
          <a-col :span="12">
            <a-form-item label="职务" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
              <a-input placeholder="请输入职务" v-model="form.position" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="联系电话" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
              <a-input placeholder="请输入联系电话" v-model="form.user_phone" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="关联公司" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
          <a-input placeholder="请输入关联公司" v-model="form.company_name" />
        </a-form-item>
        <a-form-item label="电子邮箱" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
          <a-input placeholder="请输入电子邮箱" v-model="form.user_email" />
        </a-form-item>
        <a-form-item label="是否为常用联系人" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
          <a-switch checked-children="是" un-checked-children="否" default-checked v-model="form.frequent" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { o_postAction, o_putAction } from '@/api/onApi'
export default {
  data() {
    return {
      form: {
        user_name:'',
        user_gender:'',
        company_name:'',
        position:'',
        user_phone:'',
        user_email:'',
        frequent: ''
      },
      title: '',
      visible: false,
      addOrEditFlag: 1,
      onlineId: 'dbc4743ca5f1429582a988ba362f7a87',
    }
  },
  methods: {
    //判断是添加还是编辑
    openModal(record, addOrEditFlag) {
      if (addOrEditFlag == 1) {
        this.addOrEditFlag= 1
        this.title = '新增'
        this.visible = true
      } else {
        this.addOrEditFlag= 2
        this.title = '编辑'
        // this.form=record
        this.form.id = record.id
        this.form.user_name = record.user_name
        this.form.user_gender = record.user_gender
        this.form.user_phone = record.user_phone
        this.form.position = record.position
        this.form.company_name = record.company_name
        this.form.user_email = record.user_email
        this.visible = true
      }
    },
    //确定按钮
    handleOk() {
      if (this.addOrEditFlag == 1){
        console.log('form aaa', this.form)
        o_postAction(`/cgform/api/form/${this.onlineId}`, this.form)
        .then((res) => {
          this.$emit('ok')
          console.log('res', res)
        })
        .catch((err) => {})
      }else{
        o_putAction(`/cgform/api/form/${this.onlineId}`, this.form).then((res) => {
          console.log("编辑res",res);
          this.$emit('ok')
        })
      }
      this.visible = false
    },
    //取消按钮
    handleCancel() {
      this.visible = false
      this.form = {
        user_name:'',
        user_gender:'',
        company_name:'',
        position:'',
        user_phone:'',
        user_email:'',
        frequent: ''
      }
    },
  },
}
</script>

<style>
</style>