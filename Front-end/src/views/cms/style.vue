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
                <a-form-item label="风格名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
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
          <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">上传皮肤</a-button>
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
              <a @click="usestyle()">使用风格</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </span>
          </a-table>
        </div>
      </div>
    <upload ref="modalform" :data.sync="filePath"></upload>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
import moment from 'moment'
import upload from './modules/upload.vue'

export default {
  name: "vEnclosure",
  components:{
    moment
  },
  data: function () {
    return {
      dataSource: [],
      name:'',
      filePath:'',
      columns: [
        {
          title: '风格名称',
          align: 'center',
          dataIndex: 'themeName',
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          align: 'center',
          customRender:(createTime)=>{
              return moment(createTime).format('YYYY-MM-DD HH-MM-SS')
          }
        },
        {
          title: '更新时间',
          align: 'center',
          dataIndex: 'updateTime',
          customRender:(updateTime)=>{
              return moment(updateTime).format('YYYY-MM-DD HH-MM-SS')
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
        c_postAction("/admin/theme/list", { page: this.ipagination.current, pageSize: 10, themeName: arg })
        .then((res) => {
          console.log(res);
          console.log("success");
          _this.dataSource = res.data;
        //   _this.ipagination.total = res.data.total
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
    usestyle(){
        c_postAction('/admin/theme/add',this.filePath).then(res=>{

        }).catch(err=>{
            console.log("error")
        })
    },
    searchQuery(){
      console.log('搜索姓名' , this.name)
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
      c_postAction("/admin/theme/delete",{id: string3})
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
  overflow: auto;
  text-align: left;
  background-color: white;
  height: 740px;
  padding: 24px;
}
</style>