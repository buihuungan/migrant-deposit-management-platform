<template>
  <div class="article">
    <div class="col-lg">
      <div class="col-ld">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="文章标题" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                  <a-input placeholder="请输入文章标题查询" v-model="title"></a-input>
                </a-form-item>
              </a-col>
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
                </a-col>
              </span>
            </a-row>
          </a-form>
        </div>

        <div class="table-operator" style="margin: 5px 0 10px 2px">
          <a-button class="ant-btn" icon="plus" @click="handleAdd()">添加</a-button>
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
      <addArticle ref="modalform"></addArticle>
      <editarticle ref="modalform1"></editarticle>
    </div>
  </div>
</template>
<script>
import addArticle from './modules/addarticle'
import editarticle from './modules/editarticle'
import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
import moment from 'moment'
export default {
  name: 'vArticle',
  components: { 
    moment,
    addArticle,
    editarticle 
    },
  data: function () {
    return {
      dataSource: [],
      title:'',
      columns: [
        {
          title: '文章标题',
          align: 'center',
          dataIndex: 'title',
        },
        {
          title: '所属栏目',
          align: 'center',
          dataIndex: 'categoryCnName',
        },
        {
          title: '发布时间',
          dataIndex: 'updateTime',
          align: 'center',
          customRender: (text) => {
            return moment(text).format('YYYY-MM-DD')
          },
        },
        {
          title: '浏览量',
          align: 'center',
          dataIndex: 'subscribe',
        },
        {
          title: '权重',
          align: 'center',
          dataIndex: 'weight',
        },
        {
          title: '发布状态',
          align: 'center',
          dataIndex: 'status',
          customRender: (status)=>{
            if(status===0){
              return "未发布"
            }else{
              return "已发布"
            }
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      ipagination: {
        current: 0,
        pageSize: 10,
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
      queryInfo: {
        cid: '',
        page: 1,
        pageSize: 10,
      },
      queryParam: {},
    }
  },
  methods: {
    Getarticle(arg) {
      var _this = this
      c_postAction('/admin/archives/list', {cid: null, page: this.ipagination.current, pageSize: 10,title:arg}).then(
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
      this.Getarticle()
    },
    handleCurrentChange(index) {
      this.queryInfo.page = index
      this.Getarticle()
    },
    handleEdit(record) {
      this.$refs.modalform1.editarticle(record)
      window.sessionStorage.setItem('message', JSON.stringify(record))
      var MJsonStr = sessionStorage.getItem('message')
      MJsonStr = JSON.parse(MJsonStr)
      console.log(MJsonStr)
    },
    handleAdd(){
      this.$refs.modalform.handleOpen()
    },
    // onClearSelected() {
    //   this.selectedRowKeys = []
    //   this.selectionRows = []
    // },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.Getarticle()
    },
    async handleDelete(record) {
      var _this = this
      // const res = await this.$confirm('此操作将永久删除该订单, 是否继续?', '提示', {
      //   confirmButtonText: '确定',
      //   cancelButtonText: '取消',
      //   type: 'warning',
      // }).catch((err) => err)
      // if (res !== 'confirm') {
      //   return this.$message.info('取消删除')
      // }
      console.log(record)
      var queryInfo = { cid: record.categoryId, id: record.aid }
      var _this = this
      c_postAction('/admin/archives/delete', queryInfo).then(
        function (res) {
          console.log(res)
          _this.Getarticle()
        },
        function (err) {
          console.log(err)
        }
      )
    },
    searchQuery(){
      this.Getarticle(this.title)
    },
    searchReset(){
      this.title = '';
      this.Getarticle();
    },
    // bianji(val) {
    //   window.sessionStorage.setItem('message', JSON.stringify(val))
    //   var MJsonStr = sessionStorage.getItem('message')
    //   MJsonStr = JSON.parse(MJsonStr)
    //   console.log(MJsonStr)
    //   this.$router.push('/article/EditArticle')
    // },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.rightcolval = 1
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectionRows
      this.model = Object.assign({}, selectionRows[0])
      console.log(this.model)
      this.currentRoleId = selectedRowKeys[0]
      // this.loadData2()
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
  },
  created() {
    this.queryInfo.cid = sessionStorage.getItem('cid')
    this.Getarticle()
  },
}
</script>
<style scoped>
.col-ld{
  padding: 24px;
}
.ant-btn {
  color: #fff;
  background-color: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.12);
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.045);
}
.article {
  overflow: auto;
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
.breadcrumb {
  padding: 8px 15px;
  list-style: none;
  background-color: white;
  border-radius: 4px;
  margin-top: 0px;
  margin-bottom: 20px;
  text-align: left;
}
li {
  display: inline;
}
a {
  text-decoration: none;
}
.addarticle {
  padding: 15px;
  background-color: white;
  text-align: initial;
  margin-bottom: 2px;
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
  background-color: #f5f5f5;
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
.addarticle > a {
  background-color: #409eff;
  border: #409eff solid 1px;
  color: white;
  font-size: 12px;
  border-radius: 4px;
  padding: 5px;
  text-decoration: none;
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
