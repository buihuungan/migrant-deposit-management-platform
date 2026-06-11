<template>
  <!-- 待签采购订单 -->
  <div class="purchase_order_to_be_signed" style="display: grid">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item label="订单标识编码：" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
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
      <el-link
        type="primary"
        icon="download"
        :href="`${serverUrl}/plm/common/download${contractExample_url}`"
        @click="handleAdd()"
        >下载合同模板</el-link
      >
    </div>
    <div class="article-table">
      <a-table
        style="height: 500px"
        ref="table"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
      >
        <span slot="action" slot-scope="text, record">
          <a-upload
            name="multipartFile"
            :action="`/plm/common/upload/`"
            :showUploadList="false"
            @change="handleChange(record, arguments)"
          >
            <a>上传合同</a>
          </a-upload>

          <a-divider type="vertical" />
          <a @click="showDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a :disabled="record.contract_name == '' ? true : false" @click="generatePurchaseOrder(record)"
            >生成采购订单</a
          >
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" cancelText="取消" okText="确定" @confirm="() => handleDelete(record)">
            <a-icon slot="icon" type="question-circle-o" style="color: red" />
            <a>删除</a>
          </a-popconfirm>
        </span>
        <a-table
          :columns="columns1"
          :dataSource="record.purchase_order_material_details"
          :pagination="false"
          slot="expandedRowRender"
          slot-scope="record"
          style="margin: 0"
        >
        </a-table>
      </a-table>
    </div>
    <detailPotbs ref="modalform"></detailPotbs>
  </div>
</template>

