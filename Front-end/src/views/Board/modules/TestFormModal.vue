<template>
  <a-modal
    title="测试表单"
    :visible="testFormModalVisible"
    @cancel="handleCancel"
    :footer="null"
    style="text-align:center"
  >
    <GenerateForm
      v-if="testFormModalVisible"
      :data="data"
      ref="generateForm"
      insite="true"
    >
    </GenerateForm>
    <a-button type="primary" @click="handleOk">Submit</a-button>
  </a-modal>
</template>

<script>
// import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import { getAction,postAction } from '@/api/manage'

export default {
  name:'TestFormModal',
  data(){
    return{
      data:{
        "list": [],
        "config": {
          "labelCol": 3,
          "layout" : "horizontal",
          "size": "small",
          "customClass": ""
        }
      },
      confirmLoading:false,
      dataTable:{
        id:'',
        name:''
      }
    }
  },
  components:{
    // GenerateForm
  },
  props:{
    testFormModalVisible:{
      type:Boolean,
      default:false
    },
    formTableData:{
      type:Array,
      default:()=>[]
    },
    tableIndex:{
      type:Number,
      default:0
    }
  },
  methods:{
    initForm(){
      if(this.formTableData[this.tableIndex].content!==''){
        this.data=JSON.parse(this.formTableData[this.tableIndex].content);
        this.getDataTableId(this.data.config.dataBase);
      }
      else this.data={
        "list": [],
        "config": {
          "labelCol": 3,
          "layout" : "horizontal",
          "size": "small",
          "customClass": ""
        }
      };
    },
    getDataTableId(tableName){
      let param = Object.assign({}, this.queryParam, this.isorter ,this.filters);
      param.copyType = 0;
      param.field="id,tableName";
      if(tableName===''){
        this.dataTable={id:'', name:''};
        return false;
      }
      getAction('/cgform/head/list', param).then((res) => {
        if (res.success) {
          this.$store.commit('changeDataBaseList',res.result.records);
          let record=res.result.records.find(item=>item.tableName===tableName);
          if(record===undefined){
            this.dataTable={id:'', name:''};
          }
          else{
            this.dataTable={id:record.id, name:tableName};
          }
        }
        if(res.code===510){
          this.$message.warning(res.message)
          this.dataTable={id:'', name:''};
        }
      })
    },
    handleOk(){
      if(this.dataTable.id===''){
        this.$message.error('未绑定数据库');
        return false;
      }
      this.$refs.generateForm.getData()
        .then(data => {
          let importData={};
          for(let item of this.data.list){
            if(item.tableCol!==''){
              importData[item.tableCol]=data[item.model];
            }
          }
          postAction('/cgform/api/form/'+this.dataTable.id,importData)
            .then((res)=>{
              if(res.success) {
                this.$message.success('提交成功');
                this.$emit('update:testFormModalVisible',false);
              }
              else this.$message.error('提交失败');
            })
            .catch(e => {
              this.$message.error(e);
            })
        })
        .catch(e => {
          this.$message.error(e);
        })
    },
    handleCancel(){
        this.$emit('update:testFormModalVisible',false);
    }
  },
  watch:{
    'testFormModalVisible':function(val){
      if(val) this.initForm();
    }
  }
}
</script>