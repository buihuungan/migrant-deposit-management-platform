<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    :width="drawerWidth"
    height="80%"
    :destroyOnClose="true"
    placement="bottom"
    :closable="true"
    @close="handleCancel"
    :visible="visible"
    style="padding-bottom: 53px"
  >
    <template slot="title">
      <div style="width: 100%">
        <span>{{ title }}</span>
        <span style="display: inline-block; width: calc(100% - 51px); padding-right: 10px; text-align: right">
          <a-button @click="toggleScreen" icon="appstore" style="height: 20px; width: 20px; border: 0px"></a-button>
        </span>
      </div>
    </template>

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="计划年份" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-date-picker
            style="width: 100%"
            mode="year"
            placeholder="请选择计划年份"
            :open="dateopen"
            format="YYYY"
            v-decorator="['plan_year', { initialValue: !model.plan_year ? null : moment(model.plan_year, dateFormat) }]"
            @openChange="openChangeYear"
            @panelChange="panelChangeYear"
            :getCalendarContainer="(node) => node.parentNode"
          />
        </a-form-item>

        <a-form-item label="学期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select
            v-decorator="['plan_term', {}]"
            placeholder="请选择学期"
            :getPopupContainer="(target) => target.parentNode"
          >
            <a-select-option :value="1">1</a-select-option>
            <a-select-option :value="2">2</a-select-option>
            <a-select-option :value="3">3</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <a-button @click="handleAdd" type="primary" icon="plus" style="margin-top: 16px; margin-bottom: 10px"
        >新增</a-button
      >
      <el-table size="mini" :data="dataSource" border>
        <el-table-column align="center" type="index" width="50" :resizable="false"></el-table-column>

        <el-table-column align="center" prop="lesson_name" label="课程名称" :resizable="false">
          <template slot-scope="scope">
            <j-select-lesson
              @getobject="getSelectedObject(scope.$index, arguments[0])"
              :value="scope.row.lesson_name"
              placeholder="请选择课程"
              type="lesson"
              url="/online/cgform/api/getData/53349c93563748bf9bac7eec1ea09d3f"
              :multiple="false"
              :sys_org_code="sys_org_code"
              v-decorator="['lesson_id', {}]"
            /> </template
        ></el-table-column>
        <el-table-column align="center" prop="ware_name" label="仿真课件名称" :resizable="false">
          <template slot-scope="scope">
            <j-select-ware
              @getobject="getSelectedObject(scope.$index, arguments[0])"
              :value="scope.row.ware_name"
              ischildtable="edu_lessons_ware"
              placeholder="请选择仿真课件"
              :searchobj="selectedLesson[scope.$index]"
              type="bean"
              url="/online/cgform/api/form/53349c93563748bf9bac7eec1ea09d3f"
              :multiple="false"
              :disabled="!selectedLesson[scope.$index]"
              v-decorator="['ware_id', {}]" /></template
        ></el-table-column>
         <el-table-column align="center" prop="lesson_name" label="课程类别" :resizable="false">
          <template slot-scope="scope">
            <el-input
              placeholder="课程类别"
              :value="scope.row.category"
              :disabled="true">
            </el-input>
          </template
        ></el-table-column>
        <el-table-column align="center" prop="lesson_name" label="课程描述" :resizable="false">
          <template slot-scope="scope">
            <el-input
              placeholder="课程描述"
              :value="scope.row.lesson_description"
              :disabled="true">
            </el-input>
          </template
        ></el-table-column>
        <el-table-column align="center" prop="teacher_name" label="教师姓名" :resizable="false">
          <template slot-scope="scope">
            <j-select-teacher
              @getobject="getSelectedObject(scope.$index, arguments[0])"
              :value="scope.row.teacher_name"
              :searchobj="selectedLesson[scope.$index]"
              ischildtable="edu_lessons_teacher"
              placeholder="请选择授课教师"
              type="teacher_lesson"
              url="/online/cgform/api/form/53349c93563748bf9bac7eec1ea09d3f"
              :multiple="false"
              :disabled="!selectedLesson[scope.$index]"
              v-decorator="['teacher_id', {}]" /></template
        ></el-table-column>
        <!-- <el-table-column align="center" prop="progress" label="进度条" :resizable="false">
          <template slot-scope="scope">
            <el-progress :percentage="formatPercentage(scope.row.progress)"></el-progress>
          </template>
        </el-table-column> -->
        <el-table-column align="center" prop="teaching_mode" label="教学模式" :resizable="false">
          <template slot-scope="scope">
            <el-select v-model="scope.row.teaching_mode" size="mini" @change="selectChangeBef()">
              <el-option v-for="item in teaching_mode_select" :label="item.label" :key="item.value" :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="order_num" label="排序"  width="80" :resizable="false">
          <template slot-scope="scope">
            <a-input-number v-model="scope.row.order_num" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="80" :resizable="false">
          <template slot-scope="scope">
            <i
              style="font-size: 30px; color: #aeaeae"
              class="el-icon-remove-outline"
              @click="Deletecontcat(scope.$index)"
            ></i>
          </template> </el-table-column
      ></el-table>
    </a-spin>

    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: 0.8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="confirmLoading">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import pick from 'lodash.pick'
