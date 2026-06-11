<template>
  <div class="Identification" style="display: grid">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item label="标识编码：" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
              <a-input placeholder="请输入关键字搜索" v-model="form.index"></a-input>
            </a-form-item>
          </a-col>
          <!-- <a-col :md="10" :sm="10">
            <a-form-item
              label="创建时间:"
            >
              <el-date-picker
                v-model="form.dateTime"
                type="daterange"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
              </el-date-picker>
            </a-form-item>
          </a-col> -->
          <a-col :md="6" :sm="6">
            <a-form-item>
              <a-button type="primary" @click="searchQuery">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator" style="margin: 5px 0 10px 2px">
      <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">新增</a-button>
      <a-button class="ant-btn" type="primary" @click="upload"> <a-icon type="cloud-upload" />导入 </a-button>
      <a-button class="ant-btn" type="primary" @click="multipleDelete(deleteOptions)" v-if="selectedRowKeys.length > 0"
        ><a-icon type="delete" />批量删除</a-button
      >
      <a-button type="primary" style="float: right" @click="openScan"><a-icon type="file-search" />扫描导入</a-button>
    </div>
    <div class="article-table">
      <a-table
        style="height: 500px"
        ref="table"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="openDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" cancelText="取消" okText="确定" @confirm="() => handleDelete(record)">
            <a-icon slot="icon" type="question-circle-o" style="color: red" />
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>
    <addIdentification ref="modalform" @ok="handleOk" :listOfTemplate="listOfTemplate"></addIdentification>
    <detail ref="modal"></detail>
    <upload ref="upload" @ok="handleOk"></upload>
    <scanning ref="scanning"></scanning>
  </div>
</template>

