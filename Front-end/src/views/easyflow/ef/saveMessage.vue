<template>
  <a-modal :centered="true" :visible="visible" :footer="null" @cancel="handleCancel">
    <el-form class="demo-form-inline" label-width="75px">
      <el-form-item label="课程名称:">
        <el-input v-model="formData.ware_name" placeholder="课程名称"></el-input>
      </el-form-item>
      <!-- <el-form-item label="选择租户:">
        <el-select class="ware-select" v-model="formData.tenant_id" placeholder="选择租户">
          <el-option
            v-for="(item, index) in ware.tenant_id"
            :key="index"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="课程领域:">
        <el-select class="ware-select" multiple v-model="formData.ware_domain_name" placeholder="课程领域">
          <el-option
            v-for="(item, index) in ware.edu_ware"
            :key="index"
            :label="item.ware_domain_name"
            :value="item.ware_domain_name"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课程类型:">
        <el-select class="ware-select" multiple v-model="formData.ware_type_name" placeholder="课程类型">
          <el-option
            v-for="(item, index) in ware.edu_ware_type"
            :key="index"
            :label="item.ware_type_name"
            :value="item.ware_type_name"
          ></el-option>
        </el-select>
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
import { USER_INFO, TENANT_ID } from '../../../store/mutation-types'
import vue from 'vue'
export default {
  props: ['data', 'org_code'],
  data() {
    return {
      visible: false,
      formData: { ware_name: '', ware_domain_name: [], ware_type_name: [] },
      ware: { edu_ware: {}, edu_ware_type: {}, tenant_id: {} },
      wareUrl: {
        edu_ware_url: '/online/cgreport/api/getColumnsAndData/1364017324639719425',
        edu_ware_type_url: '/online/cgreport/api/getColumnsAndData/1364017676046897153',
        tenant_id_url: '/sys/tenant/list',
        insertUrl: '/online/cgform/api/form/22f5f24e82444c3db7228f2c2412654d',
      },
      loading: false,
    }
  },
  methods: {
    loadData() {
      this.visible = true
      this.loadWare()
    },
    loadWare() {
      this.getWareData(this.wareUrl.edu_ware_url, 'edu_ware')
      this.getWareData(this.wareUrl.edu_ware_type_url, 'edu_ware_type')
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
      let queryParams = {
        order: 'desc',
        pageNo: 1,
        pageSize: 10,
      }
      getAction(url, queryParams).then((res) => {
        this.ware[params] = res.result.data.records
      })
    },
    onSubmit() {
      this.loading = true
      let insertParamas = {}
      let userInfo = vue.ls.get(USER_INFO)
      console.log('org_code', this.org_code)
      insertParamas.org_code = this.org_code
      insertParamas.teacher_name = userInfo.realname
      insertParamas.teacher_work_no = userInfo.workNo
      insertParamas.teacher_id = userInfo.id
      insertParamas.ware_name = this.formData.ware_name
      insertParamas.ware_content = JSON.stringify(this.data)
      insertParamas.edu_ware_ware_domain = [
        { ware_domain_name: this.formData.ware_domain_name, sys_tenant_id: vue.ls.get(TENANT_ID) },
      ]
      insertParamas.edu_ware_ware_type = [
        { ware_type_name: this.formData.ware_type_name, sys_tenant_id: vue.ls.get(TENANT_ID) },
      ]
      insertParamas.tenant_id = vue.ls.get(TENANT_ID)
      postAction(this.wareUrl.insertUrl, insertParamas).then((res) => {
        this.visible = false
        this.loading = false
        this.$message.success('保存成功')
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