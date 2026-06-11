<template>
    <div>
        <div>
            <a-card :bordered="false">
                <div id="formContent" style="margin-top: -10px">
                    <div id="taskList">
                        <div>
                            <a-tabs :tabBarStyle="{ textAlign: 'center' }" v-model="taskTab.tabKey">
                                <a-tab-pane key="2" tab="历史">
                                    <div>
                                        <div class="card-table">
                                            <a-card :bordered="false">
                                                <div class="table-container">
                                                    <commonTable ref="commonTableRef1"
                                                        :configurationParameter="configurationParameter1"
                                                        :seeHistory="seeHistory" :download="download">
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
                                                    <commonTable ref="commonTableRef2"
                                                        :configurationParameter="configurationParameter2"
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
import { registerList, registerCategoryId } from '@/api/processId'
import { downloadDocument } from '@/api/userList'

export default {
    name: 'flowUse',
    components: { annTask, ApproveTask, ApproveNewTask, RollbackTask, approveModel, FlowHistory, commonTable },
    data() {
        return {
            configurationParameter1: {
                inquire: {
                    categoryId: registerCategoryId, //流程分类
                    processIdList: registerList, //存缴+使用
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
                        title: '企业账号',
                        align: 'center',
                        dataIndex: 'userName',
                        dataLocation: 'allData.main_register.username',
                        show: true,
                        filterType: 'mixedInput',
                    },
                    {
                        title: '企业名称',
                        align: 'center',
                        dataIndex: 'companyName',
                        dataLocation: 'allData.main_register.company_name',
                        show: true,
                        filterType: 'mixedInput',
                    },
                    {
                        title: '统一社会信用代码',
                        align: 'center',
                        dataIndex: 'creditCode',
                        dataLocation: 'allData.main_register.credit_code',
                        show: true,
                    },
                    {
                        title: '法定代表人',
                        align: 'center',
                        dataIndex: 'representative',
                        dataLocation: 'allData.main_register.representative',
                        show: true,
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        dataIndex: 'createDate',
                        dataLocation: 'allData.main_register.create_time',
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
            configurationParameter2: {
                inquire: {
                    categoryId: registerCategoryId, //流程分类
                    processIdList: registerList, //存缴+使用
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
                        title: '企业账号',
                        align: 'center',
                        dataIndex: 'userName',
                        dataLocation: 'allData.main_register.username',
                        show: true,
                        filterType: 'mixedInput',
                    },
                    {
                        title: '企业名称',
                        align: 'center',
                        dataIndex: 'companyName',
                        dataLocation: 'allData.main_register.company_name',
                        show: true,
                        filterType: 'mixedInput',
                    },
                    {
                        title: '统一社会信用代码',
                        align: 'center',
                        dataIndex: 'creditCode',
                        dataLocation: 'allData.main_register.credit_code',
                        show: true,
                    },
                    {
                        title: '法定代表人',
                        align: 'center',
                        dataIndex: 'representative',
                        dataLocation: 'allData.main_register.representative',
                        show: true,
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        dataIndex: 'createDate',
                        dataLocation: 'allData.main_register.create_time',
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
            useProcessId: 0,
            backlogNumber: 0,
            taskTab: {
                tabKey: '2', // 主 Tab 页的状态
            },
        }
    },
    computed: {
        userInfo() {
            // 从 Vue.ls 中获取 USER_INFO
            return Vue.ls.get(USER_INFO) || {} // 如果没有值，默认为空对象
        },
    },
    mounted() {
        this.getData()
        console.log('当前用户信息', this.userInfo)
        // 添加路由参数主动检查
        if (this.$route.query.tab) {
            this.taskTab.tabKey = this.$route.query.tab
        }
    },
    methods: {
        //查看历史
        seeHistory(record) {
            this.$refs.flowHistory.openModal(record)
        },
        download(record) {
            console.log('downloadrecord', record);
            downloadDocument(record)
        },
        // 更新表格数据
        getData() {
            // 先获取子组件实例
            const commonTableInstance1 = this.$refs.commonTableRef1
            if (commonTableInstance1) {
                commonTableInstance1.getAllList()
            }
            const commonTableInstance2 = this.$refs.commonTableRef2
            if (commonTableInstance2) {
                commonTableInstance2.getAllList()
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
                    this.taskTab.tabKey = '2'
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