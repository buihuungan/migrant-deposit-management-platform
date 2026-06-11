
<template>
  <div class="Template">
    <div class="table-page-search-wrapper">
      <a-row :gutter="10">
        <a-col :md="leftCol" :sm="24" style="height: 685.6px">
          <a-card :bordered="false">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
              <a-form layout="inline" style="width: 100%">
                <a-row :gutter="24">
                  <a-col :md="6" :sm="24">
                    <a-form-item label="表名">
                      <a-input placeholder="请输入表名" v-model="queryParam.tableName"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="24">
                    <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                      <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                      <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px"
                        >重置</a-button
                      >
                    </span>
                  </a-col>
                </a-row>
              </a-form>
            </div>

            <!-- 操作按钮区域 -->
            <div class="table-operator">
              <a-button @click="handleTestTable" type="primary" icon="plus">新增</a-button>
              <!-- <a-button class="ant-btn" type="primary" @click="upload"> <a-icon type="cloud-upload" />导入 </a-button> -->
            </div>

            <!-- 表格 -->
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
              <i class="anticon anticon-info-circle ant-alert-icon"></i>
              已选择
              <a
                ><b>{{ selectedRowKeys1.length }}</b></a
              >项
              <a style="margin-left: 24px" @click="onClearSelected1">清空</a>
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
                :rowSelection="{
                  selectedRowKeys: selectedRowKeys1,
                  onChange: onSelectChange1,
                  type: 'radio',
                }"
              >
                <template slot="action" slot-scope="text, record">
                  <!-- <a @click="handleOpen(record)">用户</a> -->
                  <a @click="handleEdit(record)">编辑</a>
                  <a-divider type="vertical" />
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                  <a-divider type="vertical" />
                  <a @click="handleAuth(record)">角色授权</a>
                  <a-divider type="vertical" />
                  <a-dropdown>
                    <a class="ant-dropdown-link">
                      更多
                      <a-icon type="down" />
                    </a>
                    <a-menu slot="overlay">
                      <a-menu-item v-if="record.isDbSynch != 'Y'">
                        <a @click="openSyncModal(record.id)">同步数据库</a>
                      </a-menu-item>

                      <a-menu-item>
                        <a @click="outExcel(record)">导出Excel模板</a>
                      </a-menu-item>
                      <a-menu-item>
                        <a @click="outXML(record)">导出XML模板</a>
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                </template>

                <template slot="dbsync" slot-scope="text">
                  <span v-if="text === 'Y'" style="color: limegreen">已同步</span>
                  <span v-if="text === 'N'" style="color: red">未同步</span>
                </template>
              </a-table>
            </div>
            <!-- table区域-end -->
            <!-- 同步数据库提示框 -->
            <a-modal
              :width="500"
              :height="300"
              title="同步数据库"
              :visible="syncModalVisible"
              @cancel="handleCancleDbSync"
              style="top: 5%; height: 95%"
            >
              <template slot="footer">
                <a-button @click="handleCancleDbSync">关闭</a-button>
                <a-button type="primary" :loading="syncLoading" @click="handleDbSync">确定</a-button>
              </template>
              <a-radio-group v-model="synMethod">
                <a-radio style="display: block; width: 30px; height: 30px" value="normal">普通同步(保留表数据)</a-radio>
                <a-radio style="display: block; width: 30px; height: 30px" value="force"
                  >强制同步(删除表,重新生成)</a-radio
                >
              </a-radio-group>
            </a-modal>

            
            <!-- <upload ref="upload" @ok="handleOk2"></upload> -->
            <onlCgformModal ref="onformModal" @ok="modalFormOk" />
            <!-- 授权 -->
            <authModal ref="authModal" />
          </a-card>
        </a-col>
        <a-col :md="rightColMd" :sm="24" v-if="this.rightcolval == 1">
          <a-card :bordered="false">
            <div style="text-align: right">
              <a-icon type="close-circle" @click="hideList" />
            </div>
            <div class="Identification">
              <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery2">
                  <a-row :gutter="24">
                    <a-col :md="6" :sm="6">
                      <a-form-item label="前缀：" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
                        <a-input placeholder="请输入关键字搜索" v-model="form.index"></a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :md="12" :sm="12">
                      <a-form-item label="创建时间:" class="inputDate">
                        <el-date-picker
                          v-model="form.dateTime"
                          type="daterange"
                          value-format="yyyy-MM-dd"
                          range-separator="至"
                          start-placeholder="开始日期"
                          end-placeholder="结束日期"
                        >
                        </el-date-picker>
                      </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="6">
                      <a-form-item style="float: right">
                        <a-button type="primary" @click="searchQuery2">查询</a-button>
                        <a-button style="margin-left: 8px" @click="searchReset2">重置</a-button>
                      </a-form-item>
                    </a-col>
                  </a-row>
                </a-form>
              </div>
              <div class="table-operator" style="margin: 5px 0 10px 2px">
                <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd2()">新增</a-button>
                <!-- <a-button type="primary" style="margin-left: 54%" @click="openScan"
                  ><a-icon type="file-search" />扫描解析</a-button
                > -->
              </div>
              <div class="article-table">
                <a-table
                  style="height: 500px"
                  ref="table"
                  size="middle"
                  :columns="columns2"
                  :dataSource="dataSource2"
                  :pagination="ipagination2"
                >
                  <span slot="action" slot-scope="text, record">
                    <a @click="openDetail2(record)">详情</a>
                    <a-divider type="vertical" />
                    <a-dropdown>
                      <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
                      <a-menu slot="overlay">
                        <a-menu-item>
                          <a @click="handleEdit2(record)">编辑</a>
                        </a-menu-item>
                        <a-menu-item>
                          <a-popconfirm
                            title="确定删除吗?"
                            cancelText="取消"
                            okText="确定"
                            @confirm="() => handleDelete2(record)"
                          >
                            <a-icon slot="icon" type="question-circle-o" style="color: red" />
                            <a>删除</a>
                          </a-popconfirm>
                        </a-menu-item>
                      </a-menu>
                    </a-dropdown>
                  </span>
                </a-table>
              </div>
              <addIdentification2
                ref="modalform2"
                @ok="handleOk2"
                :tableName1="tableName1"
                :listData="listData"
                :tableId="tableId"
              ></addIdentification2>
              <detail2 ref="modal2" :tableName1="tableName1" :listData="listData"></detail2>
              <scanning ref="scanning"></scanning>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
