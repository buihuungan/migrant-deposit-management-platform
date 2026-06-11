<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper"></div>
    <div class="table-operator" :md="24" :sm="24" style="margin-top: -15px">
      <a-button @click="handleAdd" type="primary" icon="plus" style="margin-top: 16px" :disabled="!isAdd">{{
        discription
      }}</a-button>
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
        showSizeChanger
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
                <a-popconfirm title="确定取消与选中部门关联吗?" @confirm="() => handleDelete(record)">
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
    <class-modal ref="modalForm" :formItems="formItems" @ok="modalFormOk" @loadData="loadData"></class-modal>
    <Select-Class-Modal ref="SelectClassModal" @selectFinished="selectOK"></Select-Class-Modal>
    <dept-role-user-modal ref="deptRoleUser"></dept-role-user-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction, postAction, deleteAction } from '@/api/manage'
import SelectClassModal from './SelectClassModal'
import ClassModal from './ClassModal'
import DeptRoleUserModal from '../../system/modules/DeptRoleUserModal'
import { buttonDescription, columns, urls, formItems } from '../util/static'
import { shallowClone, deepClone } from '../util/util'
import { TENANT_ID } from '@/store/mutation-types'
import Vue from 'vue'

export default {
  name: 'DeptUserInfo',
  mixins: [JeecgListMixin],
  components: {
    DeptRoleUserModal,
    SelectClassModal,
    ClassModal,
  },
  props: {
    canAdd: {
      type: Boolean,
      default: false,
    },
    managementType: {
      type: String,
      default: '',
    },
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
      currentDeptId: '',
      sys_org_code: '',
      subject_code: '',
      subject_name: '',
      record: {},
      classUrl: { search: '/online/cgreport/api/getColumnsAndData/1356838975552684034' },
      professionUrl: { search: '/online/cgreport/api/getColumnsAndData/1356848251444723713' },
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
    }
  },
  computed: {
    isAdd: function () {
      const { canAdd, idNotDep } = this
      return canAdd || !idNotDep
    },
    columns: function () {
      const { managementType } = this
      return columns[managementType]
    },
    discription: function () {
      const { managementType } = this
      return buttonDescription[managementType]
    },
    url: function () {
      const { managementType } = this
      return urls[managementType]
    },
    formItems: function () {
      const { managementType } = this
      return formItems[managementType]
    },
  },
  methods: {
    searchReset() {
      this.queryParam = {}
      this.loadData()
    },
    loadData(arg, isClassFlag) {
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
        tenant_id: Vue.ls.get(TENANT_ID),
      }
      if (this.idNotDep) {
        params.sys_org_code = this.sys_org_code
      } else {
        params.sys_org_code = this.sys_org_code
        params.subject_code = this.subject_code
      }
      if (isClassFlag) {
        getAction(this.classUrl.search, params).then((res) => {
          this.dataSource = res.result.data.records
          this.ipagination.total = res.result.data.total
        })
      } else {
        getAction(this.professionUrl.search, params).then((res) => {
          this.dataSource = res.result.data.records
          this.ipagination.total = res.result.data.total
        })
      }
    },
    // batchDel: function () {
    //   if (this.selectedRowKeys.length <= 0) {
    //     this.$message.warning('请选择一条记录！')
    //     return
    //   } else {
    //     var ids = ''
    //     for (var a = 0; a < this.selectedRowKeys.length; a++) {
    //       ids += this.selectedRowKeys[a] + ','
    //     }
    //     var that = this
    //     console.log(this.currentDeptId)
    //     this.$confirm({
    //       title: '确认取消',
    //       content: '是否取消用户与选中部门的关联?',
    //       onOk: function () {
    //         deleteAction(that.url.deleteBatch, { depId: that.currentDeptId, userIds: ids }).then((res) => {
    //           if (res.success) {
    //             that.$message.success(res.message)
    //             that.loadData()
    //             that.onClearSelected()
    //           } else {
    //             that.$message.warning(res.message)
    //           }
    //         })
    //       },
    //     })
    //   }
    // },
    handleDelete: function (record) {
      const { managementType } = this
      var that = this

      deleteAction(`${that.url.delete}/${record.id}`).then((res) => {
        if (res.success) {
          const str = managementType === 'class' ? '删除成功！' : '删除成功'
          that.$message.success(res.message)
          // if (this.selectedRowKeys.length > 0) {
          //   for (let i = 0; i < this.selectedRowKeys.length; i++) {
          //     if (this.selectedRowKeys[i] == id) {
          //       this.selectedRowKeys.splice(i, 1)
          //       break
          //     }
          //   }
          // }
          console.log('managementType', managementType)
          if (managementType === 'class') {
            that.loadData(1, true)
          } else {
            that.loadData(1, false)
          }
        } else {
          that.$message.warning(res.message)
        }
      })
    },
    open(record, isClassFlag) {
      console.log('record', record)
      //是机构
      if (!record.parentId) {
        this.sys_org_code = record.id
        this.subject_code = ''
        this.idNotDep = true
      } else {
        // 部门
        this.idNotDep = false
        this.sys_org_code = record.parentId
        this.subject_code = record.id
      }
      console.log('部门班级信息', record)
      this.record = record
      this.currentDeptId = record.id
      this.loadData(1, isClassFlag)
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
        this.$refs.SelectClassModal.visible = true
      }
    },
    handleEdit: function (record) {
      const { managementType } = this
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.disableSubmit = false
      const { fix } = urls[managementType]
      this.$refs.modalForm.edit(record, managementType, fix, false, true)
    },
    handleDetail: function (record) {
      const { managementType } = this
      this.$refs.modalForm.title = '详情'
      this.$refs.modalForm.disableSubmit = true
      const { fix } = urls[managementType]
      this.$refs.modalForm.edit(record, managementType, fix, true, false)
    },
    handleAdd: function () {
      const { managementType } = this
      if (managementType === 'class' && (this.currentDeptId == '' || !this.subject_code)) {
        this.$message.error('请选择一个部门!')
        return
      }
      if (managementType === 'profession' && !this.sys_org_code) {
        this.$message.error('请选择一个专业!')
        return
      }
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.userDepartModel.departIdList = [this.currentDeptId] //传入一个部门id
      const { fix } = urls[managementType]
      this.$refs.modalForm.add(this.record, managementType, fix, false)
      this.$refs.modalForm.title = '新增'
    },
    selectOK(data) {
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