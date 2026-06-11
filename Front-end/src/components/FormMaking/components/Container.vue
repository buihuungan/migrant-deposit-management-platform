<template>
  <el-container class="fm2-container">
    <el-main class="fm2-main">
      <el-container>
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
              关闭
              </el-button
            >
            <!-- {{ $t('fm.actions.undo') }} -->
            <el-button type="text" :disabled="!undo" size="medium" icon="el-icon-back" @click="handleUndo">撤销</el-button>
            <!-- {{ $t('fm.actions.redo') }} -->
            <el-button type="text" :disabled="!redo" size="medium" icon="el-icon-right" @click="handleRedo">重做</el-button>
            <!-- {{ $t('fm.actions.import') }} -->
            <el-button v-if="upload" type="text" size="medium" icon="el-icon-upload2" @click="handleUpload">导入JSON</el-button>
            <!-- {{ $t('fm.actions.clear') }} -->
            <el-button v-if="clearable" type="text" size="medium" icon="el-icon-delete" @click="handleClear">清空</el-button>
            <!-- {{ $t('fm.actions.preview') }} -->
            <el-button v-if="preview" type="text" size="medium" icon="el-icon-view" @click="handlePreview">预览</el-button>
            <!-- {{ $t('fm.actions.json') }} -->
            <el-button
              v-if="generateJson"
              type="text"
              size="medium"
              icon="el-icon-tickets"
              @click="handleGenerateJson"
              >生成JSON</el-button
            >
            <!-- {{ $t('fm.actions.submit')}} -->
            <el-button size="medium" type="text" icon="el-icon-download" @click="handleSubmit">保存</el-button>
            <!-- {{ $t('fm.actions.code') }} -->
            <el-button
              v-if="generateCode"
              type="text"
              size="medium"
              icon="el-icon-document"
              @click="handleGenerateCode"
              >生成代码</el-button
            >
          </el-header>
          <el-main :class="{ 'widget-empty': widgetForm.list.length === 0 }">
            <widget-form v-if="!resetJson" ref="widgetForm" :data="widgetForm" :select.sync="widgetFormSelect" />
          </el-main>
        </el-container>

        <el-aside class="widget-config-container">
          <el-container>
            <el-header height="45px">
              <!-- {{ $t('fm.config.widget.title') }} -->
              <div class="config-tab" :class="{ active: configTab === 'widget' }" @click="handleConfigSelect('widget')">
                字段属性
              </div>
              <!-- {{ $t('fm.config.form.title') }} -->
              <div class="config-tab" :class="{ active: configTab === 'form' }" @click="handleConfigSelect('form')">
                表单属性
              </div>
            </el-header>
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
        <!-- :title="$t('fm.actions.preview')" -->
        <cus-dialog
          :visible="previewVisible"
          @on-close="closePreview()"
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
            <!-- <el-button
              type="primary"
              @click="handleTest"
            >
              {{$t('fm.actions.getData')}}
            </el-button> -->

            <el-button type="primary" class="json-btn" v-if="comdataflag" @click="handleTest">提交至数据库 </el-button>
            
            <!-- <el-button type="warning" @click="handleReset">
              {{ $t('fm.actions.reset') }}
            </el-button> -->
            <el-button type="warning" @click="handleReset">
               重置
            </el-button>

            <el-button type="info" @click="closePreview()"> 退出 </el-button>
          </template>
        </cus-dialog>
        <!-- 预览按钮弹出框结束 -->
        <!-- :title="$t('fm.actions.import')" -->
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
          <html-editor style="height: 400px;" height="400px" mode="json" v-model="jsonEg" />
        </cus-dialog>

        <!-- 预览点击获取数据按钮弹框开始 -->
        <!-- :title="$t('fm.actions.json')" -->
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
            <el-button type="primary" class="json-btn" v-if="comdataflag" @click="commitToDatabase(jsonTemplate)"
              >提交至数据库
            </el-button>
            <!-- {{ $t('fm.actions.copyData') }} -->
            <el-button type="primary" class="json-btn" :data-clipboard-text="jsonCopyValue"
              >复制
            </el-button>
          </template>
        </cus-dialog>
        <!-- 预览点击获取数据按钮弹框结束 -->
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
            <!-- {{ $t('fm.actions.copyData') }} -->
            <el-button type="primary" class="code-btn" :data-clipboard-text="codeCopyValue">复制</el-button>
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
import HtmlEditor from '../components/HtmlEditor'
import { basicComponents, layoutComponents, advanceComponents } from './componentsConfig.js'
import { EventBus } from '../util/event-bus.js'
import generateCode from './generateCode.js'
import historyManager from '../util/history-manager.js'
import _ from 'lodash'
import Vue from 'vue'
import axios from 'axios'
import api from '../../../api/index.js'
import { t_postAction } from '@/api/tempApi'
import { Message } from 'element-ui'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'

