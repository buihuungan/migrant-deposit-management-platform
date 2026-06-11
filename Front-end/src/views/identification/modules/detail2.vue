<template>
  <a-modal
    title="详情"
    :visible="visible"
    :centered=true
    :footer="null"
    @cancel="handleCancel"
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
      <a-form-item class="detailItem" label="标识编码">
        <a-input
          class="detailInput"
          :addon-before="addonbefore"
          :disabled="true"
          v-model="form.identify"
        />
      </a-form-item>
      <div id="components-table-demo-size">        
        <!-- <h4>Small size table</h4> -->
        <a-table
          :columns="columns"
          :data-source="data"
          :pagination="ipagination"
          size="small"
        />
      </div>
    </a-form>
  </a-modal>
</template>

<script>
import editTable from "./editTable";
// import axios from "axios";
import api from "@/api/index.js";
import { Message } from "element-ui";
// import qs from "qs";
import { tm_getAction } from '@/api/templateApi';
export default {
  props:['tableName1','listData'],
  data() {
    return {
      identity: "",
      identify: "",
      addonbefore: "",
      visible: false,
      columns: [
          { dataIndex: "paramName", title: "属性名", width: 175 },
          { dataIndex: "paramType", title: "属性类别", width: 175 },
          { dataIndex: "paramValue", title: "属性值", width: 175 },
        ],
      form: {
        identityIndex: "",
        identify: "",
      },
      data:[],
      dataitem:{},
      ipagination: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        pageSize: 8,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
    };
  },
  created() {},

  methods: {
    getTableData(record){
      this.form.identityIndex=record.identity.split("/")[0];
      this.form.identify=record.identity;
      console.log("record.id",record.id);
      var identity = {
        'identity': record.identity
      }

      tm_getAction('/cgform/api/form/table_name/'+this.tableName1+'/'+record.id).then(res=>{
        console.log("res1",res.result);
        for(let index = 0 ;index <this.listData.length;index++){
          var data2 = {paramName :'',paramType:'',paramValue:''}
          this.$set(data2,'paramName',this.listData[index].dbFieldName) 
          this.$set(data2,'paramType',this.listData[index].dbFieldTxt) 
          this.$set(data2,'paramValue',res.result[this.listData[index].dbFieldName]) 
          this.data.push(data2)
        }
        this.data = this.data.filter((item)=>{ 
          return item.paramName !='id' && item.paramName!='identity'  && item.paramName!='create_time' && item.paramName!='update_time'
        })
        console.log("detail2的data",this.data);
      }).catch(err=>{
        console.log("err");
      })
    },
    openModal() { 
      this.visible = true;
    },
    handleCancel() {
      this.visible = false;
      this.data = [];
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