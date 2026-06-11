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
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="12" :sm="12">
            <a-form-item label="发货人姓名:">
              <a-input placeholder="请输入发货人姓名" v-model="deliver_goods.shipper_name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item label="发货人所在城市:">
              <a-input placeholder="请输入发货人所在城市" v-model="deliver_goods.shipper_city"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :md="12" :sm="12">
            <a-form-item label="收货人姓名:">
              <a-input placeholder="请输入收货人姓名" v-model="deliver_goods.consignee_name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="12">
            <a-form-item label="收货人所在城市:">
              <a-input placeholder="请输入收货人所在城市" v-model="deliver_goods.consignee_city"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :md="12" :sm="12">
            <a-form-item label="收货人电话号码:">
              <a-input placeholder="请输入收货人电话号码" v-model="deliver_goods.consignee_phone"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <div class="formbody">
        <generate-form
          style="margin: 0 auto"
          insite="true"
          :data="formJson"
          ref="generateForm"
          :edit="false"
        >
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>
      </div>
      <div class="submitBtn">
        <a-button type="primary" @click="close()">点击发货</a-button>
      </div>
    </div>
  </a-modal>
</template>
<script>
import GenerateForm from '../FormMaking/components/GenerateForm'
import AntdGenerateForm from '../FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { e_postAction } from '@/api/eduApi.js'
import { o_postAction, o_getAction, o_putAction } from '@/api/onApi.js'
import { postAction } from '@api/manage'
import Vue from 'vue'
import axios from 'axios'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
import { order_postAction, order_getAction, order_putAction } from '@/api/orderApi'
import { USER_ID, USER_INFO } from '@/store/mutation-types'
//import api from '@/api/index'

export default {
  name: 'salesModal',
  components: { GenerateForm, AntdGenerateForm },
  data() {
    return {
      formJson: {},
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
      visible: false,
      destroy: true,
    }
  },
  methods: {
    close() {
      var _this = this
      _this.visible = false
      postAction('/online/cgform/api/form/846ae23ba9de49009b356a404a070c9e', this.deliver_goods).then((res) => {
        let userMessage = Vue.ls.get(USER_INFO)
        let params = {}
        params.userId = userMessage.id
        params.roleIds = []
        console.log(params)
        e_postAction(`/manager/grantRole`, params).then((res) => {
          console.log(res)
        })
      })
    },
    getForm(flowId) {
      t_getAction('/admin/desform/' + flowId + '/getConent')
        .then((res) => {
          this.getOnlineJson(JSON.parse(res.result))
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getOnlineJson(formJson) {
      let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
      console.log(userData.value.id)
      // let tenant_id = Vue.ls.get(TENANT_ID)
      // axios.defaults.headers.common['X-Access-Token'] = Vue.ls.get(ACCESS_TOKEN)
      // axios.defaults.headers.common['tenant_id'] = tenant_id
      // axios.defaults.headers.common['userid'] = userData.value.id
      // axios.get(`http://10.162.35.2:41684/order/send?userId=` + userData.value.id)
      order_getAction(`/order/send?userId=` + userData.value.id)
        .then((res) => {
          console.log(res.result)
          var result = res.result
          if (result != {}) {
            this.deliver_goods.delivery_party_addr = result.cictOrder.delivery_party_addr
            this.deliver_goods.order_name = result.cictOrder.order_name
            this.deliver_goods.order_no = this.randomString(9)
            this.deliver_goods.plan_date = result.cictOrder.plan_date
            this.deliver_goods.seller_name = result.cictOrder.seller_name
            this.deliver_goods.process_id = result.cictOrder.process_id
            this.connData(result, formJson)
            if (result.enough == true) {
              this.updateOrder()
            }
            //if (result.deliveryResult != null) {
            this.$message.success('物料充足，可以发货')

            // } else {
            //   this.$message.error('发货失败')
            // }
          }
        })
        .catch((error) => {
          console.log(error)
        })
    },
    connData(json, formJson) {
      var cict_order_detail = new Array()
      cict_order_detail.push(json.CictOrderDetail)
      json.cictOrder['cict_order_detail'] = cict_order_detail
      var onlineJson = json.cictOrder
      console.log(onlineJson)
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
      console.log(formJson)
      this.formJson = formJson
      this.visible = true

      // this.$nextTick(() => {
      //   $('.is_customized')[0].style.display = 'none'
      //   if ($('.is_customized').find('input[type=radio]:checked').val() === '1') {
      //     $('.childTable')[0].style.display = 'block'
      //     $('.customized_desc')[0].style.display = 'none'
      //   } else {
      //     $('.childTable')[0].style.display = 'none'
      //     $('.customized_desc')[0].style.display = 'block'
      //   }
      // })
    },
    updateOrder() {
      let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
      console.log(userData.value.id)
      // let tenant_id = Vue.ls.get(TENANT_ID)
      // axios.defaults.headers.common['X-Access-Token'] = Vue.ls.get(ACCESS_TOKEN)
      // axios.defaults.headers.common['tenant_id'] = tenant_id
      // axios.defaults.headers.common['userid'] = userData.value.id
      // axios.put(`http://10.162.35.2:41684/order/update?userId=` + userData.value.id)
      order_putAction(`/order/update?userId=` + userData.value.id)
        .then((res) => {
          console.log(res)
        })
        .catch((error) => {
          console.log(error)
        })
    },
    randomString(e) {
      e = e || 5
      var t = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678',
        a = t.length,
        n = ''
      for (let i = 0; i < e; i++) n += t.charAt(Math.floor(Math.random() * a))
      return n
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