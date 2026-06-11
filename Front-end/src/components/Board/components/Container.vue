<template>
  <el-container class="fm2-container">
    <el-main class="fm2-main">
      <el-container>
        <!-- 左侧基础字段 -->
        <el-aside width="250px">
          <div class="components-list">
            <template v-if="basicFields.length">
              <!-- <div class="widget-cate">{{ $t('fm.components.basic.title') }}</div> -->
              <div class="widget-cate">基础字段</div>
              <draggable
                tag="ul"
                :list="basicComponents"
                v-bind="{ group: { name: 'people', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' }"
                @end="handleMoveEnd"
                @start="handleMoveStart"
                :move="handleMove"
              >
                <li
                  @click="handleField(item)"
                  :v-if="basicFields.indexOf(item.type) >= 0"
                  class="form-edit-widget-label"
                  :class="{ 'no-put': item.type === 'divider' }"
                  v-for="(item, index) in basicComponents"
                  :key="index"
                >
                  <a>
                    <i class="icon iconfont" :class="item.icon" />
                    <span>{{ item.name }}</span>
                  </a>
                </li>
              </draggable>
            </template>

            <template v-if="advanceFields.length">
              <!-- <div class="widget-cate">{{ $t('fm.components.advance.title') }}</div> -->
              <div class="widget-cate">高级字段</div>
              <draggable
                tag="ul"
                :list="advanceComponents"
                v-bind="{ group: { name: 'people', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' }"
                @end="handleMoveEnd"
                @start="handleMoveStart"
                :move="handleMove"
              >
                <li
                  @click="handleField(item)"
                  :v-if="advanceFields.indexOf(item.type) >= 0"
                  class="form-edit-widget-label"
                  :class="{ 'no-put': item.type === 'table' }"
                  v-for="(item, index) in advanceComponents"
                  :key="index"
                >
                  <a>
                    <i class="icon iconfont" :class="item.icon" />
                    <span>{{ item.name }}</span>
                  </a>
                </li>
              </draggable>
            </template>

            <template v-if="linechartFields.length">
              <!-- <div class="widget-cate">{{ $t('fm.components.line.title') }}</div> -->
              <div class="widget-cate">折线图</div>
              <draggable
                tag="ul"
                :list="linechartComponents"
                v-bind="{ group: { name: 'people', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' }"
                @end="handleMoveEnd"
                @start="handleMoveStart"
                :move="handleMove"
              >
                <li
                  @click="handleField(item)"
                  :v-if="linechartFields.indexOf(item.type) >= 0"
                  class="form-edit-widget-label"
                  :class="{ 'no-put': item.type === 'table' }"
                  v-for="(item, index) in linechartComponents"
                  :key="index"
                >
                  <a>
                    <i class="icon iconfont" :class="item.icon" />
                    <span>{{ item.name }}</span>
                  </a>
                </li>
              </draggable>
            </template>

            <template v-if="columnchartFields.length">
              <!-- <div class="widget-cate">{{ $t('fm.components.column.title') }}</div> -->
              <div class="widget-cate">柱状图</div>
              <draggable
                tag="ul"
                :list="columnchartComponents"
                v-bind="{ group: { name: 'people', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' }"
                @end="handleMoveEnd"
                @start="handleMoveStart"
                :move="handleMove"
              >
                <li
                  @click="handleField(item)"
                  :v-if="columnchartFields.indexOf(item.type) >= 0"
                  class="form-edit-widget-label"
                  :class="{ 'no-put': item.type === 'table' }"
                  v-for="(item, index) in columnchartComponents"
                  :key="index"
                >
                  <a>
                    <i class="icon iconfont" :class="item.icon" />
                    <span>{{ item.name }}</span>
                  </a>
                </li>
              </draggable>
            </template>

            <template v-if="piechartFields.length">
              <!-- <div class="widget-cate">{{ $t('fm.components.pie.title') }}</div> -->
              <div class="widget-cate">饼状图</div>
              <draggable
                tag="ul"
                :list="piechartComponents"
                v-bind="{ group: { name: 'people', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' }"
                @end="handleMoveEnd"
                @start="handleMoveStart"
                :move="handleMove"
              >
                <li
                  @click="handleField(item)"
                  :v-if="piechartFields.indexOf(item.type) >= 0"
                  class="form-edit-widget-label"
                  :class="{ 'no-put': item.type === 'table' }"
                  v-for="(item, index) in piechartComponents"
                  :key="index"
                >
                  <a>
                    <i class="icon iconfont" :class="item.icon" />
                    <span>{{ item.name }}</span>
                  </a>
                </li>
              </draggable>
            </template>

            <template v-if="layoutFields.length">
              <!-- <div class="widget-cate">{{ $t('fm.components.layout.title') }}</div> -->
              <div class="widget-cate">布局字段</div>
              <draggable
                tag="ul"
                :list="layoutComponents"
                v-bind="{ group: { name: 'people', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' }"
                @end="handleMoveEnd"
                @start="handleMoveStart"
                :move="handleMove"
              >
                <li
                  @click="handleField(item)"
                  :v-if="layoutFields.indexOf(item.type) >= 0"
                  class="form-edit-widget-label no-put"
                  v-for="(item, index) in layoutComponents"
                  :key="index"
                >
                  <a>
                    <i class="icon iconfont" :class="item.icon" />
                    <span>{{ item.name }}</span>
                  </a>
                </li>
              </draggable>
            </template>

            <template v-if="customFields.length">
              <!-- <div class="widget-cate">{{ $t('fm.components.custom.title') }}</div> -->
              <div class="widget-cate">自定义字段</div>
              <draggable
                tag="ul"
                :list="customComponents"
                v-bind="{ group: { name: 'people', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' }"
                @end="handleMoveEnd"
                @start="handleMoveStart"
                :move="handleMove"
              >
                <li
                  @click="handleField(item)"
                  class="form-edit-widget-label"
                  v-for="(item, index) in customComponents"
                  :key="index"
                >
                  <a>
                    <i class="icon iconfont" :class="item.icon" />
                    <span>{{ item.name }}</span>
                  </a>
                </li>
              </draggable>
            </template>
          </div>
        </el-aside>
        <el-container class="center-container" direction="vertical">
          <!-- 中间上部按钮部分 -->
          <el-header class="btn-bar" style="height: 45px">
            <slot name="action" />
            <!-- {{ $t('fm.actions.close') }} -->
            <el-button
              size="medium"
              type="text"
              icon="el-icon-close"
              class="formakingclose"
              @click="handleClose"
              style="color: #f56c6c"
            >
             关闭 </el-button
            >
            <!-- {{ $t('fm.actions.undo') }} -->
            <el-button type="text" :disabled="!undo" size="medium" icon="el-icon-back" @click="handleUndo">
             撤销 </el-button
            >
            <!--{{ $t('fm.actions.redo') }}  -->
            <el-button type="text" :disabled="!redo" size="medium" icon="el-icon-right" @click="handleRedo">
             重做 </el-button
            >
            <!-- {{ $t('fm.actions.import') }} -->
            <el-button v-if="upload" type="text" size="medium" icon="el-icon-upload2" @click="handleUpload">
             导入JSON </el-button
            >
            <!--  {{ $t('fm.actions.clear') }} -->
            <el-button v-if="clearable" type="text" size="medium" icon="el-icon-delete" @click="handleClear">
             清空 </el-button
            >
            <!-- {{ $t('fm.actions.preview') }} -->
            <el-button v-if="preview" type="text" size="medium" icon="el-icon-view" @click="handlePreview">
             预览 </el-button
            >
            <!-- {{ $t('fm.actions.json') }} -->
            <el-button v-if="generateJson" type="text" size="medium" icon="el-icon-tickets" @click="handleGenerateJson">
             生成JSON </el-button
            >
            <!-- {{ $t('fm.actions.submit') }} -->
            <el-button size="medium" type="text" icon="el-icon-download" @click="handleSubmit">
             保存 </el-button
            >
            <!-- {{ $t('fm.actions.code') }} -->
            <el-button
              v-if="generateCode"
              type="text"
              size="medium"
              icon="el-icon-document"
              @click="handleGenerateCode"
            >
             生成代码 </el-button
            >
          </el-header>
          <!-- 中间中部图形部分 -->
          <el-main :class="{ 'widget-empty': widgetForm.list.length === 0 }">
            <widget-form v-if="!resetJson" ref="widgetForm" :data="widgetForm" :select.sync="widgetFormSelect" />
          </el-main>
        </el-container>

        <el-aside class="widget-config-container">
          <el-container>
            <!-- 右侧标题导航 -->
            <el-header height="45px">
              <div class="config-tab" :class="{ active: configTab === 'widget' }" @click="handleConfigSelect('widget')">
                <!-- {{ $t('fm.config.widget.title') }} -->
                字段属性
              </div>
              <div class="config-tab" :class="{ active: configTab === 'form' }" @click="handleConfigSelect('form')">
                <!-- {{ $t('fm.config.form.title') }} -->
                表单属性
              </div>
            </el-header>
            <!-- 右侧主体部分 -->
            <el-main class="config-content">
              <widget-config
                v-show="configTab === 'widget'"
                :widgetForm="widgetForm"
                :data="widgetFormSelect"
                :key="widgetFormSelect ? widgetFormSelect.key : 0"
              >
              </widget-config>
              <form-config v-show="configTab === 'form'" :widgetForm="widgetForm" :data="widgetForm.config">
              </form-config>
            </el-main>
          </el-container>
        </el-aside>

        <!-- 预览按钮弹出框开始 -->
        <!-- "$t('fm.actions.preview')" -->
        <cus-dialog
          :visible="previewVisible"
          @on-close="previewVisible = false"
          ref="widgetPreview"
          form
          title="预览"
          fullscreen
        >
          <generate-form
            style="margin: 0 auto"
            insite="true"
            v-if="previewVisible && (previewForm.config.ui === 'element' || !previewForm.config.ui)"
            :data="previewForm"
            :value="widgetModels"
            :remote="remoteFuncs"
            ref="generateForm"
          >
            <template v-slot:blank="scope">
              Width
              <el-input v-model="scope.model.blank.width" style="width: 100px" />
              Height：
              <el-input v-model="scope.model.blank.height" style="width: 100px" />
            </template>
          </generate-form>

          <antd-generate-form
            style="margin: 0 auto"
            insite="true"
            v-if="previewVisible && previewForm.config.ui === 'antd'"
            :data="previewForm"
            :value="widgetModels"
            :remote="remoteFuncs"
            ref="generateAntForm"
          >
            <template v-slot:blank="scope">
              <a-input
                v-decorator="[
                  'blank',
                  {
                    initialValue: scope.model.blank,
                  },
                ]"
                style="width: 100px"
              />
            </template>
          </antd-generate-form>

          <template slot="action">
            <el-button type="primary" class="json-btn" v-if="comdataflag" @click="handleTest"> 提交至数据库</el-button>
            <el-button type="warning" @click="handleReset">
              <!-- {{ $t('fm.actions.reset') }} -->
              重置
            </el-button>
            <el-button type="info" @click="previewVisible = false"> 退出</el-button>
          </template>
        </cus-dialog>
        <!-- 预览按钮弹出框结束 -->
        <!-- :title="$t('fm.actions.import')"  -->
        <cus-dialog
          :visible="uploadVisible"
          @on-close="uploadVisible = false"
          @on-submit="handleUploadJson"
          ref="uploadJson"
          width="800px"
          form
          title="导入JSON"
        >
          <!-- <el-alert type="info" :title="$t('fm.description.uploadJsonInfo')" /> -->
          <el-alert type="info" title="JSON格式如下，直接复制生成的json覆盖此处代码点击确定即可" />
          <code-editor height="400px" mode="json" v-model="jsonEg" />
        </cus-dialog>

        <!-- 预览点击获取数据按钮弹框开始 -->
        <!-- :title="$t('fm.actions.json')"  -->
        <cus-dialog
          :visible="jsonVisible"
          @on-close="jsonVisible = false"
          ref="jsonPreview"
          width="800px"
          form
          title="生成JSON"
        >
          <code-editor height="400px" mode="json" v-model="jsonTemplate" />
          <template slot="action">
            <el-button type="primary" class="json-btn" v-if="comdataflag" @click="commitToDatabase(jsonTemplate)">
              提交至数据库</el-button
            >
            <el-button type="primary" class="json-btn" :data-clipboard-text="jsonCopyValue">
              <!-- {{ $t('fm.actions.copyData') }} -->
              复制
            </el-button
            >
          </template>
        </cus-dialog>
        <!-- 预览点击获取数据按钮弹框开始 -->
        <!-- :title="$t('fm.actions.code')" -->
        <cus-dialog
          :visible="codeVisible"
          @on-close="codeVisible = false"
          ref="codePreview"
          width="800px"
          form
          title="生成代码"
        >
          <el-tabs type="border-card" style="box-shadow: none" v-model="codeActiveName">
            <el-tab-pane label="Vue Component" name="vue">
              <code-editor height="450px" mode="html" v-model="vueTemplate" />
            </el-tab-pane>
            <el-tab-pane label="HTML" name="html">
              <code-editor height="450px" mode="html" v-model="htmlTemplate" />
            </el-tab-pane>
          </el-tabs>

          <template slot="action">
            <el-button type="primary" class="code-btn" :data-clipboard-text="codeCopyValue">
              <!-- {{ $t('fm.actions.copyData') }} -->
              复制
              </el-button
            >
          </template>
        </cus-dialog>
      </el-container>
    </el-main>
    <el-footer height="30px" style="font-weight: 600">
      Powered by
      <a target="_blank" href="http://form.making.link">FormMaking</a>
    </el-footer>
  </el-container>
