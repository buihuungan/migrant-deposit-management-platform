<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
      <!-- <a-form layout="inline">
        <a-row :gutter="10">
          <a-col :md="10" :sm="12">
            <a-form-item label="用户账号" style="margin-left:8px">
              <a-input placeholder="请输入账号" v-model="queryParam.username"></a-input>
            </a-form-item>
          </a-col> -->
      <!--<a-col :md="8" :sm="8">-->
      <!--<a-form-item label="用户名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">-->
      <!--<a-input placeholder="请输入名称查询" v-model="queryParam.realname"></a-input>-->
      <!--</a-form-item>-->
      <!--</a-col>-->
      <!-- <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
             <a-button type="primary" @click="searchQuery" icon="search" style="margin-left: 18px">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </a-col>
          </span>
        </a-row>
      </a-form> -->
    </div>
    <!-- 操作按钮区域 -->
    <div class="table-operator" :md="24" :sm="24" style="margin-top: -15px">
      <!--<a-button @click="handleEdit" type="primary" icon="edit" style="margin-top: 16px">用户编辑</a-button>-->
      <a-button @click="handleAddUserDepart" :disabled="idNotDep" type="primary" icon="plus">已有学生</a-button>
      <a-button @click="handleAdd" type="primary" icon="plus" style="margin-top: 16px" :disabled="idNotDep">新建学生</a-button>
      <!-- <a-upload
        name="file"
        :action="uploadUrl"
        :data="paramData"
        :multiple="false"
        @change="handleChange"
        :showUploadList="false"
        :before-upload="beforeUpload"
        :headers="headers"
      >
      <a-button type="primary" icon="plus" style="margin-top: 16px" :disabled="idNotDep">上传学生信息excel表</a-button>
    </a-upload> -->
    <a-button type="primary" icon="plus" style="margin-top: 16px" :disabled="idNotDep" @click="uploadStudentInformation">上传学生信息excel表</a-button>
    </div>

    <!-- 密码格式对话框 -->
    <a-modal
      :visible="passwordVisible"
      @ok="passwordHandleOk"
      @cancel="passwordHandleCancle"
      title="密码格式"
      width="400px"
    >
      <template #footer>
        <a-button key="back" @click="passwordHandleCancle">取消</a-button>
        <a-upload
          name="file"
          :action="uploadUrl"
          :data="paramData"
          :multiple="false"
          @change="handleChange"
          :showUploadList="false"
          :before-upload="beforeUpload"
          :headers="headers"
        >
          <a-button type="primary" icon="plus" style="margin-top: 16px" :disabled="idNotDep">上传</a-button>
        </a-upload>
      </template>
      <a-form
        :model="paramData"
        name="basic"
        layout="inline"
        autocomplete="off"
      >
        <a-form-item
          label="密码前缀"
          name="prefix"
          :rules="[{ required: true, message: '请输入密码格式',trigger: 'change' }]"
        >
          <a-input v-model="paramData.prefix" display="inline" />
        </a-form-item>
        <a-form-item
          label="最终密码"
        >
          前缀+学号
        </a-form-item>
      </a-form>
    </a-modal>

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
              <!-- <a-menu-item>
                <a href="javascript:;" @click="handleDeptRole(record)">分配部门角色</a>
              </a-menu-item> -->

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
      <a-modal 
        :visible="noPassVisible" 
        :footer="null" 
        @cancel="handleCancel()"
        title="已经注册过的账号">
        <a-table
          :dataSource="noPassTable"
          :columns="noPasscolumns">
        </a-table>
      </a-modal>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <user-modal ref="modalForm" :isStudent="true" @ok="loadData"></user-modal>
    <student-Modal
      ref="studentModal"
      :studentData="dataSource"
      :classContent="classContent"
      :roleStudentId="roleStudentId"
      :sys_org_code="record.sys_org_code"
      @ok="studentModalOk"
    ></student-Modal>
    <dept-role-user-modal ref="deptRoleUser"></dept-role-user-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction, postAction, deleteAction } from '@/api/manage'
