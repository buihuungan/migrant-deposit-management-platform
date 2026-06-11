<template>
  <a-modal
    title="编辑"
    :visible="editFormtemplateVisible"
    @ok="handleOk"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-form :form="form">
      <a-form-item label="模板名称">
        <a-input placeholder="请输入模板名称" v-decorator="[ 'name', validatorRules.name]" />
      </a-form-item>
      <a-form-item label="模板编码">
        <a-input placeholder="请输入模板编码" v-decorator="[ 'encoding', validatorRules.encoding]" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import qs from 'qs'
import {axios} from "../../../utils/request";

export default {
  name:'EditFormMedal',
  components:{
  },
  props:{
    editFormtemplateVisible:{
        type:Boolean,
        default:false
    },
    formTableData:{
      type:Array,
      default:()=>[
        {
          name:'',
          encoding:'',
          icon:''
        }
      ]
    },
    tableIndex:{
      type:Number,
      default:0
    }
  },
  data(){
    return{
      visible:false,
      confirmLoading:false,
      form: this.$form.createForm(this),
      iconChooseVisible:false,
      model:{}
    }
  },
  mounted(){
    this.form.getFieldDecorator('name', { initialValue: '' });
    this.form.getFieldDecorator('icon', { initialValue: '' });
    this.form.getFieldDecorator('encoding', { initialValue: '' });
    this.form.setFieldsValue(
      {
        name: this.formTableData[this.tableIndex] ? this.formTableData[this.tableIndex].name:'',
        encoding: this.formTableData[this.tableIndex] ? this.formTableData[this.tableIndex].encoding:'',
        icon: this.formTableData[this.tableIndex] ? this.formTableData[this.tableIndex].icon:''
      }
    );
    this.model.icon = this.formTableData[this.tableIndex] ? this.formTableData[this.tableIndex].icon:'';
  },
  computed:{
    validatorRules:function() {
      return {
        name:{rules: [{ required: true, message: '请输入表单名称!' }]},
        encoding:{rules: [
          { required: true, message: '请输入表单名称!' },
          {pattern:new RegExp('^[a-zA-Z][a-zA-Z0-9_-]{1,30}$'),message:'编码必须以字母开头，可包含数字、下划线、横杠。不得超过32个字符！'}
        ]}
      }
    }
  },
  methods:{
    initForm(){
      this.form.setFieldsValue(
        {
          name: this.formTableData[this.tableIndex].name,
          encoding: this.formTableData[this.tableIndex].encoding,
          icon: this.formTableData[this.tableIndex].icon
        }
      );
      this.model.icon=this.formTableData[this.tableIndex].icon;
    },
    handleOk(e){
      e.preventDefault();
      this.confirmLoading = true;
      this.form.validateFields((err, fieldsValue) => {
        if (err) {
          return false;
        }
        let requestData={
          id: this.formTableData[this.tableIndex].id,
          name: this.form.getFieldValue('name'),
          encoding: this.form.getFieldValue('encoding'),
          icon: this.model.icon,
          userId: this.formTableData[this.tableIndex].userId
        };
        axios({
          url: '/desform/update',
          method: 'put',
          data: qs.stringify(requestData),
          baseURL: this.FORM_MAKING_BASE_URL
        }).then(res=>{
          if(res.success){
            this.$message.success('保存成功');
            this.$emit('loadData',{});
            this.confirmLoading = false;
          } else{
            this.$message.error('保存失败');
            this.$emit('loadData',{});
            this.confirmLoading = false;
            return false;
          }
          this.$emit('update:editFormtemplateVisible',false);
        }).catch(e=>{
          this.$message.error(e);
          this.confirmLoading=false;
            return false;
        })
      });
    },
    handleCancel(){
      this.$emit('update:editFormtemplateVisible',false);
    },
  },
  watch:{
    'editFormtemplateVisible':function(val){
      if(val)
        this.initForm();
    }
  }
}
</script>

<style scoped>

</style>