</template>

<script>
import Draggable from 'vuedraggable'
import WidgetConfig from './WidgetConfig'
import FormConfig from './FormConfig'
import WidgetForm from './WidgetForm'
import CusDialog from './CusDialog'
import GenerateForm from './GenerateForm'
import AntdGenerateForm from './AntdvGenerator/GenerateForm'
import Clipboard from 'clipboard'
import CodeEditor from '../components/CodeEditor'
import { basicComponents, layoutComponents, advanceComponents, linechartComponents, columnchartComponents, piechartComponents } from './componentsConfig.js'
import { EventBus } from '../util/event-bus.js'
import generateCode from './generateCode.js'
import historyManager from '../util/history-manager.js'
import _ from 'lodash'
import { axios } from "../../../utils/request";
import { changeScreen } from '@api/boardapi'

export default {
  name: 'fm-making-form1',
  components: {
    Draggable,
    WidgetConfig,
    FormConfig,
    WidgetForm,
    CusDialog,
    GenerateForm,
    CodeEditor,
    AntdGenerateForm,
  },
  props: {
    preview: {
      type: Boolean,
      default: false,
    },
    generateCode: {
      type: Boolean,
      default: false,
    },
    generateJson: {
      type: Boolean,
      default: false,
    },
    upload: {
      type: Boolean,
      default: false,
    },
    clearable: {
      type: Boolean,
      default: false,
    },
    // 基础字段 具体对应组件查看lang文件夹下面的zh-CN.js文件
    basicFields: {
      type: Array,
      default: () => [
        'input',
        'textarea',
        'number',
        'radio',
        'checkbox',
        'time',
        'date',
        'rate',
        'color',
        'select',
        'switch',
        'slider',
        'text',
        'html',
      ],
    },
    // 高级字段
    advanceFields: {
      type: Array,
      default: () => ['blank', 'component', 'fileupload', 'imgupload', 'editor', 'cascader', 'pcdcascader', 'table'],
    },
    // 折线图
    linechartFields: {
      type: Array,
      default: () => ['baselinechart', 'serieslinechart']
    },
    // 柱状图
    columnchartFields: {
      type: Array,
      default: () => ['seriescolumnchart', 'basecolumnchart']
    },
    // 饼状图
    piechartFields: {
      type: Array,
      default: () => ['basepiechart', 'table_test']
    },
    // 布局字段
    layoutFields: {
      type: Array,
      default: () => ['grid', 'tabs', 'divider'],
    },
    customFields: {
      type: Array,
      default: () => [],
    },
    importFormData: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      comdataflag: true,
      basicComponents,
      layoutComponents,
      advanceComponents,
      linechartComponents,
      columnchartComponents,
      piechartComponents,
      customComponents: [],
      resetJson: false,
      widgetForm: {
        list: [],
        config: {
          labelWidth: 100,
          labelPosition: 'right',
          size: 'small',
          customClass: '',
          ui: 'element',
          layout: 'horizontal',
          labelCol: 3,
          width: '100%',
          tableId: '',
        },
      },
      previewForm: {},
      configTab: 'widget',
      widgetFormSelect: null,
      previewVisible: false,
      jsonVisible: false,
      codeVisible: false,
      uploadVisible: false,
      remoteFuncs: {
        func_test(resolve) {
          const options = [
            { value: 'value1', label: 'label1' },
            { value: 'value2', label: 'label3' },
            { value: 'value3', label: 'label3' },
          ]
          resolve(options)
        },
        funcGetToken(resolve) {
          axios.get('http://tools-server.making.link/api/uptoken').then((res) => {
            resolve(res.uptoken)
          })
        },
        upload_callback(response, file, fileList) {
          console.log('callback', response, file, fileList)
        }
      },
      widgetModels: {},
      blank: '',
      htmlTemplate: '',
      jsonTemplate: '',
      vueTemplate: '',
      uploadEditor: null,
      jsonCopyValue: '',
      jsonClipboard: null,
      jsonEg: `{
        "list": [],
        "config": {
          "labelWidth": 100,
          "labelPosition": "right",
          "size": "small",
          "customClass": "",
          "ui": "element",
          "layout": "horizontal",
          "labelCol": 3,
          "width": "100%"
        }
      }`,
      codeCopyValue: '',
      codeClipboard: null,
      codeActiveName: 'vue',
      undo: false,
      redo: false,
    }
  },
  created() {
    this.Containerconlog()
    this._loadComponents()
  },
  mounted() {
    const that = this
    historyManager.clear().then(() => {
      EventBus.$on('on-history-add', () => {
        historyManager
          .add(this.widgetForm, this.widgetFormSelect && this.widgetFormSelect.key ? this.widgetFormSelect.key : '')
          .then(() => {
            that.undo = true
            that.redo = false
          })
      })
    })

    //modal打开时初始化
    this.setJSON(this.importFormData)
  },
  methods: {
    Containerconlog() {
      // console.log('Container界面打印this.widgetForm',this.widgetForm)
      // console.log('Container界面打印this.widgetFormSelect',this.widgetFormSelect)
    },
    handleGoGithub() {
      window.location.href = 'https://github.com/GavinZhuLei/vue-form-making'
    },
    handleConfigSelect(value) {
      this.configTab = value
    },
    handleMoveEnd(evt) {
      // console.log('end', evt)
    },
    handleMoveStart({ oldIndex }) {
      // console.log('start', oldIndex, this.basicComponents)
    },
    handleMove() {
      return true
    },
    handleSubmit() {
      let params = {}
      params.id = this.$store.state.chooseTablefromId
      params.content = JSON.stringify(this.widgetForm)
      changeScreen(params).then((res) => {
        if (res.status === 200) {
          this.$message.success('表单json数据保存成功')
        } else {
          this.$message.error('保存表单数据失败！')
        }
      }).catch((err) => {
        this.$message.error('保存操作异常终止！')
      })
    },
    handleClose() {
      this.handleClear()
      this.$emit('handleCancel')
      this.$store.commit('changeTableCols', [])
      this.$store.commit('changeFormMakingJson', [])
    },
    handlePreview() {
      this.previewForm = _.cloneDeep(this.widgetForm)
      this.previewVisible = true
    },
    handleReset() {
      const $form = this.previewForm.config.ui === 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form.reset()
    },
    handleTest() {
      this.comdataflag = true/* 此语句由于将预览界面里面的获取数据按钮直接改为提交数据库不再和生成json按钮公用，故标志无用，留作参考以防需求更改 */
      const $form = this.previewForm.config.ui === 'element' ? this.$refs.generateForm : this.$refs.generateAntForm
      $form.getData().then((data) => {
        this.jsonVisible = false
        this.jsonTemplate = data
        this.$nextTick(() => {
          if (!this.jsonClipboard) {
            this.jsonClipboard = new Clipboard('.json-btn')
            this.jsonClipboard.on('success', (e) => {
              // this.$message.success(this.$t('fm.message.copySuccess'))
              this.$message.success('复制成功')
            })
          }
          this.jsonCopyValue = JSON.stringify(data)
        })
        this.commitToDatabase(this.jsonTemplate)
        this.$refs.widgetPreview.end()
      }).catch((e) => {
        this.$message.error(e)
        this.$refs.widgetPreview.end()
      })
    },
    // 递归获取keys
    getKeys(adKeysArray, obj) {
      // for (var key in obj) {
      //   if (typeof obj[key] != "object"){
      //       adKeysArray.push(key);
      //   }else {
      //       getKeys(obj[key],adKeysArray);
      //   }
      // }
      // return adKeysArray;

      // let keyList = [];
      // for(var k = 0; k < obj.list.length; k++){
      //   if(obj.list[k].type === 'grid'){
      //     // 暂时只针对栅格布局的嵌套
      //     for(var j = 0; j < obj.list[k].columns.length; j++){
      //       this.getKeys(obj.list[k].columns[j])
      //     }
      //   }else if(obj.list[k].type === 'table'){
      //     // 说明有子表单
      //     for(var i = 0; i < obj.list[k].tableColumns.length; i++){
      //       if (this.data.key == obj.list[k].tableColumns[i].key) {
      //         this.getSecondField(obj.list[k].schedule == undefined?"":obj.list[k].schedule)
      //         return
      //       }
      //     }
      //   }else{
      //     // 是单独的，没有子表单
      //     for(let t = 0; t<commitdata.length;t++){
      //       keyList.push(Object.keys(commitdata))
      //     }
      //   }
      // }
    },
    findKeyRecu(model, key, parent) {
      let tableCol = "";
      let recuFlag = false;
      parent = parent ? parent : ""
      // 递归查找
      const testRecu = function (obj) {
        if (recuFlag) return
        for (let k = 0; k < obj.list.length; k++) {
          if (obj.list[k].type === 'grid') {
            // 暂时只针对栅格布局的嵌套
            for (let j = 0; j < obj.list[k].columns.length; j++) {
              testRecu(obj.list[k].columns[j])
            }
          } else if (obj.list[k].type === 'table') {
            // 说明有子表单
            if (key === "schedule" && obj.list[k].model === model) {
              // 找到了子表单的schedule
              tableCol = obj.list[k][key]
              recuFlag = true
              return
            } else if (parent === obj.list[k].model) {
              // 这个组件在这个子表单里面，需要遍历该子表单
              for (var i = 0; i < obj.list[k].tableColumns.length; i++) {
                if (key === "tableCol" && obj.list[k].tableColumns[i].model === model) {
                  // 找到了子表单里面组件的tableCol
                  tableCol = obj.list[k].tableColumns[i][key]
                  recuFlag = true
                  return
                }
              }
            }
          } else {
            if (recuFlag) return
            // 在绑定主表字段的组件中找到了这个tableCol
            if (key === "tableCol" && obj.list[k].model === model) {
              tableCol = obj.list[k][key]
              recuFlag = true
              return
            }
          }
        }
      };
      testRecu(this.widgetForm);
      return tableCol
    },
    commitToDatabase(commitdata) {
      let tableCol;
      // 判断是否有未填的值
      const emptyFlag = this.isEmpty(commitdata);
      if (emptyFlag) return
      const vm = this;
      // 复制一个副本用于替换key操作，最后的结果就是tempCommitdata，原始的commitdata单纯用来遍历
      const tempCommitdata = JSON.parse(JSON.stringify(commitdata));
      // commitdata格式：一个大对象，每个键值对的键是组件/子表单的model,
      // 每个键值对的值：一个表单为一个list；如果直接绑定主表的字段（不管是否在栅格布局里面）都是一个字符串
      // 子表单需要取到schedule  组件需要取到tableCol，都是根据model取
      for (const key in commitdata) {
        if (typeof commitdata[key] === "object") {
          // 说明是有子表单的，子表单可能在栅格内，也可能在最外层
          // 替换子表单的表名
          const schedule = vm.findKeyRecu(key, "tableCol");
          tempCommitdata[schedule] = JSON.parse(JSON.stringify(commitdata[key]))
          delete tempCommitdata[key]
          // 遍历子表单的每一行
          for (let j = 0; j < commitdata[key].length; j++) {
            // 遍历子表单的一行的每一项
            if (typeof commitdata[key][j] === "object") {
              for (const key1 in commitdata[key][j]) {
                // 这里传schedule是为了解决不同子表单绑定同一个附表的问题
                tableCol = vm.findKeyRecu(key1, "tableCol", key);
                tempCommitdata[schedule][j][tableCol] = commitdata[key][j][key1]
                delete tempCommitdata[schedule][j][key1]
              }
            }
          }
        } else {
          // 说明是直接绑定的主表的字段，没有在子表单内，可能在栅格内
          // 直接调用递归查找key对应的tableCol
          tableCol = vm.findKeyRecu(key, "tableCol");
          // 以下两行功能：将model换成tableCol
          tempCommitdata[tableCol] = commitdata[key]
          delete tempCommitdata[key]
        }
      }
      // 传给后台
      vm.commitAxios(tempCommitdata)
    },
    isEmpty(tempCommitdata) {
      // 判断是否有未填的值
      let dataisnull = false;
      const getKeys = function (obj) {
        for (var key in obj) {
          if (typeof obj[key] != "object") {
            if (obj[key] == '') {
              dataisnull = true;
            }
          } else {
            getKeys(obj[key]);
          }
        }
        return;
      };
      getKeys(tempCommitdata);
      // 为空时提示
      if (dataisnull) {
        this.$message.warning('有数据为空。请检查补填！')
      }
      return dataisnull
    },
    commitAxios(tempCommitdata) {
      let name = this.widgetForm.config.dataBase;
      axios({
        url: `/online/cgform/api/crazyForm/${name}`,
        method: 'post',
        data: JSON.stringify(tempCommitdata),
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then((res) => {
        if (res.success) {
          this.$message.success('数据成功提交数据库!')
          this.previewVisible = false/* 提交数据后关闭预览弹窗 */
          /*数据为空也可以添加成功，需要判断，增加一个退出按钮，退出预览界面*/
        } else {
          this.$message.error('数据提交失败！！！！')
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    commitToDatabaseOld(commitdata) {
      // 递归获取所有的keys存入adKeysArray
      let adKeysArray = [];
      // 同时判断是否有未填的值
      let dataisnull = false;
      // act_adKeysArray存输入值的控件model。如input控件有输入值，则存其model，table控件没有输入值，则不存
      // act_adKeysArray用于在接下来的遍历中，不再遍历无输入值的控件
      let act_adKeysArray = [];

      const getKeys = function (obj) {
        for (var key in obj) {
          if (key.indexOf('_') != -1) {// 过滤为纯数字的键名, 一般键名形如input_123456
            adKeysArray.push(key);
          }
          if (typeof obj[key] != "object") {
            act_adKeysArray.push(key)
            if (obj[key] == '') {
              dataisnull = true;
            }
          } else {
            getKeys(obj[key]);
          }
        }
        return adKeysArray;
      };
      getKeys(commitdata);
      // 为空时提示
      if (dataisnull) {
        this.$message.warning('有数据为空。请检查补填！')
        return false
      }
      // 递归获取要传给后台的数据，由主表名、附表名、字段名组成
      let name = this.widgetForm.config.dataBase;
      let datajson = {};
      // 构造提交给后台的格式化数据
      // 参数：obj为要遍历的json对象，datajson为最后得到的结果（同样是json对象）
      const getFormData = function (obj, datajson) {
        let len = obj.list ? obj.list.length : obj.length;
        let opObj = obj.list ? obj.list : obj;
        let currentObj = {};

        for (let i = 0; i < len; i++) {
          // 判断非直接存储类型
          if (opObj[i].type === "table") {
            // 检测到附表时，创建空数组
            datajson[opObj[i].schedule] = [];
            // 将tableColumns递归遍历
            getFormData(opObj[i].tableColumns, datajson[opObj[i].schedule]);
          } else {
            // 先从commitdata取到填写的值currentVal
            // 再从每一次进入递归时的opObj中获取到tableCol，此时需要使用控件的model值（已存到act_adKeysArray中）去检索
            let currentVal = '';
            let currentKey = opObj[i].model;

            currentObj = {};
            try {
              for (let t = 0; t < act_adKeysArray.length; t++) {
                const getValByKey = function (obj, cKey) {
                  for (const key in obj) {
                    if (typeof obj[key] != "object") { //存在且为值
                      if (key === cKey) {
                        currentVal = obj[key];
                      }
                    } else {
                      for (let index = 0; index < obj[key].length; index++) {
                        getValByKey(obj[key][index], cKey);
                      }
                    }
                  }
                };
                getValByKey(commitdata, act_adKeysArray[t]);
                if (currentVal !== '') {
                  currentKey = act_adKeysArray[t];
                  currentObj[opObj[opObj.findIndex(item => item.model === currentKey)].tableCol] = currentVal;
                }
              }
            } catch (error) {
              currentObj = {};
            }
            if (JSON.stringify(currentObj) !== '{}') {
              datajson.splice(1, 0, currentObj);
            } else {
              datajson[opObj[opObj.findIndex(item => item.model === currentKey)].tableCol] = currentVal;
            }
          }
        }
        return datajson;
      };
      getFormData(this.widgetForm, datajson);
    },
    handleGenerateJson() {
      this.jsonVisible = true
      this.comdataflag = false
      this.jsonTemplate = this.widgetForm
      this.$nextTick(() => {
        if (!this.jsonClipboard) {
          this.jsonClipboard = new Clipboard('.json-btn')
          this.jsonClipboard.on('success', (e) => {
            // this.$message.success(this.$t('fm.message.copySuccess'))
            this.$message.success('复制成功')
          })
        }
        this.jsonCopyValue = JSON.stringify(this.widgetForm)
      })
    },
    handleGenerateCode() {
      this.codeVisible = true
      this.htmlTemplate = generateCode(JSON.stringify(this.widgetForm), 'html', this.widgetForm.config.ui)
      this.vueTemplate = generateCode(JSON.stringify(this.widgetForm), 'vue', this.widgetForm.config.ui)
      this.$nextTick(() => {
        if (!this.codeClipboard) {
          this.codeClipboard = new Clipboard('.code-btn')
          this.codeClipboard.on('success', (e) => {
            // this.$message.success(this.$t('fm.message.copySuccess'))
            this.$message.success('复制成功')
          })
        }
        this.codeCopyValue = this.codeActiveName === 'vue' ? this.vueTemplate : this.htmlTemplate
      })
    },
    handleUpload() {
      this.uploadVisible = true
    },
    handleUploadJson() {
      try {
        this.setJSON(this.jsonEg)
        this.uploadVisible = false
      } catch (e) {
        this.$refs.uploadJson.end()
      }
    },
    handleClear() {
      this.widgetForm = {
        ...this.widgetForm,
        list: [],
      }

      this.widgetFormSelect = {}
    },
    clear() {
      this.handleClear()
    },
    getJSON() {
      return this.widgetForm
    },
    getHtml() {
      return generateCode(JSON.stringify(this.widgetForm))
    },
    setJSON(json) {
      if (!json) {
        this.handleClear()
        return
      }
      if (typeof json === 'string') {
        if (json === '') {
          this.handleClear()
          return
        }
        json = JSON.parse(json)
      }
      this.widgetForm = json
      if (json.list.length > 0) {
        this.widgetFormSelect = json.list[0]
      }

      this.$nextTick(() => {
        EventBus.$emit('on-history-add')
      })
    },
    handleInput(val) {
      this.blank = val
    },
    handleField(item) {
      EventBus.$emit('on-field-add', item)
    },
    handleUndo() {
      historyManager
        .updateLatest(
          this.widgetForm,
          this.widgetFormSelect && this.widgetFormSelect.key ? this.widgetFormSelect.key : ''
        )
        .then(() => {
          historyManager.undo().then((data) => {
            this.widgetForm = { ...data.data }
            this.widgetFormSelect = this._findWidgetItem(this.widgetForm.list, data.key)
            this.undo = data.undo
            this.redo = data.redo
          })
        })
    },
    handleRedo() {
      historyManager.redo().then((data) => {
        this.widgetForm = { ...data.data }
        this.widgetFormSelect = this._findWidgetItem(this.widgetForm.list, data.key)

        this.undo = data.undo
        this.redo = data.redo
      })
    },
    _findWidgetItem(list, key) {
      const index = list.findIndex((item) => item.key === key)

      if (index >= 0) {
        return list[index]
      } else {
        for (let m = 0; m < list.length; m++) {
          const item = list[m]

          if (item.type === 'grid') {
            for (let i = 0; i < item.columns.length; i++) {
              return this._findWidgetItem(item.columns[i].list, key)
            }
          }
          if (item.type === 'table') {
            return this._findWidgetItem(item.tableColumns, key)
          }
          if (item.type === 'tabs') {
            for (let i = 0; i < item.tabs.length; i++) {
              return this._findWidgetItem(item.tabs[i].list, key)
            }
          }
        }

        return {}
      }
    },
    _loadComponents() {
      this.basicComponents = this.basicComponents.map((item) => {
        return {
          ...item,
          // name: this.$t(`fm.components.fields.${item.type}`),
          name:`${item.name}`,
        }
      })
      this.advanceComponents = this.advanceComponents.map((item) => {
        return {
          ...item,
          // name: this.$t(`fm.components.fields.${item.type}`),
          name: `${item.name}`,
        }
      })
      this.linechartComponents = this.linechartComponents.map((item) => {
        return {
          ...item,
          // name: this.$t(`fm.components.fields.${item.type}`),
          name: `${item.name}`,
        }
      })
      this.columnchartComponents = this.columnchartComponents.map((item) => {
        return {
          ...item,
          // name: this.$t(`fm.components.fields.${item.type}`),
          name: `${item.name}`,
        }
      })
      this.piechartComponents = this.piechartComponents.map((item) => {
        return {
          ...item,
          // name: this.$t(`fm.components.fields.${item.type}`),
          name: `${item.name}`,
        }
      })
      this.layoutComponents = this.layoutComponents.map((item) => {
        return {
          ...item,
          // name: this.$t(`fm.components.fields.${item.type}`),
          name: `${item.name}`,
        }
      })
      this.customComponents = this.customFields.map((item) => {
        return {
          ...item,
          type: 'custom',
          icon: 'icon-zidingyi',
          options: { ...item.options },
        }
      })
    },
  },
  watch: {
    $lang: function (val) {
      this._loadComponents()
    },
    codeActiveName(val) {
      this.codeCopyValue = this.codeActiveName === 'vue' ? this.vueTemplate : this.htmlTemplate
    },
    widgetForm(val) {
      this.setJSON(val)
    },
  },
}
</script>

<style lang="scss">
.widget-empty {
  background-position: 50%;
}

.custom1 .el-col {
  border: 1px solid #ccc;
  overflow: hidden;
  padding: 5px;
  margin-right: -1px;
  margin-bottom: -1px;
}
.custom .el-col {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.formakingclose {
  margin-right: 50px;
  // background-color: rgb(245, 228, 136);
}
</style>