<script>
import addIdentification from './modules/addIdentification'
// import upload from "./modules/upload";
import detail from './modules/detail'
import scanning from './modules/scanning'
import axios from 'axios'
import upload from './modules/upload'
import { tm_getAction, tm_fakegetAction } from '@/api/templateApi'
import { im_postAction, im_deleteAction, im_getAction, im_putAction } from '@/api/icimApi'
import { Message } from 'element-ui'
import qs from 'qs'
export default {
  components: {
    addIdentification,
    detail,
    upload,
    scanning,
  },
  data() {
    return {
      deleteOptions: [],
      selectedRowKeys: [],
      form: {
        index: '',
        dateTime: '',
      },
      querynum: 1,

      addOrEditFlag: 1,

      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: '4%',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '前缀',
          align: 'center',
          width: '12%',
          dataIndex: 'identityIndex',
        },
        {
          title: '标识',
          align: 'center',
          width: '40%',
          dataIndex: 'identification',
        },
        {
          title: '创建时间',
          align: 'center',
          width: '12%',
          dataIndex: 'createTime',
        },
        {
          title: '更新时间',
          align: 'center',
          width: '12%',
          dataIndex: 'updateTime',
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '20%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      pageNo: 1,
      pageSize: 10,
      listOfTemplate: [],

      dataSource: [],
      deleteId: '',
      ipagination: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        pageSize: 10,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
    }
  },
  created() {
    this.getAllIdentites()
  },
  methods: {
    //批量删除
    async multipleDelete(deleteOptions) {
      console.log('deleteOptions', deleteOptions)
      let tempdelete = []
      for (let i = 0; i < deleteOptions.length; i++) {
        tempdelete.push({
          dataModel: deleteOptions[i].dataModel,
          id: deleteOptions[i].id,
          identification: deleteOptions[i].identification,
        })
      }
      const confirmResult = await this.$elementConfirm('确定删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).catch((err) => {
        console.log('err', err)
      })
      if (confirmResult === 'confirm') {
        im_postAction(`/identityManage/batchRemove`, tempdelete).then((res) => {
          console.log('res', res)
          if (res.data.success === true) {
            this.$message.success('删除成功')
            this.selectedRowKeys = []
            this.getAllIdentites()
          } else {
            this.$message.info('删除失败')
          }
        })
      } else {
        this.$message.info('已取消删除')
      }
    },
    onSelectChange(selectedRowKeys, row) {
      console.log('selectedRowKeys changed: ', selectedRowKeys)

      this.selectedRowKeys = selectedRowKeys
      this.deleteOptions = row
      console.log('rowwwww changed: ', row)
    },
    //查询操作
    searchQuery() {
      axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
      var queryDateInfo = {}
      queryDateInfo.key = this.form.index
      queryDateInfo.pageNumber = 0
      queryDateInfo.pageSize = 9999
      // queryDateInfo.beginTime = this.form.dateTime[0];
      // queryDateInfo.endTime = this.form.dateTime[1];
      // console.log("queryDateInfo",queryDateInfo)
      // axios.defaults.headers.post['Content-Type'] = 'application/json'
      im_postAction('/identityManage/identitySearch', qs.stringify(queryDateInfo))
        .then((res) => {
          console.log('res', res)
          this.dataSource = res.data.result.list.records

          // console.log("dataSource",this.dataSource);
          this.ipagination.total = res.data.result.total
          this.querynum = 0
        })
        .catch((err) => {
          console.log(err)
        })
    },
    searchReset() {
      this.form = {}
      this.getAllIdentites()
      this.querynum = 1
    },

    getAllIdentites() {
      var that = this
      axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
      im_postAction(
        `/identityManage/allIdentities`,
        qs.stringify({ pageNo: that.pageNo, pageSize: that.pageSize })
      ).then((res) => {
        console.log('res654321', res)
        that.ipagination.total = res.data.result.total
        that.dataSource = res.data.result.list.records
        // for (let i = 0; i < res.data.result.list.records.length; i++) {
        //   that.dataSource[i].key = res.data.result.list.records[i];

        // }
        console.log('dataSource', that.dataSource)
      })
    },
    handleAdd() {
      this.addOrEditFlag = 1
      this.getTemplate()
      this.$refs.modalform.openModal('', this.addOrEditFlag)
    },
    //编辑
    handleEdit(record) {
      this.addOrEditFlag = 2
      this.getTemplate()
      this.$refs.modalform.openModal(record, this.addOrEditFlag)
    },
    openDetail(record) {
      this.$refs.modal.openModal(record)
    },

    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.pageNo = page
      this.pageSize = pageSize
      if (this.querynum == 1) {
        this.getAllIdentites()
      }
    },
    //删除数据 异步 async
    async handleDelete(record) {
      console.log('record获取选中行的数据：', record)
      this.tableId = record.dataModel //table的id
      console.log('this.tableId', this.tableId)
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      await im_postAction('/identityManage/identityParam', JSON.stringify(record))
        .then((res) => {
          // console.log('res', res)
          this.deleteId = res.data.result.id
          // console.log('this.deleteId', this.deleteId)
        })
        .catch((err) => {
          console.log(err)
        })
      await im_deleteAction('admin/cgform/api/form/' + this.tableId + '/' + this.deleteId)
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
          this.getAllIdentites()
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    handleOk() {
      this.getAllIdentites()
    },
    upload() {
      this.$refs.upload.openUpload()
    },
    openScan() {
      this.$refs.scanning.openScan()
    },
    getTemplate(arg) {
      //, { pageNo: this.ipagination.current, tableName: arg }
      //tm_fakegetAction
      tm_getAction('/cgform/head/list', { pageSize: 100 })
        .then((res) => {
          let loadData = res.result.records
          console.log('loadData的res123', loadData)
          let _listData = {}
          for (let i = 0; i < loadData.length; i++) {
            // let data = {id:'',tableName:''}
            // this.$set(data,'id',loadData[i].id)
            // this.$set(data,'tableName',loadData[i].tableName)
            // _listData.push(data)
            let str = loadData[i].tableName + '(' + loadData[i].tableTxt + ')'
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

<style scoped>
.ant-col-sm-24 {
  height: 40px;
  line-height: 40px;
}
.el-range-input {
  margin-left: 15px !important;
}
.Identification {
  background: #fff;
  /* margin:24px 16px; */
  padding: 24px;
}
</style>
