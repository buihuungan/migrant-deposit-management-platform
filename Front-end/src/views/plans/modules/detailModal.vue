<template>
  <div id="formContent">
    <div class="title">
      <h1>{{ '详情' }}</h1>
    </div>
    <div class="formbody" v-cloak>
      <generate-form style="margin: 0 auto" insite="true" :data="formJsonEnd" ref="generateForm">
        <template v-slot:blank="scope">
          Width
          <el-input v-model="scope.model.blank.width" style="width: 100px" />Height：
          <el-input v-model="scope.model.blank.height" style="width: 100px" />
        </template>
      </generate-form>
    </div>
    <div class="submitBtn">
      <a-button type="dashed" @click="close()">返回</a-button>
    </div>
  </div>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_getAction } from '@/api/tempApi'
import { Message } from 'element-ui'

export default {
  name: 'AnnTask',
  components: { GenerateForm, AntdGenerateForm },
  // mixins:[WebsocketMixin],
  props: ['record', 'detailFormId'],
  data() {
    return {
      nowData: '',
      formJson: {},
      formJsonEnd: {},
      dialogVisible: false,
      projectId: '',
      operator_id: '',
      userInfo: {},
      options: [],
      beSelectedInfo: '',
      username: '',
      username1: '',
      uid: '',
      saveIconLoading: false,
      handleIconLoading: false,
      handleBackIconLoading: false,
      url: {
        search: '/online/cgform/api/getData/b6cd9cf08487444885d5fd2c0744d378',
        delete: '/online/cgform/api/form/b6cd9cf08487444885d5fd2c0744d378',
        add: '/online/cgform/api/form/b6cd9cf08487444885d5fd2c0744d378'
      }
    }
  },
  created() {
    this.getForm()
  },
  watch: {},
  mounted() {},
  methods: {
    close() {
      this.$emit('close-detailModal')
    },
    getForm() {
      t_getAction('/admin/desform/' + this.detailFormId + '/getConent')
        .then(res => {
          this.formJson = JSON.parse(res.result)
          console.log('this.formJson', this.formJson)
          console.log("this.record",this.record);
          this.formJson.list[0].columns[0].list[0].options.defaultValue = this.record.prodId
          this.formJson.list[0].columns[0].list[0].options.disabled = true
          this.formJson.list[0].columns[0].list[1].options.defaultValue = this.record.schedulingDate
          this.formJson.list[0].columns[0].list[1].options.disabled = true
          this.formJson.list[0].columns[0].list[2].options.defaultValue = this.record.productionLineId
          this.formJson.list[0].columns[0].list[2].options.disabled = true
          this.formJson.list[0].columns[1].list[0].options.defaultValue = this.record.prodName
          this.formJson.list[0].columns[1].list[0].options.disabled = true
          this.formJson.list[0].columns[1].list[1].options.defaultValue = this.record.amount
          this.formJson.list[0].columns[1].list[1].options.disabled = true
          this.formJsonEnd = this.formJson
          console.log('this.formJsonEnd', this.formJsonEnd)
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>
<style scoped>
#formContent {
  padding: 5px;
  background-color: #fff;
  margin: 0 auto;
}
.formTable {
  width: 90%;
  border: 1px solid #000000;
  margin: 0 auto;
}
.title {
  width: 100%;
  height: 50px;
  text-align: center;
}
.formbody {
  margin-top: 20px;
}
.submitBtn {
  margin-top: 20px;
}
.submitBtn button {
  margin: 20px auto;
  left: 46%;
}
</style>
