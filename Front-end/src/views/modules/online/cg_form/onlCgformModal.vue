<template>
  <div class="onlineform">
    <!-- onlCgformModal.vue -->
    <a-modal
      :title="title"
      :visible.sync="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      width="800"
    >
      <div class="table-page-search-wrapper">
        <a-form style="width:100%;height:100%" layout="inline" :form="form">
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="表名">
                <a-input
                  placeholder="请输入表名"
                  v-decorator="['tableName',{ rules: [{ required: true, message: '请输入表名!' }] },]"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="表描述">
                <a-input
                  placeholder="请输入表描述"
                  v-decorator="['tableTxt',{ rules: [{ required: true, message: '请输入表说明!' }] },]"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="表类型">
                <a-select  placeholder="请选择表类型" v-model="tableType" @select="change(tableType)" defaultValue="2" ref="opt"
                :getPopupContainer="triggerNode => triggerNode.parentNode" >
                  <a-select-option :value="1">单表</a-select-option>
                  <a-select-option :value="2">主表</a-select-option>
                  <a-select-option :value="3">附表</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row v-if="isshow">
            <a-col :span="3" :offset="17">
              <a-form-item>
                <a-radio-group name="radioGroup" v-model="relationType">
                  <a-radio :value="1">一对多</a-radio>
                  <a-radio :value="2">一对一</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="3" :offset="1">
              <a-form-item label="序号">
                <a-input-number
                  id="inputNumber"
                  v-decorator="['tabOrderNum',{ rules: [{ required: true, message: '请输入序号!' }] },]"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-divider orientation="left"></a-divider>
          <!-- 第二行 -->
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="表单分类">
                <a-select placeholder="请选择表单分类" v-model="formCategory">
                  <a-select-option value="demo">官方示例</a-select-option>
                  <a-select-option value="bpm">流程表单</a-select-option>
                  <a-select-option value="temp">测试表单</a-select-option>
                  <a-select-option value="bdfl_include">导入表单</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="主键策略">
                <a-select placeholder="请选择主键策略" v-model="idType">
                  <a-select-option value="UUID">ID-WORKER(分布式自增)</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="查询模式">
                <a-select placeholder="请选择查询模式" v-model="queryMode">
                  <a-select-option value="single">单表查询</a-select-option>
                  <a-select-option value="group">组合查询</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-divider orientation="left"></a-divider>
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="主题模板">
                <a-select placeholder="请选择主题模板" v-model="themeTemplate">
                  <a-select-option value="normal">默认主题</a-select-option>
                  <a-select-option value="erp">ERP主题(一对多)</a-select-option>
                  <a-select-option value="innerTable">内嵌子表主题(一对多)</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="表单风格">
                <a-select placeholder="请选择表单风格" v-model="formTemplate">
                  <a-select-option value="1">一列</a-select-option>
                  <a-select-option value="2">两列</a-select-option>
                  <a-select-option value="3">三列</a-select-option>
                  <a-select-option value="4">四列</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="滚动条">
                <a-select placeholder="请选择滚动条" v-model="scroll">
                  <a-select-option :value="1">有</a-select-option>
                  <a-select-option :value="2">无</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-divider orientation="left"></a-divider>
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="显示复选框">
                <a-select placeholder="请选择是否显示复选框" v-model="isCheckbox">
                  <a-select-option value="Y">是</a-select-option>
                  <a-select-option value="N">否</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="是否分页">
                <a-select placeholder="请选择是否分页" v-model="isPage">
                  <a-select-option value="Y">是</a-select-option>
                  <a-select-option value="N">否</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="是否树">
                <a-select placeholder="请选择是否树" v-model="isTree" @select="changeShow(isTree)">
                  <a-select-option value="Y">是</a-select-option>
                  <a-select-option value="N">否</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-divider v-if="isShow" orientation="left"></a-divider>
          <a-row :gutter="24" v-if="isShow">
            <a-col :span="8">
              <a-form-item label="树表单父ID">
                <a-input
                  placeholder="请输入树表单父ID字段名"
                  v-decorator="['treeParentIdField',{ rules: [{ required: true, message: '请输入树表单父ID!' }] },]"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="树开表单列">
                <a-input
                  placeholder="请输入树开表单列字段名"
                  v-decorator="['treeFieldname',{ rules: [{ required: true, message: '请输入树开表单列!' }] },]"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
        <a-tabs v-model="activeKey"  style="margin-top: 15px;" @change="tabchange()" >
          <a-tab-pane key="1" tab="数据库属性" :forceRender="true">
            <j-editable-table
              ref="editableTable"
              :loading="loading"
              :columns="columns"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="true"
              :actionButton="true"
              :dragSort="true"
              style="margin-top: 8px;"
              :disabledRows="dataSource[0]"
              @added="added"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-table>
          </a-tab-pane>
          <a-tab-pane key="2" tab="页面属性" :forceRender="true">
            <j-editable-table
              ref="editableTable1"
              :loading="loading"
              :columns="columns1"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false"
              :disabledRows="dataSource[0]"
              style="margin-top: 8px;"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-table>
          </a-tab-pane>
          <a-tab-pane key="3" tab="校验字段" :forceRender="true">
            <j-editable-table
              ref="editableTable2"
              :loading="loading"
              :columns="columns2"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false"
              :disabledRows="dataSource[0]"
              style="margin-top: 8px;"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-table>
          </a-tab-pane>
          <a-tab-pane key="4" tab="外键" :forceRender="true">
            <j-editable-table
              ref="editableTable3"
              :loading="loading"
              :columns="columns3"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false"
              :disabledRows="dataSource[0]"
              style="margin-top: 8px;"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-table>
          </a-tab-pane>
          <a-tab-pane key="5" tab="索引" :forceRender="true">
            <j-editable-table
              ref="editableTable4"
              :loading="loading"
              :columns="columns4"
              :dataSource="dataSource1"
              :rowNumber="true"
              :rowSelection="true"
              :actionButton="true"
              :dragSort="true"
              style="margin-top: 8px;"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-table>
          </a-tab-pane>
          <a-tab-pane key="6" tab="查询配置" :forceRender="true">
            <j-editable-table
              ref="editableTable5"
              :loading="loading"
              :columns="columns5"
              :dataSource="dataSource"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false"
              :disabledRows="dataSource[0]"
              style="margin-top: 8px;"
              @selectRowChange="handleSelectRowChange"
            ></j-editable-table>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { o_postAction, o_getAction, o_putAction } from '../../../../api/onApi'
