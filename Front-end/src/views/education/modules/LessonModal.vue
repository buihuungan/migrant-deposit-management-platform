<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    :width="drawerWidth"
    placement="right"
    :closable="true"
    @close="handleCancel"
    :visible="visible"
    style="height: 100%; overflow: auto; padding-bottom: 53px"
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
        <a-form-item label="课程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            placeholder="请输入课程名称"
            v-decorator.trim="['lesson_name', validatorRules.lesson_name]"
            :disabled="isDetail"
          />
        </a-form-item>

        <a-form-item label="课程描述" :labelCol="labelCol" :wrapperCol="wrapperCol" :disabled="isDetail">
          <a-textarea
            placeholder="请输入课程描述"
            rows="3"
            v-decorator.trim="['lesson_description']"
            :disabled="isDetail"
          >
          </a-textarea>
          <!-- <a-input  /> -->
        </a-form-item>

        <a-form-item label="选择课程类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select
            placeholder="请选择课程类型"
            style="width: 50%"
            v-decorator.trim="['category',validatorRules.category]"
            :disabled="isDetail"
          >
            <a-select-option v-for="(item, index) in courseType" :key="index" :value="item.ware_domain_name">
              {{ item.ware_domain_name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="上传课程封面" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-upload
            action="/plm/common/upload/"
            list-type="picture-card"
            name="multipartFile"
            :file-list="fileList"
            @preview="handlePreview"
            @change="handleChange"
            v-decorator.trim="['course_cover',validatorRules.course_cover]"
            :disabled="isDetail"
          >
            <div v-if="fileList.length < 1">
              <a-icon type="plus" />
              <div class="ant-upload-text">上传课程封面</div>
            </div>
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handlePreviewCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-form-item>

        <a-form-item label="教师" :labelCol="labelCol" :wrapperCol="wrapperCol" :disabled="isDetail">
          <j-select-teacher
            @getobject="getSelectedObject"
            placeholder="请选择教师"
            type="edu_lessons_teacher"
            url="/online/cgform/api/getData/45e06c958a3b40be83e712cfff43ec68"
            :multiple="true"
            :sys_org_code="record.id"
            v-decorator.trim="['teacher_id']"
            :disabled="isDetail"
          />
        </a-form-item>

        <a-form-item label="仿真课件" :labelCol="labelCol" :wrapperCol="wrapperCol" :disabled="isDetail">
          <a-select
            mode="multiple"
            style="width: 88.5%"
            placeholder="请选择仿真课件"
            @select="wareSelectChanged"
            @deselect="wareDeselectChanged"
            v-decorator="['ware_name']"
            :disabled="isDetail"
          >
            <a-select-option v-for="i in dataSource" :key="i.ware_id">
              {{ i.ware_name }}
            </a-select-option>
          </a-select>
          <!-- <j-select-ware
            @getobject="getSelectedObject"
            placeholder="请选择仿真课件"
            type="edu_lessons_ware"
            url="/online/cgform/api/getData/22f5f24e82444c3db7228f2c2412654d"
            :multiple="true"
            :sys_org_code="record.id"
            v-decorator.trim="['ware_id']"
            :disabled="isDetail"
          /> -->
        </a-form-item>

        <a-form-item label="其它" :labelCol="labelCol" :wrapperCol="wrapperCol" :disabled="isDetail">
          <a-textarea
            placeholder="请输入其它信息"
            rows="3"
            v-decorator.trim="['lesson_other']"
            :disabled="isDetail"
          >
          </a-textarea>
          <!-- <a-input  /> -->
        </a-form-item>
      </a-form>
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
// 引入搜索部门弹出框的组件

import JSelectTeacher from '@/components/jeecgbiz/JSelectTeacher'
import JSelectWare from '@/components/jeecgbiz/JSelectWare'

import moment from 'moment'
import { getAction, postAction, putAction } from '../../../api/manage'
import pick from 'lodash.pick'
import { TENANT_ID } from '@/store/mutation-types'
import Vue from 'vue'
import { o_getAction } from '../../../api/onApi'
import { ce_getWares, ce_getAction } from '@api/coursewareEngineApi'
import api from '@/api/index'

export default {
  name: 'LessonModal',
  components: {
    JSelectTeacher,
    JSelectWare,
  },
  data() {
    return {
      isDetail: false,
      confirmLoading: false,
      modalWidth: 800,
      drawerWidth: 700,
      modaltoggleFlag: true,
      confirmDirty: false,

      disableSubmit: false,
      validatorRules: {
        lesson_name: { rules: [{ required: true, message: '请输入课程名称!' }] },
        category: { rules: [{ required: true, message: '请选择课程类型!' }] },
        // course_cover: { rules: [{ required: true, message: '请上传课程封面!' }] },
      },
      title: '操作',
      visible: false,
      model: {},

      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },

      headers: {},
      form: this.$form.createForm(this),

      params: {},
      record: {},
      isEdit: false,
      url: {
        search: '/online/cgform/api/getData/53349c93563748bf9bac7eec1ea09d3f',
        change: '/online/cgform/api/form/53349c93563748bf9bac7eec1ea09d3f',
        searchTeacherUrl: '/online/cgform/api/getData/45e06c958a3b40be83e712cfff43ec68',
        searchWareUrl: '/online/cgform/api/getData/22f5f24e82444c3db7228f2c2412654d',
        searchCouresUrl: '/cgform/api/getData/1464fd461cf84acfa700ca4b40294b1d',
      },
      courseType: [],
      fileList: [],
      previewVisible: false,
      previewImage: '',
      wareId: '',
      dataSource: [],
    }
  },
  mounted(){
    // this.getWare()
  },
  created() {
    //this.getAllData()
    this.getWare();
  },
  methods: {
    //上传课程图片
    getBase64(file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = () => resolve(reader.result)
        reader.onerror = (error) => reject(error)
      })
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await this.getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    handleChange({ fileList }) {
      console.log('fileList', fileList)
      this.fileList = fileList
    },
    handlePreviewCancel() {
      this.previewVisible = false
    },
    //获取课程类别
    getCouresType() {
      let params = {
        column: 'createTime',
        order: 'desc',
        pageNo: 1,
        pageSize: 20,
        superQueryMatchType: 'and',
      }
      o_getAction(this.url.searchCouresUrl, params)
        .then((res) => {
          this.courseType = res.result.records
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    getWare(){
       var url = `/business/getWares/type`;
      //  if(this.tableType == 'type'){ 
      //    url = `/business/getWares/type`
      //  }else if(this.tableType == 'domain'){
      //    url = `/business/getWares/domain`
      //  }
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
      }
    })
    },
    wareSelectChanged(value) {
      if(!this.model.edu_lessons_ware){
        this.model.edu_lessons_ware=[]
      }
      for(let i=0;i<this.dataSource.length;i++){
        if(value==this.dataSource[i].ware_id){
          this.model.edu_lessons_ware.push(this.dataSource[i])
          break;
        }
      }
    },
    wareDeselectChanged(value) {
      for(let i=0;i<this.model.edu_lessons_ware.length;i++){
        if(value==this.model.edu_lessons_ware[i].ware_id){
          this.model.edu_lessons_ware.splice(i,1)
          break;
        }
      }
    },
    //获取组件传回来的数据
    getSelectedObject(val) {
      //主要用于教师的选择
      //,obj就是所有选中的对象
      const { ctype, obj } = val
      for (let i = 0; i < obj.length; i++) {
        if (typeof obj[i] == 'undefined' || !obj[i]) {
          obj.splice(i, 1)
        }
      }
      this.model[ctype] = obj
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
    add(node) {
      this.record = node
      this.edit({ activitiSync: '1' }, false)
    },
    edit(record, isEdit, isDetail) {
      if (!isEdit) {
        this.getCouresType()
      }
      this.resetScreenSize() // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
      let that = this
      this.isEdit = isEdit

      this.isDetail = isDetail

      that.form.resetFields()

      that.visible = true
      that.model = Object.assign({}, record)
      if (record.teacher_id) {
        let params = { column: 'createTime', order: 'desc', pageNo: 1, pageSize: 10, superQueryMatchType: 'and' }
        params.superQueryParams = escape(
          JSON.stringify([{ rule: 'eq', type: 'text', val: record.teacher_id, field: 'uid' }])
        )
        getAction(this.url.searchTeacherUrl, params)
          .then((res) => {
            that.model.edu_lessons_teacher = res.result.records
          })
          .catch((err) => {
            console.log('err', err)
          })
      }
      //that.model.edu_lessons_ware=that.wareList
      if (record.ware_id) {
        let params = { column: 'createTime', order: 'desc', pageNo: 1, pageSize: 10, superQueryMatchType: 'and' }
        params.superQueryParams = escape(
          JSON.stringify([{ rule: 'eq', type: 'text', val: record.ware_id, field: 'id' }])
        )
       
        // getAction(this.url.searchWareUrl, params)
        //   .then((res) => {
        //     that.model.edu_lessons_ware = res.result.records
        //   })
        //   .catch((err) => {
        //     console.log('err', err)
        //   })
      }
      if (record.course_cover) {
        console.log('record.course_cover', record.course_cover)
        this.fileList = [
          { uid: '-1', name: record.course_cover.split('/')[6], url: record.course_cover, status: 'done' },
        ]
      }
      that.$nextTick(() => {
        that.form.setFieldsValue(
          pick(
            this.model,
            'lesson_id',
            'lesson_name',
            'lesson_description',
            'course_cover',
            'category',
            'teacher_id',
            'ware_id',
            'sys_org_code',
            'tenant_id',
            'teacher_id',
            'teacher_name',
            'ware_name',
            'edu_lessons_teacher',
            'edu_lessons_ware'
          )
        )
      })
    },
    close() {
      this.$emit('close')
      this.fileList = []
      //关闭窗口
      this.visible = false
      this.disableSubmit = false
    },
    moment,
    handleSubmit() {
      const that = this

      // 触发表单验证
      this.form.validateFields((err, values) => {
        console.log('tttttttttttt',values,err)
        if (!err) {
          that.confirmLoading = true

          //构造传入的参数formdata
          let formData = Object.assign(this.model, values)
          // formData.course_cover =
          //   api.server_url+'/plm/common/static/' + formData.course_cover.fileList[0].response.result.fileName
          if (!formData.sys_org_code) {
            formData.org_code = this.record.id
          }
          if (!formData.tenant_id) {
            formData.tenant_id = Vue.ls.get(TENANT_ID) ? Vue.ls.get(TENANT_ID) : ''
          }

          if (formData.edu_lessons_teacher && formData.edu_lessons_teacher.length) {
            formData.edu_lessons_teacher = formData.edu_lessons_teacher.map((item) => {
              return {
                lesson_name: formData.lesson_name,
                teacher_id: item.uid || item.teacher_id,
                teacher_name: item.teacher_name,
                teacher_work_no: item.work_no || item.teacher_work_no,
                org_code: item.org_code || formData.sys_org_code,
                tenant_id: item.tenant_id || formData.tenant_id,
              }
            })
          }
          console.log("formdata")
          console.log(formData)
          if (formData.edu_lessons_ware && formData.edu_lessons_ware.length) {
            formData.edu_lessons_ware = formData.edu_lessons_ware.map((item) => {
              return {
                lesson_name: formData.lesson_name,
                ware_id: item.id || item.ware_id,
                ware_name: item.ware_name,
                org_code: item.org_code || formData.sys_org_code,
                tenant_id: item.tenant_id || formData.tenant_id,
              }
            })
          }
          this.$delete(formData, 'teacher_id')
          this.$delete(formData, 'ware_id')
          //有id的是编辑，没有id的是新建
          if (!this.model.id) {
            postAction(that.url.change, formData).then((res) => {
              if (res.success) {
                that.confirmLoading = false
                that.$message.success(res.message)
                that.$emit('loaddata')
                that.close()
              } else {
                that.$message.error(res.message)
                that.close()
              }
            })
          } else {
            putAction(that.url.change, formData).then((res) => {
              if (res.success) {
                that.confirmLoading = false
                that.$message.success(res.message)
                that.$emit('loaddata')
                that.close()
              } else {
                that.$message.error(res.message)
                that.close()
              }
            })
          }
        } else {
          console.log('err', err)
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
        this.drawerWidth = 700
      }
    },
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
</style>
