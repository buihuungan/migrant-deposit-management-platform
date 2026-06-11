<template>
  <div>
    <div>
      <a-card :bordered="false">
        <div id="formContent" style="margin-top: -10px">
          <div id="taskList">
            <div>
              <a-tabs :tabBarStyle="{ textAlign: 'center' }" v-model="taskTab.tabKey">
                <a-tab-pane key="3" tab="待返还" v-if="['施工企业', '管理员'].some((role) => userInfo.roleNames.includes(role))">
                  <div>
                    <div class="card-table" style="padding: 10px">
                      <a-card :bordered="false">
                        <div class="flowAnnounce">
                          <commonTable ref="commonTableRef1" :configurationParameter="configurationParameter1"
                            :startProcess="startProcess">
                          </commonTable>
                        </div>
                      </a-card>
                    </div>
                  </div>
                </a-tab-pane>

                <a-tab-pane key="2" tab="历史">
                  <div>
                    <div class="card-table">
                      <a-card :bordered="false">
                        <div class="table-container">
                          <commonTable ref="commonTableRef2" :configurationParameter="configurationParameter2"
                            :seeHistory="seeHistory" :download="download">
                          </commonTable>
                        </div>
                      </a-card>
                    </div>
                  </div>
                </a-tab-pane>
                <a-tab-pane key="1">
                  <template #tab>
                    <!-- <a-badge :count="pendingTasksCount"> -->
                    <a-badge :count="backlogNumber" :offset="[10, 0]">
                      <span>待办事项</span>
                    </a-badge>
                  </template>
                  <div>
                    <div class="card-table" style="padding: 10px">
                      <a-card :bordered="false">
                        <div class="flowAnnounce">
                          <commonTable ref="commonTableRef3" :configurationParameter="configurationParameter3"
                            :seeHistory="seeHistory" :announceTask="announceTask">
                          </commonTable>
                        </div>
                      </a-card>
                    </div>
                  </div>
                </a-tab-pane>
              </a-tabs>
            </div>
          </div>
        </div>
      </a-card>
    </div>
    <annTask ref="modalform" :getData="getData" :userInfo="userInfo"> </annTask>

    <approve-model ref="approveModel" @close="getData"></approve-model>
    <flow-history ref="flowHistory"></flow-history>
  </div>
