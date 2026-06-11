<template>
  <div>
    <!-- 物料领取 -->
    <div class="material" style="display: grid">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="6">
              <a-form-item label="订单编号：" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
                <a-input placeholder="请输入订单编号搜索" v-model="searchIdentity"></a-input>
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
      <div class="article-table" style="white-space: nowrap; overflow: auto">
        <a-table
          :rowKey="(record) => record.id"
          style="height: 500px"
          ref="table"
          size="middle"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
        >
          <span slot="action" slot-scope="text, record">
            <div>
              <a @click="handleDetail(record)">详情</a>
              <a-divider type="vertical" />
              <a :disabled="record.reveiveState != '0'" @click="materialReceiving(record)">领取物料</a>
            </div>
          </span>
        </a-table>
      </div>
    </div>
    <materialRecord ref="materialRecordRef"></materialRecord>
  </div>
</template>

<script>
import { tm_postAction } from '@/api/templateApi'
import { order_postAction, order_getAction } from '@api/orderApi'
import axios from 'axios'
import materialRecord from './modules/materialRecord'
import { Message } from 'element-ui'
import api from '@/api/index'
import vue from 'vue'
import { USER_INFO } from '@/store/mutation-types'
export default {
  components: {
    materialRecord,
  },
  data() {
    return {
      materialIdentity: '',
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
      visible: false,
      dataSource: [],
      searchIdentity: '',
      columns: [
        {
          title: '物流单标识',
          align: 'center',
          width: '15%',
          dataIndex: 'logisticsIdentity',
        },
        {
          title: 'PN码',
          align: 'center',
          width: '15%',
          dataIndex: 'pnCode',
        },
        {
          title: '发货商标识',
          align: 'center',
          width: '10%',
          dataIndex: 'supplierIdentity',
        },
        {
          title: '发货商名称',
          align: 'center',
          width: '10%',
          dataIndex: 'supplierName',
        },
        {
          title: '物料名称',
          align: 'center',
          width: '10%',
          dataIndex: 'materialName',
        },
        {
          title: '物料类标识',
          align: 'center',
          width: '10%',
          dataIndex: 'materialId',
        },
        {
          title: '物料数量',
          align: 'center',
          width: '10%',
          dataIndex: 'materialNumber',
        },
        {
          title: '领取状态',
          align: 'center',
          width: '10%',
          dataIndex: 'reveiveState',
          customRender: (text, record, index) => {
            if (text == '0') {
              return '未领取'
            } else if (text == '1') {
              return '已领取'
            } else {
              return ''
            }
          },
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '10%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    searchQuery() {},
    loadData() {
      // var postData = {
      //   pageSize: '',
      //   pageNum: '',
      //   exampleId:''
      // }
      // postData.pageSize = this.ipagination.pageSize
      // postData.pageNum = this.ipagination.current
      let exampleId = this.$store.state.exampleId
      let userInfo = vue.ls.get(USER_INFO)
      console.log('userInfo',userInfo)
      if(userInfo.realname == '管理员')
      {
        exampleId = '1'
      }
      order_getAction(
        `/materialCollection/getMaterialCollection/${this.ipagination.current}/${this.ipagination.pageSize}/${exampleId}`
      ).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
        }
      })
    },
    searchReset() {},
    async materialReceiving(record) {
      var postData1 = record
      postData1.reveiveState = '1'
      order_postAction('/materialCollection/addOrUpdMaterialCollection', postData1).then((res) => {
        if (res.success) {
          console.log('record', record)
          var postData2 = {
            create_by: '',
            name: '',
            identity: '',
            sum: '',
          }
          postData2.create_by = record.userId
          postData2.name = record.materialName
          postData2.identity = record.materialId
          postData2.sum = record.materialNumber
          order_postAction('/supplyChainInventory/addTotal', postData2).then((res) => {
            console.log('res', res)
          })
          Message({
            showClose: true,
            message: '领取物料成功',
            type: 'success',
          })
          console.log('领取物料成功')
        }
      })
      var requestData = {
        pnCode: record.pnCode,
        pageSize: '10',
        pageNo: '1',
      }
      // await tm_postActionAnother('docking/pnCode', requestData).then((res) => {
      //   console.log('ggggggggggggggggggggggg', res)
      //   this.materialIdentity = res.result.list[0].identity
      // })
      await axios({
        method: 'post',
        baseURL: api.server_url + '/identity',
        url: `/docking/pnCode`,
        params: requestData,
      }).then((res) => {
        console.log('ggggggggggggggggggggggg', res)
        this.materialIdentity = res.data.result.list[0].identity
      })
      console.log('this.materialIdentity', this.materialIdentity)
      var postData = {
        identity: '',
        inquire: 'inquire',
        user_id: '',
        plan_id: '',
        lesson_id: '',
        teaching_mode: '',
      }
      var userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
      console.log('userid', userid)
      postData.user_id = userid.value
      var process = JSON.parse(window.localStorage.getItem('process'))
      postData.plan_id = process.planId
      postData.lesson_id = process.lessonId
      postData.teaching_mode = process.teachingMode
      postData.identity = this.materialIdentity + '/inquire'
      tm_postAction('/cgform/api/form/560ef424771845929d220fbee603883b', postData).then((res) => {
        if (res.success) {
          console.log('查询物流标识模板成功')
        }
      })
    },
    handleDetail(record) {
      this.$refs.materialRecordRef.openModal(record)
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
.material {
  background: #fff;
  padding: 24px;
}
</style>
