<template>
  <a-modal 
    :title="title" 
    :visible="visible" 
    :confirm-loading="confirmLoading" 
    @ok="handleOk" @cancel="handleCancel" 
    :destroyOnClose="true"
    width="50%"
  >
    <a-table :dataSource="dataSource" :columns="columns" :pagination="false" size="middle">
    </a-table>
  </a-modal>
</template>

<script>
import api from '@/api/index'
import axios from 'axios'
export default {
  data() {
    return {
      title: '分组情况',
      visible: false,
      confirmLoading: false,
      dataSource: [],
      columns: [
        {
          title: '组号',
          align: 'center',
          dataIndex: 'groupId',
        },
        {
          title: '组人数',
          align: 'center',
          dataIndex: 'groupNum',
        },
        {
          title: '组员',
          align: 'center',
          dataIndex: 'member',
        },
      ],
    }
  },
  methods: {
    openViewModal(record) {
      console.log('record',record)
      this.visible = true 
      var params = {
        lessonId: record.lesson_id,
        planId: record.plan_id,
      }
      axios({
        url: api.server_url + api.global_edu_baseURL + '/eduStudentGroupDetail/groupInformation',
        methods: 'get',
        params: params
      }).then( res =>{
        console.log('res',res)
        res.data.result.groupInformation.forEach( (item,index) => {
          let data = {
            groupId: '',
            groupNum: '',
            member: '',
          }
          data.groupId = index + 1
          data.groupNum = item.name.length
          for(let i=0;i<item.name.length;i++) {
            data.member = data.member + item.name[i].name +','
          }
          data.member.substring(0,data.member.length-1)
          this.dataSource.push(data)
        });
      })
    },
    handleOk() {
      this.dataSource = []
      this.visible = false
    },
    handleCancel() {
      this.dataSource = []
      this.visible = false
    },
  },
}
</script>

<style></style>
