<template>
  <a-modal :centered="true" :visible="visible" :footer="null" @cancel="handleCancel">
    <el-form class="demo-form-inline" label-width="75px">
      <el-form-item label="流程名称:">
        <el-input v-model="formData.name" placeholder="流程名称"></el-input>
      </el-form-item>
      <el-form-item label="流程Bean:">
        <el-input v-model="formData.event_hander_bean"></el-input>
      </el-form-item>
      <!-- <el-form-item label="流程url:">
        <el-input v-model="formData.start_process_url"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="流程Key:">
        <el-input v-model="formData.key"></el-input>
      </el-form-item> -->
      <el-form-item label="流程类别:">
        <el-select size="small" v-model="formData.category_id" placeholder="请选择流程类别">
          <el-option v-for="item in flowCategory" :key="item.id" :label="item.category_name" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
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
import { nw_postAction1 } from '@api/newWorkApi'
import { o_postAction, o_getAction } from '@/api/onApi.js'
export default {
  props: ['data'],
  data() {
    return {
      visible: false,
      formData: {
        name: '',
        event_hander_bean: '',
        start_process_url: '',
        //key: '',
        category_id: '',
        effect_date: '',
        description: '',
        type: 'process',
      },
      loading: false,
      flowCategory: [],
    }
  },
  mounted() {
    this.getAllFlowCategory()
  },
  methods: {
    openModel() {
      this.visible = true
    },
    handleCancel() {
      this.visible = false
    },
    //得到所有的流程类别
    getAllFlowCategory() {
      let params = { pageNo: 1, pageSize: 9999 }
      o_getAction('/cgreport/api/getColumnsAndData/1346455081826521090', params).then((res) => {
        this.flowCategory = res.result.data.records
      })
    },
    onSubmit() {
      // 校验流程类别是否为空
      if (!this.formData.category_id) {
        this.$message.error('请选择流程类别')
        this.loading = false
        return // 阻止提交
      }
      this.loading = true
      this.data.name = this.formData.name
      this.data.event_hander_bean = this.formData.event_hander_bean
      this.data.start_process_url = this.formData.start_process_url
      // this.data.key = this.formData.key
      this.data.category_id = this.formData.category_id
      var date = new Date()
      this.data.effect_date =
        date.getFullYear() +
        '-' +
        (date.getMonth() + 1) +
        '-' +
        date.getDate() +
        ' ' +
        date.getHours() +
        ':' +
        date.getMinutes() +
        ':' +
        date.getSeconds()
      this.data.description = this.formData.description
      this.data.type = this.formData.type
      var content = JSON.stringify(this.data)
      nw_postAction1(`/design/saveFile`, { name: this.data.name, content: content, update: false }).then((res) => {
        if (res.result) {
          this.$message.success('保存流程成功')
          this.loading = false
          this.visible = false
        } else {
          if (res.code == 501) {
            this.$message.error('流程重复！')
          } else {
            this.$message.error('保存流程失败')
          }
          this.loading = false
        }
      })
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