import moment from 'moment'
import Vue from 'vue'

// 引入弹出框的组件
import JSelectLesson from '@/components/jeecgbiz/JSelectLesson'
import JSelectWare from '@/components/jeecgbiz/JSelectWare'
import JSelectTeacher from '@/components/jeecgbiz/JSelectTeacher'

import { ACCESS_TOKEN } from '@/store/mutation-types'
import { getAction } from '@/api/manage'

import { duplicateCheck } from '@/api/api'
import JImageUpload from '../../../components/jeecg/JImageUpload'
import { postAction, putAction } from '../../../api/manage'
import Template1 from '../../jeecg/JVxeDemo/layout-demo/Template1.vue'

export default {
  name: 'UserModal',
  components: {
    JSelectLesson,
    JSelectWare,
    JSelectTeacher
  },
  props: {
    roleStudentId: '',
    isStudent: true,
    roleTeacherId: '',
  },
  data() {
    return {
      teaching_mode: [],
      teaching_mode_select: [
        {
          value: '1',
          label: '单人模式'
        },
        {
          value: '2',
          label: '团队模式'
        }
      ],
      class_code: '',
      year: moment(),
      dateopen: false, // 默认是否打开弹框
      title: '操作',
      visible: false,
      model: {},
      lessonList: [],

      departDisabled: false, //是否是我的部门调用该页面
      roleDisabled: true, //是否是角色维护调用该页面
      modalWidth: 800,
      drawerWidth: 1200,
      modaltoggleFlag: true,
      confirmDirty: false,

      disableSubmit: false,
      dateFormat: 'YYYY',
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      dataSource: [],
      // disabled: false,
      confirmLoading: false,
      headers: {},
      form: this.$form.createForm(this),
      records: {},
      selectNode: {},
      submitUrl: {
        url: '/online/cgform/api/form/d6a24fd82eb145d6870a43c5055b546c',
      },
      isEdit: true,
      selectedLesson: [],
      sys_org_code: '',
    }
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN)
    this.headers = { 'X-Access-Token': token }
  },
  methods: {
    //选择教学模式 1代表单人模式 2代表多人模式
    selectChangeBef(value) {
      // this.dataSource[index].teaching_mode = value
      // console.log('ssssssssssssss',this.dataSource)
    },
    //获取组件传回来的数据
    getSelectedObject(index, val) {
      const { ctype, obj } = val
      this.model[ctype] = obj
      //这个地方的ctype 是上面组件传的参数        type="lesson" type="bean" type="teacher_lesson"
      console.log("ctype:"+ctype+"，obj:"+obj)
      if (ctype == 'lesson') {
        if (obj.length) {
          // this.selectedLesson[index] = {
          //   key:'lesson_id',
          //   val:obj[0].id
          // }
          this.$set(this.selectedLesson, index, obj[0].id)
        } else {
          this.selectedLesson[index] = ''

          this.$set(this.dataSource[index], 'lesson_id', '')
          this.$set(this.dataSource[index], 'lesson_name', '')
          this.$set(this.dataSource[index], 'order_num', index)

          this.$set(this.dataSource[index], 'ware_id', '')
          this.$set(this.dataSource[index], 'ware_name', '')
          this.$delete(this.model, 'bean')

          this.$set(this.dataSource[index], 'teacher_id', '')
          this.$set(this.dataSource[index], 'teacher_name', '')
          this.$set(this.dataSource[index], 'teacher_code', '')
          this.$delete(this.model, 'teacher_lesson')

        }
      }
      if (ctype == 'lesson') {
        if (!this.model.lesson.length) {
          this.$set(this.dataSource[index], 'lesson_id', '')
          this.$set(this.dataSource[index], 'lesson_name', '')
          this.$set(this.dataSource[index], 'category', '')
          this.$set(this.dataSource[index], 'lesson_description', '')
          this.$set(this.dataSource[index], 'order_num', index)
        } else {
          this.$set(this.dataSource[index], 'lesson_id', this.model.lesson[0].id)
          this.$set(this.dataSource[index], 'lesson_name', this.model.lesson[0].lesson_name)
          this.$set(this.dataSource[index], 'category', this.model.lesson[0].category)
          this.$set(this.dataSource[index], 'lesson_description', this.model.lesson[0].lesson_description)
          this.$set(this.dataSource[index], 'order_num', index)
        }

        this.$delete(this.model, 'lesson')
      } else if (ctype == 'bean') {
        if (this.model.bean.length) {
          this.$set(this.dataSource[index], 'ware_id', this.model.bean[0].ware_id)
          this.$set(this.dataSource[index], 'ware_name', this.model.bean[0].ware_name)
        } else {
          this.$set(this.dataSource[index], 'ware_id', '')
          this.$set(this.dataSource[index], 'ware_name', '')
        }
        this.$delete(this.model, 'bean')
      } else if (ctype == 'teacher_lesson') {
        if (this.model.teacher_lesson.length) {
          this.$set(this.dataSource[index], 'teacher_id', this.model.teacher_lesson[0].teacher_id)
          this.$set(this.dataSource[index], 'teacher_name', this.model.teacher_lesson[0].teacher_name)
          this.$set(this.dataSource[index], 'teacher_code', this.model.teacher_lesson[0].teacher_work_no)
        } else {
          this.$set(this.dataSource[index], 'teacher_id', '')
          this.$set(this.dataSource[index], 'teacher_name', '')
          this.$set(this.dataSource[index], 'teacher_code', '')
        }
        this.$delete(this.model, 'teacher_lesson')
      }

      // console.log('this.dataSource', this.dataSource)
    },
    // 点击选择框事件 弹出日期组件选择年
    openChangeYear(status) {
      if (status) {
        this.dateopen = true
      } else {
        this.dateopen = false
      }
    },
    panelChangeYear(value) {
      this.model.plan_year = value.format(this.dateFormat)
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'plan_year'))
      })
      this.dateopen = false
    },

    //窗口最大化切换
    toggleScreen() {
      if (this.modaltoggleFlag) {
        this.modalWidth = window.innerWidth
      } else {
        this.modalWidth = 800
      }
      this.modaltoggleFlag = !this.modaltoggleFlag
    },
    refresh() {
      this.selectedDepartKeys = []
      this.checkedDepartKeys = []
      this.checkedDepartNames = []
      this.checkedDepartNameString = ''
      this.userId = ''
      this.resultDepartOptions = []
      this.departId = []
      this.departIdShow = false
      this.currentTenant = []
      this.model.plan_year = ''
    },
    add(records) {
      // console.log('+_----------------------', records)
      this.records = records
      this.refresh()
      this.edit({ activitiSync: '1' }, false)
    },
    edit(record, isEdit) {
      let that = this
      that.resetScreenSize() // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度

      that.isEdit = isEdit
      that.form.resetFields()

      that.userId = record.id
      // console.log('+_----------------------', record)

      if (record.edu_plan_detail && record.edu_plan_detail.length) {
        record.edu_plan_detail.forEach((element, index) => {
          // this.selectedLesson.push(element.lesson_id)
          this.$set(this.selectedLesson, index, element.lesson_id)
        })

        record.edu_plan_detail.sort((val1, val2) => {
          if (val1.order_num && val2.order_num) return val1.order_num < val2.order_num ? -1 : 1
        })
      }

      that.visible = true
      that.dataSource = Object.assign([], record.edu_plan_detail)
      if (isEdit) {
        that.records = record
      }
      console.log()
      that.$nextTick(() => {
        that.form.setFieldsValue(pick(record, 'plan_year', 'plan_term'))
      })
      //console.log('+++++++++++++---',this.dataSource)
    },
    close() {
      this.$emit('close')
      this.visible = false
      this.disableSubmit = false
      this.selectedLesson = []
    },
    moment,
    handleSubmit() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true

          // values.plan_year = values.plan_year ? values.plan_year.format(this.dateFormat) :''

          let formData = Object.assign({}, this.model, values)
          //处理数据
          //班级编号

          formData.class_code = this.records.id ? this.records.id : ''
          formData.subject_code = this.records.subject_code ? this.records.subject_code : ''
          formData.sys_org_code = this.records.sys_org_code ? this.records.sys_org_code : ''
          formData.tenant_id = this.records.tenant_id ? this.records.tenant_id : ''
          formData.edu_plan_detail = this.dataSource
          formData.is_start = '0'
          if (this.isEdit) {
            formData.id = this.records.id
            formData.class_code = this.records.class_code ? this.records.class_code : ''
            putAction(this.submitUrl.url, formData)
              .then((res) => {
                this.confirmLoading = false
                this.visible = false
                // console.log('res', res)
                this.$message.success(res.message)
                this.$emit('close')
              })
              .catch((err) => {
                this.$message.error(err)

                console.log(err)
              })
          } else {
            postAction(this.submitUrl.url, formData)
              .then((res) => {
                this.confirmLoading = false
                this.visible = false
                // console.log('res', res)
                this.$message.success(res.message)

                this.$emit('close')
              })
              .catch((err) => {
                console.log(err)
                this.$message.error(err)
              })
          }
        }
      })
    },
    handleCancel() {
      this.close()
    },
    // 根据屏幕变化,设置抽屉尺寸
    resetScreenSize() {
      let screenWidth = document.body.clientWidth
      if (screenWidth < 500) {
        this.drawerWidth = screenWidth
      } else {
        this.drawerWidth = '60%'
      }
    },
    handleAdd() {
      this.dataSource.push({})
      this.sys_org_code = this.records.sys_org_code
    },
    Deletecontcat(index) {
      this.dataSource.splice(index, 1)
    },
    formatPercentage(p) {
      if(!p){
        p="0"
      }
      p=p.replace("%","")
    }
  },
}
</script>

<style scoped>
.avatar-uploader > .ant-upload {
  width: 104px;
  height: 104px;
}
.ant-upload-select-picture-card i {
  font-size: 49px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}

.ant-table-tbody .ant-table-row td {
  padding-top: 10px;
  padding-bottom: 10px;
}

.drawer-bootom-button {
  position: absolute;
  bottom: -8px;
  width: 100%;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  left: 0;
  background: #fff;
  border-radius: 0 0 2px 2px;
}
.ant-input-number {
  /* margin-left: 15px; */
  width: 60px;
}
</style>