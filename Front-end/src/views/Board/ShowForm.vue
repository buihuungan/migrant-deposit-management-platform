<template>
  <a-card :bordered="false" style="text-align:center">
    <GenerateForm
      v-if="formVisible"
      :data="data"
      ref="generateForm"
      insite="true"
      :edit="edit"
    >
    </GenerateForm>
    <a-button type="primary" @click="handleOk">提交</a-button>
  </a-card>
</template>

<script>
import {axios} from "../../utils/request";

export default {
  name:'ShowForm',
  data(){
    return{
      formVisible:false,
      data:{
        "list": [],
        "config": {
          "labelCol": 3,
          "layout" : "horizontal",
          "size": "small",
          "customClass": ""
        }
      },
      dataTable:{
        id:'',
        name:''
      },
      type:'',
      formId:'',
      dataId:'',
      tableData:[],
      edit:true
    }
  },
  components:{
    GenerateForm
  },
  created(){
    this.initForm();
  },
  methods:{
    initForm(){
      let strs = this.$route.path.split("/"); //字符分割
      this.type=strs[3];
      this.formId=this.$route.params.formId;
      this.dataId=this.$route.params.dataId;
      if(this.type==='add'){
        this.getFormData(1);
      }
      else if(this.type==='edit'){
        this.getFormData(2);
      }
      else{
        this.getFormData(2);
        this.edit=false;
      }
    },
    getFormData(arg){
      if(arg===1){
        axios({
          url: `/desform/${this.formId}/edit`,
          method: 'get',
          baseURL: this.FORM_NAKING_BASE_URL
        }).then(res=>{
          if(res.success){
            this.data=JSON.parse(res.data);
            this.formVisible=true;
            return true;
          }
          else{
            this.$message.error('表单初始化失败');
            return false;
          }
        })
        .catch(e=>{
          this.$message.error(e);
            return false;
        })
      }
      else{
        axios({
          url: `/desform/${this.formId}/edit`,
          method: 'get',
          baseURL: this.FORM_MAKING_BASE_URL
        }).then(res=>{
          if(res.success){
            this.data=JSON.parse(res.data);
            if(this.data.config.tableId!==''){
              let param ={
                column: "createTime",
                order: "desc",
                pageNo: 1,
                pageSize: -1,
                superQueryMatchType: "and"
              }
              axios({
                url: `/online/cgform/api/getData/${this.data.config.tableId}`,
                method: 'get',
                data: param
              }).then(res=>{
                  if(res.success){
                    let result = res.result;
                    this.tableData = result.records;
                    let dataRow=this.tableData.find(item=>item.id===this.dataId);
                    if(typeof dataRow!=='undefined'){
                      this.fillData(dataRow);
                      this.formVisible=true;
                    }
                    else{
                      this.$message.warning('无该dataId对应数据,即将关闭当前窗口..');
                      setTimeout(
                        window.close(),1000
                      );
                    }
                  }
                  else{
                    this.$message.warning(res.message);
                  }
                })
            }
          }
          else{
            this.$message.error('表单初始化失败');
            return false;
          }
        })
        .catch(e=>{
          this.$message.error(e);
            return false;
        })
      }
    },
    fillData(dataRow){
      if (Object.keys(this.data).length) {
        if(this.data.list.length>0){
          let keys=Object.keys(dataRow);
          for(let i=0;i<keys.length;i++){
            if(keys[i]==='id')continue;
            let index=this.data.list.findIndex(item=>item.tableCol===keys[i]);
            if(index!==-1)this.data.list[index].options.defaultValue=dataRow[keys[i]];
          }
        }
      }
    },
    handleOk(){
      if(this.type==='add')this.addForm();
      else if(this.type==='edit')this.editForm();
    },
    addForm(){
      this.$refs.generateForm.getData()
        .then(data => {
          let importData={};
          for(let item of this.data.list){
            if(item.tableCol!==''){
              importData[item.tableCol]=data[item.model];
            }
          }
          axios({
            url: `/online/cgform/api/form/${this.data.config.tableId}`,
            method: 'post',
            data: importData
          }).then((res)=>{
              if(res.success) {
                this.$message.success('提交成功');
              }
              else this.$message.error('提交失败');
            })
            .catch(e => {
              this.$message.error(e);
            })
        }).catch(e => {
          this.$message.error(e);
        })
    },
    editForm(){
      this.$refs.generateForm.getData()
        .then(data => {
          let importData={
            id:this.dataId
          };
          for(let item of this.data.list){
            if(item.tableCol!==''){
              importData[item.tableCol]=data[item.model];
            }
          }
          axios({
            url: `/online//cgform/api/form/${this.data.config.tableId}`,
            method: 'put',
            data: importData
          }).then((res)=>{
              if(res.success) {
                this.$message.success('提交成功');
              }
              else this.$message.error('提交失败');
            })
            .catch(e => {
              this.$message.error(e);
            })
        }).catch(e => {
          this.$message.error(e);
        })
    }
  },
}
</script>