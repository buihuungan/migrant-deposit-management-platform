<template>
  <fm-making-form1 ref="makingForm1" clearable upload preview generate-code generate-json
    :custom-fields="customFields"
  >
    <template slot="action">
      <!-- 自定义操作区域插槽 -->
      <el-button type="text" icon="el-icon-upload" @click="handleSave">保存</el-button>
    </template>
  </fm-making-form1>
</template>

<script>
export default {
  data() {
    return {
      customFields: [
        {
          name: '自定义组件',
          el: 'custom-width-height',
          options: {
            defaultValue: {},
            customClass: '',
            labelWidth: 100,
            isLabelWidth: false,
            hidden: false,
            dataBind: true,
            required: false,
            dataType: '',
            pattern: '',
          }
        }
      ]
    }
  },
  mounted () {
  },
  methods: {
    handleSave () {
      this.$router.push('/test')
    }
  }
}
</script>
