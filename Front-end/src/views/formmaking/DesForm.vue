<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="表单名称">
          <a-input v-model="searchData.name" placeholder="请输入表单名称" />
        </a-form-item>
        <a-form-item label="表单编码">
          <a-input v-model="searchData.encoding" placeholder="请输入表单编号" />
        </a-form-item>
        <a-form-item>
          <a-button-group>
            <a-button type="primary" icon="search" @click="loadData(searchData)">查询</a-button>
            <a-button type="primary" icon="reload" @click="loadData(searchData1)">重置</a-button>
          </a-button-group>
        </a-form-item>
      </a-form>
      <div>
        <a-button-group>
          <a-button type="primary" icon="plus" @click="openAddFormModal">新增</a-button>
          <!-- <a-button
            type="primary"
            icon="reload"
          >重置索引</a-button> -->
        </a-button-group>
        <a-button
          v-show="selectedRowKeys.length > 0"
          style="margin-left: 20px"
          @click="openDeleteFormConfirm('Multiple', selectedRowKeys)"
          >批量删除</a-button
        >
      </div>
      <div style="padding-top: 20px">
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
          <i class="anticon anticon-info-circle ant-alert-icon"> </i> 已选择
          <a
            ><b>{{ selectedRowKeys.length }}</b></a
          >项
          <a style="margin-left: 24px" @click="clearSelectedRows">清空</a>
        </div>
        <a-table
          :loading="tableLoading"
          :columns="columns"
          :dataSource="formTableData"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          :pagination="ipagination"
        >
          <span class="desformspana" slot="action" slot-scope="text, record, index">
            <a @click="openEditFormModal(index)">编辑</a>
            <a-divider type="vertical" />
            <a @click="openDesFormModal(index)"> <a-icon type="setting" />设计表单</a>
            <a-divider type="vertical" />
            <!-- <a @click="openFormData(index)">表单数据</a>
            <a-divider type="vertical" />
            <a @click="openConfigAddressModal(index)">配置地址</a>
            <a-divider type="vertical" /> -->
            <a-dropdown>
              <a class="ant-dropdown-link">
                更多
                <a-icon type="down" />
              </a>
              <a-menu slot="overlay">
                <!-- <a-menu-item>
                  <a @click="openTestFormModal(index)">测试</a>
                </a-menu-item> -->
                <!-- <a-menu-item>
                  <a>权限控制</a>
                </a-menu-item> -->
                <a-menu-item>
                  <a @click="openDeleteFormConfirm('single', index)">删除</a>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </div>
    </a-card>

    <!--modal-->
    <addForm-modal
      ref="addFormModal"
      :addFormModalVisible.sync="addFormModalVisible"
      :formTableData.sync="formTableData"
      :searchData.sync="searchData"
      @loadData="loadData"
    ></addForm-modal>
    <desForm-modal
      ref="desFormModal"
      :desFormModalVisible.sync="desFormModalVisible"
      :formTableData.sync="formTableData"
      :tableIndex="selectedFormIndex"
      :searchData.sync="searchData"
      @loadData="loadData"
    ></desForm-modal>
    <editForm-modal
      ref="editFormModal"
      :editFormModalVisible.sync="editFormModalVisible"
      :formTableData.sync="formTableData"
      :tableIndex="selectedFormIndex"
      :searchData.sync="searchData"
      @loadData="loadData"
    ></editForm-modal>
    <!-- <testForm-modal
      ref="testFormModal"
      :testFormModalVisible.sync="testFormModalVisible"
      :formTableData.sync="formTableData"
      :tableIndex="selectedFormIndex"
    ></testForm-modal>
    <configAddress-modal
      ref="configAddressModal"
      :configAddressModalVisible.sync="configAddressModalVisible"
      :formTableData.sync="formTableData"
      :tableIndex="selectedFormIndex"
      :tableId="selectTableId"
    ></configAddress-modal> -->
  </div>
</template>