</template>
<script>
import axios from 'axios'
import annTask from './modules/annTask'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import { nw_postAction, nw_postAction1, nw_getAction } from '@api/newWorkApi'
import ApproveTask from './modules/approveTask'
import ApproveNewTask from './modules/approveNewTask'
import RollbackTask from './modules/rollbackTask'
import approveModel from './modules/approveModel'
import { w_postAction, w_postAction1, w_getAction } from '@/api/workapi'
import Template from '../identification/template.vue'
import FlowHistory from './modules/flowHistory'
import { USER_NAME, USER_INFO } from '@/store/mutation-types'
import Vue from 'vue'
import { mapState } from 'vuex'
import { taskStateMapping } from './taskStateMapping'
import commonTable from './modules/commonTable.vue'
import { returnList, returnCategoryId, depositList, depositCategoryId } from '@/api/processId'
import { downloadDocument } from '@/api/userList'
export default {
  name: 'flowReturn',
  components: { annTask, ApproveTask, ApproveNewTask, RollbackTask, approveModel, FlowHistory, commonTable },
  data() {
    return {
      configurationParameter1: {
        inquire: {
          categoryId: depositCategoryId, //流程分类
          processIdList: depositList, //想要显示的流程信息
          applyState: ['complete'], //想要查询的流程类型
        },
        columnsData: [
          {
            title: '企业名称',
            align: 'center',
            dataIndex: 'companyName',
            dataLocation: 'allData.main_payment.enterprise_name',
            show: true,
            filterType: 'select',
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            dataLocation: 'allData.main_payment.project_name',
            show: true,
            filterType: 'mixedInput',
          },
          {
            title: '所属区县',
            align: 'center',
            dataIndex: 'projectAddress',
            dataLocation: 'allData.main_payment.project_address',
            show: true,
            filterType: 'mixedInput',
          },
          {
            title: '保证金金额（万元）',
            align: 'center',
            dataIndex: 'Money',
            dataLocation: 'allData.main_payment.money',
            show: true,
          },
          {
            title: '剩余金额（万元）',
            align: 'center',
            dataIndex: 'remainingAmount',
            dataLocation: 'allData.main_payment.remaining_amount',
            show: true,
          },
          {
            title: '负责人',
            align: 'center',
            dataIndex: 'responsiblePerson',
            dataLocation: 'allData.main_payment.responsible_person',
            show: true,
            filterType: 'input',
          },
          {
            title: '联系方式',
            align: 'center',
            dataIndex: 'mobile',
            dataLocation: 'allData.main_payment.mobile',
            show: true,
          },
          {
            title: '存缴完成时间',
            align: 'center',
            dataIndex: 'createDate',
            dataLocation: 'endDate',
            show: true,
            filterType: 'date',
          },
          {
            dataIndex: 'companyAddress',
            dataLocation: 'allData.main_payment.company_address',
            show: false,
          },
          {
            dataIndex: 'addressDetail',
            dataLocation: 'allData.main_payment.address_detail',
            show: false,
          },
          {
            dataIndex: 'storageStartDate',
            dataLocation: 'allData.main_payment.storage_start_date',
            show: false,
          },
          {
            dataIndex: 'storageEndDate',
            dataLocation: 'allData.main_payment.storage_end_date',
            show: false,
          },
          {
            dataIndex: 'isRefundable',
            dataLocation: 'allData.main_payment.is_refundable',
            show: false,
          },
          {
            title: '操作',
            align: 'center',
            dataIndex: 'flowReturncolumns',
            scopedSlots: { customRender: 'flowReturncolumns' },
            show: true,
          },
        ],
        filterFunction: (dataList) => {
          return dataList.filter(item =>
            // 过滤掉 保证金 为 0 和返还过的项
            Number(item.Money) !== 0 && Number(item.isRefundable) !== 1
          );
        }
      },
      configurationParameter2: {
        inquire: {
          categoryId: returnCategoryId, //流程分类
          // 这里差一个返还的流程
          processIdList: returnList, //想要显示的流程信息
          applyState: ['instance', 'cancel', 'complete'], //想要查询的流程类型
        },
        columnsData: [
          {
            title: '状态',
            align: 'center',
            dataIndex: 'nodeName',
            dataLocation: 'nodeName',
            show: true,
            filterType: 'select',
          },
          {
            title: '企业名称',
            align: 'center',
            dataIndex: 'companyName',
            dataLocation: 'allData.main_payment.enterprise_name',
            show: true,
            filterType: 'select',
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            dataLocation: 'allData.main_payment.project_name',
            show: true,
            filterType: 'mixedInput',
          },
          {
            title: '所属区县',
            align: 'center',
            dataIndex: 'projectAddress',
            dataLocation: 'allData.main_payment.project_address',
            show: true,
            filterType: 'mixedInput',
          },
          {
            title: '保证金金额（万元）',
            align: 'center',
            dataIndex: 'Money',
            dataLocation: 'allData.main_payment.money',
            show: true,
          },
          {
            title: '剩余金额（万元）',
            align: 'center',
            dataIndex: 'remainingAmount',
            dataLocation: 'allData.main_payment.remaining_amount',
            show: true,
          },
          {
            title: '申请返还金额（万元）',
            align: 'center',
            dataIndex: 'returnAmount',
            dataLocation: 'allData.main_return.return_amount',
            show: true,
          },
          {
            title: '负责人',
            align: 'center',
            dataIndex: 'responsiblePerson',
            dataLocation: 'allData.main_payment.responsible_person',
            show: true,
            filterType: 'input',
          },
          {
            title: '联系方式',
            align: 'center',
            dataIndex: 'mobile',
            dataLocation: 'allData.main_payment.mobile',
            show: true,
          },
          {
            title: '创建时间',
            align: 'center',
            dataIndex: 'createDate',
            dataLocation: 'allData.main_return.create_time',
            show: true,
          },
          {
            dataIndex: 'is_export',
            dataLocation: 'allData.main_return.is_export',
            show: false,
          },
          {
            dataIndex: 'export_path',
            dataLocation: 'allData.main_return.export_path',
            show: false,
          },
          {
            title: '详情',
            align: 'center',
            dataIndex: 'flowHistoryaction',
            scopedSlots: { customRender: 'flowHistoryaction' },
            show: true,
          },
        ],
      },
      configurationParameter3: {
        inquire: {
          categoryId: returnCategoryId, //流程分类
          // 这里差一个返还的流程
          processIdList: returnList, //想要显示的流程信息
          applyState: ['pending'], //想要查询的流程类型
        },
        columnsData: [
          {
            title: '状态',
            align: 'center',
            dataIndex: 'nodeName',
            dataLocation: 'nodeName',
            show: true,
            filterType: 'select',
          },
          {
            title: '企业名称',
            align: 'center',
            dataIndex: 'companyName',
            dataLocation: 'allData.main_payment.enterprise_name',
            show: true,
            filterType: 'select',
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            dataLocation: 'allData.main_payment.project_name',
            show: true,
            filterType: 'mixedInput',
          },
          {
            title: '所属区县',
            align: 'center',
            dataIndex: 'projectAddress',
            dataLocation: 'allData.main_payment.project_address',
            show: true,
            filterType: 'mixedInput',
          },
          {
            title: '保证金金额（万元）',
            align: 'center',
            dataIndex: 'Money',
            dataLocation: 'allData.main_payment.money',
            show: true,
          },
          {
            title: '剩余金额（万元）',
            align: 'center',
            dataIndex: 'remainingAmount',
            dataLocation: 'allData.main_payment.remaining_amount',
            show: true,
          },
          {
            title: '申请返还金额（万元）',
            align: 'center',
            dataIndex: 'returnAmount',
            dataLocation: 'allData.main_return.return_amount',
            show: true,
          },
          {
            title: '负责人',
            align: 'center',
            dataIndex: 'responsiblePerson',
            dataLocation: 'allData.main_payment.responsible_person',
            show: true,
            filterType: 'input',
          },
          {
            title: '联系方式',
            align: 'center',
            dataIndex: 'mobile',
            dataLocation: 'allData.main_payment.mobile',
            show: true,
          },
          {
            title: '创建时间',
            align: 'center',
            dataIndex: 'createDate',
            dataLocation: 'allData.main_return.create_time',
            show: true,
          },
          {
            title: '操作',
            align: 'center',
            dataIndex: 'flowWillAnnounceaction',
            scopedSlots: { customRender: 'flowWillAnnounceaction' },
            show: true,
          },
        ],
      },
      returnProcessId: 0,
      backlogNumber: 0,
      taskTab: {
        tabKey: '3', // 主 Tab 页的状态
      },
    }
  },
  computed: {
    userInfo() {
      // 从 Vue.ls 中获取 USER_INFO
      return Vue.ls.get(USER_INFO) || {} // 如果没有值，默认为空对象
    },
  },

  created() {
    if (['施工企业'].some((role) => this.userInfo.roleNames.includes(role))) {
      this.taskTab.tabKey = '3' // 显示 "待返还" 页
    } else {
      this.taskTab.tabKey = '2' // 显示 "历史" 页
    }
  },
  mounted() {
    this.startFixedProcess()
    this.getData()
    console.log('当前用户信息', this.userInfo)
    // 添加路由参数主动检查
    if(this.$route.query.tab) {
      this.taskTab.tabKey = this.$route.query.tab
    }
  },
  methods: {
    //获取保证金返还的流程数据,1867119977859956738是保证金返还的流程分类id
    startFixedProcess() {
      let url = '/process/processList/{categoryId}?categoryId=1867119977859956738&category=1'
      nw_getAction(url)
        .then((res) => {
          console.log('保证金返还流程数据', res)
          if (res.success) {
            this.returnProcessId = res.result[0].processId
          } else {
            this.$message.error('查询可开启的流程失败')
          }
        })
        .catch((error) => {
          console.log(error)
          this.$message.error('请求失败')
        })
    },
    //开启流程
    startProcess(record) {
      let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
      axios.defaults.headers.common['userName'] = userData.value.username
      console.log('userData.value.username', userData.value.username)
      nw_getAction(`/process/startProcess/{processId}?processId=${this.returnProcessId}`)
        .then((res) => {
          if (res.success) {
            this.$message.success('开启流程成功')
            const { formDesignerId, onlineDataId, onlineTableId, processInstanceId } = res.result.startProcessVO
            const taskId = res.result.fistTaskId
            this.$refs.modalform.openModal(
              formDesignerId,
              onlineDataId,
              onlineTableId,
              taskId,
              processInstanceId,
              '返还',
              record
            )
          } else {
            this.$message.error('开启流程失败')
          }
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //查看历史
    seeHistory(record) {
      this.$refs.flowHistory.openModal(record)
    },
    download(record) {
      console.log('downloadrecord', record);
      downloadDocument(record)
    },    // 更新表格数据
    getData() {
      const commonTableInstance1 = this.$refs.commonTableRef1
      if (commonTableInstance1) {
        commonTableInstance1.getAllList()
      }
      const commonTableInstance2 = this.$refs.commonTableRef2
      if (commonTableInstance2) {
        commonTableInstance2.getAllList()
      }
      const commonTableInstance3 = this.$refs.commonTableRef3
      if (commonTableInstance3) {
        commonTableInstance3.getAllList()
      }
    },
    //处理该任务
    announceTask(record) {
      console.log('record1', record)
      if (record.flag) {
        this.$refs.approveModel.announceRollTask(record)
      } else {
        this.$refs.approveModel.announceTask(record)
      }
    },
  },
  watch: {
    '$route.query.tab': {
      immediate: true,
      deep: true,
      handler(newVal) {
        if (newVal) {
          this.taskTab.tabKey = newVal;
        } else {
          this.taskTab.tabKey = '2' // 默认显示历史tab
        }
        this.$nextTick(() => {
          this.getData()
        })
      }
    }
  },
}
</script>
<style scoped>
.card-table {
  background-color: white;
  min-height: 650px;
}

.table-container {
  width: 100%;
  /* 使容器宽度自适应 */
  overflow-x: auto;
  /* 在小屏幕上支持水平滚动 */
}
</style>