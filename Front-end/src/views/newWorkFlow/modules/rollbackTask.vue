<template>
  <div id="formContent">
    <div class="formbody">
      <div>
        <generate-form style="margin: 0 auto" insite="true" :remote="func1" :data="dataForm" ref="generateForm">
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>
      </div>
      <div class="submitBtn">
        <a-button type="primary" @click="handleTest('1')">确认再次提交</a-button>
      </div>
    </div>
  </div>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import { nw_postAction1, nw_getAction } from '@api/newWorkApi'
export default {
  name: 'RollbackTask',
  components: { GenerateForm, AntdGenerateForm },
  props: ['formId', 'dataId', 'tableId', 'taskId', 'processName'],
  data() {
    return {
      dataForm: {},
      func1: {},
    }
  },
  updated() {},
  methods: {
    close() {
      var _this = this
      this.$elementConfirm('是否关闭处理界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          _this.$emit('close-approveRollTask')
        })
        .catch(() => {})
    },
    //得到一个表单
    getForm() {
      t_getAction('/admin/desform/' + this.formId + '/getConent')
        .then((res) => {
          var formJson = JSON.parse(res.result)
          if (this.tableId != '' && this.dataId != '') {
            this.getOnlineJson(this.tableId, this.dataId, formJson)
          } else {
            this.dataForm = formJson
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getOnlineJson(tableId, dataId, formJson) {
      //得到online的json
      o_getAction('/cgform/api/form/' + tableId + '/' + dataId)
        .then((res) => {
          var onlineJson = res.result
          if (onlineJson != null && onlineJson != '') {
            this.connData(onlineJson, formJson)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    connData(onlineJson, formJson) {
      for (let i = 0; i < formJson.list.length; i++) {
        if (formJson.list[i].type == 'grid') {
          for (let j = 0; j < formJson.list[i].columns.length; j++) {
            for (let z = 0; z < formJson.list[i].columns[j].list.length; z++) {
              if (formJson.list[i].columns[j].list[z].tableCol != '') {
                formJson.list[i].columns[j].list[z].options.defaultValue =
                  onlineJson[formJson.list[i].columns[j].list[z].tableCol]
              }
            }
          }
        } else if (formJson.list[i].type == 'table') {
          // for (let h = 0; h < formJson.list[i].tableColumns.length; h++) {
          //   formJson.list[i].tableColumns[h].options.defaultValue =
          //     onlineJson[formJson.list[i].tableColumns[h].tableCol]
          // }
          //console.log(onlineJson[formJson.list[i].schedule])
          for (let k = 0; k < onlineJson[formJson.list[i].schedule].length; k++) {
            //console.log(onlineJson[formJson.list[i].schedule][k])
            for (let h = 0; h < formJson.list[i].tableColumns.length; h++) {
              formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].schedule]
            }
          }
        } else {
          if (formJson.list[i].tableCol != '') {
            formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].tableCol]
            // if (formJson.list[i].type == 'fileupload') {
            //   var fileupload=JSON.parse(tformJson.list[i].options.defaultValue)
            //   fileupload[0].dbpath="https://git.data.wust.edu.cn/api1/online/common/download"+fileupload[0].dbpath
            //   formJson.list[i].options.defaultValue=JSON.stringify(fileupload)
            // }
          }
        }
      }
      this.dataForm = formJson
    },
    handleTest(stateflag) {
      const $form = this.dataForm.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          //console.log(data, '提交出去')
          this.commitToDatabase(data)
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    commitToDatabase(commitdata) {
      console.log('commitdata', commitdata)
      var _this = this
      var onlineId = this.dataForm.config.tableId
      let datajson = {}
      for (let i = 0; i < this.dataForm.list.length; i++) {
        if (this.dataForm.list[i].type == 'grid') {
          for (let j = 0; j < this.dataForm.list[i].columns.length; j++) {
            for (let z = 0; z < this.dataForm.list[i].columns[j].list.length; z++) {
              let model1 = this.dataForm.list[i].columns[j].list[z].model
              let tableCol1 = this.dataForm.list[i].columns[j].list[z].tableCol
              datajson[tableCol1.toString()] = commitdata[model1]
            }
          }
        } else if (this.dataForm.list[i].type == 'divider') {
          // let model = this.dataForm.list[i].model
          // let key = this.dataForm.list[i].key
          // datajson[key.toString()] = commitdata[model]
        } else if (this.dataForm.list[i].type == 'table') {
          let schedule = this.dataForm.list[i].schedule
          let array = new Array()
          for (let k = 0; k < commitdata[this.dataForm.list[i].model].length; k++) {
            let datajson1 = {}
            for (let h = 0; h < this.dataForm.list[i].tableColumns.length; h++) {
              let model2 = this.dataForm.list[i].tableColumns[h].model
              let tableCol2 = this.dataForm.list[i].tableColumns[h].tableCol

              datajson1[tableCol2.toString()] = commitdata[this.dataForm.list[i].model][k][model2]
            }
            array.push(datajson1)
          }
          datajson[this.dataForm.list[i].schedule.toString()] = array
        } else if (this.dataForm.list[i].type == 'fileupload') {
          let model = this.dataForm.list[i].model
          let tableCol = this.dataForm.list[i].tableCol
          if (commitdata[model] && commitdata[model].length > 0) {
            datajson[tableCol.toString()] = JSON.stringify(commitdata[model]);
          } else {
            datajson[tableCol.toString()] = '[]';
          }
        } else {
          let model = this.dataForm.list[i].model
          let tableCol = this.dataForm.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        }
      }
      o_postAction('/cgform/api/form/' + onlineId, datajson)
        .then((res) => {
          this.completeTask(onlineId, res.result)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    completeTask(onlineId, dataId) {
      var _this = this
      nw_postAction1('/task/complete', {
        taskId: this.taskId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
        depositWay: this.processName,
        isWithdraw: 1,
      })
        .then((res) => {
          console.log(res)
          _this.$message.success('提交成功')
          _this.$emit('close-approveRollTask')
          this.$bus.$emit('callGetTotal');
        })
        .catch((err) => {
          _this.$message.error('提交失败')
          console.log(err)
        })
    },

  },
  mounted() {
    this.getForm()
  },
}
</script>
<style scoped>
#formContent {
  display: flex;
  justify-content: center;
  padding: 5px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin: 0 auto;
}
.formTable {
  width: 1250px;
  border: 1px solid #000000;
  margin: 0 auto;
}
.title {
  width: 100%;
  height: 50px;
  text-align: center;
}
.formbody {
  margin-top: 20px;
  width: 1250px;
}
.submitBtn {
  margin-top: 20px;
}
.submitBtn button {
  margin: 20px auto;
  left: 38%;
}
.buttonstyle {
  margin-left: 670px;
}
</style>