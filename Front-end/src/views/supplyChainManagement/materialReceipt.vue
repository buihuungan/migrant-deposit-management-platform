<template>
  <!-- 采购订单 -->
  <div class="purchase_order" style="display: grid">
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
          <a @click="showDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a :disabled="record.order_status === 2 || record.order_status === 0" @click="receivingGoods(record)">收货</a>
          <a-divider type="vertical" />
          <a-popconfirm
            :title="record.order_status === 0 ? '确定取消订单吗?' : '确定退货吗?'"
            cancelText="取消"
            okText="确定"
            @confirm="() => returnGoods(record)"
          >
            <a-icon slot="icon" type="question-circle-o" style="color: red" />
            <a v-if="record.order_status === 0">取消订单</a>
            <a v-if="record.order_status === 1">退货</a>
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

    <a-modal v-model="warehousingVisible" title="货物入库单" width="50%" @ok="warehousing" @cancel="handleCancle">
      <a-form-model :model="warehousingForm" :label-col="{ span: 6 }" :wrapper-col="{ span: 12 }">
        <a-form-model-item label="入库单类型">
          <a-select v-model="warehousingForm.billType">
            <a-select-option :value="1"> 原材料入库 </a-select-option>
            <a-select-option :value="2"> 成品入库 </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="订单编号">
          <a-input v-model="warehousingForm.orderId" />
        </a-form-model-item>
        <a-form-model-item label="客户">
          <a-input v-model="warehousingForm.providerId" />
        </a-form-model-item>
        <a-form-model-item label="单据日期">
          <a-date-picker style="width: 100%" :format="dateFormat" @change="onChange" />
        </a-form-model-item>
        <a-form-model-item label="送检员">
          <a-input v-model="warehousingForm.inspecter" />
        </a-form-model-item>
        <a-form-model-item label="质检员">
          <a-input v-model="warehousingForm.qcor" />
        </a-form-model-item>
        <a-form-model-item label="备注">
          <a-input v-model="warehousingForm.remark" />
        </a-form-model-item>
      </a-form-model>
      <a-table bordered :data-source="orderDataSource" :columns="columns3" :pagination="false">
        <a-select
          slot="inventoryPoint"
          slot-scope="text, record, index"
          @change="selectChange(index, arguments)"
          style="width: 100%"
        >
          <a-select-option v-for="wmsInBill in wmsInBills" :key="wmsInBill.id" :value="wmsInBill.id">
            {{ wmsInBill.unit_name }}
          </a-select-option>
        </a-select>
      </a-table>
    </a-modal>
    <deliverMaterialRecord ref="deliverMaterialRecordRef" @ok="handleOk"></deliverMaterialRecord>
  </div>
</template>

