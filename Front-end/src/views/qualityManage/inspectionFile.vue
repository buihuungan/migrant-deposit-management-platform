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
            <h1>{{ annFileName }}</h1>
          </div>
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
            <!-- <span slot="action" slot-scope="text, record">
              <a-button type="primary" @click="handleTest(record)" style="margin-right: 20px">提交</a-button>
              <a-button type="primary" @click="close()">返回</a-button>
            </span> -->
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
import { o_postAction, o_putAction, o_getAction} from '@/api/onApi.js'
import { order_postAction,order_postAction2 } from '@/api/orderApi.js'
//import api from '@/api/index'
import { p_postAction, p_getAction } from '@/api/plmApi'

export default {
  name: 'inspectionFile',
  components: { GenerateForm, AntdGenerateForm },
  data() {
    return {
      orderInfoId: 'e8c9f23b81d74fc6a292c436d27faa70', //订单信息表id
      qualityInspectionOnlineId: 'bba30b3a70924ac7aa65c220ee62cb30', //质量检测表id
      visible: false,
      formId: '',
      annFileName: '',
      record: {},
      formJson: {},
      func1: {},
    }
  },
  methods: {
    openModal(annFileName, record) {
      console.log(record);
      this.formId = record.check_form_id
      this.annFileName = annFileName
      this.record = record
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
          this.formJson = formJson
          this.visible = true
        })
        .catch((err) => {
          console.log(err)
        })
    },
    handleTest() {
      const $form = this.formJson.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          let datajson = {}
          var formJson = this.formJson
          datajson = this.commitToDatabase(data, formJson)
          this.saveOnline(datajson) //将数据存储到online数据库中
          
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    commitToDatabase(commitdata, formJson) {
      var _this = this
      let name = formJson.config.dataBase
      var onlineId = formJson.config.tableId
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
              console.log(commitdata[model1]);
            }
          }
        } else if (formJson.list[i].type == 'divider' || formJson.list[i].type == 'text') {
          // let model = formJson.list[i].model
          // let key = formJson.list[i].key
          // datajson[key.toString()] = commitdata[model]
        } else if (formJson.list[i].type == 'fileupload') {
          let model = formJson.list[i].model
          let tableCol = formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
          console.log(commitdata[model]);
        } else {
          let model = formJson.list[i].model
          let tableCol = formJson.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
          console.log(commitdata[model]);
        }
      }
      console.log(datajson);
      return datajson
    },
    saveOnline(datajson) {
      console.log("saveOnline");
      var _this = this
      var onlineId = this.formJson.config.tableId
      o_postAction('/cgform/api/form/' + onlineId, datajson)
        .then((res) => {
          _this.visible = false
          console.log('----res.result----',res.result)
          var onlineDataId = res.result
          this.record.data_id = onlineDataId
          // this.record.order_state = 3
          console.log('------record-----',this.record)
          _this.closeFlag = true
          if (res.success == true) {
            o_putAction('/cgform/api/form/bba30b3a70924ac7aa65c220ee62cb30', this.record)
              .then((res) => {
                console.log('------res-----',res)
                console.log('----datajson-------',datajson.result)
                if(this.record.remark === '出货前检测'){
                  if(datajson.result === '不合格'){
                    var params = {}
                    params.processId = this.record.product_id
                    params.orderState = 3
                    order_postAction2(`/cictOrder/updateOrderState`, params)
                      .then((res)=>{
                        console.log('出货前检测完成 不合格 状态为3');
                      })
                  }else if(datajson.result === '合格'){
                    var params = {}
                    params.processId = this.record.product_id
                    params.orderState = 4
                    order_postAction2(`/cictOrder/updateOrderState`, params)
                      .then((res)=>{
                        console.log('出货前检测完成 合格 状态为4');
                      })                    
                  }
                }
                _this.visible = false
                this.$message.success('提交成功')
                _this.$emit('ok')
              })
              .catch((err) => {
                this.$message.error('提交失败')
                console.log(err)
              })
          } else {
            this.$message.error('提交失败')
          }
          _this.$emit('ok')
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