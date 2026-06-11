<template>
  <div class="productProduction" style="display: grid">
    <a-form layout="inline">
      <a-form-item label="名称">
        <a-input v-model="searchData.prod_name" placeholder="请输入名称" />
      </a-form-item>
      <a-form-item>
        <a-button-group>
          <a-button type="primary" icon="search" @click="searchQuery()">查询</a-button>
          <!-- <a-button type="primary" icon="search" @click="loadData()">查询</a-button> -->
          <!-- <a-button
            type="primary"
            icon="reload"
            @click="
              () => {
                this.searchData = []
                loadData()
              }
            "
            >重置</a-button -->
          <a-button
            type="primary"
            icon="reload"
            @click="searchReset()"
            >重置</a-button>
        </a-button-group>
      </a-form-item>
    </a-form>
    <div style="padding-top: 20px; overflow: auto">
      <a-table
        :loading="tableLoading"
        :columns="columns"
        :data-source="formTableData"
        :pagination="ipagination"
        class="j-table-force-nowrap"
      >
        <span slot="action" slot-scope="text, record">
          <a-popconfirm
            title="确定开始生产吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => startProduction(record)"
          >
            <a-icon slot="icon" type="question-circle-o" style="color: green" />
            <a :disabled="record.production_status != 0">{{ sp_name(record.production_status) }}</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定取消生产吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => cancelProduction(record)"
          >
            <a-icon slot="icon" type="question-circle-o" style="color: red" />
            <a :disabled="record.production_status != 1">取消生产</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定生产完毕吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => productionCompleted(record)"
          >
            <a-icon slot="icon" type="question-circle-o" style="color: green" />
            <a :disabled="record.production_status != 1">生产完毕</a>
          </a-popconfirm>
          <!-- <a-divider type="vertical" />
          <a
            :disabled="record.production_status != 2"
            @click="productStorage(record)"
          >{{
            cp_name(record.production_status)
          }}</a>
          <a-divider type="vertical" />
          <a
            :disabled="record.production_status != 4"
            @click="shipmentCompletion(record)"
          >{{
            sc_name(record.production_status)
          }}</a>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定退回货物吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => returnOfGoods(record)"
          >
            <a-icon
              slot="icon"
              type="question-circle-o"
              style="color: red"
            />
            <a :disabled="record.production_status != 6">{{ rog_name(record.production_status) }}</a>
          </a-popconfirm> -->
        </span>
      </a-table>
    </div>
  </div>
</template>

