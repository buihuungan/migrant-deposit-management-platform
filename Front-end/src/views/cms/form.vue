<template>
  <div class="form">
     <div class="col-lg">
      <div class="col-ld">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="表单名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                  <a-input placeholder="请输入表单名称查询" v-model="formName"></a-input>
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
      <addform ref="modalform" @ok="modalformok"></addform>
      <editform ref="modalform1" @ok="modalformok"></editform>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
import addform from './modules/addform'
import editform from './modules/editform'
export default {
  components:{
    moment,
    addform,
    editform
  },
  data: function () {
    return {
      dataSource: [],
      formName:'',
      columns: [
        {
          title: '表单编码',
          align: 'center',
          dataIndex: 'code',
        },
        {
          title: '表单名称',
          align: 'center',
          dataIndex: 'formName',
        },
        {
          title: '附加表',
          dataIndex: 'tableName',
          align: 'center',
        },
        {
          title: '类型',
          align: 'center',
          dataIndex: 'type',
          customRender: (type) => {
            if(type === 1){
              return "自定义表单"
            }else{
              return "系统表单"
            }
          },
        },
        {
          title: '发布时间',
          align: 'center',
          dataIndex: 'updateTime',
          customRender: (updateTime) => {
            return moment(updateTime).format('YYYY-MM-DD')
          },
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
    };
  },
  methods: {
    Getform (arg) {
      var _this = this;
      c_postAction(
          "/admin/forms/list",
          {page: this.ipagination.current, pageSize: 10 , formName:arg}
        )
        .then(
          function (res) {
            _this.dataSource = res.data.list;
            _this.ipagination.total = res.data.total;
          },
          function (err) {
            console.log(err);
          }
        );
    },
    handleSizeChange (index) {
      this.queryInfo.page = 1;
      this.queryInfo.pageSize = index;
      this.Getform();
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.Getform()
    },
    modalformok(){
      this.Getform()
    },
    handleAdd(){
      this.$refs.modalform.handleOpen()
    },
    searchQuery(){
      this.Getform(this.formName)
    },
    searchReset(){
      this.formName = '';
      this.Getform();
    },
    handleEdit(record){
      this.$refs.modalform1.handleOpen(record)
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
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    handleCurrentChange (index) {
      this.queryInfo.page = index;
      this.Getform();
    },
    async handleDelete (row) {
      var _this = this
      // const res = await this.$confirm('此操作将删除该表单, 是否继续?', '提示', {
      //   confirmButtonText: '确定',
      //   cancelButtonText: '取消',
      //   type: 'warning'
      // }).catch(err => err)
      // if (res !== 'confirm') {
      //   return this.$message.info('取消删除')
      // }
      var queryInfo = { id: row.id }
      var _this = this
      c_postAction(
          "/admin/forms/delete",
          queryInfo
        )
        .then(
          function (res) {
            console.log(res)
            _this.Getform()
          },
          function (err) {
            console.log(err);
          }
        );
    },
    bianji (val) {
      window.sessionStorage.setItem("message_form", JSON.stringify(val));
      var MJsonStr = sessionStorage.getItem("message_form");
      MJsonStr = JSON.parse(MJsonStr);
      console.log(MJsonStr);
      this.$router.push("/form/editForm");
    }
  },
  created () {
    this.Getform();
  }
};
</script>

<style scoped>
.form {
  /* float: right;
  width: 84%;
  height: 100%; */
  overflow: auto;
  /* border: 1px solid #000000; */
  background-color: white;
  text-align: left;
  /* height: 740px; */
  padding: 24px;
}
.col-md,
.col-lg {
  position: relative;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
  text-align: initial;
}
.title {
  border-bottom: 1px solid #eee;
  background-color: white;
  padding: 10px 15px;
  border-top-right-radius: 3px;
  border-top-left-radius: 3px;
}
.active > a {
  text-decoration: none;
}
h1 {
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 10px;
}
.breadcrumb {
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
.panel-body {
  padding: 15px;
  background-color: white;
  margin-bottom: 2px;
}
.btn {
  color: #fff;
  background-color: #5bc0de;
  border-color: #46b8da;
  font-size: 12px;
  padding-left: 10px;
  padding-top: 5px;
  padding-right: 10px;
  padding-bottom: 5px;
  border-radius: 4px;
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
}
.el-tag {
  background-color: #ecf5ff;
  border-color: #d9ecff;
  display: inline-block;
  height: 32px;
  padding: 0 10px;
  line-height: 30px;
  font-size: 12px;
  /* color: #ffffff; */
  border-width: 1px;
  border-style: solid;
  border-radius: 4px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  white-space: nowrap;
}
.addform {
  background-color: white;
}
.addform > a {
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
  text-decoration: none;
}
.cell > a:hover {
  background-color: rgb(150, 224, 120);
}
</style>
