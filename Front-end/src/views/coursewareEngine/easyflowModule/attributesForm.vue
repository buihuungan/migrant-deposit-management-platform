<template>
  <div>
    <div v-if="attributeFormFlag">
      <div>
        <generate-form style="margin: 0 auto" insite="true" :data="formJson" :remote="func1" ref="generateForm">
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>
      </div>

      <div class="form-button">
        <el-button class="form-button-save" @click="handleTest">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
    </div>
  </div>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '../../../components/FormMaking/components/AntdvGenerator/GenerateForm'
import { axios } from '../../../utils/request'
import { t_postAction, t_getAction } from '@/api/tempApi'
import api from '../../../api/index'
export default {
  name: 'AttributeForm',
  components: { GenerateForm, AntdGenerateForm },
  data() {
    return {
      myValue: { formDesignerId: '', onlineTableId: '', onlineDataId: '' },
      formJson: {},
      attributeFormFlag: false,
      func1: {
        //表单数据绑定
        func_1637737850628(resolve) {
          var data = new FormData()
          data.append('current', 1)
          //data.append('size', 22)
          data.append('deleteFlag', 1)
          data.append('encoding', 'Scheduling')
          t_postAction('/admin/desform/search', data).then((res) => {
            let selectDataList = []
            if (res.success) {
              let formTableData = res.result.list
              for (let i = 0; i < formTableData.length; i++) {
                let temp = {
                  value: formTableData[i].id,
                  label: formTableData[i].name,
                }
                selectDataList.push(temp)
              }
            }
            resolve(selectDataList)
          })
        },
        //得到所有的表单
    // getAllForm() {
    //   var data = new FormData()
    //   data.append('current', 1)
    //   //data.append('size', 22)
    //   data.append('deleteFlag', 1)
    //   data.append('encoding', 'Scheduling')
    //   t_postAction('/admin/desform/search', data).then((res) => {
    //     console.log(res)
    //     this.formList = res.result.list
    //   })
    // },
      },
    }
  },
  // watch: {
  //   //监听prop传的value，如果父级有变化了，将子组件的myValue也跟着变，达到父变子变的效果
  //   value(newVal) {
  //     console.log(this.$refs.generateForm)
  //     console.log(this.formJson)
  //     this.myValue = newVal
  //     this.formJson = {}
  //     if (this.myValue.formDesignerId != '') {
  //       this.getForm()
  //     }
  //   },
  // },
  // mounted() {
  //   console.log('this.myValue.onlineDataId', this.myValue.onlineDataId)
  //   if (this.myValue.formDesignerId != '') {
  //     this.getForm()
  //   }
  // },
  methods: {
    close() {
      this.attributeFormFlag = false
    },
    
    openForm(myValue) {
      console.log('this.myValue', myValue)
      this.myValue = myValue
      this.formJson = {}
      this.getForm()
    },
    //得到表单
    getForm() {
      var id = this.myValue.formDesignerId
      axios({
        url: `/admin/desform/${id}/getConent`,
        method: 'get',
        baseURL: api.server_url + api.global_fmaking_baseURL,
      })
        .then((res) => {
          const onlineTableId = this.myValue.onlineTableId
          const onlineDataId = this.myValue.onlineDataId
          const formJson = JSON.parse(res.result)
          if (onlineTableId != '' && onlineDataId != '') {
            this.getOnlineJson(onlineTableId, onlineDataId, formJson)
          } else {
            this.formJson = formJson
            this.attributeFormFlag = true
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //得到online的data
    getOnlineJson(onlineTableId, onlineDataId, formJson) {
      axios({
        url: `/cgform/api/form/${onlineTableId}/${onlineDataId}`,
        method: 'get',
        baseURL: api.server_url + api.global_online_baseURL,
      })
        .then((res) => {
          var onlineJson = res.result
          console.log('onlineJson', onlineJson)
          this.$nextTick(() => {
            if (onlineJson != null && onlineJson != '') {
              this.connData(onlineJson, formJson)
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
        this.attributeFormFlag = true
      })
    },
    //提交表单填写信息
    handleTest() {
      const $form = this.formJson.config.ui === 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          console.log(data)
          this.commitToDatabase(data) //将数据存储到online数据库中
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    //提交的信息存入数据库
    commitToDatabase(commitdata) {
      console.log('提交的数据', commitdata)
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
          console.log(datajson[tableCol.toString()])
        }
      }
      axios({
        url: `/online/cgform/api/form/${onlineId}`,
        method: 'post',
        data: datajson,
      })
        .then((res) => {
          this.$emit('changeOnline', onlineId, res.result)
          this.$message.success('保存表单成功')
        })
        .catch((err) => {
          this.$message.error('保存表单失败')
        })
    },
  },
}
</script>
<style scoped>
.form-button {
  z-index: auto;
  margin-left: 86px;
  justify-content: space-between;
}
.form-button-save {
  display: flex;
  flex-direction: column;
  height: 32px;
  border-radius: 4px;
  border-width: 1px;
  border: 1px solid rgba(23, 145, 255, 1);
  background: #fbfbfb;
  width: 65px;
  justify-content: center;
  align-items: center;
}

.form-button-save-text {
  z-index: 82;
  width: 33px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(23, 145, 255, 1);
  font-size: 14px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}
</style>