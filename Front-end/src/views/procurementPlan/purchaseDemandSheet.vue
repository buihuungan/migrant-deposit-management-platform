<template>
  <!-- 采购需求单 -->
  <div class="purchase_demand_sheet" style="display: grid">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item label="标识编码：" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
              <a-input placeholder="请输入关键字搜索" v-model="searchIdentity"></a-input>
            </a-form-item>
          </a-col>
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
    </div>
    <div class="article-table" style="overflow: auto">
      <a-table
        style="height: 500px"
        ref="table"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
      >
        <span slot="action" slot-scope="text, record">
          <div v-if="!record.hasOwnProperty('createBy')">
            <a @click="chooseSupplier(record)">选择供应商</a>
            <a-divider type="vertical" />
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm title="确定删除吗?" cancelText="取消" okText="确定" @confirm="() => handleDelete(record)">
              <a-icon slot="icon" type="question-circle-o" style="color: red" />
              <a>删除</a>
            </a-popconfirm>
          </div>
        </span>
      </a-table>
    </div>
    <addAndEditPDS ref="modalform" @ok="handleOk"> </addAndEditPDS>
    <chooseSupplier ref="modalform2" @ok="handleOk2"> </chooseSupplier>
  </div>
</template>

<script>
import api from '@/api/index'
import { tm_postAction } from '@/api/templateApi'
import addAndEditPDS from './modules/addAndEditPDS'
import chooseSupplier from './modules/chooseSupplier'
import axios from 'axios'
import { Message } from 'element-ui'
import qs from 'qs'
import { order_getAction } from '@/api/orderApi'
import { o_deleteAction, o_putAction, o_postAction } from '@/api/onApi'
export default {
  components: {
    addAndEditPDS,
    chooseSupplier,
  },
  data() {
    return {
      onlineId: '597983d3ade2449aa91338e05148332a',
      addOrEditFlag: 1,
      columns: [
        {
          title: '标识编码',
          width: '20%',
          dataIndex: 'identity',
        },
        {
          title: '名称',
          width: '15%',
          dataIndex: 'name',
        },
        {
          title: '型号',
          width: '15%',
          dataIndex: 'model',
        },
        {
          title: '单位',
          width: '15%',
          dataIndex: 'unit',
        },
        {
          title: '数量',
          width: '15%',
          dataIndex: 'number',
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
      dataSource: [],
      ipagination: {
        current: 1,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        pageSize: 10,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
      searchIdentity: '',
      inSearch: false,
      records: {},
      materialIdentity: '',
      insertMaterialData: {
        identity: '',
        materials_model: '',
        name: '',
        pn_code: '',
        unit: '',
      },
    }
  },
  created() {
    this.getAllData()
  },
  methods: {
    //查询操作
    searchQuery() {
      this.inSearch = true
      order_getAction(`/purchaseDemand/material/`, {
        chooseSupplier: 1,
        identity: this.searchIdentity,
        pageNo: this.pageNo,
        pageSize: this.pageSize,
      })
        .then((res) => {
          this.dataSource = []
          res.result.data.forEach((element, index) => {
            var dataList = {}
            dataList.id = element.id
            dataList.key = element.id
            dataList.orderIdentity = element.orderIdentity
            if (element.purchaseOrderMaterialDetails.length > 0) {
              dataList.identity = element.purchaseOrderMaterialDetails[0].identity
              dataList.model = element.purchaseOrderMaterialDetails[0].model
              dataList.name = element.purchaseOrderMaterialDetails[0].name
              dataList.number = element.purchaseOrderMaterialDetails[0].number
              dataList.unit = element.purchaseOrderMaterialDetails[0].unit
              element.purchaseOrderMaterialDetails.forEach((element) => {
                element.key = element.id
              })
              dataList.children = element.purchaseOrderMaterialDetails
            }
            this.dataSource.push(dataList)
          })
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    searchReset() {
      this.inSearch = false
      this.searchIdentity = ''
      this.pageSize = 10
      this.pageNo = 1
      this.getAllData()
    },

    getAllData() {
      order_getAction(`/purchaseDemand/material`, {
        chooseSupplier: 1,
        identity: '',
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        exampleId: this.$store.state.exampleId
      })
        .then((res) => {
          this.dataSource = []
          res.result.data.forEach((element, index) => {
            var dataList = {}
            dataList.id = element.id
            dataList.key = element.id
            dataList.orderIdentity = element.orderIdentity
            dataList.orderStatus = element.orderStatus
            if (element.purchaseOrderMaterialDetails.length > 0) {
              dataList.identity = element.purchaseOrderMaterialDetails[0].identity
              dataList.model = element.purchaseOrderMaterialDetails[0].model
              dataList.name = element.purchaseOrderMaterialDetails[0].name
              dataList.number = element.purchaseOrderMaterialDetails[0].number
              dataList.unit = element.purchaseOrderMaterialDetails[0].unit
              element.purchaseOrderMaterialDetails.forEach((element) => {
                element.key = element.id
              })
              dataList.children = element.purchaseOrderMaterialDetails
            }
            this.dataSource.push(dataList)
          })
          this.ipagination.total = res.result.total
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    //新增
    handleAdd() {
      this.addOrEditFlag = 1
      this.$refs.modalform.openModal('', this.addOrEditFlag)
    },
    //编辑
    handleEdit(record) {
      this.records = record.children
      this.addOrEditFlag = 2
      this.$refs.modalform.openModal(record, this.addOrEditFlag)
    },
    //选择供应商
    chooseSupplier(record) {
      this.$refs.modalform2.openModal(record)
    },

    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.pageNo = page
      this.pageSize = pageSize
      if (this.inSearch) {
        //处于查询状态，分页数据不能再次重新查询
        this.searchQuery()
      } else {
        this.getAllData()
      }
    },
    //删除数据
    handleDelete(record) {
      o_deleteAction(`/cgform/api/form/${this.onlineId}/${record.id}`)
        .then((res) => {
          if (res.success) {
            Message({
              showClose: true,
              message: '删除成功！',
              type: 'success',
            })
            this.getAllData()
          } else {
            Message({
              showClose: true,
              message: '删除失败！刷新后再次尝试！',
              type: 'error',
            })
          }
        })
        .catch((err) => {
          Message({
            showClose: true,
            message: '删除失败！',
            type: 'error',
          })
        })
    },
    handleOk() {
      this.getAllData()
    },
    handleOk2(row) {
      console.log('row', row)
      var materialData = row.purchase_order_material_details
      for (let i = 0; i < materialData.length; i++) {
        console.log('materialData', materialData)
        for (let j = 0; j < materialData[i].number; j++) {
          axios({
            method: 'get',
            baseURL: api.server_url + '/identity',
            url: `/docking/material`,
          })
            .then((res) => {
              if (res.status === 200) {
                this.insertMaterialData.name = materialData[i].name
                if (materialData[i].model != null) {
                  this.insertMaterialData.materials_model = materialData[i].model
                } else {
                  this.insertMaterialData.materials_model = ''
                }
                if (materialData[i].unit != null) {
                  this.insertMaterialData.unit = materialData[i].unit
                } else {
                  this.insertMaterialData.unit = ''
                }
                this.insertMaterialData.pn_code = materialData[i].pn_code
                this.insertMaterialData.identity = res.data.result.identity
                  // '88.165.333/' + materialData[i].identity + this.insertMaterialData.identity.substr(14, 18)
                  row.supplier_identity + '/' + materialData[i].identity + this.insertMaterialData.identity.substr(14, 18)
                tm_postAction('/cgform/api/form/868e5cb6dabb4146afdabc3fb4ff4c0a', this.insertMaterialData).then(
                  (res) => {}
                )
              }
            })
            .catch((err) => {
              console.log('oooooooooerr', err)
            })
        }
      }
      console.log('pppppppp', row)
      let a = Object.assign({}, row)
      let b = JSON.parse(JSON.stringify(row))
      console.log(':aaaaaa', a)
      // this.$delete(b.purchase_order_material_details[0],'key')
      b.shq_id = ''
      b.example_id = this.$store.state.exampleId
      o_putAction(`/cgform/api/form/` + this.onlineId + '?tabletype=1', b)
        .then((res) => {
          if (res.success) {
            Message({
              showClose: true,
              message: '供应商已选择，该条数据流至代签采购订单页面',
              type: 'success',
            })
            this.getAllData()
          } else {
            Message({
              showClose: true,
              message: '数据修改失败，刷新后重新选择供应商！',
              type: 'error',
            })
          }
        })
        .catch((err) => {
          Message({
            showClose: true,
            message: '出现错误，刷新重新尝试！',
            type: 'error',
          })
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
.purchase_demand_sheet {
  background: #fff;
  padding: 24px;
}
</style>
