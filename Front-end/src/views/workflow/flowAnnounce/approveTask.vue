<template>
  <div>
    <div id="approveTaskContent">
      <!-- <div class="buttonstyle"><a-button type="dashed" @click="close()">X</a-button></div> -->
      <!-- <div class="title">
      <h1>{{ flowName }}</h1>
    </div> -->
      <div class="formbody">
        <!-- <div v-for="(dataForm,index) in formData" :key="index+'data'"> -->
        <div>
          <generate-form
            style="margin: 0 auto"
            insite="true"
            :data="dataForm"
            ref="generateForm"
            :remote="func1"
            :edit="false"
          >
            <template v-slot:blank="scope">
              Width
              <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
              <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
            </template>
          </generate-form>
        </div>
      </div>
    </div> 
      <!-- <div class="submitBtn">
      <a-button type="primary" @click="handleTest('1')">通过</a-button>
      <a-button type="dashed" @click="reject()">拒绝</a-button>
      <a-button type="primary" @click="rollback()">退回</a-button>
    </div> -->
    </div>
  </div>
</template>
<script>
import GenerateForm from '../../../components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '../../../components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import { w_postAction, w_getAction } from '../../../api/workapi'
import { getAction } from '@/api/manage'

import Vue from 'vue'
import axios from 'axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import api from '@/api/index'
import { Message } from 'element-ui'

export default {
  name: 'ApproveTask',
  components: { GenerateForm, AntdGenerateForm },
  props: ['preflowId', 'flowName', 'nowflowId', 'predataId', 'pretableId', 'taskId'],
  data() {
    return {
      onlineJson: [],
      allJson: [],
      newForm: {},
      dataForm: {},
      formData: [],
      dialogVisible: false,
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
    }
  },
  updated() {
    // setTimeout(() => {
    //   $('.is_customized')
    //     .find('input[type=radio]')
    //     .change(function () {
    //       if ($('.is_customized').find('input[type=radio]:checked').val() === '0') {
    //         $('.childTable')[0].style.display = 'block'
    //         $('.customized_desc').each(function () {
    //           $(this)[0].style.display = 'none'
    //         })
    //       } else {
    //         $('.childTable')[0].style.display = 'none'
    //         $('.customized_desc').each(function () {
    //           $(this)[0].style.display = 'block'
    //         })
    //       }
    //     })
    // }, 0)
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
          _this.$emit('close-approveTask')
        })
        .catch(() => { })
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
          //     } else {
          //       $('.childTable')[0].style.display = 'none'
          //       $('.customized_desc').each(function () {
          //         $(this)[0].style.display = 'block'
          //       })
          //     }
          //   }
          // })
          this.getOnlineJson(this.pretableId, this.predataId)
          // for (let i = 0; i < _this.preJson.length; i++) {
          //   _this.connData(_this.preJson[i].formData, _this.preJson[i].formDesigner, i)
          // }
          //console.log(this.formData[0])
          // _this.newForm = res.result.nowNodeFormDesigner.formDesignerJson
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getOnlineJson(pretableId, predataId) {
      //得到online的json
      o_getAction('/cgform/api/form/' + pretableId + '/' + predataId)
        .then((res) => {
          console.log(pretableId + '/' + predataId);
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
      //拼接online键值配对
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
          for (let k = 0; k < onlineJson[this.preJson.list[i].schedule].length; k++) {
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
      // for (var key in onlineJson) {
      //   for (var i = 0; i < preJson.list.length; i++) {
      //     // console.log("6666",onlineJson[key])
      //     if (preJson.list[i].tableCol == key) {
      //       preJson.list[i].options.defaultValue = onlineJson[key]
      //     }
      //   }
      // }
      // console.log(onlineJson);
      this.dataForm = preJson
      console.log('this.dataForm', this.dataForm);
      this.onlineJson = onlineJson
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
    },
  },
  mounted() {
    this.getForm()
  },
}
</script>
<style scoped>
#approveTaskContent {
  display: flex;
  justify-content: center;
  padding: 5px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin: 0 auto;
  cursor: pointer;
  z-index: 9999;
  /* pointer-events: none; */
  user-select: none;
}
/* .formbody .el-input__suffix-inner {
  pointer-events: none;
} */
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
  left: 46%;
}
.buttonstyle {
  margin-left: 670px;
}
</style>