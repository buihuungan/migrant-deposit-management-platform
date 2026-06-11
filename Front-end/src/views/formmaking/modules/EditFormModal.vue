<template>
  <a-modal
    title="编辑表单"
    :visible="editFormModalVisible"
    @ok="handleOk"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-form :form="form">
      <a-form-item label="表单名称">
        <a-input placeholder="请输入表单名称" v-decorator="[ 'name', validatorRules.name]" />
      </a-form-item>
      <a-form-item label="表单编号">
        <a-input placeholder="请输入表单编号" v-decorator="[ 'encoding', validatorRules.encoding]" />
      </a-form-item>
      <a-form-item label="表单图标">
        <a-input placeholder="点击右侧按钮选择图标" v-model="model.icon" disabled="disabled">
          <a-icon slot="addonAfter" type="setting" @click="selectIcons" />
        </a-input>
      </a-form-item>

      <!--图标选择-->
      <icons @choose="handleIconChoose" @close="handleIconCancel" :iconChooseVisible="iconChooseVisible"></icons>
    </a-form>
  </a-modal>
</template>

<script>
import Icons from '../../system/modules/icon/Icons'
import { t_putAction } from '@/api/tempApi'

export default {
  name:'EditFormMedal',
  components:{
    Icons
  },
  props:{
    editFormModalVisible:{
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
    },
    searchData:{
      type:Object
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
         name: this.tableIndex < this.formTableData.length ? this.formTableData[this.tableIndex].name:'',
        encoding: this.tableIndex < this.formTableData.length ? this.formTableData[this.tableIndex].encoding:'',
        icon: this.tableIndex < this.formTableData.length ? this.formTableData[this.tableIndex].icon:''
       }
     );
    this.model.icon = this.tableIndex < this.formTableData.length ? this.formTableData[this.tableIndex].icon:'';
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
            name:this.formTableData[this.tableIndex].name,
            encoding:this.formTableData[this.tableIndex].encoding,
            icon:this.formTableData[this.tableIndex].icon
          }
        );
        this.model.icon=this.formTableData[this.tableIndex].icon;
      },
      handleOk(e){
        e.preventDefault();
        this.confirmLoading=true;
        this.form.validateFields((err, fieldsValue) => {
          if (err) {
            return false;
          }
          let qs = require('qs');
          let requestData={
            id:this.formTableData[this.tableIndex].id,
            name:this.form.getFieldValue('name'),
            encoding:this.form.getFieldValue('encoding'),
            icon:this.model.icon,
            // userId:this.formTableData[this.tableIndex].userId
          };
          t_putAction('/admin/desform/update',qs.stringify(requestData))
            .then(res=>{
              if(res.success){
                this.$message.success('保存成功');
                this.$emit('loadData',this.searchData);
                this.confirmLoading=false;
                this.$emit('update:editFormModalVisible',false);
              }
              else{
                console.log(res);
                this.$message.error('保存失败');
                this.$emit('loadData',this.searchData);
                this.confirmLoading=false;
                return false;
              }
            })
            .catch(e=>{
              this.$message.error(e);
              this.confirmLoading=false;
                return false;
            })
        });
      },
      handleCancel(){
          this.$emit('update:editFormModalVisible',false);
      },
      selectIcons(){
        this.iconChooseVisible = true
      },
      handleIconChoose (value) {
        this.model.icon = value
        this.form.icon = value
        this.iconChooseVisible = false
      },
      handleIconCancel () {
        this.iconChooseVisible = false
      },
  },
  watch:{
    'editFormModalVisible':function(val){
      if(val)this.initForm();
    }
  }
}
</script>

<style scoped>

</style>