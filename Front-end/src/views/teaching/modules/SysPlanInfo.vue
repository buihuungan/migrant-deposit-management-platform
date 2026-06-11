<template>
  <div>
    <a-tabs default-active-key="0" @change="callback">
      <a-tab-pane key="0" tab="新建教学计划">
        <a-card :bordered="false">
          <!-- 查询区域 -->
          <!-- 操作按钮区域 -->
          <div class="table-operator" :md="24" :sm="24" style="margin-top: -15px">
            <a-button @click="handleAdd" type="primary" icon="plus" style="margin-top: 16px" :disabled="idNotDep"
              >新建教学计划</a-button
            >
          </div>

          <!-- table区域-begin -->
          <div>
            <a-table
              :columns="columns"
              size="middle"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
            >
              <span slot="action" slot-scope="text, record">
                <a v-if="record.edu_plan_detail" @click="handleGo(record)">开启计划</a>
                <a-divider v-if="record.edu_plan_detail" type="vertical" />
                <a-dropdown v-if="record.edu_plan_detail">
                  <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
                  <a-menu slot="overlay">
                    <a-menu-item>
                      <a href="javascript:;" @click="handleEdit(record)">编辑</a>
                    </a-menu-item>
                    <a-menu-item>
                      <a href="javascript:;" @click="handleDetail(record)">详情</a>
                    </a-menu-item>
                    <a-menu-item>
                      <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                        <a>删除</a>
                      </a-popconfirm>
                    </a-menu-item>
                  </a-menu>
                </a-dropdown>
              </span>
                <a-table
                  :columns="innerColumns"
                  :dataSource="record.edu_plan_detail"
                  :pagination="false"
                  slot="expandedRowRender"
                  slot-scope="record"
                >
                <span slot="action1" slot-scope="text, record">
                  <span v-if="record.teaching_mode=='1'">单人模式</span>
                  <span v-else-if="record.teaching_mode=='2'">团队模式</span>
                  <span v-else>无模式</span>
                </span>
                <span slot="action2" slot-scope="text, record">
                  <a-dropdown v-if="record.teaching_mode == '2' && record.is_group == '0'">
                    <a class="ant-dropdown-link"> 分组 <a-icon type="down" /> </a>
                    <a-menu slot="overlay">
                      <a-menu-item>
                        <a href="javascript:;" @click="manualGroup(record)">手动分组</a>
                      </a-menu-item>
                      <a-menu-item>
                        <a href="javascript:;" @click="automaticGroup(record)">自动分组</a>
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                  <a-dropdown v-if="record.teaching_mode == '2' && record.is_group == '1'">
                    <a class="ant-dropdown-link">分组详情<a-icon type="down" /> </a>
                    <a-menu slot="overlay">
                      <a-menu-item>
                        <a href="javascript:;" @click="viewGroup(record)">查看分组</a>
                      </a-menu-item>
                      <a-menu-item>
                        <a href="javascript:;" @click="downloadGroupExcel(record)">导出excel表</a>
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                  <a v-if="record.teaching_mode == '1'">无分组</a>
                  <!-- <a v-if="record.teaching_mode == '2' && record.is_group == '0'" @click="startGroup(record)">分组</a> -->
                  <!-- <a v-else-if="record.teaching_mode == '2' && record.is_group == '1'" @click="viewGroup(record)">查看分组</a> -->
                  <!-- <a :disabled="record.teaching_mode!== '2'" @click="startGroup(record)">分组</a> -->
                </span>
              </a-table>
            </a-table>
          </div>
          <!-- table区域-end

          表单区域 -->
        </a-card>
      </a-tab-pane>
      <a-tab-pane key="1" tab="已开启教学计划">
        <a-card :bordered="false">
          <!-- table区域-begin -->
          <div>
            <a-table
              :columns="columns"
              size="middle"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            >
              <span v-if="record.edu_plan_detail" slot="action" slot-scope="text, record">
                <a-popconfirm title="确定完成教学计划吗?" @confirm="() => handleOver(record)">
                  <a>完成教学计划</a>
                </a-popconfirm>
                <a-divider type="vertical" />
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </span>
              <a-table
                  :columns="innerColumns"
                  :dataSource="record.edu_plan_detail"
                  :pagination="false"
                  slot="expandedRowRender"
                  slot-scope="record"
                >
                <span slot="action1" slot-scope="text, record">
                  <span v-if="record.teaching_mode=='1'">单人模式</span>
                  <span v-else-if="record.teaching_mode=='2'">团队模式</span>
                  <span v-else>无模式</span>
                </span>
                <span slot="action2" slot-scope="text, record">
                  
                  <!-- <a v-if="record.teaching_mode == '2' && record.is_group == '0'" @click="startGroup(record)">分组</a>
                  <a v-else-if="record.teaching_mode == '2' && record.is_group == '1'" @click="viewGroup(record)">查看分组</a>
                  <a v-else>无分组</a> -->
                  <a-dropdown v-if="record.teaching_mode == '2' && record.is_group == '0'">
                    <a class="ant-dropdown-link"> 分组 <a-icon type="down" /> </a>
                    <a-menu slot="overlay">
                      <a-menu-item>
                        <a href="javascript:;" @click="manualGroup(record)">手动分组</a>
                      </a-menu-item>
                      <a-menu-item>
                        <a href="javascript:;" @click="automaticGroup(record)">自动分组</a>
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                  <a-dropdown v-if="record.teaching_mode == '2' && record.is_group == '1'">
                    <a class="ant-dropdown-link">分组详情<a-icon type="down" /> </a>
                    <a-menu slot="overlay">
                      <a-menu-item>
                        <a href="javascript:;" @click="viewGroup(record)">查看分组</a>
                      </a-menu-item>
                      <a-menu-item>
                        <a href="javascript:;" @click="downloadGroupExcel(record)">导出excel表</a>
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                  <a v-if="record.teaching_mode == '1'">无分组</a>

                  <!-- <a :disabled="record.teaching_mode!== '2'" @click="startGroup(record)">分组</a> -->
                  <a-divider type="vertical" />
                  <a @click="viewStudyStatistics(record)">查看学习统计</a>
                  <a-divider type="vertical" />
                  <a @click="suspengCourse(record)">中止课程</a>
                </span>
              </a-table>
            </a-table>
          </div>
          <!-- table区域-end -->
        </a-card>
      </a-tab-pane>
      <a-tab-pane key="2" tab="已完成教学计划">
        <a-card :bordered="false">
          <!-- table区域-begin -->
          <div>
            <a-table
              :columns="columns"
              size="middle"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            >
              <span slot="action" slot-scope="text, record">
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </span>
              <a-table
                  :columns="innerColumns"
                  :dataSource="record.edu_plan_detail"
                  :pagination="false"
                  slot="expandedRowRender"
                  slot-scope="record"
                >
                <span slot="action1" slot-scope="text, record">
                  <span v-if="record.teaching_mode=='1'">单人模式</span>
                  <span v-else-if="record.teaching_mode=='2'">团队模式</span>
                  <span v-else>无模式</span>
                </span>
                <span slot="action2" slot-scope="text, record">
                  <!-- <a v-if="record.teaching_mode == '2' && record.is_group == '0'" @click="startGroup(record)">分组</a>
                  <a v-else-if="record.teaching_mode == '2' && record.is_group == '1'" @click="viewGroup(record)">查看分组</a>
                  <a v-else>无分组</a> -->

                  <a-dropdown v-if="record.teaching_mode == '2' && record.is_group == '0'">
                    <a class="ant-dropdown-link"> 分组 <a-icon type="down" /> </a>
                    <a-menu slot="overlay">
                      <a-menu-item>
                        <a href="javascript:;" @click="manualGroup(record)">手动分组</a>
                      </a-menu-item>
                      <a-menu-item>
                        <a href="javascript:;" @click="automaticGroup(record)">自动分组</a>
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                  <a-dropdown v-if="record.teaching_mode == '2' && record.is_group == '1'">
                    <a class="ant-dropdown-link">分组详情<a-icon type="down" /> </a>
                    <a-menu slot="overlay">
                      <a-menu-item>
                        <a href="javascript:;" @click="viewGroup(record)">查看分组</a>
                      </a-menu-item>
                      <a-menu-item>
                        <a href="javascript:;" @click="downloadGroupExcel(record)">导出excel表</a>
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                  <a v-if="record.teaching_mode == '1'">无分组</a>
                  <!-- <a :disabled="record.teaching_mode!== '2'" @click="startGroup(record)">分组</a> -->
                  <a-divider type="vertical" />
                  <a @click="viewStudyStatistics(record)">查看学习统计</a>
                </span>
              </a-table>
            </a-table>
            <!-- 分组对话框 -->
          </div>
          
          <!-- table区域-end -->
        </a-card>
      </a-tab-pane>
    </a-tabs>
    <a-modal
      :title="title"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      width="300px"
    >
      <a-input v-model="groupNumber" placeholder="请输入每组人数" />
    </a-modal>

    <plan-modal ref="modalForm" @ok="modalFormOk" @close="close"></plan-modal>

    <start-plan ref="planForm" @ok="loadData(1)"></start-plan>
    <start-group ref="startGroupModal" @ok="loadData(1)"></start-group>
    <view-group ref="viewGroupModal"></view-group>
    <study-statistics ref="StudyStatisticsModal"></study-statistics>
    <end-course ref="endCourse"></end-course>
  </div>
