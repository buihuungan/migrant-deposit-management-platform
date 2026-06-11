<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="名称">
          <a-input v-model="searchData.prod_name" placeholder="请输入名称" />
        </a-form-item>
        <a-form-item>
          <a-button-group>
            <a-button type="primary" icon="search" @click="loadData()">查询</a-button>
            <a-button
              type="primary"
              icon="reload"
              @click="
                () => {
                  this.searchData = []
                  loadData()
                }
              "
              >重置</a-button
            >
          </a-button-group>
        </a-form-item>
      </a-form>
      <div style="padding-top: 20px">
        <a-table
          rowKey="id"
          :loading="tableLoading"
          :columns="columns"
          :dataSource="formTableData"
          :pagination="ipagination"
          :scroll="{ x: 1300 }"
        >
          <span class="desformspana" slot="action" slot-scope="text, record, index">
            <a @click="releaseProductionTask(record)"> <a-icon type="setting" />发布生产任务 </a>
          </span>
        </a-table>
      </div>
    </a-card>
    <ann-task ref="annModal" @closeTask="closeTask"></ann-task>

    <!-- 详情页面，暂时用不上注释掉 -->
    <!-- <a-modal
      :visible="detailVisible"
      :centered="true"
      :footer="null"
      width="80%"
      :zIndex="100"
      :closable="true"
      :destroyOnClose="destroy"
      dialogClass="dialogTest"
      @cancel="handleCancel2"
    >
      <detail-modal
        ref="detailModal"
        :record="record"
        @close-detailModal="closeDetailModal"
        :detailFormId="detailFormId"
      ></detail-modal>
    </a-modal> -->
  </div>
</template>

<script>
import { order_postAction } from '@/api/orderApi'
import { getAction } from '@/api/manage'
import AnnTask from './modules/annTask'
import DetailModal from './modules/detailModal'
import { USER_ID ,ACCESS_TOKEN} from '@/store/mutation-types'
import Vue from 'vue'
import api from '@/api/index'
import axios from 'axios'

export default {
  name: 'UnitManage',
  components: { AnnTask, DetailModal },
  data() {
    return {
      record: {},
      detailVisible: false,
      destroy: true,
      // formId: 'f52d908891f0484d81f66574e89a6b16',//生产工单表
      formId: '3b2a821907fb489196da6d1ef4528906',//生产工单表(去掉销售订单字段)
      detailFormId: 'd2cc63a6c7134034a212ec07f2db6d26',
      searchData: {
        prod_name: ''
      },
      url: {
        getColumns: '/online/cgform/api/getColumns/24e5e0a2c5d04bf3a0b99a9f8cccc2b4',//日排产计划
        getPlansData: 'online/cgform/api/getData/24e5e0a2c5d04bf3a0b99a9f8cccc2b4',
        createIdentity: 'productionIdentity/createIdentity',
        uploadProductTraceability: 'productTraceability/uploadProductTraceability'
      },
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        total: 0,
        showSizeChanger: true,
        showQuickJumper: true,
        showTotal: (total, range) => range + ' 共' + total + '条',
        onChange: this.onPageChange.bind(this),
        onShowSizeChange: (current, pageSize) => {
          this.ipagination.current = 1
          this.ipagination.pageSize = pageSize
          this.loadData()
        }
      },
      tableLoading: false,
      columns: [
        {
          title: '标识编码',
          align: 'center',
          dataIndex: 'prodId'
        },
        {
          title: '名称',
          align: 'center',
          dataIndex: 'prodName'
        },
        {
          title: '排产日期',
          align: 'center',
          dataIndex: 'schedulingDate'
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'amount'
        },
        {
          title: '产线id',
          align: 'center',
          dataIndex: 'productionLineId'
        },
        // {
        //   title: '生产状态',
        //   align: 'center',
        //   dataIndex: 'production_status'
        // },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          fixed: 'right',
          width: 200
        }
      ],
      formTableData: [],
      selectedRowKeys: [],
      selectedRows: [],
      wigetForm: {
        list: [],
        config: {
          labelWidth: 100,
          labelPosition: 'right',
          size: 'small',
          customClass: '',
          ui: 'element',
          layout: 'horizontal',
          labelCol: 3,
          width: '100%',
          tableId: ''
        }
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    closeDetailModal() {
      this.detailVisible = false
      // this.$emit('updateData')
    },
    closeTask() {
      this.loadData()
    },
    loadData() {
      this.tableLoading = true
      // let params = {
      //   column: 'createTime',
      //   order: 'desc',
      //   pageNo: this.ipagination.current,
      //   pageSize: this.ipagination.pageSize,
      //   superQueryMatchType: 'and'
      // }
      // var superQueryParams = []
      // if (this.searchData.prod_name) {
      //   superQueryParams.push(
      //     {
      //       rule: 'like',
      //       type: 'text',
      //       val: this.searchData.prod_name,
      //       field: 'prod_name'
      //     },
      //     { rule: 'eq', type: 'text', val: '0', field: 'is_publish' }
      //   )
      // } else {
      //   superQueryParams.push({ rule: 'eq', type: 'text', val: '0', field: 'is_publish' })
      // }
      // if (vue.ls.get(USER_INFO).username != 'admin') {
      //   superQueryParams.push({ rule: 'eq', type: 'text', val: vue.ls.get(USER_INFO).username, field: 'create_by' })
      // }
      // if (superQueryParams.length) {
      //   params.superQueryParams = encodeURI(JSON.stringify(superQueryParams))
      // }
      // getAction(this.url.getPlansData, params).then(res2 => {
      //   this.formTableData = res2.result.records
      //   this.ipagination.total = res2.result.total
      // })
      let params = {
        current: this.ipagination.current,
        pages: this.ipagination.pageSize,
        isPublish: '0',  //0表示未发布
        exampleId: this.$store.state.exampleId,
      }
      axios({
        url: api.server_url + api.global_erp_baseURL + '/dailyProductionSchedulingPlan/getAlreadyDailyPublish',
        method: 'get',
        params: params,
        headers: {
          'userid':  Vue.ls.get(USER_ID),
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },
      }).then(res => {
        console.log('res',res)
        this.formTableData = res.data.result.records
        this.ipagination.total = res.data.result.total
        console.log('this.formTableData',this.formTableData)
      })
      this.tableLoading = false
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectionRows
    },
    clearSelectedRows() {
      this.selectedRowKeys = []
    },
    handleDetail(record) {
      this.record = record
      this.detailVisible = true
    },

    releaseProductionTask(record) {
      this.record = record
      this.$refs.annModal.openModal(this.record, this.formId)
      // 注释代码，接口调用异常暂且注释，不要删除
      // let param = {
      //   location: record.productline,
      //   modelIdentity: record.prod_id,
      //   productNum: record.prod_amount,
      //   productionName: record.prod_name,
      //   result: '',
      //   updateId: ''
      // }
      // let url = this.url.createIdentity
      // order_postAction(url, param)
      //   .then(res => {
      //     if (res.code == 200) {
      //       for (let i = 0; i < res.result.length; i++) {
      //         let url1 = this.url.uploadProductTraceability
      //         let param1 = {
      //           location: record.productline,
      //           identity: res.result[i],
      //           result: ''
      //         }
      //         order_postAction(url1, param1)
      //           .then(res => {})
      //           .catch(err => {
      //             console.log(err)
      //           })
      //       }
      //     }
      //   })
      //   .catch(err => {
      //     console.log(err)
      //   })
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    handleCancel2() {
      this.detailVisible = false
    }
  }
}
</script>

<style>
.desformspana a {
  color: #1890ff !important;
}
</style>