import studentModal from '../modules/studentModal'
import UserModal from './UserModal'
import pick from 'lodash.pick'
import Vue from 'vue'
import DeptRoleUserModal from '../../system/modules/DeptRoleUserModal'
import { addUser, editUser, queryall, getUserList } from '@/api/api'
import { TENANT_ID,ACCESS_TOKEN } from '@/store/mutation-types'
import api from '@/api/index'
// import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  name: 'DeptUserInfo',
  mixins: [JeecgListMixin],
  components: {
    DeptRoleUserModal,
    studentModal,
    UserModal,
  },
  props: {
    departIdList: {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  // watch: {
  //   departIdList: function (value) {
  //     if (!value.length) {
  //       return
  //     }
  //     this.loadData()
  //   },
  //   classContent:function(newvalue,old){
  //     console.log("classContent",newvalue)
  //   },
  // },
  data() {
    return {
      passwordVisible: false,
      noPasscolumns: [
        {
          title: '学号',
          align: 'center',
          dataIndex: 'studentId'
        },
        // {
        //   title: '姓名',
        //   align: 'center',
        //   dataIndex: ''
        // }
      ],
      noPassTable: [],
      noPassVisible: false,
      headers: {
        "X-Access-Token": Vue.ls.get(ACCESS_TOKEN),
      },
      uploadUrl: api.server_url + api.global_edu_baseURL + '/eduStudent/uploadStudent/forRegister', 
      paramData: {
        classCode: '',
        tenantName: '',
        departId:'',
        prefix: '',
      },
      idNotDep: true,
      description: '用户信息',
      currentDeptId: '',
      classContent: {},
      // 表头
      columns: [
        {
          title: '用户账号',
          align: 'center',
          dataIndex: 'username',
        },
        {
          title: '用户名称',
          align: 'center',
          dataIndex: 'realname',
        },
        {
          title: '性别',
          align: 'center',
          dataIndex: 'sex',
        },
        {
          title: '专业',
          align: 'center',
          dataIndex: 'subject_name',
        },
        {
          title: '班级',
          align: 'center',
          dataIndex: 'class_name',
        },
        {
          title: '电话',
          align: 'center',
          dataIndex: 'phone',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 150,
        },
      ],
      url: {
        list: '/sys/user/departUserList',
        edit: '/sys/user/editSysDepartWithUser',
        delete: '/sys/user/deleteUserInDepart',
        deleteBatch: '/sys/user/deleteUserInDepartBatch',
      },
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
      roleStudentId: '',
      record: {},
      studentUrl: {
        search: '/online/cgreport/api/getColumnsAndData/1356531732298924033',
        // search: '/online/cgform/api/getData/46487d65227f45338ff2cff1b4e748a7',
        delete: '/online/cgform/api/form/46487d65227f45338ff2cff1b4e748a7',
        userdelete: '/sys/user/delete',
      },
    }
  },
  created() {},

  methods: {
    uploadStudentInformation() {
      this.passwordVisible = true
    },
    passwordHandleCancle() {
      this.paramData.prefix = ''
      this.passwordVisible = false
    },
    passwordHandleOk() {
      this.passwordVisible = false
    },
    handleCancel() {
      this.noPassVisible = false
    },
    beforeUpload(file) {
      console.log('file',file)
      if(file.type =='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        return true
      }
      else {
        this.$message.error('请上传xlsx格式文件！')
        return false
      }
    },
    handleChange(info) {
      console.log('info',info)
      if(info.file.status === 'done') {
        if(info.file.response.success)
        {
          this.$message.success('上传成功！')
          this.passwordHandleCancle()
        }
        else{
          this.$message.error('上传失败！')
        }
        this.loadData(1)
        if(info.file.response.result.noPass.length > 0) {
          for(var i=0;i<info.file.response.result.noPass.length;i++) {
            var params = {studentId: info.file.response.result.noPass[i]}
            this.noPassTable.push(params)
          }
          console.log('this.noPassTable',this.noPassTable)
          this.noPassVisible = true
        }
      }
    },
    studentModalOk() {
      this.loadData()
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1)
    },
    getStudentRoleId() {
      queryall().then((res) => {
        if (res.success) {
          if (res.result) {
            res.result.forEach((item) => {
              if (item.roleCode == 'student') {
                this.roleStudentId = item.id
              }
            })
          }
        } else {
          console.log(res.message)
        }
      })
    },
    returnQueryParams() {
      return this.getQueryParams()
    },
    async loadData(arg) {
      this.dataSource = []
      this.ipagination.total = 0
      await this.getStudentRoleId()

      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }

      // let params = { superQueryMatchType: 'or' }
      let params = {
        order: 'desc',
        pageNo: this.ipagination.current,
        pageSize: 10,
        tenant_id: Vue.ls.get(TENANT_ID),
      }
      if (!this.departIdList || !this.departIdList.length) {
        //没有左边栏的机构，就不查询
        return
      }

      if (this.record.type && this.record.type == 'class') {
        params.class_code = this.record.id
        params.subject_code = this.record.subject_code
        params.sys_org_code = this.record.sys_org_code
      } else if (this.currentDeptId) {
        //区分是部门
        if (this.record.parentId) {
          params.subject_code = this.record.id
          params.sys_org_code = this.record.parentId
        } else {
          //机构了，只能找机构下的部门id了
          params.sys_org_code = this.record.id
        }
      }
      console.log('params', params)
      getAction(this.studentUrl.search, params).then((res) => {
        this.dataSource = res.result.data.records
        this.ipagination.total = res.result.data.total
      })
    },
    batchDel: function () {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！')
        return
      } else {
        var ids = ''
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ','
        }
        var that = this
        console.log('this.currentDeptId', this.currentDeptId)
        this.$confirm({
          title: '确认删除',
          content: '是否删除?',
          onOk: function () {
            deleteAction(that.url.deleteBatch, { depId: that.currentDeptId, userIds: ids }).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.loadData()
                that.onClearSelected()
              } else {
                that.$message.warning(res.message)
              }
            })
          },
        })
      }
    },
    handleDelete(record) {
      var that = this
      deleteAction(`${that.studentUrl.delete}/${record.id}`)
        .then((data) => {
          if (data.success) {
            that.$message.success(data.message)
            if (this.selectedRowKeys.length > 0) {
              for (let i = 0; i < this.selectedRowKeys.length; i++) {
                if (this.selectedRowKeys[i] == id) {
                  this.selectedRowKeys.splice(i, 1)
                  break
                }
              }
            }
            that.loadData()
          } else {
            that.$message.warning('删除失败')
          }
        })
        .catch((e) => {
          that.$message.warning('删除失败')
        })
    },
    open(record,treeInformation) {
      console.log('record',record)
      console.log('treeInformation',treeInformation)
      //是否为班级
      if (record.type != 'class') {
        this.idNotDep = true
      } else {
        // 部门
        this.idNotDep = false
        this.paramData.departId = record.sys_org_code
        this.paramData.classCode = record.id
        // record.sys_org_code
        for(var i=0;i<treeInformation.length;i++) {
          if(record.sys_org_code == treeInformation[i].id) {
            this.paramData.tenantName = treeInformation[i].departName
          }
        }
        console.log('this.tenantName',this.tenantName)
      }
      this.record = record
      this.currentDeptId = record.id
      this.classContent = pick(record, 'subject_name', 'title', 'subject_code', 'id', 'tenant_id')
      this.loadData(1)
    },
    clearList() {
      this.currentDeptId = ''
      this.dataSource = []
    },
    hasSelectDept() {
      if (this.currentDeptId == '') {
        this.$message.error('请选择一个部门!')
        return false
      }
      return true
    },
    handleAddUserDepart() {
      if (this.currentDeptId == '') {
        this.$message.error('请选择一个部门!')
      } else {
        this.$refs.studentModal.add()
      }
    },
    handleEdit: function (record) {
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.disableSubmit = false
      this.$refs.modalForm.roleStudentId = this.roleStudentId
      this.$refs.modalForm.edit(record, true)
    },
    handleAdd: function () {
      if (this.currentDeptId == '' || !this.record.type || this.record.type != 'class') {
        this.$message.error('请选择一个班级!')
      } else {
        this.$refs.modalForm.departDisabled = true
        this.$refs.modalForm.roleStudentId = this.roleStudentId
        this.$refs.modalForm.userDepartModel.departIdList = [this.currentDeptId] //传入一个部门id

        this.$refs.modalForm.add({
          info: this.record,
          params: this.returnQueryParams(),
        })
        this.$refs.modalForm.title = '新增'
      }
    },
    handleDeptRole(record) {
      if (this.currentDeptId != '') {
        this.$refs.deptRoleUser.add(record, this.currentDeptId)
        this.$refs.deptRoleUser.title = '部门角色分配'
      } else {
        this.$message.warning('请先选择一个部门!')
      }
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