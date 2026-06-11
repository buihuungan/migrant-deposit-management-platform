<template>
  <div id="formContent">
    <!-- <div class="buttonstyle"><a-button type="dashed" @click="close()">X</a-button></div> -->
    <!-- <div class="title">
      <h1>{{ flowName }}</h1>
    </div> -->
    <div class="formbody">
      <!-- <div v-for="(dataForm,index) in formData" :key="index+'data'"> -->
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
import GenerateForm from '../../../components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '../../../components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import { w_postAction, w_postAction1 } from '../../../api/workapi'
import { getAction } from '@/api/manage'
import Vue from 'vue'
import axios from 'axios'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
import api from '@/api/index'
import { Message } from 'element-ui'

export default {
  name: 'RollbackTask',
  components: { GenerateForm, AntdGenerateForm },
  props: ['preflowId', 'flowName', 'predataId', 'pretableId', 'taskId'],
  data() {
    return {
      allJson: [],
      dataForm: {},
      func1: {
        //获得所有的库存地址
        func_1615188754467(resolve) {
          let params = {
            column: 'createTime',
            order: 'desc',
            pageNo: 1,
            pageSize: 1000,
            superQueryMatchType: 'and',
          }
          getAction('/online/cgform/api/getData/4e7015e9b56447d3a29c3fb392a90b49', params).then((res) => {
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
      formData: [],
      dialogVisible: false,
    }
  },
  updated() {
    setTimeout(() => {
      if (this.preflowId == '919e12ea043f412eb25ae8ef75b820b2') {
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
      }
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
          _this.$emit('close-approveRollTask')
        })
        .catch(() => {})
    },
    getForm() {
      //得到一个表单
      var _this = this
      var preid = this.preflowId
      t_getAction('/admin/desform/' + preid + '/getConent')
        //w_getAction('/business/getForm/' + id)
        .then((res) => {
          this.preJson = JSON.parse(res.result)
          // this.$nextTick(() => {
          //   if (preid == '919e12ea043f412eb25ae8ef75b820b2') {
          //     console.log("$('.customized_desc')", $('.customized_desc'))
          //     console.log("$('.childTable')", $('.childTable'))
          //     if ($('.is_customized').find('input[type=radio]:checked').val() === '0') {
          //       $('.childTable')[0].style.display = 'block'
          //       $('.customized_desc').each(function () {
          //         $(this)[0].style.display = 'none'
          //       })

          //       // $('.customized_desc1')[0].style.display = 'none'
          //       // $('.customized_desc2')[0].style.display = 'none'
          //     } else {
          //       $('.childTable')[0].style.display = 'none'
          //       $('.customized_desc').each(function () {
          //         $(this)[0].style.display = 'block'
          //       })
          //       // $('.customized_desc1')[0].style.display = 'block'
          //       // $('.customized_desc2')[0].style.display = 'block'
          //     }
          //   }
          // })
          this.getOnlineJson(this.pretableId, this.predataId)
          // for (let i = 0; i < _this.preJson.length; i++) {
          //   _this.connData(_this.preJson[i].formData, _this.preJson[i].formDesigner, i)
          // }
          //console.log(this.formData[0])
          // _this.dataForm = res.result.nowNodeFormDesigner.formDesignerJson
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getOnlineJson(pretableId, predataId) {
      //得到online的json
      o_getAction('/cgform/api/form/' + pretableId + '/' + predataId)
        .then((res) => {
          var onlineJson = res.result
          if (onlineJson != null && onlineJson != '') {
            this.connData(onlineJson, this.preJson)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //connData(formDataJson, dataJson, k) {
    connData(onlineJson, preJson) {
      for (let i = 0; i < this.preJson.list.length; i++) {
        if (this.preJson.list[i].type == 'grid') {
          for (let j = 0; j < this.preJson.list[i].columns.length; j++) {
            for (let z = 0; z < this.preJson.list[i].columns[j].list.length; z++) {
              if (this.preJson.list[i].columns[j].list[z].tableCol != '') {
                this.preJson.list[i].columns[j].list[z].options.defaultValue =
                  onlineJson[this.preJson.list[i].columns[j].list[z].tableCol]
              }
            }
          }
        } else if (this.preJson.list[i].type == 'table') {
          // for (let h = 0; h < this.preJson.list[i].tableColumns.length; h++) {
          //   this.preJson.list[i].tableColumns[h].options.defaultValue =
          //     onlineJson[this.preJson.list[i].tableColumns[h].tableCol]
          // }
          //console.log(onlineJson[this.preJson.list[i].schedule])
          for (let k = 0; k < onlineJson[this.preJson.list[i].schedule].length; k++) {
            //console.log(onlineJson[this.preJson.list[i].schedule][k])
            for (let h = 0; h < this.preJson.list[i].tableColumns.length; h++) {
              this.preJson.list[i].options.defaultValue = onlineJson[this.preJson.list[i].schedule]
            }
          }
        } else {
          if (this.preJson.list[i].tableCol != '') {
            this.preJson.list[i].options.defaultValue = onlineJson[this.preJson.list[i].tableCol]
            // if (this.preJson.list[i].type == 'fileupload') {
            //   var fileupload=JSON.parse(this.preJson.list[i].options.defaultValue)
            //   fileupload[0].dbpath="https://git.data.wust.edu.cn/api1/online/common/download"+fileupload[0].dbpath
            //   this.preJson.list[i].options.defaultValue=JSON.stringify(fileupload)
            // }
          }
        }
      }
      //console.log('我在这', preJson)
      // console.log(onlineJson);
      this.dataForm = preJson
      this.$nextTick(() => {
        if (this.preflowId == '919e12ea043f412eb25ae8ef75b820b2') {
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
        }
      })
      //this.formData[k] = preJson
      // let vm = this
      // console.log('拼接数据' + k)
      // for (var key in formDataJson) {
      //   for (var i = 0; i < dataJson.list.length; i++) {
      //     if (dataJson.list[i].tableCol == key) {
      //       dataJson.list[i].options.defaultValue = formDataJson[key]
      //     }
      //   }
      // }
      //this.formData[k] = dataJson
    },
    handleTest(stateflag) {
      //通过stateflag：1
      const $form = this.dataForm.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          //console.log(data, '提交出去')
          this.commitToDatabase(data, stateflag)
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    commitToDatabase(commitdata, stateflag) {
      var _this = this
      //将数据提交到online表单数据库
      //console.log('commitToDatabase')
      var _this = this
      // console.log('执行提交至数据库功能')
      // console.log('提交的数据为', commitdata)
      // console.log('当前提交数据绑定的表单json数据newForm为：', this.dataForm)
      let name = this.dataForm.config.dataBase
      var onlineId = this.dataForm.config.tableId
      //console.log('当前提交数据到数据库：', name)
      let datajson = {}

      let commitdataarray = Object.keys(commitdata) /* 赋值，只需运行一次后面调用无需再处理，加快速度 */
      // console.log(
      //   'Object.keys(commitdata)值为：',
      //   commitdataarray
      // ) /* 获取提交数据键值，因为本身获取为model加数值需要处理为tableCol加数值 */

      let commitdataarraylength = commitdataarray.length /* 获取数据长度 */

      //开始处理数据是否有空值
      // for (let m = 0; m < commitdataarraylength; m++) {
      //   console.log(commitdata[commitdataarray[m]])
      //   if (commitdata[commitdataarray[m]] == '') {
      //     this.dataisnull = true
      //   }
      // }
      // if (this.dataisnull) {
      //   Message.warning('有数据为空。请检查补填！')
      // }
      // if (commitdataarraylength < this.widgetForm.list.length) {
      //   Message.warning('有数据为空。请检查补填！')
      // }
      //处理数据是否有空值结束

      // for (let i = 0; i < commitdataarraylength; i++) {
      //   datajson[
      //     this.dataForm.list[this.dataForm.list.findIndex((item) => item.model === commitdataarray[i])].tableCol
      //   ] = commitdata[commitdataarray[i]]
      // }
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
        } else {
          let model = this.dataForm.list[i].model
          let tableCol = this.dataForm.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        }
      }
      console.log('处理后即将存入数据库数据json为：', JSON.stringify(datajson))
      o_postAction('/cgform/api/form/' + onlineId, datajson)
        //w_postAction('/business/completeTask/' + this.flowId, datajson)
        .then((res) => {
          this.completeTask(res.result)
          _this.$emit('close-approveRollTask')
        })
        .catch((err) => {
          console.log(err)
        })
    },
    completeTask(dataId) {
      var _this = this
      w_postAction1('/todo/completeTask?taskId=' + _this.taskId, {
        prevOnlineDataId: this.predataId,
        onlineDataId: dataId,
        completeAagin: '1',
      })
        .then((res) => {
          _this.$message.success('提交成功')
          console.log(res)
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
  left: 38%;
}
.buttonstyle {
  margin-left: 670px;
}
</style>