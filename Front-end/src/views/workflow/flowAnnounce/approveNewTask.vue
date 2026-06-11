<template>
  <div id="newformContent">
    <!-- <div class="buttonstyle"><a-button type="dashed" @click="close()">X</a-button></div> -->
    <div class="formbody">
      <div class="newForm">
        <generate-form style="margin: 0 auto" insite="true" :remote="func1" :data="newForm" ref="generateForm">
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>
      </div>
      <div class="submitBtn">
        <a-button type="primary" @click="handleTest('1')" style="margin-right: 20px">通过</a-button>
        <a-button type="primary" @click="handleTest('0')" style="margin-right: 20px">拒绝</a-button>
        <a-popover title="请选择退回到哪一个节点">
          <template slot="content">
            <!-- <a-button type="dashed" @click="handleTest('2')"> 第一个节点 </a-button> -->
            <a-button type="dashed" @click="handleTest('3')" style="margin-left: 10px"> 上一个节点 </a-button>
          </template>
          <a-button type="primary">退回</a-button>
        </a-popover>
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
  name: 'AnnNewTask',
  components: { GenerateForm, AntdGenerateForm },
  props: ['nowflowId', 'taskId', 'recordLastForm', 'record'],
  data() {
    return {
      process_id: '', //e8c9f23b81d74fc6a292c436d27faa70的流程编号
      newForm: {},
      func1: {
        //获得所有的库存地址
        func_1615188754467(resolve) {
          console.log('走到这里')
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
      if (this.nowflowId == '919e12ea043f412eb25ae8ef75b820b2') {
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
  mounted() {
    this.getNowFrom()
  },
  methods: {
    //定制化操作，拿到e8c9f23b81d74fc6a292c436d27faa70的流程编号
    getId() {
      var tableId = this.recordLastForm.prevOnline_table_id
      var dataId = this.recordLastForm.prevOnline_data_id
      o_getAction('/cgform/api/form/' + tableId + '/' + dataId).then((res) => {
        this.process_id = res.result['process_id']
      })
    },
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
        .catch(() => {})
    },
    getNowFrom() {
      if (
        this.recordLastForm != undefined &&
        this.recordLastForm.prevOnline_table_id == 'e8c9f23b81d74fc6a292c436d27faa70'
      ) {
        this.getId()
      }
      //得到一个现在节点的表单
      var nowid = this.nowflowId
      t_getAction('/admin/desform/' + nowid + '/getConent')
        //w_getAction('/business/getForm/' + id)
        .then((res) => {
          this.newForm = JSON.parse(res.result)
          this.$nextTick(() => {
            if (nowid == '919e12ea043f412eb25ae8ef75b820b2') {
              if ($('.is_customized').find('input[type=radio]:checked').val() === '0') {
                $('.childTable')[0].style.display = 'block'
                $('.customized_desc').each(function () {
                  $(this)[0].style.display = 'none'
                })

                // $('.customized_desc1')[0].style.display = 'none'
                // $('.customized_desc2')[0].style.display = 'none'
              } else {
                $('.childTable')[0].style.display = 'none'
                $('.customized_desc').each(function () {
                  $(this)[0].style.display = 'block'
                })
                // $('.customized_desc1')[0].style.display = 'block'
                // $('.customized_desc2')[0].style.display = 'block'
              }
            }
          })
          // for (let i = 0; i < _this.allJson.length; i++) {
          //   _this.connData(_this.allJson[i].formData, _this.allJson[i].formDesigner, i)
          // }
          // console.log(this.formData[0])
          // _this.newForm = res.result.nowNodeFormDesigner.formDesignerJson
        })
        .catch((err) => {
          console.log(err)
        })
    },
    rollback(onlineId, dataId, rollbackState) {
      //退回操作
      var _this = this
      w_postAction1('/todo/rollbackToFirstTask?taskId=' + _this.taskId + '&rollbackState=' + rollbackState, {
        formDesignerId: this.nowflowId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
      })
        .then((res) => {
          console.log(res)
          _this.$message.success('退回成功')
        })
        .catch((err) => {
          console.log(err)
          _this.$message.success('退回失败')
        })
    },
    reject(onlineId, dataId, stateflag) {
      //拒绝操作
      var _this = this
      w_postAction1('/todo/rejectTask?taskId=' + _this.taskId, {
        formDesignerId: this.nowflowId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
        resultCode: stateflag,
      })
        .then((res) => {
          _this.$message.success('拒绝成功')
          console.log(res)
        })
        .catch((err) => {
          _this.$message.error('拒绝失败')
          console.log(err)
        })
    },
    handleTest(stateflag) {
      let { 0: username } = this.record.processUniqueName.split('-')
      //通过,拒绝操作
      const $form = this.newForm.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          this.commitToDatabase(data, stateflag)
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    commitToDatabase(commitdata, stateflag) {
      var _this = this
      //将数据提交到online表单数据库
      var _this = this
      // console.log('执行提交至数据库功能')
      // console.log('提交的数据为', commitdata)
      // console.log('当前提交数据绑定的表单json数据newForm为：', this.newForm)
      let name = this.newForm.config.dataBase
      var onlineId = this.newForm.config.tableId
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
      //   datajson[this.newForm.list[this.newForm.list.findIndex((item) => item.model === commitdataarray[i])].tableCol] =
      //     commitdata[commitdataarray[i]]
      // }
      for (let i = 0; i < this.newForm.list.length; i++) {
        if (this.newForm.list[i].type == 'grid') {
          for (let j = 0; j < this.newForm.list[i].columns.length; j++) {
            for (let z = 0; z < this.newForm.list[i].columns[j].list.length; z++) {
              let model1 = this.newForm.list[i].columns[j].list[z].model
              let tableCol1 = this.newForm.list[i].columns[j].list[z].tableCol
              datajson[tableCol1.toString()] = commitdata[model1]
            }
          }
        } else if (this.newForm.list[i].type == 'divider') {
          // let model = this.newForm.list[i].model
          // let key = this.newForm.list[i].key
          // datajson[key.toString()] = commitdata[model]
        } else if (this.newForm.list[i].type == 'table') {
          let schedule = this.newForm.list[i].schedule
          let array = new Array()
          for (let k = 0; k < commitdata[this.newForm.list[i].model].length; k++) {
            let datajson1 = {}
            for (let h = 0; h < this.newForm.list[i].tableColumns.length; h++) {
              let model2 = this.newForm.list[i].tableColumns[h].model
              let tableCol2 = this.newForm.list[i].tableColumns[h].tableCol
              datajson1[tableCol2.toString()] = commitdata[this.newForm.list[i].model][k][model2]
            }
            array.push(datajson1)
          }
          datajson[this.newForm.list[i].schedule.toString()] = array
        } else {
          let model = this.newForm.list[i].model
          let tableCol = this.newForm.list[i].tableCol
          datajson[tableCol.toString()] = commitdata[model]
        }
      }
      o_postAction('/cgform/api/form/' + onlineId, datajson)
        //w_postAction('/business/completeTask/' + this.flowId, datajson)
        .then((res) => {
          //stateflag:0 拒绝 ;1 通过;2 退回
          if (stateflag == '0') {
            this.reject(onlineId, res.result, stateflag)
            setTimeout(function () {
              _this.$emit('close-approveTask')
            }, 3000)
          } else if (stateflag == '1') {
            this.completeTask(onlineId, res.result, stateflag)
            console.log('completeTask' + '---------')
            setTimeout(function () {
              _this.$emit('close-approveTask')
            }, 3000)
          } else if (stateflag == '2') {
            const rollbackstate = '0' //0:退回到第一个节点，1：退回到上一个节点
            this.rollback(onlineId, res.result, rollbackstate)
            setTimeout(function () {
              _this.$emit('close-approveTask')
            }, 3000)
          } else if (stateflag == '3') {
            const rollbackstate = '1'
            this.rollback(onlineId, res.result, rollbackstate)
            setTimeout(function () {
              _this.$emit('close-approveTask')
            }, 3000)
          }
          // switch (stateflag) {
          //   case '0':
          //     this.reject()
          //      _this.$emit('close-annTask')
          //      _this.$emit('close-approveNewTask')
          //     break
          //   case '1':
          //     this.completeTask(onlineId, res.result, stateflag);
          //     console.log('completeTask'+'---------');
          //      _this.$emit('close-annTask')
          //      _this.$emit('close-approveNewTask')
          //     break
          //   case '2':
          //     this.rollback()
          //      _this.$emit('close-annTask')
          //      _this.$emit('close-approveNewTask')
          //     break
          //   default:
          //     break
          // }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    completeTask(onlineId, dataId, stateflag) {
      var _this = this
      w_postAction1('/todo/completeTask?taskId=' + _this.taskId, {
        formDesignerId: this.nowflowId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
        resultCode: stateflag,
      })
        .then((res) => {
          _this.$message.success('通过成功')
          console.log(res)
        })
        .catch((err) => {
          _this.$message.error('通过失败')
          console.log(err)
        })
    },
  },
}
</script>
<style scoped>
#newformContent {
  /* width:80%; */
  display: flex;
  justify-content: center;
  padding: 5px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin: 20px auto;
}
.formbody {
  margin-top: 20px;
  width: 100%;
}
.submitBtn {
  margin-top: 20px;
  /* display: flex; */
  /* justify-content: space-between; */
}
.submitBtn button {
  margin: 20px auto;
  left: 40%;
}
</style>