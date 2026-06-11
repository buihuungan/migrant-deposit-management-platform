<template>
  <div class="supplier">
    <div class="col-lg">
      <div class="col-ld">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="关键字" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                  <a-input placeholder="请输入关键字搜索" v-model="title"></a-input>
                </a-form-item>
              </a-col>
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" type="primary" @click="searchReset">重置</a-button>
                </a-col>
              </span>
            </a-row>
          </a-form>
        </div>

        <div class="table-operator" style="margin: 5px 0 10px 2px">
          <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">供应发布</a-button>
        </div>
        <div class="article-table">
          <a-table
            style="height:500px"
            ref="table"
            bordered
            size="middle"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :loading="loading"
          >
            <div
              :title="record.supplierDescription"
              :style="{maxWidth: '300px',whiteSpace: 'nowrap',textOverflow: 'ellipsis',overflow: 'hidden', wordWrap: 'break-word', wordBreak: 'break-all' }"
              slot="titleShow"
              slot-scope="text, record"
            >{{record.supplierDescription}}</div>
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                <a>删除</a>
              </a-popconfirm>
            </span>
          </a-table>
        </div>
      </div>
      <addSupplier ref="modalform" @ok="modalformOk"></addSupplier>
    </div>
  </div>
</template>
<script>
import addSupplier from './modules/addSupplier'
import { c_getAction, c_postAction, c_httpAction, c_putAction, c_deleteAction } from '@/api/cmsApi'
import moment from 'moment'
export default {
  name: 'vArticle',
  components: {
    moment,
    addSupplier
    // addArticle,
    // editarticle
  },
  data: function () {
    return {
      dataSource: [],
      title: '',
      columns: [
        {
          title: '方案名称',
          align: 'center',
          width: '10%',
          dataIndex: 'supplierProductName',
        },
        {
          title: '方案类型',
          align: 'center',
          dataIndex: 'supplierProductType',
        },

        {
          title: '供应商',
          align: 'center',
          dataIndex: 'supplierName',
        },
        {
          title: '所属地区',
          align: 'center',
          dataIndex: 'supplierArea',
        },
        {
          title: '主要功能',
          align: 'center',
          width: '25%',
          dataIndex: 'supplierDescription',
          scopedSlots: { customRender: 'titleShow' },
        },
        {
          title: '联系方式',
          align: 'center',
          width: '11.9%',
          dataIndex: 'supplierContact',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width:'15%',
          scopedSlots: { customRender: 'action' },
        },
      ],
      ipagination: {
        current: 0,
        pageSize: 6,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
        // showSizeChanger: true,
        total: 0,
      },
      selectedRowKeys: [],
      rightcolval: 0,
      loading: false,
      total: 10,
      queryinfo: {
        demanderArea: '',
        demanderIndustryType: '',
        demanderType: '',
        keywords: '',
        page: 1,
        pageSize: 6,
      },
      queryParam: {},
    }
  },
  methods: {
    Getsupplier(arg) {
      var _this = this
      _this.queryinfo.keywords = arg
      _this.queryinfo.page = this.ipagination.current
      c_postAction('/supplier/supplierList', this.queryinfo).then(
        function (res) {
          console.log(res)
          _this.dataSource = res.data.list
          _this.ipagination.total = res.data.total
        },
        function (err) {
          console.log(err)
        }
      )
    },
    handleSizeChange(index) {
      this.queryInfo.page = 1
      this.queryInfo.pageSize = index
      this.Getsupplier()
    },
    handleCurrentChange(index) {
      this.queryInfo.page = index
      this.Getsupplier()
    },
    handleEdit(record) {
      this.$refs.modalform.editSupplier(record)
      window.sessionStorage.setItem('message', JSON.stringify(record))
      var MJsonStr = sessionStorage.getItem('message')
      MJsonStr = JSON.parse(MJsonStr)
      console.log(MJsonStr)
    },
    handleAdd() {
      this.$refs.modalform.handleOpen()
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.Getsupplier()
    },
    handleDelete(record) {
      var _this = this
      console.log(record)
      c_postAction('/user/supplier/supplierDelete', record).then(
        function (res) {
          _this.Getsupplier()
        },
        function (err) {
          console.log(err)
        }
      )
    },
    searchQuery() {
      this.Getsupplier(this.title)
    },
    searchReset() {
      this.title = ''
      this.Getsupplier()
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.rightcolval = 1
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectionRows
      this.model = Object.assign({}, selectionRows[0])
      console.log(this.model)
      this.currentRoleId = selectedRowKeys[0]
      // this.loadData2()
    },
    modalformOk(){
      this.Getsupplier()
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
  },
  created() {
    // this.queryInfo.cid = sessionStorage.getItem('cid')
    this.Getsupplier()
  },
}
</script>
<style scoped>
.col-ld{
  padding: 24px;
}
.supplier {
  /* overflow: auto; */
  background-color: #f6f8fa;
  text-align: left;
}
.col-lg {
  background-color: white;
}
.col-md,
.col-lg {
  position: relative;
  min-height: 1px;
}
.title {
  border-bottom: 1px solid #eee;
  background-color: white;
  padding: 10px 15px;
  border-top-right-radius: 3px;
  border-top-left-radius: 3px;
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 10px;
  text-align: left;
}

li {
  display: inline;
}
a {
  text-decoration: none;
}

.table > thead > tr > th {
  vertical-align: bottom;
  border-bottom: 1px solid #dddddd;
  padding: 8px;
  line-height: 1.428571429;
}
th {
  text-align: left;
}
.panel-footer {
  background-color: white;
  border-top: 1px solid #ddd;
  border-bottom-right-radius: 3px;
  border-bottom-left-radius: 3px;
  text-align: right;
  background-color: #fff;
  margin: 15px;
}
ul {
  display: block;
  list-style-type: disc;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  padding-inline-start: 40px;
}
.el-tag {
  background-color: #ecf5ff;
  border-color: #d9ecff;
  display: inline-block;
  height: 32px;
  padding: 0 10px;
  line-height: 30px;
  font-size: 12px;
  color: #ffffff;
  border-width: 1px;
  border-style: solid;
  border-radius: 4px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  white-space: nowrap;
}
.table_title {
  color: #3c8dbc;
}
.cell > a {
  background-color: #27c24c;
  font-weight: 500;
  border-radius: 3px;
  padding: 1px 5px;
  font-size: 12px;
  color: white;
  width: 24px;
  height: 12px;
  padding-left: 15px;
  padding-right: 15px;
  padding-top: 7px;
  padding-bottom: 7px;
}
.cell > a:hover {
  background-color: rgb(150, 224, 120);
}
</style>
