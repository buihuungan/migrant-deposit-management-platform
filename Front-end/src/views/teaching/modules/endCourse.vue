<template>
  <a-modal
    :title="title"
    size="middle"
    :visible="visible"
    @cancel="handleCancel"
    @ok="handleOK">
      <a-table
      :dataSource="dataSource"
      :columns="columns"
      :row-selection="{selectedRowKeys: selectedRowKeys,onChange:onSelectChange}"
      rowKey="uid">
      </a-table>
  </a-modal>
</template>

<script>
import {e_getAction,} from '@/api/eduApi'
import { ce_getWares2 } from '@api/coursewareEngineApi'
import { Message } from 'element-ui'
export default {
  data() {
    return {
      title: '中止学生课程进度',
      visible: false,
      class_code: '',
      dataSource: [],
      selectedRowKeys: [],
      columns: [
        {
          title: '姓名',
          align: 'center',
          dataIndex: 'studentName'
        }

      ],
      record: {},
    }
  },
  methods: {
    openEndCourse(record,class_code) {
      this.record = record
      this.class_code = class_code
      let params = {
        classCode: this.class_code
      }
      e_getAction('/eduStudent/selectAllStudent',params)
      .then(res => {
        this.dataSource = res.result.list
        this.visible = true
      })
    },
    handleCancel() {
      this.selectedRowKeys = []
      this.visible = false
    },
    handleOK() {
      console.log('this.record',this.record)
      let params = {
        planId: this.record.plan_id,
        lessonId: this.record.lesson_id,
        wareName: this.record.ware_name,
        teachingMode: this.record.teaching_mode,
        loginUserList: this.selectedRowKeys
      }
      ce_getWares2('/business/shutDownTask/byTeacher',params)
      .then(res => {
        if(res.success) {
          Message({
            showClose: true,
            message: res.message,
            type: 'success',
          })
          this.selectedRowKeys = []
          this.visible = false
        }
      })
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
  }
}
</script>

<style>

</style>