<template>
  <vue-editor
    v-model="dataModel"
    :style="sty"
    :editor-toolbar="toolbar"
    class="fm-editor"
    :disabled="disabled"
  >
  </vue-editor>
</template>

<script>
import { EventBus } from '../../util/event-bus.js'

export default {
  props: {
    value: {
      type: String,
      default: ''
    },
    sty: {
      type: Object,
      default: () => ({})
    },
    toolbar: {
      type: Array,
      default: () => []
    },
    disabled: {
      type: Boolean,
      default: false
    },
    ui: {
      type: String,
      default: 'element'
    }
  },
  data () {
    return {
      dataModel: this.value
    }
  },
  watch: {
    dataModel (val) {

      if (this.ui == 'antd') {
        EventBus.$emit('on-field-change', this.$attrs.id, val)
      } else {
        this.$emit('input', val)
      }
    },
    value (val) {
      this.dataModel = val
    }
  }
}
</script>
