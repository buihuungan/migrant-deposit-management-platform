<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
    </div>
    <!-- 操作按钮区域 -->
    <div class="table-operator" :md="24" :sm="24" style="margin-top: -15px">
      <!--<a-button @click="handleEdit" type="primary" icon="edit" style="margin-top: 16px">用户编辑</a-button>-->
      <a-button @click="handleAddUserDepart" type="primary" icon="plus" :disabled="idNotDep">已有教师</a-button>
      <a-button @click="handleAdd" type="primary" icon="plus" style="margin-top: 16px" :disabled="idNotDep"
        >新建老师</a-button
      >
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        borderedz
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
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <user-modal ref="modalForm" :nowParentDepart = "nowParentDepart" :isStudent="false" @ok="loadData"></user-modal>
    <teacher-Modal
      ref="teacherModal"
      :teacherData="dataSource"
      :subjectContent="subjectContent"
      :roleTeacherId="roleTeacherId"
      :sys_org_code="record.sys_org_code"
      @ok="teacherModalOk()"
    ></teacher-Modal>
    <dept-role-user-modal ref="deptRoleUser"></dept-role-user-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction, postAction, deleteAction } from '@/api/manage'
import SelectUserModal from '../../system/modules/SelectUserModal'
import teacherModal from '../modules/teacherModal'
import UserModal from './UserModal'
import pick from 'lodash.pick'
import DeptRoleUserModal from '../../system/modules/DeptRoleUserModal'
import { addUser, editUser, queryall, getUserList } from '@/api/api'
import Vue from 'vue'
import { TENANT_ID } from '@/store/mutation-types'
import compro from '../../../components/FormMaking/components/componentProperties'

export default {
  name: 'DeptUserInfo',
  mixins: [JeecgListMixin],
  components: {
    DeptRoleUserModal,
    SelectUserModal,
    'teacher-Modal': teacherModal,
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
  //   subjectContent:function(newvalue,old){
  //     console.log("subjectContent",newvalue)
  //   },
  // },
  data() {
    return {
      idNotDep: true,
      description: '用户信息',
      currentDeptId: '',
      // 当前部门信息
      nowParentDepart: '',
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
        // {
        //   title: '部门',
        //   align: "center",
        //   dataIndex: 'orgCode'
        // },
        {
          title: '专业',
          align: 'center',
          dataIndex: 'subject_name',
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
      url: {
        list: '/sys/user/departUserList',
        edit: '/sys/user/editSysDepartWithUser',
        delete: '/sys/user/deleteUserInDepart',
        deleteBatch: '/sys/user/deleteUserInDepartBatch',
      },
      roleTeacherId: '',
      subjectContent: {},
      record: {},
      teacherUrl: {
        search: '/online/cgreport/api/getColumnsAndData/1356570278204342273',
        delete: '/online/cgform/api/form/45e06c958a3b40be83e712cfff43ec68',
        userdelete: '/sys/user/delete',
      },
    }
  },
  created() {},

  methods: {
    teacherModalOk() {
      this.loadData()
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1)
    },
    getTeacherRoleId() {
      queryall().then((res) => {
        if (res.success) {
          if (res.result) {
            res.result.forEach((item) => {
              if (item.roleCode == 'A0201') {
                this.roleTeacherId = item.id
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
      await this.getTeacherRoleId()

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
      getAction(this.teacherUrl.search, params).then((res) => {
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
        console.log(this.currentDeptId)
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
      // if (!this.url.delete) {
      //   this.$message.error("请设置url.delete属性!")
      //   return
      // }
      // if (!this.currentDeptId) {
      //   this.$message.error("未选中任何部门，无法删除!")
      //   return
      // }
      var that = this
      deleteAction(`${that.teacherUrl.delete}/${record.id}`)
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
    open(record) {
      //是机构
      if (!record.parentId) {
        this.idNotDep = true
      } else {
        // 部门
        this.idNotDep = false
      }
      this.record = record
      this.currentDeptId = record.id
      this.nowParentDepart = record.parentId
      console.log("record",record);
      this.subjectContent = pick(record, 'departName', 'id', 'sys_org_code', 'tenant_id','parentId')
      console.log("this.subjectContent",this.subjectContent);
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
        this.$refs.teacherModal.add()
      }
    },
    handleEdit: function (record) {
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.disableSubmit = false
      this.$refs.modalForm.roleTeacherId = this.roleTeacherId
      this.$refs.modalForm.edit(record, true)
    },
    handleAdd: function () {
      if (this.currentDeptId == '' || !this.record.parentId) {
        this.$message.error('请选择一个部门!')
      } else {
        this.$refs.modalForm.departDisabled = true
        this.$refs.modalForm.userDepartModel.departIdList = [this.currentDeptId] //传入一个部门id
        this.$refs.modalForm.roleTeacherId = this.roleTeacherId
        this.$refs.modalForm.add({
          info: this.record,
          params: this.returnQueryParams(),
        })
        this.$refs.modalForm.title = '新增'
      }
    },
    selectOK(data) {
      console.log('------------提交数据', data)
      let params = {}
      params.depId = this.currentDeptId
      params.userIdList = []
      for (var a = 0; a < data.length; a++) {
        params.userIdList.push(data[a])
      }
      console.log(params)
      postAction(this.url.edit, params).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.loadData()
        } else {
          this.$message.warning(res.message)
        }
      })
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