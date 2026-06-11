<template>
  <div class="onlineform">
    <!-- onlCgformModal.vue -->
    <a-modal
      :title="title"
      :visible.sync="visible"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="handleCancel"
      width="800"
      class="table55"
    >
      <div style="display: block">
        <div class="forspinc" v-if="spining == true">
          <a-spin tip="正在同步中" size="large" style="margin-top: 450px" v-if="spining == true"></a-spin>
        </div>
        <a-form style="width: 100%; height: 100%" layout="inline" :form="form">
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="表名" style="display: inline">
                <a-input
                  v-bind:disabled="(tableName = diasabledInput)"
                  placeholder="请输入表名"
                  v-decorator="['tableName', { rules: [{ required: true, message: '请输入表名!' }] }]"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="表描述" style="display: inline">
                <a-input
                  placeholder="请输入表描述"
                  v-decorator="['tableTxt', { rules: [{ required: true, message: '请输入表说明!' }] }]"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="表单分类" style="display: inline">
                <a-select placeholder="请选择表单分类" v-model="formCategory">
                  <a-select-option value="identity">标识模板</a-select-option>
                  <a-select-option value="materiel">物料</a-select-option>
                  <!-- <a-select-option value="bpm">流程表单</a-select-option>
                  <a-select-option value="temp">测试表单</a-select-option>
                  <a-select-option value="bdfl_include">导入表单</a-select-option> -->
                </a-select>
              </a-form-item>
              <!-- <a-form-item label="表类型">
                <a-select  placeholder="请选择表类型" v-model="tableType" @select="change(tableType)"  ref="opt"
                :getPopupContainer="triggerNode => triggerNode.parentNode" >
                  <a-select-option :value="1">单表</a-select-option>
                  <a-select-option :value="2">主表</a-select-option>
                  <a-select-option :value="3">附表</a-select-option>
                </a-select>
              </a-form-item> -->
            </a-col>
          </a-row>
          <!-- 产品的标识编码 前部分 -->
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item class="detailItem" label="标识前缀">
                <a-select v-model="iform.identityIndex">
                  <a-select-option v-for="(item, index) in identityIndexList" :key="index" :value="item">
                    {{ item }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <!-- <a-col :span="8">
              <a-form-item label="物料号" style="display: inline">
                <a-input v-model="iform.identityData" placeholder="产品标识"></a-input>
              </a-form-item>
            </a-col> -->
            <!-- <a-col :span="8">
              <a-form-item label="类别" style="display: inline">
                <a-input v-model="iform.category" placeholder="类别"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="型号" style="display: inline">
                <a-input v-model="iform.modelIdentity" placeholder="请输入产品型号"></a-input>
              </a-form-item>
            </a-col> -->
          </a-row>
          <!-- <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="单位" style="display: inline">
                <a-input v-model="iform.unitIdentity" placeholder="请输入产品单位"></a-input>
              </a-form-item>
            </a-col>
          </a-row> -->
          <a-divider orientation="left"></a-divider>
        </a-form>
        <a-tabs v-model="activeKey" style="margin-top: 15px" @change="tabchange()">
          <a-tab-pane key="1" tab="数据库属性" :forceRender="true">
            <j-editable-tablec
              ref="editableTable"
              :tempNumber.sync="tempNumber"
              :loading="loading"
              :columns="columns"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="true"
              :actionButton="true"
              :dragSort="false"
              style="margin-top: 8px"
              :disabledRows="dataSource[0]"
              @added="added"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-tablec>
          </a-tab-pane>
          <a-tab-pane key="2" tab="页面属性" :forceRender="true" disabled>
            <j-editable-tablec
              ref="editableTable1"
              :tempNumber.sync="tempNumber"
              :loading="loading"
              :columns="columns1"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false"
              :disabledRows="dataSource[0]"
              style="margin-top: 8px"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-tablec>
          </a-tab-pane>
          <a-tab-pane key="3" tab="校验字段" :forceRender="true" disabled>
            <j-editable-tablec
              ref="editableTable2"
              :tempNumber.sync="tempNumber"
              :loading="loading"
              :columns="columns2"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false"
              :disabledRows="dataSource[0]"
              style="margin-top: 8px"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-tablec>
          </a-tab-pane>
          <a-tab-pane key="4" tab="外键" :forceRender="true" disabled>
            <j-editable-tablec
              ref="editableTable3"
              :tempNumber.sync="tempNumber"
              :loading="loading"
              :columns="columns3"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false"
              :disabledRows="dataSource[0]"
              style="margin-top: 8px"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-tablec>
          </a-tab-pane>
          <a-tab-pane key="5" tab="索引" :forceRender="true" disabled>
            <j-editable-tablec
              ref="editableTable4"
              :tempNumber.sync="tempNumber"
              :loading="loading"
              :columns="columns4"
              :dataSource="dataSource1"
              :rowNumber="true"
              :rowSelection="true"
              :actionButton="true"
              :dragSort="true"
              style="margin-top: 8px"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-tablec>
          </a-tab-pane>
          <a-tab-pane key="6" tab="查询配置" :forceRender="true" disabled>
            <j-editable-tablec
              ref="editableTable5"
              :tempNumber.sync="tempNumber"
              :loading="loading"
              :columns="columns5"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false"
              :disabledRows="dataSource[0]"
              style="margin-top: 8px"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-tablec>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { tm_deleteAction, tm_postAction, tm_getAction, tm_fakepostAction } from '@/api/templateApi'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { im_postAction, im_deleteAction, im_getAction, im_putAction, im_downFile } from '@/api/icimApi'
import JEditableTablec from './modules/JEditableTable1'
import { FormTypes } from '@/utils/JEditableTableUtil'
import pick from 'lodash.pick'
import axios from 'axios'
import { getFileAccessHttpUrl } from '../../api/manage'
import { Message } from 'element-ui'
export default {
  name: 'onlCgformModal',
  components: { JEditableTablec },
  data() {
    return {
      //result_code 0未通过校验   1通过校验
      result_code: 0,
      tempNumber: 0,
      disabledData: [],
      spining: false,
      // destroyOnClose:true,
      diasabledInput: false,
      activeKey: '1',
      loading: false,
      addoreditflag: 0,
      form: this.$form.createForm(this),
      visible: false,
      length: 0,
      fields: [],
      index: [],
      isshow: false,
      isShow: false,
      list: [],
      result: [],
      data: {},
      title: '',
      id: '',
      //前缀
      identityIndexList: ['88.175.111'],
      iform: {
        identityIndex: '88.175.111',
        identityData: '',
        category: '',
        modelIdentity: '',
        unitIdentity: '',
      },
      //存放编辑模板表的标识
      modelData: '',
      identity: '',
      dataSource: [
        {
          dbFieldName: 'id',
          dbFieldTxt: '主键',
          dbLength: '36',
          dbPointLength: '0',
          dbDefaultVal: '',
          dbType: 'String',
          dbIsKey: 1,
          dbIsNull: 0,
          isShowForm: 0,
          isShowList: 0,
          sortFlag: 0,
          isReadOnly: 1,
          fieldShowType: 'text',
          fieldLength: 120,
          isQuery: 0,
          queryMode: 'single',
          fieldDefaultValue: '',
          fieldExtendJson: '',
          converter: '',
          fieldHref: '',
          fieldValidType: '',
          fieldMustInput: '',
          dictTable: '',
          dictField: '',
          dictText: '',
          mainTable: '',
          mainField: '',
          queryShowType: '',
          queryDictTable: '',
          queryDictField: '',
          queryDictText: '',
          queryDefVal: '',
          queryConfigFlag: '',
        },
        {
          dbFieldName: 'identity',
          dbFieldTxt: '标识编码',
          dbLength: '256',
          dbPointLength: '0',
          dbDefaultVal: '',
          dbType: 'String',
          dbIsKey: 0,
          dbIsNull: 1,
          isShowForm: 1,
          isShowList: 1,
          sortFlag: 0,
          isReadOnly: 0,
          fieldShowType: 'text',
          fieldLength: 120,
          isQuery: 0,
          queryMode: 'single',
          fieldDefaultValue: '',
          fieldExtendJson: '',
          converter: '',
          fieldHref: '',
          fieldValidType: '',
          fieldMustInput: '',
          dictTable: '',
          dictField: '',
          dictText: '',
          mainTable: '',
          mainField: '',
          queryShowType: '',
          queryDictTable: '',
          queryDictField: '',
          queryDictText: '',
          queryDefVal: '',
          queryConfigFlag: '1',
        },
        {
          dbFieldName: 'create_time',
          dbFieldTxt: '创建日期',
          dbLength: '20',
          dbPointLength: '0',
          dbDefaultVal: '',
          dbType: 'Date',
          dbIsKey: 0,
          dbIsNull: 1,
          isShowForm: 0,
          isShowList: 1,
          sortFlag: 0,
          isReadOnly: 0,
          fieldShowType: 'datetime',
          fieldLength: 120,
          isQuery: 0,
          queryMode: 'single',
          fieldDefaultValue: '',
          fieldExtendJson: '',
          converter: '',
          fieldHref: '',
          fieldValidType: '',
          fieldMustInput: '',
          dictTable: '',
          dictField: '',
          dictText: '',
          mainTable: '',
          mainField: '',
          queryShowType: '',
          queryDictTable: '',
          queryDictField: '',
          queryDictText: '',
          queryDefVal: '',
          queryConfigFlag: '',
        },
        {
          dbFieldName: 'update_time',
          dbFieldTxt: '更新日期',
          dbLength: '20',
          dbPointLength: '0',
          dbDefaultVal: '',
          dbType: 'Date',
          dbIsKey: 0,
          dbIsNull: 1,
          isShowForm: 0,
          isShowList: 1,
          sortFlag: 0,
          isReadOnly: 0,
          fieldShowType: 'datetime',
          fieldLength: 120,
          isQuery: 0,
          queryMode: 'single',
          fieldDefaultValue: '',
          fieldExtendJson: '',
          converter: '',
          fieldHref: '',
          fieldValidType: '',
          fieldMustInput: '',
          dictTable: '',
          dictField: '',
          dictText: '',
          mainTable: '',
          mainField: '',
          queryShowType: '',
          queryDictTable: '',
          queryDictField: '',
          queryDictText: '',
          queryDefVal: '',
          queryConfigFlag: '',
        },
        {
          dbFieldName: 'power',
          dbFieldTxt: '功率大小',
          dbLength: '64',
          dbPointLength: '0',
          dbDefaultVal: '',
          dbType: 'String',
          dbIsKey: 0,
          dbIsNull: 1,
          isShowForm: 1,
          isShowList: 1,
          sortFlag: 0,
          isReadOnly: 0,
          fieldShowType: 'text',
          fieldLength: 120,
          isQuery: 0,
          queryMode: 'single',
          fieldDefaultValue: '',
          fieldExtendJson: '',
          converter: '',
          fieldHref: '',
          fieldValidType: '',
          fieldMustInput: '',
          dictTable: '',
          dictField: '',
          dictText: '',
          mainTable: '',
          mainField: '',
          queryShowType: '',
          queryDictTable: '',
          queryDictField: '',
          queryDictText: '',
          queryDefVal: '',
          queryConfigFlag: '1',
        },
        {
          dbFieldName: 'shape',
          dbFieldTxt: '形状',
          dbLength: '64',
          dbPointLength: '0',
          dbDefaultVal: '',
          dbType: 'String',
          dbIsKey: 0,
          dbIsNull: 1,
          isShowForm: 1,
          isShowList: 1,
          sortFlag: 0,
          isReadOnly: 0,
          fieldShowType: 'text',
          fieldLength: 120,
          isQuery: 0,
          queryMode: 'single',
          fieldDefaultValue: '',
          fieldExtendJson: '',
          converter: '',
          fieldHref: '',
          fieldValidType: '',
          fieldMustInput: '',
          dictTable: '',
          dictField: '',
          dictText: '',
          mainTable: '',
          mainField: '',
          queryShowType: '',
          queryDictTable: '',
          queryDictField: '',
          queryDictText: '',
          queryDefVal: '',
          queryConfigFlag: '1',
        },
        {
          dbFieldName: 'color',
          dbFieldTxt: '颜色',
          dbLength: '64',
          dbPointLength: '0',
          dbDefaultVal: '',
          dbType: 'String',
          dbIsKey: 0,
          dbIsNull: 1,
          isShowForm: 1,
          isShowList: 1,
          sortFlag: 0,
          isReadOnly: 0,
          fieldShowType: 'text',
          fieldLength: 120,
          isQuery: 0,
          queryMode: 'single',
          fieldDefaultValue: '',
          fieldExtendJson: '',
          converter: '',
          fieldHref: '',
          fieldValidType: '',
          fieldMustInput: '',
          dictTable: '',
          dictField: '',
          dictText: '',
          mainTable: '',
          mainField: '',
          queryShowType: '',
          queryDictTable: '',
          queryDictField: '',
          queryDictText: '',
          queryDefVal: '',
          queryConfigFlag: '1',
        },
      ],
      dataSource1: [
        {
          indexName: 'identity',
          indexField: 'identity',
          indexType: 'unique',
        },
      ],
      tableType: 1,
      relationType: 1,
      formCategory: 'identity',
      idType: 'UUID',
      queryMode: 'single',
      themeTemplate: 'normal',
      formTemplate: '1',
      scroll: 1,
      isCheckbox: 'Y',
      isPage: 'Y',
      isTree: 'N',
      columns: [
        {
          title: '字段名称',
          align: 'center',
          dataIndex: 'dbFieldName',
          key: 'dbFieldName',
          type: FormTypes.input,
          defaultValue: '',
          validateRules: [
            {
              required: true,
              message: '${title}不能为空',
            },
            {
              pattern: /^[a-z][a-z\d_]{0,}$/, // 正则
              message: '${title}必须以小写字母开头，可包含数字、下划线',
            },
            {
              unique: true,
              message: '${title}已存在,不能重复',
            },
          ],
        },
        {
          title: '字段备注',
          align: 'center',
          dataIndex: 'dbFieldTxt',
          key: 'dbFieldTxt',
          type: FormTypes.input,
          defaultValue: '',
          validateRules: [{ required: true, message: '${title}不能为空' }],
        },
        {
          title: '字段长度',
          align: 'center',
          dataIndex: 'dbLength',
          key: 'dbLength',
          type: FormTypes.inputNumber,
          defaultValue: '64',
          validateRules: [{ required: true, message: '${title}不能为空' }],
        },
        {
          title: '小数点',
          align: 'center',
          dataIndex: 'dbPointLength',
          key: 'dbPointLength',
          type: FormTypes.inputNumber,
          defaultValue: '0',
          validateRules: [{ required: true, message: '${title}不能为空' }],
        },
        {
          title: '默认值',
          align: 'center',
          dataIndex: 'dbDefaultVal',
          key: 'dbDefaultVal',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '字段类型',
          dataIndex: 'dbType',
          key: 'dbType',
          align: 'center',
          type: FormTypes.select,
          options: [
            // 下拉选项
            { title: 'String', value: 'String' },
            // { title: 'Integer', value: 'Integer' },
            // { title: 'Double', value: 'Double' },
            { title: 'Date', value: 'Date' },
            // { title: 'BigDecimal', value: 'Bigdecimal' },
            // { title: 'Text', value: 'Text' },
            // { title: 'Blob', value: 'Blob' },
          ],
          validateRules: [{ required: true, message: '${title}不能为空' }],
          defaultValue: 'String',
        },
        {
          title: '主键',
          dataIndex: 'dbIsKey',
          key: 'dbIsKey',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
        },
        {
          title: '允许空值',
          dataIndex: 'dbIsNull',
          key: 'dbIsNull',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
        },
      ],
      columns1: [
        {
          title: '字段名称',
          align: 'center',
          dataIndex: 'dbFieldName',
          disabled: true,
          key: 'dbFieldName',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '字段备注',
          align: 'center',
          dataIndex: 'dbFieldTxt',
          disabled: true,
          key: 'dbFieldTxt',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '表单显示',
          dataIndex: 'isShowForm',
          width: '40px',
          key: 'isShowForm',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: '1',
        },
        {
          title: '列表显示',
          dataIndex: 'isShowList',
          width: '40px',
          key: 'isShowList',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: '1',
        },
        {
          title: '是否排序',
          dataIndex: 'sortFlag',
          width: '40px',
          key: 'sortFlag',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
        },
        {
          title: '是否只读',
          dataIndex: 'isReadOnly',
          width: '40px',
          key: 'isReadOnly',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
        },
        {
          title: '控件类型',
          dataIndex: 'fieldShowType',
          width: '170px',
          key: 'fieldShowType',
          align: 'center',
          type: FormTypes.select,
          options: [
            // 下拉选项
            { title: '文本框', value: 'text' },
            { title: '密码', value: 'password' },
            { title: '下拉框', value: 'list' },
            { title: '单选框', value: 'radio' },
            { title: '多选框', value: 'checkbox' },
            { title: '开关', value: 'switch' },
            { title: '日期(yyyy-MM-dd)', value: 'date' },
            { title: '日期(yyyy-MM-dd HH-mm-ss)', value: 'datetime' },
            { title: '时间(HH-mm-ss)', value: 'time' },
            { title: '文件', value: 'file' },
            { title: '图片', value: 'image' },
            { title: '多行文本', value: 'textarea' },
            { title: '下拉多选框', value: 'list_multi' },
            { title: '下拉搜索框', value: 'sel_search' },
            { title: 'Popup弹框', value: 'popup' },
            { title: '分类字典树', value: 'cat_tree' },
            { title: '部门选择', value: 'sel_depart' },
            { title: '用户选择', value: 'sel_user' },
            { title: '富文本', value: 'umeditor' },
            { title: 'MarkDown', value: 'markdown' },
            { title: '省市区组件', value: 'pca' },
            { title: '联动组件', value: 'link_down' },
            { title: '自定义树控件', value: 'sel_tree' },
          ],
          defaultValue: 'text',
          validateRules: [{ required: true, message: '${title}不能为空' }],
        },
        {
          title: '控件长度',
          align: 'center',
          width: '100px',
          dataIndex: 'fieldLength',
          key: 'fieldLength',
          type: FormTypes.inputNumber,
          defaultValue: '120',
          validateRules: [{ required: true, message: '${title}不能为空' }],
        },
        {
          title: '是否查询',
          dataIndex: 'isQuery',
          width: '60px',
          key: 'isQuery',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
        },
        {
          title: '查询类型',
          dataIndex: 'queryMode',
          width: '110px',
          key: 'queryMode',
          align: 'center',
          type: FormTypes.select,
          options: [
            // 下拉选项
            { title: '普通查询', value: 'single' },
            { title: '范围查询', value: 'group' },
          ],
          defaultValue: 'single',
          validateRules: [{ required: true, message: '${title}不能为空' }],
        },
        {
          title: '控件默认值',
          align: 'center',
          width: '180px',
          dataIndex: 'fieldDefaultValue',
          key: 'fieldDefaultValue',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '扩展参数',
          align: 'center',
          width: '160px',
          dataIndex: 'fieldExtendJson',
          key: 'fieldExtendJson',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '自定义转换器',
          align: 'center',
          width: '160px',
          dataIndex: 'converter',
          key: 'converter',
          type: FormTypes.input,
          defaultValue: '',
        },
      ],
      columns2: [
        {
          title: '字段名称',
          align: 'center',
          dataIndex: 'dbFieldName',
          disabled: true,
          key: 'dbFieldName',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '字段备注',
          align: 'center',
          dataIndex: 'dbFieldTxt',
          disabled: true,
          key: 'dbFieldTxt',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '字段Href',
          align: 'center',
          dataIndex: 'fieldHref',
          key: 'fieldHref',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '验证规则',
          dataIndex: 'fieldValidType',
          key: 'fieldValidType',
          align: 'center',
          type: FormTypes.select,
          options: [
            // 下拉选项
            { title: '空', value: '' },
            { title: '唯一校验', value: 'only' },
            { title: '6到16位数字', value: 'n6-16' },
            { title: '6到16位任意字符', value: '*6-16' },
            { title: '网址', value: 'url' },
            { title: '电子邮件', value: 'e' },
            { title: '手机号码', value: 'm' },
            { title: '邮政编码', value: 'p' },
            { title: '字母', value: 's' },
            { title: '数字', value: 'n' },
            { title: '整数', value: 'z' },
            { title: '非空', value: '*' },
            { title: '6到18位字符串', value: 's6-18' },
            { title: '金额', value: 'money' },
          ],
          defaultValue: '',
        },
        {
          title: '校验必填',
          dataIndex: 'fieldMustInput',
          key: 'fieldMustInput',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
        },
        {
          title: '字典Table',
          dataIndex: 'dictTable',
          key: 'dictTable',
          align: 'center',
          type: FormTypes.input_pop,
        },
        {
          title: '字典Code',
          dataIndex: 'dictField',
          key: 'dictField',
          align: 'center',
          type: FormTypes.input,
        },
        {
          title: '字典Text',
          dataIndex: 'dictText',
          key: 'dictText',
          align: 'center',
          type: FormTypes.input,
        },
      ],
      // 外键标签
      columns3: [
        {
          title: '字段名称',
          width: '160px',
          align: 'center',
          dataIndex: 'dbFieldName',
          disabled: true,
          key: 'dbFieldName',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '字段备注',
          align: 'center',
          width: '160px',
          disabled: true,
          dataIndex: 'dbFieldTxt',
          key: 'dbFieldTxt',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '主表名',
          align: 'center',
          width: '120px',
          dataIndex: 'mainTable',
          key: 'mainTable',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '主表字段',
          align: 'center',
          width: '120px',
          dataIndex: 'mainField',
          key: 'mainField',
          type: FormTypes.input,
          defaultValue: '',
        },
      ],
      columns4: [
        {
          title: '索引名称',
          align: 'center',
          width: '330px',
          placeholder: '请输入索引名称',
          dataIndex: 'indexName',
          key: 'indexName',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '索引栏位',
          align: 'center',
          width: '330px',
          placeholder: '请选择索引栏位',
          dataIndex: 'indexField',
          key: 'indexField',
          options: [],
          type: FormTypes.list_multi,
          defaultValue: '',
        },
        {
          title: '索引类型',
          dataIndex: 'indexType',
          width: '330px',
          key: 'indexType',
          align: 'center',
          type: FormTypes.select,
          options: [
            // 下拉选项
            { title: 'normal', value: 'normal' },
            { title: 'unique', value: 'unique' },
          ],
          defaultValue: 'normal',
        },
      ],
      columns5: [
        {
          title: '字段名称',
          width: '160px',
          align: 'center',
          disabled: true,
          dataIndex: 'dbFieldName',
          key: 'dbFieldName',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '字段备注',
          align: 'center',
          width: '160px',
          disabled: true,
          dataIndex: 'dbFieldTxt',
          key: 'dbFieldTxt',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '控件类型',
          dataIndex: 'queryShowType',
          width: '170px',
          key: 'queryShowType',
          align: 'center',
          type: FormTypes.select,
          options: [
            // 下拉选项
            { title: '文本框', value: 'text' },
            { title: '日期(yyyy-MM-dd)', value: 'date' },
            { title: '日期(yyyy-MM-dd HH-mm-ss)', value: 'datetime' },
            { title: '时间(HH-mm-ss)', value: 'time' },
            { title: '下拉框', value: 'list' },
            { title: '下拉多选框', value: 'list_multi' },
            { title: '下拉搜索框', value: 'sel_search' },
            { title: '分类字典树', value: 'cat_tree' },
            { title: 'Popup弹框', value: 'popup' },
            { title: '部门选择', value: 'sel_depart' },
            { title: '用户选择', value: 'sel_user' },
            { title: '省市区组件', value: 'pca' },
            { title: '自定义树控件', value: 'sel_tree' },
          ],
          defaultValue: '',
        },
        {
          title: '字典Table',
          dataIndex: 'queryDictTable',
          key: 'queryDictTable',
          align: 'center',
          type: FormTypes.input_pop,
        },
        {
          title: '字典Code',
          dataIndex: 'queryDictField',
          key: 'queryDictField',
          align: 'center',
          type: FormTypes.input,
        },
        {
          title: '字典Text',
          dataIndex: 'queryDictText',
          key: 'queryDictText',
          align: 'center',
          type: FormTypes.input,
        },
        {
          title: '默认值',
          align: 'center',
          dataIndex: 'queryDefVal',
          key: 'queryDefVal',
          type: FormTypes.input,
          defaultValue: '',
        },
        {
          title: '是否启用',
          dataIndex: 'queryConfigFlag',
          key: 'queryConfigFlag',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
        },
      ],
    }
  },
  created() {
    this.isShow = false
    this.data = {}
    this.length = this.dataSource.length
    this.list = Object.assign([], this.dataSource)
    for (let index = 0; index < this.list.length; index++) {
      let data = {}
      data.title = this.list[index].dbFieldName
      data.value = this.list[index].dbFieldName
      this.columns4[1].options.push(data)
    }
    // console.log('this.dataSource', this.dataSource)
    this.disabledData = this.dataSource.slice(0, 4)
    this.disabledData = { ...this.disabledData }
    // console.log('this.disabledDatadisabledData', this.disabledData)
  },
  onCancel() {
    // console.log('少时诵诗书')
  },
  afterClose() {
    // console.log('生成了--------')
    this.activeKey = '1'
  },
  methods: {
    async handleOk() {
      this.activeKey = '1'
      await this.$refs.editableTable.getValues((error, values) => {
        // 错误数 = 0 则代表验证通过
        // console.log('errrrahhah', error)
        if (error !== 0) {
          this.$message.error('验证未通过')
          this.result_code = 0
        } else {
          this.spining = true
          this.result_code = 1
          this.submitOk()
        }
      })
    },

    async submitOk() {
      this.tableType = '1'
      await this.form.validateFields(async (err) => {
        // console.log('errrrrrrrr', err)
        if (!err) {
          this.data.head = this.form.getFieldsValue()
          //
          this.data.head.tableType = this.tableType
          this.data.head.relationType = this.relationType
          this.data.head.tabOrderNum = this.tabOrderNum
          this.data.head.formCategory = this.formCategory
          this.data.head.idType = this.idType
          this.data.head.queryMode = this.queryMode
          this.data.head.themeTemplate = this.themeTemplate
          this.data.head.formTemplate = this.formTemplate
          this.data.head.scroll = this.scroll
          this.data.head.isCheckbox = this.isCheckbox
          this.data.head.isPage = this.isPage
          this.data.head.isTree = this.isTree
          this.data.fields = []
          this.data.fields = this.$refs.editableTable.getValuesSync().values
          // console.log('this.data.fields1:', this.data.fields)
          for (var index = 0; index < this.$refs.editableTable.getValuesSync().values.length; index++) {
            Object.assign(
              this.data.fields[index],
              pick(
                this.$refs.editableTable1.getValuesSync().values[index],
                'isShowForm',
                'isShowList',
                'sortFlag',
                'isReadOnly',
                'fieldShowType',
                'fieldLength',
                'isQuery',
                'queryMode',
                'fieldDefaultValue',
                'fieldExtendJson',
                'converter'
              ),
              pick(
                this.$refs.editableTable2.getValuesSync().values[index],
                'fieldHref',
                'fieldValidType',
                'fieldMustInput',
                'dictTable',
                'dictField',
                'dictText'
              ),
              pick(
                this.$refs.editableTable3.getValuesSync().values[index],
                'mainTable',
                'mainField',
                'queryShowType',
                'queryDictTable',
                'queryDictField',
                'queryDictText',
                'queryDefVal',
                'queryConfigFlag'
              )
            )
          }
          // console.log('this.data.fields2:', this.data.fields)
          // console.log('this.title', this.title)
          //索引
          if (this.title === '新增') {
            this.data.indexs = []
            this.$refs.editableTable4.getValuesSync().values.forEach((item, index) => {
              this.data.indexs.push(pick(item, 'indexField', 'indexName', 'indexType'))
            })
          } else {
            // console.log('this.dataSource2', this.dataSource2)
            this.data.indexs = []
            this.data.indexs.push(pick(this.dataSource2, 'id', 'indexField', 'indexName', 'indexType'))
          }

          this.data.deleteFieldIds = []
          this.data.deleteIndexIds = []
          // console.log('data:', this.data)

          if (this.addoreditflag === 0) {
            this.data.identity = this.iform.identityIndex + '/' + this.iform.identityData
            this.data.category = this.iform.category
            this.data.modelIdentity = this.iform.modelIdentity
            this.data.unitIdentity = this.iform.unitIdentity
            // console.log('data:', this.data)
            // tm_fakepostAction
            tm_postAction('/cgform/api/addAll', this.data)
              .then((res) => {
                if (res.success === true) {
                  this.$message.success(res.message)
                } else {
                  this.$message.error(res.message)
                }

                this.$emit('ok')
              })
              .catch((err) => {
                console.log(err)
              })
            // tm_postAction('/cgform/api/addAll', this.data)
            //   .then((res) => {
            //     if (res.success === true) {
            //       this.$message.success(res.message)
            //     } else {
            //       this.$message.error(res.message)
            //     }

            //     this.$emit('ok')
            //   })
            //   .catch((err) => {
            //     console.log(err)
            //   })
          } else {
            this.data.head.id = this.id
            this.modelData.identity = this.iform.identityIndex + '/' + this.iform.identityData
            this.modelData.category = this.iform.category
            this.modelData.modelIdentity = this.iform.modelIdentity
            this.modelData.unitIdentity = this.iform.unitIdentity
            // console.log('this.modelData', this.modelData)
            // this.$delete(this.modelData, 'category')
            axios.defaults.headers.put['Content-Type'] = 'application/json;charset=UTF-8'
            const token = Vue.ls.get(ACCESS_TOKEN)
            axios.defaults.headers.put['X-Access-Token'] = token
            await im_putAction('/modelIdentity/update', JSON.stringify(this.modelData))
              .then((res) => {
                // console.log('updateres', res)
              })
              .catch((err) => {
                console.log(err)
              })
            // console.log('online_edit', this.data)
            this.data.deleteFieldIds = this.$refs.editableTable.deleteFieldIds
            // console.log('this.data.head.id', this.data.head.id)
            await im_putAction('/admin/cgform/api/editAll', this.data)
              .then((res) => {
                if (res.success) {
                  Message({
                    type: 'success',
                    showClose: true,
                    message: '编辑成功!',
                  })
                }

                this.$emit('ok')
              })
              .catch((err) => {
                console.log('error')
              })
          }
          this.dataSource = this.list
          this.spining = false
          this.visible = false
          this.iform = {}
          this.iform.identityIndex = '88.175.111'
          this.formCategory = 'identity'
        }
      })
    },

    handleCancel() {
      console.log('handleCancel11111111111111111111111')
      this.visible = false
      // console.log('this.list', this.list)
      this.dataSource = this.list
      // console.log('CancelList', this.dataSource)
      this.queryParam = {}
      this.activeKey = '1'
      //
      this.isshow = false
      this.iform = {}
      this.iform.identityIndex = '88.175.111'
      this.formCategory = 'identity'

      // this.tableType = '请选择表类型'
      // this.tableType = '1'
      // console.log('this.value')
      // console.log(this.$refs.opt.value)
    },
    handleadd() {
      this.queryParam = {}
      this.form.resetFields()
      this.visible = true
      this.diasabledInput = false
      this.title = '新增'
      this.addoreditflag = 0
      this.tempNumber = 4
    },
    handleedit(record) {
      // console.log('record:', record)
      this.title = '编辑'
      this.visible = true
      this.diasabledInput = true
      console.log("pick(record, 'tableName', 'tableTxt')", pick(record, 'tableName', 'tableTxt'))
      setTimeout(() => {
        this.form.setFieldsValue(pick(record, 'tableName', 'tableTxt'))
        this.formCategory = record.formCategory
      }, 0)
      this.addoreditflag = 1
      this.id = record.id
      tm_getAction('/cgform/field/listByHeadId', { headId: this.id })
        .then((res) => {
          this.dataSource = res.result
          this.tempNumber = res.result.length
          // console.log('res:dierci', res)
          // console.log('this.tempNumber', this.tempNumber)
        })
        .catch((err) => {
          console.log('error')
        })
      tm_getAction('/cgform/index/listByHeadId', { headId: this.id })
        .then((res) => {
          // console.log('listByHeadId的res', res)
          this.dataSource2 = res.result[0]
          // console.log('this.dataSource2', this.dataSource2)
          // console.log('res:123', res)
          // console.log('success')
        })
        .catch((err) => {
          console.log('error')
        })
    },
    change(value) {
      if (value === 3) {
        this.isshow = true
      } else {
        this.isshow = false
      }
    },
    changeShow(value) {
      if (value === 'Y') {
        this.isShow = true
        this.$nextTick(() => {
          this.form.setFieldsValue({ treeParentIdField: 'pid' })
          this.dataSource.push({
            dbFieldName: 'pid',
            dbFieldTxt: '父级节点',
            dbLength: '32',
            dbPointLength: '0',
            dbDefaultVal: '',
            dbType: 'String',
            dbIsKey: 0,
            dbIsNull: 1,
            isShowForm: 1,
            isShowList: 0,
            sortFlag: 0,
            isReadOnly: 0,
            fieldShowType: 'list',
            fieldLength: 120,
            isQuery: 0,
            queryMode: 'single',
            fieldDefaultValue: '',
            fieldExtendJson: '',
            converter: '',
            fieldHref: '',
            fieldValidType: '',
            fieldMustInput: '',
            dictTable: '',
            dictField: '',
            dictText: '',
            mainTable: '',
            mainField: '',
            queryShowType: '',
            queryDictTable: '',
            queryDictField: '',
            queryDictText: '',
            queryDefVal: '',
            queryConfigFlag: '',
          })
          this.dataSource.push({
            dbFieldName: 'has_child',
            dbFieldTxt: '是否有子节点',
            dbLength: '3',
            dbPointLength: '0',
            dbDefaultVal: '',
            dbType: 'String',
            dbIsKey: 0,
            dbIsNull: 1,
            isShowForm: 0,
            isShowList: 0,
            sortFlag: 0,
            isReadOnly: 0,
            fieldShowType: 'text',
            fieldLength: 120,
            isQuery: 0,
            queryMode: 'single',
            fieldDefaultValue: '',
            fieldExtendJson: '',
            converter: '',
            fieldHref: '',
            fieldValidType: '',
            fieldMustInput: '',
            dictTable: '',
            dictField: 'yn',
            dictText: '',
            mainTable: '',
            mainField: '',
            queryShowType: '',
            queryDictTable: '',
            queryDictField: '',
            queryDictText: '',
            queryDefVal: '',
            queryConfigFlag: '',
          })
        })
      } else {
        this.isShow = false
        this.dataSource.splice(this.dataSource.length - 2, 2)
      }
    },
    handleSelectRowChange(selectedRowIds) {
      this.selectedRowIds = selectedRowIds
    },
    handleSelectRowChange(selectedRowIds) {
      this.selectedRowIds = selectedRowIds
    },
    handleDelete(props) {
      let { rowId, target } = props
      target.removeRows(rowId)
    },
    tabchange() {
      for (let index = 0; index < this.$refs.editableTable.getValuesSync().values.length; index++) {
        if (this.length < this.$refs.editableTable.getValuesSync().values.length) {
          this.dataSource.splice(
            this.$refs.editableTable.getValuesSync().values.length,
            this.$refs.editableTable.getValuesSync().values.length - this.length
          )
        }
        // if (index > this.length-1) {
        this.$set(
          this.dataSource[index],
          'dbFieldName',
          this.$refs.editableTable.getValuesSync().values[index].dbFieldName
        )
        this.$set(
          this.dataSource[index],
          'dbFieldTxt',
          this.$refs.editableTable.getValuesSync().values[index].dbFieldTxt
        )
        this.$set(this.dataSource[index], 'dbLength', this.$refs.editableTable.getValuesSync().values[index].dbLength)
        this.$set(
          this.dataSource[index],
          'dbPointLength',
          this.$refs.editableTable.getValuesSync().values[index].dbPointLength
        )
        this.$set(
          this.dataSource[index],
          'dbDefaultVal',
          this.$refs.editableTable.getValuesSync().values[index].dbDefaultVal
        )
        this.dataSource[index].dbType = this.$refs.editableTable.getValuesSync().values[index].dbType
        this.dataSource[index].dbIsKey = this.$refs.editableTable.getValuesSync().values[index].dbIsKey
        this.dataSource[index].dbIsNull = this.$refs.editableTable.getValuesSync().values[index].dbIsNull
        // }
      }
    },
    added() {
      // console.log('触发',this.$refs.editableTable.getValuesSync());
      // console.log('datass飞洒',JSON.parse(JSON.stringify(this.dataSource)));
      // console.log('datass飞洒',this.dataSource);
      // console.log("this.dataSource.length",this.dataSource.length);
      // console.log("this.$refs.editableTable.getValuesSync().values.length",this.$refs.editableTable.getValuesSync().values.length);
      if (this.dataSource.length < this.$refs.editableTable.getValuesSync().values.length) {
        for (let index = 0; index < this.$refs.editableTable.getValuesSync().values.length; index++) {
          if (index < this.dataSource.length) {
            // console.log('第一个');
            this.$set(
              this.dataSource[index],
              'dbFieldName',
              this.$refs.editableTable.getValuesSync().values[index].dbFieldName
            )
            this.$set(this.dataSource[index], 'dbFieldTxt', this.$refs.editableTable.inputValues[index].dbFieldTxt)
            this.$set(this.dataSource[index], 'dbLength', this.$refs.editableTable.inputValues[index].dbLength)
            this.$set(
              this.dataSource[index],
              'dbPointLength',
              this.$refs.editableTable.inputValues[index].dbPointLength
            )
            this.$set(this.dataSource[index], 'dbDefaultVal', this.$refs.editableTable.inputValues[index].dbDefaultVal)
            this.$set(this.dataSource[index], 'dbType', this.$refs.editableTable.getValuesSync().values[index].dbType)
            this.$set(this.dataSource[index], 'dbIsKey', this.$refs.editableTable.getValuesSync().values[index].dbIsKey)
            this.$set(
              this.dataSource[index],
              'dbIsNull',
              this.$refs.editableTable.getValuesSync().values[index].dbIsNull
            )
          } else {
            // console.log('第二个');
            console.log('Object', Object.assign({}, this.$refs.editableTable.inputValues[index]))
            console.log(
              'Object+input',
              Object.assign({}, this.$refs.editableTable.inputValues[index], {
                dbType: 'String',
                dbIsKey: '0',
                dbIsNull: '1',
                isShowForm: '1',
                isShowList: '1',
              })
            )
            //往dataSource新增数据   设置默认
            this.dataSource.push(
              Object.assign({}, this.$refs.editableTable.inputValues[index], {
                dbType: 'String',
                dbIsKey: '0',
                dbIsNull: '1',
                isShowForm: '1',
                isShowList: '1',
              })
            )
            // console.log('this.dataSource2', this.dataSource)
          }
        }
      } else {
        this.dataSource = this.dataSource.splice(0, this.$refs.editableTable.getValuesSync().values.length - 1)
        // this.dataSource.push({})
        this.added()
      }
      // console.log('this.dataSource3', this.dataSource)
    },
  },
}
</script>

<style lang="less"  scoped>
.table55 > .ant-modal-wrap > .ant-modal {
  width: 80% !important;
}
.forspinc {
  text-align: center;
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 9999;
}
.ant-spin {
  top: 200px;
}
.table55 > .ant-modal-wrap > .ant-modal > .ant-modal-content {
  width: 100% !important;
  z-index: 9999999;
}
/* .ant-form-item-label {
  width: 25% !important;
} */
.ant-form label {
  font-size: 13px;
}
.ant-divider {
  margin-top: 0px !important;
  margin-bottom: 10px !important;
}
.onlineform {
  background-color: hotpink !;
}
.detailItem {
  display: flex !important;
}
.detailItem > .ant-form-item-control-wrapper {
  width: 48% !important;
}
.detailItem > .ant-form-item-label {
  display: flex !important;
}
</style >
