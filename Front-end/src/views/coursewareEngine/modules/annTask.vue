<template>
  <div>
    <a-modal
      :visible="visible"
      :footer="null"
      width="95%"
      :zIndex="100"
      :closable="false"
      :destroyOnClose="true"
      :centered="true"
    >
      <div>
        <div id="formContent">
          <div class="formbody">
            <generate-form style="margin: 0 auto" insite="true" :data="formJson" :remote="func1" ref="generateForm">
              <template v-slot:blank="scope">
                Width
                <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
                <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
              </template>
            </generate-form>
          </div>
          <div class="submitBtn">
            <a-button type="primary" @click="handleTest()" style="margin-right: 20px">提交</a-button>
            <a-button type="primary" @click="close()">返回</a-button>
          </div>
        </div>
      </div>
    </a-modal>
  </div>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import { nw_postAction1 } from '@api/newWorkApi'

export default {
  name: 'AnnTask',
  components: { GenerateForm, AntdGenerateForm },
  data() {
    return {
      formDesignerId: '',
      onlineDataId: '',
      onlineTableId: '',
      taskId: '',
      visible: false,
      formJson: {},
      func1: {
        //获得所有的库存地址
        func_1615188754467(resolve) {
          //console.log('走到这里')
          let params = {
            column: 'createTime',
            order: 'desc',
            pageNo: 1,
            pageSize: 1000,
            superQueryMatchType: 'and',
          }
          o_getAction('/cgform/api/getData/4e7015e9b56447d3a29c3fb392a90b49', params).then((res) => {
            let selectDataList = []
            if (res.success) {
              let formTableData = res.result.records
              for (let i = 0; i < formTableData.length; i++) {
                let temp = {
                  value: formTableData[i].id,
                  label: formTableData[i].unit_name,
                }
                selectDataList.push(temp)
              }
            }
            resolve(selectDataList)
          })
        },
      },
    }
  },
  updated() {},
  methods: {
    openModal(formDesignerId, onlineDataId, onlineTableId, taskId) {
      this.formDesignerId = formDesignerId
      this.onlineDataId = onlineDataId
      this.onlineTableId = onlineTableId
      this.taskId = taskId
      this.getForm()
    },
    //点击关闭按钮关闭
    close() {
      var _this = this
      this.$elementConfirm('是否关闭处理界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          _this.visible = false
        })
        .catch(() => {})
    },
    //得到表单
    getForm() {
      var id = this.formDesignerId
      t_getAction('/admin/desform/' + id + '/getConent')
        .then((res) => {
          const formJson = JSON.parse(res.result)
          if (this.onlineTableId != '' && this.onlineDataId != '') {
            this.getOnlineJson(this.onlineTableId, this.onlineDataId, formJson)
          } else {
            this.formJson = formJson
            this.visible = true
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //提交表单填写信息
    handleTest() {
      const $form = this.formJson.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          this.commitToDatabase(data) //将数据存储到online数据库中
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    //完成该节点的任务，把该节点填写的表单id，online表id，online数据id传入工作流
    completeTask(onlineId, dataId) {
      var _this = this
      console.log('completeTask')
      nw_postAction1('/task/complete', {
        taskId: _this.taskId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
      })
        .then((res) => {
          if (res.result.result) {
            _this.$message.success('通过成功')
          } else {
            _this.$message.error('通过失败')
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //得到online的data
    getOnlineJson(onlineTableId, onlineDataId, formJson) {
      //得到online的json
      o_getAction('/cgform/api/form/' + onlineTableId + '/' + onlineDataId)
        .then((res) => {
          var onlineJson = res.result
          console.log('onlineJson', onlineJson)
          this.$nextTick(() => {
            if (onlineJson != null && onlineJson != '') {
              this.connData(onlineJson, formJson)
            } else {
              this.formJson = formJson
              this.visible = true
            }
          })
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //拼接formJson和onlineJson
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
          console.log(onlineJson[formJson.list[i].schedule])
          for (let k = 0; k < onlineJson[formJson.list[i].schedule].length; k++) {
            console.log(onlineJson[formJson.list[i].schedule][k])
            let tableValue = onlineJson[formJson.list[i].schedule][k]
            if (tableValue != undefined) {
              for (let h = 0; h < formJson.list[i].tableColumns.length; h++) {
                formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].schedule]
              }
            }
          }
        } else {
          if (formJson.list[i].tableCol != '') {
            formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].tableCol]
          }
        }
      }
      this.$nextTick(() => {
        this.formJson = formJson
        this.visible = true
      })
    },
    //提交的信息存入数据库
    commitToDatabase(commitdata) {
      var _this = this
      var onlineId = this.formJson.config.tableId
      let datajson = {}
      for (let i = 0; i < this.formJson.list.length; i++) {
        if (this.formJson.list[i].type == 'grid') {
          for (let j = 0; j < this.formJson.list[i].columns.length; j++) {
            for (let z = 0; z < this.formJson.list[i].columns[j].list.length; z++) {
              let model1 = this.formJson.list[i].columns[j].list[z].model
              let tableCol1 = this.formJson.list[i].columns[j].list[z].tableCol
              datajson[tableCol1.toString()] = commitdata[model1]
            }
          }
        } else if (this.formJson.list[i].type == 'divider') {
          // let model = this.formJson.list[i].model
          // let key = this.formJson.list[i].key
          // datajson[key.toString()] = commitdata[model]
        } else if (this.formJson.list[i].type == 'table') {
          let schedule = this.formJson.list[i].schedule
          let array = new Array()
          for (let k = 0; k < commitdata[this.formJson.list[i].model].length; k++) {
            let datajson1 = {}
            for (let h = 0; h < this.formJson.list[i].tableColumns.length; h++) {
              let model2 = this.formJson.list[i].tableColumns[h].model
              let tableCol2 = this.formJson.list[i].tableColumns[h].tableCol
              datajson1[tableCol2.toString()] = commitdata[this.formJson.list[i].model][k][model2]
            }
            array.push(datajson1)
          }
          datajson[this.formJson.list[i].schedule.toString()] = array
        } else {
          let model = this.formJson.list[i].model
          let tableCol = this.formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        }
      }
      o_postAction('/cgform/api/form/' + onlineId, datajson)
        .then((res) => {
          _this.visible = false
          this.completeTask(onlineId, res.result)
          this.$message.success('提交成功')
        })
        .catch((err) => {
          this.$message.error('提交失败')
          console.log(err)
        })
    },
  },
}
</script>
<style scoped>
#formContent {
  padding: 5px;
  background-color: #fff;
  border: 1px solid;
  border-radius: 5px;
  margin: 0 auto;
}
.formTable {
  width: 90%;
  border: 1px solid #000000;
  margin: 0 auto;
}
.title {
  width: 100%;
  height: 50px;
  text-align: center;
}
.formbody {
  margin-top: 50px;
  width: 97%;
}
.submitBtn {
  margin-top: 20px;
}
.submitBtn button {
  margin: 20px auto;
  left: 45%;
}
</style>