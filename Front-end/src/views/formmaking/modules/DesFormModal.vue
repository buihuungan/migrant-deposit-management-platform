<template>
  <a-modal
    :visible="desFormModalVisible"
    @ok="handleOk"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :footer="null"
    width="100%"
    :closable="false"
    :centered="true"
    :zIndex="200"
  >
    <fm-making-form
      v-if="isShow"
      ref="makingForm"
      style="height:650px"
      clearable
      upload
      preview
      generate-code
      generate-json
      :importFormData="formTableData[tableIndex].content"
      :desFormModalVisible="desFormModalVisible"
      @handleOk="handleOk"
      @handleCancel="handleCancel">
      <template slot="action"></template>
    </fm-making-form>
  </a-modal>
</template>

<script>
import Vue from 'vue'
import { t_putAction } from '@/api/tempApi'
import { axios } from '../../../utils/request';

export default {
  name:'DesFormModal',
  props:{
    desFormModalVisible:{
      type:Boolean,
      default:false
    },
    formTableData:{
      type:Array
    },
    tableIndex:{
        type:Number,
        default:0
    },
    searchData:{
      type:Object
    }
  },
  data(){
      return{
          confirmLoading:false,
          formData:'',
          changeVisible:false,
          isShow:false
      }
  },
  mounted(){
    if (this.tableIndex < this.formTableData.length)
      this.formData=this.formTableData[this.tableIndex].content;
  },
  methods:{
    handleOk(formData){
      let requestData={
        id:this.formTableData[this.tableIndex].id,
        content:formData
      };
      let qs = require('qs');
        axios({
          url: '/desform/updateContent',
          method: 'put',
          data: qs.stringify(requestData),
          baseURL: process.env.VUE_APP_ONLINE_BASE_URL
        }).then(res=>{
          if(res.success){
            this.$message.success('保存成功');
            this.$emit('loadData',this.searchData);
            this.$emit('update:desFormModalVisible',false);
          }
          else{
            this.$message.error('保存失败('+res.message+')');
            this.$emit('loadData',this.searchData);
          }
        })
        .catch(e=>{
          this.$message.error(e);
        })
    },
    handleCancel(){
      this.isShow=false;
      this.$emit('update:desFormModalVisible',false);
      this.isShow=false;
      // parent.location.reload();
    },
    handleOpen(){
      this.changeVisible=this.desFormModalVisible;
      this.isShow=true;
    }
  }
}
</script>
<style>
.el-select-dropdown__list{
  display: grid !important;
}
</style>