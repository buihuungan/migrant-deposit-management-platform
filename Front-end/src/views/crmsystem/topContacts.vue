<template>
<!-- 常用联系人 -->
  <div class="top_contacts" style="display: grid">
    <!-- 搜索、新增、导入导出栏 -->
    <div class="table-head">
      <a-form>
        <a-row :gutter="20">
          <a-col :span="6">
            <a-form-item label="" :label-col="{}" :wrapper-col="{}">
              <a-input placeholder="请输入姓名搜索" v-model="searchIdentity" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item>
              <a-button type="primary" @click="searchQuery">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item>
          <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
        </a-form-item>
      </a-form>
      <!-- 表格 -->
    </div>
    <div class="table-acticle">
      <a-table
        ref="table"
        :columns="columns"
        :data-source="data"
        :pagination="pagination"
        :rowKey="(record) => record.id"
      >
        <template slot-scope="text, record" slot="gender">
          <span v-if="record.user_gender === '0'">女</span>
          <span v-if="record.user_gender === '1'">男</span>
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="你确定删除吗？" ok-text="确定" cancel-text="取消" @confirm="() => handleDelete(record)">
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>
    <add-and-edit ref="modalform" @ok="modalformOk"></add-and-edit>
  </div>
</template>

<script>
import addAndEdit from './modules/addAndEditTC.vue'
import { Message } from 'element-ui'
import { o_getAction, o_deleteAction } from '@/api/onApi'
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
          width: '10%',
          dataIndex: 'user_name',
        },
        {
          title: '性别',
          align: 'center',
          width: '7%',
          dataIndex: 'user_gender',
          scopedSlots: { customRender: 'gender' },
        },
        {
          title: '关联公司',
          align: 'center',
          width: '18%',
          dataIndex: 'company_name',
        },
        {
          title: '职务',
          align: 'center',
          width: '10%',
          dataIndex: 'position',
        },
        {
          title: '联系电话',
          align: 'center',
          width: '15%',
          dataIndex: 'user_phone',
        },
        {
          title: '电子邮箱',
          align: 'center',
          width: '15%',
          dataIndex: 'user_email',
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
        current: 1,
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
        pageNo: this.pagination.current,
        pageSize: 10,
        superQueryMatchType: 'and',
      }
      params.superQueryParams = encodeURIComponent(
        JSON.stringify([
          {
            rule: 'eq',
            type: 'number',
            val: 1,
            field: 'frequent',
          },
        ])
      )
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params).then((res) => {
        console.log('res.result.records', res.result.records)
        this.pagination.total = res.result.total
        this.data = res.result.records
        // this.data = res.result.records.filter((item) => {
        //   return item.frequent === 1
        // })
        // for (i = 0; i < res.result.records.length; i++) {
        //   if (res.result.records[i].frequent === '1') {
        //     this.data = res.result.records[i]
        //   }
        // }
      })
    },
    onPageChange(page) {
      console.log(page)
      this.pagination.current = page
      if (this.inSearch) {
        //处于查询状态，分页数据不能再次重新查询
        this.searchQuery()
      } else {
        this.getData()
      }
    },
    //查询操作
    searchQuery() {
      this.inSearch = true
      let params = {
        pageSize: 10,
        pageNo: 1,
        column: 'createTime',
        order: 'desc',
      }
      params.superQueryMatchType = 'and'
      params.superQueryParams = encodeURIComponent(
        JSON.stringify([
          {
            rule: 'like',
            type: 'text',
            val: this.searchIdentity,
            field: 'user_name',
          },
        ])
      )
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params)
        .then((res) => {
          console.log('res', res)
          this.pagination.current = 1
          this.pagination.total = res.result.total
          this.data = res.result.records
        })
        .catch((err) => {})
    },
    //重置操作
    searchReset() {
      this.searchIdentity = ''
      this.pageNo = 1
      this.pageSize = 10
      this.getData()
    },
    //新增
    handleAdd() {
      this.addOrEditFlag = 1
      this.$refs.modalform.openModal('', this.addOrEditFlag)
    },
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
.top_contacts {
  background-color: #fff;
  padding: 24px;
}
</style>
