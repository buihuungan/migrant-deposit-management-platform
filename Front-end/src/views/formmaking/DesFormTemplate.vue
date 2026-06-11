<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="表单名称">
          <a-input v-model="searchData.name" placeholder="请输入表单名称" />
        </a-form-item>
        <a-form-item label="表单编码">
          <a-input v-model="searchData.encoding" placeholder="请输入表单编号" />
        </a-form-item>
        <a-form-item style="margin: 5px 0 10px 2px">
          <a-button-group>
            <a-button type="primary" icon="search" @click="loadData(searchData)">查询</a-button>
            <a-button type="primary" icon="reload" @click="loadData({})">重置</a-button>
          </a-button-group>
        </a-form-item>
      </a-form>
      <div>
        <a-button-group>
          <a-button type="primary" icon="plus" @click="openAddFormTemplateModal">新增</a-button>
          <a-button type="primary" icon="vertical-align-bottom">导入</a-button>
          <a-button type="primary" icon="vertical-align-bottom">导出</a-button>
        </a-button-group>
        <a-button v-show="selectedRowKeys.length>0" style="margin-left:20px" @click="openDeleteFormConfirm('Multiple',selectedRowKeys)">批量删除</a-button>
      </div>
      <div style="padding-top:20px">
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
          <i class="anticon anticon-info-circle ant-alert-icon">
          </i> 已选择 <a><b>{{ selectedRowKeys.length }}</b></a>项
          <a style="margin-left: 24px" @click="clearSelectedRows">清空</a>
        </div>
        <a-table
          :loading="tableLoading"
          :columns="columns"
          :dataSource="formTableData"
          :rowSelection="{selectedRowKeys: selectedRowKeys,onChange: onSelectChange}"
          :pagination="ipagination">
          <span slot="action" slot-scope="text, record,index">
            <a @click="openEditFormTemplateModal(index)">编辑</a>
            <a-divider type="vertical" />
            <a @click="openDesFormModal(index)"><a-icon type="setting" />设计模板</a>
            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">
                更多<a-icon type="down" />
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a @click="openDeleteFormConfirm('single',index)">删除</a>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </div>
    </a-card>


    <!--modal-->
    <AddFormTemplateMedal ref="addFormTemplateModal" :addFormTemplateVisible.sync="addFormTemplateVisible" :formTableData.sync="formTableData" @loadData="loadData"></AddFormTemplateMedal>
    <EditFormTemplateMedal ref="editFormTemplateModal" :editFormtemplateVisible.sync="editFormtemplateVisible" :formTableData.sync="formTableData" :tableIndex="selectedFormIndex" @loadData="loadData"></EditFormTemplateMedal>
    <desForm-modal ref="desFormModal" :desFormModalVisible.sync="desFormModalVisible" :formTableData.sync="formTableData" :tableIndex="selectedFormIndex" @loadData="loadData"></desForm-modal>
  </div>
</template>

<script>
import desFormModal from './modules/DesFormModal'
import AddFormTemplateMedal from './modules/AddFormTemplateMedal'
import EditFormTemplateMedal from './modules/EditFormTemplateMedal'
import { getAction } from '@/api/manage'
import { o_getAction } from '@/api/onApi'
import { t_postAction,t_deleteAction,t_getAction } from '@/api/tempApi'
import { axios } from '@/utils/request'
import moment from 'moment'


