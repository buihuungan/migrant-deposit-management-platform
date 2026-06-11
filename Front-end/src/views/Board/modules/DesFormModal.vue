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
    class="des-container"
  >
    <fm-making-form1
      v-if="isShow"
      ref="makingForm1"
      style="height:100%"
      clearable
      upload
      preview
      generate-code
      generate-json
      :importFormData="formTableData[tableIndex].content"
      :desFormModalVisible="desFormModalVisible"
      @handleOk="handleOk"
      @handleCancel="handleCancel"
    >
      <template slot="action"></template>
    </fm-making-form1>
  </a-modal>
</template>

<script>
import { axios } from '../../../utils/request'
import qs from 'qs'

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
      axios({
        url: '/desform/updateContent',
        method: 'post',
        data: qs.stringify(requestData),
        baseURL: this.FORM_MAKING_BASE_URL
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
      this.isShow = false;
      this.$emit('update:desFormModalVisible',false);
      this.isShow = false;
    },
    handleOpen(){
      this.changeVisible = this.desFormModalVisible;
      this.isShow = true;
    }
  }
}
</script>
<style>
.el-select-dropdown__list{
  display: grid !important;
}
.des-container .ant-modal-body{
  padding: 0;
  height: 100%;
}
.des-container .ant-modal-content{
  border-radius: 0;
  height: 100%;
}
.des-container {
  padding: 0;
  height: 100vh !important;
}
.des-container .ant-modal {
  padding-bottom: 0 !important;
  height: 100%;
}
</style>