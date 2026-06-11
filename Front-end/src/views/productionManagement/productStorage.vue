<template>
  <div
    class="productProduction"
    style="display: grid"
  >
    <a-form layout="inline">
      <a-form-item label="名称">
        <a-input
          v-model="searchData.prod_name"
          placeholder="请输入名称"
        />
      </a-form-item>
      <a-form-item>
        <a-button-group>
          <a-button type="primary" icon="search" @click="searchQuery()">查询</a-button>
          <!-- <a-button
            type="primary"
            icon="search"
            @click="loadData()"
          >查询</a-button> -->
          <a-button
            type="primary"
            icon="reload"
            @click="searchReset()"
            >重置</a-button>
          <!-- <a-button
            type="primary"
            icon="reload"
            @click="
              () => {
                this.searchData = []
                loadData()
              }
            "
          >重置</a-button> -->
        </a-button-group>
      </a-form-item>
    </a-form>
    <div style="padding-top: 20px">
      <a-table
        :loading="tableLoading"
        :columns="columns"
        :data-source="formTableData"
        :pagination="ipagination"
        class="j-table-force-nowrap"
      >
        <span
          slot="action"
          slot-scope="text, record"
        >
          <a
            :disabled="record.production_status != 2"
            @click="productStorage(record)"
          >{{
            cp_name(record.production_status)
          }}</a>
        </span>
      </a-table>
    </div>

    <!-- <el-dialog
      title="分配角色"
      :visible.sync="chooseVisible"
      width="50%"
      @close="chooseDialogClosed"> -->
    <el-dialog
      title="选择"
      :visible.sync="chooseVisible"
      width="30%"
      @close="storageDialogClosed">
      <div>
        <p>选择:
          <el-select v-model="selectesdRoleId" placeholder="请选择">
            <el-option
              v-for="(item,index) in wmsInBills"
              :key="index+1"
              :label="item.unit_name"
              :value="item.id">
            </el-option>
          </el-select>
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="chooseVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmStorage()">确 定</el-button>
        <!-- <el-button type="primary" @click="saveRoleInfo">确 定</el-button> -->
      </span>
    </el-dialog>    


  </div>
</template>

