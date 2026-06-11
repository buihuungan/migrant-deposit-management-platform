<template>
  <!-- 售后管理界面 -->
  <div class="aftersale_body" style="display: grid">
    <div class="search_information">
      <a-form layout="inline">
        <!-- <a-row :gutter="24"> -->
        <!-- <a-col :span="6"> -->
        <a-form-item label="供应商名称：">
          <a-input placeholder="请输入关键字搜索" v-model="searchdata"></a-input>
        </a-form-item>
        <!-- </a-col>
          <a-col> -->
        <a-form-item>
          <a-button type="primary" @click="searchQuery">查询</a-button>
          <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
        </a-form-item>
        <!-- </a-col>
        </a-row> -->
      </a-form>
    </div>
    <!-- 新增数据 -->
    <div class="table-operator" style="margin: 5px 0 10px 2px">
      <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">新增</a-button>
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
          <div>
            <a @click="handleEdit(record)">修改处理状态</a>
            <a-divider type="vertical" />
            <a-popconfirm title="确定删除吗?" cancelText="取消" okText="确定" @confirm="() => handleDelete(record)">
              <a-icon slot="icon" type="question-circle-o" style="color: red" />
              <a>删除</a>
            </a-popconfirm>
          </div>
        </span>
      </a-table>
    </div>
    <add-modal ref="modal" @ok="searchQuery()"></add-modal>
  </div>
</template>

<script>
import addModal from './modules/addAfterSaleInformation'
import { o_getAction, o_deleteAction } from '@/api/onApi'
export default {
  components: { addModal },
  data() {
    return {
      onlineID: 'f3a3f37ffa9042378873c8c35f1a571b',
      searchdata: '',
      dataSource: [],
      columns: [
        {
          title: '订单标识',
          width: '15%',
          dataIndex: 'order_identity',
        },
        {
          title: '提供零件名称',
          width: '15%',
          dataIndex: 'parts_name',
        },
        {
          title: '供应商名称',
          width: '15%',
          dataIndex: 'supplier_name',
        },
        {
          title: '售后原因',
          width: '15%',
          dataIndex: 'aftersale_reason',
        },
        {
          title: '处理时间',
          width: '15%',
          dataIndex: 'processing_time',
        },
        {
          title: '处理状态',
          width: '10%',
          dataIndex: 'processing_state',
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '15%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
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
    }
  },
  created() {
    this.searchQuery()
  },
  methods: {
    //编辑数据
    handleEdit(record) {
      this.$refs.modal.openModal(record)
    },
    //删除数据
    handleDelete(record) {
      console.log('record', record)
      o_deleteAction(`/cgform/api/form/${this.onlineID}/${record.id}`)
        .then((res) => {
          this.$message.success('删除成功')
          this.searchQuery()
        })
        .catch((err) => {
          this.$message.error('删除失败')
        })
    },
    //查询
    searchQuery() {
      console.log('111')
      let params = {
        pageSize: this.pageSize,
        pageNo: this.pageNo,
        column: 'createTime',
        order: 'desc',
        superQueryMatchType: 'and',
      }
      if (this.searchdata != '') {
        params.superQueryParams = encodeURIComponent(
          JSON.stringify([
            {
              rule: 'like',
              type: 'text',
              val: this.searchdata,
              field: 'supplier_name',
            },
          ])
        )
      }
      o_getAction(`/cgform/api/getData/${this.onlineID}`, params).then((res) => {
        this.dataSource = res.result.records
        console.log('online结果', res)
      })
    },
    handleAdd() {
      //新增
      this.$refs.modal.openModal()
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.pageNo = page
      this.pageSize = pageSize
    },
    searchReset() {
      //查询所有数据
      this.searchdata = ''
      this.searchQuery()
    },
  },
}
</script>

<style>
.aftersale_body {
  background: #fff;
  padding: 24px;
}
</style>