<template>
  <div class="colleationAndInvoice_management" style="display: grid">
    <!-- 查询部分 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item label="客户名称" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
              <a-input
                placeholder="请输入客户名搜索"
                v-model="searchIdentity"
                @keyup.enter.native="searchQuery"
              ></a-input>
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
    <!-- 新增部份 -->
    <div class="table-operator" style="margin: 5px 0 10px 2px">
      <a-button class="ant-btn add" type="primary" icon="plus" @click="handlecollectionAdd">新增</a-button>
    </div>

    <!-- 表格部分 -->
    <div class="article-table">
      <a-table
        style="height: 500px"
        ref="table"
        size="middle"
        row-key="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
      >
        <template slot="createTime" slot-scope="text, record">
          <span>{{ record.input_time.slice(0, 11) }}</span>
        </template>

        <a slot="overdue" slot-scope="text">{{ text === '0' ? '否' : '是' }}</a>

        <!-- 发票操作区 -->
        <span slot="invoice" slot-scope="text, record">
          <span v-if="record.invoice_number">
            <a @click="handleInvoiceDetail(record)">详情</a>
            <a-divider type="vertical" />
            <a @click="handleInvoiceEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定删除吗?"
              cancelText="取消"
              okText="确定"
              @confirm="() => handleInvoiceDelete(record)"
            >
              <a-icon slot="icon" type="question-circle-o" style="color: red" />
              <a>删除</a>
            </a-popconfirm>
            <!-- <a @click="handleInvoiceDelete(record)" >删除</a> -->
          </span>
          <a @click="handleInvoiceAdd(record)" v-else>添加</a>
        </span>

        <span slot="action" slot-scope="text, record">
          <a @click="handleCollectionEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定删除吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => handleCollectionDelete(record)"
          >
            <a-icon slot="icon" type="question-circle-o" style="color: red" />
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>

    <!-- 回款编辑/添加组件 -->
    <collectionAddAndEditPDS ref="collectionModalRef" @ok="handleOk"> </collectionAddAndEditPDS>

    <!-- 发票详情/编辑组件 -->
    <invoiceAddAndDetailPDS
      ref="invoiceModelRef"
      @addOk="invoiceAddEnd"
      @editOk="invoiceEditEnd"
    ></invoiceAddAndDetailPDS>
  </div>
</template>

<script>
import collectionAddAndEditPDS from './modules/collectionAddAndEditPDS.vue'
import invoiceAddAndDetailPDS from './modules/invoiceAddAndDetail.vue'
import { o_getAction, o_deleteAction, o_putAction } from '@/api/onApi'
import { Message } from 'element-ui'
import api from '@/api/index'

