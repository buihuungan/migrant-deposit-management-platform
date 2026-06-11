<template>
  <a-modal
    title="配置地址"
    :visible="configAddressModalVisible"
    @cancel="handleCancel"
    width="45%"
  >
    <a-input :defaultValue="listAddress+tableId" addonBefore="数据列表地址" :readonly="true" style="margin-bottom:10px">
      <a slot="addonAfter" @click="openList">打开</a>
    </a-input>
    <a-input :defaultValue="addAddress+this.formTableData[tableIndex].id" addonBefore="后台新增地址" :readonly="true" style="margin-bottom:10px">
      <a slot="addonAfter" @click="openForm('add')">打开</a>
    </a-input>
    <a-input :defaultValue="editAddress+this.formTableData[tableIndex].id+'/{dataId}'" addonBefore="后台修改地址" :readonly="true" style="margin-bottom:10px">
      <a slot="addonAfter" @click="openDataIdModal('edit')">打开</a>
    </a-input>
    <a-input :defaultValue="detailAddress+this.formTableData[tableIndex].id+'/{dataId}'" addonBefore="后台详情地址" :readonly="true" style="margin-bottom:10px">
      <a slot="addonAfter" @click="openDataIdModal('detail')">打开</a>
    </a-input>

    <a-modal
      :visible="dataIdModalVisible"
      @cancel="closeDataIdModal"
      @ok="openForm(openType)"
      title="输入dataId">
      <a-input placeholder="请输入dataId" v-model="dataId" />
    </a-modal>
  </a-modal>
</template>

<script>
export default {
  name:'ConfigAddressModal',
  props:{
    configAddressModalVisible:{
      type:Boolean,
      default:false
    },
    tableId:{
      type:String,
      default:''
    },
    formTableData:{
      type:Array
    },
    tableIndex:{
        type:Number,
        default:0
    }
  },
  data(){
    return{
      listAddress:'/fmaking/formData/',
      addAddress:'/fmaking/showForm/add/',
      editAddress:'/fmaking/showForm/edit/',
      detailAddress:'/fmaking/showForm/detail/',
      dataId:'',
      dataIdModalVisible:false,
      openType:''
    }
  },
  created(){
    console.log(this.formTableData)
    console.log(this.formTableData[tableIndex].id)
  },
  methods:{
    openList(){
      let routeData = this.$router.resolve({ path: this.listAddress+this.tableId});
      window.open(routeData.href, '_blank');
    },
    openDataIdModal(type){
      this.dataIdModalVisible=true;
      this.openType=type;
    },
    closeDataIdModal(){
      this.dataIdModalVisible=false;
      this.openType='';
    },
    openForm(type){
      if(type==='add'){
        let routeData = this.$router.resolve({ path: this.addAddress+this.formTableData[this.tableIndex].id});
        window.open(routeData.href, '_blank');
      }
      else if(type==='edit'){
        let routeData = this.$router.resolve({ path: this.editAddress+this.formTableData[this.tableIndex].id+'/'+this.dataId});
        window.open(routeData.href, '_blank');
      }
      else{
        let routeData = this.$router.resolve({ path: this.detailAddress+this.formTableData[this.tableIndex].id+'/'+this.dataId});
        window.open(routeData.href, '_blank');
      }
      this.dataIdModalVisible=false;
    },
    handleCancel(){
      this.$emit('update:configAddressModalVisible',false);
    }
  },
  /*watch:{
    'configAddressModalVisible':function(val){
      if(val)
    }
  }*/
}
</script>