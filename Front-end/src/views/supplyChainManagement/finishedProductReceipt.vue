<template>
  <div>
    <a-card :bordered="false">
      <div>
        <a-table
          ref="table"
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
        >
          <span slot="action" slot-scope="text, record">
            <!-- <a :disabled="record.logisticsCode != null" @click="chooseLogisticsProvider(record)">选择物流商</a>
            <a-divider type="vertical" /> -->
            <a @click="receiveGoods(record)" v-if="record.orderState == '1'">收货</a>
            <a v-if="record.orderState == '2'" disabled>已收货</a>
            <a v-if="record.orderState == '0'" disabled>收货</a>
          </span>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import axios from 'axios'
import { erp_getAction } from '@/api/erpApi'
import { Message } from 'element-ui'
export default {
  data() {
    return {
      dataSource: [],
      columns: [
        {
          title: '物流单号',
          align: 'center',
          width: '20%',
          dataIndex: 'logisticsCode',
        },
        {
          title: '发货方',
          align: 'center',
          width: '8%',
          dataIndex: 'shipper',
        },
        {
          title: '收货方',
          align: 'center',
          width: '8%',
          dataIndex: 'consignee',
        },
        {
          title: '产品名称',
          align: 'center',
          width: '8%',
          dataIndex: 'name',
        },
        {
          title: '产品标识',
          align: 'center',
          width: '16%',
          dataIndex: 'identity',
        },
        {
          title: '发货日期',
          align: 'center',
          width: '8%',
          dataIndex: 'date',
        },
        {
          title: '发货数量',
          align: 'center',
          width: '8%',
          dataIndex: 'number',
        },
        {
          title: '订单状态',
          align: 'center',
          width: '8%',
          dataIndex: 'orderState',
          customRender: (text, record, index) => {
            if (text == '0') {
              return '未发货'
            } else if (text == '1') {
              return '已发货'
            } else {
              return '已收货'
            }
          },
        },
        {
          title: '操作',
          align: 'center',
          width: '16%',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      ipagination: {
        current: 1,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + '共' + total + '条'
        },
        pageSize: 10,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        exampleId: this.$store.state.exampleId
      }
      erp_getAction(`/SupplyDelivery/Query`, params
      )
        .then((res) => {
          res.result.records.forEach((item) => {
            item.date = item.date.substring(0, 10)
          })
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
        })
    },
    receiveGoods(record) {
      erp_getAction(`/SupplyDelivery/updateStatus?id=${record.id}&orderState=2`).then((res) => {
        Message({
          showClose: true,
          message: '收货成功',
          type: 'success',
        })
        this.loadData()
      })
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
  },
}
</script>

<style scoped>
.ant-btn {
  line-height: 1.499;
  position: relative;
  display: inline-block;
  font-weight: 400;
  white-space: nowrap;
  text-align: center;
  background-image: none;
  border: 1px solid transparent;
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.015);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -ms-touch-action: manipulation;
  touch-action: manipulation;
  height: 32px;
  padding: 0 15px;
  font-size: 14px;
  border-radius: 4px;
  color: rgba(0, 0, 0, 0.65);
  background-color: #fff;
  border-color: #d9d9d9;
}
.ant-btn-primary {
  color: #fff;
  background-color: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.12);
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.045);
}
</style>