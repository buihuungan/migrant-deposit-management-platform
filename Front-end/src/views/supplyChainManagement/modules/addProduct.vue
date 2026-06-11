<template>
  <a-modal
    title="新增"
    :visible="visible"
    :centered="true"
    okText="确定"
    cancelText="取消"
    :destroyOnClose="true"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="产品名称">
        <a-select v-model="form.prodName" placeholder="请选择生产产品" style="width: 300px" allowClear>
          <a-select-option v-for="(item, index) in productList" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="生产数量">
        <a-input v-model="form.prodNumber" placeholder="请输入生产数量" style="width: 300px" />
      </a-form-item>
      <a-form-item label="生产日期">
        <a-date-picker :format="dateFormat" @change="onChange" style="width: 300px" />
      </a-form-item>
      <a-form-item label="生产产线">
        <a-select v-model="form.prodProductionLine" placeholder="请选择生产产线" style="width: 300px" allowClear>
          <a-select-option v-for="(item, index) in productionLineList" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import axios from 'axios'
import moment from 'moment'
import { erp_postAction } from '@/api/erpApi'

export default {
  data() {
    return {
      visible: false,
      productList: ['基站天线-88.175.111/O30050150208B3'],
      productionLineList: ['J2', 'J5'],
      form: {
        prodName: '',
        prodNumber: '',
        prodDate: '',
        prodProductionLine: '',
        exampleId: ''
      },
      dateFormat: 'YYYY-MM-DD',
    }
  },
  methods: {
    openModal() {
      this.visible = true
    },
    handleOk() {
      this.visible = false
      this.$set(this.form, 'productionStatus', 0)
      this.form.exampleId = this.$store.state.exampleId
      console.log('this.form', this.form)
      erp_postAction('/ProductProduction/insert', this.form).then((res) => {
        console.log('res', res)
        this.$parent.loadData()
      })
    },
    handleCancel() {
      this.visible = false
      form.resetFields()
    },
    onChange(date, dateString) {
      this.form.prodDate = dateString
    },
  },
}
</script>

<style scoped></style>
