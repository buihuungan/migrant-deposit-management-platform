<template>
  <div>
    <a-modal
      :centered="true"
      :title="title"
      :width="1000"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭"
    >
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :span="10">
              <a-form-item label="用户账号">
                <a-input placeholder="请输入用户账号" v-model="queryParam.username"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- table区域-begin -->
      <div>
        <a-table
          size="small"
          bordered
          rowKey="id"
          :columns="columns1"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ y: 240 }"
          :rowSelection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange,
          }"
          @change="handleTableChange"
        >
        </a-table>
      </div>
      <!-- table区域-end -->
    </a-modal>
  </div>
</template>

<script>
import { postAction, putAction } from '../../../api/manage'
import { e_postAction } from '@/api/eduApi'

export default {
  name: 'SelectUserModal',
  props: ['teacherData', 'roleTeacherId', 'subjectContent', 'sys_org_code'],
  data() {
    return {
      title: '添加已有教师',
      names: [],
      visible: false,
      placement: 'right',
      description: '',
      // 查询条件
      queryParam: {},
      // 表头
      columns1: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 50,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '用户账号',
          align: 'center',
          width: 100,
          dataIndex: 'account',
        },
        {
          title: '用户名称',
          align: 'center',
          width: 100,
          dataIndex: 'name',
        },
        {
          title: '性别',
          align: 'center',
          width: 100,
          dataIndex: 'sex',
        },
        {
          title: '电话',
          align: 'center',
          width: 100,
          dataIndex: 'phone',
        },
      ],
      //数据集
      dataSource: [],
      // 分页参数
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      isorter: {
        column: 'createTime',
        order: 'desc',
      },
      loading: false,
      selectedRowKeys: [],
      teacherUrl: {
        insert: '/online/cgform/api/form/45e06c958a3b40be83e712cfff43ec68',
        edit: '/online/cgform/api/form/45e06c958a3b40be83e712cfff43ec68',
      },
      selectedRows: [],
      teacherId: [],
      url: {
        list: '/sys/user/list',
      },
    }
  },
  created() {},
  methods: {
    searchQuery() {
      this.loadData(1)
      console.log(this.queryParam.username)
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1)
    },
    handleCancel() {
      this.visible = false
      this.selectedRowKeys = []
      this.dataSource = []
    },
    handleOk() {
      let formParams = []
      this.selectedRowKeys.forEach((item) => {
        formParams.push({
          uid: this.dataSource[item].userId,
          work_no: this.dataSource[item].account,
          teacher_name: this.dataSource[item].name,
          org_code: this.subjectContent.parentId,
          subject_code: this.subjectContent.id,
          subject_name: this.subjectContent.departName,
          sys_tenant_id: this.subjectContent.tenant_id,
        })
      })
      let promiseList = []
      formParams.forEach((item) => {
        promiseList.push(this.insert(item))
      })
      Promise.all(promiseList).then((values) => {
        this.handleCancel()
        this.$emit('ok')
      })
    },
    insert(item) {
      return postAction(this.teacherUrl.insert, item).then((res) => {})
    },
    add() {
      this.visible = true
      this.loadData()
    },
    loadData(arg) {
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      this.teacherId = []
      this.teacherData.forEach((item) => {
        this.teacherId.push(item.uid)
      })
      var queryParams = {}
      queryParams.roleId = this.roleTeacherId
      queryParams.userList = this.teacherId
      queryParams.departId = this.sys_org_code
      e_postAction('/manager/extraTeacher', JSON.stringify(queryParams)).then((res) => {
        if (res.result.list.length > 0) {
          this.dataSource = res.result.list
        } else {
          this.$message.warning('暂无已有教师')
        }
      })
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectedRows
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      console.log(sorter)
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field
        this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
      }
      this.ipagination = pagination
      this.loadData()
    },
  },
}
</script>
<style lang="less" scoped>
.ant-card-body .table-operator {
  margin-bottom: 18px;
}

.ant-table-tbody .ant-table-row td {
  padding-top: 15px;
  padding-bottom: 15px;
}

.anty-row-operator button {
  margin: 0 5px;
}

.ant-btn-danger {
  background-color: #ffffff;
}

.ant-modal-cust-warp {
  height: 100%;
}

.ant-modal-cust-warp .ant-modal-body {
  height: calc(100% - 110px) !important;
  overflow-y: auto;
}

.ant-modal-cust-warp .ant-modal-content {
  height: 90% !important;
  overflow-y: hidden;
}
</style>