<template>
  <a-modal
    :title="title"
    :visible="visible"
    :confirm-loading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    width="80%"
  >
  <div class="dndList-list">
      <h3>所有学生</h3>
      <draggable :list="list" :options="{group:{name: falgs,pull:'clone'},filter: '.undraggable', sort: false}"
                 @end="end"
                 class="dragArea">
        <div v-for="element in list" :key="element.id"
             :class="{undraggable : element.flag}"
             class="list-complete-item">
          <div class="list-complete-item-handle2"> {{element.studentName}}</div>
        </div>
      </draggable>
    </div>
    <div style="width: 100%; height: 10px; background-color: #bfbfbf"></div>
   <div class="dndList">
    <div class="dndList-list">
      <h3>第一组</h3>
      <draggable :list="list1" :options="{group:'article', disabled: disabled}"
                 @start="start22"
                 @end="end22"
                 class="dragArea11"
                 style="height: 100px">
        <div v-for="(element, index) in list1" :key="element.id" class="list-complete-item">
          <div class="list-complete-item-handle">{{element.studentName}}</div>
          <div>
            <i class="el-icon-delete" @click="handleDel(index, element.id)"></i>
          </div>
        </div>
      </draggable>
    </div>
  </div>
  <div style="width: 100%; height: 10px; background-color: #bfbfbf"></div>
  <div class="dndList">
    <div class="dndList-list">
      <h3>第二组</h3>
      <draggable :list="list2" :options="{group:'article', disabled: disabled}"
                 @start="start22"
                 @end="end22"
                 class="dragArea11"
                 style="height: 100px">
        <div v-for="(element, index) in list2" :key="element.id" class="list-complete-item">
          <div class="list-complete-item-handle">{{element.studentName}}</div>
          <div>
            <i class="el-icon-delete" @click="handleDel(index, element.id)"></i>
          </div>
        </div>
      </draggable>
    </div>
  </div>
  <div style="width: 100%; height: 10px; background-color: #bfbfbf"></div>
  <div class="dndList">
    <div class="dndList-list">
      <h3>第三组</h3>
      <draggable :list="list3" :options="{group:'article', disabled: disabled}"
                 @start="start22"
                 @end="end22"
                 class="dragArea11"
                 style="height: 100px">
        <div v-for="(element, index) in list3" :key="element.id" class="list-complete-item">
          <div class="list-complete-item-handle">{{element.studentName}}</div>
          <div>
            <i class="el-icon-delete" @click="handleDel(index, element.id)"></i>
          </div>
        </div>
      </draggable>
    </div>
  </div>
  </a-modal>
</template>

<script>
import draggable from "vuedraggable"
import api from '@/api/index'
import axios from 'axios'
export default {
  components: {draggable},
  data() {
    return {
      lessonInfo: {},
      title: '',
      falgs: 'article',
      disabled: false,
      class_code: '',
      list1: [],
      list2: [],
      list3: [],
      list: [],
      visible: false,
      confirmLoading: false,
      columns:[
        {
          title: '组号',
          dataIndex: 'groupId',
          align: 'center',
          width: '20%'
        },
        {
          title: '组员',
          dataIndex: 'groupMember',
          // align: 'center',
          width: '80%',
          scopedSlots: { customRender: 'action'}
        }
      ],
      group: [
        {
          people: '六合',
          id: '111'
        },
        {
          people: '七八',
          id: '222'
        }
      ],
      dataSource: [
        {
          groupId: '1',
          groupMember: ''
        },
        {
          groupId: '2',
          groupMember: ''
        }
      ]
    }
  },
  methods: {
    handleChange(value) {
    },
    openGroupModal(record,class_code) {
      this.visible = true
      this.lessonInfo = record
      this.class_code = class_code
      this.getStudentInfo()
    },
    getStudentInfo() {
      // 获取班级对应学生信息
      let params = {
        classCode: this.class_code
      }
      axios({
        url: api.server_url + api.global_edu_baseURL + '/eduStudent/selectAllStudent',
        methods: 'get',
        params: params
      }).then(res => {
        console.log('classRes',res)
        this.list = res.data.result.list
      })
      // 获取课件流程信息，确定每组最多人数
      
      axios({
        url: api.server_url + api.global_course_baseURL + `/business/getNumOfProcess/${this.lessonInfo.ware_id}`,
        methods: 'get',
      }).then(res => {
        console.log('StudentRes',res)
        this.title = '开始分组(每组不超过' +  res.data.result.num + '人)'
      })
    },
    handleOk() {
      let params = {
        planId: this.lessonInfo.plan_id,
        lessonId: this.lessonInfo.lesson_id,
        userId: []
      }
      for(var i=1;i<=3;i++) {
        if(this['list' + i].length>0)
        {
          var group11 = []
          for(var j=0;j<this['list' + i].length;j++) {
            group11.push(this['list' + i][j].uid)
          }
          console.log('group11',group11)
          params.userId.push(group11)
          console.log('params',params)
        }
      }
      axios({
        url: api.server_url + api.global_edu_baseURL + '/eduStudentGroupDetail/divideGroups',
        method: 'post',
        data: params
      }).then(res => {
        console.log(res)
        if(res.data.code == '200')
        {
          this.$message.success('分组成功')
        }
        else {
          this.$message.warning('分组失败')
        }
      })
      this.clearData()
      this.$emit('ok')
    },
    handleCancel() {
      this.clearData()
    },
    clearData() {
      this.title = '',
      this.list = [],
      this.list1 = [],
      this.list2 = [],
      this.list3 = []
      this.visible = false
    },
    end (ev) {
      if (ev.to.className === 'dragArea11') {
        this.$set(this.list[ev.oldIndex], 'flag', true)
      }
    },
    start22 (event) {
      this.falgs = '222222'
    },
    end22 (ev) {
      this.falgs = 'article'
    },
    handleDel (index, id) {
      this.list1.splice(index, 1)
      let q = this.list.find((value, index, arr) => {
        return value.id === id
      })
      this.$set(q, 'flag', false)
    }
  }
}
</script>

<style scoped>
  .list-complete-item {
    cursor: pointer;
    position: relative;
    font-size: 14px;
    padding: 5px 12px;
    display: inline-block;
    margin-right: 20px;
    width: 80px;
    height: 50px;
    border: 1px solid #bfcbd9;
    transition: all 1s;
  }
 
  .list-complete-item.sortable-chosen {
    background: #4AB7BD;
  }
 
  .list-complete-item.sortable-ghost {
    background: #30B08F;
  }
  .undraggable {
    background-color: #3BD4C3;
  }
 
  .list-complete-enter,
  .list-complete-leave-active {
    opacity: 0;
  }
</style>