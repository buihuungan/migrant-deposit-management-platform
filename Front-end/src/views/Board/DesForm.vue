<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="看板名称">
          <a-input v-model="searchData.name" placeholder="请输入看板名称"/>
        </a-form-item>
        <a-form-item label="看板描述">
          <a-input v-model="searchData.description" placeholder="请输入看板描述"/>
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
        </a-button-group>
        <a-button v-show="selectedRowKeys.length>0" style="margin-left:20px" @click="openDeleteFormConfirm('Multiple',selectedRowKeys)">批量删除</a-button>
      </div>
      <div style="padding-top:20px">
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
          <i class="anticon anticon-info-circle ant-alert-icon">
          </i> 已选择 <a><b>{{ selectedRowKeys.length }}</b></a>项
          <a style="margin-left: 24px" @click="clearSelectedRows">清空</a>
        </div>
        <a-table
          :loading="tableLoading"
          :columns="columns"
          :dataSource="formTableData"
          :rowSelection="{selectedRowKeys: selectedRowKeys,onChange: onSelectChange}"
          :pagination="ipagination"
        >
          <span class="desformspana" slot="action" slot-scope="text, record, index">
            <a @click="openEditFormModal(index)">编辑</a>
            <a-divider type="vertical" />
            <a @click="openDesFormModal(index)">
              <a-icon type="setting" />设计看板
            </a>
            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">
                更多<a-icon type="down" />
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a @click="openDeleteFormConfirm('single',index)">删除</a>
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
    >
    </addForm-modal>
    <desForm-modal
      ref="desFormModal"
      :desFormModalVisible.sync="desFormModalVisible"
      :formTableData.sync="formTableData"
      :tableIndex="selectedFormIndex"
      :searchData.sync="searchData"
      @loadData="loadData"
    >
    </desForm-modal>
    <editForm-modal
      ref="editFormModal"
      :editFormModalVisible.sync="editFormModalVisible"
      :formTableData.sync="formTableData"
      :tableIndex="selectedFormIndex"
      :searchData.sync="searchData"
      @loadData="loadData"
    >
    </editForm-modal>
  </div>
</template>

<script>
import $ from 'jquery'
import AddFormModal from './modules/AddFormMedal'
import DesFormModal from './modules/DesFormModal'
import EditFormModal from './modules/EditFormModal'
import {axios} from "../../utils/request";
import qs from 'qs'
import {getScreen,deleteScreen,deleteBatchScreen,getOneScreen} from '@api/boardapi'

