<template>
  <div>
    <a-modal
      :visible="visible"
      :footer="null"
      :centered="true"
      width="95%"
      :zIndex="100"
      :closable="false"
      :destroyOnClose="true"
      dialogClass="dialogTest"
    >
      <div class="xbutton"><a-button type="dashed" @click="close()">X</a-button></div>
      <div>
        <div id="formContent">
          <div class="title">
            <h1>{{ editFileName }}</h1>
          </div>
          <div class="formbody">
            <generate-form style="margin: 0 auto" insite="true" :data="dataForm" :remote="func1" ref="generateForm">
              <template v-slot:blank="scope">
                Width
                <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
                <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
              </template>
            </generate-form>
          </div>
          <div class="submitBtn">
            <a-button type="primary" @click="handleTest()" style="margin-right: 20px">修改</a-button>
            <a-button type="primary" @click="close()">返回</a-button>
          </div>
        </div>
      </div>
    </a-modal>
  </div>
</template>
<script>
import GenerateForm from '../../../src/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '../../../src/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_getAction } from '@/api/tempApi.js'
import { o_getAction, o_putAction } from '@/api/onApi.js'
//import api from '@/api/index'
import { p_postAction } from '@/api/plmApi'

export default {
  name: 'editFile',
  components: { GenerateForm, AntdGenerateForm },
  data() {
    return {
      visible: false,
      formId:'',
      onlineTableId:'',
      onlineDataId:'',
      editFileName:'',
      onlineJson: {},
      noDateFormJson: {},
      formJson: {},
      func1: {},
      dataForm: {},
    }
  },
  methods: {
    openModal(formId, onlineTableId, onlineDataId, editFileName) {
      this.formId = formId
      this.onlineTableId = onlineTableId
      this.onlineDataId = onlineDataId
      this.editFileName = editFileName
      this.getForm()
    },
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
    getForm() {
      var id = this.formId
      t_getAction('/admin/desform/' + id + '/getConent')
        .then((res) => {
          var formJson = JSON.parse(res.result)
          this.noDateFormJson = formJson
          this.getOnlineJson(this.onlineTableId, this.onlineDataId, formJson)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getOnlineJson(onlineTableId, onlineDataId, formJson) {
      //得到online的json
      o_getAction('/cgform/api/form/' + onlineTableId + '/' + onlineDataId)
        .then((res) => {
          this.onlineJson = res.result
          this.connData(this.onlineJson, formJson)
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
          for (let k = 0; k < onlineJson[formJson.list[i].schedule].length; k++) {
            let tableValue = onlineJson[formJson.list[i].schedule][k]
            if (tableValue != undefined) {
              for (let h = 0; h < formJson.list[i].tableColumns.length; h++) {
                formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].schedule]
              }
            }
          }
        } else if (formJson.list[i].type == 'fileupload') {
          var fileupload = onlineJson[formJson.list[i].tableCol]
          formJson.list[i].options.defaultValue = JSON.stringify(fileupload)
        } else {
          if (formJson.list[i].tableCol != '') {
            formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].tableCol]
          }
        }
      }
      this.dataForm = formJson
      this.visible = true
    },
    handleTest() {
      const $form = this.dataForm.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          let datajson = {}
          var noDateFormJson = this.noDateFormJson
          datajson = this.commitToDatabase(data, noDateFormJson)
          this.saveOnline(datajson) //将数据存储到online数据库中
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    commitToDatabase(commitdata, formJson) {
      let datajson = {}
      let commitdataarray = Object.keys(commitdata) /* 赋值，只需运行一次后面调用无需再处理，加快速度 */
      console.log('commitdata', commitdata)
      for (let i = 0; i < formJson.list.length; i++) {
        if (formJson.list[i].type == 'grid') {
          for (let j = 0; j < formJson.list[i].columns.length; j++) {
            for (let z = 0; z < formJson.list[i].columns[j].list.length; z++) {
              let model1 = formJson.list[i].columns[j].list[z].model
              let tableCol1 = formJson.list[i].columns[j].list[z].tableCol
              datajson[tableCol1.toString()] = commitdata[model1]
            }
          }
        } else if (formJson.list[i].type == 'divider') {
          // let model = formJson.list[i].model
          // let key = formJson.list[i].key
          // datajson[key.toString()] = commitdata[model]
        } else if (formJson.list[i].type == 'fileupload') {
          let model = formJson.list[i].model
          let tableCol = formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        } else {
          let model = formJson.list[i].model
          let tableCol = formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        }
      }
      this.temp_datajson = datajson
      return datajson
    },
    saveOnline(datajson) {
      var _this = this
      var date = new Date()
      var create_time =
        date.getFullYear() +
        '-' +
        (date.getMonth() + 1) +
        '-' +
        date.getDate() +
        ' ' +
        date.getHours() +
        ':' +
        date.getMinutes() +
        ':' +
        date.getSeconds()
      let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
      var create_by = userData.value.username
      datajson['id'] = this.onlineDataId
      datajson['create_time'] = create_time
      datajson['create_by'] = create_by
      o_putAction('/cgform/api/form/' + this.onlineTableId + '?tabletype=1', datajson)
        .then((res) => {
          this.visible = false
          this.$message.success('提交成功')
         this.$emit('ok')
        })
        .catch((err) => {
          this.$message.error('提交失败')
          console.log(err)
        })
    },
  },
}
</script>

<style lang="less" scoped>
//scoped 属性是一个布尔属性。如果使用该属性，则样式仅仅应用到 style 元素的父元素及其子元素。
//使用lang="less"，可在CSS界面写JS
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
  margin-top: 20px;
  width: 100%;
}
.submitBtn {
  margin-top: 20px;
}
.submitBtn button {
  margin: 20px auto;
  left: 45%;
}
.abutton {
  margin-right: 20px;
}
//使用deep进行穿透，可以修改此界面的第三方组件但影响不到其他地方
/deep/ .tips-class > .el-form-item__label {
  font-size: 18px;
  color: red;
}
</style>