<script>
import { o_getAction, o_putAction, o_postAction } from '@api/onApi'
import { tm_postAction } from '@/api/templateApi'
import { putAction } from '@api/manage'
import { Message } from 'element-ui'
import { w_postAction } from '@/api/wmsApi'
import { USER_ID ,ACCESS_TOKEN} from '@/store/mutation-types'
import Vue from 'vue'
import api from '@/api/index'
import axios from 'axios'
export default {
  data() {
    return {
      onlineId: '24e5e0a2c5d04bf3a0b99a9f8cccc2b4', //日排产计划
      qualityInspectionOnlineId: 'bba30b3a70924ac7aa65c220ee62cb30', //质量检测
      resultOnlineid: '6e02a314b8b54c88bb12ff13fc28114c', //获取检测结果表的id
      jcbdOnlineId: '737bd4f254cf431f8cbad40a1a95550f',
      wms_in_bill_onlineId: 'c5656d37308842e6a400ba633131adc9', //入库表id
      wms_out_bill_onlineId: 'faa27a094b10473fa222b2064f555131', //出库表id
      searchData: {
        prod_name: '',
      },
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        total: 0,
        showSizeChanger: true,
        showQuickJumper: true,
        showTotal: (total, range) => range + ' 共' + total + '条',
        onChange: this.onPageChange.bind(this),
        onShowSizeChange: (current, pageSize) => {
          this.ipagination.current = 1
          this.ipagination.pageSize = pageSize
          this.loadData()
        },
      },
      tableLoading: false,
      columns: [
        {
          title: '标识编码',
          width: '25%',
          dataIndex: 'prod_id',
          key: 'prod_id',
          align: 'center',
        },
        {
          title: '名称',
          width: '15%',
          dataIndex: 'prod_name',
          key: 'prod_name',
          align: 'center',
        },
        {
          title: '排产日期',
          width: '10%',
          dataIndex: 'scheduling_date',
          key: 'scheduling_date',
          align: 'center',
        },
        {
          title: '数量',
          width: '10%',
          dataIndex: 'amount',
          key: 'amount',
          align: 'center',
        },
        {
          title: '产线',
          width: '10%',
          dataIndex: 'production_line_id',
          key: 'production_line_id',
          align: 'center',
        },
        {
          title: '操作',
          dataIndex: 'action',
          key: 'action',
          width: '30%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      formTableData: [],
      prod_identity: '',
      create_by: '',
    }
  },
  created() {
    this.loadData()
  },
  computed: {
    sp_name() {
      return function (status) {
        if (status == 1) {
          return '正在生产'
        } else {
          return '开始生产'
        }
      }
    },
    // cp_name() {
    //   return function (status) {
    //     if (status == 4) {
    //       return '已经入库'
    //     } else {
    //       return '产品入库'
    //     }
    //   }
    // },
    // sc_name() {
    //   return function (status) {
    //     if (status == 6) {
    //       return '已出货'
    //     } else {
    //       return '可出货'
    //     }
    //   }
    // },
    // rog_name() {
    //   return function (status) {
    //     if (status == 7) {
    //       return '货物已退'
    //     } else {
    //       return '货物退回'
    //     }
    //   }
    // },
  },
  methods: {
    searchQuery(){
      console.log('查询值' , this.searchData.prod_name )
      this.tableLoading = true
      this.ipagination.current = 1
      let params = {
        current: this.ipagination.current,
        pages: this.ipagination.pageSize,
        isPublish: '1',  //1表示已发布未生产
        exampleId: this.$store.state.exampleId,
        prodName : this.searchData.prod_name,
      }
      axios({
        url: api.server_url + api.global_erp_baseURL + '/dailyProductionSchedulingPlan/getAlreadyDailyPublish',
        method: 'get',
        params: params,
        headers: {
          'userid':  Vue.ls.get(USER_ID),
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },
      }).then(res => {
        console.log('res',res)
        this.formTableData = []
        res.data.result.records.forEach((item) => {
          let record = {}
          record.amount = item.amount
          record.cprqq_id = item.cprqqId
          record.create_by = item.createBy
          record.create_time = item.createTime
          record.example_id = item.exampleId
          record.id = item.id
          record.is_publish = item.isPublish
          record.prod_id = item.prodId
          record.prod_name = item.prodName
          record.production_line_id = item.productionLineId
          record.production_status = item.productionStatus
          record.scheduling_date = item.schedulingDate
          record.sys_org_code = item.sysOrgCode
          record.update_by = item.updateBy
          record.update_time = item.updateTime
          this.formTableData.push(record)
        })
        this.ipagination.total = res.data.result.total
        console.log('this.formTableData',this.formTableData)
      })
      this.tableLoading = false
    },
    searchReset(){
      this.searchData.prod_name = ''
      this.ipagination.current = 1
      this.ipagination.pageSize = 10
      this.loadData()
    },
    loadData() {
      this.tableLoading = true
      // let params = {
      //   column: 'createTime',
      //   order: 'desc',
      //   pageNo: this.ipagination.current,
      //   pageSize: this.ipagination.pageSize,
      //   superQueryMatchType: 'and',
      // }
      // var superQueryParams = []
      // if (this.searchData.prod_name) {
      //   superQueryParams.push(
      //     {
      //       rule: 'like',
      //       type: 'text',
      //       val: this.searchData.prod_name,
      //       field: 'prod_name',
      //     },
      //     { rule: 'eq', type: 'list', val: '1', field: 'is_publish' }
      //   )
      // } else {
      //   superQueryParams.push({ rule: 'eq', type: 'list', val: '1', field: 'is_publish' })
      // }
      // if (superQueryParams.length) {
      //   params.superQueryParams = encodeURI(JSON.stringify(superQueryParams))
      // }
      // o_getAction(`/cgform/api/getData/` + this.onlineId, params)
      //   .then((res) => {
      //     if (res.success) {
      //       console.log('res', res)
      //       this.formTableData = res.result.records
      //       this.tableLoading = false
      //       this.ipagination.total = res.result.total
      //     } else {
      //       Message({
      //         showClose: true,
      //         message: '数据获取失败！',
      //         type: 'error',
      //       })
      //     }
      //   })
      //   .catch((error) => {
      //     Message({
      //       showClose: true,
      //       message: '数据获取失败！',
      //       type: 'error',
      //     })
      //     console.log('error', error)
      //   })
      let params = {
        current: this.ipagination.current,
        pages: this.ipagination.pageSize,
        isPublish: '1',  //1表示已发布未生产
        exampleId: this.$store.state.exampleId,
        prodName : this.searchData.prod_name,
      }
      axios({
        url: api.server_url + api.global_erp_baseURL + '/dailyProductionSchedulingPlan/getAlreadyDailyPublish',
        method: 'get',
        params: params,
        headers: {
          'userid':  Vue.ls.get(USER_ID),
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },
      }).then(res => {
        console.log('res',res)
        res.data.result.records.forEach((item) => {
          let record = {}
          record.amount = item.amount
          record.cprqq_id = item.cprqqId
          record.create_by = item.createBy
          record.create_time = item.createTime
          record.example_id = item.exampleId
          record.id = item.id
          record.is_publish = item.isPublish
          record.prod_id = item.prodId
          record.prod_name = item.prodName
          record.production_line_id = item.productionLineId
          record.production_status = item.productionStatus
          record.scheduling_date = item.schedulingDate
          record.sys_org_code = item.sysOrgCode
          record.update_by = item.updateBy
          record.update_time = item.updateTime
          this.formTableData.push(record)
        })
        this.ipagination.total = res.data.result.total
        console.log('this.formTableData',this.formTableData)
      })
      this.tableLoading = false
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      // console.log("this.ipagination.current",this.ipagination.current);
      // this.searchData.current = page
      // console.log("this.searchData.current",this.searchData.current);
      // this.searchData.size = pageSize
      this.loadData()
    },

    //日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为已出货 7货物已退回

    //开始生产
    async startProduction(record) {
      //修改 日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为出货完成 7货物已退回
      var model = { prefix: '' }
      await putAction('/sys/fillRule/executeRuleByCode/prod_identity_auto', model).then((res) => {
        if (res.success) {
          console.log('yyyyyyyyyyyyy', res)
          this.prod_identity = res.result
        }
      })
      var postData0 = {
        identity: '',
        prod_identity: '',
        prod_name: '',
        prod_line_id: '',
        user_id: '',
        prod_number: '',
        plan_id: '',
        lesson_id: '',
        teaching_mode: '',
      }
      var userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
      postData0.user_id = userid.value
      var process = JSON.parse(window.localStorage.getItem('process'))
      if(process) {
        postData0.plan_id = process.planId
        postData0.lesson_id = process.lessonId
        postData0.teaching_mode = process.teachingMode
        postData0.identity = this.prod_identity
        postData0.prod_identity = ''
        postData0.prod_name = record.prod_name
        postData0.prod_number = record.amount
        postData0.prod_line_id = record.production_line_id
        tm_postAction('/cgform/api/form/d89dea4eff4a47a19c356777ff598714', postData0).then((res) => {
          if (res.success) {
            console.log('产品标识插入模板成功')
          }
        })
      }
      console.log('开始生产record', record)
      let params = record
      params.production_status = 1
      o_putAction(`/cgform/api/form/` + this.onlineId, params)
        .then((res) => {
          console.log('修改生产状态为1 开始生产', res)
        })
        .catch((error) => {
          Message({
            showClose: true,
            message: '状态修改失败！',
            type: 'error',
          })
          console.log('error', error)
        })
    },

    //取消生产
    cancelProduction(record) {
      //修改 日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为出货完成 7货物已退回
      console.log('取消生产')
      let params = record
      params.production_status = 0
      o_putAction(`/cgform/api/form/` + this.onlineId, params)
        .then((res) => {
          console.log('修改生产状态为0 取消生产', res)
        })
        .catch((error) => {
          Message({
            showClose: true,
            message: '状态修改失败！',
            type: 'error',
          })
          console.log('error', error)
        })
    },

    //生产完毕
    productionCompleted(record) {
      //修改 日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为出货完成 7货物已退回
      console.log('生产完毕')
      let params = record
      params.production_status = 2
      o_putAction(`/cgform/api/form/` + this.onlineId, params)
        .then((res) => {
          console.log('修改生产状态为2 生产完毕', res)
          if (res.success) {
            // let jcparams = {
            //   pageSize: 10,
            //   pageNo: 1,
            //   column: 'createTime',
            //   order: 'desc',
            // }
            // jcparams.superQueryMatchType = 'and'
            // jcparams.superQueryParams = encodeURI(
            //   JSON.stringify([
            //     {
            //       rule: 'eq',
            //       type: 'text',
            //       val: '非定制检测',
            //       field: 'categories',
            //     },
            //   ])
            // )
            // o_getAction(`/cgform/api/getData/${this.jcbdOnlineId}`, jcparams)
            //   .then((res) => {
            //     var params = {}
            //     params.categories = '非定制检测'
            //     params.check_form_id = res.result.records[0].form_id
            //     params.product_data_file = []
            //     params.product_id = record.prod_id
            //     params.product_name = record.prod_name
            //     params.remark = '生产产品完毕进行入库前检测'
            //     o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params)
            //       .then((res) => {
            //         Message({
            //           showClose: true,
            //           message: '生产完毕，请前往质量检测页面进行入库前检测',
            //           type: 'success',
            //         })
            //         var params1 = record
            //         console.log('huoqushuju', res)
            //         params1.cprqq_id = res.result
            //         o_putAction(`/cgform/api/form/` + this.onlineId, params1)
            //           .then((res) => {
            //             console.log('修改日排产计划该条数据的产品入库前检测id', res)
            //           })
            //           .catch((error) => {
            //             Message({
            //               showClose: true,
            //               message: '产品入库前检测id修改失败！',
            //               type: 'error',
            //             })
            //             console.log('error', error)
            //           })
            //       })
            //       .catch((err) => {
            //         console.log('err', err)
            //         Message({
            //           showClose: true,
            //           message: '送检失败！',
            //           type: 'error',
            //         })
            //       })
            //   })
            //   .catch((e) => {
            //     console.log('e', e)
            //     Message({
            //       showClose: true,
            //       message: '获取检测表单的表单id失败，检测online接口是否正常！需自行添加检测数据',
            //       type: 'error',
            //     })
            //   })
            var params = {}
            params.categories = '非定制检测'
            params.check_form_id = 'ad6c42e52d984e2fb1cf174071a8dfbf'
            params.product_data_file = []
            params.product_id = record.prod_id
            params.product_name = record.prod_name
            params.remark = '生产产品完毕进行入库前检测'
            params.example_id = this.$store.state.exampleId
            o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params)
              .then((res) => {
                Message({
                  showClose: true,
                  message: '生产完毕，请前往质量检测页面进行入库前检测',
                  type: 'success',
                })
                var params1 = record
                console.log('huoqushuju', res)
                params1.cprqq_id = res.result
                o_putAction(`/cgform/api/form/` + this.onlineId, params1)
                  .then((res) => {
                    console.log('修改日排产计划该条数据的产品入库前检测id', res)
                  })
                  .catch((error) => {
                    Message({
                      showClose: true,
                      message: '产品入库前检测id修改失败！',
                      type: 'error',
                    })
                    console.log('error', error)
                  })
              })
              .catch((err) => {
                console.log('err', err)
                Message({
                  showClose: true,
                  message: '送检失败！',
                  type: 'error',
                })
              })
          } else {
            Message({
              showClose: true,
              message: '状态修改失败！',
              type: 'error',
            })
          }
        })
        .catch((error) => {
          Message({
            showClose: true,
            message: '状态修改失败！',
            type: 'error',
          })
          console.log('error', error)
        })
    },

    //产品入库
    // productStorage(record) {
    //   //日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为出货完成 7货物已退回
    //   console.log('产品入库')
    //   console.log('产品入库 record', record)
    //   this.getOrderStatus(record.cprqq_id).then(res=>res).then((res) => {
    //     console.log('检测返回res', res)
    //     if (res === '' || res === undefined) {
    //       Message({
    //         showClose: true,
    //         message: '订单所生产的产品尚未检测，请前往检测',
    //         type: 'warning',
    //       })
    //     } else if (res === '不合格') {
    //       Message({
    //         showClose: true,
    //         message: '订单尚未检测合格',
    //         type: 'warning',
    //       })
    //     } else {
    //       var params = {}
    //       params.billType = 2 //原材料入库1 成品入库为2
    //       params.inspecter = '' //送检员
    //       params.orderId = record.id
    //       params.providerId = '' //客户
    //       params.qcor = '' //质检员
    //       params.remark = '产品生产中的产品入库'
    //       params.wmsInBillDetails = []
    //       let item = {}
    //       item.prodAmount = record.amount
    //       item.prodDesc = '' //描述
    //       item.prodId = record.prod_id
    //       item.prodName = record.prod_name //标识编码
    //       item.remark = '产品生产成品入库' //备注
    //       item.unit = '' //单位
    //       item.wmsInBillId = '' //入库表id
    //       item.wmsUnitId = '' //库存点
    //       params.wmsInBillDetails.push(item)
    //       w_postAction(`/wms/warehousing`, params)
    //         .then((res) => {
    //           if (res.success) {
    //             var params1 = record
    //             params1.production_status = 4
    //             let that1 = this
    //             o_putAction(`/cgform/api/form/` + this.onlineId, params1)
    //               .then((res) => {
    //                 console.log('修改生产状态为4 产品入库', res)
    //                 if (res.success) {
    //                   //出货前需要检查，故将该条数据添加到质量检测中
    //                   let jcparams = {
    //                     pageSize: 10,
    //                     pageNo: 1,
    //                     column: 'createTime',
    //                     order: 'desc',
    //                   }
    //                   jcparams.superQueryMatchType = 'and'
    //                   jcparams.superQueryParams = encodeURI(
    //                     JSON.stringify([
    //                       {
    //                         rule: 'eq',
    //                         type: 'text',
    //                         val: '非定制检测',
    //                         field: 'categories',
    //                       },
    //                     ])
    //                   )
    //                   o_getAction(`/cgform/api/getData/${this.jcbdOnlineId}`, jcparams).then((res) => {
    //                     var params2 = {}
    //                     params2.categories = '非定制检测'
    //                     params2.check_form_id = res.result.records[0].form_id
    //                     params2.product_data_file = []
    //                     params2.product_id = record.prod_id
    //                     params2.product_name = record.prod_name
    //                     params2.remark = '生产产品后进行出货前检测'
    //                     o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params2)
    //                       .then((res) => {
    //                         Message({
    //                           showClose: true,
    //                           message: '入库成功，请前往质量检测页面进行出货前检测',
    //                           type: 'success',
    //                         })
    //                         var params3 = record
    //                         params3.cpchq_id = res.result
    //                         o_putAction(`/cgform/api/form/` + this.onlineId, params3)
    //                           .then((res) => {
    //                             console.log('修改日排产计划该条数据的产品出货前检测id', res)
    //                           })
    //                           .catch((error) => {
    //                             Message({
    //                               showClose: true,
    //                               message: '产品出货前检测id修改失败！',
    //                               type: 'error',
    //                             })
    //                             console.log('error', error)
    //                           })
    //                       })
    //                       .catch((err) => {
    //                         console.log('err', err)
    //                         Message({
    //                           showClose: true,
    //                           message: '送检失败！',
    //                           type: 'error',
    //                         })
    //                       })
    //                   })
    //                 } else {
    //                   Message({
    //                     showClose: true,
    //                     message: '修改产品入库状态失败！',
    //                     type: 'error',
    //                   })
    //                 }
    //               })
    //               .catch((error) => {
    //                 Message({
    //                   showClose: true,
    //                   message: '状态修改失败！',
    //                   type: 'error',
    //                 })
    //                 console.log('error', error)
    //               })
    //           } else {
    //             Message({
    //               showClose: true,
    //               message: '产品入库失败',
    //               type: 'error',
    //             })
    //           }
    //         })
    //         .catch((e) => {
    //           Message({
    //             showClose: true,
    //             message: '产品入库异常，数据异常！',
    //             type: 'error',
    //           })
    //           console.log('e', e)
    //         })
    //     }
    //   })
    // },

    //出货完成
    // shipmentCompletion(record) {
    //   //日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为出货完成 7货物已退回
    //   console.log('出货')
    //   this.getOrderStatus(record.cpchq_id).then(res=>res).then((res) => {
    //     console.log('检测返回res', res)
    //     if (res === '' || res === undefined) {
    //       Message({
    //         showClose: true,
    //         message: '产品出货尚未检测，请前往检测',
    //         type: 'warning',
    //       })
    //     } else if (res === '不合格') {
    //       Message({
    //         showClose: true,
    //         message: '产品出货前检测不合格',
    //         type: 'warning',
    //       })
    //     } else {
    //       var params = {}
    //       params.billType = 2 //原材料出库1 成品出库2
    //       params.orderId = record.id
    //       params.productionPlantId = '' //需求工厂
    //       params.productionLineId = record.production_line_id //产线
    //       params.remark = ''
    //       params.wmsOutBillDetails = []

    //       let item = {}
    //       item.prodAmount = record.amount
    //       item.prodDesc = ''
    //       item.prodId = record.prod_id
    //       item.prodName = record.prod_name
    //       item.remark = ''
    //       item.unit = ''
    //       item.wmsOutBillId = ''
    //       item.wmsUnitId = ''
    //       params.wmsOutBillDetails.push(item)

    //       w_postAction(`/wms/delivery`, params)
    //         .then((res) => {
    //           if (res.success) {
    //             var params1 = record
    //             params1.production_status = 6
    //             o_putAction(`/cgform/api/form/` + this.onlineId, params1)
    //               .then((res) => {
    //                 console.log('修改生产状态为6 产品已出库', res)
    //               })
    //               .catch((error) => {
    //                 Message({
    //                   showClose: true,
    //                   message: '状态修改失败！',
    //                   type: 'error',
    //                 })
    //                 console.log('error', error)
    //               })
    //           } else {
    //             Message({
    //               showClose: true,
    //               message: '产品出货失败',
    //               type: 'error',
    //             })
    //           }
    //         })
    //         .catch((e) => {
    //           Message({
    //             showClose: true,
    //             message: '产品入库异常，数据异常！',
    //             type: 'error',
    //           })
    //           console.log('e', e)
    //         })
    //     }
    //   })
    // },

    //货物已退回
    // returnOfGoods(record) {
    //   //日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为出货完成 7货物已退回
    //   console.log('货物已退回')
    //   var params = {}
    //   params.billType = 2 //原材料入库1 成品入库为2
    //   params.inspecter = '' //送检员
    //   params.orderId = record.id
    //   params.providerId = '' //客户
    //   params.qcor = '' //质检员
    //   params.remark = '产品生产中的货物退回'
    //   params.wmsInBillDetails = []
    //   let item = {}
    //   item.prodAmount = record.amount
    //   item.prodDesc = '' //描述
    //   item.prodId = record.prod_id
    //   item.prodName = record.prod_name //标识编码
    //   item.remark = '产品生产成品入库' //备注
    //   item.unit = '' //单位
    //   item.wmsInBillId = '' //入库表id
    //   item.wmsUnitId = '' //库存点
    //   params.wmsInBillDetails.push(item)
    //   w_postAction(`/wms/warehousing`, params)
    //     .then((res) => {
    //       if (res.success) {
    //         var params1 = record
    //         params1.production_status = 7
    //         o_putAction(`/cgform/api/form/` + this.onlineId, params1)
    //           .then((res) => {
    //             console.log('修改生产状态为7 产品退回', res)
    //             if (res.success) {
    //               Message({
    //                 showClose: true,
    //                 message: '货物退回成功！',
    //                 type: 'success',
    //               })
    //             } else {
    //               Message({
    //                 showClose: true,
    //                 message: '修改产品入库状态失败！',
    //                 type: 'error',
    //               })
    //             }
    //           })
    //           .catch((error) => {
    //             Message({
    //               showClose: true,
    //               message: '状态修改失败！',
    //               type: 'error',
    //             })
    //             console.log('error', error)
    //           })
    //       } else {
    //         Message({
    //           showClose: true,
    //           message: '货物退回后入库失败',
    //           type: 'error',
    //         })
    //       }
    //     })
    //     .catch((e) => {
    //       Message({
    //         showClose: true,
    //         message: '货物退回后异常，数据异常！',
    //         type: 'error',
    //       })
    //       console.log('e', e)
    //     })
    // },

    //获取当前数据是否质量检测成功 返回结果为'合格'，'不合格'
    // getOrderStatus(id) {
    //   return o_getAction(`/cgform/api/form/${this.qualityInspectionOnlineId}/${id}`).then((res) => {
    //      return o_getAction(`/cgform/api/form/${this.resultOnlineid}/${res.result.data_id}`).then((res) => {
    //       return res?.result?.result
    //     })
    //   })
    // },

    //获取当前时间，格式为2021-08-26 00:00:00
    // getNowData() {
    //   let date = new Date()
    //   let y = date.getFullYear()
    //   let m = date.getMonth() + 1
    //   let d = date.getDate()
    //   let H = date.getHours()
    //   let mm = date.getMinutes()
    //   let s = date.getSeconds()
    //   m = m < 10 ? '0' + m : m
    //   d = d < 10 ? '0' + d : d
    //   H = H < 10 ? '0' + H : H
    //   return y + '-' + m + '-' + d + ' ' + H + ':' + mm + ':' + s
    // },
  },
}
</script>

<style>
.productProduction {
  background: #fff;
  padding: 24px;
}
</style>
