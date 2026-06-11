<template>
  <a-modal
    title="编辑看板"
    :visible="editFormModalVisible"
    @ok="handleOk"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-form :form="form">
      <a-form-item label="看板名称">
        <a-input placeholder="请输入看板名称" v-decorator="[ 'name', validatorRules.name]" />
      </a-form-item>
      <a-form-item label="看板描述">
        <a-input placeholder="请输入表单描述" v-decorator="[ 'description', validatorRules.description]" />
      </a-form-item>
      <!-- <a-form-item label="表单图标">
        <a-input placeholder="点击右侧按钮选择图标" v-model="model.icon" disabled="disabled">
          <a-icon slot="addonAfter" type="setting" @click="selectIcons" />
        </a-input>
      </a-form-item> -->

      <!--图标选择-->
      <!-- <icons @choose="handleIconChoose" @close="handleIconCancel" :iconChooseVisible="iconChooseVisible"></icons> -->
    </a-form>
  </a-modal>
</template>

<script>
import Icons from '../../system/modules/icon/Icons'
import {axios} from "../../../utils/request";
import qs from 'qs'
import {changeScreen} from '@api/boardapi'

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
          description:'',
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
    // this.form.getFieldDecorator('icon', { initialValue: '' });
    this.form.getFieldDecorator('description', { initialValue: '' });
    this.form.setFieldsValue(
      {
        name: this.formTableData[this.tableIndex] ? this.formTableData[this.tableIndex].name:'',
        description: this.formTableData[this.tableIndex] ? this.formTableData[this.tableIndex].description:'',
        icon: this.formTableData[this.tableIndex] ? this.formTableData[this.tableIndex].icon:''
      }
    );
    this.model.icon = this.formTableData[this.tableIndex] ? this.formTableData[this.tableIndex].icon:'';
  },
  computed:{
    validatorRules:function() {
      return {
        name:{rules: [{ required: true, message: '请输入看板名称!' }]},
        description:{rules: [
          { required: true, message: '请输入看板描述!' },
          // {pattern:new RegExp('^[a-zA-Z][a-zA-Z0-9_-]{1,30}$'),message:'编码必须以字母开头，可包含数字、下划线、横杠。不得超过32个字符！'}
        ]}
      }
    }
  },
  methods:{
    initForm(){
      this.form.setFieldsValue(
        {
          name:this.formTableData[this.tableIndex].name,
          description:this.formTableData[this.tableIndex].description,
          // icon:this.formTableData[this.tableIndex].icon
        }
      );
      // this.model.icon=this.formTableData[this.tableIndex].icon;
    },
    handleOk(e){
      e.preventDefault();
      this.confirmLoading=true;
      this.form.validateFields((err, fieldsValue) => {
        if (err) {
          return false;
        }
        let requestData={
          id: this.formTableData[this.tableIndex].id,
          name: this.form.getFieldValue('name'),
          description: this.form.getFieldValue('description'),
          // icon: this.model.icon
        };
       changeScreen(requestData).then(res=>{
          if(res.status===200){
            this.$message.success('保存成功');
            this.$emit('loadData',this.searchData);
            this.confirmLoading=false;
            this.$emit('update:editFormModalVisible',false);
          }
          else{
            this.$message.error('保存失败');
            this.$emit('loadData',this.searchData);
            this.confirmLoading=false;
            return false;
          }
        }).catch(e=>{
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