<script>
import { putAction } from '@api/manage'
import { tm_postAction } from '@/api/templateApi'
import detailPotbs from './modules/detailPotbs'
import { Message } from 'element-ui'
import { USER_NAME } from '@/store/mutation-types'
import { order_getAction, order_putAction, order_postAction } from '@/api/orderApi'
import api from '@/api/index'
import Vue from 'vue'
import { USER_ID } from '@/store/mutation-types'
import { o_putAction } from '@/api/onApi'
import { tm_fakepostAction } from '@/api/templateApi.js'
import qs from 'qs'
import { o_postAction } from '../../api/onApi'
import axios from 'axios'
export default {
  components: {
    detailPotbs,
  },
  data() {
    return {
      serverUrl: api.server_url,
      onlineId: '597983d3ade2449aa91338e05148332a',
      contractId: '74e638063d34429b88f48efbfb2315b2',
      mbid: '5893ae09a5014705b1c9459c7e24aa87', //给大屏展示加一条数据
      demand_identity_id: '0360e6b38be4443b858e667eb207f785', //采购标识表
      addOrEditFlag: 1,
      columns: [
        {
          title: '订单标识编码',
          align: 'center',
          width: '20%',
          dataIndex: 'order_identity',
        },
        {
          title: '供应商标识编码',
          align: 'center',
          width: '20%',
          dataIndex: 'supplier_identity',
        },
        {
          title: '供应商名称',
          align: 'center',
          width: '22%',
          dataIndex: 'supplier_name',
        },
        {
          title: '供应商评分',
          align: 'center',
          width: '8%',
          dataIndex: 'supplier_rate',
        },
        {
          title: '操作',
          align: 'center',
          width: '30%',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      columns1: [
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
      contractExample_url: '/contract_example_1652338251504.doc',
      //contractExample_url: '/contract_example_1652338296783.doc',   ii2模板地址
      logistics_code: '',
    }
  },
  created() {
    this.getAllData()
  },
  methods: {
    //查询操作
    searchQuery() {
      this.inSearch = true
      order_getAction(`/purchaseDemand/orderIdentity?orderIdentity=${this.searchIdentity}`).then((res) => {
        this.dataSource = []
        var element = res.result
        var dataList = {}
        dataList.id = element.id
        dataList.key = element.id
        dataList.choose_supplier = element.chooseSupplier
        dataList.contract_name = element.contractName
        dataList.contract_url = element.contractUrl
        dataList.supplier_id = element.supplierId
        dataList.supplier_goods = element.supplierGoods
        dataList.supplier_identity = element.supplierIdentity
        dataList.supplier_name = element.supplierName
        dataList.supplier_rate = element.supplierRate
        dataList.order_identity = element.orderIdentity
        if (element.purchaseOrderMaterialDetails.length > 0) {
          dataList.purchase_order_material_details = element.purchaseOrderMaterialDetails
        }
        this.dataSource.push(dataList)
        this.ipagination.total = this.dataSource.length
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
        chooseSupplier: 2,
        pageNo: this.ipagination.current,
        pageSize: this.pageSize,
        exampleId: this.$store.state.exampleId
      })
        .then((res) => {
          console.log('sssssssssssssss', res)
          this.dataSource = []
          res.result.data.forEach((element, index) => {
            var dataList = {}
            dataList.id = element.id
            dataList.key = element.id
            dataList.order_identity = element.orderIdentity
            dataList.choose_supplier = element.chooseSupplier
            dataList.contract_name = element.contractName
            dataList.contract_url = element.contractUrl
            dataList.supplier_goods = element.supplierGoods
            dataList.supplier_identity = element.supplierIdentity
            dataList.supplier_name = element.supplierName
            dataList.supplier_rate = element.supplierRate
            dataList.order_identity = element.orderIdentity
            dataList.order_status = element.orderStatus
            dataList.pn_code = element.pnCode
            if (element.purchaseOrderMaterialDetails.length > 0) {
              dataList.purchase_order_material_details = element.purchaseOrderMaterialDetails
            }
            this.dataSource.push(dataList)
            // this.ipagination.total = this.dataSource.length
            this.ipagination.total = res.result.total
          })
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    //详情
    showDetail(record) {
      this.$refs.modalform.openModal(record)
    },
    //生成采购订单
    async generatePurchaseOrder(record) {
      //record.is_finish待改
      //修改合同表的该条数据合同状态
      let deal_record = JSON.parse(JSON.stringify(record))
      this.$delete(deal_record, 'key')
      deal_record.choose_supplier = 3
      let children = JSON.parse(
        JSON.stringify(deal_record.purchase_order_material_details)
          .replace(/pnCode/g, 'pn_code')
          .replace(/createBy/g, 'create_by')
          .replace(/createTime/g, 'create_time')
          .replace(/purchaseOrderId/g, 'purchase_order_id')
          .replace(/sysOrgCode/g, 'sys_org_code')
          .replace(/updateBy/g, 'update_by')
          .replace(/updateTime/g, 'update_time')
      )
      deal_record.purchase_order_material_details = children
      deal_record.example_id = this.$store.state.exampleId
      console.log('deal_record aaaaaaaaaaaaa', deal_record)
      o_putAction(`/cgform/api/form/` + this.onlineId, deal_record)
        .then((res) => {
          if (res.success) {
            Message({
              showClose: true,
              message: '生成采购订单成功，该条数据流至采购订单页面',
              type: 'success',
            })
            this.getAllData()
            let postData0 = {
              demand_identity: '88.343.000000/' + deal_record.order_identity,
              user_id: Vue.ls.get(USER_ID),
            }
            console.log('postData0', postData0)
            o_postAction(`/cgform/api/form/${this.demand_identity_id}`, postData0).then((res) => {
              if (res.success) {
                let postData = {
                  order_identity: deal_record.order_identity,
                  supplier_name: deal_record.supplier_name,
                  // supplier_goods: deal_record.supplier_goods,
                  contract_name: deal_record.contract_name,
                  supplier_identity: deal_record.supplier_identity,
                  pn_code: deal_record.pn_code,
                  // contract_url: deal_record.contract_url,
                  // name: '',
                  identity: '88.343.000000/' + deal_record.order_identity,
                  material_name: '',
                  material_model: '',
                  material_number: '',
                  material_code: '',
                  user_id: '',
                  plan_id: '',
                  lesson_id: '',
                  teaching_mode: '',
                }
                for (let i = 0; i < deal_record.purchase_order_material_details.length; i++) {
                  postData.material_name += deal_record.purchase_order_material_details[i].name + ','
                  postData.material_model += deal_record.purchase_order_material_details[i].model + ','
                  postData.material_number += deal_record.purchase_order_material_details[i].number + ','
                  postData.material_code +=
                    deal_record.supplier_identity + '/' + deal_record.purchase_order_material_details[i].identity + ','
                }
                if (postData.material_name.length > 0) {
                  postData.material_name = postData.material_name.substr(0, postData.material_name.length - 1)
                  postData.material_model = postData.material_model.substr(0, postData.material_model.length - 1)
                  postData.material_number = postData.material_number.substr(0, postData.material_number.length - 1)
                  postData.material_code = postData.material_code.substr(0, postData.material_code.length - 1)
                }
                var userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
                postData.user_id = userid.value
                var process = JSON.parse(window.localStorage.getItem('process'))
                postData.plan_id = process.planId
                postData.lesson_id = process.lessonId
                postData.teaching_mode = process.teachingMode
                tm_postAction('/cgform/api/form/c49ee2116da04ae0a190eb2c0d30dac2', postData).then((res) => {
                  console.log('生菜采购订单插入模板成功')
                })
                // for (let i = 0; i < deal_record.purchase_order_material_details.length; i++) {

                //   postData.name += deal_record.purchase_order_material_details[i].name + ','
                // }
                // if (postData.name.length > 0) {
                //   postData.name = postData.name.substr(0, postData.name.length - 1)
                // }
                // tm_fakepostAction(`/cgform/api/form/${this.mbid}`, postData).then((res) => {
                //   console.log('res', res)
                // })
                // .catch((e) => {
                //   console.log('e', e)
                //   Message({
                //     showClose: true,
                //     message: '大屏新增采购数据异常！',
                //     type: 'error',
                //   })
                // })
              }
            })
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
      //   修改合同表的该条数据合同状态
      order_putAction(`/contract/finish`, qs.stringify({ orderId: record.id }))
        .then((res) => {
          console.log('合同状态修改成功，生成采购订单')
        })
        .catch((err) => {
          console.log('err', err)
        })
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
      let deal_record = JSON.parse(JSON.stringify(record))
      this.$delete(deal_record, 'key')
      deal_record.choose_supplier = 1
      deal_record.supplier_identity = ''
      deal_record.supplier_name = ''
      deal_record.supplier_rate = ''
      deal_record.supplier_goods = ''
      deal_record.contract_name = ''
      deal_record.contract_url = ''
      deal_record.order_name = ''
      deal_record.order_identity = ''
      deal_record.example_id = this.$store.state.exampleId
      o_putAction(`/cgform/api/form/` + this.onlineId, deal_record)
        .then((res) => {
          if (res.success) {
            Message({
              showClose: true,
              message: '数据回流至采购需求单页面',
              type: 'success',
            })
            this.getAllData()
          } else {
            Message({
              showClose: true,
              message: '删除失败！',
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
    handleAdd() {
      let a = document.createElement('a')
      a.href = this.serverUrl + this.templateUrl
      a.click()
    },
    handleChange(record, args) {
      console.log('record', record)
      var params = {}
      params.supplier = record.supplier_name
      params.demander = Vue.ls.get(USER_NAME)
      params.purchaseOrderId = record.id
      params.isFinish = 1
      params.purchaseOrderIdentity = record.order_identity
      if (args[0]?.file?.response?.success && args[0]?.file?.status === 'done') {
        params.contractName = args[0].file.response.result.fileName.split('.')[0]
        params.contractLink = args[0].file.response.result.dbpath.split('/')[2]
        order_postAction(`/contract/manage`, params)
          .then((res) => {
            var that = this
            let deal_record = JSON.parse(JSON.stringify(record))
            deal_record.contract_name = params.contractName
            deal_record.contract_url = params.contractLink
            this.$delete(deal_record, 'key')
            let children = JSON.parse(
              JSON.stringify(deal_record.purchase_order_material_details)
                .replace(/pnCode/g, 'pn_code')
                .replace(/createBy/g, 'create_by')
                .replace(/createTime/g, 'create_time')
                .replace(/purchaseOrderId/g, 'purchase_order_id')
                .replace(/sysOrgCode/g, 'sys_org_code')
                .replace(/updateBy/g, 'update_by')
                .replace(/updateTime/g, 'update_time')
            )
            deal_record.purchase_order_material_details = children
            deal_record.example_id = this.$store.state.exampleId
            o_putAction(`/cgform/api/form/` + that.onlineId, deal_record)
              .then((res) => {
                if (res.success) {
                  that.getAllData()
                  Message({
                    showClose: true,
                    message: '合同上传成功！',
                    type: 'success',
                  })
                } else {
                  Message({
                    showClose: true,
                    message: '数据修改失败，刷新后重新上传合同！',
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
          })
          .catch((err) => {
            console.log('err', err)
          })
      } else if (!args[0]?.file?.response?.success && args[0]?.file?.status === 'done') {
        Message({
          showClose: true,
          message: '文件上传失败，请重新尝试！',
          type: 'error',
        })
      }
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
.purchase_order_to_be_signed {
  background: #fff;
  padding: 24px;
}
.el-link.el-link--primary {
  color: #fff !important;
  background-color: #1890ff !important;
  border-color: #1890ff !important;
  padding: 5px 15px !important;
  border-radius: 4px;
  text-shadow: 0 -1px 0 rgb(0 0 0 / 12%) !important;
  box-shadow: 0 2px 0 rgb(0 0 0 / 5%) !important;
}
</style>
