<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="标识">
          <a-input v-model="identity" />
        </a-form-item>
        <a-form-item>
          <a-button-group>
            <a-button type="primary" icon="search" @click="show(1)">查询</a-button>
            <a-button type="primary" icon="reload" @click="reData()">重置</a-button>
          </a-button-group>
        </a-form-item>
      </a-form>
      <div class="button-group">
        <a-form-item>
          <a-button icon="plus" type="primary" @click="handleExportXls('设备信息')">导出</a-button>
          <a-button style="margin-left: 20px" icon="carry-out" type="primary" @click="handleAdd()">采购</a-button>
        </a-form-item>
      </div>
      <div>
        <showTable ref='showTable' @ok="handleOk" :columns="columns" :dataSource="dataSource" :dataModel="dataModel"></showTable>
      </div>
    </a-card>
    <addAndEditPDS ref="modalform1" @ok="handleOk"> </addAndEditPDS>
  </div>
</template>
<script>
import showTable from './modules/showTable.vue'
import addAndEditPDS from './../procurementPlan/modules/addAndEditPDS.vue'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { o_postAction, o_getAction } from '@/api/onApi.js'
export default {
  name: 'equipmentInformation',
  mixins: [JeecgListMixin],
  components: {
    addAndEditPDS,
    showTable
  },
  data() {
    return {
      identity:'',
      addOrEditFlag: 1,
      dataSource: [],
      columns: [
        {
          title: '设备编码',
          align: 'center',
          dataIndex: 'identity',
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'equipment_name',
        },
        {
          title: '设备属性',
          align: 'center',
          dataIndex: 'equipment_property',
        },
        {
          title: '设备型号',
          align: 'center',
          dataIndex: 'equipment_model',
        },
        {
          title: '设备规格',
          align: 'center',
          dataIndex: 'equipment_specification',
        },
        {
          title: '购买日期',
          align: 'center',
          dataIndex: 'purchase_date',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/online/cgreport/api/getColumnsAndData/1415202128682090498',
        exportXlsUrl: '/online/cgreport/api/exportXls/1415202128682090498',
      },
      dataModel: 'f6608bd8f06b45d59cf5995fa7b511cb',
    }
  },
  mounted() {
    this.show()
  },
  methods: {
    reData(){
      this.identity=''
    },
    handleOk() {
      this.show()
    },
    //采购
    handleAdd() {
      this.addOrEditFlag = 1
      this.$refs.modalform1.openModal('', this.addOrEditFlag)
    },
    //展示列表f6608bd8f06b45d59cf5995fa7b511cb
    show() {
      let params = { pageNo: 1, pageSize: 9999 }
      if (this.identity) {
        params.identity = this.identity
      }
      o_getAction('/cgreport/api/getColumnsAndData/1415202128682090498',params).then((res) => {
        this.dataSource = res.result.data.records
      })
      this.$refs.showTable.openModal('')
    },
  },
}
</script>