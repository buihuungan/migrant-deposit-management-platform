<template>
<div>
  <a-card :bordered="false">
    <!-- 操作按钮区域 -->
    <div class="table-operator" :md="24" :sm="24" style="margin-bottom: 15px; font-weight: 600">
      <a-divider type="vertical" />
      <label style="margin-top: 16px">案件信息列表:</label>
    </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"    
      >
        <span slot="action" slot-scope="text, record, index" >
          <a @click="courseDetails(record)" style="margin-right:5px">案件描述</a>
          <a-upload
            name="file"
            :action="uploadUrl"
            :data="paramData"
            :multiple="false"
            @change="handleChange"
            :showUploadList="false"
          >
            <!-- <a @click="uploadFile(record)" style="margin-right:5px">操作手册</a> -->
          </a-upload>
          <a @click="handleDetail(record)" style="margin-right:5px">详情</a>
          <a @click="deploy(record)" style="margin-right:5px">部署</a>
          <a @click="editFlow(record)" style="margin-right:5px">编辑</a>
          <a @click="deleteFlow(record)" style="margin-right:5px">删除</a>
          <!-- <a-dropdown>
            <a >
              数据预置
              <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item><a @click="saleDataPreset(record)">销售订单</a></a-menu-item>
              <a-menu-item><a @click="saleDataDetail(record)">销售订单详情</a></a-menu-item>
              <a-menu-item><a @click="productDataPreset(record)">排产订单</a></a-menu-item>
              <a-menu-item><a @click="productDataDetail(record)">排产订单详情</a></a-menu-item>
            </a-menu>
          </a-dropdown> -->
          <!-- <a-divider type="vertical"/> -->
          <!-- <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleEdit(record)">编辑</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown> -->
        </span>
      </a-table>
    </div>
  </a-card>
  <course-content ref="courseContent"></course-content>
    <panel ref="panel" @close="closePanel()"></panel>
  <Anntask ref="anntask" ></Anntask>
  <salesModal ref="salesModal" :flowId="flowId"></salesModal>
  <scheduleOrder ref="scheduleOrderModal"></scheduleOrder>
</div>
</template>

<script>
import axios from 'axios'
import qs from 'qs'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { ce_getWares, ce_getAction } from '@api/coursewareEngineApi'
import { getAction, postAction, deleteAction } from '@/api/manage'
import {ce_postAction1} from '@api/coursewareEngineApi'
import panel from '@views/coursewareEngine/easyflowModule/panel.vue'
import  courseContent  from "@views/coursewareEngine/easyflowModule/courseContent.vue"
import api from '@/api/index'
import Anntask from '../../workflow/flowConfig/annTask.vue'
import { w_postAction} from '../../../api/workapi'
import { order_getAction, order_postAction } from '@/api/orderApi'
import salesModal from '../../salesManagement/modules/salesModal.vue'
import scheduleOrder from './scheduleOrder.vue'

