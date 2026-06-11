<template>
  <div>
    <el-card>
      <el-form :inline="true">
        <el-form-item label="分类属性">
          <el-select v-model="field" placeholder="属性">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column :prop="oldField" :label="fieldLabel"> </el-table-column>
        <el-table-column prop="num" label="数量"> </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getAction, postAction, deleteAction } from '@/api/manage'
import api from '@/api/index'
import qs from 'qs'

export default {
  data() {
    return {
      serverUrl: api.server_url,
      options: [
        {
          value: 'unit_name',
          label: '货位'
        },
        {
          value: 'manager_name',
          label: '审核人'
        },
        {
          value: 'early_warning_value',
          label: '预警值'
        },
        {
          value: 'is_frozen',
          label: '冻结状态'
        },
        {
          value: 'batch',
          label: '批次'
        },
        {
          value: 'cargo_owner',
          label: '货主'
        }
      ],
      value: '',
      field: '',
      oldField: '',
      fieldLabel: '',

      url: {
        statisticsUrl: '/wms/wms/statistics'
      },

      tableData: []
    }
  },

  created() {},

  methods: {
    onSubmit() {
      getAction(this.serverUrl + this.url.statisticsUrl, { field: this.field })
        .then(res => {
          // console.log(res)
          this.options.forEach(item => {
            if (item.value === this.field) {
              this.fieldLabel = item.label
              this.oldField = this.field
            }
            this.tableData = res.result
          })
        })
        .catch(err => {})
    }
  }
}
</script>

<style scoped>
.el-table {
  margin-top: 30px;
}
</style>
