<template>
  <span class="generateElementItem">
    <template v-if="widget.type === 'blank'">
      <!-- 自定义区域 -->
      <div :style="{ width: isTable ? '100%' : widget.options.width }">
        <slot :name="widget.model" :model="dataModels" />
      </div>
    </template>

    <template v-if="widget.type === 'component'">
      <!-- 自定义组件 -->
      <div :style="{ width: isTable ? '100%' : widget.options.width }">
        <component :is="`component-${widget.key}-${key}`" :key="key" v-model="dataModel" />
      </div>
    </template>

    <template v-if="widget.type === 'custom'">
      <!-- 自定义 -->
      <div :style="{width: isTable ? '100%' : widget.options.width}">
        <component :is="widget.el" v-model="dataModel" :width="widget.options.width" :height="widget.options.height" :placeholder="widget.options.placeholder" :readonly="widget.options.readonly" :disabled="!edit || widget.options.disabled" :editable="widget.options.editable" :clearable="widget.options.clearable" />
      </div>
    </template>

    <template v-if="widget.type === 'input'">
      <!-- 单行文本 -->
      <el-input v-if="widget.options.dataType === 'number' || widget.options.dataType === 'integer' || widget.options.dataType === 'float'" type="number" v-model.number="dataModel" :disabled="!edit || widget.options.disabled" :placeholder="widget.options.placeholder" :show-password="widget.options.showPassword" :style="{ width: isTable ? '100%' : widget.options.width }" />
      <el-input v-else :type="widget.options.dataType" v-model="dataModel" :disabled="!edit || widget.options.disabled" :placeholder="widget.options.placeholder" :show-password="widget.options.showPassword" :style="{ width: isTable ? '100%' : widget.options.width }" />
    </template>

    <template v-if="widget.type === 'textarea'">
      <!-- 多行文本 -->
      <el-input type="textarea" :rows="5" v-model="dataModel" :disabled="!edit || widget.options.disabled" :placeholder="widget.options.placeholder" :style="{ width: isTable ? '100%' : widget.options.width }" />
    </template>

    <template v-if="widget.type === 'number'">
      <!-- 计数器 -->
      <el-input-number v-model="dataModel" :style="{width: isTable ? '100%' : widget.options.width}" :step="widget.options.step" controls-position="right" :disabled="!edit || widget.options.disabled" :min="widget.options.min" :max="widget.options.max" />
    </template>

    <template v-if="widget.type === 'radio'">
      <!-- 单选框组 -->
      <el-radio-group v-model="dataModel" :style="{ width: isTable ? '100%' : widget.options.width }" :disabled="!edit || widget.options.disabled">
        <el-radio :style="{ display: widget.options.inline ? 'inline-block' : 'block' }" :label="item.value" v-for="(item, index) in widget.options.remote ? remoteOptions : widget.options.options" :key="index">
          <template v-if="widget.options.remote">{{ item.label }}</template>
          <template v-else>{{ widget.options.showLabel ? item.label : item.value }}</template>
        </el-radio>
      </el-radio-group>
    </template>

    <template v-if="widget.type === 'checkbox'">
      <!-- 多选框组 -->
      <el-checkbox-group v-model="dataModel" :style="{ width: isTable ? '100%' : widget.options.width }" :disabled="!edit || widget.options.disabled">
        <el-checkbox :style="{display: widget.options.inline ? 'inline-block' : 'block'}" :label="item.value" v-for="(item, index) in (widget.options.remote ? remoteOptions : widget.options.options)" :key="index">
          <template v-if="widget.options.remote">{{ item.label }}</template>
          <template v-else>{{ widget.options.showLabel ? item.label : item.value }}</template>
        </el-checkbox>
      </el-checkbox-group>
    </template>

    <template v-if="widget.type === 'time'">
      <!-- 时间选择器 -->
      <el-time-picker v-model="dataModel" :is-range="widget.options.isRange" :placeholder="widget.options.placeholder" :start-placeholder="widget.options.startPlaceholder" :end-placeholder="widget.options.endPlaceholder" :readonly="widget.options.readonly" :disabled="!edit || widget.options.disabled" :editable="widget.options.editable" :clearable="widget.options.clearable" :arrowControl="widget.options.arrowControl" :value-format="widget.options.format" :style="{ width: isTable ? '100%' : widget.options.width }">
      </el-time-picker>
    </template>

    <template v-if="widget.type === 'date'">
      <!-- 日期选择器 -->
      <el-date-picker v-model="dataModel" :type="widget.options.type" :placeholder="widget.options.placeholder" :start-placeholder="widget.options.startPlaceholder" :end-placeholder="widget.options.endPlaceholder" :readonly="widget.options.readonly" :disabled="!edit || widget.options.disabled" :editable="widget.options.editable" :clearable="widget.options.clearable" :value-format="widget.options.timestamp ? 'timestamp' : widget.options.format" :format="widget.options.format" :style="{ width: isTable ? '100%' : widget.options.width }">
      </el-date-picker>
    </template>

    <template v-if="widget.type === 'rate'">
      <!-- 评分 -->
      <el-rate v-model="dataModel" :max="widget.options.max" :disabled="!edit || widget.options.disabled" :allow-half="widget.options.allowHalf" :show-score="widget.options.showScore" />
    </template>

    <template v-if="widget.type === 'color'">
      <!-- 颜色选择器 -->
      <el-color-picker v-model="dataModel" :disabled="!edit || widget.options.disabled" :show-alpha="widget.options.showAlpha" />
    </template>

    <template v-if="widget.type === 'select'">
      <!-- 下拉选择框 -->
      <el-select v-model="dataModel" :disabled="!edit || widget.options.disabled" :multiple="widget.options.multiple" :clearable="widget.options.clearable" :placeholder="widget.options.placeholder" :style="{ width: isTable ? '100%' : widget.options.width }" :filterable="widget.options.filterable" :popper-append-to-body="false">
        <el-option v-for="item in widget.options.remote ? remoteOptions : widget.options.options" :key="item.value" :value="item.value" :label="widget.options.showLabel || widget.options.remote ? item.label : item.value" />
      </el-select>
    </template>

    <template v-if="widget.type === 'switch'">
      <!-- 开关 -->
      <el-switch v-model="dataModel" :disabled="!edit || widget.options.disabled"> </el-switch>
    </template>

    <template v-if="widget.type === 'slider'">
      <!-- 滑块 -->
      <el-slider v-model="dataModel" :min="widget.options.min" :max="widget.options.max" :disabled="!edit || widget.options.disabled" :step="widget.options.step" :show-input="widget.options.showInput" :range="widget.options.range" :style="{ width: isTable ? '100%' : widget.options.width }" />
    </template>

    <template v-if="widget.type === 'imgupload'">
      <!-- 图片上传 -->
      <fm-upload v-model="dataModel" :disabled="!edit || widget.options.disabled" :readonly="widget.options.readonly" :style="{ width: isTable ? '100%' : widget.options.width }" :width="widget.options.size.width" :height="widget.options.size.height" :token="widget.options.token" :domain="widget.options.domain" :multiple="widget.options.multiple" :limit="widget.options.limit" :is-qiniu="widget.options.isQiniu" :is-delete="widget.options.isDelete" :min="widget.options.min" :is-edit="widget.options.isEdit" :action="widget.options.action" :headers="widget.options.headers || []">
      </fm-upload>
    </template>

    <template v-if="widget.type === 'editor'">
      <!-- 编辑器 -->
      <fm-editor v-model="dataModel" :sty="{
          width: isTable ? '100%' : widget.options.width,
          cursor: !edit || widget.options.disabled ? 'no-drop' : '',
          backgroundColor: !edit || widget.options.disabled ? '#F5F7FA' : '',
        }" :toolbar="widget.options.customToolbar" :disabled="!edit || widget.options.disabled">
      </fm-editor>
    </template>

    <template v-if="widget.type === 'cascader'">
      <!-- 级联选择器 -->
      <el-cascader v-model="dataModel" :disabled="!edit || widget.options.disabled" :clearable="widget.options.clearable" :placeholder="widget.options.placeholder" :style="{width: isTable ? '100%' : widget.options.width}" :options="remoteOptions">
      </el-cascader>
    </template>

    <template v-if="widget.type === 'pcdcascader'">
      <!-- 省市区联动 -->
      <el-cascader v-model="dataModel" :disabled="!edit || widget.options.disabled" :clearable="widget.options.clearable" :placeholder="widget.options.placeholder" :style="{width: isTable ? '100%' : widget.options.width}" :options="pcdremoteOptions">
      </el-cascader>
    </template>

    <template v-if="widget.type === 'text'">
      <!-- 文字 -->
      <span>{{ dataModel }}</span>
    </template>

    <template v-if="widget.type === 'html'">
      <!-- HTML -->
      <span v-html="dataModel" />
    </template>

    <template v-if="widget.type === 'table'">
      <!-- 子表单 -->
      <fm-form-table :value="dataModel" :columns="widget.tableColumns" :models="dataModels" :remote="remote" :blanks="blanks" :disabled="!edit || widget.options.disabled" :rules="rules" :name="widget.model" :remote-option="remoteOption">
        <template v-slot:[blank.name]="scope" v-for="blank in blanks">
          <slot :name="blank.name" :model="scope.model" />
        </template>
      </fm-form-table>
    </template>

    <template v-if="widget.type === 'fileupload'">
      <!-- 文件上传 -->
      <fm-file-upload v-model="dataModel" :disabled="!edit || widget.options.disabled" :style="{ width: isTable ? '100%' : widget.options.width }" :token="widget.options.token" :domain="widget.options.domain" :multiple="widget.options.multiple" :limit="widget.options.limit" :is-qiniu="widget.options.isQiniu" :min="widget.options.min" :action="widget.options.action" :tip="widget.options.tip" :headers="widget.options.headers || []" @change="handleDataModelChange" />
    </template>

    <template v-if="widget.type === 'baselinechart'">
      <!-- eachart 基础折线图 测试 -->
      <el-card v-model="widget.options.defaultValue" :body-style="widget.bodyStyle" :style="{ width: widget.options.style.width,height: widget.options.style.height }" :disabled="widget.options.disabled" :shadow="widget.shadow">
        <div ref="baselinechart" :style="widget.options.style"></div>
      </el-card>
    </template>
    <template v-if="widget.type === 'basecolumnchart'">
      <!-- eachart 基础柱状图 测试 -->
      <el-card v-model="widget.options.defaultValue" :body-style="widget.bodyStyle" :style="{ width: widget.options.style.width,height: widget.options.style.height }" :disabled="widget.options.disabled" :shadow="widget.shadow">
        <div ref="basecolumnchart" :style="widget.options.style"></div>
      </el-card>
    </template>
    <template v-if="widget.type === 'basepiechart'">
      <!-- eachart 基础饼状图 测试 -->
      <el-card v-model="widget.options.defaultValue" :body-style="widget.bodyStyle" :style="{ width: widget.options.style.width,height: widget.options.style.height }" :disabled="widget.options.disabled" :shadow="widget.shadow">
        <div ref="basepiechart" :style="widget.options.style"></div>
      </el-card>
    </template>
    <template v-if="widget.type === 'table_test'">
      <!-- eachart 表格 测试 -->
      <el-card v-model="widget.options.defaultValue" :body-style="widget.bodyStyle" :style="{ width: widget.options.style.width,height: widget.options.style.height }" :disabled="widget.options.disabled" :shadow="widget.shadow">
        <div  :style="widget.options.style">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="date" label="日期" width="180"> </el-table-column>
            <el-table-column prop="name" label="姓名" width="180"> </el-table-column>
            <el-table-column prop="address" label="地址"> </el-table-column>
          </el-table>
        </div>
      </el-card>
    </template>
    <template v-if="widget.type === 'seriescolumnchart'">
      <!-- eachart 多系柱状图 测试 -->
      <el-card v-model="widget.options.defaultValue" :body-style="widget.bodyStyle" :style="{ width: widget.options.style.width,height: widget.options.style.height }" :disabled="widget.options.disabled" :shadow="widget.shadow">
        <div ref="seriescolumnchart" :style="widget.options.style"></div>
      </el-card>
    </template>
    <template v-if="widget.type === 'serieslinechart'">
      <!-- eachart 多系列折现图 测试 -->
      <el-card v-model="widget.options.defaultValue" :body-style="widget.bodyStyle" :style="{ width: widget.options.style.width,height: widget.options.style.height }" :disabled="widget.options.disabled" :shadow="widget.shadow">
        <div ref="serieslinechart" :style="widget.options.style"></div>
      </el-card>
    </template>
  </span>
