<template>
  <a-modal
    title="二维码"
    :visible="visible"
    okText="确定"
    cancelText="取消"
    width="16%"
    :destroyOnClose="true"
    @ok="handleOk"
    @cancel="handleCancel"
    :footer="null"
  >
    <vue-qr :text="codeData" :margin="0"  :size="200"></vue-qr>
  </a-modal>
</template>

<script>
import vueQr from 'vue-qr'
export default {
  components: {
    vueQr
  },
  data() {
    return {
      downloadData: {
      },
      visible: false,
      codeData: ''
    }
  },
  methods: {
    openModal(record) {
      this.visible = true
      console.log('ttttt',record)
      this.codeData = record.codeData
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false
    }
  }
}
</script>

<style>

</style>