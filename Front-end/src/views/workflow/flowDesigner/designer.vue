<template>
  <div>
    <a-card :bordered="false">
      <div id="designerlist">
        <div>
          <a-button-group>
            <a-button type="primary" icon="plus" @click="openAddFlow">新增</a-button>
          </a-button-group>
        </div>
        <div style="padding-top: 20px">
          <a-table :loading="tableLoading" :columns="columns" :dataSource="formTableData"  rowKey="name">
            <!-- :pagination="{pageSizeOptions:['10','20','30'],showSizeChanger:true,showQuickJumper:true,showTotal:(total, range)=>range+' 共'+total+'条'}" -->
            <span slot="action" slot-scope="text, record, index">
              <a-modal
                title="请选择部署为哪一类流程?"
                :visible="popoverVisible"
                :footer="null"
                width="370px"
                :mask="false"
                :centered="true"
                style="position: absolute; z-index: 200"
                @cancel="flowDeployCancel"
              >
                <a-select style="width: 200px" v-model="deployinstance" :defaultActiveFirstOption="true">
                  <a-select-option v-for="item in flowCategory" :key="item.id" :value="item.id">
                    {{ item.category_name }}
                  </a-select-option>
                </a-select>
                <a-button type="primary" @click="deploy()" style="margin-left: 20px">部署</a-button>
              </a-modal>
              <a @click="opendeploy(index)">部署</a>
              <!-- <a-divider type="vertical" />  -->
              <!-- <a @click="seeFlow(index)">查看</a> -->
              <a-divider type="vertical" />
              <a @click="openEditflow(record)">查看</a>
              <a-divider type="vertical" />
              <a @click="deleteFlow(record)">删除</a>
            </span>
          </a-table>
        </div>
      </div>
    </a-card>
    <a-modal
      :visible="addflowShow"
      :footer="null"
      width="100%"
      :zIndex="300"
      :closable="false"
      :destroyOnClose="destroyadd"
    >
      <add-flow @close-addflow="closeAddflow"></add-flow>
    </a-modal>
    <a-modal
      :visible="editflowShow"
      :footer="null"
      width="100%"
      :zIndex="300"
      :closable="false"
      :destroyOnClose="destroy"
    >
      <edit-flow @close-editflow="closeEditflow" v-bind:editName="editName"></edit-flow>
    </a-modal>
  </div>
</template>
<script>
import addFlow from './flowDesigner'
import editFlow from './editflowDesigner'
import axios from 'axios'
import Vue from 'vue'
import { w_postAction, w_postAction1, w_deleteAction } from '@/api/workapi'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
import { o_postAction, o_getAction } from '@/api/onApi.js'

// const token = Vue.ls.get(ACCESS_TOKEN);

export default {
  name: 'designer',
  components: { addFlow, editFlow },
  data() {
    return {
      destroy: false,
      destroyadd: false,
      editName: this.editName,
      tableLoading: false,
      popoverVisible: false,
      deployinstance: '',
      deployindex: '',
      flowCategory: [],
      columns: [
        {
          title: '模板名称',
          align: 'center',
          dataIndex: 'name',
        },
        {
          title: '建立时间',
          align: 'center',
          dataIndex: 'updateDate',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      formTableData: [],
      addflowShow: false,
      editflowShow: false,
    }
  },
  created() {
    this.getAllFlowCategory()
  },
  methods: {
    //得到所有的流程模板
    getList() {
      var temp = ''
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      w_postAction(`/uflo/designer/loadProcessProviderFiles`, {
        name: "默认文件系统",
      })
        .then((res) => {
          let dataTable = res
          var dataTableNew = new Array()
          for (let i = 0; i < dataTable.length; i++) {
            let len = dataTable[i].name.length
            if (dataTable[i].name.substring(len - 9, len) == '.uflo.xml') {
              dataTable[i].name = dataTable[i].name.substring(0, len - 9)
              dataTableNew.push(dataTable[i]);
            }
          }
          console.log(dataTableNew)
          temp = dataTableNew
          this.formTableData = temp
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //得到所有的流程类别
    getAllFlowCategory() {
      let params = { pageNo: 1, pageSize: 9999 }
      o_getAction('/cgreport/api/getColumnsAndData/1346455081826521090',params).then((res) => {
        this.flowCategory = res.result.data.records
      })
    },
    //关闭部署流程model
    flowDeployCancel() {
      this.popoverVisible = false
    },
    //打开部署流程model
    opendeploy(index) {
      this.deployindex = index
      this.popoverVisible = true
    },
    //打开增加页面
    openAddFlow() {
      this.addflowShow = true
    },
    //关闭增加页面
    closeAddflow() {
      this.destroyadd = true
      this.addflowShow = false
      this.getList()
    },
    //打开编辑页面
    openEditflow(record) {
      this.editName = 'file:' + record.name
      this.editflowShow = true
    },
    //关闭编辑页面
    closeEditflow() {
      this.destroy = true
      this.editflowShow = false
      this.getList()
    },
    //删除流程模板
    deleteFlow(record) {
      let that = this
      const fullName = 'file:' + record.name + '.uflo.xml'
      this.$elementConfirm('真的要删除吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
           w_postAction(`/uflo/designer/deleteFile?fileName=` + fullName)
            .then((res) => {
              that.$message.success('删除成功')
              that.getList()
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch(() => {})
    },
    seeFlow() {
    },
    //部署流程模板
    deploy() {
      let that = this
      var index = that.deployindex
      const deployData = new FormData()
      const fullName = 'file:' + that.formTableData[index].name + '.uflo.xml'
      deployData.append('fileName', fullName)
      deployData.append('categoryId', this.deployinstance)
      this.$elementConfirm('真的要部署吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
         w_postAction1(`/process/deploy`, deployData)
            .then((res) => {
              that.$message.success('部署成功')
              that.flowDeployCancel()
              that.getList()
            })
            .catch((err) => {
              that.$message.error('部署失败')
              console.log(err)
            })
        })
        .catch(() => {})
    },
  },
  mounted() {
    this.getList()
  },
}
</script>
<style scoped>
#adddesigner {
  display: flex;
  background-color: #ffffff;
  z-index: 200;
  width: 100%;
  position: fixed;
  top: 18px;
  right: 0;
  left: 0;
  bottom: 40px;
  border: 0;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
.ant-modal-root .ant-modal {
  position: relative;
  top: 0;
  padding: 0;
}
.ant-modal-content .ant-modal-body {
  padding: 14px;
}
</style>