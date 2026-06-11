<template>
  <div id="newformContent">
    <div class="formbody">
      <div class="newForm">
        <generate-form style="margin: 0 auto" insite="true" :remote="func1" :data="newForm" ref="generateForm">
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>
      </div>
      <div class="submitBtn">
        <a-button v-if="nodePowerResult.includes('通过')" type="primary" @click="handleTest('1')"
          style="margin-right: 20px">通过</a-button>
        <a-popover v-if="nodePowerResult.includes('退回')" title="请选择退回到哪一个节点">
          <template slot="content">
            <a-button type="dashed" @click="handleTest('2')" style="margin-left: 10px"> 上一个节点 </a-button>
          </template>
          <a-button type="primary">退回</a-button>
        </a-popover>
        <a-button v-if="nodePowerResult.includes('拒绝')" type="danger" @click="handleTest('0')"
          style="margin-left: 200px">终止</a-button>
      </div>
    </div>
  </div>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import { w_postAction, w_postAction1 } from '../../../api/workapi'
import { nw_postAction1, nw_getAction } from '@api/newWorkApi'
import axios from 'axios'
import api from '@/api/index'
export default {
  name: 'approveNewTask',
  components: { GenerateForm, AntdGenerateForm },
  props: {
    formId: {
      type: String,
    },
    taskId: {
      type: Number,
    },
    record: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      process_id: '',
      nodePowerResult: [],
      newForm: {},
      func1: {
        func_kyf(resolve) {
          const options = [
            { value: 'kyf', label: 'kyf' },
            { value: 'cj', label: 'cj' },
            { value: 'kl', label: 'kl' },
          ]

          resolve(options)
        },
      },
    }
  },
  updated() { },
  mounted() {
    this.getNowFrom()
    this.checkNodePower()
  },
  methods: {
    async checkNodePower() {
      this.nodePowerResult = []
      try {
        const response = await axios.post(`${api.server_url}${api.global_course_baseURL}/task/nodePower`, {
          taskId: this.record.taskId,
          processId: this.record.processId,
          processInstanceId: this.record.processInstanceId,
        })
        console.log('检查节点权限成功：', response.data.result)
        // this.nodePowerResult = response.data.result
        if (response.data.result) {
          this.nodePowerResult = response.data.result
        } else {
          this.nodePowerResult = []
        }
      } catch (error) {
        console.error('检查节点权限失败：', error)
        this.$message.error('获取节点权限失败')
      }
    },

    close() {
      var _this = this
      this.$elementConfirm('是否关闭处理界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          _this.$emit('close-approveTask')
        })
        .catch(() => { })
    },
    //得到一个现在节点的表单

    getNowFrom() {
      var nowid = this.formId
      t_getAction('/admin/desform/' + nowid + '/getConent')
        .then((res) => {
          this.newForm = JSON.parse(res.result)
          console.log('尝试能否得到按钮的数据json')
          console.log('this.newForm', this.newForm)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //通过操作
    completeTask(onlineId, dataId) {
      let params = {
        taskId: this.taskId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
      }
      // if (this.category === '存缴') {
      //   params.depositWay = this.projectStatus
      // }
      if (this.frontId) {
        params.frontId = this.frontId
      }
      var _this = this
      nw_postAction1('/task/complete', params)
        .then((res) => {
          if (res.result.result) {
            _this.$message.success('通过成功')
            _this.$emit('close-approveTask')
            this.$bus.$emit('callGetTotal');
          } else {
            _this.$message.error('通过失败')
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },

    //保存数据的接口
    // saveMarginData(onlineId, dataId) {

    //   nw_postAction1('/margin/saveMarginData', params)
    //     .then((res) => {
    //       console.log('保存数据的接口返回值', res)
    //       let mainId = res.result.mainId
    //       this.$nextTick(() => {
    //         this.completeTask(onlineId, dataId, mainId)
    //       })
    //     })
    //     .catch((err) => {
    //       console.log(err)
    //     })
    // },

    //退回操作
    rollback(onlineId, dataId) {
      var _this = this
      nw_postAction1('/task/withdraw', {
        taskId: this.taskId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
      })
        .then((res) => {
          console.log(res)
          if (res.result.status) {
            _this.$message.success('退回成功')
            _this.$emit('close-approveTask')
          } else {
            _this.$message.error('退回失败')
          }
        })
        .catch((err) => {
          console.log(err)
          _this.$message.error('退回失败')
        })
    },
    //拒绝操作
    reject(onlineId, dataId) {
      var _this = this
      nw_postAction1('/task/cancel', {
        taskId: this.taskId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
      })
        .then((res) => {
          if (res.result) {
            _this.$message.success('拒绝成功')
            _this.$emit('close-approveTask')
          } else {
            _this.$message.error('拒绝失败')
          }
        })
        .catch((err) => {
          _this.$message.error('拒绝失败')
          console.log(err)
        })
    },
    handleTest(stateflag) {
      const $form = this.newForm.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          this.commitToDatabase(data, stateflag)
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    commitToDatabase(commitdata, stateflag) {
      var _this = this
      let name = this.newForm.config.dataBase
      var onlineId = this.newForm.config.tableId
      let datajson = {}
      for (let i = 0; i < this.newForm.list.length; i++) {
        if (this.newForm.list[i].type == 'grid') {
          for (let j = 0; j < this.newForm.list[i].columns.length; j++) {
            for (let z = 0; z < this.newForm.list[i].columns[j].list.length; z++) {
              let model1 = this.newForm.list[i].columns[j].list[z].model
              let tableCol1 = this.newForm.list[i].columns[j].list[z].tableCol
              datajson[tableCol1.toString()] = commitdata[model1]
            }
          }
        } else if (this.newForm.list[i].type == 'divider') {
          // let model = this.newForm.list[i].model
          // let key = this.newForm.list[i].key
          // datajson[key.toString()] = commitdata[model]
        } else if (this.newForm.list[i].type == 'table') {
          let schedule = this.newForm.list[i].schedule
          let array = new Array()
          for (let k = 0; k < commitdata[this.newForm.list[i].model].length; k++) {
            let datajson1 = {}
            for (let h = 0; h < this.newForm.list[i].tableColumns.length; h++) {
              let model2 = this.newForm.list[i].tableColumns[h].model
              let tableCol2 = this.newForm.list[i].tableColumns[h].tableCol
              datajson1[tableCol2.toString()] = commitdata[this.newForm.list[i].model][k][model2]
            }
            array.push(datajson1)
          }
          datajson[this.newForm.list[i].schedule.toString()] = array
        } else {
          let model = this.newForm.list[i].model
          let tableCol = this.newForm.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        }
      }
      o_postAction('/cgform/api/form/' + onlineId, datajson)
        .then((res) => {
          //stateflag:0 终止 ;1 通过;2 退回
          if (stateflag == '0') {
            this.reject(onlineId, res.result)
          } else if (stateflag == '1') {
            this.completeTask(onlineId, res.result)
            // this.saveMarginData(onlineId, res.result)
          } else if (stateflag == '2') {
            this.rollback(onlineId, res.result)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
  },
}
</script>
<style scoped>
#newformContent {
  width: 1250px;
  display: flex;
  justify-content: center;
  padding: 5px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin: 20px auto;
}

.formbody {
  margin-top: 20px;
  width: 1250px;
}

.submitBtn {
  margin-top: 20px;
  /* display: flex; */
  /* justify-content: space-between; */
}

.submitBtn button {
  margin: 20px auto;
  left: 40%;
}
</style>