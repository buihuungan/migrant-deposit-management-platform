<template>
  <a-form-item
    :class="{
      [widget.options.customClass]: widget.options.customClass ? true : false,
    }"
    :key="widget.key"
  >
    <span>
      <template v-if="widget.type == 'blank'">
        <div :style="{ width: isTable ? '100%' : widget.options.width }">
          <slot :name="widget.model" :model="dataModels"></slot>
        </div>
      </template>

      <template v-if="widget.type == 'input'">
        <a-input
          v-if="
            widget.options.dataType == 'number' ||
            widget.options.dataType == 'integer' ||
            widget.options.dataType == 'float'
          "
          :type="widget.options.dataType"
          :disabled="!edit || widget.options.disabled"
          :placeholder="widget.options.placeholder"
          :show-password="widget.options.showPassword"
          :style="{ width: isTable ? '100%' : widget.options.width }"
          v-model="dataModel"
        ></a-input>
        <a-input
          v-else
          :type="widget.options.dataType"
          :disabled="!edit || widget.options.disabled"
          :placeholder="widget.options.placeholder"
          :show-password="widget.options.showPassword"
          :style="{ width: isTable ? '100%' : widget.options.width }"
          v-model="dataModel"
        ></a-input>
      </template>

      <template v-if="widget.type == 'textarea'">
        <a-textarea
          v-model="dataModel"
          :disabled="!edit || widget.options.disabled"
          :placeholder="widget.options.placeholder"
          :style="{ width: isTable ? '100%' : widget.options.width }"
        >
        </a-textarea>
      </template>

      <template v-if="widget.type == 'number'">
        <a-input-number
          v-model="dataModel"
          :style="{ width: isTable ? '100%' : widget.options.width }"
          :step="widget.options.step"
          controls-position="right"
          :disabled="!edit || widget.options.disabled"
          :min="widget.options.min"
          :max="widget.options.max"
        >
        </a-input-number>
      </template>

      <template v-if="widget.type == 'radio'">
        <a-radio-group
          :style="{ width: isTable ? '100%' : widget.options.width }"
          :disabled="!edit || widget.options.disabled"
          v-model="dataModel"
        >
          <a-radio
            style="line-height: 30px"
            :style="{ display: widget.options.inline ? 'inline-block' : 'block' }"
            :value="item.value"
            v-for="(item, index) in widget.options.remote ? remoteOptions : widget.options.options"
            :key="index"
          >
            <template v-if="widget.options.remote">{{ item.label }}</template>
            <template v-else>{{ widget.options.showLabel ? item.label : item.value }}</template>
          </a-radio>
        </a-radio-group>
      </template>

      <template v-if="widget.type == 'checkbox'">
        <a-checkbox-group
          v-model="dataModel"
          :style="{ width: isTable ? '100%' : widget.options.width }"
          :disabled="!edit || widget.options.disabled"
        >
          <a-checkbox
            style="line-height: 30px"
            :style="{ display: widget.options.inline ? 'inline-block' : 'block' }"
            :value="item.value"
            v-for="(item, index) in widget.options.remote ? remoteOptions : widget.options.options"
            :key="index"
          >
            <template v-if="widget.options.remote">{{ item.label }}</template>
            <template v-else>{{ widget.options.showLabel ? item.label : item.value }}</template>
          </a-checkbox>
        </a-checkbox-group>
      </template>

      <template v-if="widget.type == 'time'">
        <a-time-picker
          v-model="dataModel"
          :is-range="widget.options.isRange"
          :placeholder="widget.options.placeholder"
          :start-placeholder="widget.options.startPlaceholder"
          :end-placeholder="widget.options.endPlaceholder"
          :readonly="widget.options.readonly"
          :disabled="!edit || widget.options.disabled"
          :editable="widget.options.editable"
          :clearable="widget.options.clearable"
          :arrowControl="widget.options.arrowControl"
          :value-format="widget.options.format"
          :format="widget.options.format"
          :style="{ width: isTable ? '100%' : widget.options.width }"
        >
        </a-time-picker>
      </template>

      <template v-if="widget.type == 'date'">
        <a-date-picker
          v-model="dataModel"
          :type="widget.options.type"
          :mode="widget.options.type"
          :placeholder="widget.options.placeholder"
          :start-placeholder="widget.options.startPlaceholder"
          :end-placeholder="widget.options.endPlaceholder"
          :readonly="widget.options.readonly"
          :disabled="!edit || widget.options.disabled"
          :editable="widget.options.editable"
          :clearable="widget.options.clearable"
          :value-format="widget.options.timestamp ? 'timestamp' : widget.options.format"
          :format="widget.options.format"
          :style="{ width: isTable ? '100%' : widget.options.width }"
        >
        </a-date-picker>
      </template>

      <template v-if="widget.type == 'rate'">
        <a-rate
          v-model="dataModel"
          :count="widget.options.max"
          :disabled="!edit || widget.options.disabled"
          :allow-half="widget.options.allowHalf"
          :show-score="widget.options.showScore"
        ></a-rate>
      </template>

      <template v-if="widget.type == 'color'">
        <div :style="{ width: isTable ? '100%' : widget.options.width, color: '#999' }">Not currently supported.</div>
      </template>

      <template v-if="widget.type == 'select'">
        <a-select
          v-model="dataModel"
          :disabled="!edit || widget.options.disabled"
          :mode="widget.options.multiple ? 'multiple' : 'default'"
          :allow-clear="widget.options.clearable"
          :placeholder="widget.options.placeholder"
          :style="{ width: isTable ? '100%' : widget.options.width }"
          :show-search="widget.options.filterable"
        >
          <a-select-option
            v-for="item in widget.options.remote ? remoteOptions : widget.options.options"
            :key="item.value"
            :value="item.value"
          >
            {{ widget.options.showLabel || widget.options.remote ? item.label : item.value }}
          </a-select-option>
        </a-select>
      </template>

      <template v-if="widget.type == 'switch'">
        <a-switch v-model="dataModel" :default-checked="dataModel" :disabled="!edit || widget.options.disabled">
        </a-switch>
      </template>

      <template v-if="widget.type == 'slider'">
        <a-slider
          v-model="dataModel"
          :min="widget.options.min"
          :max="widget.options.max"
          :disabled="!edit || widget.options.disabled"
          :step="widget.options.step"
          :show-input="widget.options.showInput"
          :range="widget.options.range"
          :style="{ width: isTable ? '100%' : widget.options.width }"
        ></a-slider>
      </template>

      <template v-if="widget.type == 'imgupload'">
        <fm-upload
          v-model="dataModel"
          :disabled="!edit || widget.options.disabled"
          :readonly="widget.options.readonly"
          :style="{ width: isTable ? '100%' : widget.options.width }"
          :width="widget.options.size.width"
          :height="widget.options.size.height"
          :token="widget.options.token"
          :domain="widget.options.domain"
          :multiple="widget.options.multiple"
          :limit="widget.options.limit"
          :is-qiniu="widget.options.isQiniu"
          :is-delete="widget.options.isDelete"
          :min="widget.options.min"
          :is-edit="widget.options.isEdit"
          :action="widget.options.action"
          ui="antd"
        >
        </fm-upload>
      </template>

      <template v-if="widget.type == 'editor'">
        <fm-editor
          v-model="dataModel"
          :sty="{
            width: isTable ? '100%' : widget.options.width,
            cursor: !edit || widget.options.disabled ? 'no-drop' : '',
            backgroundColor: !edit || widget.options.disabled ? '#F5F7FA' : '',
          }"
          :toolbar="widget.options.customToolbar"
          :disabled="!edit || widget.options.disabled"
          ui="antd"
        >
        </fm-editor>
      </template>

      <template v-if="widget.type == 'cascader'">
        <a-cascader
          v-model="dataModel"
          :disabled="!edit || widget.options.disabled"
          :allow-clear="widget.options.clearable"
          :placeholder="widget.options.placeholder"
          :style="{ width: isTable ? '100%' : widget.options.width }"
          :options="remoteOptions"
        >
        </a-cascader>
      </template>

      <template v-if="widget.type == 'pcdcascader'">
        <a-cascader
          v-model="dataModel"
          :disabled="!edit || widget.options.disabled"
          :allow-clear="widget.options.clearable"
          :placeholder="widget.options.placeholder"
          :style="{ width: isTable ? '100%' : widget.options.width }"
          :options="pcdremoteOptions"
        >
        </a-cascader>
      </template>

      <template v-if="widget.type == 'text'">
        <span>{{ dataModel }}</span>
      </template>

      <template v-if="widget.type == 'html'">
        <span v-html="dataModel"></span>
      </template>

      <template v-if="widget.type == 'fileupload'">
        <fm-file-upload
          v-model="dataModel"
          :action="widget.options.action"
          :width="isTable ? '100%' : widget.options.width"
          :disabled="!edit || widget.options.disabled"
          :limit="widget.options.limit"
          :tip="widget.options.tip"
          ui="antd"
        ></fm-file-upload>
      </template>

      <template v-if="widget.type === 'baselinechart'">
        <!-- eachart 基础折线图 测试 -->
        <el-card
          v-model="dataModel"
          :body-style="widget.bodyStyle"
          :style="{ width: widget.options.style.width, height: widget.options.style.height }"
          :disabled="!edit || widget.options.disabled"
          :shadow="widget.shadow"
        >
          <div ref="baselinechart" :style="widget.options.style"></div>
        </el-card>
      </template>

      <template v-if="widget.type === 'basecolumnchart'">
        <!-- eachart 基础柱状图 测试 -->
        <el-card
          v-model="dataModel"
          :body-style="widget.bodyStyle"
          :style="{ width: widget.options.style.width, height: widget.options.style.height }"
          :disabled="!edit || widget.options.disabled"
          :shadow="widget.shadow"
        >
          <div ref="basecolumnchart" :style="widget.options.style"></div>
        </el-card>
      </template>

      <template v-if="widget.type === 'table_test'">
        <!-- 表格 测试 -->
        <el-card
          v-model="dataModel"
          :body-style="widget.bodyStyle"
          :style="{ width: widget.options.style.width, height: widget.options.style.height }"
          :disabled="!edit || widget.options.disabled"
          :shadow="widget.shadow"
        >
          <div :style="widget.options.style">
            <el-table :data="tableData" style="width: 100%">
              <el-table-column prop="date" label="日期" width="180"> </el-table-column>
              <el-table-column prop="name" label="姓名" width="180"> </el-table-column>
              <el-table-column prop="address" label="地址"> </el-table-column>
            </el-table>
          </div>
        </el-card>
      </template>

      <template v-if="widget.type === 'basepiechart'">
        <!-- eachart 基础饼状图 测试 -->
        <el-card
          v-model="dataModel"
          :body-style="widget.bodyStyle"
          :style="{ width: widget.options.style.width, height: widget.options.style.height }"
          :disabled="!edit || widget.options.disabled"
          :shadow="widget.shadow"
        >
          <div ref="basepiechart" :style="widget.options.style"></div>
        </el-card>
      </template>

      <template v-if="widget.type === 'seriescolumnchart'">
        <!-- eachart 多系列柱状图 测试 -->
        <el-card
          v-model="dataModel"
          :body-style="widget.bodyStyle"
          :style="{ width: widget.options.style.width, height: widget.options.style.height }"
          :disabled="!edit || widget.options.disabled"
          :shadow="widget.shadow"
        >
          <div ref="seriescolumnchart" :style="widget.options.style"></div>
        </el-card>
      </template>

      <template v-if="widget.type === 'serieslinechart'">
        <!-- eachart 多系列折线图 测试 -->
        <el-card
          v-model="dataModel"
          :body-style="widget.bodyStyle"
          :style="{ width: widget.options.style.width, height: widget.options.style.height }"
          :disabled="!edit || widget.options.disabled"
          :shadow="widget.shadow"
        >
          <div ref="serieslinechart" :style="widget.options.style"></div>
        </el-card>
      </template>
    </span>
  </a-form-item>