<script>
import { o_getAction, o_putAction, o_postAction } from '@api/onApi'
import { Message } from 'element-ui'
import { w_postAction } from '@/api/wmsApi'
import { USER_ID ,ACCESS_TOKEN} from '@/store/mutation-types'
import Vue from 'vue'
import api from '@/api/index'
import axios from 'axios'
import pick from 'lodash.pick'
export default {
  data() {
    return {
      storageRecord:{},
      wmsInBills:[],
      selectesdRoleId: '',
      chooseVisible: false,
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
          this.ipagination.current = current
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
    }
  },
  created() {
    this.loadData()
  },
  computed: {
    cp_name() {
      return function (status) {
        if (status == 4) {
          return '已经入库'
        } else {
          return '产品入库'
        }
      }
    },
  },
  methods: {
    searchReset(){
      this.searchData.prod_name = ''
      this.ipagination.current = 1
      this.ipagination.pageSize = 10
      this.loadData()
    },
    searchQuery(){
      this.ipagination.current = 1
      this.tableLoading = true
      let params = {
        current: this.ipagination.current,
        pages: this.ipagination.pageSize,
        // isPublish: '1',  //1表示已发布未生产
        exampleId: this.$store.state.exampleId,
        prodName : this.searchData.prod_name,
      }
      axios({
        url: api.server_url + api.global_erp_baseURL + 'dailyProductionSchedulingPlan/getAlreadyProduct',
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
    getBillId() {
      o_getAction('/cgform/api/getData/4e7015e9b56447d3a29c3fb392a90b49').then((res) => {
        res.result.records.forEach((item) => {
          this.wmsInBills.push(pick(item, 'id', 'unit_name'))
        })
      })
    },
    loadData() {
      this.tableLoading = true
      let params = {
        current: this.ipagination.current,
        pages: this.ipagination.pageSize,
        // isPublish: '1',  //1表示已发布未生产
        exampleId: this.$store.state.exampleId,
        prodName : this.searchData.prod_name,
      }
      axios({
        url: api.server_url + api.global_erp_baseURL + 'dailyProductionSchedulingPlan/getAlreadyProduct',
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
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },

    //日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为已出货 7货物已退回
    // 确定入库
    confirmStorage(){
      console.log('产品入库 record', this.storageRecord)
      var record = this.storageRecord
      console.log('产品入库 record id', this.selectesdRoleId)
      this.getOrderStatus(record.cprqq_id)
        .then((res) => res)
        .then((res) => {
          console.log('检测返回res', res)
          if (res === '' || res === undefined) {
            Message({
              showClose: true,
              message: '订单所生产的产品尚未检测，请前往检测',
              type: 'warning',
            })
          } else if (res === '不合格') {
            Message({
              showClose: true,
              message: '订单尚未检测合格',
              type: 'warning',
            })
          } else {
            var params = {}
            params.billType = 2 //原材料入库1 成品入库为2
            params.inspecter = '' //送检员
            params.orderId = record.id
            params.providerId = '' //客户
            params.qcor = '' //质检员
            params.remark = '产品生产中的产品入库'
            params.wmsInBillDetails = []
            let item = {}
            item.prodAmount = record.amount
            item.prodDesc = '' //描述
            item.prodId = record.prod_id
            item.prodName = record.prod_name //标识编码
            item.remark = '产品生产成品入库' //备注
            item.unit = '' //单位
            item.wmsInBillId = '' //入库表id
            item.wmsUnitId = this.selectesdRoleId //库存点
            params.wmsInBillDetails.push(item)
            w_postAction(`/wms/warehousing`, params)
              .then((res) => {
                if (res.success) {
                  var params1 = record
                  params1.production_status = 4
                  let that1 = this
                  o_putAction(`/cgform/api/form/` + this.onlineId, params1)
                    .then((res) => {
                      
                      if (res.success) {
                        console.log('修改生产状态为4 产品入库', res)
                        this.chooseVisible = false

                      } else {
                        Message({
                          showClose: true,
                          message: '修改产品入库状态失败！',
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
                } else {
                  Message({
                    showClose: true,
                    message: '产品入库失败',
                    type: 'error',
                  })
                }
              })
              .catch((e) => {
                Message({
                  showClose: true,
                  message: '产品入库异常，数据异常！',
                  type: 'error',
                })
                console.log('e', e)
              })
          }
        })
    },

    storageDialogClosed(){
      this.selectesdRoleId = ''
      this.wmsInBills=[]
    },
    //产品入库
    productStorage(record) {
      //日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为出货完成 7货物已退回
      this.getOrderStatus(record.cprqq_id)
        .then((res) => res)
        .then((res) => {
          console.log('检测返回res', res)
          if (res === '' || res === undefined) {
            Message({
              showClose: true,
              message: '订单所生产的产品尚未检测，请前往检测',
              type: 'warning',
            })
          } else if (res === '不合格') {
            Message({
              showClose: true,
              message: '订单尚未检测合格',
              type: 'warning',
            })
          } else {
            this.getBillId()
            this.chooseVisible = true
            console.log('wmsInBills',this.wmsInBills);
            console.log('入库的record',record);
            this.storageRecord = record
          }
        })
    },

      // console.log('产品入库')
      // console.log('产品入库 record', record)
      // this.getOrderStatus(record.cprqq_id)
      //   .then((res) => res)
      //   .then((res) => {
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
                      
      //                 if (res.success) {
      //                   console.log('修改生产状态为4 产品入库', res)


                        //出货前需要检查，故将该条数据添加到质量检测中
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
                        // o_getAction(`/cgform/api/getData/${this.jcbdOnlineId}`, jcparams).then((res) => {
                        //   var params2 = {}
                        //   params2.categories = '非定制检测'
                        //   params2.check_form_id = res.result.records[0].form_id
                        //   params2.product_data_file = []
                        //   params2.product_id = record.prod_id
                        //   params2.product_name = record.prod_name
                        //   params2.remark = '生产产品后进行出货前检测'
                        //   o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params2)
                        //     .then((res) => {
                        //       Message({
                        //         showClose: true,
                        //         message: '入库成功，请前往质量检测页面进行出货前检测',
                        //         type: 'success',
                        //       })
                        //       var params3 = record
                        //       params3.cpchq_id = res.result
                        //       o_putAction(`/cgform/api/form/` + this.onlineId, params3)
                        //         .then((res) => {
                        //           console.log('修改日排产计划该条数据的产品出货前检测id', res)
                        //         })
                        //         .catch((error) => {
                        //           Message({
                        //             showClose: true,
                        //             message: '产品出货前检测id修改失败！',
                        //             type: 'error',
                        //           })
                        //           console.log('error', error)
                        //         })
                        //     })
                        //     .catch((err) => {
                        //       console.log('err', err)
                        //       Message({
                        //         showClose: true,
                        //         message: '送检失败！',
                        //         type: 'error',
                        //       })
                        //     })
                        // })


    //                   } else {
    //                     Message({
    //                       showClose: true,
    //                       message: '修改产品入库状态失败！',
    //                       type: 'error',
    //                     })
    //                   }
    //                 })
    //                 .catch((error) => {
    //                   Message({
    //                     showClose: true,
    //                     message: '状态修改失败！',
    //                     type: 'error',
    //                   })
    //                   console.log('error', error)
    //                 })
    //             } else {
    //               Message({
    //                 showClose: true,
    //                 message: '产品入库失败',
    //                 type: 'error',
    //               })
    //             }
    //           })
    //           .catch((e) => {
    //             Message({
    //               showClose: true,
    //               message: '产品入库异常，数据异常！',
    //               type: 'error',
    //             })
    //             console.log('e', e)
    //           })
    //       }
    //     })
    // },

    //获取当前数据是否质量检测成功 返回结果为'合格'，'不合格'
    getOrderStatus(id) {
      return o_getAction(`/cgform/api/form/${this.qualityInspectionOnlineId}/${id}`).then((res) => {
        return o_getAction(`/cgform/api/form/${this.resultOnlineid}/${res.result.data_id}`).then((res) => {
          return res?.result?.result
        })
      })
    },
  },
}
</script>

<style>
.productProduction {
  background: #fff;
  padding: 24px;
}
</style>
