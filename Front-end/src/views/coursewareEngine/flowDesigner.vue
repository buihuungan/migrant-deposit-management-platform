<template>
  <div>
    <a-card :bordered="false">
      <div id="designerlist">
        <div>
          <a-button-group>
            <a-button type="primary" icon="plus" @click="openEasyflow('0')">新增</a-button>
            <!-- <a-button type="primary"  style="margin-left:20px"  @click="open3DModal('0')">查看模型</a-button> -->
          </a-button-group>
        </div>
        <!-- <saveMessage @formName="getFormName"></saveMessage> -->
        <course-detail v-if="tableReset == true"></course-detail>
        <!-- <div style="padding-top: 20px">
          <a-table :columns="columns" :dataSource="formTableData" rowKey="id">
            <span slot="action" slot-scope="text, record, index">
              <a @click="deploy(record)">部署</a>
              <a-divider type="vertical" />
              <a @click="editFlow(record)">查看</a>
              <a-divider type="vertical" />
              <a @click="deleteFlow(record)">删除</a>
            </span>
          </a-table>
        </div> -->
      </div>
    </a-card>
    <model-3d ref="Model3d"></model-3d>
    <panel ref="panel" @close="getTableData"></panel>
  </div>
</template>
<script>
import axios from 'axios'
import qs from 'qs'
import courseDetail from "@/views/courseware/courseDetail.vue";
import { nw_postAction, ce_postAction1, nw_getAction } from '@api/coursewareEngineApi'
import panel from './easyflowModule/panel.vue'
import CourseDetail from '../courseware/courseDetail.vue';
import Model3d from './Model3d.vue'
import { saveMessage } from "@/views/coursewareEngine/easyflowModule/saveMessage.vue";
import Vue from 'vue'
import { USER_ID } from '@/store/mutation-types'
import { ce_getWares } from '@api/coursewareEngineApi'
let dataInitial = {
  name: '流程',
  //event_hander_bean: '',
  start_process_url: '',
  // key: '',
  category_id: '',
  effect_date: '',
  description: '',
  type: 'process',
  user_id: Vue.ls.get(USER_ID),
  nodeList: [],
  lineList: [],
}
export default {
  name: 'flowDesigner',
  components: { panel ,courseDetail, CourseDetail,Model3d , saveMessage},
  data() {
    return {
      tableReset: true,
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
  created() {
    
  },
  mounted() {
    // this.getWare()
    
    //this.getFormName()
  },
  methods: {
    openEasyflow(saveFlag) {
      this.$refs.panel.openModel(dataInitial,saveFlag)
    },
    // getFormName(formName){
    //      dataInitial.name = formName
    //      console.log("formName",formName)
    // },
    //保存画板新增数据后刷新表格内容
    getTableData() {
      this.tableReset = false
      this.$nextTick(() => {
        this.tableReset = true
      })
    },
    // getWare(){
    //    var url = '';
    //    if(this.tableType == 'type'){ 
    //      url = `/business/getWares/type`
    //    }else if(this.tableType == 'domain'){
    //      url = `/business/getWares/domain`
    //    }
    //    ce_getWares(url).then((res) => {
    //     if (res.result) {
    //       this.wareId = res.result.wareId;
    //       console.log("12321",this.wareId)
    //       var courseDetail = res.result;
    //       for ( var i =0 ; i<courseDetail.length ; i++){
    //         var domainName = "";
    //         var typeName = "";
    //         for (var j =0 ;j< courseDetail[i].domains.length ; j++){
    //           domainName += courseDetail[i].domains[j] + ',';
    //         }
    //         for ( var j =0 ;j<courseDetail[i].types.length; j++){
    //           typeName += courseDetail[i].types[j] + ',';
    //         }
    //         this.dataSource.push({
    //           'name':courseDetail[i].fileName,
    //           'teacher_name': courseDetail[i].wareTeacher,
    //           'ware_content': JSON.stringify(courseDetail[i].ware),
    //           'ware_domain_name' : domainName.substring(0,domainName.length-1),
    //           'ware_name':courseDetail[i].wareName,
    //           'ware_type_name':typeName.substring(0,typeName.length-1),
    //           'ware_id':courseDetail[i].wareId,
    //         });
            
    //       }
    //       this.ipagination.total = this.dataSource.length
    //   }
    // })
    // },
    //得到所有的流程模板
    getList() {
      var temp = ''
      ce_postAction1(`/design/getDeploy`)
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
    //显示3D模型
    open3DModal(record) {
      this.$refs.Model3d.openModel(record);
    },
    //删除流程模板
    deleteFlow(record) {
      let that = this
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
    // editFlow(record) {
    //   axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
    //   ce_postAction1(`/design/modifyFile`, qs.stringify({ id: record.id }))
    //     .then((res) => {
    //       console.log(res)
    //       this.$refs.panel.openModel(res.result,'1')
    //     })
    //     .catch((err) => {
    //       console.log(err)
    //     })
    // },
    //部署流程模板
    deploy(record) {
      this.$elementConfirm('真的要部署吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          ce_postAction1(`/design/deploy`, { fileName: record.name })
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
