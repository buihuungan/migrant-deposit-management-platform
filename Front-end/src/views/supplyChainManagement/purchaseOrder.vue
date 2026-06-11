<template>
  <!-- 采购订单 -->
  <div
    class="purchase_order"
    style="display: grid"
  >
    <div class="table-page-search-wrapper">
      <a-form
        layout="inline"
        @keyup.enter.native="searchQuery"
      >
        <a-row :gutter="24">
          <a-col
            :md="6"
            :sm="6"
          >
            <a-form-item
              label="订单标识编码："
              :labelCol="{ span: 6 }"
              :wrapperCol="{ span: 17, push: 1 }"
            >
              <a-input
                placeholder="请输入关键字搜索"
                v-model="searchIdentity"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col
            :md="6"
            :sm="6"
          >
            <a-form-item>
              <a-button
                type="primary"
                @click="searchQuery"
              >查询</a-button>
              <a-button
                style="margin-left: 8px"
                @click="searchReset"
              >重置</a-button>
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
        <span
          slot="action"
          slot-scope="text, record"
        >
          <a @click="showDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a-popconfirm
            :title="record.order_status === 0 ? '确定取消订单吗?' : '确定退货吗?'"
            cancelText="取消"
            okText="确定"
            @confirm="() => returnGoods(record)"
          >
            <a-icon
              slot="icon"
              type="question-circle-o"
              style="color: red"
            />
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
  </div>
</template>

<script>
import detailPotbs from '@/views/procurementPlan/modules/detailPotbs'
import { Message } from 'element-ui'
import { order_getAction, order_putAction } from '@/api/orderApi'
import api from '@/api/index'
import Vue from 'vue'
import pick from 'lodash.pick'
import { o_getAction, o_postAction, o_putAction } from '../../api/onApi'
import { w_postAction } from '@/api/wmsApi'
export default {
  components: {
    detailPotbs,
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
          width: '20%',
          dataIndex: 'order_identity',
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
          width: '8%',
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
          width: '15%',
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
          width: '15%',
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
    //退货
    returnGoods(record) {
      let deal_record = JSON.parse(JSON.stringify(record))
      this.$delete(deal_record, 'key')
      deal_record.choose_supplier = 4
      deal_record.example_id = this.$store.state.exampleId
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
    onChange(date, dateString) {
      this.warehousingForm.createDate = new Date(dateString).getTime()
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
