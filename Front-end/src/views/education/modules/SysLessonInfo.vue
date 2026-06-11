<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
    </div>
    <!-- 操作按钮区域 -->
    <div class="table-operator" :md="24" :sm="24" style="margin-top: -15px">
      <a-button @click="handleAdd" type="primary" icon="plus" style="margin-top: 16px" :disabled="idNotDep"
        >新建课程</a-button
      >
    </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />

          <a-dropdown>
            <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <lesson-modal ref="modalForm" @ok="modalFormOk" @loaddata="loadData"></lesson-modal>
  </a-card>
</template>

<script>
import Vue from 'vue'
import { TENANT_ID } from '@/store/mutation-types'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction, postAction, deleteAction } from '@/api/manage'
// import SelectUserModal from '../../system/modules/SelectUserModal'
import LessonModal from './LessonModal'
import pick from 'lodash.pick'

export default {
  name: 'LessonInfo',
  mixins: [JeecgListMixin],
  components: {
    // DeptRoleUserModal,
    // SelectUserModal,
    LessonModal,
  },
  props: {
    departIdList: {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  data() {
    return {
      idNotDep: true,
      sys_org_code: '',
      currentDeptId: '',
      dataSource: [],
      dataSource1: [],
      // 表头
      columns: [
        {
          title: '课程名称',
          align: 'center',
          dataIndex: 'lesson_name',
        },
        {
          title: '课程描述',
          align: 'center',
          dataIndex: 'lesson_description',
        },
        {
          title: '课程类别',
          align: 'center',
          dataIndex: 'category',
        },
        {
          title: '对应教师',
          align: 'center',
          dataIndex: 'teacher_name',
        },
        {
          title: '仿真课件',
          align: 'center',
          dataIndex: 'ware_name',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 150,
        },
      ],
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['5', '10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: false,
        showSizeChanger: false,
        total: 0,
      },
      record: {},
      selectUniversityName: '',
      url: {
        search: '/online/cgreport/api/getColumnsAndData/1356967172441178114',
        change: '/online/cgform/api/form/53349c93563748bf9bac7eec1ea09d3f',
        searchdetail: '/online/cgform/api/form/53349c93563748bf9bac7eec1ea09d3f',
      },
    }
  },
  methods: {
    searchReset() {
      this.queryParam = {}
      this.loadData(1)
    },
    loadData(arg) {
      this.dataSource = []
      this.ipagination.total = 0
      if (!this.departIdList || !this.departIdList.length) {
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      let params = {
        order: 'desc',
        pageNo: this.ipagination.current,
        pageSize: 10,
      }
      params.org_code = this.sys_org_code
      getAction(this.url.search, params).then((res) => {
        this.dataSource = res.result.data.records
        this.ipagination.total = res.result.data.total
      })
    },
    handleDelete: function (record) {
      var that = this
      deleteAction(`${that.url.change}/${record.id}`).then((res) => {
        if (res.success) {
          that.$message.success(res.message)
          that.loadData()
        } else {
          that.$message.error(res.message)
        }
      })
    },

    open(record) {
      this.sys_org_code = record.id
      this.idNotDep = false
      this.record = record
      this.currentDeptId = record.id
      this.loadData(1)
    },
    clearList() {
      this.currentDeptId = ''
      this.dataSource = []
    },
    hasSelectDept() {
      if (this.currentDeptId == '') {
        this.$message.error('请选择一个机构!')
        return false
      }
      return true
    },
    handleAddUserDepart() {
      if (this.currentDeptId == '') {
        this.$message.error('请选择一个机构!')
      } else {
        this.$refs.selectUserModal.visible = true
      }
    },
    handleEdit: function (record) {
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.disableSubmit = false
      this.$refs.modalForm.edit(record, true)
    },
    handleAdd: function () {
      this.$refs.modalForm.add(this.record)
      this.$refs.modalForm.title = '新增'
    },
  },
}
</script>
<style scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 3px;
}

.ant-card {
  margin-left: -30px;
  margin-right: -30px;
}

.table-page-search-wrapper {
  margin-top: -16px;
  margin-bottom: 16px;
}
</style>
