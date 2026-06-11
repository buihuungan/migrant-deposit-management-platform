<template>
  <!-- 物流管理 -->
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
          <a
            :disabled="record.logistics_code != null"
            @click="chooseLogisticsProvider(record)"
          >选择物流商</a>
          <a-divider type="vertical" />
          <a
            :disabled="record.order_status === 1 || record.order_status === 2"
            @click="deliverGoods(record)"
          >发货</a>
        </span>
      </a-table>
    </div>
    <detailPotbs ref="modalform"></detailPotbs>
    <chooseLogisticsProvider ref="modalform2" @ok="handleOk"></chooseLogisticsProvider>
  </div>
</template>

<script>
import chooseLogisticsProvider from './modules/chooseLogisticsProvider'
import { order_getAction, order_putAction } from '@/api/orderApi'
import { o_getAction, o_postAction, o_putAction } from '../../api/onApi'
import detailPotbs from '../procurementPlan/modules/detailPotbs' 
import { Message } from 'element-ui'
import axios from 'axios'
export default {
  components: {
    detailPotbs,
    chooseLogisticsProvider
  },
  data() {
    return {
      tableKey: '',
      qualityInspectionOnlineId: 'bba30b3a70924ac7aa65c220ee62cb30', //质量检测
      onlineId: '597983d3ade2449aa91338e05148332a',
      searchIdentity: '',
      dataSource: [],
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
          width: '20%',
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
        // {
        //   title: '合同名称',
        //   align: 'center',
        //   width: '15%',
        //   dataIndex: 'contract_name',
        // },
        {
          title: '订单状态',
          align: 'center',
          width: '10%',
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
          width: '20%',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      pageNo: 1,
      pageSize: 10,
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
      codeData: [],
    }
  },
  created() {
    this.getAllData()
  },
  methods: {
    getAllData() {
      order_getAction(`/purchaseDemand/material`, {
        chooseSupplier: 3,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        exampleId: this.$store.state.exampleId
      })
        .then((res) => {
           console.log(res)
           this.dataSource = []
           res.result.data.forEach((element, index) => {
            var str = '订单标识编码：' + element.orderIdentity + '/供应商标识编码：' + element.supplierIdentity + '/供应商名称：' + element.supplierName +'/合同名称：' + element.contractName
            var dataList = {}
            dataList.codeData = str
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
    searchQuery() {

    },
    searchReset() {

    },
    handleOk(deal_record) {
      this.getAllData()
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
    chooseLogisticsProvider(record) {
      this.$refs.modalform2.openModal(record)
    },
    showCode(record) {
      this.$refs.enlargeCodeRef.openModal(record)
    },
    showDetail(record) {
      this.$refs.modalform.openModal(record)
    },
    deliverGoods(record) {
      console.log('发货record',record)
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
            record.example_id = this.$store.state.exampleId
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
    }
  }
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