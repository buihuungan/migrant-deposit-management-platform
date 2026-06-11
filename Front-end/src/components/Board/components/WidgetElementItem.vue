<template>
  <span>
    <!-- 单行文本 -->
    <template v-if="element.type === 'input'">
      <el-input
        v-model="element.options.defaultValue"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :placeholder="element.options.placeholder"
        :disabled="element.options.disabled"
        :show-password="element.options.showPassword"
      />
    </template>

    <!-- 多行文本 -->
    <template v-if="element.type === 'textarea'">
      <el-input
        type="textarea"
        :rows="5"
        v-model="element.options.defaultValue"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :disabled="element.options.disabled"
        :placeholder="element.options.placeholder"
      />
    </template>

    <!-- 计数器 -->
    <template v-if="element.type === 'number'">
      <el-input-number
        v-model="element.options.defaultValue"
        :disabled="element.options.disabled"
        :controls-position="element.options.controlsPosition"
        :style="{ width: isTable ? '100%' : element.options.width }"
      />
    </template>

    <!-- 单选框组 -->
    <template v-if="element.type === 'radio'">
      <el-radio-group
        v-model="element.options.defaultValue"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :disabled="element.options.disabled"
      >
        <el-radio
          :style="{ display: element.options.inline ? 'inline-block' : 'block' }"
          :label="item.value"
          v-for="(item, index) in element.options.options"
          :key="item.value + index"
        >
          {{ element.options.showLabel ? item.label : item.value }}
        </el-radio>
      </el-radio-group>
    </template>

    <!-- 多选框组 -->
    <template v-if="element.type === 'checkbox'">
      <el-checkbox-group
        v-model="element.options.defaultValue"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :disabled="element.options.disabled"
      >
        <el-checkbox
          :style="{ display: element.options.inline ? 'inline-block' : 'block' }"
          :label="item.value"
          v-for="(item, index) in element.options.options"
          :key="item.value + index"
        >
          {{ element.options.showLabel ? item.label : item.value }}
        </el-checkbox>
      </el-checkbox-group>
    </template>

    <!-- 时间选择器 -->
    <template v-if="element.type === 'time'">
      <el-time-picker
        v-model="element.options.defaultValue"
        :is-range="element.options.isRange"
        :placeholder="element.options.placeholder"
        :start-placeholder="element.options.startPlaceholder"
        :end-placeholder="element.options.endPlaceholder"
        :readonly="element.options.readonly"
        :disabled="element.options.disabled"
        :editable="element.options.editable"
        :clearable="element.options.clearable"
        :arrowControl="element.options.arrowControl"
        :style="{ width: isTable ? '100%' : element.options.width }"
      />
    </template>

    <!-- 日期选择器 -->
    <template v-if="element.type === 'date'">
      <el-date-picker
        v-model="element.options.defaultValue"
        :type="element.options.type"
        :is-range="element.options.isRange"
        :placeholder="element.options.placeholder"
        :start-placeholder="element.options.startPlaceholder"
        :end-placeholder="element.options.endPlaceholder"
        :readonly="element.options.readonly"
        :disabled="element.options.disabled"
        :editable="element.options.editable"
        :clearable="element.options.clearable"
        :style="{ width: isTable ? '100%' : element.options.width }"
      />
    </template>

    <!-- 评分 -->
    <template v-if="element.type === 'rate'">
      <el-rate
        v-model="element.options.defaultValue"
        :max="element.options.max"
        :disabled="element.options.disabled"
        :allow-half="element.options.allowHalf"
        :show-score="element.options.showScore"
      />
    </template>

    <!-- 颜色选择器 -->
    <template v-if="element.type === 'color'">
      <el-color-picker
        v-model="element.options.defaultValue"
        :disabled="element.options.disabled"
        :show-alpha="element.options.showAlpha"
      />
    </template>

    <!-- 下拉选择框 -->
    <template v-if="element.type === 'select'">
      <el-select
        v-model="element.options.defaultValue"
        :disabled="element.options.disabled"
        :multiple="element.options.multiple"
        :clearable="element.options.clearable"
        :placeholder="element.options.placeholder"
        :style="{ width: isTable ? '100%' : element.options.width }"
      >
        <el-option
          v-for="item in element.options.options"
          :key="item.value"
          :value="item.value"
          :label="element.options.showLabel ? item.label : item.value"
        />
      </el-select>
    </template>

    <!-- 开关 -->
    <template v-if="element.type === 'switch'">
      <el-switch v-model="element.options.defaultValue" :disabled="element.options.disabled" />
    </template>

    <!-- 滑块 -->
    <template v-if="element.type === 'slider'">
      <el-slider
        v-model="element.options.defaultValue"
        :min="element.options.min"
        :max="element.options.max"
        :disabled="element.options.disabled"
        :step="element.options.step"
        :show-input="element.options.showInput"
        :range="element.options.range"
        :style="{ width: isTable ? '100%' : element.options.width }"
      />
    </template>

    <!-- 图片上传 -->
    <template v-if="element.type === 'imgupload'">
      <fm-upload
        v-model="element.options.defaultValue"
        :disabled="element.options.disabled"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :width="element.options.size.width"
        :height="element.options.size.height"
        token="xxx"
        domain="xxx"
      />
    </template>

    <!-- 级联选择器 -->
    <template v-if="element.type === 'cascader'">
      <el-cascader
        v-model="element.options.defaultValue"
        :disabled="element.options.disabled"
        :clearable="element.options.clearable"
        :placeholder="element.options.placeholder"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :options="element.options.remoteOptions"
      />
    </template>

    <!-- 省市区联动 -->
    <template v-if="element.type === 'pcdcascader'">
      <el-cascader
        v-model="element.options.defaultValue"
        :disabled="element.options.disabled"
        :clearable="element.options.clearable"
        :placeholder="element.options.placeholder"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :options="pcdremoteOptions"
      />
    </template>

    <!-- 编辑器 -->
    <template v-if="element.type === 'editor'">
      <vue-editor
        v-model="element.options.defaultValue"
        :style="{
          width: isTable ? '100%' : element.options.width,
          cursor: element.options.disabled ? 'no-drop' : '',
          backgroundColor: element.options.disabled ? '#F5F7FA' : '',
        }"
        :editor-toolbar="element.options.customToolbar"
        class="fm-editor"
        :disabled="element.options.disabled"
      />
    </template>

    <!-- 自定义区域 -->
    <template v-if="element.type === 'blank'">
      <div style="height: 50px; color: #999; background: #eee; line-height: 50px; text-align: center">
        {{ element.model }}
      </div>
    </template>

    <!-- 自定义组件 -->
    <template v-if="element.type === 'component'">
      <component :is="`component-${element.key}`" :key="key" v-model="element.options.defaultValue" />
    </template>

    <!-- 自定义 -->
    <template v-if="element.type === 'custom'">
      <component v-if="element.component" :is="element.component.name" v-model="element.options.defaultValue" />
      <component v-else :is="element.el" v-model="element.options.defaultValue" />
    </template>

    <!-- 文字 -->
    <template v-if="element.type === 'text'">
      <span>{{ element.options.defaultValue }}</span>
    </template>

    <!-- HTML -->
    <template v-if="element.type === 'html'">
      <span v-html="element.options.defaultValue" />
    </template>
    <!-- 标签 -->
    <template v-if="element.type === 'tag'">
      <el-tag :type="element.options.type">{{ element.options.defaultValue }}</el-tag>
    </template>
    <!-- 文件上传 -->
    <template v-if="element.type === 'fileupload'">
      <el-upload
        action="https://jsonplaceholder.typicode.com/posts/"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :disabled="element.options.disabled"
        :limit="element.options.limit"
      >
        <!-- <el-button size="small" type="primary">{{ $t('fm.actions.upload') }}</el-button> -->
        <el-button size="small" type="primary">点击上传</el-button>
        <div v-if="element.options.tip" class="el-upload__tip" slot="tip">{{ element.options.tip }}</div>
      </el-upload>
    </template>

    <!-- eachart 基础折线图 测试 -->
    <template v-if="element.type === 'baselinechart'">
      <el-card
        v-model="element.options.defaultValue"
        :body-style="element.bodyStyle"
        :style="{ width: element.options.style.width, height: element.options.style.height }"
        :disabled="element.options.disabled"
        :shadow="element.shadow"
      >
        <div ref="baselinechart" :style="element.options.style"></div>
      </el-card>
    </template>

    <!-- 表格 测试 -->
    <template v-if="element.type === 'table_test'">
      <el-card
        v-model="element.options.defaultValue"
        :body-style="element.bodyStyle"
        :style="{ width: element.options.style.width, height: element.options.style.height }"
        :disabled="element.options.disabled"
        :shadow="element.shadow"
      >
        <div :style="element.options.style">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="date" label="日期" width="180"> </el-table-column>
            <el-table-column prop="name" label="姓名" width="180"> </el-table-column>
            <el-table-column prop="address" label="地址"> </el-table-column>
          </el-table>
          <el-table :data="element.options.defaultValue" v-if="element.options.defaultValue.length > 0">
            <el-table-column label="date" prop="date"></el-table-column>
            <el-table-column label="count" prop="count"></el-table-column>
          </el-table>
        </div>
      </el-card>
    </template>

    <!-- eachart 基础柱状图 测试 -->
    <template v-if="element.type === 'basecolumnchart'">
      <el-card
        v-model="element.options.defaultValue"
        :body-style="element.bodyStyle"
        :style="{ width: element.options.style.width, height: element.options.style.height }"
        :disabled="element.options.disabled"
        :shadow="element.shadow"
      >
        <div ref="basecolumnchart" :style="element.options.style"></div>
      </el-card>
    </template>

    <!-- eachart 基础饼状图 测试 -->
    <template v-if="element.type === 'basepiechart'">
      <el-card
        v-model="element.options.defaultValue"
        :body-style="element.bodyStyle"
        :style="{ width: element.options.style.width, height: element.options.style.height }"
        :disabled="element.options.disabled"
        :shadow="element.shadow"
      >
        <div ref="basepiechart" :style="element.options.style"></div>
      </el-card>
    </template>

    <!-- eachart 多系列柱状图 测试 -->
    <template v-if="element.type === 'seriescolumnchart'">
      <el-card
        v-model="element.options.defaultValue"
        :body-style="element.bodyStyle"
        :style="{ width: element.options.style.width, height: element.options.style.height }"
        :disabled="element.options.disabled"
        :shadow="element.shadow"
      >
        <div ref="seriescolumnchart" :style="element.options.style"></div>
      </el-card>
    </template>

    <!-- eachart 多系列折线图 测试 -->
    <template v-if="element.type === 'serieslinechart'">
      <el-card
        v-model="element.options.defaultValue"
        :body-style="element.bodyStyle"
        :style="{ width: element.options.style.width, height: element.options.style.height }"
        :disabled="element.options.disabled"
        :shadow="element.shadow"
      >
        <div ref="serieslinechart" :style="element.options.style"></div>
      </el-card>
    </template>
  </span>
