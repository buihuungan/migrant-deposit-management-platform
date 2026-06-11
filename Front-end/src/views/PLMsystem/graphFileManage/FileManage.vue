<template>
  <div>
    <!-- //表单界面 -->
    <el-dialog :visible.sync="visible" :title="title" :close-on-click-modal="false" append-to-body center>
      <div>
        <generate-form
          style="margin: 0 auto"
          insite="true"
          :data="formJson"
          ref="generateForm"
          v-if="formJson.config.ui == 'element'"
          :remote="remoteFuncs"
        >
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>

        <antd-generate-form
          style="margin: 0 auto"
          insite="true"
          v-if="formJson.config.ui == 'antd'"
          :data="formJson"
          ref="generateAntForm"
          :remote="remoteFuncs"
        >
          <template v-slot:blank="scope">
            <a-input
              v-decorator="[
                'blank',
                {
                  initialValue: scope.model.blank,
                },
              ]"
              style="width: 100px"
            ></a-input>
          </template>
        </antd-generate-form>
      </div>
      <div class="submitBtn">
        <a-button type="primary" @click="handleTest()" style="margin-right: 20px">提交</a-button>
        <a-button type="primary" @click="this.visible = false">返回</a-button>
      </div>
    </el-dialog>

    <!-- 查看历史版本界面 -->
    <a-modal
      width="80%"
      v-model="historyvisible"
      title="历史版本"
      :maskClosable="false"
      :closable="false"
      centered
      style="z-index: 1"
      @ok="historyvisible = false"
      @cancel="historyvisible = false"
      ok-text="确定"
      cancel-text="取消"
    >
      <a-table :columns="historycolumns" :data-source="historydata" bordered></a-table>
    </a-modal>

    <!-- 回收站界面 -->
    <a-modal
      width="80%"
      v-model="rubishvisible"
      title="回收站"
      :maskClosable="false"
      :closable="false"
      centered
      style="z-index: 1"
      @ok="rubishvisible = false"
      @cancel="rubishvisible = false"
      ok-text="确定"
      cancel-text="取消"
    >
      <a-table :columns="rubishcolumns" :data-source="rubishdata" bordered>
        <span slot="action" slot-scope="text, record, index">
          <a @click="back(record)">还原</a>
        </span>
      </a-table>
    </a-modal>

    <!-- //主界面   -->
    <a-card>
      <a-form layout="inline">
        <a-form-item label="文档类型">
          <a-select v-model="file_type" showSearch allowClear placeholder="请选择文档类型" style="width: 250px">
            <a-select-option v-for="item in fileTypes" :value="item.id" :key="item.id">
              {{ item.label }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button-group>
            <a-button type="primary" icon="search" @click="loadData()">查询</a-button>
          </a-button-group>
        </a-form-item>
      </a-form>
      <div style="margin-top: 20px">
        <a-button-group>
          <a-button type="primary" icon="upload" @click="visible = true">上传文档</a-button>
          <a-button type="primary" icon="hdd" style="margin-left: 20px" @click="goRubish()">回收站</a-button>
        </a-button-group>
      </div>
      <div style="padding-top: 20px">
        <a-table
          :loading="tableLoading"
          :columns="columns"
          :dataSource="formTableData"
          :pagination="ipagination"
          class="j-table-force-nowrap"
          :scroll="{ x: 1300 }"
        >
          <span slot="action" slot-scope="text, record, index">
            <a @click="gohistory(record)">查看历史版本</a>
            <a-divider type="vertical" />
            <a @click="handledelete(record)">删除</a>
          </span>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import Vue from 'vue'
import GenerateForm from '../../../components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '../../../components/FormMaking/components/AntdvGenerator/GenerateForm'
import api from '@api/index'
import axios from 'axios'
import { Message } from 'element-ui'
import { rest } from 'lodash'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  name: 'FileManage',
  components: { GenerateForm, AntdGenerateForm },
  data() {
    return {
      headers: {},
      remoteFuncs: {
        func_1625800798391(resolve) {
          let selectData = []
          axios({
            url: `/cgform/api/getData/8d96abf0fbe447e2b4f56c49b231b3ec`,
            method: 'get',
            headers: {
              'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
            },
            baseURL: api.server_url + api.global_online_baseURL,
            // baseURL: 'http://192.168.137.1:8090',
          }).then((res) => {
            let data = res.data.result.records
            // console.log("获取的类型数据",data)
            for (let i = 0; i < data.length; i++) {
              let temp = {
                value: data[i].file_type,
                label: data[i].file_type,
              }
              selectData.push(temp)
            }
            // console.log("获取的类型数据",selectData)
            resolve(selectData)
          })
        },
      },
      //回收站数据
      rubishvisible: false,
      rubishcolumns: [
        {
          title: '图文档名称',
          align: 'center',
          width: '20%',
          dataIndex: 'fileName',
          ellipsis: true,
        },
        {
          title: '图文档下载链接',
          align: 'center',
          width: '30%',
          dataIndex: 'fileLink',
          customRender: (text) => (
            <a href={text} download={text}>
              {text}
            </a>
          ),
          ellipsis: true,
        },
        {
          title: '版本号',
          width: '15%',
          align: 'center',
          dataIndex: 'version',
          ellipsis: true,
        },
        {
          title: '图文档类型',
          width: '15%',
          align: 'center',
          dataIndex: 'fileType',
          ellipsis: true,
        },
        {
          title: '上传日期',
          width: '15%',
          align: 'center',
          dataIndex: 'createTime',
          ellipsis: true,
        },
        {
          title: '操作',
          width: '15%',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      rubishdata: [],

      //查看历史版本界面数据
      historyvisible: false,
      historycolumns: [
        {
          title: '图文档名称',
          align: 'center',
          width: '20%',
          dataIndex: 'fileName',
          ellipsis: true,
        },
        {
          title: '图文档下载链接',
          align: 'center',
          width: '30%',
          dataIndex: 'fileLink',
          customRender: (text) => (
            <a href={text} download={text}>
              {text}
            </a>
          ),
          ellipsis: true,
        },
        {
          title: '版本号',
          width: '15%',
          align: 'center',
          dataIndex: 'version',
          ellipsis: true,
        },
        {
          title: '图文档类型',
          width: '15%',
          align: 'center',
          dataIndex: 'fileType',
          ellipsis: true,
        },
        {
          title: '上传日期',
          width: '15%',
          align: 'center',
          dataIndex: 'createTime',
          ellipsis: true,
        },
      ],
      historydata: [],

      file_type: '',
      fileTypes: [],
      current_userid: '', //当前登录用户id
      current_syscode: 'A01', //当前登录用户部门编码
      title: '新增',
      formJson: {
        list: [],
        config: {
          labelWidth: 100,
          labelPosition: 'right',
          size: 'small',
          customClass: '',
          ui: 'element',
          layout: 'horizontal',
          labelCol: 3,
          width: '100%',
          tableId: '',
        },
      },
      destroy: true,
      visible: false,
      fileName: '',
      tableLoading: false,
      columns: [
        {
          title: '图文档名称',
          align: 'center',
          width: '20%',
          dataIndex: 'fileName',
          ellipsis: true,
        },
        {
          title: '图文档下载链接',
          align: 'center',
          width: '30%',
          dataIndex: 'fileLink',
          customRender: (text) => (
            <a href={text} download={text}>
              {text}
            </a>
          ),
          ellipsis: true,
        },
        {
          title: '版本号',
          width: '15%',
          align: 'center',
          dataIndex: 'version',
          ellipsis: true,
        },
        {
          title: '图文档类型',
          width: '15%',
          align: 'center',
          dataIndex: 'fileType',
          ellipsis: true,
        },
        {
          title: '上传日期',
          width: '15%',
          align: 'center',
          dataIndex: 'createTime',
          ellipsis: true,
        },
        {
          title: '操作',
          width: '15%',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      formTableData: [],
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        total: 0,
        showSizeChanger: true,
        showQuickJumper: true,
        // showTotal: (total, range) => range + ' 共' + total + '条',
        // onChange: this.onPageChange.bind(this),
        // onShowSizeChange: (current, pageSize) => {
        //   this.ipagination.current = 1
        //   this.ipagination.pageSize = pageSize
        //   this.loadData()
      },
    }
  },
  created() {
    this.loadformJson()
    let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
    this.current_userid = userData.value.id
    this.getFiletypes()
    console.log('当前用户信息', userData)
  },
  methods: {
    loadformJson() {
      let id = '7cd90aa590b74627bfc15a342203e269'
      axios({
        url: `/admin/desform/${id}/getConent`,
        method: 'get',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
        },
        baseURL: api.server_url + api.global_fmaking_baseURL,
      }).then((res) => {
        res = res.data
        if (res.success) {
          this.formJson = JSON.parse(res.result)
          this.loadData()
          // this.visible = true
        } else {
          this.$message.error(res.message)
        }
      })
    },
    //加载界面数据
    loadData() {
      let params = {}
      params.createBy = this.current_userid
      params.fileType = this.file_type
      axios({
        url: `/fileManage/getList`,
        method: 'post',
        data: params,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
        },
        // baseURL: 'http://192.168.137.13:37039',
        baseURL: api.server_url+api.global_plm_baseURL,
      }).then((res) => {
        console.log('当前页面数据', res)
        let data = res.data.result
        for (let i = 0; i < data.length; i++) {
          if (data[i].fileLink != '' && data[i].fileLink != undefined) {
            data[i].fileLink = api.server_url + `/plm/common/download` + data[i].fileLink
          }
        }
        this.formTableData = data
      })
    },

    //表单提交时触发
    handleTest() {
      const $form = this.formJson.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          let datajson = {}
          var formJson = this.formJson
          datajson = this.commitToDatabase(data, formJson)
          console.log('提交的数据', datajson)
          this.saveDataBase(datajson)
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    commitToDatabase(commitdata, formJson) {
      var _this = this
      let name = formJson.config.dataBase
      var onlineId = formJson.config.tableId
      let datajson = {}
      let commitdataarray = Object.keys(commitdata) /* 赋值，只需运行一次后面调用无需再处理，加快速度 */
      let commitdataarraylength = commitdataarray.length /* 获取数据长度 */
      console.log('---------', commitdata)
      console.log('---------', formJson)
      for (let i = 0; i < formJson.list.length; i++) {
        if (formJson.list[i].type == 'grid') {
          for (let j = 0; j < formJson.list[i].columns.length; j++) {
            for (let z = 0; z < formJson.list[i].columns[j].list.length; z++) {
              let model1 = formJson.list[i].columns[j].list[z].model
              let tableCol1 = formJson.list[i].columns[j].list[z].tableCol
              datajson[tableCol1.toString()] = commitdata[model1]
            }
          }
        } else if (formJson.list[i].type == 'divider' || formJson.list[i].type == 'text') {
          // let model = formJson.list[i].model
          // let key = formJson.list[i].key
          // datajson[key.toString()] = commitdata[model]
        } else if (formJson.list[i].type == 'fileupload') {
          let model = formJson.list[i].model
          let tableCol = formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        } else {
          let model = formJson.list[i].model
          let tableCol = formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        }
      }
      this.temp_datajson = datajson
      console.log('########', datajson)
      return datajson
    },

    saveDataBase(formJson) {
      let data = {}
      let datajson = JSON.parse(JSON.stringify(formJson))
      let file = JSON.parse(datajson.file_link)
      let current_time = this.getCurrentTime()
      data.createTime = current_time.time
      data.fileName = file[0].fileName
      data.fileLink = file[0].dbpath
      data.fileType = formJson.file_type
      data.createBy = this.current_userid
      data.sysOrgCode = this.current_syscode
      console.log('存到库里的', data)
      axios({
        url: `/fileManage/insertFile`,
        method: 'post',
        data: data,
        // baseURL: 'http://192.168.137.13:37039',
        baseURL:api.server_url+api.global_plm_baseURL,
      }).then((res) => {
        console.log('res', res)
        Message({ showClose: true, message: '提交成功!', type: 'success' })
        this.visible = false
        this.loadData()
      })
    },

    getCurrentTime() {
      //获取当前时间并打印
      var _this = this
      let yy = new Date().getFullYear()
      let mm = new Date().getMonth() + 1
      let dd = new Date().getDate()
      let hh = new Date().getHours()
      let mf = new Date().getMinutes() < 10 ? '0' + new Date().getMinutes() : new Date().getMinutes()
      let ss = new Date().getSeconds() < 10 ? '0' + new Date().getSeconds() : new Date().getSeconds()
      let gettime = yy + '/' + mm + '/' + dd + ' ' + hh + ':' + mf + ':' + ss
      let nowtime = {}
      nowtime.time = gettime
      return nowtime
    },

    //逻辑删除
    handledelete(record) {
      let delete_id = record.id
      axios({
        url: `/fileManage/deleteFile?fileId=${delete_id}`,
        method: 'get',
        // baseURL: 'http://192.168.137.13:37039',
        baseURL:api.server_url+api.global_plm_baseURL,
      }).then((res) => {
        if (res.data.success) {
          Message({ showClose: true, message: '删除成功!', type: 'success' })
          this.loadData()
        } else {
          Message({ showClose: true, message: '删除失败!', type: 'warning' })
        }
      })
    },
    //查看历史版本
    gohistory(record) {
      let params = {}
      params.fileName = record.fileName
      axios({
        url: `/fileManage/getHisList`,
        method: 'post',
        data: params,
        // baseURL: 'http://192.168.137.13:37039',
        baseURL:api.server_url+api.global_plm_baseURL,
      }).then((res) => {
        console.log('????', res)
        if (res.data.success) {
          let data = res.data.result
          for (let i = 0; i < data.length; i++) {
            if (data[i].fileLink != '' && data[i].fileLink != undefined) {
              data[i].fileLink = api.server_url + `/plm/common/download` + data[i].fileLink
            }
          }
          this.historydata = data
          this.historyvisible = true
        } else {
          Message({ showClose: true, message: '查看失败!', type: 'warning' })
        }
      })
    },

    //回收站
    goRubish() {
      this.getrubish()
    },
    getrubish() {
      let params = {}
      params.createBy = this.current_userid
      params.fileType = this.file_type
      axios({
        url: `/fileManage/getRecList`,
        method: 'post',
        data: params,
        // baseURL: 'http://192.168.137.13:37039',
        baseURL:api.server_url+api.global_plm_baseURL,
      }).then((res) => {
        console.log('回收站数据', res)
        if (res.data.success) {
          let data = res.data.result
          for (let i = 0; i < data.length; i++) {
            if (data[i].fileLink != '' && data[i].fileLink != undefined) {
              data[i].fileLink = api.server_url + `/plm/common/download` + data[i].fileLink
            }
          }
          this.rubishdata = data
          this.rubishvisible = true
        } else {
          Message({ showClose: true, message: '打开回收站失败', type: 'warning' })
        }
      })
    },
    //回收站还原
    back(record) {
      let fileId = record.id
      axios({
        url: `/fileManage/reductionFile?fileId=${fileId}`,
        method: 'get',
        // baseURL: 'http://192.168.137.13:37039',
        baseURL:api.server_url+api.global_plm_baseURL,
      }).then((res) => {
        if (res.data.success) {
          Message({ showClose: true, message: '还原成功!', type: 'success' })
          this.loadData()
          this.getrubish()
        } else {
          Message({ showClose: true, message: '还原失败!', type: 'warning' })
        }
      })
    },

    //获取文档类型
    getFiletypes() {
      axios({
        url: `/cgform/api/getData/8d96abf0fbe447e2b4f56c49b231b3ec`,
        method: 'get',
        headers: {
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
        },
        baseURL: api.server_url + api.global_online_baseURL,
        // baseURL: 'http://192.168.137.1:8090',
      }).then((res) => {
        let data = res.data.result.records
        // console.log("获取的类型数据",data)
        for (let i = 0; i < data.length; i++) {
          let temp = {
            id: data[i].file_type,
            label: data[i].file_type,
          }
          this.fileTypes.push(temp)
        }
        // console.log("获取的类型数据",selectData)
      })
    },
  },
}
</script>

<style scoped lang='less'>
/deep/.el-dialog__header {
  background: rgb(230, 239, 245);
  padding: 10px;
  .el-dialog__headerbtn {
    top: 10px;
    right: 10px;
    font-size: 20px;
  }
}
/deep/&.notitle {
  .el-dialog__header {
    padding: 0;
  }
}

/deep/.el-dialog--center .el-dialog__body {
  padding: 20px;
}
.submitBtn {
  margin-top: 20px;
}
.submitBtn button {
  margin: 20px auto;
  left: 40%;
}
.abutton {
  margin-right: 20px;
}
</style>
