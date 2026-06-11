<template>
  <a-modal
    :visible="visible"
    width="80%"
    :zIndex="100"
    :closable="true"
    :centered="true"
    @ok="handleOk"
    okText="开启计划"
    @cancel="handleCancel"
  >
    <a-table style="margin: 20px" :columns="columns" :data-source="dataSource" :pagination="false" />
  </a-modal>
</template>

<script>
import { getAction, putAction } from '@/api/manage'
import { e_postAction } from '@/api/eduApi'
import axios from 'axios'
import api from '@/api/index'
export default {
  data() {
    return {
      visible: false,
      columns: [
        {
          title: '课件名称',
          dataIndex: 'lesson_name',
          key: 'lesson_name',
        },
        {
          title: '课件描述',
          dataIndex: 'lesson_description',
          key: 'lesson_description',
        },
      ],
      dataSource: [],
      record: {},
      planUrl: {
        edit: '/online/cgform/api/form/d6a24fd82eb145d6870a43c5055b546c',
        insert: '/manager/startPlan',
      },
      wareUrl: {
        search: '/online/cgform/api/form/53349c93563748bf9bac7eec1ea09d3f',
      },
    }
  },
  methods: {
    loadWareContent(record) {
      console.log('record',record)
      let queryList = []
      for (let index = 0; index < record.edu_plan_detail.length; index++) {
        queryList.push(this.queryWare(record.edu_plan_detail[index].lesson_id))
      }
      Promise.all(queryList).then((res) => {
        res.forEach((element) => {
          this.dataSource.push({
            lesson_name: element.result.lesson_name,
            lesson_description: element.result.lesson_description,
          })
        })
      })
    },
    queryWare(lesson_id) {
      return getAction(this.wareUrl.search + `/${lesson_id}`)
    },
    handleCancel() {
      this.visible = false
      this.dataSource = []
    },
    handleOk() {
      let queryParam = {}
      queryParam.planId = this.record.id
      queryParam.classId = this.record.class_code
      e_postAction(this.planUrl.insert, queryParam).then((res) => {
        if (res.success) {
          this.$message.success('成功开启教学计划')
          for(let i=0;i<this.record.edu_plan_detail.length;i++) {
            var params = {
              planId: this.record.edu_plan_detail[i].plan_id,
              lessonId: this.record.edu_plan_detail[i].lesson_id,
              classCode: this.record.class_code
          }
          axios({
            url: api.server_url + api.global_edu_baseURL + '/EduCreditStatistics/newLessonMember',
            method: 'post',
            params:params
          }).then( res=> {

          })
          }
          
          this.$emit('ok')
          this.visible = false
        } else {
          this.$message.error('开启教学计划失败')
        }
      })
    },
  },
}
</script>

<style>
</style>