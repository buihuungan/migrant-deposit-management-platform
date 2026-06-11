<template>
  <div>
    <a-modal
      :visible="visible"
      :footer="null"
      :centered="true"
      width="80%"
      :closable="true"
      :destroyOnClose="true"
      @cancel="close"
    >
      <div id="formContent">
        <div class="title">
          <h1>{{ '生产工单' }}</h1>
        </div>
        <div
          class="formbody"
          v-cloak
        >
          <generate-form
            style="margin: 0 auto"
            insite="true"
            :data="formJsonEnd"
            ref="generateForm"
          >
            <template v-slot:blank="scope">
              Width
              <el-input
                v-model="scope.model.blank.width"
                style="width: 100px"
              />Height：
              <el-input
                v-model="scope.model.blank.height"
                style="width: 100px"
              />
            </template>
          </generate-form>
        </div>
        <div class="submitBtn">
          <a-button
            type="primary"
            @click="handleSubmit()"
            style="margin-right: 10px"
          >提交</a-button>
          <a-button
            type="dashed"
            @click="close()"
          >返回</a-button>
        </div>
      </div>
      <!-- </ann-task> -->
    </a-modal>
  </div>

</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import { t_getAction } from '@/api/tempApi'
import { getAction, postAction } from '@/api/manage'
import { w_postAction } from '@api/wmsApi'
import { order_getAction } from '@api/orderApi'
import { v4 } from 'uuid'
import { Message } from 'element-ui'