<script>
import $ from 'jquery'
import AddFormModal from './modules/AddFormMedal'
import DesFormModal from './modules/DesFormModal'
import EditFormModal from './modules/EditFormModal'
// import TestFormModal from './modules/TestFormModal'
// import ConfigAddressModal from './modules/ConfigAddressModal'
import { getAction } from '@/api/manage'
import { t_postAction, t_deleteAction, t_getAction, batch_deletion } from '@/api/tempApi'
import { o_getAction } from '@/api/onApi'
import { axios } from '@/utils/request'
import { ACCESS_TOKEN, USER_NAME, USER_INFO, ENCRYPTED_STRING } from '@/store/mutation-types'
import Vue from 'vue'

export default {
  name: 'DesForm',
  components: {
    AddFormModal,
    DesFormModal,
    EditFormModal,
    // TestFormModal,
    // ConfigAddressModal,
  },
  data() {
    return {
      searchData: {
        name: '',
        encoding: '',
        deleteFlag: 1,
        current: 1,
        size: 10,
      },
      searchData1: {
        //重置的时候传参用。如果无参会将已经删除掉的数据也给查出来了
        name: '',
        encoding: '',
        deleteFlag: 1,
        current: 1,
        size: 10,
      },
      ipagination: {
        current: 0,
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
          this.searchData.current = 1
          this.searchData.size = pageSize
          this.loadData(this.searchData)
        },
      },
      tableLoading: false,
      columns: [
        {
          title: '表单名称',
          align: 'center',
          dataIndex: 'name',
        },
        {
          title: '表单编号',
          align: 'center',
          dataIndex: 'encoding',
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
        },
        {
          title: '更新时间',
          align: 'center',
          dataIndex: 'updateTime',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],

      formTableData: [],
      selectedRowKeys: [],
      selectedRows: [],
      addFormModalVisible: false,
      desFormModalVisible: false,
      selectedFormIndex: 0,
      editFormModalVisible: false,
      testFormModalVisible: false,
      configAddressModalVisible: false,
      selectTableId: '',
      // total:0,
    }
  },
  created() {
    this.loadData(this.searchData)
    // console.log('地址',process.env.VUE_APP_ONLINE_BASE_URL)
  },
  methods: {
    loadData(data) {
      this.tableLoading = true
      console.log(data)
      let qs = require('qs')
      t_postAction('/admin/desform/search', qs.stringify(data)).then((res) => {
        if (res.success) {
          console.log(res)
          this.formTableData = res.result.list
          this.ipagination.total = res.result.total
          this.tableLoading = false
        } else {
          this.$message.error('数据获取失败')
          return false
        }
      })
    },
    getFormContent(index) {
      return new Promise((resolve, reject) => {
        t_getAction('/admin/desform/' + this.formTableData[index].id + '/getConent')
          .then((res) => {
            if (res.success) {
              console.log('所选表单的id为：' + this.formTableData[index].id)
              this.$store.commit('changeChooseTablefromId', this.formTableData[index].id)
              // console.log('所选表单的changeChooseTablefromId为：', this.$store.state.chooseTablefromId)
              this.formTableData[index].content = res.result
              console.log('所选表单的json数据为：', this.formTableData[index].content)
              if (this.formTableData[index].content) {
                this.$store.commit('changeFormMakingJson', JSON.parse(res.result))
                console.log('this.$store.state.formMakingJson.list为：', this.$store.state.formMakingJson.list)
              } else {
                this.$store.commit('changeFormMakingJson', [])
                console.log('DesForm界面this.$store.state.formMakingJson.list为空')
              }
            } else {
              this.$message.warning(res.message)
            }
            resolve(this.$store.state.formMakingJson)
          })
          .catch((e) => {
            console.log(e)
          })
      })
    },
    desgetTableCol() {
      return new Promise((resolve, reject) => {
        console.log('DesForm执行了desgetTableCol函数，绑定数据表id为' + this.$store.state.formMakingJson.config.tableId)
        if (!this.$store.state.formMakingJson.config.tableId) {
          console.log(
            'this.$store.state.formMakingJson.config.tableId=',
            this.$store.state.formMakingJson.config.tableId
          )
          // this.$store.commit('formMakingJson', [])
          console.log('没绑定数据表，清空字段列表')
        } else {
          console.log('DesForm页面开始请求数据库中表字段')
          o_getAction(`/cgform/field/listByHeadId?headId=${this.$store.state.formMakingJson.config.tableId}`)
            .then((res) => {
              console.log(res)
              this.$store.commit('changeTableCols', res.result)
              console.log('DesForm页面请求数据表字段开始输出')
              console.log(this.$store.state.tableCols)
              console.log('DesForm页面请求数据表字段输出结束')
              resolve(this.$store.state.tableCols)
            })
            .catch((err) => {
              console.log('字段属性获取数据表字段属性失败！')
            })
        }
      })
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectionRows
    },
    clearSelectedRows() {
      this.selectedRowKeys = []
    },
    openAddFormModal() {
      this.addFormModalVisible = true
    },
    openDesFormModal(index) {
      this.getFormContent(index)
        .then((data) => {
          console.log('openDesFormModal(index)函数里面的data值为：', data)
          if ($.isEmptyObject(data)) {
            console.log('date是空的****************')
            return false
          } else {
            if (data.config.tableId) {
              return this.desgetTableCol()
            } else {
              return false
            }
          }
        })
        .then((data) => {
          this.selectedFormIndex = index
          this.desFormModalVisible = true
          this.$refs.desFormModal.handleOpen()
        })

      // this.getFormContent(index)
      // this.desgetTableCol()
      // this.selectedFormIndex = index
      // this.desFormModalVisible = true
      // this.$refs.desFormModal.handleOpen()
    },
    openEditFormModal(index) {
      this.selectedFormIndex = index
      this.editFormModalVisible = true
    },
    openDeleteFormConfirm(type, index) {
      const that = this
      this.$elementConfirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          if (type === 'single') {
            t_postAction('/admin/desform/updateDeleteFlag?id=' + that.formTableData[index].id + '&deleteFlag=' + 0)
              .then((res) => {
                if (res.success) {
                  that.$message.success('删除成功')
                } else {
                  that.$message.error('删除失败')
                }
                that.loadData(that.searchData)
              })
              .catch((e) => {
                that.$message.error(e)
              })
          } else {
            let requestData = []
            for (let i = 0; i < index.length; i++) {
              requestData[i] = that.formTableData[index[i]].id
            }
            let jsonRequest = {
              ids: requestData,
            }
            // axios({
            //   url: '/admin/desform/ids/delete',
            //   method: 'delete',
            //   data: requestData,
            //   baseURL: '/api',
            // })
            batch_deletion('/admin/desform/ids/delete', jsonRequest)
              .then((res) => {
                if (res.success) {
                  that.$message.success('删除成功')
                } else {
                  that.$message.error('删除失败')
                }
                that.loadData(this.searchData)
              })
              .catch((e) => {
                that.$message.error(e)
              })
          }
        })
        .catch(() => {})

      // 原jeecg代码
      // let that = this
      // this.$confirm({
      //   title: '确定删除',
      //   content: '是否删除数据?',
      //   okType: 'danger',
      //   onOk() {
      //     if (type === 'single') {
      //       // t_deleteAction('/admin/desform/'+that.formTableData[index].id+'/delete')
      //       t_postAction('https://git.data.wust.edu.cn/api1/fmaking/admin/desform/updateDeleteFlag?id=' + that.formTableData[index].id + '&deleteFlag=' + 0)
      //         // t_postAction('/admin/desform/updateDeleteFlag',{data:{id:that.formTableData[index].id,deleteFlag:0}})
      //         .then((res) => {
      //           if (res.success) {
      //             that.$message.success('删除成功')
      //           } else {
      //             that.$message.error('删除失败')
      //           }
      //           that.loadData(that.searchData)
      //         })
      //         .catch((e) => {
      //           that.$message.error(e)
      //         })
      //     } else {
      //       let requestData = []
      //       for (let i = 0; i < index.length; i++) {
      //         requestData[i] = that.formTableData[index[i]].id
      //       }
      //       axios({
      //         url: '/admin/desform/ids/delete',
      //         method: 'delete',
      //         data: requestData,
      //         baseURL: '/api',
      //       })
      //         .then((res) => {
      //           if (res.success) {
      //             that.$message.success('删除成功')
      //           } else {
      //             that.$message.error('删除失败')
      //           }
      //           that.loadData(this.searchData)
      //         })
      //         .catch((e) => {
      //           that.$message.error(e)
      //         })
      //     }
      //   },
      // })
    },
    openTestFormModal(index) {
      axios({
        url: `/admin/desform/${this.formTableData[index].id}/getConent`,
        method: 'get',
        baseURL: process.env.VUE_APP_ONLINE_BASE_URL,
      })
        .then((res) => {
          if (res.success) {
            this.formTableData[index].content = res.data
            this.selectedFormIndex = index
            this.testFormModalVisible = true
          } else {
            this.$message.warning(res.message)
          }
        })
        .catch((e) => {
          console.log(e)
        })
    },
    openConfigAddressModal(index) {
      axios({
        url: `/admin/desform/${this.formTableData[index].id}/getConent`,
        method: 'get',
        baseURL: process.env.VUE_APP_ONLINE_BASE_URL,
      })
        .then((res) => {
          if (res.success) {
            this.formTableData[index].content = res.data
            let formJson = JSON.parse(this.formTableData[index].content)
            if (formJson.config.hasOwnProperty('tableId') && formJson.config.tableId !== '') {
              this.selectTableId = formJson.config.tableId
              this.selectedFormIndex = index
              this.configAddressModalVisible = true
            } else {
              this.$message.warning('未绑定数据表')
            }
          } else {
            this.$message.error(res.message)
          }
        })
        .catch((e) => {
          console.log(e)
        })
    },
    openFormData(index) {
      axios({
        url: `/admin/desform/${this.formTableData[index].id}/getConent`,
        method: 'get',
        baseURL: process.env.VUE_APP_ONLINE_BASE_URL,
      })
        .then((res) => {
          if (res.success) {
            this.formTableData[index].content = res.data
            let formJson = JSON.parse(this.formTableData[index].content)
            if (formJson.config.hasOwnProperty('tableId') && formJson.config.tableId !== '') {
              this.$router.push('/fmaking/formData/' + formJson.config.tableId)
            } else {
              this.$message.warning('未绑定数据表')
            }
          } else {
            this.$message.warning(res.message)
          }
        })
        .catch((e) => {
          console.log(e)
        })

      let param = Object.assign({}, this.queryParam, this.isorter, this.filters)
      param.copyType = 0
      param.field = 'id,tableName'
      if (this.formTableData[index].content === '') {
        this.$message.error('未绑定数据表')
        return false
      }
      let formJson = JSON.parse(this.formTableData[index].content)
      let tableId = ''
      if (formJson.config.dataBase === '') {
        tableId = ''
        this.$message.error('未绑定数据表')
        return false
      }
      axios({
        url: '/admin/cgform/head/list',
        method: 'post',
        data: param,
        baseURL: Vue.API_BASE_URL,
      }).then((res) => {
        if (res.success) {
          this.$store.commit('changeDataBaseList', res.result.records)
          let record = res.result.records.find((item) => item.tableName === formJson.config.dataBase)
          if (record === undefined) {
            this.$message.error('未找到对应数据表')
          } else {
            tableId = record.id
            this.$router.push('/fmaking/formData/' + tableId)
          }
        }
        if (res.code === 510) {
          this.$message.warning(res.message)
          return false
        }
      })
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.searchData.current = page
      this.searchData.size = pageSize
      this.loadData(this.searchData)
    },
    // onShowSizeChange(current, size) {
    //   // this.pageSize = size,
    //   console.log("onShowSizeChange函数触发",current,size)
    //   this.searchData.size=size,
    //   this.ipagination.current = current,
    //   this.searchData.current = current
    //   this.loadData(this.searchData)
    // },
    deleteMultipleForm(index) {},
  },
}
</script>

<style>
/* .ant-form-inline .ant-form-item{
    display: inline-flex !important;
  } */
.desformspana a {
  color: #1890ff !important;
}
</style>