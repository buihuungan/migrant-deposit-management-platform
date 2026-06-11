<template>
  <div class="Identification" style="display:grid">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="6">
          </a-col>
          <a-col :md="10" :sm="10">
            <a-form-item
              label="创建时间:"
            >
              <el-date-picker
                v-model="dateTime"
                type="daterange"
                value-format="yyyy-MM-dd"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
              </el-date-picker>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item>
              <a-button type="primary" @click="searchQuery">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <div class="article-table">
      <a-table
        style="height: 500px"
        ref="table"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
      >
        <!-- <span slot="action2" slot-scope="text, record">
          <a @click="openDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定删除吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => handleDelete(record)"
          >
            <a-icon
              slot="icon"
              type="question-circle-o"
              style="color: red"
            />
            <a>删除</a>
          </a-popconfirm>
        </span> -->
      </a-table>
    </div>
    <detail ref="modal"></detail>
  </div>
</template>

<script>
import detail from "./modules/detail";
import axios from "axios";
import api from "@/api/index.js";
import { tm_deleteAction, tm_postAction, tm_getAction } from '@/api/onApi'
import {
  im_postAction,
  im_deleteAction,
  im_getAction,
  im_putAction,
} from "@/api/icimApi";
import { Message } from "element-ui";
import qs from "qs";
export default {
  components: {
    detail,
  },
  data() {
    return {
      dateTime: "",
      columns: [
        {
          title: "#",
          dataIndex: "",
          key: "rowIndex",
          width: "4%",
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "用户名",
          align: "center",
          width: "12%",
          dataIndex: "username",
        },
        {
          title: "操作信息",
          align: "center",
          width: "40%",
          dataIndex: "action",
        },
        {
          title: "表名",
          align: "center",
          width: "12%",
          dataIndex: "tableName",
        },
        {
          title: "操作时间",
          align: "center",
          width: "12%",
          dataIndex: "date",
          customRender:((item)=>{
            let date = new Date(item);
            let Y = date.getFullYear() + '-'
            let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-'
            let D = date.getDate()
            // let h = date.getHours() + ':'
            // let m = date.getMinutes() + ':'
            // let s = date.getSeconds();
            // moment(item).format("YYYY-MM-DD")+h+m+s
            return Y+M+D
          })
        },
        // {
        //   title: "操作",
        //   dataIndex: "action2",
        //   width: "20%",
        //   align: "center",
        //   scopedSlots: { customRender: "action2" },
        // },
      ],
      pageNo: 1,
      pageSize: 50,
      listOfTemplate:[],

      dataSource:[],
      deleteId:'',
      ipagination: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        pageSize: 15,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
    };
  },
  created() {
    this.getAllLogs();
  },
  methods: {
    //搜索
    searchQuery() {
      let queryDateInfo = {}
      queryDateInfo.startTime = Date.parse(this.dateTime[0]);
      queryDateInfo.endTime = Date.parse(this.dateTime[1]);
      console.log("queryDateInfo",queryDateInfo)
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      im_postAction('/logList',JSON.stringify(queryDateInfo)).then(res=>{
        console.log("res",res);
        this.dataSource = res.data.result.list;
      }).catch(err=>{
        console.log(err)
      })
    },
    //重置
    searchReset() {
      this.form={};
      this.getAllLogs();
    },
    //获取所有日志
    getAllLogs() {
      var that = this;
      console.log("this",this);
      let queryInfo = {
        pageNo: that.pageNo,
        pageSize: that.pageSize,
      };
      queryInfo.startTime = 0;
      queryInfo.endTime = 0;
      console.log("queryInfo",queryInfo)
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      im_postAction('/logList',JSON.stringify(queryInfo)).then(res=>{
        console.log("res",res);
        that.dataSource = res.data.result.list;
        console.log("dataSource",that.dataSource);
      }).catch(err=>{
        console.log(err)
      })
    },
    handleEdit(record) {
      console.log("编辑");
    },
    openDetail(record) {
      console.log("详情");
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page;
    },
    handleDelete(record) {
      console.log("删除");
        
    },
  },
};
</script>

<style scoped>
.ant-col-sm-24 {
  height: 40px;
  line-height: 40px;
}
.el-range-input{
  margin-left: 15px !important;
}
.Identification{
  background: #fff;
  /* margin:24px 16px; */
  padding:24px
}

</style>