</template>

<script>
import FmUpload from './Upload'
import FmFormTable from './FormTable'
import FmFileUpload from './Upload/file'
import FmEditor from './Editor'
import Vue from 'vue'
import { regionData } from 'element-china-area-data'

export default {
  name: 'generate-element-item',
  components: {
    FmUpload,
    FmFormTable,
    FmFileUpload,
    FmEditor,
  },
  props: ['widget', 'value', 'models', 'remote', 'isTable', 'blanks', 'disabled', 'edit', 'remoteOption', 'rules'],
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
      dataModel: this.value,
      dataModels: { ...this.models },
      remoteOptions: [],
      pcdremoteOptions: regionData,
      key: new Date().getTime(),
    }
  },
  mounted() {
    if (this.widget.type === 'baselinechart') {
      console.log('判断当前加载组件为基础折线图66')
      this.baselinechart()
    } else if (this.widget.type === 'basecolumnchart') {
      console.log('判断当前加载组件为基础柱状图66')
      this.basecolumnchart()
    } else if (this.widget.type === 'basepiechart') {
      console.log('判断当前加载组件为基础饼状图')
      this.basepiechart()
    } else if (this.widget.type === 'seriescolumnchart') {
      console.log('判断当前加载组件为多系列柱状图')
      this.seriescolumnchart()
    } else if (this.widget.type === 'serieslinechart') {
      console.log('判断当前加载组件为多系列折线图')
      this.serieslinechart()
    }
  },
  created() {
    this.remoteOptions = []
    if (
      this.widget.options.remote &&
      (Object.keys(this.widget.options).indexOf('remoteType') >= 0
        ? this.widget.options.remoteType === 'func'
        : true) &&
      this.remote[this.widget.options.remoteFunc]
    ) {
      this.remote[this.widget.options.remoteFunc]((data) => {
        this.loadOptions(data)
      })
    }
    if (
      this.widget.options.remote &&
      this.widget.options.remoteType === 'option' &&
      this.remoteOption[this.widget.options.remoteOption]
    ) {
      this.loadOptions(this.remoteOption[this.widget.options.remoteOption])
    }

    if ((this.widget.type === 'imgupload' || this.widget.type === 'fileupload') && this.widget.options.isQiniu) {
      !this.widget.options.token &&
        this.remote[this.widget.options.tokenFunc]((data) => {
          this.widget.options.token = data
        })
    }

    if (this.widget.type === 'component') {
      Vue.component(`component-${this.widget.key}-${this.key}`, {
        template: `<span>${this.widget.options.template}</span>`,
        props: ['value'],
        data: () => ({
          dataModel: this.value,
        }),
        watch: {
          dataModel(val) {
            if (this.ui === 'antd') {
              EventBus.$emit('on-field-change', this.$attrs.id, val)
            } else {
              this.$emit('input', val)
            }
          },
          value(val) {
            this.dataModel = val
          },
        },
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

    handleDataModelChange(value) {
      this.dataModel = value
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
    value(val) {
      this.dataModel = val
    },
    models: {
      deep: true,
      handler(val) {
        this.dataModels = { ...val }
      },
    },
    dataModel(val) {
      this.$emit('input', val)
    },
    dataModels: {
      deep: true,
      handler(val) {
        this.$emit('input', val[this.widget.model])
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
  },
}
</script>
<style >
/* .generateElementItem .el-select-dropdown { */
/* position: absolute !important; */
/* top:30px !important;
  left: 0px !important;
} */
.cell {
  overflow: visible !important;
}
</style>