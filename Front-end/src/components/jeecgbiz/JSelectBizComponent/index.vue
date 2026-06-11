<template>
  <a-row class="j-select-biz-component-box" type="flex" :gutter="8">
    <a-col class="left" :class="{'full': !buttons}">
      <slot name="left">
        <a-select
          mode="multiple"
          :placeholder="placeholder"
          v-model="selectValue"
          :options="selectOptions"
          allowClear
          :disabled="disabled"
          :open="selectOpen"
          style="width: 100%;"
          @dropdownVisibleChange="handleDropdownVisibleChange"
          @click.native="visible=(buttons || disabled ?visible:true)"
        />
      </slot>
    </a-col>

    <a-col v-if="buttons" class="right">
      <a-button type="primary" icon="search" :block="true" :disabled="disabled" @click="visible=true">{{selectButtonText}}</a-button>
    </a-col>

    <j-select-biz-component-modal
      v-model="selectValue"
      :visible.sync="visible"
      v-bind="modalProps"
      :isonlineUrl="isonlineUrl"
      :searchobj="searchobj"
      :ischildtable="ischildtable"
      @options="handleOptions"
    />
  </a-row>
</template>

<script>
  import JSelectBizComponentModal from './JSelectBizComponentModal'

  export default {
    name: 'JSelectBizComponent',
    components: { JSelectBizComponentModal },
    props: {
      ischildtable:{
        type:String,
        default:'',
      },
      searchobj:{
        type:String,
        default:''
      },
      ctype:{
        type: String,
        default: ''
      },
      //是否需要选中的全部的对象
      needobj:{
        type: Boolean,
        default :false
      },
      //是否是online表单中的配置接口
      isonlineUrl:{
        type: Boolean,
        default: false
      },
      value: {
        type: String,
        default: ''
      },
      /** 是否返回 id，默认 false，返回 code */
      returnId: {
        type: Boolean,
        default: false
      },
      placeholder: {
        type: String,
        default: '请选择'
      },
      disabled: {
        type: Boolean,
        default: false
      },
      // 是否支持多选，默认 true
      multiple: {
        type: Boolean,
        default: true
      },
      sys_org_code:{
        type: String,
        default: null
      },
      // 是否显示按钮，默认 true
      buttons: {
        type: Boolean,
        default: true
      },
      // 显示的 Key
      displayKey: {
        type: String,
        default: null
      },
      // 返回的 key
      returnKeys: {
        type: Array,
        default: () => ['id', 'id']
      },
      // 选择按钮文字
      selectButtonText: {
        type: String,
        default: ''
      },

    },
    data() {
      return {
        selectValue: [],
        selectOptions: [],
        dataSourceMap: {},
        visible: false,
        selectOpen: false,
      }
    },
    computed: {
      valueKey() {
        return this.returnId ? this.returnKeys[0] : this.returnKeys[1]
      },
      modalProps() {
        return Object.assign({
          valueKey: this.valueKey,
          multiple: this.multiple,
          sys_org_code:this.sys_org_code,
          returnKeys: this.returnKeys,
          displayKey: this.displayKey || this.valueKey
        }, this.$attrs)
      },
    },
    watch: {
      value: {
        immediate: true,
        handler(val) {
          if (val) {
            this.selectValue = val.split(',')
          } else {
            this.selectValue = []
          }
        }
      },
      selectValue: {
        deep: true,
        handler(val) {
          let rows = val.map(key => this.dataSourceMap[key])
          let data = val.join(',')
          if (data !== this.value) {
            this.$emit('select', val)
            this.$emit('input', data)
            this.$emit('change', data)
            //传递所有选中对象参数
            this.$emit('getobject',{obj:rows,ctype:this.ctype})
          }
        }
      },

    },
    methods: {
      handleOptions(options, dataSourceMap) {
        this.selectOptions = options
        this.dataSourceMap = dataSourceMap
      },
      handleDropdownVisibleChange() {
        // 解决antdv自己的bug —— open 设置为 false 了，点击后还是添加了 open 样式，导致点击事件失效
        this.selectOpen = true
        this.$nextTick(() => {
          this.selectOpen = false
        })
      },
    }
  }
</script>

<style lang="less" scoped>
  .j-select-biz-component-box {

    @width: 42px;

    .left {
      width: calc(100% - @width - 8px);
    }

    .right {
      width: @width;
    }

    .full {
      width: 100%;
    }

    /deep/ .ant-select-search__field {
      display: none !important;
    }
  }
</style>