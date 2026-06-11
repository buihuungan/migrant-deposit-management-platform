<template>
  <a-modal :centered="true" :visible="dialogVisible" width="70%" title="流程数据信息" :closable="false">
    <div>
      <a-alert message="使用说明" type="warning" description="以下流程信息可以被存储起来，方便下一次流程加载" show-icon>
      </a-alert>
      <br />
      <!--一个高亮显示的插件-->
      <codemirror :value="flowJsonData" :options="options" class="code" @input="updateFlowJsonData"></codemirror>

      <el-button type="primary" class="json-copy" :data-clipboard-text="jsonCopyValue"> 复制</el-button>
      <el-button type="primary" @click="handleSubmit"> 生成流程</el-button>
    </div>

    <template slot="footer">
      <a-button key="back" @click="handleCancel"> 关闭 </a-button>
    </template>
  </a-modal>
</template>

<script>
import Clipboard from 'clipboard'
import 'codemirror/lib/codemirror.css'
import { codemirror } from 'vue-codemirror'

import 'codemirror/addon/lint/lint.css'
import 'codemirror/addon/lint/lint.js'
import 'codemirror/addon/lint/json-lint.js'
require('codemirror/mode/javascript/javascript.js')

export default {
  props: {
    data: Object,
    dataReload: Function,
  },
  data() {
    return {
      jsonFormat:true,
      jsonCopyValue: '',
      jsonClipboard: null,
      dialogVisible: false,
      flowJsonData: '',
      options: {
        mode: { name: 'javascript', json: true },
        lineNumbers: true,
        lint: true,
      },
    }
  },
  components: {
    codemirror,
  },
  methods: {
    handleSubmit() {
      console.log('通过json得到流程图');
      if(!this.jsonFormat){
        this.$message.error('JSON 格式错误')
        return
      }else{
        this.dataReload(this.flowJsonData);
      }
    },

    updateFlowJsonData(newValue) {
      this.flowJsonData = newValue // 更新 flowJsonData
      this.jsonCopyValue = newValue // 同步更新复制内容
      this.validateJson(newValue) // 验证 JSON 格式
    },
    validateJson(data) {
      try {
        JSON.parse(data) // 尝试解析 JSON
        this.jsonFormat = true
      } catch (error) {
        this.$message.error('JSON 格式错误')
        this.jsonFormat = false
      }
    },
    init() {
      this.dialogVisible = true
      this.jsonFormat = true
      this.flowJsonData = JSON.stringify(this.data, null, 4).toString()
      this.$nextTick(() => {
        if (!this.jsonClipboard) {
          this.jsonClipboard = new Clipboard('.json-copy')
          this.jsonClipboard.on('success', (e) => {
            this.$message.success('复制成功')
          })
        }
        this.jsonCopyValue = this.flowJsonData
      })
    },
    handleCancel() {
      this.dialogVisible = false
    },
  },
}
</script>
<style lang="scss" scoped>
.json-copy {
  margin: 10px 0 0 45%;
}
</style>