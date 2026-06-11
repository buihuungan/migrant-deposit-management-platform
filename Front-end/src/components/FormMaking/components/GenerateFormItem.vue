<template>
  <div>
    <el-form-item
      v-if="widget.type != 'divider' && display[widget.model]"
      :label="widget.name"
      :prop="widget.model"
      :class="{
        [widget.options.customClass]: widget.options.customClass ? true : false,
        'no-label-form-item': widget.options.isLabelWidth && widget.options.labelWidth == 0,
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
        @formOk="formOk"
      >
        <template v-slot:[blank.name]="scope" v-for="blank in blanks">
          <slot :name="blank.name" :model="scope.model"></slot>
        </template>
      </generate-element-item>
    </el-form-item>

    <el-divider
      v-if="widget.type == 'divider' && display[widget.model]"
      :content-position="widget.options.contentPosition"
    >
      {{ widget.name }}
    </el-divider>
  </div>
</template>

<script>
import GenerateElementItem from './GenerateElementItem'

export default {
  components: {
    GenerateElementItem,
  },
  props: ['widget', 'models', 'rules', 'remote', 'blanks', 'display', 'edit', 'remoteOption','listformitem'],
  data() {
    return {
      dataModel:this.models[this.widget.model],
      dataModels: this.models,
    }
  },
  created() {
    // setTime
  },
  methods: {
    formOk(data, index) {
      this.$emit('gfigci-ok', data)
      
    },
  },
  watch: {
    //增加一个监听来控制值的转变
    widget:function(val){
      let models111;
      models111=JSON.parse(JSON.stringify(this.models))
      this.dataModel=models111[val.model]
    },
    dataModel: {
      deep: true,
      handler(newVal) {
        this.models[this.widget.model] = newVal
        this.dataModels[this.widget.model] = newVal      
        this.$emit('update:models', {
          ...this.models,
          [this.widget.model]: newVal,
        })
        this.$emit('input-change', newVal, this.widget.model)
      },
    },
    models:function(val){
      this.dataModel=val[this.widget.model];
      this.dataModels = val
    },
  },
}
</script>