</template>

<script>
import axios from 'axios'
import api from '@/api/index'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction, postAction, deleteAction, putAction } from '@/api/manage'
import { e_putAction, e_postAction, e_getAction } from '@/api/eduApi'
import PlanModal from './PlanModal'
import StartPlan from './startPlan.vue'
import qs from 'qs'
import StartGroup from './startGroup.vue'
import viewGroup from './viewGroup.vue'
import studyStatistics from  './studyStatistics.vue'
import endCourse from './endCourse.vue'
import { resolve } from 'path'
import { reject } from 'lodash'

export default {
  name: 'SysPlanInfo',
  mixins: [JeecgListMixin],
  components: {
    PlanModal,
    StartPlan,
    StartGroup,
    viewGroup,
    studyStatistics,
    endCourse,
  },
  props: {
    departIdList: {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  data() {
    return {
      loading : false,
      groupLessonId: '',
      groupPlanId: '',
      groupNumber: '',
      title: '分组',
      visible: false,
      class_code: '', //班级编号
      idNotDep: true,
      description: '教学计划信息',
      currentDeptId: '',
      sys_org_code: '',
      subject_code: '',
      dataSource: [],
      // 表头
      innerColumns: [
        {
          title: '课程名称',
          align: 'center',
          dataIndex: 'lesson_name',
        },
        {
          title: '仿真课件名称',
          align: 'center',
          dataIndex: 'ware_name',
        },
        {
          title: '课程类别',
          align: 'center',
          dataIndex: 'category',
        },
        // {
        //   title: '课程描述',
        //   align: 'center',
        //   dataIndex: 'lesson_description',
        // },
        {
          title: '教师名称',
          align: 'center',
          dataIndex: 'teacher_name',
        },
        {
          title: '排序',
          align: 'center',
          dataIndex: 'order_num',
        },
        {
          title: '教学模式',
          align: 'center',
          dataIndex: 'teaching_mode',
          scopedSlots: { customRender: 'action1'},
        },
        {
          title: '操作',
          dataIndex: 'action2',
          scopedSlots: { customRender: 'action2' },
          align: 'center',
        },
      ],
      innerdataSource: [],
      columns: [
        {
          title: '计划年份',
          align: 'center',
          dataIndex: 'plan_year',
        },
        {
          title: '学期',
          align: 'center',
          dataIndex: 'plan_term',
        },
        // {
        //   title: '课程名称',
        //   align: 'center',
        //   dataIndex: 'lesson_name',
        // },
        // {
        //   title: '仿真课件名称',
        //   align: 'center',
        //   dataIndex: 'ware_name',
        // },
        // {
        //   title: '课程类别',
        //   align: 'center',
        //   dataIndex: 'category',
        // },
        // {
        //   title: '课程描述',
        //   align: 'center',
        //   dataIndex: 'lesson_description',
        // },
        // {
        //   title: '教师名称',
        //   align: 'center',
        //   dataIndex: 'teacher_name',
        // },
        // {
        //   title: '排序',
        //   align: 'center',
        //   dataIndex: 'order_num',
        // },
        // {
        //   title: '教学模式',
        //   align: 'center',
        //   dataIndex: 'teaching_mode',
        //   scopedSlots: { customRender: 'action1'},
        // },
        // {
        //   title: '进度条',
        //   align: 'center',
        //   dataIndex: 'progress',
        // },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 180,
        },
      ],
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['5', '10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: false,
        onChange: this.onPageChange.bind(this),
        showSizeChanger: false,
        total: 0,
      },
      url: {
        search: '/online/cgform/api/getData/d6a24fd82eb145d6870a43c5055b546c',
        delete: '/online/cgform/api/form/d6a24fd82eb145d6870a43c5055b546c',
        detail_search: '/online/cgform/api/form/d6a24fd82eb145d6870a43c5055b546c',
      },
      planUrl: {
        edit: '/online/cgform/api/form/d6a24fd82eb145d6870a43c5055b546c',
        searchUrl: '/online/cgform/api/getData/a0a23a86799d43f5b019d3cadf2261a8',
        insert: '/manager/startPlan',
        deleteUrl: '/online/cgform/api/form/a0a23a86799d43f5b019d3cadf2261a8/',
        finishUrl: '/manager/endCourse',
      },
      record: {},
      activeKey: '',
    }
  },
  methods: {
    //中止学生课程
    suspengCourse(record) {
      this.$refs.endCourse.openEndCourse(record,this.class_code)
    },
    //查看学习统计
    viewStudyStatistics(record) {
      this.$refs.StudyStatisticsModal.openStudyStatistics(record,this.class_code)
    },
    //查看分组情况
    viewGroup(record) {
      this.$refs.viewGroupModal.openViewModal(record)
    },
    //导出分组excel表
    downloadGroupExcel(record) {
      let params = {
        lessonId: record.lesson_id,
        planId: record.plan_id,
      }
      axios({
        url:  api.server_url + api.global_edu_baseURL + '/eduStudentGroupDetail/downloadGroup',
        params: params,
        responseType: 'blob', // 表明返回服务器返回的数据类型)
      }).then( res => {
        let blob = new Blob([res.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        // let fileName = '测试报告.xlsx'
        let fileName = record.lesson_name +  '.xlsx'
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          var link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          //释放内存
          window.URL.revokeObjectURL(link.href)
        }
      })
    },
    //手动分组
    manualGroup(record) {
      this.$refs.startGroupModal.openGroupModal(record,this.class_code)
    },
    //自动分组
    automaticGroup(record) {
      console.log('record',record)
      axios({
        url: api.server_url + api.global_course_baseURL + `/business/getNumOfProcess/${record.ware_id}`,
        methods: 'get',
      }).then(res => {
        this.title = '每组最多' +  res.data.result.num + '人'
      })
      this.groupLessonId = record.lesson_id
      this.groupPlanId = record.plan_id
      this.visible = true
    },
    //开启分组对话框
    // startGroup(record) {
    //   // this.$refs.startGroupModal.openGroupModal(record,this.class_code)
    //   console.log('record',record)
    //   this.groupLessonId = record.lesson_id
    //   this.groupPlanId = record.plan_id
    //   this.visible = true
    // },
    //分组确认
    handleOk() {
      let params = {
        lessonId: this.groupLessonId,
        planId: this.groupPlanId,
        classCode: this.class_code,
        groupNums: this.groupNumber
      }
      e_postAction('/eduStudentGroupDetail/autoDivideGroups',params)
      .then( res => {
        console.log('res',res)
        if(res.success) {
          this.$message.success('分组成功！')
          this.loadData()
        }
        else {
          this.$message.error('分组失败！')
        }
      })
      this.visible = false
    },
    //分组取消
    handleCancel() {
      this.visible = false
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1)
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    loadData(arg, activeKey) {

      let that = this
      this.dataSource = []
      this.ipagination.total = 0
      let dataStaging = []
      if (!this.departIdList || !this.departIdList.length) {
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      let params = {} //查询条件
      params.superQueryMatchType = 'and'
      params.pageNo = this.ipagination.current
      params.pageSize = this.ipagination.pageSize
      params.order = 'desc'
      let superQueryParams = []
      console.log("this.record:",this.record)
      if (this.record.type == 'class') {
        superQueryParams.push({
          rule: 'eq',
          type: 'text',
          val: this.record.id,
          field: 'class_code',
        })
      } else if (this.record.type == 'profess') {
        superQueryParams.push({
          rule: 'eq',
          type: 'text',
          val: this.record.id,
          field: 'subject_code',
        })
      } else if (this.record.id) {
        superQueryParams.push({
          rule: 'eq',
          type: 'text',
          val: this.record.id,
          field: 'sys_org_code',
        })
      } else {
        for (let i = 0; i < this.departIdList.length; i++) {
          superQueryParams.push({
            rule: 'eq',
            type: 'text',
            val: this.departIdList[i],
            field: 'class_code',
          })
        }
      }
      if (activeKey) {
        superQueryParams.push({ rule: 'eq', type: 'text', val: activeKey, field: 'is_start' })
      } else {
        superQueryParams.push({ rule: 'eq', type: 'text', val: this.activeKey, field: 'is_start' })
      }
      if (superQueryParams.length) {
        params.superQueryParams = escape(JSON.stringify(superQueryParams))
      }

      // params.depId = this.currentDeptId;

      // console.log('查询查询', superQueryParams, this.url.search)

      getAction(this.url.search, params).then((res) => {
        that.ipagination.total = res.result.total
        if (res.success && res.result) {
          const { records } = res.result
          //通过id获取对应的详情
          that.getDetail(records)

          if (records.length) {
            that.class_code = records[0].class_code


            // 解决遍历数组调用接口后push乱序问题
            records.forEach((item, index) => {

              dataStaging.push(new Promise((resolve , reject) => {
                
                getAction(this.url.detail_search + '/' + item.id).then((ress) => {
                  return resolve(ress.result)
                }).catch(err => {
                  return reject(err)
                })
              }))


              // getAction(this.url.detail_search + '/' + item.id).then((ress) => {
              //   // console.log('res11111111111' , ress.result)
              //   console.log('res', dataStaging)
              //   that.dataSource.push(ress.result)
              //   // let org = {}
              //   // if (ress.success && ress.result) {
              //   //   const { result } = ress

              //   //   if (result.edu_plan_detail && result.edu_plan_detail.length) {
              //   //     result.edu_plan_detail.sort((val1, val2) => {
              //   //       if (val1.order_num && val2.order_num) {
              //   //         return val1.order_num < val2.order_num ? -1 : 1
              //   //       } else {
              //   //         return 0
              //   //       }
              //   //     })
              //   //   }
              //   //   // 通过id获取对应的详情
              //   //   item = Object.assign(item, result, {
              //   //     children: result.edu_plan_detail,
              //   //     key: item.id,
              //   //   })
              //   //   // this.$set(item, 'key', Math.round(Math.random()*100))
              //   //   this.$delete(item, 'edu_plan_detail')
              //   //   item.children.forEach((item, index) => {
              //   //     item.key = item.id
              //   //   })
              //   //   // console.log('org', item)
              //   //   // that.dataSource.push(org)
              //   // }
              // })
            })

            Promise.all(dataStaging).then(res => {
              this.dataSource = res
            }).catch(() =>{
              this.dataSource = []
            })
          }
        }
      })
    },
    getDetail(data) {
      let that = this
      let params = {} //查询条件
      if (data.length > 0) {
      }
    },
    batchDel: function () {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！')
        return
      } else {
        var ids = ''
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ','
        }
        var that = this
        this.$confirm({
          title: '确认取消',
          content: '是否取消用户与选中部门的关联?',
          onOk: function () {
            deleteAction(that.url.deleteBatch, { depId: that.currentDeptId, userIds: ids }).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.loadData()
                // that.onClearSelected();
              } else {
                that.$message.warning(res.message)
              }
            })
          },
        })
      }
    },
    handleDelete: function (record) {
      var that = this
      // console.log('删除数据',formData)
      console.log('record', record)

      deleteAction(`${that.url.delete}/${record}`).then((res) => {
        if (res.success) {
          that.loadData()
        } else {
          that.$message.warning(res.message)
        }
      })
    },
    //开启教学计划
    handleGo(record) {
      this.$refs.planForm.visible = true
      this.$refs.planForm.record = record
      this.$refs.planForm.loadWareContent(record)
    },
    open(record) {
      //是机构
      if (record.type != 'class') {
        this.idNotDep = true
      } else {
        // 班级
        this.idNotDep = false
      }
      console.log('部门班级信息', record)
      this.record = record
      this.currentDeptId = record.id
      this.loadData(1)
    },
    clearList() {
      this.currentDeptId = ''
      this.dataSource = []
    },
    hasSelectDept() {
      if (this.currentDeptId == '') {
        this.$message.error('请选择一个部门!')
        return false
      }
      return true
    },
    handleAddUserDepart() {
      if (this.currentDeptId == '') {
        this.$message.error('请选择一个部门!')
      } else {
        this.$refs.SelectClassModal.visible = true
      }
    },
    handleEdit: function (record) {
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.disableSubmit = false
      this.$refs.modalForm.edit(record, true)
    },
    handleAdd: function () {
      this.$refs.modalForm.departDisabled = true
      this.$refs.modalForm.dataSource = []
      this.$refs.modalForm.add(this.record)
      this.$refs.modalForm.title = '新增'
    },
    selectOK(data) {
      let params = {}
      params.depId = this.currentDeptId
      params.userIdList = []
      for (var a = 0; a < data.length; a++) {
        params.userIdList.push(data[a])
      }
      console.log("selectOK"+params)
      postAction(this.url.edit, params).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.loadData()
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    handleDeptRole(record) {
      if (this.currentDeptId != '') {
        this.$refs.deptRoleUser.add(record, this.currentDeptId)
        this.$refs.deptRoleUser.title = '部门角色分配'
      } else {
        this.$message.warning('请先选择一个部门!')
      }
    },
    close() {
      this.loadData(1, this.activeKey)
    },
    callback(activeKey) {
      this.activeKey = activeKey
      this.loadData(1, this.activeKey)
    },
    handleOver(record) {
      e_putAction(this.planUrl.finishUrl, qs.stringify({ planId: record.id })).then((res) => {
        if (res.success) {
          this.$message.success('成功完成教学计划')
          let searchParams = {
            column: 'createTime',
            order: 'desc',
            pageNo: 1,
            pageSize: 10,
            superQueryMatchType: 'and',
          }
          searchParams.superQueryParams = escape(
            JSON.stringify([{ rule: 'eq', type: 'text', val: record.id, field: 'plan_id' }])
          )
          getAction(this.planUrl.searchUrl, searchParams)
            .then((res) => {
              var planId = ''
              res.result.records.forEach((item) => {
                planId = planId + item.id + ','
              })
              deleteAction(this.planUrl.deleteUrl + planId)
                .then((res) => {
                  this.loadData(1)
                })
                .catch((err) => {
                  console.log('err', err)
                })
            })
            .catch((err) => {
              console.log('err', err)
            })
        } else {
          this.$message.error('完成教学计划失败')
        }
      })
    },
  },
}
</script>
<style scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 3px;
}

/* .ant-card {
  margin-left: -30px;
  margin-right: -30px;
} */

.table-page-search-wrapper {
  margin-top: -16px;
  margin-bottom: 16px;
}
</style>