export default {
  components: {
    collectionAddAndEditPDS,
    invoiceAddAndDetailPDS,
  },
  data() {
    return {
      serverUrl: api.server_url,
      onlineId: '6eb12b4bf7f74b16bcb535191045bee9',
      columns: [
        {
          title: '合同ID',
          align: 'center',
          width: '10%',
          dataIndex: 'contract_no',
        },
        {
          title: '客户',
          align: 'center',
          width: '10%',
          dataIndex: 'username',
        },
        {
          title: '业务员',
          align: 'center',
          width: '10%',
          dataIndex: 'salesman_name',
        },
        {
          title: '回款金额',
          align: 'center',
          width: '10%',
          dataIndex: 'receive_number',
        },
        {
          title: '创建时间',
          align: 'center',
          width: '10%',
          dataIndex: 'input_time',
          scopedSlots: { customRender: 'createTime' },
        },
        {
          title: '是否逾期',
          align: 'center',
          width: '10%',
          dataIndex: 'overdue',
          scopedSlots: { customRender: 'overdue' },
        },
        {
          title: '发票',
          align: 'center',
          width: '15%',
          scopedSlots: { customRender: 'invoice' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '15%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      dataSource: [],
      ipagination: {
        current: 1,
        pageSize: 10,
        total: 0,
        showQuickJumper: true,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        onChange: this.onPageChange.bind(this),
      },
      searchIdentity: '',
      inSearch: false,
      onlyOne: false
    }
  },
  created() {
    this.getData()
  },
  methods: {
    getData() {
      this.onlyOne = false
      let params = {
        column: 'input_time',
        order: 'desc',
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        superQueryMatchType: 'and',
      }
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records
          if(this.dataSource.length === 1) this.onlyOne = true
          this.ipagination.total = res.result.total
        } else {
          Message({
            showClose: true,
            message: '获取回款失败!',
            type: 'error',
          })
        }
      })
    },
    onPageChange(page) {
      this.ipagination.current = page
      if (!this.inSearch) {
        this.getData()
      } else {
        this.searchQuery()
      }
    },
    handleCollectionDelete(record) {
      o_deleteAction(`/cgform/api/form/${this.onlineId}/${record.id}`).then( (res) => {
        if(this.onlyOne){
          this.ipagination.current--
        } 
        this.getData()
        Message({
          showClose: true,
          message: '删除成功!',
          type: 'success',
        })
      })
    },
    handlecollectionAdd() {
      this.$refs.collectionModalRef.setFlag(1)
      this.$refs.collectionModalRef.show()
    },
    handleCollectionEdit(record) {
      this.$refs.collectionModalRef.setFlag(2)
      this.$refs.collectionModalRef.setForm(record)
      this.$refs.collectionModalRef.show()
    },
    searchQuery() {
      if (!this.inSearch) {
        this.inSearch = true
        this.ipagination.current = 1
      }
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        column: 'input_time',
        order: 'asc',
      }
      params.superQueryMatchType = 'and'
      params.superQueryParams = encodeURIComponent(
        JSON.stringify([
          {
            rule: 'like',
            type: 'text',
            val: this.searchIdentity,
            field: 'username',
          },
        ])
      )
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params)
        .then((res) => {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
        })
        .catch((err) => {
          Message({
            showClose: true,
            message: '数据获取失败！',
            type: 'error',
          })
        })
    },
    searchReset() {
      this.inSearch = false
      this.searchIdentity = ''
      this.ipagination.current = 1
      this.getData()
    },
    handleOk() {
      this.getData()
    },

    //发票相关方法
    handleInvoiceAdd(record) {
      this.$refs.invoiceModelRef.setForm(record)
      this.$refs.invoiceModelRef.setFlag(1)
      this.$refs.invoiceModelRef.show()
    },
    invoiceAddEnd() {
      this.getData()
    },

    invoiceEditEnd() {
      this.getData()
    },

    handleInvoiceDetail(record) {
      this.$refs.invoiceModelRef.setFlag(3)
      this.$refs.invoiceModelRef.setForm(record)
      this.$refs.invoiceModelRef.show()
    },
    handleInvoiceEdit(record) {
      this.$refs.invoiceModelRef.setForm(record)
      this.$refs.invoiceModelRef.setFlag(2)
      this.$refs.invoiceModelRef.show()
    },
    handleInvoiceDelete(record) {
      record.invoice_img = ''
      record.invoice_number = ''
      record.type = '0'
      record.money = ''
      record.date = null
      record.staff_name = ''
      record.invoice_remark = ''
      o_putAction(`/cgform/api/form/${this.onlineId}`, record).then((res) => {
        if (res.success) {
          Message({
            showClose: true,
            message: '删除发票成功!',
            type: 'success',
          })
          this.getData()
        } else {
          Message({
            showClose: true,
            message: '删除失败!',
            type: 'error',
          })
        }
      })
    },
  },
}
</script>


<style lang='less' scoped>
.ant-col-sm-24 {
  height: 40px;
  line-height: 40px;
}
.el-range-input {
  margin-left: 15px !important;
}
.colleationAndInvoice_management {
  background: #fff;
  padding: 24px;
}
</style>