export default {
  name: 'CourseListTable',
  mixins: [JeecgListMixin],
  wareId: '',
  components: {
    panel ,courseContent , Anntask, salesModal, scheduleOrder},
  props: {
    type: {
      type: String,
      default: 'type',
    },
    canAdd: {
      type: Boolean,
      default: false,
    },
    managementType: {
      type: String,
      default: '',
    },
    departIdList: {
      type: Array,
      default: function () {
        return []
      },
    },
    tableType: String,
  },
  data() {
    return {
      flowId: '919e12ea043f412eb25ae8ef75b820b2',
      paramData: {
        lessonId: ''
      },
      uploadUrl: api.server_url + '/coursewareengine/business/uploadLessonFile',  //上传文件路径
      pageSize: 5,
      currentDeptId: '',
      sys_org_code: '',
      subject_code: '',
      subject_name: '',
      leftrecords: {},
      columns: [
        {
          title: '案件名称',
          align: 'center',
          dataIndex: 'ware_name',
          width: "10%"
        },
        {
          title: '设计案件人员',
          align: 'center',
          dataIndex: 'teacher_name',
          width: "10%"
        },
        {
          title: '案件类型',
          align: 'center',
          dataIndex: 'ware_type_name',
          width: "20%"
        },
        {
          title: '案件领域',
          align: 'center',
          dataIndex: 'ware_domain_name',
          width: "20%"
        },
        {
          title: '案件行业',
          align: 'center',
          dataIndex: 'ware_industry_name',
          width: "20%"
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: "20%"
          // width:220,
        },
      ],

      typeUrl: {
        relation: '/online/cgform/api/getData/ed413176867946fbb8e7ffeab9d13817',
        searchlist: '/online/cgform/api/getData/22f5f24e82444c3db7228f2c2412654d',
        searchdomain: '/online/cgform/api/getData/0c0982cecce847099a9241c8f58b7142',
      },
      domainUrl: {
        relation: '/online/cgform/api/getData/0c0982cecce847099a9241c8f58b7142',
        searchlist: '/online/cgform/api/getData/22f5f24e82444c3db7228f2c2412654d',
        searchtype: '/online/cgform/api/getData/ed413176867946fbb8e7ffeab9d13817',
      },
      wareUrl: {
        searchUrl: '/online/cgreport/api/getColumnsAndData/1364087518393339906',
      },
      detailId: '', //当前选中的表中记录对象，用于展示详情
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['5', '10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        onChange: this.onPageChange.bind(this),
        total: 0,
      },
    }
  },
  // watch: {
  //   departIdList:function(value1,value2){
  //     this.loadData(1)
  //   }
  // },
  mounted(){
    // this.getWare()
  },
  created() {
    //this.getAllData()
    this.getWare();
  },
  methods: {
    //销售订单详情
    saleDataDetail(record) {
      order_postAction(`/preset/selectInitData/${record.ware_id}`)
      .then(res => {
        if(res.result) {
          this.$refs.salesModal.getForm(res.result.cictOrder, 'detail')
        }
        else {
          this.$message.warning('未预置数据，请先预置数据！')
        }
      })
    },
    //排产订单详情
    productDataDetail(record) {
      this.$refs.scheduleOrderModal.openScheduleOrderDetail(record)
    },
    //销售订单数据预置
    saleDataPreset(record) {
      order_postAction(`/preset/selectInitData/${record.ware_id}`)
      .then(res => {
        if(res.result) {
          var orderId = res.result.cictOrder.id
          w_postAction(`/process/startProcess?processId=4822`)
          .then((res) => {
            this.$message.info('配置流程成功')
            var formDesignerId = res.result.formDesignerId //表单ID
            var taskId = res.result.firstTaskId //表单ID
            this.$refs.anntask.openModal(formDesignerId, taskId , record.ware_id, orderId)   //   0  代表普通的创建销售订单流程，  1  代表老师给课件预置销售订单数据
          })
          .catch((error) => {
            console.log(error)
          })
        }
        else {
          w_postAction(`/process/startProcess?processId=4822`)
          .then((res) => {
            this.$message.info('配置流程成功')
            var formDesignerId = res.result.formDesignerId //表单ID
            var taskId = res.result.firstTaskId //表单ID
            this.$refs.anntask.openModal(formDesignerId, taskId , record.ware_id)   //   0  代表普通的创建销售订单流程，  1  代表老师给课件预置销售订单数据
          })
          .catch((error) => {
            console.log(error)
          })
        }
      })
    },
    productDataPreset(record) {
      this.$refs.scheduleOrderModal.openScheduleOrderModal(record)
    },
    handleChange(info) {
      if(info.file.status === 'done') {
        if(info.file.response.code == '201') {
          this.$message.success('重新上传文件成功！')
        }
        else if(info.file.response.code == '200') {
          this.$message.success('上传文件成功！')
        }
      }
    },
    uploadFile(record) {
      console.log('record',record)
      this.paramData.lessonId = record.ware_id
      console.log('this.paramData.lessonId',this.paramData.lessonId)
    },
    // searchReset() {
    //   this.queryParam = {}
    //   this.loadData(1)
    // },
    // returnSuperParams(key,value){
    //   let params = {
    //     pageSize:5,
    //     pageNo:this.ipagination.current
    //   }
    //   params.superQueryMatchType = 'or'
    //   if(value && typeof(value) != 'object'){
    //     params.superQueryParams = escape(JSON.stringify([{
    //       rules: "eq",
    //       type: "text",
    //       val: value,
    //       field: key
    //     }]))
    //   }else if(value.length && typeof(value) == 'object'){
    //     let superQueryParams = []
    //     value.forEach( item => {
    //       superQueryParams.push({
    //         rules: "eq",
    //         type: "text",
    //         val: item,
    //         field: key
    //       })
    //     })
    //      params.superQueryParams = escape(JSON.stringify(superQueryParams))
    //   }
    //   return params
    // },
    openEasyflow(saveFlag) {
      this.$refs.panel.openModel(dataInitial,saveFlag)
    },
     getWare(){
       var url = '';
       if(this.tableType == 'type'){ 
         url = `/business/getWares/type`
       }else if(this.tableType == 'domain'){
         url = `/business/getWares/domain`
       }
       ce_getWares(url).then((res) => {
        if (res.result) {
          this.wareId = res.result.wareId;
          console.log("12321",this.wareId)
          var courseDetail = res.result;
          this.dataSource = []
          for ( var i =0 ; i<courseDetail.length ; i++){
            var domainName = "";
            var typeName = "";
            var industryName = "";
            for (var j =0 ;j< courseDetail[i].domains.length ; j++){
              domainName += courseDetail[i].domains[j] + ',';
            }
            for ( var j =0 ;j<courseDetail[i].types.length; j++){
              typeName += courseDetail[i].types[j] + ',';
            }
            for ( var j=0;j<courseDetail[i].types.length; j++){
              industryName += courseDetail[i].industries[j] + ',';
            }
            this.dataSource.push({
              'name':courseDetail[i].fileName,
              'teacher_name': courseDetail[i].wareTeacher,
              'ware_content': JSON.stringify(courseDetail[i].ware),
              'ware_domain_name' : domainName.substring(0,domainName.length-1),
              'ware_industry_name' : industryName.substring(0,industryName.length-1),
              'ware_name':courseDetail[i].wareName,
              'ware_type_name':typeName.substring(0,typeName.length-1),
              'ware_id':courseDetail[i].wareId,
            });
            
          }
          this.ipagination.total = this.dataSource.length
      }
    })
    },
    open(records) {
      // this.leftrecords = records
      this.loadData(1, records)
    },
    // 查看课程详情
    courseDetails(record) {
      console.log("qs", record.id)
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      ce_getAction(`business/getDescById/${record.ware_id}`, qs.stringify({ wareId: record.ware_id}))
        .then((res) => {
          console.log("111",res)
          this.$refs.courseContent.openCourse(res.result,true,record.ware_id)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    closePanel(){
          this.dataSource = [];
              this.getWare();
    },
    //编辑流程
    editFlow(record) {
      console.log("qs", record.id)
      axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
      ce_postAction1(`/design/modifyFile`, qs.stringify({ id: record.ware_id}))
        .then((res) => {

          if(res.success){
            console.log("成功",res.success)
            this.$refs.panel.openModel(res.result,'1')
          }else{
            this.$message.error(res.message)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    deleteFlow(record) {
      let that = this
      console.log("record.ware_id",record.ware_id)
      this.$elementConfirm('真的要删除吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          ce_getAction(`/design/deleteTempBlob/` + record.ware_id)
            .then((res) => {
              if (res.result) {
                that.$message.success('删除成功')
              } else {
                that.$message.success('删除失败')
              }
              this.dataSource = [];
              this.getWare();
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch(() => {})
    },
    getAllData() {
      axios({
        //url: 'http://192.168.137.14:8012/eduWareWareDomain/getAllWareDetail',
        url: api.server_url+'/edu/eduWareWareDomain/getAllWareDetail',
        methods: 'GET'
      }).then(res => {
        this.dataSource = res.data.result
        this.dataSource = this.dataSource.map(x => {
          if (x.wareDomainName.indexOf('[') > -1) {
            x.wareDomainName = x.wareDomainName.split('[')[1].split(']')[0]
            x.wareTypeName = x.wareTypeName.split('[')[1].split(']')[0]
          }
          return new Object({
            teacher_id: x.teacherId,
            teacher_name: x.teacherName,
            teacher_work_no: x.teacherWorkNo,
            ware_content: x.wareContent,
            ware_domain_name: x.wareDomainName,
            ware_name: x.wareName,
            ware_type_name: x.wareTypeName
          })
        })
      })
    },
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
                console.log("222",record )
                this.$message.success('部署成功')
              } else {
                this.$message.error(res.message)
                // this.dataSource = [];
                // this.getWare();
              }
            })
            .catch((err) => {
              console.log(err)
            })
        })
        .catch(() => {})
    },
    loadData(arg, record) {
      // let that = this
      // //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      this.dataSource = [];
      let params = { order: 'desc', pageNo: this.ipagination.current, pageSize: 10 };
      let url = `/business/getWares/`;
      if (record) {
        if (record.ware_type_name){
          params.ware_type_name = record.ware_type_name;
          url +=`${params.ware_type_name}`
        }else {
          params.ware_domain_name = record.ware_domain_name;
          url +=`${params.ware_domain_name}`
        }
        ce_getWares(url).then((res) => {
        if (res.result) {
          var courseDetail = res.result;
          for ( var i =0 ; i<courseDetail.length ; i++){
            var domainName = "";
            var typeName = "";
            for (var j =0 ;j< courseDetail[i].domains.length ; j++){
              domainName += courseDetail[i].domains[j] + ',';
            }
            for ( var j =0 ;j<courseDetail[i].types.length; j++){
              typeName += courseDetail[i].types[j] + ',';
            }
            this.dataSource.push({
              'name':courseDetail[i].fileName,
              'teacher_name': courseDetail[i].wareTeacher,
              'ware_content': JSON.stringify(courseDetail[i].ware),
              'ware_domain_name' : domainName.substring(0,domainName.length-1),
              'ware_name':courseDetail[i].wareName,
              'ware_type_name':typeName.substring(0,typeName.length-1),
              'ware_id':courseDetail[i].wareId,
            });
            
          }
        }
        })
        }
        // if (record.ware_type_name) {
        //   params.ware_type_name = record.ware_type_name
        //   axios({
        //     url: `https://ii2.wust.edu.cn/edu/eduWareWareType/getWareDetail/${params.ware_type_name}`,
        //     methods: 'GET',
        //   }).then(res => {
        //     this.dataSource = res.data.result
        //     this.dataSource = this.dataSource.map(x => {
        //       if (x.wareDomainName.indexOf('[') > -1) {
        //         x.wareDomainName = x.wareDomainName.split('[')[1].split(']')[0]
        //       }
        //       return new Object({
        //         teacher_id: x.teacherId,
        //         teacher_name: x.teacherName,
        //         teacher_work_no: x.teacherWorkNo,
        //         ware_content: x.wareContent,
        //         ware_domain_name: x.wareDomainName,
        //         ware_name: x.wareName,
        //         ware_type_name: params.ware_type_name
        //       })
        //     })
        //   })
        // } else {
        //   params.ware_domain_name = record.ware_domain_name
        //   axios({
        //     url: `https://ii2.wust.edu.cn/edu/eduWareWareDomain/getWareDetail/${params.ware_domain_name}`,
        //     methods: 'GET',
        //   }).then(res => {
        //     this.dataSource = res.data.result
        //     this.dataSource = this.dataSource.map(x => {
        //       if (x.wareDomainName.indexOf('[') > -1) {
        //         x.wareTypeName = x.wareTypeName.split('[')[1].split(']')[0]
        //       }
        //       return new Object({
        //         teacher_id: x.teacherId,
        //         teacher_name: x.teacherName,
        //         teacher_work_no: x.teacherWorkNo,
        //         ware_content: x.wareContent,
        //         ware_domain_name: params.ware_domain_name,
        //         ware_name: x.wareName,
        //         ware_type_name: x.wareTypeName
        //       })
        //     })
        //   })

      // getAction(this.wareUrl.searchUrl, params).then((res) => {
      //   this.dataSource = res.result.data.records
      //   this.ipagination.total = res.result.data.total
      // })

    },
    handleDetail(records) {
      this.$emit('changedetail', records)
    },
    courseDetail(records){
      
    },
    handleDelete: function (record) { },
    clearList() {
      // this.currentDeptId = '';
      this.dataSource = []
    },
    handleEdit: function (record) {
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.disableSubmit = false
      const { fix } = urls[managementType]
      // this.$refs.modalForm.edit(record ,managementType , fix, true);
    },
    handleAdd: function () {
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.userDepartModel.departIdList = [this.currentDeptId] //传入一个部门id
      const { fix } = urls[managementType]
      // this.$refs.modalForm.add(this.record, managementType , fix);
      this.$refs.modalForm.title = '新增'
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.ipagination.pageSize = pageSize
    },
  },
}
</script>
<style scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 3px;
}

.ant-card {
  margin-left: -30px;
  margin-right: -30px;
}

.table-page-search-wrapper {
  margin-top: -16px;
  margin-bottom: 16px;
}
.course_detail {
  width: 50%;
}
</style>