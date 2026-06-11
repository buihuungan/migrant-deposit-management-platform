<template>
  <div>
    <a-modal
      :visible="visible"
      :footer="null"
      width="95%"
      :zIndex="100"
      :closable="false"
      :destroyOnClose="true"
      dialogClass="dialogTest"
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
import GenerateForm from '../../../components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '../../../components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction } from '@/api/onApi.js'
import { w_postAction, w_postAction1 } from '../../../api/workapi'
import { tm_postAction } from '@/api/templateApi'
import { getAction } from '@/api/manage'
import Vue from 'vue'
import axios from 'axios'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
//import api from '@/api/index'
import api from '../../../api/index.js'
import { Message } from 'element-ui'
import sendwebmessage from '@/utils/sendwebmessage'
import { order_postAction } from '@/api/orderApi'

export default {
  name: 'AnnTask',
  components: { GenerateForm, AntdGenerateForm },
  data() {
    return {
      wareId: '',  //   0  代表学生正常上课创建的销售订单， 其他为老师预置课件数据时
      orderId: '',
      formDesignerId: '',
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
    setTimeout(() => {
      if (this.formDesignerId == '919e12ea043f412eb25ae8ef75b820b2') {
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
    openModal(formDesignerId, taskId, wareId, orderId) {
      this.orderId = ''
      this.wareId = wareId
      if(orderId)
      {
        this.orderId = orderId
      }
      this.formDesignerId = formDesignerId
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
          this.formJson = JSON.parse(res.result)
          this.$nextTick(() => {
            if (id == '919e12ea043f412eb25ae8ef75b820b2') {
              //console.log("$('.childTable')", $('.childTable')[0])
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
          this.visible = true
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
          this.commitToSalesTemplate(data) //将数据存储到对接模块销售订单模板数据库中
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    //完成该节点的任务，把该节点填写的表单id，online表id，online数据id传入工作流
    completeTask(onlineId, dataId) {
      var _this = this
      w_postAction1('/todo/completeTask?taskId=' + _this.taskId, {
        formDesignerId: _this.formDesignerId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
      })
    },
    // 提交的信息存入对接模块销售订单模板
    commitToSalesTemplate(data) {
      console.log('销售订单数据', data)
      var postData = {
        buyer: '',
        identity: '',
        order_status: '创建订单',
        product_information: '',
        receive_address: '',
        seller: '虹信',
        send_address: '武汉市',
        send_date: '',
        user_id: '',
        plan_id: '',
        lesson_id: '',
        teaching_mode: '',
      }
      postData.buyer = data.input_1608635632303
      postData.identity = '88.343.000000/' + data.autocode_1617024402235
      postData.product_information = data.table_dict_1617023068722 + '-基站天线'
      postData.receive_address = data.input_1608635636276
      postData.send_date = data.date_1608635658678
      var userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
      postData.user_id = userid.value
      var process = JSON.parse(window.localStorage.getItem('process'))
      postData.plan_id = process.planId
      postData.lesson_id = process.lessonId
      postData.teaching_mode = process.teachingMode
      tm_postAction('/cgform/api/form/be35e28f48244943933d3484239de3a7', postData).then((res) => {
        console.log('生菜采购订单插入模板成功')
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
      console.log('this.datajson',datajson)
      datajson.example_id = this.$store.state.exampleId
      // sendwebmessage('2',datajson.process_id)
      //console.log('处理后即将存入数据库数据json为：', JSON.stringify(datajson))
      // 0  代表学生正常上课创建的销售订单
      if(this.wareId == '0') {
        var process = JSON.parse(localStorage.getItem('process'));
        if(process){
          if(process.teachingMode == '1') {  //如果为单人模式，则设定为直接通过审核
            datajson.audit_status = '1'
          }
        }
        // datajson.audit_status = '1'
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
      }
      //    代表老师给课件预置的销售订单数据
      else{
        let datajson2 = {}
        for(let key in datajson) {
          var key2 = key.split("_")
          var result = key2[0]
          for(var i=1;i<key2.length;i++) {
            result = result + key2[i].slice(0,1).toUpperCase() + key2[i].slice(1);
          }
          datajson2[result] = datajson[key]
        }
        let cictOrderDetail2 = []
        datajson2.cictOrderDetail.forEach((item) => {
          var newObj = new Object()
          for(let key3 in item) {
            var key4 = key3.split("_")
            var result2 = key4[0]
            for(var j=1;j<key4.length;j++) {
             result2 = result2 + key4[j].slice(0,1).toUpperCase() + key4[j].slice(1);
            }
            newObj[result2] = item[key3]
          }
          cictOrderDetail2.push(newObj)
        })
        datajson2.cictOrderDetail = cictOrderDetail2
        datajson2.id = _this.orderId
        datajson2.auditStatus = 1
        datajson2.createBy = ''
        datajson2.createTime = ''
        datajson2.updateBy = ''
        datajson2.updateTime = ''
        datajson2.sysOrgCode = ''
        for(var k=0;k<datajson2.cictOrderDetail.length;k++) {
          datajson2.cictOrderDetail[k].id = ''
          datajson2.cictOrderDetail[k].price = 0
          datajson2.cictOrderDetail[k].orderId = ''
        }
        order_postAction(`/preset/insertInitData/${this.wareId}`,datajson2)
        .then(res =>{
          console.log('res',res)
          if(res.success) {
            this.$message.success('数据预置成功！')
          }
          else {
            this.$message.error(res.message)
          }
          _this.visible = false
        })
      }
      
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
