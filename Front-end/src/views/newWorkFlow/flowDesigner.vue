<template>
  <div>
    <a-card :bordered="false">
      <div id="designerlist">
        <div>
          <a-button-group>
            <a-button type="primary" icon="plus" @click="openEasyflow('0')">新增</a-button>
          </a-button-group>
        </div>
        <div style="padding-top: 20px">
          <a-table :columns="columns" :dataSource="formTableData" rowKey="id">
            <span slot="action" slot-scope="text, record, index">
              <a @click="deploy(record)">部署</a>
              <a-divider type="vertical" />
              <a @click="editFlow(record)">查看</a>
              <a-divider type="vertical" />
              <a @click="deleteFlow(record)">删除</a>
            </span>
          </a-table>
        </div>
      </div>
    </a-card>
    <panel ref="panel" @close="getList"></panel>
  </div>
</template>
<script>
import axios from 'axios'
import qs from 'qs'
import { nw_postAction, nw_postAction1, nw_getAction } from '@api/newWorkApi'
import panel from './easyflowModule/panel.vue'
let dataInitial = {
  name: '流程',
  //event_hander_bean: '',
  start_process_url: '',
  isNotDeploy: true,
  // key: '',
  category_id: '',
  effect_date: '',
  description: '',
  type: 'process',
  nodeList: [],
  lineList: [],
  formAttribute: {
    processMainTable: '',
    processAssociationKey: '',
    processAssociationTable: '',
    mainTableAssociationkey: '',
    processMainKey: '',
  },
}
export default {
  name: 'flowDesigner',
  components: { panel },
  data() {
    return {
      deployinstance: '',
      deployindex: '',
      columns: [
        {
          title: '模板名称',
          align: 'center',
          dataIndex: 'name',
        },
        {
          title: '建立时间',
          align: 'center',
          dataIndex: 'createTime',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      formTableData: [],
    }
  },
  created() {},
  mounted() {
    this.getList()
  },
  methods: {
    openEasyflow(saveFlag) {
      this.$refs.panel.openModel(true, dataInitial, saveFlag)
    },
    //得到所有的流程模板
    getList() {
      var temp = ''
      nw_postAction1(`/design/getDeploy`)
        .then((res) => {
          if (res.success) {
            this.formTableData = res.result
          } else {
            this.$message.error('查询流程列表失败')
          }
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //删除流程模板
    deleteFlow(record) {
      let that = this
      console.log('进入流程删除！')
      this.$elementConfirm('真的要删除吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          nw_getAction(`/design/deleteTempBlob/` + record.id)
            .then((res) => {
              if (res.result) {
                that.$message.success('删除成功')
              } else {
                that.$message.success('删除失败')
              }
              that.getList()
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch(() => {})
    },
    //编辑流程
    editFlow(record) {
      axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
      return nw_postAction1(`/design/modifyFile`, qs.stringify({ id: record.id }))
        .then((res) => {
          console.log(res)
          if (res.success) {
            this.isNotDeploy = true
            // this.$message.warning('该流程已部署，禁止查看！')
          } else {
            this.isNotDeploy = false
          }
          console.log('当前表单是不是没部署', this.isNotDeploy)
          return this.$refs.panel.openModel(this.isNotDeploy, res.result, '1')
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //部署流程模板
    deploy(record) {
      this.$elementConfirm('真的要部署吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          nw_postAction1(`/design/deploy`, { fileName: record.name })
            .then((res) => {
              if (res.result) {
                this.$message.success('部署成功')
              } else {
                this.$message.error('部署失败')
              }
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch(() => {})
    },
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
