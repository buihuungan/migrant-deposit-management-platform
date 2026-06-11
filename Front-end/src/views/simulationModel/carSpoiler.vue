<template>
  <div>
    <el-card>
      <a-popconfirm
        :disabled="isStuding"
        title="确定开课吗?"
        cancelText="取消"
        okText="确定"
        @confirm="() => startClass()"
      >
        <a-icon slot="icon" type="question-circle-o" style="color: red" />
        <a-button :disabled="isStuding" type="primary" style="margin-right: 20px">开课</a-button>
      </a-popconfirm>
      <a-popconfirm
        :disabled="!isStuding"
        title="确定结课吗?"
        cancelText="取消"
        okText="确定"
        @confirm="() => endClass()"
      >
        <a-icon slot="icon" type="question-circle-o" style="color: red" />
        <a-button type="primary" :disabled="!isStuding" style="margin-right: 20px">结课</a-button>
      </a-popconfirm>
      <a-button :disabled="detailVisiable" type="primary" style="margin-right: 20px" @click="getMessage"
        >获取工艺信息</a-button
      >
      <div style="margin-top: 10px; margin-bottom: 10px">
        <span v-if="isStuding"
          >课程地址：<a @click="openKMClass">{{ classURL }}</a>
        </span>
      </div>
      <div v-if="detailVisiable1">
        <el-container>
          <el-aside width="451px">
            <div>
              <span>MABOM代号为{{ argProcessFlag }}的基本信息</span>
            </div>
            <el-table
              :data="DocList"
              :show-header="false"
              :cell-style="changeCellStyle"
              style="width: 401; margin-top: 10px"
            >
              <el-table-column prop="tableHeader" label="表头" align="right" width="100px"></el-table-column>
              <el-table-column prop="tableData" label="数据" align="left" width="300px"></el-table-column>
            </el-table>
          </el-aside>
          <el-main style="padding: 0 0 0 20px">
            <div>
              <span>MABOM代号为{{ argProcessFlag }}的加工信息</span>
            </div>
            <el-tree :data="treeList" :props="defaultProps" style="margin-top: 10px"></el-tree>
          </el-main>
        </el-container>
      </div>
    </el-card>
  </div>
</template>

<script>
import {e_httpAction} from '@/api/eduApi.js'

export default {
  data() {
    return {
      detailVisiable1: false,
      detailVisiable: true,
      argProcessFlag: '88.343.000005/MABOM/_32FFZTCYBC',
      DocList: [],
      classURL: "",
      workMessage: '',
      isStuding: false,
      headerList: [{
        label: '标识',
        code: 'Symbol'
      }, {
        label: '名称',
        code: 'Name'
      }, {
        label: '版本',
        code: 'Version'
      }, {
        label: '分类',
        code: 'ObjClsName'
      }, {
        label: '创建人',
        code: 'Creator'
      }, {
        label: '创建时间',
        code: 'CreateTime'
      }],
      treeList: [],
      defaultProps: {
        children: 'Material',
        label: 'Name'
      }
    }
  },
  created() { },
  methods: {
    openKMClass () {
      window.open(this.classURL)
    },
    changeCellStyle(row, column, rowIndex, columnIndex) {
      //列的label的名称
      if (row.column.label === "表头") {
        return 'color:black' //修改的样式
      } else {
        return ''
      }
    },
    startClass() {
      e_httpAction('/abuttingJoint/getStartClass',{
          argLoginName: '1',
          argStudentName: '2',
          argRoleName: '工艺员'
        },'POST').then(res => {
          console.log(res.result);
        if (res.result.d != '') {
          this.classURL = res.result.d
          
          this.$message.success('开课成功！')
          this.isStuding = true
          this.detailVisiable = false
        };
      })
    },
    endClass() {
      e_httpAction('/abuttingJoint/getEndClass',{
          argLoginName: '1',
        },'POST').then(res => {
        if (res.result.d != '') {
          this.$message.success('结课成功！')
          this.isStuding = false
          this.detailVisiable = true
          this.detailVisiable1 = false
        };
      })
    },
    getMessage() {
      e_httpAction('/abuttingJoint/GetProcessDocInfo',{
           argProcessFlag: this.argProcessFlag,
        },'POST').then(res => {
        if (res.result.d != '') {
          this.$message.success("获取流程信息成功")
        }
        this.detailVisiable1 = true
        this.detailVisiable = true
        var data1 = JSON.parse(res.result.d).Doc
        this.treeList = JSON.parse(res.result.d).Process
        for (var i = 0; i < this.headerList.length; i++) {
          this.DocList.push(new Object({
            tableHeader: this.headerList[i].label,
            tableData: data1[this.headerList[i].code]
          }))
        }
        console.log(this.DocList);
        console.log(JSON.parse(res.result.d));
      })
    }
  },
}
</script>

<style scoped>
.mobile_work_shop {
  background: #fff;
  padding: 24px;
}
</style>