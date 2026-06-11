<template>
  <a-modal :centered="true" :visible="dialogVisible" width="70%" title="流程数据信息" :closable="false">
    <div>
      <a-alert message="使用说明" type="warning" description="以下流程信息可以被存储起来，方便下一次流程加载" show-icon>
      </a-alert>
      <br />
      <!--一个高亮显示的插件-->
      <codemirror :value="flowJsonData" :options="options" class="code"></codemirror>

      <el-button type="primary" class="json-copy" :data-clipboard-text="jsonCopyValue"> 复制</el-button>
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

require('codemirror/mode/javascript/javascript.js')

export default {
  props: {
    data: Object,
  },
  data() {
    return {
      jsonCopyValue: '',
      jsonClipboard: null,
      dialogVisible: false,
      flowJsonData: {},
      options: {
        mode: { name: 'javascript', json: true },
        lineNumbers: true,
        readOnly: 'nocursor',
      },
    }
  },
  components: {
    codemirror,
  },
  methods: {
    init() {
      this.dialogVisible = true
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