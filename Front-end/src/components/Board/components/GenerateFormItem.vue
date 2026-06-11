<template>
  <div>
    <el-form-item v-if="widget.type != 'divider' && display[widget.model]" :label="widget.name" :prop="widget.model"
      :class="{
        [widget.options.customClass]: widget.options.customClass?true: false,
        'no-label-form-item': widget.options.isLabelWidth && widget.options.labelWidth == 0
      }"
      :label-width="widget.options.isLabelWidth ? widget.options.labelWidth + 'px' : ''"
    >
      <generate-element-item
        :blanks="blanks"
        :is-table="false"
        :widget="widget"
        v-model="dataModel"
        :models.sync="dataModels"
        :remote="remote"
        :edit="edit"
        :remote-option="remoteOption"
        :key="widget.key"
        :rules="rules"
      >

        <template v-slot:[blank.name]="scope" v-for="blank in blanks">
          <slot :name="blank.name" :model="scope.model"></slot>
        </template>
      </generate-element-item>
    </el-form-item>

    <el-divider v-if="widget.type == 'divider' && display[widget.model]"
      :content-position="widget.options.contentPosition"
    >
      {{widget.name}}
    </el-divider>
  </div>

</template>

<script>
import GenerateElementItem from './GenerateElementItem'

export default {
  components: {
    GenerateElementItem
  },
  props: ['widget', 'models', 'rules', 'remote', 'blanks', 'display', 'edit', 'remoteOption'],
  data () {
    return {
      dataModel: this.models[this.widget.model],
      dataModels: this.models
    }
  },
  created() {
    // setTime
  },
  methods: {
  },
  watch: {
    dataModel: {
      deep: true,
      handler (val) {
        this.models[this.widget.model] = val
        this.$emit('update:models', {
          ...this.models,
          [this.widget.model]: val
        })
        this.$emit('input-change', val, this.widget.model)
      }
    },
    models: {
      deep: true,
      handler (val) {
        this.dataModel = val[this.widget.model]
        this.dataModels = val
      }
    },
  }
}
</script>
