<template>
  <a-modal
    :title="title"
    class="addIdentifyMpdal"
    :visible="visible"
    :centered="true"
    okText="确定"
    width="52%"
    cancelText="取消"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item class="detailItem" label="产品标识前缀">
        <a-input :disabled="true" v-model="form.identityIndex" />
      </a-form-item>
      <a-form-item class="detailItem" label="标识">
        <a-input :addon-before="addonbefore" v-model="form.identityData" />
      </a-form-item>
      <editTable2
        ref="editTable2"
        :identityId="identityId"
        :identity="identity"
        :master_userData="master_userData"
        :addOrEditFlag1="addOrEditFlag1"
        :listData="listData"
      ></editTable2>
    </a-form>
  </a-modal>
</template>

<script>
import editTable2 from './editTable2'
import axios from 'axios'
import api from '@/api/index.js'
import { tm_getAction, tm_postAction } from '@/api/templateApi'
import { im_putAction,  } from '@/api/icimApi'


import { Message } from 'element-ui'
import qs from 'qs'
export default {
  components: { editTable2 },
  props: ['listData', 'tableId', 'tableName1'],
  data() {
    return {
      title: '',
      addOrEditFlag1: 1,
      data: [],
      master_userData: [],
      addonbefore: '',
      visible: false,
      // form: this.$form.createForm(this),
      form: {
        identityIndex: '',
        identityData: '',
      },
      addonbefore: '',
      identity: '',
      // flag: true,
      checkEditId: '',
      identityId: '',
      editTableData: [],
    }
  },
  created() {},
  // watch: {
  //   'form.identityIndex':function(){
  //      console.log("this.form.identityIndex",this.form.identityIndex)
  //   }
  // },
  methods: {
    //获取tableData
    getTableData(record) {
      console.log('record.id', record.id)
      console.log('this.listData', this.listData)
      var identity = {
        identity: record.identity,
      }
      tm_getAction('/cgform/api/form/table_name/' + this.tableName1 + '/' + record.id)
        .then((res) => {
          console.log('res', res)
          for (let index = 0; index < this.listData.length; index++) {
            var data2 = { paramName: '', paramType: '', paramValue: '' }
            this.$set(data2, 'paramName', this.listData[index].dbFieldName)
            this.$set(data2, 'paramType', this.listData[index].dbFieldTxt)
            this.$set(data2, 'paramValue', res.result[this.listData[index].dbFieldName])
            this.master_userData.push(data2)
          }
          this.master_userData = this.master_userData.filter((item) => {
            return (
              item.paramName != 'id' &&
              item.paramName != 'identity' &&
              item.paramName != 'create_time' &&
              item.paramName != 'update_time'
            )
          })
          console.log('add2的master_userData', this.master_userData)
          // this.master_userData = this.master_userData[0]
        })
        .catch((err) => {
          console.log('err')
        })
    },
    //点击新增 调用此方法  record ='' addOrEditFlag = 1;
    openModal(record, addOrEditFlag) {
      this.addOrEditFlag1 = addOrEditFlag
      if (this.addOrEditFlag1 == 1) {
        this.title = '新增'
        console.log('this.form.identityIndex', this.form.identityIndex)
        console.log('this.addonbefore', this.addonbefore)

        this.$nextTick(() => {
          this.$refs.editTable2.getList(this.listData)
        })
      } else if (this.addOrEditFlag1 == 2) {
        this.title = '编辑'
        console.log('编辑的record', record)
        this.checkEditId = record.id
        console.log('this.checkEditId', this.checkEditId)
        this.form.identityIndex = record.identity.split('/')[0]
        console.log('identityIndex', this.form.identityIndex)
        console.log('判断是否含有#', record.identity.indexOf('#'))
        if (record.identity.indexOf('#')!=-1) {
          this.addonbefore = record.identity.split('#')[0] + '#'
          this.form.identityData = record.identity.split('#')[1]
        }else{
          this.addonbefore = record.identity.split('/')[0] + '/'
          this.form.identityData = record.identity.split('/')[1]
        }
        console.log('this.form.identityData', this.form.identityData)
        this.identityId = record.id
        this.identity = record.identity
        //给表加数据
        this.getTableData(record)

        // this.$refs.editTable2.editTableStart();
      }
      this.visible = true
    },
    handleCancel() {
      this.visible = false
      this.form = {}
      //this.listData=[]
      this.addonbefore = ''
      this.master_userData = []
      // this.$refs.editTable2.master_user.data = []
    },
    handleOk() {
      console.log('addOrEditFlag1', this.addOrEditFlag1)
      //新增 1 编辑 2
      if (this.addOrEditFlag1 == 1) {
        var data = {
          paramName: 'identity',
          paramType: '标识编码',
          paramValue: this.addonbefore + this.form.identityData,
        }
        console.log('data', data)
        this.editTableData = this.$refs.editTable2.master_user.data
        this.editTableData.push(data)
        console.log('edddd', this.editTableData)
        var postData = {}
        // this.$set(postData, 'identity', this.form.identityIndex + '/' + this.form.identify)
        for (let index = 0; index < this.editTableData.length; index++) {
          this.$set(postData, this.editTableData[index].paramName, this.editTableData[index].paramValue)
        }
        console.log('postData', postData)
        console.log('tableId', this.tableId)
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        tm_postAction('/cgform/api/form/' + this.tableId, JSON.stringify(postData))
          .then((res) => {
            if (res.success) {
              Message({
                type: 'success',
                showClose: true,
                message: '新增成功!',
              })
            }
            this.$emit('ok')
          })
          .catch((err) => {
            console.log('err')
          })
        this.form = {}
        this.addonbefore = ''
        // this.$refs.editTable2.master_user.data = []
        this.master_userData = []
      } else if (this.addOrEditFlag1 == 2) {
        //编辑界面操作
        this.editTableData = this.$refs.editTable2.master_user.data
        console.log('this.editTableData', this.editTableData)
        var postData = {}
        this.$set(postData, 'id', this.checkEditId)
        this.$set(postData, 'identity', this.addonbefore + this.form.identityData)
        for (let index = 0; index < this.editTableData.length; index++) {
          this.$set(postData, this.editTableData[index].paramName, this.editTableData[index].paramValue)
        }
        console.log('postData', postData)
        // axios.defaults.headers.post['Content-Type'] = 'application/json'  JSON.stringify(postData)
        im_putAction('/admin/cgform/api/form/' + this.tableId, postData)
          .then((res) => {
            console.log("im_putActionres",res);
            if (res.data.success) {
              Message({
                type: 'success',
                showClose: true,
                message: '编辑成功!',
              })
            }
            this.$emit('ok')
          })
          .catch((err) => {
            console.log('err')
          })
        this.form = {}
        this.addonbefore = ''
        // this.$refs.editTable2.master_user.data = []
        this.master_userData = []
      }
      this.visible = false
    },
  },
}
</script>

<style>
.detailItem > .ant-form-item-label {
  display: flex !important;
}
</style>