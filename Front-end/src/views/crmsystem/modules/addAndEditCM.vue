<template>
  <div>
    <a-modal
      width="600px"
      :title="title"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-form>
        <a-row :gutter="15">
          <a-col :span="12">
            <a-form-item
              label="姓名"
              :label-col="{ span: 6 }"
              :wrapper-col="{ span: 18 }"
            >
              <a-input
                placeholder="请输入姓名"
                v-model="form.user_name"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item
              label="性别"
              :label-col="{ span: 6 }"
              :wrapper-col="{ span: 18 }"
            >
              <a-select
                placeholder=""
                v-model="form.user_gender"
              >
                <a-select-option value="0">男</a-select-option>
                <a-select-option value="1">女</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item
          label="详细地址"
          :label-col="{ span: 3 }"
          :wrapper-col="{ span: 21 }"
        >
          <a-input
            placeholder="请输入详细地址"
            v-model="form.address"
          />
        </a-form-item>
        <a-row :gutter="15">
          <a-col :span="12">
            <a-form-item
              label="联系电话"
              :label-col="{ span: 6 }"
              :wrapper-col="{ span: 18 }"
            >
              <a-input
                placeholder="请输入联系电话"
                v-model="form.user_phone"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item
              label="公司名称"
              :label-col="{ span: 6 }"
              :wrapper-col="{ span: 18 }"
            >
              <a-input
                placeholder="请输入公司名称"
                v-model="form.company_name"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="15">
          <a-col :span="12">
            <a-form-item
              label="公司级别"
              :label-col="{ span: 6 }"
              :wrapper-col="{ span: 18 }"
            >
              <a-select
                placeholder=""
                v-model="form.company_level"
              >
                <a-select-option value="small">小</a-select-option>
                <a-select-option value="inter">中</a-select-option>
                <a-select-option value="big">大</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item
              label="信用度"
              :label-col="{ span: 6 }"
              :wrapper-col="{ span: 18 }"
            >
              <a-select
                placeholder=""
                v-model="form.credit"
              >
                <a-select-option value="low">低</a-select-option>
                <a-select-option value="medium">中</a-select-option>
                <a-select-option value="high">高</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item
          label="备注"
          :label-col="{ span: 3 }"
          :wrapper-col="{ span: 21 }"
        >
          <a-input
            placeholder="请输入备注"
            v-model="form.note"
          />
        </a-form-item>
        <a-form-item
          label="是否为常用联系人"
          :label-col="{ span: 6 }"
          :wrapper-col="{ span: 18 }"
        >
          <a-switch
            checked-children="是"
            un-checked-children="否"
            default-checked
            v-model="form.frequent"
          />
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
        user_name: '',
        user_gender: '',
        address: '',
        user_phone: '',
        company_name: '',
        company_level: '',
        credit: '',
        note: '',
        frequent: '',
      },
      title: '',
      visible: false,
      onlineId: 'dbc4743ca5f1429582a988ba362f7a87',
      addOrEditFlag: 1,
    }
  },
  methods: {
    //判断是添加还是编辑
    openModal(record, addOrEditFlag) {
      if (addOrEditFlag == 1) {
        this.addOrEditFlag = 1
        this.visible = true
        this.title = '新增'
      } else {
        this.addOrEditFlag = 2
        this.title = '编辑'
        this.form = record
        this.visible = true
      }
    },
    //确定按钮
    handleOk() {
      if (this.addOrEditFlag === 1) {
        console.log('form aaa', this.form)
        o_postAction(`/cgform/api/form/${this.onlineId}`, this.form)
          .then((res) => {
            console.log('res', res)
            if (res.success) {
            } else {
            }
          })
          .catch((err) => {})
      } else {
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
        user_name: '',
        user_gender: '',
        address: '',
        user_phone: '',
        company_name: '',
        company_level: 0,
        credit: 0,
        note: '',
        frequent: 0,
      }
    },
  },
}
</script>

<style>
</style>