<template>
  <div>
    <a-modal
      :visible="visible"
      :footer="null"
      width="1300px"
      :zIndex="100"
      :closable="false"
      :destroyOnClose="true"
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
      <!-- 自定义关闭按钮 -->
      <!-- <a @click="close" class="custom-close-btn"
        style="position: absolute; right: 10px; top: 5px; font-size: 20px;color: black;">x</a> -->
      <div class="buttonstyle"><a-button class="xbutton" type="dashed" @click="close()">X</a-button></div>
    </a-modal>
  </div>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction, o_postAction1, o_postAction2 } from '@/api/onApi.js'
import { nw_postAction1, nw_delete, nw_postAction2 } from '@api/newWorkApi'
import { getSingleQYUser, getBusinessByCompanyName } from '@/api/userList'

export default {
  name: 'AnnTask',
  components: { GenerateForm, AntdGenerateForm },
  props: {
    getData: {
      type: Function,
      default: null,
    },
    userInfo: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      registerToken: '', //注册时传入的token
      isProcessingUnload: false, // 标志位，防止刷新页面时重复执行函数
      category: '',
      projectStatus: '',
      isOriginal: false,
      frontId: '',
      formDesignerId: '',
      onlineDataId: '',
      onlineTableId: '',
      taskId: '',
      processInstanceId: '',
      visible: false,
      useMoney: null,
      remainingAmount: null,
      totalMoney: null,
      supplementary: null,
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
          o_getAction('/cgform/api/getData/4e7015e9b56447d3a29c3fb392a90b49', params).then((res) => {
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
  updated() {},
  mounted() {},
  methods: {
    openModal(formDesignerId, onlineDataId, onlineTableId, taskId, processInstanceId, category, data) {
      console.log('category:', category)
      console.log('data:', data)
      console.log('onlineTableId:', onlineTableId)
      this.formDesignerId = formDesignerId
      this.onlineDataId = onlineDataId
      this.onlineTableId = onlineTableId
      this.taskId = taskId
      this.processInstanceId = processInstanceId
      this.category = category
      //存缴和原始不需要传frontId
      if (category === '存缴' || category === '原始' || category === '注册') {
        this.frontId = ''
      } else {
        this.frontId = data.frontId
      }
      //判断是否是原始流程,如果是原始的流程则没有getdata函数
      if (category === '原始') {
        this.isOriginal = true
      } else {
        this.isOriginal = false
      }
      if (category === '存缴') {
        this.projectStatus = data.projectStatus
      }

      //将注册时得到的token传入
      if (category === '注册') {
        this.registerToken = data
      }

      this.getForm(category, data)
    },
    //点击关闭按钮关闭
    close() {
      var _this = this
      this.$elementConfirm('是否关闭处理界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          _this.visible = false
          this.deleteFlow(this.processInstanceId)
            .then(() => {
              if (!this.isOriginal && this.getData) {
                this.getData() // 刷新父组件数据
              }
            })
            .catch((error) => {
              console.error('删除流程失败:', error)
            })
        })
        .catch(() => {})
    },

    //如果点击了返回，则需要将该流程删除
    async deleteFlow(record) {
      try {
        const res = await nw_delete('/process/deleteProcessInstanceFirst', {
          processInstanceId: record,
        })
        console.log('删除成功:', res)
      } catch (err) {
        console.error('删除流程时出错:', err)
      }
    },

    //得到表单
    getForm(category, data) {
      var id = this.formDesignerId
      t_getAction('/admin/desform/' + id + '/getConent')
        .then((res) => {
          const formJson = JSON.parse(res.result)
          if (this.onlineTableId != '' && this.onlineDataId != '') {
            this.getOnlineJson(this.onlineTableId, this.onlineDataId, formJson)
          } else {
            this.formJson = formJson
            this.visible = true
            this.$nextTick(() => {
              if (this.$refs.generateForm) {
                if (category === '存缴') {
                  let userData = {}
                  getSingleQYUser(this.userInfo.id)
                    .then((res) => {
                      userData = res.result
                      console.log('userData', userData)
                      const licenseCopy = JSON.stringify([
                        {
                          fileName: userData.licenseCopy.split('/').pop(),
                          dbpath: userData.licenseCopy,
                        },
                      ])
                      console.log('图片地址开始上传')
                      //将公司名称默认填写为当前登录用户的公司名称
                      this.$refs.generateForm.setData({
                        company_name: userData.companyName, //这里的company_name是表单中的字段标识
                        deposit_way: data.projectStatus,
                        // postalAddress: userData.postalAddress,
                        // postcode: userData.postcode,
                        // creditCode: userData.creditCode,
                        licenseCopy: licenseCopy,
                      })
                      console.log('图片地址上传成功')
                    })
                    .catch((error) => {
                      console.error('获取用户数据失败', error)
                    })
                } else if (category === '使用') {
                  this.remainingAmount = data.remainingAmount
                  console.log('保证金使用的数据', data)
                  let userData = {}
                  getBusinessByCompanyName(data.companyName)
                    .then((res) => {
                      userData = res.result
                      console.log('使用的userData', userData)
                      this.$refs.generateForm.setData({
                        company_name: data.companyName,
                        credit_code: userData.creditCode,
                        postal_address: userData.postalAddress,
                        post_code: userData.postcode,
                        project_contact: data.responsiblePerson,
                        project_mobile: data.mobile,
                        project_name: data.projectName,
                        project_address: data.projectAddress,
                        address_detail: data.addressDetail,
                        representative: userData.representative,
                        phone: userData.phone,
                        agency: data.agency,
                      })
                    })
                    .catch((error) => {
                      console.error('获取用户数据失败', error)
                    })
                } else if (category === '变更') {
                  let userData = {}
                  getSingleQYUser(this.userInfo.id)
                    .then((res) => {
                      userData = res.result
                      console.log('userData', userData)
                      this.$refs.generateForm.setData({
                        company_name: userData.companyName, //这里的company_name是表单中的字段标识
                        credit_code: userData.creditCode,
                        company_address: userData.postalAddress,
                        postal_code: userData.postcode,
                        project_name: data.projectName,
                        project_address: data.projectAddress,
                        address_detail: data.addressDetail,
                        money: data.contractAmount,
                        start_date: data.formCreateDate,
                        end_date: data.formEndDate,
                        // reason:,
                        project_contact: data.responsiblePerson,
                        project_mobile: data.mobile,
                        proportions: data.proportions,
                        old_deposit_method: data.depositWay,
                        new_deposit_method: data.newProjectStatus,
                        reason: data.reason,
                        proportions: data.proportions,
                        ensure_money: data.Money,
                      })
                    })
                    .catch((error) => {
                      console.error('获取用户数据失败', error)
                    })
                } else if (category === '补缴') {
                  console.log('保证金补缴的数据', data)
                  this.totalMoney = data.Money
                  this.remainingAmount = data.remainingAmount
                  let userData = {}
                  getSingleQYUser(this.userInfo.id)
                    .then((res) => {
                      userData = res.result
                      console.log('userData', userData)
                      this.$refs.generateForm.setData({
                        company_name: userData.companyName,
                        credit_code: userData.creditCode,
                        postal_address: userData.postalAddress,
                        post_code: userData.postcode,
                        project_contact: data.responsiblePerson,
                        project_mobile: data.mobile,
                        project_name: data.projectName,
                        project_address: data.projectAddress,
                        address_detail: data.addressDetail,
                        money: data.Money,
                        remaining_amount: data.remainingAmount,
                      })
                    })
                    .catch((error) => {
                      console.error('获取用户数据失败', error)
                    })
                } else if (category === '更换') {
                  console.log('保证金更换的数据', data)
                  let userData = {}
                  getSingleQYUser(this.userInfo.id)
                    .then((res) => {
                      userData = res.result
                      console.log('userData', userData)
                      this.$refs.generateForm.setData({
                        company_name: userData.companyName,
                        credit_code: userData.creditCode,
                        postal_address: userData.postalAddress,
                        post_code: userData.postcode,
                        project_contact: data.responsiblePerson,
                        project_mobile: data.mobile,
                        project_name: data.projectName,
                        project_address: data.projectAddress,
                        address_detail: data.addressDetail,
                        money: data.contractAmount,
                        start_date: data.formCreateDate,
                        end_date: data.formEndDate,
                        deposit_method: data.depositWay,
                        reason: data.reason,
                        proportions: data.proportions,
                        ensure_money: data.Money,
                      })
                    })
                    .catch((error) => {
                      console.error('获取用户数据失败', error)
                    })
                } else if (category === '返还') {
                  console.log('保证金返还的数据', data)
                  let userData = {}
                  getSingleQYUser(this.userInfo.id)
                    .then((res) => {
                      userData = res.result
                      console.log('userData', userData)
                      this.$refs.generateForm.setData({
                        company_name: userData.companyName,
                        project_name: data.projectName,
                        project_address: data.projectAddress,
                        address_detail: data.addressDetail,
                        deposit_method: data.depositWay,
                        money: data.Money,
                        return_amount: data.remainingAmount,
                        storage_start_date: data.storageStartDate,
                        storage_end_date: data.storageEndDate,
                      })
                    })
                    .catch((error) => {
                      console.error('获取用户数据失败', error)
                    })
                }
              }
            })
          }
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
          if (this.category === '使用') {
            this.useMoney = data.use_money
            this.supplementary = data.supplementary
            console.log('使用金额:', this.useMoney)
            console.log('剩余金额:', this.remainingAmount)
            console.log('总金额:', this.totalMoney)
            // 新增校验逻辑：在提交前检查金额
            if (Number(this.useMoney) > Number(this.remainingAmount)) {
              this.$message.error('使用金额不能大于剩余金额')
              return // 直接返回，不执行后续操作
            }
          }
          if (this.category === '补缴') {
            console.log('补缴金额:', this.supplementary)
            console.log('剩余金额:', this.remainingAmount)
            console.log('总金额:', this.totalMoney)
            if (Number(this.supplementary) + Number(this.remainingAmount) > Number(this.totalMoney)) {
              this.$message.error('补缴后金额不能大于保证金金额')
              return // 直接返回，不执行后续操作
            }
          }
          this.commitToDatabase(data) //将数据存储到online数据库中
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    //完成该节点的任务，把该节点填写的表单id，online表id，online数据id传入工作流
    completeTask(onlineId, dataId) {
      var _this = this
      let params = {
        taskId: _this.taskId,
        onlineTableId: onlineId,
        onlineDataId: dataId,
      }
      // if (this.category === '存缴') {
      //   params.depositWay = this.projectStatus
      // }
      if (this.frontId) {
        params.frontId = this.frontId
      }
      if (this.category === '注册') {
        nw_postAction2('/task/complete', params, this.registerToken)
          .then((res) => {
            if (res.result.result) {
              _this.$message.success('通过成功')
              this.$nextTick(() => {
                // this.getData()
                this.$bus.$emit('callGetTotal')
              })
            } else {
              _this.$message.error('通过失败')
              console.log('出错')
            }
          })
          .catch((err) => {
            this.deleteFlow(this.processInstanceId)
            console.log(err)
          })
      } else {
        nw_postAction1('/task/complete', params)
          .then((res) => {
            if (res.result.result) {
              _this.$message.success('通过成功')
              this.$nextTick(() => {
                this.getData()
                this.$bus.$emit('callGetTotal')
              })
            } else {
              _this.$message.error('通过失败')
              console.log('出错')
            }
          })
          .catch((err) => {
            this.deleteFlow(this.processInstanceId)
            console.log(err)
          })
      }
    },

    //保存数据的接口
    // saveMarginData(onlineId, dataId) {
    //   let params = {
    //     taskId: this.taskId,
    //     onlineTableId: onlineId,
    //     onlineDataId: dataId,
    //   }

    //   nw_postAction1('/margin/saveMarginData', params)
    //     .then((res) => {
    //       console.log('保存数据的接口返回值', res)
    //       let mainId = res.result.mainId
    //       this.$nextTick(() => {
    //         this.completeTask(onlineId, dataId)
    //       })
    //     })
    //     .catch((err) => {
    //       console.log(err)
    //     })
    // },

    //得到online的data
    getOnlineJson(onlineTableId, onlineDataId, formJson) {
      //得到online的json
      o_getAction('/cgform/api/form/' + onlineTableId + '/' + onlineDataId)
        .then((res) => {
          var onlineJson = res.result
          console.log('onlineJson', onlineJson)
          this.$nextTick(() => {
            if (onlineJson != null && onlineJson != '') {
              this.connData(onlineJson, formJson)
            } else {
              this.formJson = formJson
              this.visible = true
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
        this.visible = true
      })
    },
    //提交的信息存入数据库
    commitToDatabase(commitdata) {
    console.log('commitdata123123123123:', commitdata);
    
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
      console.log('this.category:', this.category)

      if (this.category === '注册') {
        o_postAction1('/cgform/api/form/' + onlineId, datajson, this.registerToken)
          .then((res) => {
            const data = res.result
            o_postAction2(`/user/checkUsername?onlineTableId=${onlineId}&onlineDataId=${data}`, this.registerToken)
              .then((res) => {
                console.log('res:', res)
                if (res.success) {
                  this.visible = false
                  this.completeTask(onlineId, data)
                  this.$message.success('提交成功')
                } else {
                  this.$message.error('提交失败，用户账号或企业名称已存在，请修改用户账号后重新提交')
                }
              })
              .catch((err) => {
                console.log(err)
              })
          })
          .catch((err) => {
            this.$message.error('提交失败')
            console.log(err)
          })
      } else {
        o_postAction('/cgform/api/form/' + onlineId, datajson)
          .then((res) => {
            this.visible = false
            this.completeTask(onlineId, res.result)
            this.$message.success('提交成功')
          })
          .catch((err) => {
            this.$message.error('提交失败')
            console.log(err)
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
  border: 1px solid #dcdcdc;
  border-radius: 5px;
  margin: 26px 0 auto;
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
  margin-top: 25px;
  width: 97%;
}

.submitBtn {
  margin-top: 20px;
}

.submitBtn button {
  margin: 20px auto;
  left: 45%;
}

.buttonstyle {
  position: absolute;
  top: 0;
  right: 0;
  margin: 10px;
}
</style>