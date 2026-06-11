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
        <template
          slot="action1"
          slot-scope="text, record"
        >
        <div @click="showCode(record)">
        <vue-qr :text="record.codeData" :margin="0" :size="50" :class="record.id"></vue-qr>
        </div>
        </template>
        <span
          slot="action"
          slot-scope="text, record"
        >
          <a @click="showDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a
            :disabled="record.order_status === 1 || record.order_status === 2"
            @click="deliverGoods(record)"
          >发货</a>
        </span>
      </a-table>
    </div>
    <div class="thumbs">
      <vue-preview :slides="thumbsList" class="imgPrev"></vue-preview>
    </div>
    <enlarge-code ref="enlargeCodeRef" @ok="handleOk"></enlarge-code>
  </div>

</template>

<script>
import { order_getAction, order_putAction } from '@/api/orderApi'
import vueQr from 'vue-qr'
import vuePreview from 'vue-preview'
import enlargeCode from './modules/enlargeCode'
export default {
  components: {
    vueQr,
    vuePreview,
    enlargeCode
  },
  data() {
    return {
      thumbsList: [],
      searchIdentity: '',
      dataSource: [],
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
          title: '物料信息',
          align: 'center',
          width: '10%',
          dataIndex: 'action1',
          scopedSlots: {customRender: 'action1'}
        },
        {
          title: '操作',
          align: 'center',
          width: '10%',
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
      })
        .then((res) => {
           console.log(res)
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
            if (element.purchaseOrderMaterialDetails.length > 0) {
              dataList.purchase_order_material_details = element.purchaseOrderMaterialDetails
            }
            this.dataSource.push(dataList)
            // this.ipagination.total = this.dataSource.length
          })
          this.ipagination.total = res.result.total
          console.log('oooooooooooo',this.dataSource)
          let a = document.getElementsByClassName(this.dataSource[0].id)
          console.log('ppppppppppp',a)
          //   var b = Array.from(a)
          //   console.log('bbbbbbbbbbbb',b)
          var data = {
            w: 500,
            h: 500,
            src: 'data:image/png;base64,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',
            msrc: 'data:image/png;base64,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'
          }
          this.thumbsList.push(data)
          console.log('tttttttttttt',this.thumbsList)
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    searchQuery() {

    },
    searchReset() {

    },onPageChange(page, pageSize) {
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
    handleOk() {

    },
    showCode(record) {
      this.$refs.enlargeCodeRef.openModal(record)
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