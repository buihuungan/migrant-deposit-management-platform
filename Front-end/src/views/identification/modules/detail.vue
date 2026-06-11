<template>
  <a-modal
    title="详情"
    :visible="visible"
    :centered=true
    :footer="null"
    @cancel="handleCancel"
    width ="650px"
  >
    <a-form
      :form="form"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 12 }"
    >
      <a-form-item class="detailItem" label="前缀">
        <a-input
          class="detailInput"
          :disabled="true"
          v-model="form.identityIndex"
        >
        </a-input>
      </a-form-item>
      <a-form-item class="detailItem" label="标识">
        <a-input
          class="detailInput"
          :addon-before="addonbefore"
          :disabled="true"
          v-model="form.identify"
        />
      </a-form-item>
      <a-form-item class="detailItem" label="模板">
        <a-input
          class="detailInput"
          :disabled="true"
          v-model="form.templateName"
        />
      </a-form-item>
      <div id="components-table-demo-size">        
        <!-- <h4>Small size table</h4> -->
        <a-table
          :columns="columns"
          :data-source="detailData"
          :pagination="ipagination"
          :scroll="{  x: false,y: 300 }"
          size="small"
        />
      </div>
    </a-form>
  </a-modal>
</template>

<script>
import editTable from "./editTable";
import axios from "axios";
import api from "@/api/index.js";
import {
  im_postAction,
  im_deleteAction,
  im_getAction,
  im_putAction,
} from "@/api/icimApi";
import {
  pm_postAction,
  pm_deleteAction,
  pm_getAction,
  pm_putAction,
} from "@/api/icipmApi";
import { Message } from "element-ui";
import qs from "qs";
export default {
  data() {
    return {
      identification: "",
      identify: "",
      addonbefore: "",
      visible: false,
      columns: [
          { dataIndex: "paramName", title: "属性英文", width: 175 },
          { dataIndex: "paramType", title: "属性中文", width: 175 },
          { dataIndex: "paramValue", title: "属性值", width: 175 },
        ],
      form: {
        identityIndex: "",
        identify: "",
        templateName:'',
      },
      detailData:[],
      dataitem:{},
      ipagination: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        pageSize: 30,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
        // hideOnSinglePage: true,
      },
    };
  },
  created() {},

  methods: {
    openModal(record) { 
      console.log("获取选中行的数据：",record)
      this.form.identityIndex=record.identityIndex;
      this.form.identify=record.identification;
      var identification = {
        'identification': record.identification
      }
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      im_postAction('/identityManage/identityParam', JSON.stringify(record))
        .then((res) => {
          console.log("res",res);
          this.form.templateName = res.data.result.tableName

          this.detailTableData(res);
        })
        .catch((err) => {
          console.log(err)
        })
      this.visible = true;
    },
    detailTableData(res){
      // this.detailData = []
      console.log("editTableData");
      let listData = res.data.result.list[0]
      console.log("listData",listData)
      for(let item in listData){
        if(item != 'id'&&item != 'create_time'&&item != 'identity'&&item != 'update_time'){
          let data = { paramName: '', paramType: '', paramValue: '' }
          this.$set(data, 'paramName', item)
          this.$set(data, 'paramType', listData[item].name)
          this.$set(data, 'paramValue', listData[item].value)
          this.detailData.push(data)
        }
      }
    },
    handleCancel() {
      this.visible = false;
      this.detailData = [];
    },
    handleOk() {
      this.visible = false;
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page;
    },
  },
};
</script>

<style lang="less" scoped>
.ant-modal-content {
  width: 799px !important;
}
.detailInput{
  color: black !important;
  background-color: white !important;
}
.detailItem > .ant-form-item-label{
  display: flex !important;
}
</style>