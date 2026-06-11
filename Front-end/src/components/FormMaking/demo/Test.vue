<template>
  <div>
    <fm-generate-form 
      :data="jsonData" 
      :remote="remoteFuncs" 
      :value="editData" 
      :remote-option="dynamicData"
      ref="generateForm"
    >
    </fm-generate-form>
    <el-button type="primary" @click="handleSubmit">Submit</el-button>
    <el-button type="primary" @click="handleSetData">SetData</el-button>
  </div>
</template>

<script>
export default {
  data () {
    return {
      jsonData: {"list":[{"type":"table","icon":"icon-table","options":{"defaultValue":[],"customClass":"","labelWidth":100,"isLabelWidth":false,"hidden":false,"dataBind":true,"disabled":false,"remoteFunc":"func_1589851529070","remoteOption":"option_1589851529070"},"tableColumns":[{"type":"input","icon":"icon-input","options":{"width":"200px","defaultValue":"","required":false,"requiredMessage":"","dataType":"","dataTypeCheck":false,"dataTypeMessage":"","pattern":"","patternCheck":false,"patternMessage":"","placeholder":"","customClass":"","disabled":false,"labelWidth":100,"isLabelWidth":false,"hidden":false,"dataBind":true,"showPassword":false,"remoteFunc":"func_1589851537880","remoteOption":"option_1589851537880","tableColumn":true},"name":"单行文本","novalid":{},"key":"1589851537880","model":"name","rules":[]}],"name":"子表单","key":"1589851529070","model":"users","rules":[]}],"config":{"labelWidth":100,"labelPosition":"right","size":"small","customClass":"","ui":"element","layout":"horizontal","labelCol":3,"width":"100%"}},
      editData: {},
      remoteFuncs: {
        
      },
      dynamicData: {
        
      }
    }
  },
  methods: {
    handleSubmit () {
      this.$refs.generateForm.getData().then(data => {
        // Data verification succeeded
        alert(JSON.stringify(data))
      }).catch(e => {
        // Data verification failed
      })
    },

    handleSetData () {
      this.$refs.generateForm.setData({
        users: [
          {name: '123'}
        ]
      })
    }
  }
}
</script>