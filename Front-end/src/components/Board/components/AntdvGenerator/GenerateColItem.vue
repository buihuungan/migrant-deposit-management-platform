<template>
  <a-row
    :class="{
      [element.options.customClass]: element.options.customClass?true: false
    }"
    type="flex"
    :gutter="element.options.gutter || 0"
    :justify="element.options.justify"
    :align="element.options.align"
    v-if="display[element.model]"
  >
    <a-col v-for="(item, index) in element.columns" :key="index"
      :span="item.span || 0" 
    >
      <template v-for="col in item.list">
        <generate-col-item
          v-if="col.type == 'grid'"
          :key="col.key"
          :model.sync="dataModels"
          :rules="rules"
          :element="col"
          :remote="remote"
          :blanks="blanks"
          :display="display"
          @input-change="onInputChange"
          :edit="edit"
          :remote-option="remoteOption"
        >
          <template v-slot:[blank.name]="scope" v-for="blank in blanks">
            <slot :name="blank.name" :model="scope.model"></slot>
          </template>
        </generate-col-item>

        <generate-tab-item
          v-else-if="col.type == 'tabs'"
          :key="col.key"
          :model.sync="dataModels"
          :rules="rules"
          :element="col"
          :remote="remote"
          :blanks="blanks"
          :display="display"
          @input-change="onInputChange"
          :edit="edit"
          :remote-option="remoteOption"
        >
          <template v-slot:[blank.name]="scope" v-for="blank in blanks">
            <slot :name="blank.name" :model="scope.model"></slot>
          </template>
        </generate-tab-item>

        <generate-form-item
          v-else
          :key="col.key"
          :models.sync="dataModels"
          :rules="rules"
          :widget="col"
          :remote="remote"
          :blanks="blanks"
          :display="display"
          @input-change="onInputChange"
          :edit="edit"
          :remote-option="remoteOption"
        >
          <template v-slot:[blank.name]="scope" v-for="blank in blanks">
            <slot :name="blank.name" :model="scope.model"></slot>
          </template>
        </generate-form-item>
      </template>
    </a-col>
  </a-row>
</template>

<script>
import GenerateFormItem from './GenerateFormItem'
import GenerateTabItem from './GenerateTabItem'

export default {
  name: 'generate-col-item',
  components: {
    GenerateFormItem,
    GenerateTabItem
  },
  props: ['element', 'model', 'rules', 'remote', 'blanks', 'display', 'edit', 'remoteOption'],
  data () {
    return {
      dataModels: this.model
    }
  },
  methods: {
    onInputChange (value, field) {
      this.$emit('input-change', value, field)
    }
  },
  watch: {
    model: {
      deep: true,
      handler (val) {
        this.dataModels = this.model
      }
    },
    dataModels: {
      deep: true,
      handler (val) {
        this.$emit('update:model', val)
      }
    }
  }
}
</script>
