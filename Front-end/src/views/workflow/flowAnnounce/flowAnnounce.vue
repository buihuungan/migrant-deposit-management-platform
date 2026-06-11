<template>
  <div>
    <div id="formContent">
      <div id="taskList">
        <a-tabs :tabBarStyle="{ textAlign: 'center' }" @change="changeTab1()" v-model="taskTab.tabKey">
          <a-tab-pane key="1" tab="未认领">
            <div class="card-table">
              <a-card :bordered="false">
                <div class="flowAnnounce">
                  <a-table bordered :columns="loadClaimcolumns" :dataSource="loadClaimData" rowKey="id">
                    <span slot="loadClaimaction" slot-scope="text, record, index">
                      <a @click="claimTask(record)">认领</a>
                      <a-divider type="vertical" />
                      <a @click="seeflow(record)">查看</a>
                      <a-divider type="vertical" />
                      <a @click="seeInformation(record)">详情</a>
                    </span>
                  </a-table>
                </div>
              </a-card>
            </div>
          </a-tab-pane>
          <a-tab-pane key="2" tab="待处理">
            <div class="card-table">
              <a-card :bordered="false">
                <div class="flowAnnounce">
                  <a-table bordered :columns="flowWillAnnouncecolumns" :dataSource="flowWillAnnounceData" rowKey="id">
                    <span slot="flowWillAnnounceaction" slot-scope="text, record, index">
                      <a @click="announceTask(record)">处理该任务</a>
                      <a-divider type="vertical" />
                      <a @click="seeflow(record)">查看</a>
                      <a-divider type="vertical" />
                      <a @click="seeInformation(record)">详情</a>
                    </span>
                  </a-table>
                </div>
              </a-card>
            </div>
          </a-tab-pane>
          <a-tab-pane key="3" tab="已完成">
            <div class="card-table">
              <div class="finishSearchList">
                <a class="selectText">选择流程:</a>
                <a-select
                  v-model="instance"
                  style="width: 200px; margin-left: 10px; margin-top: 10px"
                  @change="changeSelsct()"
                  :defaultActiveFirstOption="true"
                >
                  <a-select-option v-for="item in processInstance" :key="item.id" :value="item.id">
                    {{ item.name }}
                  </a-select-option>
                </a-select>
                <!-- <a-select v-model="city" style="width: 200px; margin-left: 30px" placeholder="请选择第二个">
                <a-select-option v-for="city in cities" :key="city">
                  {{ city }}
                </a-select-option>
              </a-select>
              <a-button type="primary" @click="getPassFlowAnnounce('1')" style="margin-left: 30px">查询 </a-button> -->
              </div>
              <a-card :bordered="false">
                <div class="">
                  <a-table bordered :columns="flowFinishcolumns" :dataSource="flowFinishData" rowKey="id">
                    <span slot="flowFinishaction" slot-scope="text, record, index">
                      <a @click="seeInformation(record)">详情</a>
                      <!-- <a-divider type="vertical" />
                      <a @click="seeflow(record)">查看</a> -->
                    </span>
                  </a-table>
                </div>
              </a-card>
            </div>
          </a-tab-pane>
          <a-tab-pane key="4" tab="已拒绝">
            <div class="card-table">
              <div class="rejectSearchList">
                <a class="selectText">选择流程:</a>
                <a-select
                  v-model="instance"
                  style="width: 200px; margin-left: 10px; margin-top: 10px"
                  @change="changeSelsct()"
                  :defaultActiveFirstOption="true"
                >
                  <a-select-option v-for="item in processInstance" :key="item.id" :value="item.id">
                    {{ item.name }}
                  </a-select-option>
                </a-select>
                <!-- <a-select v-model="city" style="width: 200px; margin-left: 30px" placeholder="请选择第二个">
                <a-select-option v-for="city in cities" :key="city">
                  {{ city }}
                </a-select-option>
              </a-select>
              <a-button type="primary" @click="getPassFlowAnnounce('0')" style="margin-left: 30px">查询 </a-button> -->
              </div>
              <a-card :bordered="false">
                <div class="">
                  <a-table bordered :columns="flowRejectcolumns" :dataSource="flowRejectData" rowKey="id">
                    <span slot="flowRejectaction" slot-scope="text, record, index">
                      <a @click="seeInformation(record)">详情</a>
                      <!-- <a-divider type="vertical" />
                      <a @click="seeflow(record)">查看</a> -->
                    </span>
                  </a-table>
                </div>
              </a-card>
            </div>
          </a-tab-pane>
          <a-tab-pane key="5" tab="已退回">
            <div class="card-table">
              <a-card :bordered="false">
                <div class="">
                  <a-table bordered :columns="flowRollbackcolumns" :dataSource="flowRollbackData" rowKey="id">
                    <span slot="flowRollbackaction" slot-scope="text, record, index">
                      <a @click="announceRollTask(record)">处理该任务</a>
                      <a-divider type="vertical" />
                      <a @click="seeflow(record)">查看</a>
                      <a-divider type="vertical" />
                      <a @click="seeInformation(record)">详情</a>
                    </span>
                  </a-table>
                </div>
              </a-card>
            </div>
          </a-tab-pane>
        </a-tabs>
      </div>
    </div>

    <!-- 学生请假 -->
    <!-- <a-modal
      :visible="annTask"
      :footer="null"
      width="100%"
      :zIndex="100"
      :closable="false"
      :destroyOnClose="destroy"
      dialogClass="dialogTest"
    >
      <ann-task
        @close-annTask="closeAnnTask"
        v-bind:preflowId="preflowId"
        v-bind:flowName="flowName"
        v-bind:templateId="templateId"
      ></ann-task>
    </a-modal>-->
    <!-- 教师审批 -->
    <div>
      <a-modal
        :visible="approveTask"
        :centered="true"
        :footer="null"
        width="95%"
        :zIndex="100"
        :dialog-style="{ top: '20px' }"
        :closable="false"
        :destroyOnClose="destroyTask"
        dialogClass="dialogTest"
      >
        <div class="buttonstyle"><a-button class="xbutton" type="dashed" @click="close()">X</a-button></div>
        <div class="ididididi" v-for="(item, index) in formList" :key="index">
          <approve-task
            @close-approveTask="closeApproveTask"
            :preflowId="item.prevForm_designer_id"
            :pretableId="item.prevOnline_table_id"
            :predataId="item.prevOnline_data_id"
            :taskId="taskId"
            :flowName="flowName"
          ></approve-task>
        </div>
        <div style="margin-top: 20px">
          <approve-new-task
            @close-approveTask="closeApproveTask"
            :nowflowId="nowflowId"
            :taskId="taskId"
            :recordLastForm="recordLastForm"
            :record="record"
          >
          </approve-new-task>
        </div>
      </a-modal>
    </div>
    <div>
      <a-modal
        :visible="approveRollTask"
        :centered="true"
        :footer="null"
        width="95%"
        :zIndex="100"
        :dialog-style="{ top: '20px' }"
        :closable="false"
        :destroyOnClose="destroyRollTask"
        dialogClass="dialogTest"
      >
        <div class="buttonstyle"><a-button class="xbutton" type="dashed" @click="close()">X</a-button></div>
        <div class="ididididi" v-for="(item, index) in rollFormList" :key="index">
          <approve-task
            @close-approveRollTask="closeRollApproveTask"
            :preflowId="item.prevForm_designer_id"
            :pretableId="item.prevOnline_table_id"
            :predataId="item.prevOnline_data_id"
            :taskId="taskId"
            :flowName="flowName"
          ></approve-task>
        </div>
        <div style="margin-top: 20px" v-show="rollbackForm">
          <rollback-task
            @close-approveRollTask="closeRollApproveTask"
            :preflowId="rollbackForm.prevForm_designer_id"
            :pretableId="rollbackForm.prevOnline_table_id"
            :predataId="rollbackForm.prevOnline_data_id"
            :taskId="taskId"
            :flowName="flowName"
          ></rollback-task>
        </div>
        <div style="margin-top: 20px">
          <div style="margin-top: 20px" v-show="lastFrom">
            <approve-task
              @close-approveRollTask="closeRollApproveTask"
              :preflowId="lastFrom.prevForm_designer_id"
              :pretableId="lastFrom.prevOnline_table_id"
              :predataId="lastFrom.prevOnline_data_id"
              :taskId="taskId"
              :flowName="flowName"
            ></approve-task>
          </div>
        </div>
      </a-modal>
    </div>
    <div class="seeFlow">
      <transition name="fade">
        <div id="container1" v-show="isOk" class="addQA1">
          <div class="showHead">
            <label class="showHeadContent">{{ taskName }}的流程图</label>
            <a-icon type="close" class="closeButton" @click="handleClose" />
          </div>
          <div id="container" style="overflow: auto"></div>
        </div>
      </transition>
      <transition name="fade">
        <div class="overflow" v-show="overflow"></div>
      </transition>
    </div>

    <a-modal
      :visible="seeInformationFlag"
      :footer="null"
      width="95%"
      :zIndex="100"
      :dialog-style="{ top: '20px' }"
      :closable="false"
      :centered="true"
      :destroyOnClose="destroyInformationFlag"
    >
      <div class="seeInformation">
        <div class="buttonstyle"><a-button class="xbutton" type="dashed" @click="close()">X</a-button></div>
        <a-timeline class="seeContent">
          <div v-for="(item, index) in formInformList" :key="index">
            <a-timeline-item>
              <a style="margin-right: 20px">任务名称：{{ item.taskName }}</a>
              <a>创建日期：{{ item.createDateStr }}</a>
              <approve-task
                @close-approveTask="closeApproveTask"
                v-bind:preflowId="item.formDesignerId"
                v-bind:pretableId="item.onlineTableId"
                v-bind:predataId="item.onlineDataId"
              ></approve-task>
            </a-timeline-item>
          </div>
        </a-timeline>
      </div>
    </a-modal>
  </div>