</template>

<script>
import FmFileUpload from '../Upload/file'
import FmEditor from '../Editor'
import FmUpload from '../Upload'
import {
  provinceAndCityData,
  regionData,
  provinceAndCityDataPlus,
  regionDataPlus,
  CodeToText,
  TextToCode,
} from 'element-china-area-data'

export default {
  components: {
    FmFileUpload,
    FmEditor,
    FmUpload,
  },
  props: ['widget', 'models', 'rules', 'remote', 'blanks', 'edit', 'remoteOption'],
  data() {
    return {
      tableData: [
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',
        },
        {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄',
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄',
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄',
        },
      ],
      dataModel: this.models[this.widget.model],
      dataModels: this.models,
      remoteOptions: [],
      pcdremoteOptions: regionData,
      isTable: true,
    }
  },
  mounted() {
    if (this.widget.type === 'baselinechart') {
      console.log('判断当前加载组件为基础折线图66')
      this.baselinechart()
    } else if (this.widget.type === 'basecolumnchart') {
      console.log('判断当前加载组件为基础柱状图66')
      this.basecolumnchart()
    } else if (this.widget.type === 'basepiechart66') {
      console.log('判断当前加载组件为基础饼状图')
      this.basepiechart()
    } else if (this.widget.type === 'seriescolumnchart66') {
      console.log('判断当前加载组件为多系列柱状图')
      this.seriescolumnchart()
    } else if (this.widget.type === 'serieslinechart66') {
      console.log('判断当前加载组件为多系列折线图')
      this.serieslinechart()
    }
  },
  created() {
    this.remoteOptions = []

    if (
      this.widget.options.remote &&
      (Object.keys(this.widget.options).indexOf('remoteType') >= 0 ? this.widget.options.remoteType == 'func' : true) &&
      this.remote[this.widget.options.remoteFunc]
    ) {
      this.remote[this.widget.options.remoteFunc]((data) => {
        this.loadOptions(data)
      })
    }

    if (
      this.widget.options.remote &&
      this.widget.options.remoteType == 'option' &&
      this.remoteOption[this.widget.options.remoteOption]
    ) {
      this.loadOptions(this.remoteOption[this.widget.options.remoteOption])
    }

    if (this.widget.type === 'imgupload' && this.widget.options.isQiniu) {
      this.remote[this.widget.options.tokenFunc]((data) => {
        this.widget.options.token = data
      })
    }
  },
  methods: {
    baselinechart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.baselinechart)
      baselinechar.setOption(this.widget.options.defaultValue)
    },
    basecolumnchart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.basecolumnchart)
      baselinechar.setOption(this.widget.options.defaultValue)
    },
    basepiechart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.basepiechart)
      baselinechar.setOption(this.widget.options.defaultValue)
    },
    seriescolumnchart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.seriescolumnchart)
      baselinechar.setOption(this.widget.options.defaultValue)
    },
    serieslinechart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.serieslinechart)
      baselinechar.setOption(this.widget.options.defaultValue)
    },

    loadOptions(data) {
      this.remoteOptions = data.map((item) => {
        if (
          this.widget.options.props.children &&
          this.widget.options.props.children.length &&
          Object.keys(item).includes(this.widget.options.props.children)
        ) {
          return {
            value: item[this.widget.options.props.value],
            label: item[this.widget.options.props.label],
            children: this.processRemoteProps(item[this.widget.options.props.children], this.widget.options.props),
          }
        } else {
          return {
            value: item[this.widget.options.props.value],
            label: item[this.widget.options.props.label],
          }
        }
      })
    },
    processRemoteProps(children, props) {
      if (children && children.length) {
        return children.map((item) => {
          if (this.processRemoteProps(item[props.children], props).length) {
            return {
              value: item[props.value],
              label: item[props.label],
              children: this.processRemoteProps(item[props.children], props),
            }
          } else {
            return {
              value: item[props.value],
              label: item[props.label],
            }
          }
        })
      } else {
        return []
      }
    },
  },
  watch: {
    dataModel: {
      deep: true,
      handler(val) {
        this.models[this.widget.model] = val
        this.$emit('update:models', {
          ...this.models,
          [this.widget.model]: val,
        })
      },
    },
    models: {
      deep: true,
      handler(val) {
        this.dataModel = val[this.widget.model]
        this.dataModels = val
      },
    },
    remoteOption: {
      deep: 'true',
      handler: function (val) {
        if (
          Object.keys(this.remoteOption).indexOf(this.widget.options.remoteOption) >= 0 &&
          this.widget.options.remote &&
          this.widget.options.remoteType == 'option'
        ) {
          this.loadOptions(this.remoteOption[this.widget.options.remoteOption])
        }
      },
    },
    'widget.options.style.height': function (val) {
      console.log('baselinechart', val)
      var myChart = this.$echarts.init(this.$refs[this.widget.type])
      myChart.setOption(this.widget.options.defaultValue)

      this.$refs[this.widget.type].style.height = this.widget.options.style.height
      this.$refs[this.widget.type].style.width = this.widget.options.style.width
      this.$refs[this.widget.type].parentElement.style.height = this.widget.options.style.height
      this.$refs[this.widget.type].parentElement.style.width = this.widget.options.style.width

      // console.log('baselinechart', val)
      // var myChart = this.$echarts.init(this.$refs.baselinechart)
      // myChart.setOption(this.widget.options.defaultValue)
      // this.$refs.baselinechart.style.height = this.widget.options.style.height
      // this.$refs.baselinechart.style.width = this.widget.options.style.width
      // this.$refs.baselinechart.parentElement.style.height = this.widget.options.style.height
      // this.$refs.baselinechart.parentElement.style.width = this.widget.options.style.width
      // console.log('baselinechart777777', this.$refs.baselinechart)
      myChart.resize()
    },
    'widget.options.defaultValue': {
      deep: true,
      handler(val) {
        console.log('深度监听echart0', val)
        var myChart = this.$echarts.init(this.$refs[this.widget.type])
        myChart.setOption(this.widget.options.defaultValue)
      },
    },
  },
}
</script>
