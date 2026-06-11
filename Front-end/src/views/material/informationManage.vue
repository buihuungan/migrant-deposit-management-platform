<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="标识">
          <a-input v-model="identification" />
        </a-form-item>
        <a-form-item label="类别名">
          <a-select v-model="table_txt" style="width: 200px">
            <a-select-option v-for="item in NameOfTemplate" :key="item.id" :value="item.tableTxt">
              {{ item.tableTxt }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button-group>
            <a-button type="primary" icon="search" @click="show(1)">查询</a-button>
            <a-button type="primary" icon="reload" @click="reData()">重置</a-button>
          </a-button-group>
        </a-form-item>
      </a-form>
      <div class="button-group">
        <a-form-item>
          <a-button icon="carry-out" type="primary" @click="handleAdd()">采购</a-button>
        </a-form-item>
      </div>
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
      </div>
    </a-card>
    <addAndEditPDS ref="modalform1"> </addAndEditPDS>
    <addIdentification ref="modalform2" :listOfTemplate="listOfTemplate"></addIdentification>
    <detail ref="modal"></detail>
  </div>
</template>
<script>
import { im_postAction, im_deleteAction, im_getAction, im_putAction } from '@/api/icimApi'
import detail from './../identification/modules/detail'
import { tm_getAction } from '@/api/templateApi'
import addIdentification from './../identification/modules/addIdentification'
import addAndEditPDS from './../procurementPlan/modules/addAndEditPDS.vue'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import axios from 'axios'
import { Message } from 'element-ui'
export default {
  name: 'informationManage',
  components: {
    addAndEditPDS,
    addIdentification,
    detail,
  },
  data() {
    return {
      table_txt: '',
      identification: '',
      addOrEditFlag: 1,
      dataSource: [],
      columns: [
        {
          title: '标识',
          align: 'center',
          dataIndex: 'identification',
        },
        {
          title: '类别模板',
          align: 'center',
          dataIndex: 'table_name',
        },
        {
          title: '类别名',
          align: 'center',
          dataIndex: 'table_txt',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      ipagination: {
        current: 0,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
        // showSizeChanger: true,
        total: 0,
      },
      deleteId: '',
      identityIndex: '88.175.111',
      listOfTemplate: [],
      NameOfTemplate: [],
    }
  },
  mounted() {
    this.show(1)
    this.getTemplate()
  },
  methods: {
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.show()
    },
    handleOk() {
      this.show(1)
    },
    //采购
    handleAdd() {
      this.addOrEditFlag = 1
      this.$refs.modalform1.openModal('', this.addOrEditFlag)
    },
    openDetail(record) {
      var identityData = record.identification.substring(11)
      console.log(identityData)
      var record1 = {
        dataModel: record.data_model,
        identification: record.identification,
        identityIndex: this.identityIndex,
        identityData: identityData,
      }
      console.log(record1)
      this.$refs.modal.openModal(record1)
    },
    reData() {
      this.table_txt = ''
      this.identification = ''
    },
    //展示列表
    show(arg) {
      this.dataSource=[]
      console.log('arg', arg)
      if (arg === 1) {
        this.ipagination.current = 1
      }
      let params = { order: 'desc', pageNo: this.ipagination.current, pageSize: 10 }
      if (this.table_txt) {
        params.table_txt = this.table_txt
      }
      if (this.identification) {
        params.identification = this.identification
      }
      o_getAction('/cgreport/api/getColumnsAndData/1420335511824461826', params).then((res) => {
        this.dataSource = res.result.data.records
        this.ipagination.total = res.result.data.total
        console.log(this.dataSource)
      })
    },
    //编辑文件
    editEquip(record) {
      this.addOrEditFlag = 2
      var identityData = record.identification.substring(11)
      console.log(identityData)
      var record1 = {
        dataModel: record.data_model,
        identification: record.identification,
        identityIndex: this.identityIndex,
        identityData: identityData,
      }
      console.log('2222',record1)
      this.$refs.modalform2.openModal(record1, this.addOrEditFlag)
    },
    //删除数据 异步 async
    async deleteEquip(record) {
      console.log('record获取选中行的数据：', record)
      var record1 = {
        identification: record.identification,
        dataModel: record.data_model,
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
      await im_deleteAction('admin/cgform/api/form/' + record.data_model + '/' + this.deleteId)
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
          this.show(1)
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
          this.NameOfTemplate = loadData
          let _listData = {}
          for (let i = 0; i < loadData.length; i++) {
            // let data = {id:'',tableName:''}
            // this.$set(data,'id',loadData[i].id)
            // this.$set(data,'tableName',loadData[i].tableName)
            // _listData.push(data)
            let str = loadData[i].tableName + '(' + loadData[i].tableTxt + ')';
            _listData[str] = loadData[i].id
            // _listData[loadData[i].tableName] = loadData[i].id
          }
          this.listOfTemplate = _listData
          console.log('listOfTemplate1', this.listOfTemplate)
        })
        .catch((err) => {
          console.log(err)
        })
    },
  },
}
</script>