<template>
  <div>
    <div>
      <a-card :bordered="false">
        <div id="formContent" style="margin-top: -10px">
          <div id="taskList">
            <div>
              <a-tabs :tabBarStyle="{ textAlign: 'center' }" v-model="taskTab.tabKey">
                <a-tab-pane key="3" tab="可变更" v-if="['施工企业', '管理员'].some((role) => userInfo.roleNames.includes(role))">
                  <div>
                    <div class="card-table" style="padding: 10px">
                      <a-card :bordered="false">
                        <div class="flowAnnounce">
                          <commonTable ref="commonTableRef1" :configurationParameter="configurationParameter1"
                            :startFixedProcess="startFixedProcess">
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
                            :seeHistory="seeHistory">
                          </commonTable>
                        </div>
                      </a-card>
                    </div>
                  </div>
                </a-tab-pane>
                <a-tab-pane key="1">
                  <template #tab>
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
    <a-modal :title="modalTitle" :visible="isModalVisible" @ok="startProcess" @cancel="handleCancel" width="800px">
      <div class="flowConfig">
        <div style="padding-top: 20px">
          <span>请选择变更的存缴方式：</span>
          <a-select v-model="selectedProcessId" placeholder="请选择一个流程" style="width: 300px">
            <a-select-option v-for="item in flowConfigData" :key="item.processId" :value="item.processId">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </div>
      </div>
    </a-modal>
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
import { depositList, depositCategoryId, changeList, changeCategoryId } from '@/api/processId'
export default {
  name: 'flowChange',
  components: { annTask, ApproveTask, ApproveNewTask, RollbackTask, approveModel, FlowHistory, commonTable },
  data() {
    return {
      currentRecord: {},
      currentProjectStatus: '',
      backlogNumber: 0,
      taskTab: {
        tabKey: '3', // 主 Tab 页的状态
      },
      flowConfigData: [],
      isModalVisible: false,
      selectedProcessId: null,
      configurationParameter1: {
        inquire: {
          categoryId: depositCategoryId, //流程分类
          processIdList: depositList,
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
            title: '存缴方式',
            align: 'center',
            dataIndex: 'depositWay',
            dataLocation: 'allData.main_payment.deposit_way',
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
            dataIndex: 'creditCode',
            dataLocation: 'allData.main_payment.credit_code',
            show: false,
          },
          {
            dataIndex: 'companyAddress',
            dataLocation: 'allData.main_payment.company_address',
            show: false,
          },
          {
            dataIndex: 'postalCode',
            dataLocation: 'allData.main_payment.postal_code',
            show: false,
          },
          {
            dataIndex: 'contractAmount',
            dataLocation: 'allData.main_payment.contract_amount',
            show: false,
          },
          {
            dataIndex: 'addressDetail',
            dataLocation: 'allData.main_payment.address_detail',
            show: false,
          },
          {
            dataIndex: 'formCreateDate',
            dataLocation: 'allData.main_payment.form_create_date',
            show: false,
          },
          {
            dataIndex: 'formEndDate',
            dataLocation: 'allData.main_payment.form_end_date',
            show: false,
          },
          {
            dataIndex: 'proportions',
            dataLocation: 'allData.main_payment.proportions',
            show: false,
          },
          {
            dataIndex: 'reason',
            dataLocation: 'allData.main_payment.reason',
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
            dataIndex: 'flowChangecolumns',
            scopedSlots: { customRender: 'flowChangecolumns' },
            show: true,
          },
        ],
        filterFunction: (dataList) => {
          return dataList.filter(item =>
            Number(item.isRefundable) !== 1 // 过滤掉 isRefundable 为 1 的项(已返还)
          );
        }
      },
      configurationParameter2: {
        inquire: {
          categoryId: changeCategoryId, //流程分类
          processIdList: changeList, //想要显示的流程信息
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
            title: '变更前存缴方式',
            align: 'center',
            dataIndex: 'oldDepositMethod',
            dataLocation: 'allData.main_change.old_deposit_method',
            show: true,
          },
          {
            title: '变更后存缴方式',
            align: 'center',
            dataIndex: 'newDepositMethod',
            dataLocation: 'allData.main_change.new_deposit_method',
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
            dataLocation: 'allData.main_change.create_time',
            show: true,
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
          categoryId: changeCategoryId, //流程分类
          processIdList: changeList, //想要显示的流程信息
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
            title: '变更前存缴方式',
            align: 'center',
            dataIndex: 'oldDepositMethod',
            dataLocation: 'allData.main_change.old_deposit_method',
            show: true,
          },
          {
            title: '变更后存缴方式',
            align: 'center',
            dataIndex: 'newDepositMethod',
            dataLocation: 'allData.main_change.new_deposit_method',
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
            dataLocation: 'allData.main_change.create_time',
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
    }
  },
  computed: {
    modalTitle() {
      return (
        <div>
          <span style="color: #52c41a;">当前存缴方式：{this.currentProjectStatus}</span>
        </div>
      )
    },
    userInfo() {
      // 从 Vue.ls 中获取 USER_INFO
      return Vue.ls.get(USER_INFO) || {} // 如果没有值，默认为空对象
    },
  },

  created() {
    if (['施工企业', '管理员'].some((role) => this.userInfo.roleNames.includes(role))) {
      this.taskTab.tabKey = '3' // 显示 "可变更" 页
    } else {
      this.taskTab.tabKey = '2' // 显示 "历史" 页
    }
  },
  mounted() {
    this.startFixedProcess(false, null)
    this.getData()
    console.log('当前用户信息', this.userInfo)
    // 添加路由参数主动检查
    if(this.$route.query.tab) {
      this.taskTab.tabKey = this.$route.query.tab
    }
  },
  methods: {
    //获取存缴方式变更的流程数据,1860602147955077121是存缴方式变更的流程分类id
    startFixedProcess(showModal, record) {
      //获取当前点击的项目名称和存缴方式
      // this.currentProjectName = record ? record.projectName : ''
      this.currentProjectStatus = record ? record.depositWay : ''
      this.currentRecord = record
      let url = '/process/processList/{categoryId}?categoryId=1860602147955077121&category=1'
      nw_getAction(url)
        .then((res) => {
          console.log('存缴方式变更流程数据', res)
          if (res.success) {
            let flowConfigData = res.result

            // 去掉name中的"变更为"前缀
            flowConfigData = flowConfigData.map((item) => {
              return {
                ...item,
                name: item.name.replace(/^变更为/, ''), // 移除前缀
              }
            })

            //是否显示弹窗
            if (showModal) {
              this.isModalVisible = true
            }
            this.flowConfigData = flowConfigData.filter((item) => {
              // 如果当前存缴方式包含某些关键词，就过滤掉对应的流程
              if (this.currentProjectStatus.includes('银行现金存单') && item.name.includes('银行现金存单')) {
                return false
              }
              if (this.currentProjectStatus.includes('保险公司保函') && item.name.includes('保险公司保函')) {
                return false
              }
              if (this.currentProjectStatus.includes('银行保函') && item.name.includes('银行保函')) {
                return false
              }
              if (this.currentProjectStatus.includes('担保公司保函') && item.name.includes('担保公司保函')) {
                return false
              }
              return true
            })
            this.$message.success('加载成功')
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
    startProcess() {
      this.isModalVisible = false
      let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
      axios.defaults.headers.common['userName'] = userData.value.username
      console.log('userData.value.username', userData.value.username)
      nw_getAction(`/process/startProcess/{processId}?processId=` + this.selectedProcessId)
        .then((res) => {
          if (res.success) {
            this.$message.success('开启流程成功')
            const { formDesignerId, onlineDataId, onlineTableId, processInstanceId } = res.result.startProcessVO
            const taskId = res.result.fistTaskId
            //在传给annTask组件的时候，将新的存缴方式传过去
            const selectedProcess = this.flowConfigData.find((item) => item.processId === this.selectedProcessId)
            if (selectedProcess) {
              // 去掉 "变更为" 前缀
              const processName = selectedProcess.name.replace(/^变更为/, '')
              // 将处理后的值赋给 newProjectStatus
              this.currentRecord.newProjectStatus = processName
            } else {
              console.log('未找到匹配的流程配置')
            }
            this.$refs.modalform.openModal(
              formDesignerId,
              onlineDataId,
              onlineTableId,
              taskId,
              processInstanceId,
              '变更',
              this.currentRecord
            )
          } else {
            this.$message.error('开启流程失败')
          }
          this.selectedProcessId = null
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //查看历史
    seeHistory(record) {
      this.$refs.flowHistory.openModal(record)
    },
    // 更新表格数据
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
    handleCancel() {
      this.isModalVisible = false // 点击取消后隐藏弹窗
      this.selectedProcessId = null
    },
    //处理该任务
    announceTask(record) {
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