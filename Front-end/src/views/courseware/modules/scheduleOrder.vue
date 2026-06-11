<template>
  <a-modal
    :title="title"
    :visible="visible"
    :centered="true"
    okText="确定"
    cancelText="取消"
    :destroyOnClose="true"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="标识编码">
        <a-select v-model="form.prodId" placeholder="请选择标识编码" style="width: 300px" allowClear>
          <a-select-option v-for="(item, index) in identityList" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="产品名称">
        <a-select v-model="form.prodName" placeholder="请选择产品名称" style="width: 300px" allowClear>
          <a-select-option v-for="(item, index) in productNameList" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="生产数量">
        <a-input v-model="form.amount" placeholder="请输入生产数量" style="width: 300px" />
      </a-form-item>
      <a-form-item label="排产日期">
        <a-date-picker v-model="form.schedulingDate"  :format="dateFormat" @change="onChange" style="width: 300px" />
      </a-form-item>
      <a-form-item label="生产产线">
        <a-select v-model="form.productionLineId" placeholder="请选择生产产线" style="width: 300px" allowClear>
          <a-select-option v-for="(item, index) in productionLineList" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="发布状态">
        <a-select v-model="form.isPublish" placeholder="请选择发布状态" style="width: 300px" allowClear>
          <a-select-option v-for="(item, index) in publishStatusList" :key="index" :value="index">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="生产状态">
        <a-select v-model="form.productionStatus" placeholder="请选择生产状态" style="width: 300px" allowClear>
          <a-select-option v-for="(item, index) in productionStatusList" :key="index" :value="index">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {erp_postAction} from '@/api/erpApi'
export default {
  data() {
    return {
      title: '',
      wareId: '',
      visible: false,
      identityList: ['88.175.111/O30050150208B3'],
      productNameList: ['基站天线'],
      productionLineList: ['J2', 'J5'],
      publishStatusList: ['待发布','已发布'],
      productionStatusList: ['待生产','开始生产','生产完毕','验收成功','已经入库','出货检查成功','出货完成','货物已退回'],
      //修改 日排产计划 表里该条数据的生产状态 0为默认值 1为开始生产 2为生产完毕 3为验收成功 4为产品入库 5为出货检查成功 6为出货完成 7货物已退回
      form: {
        id: '',
        prodId: '',
        prodName: '',
        amount: '',
        schedulingDate: '',
        productionLineId: '',
        isPublish: '',
        productionStatus: '',
        cprqqId: '',
        cpchqId: '',
        exampleId: ''
      },
      dateFormat: 'YYYY-MM-DD',
    }
  },
  methods: {
    openScheduleOrderModal(record) {
      this.title = '预置'
      this.wareId = record.ware_id
      erp_postAction(`/preset/selectInitData/${this.wareId}`)
      .then(res => {
        if(res.result) {
          this.form.id = res.result.dailyProductionSchedulingPlan.id
        }
      })
      this.visible = true
    },
    openScheduleOrderDetail(record) {
      this.title = '详情'
      this.wareId = record.ware_id
      erp_postAction(`/preset/selectInitData/${this.wareId}`)
      .then(res => {
        if(res.result) {
          this.form = res.result.dailyProductionSchedulingPlan
          this.visible = true
        }
        else {
          this.$message.warning('未预置排产订单数据，请先预置排产订单数据！')
        }
      })
    },
    handleOk() {
      erp_postAction(`/preset/insertInitData/${this.wareId}`,this.form)
      .then(res => {
        this.$message.success('排产订单数据预置成功！')
      })
      this.visible = false
      this.form = {
        id: '',
        prodId: '',
        prodName: '',
        amount: '',
        schedulingDate: '',
        productionLineId: '',
        isPublish: '',
        productionStatus: '',
        cprqqId: '',
        cpchqId: '',
        exampleId: ''
      }
    },
    handleCancel() {
      this.visible = false
      this.form = {
        id: '',
        prodId: '',
        prodName: '',
        amount: '',
        schedulingDate: '',
        productionLineId: '',
        isPublish: '',
        productionStatus: '',
        cprqqId: '',
        cpchqId: '',
        exampleId: ''
      }
    },
    onChange(date, dateString) {
      this.form.schedulingDate = dateString
    },
  }
}
</script>

<style>

</style>