<script>
import axios from 'axios'
import { tm_postAction } from '@/api/templateApi'
import deliverMaterialRecord from '../procurementPlan/modules/deliverMaterialRecord'
import detailPotbs from '../procurementPlan/modules/detailPotbs'
import { Message } from 'element-ui'
import { order_getAction, order_putAction, order_postAction } from '@/api/orderApi'
import api from '@/api/index'
import Vue from 'vue'
import pick from 'lodash.pick'
import { o_getAction, o_postAction, o_putAction } from '../../api/onApi'
import { w_postAction } from '@/api/wmsApi'
export default {
  components: {
    detailPotbs,
    deliverMaterialRecord,
  },
  data() {
    return {
      serverUrl: api.server_url,
      resultOnlineid: '6e02a314b8b54c88bb12ff13fc28114c', //获取检测结果表的id
      onlineId: '597983d3ade2449aa91338e05148332a',
      contractId: '74e638063d34429b88f48efbfb2315b2',
      qualityInspectionOnlineId: 'bba30b3a70924ac7aa65c220ee62cb30', //质量检测
      jcbdOnlineId: '737bd4f254cf431f8cbad40a1a95550f',
      addOrEditFlag: 1,
      columns: [
        {
          title: '订单标识编码',
          align: 'center',
          width: '15%',
          dataIndex: 'order_identity',
        },
        {
          title: '物流编码',
          align: 'center',
          width: '15%',
          dataIndex: 'logistics_code',
        },
        {
          title: '供应商标识编码',
          align: 'center',
          width: '15%',
          dataIndex: 'supplier_identity',
        },
        {
          title: '供应商名称',
          align: 'center',
          width: '10%',
          dataIndex: 'supplier_name',
        },
        {
          title: '供应商评分',
          align: 'center',
          width: '10%',
          dataIndex: 'supplier_rate',
        },
        {
          title: '合同名称',
          align: 'center',
          width: '15%',
          dataIndex: 'contract_name',
        },
        {
          title: '订单状态',
          align: 'center',
          width: '8%',
          dataIndex: 'order_status',
          customRender: (text, record, index) => {
            if (text === 0) {
              return '未发货'
            } else if (text === 1) {
              return '已发货'
            } else {
              return '已收货'
            }
          },
        },
        {
          title: '操作',
          align: 'center',
          width: '10%',
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
      columns3: [
        {
          title: '标识编码',
          width: '25%',
          dataIndex: 'prodId',
        },
        {
          title: '名称',
          width: '25%',
          dataIndex: 'prodName',
        },
        {
          title: '数量',
          width: '25%',
          dataIndex: 'prodAmount',
        },
        {
          title: '库存点',
          width: '25%',
          dataIndex: 'wmsUnitId',
          scopedSlots: { customRender: 'inventoryPoint' },
        },
      ],
      pageNo: 1,
      pageSize: 10,
      dataSource: [],
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
      searchIdentity: '',
      inSearch: false,
      contractExample_url: '/contract_example_1626055131664.doc',

      warehousingVisible: false,

      warehousingForm: {
        billType: 1,
        inspecter: '',
        orderId: '',
        createDate: '',
        providerId: '',
        qcor: '',
        remark: '',
        wmsInBillDetails: [],
      },
      wmsInBills: [],
      orderDataSource: [],
      record: {},
      dateFormat: 'YYYY-MM-DD',
    }
  },
  created() {
    this.getAllData()
  },
  methods: {
    getBillId() {
      o_getAction('/cgform/api/getData/4e7015e9b56447d3a29c3fb392a90b49').then((res) => {
        res.result.records.forEach((item) => {
          this.wmsInBills.push(pick(item, 'id', 'unit_name'))
        })
      })
    },
    //查询操作
    searchQuery() {
      this.inSearch = true
      order_getAction(`/purchaseDemand/orderIdentity?orderIdentity=${this.searchIdentity}`).then((res) => {
        this.dataSource = []
        var element = res.result
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
        dataList.supplier_id = element.supplierId
        dataList.order_status = element.orderStatus
        dataList.shq_id = element.shqId
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
        chooseSupplier: 3,
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
            dataList.order_identity = element.orderIdentity
            dataList.choose_supplier = element.chooseSupplier
            dataList.contract_name = element.contractName
            dataList.contract_url = element.contractUrl
            dataList.supplier_goods = element.supplierGoods
            dataList.supplier_identity = element.supplierIdentity
            dataList.supplier_name = element.supplierName
            dataList.supplier_rate = element.supplierRate
            dataList.order_status = element.orderStatus
            dataList.shq_id = element.shqId
            dataList.pn_code = element.pnCode
            dataList.logistics_code = element.logisticsCode
            if (element.purchaseOrderMaterialDetails.length > 0) {
              dataList.purchase_order_material_details = element.purchaseOrderMaterialDetails
            }
            this.dataSource.push(dataList)

            // this.ipagination.total = this.dataSource.length
          })
          this.ipagination.total = res.result.total
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    //详情
    showDetail(record) {
      this.$refs.modalform.openModal(record)
    },
    handleOk(record) {
      var params = {}
      params.categories = '订单检测'
      params.check_form_id = '73be30c3ca574deb88c639a84500862b'
      params.product_data_file = []
      params.product_id = record.order_identity
      params.product_name = '采购订单的标识编码'
      params.remark = '采购订单收货前的检测'
      o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params)
        .then((res) => {
          if (res.success) {
            record.order_status = 1
            record.shq_id = res.result
            o_putAction('/cgform/api/form/' + this.onlineId, record).then((res) => {
              if (res.success) {
                Message({
                  showClose: true,
                  message: '发货成功',
                  type: 'success',
                })
              } else {
                Message({
                  showClose: true,
                  message: '发货失败',
                  type: 'error',
                })
              }
            })
          } else {
            Message({
              showClose: true,
              message: '送往检测失败',
              type: 'error',
            })
          }
        })
        .catch((err) => {
          console.log('err', err)
          Message({
            showClose: true,
            message: '送检失败！',
            type: 'error',
          })
        })
    },
    //发货
    deliverGoods(record) {
      console.log('发货record', record)
      this.$refs.deliverMaterialRecordRef.openModal(record)
      // var params = {}
      // params.categories = '订单检测'
      // params.check_form_id = '73be30c3ca574deb88c639a84500862b'
      // params.product_data_file = []
      // params.product_id = record.order_identity
      // params.product_name = '采购订单的标识编码'
      // params.remark = '采购订单收货前的检测'
      // o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params)
      //   .then((res) => {
      //     if (res.success) {
      //       record.order_status = 1
      //       record.shq_id = res.result
      //       o_putAction('/cgform/api/form/' + this.onlineId, record).then((res) => {
      //         if (res.success) {
      //           Message({
      //             showClose: true,
      //             message: '发货成功',
      //             type: 'success',
      //           })
      //         } else {
      //           Message({
      //             showClose: true,
      //             message: '发货失败',
      //             type: 'error',
      //           })
      //         }
      //       })
      //     } else {
      //       Message({
      //         showClose: true,
      //         message: '送往检测失败',
      //         type: 'error',
      //       })
      //     }
      //   })
      //   .catch((err) => {
      //     console.log('err', err)
      //     Message({
      //       showClose: true,
      //       message: '送检失败！',
      //       type: 'error',
      //     })
      //   })

      // let jcparams = {
      //   pageSize: 10,
      //   pageNo: 1,
      //   column: 'createTime',
      //   order: 'desc',
      // }
      // jcparams.superQueryMatchType = 'and'
      // jcparams.superQueryParams = encodeURI(
      //   JSON.stringify([
      //     {
      //       rule: 'eq',
      //       type: 'text',
      //       val: '订单检测',
      //       field: 'categories',
      //     },
      //   ])
      // )
      // o_getAction(`/cgform/api/getData/${this.jcbdOnlineId}`, jcparams)
      //   .then((res) => {
      //     var params = {}
      //     params.categories = '订单检测'
      //     params.check_form_id = res.result.records[0].form_id
      //     params.product_data_file = []
      //     params.product_id = record.order_identity
      //     params.product_name = '采购订单的标识编码'
      //     params.remark = '采购订单收货前的检测'
      //     o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params)
      //       .then((res) => {
      //         if (res.success) {
      //           record.order_status = 1
      //           record.shq_id = res.result
      //           o_putAction('/cgform/api/form/' + this.onlineId, record).then((res) => {
      //             if (res.success) {
      //               Message({
      //                 showClose: true,
      //                 message: '发货成功',
      //                 type: 'success',
      //               })
      //             } else {
      //               Message({
      //                 showClose: true,
      //                 message: '发货失败',
      //                 type: 'error',
      //               })
      //             }
      //           })
      //         } else {
      //           Message({
      //             showClose: true,
      //             message: '送往检测失败',
      //             type: 'error',
      //           })
      //         }
      //       })
      //       .catch((err) => {
      //         console.log('err', err)
      //         Message({
      //           showClose: true,
      //           message: '送检失败！',
      //           type: 'error',
      //         })
      //       })
      //   })
      //   .catch((e) => {
      //     console.log('e', e)
      //     Message({
      //       showClose: true,
      //       message: '获取检测表单的表单id失败，检测online接口是否正常！需自行添加检测数据',
      //       type: 'error',
      //     })
      //   })

      // var params = {}
      // params.product_id = record.order_identity
      // params.product_name = '采购订单的标识编码'
      // params.remark = '采购订单收货前的检测'
      // o_postAction(`/cgform/api/form/${this.qualityInspectionOnlineId}?tabletype=1`, params).then((res) => {
      //   if (res.success) {
      //     record.order_status = 1
      //     record.shq_id = res.result
      //     o_putAction('/cgform/api/form/' + this.onlineId, record).then((res) => {
      //       if (res.success) {
      //         Message({
      //           showClose: true,
      //           message: '发货成功',
      //           type: 'success',
      //         })
      //       } else {
      //         Message({
      //           showClose: true,
      //           message: '发货失败',
      //           type: 'error',
      //         })
      //       }
      //     })
      //   } else {
      //     Message({
      //       showClose: true,
      //       message: '送往检测失败',
      //       type: 'error',
      //     })
      //   }
      // })
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
    //收货
    receivingGoods(record) {
      console.log('收货 record', record)
      this.getOrderStatus(record.shq_id)
        .then((res) => res)
        .then((res) => {
          // if (res === '' || res === undefined) {
          //   Message({
          //     showClose: true,
          //     message: '订单尚未检测，请前往检测',
          //     type: 'warning'
          //   })
          // } else if (res === '不合格') {
          //   console.log('receivingGoods record', record)
          //   Message({
          //     showClose: true,
          //     message: '订单尚未检测合格',
          //     type: 'warning'
          //   })
          // } else {
          this.getBillId()
          let deal_record = JSON.parse(JSON.stringify(record))
          if (deal_record.purchase_order_material_details) {
            deal_record.purchase_order_material_details.forEach((item) => {
              this.orderDataSource.push({ prodId: item.identity, prodName: item.name, prodAmount: item.number })
            })
          }
          this.warehousingForm.orderId = deal_record.order_identity
          this.warehousingForm.providerId = deal_record.supplier_name
          this.record = record
          this.warehousingVisible = true
          // }
        })
    },
    //退货
    returnGoods(record) {
      let deal_record = JSON.parse(JSON.stringify(record))
      this.$delete(deal_record, 'key')
      deal_record.choose_supplier = 4
      o_putAction(`/cgform/api/form/` + this.onlineId, deal_record)
        .then((res) => {
          if (res.success) {
            if (record.order_status === 0) {
              Message({
                showClose: true,
                message: '取消订单成功，该条数据流至采购退货单页面',
                type: 'success',
              })
            } else if (record.order_status === 1) {
              Message({
                showClose: true,
                message: '退货成功，该条数据流至采购退货单页面',
                type: 'success',
              })
            } else {
              Message({
                showClose: true,
                message: '订单状态异常！',
                type: 'error',
              })
            }

            this.getAllData()
          } else {
            Message({
              showClose: true,
              message: '退货失败，刷新后重新尝试！',
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
    //入库
    warehousing() {
      this.warehousingForm.wmsInBillDetails = this.orderDataSource
      w_postAction('/wms/warehousing', this.warehousingForm).then((res) => {
        if (res.success) {
          let deal_record = JSON.parse(JSON.stringify(this.record))
          this.$delete(deal_record, 'key')
          deal_record.order_status = 2
          deal_record.example_id = this.$store.state.exampleId
          o_putAction(`/cgform/api/form/` + this.onlineId, deal_record)
            .then((res) => {
              if (res.success) {
                Message({
                  showClose: true,
                  message: '入库成功!',
                  type: 'success',
                })
                this.getAllData()
              } else {
                Message({
                  showClose: true,
                  message: '入库失败!',
                  type: 'error',
                })
              }
            })
            .catch((err) => {
              Message({
                showClose: true,
                message: '出现错误，刷新重新尝试!',
                type: 'error',
              })
            })
          var postData = {
            identity: '',
            inquire: 'inquire',
            user_id: '',
            plan_id: '',
            lesson_id: '',
            teaching_mode: '',
          }
          var process = JSON.parse(window.localStorage.getItem('process'))
          if(process) {
            var userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
            console.log('userid', userid)
            postData.user_id = userid.value
            postData.plan_id = process.planId
            postData.lesson_id = process.lessonId
            postData.teaching_mode = process.teachingMode
            postData.identity = this.record.logistics_code + '/inquire'
            tm_postAction('/cgform/api/form/560ef424771845929d220fbee603883b', postData).then((res) => {
              if (res.success) {
                console.log('查询物流标识模板成功')
              }
            })
          }
          console.log('this.record', this.record)
          var materialData = {
            userId: '',
            orderIdentity: '',
            logisticsIdentity: '',
            pnCode: '',
            supplierIdentity: '',
            supplierName: '',
            materialNumber: '',
            materialId: '',
            materialName: '',
            reveiveState: '0',
            exampleId: this.$store.state.exampleId
          }
          materialData.orderIdentity = this.record.order_identity
          materialData.logisticsIdentity = this.record.logistics_code
          materialData.pnCode = this.record.pn_code
          materialData.supplierIdentity = this.record.supplier_identity
          materialData.supplierName = this.record.supplier_name
          materialData.materialNumber = this.record.purchase_order_material_details[0].number
          materialData.materialId = this.record.purchase_order_material_details[0].identity
          materialData.materialName = this.record.purchase_order_material_details[0].name
          var user_id = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
          materialData.userId = user_id.value
          console.log('materialData', materialData)
          order_postAction('materialCollection/addOrUpdMaterialCollection', materialData).then((res) => {
            if (res.success) {
              console.log('插入物料领取表成功')
            }
          })
        }
      })
      this.warehousingVisible = false
    },

    onChange(date, dateString) {
      this.warehousingForm.createDate = new Date(dateString).getTime()
    },
    handleCancle() {
      this.warehousingForm = {
        billType: 1,
        inspecter: '',
        orderId: '',
        createDate: '',
        providerId: '',
        qcor: '',
        remark: '',
        wmsInBillDetails: [],
      }
      this.orderDataSource = []
      this.wmsInBills = []
      this.record = {}
    },
    selectChange(index, args) {
      this.orderDataSource[index].wmsUnitId = args[0]
    },
    getOrderStatus(id) {
      return o_getAction(`/cgform/api/form/${this.qualityInspectionOnlineId}/${id}`).then((res) => {
        return o_getAction(`/cgform/api/form/${this.resultOnlineid}/${res.result.data_id}`).then((res) => {
          return res?.result?.result
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
.purchase_order {
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
