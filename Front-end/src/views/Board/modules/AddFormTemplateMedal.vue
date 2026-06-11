<template>
  <a-modal
    title="新增"
    :visible="addFormTemplateVisible"
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
import {axios} from "../../../utils/request";
import qs from 'qs'

export default {
  name:'AddFormTemplateMedal',
  components:{
  },
  props:{
    addFormTemplateVisible:{
      type:Boolean,
      default:false
    },
    formTableData:{
      type:Array
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
  computed:{
    validatorRules:function() {
      return {
        name: {
          rules: [
            { required: true, message: '请输入表单名称!' }
            ]
        },
        encoding: {
          rules: [
            { required: true, message: '请输入表单名称!' },
            { pattern:new RegExp('^[a-zA-Z][a-zA-Z0-9_-]{1,30}$'),message:'编码必须以字母开头，可包含数字、下划线、横杠。不得超过32个字符！'},
            { validator: (rule, value, callback) => {
              if(!this.formTableData.some(item => item.encoding===value))
                callback();
              },message:'该值不可用，系统中已存在！'
            }
        ]}
      }
    }
  },
  methods:{
    handleOk(e){
      e.preventDefault();
      this.confirmLoading=true;
      this.form.validateFields((err, fieldsValue) => {
        if (err) {
          return false;
        }
        let requestData={
          name: fieldsValue['name'],
          encoding: fieldsValue['encoding'],
          icon: this.form.icon,
          content:JSON.stringify({
            list: [],
            config: {
            labelWidth: 100,
            labelPosition: 'right',
            size: 'small',
            customClass: '',
            ui: 'antd',
            layout: 'horizontal',
            labelCol: 5,
            dataBase:'',
            tableId:''
          },
          })
        };
        requestData.userId = 9999;
        axios({
          url: '/desform/create',
          method: 'post',
          data: qs.stringify(requestData),
          baseURL: this.FORM_MAKING_BASE_URL
        }).then(res=>{
          if(res.success){
            this.$message.success('新增成功');
            axios({
              url: '/desform/search',
              method: 'post',
              data: qs.stringify(data),
              baseURL: this.FORM_MAKING_BASE_URL
            }).then(res=>{
              if(res.success){
                let id=res.data.records[0].id;
                axios({
                  url: '/desform/updateContent',
                  method: 'post',
                  data: qs.stringify({id:id,content:requestData.content}),
                  baseURL: this.FORM_MAKING_BASE_URL
                }).then(res=>{
                  if(!res.success){
                    this.$message.console.warning('表单数据初始化失败');
                  }
                })
              }
            })
            this.$emit('loadData',{});
            this.confirmLoading=false;
            this.$emit('update:addFormModalVisible',false);
          }
          else{
            this.$message.error('新增失败');
            this.$emit('loadData',{});
            this.confirmLoading=false;
            return false;
          }
        })
      });
    },
    handleCancel(){
        this.$emit('update:addFormTemplateVisible',false);
    },
  }
}
</script>

<style scoped>

</style>