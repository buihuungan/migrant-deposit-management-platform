<template>
  <a-modal :centered="true" :visible="visible" :footer="null" @cancel="handleCancel">
    <el-form  class="demo-form-inline" label-width="75px">
      <el-form-item label="课件名称:">
        <el-input v-model="formData.ware_name" placeholder="课件名称" :disabled="update"></el-input>
      </el-form-item>
      <el-form-item label="课件领域:">
        <el-select class="ware-select" multiple v-model="formData.ware_domain_name" placeholder="课件领域">
          <el-option
            v-for="(item, index) in ware.edu_ware"
            :key="index"
            :label="item.ware_domain_name"
            :value="item.ware_domain_name"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课件类型:">
        <el-select class="ware-select" multiple v-model="formData.ware_type_name" placeholder="课件类型">
          <el-option
            v-for="(item, index) in ware.edu_ware_type"
            :key="index"
            :label="item.ware_type_name"
            :value="item.ware_type_name"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课件行业:">
        <el-select class="ware-select" multiple v-model="formData.ware_industry_name" placeholder="课件行业">
          <el-option
            v-for="(item, index) in ware.edu_ware_industry"
            :key="index"
            :label="item.ware_industry_name"
            :value="item.ware_industry_name"
          ></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="流程名称:">
        <el-input v-model="formData.name" placeholder="流程名称"></el-input>
      </el-form-item>  -->
      
      <!-- <el-form-item label="流程Bean:">
        <el-input v-model="formData.event_hander_bean"></el-input>
      </el-form-item>
      <el-form-item label="流程url:">
        <el-input v-model="formData.start_process_url"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="流程Key:">
        <el-input v-model="formData.key"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="流程类别:">
        <el-select size="small" v-model="formData.category_id" placeholder="请选择流程类别">
          <el-option v-for="item in flowCategory" :key="item.id" :label="item.category_name" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item> -->
      <!-- <el-form-item label="流程日期:">
        <el-input v-model="formData.effect_date"></el-input>
      </el-form-item> -->
      <el-form-item label="流程描述:">
        <el-input v-model="formData.description"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="onSubmit" style="margin-left: 18%">保存</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>  
  </a-modal>
</template>

<script>
import { getAction, postAction } from '@api/manage'
import { ce_postAction1 } from '@api/coursewareEngineApi'
import { USER_INFO, TENANT_ID } from '../../../store/mutation-types'
import vue from 'vue'
export default {
  props: ['data', 'org_code'],
  data() {
    return {
      visible: false,
      formData: { ware_name: '', ware_domain_name: [], ware_type_name: [] ,ware_industry_name: [],description:''},
      ware: { edu_ware: {}, edu_ware_type: {},edu_ware_industry: {}, tenant_id: {} },
      wareUrl: {
        edu_ware_url: '/online/cgreport/api/getColumnsAndData/1364017324639719425',
        edu_ware_type_url: '/online/cgreport/api/getColumnsAndData/1364017676046897153',
        edu_ware_industry_url: 'online/cgform/api/getData/35ff6534bd0e4660a4e9d6ed7d5c7f06',
        tenant_id_url: '/sys/tenant/list',
        insertUrl: '/online/cgform/api/form/22f5f24e82444c3db7228f2c2412654d',
      },
      loading: false,
      update : false,
    }
  },

  created(){
    if( this.data.category_id !== ''){
      console.log('datalength' ,this.data.category_id.length)
      this.update = true
      this.formData.ware_name = this.data.name
      this.formData.ware_domain_name = this.data.domains
      for( var i = 0; i < this.data.types.length ; i++ ){
        if( this.data.types[i] == 'industry'){
          this.formData.ware_type_name = this.data.types.slice(0 , i)
          this.formData.ware_industry_name = this.data.types.slice(i+1)
        }
      }
      this.formData.description = this.data.description
    }
  },

  methods: {
    openModel() {
      this.visible = true
    },
    loadData() {
      console.log("6666")
      this.visible = true
      this.loadWare()
    },
    loadWare() {
      console.log("44444")
      this.getWareData(this.wareUrl.edu_ware_url, 'edu_ware')
      this.getWareData(this.wareUrl.edu_ware_type_url, 'edu_ware_type')
      this.getWareData2(this.wareUrl.edu_ware_industry_url, 'edu_ware_industry')
      this.loadTenantId()
    },
    loadTenantId() {
      let queryParams = {
        column: 'createTime',
        order: 'desc',
        pageNo: 1,
        pageSize: 10,
      }
      getAction(this.wareUrl.tenant_id_url, queryParams).then((res) => {
        this.ware.tenant_id = res.result.records
      })
    },
    getWareData(url, params) {
      console.log("111")
      let queryParams = {
        order: 'desc',
        pageNo: 1,
        pageSize: 20,
      }
      console.log("11133",url, queryParams)
      getAction(url, queryParams).then((res) => {
        console.log("2222")
        this.ware[params] = res.result.data.records
      })
    },
    getWareData2(url, params) {
      let queryParams = {
        column: 'createTime',
        order: 'desc',
        pageNo: 1,
        pageSize: 1000,
        superQueryMatchType: 'and',
      }
      getAction(url, queryParams).then((res) => {
        console.log("2222")
        this.ware[params] = res.result.records
      })
    },
    onSubmit() {
      this.loading = true
      let insertParamas = {}
      // let userInfo = vue.ls.get(USER_INFO)
      // console.log('org_code', this.org_code)
      // insertParamas.org_code = this.org_code
      // insertParamas.teacher_name = userInfo.realname
      // insertParamas.teacher_work_no = userInfo.workNo
      // insertParamas.teacher_id = userInfo.id
      insertParamas.name = this.formData.ware_name
      this.data.name = insertParamas.name
      insertParamas.content = JSON.stringify(this.data)
      insertParamas.edu_ware_ware_domain = [
        { ware_domain_name: this.formData.ware_domain_name, sys_tenant_id: vue.ls.get(TENANT_ID) },
      ]
      insertParamas.edu_ware_ware_type = [
        { ware_type_name: this.formData.ware_type_name, sys_tenant_id: vue.ls.get(TENANT_ID) },
      ]
      insertParamas.edu_ware_ware_industry = [
        { ware_industry_name: this.formData.ware_industry_name, sys_tenant_id: vue.ls.get(TENANT_ID) },
      ]
      insertParamas.description = this.formData.description
      insertParamas.tenant_id = vue.ls.get(TENANT_ID)
      insertParamas.update = this.update
      console.log("insertParamas",insertParamas)
      
      ce_postAction1(`/design/saveFile`, insertParamas).then((res) => {
        if (res.result) {
          if( this.update){
            this.$message.success('编辑课件保存成功')
            this.loading = false
            this.visible = false
            this.$emit("closeEasyflow")
          }else{
            this.$message.success('保存流程成功')
            this.loading = false
            this.visible = false
            this.$emit("closeEasyflow")
          }
        } else {
          this.$message.error(res.message)
          this.loading = false
        }
      })
    },
    handleCancel() {
      this.visible = false
    },
  },
}
</script>

<style scoped>
.demo-form-inline {
  margin: 20px;
}
.ware-select {
  width: 100%;
}
</style>