export default {
  name: 'AnnTask',
  components: { GenerateForm },
  data() {
    return {
      visible: false,
      record: {},
      formId: '',
      elId: '',
      nowData: '',
      formJson: {},
      formJsonEnd: {},
      dialogVisible: false,
      projectId: '',
      operator_id: '',
      userInfo: {},
      options: [],
      beSelectedInfo: '',
      username: '',
      username1: '',
      uid: '',
      process_id: '',
      saveIconLoading: false,
      handleIconLoading: false,
      handleBackIconLoading: false,
      production_plant_id: '',
      url: {
        search: '/online/cgform/api/getData/b6cd9cf08487444885d5fd2c0744d378', //生产工单表
        delete: '/online/cgform/api/form/b6cd9cf08487444885d5fd2c0744d378',
        add: '/online/cgform/api/form/b6cd9cf08487444885d5fd2c0744d378',
      },
    }
  },
  created() {},
  watch: {},
  mounted() {},
  methods: {
    openModal(record, formId) {
      console.log('待发布生产任务',record)
      this.record = record
      this.formId = formId
      this.elId = v4()
      this.getProductID()//获取生产工单生产工厂以及里面与所生产产品有关的销售订单
      this.getNowDate()
      this.getForm()
    },
    getNowDate() {
      let date = new Date()
      let Y = date.getFullYear()
      let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
      let D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
      let hours = date.getHours()
      let minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
      let seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
      this.nowData = Y + '-' + M + '-' + D + ' ' + hours + ':' + minutes + ':' + seconds
    },
    close() {
      this.visible = false
      this.$emit('closeTask')
    },
    getForm() {
      t_getAction('/admin/desform/' + this.formId + '/getConent')
        .then((res) => {
          this.formJson = JSON.parse(res.result)
          let aid = this.elId.replace(/-/g, '')
          console.log('this.formJson',this.formJson)
          console.log("this.formJson.list[0]",this.formJson.list[0]);
          //生产工单的销售订单字段显示时用下列注释代码赋值 此时表单设计器中换一个生产工单
          // this.formJson.list[0].columns[0].list[0].options.defaultValue = aid //工单编号
          // this.formJson.list[0].columns[0].list[1].options.defaultValue = this.nowData //预计开工日期
          // this.formJson.list[0].columns[0].list[2].options.defaultValue = this.record.prod_id //标识编码
          // this.formJson.list[0].columns[0].list[3].options.defaultValue = this.record.amount //数量
          // this.formJson.list[0].columns[0].list[4].options.defaultValue = this.process_id //销售订单
          // this.formJson.list[0].columns[0].list[5].options.defaultValue = this.record.production_line_id //产线
          // this.formJson.list[0].columns[1].list[0].options.defaultValue = this.nowData //开单日期
          // this.formJson.list[0].columns[1].list[1].options.defaultValue = this.nowData //预计完工日期
          // this.formJson.list[0].columns[1].list[2].options.defaultValue = this.record.prod_name //名称
          // this.formJson.list[0].columns[1].list[3].options.defaultValue = this.nowData //下达日期
          // this.formJson.list[0].columns[1].list[4].options.defaultValue = this.production_plant_id //生产工厂

          //生产工单的销售订单字段   不   显示时用下列注释代码赋值
          this.formJson.list[0].columns[0].list[0].options.defaultValue = aid //工单编号
          this.formJson.list[0].columns[0].list[1].options.defaultValue = this.nowData //预计开工日期
          this.formJson.list[0].columns[0].list[2].options.defaultValue = this.record.prodId //标识编码
          this.formJson.list[0].columns[0].list[3].options.defaultValue = this.record.amount //数量
          this.formJson.list[0].columns[0].list[4].options.defaultValue = this.record.productionLineId //产线
          this.formJson.list[0].columns[1].list[0].options.defaultValue = this.nowData //开单日期
          this.formJson.list[0].columns[1].list[1].options.defaultValue = this.nowData //预计完工日期
          this.formJson.list[0].columns[1].list[2].options.defaultValue = this.record.prodName //名称
          this.formJson.list[0].columns[1].list[3].options.defaultValue = this.nowData //下达日期
          this.formJson.list[0].columns[1].list[4].options.defaultValue = this.production_plant_id //生产工厂
          this.formJsonEnd = this.formJson
          this.visible = true
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getProductID() {
      let superQueryParams = [{ rule: 'eq', type: 'text', val: this.record.production_line_id, field: 'olinename' }]
      let params = {
        column: 'createTime',
        order: 'desc',
        pageNo: 1,
        pageSize: 10,
        superQueryMatchType: 'and',
      }
      params.superQueryParams = encodeURIComponent(JSON.stringify(superQueryParams))
      getAction('/online/cgform/api/getData/19809bc666f34c6c841828f75e2376a3', params)
        .then((res) => {
          if (res.result && res.result.total != 0) {
            this.production_plant_id = res.result.records[0].factory
            this.getForm()
            
            //暂时不获取获取生产工单里面与所生产产品有关的销售订单数据
            // let superQueryParams = [
            //   {
            //     rule: 'eq',
            //     type: 'popup',
            //     popup: { code: 'model_identity', field: 'identity', orgFields: 'identity', destFields: 'prod_id' },
            //     val: this.record.prod_id,
            //     field: 'cict_order_detail,prod_id',
            //   },
            // ]
            // let params = {
            //   column: 'createTime',
            //   order: 'desc',
            //   pageNo: 1,
            //   pageSize: 10,
            //   superQueryMatchType: 'and',
            // }
            // params.superQueryParams = encodeURIComponent(JSON.stringify(superQueryParams))
            // getAction('/online/cgform/api/getData/e8c9f23b81d74fc6a292c436d27faa70', params) //获取销售订单
            //   .then((res) => {
            //     const { records } = res.result
            //     records.forEach((element, index) => {
            //       if (index < records.length - 1) {
            //         this.process_id += element.process_id + ','
            //       } else {
            //         this.process_id += element.process_id
            //       }
            //     })
            //     this.getForm()
            //   })
            //   .catch((err) => {
            //     console.log('err', err)
            //   })
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    handleSubmit() {
      const $form = this.formJson.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form
        .getData()
        .then((data) => {
          let commitdata = this.handleData(data)
          commitdata.example_id = this.$store.state.exampleId 
          this.commitToDatabase(commitdata) //将数据存储到online数据库中
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    commitToDatabase(commitdata) {
      postAction(this.url.add, commitdata).then((res) => {
        //往生产工单表里新增一条数据
        if (res.success) {
          this.$message.success(res.message)
          let aid = this.elId.replace(/-/g, '')
          let params = {
            prod_amount: commitdata.prod_amount,
            prod_id: commitdata.prod_id,
            production_id: aid,
            production_plant_id: commitdata.production_plant_id, //生产工厂
            productline: commitdata.productline, //产线
          }
          w_postAction(`/material/bill/${this.$store.state.exampleId}`, JSON.stringify(params)) //向物料配送单表里面新增一条数据，用于存储该标识编码所代表产品的所有零件组成信息，由后台完成
            .then((res) => {
              if (res.success) {
                console.log('res', res)
              } else {
                Message({
                  showClose: true,
                  message: '物料配送单插入数据失败！',
                  type: 'error',
                })
              }
            })
            .catch((err) => {
              console.log('err', err)
            })
          order_getAction('/productionTask/updateStatus', { Id: this.record.id }) //修改日排产计划表里面的is_publish由未发布改为已发布
            .then((res) => {
              console.log('res', res)
              this.visible = false
              this.$emit('closeTask')
            })
            .catch((err) => {
              console.log('err', err)
            })
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    handleData(data) {
      let handleData = {}
      let dataKeys = Object.keys(data)
      for (let m = 0; m < this.formJson.list[0].columns.length; m++) {
        for (let n = 0; n < this.formJson.list[0].columns[m].list.length; n++) {
          for (let i = 0; i < dataKeys.length; i++) {
            if (this.formJson.list[0].columns[m].list[n].model == dataKeys[i]) {
              let tableColKey = this.formJson.list[0].columns[m].list[n].tableCol
              handleData[tableColKey] = data[dataKeys[i]]
            }
          }
        }
      }
      return handleData
    },
  },
}
</script>
<style scoped>
#formContent {
  padding: 5px;
  background-color: #fff;
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
}
.submitBtn {
  margin-top: 20px;
}
.submitBtn button {
  margin: 20px auto;
  left: 46%;
}
</style>
