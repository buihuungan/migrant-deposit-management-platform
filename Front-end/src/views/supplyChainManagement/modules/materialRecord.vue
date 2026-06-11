<template>
  <a-modal
    title="物料标识详情"
    :visible="visible"
    okText="确定"
    cancelText="取消"
    width="80%"
    :destroyOnClose="true"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-table
      style="height: 500px"
      ref="table"
      size="small"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      :key="dataSource.id"
    >
    </a-table>
  </a-modal>
</template>

<script>
import axios from 'axios'
import api from '@/api/index'
export default {
  data() {
    return {
      visible: false,
      dataSource: [],
      columns: [
        {
          title: '物料标识编码',
          align: 'center',
          width: '30%',
          dataIndex: 'identity',
        },
        {
          title: '名称',
          align: 'center',
          width: '15%',
          dataIndex: 'name',
        },
        {
          title: '型号',
          align: 'center',
          width: '20%',
          dataIndex: 'materialsModel',
        },
        {
          title: '单位',
          align: 'center',
          width: '10%',
          dataIndex: 'unit',
        },
        {
          title: 'PN码',
          align: 'center',
          width: '25%',
          dataIndex: 'pnCode',
        },
      ],
      parentRecord: {},
      pnCode: '',
      pageSize: 10,
      pageNo: 1,
      ipagination: {
        current: 1,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        pageSize: 10,
        pageNo: 1,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
    }
  },
  methods: {
    openModal(record) {
      console.log('ssssssssssssssss',record)
      this.pnCode = record.pnCode
      this.visible = true
      this.loadData()
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false
    },
    loadData() {
      var requestData = {
        pnCode: this.pnCode,
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
      }
      axios({
        method: 'post',
        baseURL: api.server_url + '/identity',
        url: `/docking/pnCode`,
        params: requestData,
      }).then((res) => {
        this.dataSource = res.data.result.list
        this.ipagination.total = res.data.result.total
      })
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.pageNo = page
      this.pageSize = pageSize
      this.loadData()
    },
  },
}
</script>

<style>
</style>