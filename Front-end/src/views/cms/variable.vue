<template>
  <div class="variable">
    <div class="col-lg">
      <div class="col-ld">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="变量管理" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                  <a-input placeholder="请输入变量名查询" v-model="name"></a-input>
                </a-form-item>
              </a-col>
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button type="primary" style="margin-left: 8px" @click="searchReset">重置</a-button>
                </a-col>
              </span>
            </a-row>
          </a-form>
        </div>

        <div class="table-operator" style="margin: 5px 0 10px 2px">
          <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">添加</a-button>
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
      <addVariable ref="modalform" @ok="modalformok"></addVariable>
      <editVariable ref="modalform1" @ok="modalformok"></editVariable>
    </div>
  </div>
</template>

<script>
import addVariable from './modules/addVariable'
import editVariable from './modules/editVariable'
import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
export default {
  components: {
    addVariable,
    editVariable
  },
  name: 'variable',
  data() {
    return {
      dataSource: [],
      name:'',
      columns: [
        {
          title: '变量名',
          align: 'center',
          dataIndex: 'itemName',
        },
        {
          title: '描述',
          align: 'center',
          dataIndex: 'info',
        },
        {
          title: '组',
          align: 'center',
          dataIndex: 'groupId',
        },
        {
          title: '变量值',
          align: 'center',
          dataIndex: 'value',
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
  activated(){
    this.Getvariable()
  },
  methods: {
    Getvariable(arg) {
      var _this = this
      c_postAction('/admin/variable/list',{
        cid: '',
        page: this.ipagination.current,
        pageSize: 10,
        name:arg
      }).then(
        function (res) {
          _this.dataSource = res.data.list
          _this.ipagination.total = res.data.total
        },
        function (err) {
          console.log(err)
        }
      )
    },
    modalformok(){
      this.Getvariable()
    },
    handleSizeChange(index) {
      this.queryInfo.page = 1
      this.queryInfo.pageSize = index
      this.Getvariable()
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    handleCurrentChange(index) {
      this.queryInfo.page = index
      this.Getvariable()
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.Getvariable()
    },
    searchQuery() {
      this.Getvariable(this.name)
    },
    searchReset() {
      this.name = ''
      this.Getvariable()
    },
    handleAdd(){
      this.$refs.modalform.handleOpen()
    },
    handleEdit(record) {
      // console.log("fycb")
      this.$refs.modalform1.editVariable(record)
    },
    async handleDelete(record) {
      var _this = this
      // const res = await this.$confirm('此操作将删除该变量, 是否继续?', '提示', {
      //   confirmButtonText: '确定',
      //   cancelButtonText: '取消',
      //   type: 'warning',
      // }).catch((err) => err)
      // if (res !== 'confirm') {
      //   return this.$message.info('取消删除')
      // }
      var queryInfo = { id: record.id }
      var _this = this
      c_postAction('/admin/variable/delete', queryInfo).then(
        function (res) {
          _this.Getvariable()
        },
        function (err) {
          console.log(err)
        }
      )
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
      window.sessionStorage.setItem('message_variable', JSON.stringify(val))
      var MJsonStr = sessionStorage.getItem('message_variable')
      MJsonStr = JSON.parse(MJsonStr)
      console.log(MJsonStr)
      this.$router.push('/variable/editVariable')
    },
  },
  created() {
    this.Getvariable()
  },
}
</script>

<style scoped>
/* .addvariable {
  font-size: 12px;
  padding-top: 5px;
  padding-right: 10px;
  padding-bottom: 5px;
  padding-left: 10px;
  border-radius: 4px;
  color: #fff;
  background-color: #5bc0de;
  border-color: #46b8da;
}
.addvariable:hover {
  background-color: #2eb8e2;
}
.Delete {
  padding: 7px 15px;
  border-radius: 4px;
}
.eDit:hover {
  background-color: #45e76b;
}
.eDit {
  padding: 7px 14px;
  background: #27c24c;
  border-radius: 5px;
  color: white;
} */
.variable {
  /* float: right;
  width: 84%;
  height: 100%;
  border: 1px solid #000000; */
  overflow: auto;
  background-color: white;
  padding: 24px;
  /* height: 740px; */
}
/* .panel-table {
  margin-right: 15px;
  margin-left: 15px;
} */
/* 
.col-md,
.col-lg {
  position: relative;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
}
.title {
  text-align: left;
  border-bottom: 1px solid #eee;
  background-color: white;
  padding: 10px 15px;
  border-top-right-radius: 3px;
  border-top-left-radius: 3px;
}
h1 {
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 10px;
}
.breadcrumb {
  text-align: left;
  padding: 8px 15px;
  list-style: none;
  background-color: white;
  border-radius: 4px;
  margin-top: 0px;
  margin-bottom: 20px;
}
li {
  display: inline;
}
a {
  text-decoration: none;
}
.panel-body {
  text-align: left;
  padding: 15px;
  background-color: white;
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
.pagination {
  display: inline-block;
  padding-left: 0;
  margin: 20px 0;
  border-radius: 4px;
}
.pagination > li > a,
.pagination > li > span {
  position: relative;
  float: left;
  padding: 6px 12px;
  margin-left: -1px;
  line-height: 1.428571429;
  text-decoration: none;
  background-color: #fff;
  border: 1px solid #ddd;
}
.pagination > li > a {
  background: #fafafa;
  color: #666;
}
ul {
  display: block;
  list-style-type: disc;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  padding-inline-start: 40px;
} */
</style>
