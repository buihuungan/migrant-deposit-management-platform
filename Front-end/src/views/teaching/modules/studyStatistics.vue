<template>
<div>
  <a-modal 
    :title="title" 
    :visible="visible" 
    :confirm-loading="confirmLoading" 
    @cancel="handleCancel" 
    :destroyOnClose="true"
    width="50%"
  >
    <template #footer>
      <a-button key="back" @click="handleCancel">取消</a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleOk">导出分数excel表</a-button>
    </template>
    <a-table 
      :dataSource="dataSource" 
      :columns="columns" 
      :pagination="false" 
      size="middle"
    >
      <sapn slot="action" slot-scope="text,record">
        <a @click="detailAndGrade(record)">详情及打分</a>
        <!-- <a-divider type="vertical" />
        <a>打分</a> -->
      </sapn>
    </a-table>
  </a-modal>
  <a-modal 
    :title="detailTitle" 
    :visible="detailVisible" 
    :confirm-loading="confirmLoading" 
    @ok="handleOk2" 
    @cancel="handleCancel2" 
    :destroyOnClose="true"
  >
    <a-table
      :dataSource="dataSource2" 
      :columns="columns2"
      :pagination="false" 
    >
    </a-table>
    <a-form-item label="打分">
      <a-input v-model="grade"  placeholder="请输入分数" />
    </a-form-item>
  </a-modal>
</div>
</template>

<script>
import axios from 'axios'
import api from '@/api/index'
export default {
  data() {
    return {
      classCode: '',
      studentRecord: {},
      record: {},
      grade: '',
      title: '学习统计',
      detailTitle: '学习详情及打分',
      visible: false,
      detailVisible: false,
      confirmLoading: false,
      dataSource: [],
      columns: [
        {
          title: '姓名',
          align: 'center',
          dataIndex: 'userName',
        },
        {
          title: '分数',
          align: 'center',
          dataIndex: 'credit',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'}
        }
      ],
      dataSource2: [],
      columns2: [
        {
          title: '角色名',
          align: 'center',
          dataIndex: 'role_name',
        },
        {
          title: '完成次数',
          align: 'center',
          dataIndex: 'finish_num',
        },
      ]
    }
  },
  methods:{
    detailAndGrade(record) {
      console.log('record',record)
      this.studentRecord = record
      this.grade = record.credit
      var params = {
        planId: record.planId,
        userId: record.userId,
        lessonId: record.lessonId,
        roleId: '',
      }
      axios({
        url: api.server_url + api.global_edu_baseURL + '/eduStatistics/getStatisticsNum',
        method: 'post',
        data: params
      }).then( res => {
        console.log('res',res)
        this.dataSource2 = res.data.result.map
      })
      this.detailVisible = true
    },
    openStudyStatistics(record,class_code) {
      console.log('record',record)
      this.record = record
      this.classCode = class_code
      var params = {
        planId: this.record.plan_id,
        lessonId: this.record.lesson_id,
        classCode: class_code,
      }
      axios({
        url: api.server_url + api.global_edu_baseURL + '/EduCreditStatistics/getCredit',
        method: 'post',
        params: params
      }).then( res => {
        console.log('res',res)
        this.dataSource = res.data.result.hasRecord
      })
      this.visible = true
    },
    handleOk() {
      let params = {
        planId: this.record.plan_id,
        lessonId: this.record.lesson_id,
        classCode: this.classCode
      }
      axios({
        url:  api.server_url + api.global_edu_baseURL + '/EduCreditStatistics/downloadCredit',
        params: params,
        responseType: 'blob', // 表明返回服务器返回的数据类型)
        method: 'post'
      }).then( res => {
        console.log('res',res)
        let blob = new Blob([res.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        // let fileName = '测试报告.xlsx'
        let fileName = this.record.lesson_name +  '分数统计表.xlsx'
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
      // this.visible = false
    },
    handleCancel() {
      this.visible = false
    },
    handleOk2() {
      if(this.grade!= '')
      {
        var params = {
          planId: this.studentRecord.planId,
          lessonId: this.studentRecord.lessonId,
          userId: this.studentRecord.userId,
          credit: ''
        }
        params.credit = this.grade
        console.log('params',params)
        axios({
          url: api.server_url + api.global_edu_baseURL + '/EduCreditStatistics/makeGrade',
          method: 'post',
          params: params
        }).then( res => {
          console.log('resGrade',res)
          this.detailVisible = false
          this.openStudyStatistics(this.record,this.classCode)
        })
      }
      else {
        this.$message.warning('请完成打分！')
      }
    },
    handleCancel2() {
      this.detailVisible = false
    },
  }
}
</script>

<style>

</style>