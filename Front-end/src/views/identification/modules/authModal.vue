<template>
  <a-modal
    title="角色授权"
    :visible="visible"
    :centered="true"
    okText="确定"
    cancelText="取消"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <div id="components-table-demo-size">        
      <!-- <h4>Small size table</h4> -->
      <a-table
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :columns="columns"
        :data-source="characterData"
        :pagination="ipagination"
        size="small"
      />
    </div>
  </a-modal>
</template>

<script>
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
      visible: false,
      selectedRowKeys: [],
      tableName:'',
      columns: [
          { dataIndex: "authName", title: "用户名", width: 175 },
          { dataIndex: "actionType", title: "操作类型", width: 175 },
        ],
        //武汉虹信 whhc
      characterData:[{
        authName: '武汉虹信',
        actionType: 'select',
      },
      ],
      ipagination: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        pageSize: 5,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
    };
  },
  created() {},

  methods: {
    openModal(record) { 
      this.tableName = record.tableName
      console.log("this.tableName",this.tableName)
      this.visible = true;
    },
    detailTableData(res){
    },
    handleCancel() {
      this.visible = false;
      this.selectedRowKeys =[]
      // this.characterData=[]
      console.log("handleCancel");
    },
    handleOk() {
      this.visible = false;
      let data =[]
      data.tableName=this.tableName
      data.userName= 'whhx'
      console.log("data",data);
      if(this.selectedRowKeys.length!=0){
        im_postAction('/grant',qs.stringify(data)).then(res=>{
        if(res.data.success){
          Message({
                type: 'success',
                showClose: true,
                message: '授权成功!',
              })
        }
      }).catch(err=>{
        console.log(err)
      })
      }
      console.log("handleOk");
      this.selectedRowKeys =[]
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page;
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      console.log('selectedRowKeys', this.selectedRowKeys);
    },
  },
};
</script>

<style lang="less" scoped>
.ant-modal-content {
  width: 500px !important;
}
.detailInput{
  color: black !important;
  background-color: white !important;
}
.detailItem > .ant-form-item-label{
  display: flex !important;
}
</style>