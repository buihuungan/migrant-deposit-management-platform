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
            <h1>编辑文件</h1>
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
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction, o_putAction } from '@/api/onApi.js'
import { w_postAction, w_getAction } from '@/api/workapi'
import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
//import api from '@/api/index'
import { p_postAction, p_getAction } from '@/api/plmApi'
import api from '@/api/index.js'

export default {
  name: 'AnnTask',
  components: { GenerateForm, AntdGenerateForm },
  data() {
    return {
      visible: false,
      formId:'',
      onlineTableId:'',
      onlineDataId:'',
      create_time:'',
      noDateFormJson: {},
      formJson: {},
      func1: {},
      dataForm: {},
      onlineJson: {},
    }
  },
  methods: {
    openModal(formId, onlineTableId, onlineDataId, create_time) {
      this.formId = formId
      this.onlineTableId = onlineTableId
      this.onlineDataId = onlineDataId
      this.create_time = create_time
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
          if (this.onlineJson != null && this.onlineJson != '') {
            this.connData(this.onlineJson, formJson)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    connData(onlineJson, formJson) {
      for (let i = 0; i < formJson.list.length; i++) {
        console.log(formJson.list[i])
        console.log(formJson.list[i].type)
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
        } else if (formJson.list[i].type == 'fileupload') {
          var fileupload = onlineJson[formJson.list[i].tableCol]
          console.log(fileupload)
          formJson.list[i].options.defaultValue = JSON.stringify(fileupload)
        } else {
          if (formJson.list[i].tableCol != '') {
            formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].tableCol]
          }
        }
      }
      console.log('我在这', formJson)
      this.dataForm = formJson
      this.visible=true
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
    saveBom(datajson) {
      // let doc_path = JSON.parse(datajson.doc_path)
      p_postAction(`/bom/save`, {
        fileName: doc_path[0].fileName,
        onlineDataId: this.onlineDataId,
        name: datajson.name,
      })
        .then((res) => {
          console.log('res', res.result)
        })
        .catch((error) => {
          console.log(error)
        })
    },
    commitToDatabase(commitdata, formJson) {
      let datajson = {}
      let commitdataarray = Object.keys(commitdata) /* 赋值，只需运行一次后面调用无需再处理，加快速度 */
      console.log('---------', commitdata)
      console.log('---------', formJson)
      for (let i = 0; i < formJson.list.length; i++) {
        if (formJson.list[i].type == 'grid') {
          for (let j = 0; j < formJson.list[i].columns.length; j++) {
            for (let z = 0; z < formJson.list[i].columns[j].list.length; z++) {
              console.log(formJson.list[i].columns[j].list[z].model, formJson.list[i].columns[j].list[z].tableCol)
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
          console.log(formJson.list[i].model, formJson.list[i].tableCol)
          let model = formJson.list[i].model
          let tableCol = formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        } else {
          console.log(formJson.list[i].model, formJson.list[i].tableCol)
          let model = formJson.list[i].model
          let tableCol = formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        }
      }
      this.temp_datajson = datajson
      console.log('########', datajson)
      return datajson
    },
    saveOnline(datajson) {
      var _this = this
      console.log(datajson)
      datajson['id'] = this.onlineDataId
      datajson['create_time'] = this.create_time
      datajson['prompt'] = this.onlineJson['prompt']
      datajson['bom_detail'] = JSON.stringify(this.onlineJson['bom_detail'])
      datajson['version'] = this.onlineJson['version']
      console.log(datajson)
      o_putAction('/cgform/api/form/' + this.onlineTableId + '?tabletype=1', datajson)
        .then((res) => {
          _this.visible=false
          if (res.success == true) {
            this.$message.success('提交成功')
          } else {
            this.$message.error('提交失败')
          }
          this.$emit('closeFile')
          this.saveBom(datajson) //将数据给BOM
        })
        .catch((err) => {
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