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
          <a-button icon="plus" type="primary" @click="handleExportXls('设备材料信息')">导出</a-button>
          <a-button style="margin-left: 20px" icon="carry-out" type="primary" @click="handleAdd()">采购</a-button>
        </a-form-item>
      </div>
      <div>
        <showTable ref='showTable' @ok="handleOk" :columns="columns" :dataSource="dataSource" :dataModel="dataModel"></showTable>
      </div>
    </a-card>
    <addAndEditPDS ref="modalform" @ok="handleOk"> </addAndEditPDS>
  </div>
</template>
<script>
import showTable from './modules/showTable.vue'
import addAndEditPDS from './../procurementPlan/modules/addAndEditPDS.vue'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { o_postAction, o_getAction } from '@/api/onApi.js'
export default {
  name: 'spacePartInformation',
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
          title: '材料编码',
          align: 'center',
          dataIndex: 'identity',
        },
        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'material_name',
        },
        {
          title: '材料单位',
          align: 'center',
          dataIndex: 'material_unit',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/online/cgreport/api/getColumnsAndData/1415208075496325122',
        exportXlsUrl: '/online/cgreport/api/exportXls/1415208075496325122',
      },
      dataModel: '9ce5b6a701bf4799a36c681288389715',
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
      this.$refs.modalform.openModal('', this.addOrEditFlag)
    },
    //展示列表
    show() {
      let params = { pageNo: 1, pageSize: 9999 }
      if (this.identity) {
        params.identity = this.identity
      }
      o_getAction('/cgreport/api/getColumnsAndData/1415208075496325122',params).then((res) => {
        this.dataSource = res.result.data.records
      })
      this.$refs.showTable.openModal('')
    },
  },
}
</script>