export default {
  name: 'DesForm',
  components: {
    AddFormModal,
    DesFormModal,
    EditFormModal
  },
  data() {
    return {
      searchData: {
        name: '',
        description: '',
        deleteFlag : 1,
        current: 1,
        size: 10,
      },
      searchData1: {//重置的时候传参用。如果无参会将已经删除掉的数据也给查出来了
        name: '',
        description: '',
        deleteFlag : 1,
        current: 1,
        size: 10,
      },
      ipagination:{
        current:0,
        pageSize:10,
        pageSizeOptions:['10','20','30'],
        total:0,
        showSizeChanger:true,
        showQuickJumper:true,
        showTotal:(total, range)=>range+' 共'+total+'条',
        onChange:this.onPageChange.bind(this),
        onShowSizeChange:(current, pageSize)=>{
          this.ipagination.current = 1
          this.ipagination.pageSize = pageSize
          this.searchData.current = 1
          this.searchData.size = pageSize
          this.loadData(this.searchData)
          }
        },
      tableLoading: false,
      columns: [
        {
          title: '看板名称',
          align: 'center',
          dataIndex: 'name',
        },
        {
          title: '看板描述',
          align: 'center',
          dataIndex: 'description',
        },
        // {
        //   title: '看板图标',
        //   align: 'center',
        //   dataIndex: 'icon',
        // },
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
      selectTableId: ''
    }
  },
  created() {
    this.loadData(this.searchData)
  },
  methods: {
    loadData(data) {
      this.tableLoading = true;
     getScreen(qs.stringify(data)).then(res => {
       console.log('55555555555',res);
        if (res.status===200) {
          this.formTableData = res.data.records
          this.ipagination.total=res.data.total
        } else {
          this.$message.error('数据获取失败')
          return false
        }
        this.tableLoading = false
      })
    },
    getFormContent(index) {
      return new Promise((resolve, reject) => {
        getOneScreen(this.formTableData[index].id).then((res) => {
          console.log("获取的大屏",res);
          if (res.status===200) {
            this.$store.commit('changeChooseTablefromId', this.formTableData[index].id)
            if (res.data.content) {
              this.formTableData[index].content = res.data.content
              this.$store.commit('changeFormMakingJson', JSON.parse(res.data.content))
            } else {
              this.formTableData[index].content = ''
              this.$store.commit('changeFormMakingJson', [])
            }
          } else {
            this.$message.warning('看板不存在!')
          }
          resolve(this.$store.state.formMakingJson)
        }).catch((e) => {
          console.log(e)
        })
      })
    },
    desgetTableCol() {
      return new Promise((resolve, reject) => {
        if (!this.$store.state.formMakingJson.config.tableId) {
          this.$store.commit('formMakingJson', [])
        } else {
          axios({
            url: `/online/cgform/field/listByHeadId?headId=${this.$store.state.formMakingJson.config.tableId}`,
            method: 'get'
          }).then((res) => {
            this.$store.commit('changeTableCols', res.result)
            resolve(this.$store.state.tableCols)
          }).catch((err) => {
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
      this.getFormContent(index).then((data) => {
        if ($.isEmptyObject(data)) {
          return false
        } else if (data.config.tableId) {
          return this.desgetTableCol()
        } else {
          return false
        }
      }).then((data) => {
        this.selectedFormIndex = index
        this.desFormModalVisible = true
        this.$refs.desFormModal.handleOpen()
      })
    },
    openEditFormModal(index) {
      this.selectedFormIndex = index
      this.editFormModalVisible = true
    },
    openDeleteFormConfirm(type, index) {
      this.$confirm({
        title: '提示',
        content: '确定删除?',
        onOk: () => {
          if (type === 'single') {
            deleteScreen(this.formTableData[index].id).then((res) => {
              if (res.status===200) {
                this.$message.success('删除成功')
              } else {
                this.$message.error('删除失败')
              }
              this.loadData(this.searchData)
            }).catch((e) => {
              this.$message.error(e)
            })
          } else {
            let requestData = []
            for (let i = 0; i < index.length; i++) {
              requestData[i] = this.formTableData[index[i]].id
            }
           deleteBatchScreen(requestData.toString()).then((res) => {
              if (res.status===200) {
                this.$message.success('删除成功')
              } else {
                this.$message.error('删除失败')
              }
              this.loadData(this.searchData)
            }).catch((e) => {
              this.$message.error(e)
            })
          }
        }
      })
    },
    openTestFormModal(index) {
      axios({
        url: `/desform/${this.formTableData[index].id}/getConent`,
        method: 'get',
        baseURL: this.FORM_MAKING_BASE_URL
      }).then((res) => {
        if (res.success) {
          this.formTableData[index].content = res.data
          this.selectedFormIndex = index
          this.testFormModalVisible = true
        } else {
          this.$message.warning(res.message)
        }
      }).catch((e) => {
        console.log(e)
      })
    },
    openConfigAddressModal(index) {
      axios({
        url: `/desform/${this.formTableData[index].id}/getConent`,
        method: 'get',
        baseURL: this.FORM_MAKING_BASE_URL
      }).then((res) => {
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
      }).catch((e) => {
        console.log(e)
      })
    },
    openFormData(index) {
      axios({
        url: `/desform/${this.formTableData[index].id}/getConent`,
        method: 'get',
        baseURL: this.FORM_MAKING_BASE_URL
      }).then((res) => {
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
      }).catch((e) => {
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
        url: '/online/cgform/head/list',
        method: 'get',
        data: param
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
    onPageChange(page,pageSize){
      this.ipagination.current = page
      this.searchData.current = page
      this.searchData.size=pageSize
      this.loadData(this.searchData)
    },
    deleteMultipleForm(index) {}
  },
}
</script>

<style>
.desformspana a {
  color: #1890ff !important;
}
</style>