<script>
import Vue from 'vue'
import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'
import { tm_deleteAction, tm_postAction, tm_getAction, tm_fakegetAction, tm_fakepostAction } from '@/api/templateApi'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import Clipboard from 'clipboard'
import { filterObj } from '@/utils/util'
import onlCgformModal from './onlCgformModal'
import authModal from './modules/authModal'
import addIdentification2 from './modules/addIdentification2'
import upload from './modules/upload'
import detail2 from './modules/detail2'
import scanning from './modules/scanning'
import axios from 'axios'
import api from '@/api/index.js'
import moment from 'moment'
import { im_postAction, im_deleteAction, im_getAction, im_putAction, im_downFile } from '@/api/icimApi'
import { pm_postAction, pm_deleteAction, pm_getAction, pm_putAction } from '@/api/icipmApi'
import { Message } from 'element-ui'
import qs from 'qs'
export default {
  name: 'Template',
  mixins: [JeecgListMixin],
  components: {
    JDictSelectTag,
    onlCgformModal,
    moment,
    detail2,
    upload,
    scanning,
    addIdentification2,
    authModal,
  },
  data() {
    return {
      // formModalShow:false,
      description: 'Online表单开发管理页面',
      // 表头
      columns: [
        // {
        //   title: '表类型',
        //   align: 'center',
        //   dataIndex: 'tableType',
        //   // customValue: ['单表', '主表','附表'],
        //   customRender: (text) => {
        //     return filterDictText(this.tableTypeDictOptions, `${text}`)
        //   },
        // },
        {
          title: '模板名称',
          align: 'center',
          width: '15%',
          dataIndex: 'tableName',
        },
        {
          title: '模板类型',
          align: 'center',
          width: '20%',
          dataIndex: 'tableTxt',
        },
        {
          title: '版本',
          align: 'center',
          width: '20%',
          dataIndex: 'tableVersion',
        },

        {
          title: '同步数据库状态',
          align: 'center',
          width: '20%',
          dataIndex: 'isDbSynch',
          scopedSlots: { customRender: 'dbsync' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '25%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/cgform/head/list',
        delete: '/cgform/head/delete',
        deleteBatch: '/cgform/head/deleteBatch',
        doDbSynch: '/cgform/api/doDbSynch/',
        removeRecord: '/cgform/head/removeRecord',
        copyOnline: '/cgform/head/copyOnline',
      },
      tableTypeDictOptions: [],
      sexDictOptions: [],
      syncModalVisible: false,
      syncFormId: '',
      synMethod: 'normal',
      syncLoading: false,
      onlineUrlTitle: '',
      onlineUrlVisible: false,
      onlineUrl: '',
      dataSource: [],
      selectedRowKeys: [],
      selectedRows: [],
      ipagination: {
        current: 0,
        pageSize: 10,
        onChange: this.onPageChange.bind(this),
        total: 0,
      },
      //交互
      selectedRowKeys1: [],
      rightcolval: 0,
      //右边
      tableId: '',
      tableName1: '',
      form: {
        index: '',
        dateTime: '',
      },

      columns2: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: '5%',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '标识编码',
          align: 'center',
          width: '30%',
          dataIndex: 'identity',
        },
        {
          title: '创建时间',
          align: 'center',
          width: '12%',
          dataIndex: 'create_time',
          customRender: (item) => {
            return moment(item).format('YYYY-MM-DD')
          },
        },
        {
          title: '更新时间',
          align: 'center',
          width: '12%',
          dataIndex: 'update_time',
          customRender: (item) => {
            if (!item) {
              return ''
            }
            return moment(item).format('YYYY-MM-DD')
          },
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '20%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      pageNo: 1,
      pageSize: 50,
      dataSource2: [],
      listData: [], //通过表id获取表属性的信息
      ipagination2: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        pageSize: 10,
        onChange: this.onPageChange2.bind(this),
        showQuickJumper: true,
      },
    }
  },
  created() {
    this.loadData()
    //初始化字典 - 表类型
    initDictOptions('cgform_table_type').then((res) => {
      console.log('cgform_table_type 初始化字典 - 表类型 initDictOptions')
      if (res.success) {
        this.tableTypeDictOptions = res.result
      }
    })
  },
  computed: {
    leftCol() {
      return this.selectedRowKeys1.length === 0 ? 24 : 12
    },
    rightColMd() {
      return this.selectedRowKeys1.length === 0 ? 0 : 12
    },
  },
  methods: {
    loadData(arg) {
      //tm_fakegetAction
      tm_getAction('/cgform/head/list', { pageNo: this.ipagination.current, tableName: arg })
        .then((res) => {
          console.log('loadData的res', res)
          if (res.success) {
            this.dataSource = res.result.records
            console.log('dataSource', this.dataSource)
            this.ipagination.total = res.result.total
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //新增模板
    handleTestTable() {
      this.$refs.onformModal.handleadd()
    },
    //编辑模板
    async handleEdit(record) {
      // console.log('handleEdit的record', record)
      await im_getAction('/modelIdentity/selectOne', { modelId: record.id })
        .then((res) => {
          console.log('handleEditres', res)
          this.$refs.onformModal.modelData = res.data.result
          this.$refs.onformModal.iform.identityIndex = res.data.result.identity.split('/')[0]
          this.$refs.onformModal.iform.identityData = res.data.result.identity.split('/')[1]
          this.$refs.onformModal.iform.category = res.data.result.category
          this.$refs.onformModal.iform.modelIdentity = res.data.result.modelIdentity
          this.$refs.onformModal.iform.unitIdentity = res.data.result.unitIdentity
        })
        .catch((err) => {
          console.log(err)
        })
      this.$refs.onformModal.handleedit(record)
    },
    //角色授权
    handleAuth(record) {
      console.log('handleAuth的record', record)
      console.log('handleAuth的record.id', record.id)
      this.$refs.authModal.openModal(record)
    },
    searchQuery() {
      this.loadData(this.queryParam.tableName)
    },
    searchReset() {
      this.queryParam.tableName = ''
      this.loadData()
    },
    doDbSynch(id) {
      tm_postAction(this.url.doDbSynch + id, { synMethod: '1' }).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.loadData()
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    modalFormOk() {
      this.loadData()
    },
    // getQueryParams() {
    //   //获取查询条件
    //   var param = Object.assign({}, this.queryParam, this.isorter, this.filters)
    //   param.field = this.getQueryField()
    //   param.pageNo = this.ipagination.current
    //   param.pageSize = this.ipagination.pageSize
    //   param.copyType = 0
    //   return filterObj(param)
    // },
    handleCancleDbSync() {
      this.syncModalVisible = false
    },
    //同步数据库过程
    handleDbSync() {
      this.syncLoading = true
      //tm_fakepostAction
      tm_postAction(this.url.doDbSynch + this.syncFormId + '/' + this.synMethod).then((res) => {
        this.syncModalVisible = false
        this.syncLoading = false
        if (res.success) {
          this.$message.success(res.message)
          this.loadData()
        } else {
          this.$message.warning(res.message)
        }
      })
      setTimeout(() => {
        if (this.syncLoading) {
          this.syncModalVisible = false
          this.syncLoading = false
          this.$message.success('网络延迟,已自动刷新!')
          this.loadData()
        }
      }, 10000)
    },
    //同步数据库
    openSyncModal(id) {
      console.log('iddidiidiiddddidi',id);
      this.syncModalVisible = true
      this.syncLoading = false
      this.syncFormId = id
    },
    //删除模板
    handleDelete(id) {
      let that = this
      tm_deleteAction('/cgform/head/delete', { id: id })
        .then((res) => {
          if (res.success) {
            that.$message.success('删除成功')
            that.loadData()
          } else {
            that.$message.warning(res.message)
          }
        })
        .then((err) => {
          console.log('err')
        })
    },
    // 翻页
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    //交互
    onClearSelected1() {
      this.selectedRowKeys1 = []
      this.selectionRows1 = []
    },
    onSelectChange1(selectedRowKeys, selectionRows) {
      this.rightcolval = 1
      this.selectedRowKeys1 = selectedRowKeys
      this.selectionRows1 = selectionRows
      this.model1 = Object.assign({}, selectionRows[0])
      this.currentRoleId = selectedRowKeys[0]
      console.log('selectedRowKeys', selectedRowKeys[0])
      console.log('selectionRows', selectionRows[0])
      //需替换为查询对应数据
      this.getAllIdentites()
    },
    handleOpen(record) {
      this.rightcolval = 1
      // this.selectedRowKeys1 = [record.key];
      // this.modfel1 = Object.assign({}, record);
      // this.currentRoleId = record.id;
      // this.getAllIdentites();
    },
    //右
    hideList() {
      this.rightcolval = 0
      this.selectedRowKeys1 = []
    },
    searchQuery2() {
      var queryDateInfo = [
        { rule: 'LIKE', type: 'text', val: '', field: 'identity' },
        { rule: 'ge', type: 'text', val: '', field: 'create_time' },
        { rule: 'le', type: 'text', val: '', field: 'create_time' },
      ]
      queryDateInfo[0].val = this.form.index
      queryDateInfo[1].val = this.form.dateTime[0]
      queryDateInfo[2].val = this.form.dateTime[1]
      console.log('queryDateInfo', queryDateInfo)
      tm_getAction('/cgform/api/getData/' + this.tableId, {
        column: 'id',
        order: 'desc',
        pageNo: this.ipagination.current,
        pageSize: 10,
        superQueryMatchType: 'and',
        superQueryParams: encodeURIComponent(JSON.stringify(queryDateInfo)),
      })
        .then((res) => {
          console.log('res', res)
          this.dataSource2 = res.result.records
          // console.log('searchQuery2的dataSource2', this.dataSource2)
        })
        .catch((err) => {
          console.log(err)
        })
      this.form = {}
    },
    searchReset2() {
      this.form = {}
      this.getAllIdentites()
    },
    //获取表内的数据
    getAllIdentites() {
      var queryInfo = {
        pageNo: this.pageNo,
        pageSize: this.pageSize,
      }
      console.log('this.selectionRows', this.selectionRows1[0].tableName)
      this.tableName1 = this.selectionRows1[0].tableName
      console.log('tableName1', this.tableName1)
      this.tableId = this.selectedRowKeys1[0]
      console.log('tableId====', this.tableId)
      tm_getAction('/cgform/api/getData/' + this.tableId, qs.stringify(queryInfo))
        .then((res) => {
          console.log('res===', res)
          this.dataSource2 = res.result.records
          console.log('getAllIdentites的dataSource2', this.dataSource2)
        })
        .catch((err) => {
          console.log(err)
        })
      //新增 编辑 属性表格信息
      tm_getAction('/cgform/field/listByHeadId/', { headId: this.tableId })
        .then((res) => {
          console.log('getAllIdentites的res2', res)
          this.listData = res.result
          console.log('getAllIdentites的listData', this.listData)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //添加
    async handleAdd2(record) {
      console.log('this.tableId', this.tableId)
      await im_getAction('/modelIdentity/selectOne', { modelId: this.tableId })
        .then((res) => {
          this.$refs.modalform2.form.identityIndex = res.data.result.identity.split('/')[0]
          this.$refs.modalform2.addonbefore = res.data.result.identity+'#'
        })
        .catch((err) => {
          console.log(err)
        })
      this.addOrEditFlag = 1
      this.$refs.modalform2.openModal(record, this.addOrEditFlag)
    },
    // 编辑
    handleEdit2(record) {
      this.addOrEditFlag = 2
      this.$refs.modalform2.openModal(record, this.addOrEditFlag)
    },
    //详情
    openDetail2(record) {
      this.$refs.modal2.getTableData(record)
      this.$nextTick(() => {
        this.$refs.modal2.openModal()
      })
    },
    onPageChange2(page, pageSize) {
      this.ipagination2.current = page
    },
    handleDelete2(record) {
      // console.log('delete删除数据的id', record.id)
      // console.log('table名', this.tableId)
      tm_deleteAction('/cgform/api/form/' + this.tableId + '/' + record.id)
        .then((res) => {
          if (res.success) {
            Message({
              type: 'success',
              showClose: true,
              message: '删除成功!',
            })
          }
          this.getAllIdentites()
        })
        .catch((err) => {
          console.log('err')
        })
    },
    handleOk2() {
      this.getAllIdentites()
    },
    outExcel(record) {
      console.log('导出Excel')
      console.log('表record', record)
      console.log('表id', record.id)
      im_downFile('/docking/excelExport/' + record.id)
        .then((res) => {
          console.log('res', res)
          let url = window.URL.createObjectURL(new Blob([res.data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', record.tableName + '.xlsx')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        })
        .catch((err) => {
          console.log(err)
        })
    },
    outXML(record) {
      console.log('导出XML')
      console.log('表id', record.id)
      im_downFile('/docking/xmlExport/' + record.id)
        .then((res) => {
          console.log('res', res)
          let url = window.URL.createObjectURL(new Blob([res.data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', record.tableName + '.xml')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        })
        .catch((err) => {
          console.log(err)
        })
    },
    // upload() {
    //   this.$refs.upload.openUpload()
    // },
    // openScan() {
    //   this.$refs.scanning.openScan()
    // },
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

.valid-error-cust {
  .ant-select-selection {
    border: 2px solid #f5222d;
  }
}

.ant-dropdown-menu-item {
  display: block !important;
}

.ant-col-sm-24 {
  height: 40px;
  line-height: 40px;
}

.el-range-input {
  margin-left: 15px !important;
}
.Identification {
  background: #fff;
  /* margin:24px 16px; */
  padding: 24px;
}
.inputDate .el-input__inner {
  width: 255px;
  height: 40px;
}
</style>