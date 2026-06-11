<template>
  <a-modal :visible="visible" :centered="true" :footer="null" width="1300px" :zIndex="100"
    :dialog-style="{ top: '20px' }" :closable="false" :destroyOnClose="true">
    <div class="buttonstyle"><a-button class="xbutton" type="dashed" @click="close()">X</a-button></div>
    <div style="margin-top:27px"></div>
    <div v-if="approve1">
      <div v-for="(item, index) in formList" :key="index">
        <approve-task :formId="item.prevForm_designer_id" :tableId="item.prevOnline_table_id"
          :dataId="item.prevOnline_data_id"></approve-task>
      </div>
      <div style="margin-top: 20px">
        <approve-new-task @close-approveTask="close" :formId="nowformId" :taskId="taskId" :record="currentRecord">
        </approve-new-task>
      </div>
    </div>

    <div v-if="approve2">
      <div v-for="(item, index) in rollFormList" :key="index">
        <approve-task :formId="item.prevForm_designer_id" :tableId="item.prevOnline_table_id"
          :dataId="item.prevOnline_data_id"></approve-task>
      </div>
      <div style="margin-top: 20px" v-model="rollbackForm">
        <rollback-task @close-approveRollTask="close" :formId="rollbackForm.prevForm_designer_id"
          :tableId="rollbackForm.prevOnline_table_id" :dataId="rollbackForm.prevOnline_data_id" :taskId="taskId"
          :processName="processName"></rollback-task>
      </div>
      <div style="margin-top: 20px" v-model="lastForm">
        <approve-task :formId="lastForm.prevForm_designer_id" :tableId="lastForm.prevOnline_table_id"
          :dataId="lastForm.prevOnline_data_id"></approve-task>
      </div>
    </div>
  </a-modal>

</template>
<script>
import { nw_postAction, nw_postAction1, nw_getAction } from '@api/newWorkApi'
import ApproveTask from './approveTask'
import ApproveNewTask from './approveNewTask'
import RollbackTask from './rollbackTask'
export default {
  name: 'approveModel',
  components: { ApproveTask, ApproveNewTask, RollbackTask },
  data() {
    return {
      approve1: false,
      approve2: false,
      visible: false,
      formList: [],
      formId: '',
      taskId: '',
      processName: '',
      nowformId: '',
      rollFormList: [],
      rollbackForm: {},
      lastForm: {},
      currentRecord: null,
    }
  },
  mounted() { },
  created() { },
  methods: {
    close() {
      this.visible = false
      this.approve1 = false
      this.approve2 = false
      this.currentRecord = null
      this.$emit('close')
    },
    //处理非退回任务
    announceTask(record) {
      console.log('处理', record);
      this.taskId = record.taskId
      this.currentRecord = record
      nw_postAction1(`/task/handleTask`, { taskId: record.taskId, processInstanceId: record.processInstanceId })
        .then((res) => {
          console.log(res.result.nowIds) //a036af9c58f04101b57c5b66893e47b0--
          this.nowformId = res.result.nowIds.substring(0, 32)
          console.log(this.nowformId)
          console.log(res.result.oldIdsList)
          var oldIdsList = res.result.oldIdsList
          var arrayForm = new Array()
          for (var i = oldIdsList.length - 1; i >= 0; i--) {
            var formObj = {}
            formObj['prevForm_designer_id'] = oldIdsList[i].substring(0, 32)
            formObj['prevOnline_table_id'] = oldIdsList[i].substring(33, 65)
            formObj['prevOnline_data_id'] = oldIdsList[i].substring(66, 87)
            arrayForm.push(formObj)
          }
          console.log(arrayForm)
          this.formList = arrayForm
          this.approve1 = true
          this.visible = true
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //处理退回任务
    announceRollTask(record) {
      console.log('退回')
      console.log('record', record);
      this.taskId = record.taskId
      this.processName = record.processName
      nw_postAction1(`/task/handleTask`, { taskId: record.taskId, processInstanceId: record.processInstanceId })
        .then((res) => {
          var oldIdsList = res.result.oldIdsList
          var arrayForm = new Array() //展示的
          var rollForm = {} //不仅展示还要修改
          var lastForm = {}
          for (var i = oldIdsList.length - 1; i >= 0; i--) {
            if (i > 1) {
              var formObj = {}
              formObj['prevForm_designer_id'] = oldIdsList[i].substring(0, 32)
              formObj['prevOnline_table_id'] = oldIdsList[i].substring(33, 65)
              formObj['prevOnline_data_id'] = oldIdsList[i].substring(66, 87)
              arrayForm.push(formObj)
            } else if (i == 1) {//要修改的表单内容
              rollForm['prevForm_designer_id'] = oldIdsList[i].substring(0, 32)
              rollForm['prevOnline_table_id'] = oldIdsList[i].substring(33, 65)
              rollForm['prevOnline_data_id'] = oldIdsList[i].substring(66, 87)
            } else {//最后一个表单内容
              lastForm['prevForm_designer_id'] = oldIdsList[i].substring(0, 32)
              lastForm['prevOnline_table_id'] = oldIdsList[i].substring(33, 65)
              lastForm['prevOnline_data_id'] = oldIdsList[i].substring(66, 87)
            }
          }
          this.rollbackForm = rollForm
          this.rollFormList = arrayForm
          this.lastForm = lastForm
          this.approve2 = true
          this.visible = true
        })
        .catch((error) => {
          console.log(error)
        })
    },
  },
}
</script>
<style lang="scss" scoped>
.buttonstyle {
  position: absolute;
  top: 0;
  right: 0;
  margin: 10px;
}
</style>