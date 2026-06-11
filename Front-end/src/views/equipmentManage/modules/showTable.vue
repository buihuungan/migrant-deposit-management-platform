<template>
  <div>
    <a-table
      :bordered="true"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      :rowKey="(record) => record.id"
    >
      <span slot="action" slot-scope="text, record, index">
        <a @click="openDetail(record)">详情</a>
        <a-divider type="vertical" />
        <a @click="editEquip(record)">编辑</a>
        <a-divider type="vertical" />
        <a-popconfirm title="确定删除吗?" cancelText="取消" okText="确定" @confirm="() => deleteEquip(record)">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a>删除</a>
        </a-popconfirm>
      </span>
    </a-table>
    <addIdentification ref="modalform2" @ok="handleOk" :listOfTemplate="listOfTemplate"></addIdentification>
    <detail ref="modal"></detail>
  </div>
</template>
<script>
import { im_postAction, im_deleteAction, im_getAction, im_putAction } from '@/api/icimApi'

import detail from './../../identification/modules/detail'
import { tm_getAction } from '@/api/templateApi'
import addIdentification from './../../identification/modules/addIdentification'
import axios from 'axios'
import { Message } from 'element-ui'
export default {
  name: 'showTable',
  components: {
    addIdentification,
    detail,
  },
  props: ['columns', 'dataSource', 'dataModel'],
  data() {
    return {
      addOrEditFlag: 1,
      ipagination: {
        pageSize: 10,
        showTotal: (total, range) => {
          return '当前' + range[0] + '-' + range[1] + '条' + '\xa0\xa0共' + total + '条'
        },
      },
      deleteId: '',
      identityIndex: '88.175.111',
      listOfTemplate: [],
    }
  },
  methods: {
    handleOk() {
      this.$emit('ok')
    },
    openDetail(record) {
      var identityData = record.identity.substring(11)
      console.log(identityData)
      var record1 = {
        dataModel: this.dataModel,
        identification: record.identity,
        identityIndex: this.identityIndex,
        identityData: identityData,
      }
      console.log(record1)
      this.$refs.modal.openModal(record1)
    },
    //编辑文件
    editEquip(record) {
      this.getTemplate()
      this.addOrEditFlag = 2
      var identityData = record.identity.substring(11)
      console.log(identityData)
      var record1 = {
        dataModel: this.dataModel,
        identification: record.identity,
        identityIndex: this.identityIndex,
        identityData: identityData,
      }
      console.log(record1)
      this.$refs.modalform2.openModal(record1, this.addOrEditFlag)
    },
    //删除数据 异步 async
    async deleteEquip(record) {
      console.log('record获取选中行的数据：', record)
      var record1 = {
        identification: record.identity,
        dataModel: this.dataModel,
      }
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      await im_postAction('/identityManage/identityParam', JSON.stringify(record1))
        .then((res) => {
          // console.log('res', res)
          this.deleteId = res.data.result.id
        })
        .catch((err) => {
          console.log(err)
        })
      await im_deleteAction('admin/cgform/api/form/' + this.dataModel + '/' + this.deleteId)
        .then((res) => {
          // console.log('deleteActionres', res)
          // let resultmessage = res.data.message.split(',')
          // console.log('resultmessage',resultmessage);
          if (res.data.code === 200 || res.data.code === 4) {
            Message({
              type: 'success',
              showClose: true,
              message: '删除成功!',
            })
          } else {
            Message({
              type: 'error',
              showClose: true,
              message: res.data.message + ',删除失败!',
            })
          }
          this.handleOk()
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    getTemplate(arg) {
      //, { pageNo: this.ipagination.current, tableName: arg }
      tm_getAction('/cgform/head/list')
        .then((res) => {
          let loadData = res.result.records
          console.log('loadData的res', loadData)
          let _listData = {}
          for (let i = 0; i < loadData.length; i++) {
            // let data = {id:'',tableName:''}
            // this.$set(data,'id',loadData[i].id)
            // this.$set(data,'tableName',loadData[i].tableName)
            // _listData.push(data)
            let str = loadData[i].tableName + '(' + loadData[i].tableTxt + ')'
            _listData[str] = loadData[i].id
          }
          this.listOfTemplate = _listData
          console.log('listOfTemplate1', this.listOfTemplate)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    openModal() {},
  },
}
</script>