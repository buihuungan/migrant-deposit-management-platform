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
          class="j-table-force-nowrap"
          :scroll="{ x: 1300 }"
        >
          <span class="desformspana" slot="action" slot-scope="text, record, index">
            <a @click="handleDetail(record)">详情</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <ann-task ref="annModal"></ann-task>

    <a-modal
      :visible="detailVisible"
      :centered="true"
      :footer="null"
      width="80%"
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
      >
      </detail-modal>
    </a-modal>
  </div>
</template>

<script>
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
      formId: 'f52d908891f0484d81f66574e89a6b16',
      detailFormId: 'd2cc63a6c7134034a212ec07f2db6d26',
      checkRecord: {},
      searchData: {
        prod_name: ''
      },
      url: {
        getColumns: '/online/cgform/api/getColumns/24e5e0a2c5d04bf3a0b99a9f8cccc2b4',//日排产计划
        getPlansData: 'online/cgform/api/getData/24e5e0a2c5d04bf3a0b99a9f8cccc2b4'
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
      //   console.log('查询名称不为空值为：', this.searchData.prod_name)
      //   superQueryParams.push(
      //     {
      //       rule: 'like',
      //       type: 'text',
      //       val: this.searchData.prod_name,
      //       field: 'prod_name'
      //     },
      //     { rule: 'eq', type: 'text', val: '1', field: 'is_publish' }
      //   )
      // } else {
      //   superQueryParams.push({ rule: 'eq', type: 'text', val: '1', field: 'is_publish' })
      // }
      // console.log('superQueryParams', superQueryParams)
      // if (superQueryParams.length) {
      //   params.superQueryParams = encodeURI(JSON.stringify(superQueryParams))
      // }
      // console.log(' params.superQueryParams', superQueryParams)
      // console.log(' params', params)
      // getAction(this.url.getPlansData, params).then(res2 => {
      //   this.formTableData = res2.result.records
      //   this.ipagination.total = res2.result.total
      // })
      let params = {
        current: this.ipagination.current,
        pages: this.ipagination.pageSize,
        isPublish: '1',  //1表示已发布生产任务
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
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    deleteMultipleForm(index) {},
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
