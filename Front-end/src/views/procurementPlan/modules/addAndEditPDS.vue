<template>
  <div>
    <a-modal
      :title="title"
      :visible="visible"
      okText="确定"
      cancelText="取消"
      width="80%"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <div class="formbody">
        <generate-form
          style="margin: 0 auto"
          insite="true"
          :data="formJson"
          ref="generateForm"
        >
          <template v-slot:blank="scope">
            Width
            <el-input
              v-model="scope.model.blank.width"
              style="width: 100px"
            ></el-input>Height：
            <el-input
              v-model="scope.model.blank.height"
              style="width: 100px"
            ></el-input>
          </template>
        </generate-form>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { putAction } from '@api/manage'
import { o_putAction, o_getAction, o_postAction } from '@/api/onApi'
import { Message } from 'element-ui'
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
export default {
  components: {
    GenerateForm,
    AntdGenerateForm,
  },
  data() {
    return {
      //   form: this.$form.createForm(this, { name: 'purchase_demand_sheet' }),
      flowId: '5ee1d75d992f4653892c1ad742868f7a',
      onlineId: '597983d3ade2449aa91338e05148332a',
      form: {
        identity: '',
        name: '',
        model: '',
        unit: '',
        number: '',
      },
      title: '',
      visible: false,
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      },
      formJson: {},
      destroy: true,
      addOrEditFlag: 1,
      dataId: '',
      order_identity: '',
      url: {
        rule: {
          orderNum: '/sys/fillRule/executeRuleByCode/purchase_order_autoCode',
        },
      },
    }
  },
  methods: {
    openModal(record, addOrEditFlag) {
      if (addOrEditFlag == 1) {
        this.title = '新增'
        this.addOrEditFlag = 1
        this.getForm(record, addOrEditFlag)
      } else if (addOrEditFlag == 2) {
        this.title = '编辑'
        this.addOrEditFlag = 2
        this.getForm(record, addOrEditFlag)
      } else {
        Message({
          showClose: true,
          message: '页面传参错误，打开页面失败！',
          type: 'error',
        })
      }
    },
    getForm(record, message) {
      this.dataId = record.id
      this.order_identity = record.orderIdentity
      t_getAction('/admin/desform/' + this.flowId + '/getConent')
        .then((res) => {
          if (Object.keys(record).length === 0) {
            this.formJson = JSON.parse(res.result)
            this.visible = true
          } else {
            if (message === 2) {
              this.getOnlineJson(res.result, this.dataId)
            }
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    handleOk() {
      const $form = this.formJson.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          this.commitToDatabase(data) //将数据存储到online数据库中
        })
        .catch((e) => {
          console.log('e', e)
          this.$message.error(e)
          Message({
            showClose: true,
            message: '传参错误，刷新重试！',
            type: 'error',
          })
        })
    },
    handleCancel() {
      // this.formJson = {}
      this.visible = false
    },

    commitToDatabase(commitdata) {
      var _this = this
      let name = this.formJson.config.dataBase
      var onlineId = this.formJson.config.tableId
      let datajson = {}
      let commitdataarray = Object.keys(commitdata) /* 赋值，只需运行一次后面调用无需再处理，加快速度 */

      let commitdataarraylength = commitdataarray.length /* 获取数据长度 */

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
      if (this.addOrEditFlag === 1) {
        //新增操作
        this.getOrderNum().then((res) => {
          datajson.order_identity = res
          datajson.example_id = this.$store.state.exampleId
          o_postAction('/cgform/api/form/' + onlineId, datajson)
            .then((res) => {
              _this.visible = false
              this.$message.success('新增成功')
              this.$emit('ok')
            })
            .catch((err) => {
              this.$message.error('新增失败')
              console.log(err)
            })
        })
      } else {
        datajson.id = this.dataId
        datajson.example_id = this.$store.state.exampleId
        datajson.order_identity = this.order_identity
        datajson.choose_supplier = 1
        o_putAction('/cgform/api/form/' + onlineId, datajson)
          .then((res) => {
            _this.visible = false
            this.$message.success('编辑成功')
            this.$emit('ok')
          })
          .catch((err) => {
            this.$message.error('编辑失败')
            console.log(err)
          })
      }
    },
    getOnlineJson(formJson, predataId) {
      //得到online的json
      o_getAction(`/cgform/api/form/${this.onlineId}/` + predataId)
        .then((res) => {
          var onlineJson = res.result
          this.connData(onlineJson, JSON.parse(formJson))
        })
        .catch((err) => {
          console.log(err)
        })
    },
    connData(onlineJson, formJson) {
      //拼接online键值配对
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
          for (let k = 0; k < onlineJson[formJson.list[i].schedule].length; k++) {
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
      this.formJson = formJson
      this.visible = true
    },
    getOrderNum() {
      var model = { prefix: '' }
      return putAction(this.url.rule.orderNum, model).then((res) => {
        if (res.success) {
          return res.result
        }
      })
    },
  },
}
</script>

<style scoped>
.formbody {
  margin-top: 20px;
  width: 100%;
}
</style>