</template>
<script>
import CusDialog from '../../../components/FormMaking/components/CusDialog'
import Vue from 'vue'
import axios from 'axios'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
import ApproveTask from './approveTask'
import ApproveNewTask from './approveNewTask'
import RollbackTask from './rollbackTask'
import { w_postAction, w_postAction1, w_getAction } from '../../../api/workapi'
import ProcessDiagram from './js/ProcessDiagram.js'
import api from '../../../api/index'
export default {
  name: 'flowConfig',
  components: { ApproveTask, ApproveNewTask, RollbackTask, CusDialog },
  data() {
    return {
      record: {},
      recordLastForm: {},
      func1: {},
      rollbackForm: {},
      lastFrom: {},
      seeInformationFlag: false,
      destroyInformationFlag: false,
      rolldesigner: '',
      rolltable: '',
      rolldata: '',
      isTable: -1,
      destroyTask: false,
      destroyNewTask: false,
      taskId: '',
      preflowId: '',
      nowflowId: '',
      pretableId: '',
      predataId: '',
      flowName: '',
      templateId: '',
      instance: '',
      annTask: false,
      approveTask: false,
      approveRollTask: false,
      dialogVisibleFinish: false,
      dialogVisibleReject: false,
      formList: [],
      rollFormList: [],
      formInformList: [],
      rollForm: {},
      processInstance: [{ id: '0000', name: '所有流程' }],
      loadClaimData: [],
      flowWillAnnounceData: [],
      flowRejectData: [],
      flowRollbackData: [],
      flowFinishData: [],
      isOk: false, //查看进入的界面
      overflow: false,
      destroyRollTask: false,
      loadClaimcolumns: [
        {
          title: '状态',
          align: 'center',
          dataIndex: 'state',
        },
        {
          title: '任务名称',
          align: 'center',
          dataIndex: 'taskName',
        },
        {
          title: '流程名称',
          align: 'center',
          dataIndex: 'processName',
        },
        {
          title: '流程唯一标识',
          align: 'center',
          dataIndex: 'processUniqueName',
        },
        {
          title: '类型',
          align: 'center',
          dataIndex: 'type',
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createDate',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'loadClaimaction',
          scopedSlots: { customRender: 'loadClaimaction' },
        },
      ],
      flowWillAnnouncecolumns: [
        {
          title: '状态',
          align: 'center',
          dataIndex: 'state',
        },
        {
          title: '任务名称',
          align: 'center',
          dataIndex: 'taskName',
        },
        {
          title: '流程名称',
          align: 'center',
          dataIndex: 'processName',
        },
        {
          title: '流程唯一标识',
          align: 'center',
          dataIndex: 'processUniqueName',
        },
        {
          title: '类型',
          align: 'center',
          dataIndex: 'type',
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createDate',
        },
        {
          title: '操作',
          align: 'center',
          width: '20%',
          dataIndex: 'flowWillAnnounceaction',
          scopedSlots: { customRender: 'flowWillAnnounceaction' },
        },
      ],
      flowFinishcolumns: [
        {
          title: '状态',
          align: 'center',
          dataIndex: 'state',
        },
        {
          title: '流程名称',
          align: 'center',
          dataIndex: 'processName',
        },
        {
          title: '流程唯一标识',
          align: 'center',
          dataIndex: 'processUniqueName',
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'createDate',
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endDate',
        },
        {
          title: '详情',
          align: 'center',
          dataIndex: 'flowFinishaction',
          scopedSlots: { customRender: 'flowFinishaction' },
        },
      ],
      flowRejectcolumns: [
        {
          title: '状态',
          align: 'center',
          dataIndex: 'state',
        },
        {
          title: '流程名称',
          align: 'center',
          dataIndex: 'processName',
        },
        {
          title: '流程唯一标识',
          align: 'center',
          dataIndex: 'processUniqueName',
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'createDate',
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endDate',
        },
        {
          title: '详情',
          align: 'center',
          dataIndex: 'flowRejectaction',
          scopedSlots: { customRender: 'flowRejectaction' },
        },
      ],
      flowRollbackcolumns: [
        {
          title: '状态',
          align: 'center',
          dataIndex: 'state',
        },
        {
          title: '任务名称',
          align: 'center',
          dataIndex: 'taskName',
        },
        {
          title: '流程名称',
          align: 'center',
          dataIndex: 'processName',
        },
        {
          title: '流程唯一标识',
          align: 'center',
          dataIndex: 'processUniqueName',
        },
        {
          title: '类型',
          align: 'center',
          dataIndex: 'type',
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createDate',
        },
        {
          title: '操作',
          align: 'center',
          width: '20%',
          dataIndex: 'flowRollbackaction',
          scopedSlots: { customRender: 'flowRollbackaction' },
        },
      ],
      provinceData: [],
      cities: [],
      province: '',
      city: [],
      taskName: '',
      taskTab: {
        tabKey: '1',
      },
      process: {},
    }
  },
  mounted() {
    this.getOneList()
    this.getData()
    this.process = ProcessDiagram
  },
  created() {
    this.getprocessInstance()
  },
  methods: {
    seeInformation(record) {
      var processInstanceId = record.processInstanceId
      w_postAction(`/process/loadDetailsOfProcess?processInstanceId=` + processInstanceId)
        .then((res) => {
          console.log('----------', res.result)
          this.formInformList = res.result
          this.seeInformationFlag = true
        })
        .catch((error) => {
          // console.log(error)
        })
    },
    seeflow(record) {
      //查看流程图
      var container = document.getElementById('container')
      var _this = this
      _this.isOk = true
      _this.overflow = true
      var taskId = record.id
      var taskName = record.taskName
      _this.taskName = taskName
      var processId = record.processId
      var processInstanceId = record.processInstanceId
      var processKey = record.processKey

      w_postAction(
        `/uflo/diagram/loadDiagramData?processId=` +
        processId +
        '&processInstanceId=' +
        processInstanceId +
        '&taskId=' +
        taskId +
        '&processKey=' +
        processKey
      )
        .then((res) => {
          _this.process._buildDiagram(res, container)
        })
        .catch((error) => {
          // console.log(error)
        })
    },
    handleClose(done) {
      this.isOk = false
      this.overflow = false
      document.getElementById('container').innerHTML = ''
    },
    changeTab1() {
      this.instance = ''
      this.getData()
        ; (this.processInstance = [{ id: '0000', name: '所有流程' }]), this.getprocessInstance()
    },
    // //切换标签页，切换未认领，已认领，已完成，已拒绝，已退回
    // handleClick: function () {
    //   if (this.taskTab.tabKey == '1') {
    //     console.log(this.taskTab.tabKey)
    //   } else if (this.taskTab.tabKey == '2') {
    //     console.log(this.taskTab.tabKey)
    //   } else if (this.taskTab.tabKey == '3') {
    //     console.log(this.taskTab.tabKey)
    //   } else if (this.taskTab.tabKey == '4') {
    //     console.log(this.taskTab.tabKey)
    //   } else if (this.taskTab.tabKey == '5') {
    //     console.log(this.taskTab.tabKey)
    //   }
    //   this.getData()
    // },
    // 更新表格数据
    getData() {
      this.flowWillAnnounceData = []
      this.flowFinishData = []
      this.flowRejectData = []
      this.flowRollbackData = []
      this.loadClaimData = []
      var tabKey = this.taskTab.tabKey
      switch (tabKey) {
        case '1':
          this.getLoadClaim()
          break
        case '2':
          this.getflowAnnounce()
          break
        case '3':
          this.getPassFlowAnnounce('1')
          break
        case '4':
          this.getPassFlowAnnounce('0')
          break
        case '5':
          this.getRollbackFlow()
          break
        default:
          break
      }
    },
    getLoadClaim() {
      //得到所有未认领的流程
      w_postAction(`/todo/loadClaimTasks`)
        .then((res) => {
          this.loadClaimData = res
          for (var i = 0; i < this.loadClaimData.length; i++) {
            this.loadClaimData[i].state = '待领取'
            var type = this.loadClaimData[i].type
            switch (type) {
              case 'Participative':
                this.loadClaimData[i].type = '竞争任务'
                break
              case 'Normal':
                this.loadClaimData[i].type = '正常任务'
                break
              default:
                break
            }
          }
        })
        .catch((error) => {
          // console.log(error)
        })
    },
    claimTask(reocrd) {
      var that = this
      that.taskId = reocrd.id
      w_postAction(`/todo/claimTasks?taskId=` + that.taskId)
        .then((res) => {
          console.log('---------', res)
          that.$message.success('认领成功')
          that.getLoadClaim()
        })
        .catch((error) => {
          that.$message.success('认领失败')
          console.log(error)
        })
    },
    changeSelsct() {
      //得到第二个筛选框的内容
      this.getTwoList()
      this.city = []
      this.getData()
    },
    getOneList() {
      //得到第一个筛选框的内容
      this.provinceData = []
    },
    getTwoList() {
      //得到第二个筛选框的内容
      this.cities = []
    },
    getRollbackFlow() {
      //得到所有的已退回列表
      w_postAction(`/todo/loadRollbackTasks`)
        .then((res) => {
          this.flowRollbackData = res
          for (var i = 0; i < this.flowRollbackData.length; i++) {
            this.flowRollbackData[i].state = '已退回'
            var type = this.flowRollbackData[i].type
            switch (type) {
              case 'Participative':
                this.flowRollbackData[i].type = '竞争任务'
                break
              case 'Normal':
                this.flowRollbackData[i].type = '正常任务'
                break
              default:
                break
            }
          }
        })
        .catch((error) => {
          // console.log(error)
        })
    },
    getflowAnnounce() {
      const _this = this
      w_getAction(`/todo/loadTodoTasks?pageIndex=` + 1 + '&pageSize=' + 10)
        .then((res) => {
          this.flowWillAnnounceData = res
          for (var i = 0; i < this.flowWillAnnounceData.length; i++) {
            var state = this.flowWillAnnounceData[i].state
            switch (state) {
              case 'WaitReviewWorkload':
                this.flowWillAnnounceData[i].state = '工作量待审核'
                break
              case 'Ready':
                this.flowWillAnnounceData[i].state = '已开始'
                break
              case 'Completed':
                this.flowWillAnnounceData[i].state = '已完成'
                break
              case 'Reserved':
                this.flowWillAnnounceData[i].state = '已领取'
                break
              case 'Created':
                this.flowWillAnnounceData[i].state = '已创建'
                break
              default:
                break
            }
            var type = this.flowWillAnnounceData[i].type
            switch (type) {
              case 'Participative':
                this.flowWillAnnounceData[i].type = '竞争任务'
                break
              case 'Normal':
                this.flowWillAnnounceData[i].type = '正常任务'
                break
              default:
                break
            }
          }
        })
        .catch((error) => {
          // console.log(error)
        })
    },
    getprocessInstance() {
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      w_postAction(`/process/loadDeployProcess`)
        .then((res) => {
          console.log('res', res.result)
          let result = res.result
          if (result != null) {
            this.processInstance = this.processInstance.concat(res.result)
          }
          console.log(this.processInstance)
        })
        .catch((error) => {
          console.log(error)
        })
    },
    getPassFlowAnnounce(searchState) {
      //查询已完成和已拒绝的接口，searchState为状态，查询已拒绝传"0"，查询已完成传"1"
      const _this = this
      var id = this.instance
      console.log('id',);
      this.dialogVisibleFinish = false
      w_postAction(`/process/loadPassProcessInstance?processId=` + id + '&searchState=' + searchState)
        .then((res) => {
          if (res.length == 0) {
            _this.$message.success('此流程无数据')
            return
          }
          if (searchState == '0') {
            this.flowRejectData = res
            for (var i = 0; i < this.flowRejectData.length; i++) {
              this.flowRejectData[i].state = '已拒绝'
            }
          } else if (searchState == '1') {
            this.flowFinishData = res
            for (var i = 0; i < this.flowFinishData.length; i++) {
              this.flowFinishData[i].state = '已完成'
            }
          }
        })
        .catch((error) => {
          _this.$message.error('查询流程失败')
          console.log(error)
        })
    },
    announceTask(record) {
      this.taskId = record.id
      this.record = record
      w_postAction(`/todo/getTaskFormOnlineId?taskId=` + this.taskId)
        .then((res) => {
          var sizelist = res.result.prevForm_designer_id
          var arrayForm = new Array()
          for (var i = 0; i < sizelist.length; i++) {
            var formObj = {}
            formObj['prevForm_designer_id'] = res.result.prevForm_designer_id[i]
            formObj['prevOnline_data_id'] = res.result.prevOnline_data_id[i]
            formObj['prevOnline_table_id'] = res.result.prevOnline_table_id[i]
            arrayForm.push(formObj)
          }
          this.formList = arrayForm
          this.nowflowId = res.result.currentForm_designer_id
          this.recordLastForm = arrayForm[arrayForm.length - 1]
          this.approveTask = true
          this.getflowAnnounce()
        })
        .catch((error) => {
          console.log(error)
        })
    },
    announceRollTask(record) {
      this.taskId = record.id

      console.log('-----------', this.taskId)
      w_postAction(`/todo/getRollbackTaskFormOnlineId?taskId=` + this.taskId)
        .then((res) => {
          var sizelist = res.result.prevForm_designer_id
          var arrayForm = new Array() //展示的
          var rollForm = {} //不仅展示还要修改
          var lastForm1 = {}
          for (var i = 0; i < sizelist.length; i++) {
            if (i < sizelist.length - 2) {
              var formObj = {}
              formObj['prevForm_designer_id'] = res.result.prevForm_designer_id[i]
              formObj['prevOnline_data_id'] = res.result.prevOnline_data_id[i]
              formObj['prevOnline_table_id'] = res.result.prevOnline_table_id[i]
              arrayForm.push(formObj)
            } else if (i == sizelist.length - 2) {
              rollForm['prevForm_designer_id'] = res.result.prevForm_designer_id[i]
              rollForm['prevOnline_data_id'] = res.result.prevOnline_data_id[i]
              rollForm['prevOnline_table_id'] = res.result.prevOnline_table_id[i]
            } else {
              lastForm1['prevForm_designer_id'] = res.result.prevForm_designer_id[i]
              lastForm1['prevOnline_data_id'] = res.result.prevOnline_data_id[i]
              lastForm1['prevOnline_table_id'] = res.result.prevOnline_table_id[i]
            }
          }
          this.rollbackForm = rollForm
          this.rollFormList = arrayForm
          this.lastFrom = lastForm1
          this.approveRollTask = true
          this.getRollbackFlow()
        })
        .catch((error) => {
          console.log(error)
        })
    },

    closeApproveTask() {
      this.destroyTask = true
      this.approveTask = false
      this.getflowAnnounce()
    },
    closeRollApproveTask() {
      this.destroyRollTask = true
      this.approveRollTask = false
      this.getRollbackFlow()
    },
    closeSeeInformation() {
      this.destroyInformationFlag = true
      this.seeInformationFlag = false
    },
    close() {
      var _this = this
      this.$elementConfirm('是否关闭处理界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.closeApproveTask()
          this.closeRollApproveTask()
          this.closeSeeInformation()
        })
        .catch(() => { })
    },
    opendialogFinish() {
      this.dialogVisibleFinish = true
    },
    opendialogReject() {
      this.dialogVisibleReject = true
    },
  },
}
</script>
<style lang="scss" scoped>
.ididididi {
  margin-top: 20px;
}
.buttonstyle {
  position: absolute;
  right: 0;
  top: 0;
  width: 100%;
  height: 50px;
  zoom: 1;
}
.buttonstyle .xbutton {
  float: right;
  height: 30px;
  width: 40px;
  margin-top: 10px;
  margin-right: 10px;
}
.buttonstyle::after {
  content: '';
  height: 0;
  clear: both;
}
.addQA1 {
  z-index: 10;
  border: 1px solid #aaa;
  border-radius: 5px;
  width: 80%;
  height: 60%;
  background-color: #fff;
  height: auto;
  position: fixed;
  top: 27%;
  left: 18%;
}
.addQA1 .closeButton {
  font-size: 20px;
  float: right;
  margin: 14px;
  cursor: pointer;
}
.showHead {
  border-radius: 5px 5px 0px 0px;
  height: 40px;
  color: black;
  font-size: 20px;
  position: relative;
  background-color: #1890ff;
}
.showHead .showHeadContent {
  margin-left: 20px;
  line-height: 40px;
}
.overflow {
  transition: all 0.3s ease;
  position: fixed;
  width: 100%;
  height: 100%;
  z-index: 5;
  background-color: rgba(70, 60, 60, 0.49);
  top: 0px;
}
.card-table {
  background-color: white;
}
.selectText {
  color: black;
  font-size: 20px;
  margin-left: 22px;
}
.seeInformation {
  z-index: 300;
  width: 95%;
  border: 1px solid #aaa;
  border-radius: 5px;
  margin: 20px;
  background-color: white;
}
.seeContent {
  margin-top: 7%;
  margin-bottom: 7%;
  margin-left: 7%;
  margin-right: 7%;
}
</style>
