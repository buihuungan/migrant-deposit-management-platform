<template>
  <a-modal
    :title="title"
    class="addIdentifyMpdal"
    :visible="visible"
    :centered="true"
    okText="确定"
    cancelText="取消"
    :destroyOnClose="true"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item class="detailItem" label="前缀">
        <a-select @change="selectChildrenNodes">
          <a-select-option v-for="(item, index) in identityIndexList" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>
        <a-select @change="handleIdentityIndex" v-model="form.identityIndex">
          <a-select-option v-for="(item, index) in threeIdentityIndexList" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item class="detailItem" label="标识">
        <a-input
          :addon-before="addonbefore"
          v-model="form.identify"
          :disabled="identityflag"
          @change="identifyChange()"
        />
      </a-form-item>
      <a-form-item class="detailItem" label="请选择模板">
        <a-select @change="selectTemplate" v-model="form.templateName" :disabled="disabledSelect">
          <a-select-option v-for="(item, index) in templateList" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <editTable
        ref="editTable"
        :identifyId="identifyId"
        :identity="identity"
        :master_userData="master_userData"
        :addOrEditFlag1="addOrEditFlag1"
        :listData="listData"
      ></editTable>
    </a-form>
  </a-modal>
</template>

<script>
import editTable from './editTable'
import axios from 'axios'
import api from '@/api/index.js'
import { tm_deleteAction, tm_postAction, tm_getAction, tm_fakegetAction } from '@/api/templateApi'
import { im_postAction, im_deleteAction, im_getAction, im_putAction } from '@/api/icimApi'
import { e_postAction, e_getAction, e_putAction, e_deleteAction } from '@/api/eduApi'
import { Message } from 'element-ui'
import qs from 'qs'
export default {
  components: { editTable },
  props: ['tableId', 'tableName1', 'listOfTemplate'],
  data() {
    return {
      identityflag: false,
      disabledSelect: false,
      title: '',
      addOrEditFlag1: 1,
      data: [],
      master_userData: [],
      identify: '',
      addonbefore: '',
      visible: false,
      identityIndexList: [],
      threeNodes: {},
      threeIdentityIndexList: [],
      templateList: [],
      // form: this.$form.createForm(this),
      form: {
        identityIndex: '',
        identify: '',
        templateName: '', //表名
        templateId: '', //表id
      },
      identity: '',
      listData: [],
      // flag: true,
      checkEditId: '', //在identity的数据id
      identifyId: '',
      templateId: '',
      editTableData: [],
    }
  },
  created() {
    this.identify = ''
  },
  methods: {
    // 获取所有节点数据
    getAllNodeDatas() {
      this.identityIndexList = []
      this.threeNodes = {}
      this.threeIdentityIndexList = []
      e_getAction(`/thirdNode/allnodes`).then((res) => {
        res.result.forEach((item) => {
          this.identityIndexList.push(item.nodePrefix)
        })
      })
      e_getAction(`/thirdNode/allnodes`).then((res) => {
        this.threeNodes = res
        console.log('this.threeNodes', this.threeNodes)
      })
    },
    // 查询下拉款孩子节点
    selectChildrenNodes(val) {
      for (let i = 0; i < this.threeNodes.result.length; i++) {
        if (this.threeNodes.result[i].nodePrefix == val) {
          var threeSelect = []
          for (let j = 0; j < this.threeNodes.result[i].children.length; j++) {
            threeSelect.push(this.threeNodes.result[i].children[j].nodePrefix)
          }
          console.log('threeSelect4444', threeSelect)
          this.threeIdentityIndexList = threeSelect
        }
      }
    },
    //获取tableData
    getTableData(record) {
      console.log('record.id', record.id)
      var identity = {
        identity: record.identity,
      }

      tm_getAction('/cgform/api/form/table_name/' + this.tableName1 + '/' + record.id)
        .then((res) => {
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
      this.visible = true
      this.addOrEditFlag1 = addOrEditFlag
      if (this.addOrEditFlag1 == 1) {
        this.title = '新增'
        this.disabledSelect = false
        this.identityflag = false
        this.getAllNodeDatas()
        //
        // this.$nextTick(() => {
        //   this.$refs.editTable.getList(this.listData)
        // })
      } else if (this.addOrEditFlag1 == 2) {
        console.log('编辑的record', record)
        this.title = '编辑'
        this.identityflag = true
        this.disabledSelect = true
        this.getAllNodeDatas()
        this.form.identityIndex = record.identityIndex
        console.log('identityIndex', this.form.identityIndex)
        this.addonbefore = record.identityIndex.split('/')[0] + '/'
        this.form.identify = record.identityData
        console.log('this.form.identify', this.form.identify)
        this.identifyId = record.id
        this.identity = record.identification
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        im_postAction('/identityManage/identityParam', JSON.stringify(record))
          .then((res) => {
            console.log('res', res)
            this.form.templateName = res.data.result.tableName + '(' + res.data.result.tableTxt + ')'
            console.log('this.form.templateName', this.form.templateName)
            this.checkEditId = res.data.result.id
            console.log('this.checkEditId', this.checkEditId)
            this.$refs.editTable.editTableData(res)
          })
          .catch((err) => {
            console.log(err)
          })
      }
    },
    handleCancel() {
      this.visible = false
      // this.form = {}
      this.identityIndexList = []
      this.threeNodes = {}
      this.threeIdentityIndexList = []
      this.listData = []
      this.addonbefore = ''
      this.master_userData = []
      console.log('this.form', this.form)
      Object.keys(this.form).map((key) => (this.form[key] = ''))
      // this.$refs.editTable.master_user.data = []
      // console.log("Cancel的master_userData",this.master_userData);
      // console.log("Cancel的master_user.data",this.$refs.editTable.master_user.data);
    },
    handleOk() {
      console.log('addOrEditFlag1', this.addOrEditFlag1)
      if (this.addOrEditFlag1 == 1) {
        var data = {
          paramName: 'identity',
          paramType: '标识编码',
          paramValue: this.form.identityIndex + '/' + this.form.identify,
        }
        console.log('data', data)
        this.editTableData = this.$refs.editTable.master_user.data
        this.editTableData.push(data)
        console.log('edddd', this.editTableData)
        var postData = {}
        this.$set(postData, 'identity', this.form.identityIndex + '/' + this.form.identify)
        for (let index = 0; index < this.editTableData.length; index++) {
          this.$set(postData, this.editTableData[index].paramName, this.editTableData[index].paramValue)
        }
        console.log('postData', postData)
        // axios.defaults.headers.post['Content-Type'] = 'application/json'
        tm_postAction('/cgform/api/form/' + this.form.templateId, postData)
          .then((res) => {
            console.log('res', res)

            if (res.code == 200) {
              Message({
                type: 'success',
                showClose: true,
                message: '新增成功!',
              })
            } else if (res.code == 500) {
              Message({
                type: 'warning',
                showClose: true,
                message: res.message + ',新增失败!',
              })
            } else {
              Message({
                type: 'warning',
                showClose: true,
                message: res.message,
              })
            }
            this.$emit('ok')
          })
          .catch((err) => {
            console.log('err')
          })
        this.form = {}
        this.addonbefore = ''
        // this.$refs.editTable.master_user.data = []
        this.master_userData = []
      } else if (this.addOrEditFlag1 == 2) {
        //编辑界面操作
        this.editTableData = this.$refs.editTable.master_user.data
        console.log('this.editTableData', this.editTableData)
        var postData = {}
        this.$set(postData, 'id', this.checkEditId)
        this.$set(postData, 'identity', this.form.identityIndex + '/' + this.form.identify)
        for (let index = 0; index < this.editTableData.length; index++) {
          this.$set(postData, this.editTableData[index].paramName, this.editTableData[index].paramValue)
        }
        console.log('postData', postData)
        console.log('listOfTemplate', this.listOfTemplate[this.form.templateName])
        this.templateId = this.listOfTemplate[this.form.templateName]
        axios.defaults.headers.put['Content-Type'] = 'application/json'
        im_putAction('/admin/cgform/api/form/' + this.templateId, JSON.stringify(postData))
          .then((res) => {
            console.log('res', res)
            if (res.data.success) {
              Message({
                type: 'success',
                showClose: true,
                message: '修改成功!',
              })
            } else {
              Message({
                type: 'error',
                showClose: true,
                message: '修改失败!',
              })
            }
            this.$emit('ok')
          })
          .catch((err) => {
            console.log('err')
          })
        this.form = {}
        this.addonbefore = ''
        // this.$refs.editTable.master_user.data = []
        this.master_userData = []
      }
      this.visible = false
    },
    handleIdentityIndex(val) {
      this.addonbefore = val + '/'
    },
    selectTemplate(val) {
      console.log('更换模板!!!')
      console.log('选定的表名', this.form.templateName)
      this.form.templateId = this.listOfTemplate[this.form.templateName]
      console.log('选定的表id', this.form.templateId)
      // listData  属性 和 属性类别  identity 标识编码
      //tm_fakegetAction
      tm_getAction('/cgform/field/listByHeadId/', { headId: this.form.templateId })
        .then((res) => {
          this.listData = res.result
          console.log('listData', this.listData)
          this.$nextTick(() => {
            console.log('next')
            this.$refs.editTable.getList(this.listData)
          })
        })
        .catch((err) => {
          console.log(err)
        })

      //调用table的getList(listData)
    },
    identifyChange() {
      this.identity = this.form.identityIndex + '/' + this.form.identify
    },
  },
  watch: {
    listOfTemplate: function () {
      let data = []
      console.log('listOfTemplate2', this.listOfTemplate)
      for (let name in this.listOfTemplate) {
        data.push(name)
      }
      this.templateList = data
      console.log(this.templateList)
    },
  },
}
</script>
<style>
.detailItem > .ant-form-item-label {
  display: flex !important;
}
</style>
