<script src="//unpkg.com/vue/dist/vue.js"></script>
<script src="//unpkg.com/element-ui@2.13.2/lib/index.js"></script>
<template>
  <div class="column">
    <div class="col-lg">
      <div class="col-ld">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="栏目名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                  <a-input placeholder="请输入栏目名称查询" v-model="name"></a-input>
                </a-form-item>
              </a-col>
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-col :md="6" :sm="24">
                  <a-button class="ant-btn" type="primary" @click="searchQuery">查询</a-button>
                  <a-button class="ant-btn-primary" style="margin-left: 8px" @click="searchReset">重置</a-button>
                </a-col>
              </span>
            </a-row>
          </a-form>
        </div>

        <div class="table-operator" style="margin: 5px 0 10px 2px">
          <a-button class="ant-btn-primary" icon="plus" @click="handleAdd()">添加</a-button>
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
              <a @click="handleAdd1(record)">添加下级栏目</a>
              <a-divider type="vertical" />
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                <a>删除</a>
              </a-popconfirm>
            </span>
          </a-table>
        </div>
      </div>
      <addColumn ref="modalform" @ok="modalformok"></addColumn>
      <editColumn ref="modalform1" @ok="modalformok"></editColumn>
    </div>
  </div>
</template>

<script>
import addColumn from './modules/addColumn'
import editColumn from './modules/editColumn'
import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
import moment from 'moment'
export default {
  components:{
    addColumn,
    editColumn
  },
  data() {
    return {
      dataSource: [],
      name:"",
      columns: [
        {
          title: '编码',
          align: 'center',
          dataIndex: 'code',
        },
        {
          title: '栏目名称',
          align: 'center',
          dataIndex: 'cnname',
        },
        {
          title: '英文名称',
          dataIndex: 'enname',
          align: 'center',
        },
        {
          title: '显示隐藏',
          align: 'center',
          dataIndex: 'isShow',
          customRender: (isShow)=>{
            if(isShow===1){
              return "是"
            }else{
              return "否"
            }
          }
        },
        {
          title: '排序',
          align: 'center',
          dataIndex: 'sort',
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
        page: 1,
        pageSize: 10,
      },
      queryParam: {},
    }
  },

  methods: {
    load(tree, treeNode, resolve) {
      setTimeout(() => {
        resolve([
          {
            id: 31,
            code: '35502znj',
            columnname: '需求详情	',
            englishname: '',
            showorhide: '否',
            sort: '50',
          },
          {
            id: 32,
            code: '822n58d3',
            columnname: '需求发布',
            englishname: '',
            showorhide: '否',
            sort: '50',
          },
        ])
      }, 1000)
    },
    //获取后台栏目数据
    Getcolumn(arg) {
      var _this = this
      c_postAction('/admin/category/list', {
          page: this.ipagination.current,
          pageSize: 10,
          cnname:arg
        })
        .then(
          function (res) {
            _this.dataSource = res.data.list
            _this.ipagination.total = res.data.total
          },
          function (err) {
            console.log(err)
          }
        )
    },
    searchQuery() {
      this.Getcolumn(this.name)
    },
    searchReset() {
      this.name = '',
      this.Getcolumn();
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.Getcolumn()
    },
    toAddCategory: function () {
      // const vm = this;
      // vm.$router.push({ name: './Add_column', params: { id: 10 } })
      // window.location.href = './Add_column'
      this.$router.replace({ path: '/Add_column' })
    },
    updateSort: function () {
      console.log("冯勇最帅！");
    },
    handleAdd(){
      this.$refs.modalform.handleOpen()
    },
    handleAdd1(record) {
      this.$refs.modalform.handleOpen()
    },
    handleEdit(record) {
      this.$refs.modalform1.handleOpen(record)
    },
    async handleDelete(index, row) {
      var _this = this
      const res = await this.$elementConfirm('此操作将永久删除该栏目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).catch((err) => err)
      if (res !== 'confirm') {
        return this.$message.info('取消删除')
      }
      var queryInfo = { id: row.id }
      var _this = this
      c_postAction('/admin/category/delete', queryInfo).then(
        function (res) {
          console.log(res)
          _this.Getcolumn()
        },
        function (err) {
          console.log(err)
        }
      )
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
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
    bianji(val) {
      window.sessionStorage.setItem('message_category', JSON.stringify(val))
      var MJsonStr = sessionStorage.getItem('message_category')
      MJsonStr = JSON.parse(MJsonStr)
      console.log(MJsonStr)
      this.$router.push('/column/editColumn')
    },
    modalformok(){
      this.Getcolumn()
    },
    //页面改变获取数据
    handleSizeChange(index) {
      this.queryInfo.page = 1
      this.queryInfo.pageSize = index
      this.Getcolumn()
    },
    handleCurrentChange(index) {
      this.queryInfo.page = index
      this.Getcolumn()
    },
  },
  created() {
    this.Getcolumn()
  },
}
</script>

<style>
/* @import url('//unpkg.com/element-ui@2.13.2/lib/theme-chalk/index.css'); */

.column {
  /* float: right;
  width: 84%;
  height: 650px; */
  background-color: white;
  padding: 24px;
  /* border: 1px solid #000000; */
  overflow: auto;
  text-align: left;
}

/* .ant-btn {
  color: #fff;
  background-color: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.12);
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.045);
} */
/* 
.columnrow {
  padding-left: 15px;
  padding-right: 15px;
} */
/* 
.columnmanage {
  background: white;
} */

/* .panel-heading {
  text-align: left;
  margin-top: 20px;
  margin-right: 0px;
  margin-bottom: 10px;
  margin-left: 0px;
  padding-top: 10px;
  padding-right: 15px;
  padding-bottom: 10px;
  padding-left: 15px;
} */

/* ul.route {
  background: white;
  margin-top: 0px;
  margin-right: 0px;
  margin-bottom: 20px;
  margin-left: 0px;
  padding-top: 8px;
  padding-right: 15px;
  padding-bottom: 8px;
  padding-left: 15px;
  border-radius: 4px;
  text-align: left;
} */

/* .home-page-route,
.column-route {
  display: inline;
  border-radius: 4px;
} */

/* .panel-body > .el-button {
  display: inline-block;
  padding-top: 5px;
  padding-right: 5px;
  padding-bottom: 5px;
  padding-left: 5px;
  font-size: 12px;
  margin-left: 10px;
  border-radius: 4px;
} */
/* .home-page-route-a {
  text-decoration: none;
} */

/* .panel {
  margin-bottom: 20px;
  background-color: white;
  border-radius: 4px;
}

.panel-body {
  padding-top: 15px;
  padding-right: 15px;
  padding-bottom: 15px;
  padding-left: 15px;
  text-align: left;
} */

/* .addtopcolumn {
  background-color: #409eff;
  border: #409eff solid 1px;
  color: white;
  font-size: 12px;
  border-radius: 4px;
  padding: 5px;
  text-decoration: none;
} */

/* .panel > .panel-body + .table,
.panel > .panel-body + .table-responsive {
  border-top: 1px solid #ddd;
} */

/* .columnoperate {
  display: inline-block;
}

.editcolumnrouter {
  background-color: #27c24c;
  color: white;
  text-decoration: none;
  padding-top: 5px;
  padding-right: 10px;
  padding-bottom: 5px;
  padding-left: 10px;
  border-radius: 4px;
}

.editcolumnrouter:hover {
  background-color: rgb(151, 216, 85);
} */
</style>
