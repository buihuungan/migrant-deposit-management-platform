<template>
  <div class="modal_ant">
    <a-modal title="扫描" :visible="visible" @ok="handleOk" @cancel="handleCancel">
      <el-input
        ref="inputs"
        :autofocus="false"
        placeholder="请扫描"
        clearable
        @ok="handleOk"
        @cancel="handleCancel"
        v-model="identification"
        @change="getAttributeData"
      ></el-input>
      <a-table :columns="columns" :data-source="tableData" size="small" style="margin-top: 20px" />
    </a-modal>
  </div>
</template>

<script>
import { pm_postAction, pm_deleteAction, pm_getAction, pm_putAction } from '@/api/icipmApi'
import { im_postAction, im_deleteAction, im_getAction, im_putAction } from '@/api/icimApi'
export default {
  data() {
    return {
      visible: false,
      identification: '',
      columns: [
        { dataIndex: 'paramName', title: '属性名', width: 175 },
        { dataIndex: 'paramType', title: '属性类别', width: 175 },
        { dataIndex: 'paramValue', title: '属性值', width: 175 },
      ],
      tableData: [],
    }
  },
  mounted() {},
  methods: {
    openScan() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.inputs.focus()
      })
    },
    handleCancel() {
      this.visible = false
      this.identification = ''
      this.tableData = []
      console.log("cancel的tableData",this.tableData);
    },
    getAttributeData() {
      var param_val = encodeURIComponent(this.identification);
      console.log("param_val", param_val);
      im_postAction('/identityManage/identityResolve?identity=' + param_val)
        .then((res) => {
          console.log('res', res)
          let listData = res.data.result.params[0]
          console.log('listData', listData)
          for (let item in listData) {
            if (item != 'id' && item != 'create_time' && item != 'identity' && item != 'update_time') {
              let data = { paramName: '', paramType: '', paramValue: '' }
              this.$set(data, 'paramName', item)
              this.$set(data, 'paramType', listData[item].name)
              this.$set(data, 'paramValue', listData[item].value)
              this.tableData.push(data)
            }
          }
          //this.form.templateName = res.data.result.tableName

          //this.detailTableData(res);
        })
        .catch((err) => {
          console.log(err)
        })
    },
    handleOk(e) {
      console.log('提交')
      // axios
      //   .post(
      //     `${api.server_url}/identity/admin/cgform/api/form/`,
      //     qs.stringify(
      //       { category: 'materiel', pageNo: that.pageNo, pageSize: that.pageSize },
      //       {
      //         headers: {
      //           'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
      //           'Content-Type': 'application/json',
      //         },
      //       }
      //     )
      //   )
      //   .then((res) => {
      //     console.log('res', res)
      //     this.dataSource2 = res.data.result.list
      //   })
      //   .catch((err) => {
      //     console.log(err)
      //   })
      this.visible = false
      this.identification = ''
      this.tableData = []
    },
  },
}
</script>

<style lang="less" scoped>
// .ant-modal {
//   width: 690px !important;
// }
</style>