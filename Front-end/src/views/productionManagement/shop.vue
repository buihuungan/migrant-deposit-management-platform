<template>
  <div>
    <a-card :bordered="false">
      <div>
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
        >
        <!-- 0 1：默认 2：已送检 3：检测不合格 4：检测合格 5：已发货 6：已收货 -->
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record, 'detail')">详情</a>
            <a-divider type="vertical" />
            <a @click="handleTest(record)" v-if="record.order_state == 0 || record.order_state == 1">送往检测</a>
            <a v-else disabled>已送检</a>
            <a-divider type="vertical" />
            <a @click="shop(record, 'shop')" v-if="record.order_state !== 5">发货</a>
            <a v-else disabled>已发货</a>
          </span>
        </a-table>
      </div>
    </a-card>

    <salesModal ref="modalForm" @list="closeAnnTask" @loadData="loadData" :flowId="flowId" ></salesModal>
  </div>
</template>

<script>
import { o_getAction, o_putAction, o_postAction } from '@api/onApi'
import salesModal from './modules/salesModal'
import { getAction, postAction, deleteAction } from '@/api/manage'
import { USER_INFO } from '@/store/mutation-types'
import vue from 'vue'
import { Message } from 'element-ui'
import axios from "axios"
import { order_postAction } from '@/api/orderApi'
export default {
  name: 'shop',
  components: { salesModal },
  data() {
    return {
      selectedRowKeys: [],
      flowId: '919e12ea043f412eb25ae8ef75b820b2',
      onlineId: '24e5e0a2c5d04bf3a0b99a9f8cccc2b4', //日排产计划
      qualityInspectionOnlineId: 'bba30b3a70924ac7aa65c220ee62cb30', //质量检测
      orderInfoId: 'e8c9f23b81d74fc6a292c436d27faa70', //订单信息表id
      resultOnlineid: '6e02a314b8b54c88bb12ff13fc28114c', //获取检测结果表的id
      jcbdOnlineId: '737bd4f254cf431f8cbad40a1a95550f',
      wms_in_bill_onlineId: 'c5656d37308842e6a400ba633131adc9', //入库表id
      wms_out_bill_onlineId: 'faa27a094b10473fa222b2064f555131', //出库表id
      loading: true,
      columns: [
        { align: 'center', dataIndex: 'process_id', title: '订单编号' },
        { align: 'center', dataIndex: 'order_director', title: '申请人' },
        { align: 'center', dataIndex: 'delivery_party_addr', title: '售达方名称' },
        { align: 'center', dataIndex: 'order_date', title: '订单日期' },
        {
          align: 'center',
          dataIndex: 'order_state',
          title: '订单状态',
          customRender: (text, record, index) => {
            if (text === 0) {
              return '已完成'
            } else if (text === 1) {
              return '未完成'
            } else{
              return '已发货'
            }
          },
        },
        { title: '操作', key: 'action', align: 'center', scopedSlots: { customRender: 'action' } },
      ],
      dataSource: [],
      salesOrderUrl: {
        getColumns: '/online/cgform/api/getColumns/e8c9f23b81d74fc6a292c436d27faa70', // 表名：cict_order   表描述：订单信息
        getData: '/online/cgform/api/getData/e8c9f23b81d74fc6a292c436d27faa70',
        delete: '/online/cgform/api/form/e8c9f23b81d74fc6a292c436d27faa70',
      },
      ipagination: {
        current: 1,
        pageSize: 10,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    //:row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
    loadData() {
      // let params = {
      //   column: 'createTime',
      //   order: 'desc',
      //   pageNo: this.ipagination.current,
      //   pageSize: 10,
      //   superQueryMatchType: 'and',
      // }
      // let superQueryParams = []
      // if (vue.ls.get(USER_INFO).username != 'admin') {
      //   superQueryParams.push({ rule: 'eq', type: 'text', val: vue.ls.get(USER_INFO).username, field: 'create_by' })
      // }
      // superQueryParams.push({ rule: 'eq', type: 'number', val: 1, field: 'audit_status' })
      // params.superQueryParams = encodeURI(JSON.stringify(superQueryParams))
      // getAction(this.salesOrderUrl.getData, params).then((res) => {
      //   this.ipagination.total = res.result.total
      //   this.dataSource = res.result.records
      //   console.log("this.dataSource", this.dataSource);
      //   this.loading = false
      // })
      let params = {
        pageNum: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        userId: '',
        exampleId : ''
      }
      console.log('vue.ls.get(USER_INFO)',vue.ls.get(USER_INFO))
      params.userId =  vue.ls.get(USER_INFO).id
      params.exampleId = this.$store.state.exampleId
      order_postAction('/cictOrder/getData',params).then( res => {
        this.dataSource = []
        console.log('res',res)
        res.result.records.forEach((item)=> {
          var newObj = new Object()
          for (let key in item) {
            newObj[key.replace(/([A-Z])/g, "_$1").toLowerCase()] = item[key]
          }
          this.dataSource.push(newObj)
        })
        this.ipagination.total = res.result.total
        this.loading = false
      })
    },
    newlyBuild() {
      this.$refs.modalForm.getForm({})
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    onClearSelected() {
      this.selectedRowKeys = []
    },

    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    closeAnnTask() {
      this.loadData()
    },
    handleEdit(record, message) {
      this.$refs.modalForm.getForm(record, message)
    },
    //点击送往测试按钮
    handleTest(record) {
      console.log('送往检测', record)
      //is_custormize 1定制 0非定制
      if (record.is_custormize) {
        let params1 = {
          product_id: record.process_id,
          product_name: '销售订单的标识编码',
          categories: '定制检测',
          demo_3d_all_data: '',
          bdtokenurl: '',
          true_3d_data: '',
          example_id: this.$store.state.exampleId
        }
        o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params1).then((res) => {
          console.log(res);
          res.state_state = 1
          console.log(res);
          Message({
            showClose: true,
            message: '定制送检成功，请前往质量检测页面进行出货前检测',
            type: 'success',
            // type: 'error',
          })
        })

        // axios({
        //   method: "PUT",
        //   url: `http://196.168.11.11:8080/update?id=${record.process_id}`,
        // }).then(res => {
        //   if (res.data.code === 200) {
        //     return this.$message.error("操作成功")
        //   }
        //   return this.$message.error("操作失败")
        // })

      } else {
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
        //   params2.product_id = record.process_id
        //   params2.product_name = ''
        //   params2.remark = '出货前检测'
        //   o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params2)
        //     .then((res) => {
        //       Message({
        //         showClose: true,
        //         message: '非定制送检成功，请前往质量检测页面进行出货前检测',
        //         type: 'success',
        //       })
        //       // var params3 = record
        //       // params3.cpchq_id = res.result
        //       // o_putAction(`/cgform/api/form/` + this.onlineId, params3)
        //       //   .then((res) => {
        //       //     console.log('修改日排产计划该条数据的产品出货前检测id', res)
        //       //   })
        //       //   .catch((error) => {
        //       //     Message({
        //       //       showClose: true,
        //       //       message: '产品出货前检测id修改失败！',
        //       //       type: 'error',
        //       //     })
        //       //     console.log('error', error)
        //       //   })
        //     })
        //     .catch((err) => {
        //       console.log('err', err)
        //       Message({
        //         showClose: true,
        //         message: '非定制送检失败！',
        //         type: 'error',
        //       })
        //     })
        // })
        var params2 = {}
        params2.categories = '非定制检测'
        params2.check_form_id = 'ad6c42e52d984e2fb1cf174071a8dfbf'
        params2.product_data_file = []
        params2.product_id = record.process_id
        params2.product_name = '销售订单的标识编码'
        params2.remark = '出货前检测'
        params2.example_id = this.$store.state.exampleId
        params2.order_state = 2
        o_postAction(`/cgform/api/form/` + this.qualityInspectionOnlineId, params2)
          .then((res) => {
            console.log(res);
            // res.state_state = 1
            console.log('-----------',res);
            console.log('record 的id',record.id)
            console.log('record 的状态',record.order_state)
            Message({
              showClose: true,
              message: '非定制送检成功，请前往质量检测页面进行出货前检测',
              type: 'success',
            })
            
            var params3= {}
            params3.id = record.id
            params3.create_by = record.create_by
            params3.create_time = record.create_time
            params3.update_by = record.update_by
            params3.update_time = record.update_time
            params3.sys_org_code = record.sys_org_code
            params3.process_id = record.process_id
            params3.order_director = record.order_director
            params3.rec_type = record.rec_type
            params3.order_date = record.order_date
            params3.promoter_phone = record.promoter_phone
            params3.seller_id = record.seller_id
            params3.seller_name = record.seller_name
            params3.delivery_party_name = record.delivery_party_name
            params3.delivery_party_addr = record.delivery_party_addr
            params3.request_date = record.request_date
            params3.plan_date = record.plan_date
            params3.account_order = record.account_order
            params3.receiver_name = record.receiver_name
            params3.receiver_phone = record.receiver_phone
            params3.remark = record.remark
            params3.audit_status = record.audit_status
            params3.is_custormize = record.is_custormize
            params3.custormize_basic = record.custormize_basic
            params3.custormize_color = record.custormize_color
            params3.custormize_shape = record.custormize_shape
            params3.custormize_power = record.custormize_power
            params3.custormize_amount = record.custormize_amount
            params3.custormize_flag = record.custormize_flag
            params3.order_name = record.order_name
            params3.example_id = record.example_id
            params3.order_state = 2
            o_putAction(`/cgform/api/form/` + this.orderInfoId , params3)
              .then( (res) => {
                console.log('orderpost接口',res)
                this.loadData()
              })


            // var params3 = record
            // params3.cpchq_id = res.result
            // o_putAction(`/cgform/api/form/` + this.onlineId, params3)
            //   .then((res) => {
            //     console.log('修改日排产计划该条数据的产品出货前检测id', res)
            //   })
            //   .catch((error) => {
            //     Message({
            //       showClose: true,
            //       message: '产品出货前检测id修改失败！',
            //       type: 'error',
            //     })
            //     console.log('error', error)
            //   })
          })
          .catch((err) => {
            console.log('err', err)
            Message({
              showClose: true,
              message: '非定制送检失败！',
              type: 'error',
            })
          })
      }
    },
    shop(record, message) {
      console.log("shop record", record);
      if(record.order_state == 0 || record.order_state == 1){
        return Message({
              showClose: true,
              message: '订单所生产的产品尚未送检，请送往检测',
              type: 'warning',
            })
      }else if(record.order_state == 2){
        return Message({
              showClose: true,
              message: '订单所生产的产品尚未进行检测，请进行检测',
              type: 'warning',
            })
      }else if(record.order_state == 3){
        return Message({
              showClose: true,
              message: '订单尚未检测合格',
              type: 'warning',
            })        
      }else if(record.order_state == 4){
        console.log('订单检测成功')
        this.$refs.modalForm.getForm(record, message)
      }
      // this.getOrderStatus(record.process_id)
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
      //       console.log('订单检测成功')
      //       this.$refs.modalForm.getForm(record, message)
      //     }
      //   })
    },
    //获取当前数据是否质量检测成功 返回结果为'合格'，'不合格'
    getOrderStatus(id) {
      console.log("id    id", id);
      let params = {
        column: 'createTime',
        order: 'desc',
        superQueryMatchType: 'and',
        pageNo: 1,
        pageSize: -521,
        superQueryParams: encodeURIComponent(JSON.stringify([{ "rule": "LIKE", "type": "text", "val": id, "field": "product_id" }])),
      }
      return o_getAction(`/cgform/api/getData/${this.qualityInspectionOnlineId}`, params).then((res) => {
        return o_getAction(`/cgform/api/form/${this.resultOnlineid}/${res.result.records[0].data_id}`).then((res) => {
          return res?.result?.result
        })
      })
    },
    confirm(record) {
      this.handleDelete(record)
    },
    cancel() {
      this.$message.error('已取消')
    },
    handleDelete(record) {
      deleteAction(this.salesOrderUrl.delete + '/' + record.id)
        .then((res) => {
          this.$message.success('删除成功')
          this.loadData()
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
  },
}
</script>

<style scoped>
.ant-btn {
  line-height: 1.499;
  position: relative;
  display: inline-block;
  font-weight: 400;
  white-space: nowrap;
  text-align: center;
  background-image: none;
  border: 1px solid transparent;
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.015);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -ms-touch-action: manipulation;
  touch-action: manipulation;
  height: 32px;
  padding: 0 15px;
  font-size: 14px;
  border-radius: 4px;
  color: rgba(0, 0, 0, 0.65);
  background-color: #fff;
  border-color: #d9d9d9;
}
.ant-btn-primary {
  color: #fff;
  background-color: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.12);
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.045);
}
</style>