</template>

<script>
import FmUpload from './Upload'
import Vue from 'vue'
import { regionData } from 'element-china-area-data'

export default {
  components: {
    FmUpload,
  },
  props: ['element', 'isTable'],
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
      pcdremoteOptions: regionData,
      key: new Date().getTime(),
    }
  },
  mounted() {
    if (this.element.type === 'baselinechart') {
      console.log('判断当前加载组件为基础折线图')
      this.baselinechart()
    } else if (this.element.type === 'basecolumnchart') {
      console.log('判断当前加载组件为基础柱状图')
      this.basecolumnchart()
    } else if (this.element.type === 'basepiechart') {
      console.log('判断当前加载组件为基础饼状图')
      this.basepiechart()
    } else if (this.element.type === 'seriescolumnchart') {
      console.log('判断当前加载组件为多系列柱状图')
      this.seriescolumnchart()
    } else if (this.element.type === 'serieslinechart') {
      console.log('判断当前加载组件为多系列折线图')
      this.serieslinechart()
    }
  },
  created() {
    if (this.element.type === 'component') {
      Vue.component(`component-${this.element.key}`, {
        template: `<span>${this.element.options.template}</span>`,
        props: ['dataModel'],
      })
    }
  },
  watch: {
    'element.options.type': function (val) {
      if (val === 'year') {
        this.element.options.format = 'yyyy'
      } else if (val === 'month') {
        this.element.options.format = 'yyyy-MM'
      } else if (val === 'date') {
        this.element.options.format = 'yyyy-MM-dd'
      } else if (val === 'dates') {
        this.element.options.format = 'yyyy-MM-dd'
      } else if (val === 'datetime') {
        this.element.options.format = 'yyyy-MM-dd HH:mm:ss'
      } else if (val === 'datetimerange') {
        this.element.options.format = 'yyyy-MM-dd HH:mm:ss'
      } else if (val === 'daterange') {
        this.element.options.format = 'yyyy-MM-dd'
      } else {
        this.element.options.format = 'yyyy-MM-dd'
      }
    },
    'element.options.template': function (val) {
      Vue.component(`component-${this.element.key}`, {
        template: `<span>${val}</span>`,
        props: ['dataModel'],
      })

      this.key = new Date().getTime()
    },
    'element.options.style.height': function (val) {
      console.log('baselinechart', val)
      var myChart = this.$echarts.init(this.$refs[this.element.type])
      myChart.setOption(this.element.options.defaultValue)

      this.$refs[this.element.type].style.height = this.element.options.style.height
      this.$refs[this.element.type].style.width = this.element.options.style.width
      this.$refs[this.element.type].parentElement.style.height = this.element.options.style.height
      this.$refs[this.element.type].parentElement.style.width = this.element.options.style.width

      // this.$refs.baselinechart.style.height = this.element.options.style.height
      // this.$refs.baselinechart.style.width = this.element.options.style.width
      // this.$refs.baselinechart.parentElement.style.height = this.element.options.style.height
      // this.$refs.baselinechart.parentElement.style.width = this.element.options.style.width
      // console.log('baselinechart777777', this.$refs[this.element.type])
      myChart.resize()
    },
    'element.options.defaultValue': {
      deep: true,
      handler(val) {
        console.log('深度监听echart1', val)
        if (this.element.type == 'table_test') {
          console.log('深度监听里的表格数据', this.element.options.defaultValue)
          return
        }
        var myChart = this.$echarts.init(this.$refs[this.element.type])
        // console.log('123456',this.$refs[this.element.type])
        myChart.setOption(this.element.options.defaultValue)
      },
    },
  },
  methods: {
    baselinechart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.baselinechart)
      baselinechar.setOption(this.element.options.defaultValue)
    },
    basecolumnchart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.basecolumnchart)
      baselinechar.setOption(this.element.options.defaultValue)
    },
    basepiechart() {
      console.log('初始化饼状图', this.element.options.defaultValue)
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.basepiechart)
      baselinechar.setOption(this.element.options.defaultValue)
    },
    seriescolumnchart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.seriescolumnchart)
      baselinechar.setOption(this.element.options.defaultValue)
    },
    serieslinechart() {
      // 基于准备好的dom，初始化echarts实例
      var baselinechar = this.$echarts.init(this.$refs.serieslinechart)
      baselinechar.setOption(this.element.options.defaultValue)
    },
  },
}
</script>