import JEditableTable from '@/components/jeecg/JEditableTable'
import { FormTypes } from '@/utils/JEditableTableUtil'
import pick from 'lodash.pick'
import { getFileAccessHttpUrl } from '../../../../api/manage'
import { Message } from 'element-ui'
export default {
  name: 'onlCgformModal',
  components: { JEditableTable },
  data() {
    return {
      // destroyOnClose:true,
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
          dbFieldName: 'create_by',
          dbFieldTxt: '创建人',
          dbLength: '50',
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
          dbFieldName: 'create_time',
          dbFieldTxt: '创建日期',
          dbLength: '20',
          dbPointLength: '0',
          dbDefaultVal: '',
          dbType: 'Date',
          dbIsKey: 0,
          dbIsNull: 1,
          isShowForm: 0,
          isShowList: 0,
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
          dbFieldName: 'update_by',
          dbFieldTxt: '更新人',
          dbLength: '50',
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
          isShowList: 0,
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
          dbFieldName: 'sys_org_code',
          dbFieldTxt: '所属部门',
          dbLength: '64',
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
      ],
      dataSource1: [],
      tableType: 1,
      relationType: 1,
      formCategory: 'temp',
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
          validateRules: [{ required: true, message: '${title}不能为空' }],
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
            { title: 'Integer', value: 'Integer' },
            { title: 'Double', value: 'Double' },
            { title: 'Date', value: 'Date' },
            { title: 'BigDecimal', value: 'Bigdecimal' },
            { title: 'Text', value: 'Text' },
            { title: 'Blob', value: 'Blob' },
          ],
          validateRules: [{ required: true, message: '${title}不能为空' }],
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
          defaultChecked: false,
        },
        {
          title: '列表显示',
          dataIndex: 'isShowList',
          width: '40px',
          key: 'isShowList',
          align: 'center',
          type: FormTypes.checkbox,
          customValue: ['1', '0'],
          defaultChecked: false,
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
          defaultValue: '',
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
          defaultValue: 'normal',
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
    // this.activeKey = '1'
    this.tableType = '请选择表类型'
    this.isShow = false
    this.data = {}
    this.length = this.dataSource.length
    this.list = this.dataSource
    for (let index = 0; index < this.list.length; index++) {
      let data = {}
      data.title = this.list[index].dbFieldName
      data.value = this.list[index].dbFieldName
      this.columns4[1].options.push(data)
    }
  },
  onCancel(){
    console.log('少时诵诗书')
  },
  afterClose(){
      console.log('生成了--------')
      this.activeKey = "1"
  },
  methods: {
    async handleOk() {
      console.log('errrrrrrrr12456',err);
      this.activeKey = '1'
      //this.tableType = '1' 
      await this.form.validateFields(async (err) => {
        console.log('errrrrrrrr',err);
        if (!err) {
          this.data.head = this.form.getFieldsValue()
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
          for (var index = 0; index < this.$refs.editableTable.getValuesSync().values.length; index++) {
            this.$set(
              this.data.fields[index],
              'isShowForm',
              this.$refs.editableTable1.getValuesSync().values[index].isShowForm
            )
            this.data.fields[index].isShowForm = this.$refs.editableTable1.getValuesSync().values[index].isShowForm
            this.data.fields[index].isShowList = this.$refs.editableTable1.getValuesSync().values[index].isShowList
            this.data.fields[index].sortFlag = this.$refs.editableTable1.getValuesSync().values[index].sortFlag
            this.data.fields[index].isReadOnly = this.$refs.editableTable1.getValuesSync().values[index].isReadOnly
            this.data.fields[index].fieldShowType = this.$refs.editableTable1.getValuesSync().values[
              index
            ].fieldShowType
            this.data.fields[index].fieldLength = this.$refs.editableTable1.getValuesSync().values[index].fieldLength
            this.data.fields[index].isQuery = this.$refs.editableTable1.getValuesSync().values[index].isQuery
            this.data.fields[index].queryMode = this.$refs.editableTable1.getValuesSync().values[index].queryMode
            this.data.fields[index].fieldDefaultValue = this.$refs.editableTable1.getValuesSync().values[
              index
            ].fieldDefaultValue
            this.data.fields[index].fieldExtendJson = this.$refs.editableTable1.getValuesSync().values[
              index
            ].fieldExtendJson
            this.data.fields[index].converter = this.$refs.editableTable1.getValuesSync().values[index].converter
            this.data.fields[index].fieldHref = this.$refs.editableTable2.getValuesSync().values[index].fieldHref
            this.data.fields[index].fieldValidType = this.$refs.editableTable2.getValuesSync().values[
              index
            ].fieldValidType
            this.data.fields[index].fieldMustInput = this.$refs.editableTable2.getValuesSync().values[
              index
            ].fieldMustInput
            this.data.fields[index].dictTable = this.$refs.editableTable2.getValuesSync().values[index].dictTable
            this.data.fields[index].dictField = this.$refs.editableTable2.getValuesSync().values[index].dictField
            this.data.fields[index].dictText = this.$refs.editableTable2.getValuesSync().values[index].dictText
            this.data.fields[index].mainTable = this.$refs.editableTable3.getValuesSync().values[index].mainTable
            this.data.fields[index].mainField = this.$refs.editableTable3.getValuesSync().values[index].mainField
            this.data.fields[index].queryShowType = this.$refs.editableTable5.getValuesSync().values[
              index
            ].queryShowType
            this.data.fields[index].queryDictTable = this.$refs.editableTable5.getValuesSync().values[
              index
            ].queryDictTable
            this.data.fields[index].queryDictField = this.$refs.editableTable5.getValuesSync().values[
              index
            ].queryDictField
            this.data.fields[index].queryDictText = this.$refs.editableTable5.getValuesSync().values[
              index
            ].queryDictText
            this.data.fields[index].queryDefVal = this.$refs.editableTable5.getValuesSync().values[index].queryDefVal
            this.data.fields[index].queryConfigFlag = this.$refs.editableTable5.getValuesSync().values[
              index
            ].queryConfigFlag
          }
          console.log('this.data.fields:', this.data.fields)
          this.data.indexs = this.$refs.editableTable4.getValuesSync().values
          this.data.deleteFieldIds = []
          this.data.deleteIndexIds = []
          if (this.addoreditflag === 0) {
            console.log('data:', this.data)
            o_postAction('/cgform/api/addAll', this.data)
              .then((res) => {
                if (res.success === true) {
                  this.$message.success(res.message)
                } else {
                  this.$message.error(res.message)
                }

                this.$emit('ok')
              })
              .catch((err) => {
                console.log('error')
              })
          } else {
            this.data.head.id = this.id
            o_putAction('/cgform/api/editAll', this.data)
              .then((res) => {
                console.log('success')
                this.$emit('ok')
              })
              .catch((err) => {
                console.log('error')
              })
          }
          this.visible = false
        }
      })
    },
    
    handleCancel() {
      this.visible = false
      this.dataSource = this.list
      this.queryParam = {}
      this.activeKey = '1'
      // 
      this.isshow = false
      this.tableType = '请选择表类型'
      // this.tableType = '1'
      console.log('this.value')
      console.log(this.$refs.opt.value)
    },
    handleadd() {
      this.queryParam = {}
      this.form.resetFields()
      this.visible = true
      this.title = '新增'
      this.addoreditflag = 0
    },
    handleedit(record) {
      console.log(record)
      this.title = '编辑'
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(this.model, 'tableName', 'tableTxt', 'tabOrderNum', 'treeParentIdField', 'treeFieldname')
        )
      })
      this.visible = true
      this.addoreditflag = 1
      this.id = record.id
      console.log('record:', record)
      o_getAction('/cgform/field/listByHeadId', { headId: this.id })
        .then((res) => {
          this.dataSource = res.result
          console.log('res:', res)
          console.log('success')
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
      if (this.dataSource.length < this.$refs.editableTable.getValuesSync().values.length) {
        for (let index = 0; index < this.$refs.editableTable.getValuesSync().values.length; index++) {
          if (index < this.dataSource.length) {
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

            this.dataSource[index].dbType = this.$refs.editableTable.getValuesSync().values[index].dbType
            this.dataSource[index].dbIsKey = this.$refs.editableTable.getValuesSync().values[index].dbIsKey
            this.dataSource[index].dbIsNull = this.$refs.editableTable.getValuesSync().values[index].dbIsNull
          } else {
            this.dataSource.push(this.$refs.editableTable.inputValues[index])
          }
        }
      }else{
        this.dataSource = this.dataSource.splice(0,this.$refs.editableTable.getValuesSync().values.length-1)
        this.dataSource.push({})
      }
    },
  },
}
</script>

<style  lang="less" scoped>

.ant-form label {
  font-size: 13px;
}
.ant-divider {
  margin-top: 0px !important;
  margin-bottom: 10px !important;
}
// .ant-form-item-label {
//   width: 25%  !important; 
// }
.onlineform{
  background-color: hotpink !;
}
</style >
