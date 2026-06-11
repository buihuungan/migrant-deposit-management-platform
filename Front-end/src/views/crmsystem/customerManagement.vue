<template>
<!-- 客户管理 -->
  <div
    class="customer_management"
    style="display: grid"
  >
    <!-- 搜索、新增、导入导出栏 -->
    <div class="table-head">
      <a-form>
        <a-row :gutter="20">
          <a-col :span="6">
            <a-form-item
              label=""
              :label-col="{}"
              :wrapper-col="{}"
            >
              <a-input
                placeholder="请输入关键字搜索"
                v-model="searchIdentity"
              />
            </a-form-item>
          </a-col>
          <a-col :span="8">
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
          <a-col>
            <a-form-item style="float: right">
              <a-button
                type="primary"
                icon="plus"
                @click="handleAdd"
              >新增</a-button>
              <a-button
                style="margin-left: 8px"
                @click="handleImport"
              >导入</a-button>
              <a-button
                style="margin-left: 8px"
                @click="handleExport"
              >导出</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <!-- 表格 -->
    </div>
    <div class="table-acticle">
      <a-table
        ref="table"
        :columns="columns"
        :data-source="data"
        :pagination="pagination"
      >
        <span
          slot="action"
          slot-scope="text, record"
        >
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm
            title="你确定删除吗？"
            ok-text="确定"
            cancel-text="取消"
            @confirm="() => handleDelete(record)"
          >
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>
    <add-and-edit
      ref="modalform"
      @ok='modalformOk'
    ></add-and-edit>
  </div>
</template>

<script>
import addAndEdit from './modules/addAndEditCM.vue'
import { o_getAction, o_deleteAction } from '@/api/onApi'
import { Message } from 'element-ui'
export default {
  components: {
    addAndEdit,
  },
  data() {
    return {
      onlineId: 'dbc4743ca5f1429582a988ba362f7a87',
      data: [],
      columns: [
        {
          title: '姓名',
          align: 'center',
          width: '8%',
          dataIndex: 'user_name',
        },
        {
          title: '性别',
          align: 'center',
          width: '8%',
          dataIndex: 'user_gender',
        },
        {
          title: '电话',
          align: 'center',
          width: '10%',
          dataIndex: 'user_phone',
        },
        {
          title: '地址',
          align: 'center',
          width: '15%',
          dataIndex: 'address',
        },
        {
          title: '公司名称',
          align: 'center',
          width: '10%',
          dataIndex: 'company_name',
        },
        {
          title: '信用度',
          align: 'center',
          width: '10%',
          dataIndex: 'credit',
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '15%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      addOrEditFlag: 1,
      //分页
      pageNo: 1,
      pageSize: 10,
      // dataSource: [],
      pagination: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        pageSize: 10,
        // onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
      searchIdentity: '',
    }
  },
  created() {
    this.getData()
  },
  methods: {
    modalformOk() {
      this.getData()
    },
    getData() {
      let params = {
        column: 'createTime',
        order: 'desc',
        pageNo: 1,
        pageSize: 10,
        superQueryMatchType: 'and',
      }
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params).then((res) => {
        console.log('res.result.records', res.result.records)
        this.data = res.result.records
      })
    },
    //查询操作
    searchQuery() {},
    //重置操作
    searchReset() {
      this.searchIdentity = ''
      this.pageNo = 1
      this.pageSize = 10
    },
    //新增
    handleAdd() {
      this.addOrEditFlag = 1
      this.$refs.modalform.openModal('', this.addOrEditFlag)
    },
    handleImport() {},
    handleExport() {},
    handleDelete(record) {
      o_deleteAction(`/cgform/api/form/${this.onlineId}/${record.id}`).then((res) => {
        console.log('res', res)
        this.getData()
        Message({
          showClose: true,
          message: '删除成功!',
          type: 'success',
        })
      })
    },
    //编辑
    handleEdit(record) {
      this.addOrEditFlag = 2
      console.log(record.credit)
      this.$refs.modalform.openModal(record, this.addOrEditFlag)
    },
  },
}
</script>

<style scoped>
.customer_management {
  background-color: #fff;
  padding: 24px;
}
</style>
