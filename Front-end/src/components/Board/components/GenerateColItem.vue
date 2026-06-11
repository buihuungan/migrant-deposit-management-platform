<template>
  <el-row
    :class="{
      [element.options.customClass]: element.options.customClass?true: false
    }"
    :type="element.options.flex ? 'flex' : ''"
    :gutter="element.options.gutter || 0"
    :justify="element.options.justify"
    :align="element.options.align"
    v-if="display[element.model]"
  >

    <el-col v-for="(item, index) in element.columns" :key="index"
      :xs="element.options.responsive ? item.xs || 0 : item.span || 0"
      :sm="element.options.responsive ? item.sm || 0 : item.span || 0"
      :md="element.options.responsive ? item.md || 0 : item.span || 0"
      :lg="element.options.responsive ? item.lg || 0 : item.span || 0"
      :xl="element.options.responsive ? item.xl || 0 : item.span || 0"
    >
      <template v-for="col in item.list">
        <generate-col-item
          v-if="col.type === 'grid'"
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
            <slot :name="blank.name" :model="scope.model" />
          </template>
        </generate-col-item>

        <generate-tab-item
          v-else-if="col.type === 'tabs'"
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
            <slot :name="blank.name" :model="scope.model" />
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
            <slot :name="blank.name" :model="scope.model" />
          </template>
        </generate-form-item>
      </template>
    </el-col>
  </el-row>
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
