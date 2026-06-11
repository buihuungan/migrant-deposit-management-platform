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

    <!-- 自动生成编码 -->
    <template v-if="element.type === 'autocode'">
      <el-input
        v-model="element.options.defaultValue"
        :style="{ width: isTable ? '100%' : element.options.width }"
        :placeholder="element.options.placeholder"
        :disabled="element.options.disabled"
        :show-password="element.options.showPassword"
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
      <!-- <vue-editor
        v-model="element.options.defaultValue"
        :style="{
          width: isTable ? '100%' : element.options.width,
          cursor: element.options.disabled ? 'no-drop' : '',
          backgroundColor: element.options.disabled ? '#F5F7FA' : '',
        }"
        :editor-toolbar="element.options.customToolbar"
        class="fm-editor"
        :disabled="element.options.disabled"
      /> -->
      <j-editor
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
    <!-- 弹框搜索 -->
    <template v-if="element.type === 'table_dict'">
      <j-popup v-model="element.options.defaultValue" :formFlag="true" :code="element.options.dictTable"></j-popup> 
      <i class="el-icon-question"></i>
    </template>
    <!-- 提示 -->
    <template v-if="element.type === 'tips'">
      <i class="el-icon-question"></i>
    </template>
  </span>
</template>

<script>
import FmUpload from './Upload'
import JPopup from '../../jeecg/JPopup'
import JEditor from '@/components/jeecg/JEditor'
import Vue from 'vue'
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
    FmUpload,
    JPopup,
    JEditor,
  },
  props: ['element', 'isTable'],
  data() {
    return {
      pcdremoteOptions: regionData,
      key: new Date().getTime(),
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
  },
}
</script>

<style  lang="scss">
.el-upload__input {
  display: none !important;
}
</style>