export default {
    name:'DesFormTemplate',
    components:{
      AddFormTemplateMedal,
      EditFormTemplateMedal,
      desFormModal
    },
    data(){
      return{
        searchData:{
          name:'',
          encoding:'',
          current:1,
          size:-1
        },
        ipagination:{
        current:0,
        pageSize:10,
        pageSizeOptions:['10','20','30'],
        total:0,
        showSizeChanger:true,
        showQuickJumper:true,
        showTotal:(total, range)=>range+' 共'+total+'条',
        onChange:this.onPageChange.bind(this),
        onShowSizeChange:(current, pageSize)=>{
          this.ipagination.current = 1
          this.ipagination.pageSize = pageSize
          this.searchData.current = 1
          this.searchData.size = pageSize
          this.loadData(this.searchData)
          }
        },
        tableLoading:false,
        columns:[
          {
            title: '模板名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '模板编码',
            align: 'center',
            dataIndex: 'encoding'
          },
          {
            title: '创建时间',
            align: 'center',
            dataIndex: 'createTime',
            customRender: (text) => {
                return moment(text).format('YYYY-MM-DD HH:MM:SS')
              }
          },
          {
            title: '修改时间',
            align: 'center',
            dataIndex: 'updateTime',
            customRender: (text) => {
                return moment(text).format('YYYY-MM-DD HH:MM:SS')
              }
          },
          {
            title: '操作',
            align: 'center',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' }
          }
        ],
        formTableData:[],
        selectedRowKeys:[],
        selectedRows:[],
        desFormModalVisible:false,
        addFormTemplateVisible:false,
        selectedFormIndex:0,
        editFormtemplateVisible:false,
        selectTableId:''
      }
    },
    created(){
      this.loadData({});
    },
    methods:{
      loadData(data){
        this.tableLoading=true;
        //
        // data.userId=9999;
        //
        // console.log(data);
        let qs = require('qs');
       t_postAction('/admin/desform/search', qs.stringify(data)).then((res) => {
        if (res.success) {
          console.log(res)
          this.formTableData = res.result.list
          this.ipagination.total=res.result.total 
          this.tableLoading = false
          //  console.log('##');
          console.log(this.formTableData);
        } else {
          this.$message.error('数据获取失败')
          return false
        }
       })
      },
     getFormContent(index){
        t_getAction('/admin/form/'+this.formTableData[index].id+'/edit')
          .then(res=>{
            if(res.success){
              this.formTableData[index].content=res.data;
            }
            else{
              this.$message.warning(res.message);
            }
          })
          .catch(e=>{
            console.log(e);
          })
      },
      
      onSelectChange(selectedRowKeys, selectionRows){
        this.selectedRowKeys=selectedRowKeys;
        this.selectedRows=selectionRows;
      },
      clearSelectedRows(){
        this.selectedRowKeys=[];
      },
      openAddFormTemplateModal(){
        this.addFormTemplateVisible=true;
      },
      openDesFormModal(index){
        this.getFormContent(index);
        this.selectedFormIndex=index;
        this.desFormModalVisible=true;
        this.$refs.desFormModal.handleOpen();
      },
      openEditFormTemplateModal(index){
        this.selectedFormIndex=index;
        this.editFormtemplateVisible=true;
      },
      openDeleteFormConfirm(type,index){
        let that=this;
        this.$confirm({
          title: '确定删除',
          content: '是否删除数据?',
          okType: 'danger',
          onOk() {
            if(type==='single'){
              t_deleteAction('/admin/form/'+that.formTableData[index].id+'/delete')
                .then(res=>{
                  if(res.success){
                    that.$message.success('删除成功');
                  }
                  else{
                    that.$message.error('删除失败');
                  }
                  that.loadData({});
                })
                .catch(e=>{
                  that.$message.error(e);
                })
            }
            else{
              let requestData=[];
              for(let i=0;i<index.length;i++){
                requestData[i]=that.formTableData[index[i]].id;
              }
              axios({
                url: '/admin/form/ids/delete',
                method:'delete' ,
                data: requestData,
                baseURL:'https://git.data.wust.edu.cn/api'
              })
                .then(res=>{
                  if(res.success){
                    that.$message.success('删除成功');
                  }
                  else{
                    that.$message.error('删除失败');
                  }
                  that.loadData({});
                })
                .catch(e=>{
                  that.$message.error(e);
                })
            }
          }
        });
      },
      onPageChange(page,pageSize){
        this.ipagination.current = page
        this.searchData.current = page
        this.searchData.size=pageSize
        this.loadData(this.searchData)
      },
      openTestFormModal(index){
        t_getAction('/admin/form/'+this.formTableData[index].id+'/edit')
          .then(res=>{
            if(res.success){
              this.formTableData[index].content=res.data;
              this.selectedFormIndex=index;
              this.testFormModalVisible=true;
            }
            else{
              this.$message.warning(res.message);
            }
          })
          .catch(e=>{
            console.log(e);
          })
      },
      openConfigAddressModal(index){
        t_getAction('/admin/form/'+this.formTableData[index].id+'/edit')
          .then(res=>{
            if(res.success){
              this.formTableData[index].content=res.data;
              let formJson=JSON.parse(this.formTableData[index].content);
              if(formJson.config.hasOwnProperty('tableId')&&formJson.config.tableId!==''){
                this.selectTableId=formJson.config.tableId;
                this.selectedFormIndex=index;
                this.configAddressModalVisible=true;
              }
              else{
                this.$message.warning('未绑定数据表');
              }
            }
            else{
              this.$message.error(res.message);
            }
          })
          .catch(e=>{
            console.log(e);
          })
      },
      openFormData(index){
        t_getAction('/admin/form/'+this.formTableData[index].id+'/edit')
          .then(res=>{
            if(res.success){
              this.formTableData[index].content=res.data;
              let formJson=JSON.parse(this.formTableData[index].content);
              if(formJson.config.hasOwnProperty('tableId')&&formJson.config.tableId!==''){
                this.$router.push('/fmaking/formData/'+formJson.config.tableId);
              }
              else{
                this.$message.warning('未绑定数据表');
              }
            }
            else{
              this.$message.warning(res.message);
            }
          })
          .catch(e=>{
            console.log(e);
          })

        let param = Object.assign({}, this.queryParam, this.isorter ,this.filters);
        param.copyType = 0;
        param.field="id,tableName";
        if(this.formTableData[index].content===''){
          this.$message.error('未绑定数据表');
          return false;
        }
        let formJson=JSON.parse(this.formTableData[index].content);
        let tableId='';
        if(formJson.config.dataBase===''){
          tableId='';
          this.$message.error('未绑定数据表');
          return false;
        }
        getAction('/cgform/head/list', param).then((res) => {
          if (res.success) {
            this.$store.commit('changeDataBaseList',res.result.records);
            let record=res.result.records.find(item=>item.tableName===formJson.config.dataBase);
            if(record===undefined){
              this.$message.error('未找到对应数据表');
            }
            else{
              tableId=record.id;
              this.$router.push('/fmaking/formData/'+tableId);
            }
          }
          if(res.code===510){
            this.$message.warning(res.message);
            return false;
          }
        })
      },
      deleteMultipleForm(index){

      }
    }
}
</script>

<style scoped>
</style>