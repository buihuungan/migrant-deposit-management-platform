<template>
  <a-modal
    :centered="true"
    :visible="visible"
    :footer="null"
    width="95%"
    :zIndex="100"
    :closable="false"
    :destroyOnClose="destroy"
  >
    <div id="formContent">
      <div class="formbody">
        <generate-form style="margin: 0 auto" insite="true" :data="formJson" ref="generateForm" :edit="editFlag">
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>
      </div>
      <div class="submitBtn">
        <a-button type="primary" v-if="editFlag" @click="handleTest()" style="margin-right: 20px">提交</a-button>
        <a-button type="primary" @click="close()">返回</a-button>
      </div>
    </div>
  </a-modal>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import { t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction, o_putAction } from '@/api/onApi.js'

export default {
  name: 'salesModal',
  components: { GenerateForm },
  props: ['flowId'],
  data() {
    return {
      formJson: {},
      visible: false,
      destroy: true,
      editFlag: true,
      addFlag: false,
    }
  },
  updated() {
    setTimeout(() => {
      $('.is_customized')
        .find('input[type=radio]')
        .change(function () {
          if ($('.is_customized').find('input[type=radio]:checked').val() === '0') {
            $('.childTable')[0].style.display = 'block'
            $('.customized_desc').each(function () {
              $(this)[0].style.display = 'none'
            })
          } else {
            $('.childTable')[0].style.display = 'none'
            $('.customized_desc').each(function () {
              $(this)[0].style.display = 'block'
            })
          }
        })
    }, 0)
  },
  methods: {
    close() {
      var _this = this
      this.$elementConfirm('是否关闭处理界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          _this.visible = false
          _this.editFlag = true
          _this.$emit('close-annTask')
        })
        .catch(() => {})
    },
    getForm(record, message) {
      var id = this.flowId
      this.dataId = record.id
      t_getAction('/admin/desform/' + id + '/getConent')
        .then((res) => {
          if (Object.keys(record).length === 0) {
            this.addFlag = true
            this.formJson = JSON.parse(res.result)
            this.visible = true

            this.$nextTick(() => {
              if ($('.is_customized').find('input[type=radio]:checked').val() === '0') {
                $('.childTable')[0].style.display = 'block'
                $('.customized_desc').each(function () {
                  $(this)[0].style.display = 'none'
                })
              } else {
                $('.childTable')[0].style.display = 'none'
                $('.customized_desc').each(function () {
                  $(this)[0].style.display = 'block'
                })
              }
            })
          } else {
            if (message === 'edit') {
              this.editFlag = true
            } else {
              this.editFlag = false
            }
            this.addFlag = false
            this.getOnlineJson(res.result, this.dataId)
          }
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
          this.commitToDatabase(data) //将数据存储到online数据库中
        })
        .catch((e) => {
          this.$message.error(e)
        })
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
      if (this.addFlag) {
        datajson.status = '0'
        o_postAction('/cgform/api/form/' + onlineId, datajson)
          .then((res) => {
            _this.visible = false
            this.$message.success('提交成功')
            _this.$emit('close-annTask')
          })
          .catch((err) => {
            this.$message.error('提交失败')

            console.log(err)
          })
      } else {
        datajson.id = this.dataId
        datajson.status = '0'
        if ($('.is_customized').find('input[type=radio]:checked').val() === '0') {
          datajson.custormize_desc = ''
        } else {
          datajson.cict_order_detail = []
        }
        o_putAction('/cgform/api/form/' + onlineId + '?tabletype=1', datajson)
          .then((res) => {
            _this.visible = false
            this.$message.success('提交成功')
            _this.$emit('close-annTask')
          })
          .catch((err) => {
            this.$message.error('提交失败')
            console.log(err)
          })
      }
    },
    getOnlineJson(formJson, predataId) {
      //得到online的json
      o_getAction('/cgform/api/form/e8c9f23b81d74fc6a292c436d27faa70/' + predataId)
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

      this.$nextTick(() => {
        if ($('.is_customized').find('input[type=radio]:checked').val() === '0') {
          $('.childTable')[0].style.display = 'block'
          $('.customized_desc').each(function () {
            $(this)[0].style.display = 'none'
          })
        } else {
          // $('.childTable')[0].style.display = 'none'
          $('.customized_desc').each(function () {
            $(this)[0].style.display = 'block'
          })
        }
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
</style>