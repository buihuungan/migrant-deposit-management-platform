<template>
  <a-modal
    :centered="true"
    :visible="visible"
    :footer="null"
    width="90%"
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
      <div class="submitBtn" v-if="!shopFlag">
        <a-button type="primary" v-if="editFlag" @click="handleTest()" style="margin-right: 20px">提交</a-button>
        <a-button type="primary" @click="close()">返回</a-button>
      </div>
      <div class="shop" v-if="shopFlag">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="2" :sm="2"></a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="发货人姓名:">
                <a-input placeholder="请输入发货人姓名" v-model="deliver_goods.shipper_name"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="3" :sm="3"></a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="发货人所在城市:">
                <a-input placeholder="请输入发货人所在城市" v-model="deliver_goods.shipper_city"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="2" :sm="2"></a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="2" :sm="2"></a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="收货人姓名:">
                <a-input placeholder="请输入收货人姓名" v-model="deliver_goods.consignee_name"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="3" :sm="3"></a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="收货人所在城市:">
                <a-input placeholder="请输入收货人所在城市" v-model="deliver_goods.consignee_city"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="2" :sm="2"></a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="2" :sm="2"></a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="收货人电话:">
                <a-input placeholder="请输入收货人电话" v-model="deliver_goods.consignee_phone"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="14" :sm="14"></a-col>
          </a-row>
        </a-form>
        <div class="submitBtn">
          <a-button type="primary" @click="shop()" style="margin-right: 20px">发货</a-button>
          <a-button type="primary" @click="close()">返回</a-button>
        </div>
      </div>
    </div>
  </a-modal>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import { t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction, o_putAction } from '@/api/onApi.js'
import { erp_getAction, erp_postAction } from '@/api/erpApi.js'
export default {
  name: 'salesModal',
  components: { GenerateForm },
  props: ['flowId'],
  data() {
    return {
      orderInfoId: 'e8c9f23b81d74fc6a292c436d27faa70', //订单信息表id
      recordInfo:{}, //暂存订单信息
      formJson: {},
      shopFlag: false,
      visible: false,
      destroy: true,
      editFlag: true,
      deliver_goods: {
        delivery_party_addr: '',
        order_name: '',
        order_no: '',
        plan_date: '',
        seller_name: '',
        process_id: '',
        shipper_name: '',
        shipper_city: '',
        consignee_name: '',
        consignee_city: '',
        consignee_phone: '',
      },
      addFlag: false,
      onlineJson: {},
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
    shop() {
      console.log(this.deliver_goods)
      let params = {
        goodsList: [],
        orderId: '',
      }
      params.orderId = this.onlineJson.id
      var materials = this.onlineJson.cict_order_detail
      for (var i = 0; i < materials.length; i++) {
        var material = {}
        material.prodAmount = materials[i].prod_amount
        material.prodId = materials[i].prod_id
        material.prodName = materials[i].prod_name
        material.prodUnit = materials[i].prod_unit
        params.goodsList.push(material)
      }
      console.log('/cictOrder/deliverGoods', params)
      erp_postAction('/cictOrder/deliverGoods', params)
        .then((res) => {
          console.log('res', res.result.deliverSuccess)
          if (res.result.deliverSuccess) {
            this.$message.success('发货成功')
            // 发货成功修改订单状态
            console.log('this.record信息' , this.recordInfo);
            var params1= {}
            params1.id = this.recordInfo.id
            params1.create_by = this.recordInfo.create_by
            params1.create_time = this.recordInfo.create_time
            params1.update_by = this.recordInfo.update_by
            params1.update_time = this.recordInfo.update_time
            params1.sys_org_code = this.recordInfo.sys_org_code
            params1.process_id = this.recordInfo.process_id
            params1.order_director = this.recordInfo.order_director
            params1.rec_type = this.recordInfo.rec_type
            params1.order_date = this.recordInfo.order_date
            params1.promoter_phone = this.recordInfo.promoter_phone
            params1.seller_id = this.recordInfo.seller_id
            params1.seller_name = this.recordInfo.seller_name
            params1.delivery_party_name = this.recordInfo.delivery_party_name
            params1.delivery_party_addr = this.recordInfo.delivery_party_addr
            params1.request_date = this.recordInfo.request_date
            params1.plan_date = this.recordInfo.plan_date
            params1.account_order = this.recordInfo.account_order
            params1.receiver_name = this.recordInfo.receiver_name
            params1.receiver_phone = this.recordInfo.receiver_phone
            params1.remark = this.recordInfo.remark
            params1.audit_status = this.recordInfo.audit_status
            params1.is_custormize = this.recordInfo.is_custormize
            params1.custormize_basic = this.recordInfo.custormize_basic
            params1.custormize_color = this.recordInfo.custormize_color
            params1.custormize_shape = this.recordInfo.custormize_shape
            params1.custormize_power = this.recordInfo.custormize_power
            params1.custormize_amount = this.recordInfo.custormize_amount
            params1.custormize_flag = this.recordInfo.custormize_flag
            params1.order_name = this.recordInfo.order_name
            params1.example_id = this.recordInfo.example_id
            params1.order_state = 5
            o_putAction(`/cgform/api/form/` + this.orderInfoId , params1)
              .then( (res) => {
                console.log('orderpost接口',res)
                this.$emit('loadData')
              })
            o_postAction('/cgform/api/form/846ae23ba9de49009b356a404a070c9e', this.deliver_goods).then((res) => {
              this.$message.success('保存发货信息成功')
            })
            console.log('res11',res)

            this.visible=false

          } else {
            if (res.result.needGoodsList != []) {
              var needGoodsList = res.result.needGoodsList
              var message = '物料不足，还需要:'
              for (var j = 0; j < needGoodsList.length; j++) {
                message += needGoodsList[j].prodName.toString() + needGoodsList[j].prodAmount.toString()+';'
              }
              this.$message.error(message)
            }
          }
        })
        .catch((err) => {
          console.log('err', err)
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
          _this.visible = false
          _this.editFlag = true
          _this.$emit('close-annTask')
        })
        .catch(() => {})
    },
    getForm(record, message) {
      this.recordInfo = record
      // console.log('record信息' , record);
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
            console.log('message', message)
            if (message === 'detail') {
              this.shopFlag = false
              this.editFlag = false
            }
            if (message === 'shop') {
              this.shopFlag = true
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
    randomString(e) {
      e = e || 5
      var t = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678',
        a = t.length,
        n = ''
      for (let i = 0; i < e; i++) n += t.charAt(Math.floor(Math.random() * a))
      return n
    },
    getOnlineJson(formJson, predataId) {
      //得到online的json
      o_getAction('/cgform/api/form/e8c9f23b81d74fc6a292c436d27faa70/' + predataId)
        .then((res) => {
          console.log(res.result)
          var result = res.result
          if (result != {}) {
            console.log(result.delivery_party_addr)
            this.deliver_goods.delivery_party_addr = result.delivery_party_addr
            this.deliver_goods.order_name = result.order_name
            this.deliver_goods.order_no = this.randomString(9)
            this.deliver_goods.plan_date = result.plan_date
            this.deliver_goods.seller_name = result.seller_name
            this.deliver_goods.process_id = result.process_id
          }
          this.onlineJson = res.result
          this.connData(this.onlineJson, JSON.parse(formJson))
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