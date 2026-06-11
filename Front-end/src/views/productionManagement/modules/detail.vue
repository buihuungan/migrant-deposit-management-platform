<template>
  <div>
    <a-modal
      :title="title"
      :visible="visible"
      width="70%"
      okText="确定"
      cancelText="取消"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <template slot="footer" v-if="addOrEditFlag === 3">
        <a-button @click="handleCancel">返回</a-button>
      </template>
      <template slot="footer" v-if="addOrEditFlag === 4">
        <a-button type="primary" @click="materialReceiving()">领取物料</a-button>
        <a-button @click="handleCancel">返回</a-button>
      </template>
      <template slot="footer" v-if="addOrEditFlag === 5">
        <a-button type="primary" @click="materialReturn()">物料退回</a-button>
        <a-button @click="handleCancel">返回</a-button>
      </template>

      <div class="formbody">
        <generate-form style="margin: 0 auto" insite="true" :data="formJson" :edit="editFlag" ref="generateForm">
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { o_postAction, o_getAction, o_putAction } from '@/api/onApi'
import { Message } from 'element-ui'
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { w_postAction } from '@/api/wmsApi'
export default {
  components: {
    GenerateForm,
    AntdGenerateForm,
  },
  data() {
    return {
      editFlag: true,
      addOrEditFlag: 1,
      isDetail: false,
      onlineId: '2c94f38a76dc81a50176dc81a50b0000', //物料配送单id
      wms_out_bill_onlineId: 'faa27a094b10473fa222b2064f555131', //出库单id
      wms_in_bill_onlineId: 'c5656d37308842e6a400ba633131adc9', //入库表id
      flowId: '54cd1ed909b244829d06b1d47486ab68',
      form: {
        order_id: '',
        create_time: '',
        production_plant_id: '',
        production_line_id: '',
        remark: '',
        material_out_bill_detail: [],
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
      columns: [
        {
          title: '标识解析',
          align: 'center',
          width: '25%',
          dataIndex: 'prod_id',
        },
        {
          title: '名称',
          align: 'center',
          width: '15%',
          dataIndex: 'prod_name',
        },
        {
          title: '数量',
          align: 'center',
          width: '10%',
          dataIndex: 'prod_amount',
        },
        {
          title: '描述',
          align: 'center',
          width: '10%',
          dataIndex: 'prod_desc',
        },
        {
          title: '备注',
          align: 'center',
          width: '20%',
          dataIndex: 'remark',
        },
      ],
      formJson: {},
      dataId: '',
    }
  },
  mounted() {
  },
  methods: {
    getScheduleData(record) {
      //根据主表数据id获取该条数据的附表数据
      return o_getAction(`/cgform/api/form/${this.onlineId}/${record.id}`)
        .then((res) => {
          this.form = res.result
          this.form.create_time = record.create_time
        })
        .catch((e) => {
          Message({
            showClose: true,
            message: '出现错误，刷新重新尝试！',
            type: 'error',
          })
        })
    },
    openModal(record, addOrEditFlag) {
      if (addOrEditFlag === 1) {
        this.addOrEditFlag = 1
        this.title = '新增'
        this.editFlag = true
        this.getForm(record, 1)
      } else if (addOrEditFlag === 2) {
        this.addOrEditFlag = 2
        this.title = '编辑'
        this.editFlag = true
        this.getScheduleData(record).then(() => {
          this.getForm(this.form, 2)
        })
      } else if (addOrEditFlag === 3) {
        this.addOrEditFlag = 3
        this.title = '详情'
        this.isDetail = true
        this.editFlag = false
        this.getScheduleData(record).then(() => {
          this.getForm(this.form, 3)
        })
      } else if (addOrEditFlag === 4) {
        this.addOrEditFlag = 4
        this.title = '领取物料'
        this.isDetail = true
        this.editFlag = false
        this.getScheduleData(record).then(() => {
          this.getForm(this.form, 4)
        })
      } else if (addOrEditFlag === 5) {
        this.addOrEditFlag = 5
        this.title = '物料退回'
        this.editFlag = true
        this.getScheduleData(record).then(() => {
          this.getForm(this.form, 5)
        })
      } else {
        Message({
          showClose: true,
          message: '页面传参错误，打开页面失败！',
          type: 'error',
        })
      }
    },
    materialReceiving() {
      var params = {}
      params.billType = 1 //原材料出库1 成品出库2
      params.orderId = this.form.order_id
      params.productionPlantId = this.form.production_plant_id
      params.productionLineId = this.form.production_line_id
      params.remark = this.form.remark
      params.wmsOutBillDetails = []

      for (let i = 0; i < this.form.material_out_bill_detail.length; i++) {
        let item = {}
        item.prodAmount = this.form.material_out_bill_detail[i].prod_amount
        item.prodDesc = this.form.material_out_bill_detail[i].prod_desc
        item.prodId = this.form.material_out_bill_detail[i].prod_id
        item.prodName = this.form.material_out_bill_detail[i].prod_name
        item.remark = this.form.material_out_bill_detail[i].remark
        item.unit = this.form.material_out_bill_detail[i].unit
        item.wmsOutBillId = ''
        item.wmsUnitId = ''
        params.wmsOutBillDetails.push(item)
      }
      w_postAction(`/wms/delivery`, params)
        .then((res) => {
          //设计有问题，发布任务时扣除了物料，领取物料又要扣除，造成物料不足
          // if (res.success) {
            Message({
              showClose: true,
              message: '物料领取成功',
              type: 'success',
            })
            let params = {}
            params = this.form
            params.wl_status = 1
            params.example_id = this.$store.state.exampleId
            let that = this
            o_putAction(`/cgform/api/form/` + this.onlineId, params).then((res) => {
              console.log('状态修改成功')
              that.$emit('ok')
            })
            this.visible = false
          // } 
          // else {
          //   Message({
          //     showClose: true,
          //     message: res.message + ' 物料领取失败',
          //     type: 'error',
          //   })
          // }
        })
        .catch((e) => {
          Message({
            showClose: true,
            message: '物料领取异常，数据异常！',
            type: 'error',
          })
          console.log('e', e)
        })
    },
    materialReturn() {
      var params = {}
      params.billType = 1 //原材料入库1 成品入库为2
      params.inspecter = '' //送检员
      params.orderId = this.form.order_id
      params.providerId = '' //客户
      params.qcor = '' //质检员
      params.remark = this.form.remark
      params.wmsInBillDetails = []
      for (let i = 0; i < this.form.material_out_bill_detail.length; i++) {
        let item = {}
        item.prodAmount = this.form.material_out_bill_detail[i].prod_amount
        item.prodDesc = this.form.material_out_bill_detail[i].prod_desc
        item.prodId = this.form.material_out_bill_detail[i].prod_id
        item.prodName = this.form.material_out_bill_detail[i].prod_name
        item.remark = this.form.material_out_bill_detail[i].remark
        item.unit = this.form.material_out_bill_detail[i].unit
        item.wmsInBillId = '' //入库表id
        item.wmsUnitId = '' //库存点
        params.wmsInBillDetails.push(item)
      }
      w_postAction(`/wms/warehousing`, params)
        .then((res) => {
          if (res.success) {
            Message({
              showClose: true,
              message: '物料退回成功',
              type: 'success',
            })
            let params = {}
            params = this.form
            params.wl_status = 2
            params.example_id = this.$store.state.exampleId
            let that = this
            o_putAction(`/cgform/api/form/` + this.onlineId, params).then((res) => {
              console.log('状态修改成功')
              that.$emit('ok')
            })
            this.visible = false
          } else {
            Message({
              showClose: true,
              message: '物料退回失败',
              type: 'error',
            })
          }
        })
        .catch((e) => {
          Message({
            showClose: true,
            message: '物料退回异常，数据异常！',
            type: 'error',
          })
          console.log('e', e)
        })
    },

    getForm(record, message) {
      console.log('record', record);
      this.dataId = record.id
      t_getAction('/admin/desform/' + this.flowId + '/getConent') //根据表单设计器表单id获取表单json
        .then((res) => {
          if (Object.keys(record).length === 0) {
            this.formJson = JSON.parse(res.result) //获取到json赋值给表单生成器生成表单页面
            this.visible = true
          } else {
            if (message != 1) {
              //如果是编辑，还需要回填数据
              this.getOnlineJson(res.result, this.dataId) //传入表单json，和所选数据在所绑定数据表中这条数据的id
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
          Message({
            showClose: true,
            message: '传参错误，刷新重试！',
            type: 'error',
          })
          console.log('e', e)
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
              //循环附表所绑定子表单字段数
              let model2 = this.formJson.list[i].tableColumns[h].model
              let tableCol2 = this.formJson.list[i].tableColumns[h].tableCol
              datajson1[tableCol2.toString()] = commitdata[this.formJson.list[i].model][k][model2]
              datajson1.id = commitdata[this.formJson.list[i].model][k].id
              datajson1.material_out_bill_id = commitdata[this.formJson.list[i].model][k].material_out_bill_id //等待测试 测试成功，此段代码加上表单特有字段，因为数据需要附表id
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
      } else {
        datajson.id = this.dataId
        datajson.example_id = this.$store.state.exampleId
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
      o_getAction(`/cgform/api/form/${this.onlineId}/` + predataId) //根据这天数据id获取这条数据的所有数据包括附表的数据。
        .then((res) => {
          var onlineJson = res.result
          console.log('onlineJson=>', onlineJson);
          this.connData(onlineJson, JSON.parse(formJson)) //传入这条数据的所有数据和表单的json
        })
        .catch((err) => {
          console.log(err)
        })
    },
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
  },
}
</script>

<style lang="scss" scoped>
.formbody {
  margin-top: 20px;
  width: 100%;
}
</style>