export default {
  name: 'fm-making-form',
  components: {
    Draggable,
    WidgetConfig,
    FormConfig,
    WidgetForm,
    CusDialog,
    GenerateForm,
    CodeEditor,
    AntdGenerateForm,
    HtmlEditor,
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
        'autocode',
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
      default: () => [
        'blank',
        'component',
        'fileupload',
        'imgupload',
        'editor',
        'cascader',
        'pcdcascader',
        'table',
        'table_dict',
      ],
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
        func_kyf(resolve) {
          const options = [
            { value: 'kyf', label: 'kyf' },
            { value: 'cj', label: 'cj' },
            { value: 'kl', label: 'kl' },
          ]

          resolve(options)
        },
        funcGetToken(resolve) {
          // request.get('http://tools-server.making.link/api/uptoken').then((res) => {
          //   resolve(res.uptoken)
          // })
          axios.get('http://tools-server.making.link/api/uptoken').then((res) => {
            resolve(res.uptoken)
          })
        },
        upload_callback(response, file, fileList) {
          console.log('callback', response, file, fileList)
        },
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
    // test
    this.Containerconlog()

    this._loadComponents()
  },
  mounted() {
    const that = this

    historyManager.clear().then(() => {
      EventBus.$on('on-history-add', () => {
        console.log('xxx', this.widgetFormSelect)
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
    // this.loadDataBase();
  },
  methods: {
    closePreview(){
      this.previewVisible = false
      this.$store.commit('cleanTableData', [])

    },
    Containerconlog() {
      console.log('Container界面打印this.widgetForm', this.widgetForm)
      console.log('Container界面打印this.widgetFormSelect', this.widgetFormSelect)
    },
    handleGoGithub() {
      window.location.href = 'https://github.com/GavinZhuLei/vue-form-making'
    },
    handleConfigSelect(value) {
      this.configTab = value
    },
    handleMoveEnd(evt) {
      console.log('end', evt)
    },
    handleMoveStart({ oldIndex }) {
      console.log('start', oldIndex, this.basicComponents)
    },
    handleMove() {
      return true
    },
    handleSubmit() {
      //保存按钮
      let qs = require('qs')
      // this.$emit('handleOk',JSON.stringify(this.widgetForm));
      console.log('所选表单的id为：', this.$store.state.chooseTablefromId)
      console.log('所选表单json数据为', JSON.stringify(this.widgetForm))
      // console.log('所选表单json数据JsonObject格式为', JSONObject.fromObject(this.widgetForm))
      // console.log('所选表单json数据最初格式为', this.widgetForm )

      axios.defaults.headers.post['Content-Type'] = 'application/json'
      t_postAction(
        `/admin/desform/updateContent?id=${this.$store.state.chooseTablefromId}`,
        JSON.stringify(this.widgetForm)
      )
        // t_postAction(
        //   `/admin/desform/updateContent`,
        //   qs.stringify({ id: this.$store.state.chooseTablefromId, content: JSON.stringify(this.widgetForm) })
        // )

        .then((res) => {
          console.log('执行保存请求')
          console.log(res)
          console.log('所选表单的id为：' + this.$store.state.chooseTablefromId)
          if (res.success == true) {
            console.log('保存表单数据成功！')
            console.log(res.message)

            Message({
              showClose: true,
              message: '表单json数据保存成功',
              type: 'success',
            })
          } else {
            console.log('保存表单数据失败！')
            Message({
              showClose: true,
              message: '保存表单数据失败！',
              type: 'error',
            })
          }
        })
        .catch((err) => {
          console.log('保存操作异常终止！')
          Message({
            showClose: true,
            message: '保存操作异常终止！',
            type: 'error',
          })
        })
      // parent.location.reload();
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
      const $form = this.previewForm.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm

      $form.reset()
    },
    handleTest() {
      this.comdataflag = true /* 此语句由于将预览界面里面的获取数据按钮直接改为提交数据库不再和生成json按钮共用，故标志无用，留作参考以防需求更改 */
      const $form = this.previewForm.config.ui == 'element' ? this.$refs.generateForm : this.$refs.generateAntForm

      $form
        .getData()
        .then((data) => {
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
        })
        .catch((e) => {
          this.$message.error(e)
          this.$refs.widgetPreview.end()
        })
    }, 
    getKeys(adKeysArray, obj) { // 递归获取keys
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
      var tableCol = ''
      var vm = this
      var recuFlag = false
      parent = parent ? parent : ''
      // 递归查找
      var testRecu = function (obj) {
        if (recuFlag) return
        for (var k = 0; k < obj.list.length; k++) {
          if (obj.list[k].type === 'grid') {
            // 暂时只针对栅格布局的嵌套
            for (var j = 0; j < obj.list[k].columns.length; j++) {
              testRecu(obj.list[k].columns[j])
            }
          } else if (obj.list[k].type === 'table') {
            // 说明有子表单
            if (key == 'schedule' && obj.list[k].model == model) {
              // 找到了子表单的schedule
              tableCol = obj.list[k][key]
              recuFlag = true
              return
            } else if (parent === obj.list[k].model) {
              // 这个组件在这个子表单里面，需要遍历该子表单
              for (var i = 0; i < obj.list[k].tableColumns.length; i++) {
                if (key == 'tableCol' && obj.list[k].tableColumns[i].model == model) {
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
            if (key == 'tableCol' && obj.list[k].model == model) {
              tableCol = obj.list[k][key]
              recuFlag = true
              return
            }
          }
        }
      }
      testRecu(this.widgetForm)
      return tableCol
    },
    commitToDatabase(commitdata) {
      // 判断是否有未填的值
      var emptyFlag = this.isEmpty(commitdata)
      if (emptyFlag) return
      var vm = this
      // 复制一个副本用于替换key操作，最后的结果就是tempCommitdata，原始的commitdata单纯用来遍历
      var tempCommitdata = JSON.parse(JSON.stringify(commitdata))
      // commitdata格式：一个大对象，每个键值对的键是组件/子表单的model,
      // 每个键值对的值：一个表单为一个list；如果直接绑定主表的字段（不管是否在栅格布局里面）都是一个字符串
      // 子表单需要取到schedule  组件需要取到tableCol，都是根据model取
      for (var key in commitdata) {
        if (typeof commitdata[key] === 'object') {
          // 说明是有子表单的，子表单可能在栅格内，也可能在最外层
          // 替换子表单的表名
          var schedule = vm.findKeyRecu(key, 'tableCol')
          tempCommitdata[schedule] = JSON.parse(JSON.stringify(commitdata[key]))
          delete tempCommitdata[key]
          // 遍历子表单的每一行
          for (var j = 0; j < commitdata[key].length; j++) {
            // 遍历子表单的一行的每一项
            if (typeof commitdata[key][j] === 'object') {
              for (var key1 in commitdata[key][j]) {
                // 这里传schedule是为了解决不同子表单绑定同一个附表的问题
                var tableCol = vm.findKeyRecu(key1, 'tableCol', key)
                tempCommitdata[schedule][j][tableCol] = commitdata[key][j][key1]
                delete tempCommitdata[schedule][j][key1]
              }
            }
          }
        } else {
          // 说明是直接绑定的主表的字段，没有在子表单内，可能在栅格内
          // 直接调用递归查找key对应的tableCol
          var tableCol = vm.findKeyRecu(key, 'tableCol')
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
      let dataisnull = false
      var getKeys = function (obj) {
        for (var key in obj) {
          if (typeof obj[key] != 'object') {
            if (obj[key] == '') {
              dataisnull = true
            }
          } else {
            getKeys(obj[key])
          }
        }
        return
      }
      getKeys(tempCommitdata)
      // 为空时提示
      if (dataisnull) {
        Message({
          showClose: true,
          message: '有数据为空。请检查补填！',
          type: 'warning',
        })
      }
      return dataisnull
    },
    commitAxios(tempCommitdata) {
      let name = this.widgetForm.config.dataBase
      axios.defaults.headers.post['X-Access-Token'] = Vue.ls.get(ACCESS_TOKEN)
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      axios
        .post(
          api.server_url + api.global_online_baseURL + `/cgform/api/crazyForm/${name}`,
          JSON.stringify(tempCommitdata)
        )
        .then((res) => {
          // o_postAction(api.global_online_baseURL+`/cgform/api/crazyForm/${name}`,JSON.stringify(datajson)).then((res)=>{
          if (res.data.success) {
            Message({
              showClose: true,
              message: '数据成功提交数据库!',
              type: 'success',
            })
            this.previewVisible = false /* 提交数据后关闭预览弹窗 */

            /*
        数据为空也可以添加成功，需要判断，
        增加一个退出按钮，退出预览界面
        */
          } else {
            Message({
              showClose: true,
              message: '数据提交失败！！！！',
              type: 'error',
            })
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    commitToDatabaseOld(commitdata) {
      console.log('提交的数据为', commitdata)

      // 递归获取所有的keys存入adKeysArray
      let adKeysArray = []
      // 同时判断是否有未填的值
      let dataisnull = false
      // act_adKeysArray存输入值的控件model。如input控件有输入值，则存其model，table控件没有输入值，则不存
      // act_adKeysArray用于在接下来的遍历中，不再遍历无输入值的控件
      let act_adKeysArray = []

      var getKeys = function (obj) {
        for (var key in obj) {
          if (key.indexOf('_') != -1) {
            // 过滤为纯数字的键名, 一般键名形如input_123456
            adKeysArray.push(key)
          }
          if (typeof obj[key] != 'object') {
            act_adKeysArray.push(key)
            if (obj[key] == '') {
              dataisnull = true
            }
          } else {
            getKeys(obj[key])
          }
        }
        return adKeysArray
      }

      getKeys(commitdata)
      let keysLen = adKeysArray.length /* 获取数据长度 */

      // 为空时提示
      if (dataisnull) {
        Message({
          showClose: true,
          message: '有数据为空。请检查补填！',
          type: 'warning',
        })
        return false
      }

      console.log('this.widgetForm', this.widgetForm)
      console.log('commitdata', commitdata)
      console.log('adKeysArray', adKeysArray, act_adKeysArray)

      // for(let i=0;i<keysLen;i++){
      //   console.log(this.widgetForm.list.findIndex(item=>item.model===adKeysArray[i]));
      //   console.log(this.widgetForm.list[this.widgetForm.list.findIndex(item=>item.model===adKeysArray[i])]);
      //   datajson[this.widgetForm.list[this.widgetForm.list.findIndex(item=>item.model===adKeysArray[i])].tableCol]=commitdata[adKeysArray[i]];
      // }

      // 递归获取要传给后台的数据，由主表名、附表名、字段名组成

      let name = this.widgetForm.config.dataBase
      console.log('当前提交数据到数据库：', name)
      let datajson = {}

      // 构造提交给后台的格式化数据
      // 参数：obj为要遍历的json对象，datajson为最后得到的结果（同样是json对象）
      var getFormData = function (obj, datajson) {
        let len = obj.list ? obj.list.length : obj.length
        let opObj = obj.list ? obj.list : obj
        let currentObj = {}

        for (let i = 0; i < len; i++) {
          console.log('opObj[i]', opObj[i], opObj[i].type, opObj[i].model)
          // 判断非直接存储类型
          if (opObj[i].type === 'table') {
            // 检测到附表时，创建空数组
            datajson[opObj[i].schedule] = []
            // 将tableColumns递归遍历
            getFormData(opObj[i].tableColumns, datajson[opObj[i].schedule])
          } else {
            // 先从commitdata取到填写的值currentVal
            // 再从每一次进入递归时的opObj中获取到tableCol，此时需要使用控件的model值（已存到act_adKeysArray中）去检索
            let currentVal = ''
            let currentKey = opObj[i].model
            let currentCol = opObj[opObj.findIndex((item) => item.model === currentKey)].tableCol

            // if(currentObj.hasOwnProperty(currentCol)){
            //   break;
            // }

            currentObj = {}
            try {
              for (let t = 0; t < act_adKeysArray.length; t++) {
                var getValByKey = function (obj, cKey) {
                  console.log('------', obj, cKey)
                  for (var key in obj) {
                    console.log('000', typeof obj[key], key)
                    if (typeof obj[key] != 'object') {
                      //存在且为值
                      if (key == cKey) {
                        console.log('===', obj[key])
                        currentVal = obj[key]
                      }
                    } else {
                      for (let index = 0; index < obj[key].length; index++) {
                        getValByKey(obj[key][index], cKey)
                      }
                    }
                  }
                }
                getValByKey(commitdata, act_adKeysArray[t])
                console.log('currentVa222l', currentVal)
                if (currentVal != '') {
                  currentKey = act_adKeysArray[t]
                  currentObj[opObj[opObj.findIndex((item) => item.model === currentKey)].tableCol] = currentVal
                }
              }
            } catch (error) {
              currentObj = {}
            }

            console.log('current', currentVal, currentKey, currentObj)
            if (JSON.stringify(currentObj) !== '{}') {
              datajson.splice(1, 0, currentObj)
            } else {
              datajson[opObj[opObj.findIndex((item) => item.model === currentKey)].tableCol] = currentVal
            }
          }
        }
        return datajson
      }
      getFormData(this.widgetForm, datajson)
      console.log('datajson', datajson)

      // console.log("处理后即将存入数据库数据json为：",JSON.stringify(datajson))

      // axios.defaults.headers.post['X-Access-Token']=Vue.ls.get(ACCESS_TOKEN)
      // axios.defaults.headers.post['Content-Type']="application/json"
      // axios.post(api.global_online_baseURL+`/cgform/api/crazyForm/${name}`,JSON.stringify(datajson)).then((res)=>{
      //   // o_postAction(api.global_online_baseURL+`/cgform/api/crazyForm/${name}`,JSON.stringify(datajson)).then((res)=>{

      //   if (res.data.success){
      //     Message({
      //     showClose: true,
      //     message: '数据成功提交数据库!',
      //     type: 'success',
      //   })
      //   //   that.$message({
      //   //   message: '数据成功提交数据库！',
      //   //   type: 'success'
      //   // })
      //   this.previewVisible=false/* 提交数据后关闭预览弹窗 */

      //   /*
      //   数据为空也可以添加成功，需要判断，
      //   增加一个退出按钮，退出预览界面
      //   */
      //   } else {
      //     Message({
      //     showClose: true,
      //     message: '数据提交失败！！！！',
      //     type: 'error',
      //   })
      //   //   that.$message({
      //   //   message: '数据提交失败！',
      //   //   type: 'error'
      //   // })
      //   }
      // }).catch((err)=>{
      //   console.log(err)
      // })
    },
    handleGenerateJson() {
      this.jsonVisible = true
      this.comdataflag = false
      this.jsonTemplate = this.widgetForm
      console.log(JSON.stringify(this.widgetForm))
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
        this.codeCopyValue = this.codeActiveName == 'vue' ? this.vueTemplate : this.htmlTemplate
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
        // this.$message.error(e.message)
        this.$refs.uploadJson.end()
      }
    },
    handleClear() {
      this.widgetForm = {
        ...this.widgetForm,
        list: [],
      }

      this.widgetFormSelect = {}

      // this.$nextTick(() => {
      //   EventBus.$emit('on-history-add')
      // })
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
      console.log(val)
      this.blank = val
    },
    handleField(item) {
      console.log(item)
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
      const index = list.findIndex((item) => item.key == key)

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
          name: `${item.name}`,
        }
      })
      this.advanceComponents = this.advanceComponents.map((item) => {
        return {
          ...item,
          // name: this.$t(`fm.components.fields.${item.type}`),
          name: `${item.name}`
        }
      })
      this.layoutComponents = this.layoutComponents.map((item) => {
        return {
          ...item,
          // name: this.$t(`fm.components.fields.${item.type}`),
          name: `${item.name}`
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
      this.codeCopyValue = this.codeActiveName == 'vue' ? this.vueTemplate : this.htmlTemplate
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
  /* // margin-right:-1px;   */
  /* // margin-bottom:-1px;  */
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
