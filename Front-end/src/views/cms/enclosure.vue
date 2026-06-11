<script src="//unpkg.com/vue/dist/vue.js"></script>
<script src="//unpkg.com/element-ui@2.13.2/lib/index.js"></script>
<template>
  <div class="enclosure">

    <div class="col-lg">
      <div class="col-ld">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="文件名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                  <a-input placeholder="请输入文件名称查询" v-model="name"></a-input>
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
              <a @click="logid(record)">下载预览</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </span>
          </a-table>
        </div>
      </div>
    <upload ref="modalform"></upload>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
import moment from 'moment'
import upload from './modules/upload.vue'
import api from '@api/index'

export default {
  name: "vEnclosure",
  components:{
    moment
  },
  data: function () {
    return {
      dataSource: [],
      name:'',
      columns: [
        {
          title: '编码',
          align: 'center',
          dataIndex: 'code',
        },
        {
          title: '文件名称',
          align: 'center',
          dataIndex: 'filename',
        },
        {
          title: '文件类型',
          dataIndex: 'filetype',
          align: 'center',
        },
        {
          title: '文件大小',
          align: 'center',
          dataIndex: 'filesize',
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
  components:{
    upload
  },
  created() {
    this.loaddata();
  },
  methods: {
    loaddata(arg) {
      var _this = this
        c_postAction("/admin/attachment/list", { filename: arg,page: this.ipagination.current, pageSize: 10 })
        .then((res) => {
          console.log(res);
          console.log("success");
          _this.dataSource = res.data.list;
          _this.ipagination.total = res.data.total
        })
        .catch((err) => {
          console.log("error");
        });
    },
    handleAdd(){
      this.$refs.modalform.handleOpen()
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
    logid(row) {
      const elt = document.createElement('a');
      let url=api.server_url+"/cms/admin/attachment/download?id=" + row.id;
      elt.setAttribute('href', url);
      elt.setAttribute('download', row.fileName);
      elt.style.display = 'none';
      document.body.appendChild(elt);
      elt.click();
      elt.remove();
      
    },
    searchQuery(){
      this.loaddata(this.name)
    },
    searchReset(){
      this.name = '';
      this.loaddata();
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    addattachment() {
      this.dialogVisible=true;
      
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loaddata()
    },
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(string3) {
      c_postAction("/admin/attachment/delete",{id: string3})
            .then((res) => {
              console.log(res);
              console.log("deletesuccess");
              console.log("删除结束");
              this.loaddata();
            })
            .catch((err) => {
              console.log("deleteerror");
            });
    },
    
    getEnclosureList: function () {},
  },
  mounted() {
    this.getEnclosureList();
  },
};
</script>

<style>
/* @import url("//unpkg.com/element-ui@2.13.2/lib/theme-chalk/index.css"); */

.enclosure {
  /* float: right;
  width: 84%;
  height: 650px;
  border: 1px solid #000000; */
  overflow: auto;
  text-align: left;
  background-color: white;
  height: 740px;
  padding: 24px;
}
/* 
.enclosurerow {
  padding-left: 15px;
  padding-right: 15px;
}

.attmanage {
  background: white;
}

.panel-heading {
  text-align: left;
  margin-top: 20px;
  margin-right: 0px;
  margin-bottom: 10px;
  margin-left: 0px;
  padding-top: 10px;
  padding-right: 15px;
  padding-bottom: 10px;
  padding-left: 15px;
}

ul.route {
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
}

.home-page-route,
.att-route {
  display: inline;
  border-radius: 4px;
}

.home-page-route-a {
  text-decoration: none;
}

.panel {
  margin-bottom: 20px;
  background-color: white;
  border-radius: 4px;
}

.enclosure-body {
  padding-top: 15px;
  padding-right: 15px;
  padding-bottom: 15px;
  padding-left: 15px;
}

.panel > .enclosure-body + .table,
.panel > .enclosure-body + .table-responsive {
  border-top: 1px solid #ddd;
}

.elbtn-toaddcategory {
  background-color: #409eff !important;
  border: #409eff solid 1px !important;
  color: white !important;
  font-size: 12px !important;
  border-radius: 4px !important;
  padding: 5px !important;
  text-decoration: none !important;
}

.labeltips {
  color: #a94442;
  background-color: #f2dede;
  padding-top: 15px;
  padding-bottom: 15px;
  border-radius: 4px;
}
.download {
  background-color: #27c24c;
  color: white;
}

.selectfile {
  margin-top: 30px;
}

.el-message-box__message {
  margin-top: 15px;
}

.downloadbutton {
  background-color: #27c24c !important;
  color: white !important;
  text-decoration: none !important;
}

.downloadbutton:hover {
  background-color: rgb(151, 216, 85);
  color: white;
} */
</style>