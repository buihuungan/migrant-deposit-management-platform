<template>
<div>
  <div>
    <div class="node-form-header flex-col" @click="attMenu1Open = !attMenu1Open">
      <div class="node-form-header-content flex-row">
        <img
          class="node-form-header-icon1"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng096494320fc014d16cfa3fa68c4ac231773f1364bcb1376625d7698d27f657d5"
        />
        <span class="node-form-header-text">基本属性</span>
        <img
          v-if="attMenu1Open"
          class="node-form-header-icon2"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc563fff86a5c839a00d798119c04b8bbe7a720e4283ba743a7c234d12efa85d0"
        />
        <img
          v-if="!attMenu1Open"
          class="node-form-header-icon2"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng7762e7a1e11d189f72dbcab4675e41ba40d5e2c4ae8fdabe98a30742914c97b5"
        />
      </div>
    </div>
    <div v-show="attMenu1Open" class="form-content flex-col">
      <div class='form-content1'>
        <!-- 流程节点属性框 -->
      <el-form
        :model="node"
        label-width="75px"
        label-position="left"
        ref="dataForm"
        v-if="node.type === 'subprocess'||node.type === 'fork'||node.type === 'join'"
      >
        <el-form-item label="节点类型:" style="display:none">
          <el-input v-model="node.type" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 开始节点属性框 -->
      <el-form
        :model="node"
        label-width="78px"
        label-position="left"
        ref="dataForm"
        v-if="node.type === 'start'"
      >
        <el-form-item label="节点类型:" style="display:none">
          <el-input v-model="node.type" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name" size="small"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点标签:">
          <el-input v-model="node.label" size="small"></el-input>
        </el-form-item>
        <el-form-item label="事件bean:">
          <el-select v-model="node.event_handler_bean" placeholder="请选择" size="small">
            <el-option v-for="item in beanList" :key="item" :label="item" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="处理bean:">
          <el-input v-model="node.handler_bean" size="small"></el-input>
        </el-form-item> -->
        <!-- <el-form-item label="位置信息:">
          <el-input v-model="node.g"></el-input>
        </el-form-item> -->
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择" size="small">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 人工任务节点属性框 -->
      <el-form
        :model="node"
        label-width="75px"
        label-position="left"
        ref="dataForm"
        v-if="node.type === 'task'"
      >
        <el-form-item label="节点类型:" style="display:none">
          <el-input size="small" v-model="node.type" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input size="small" v-model="node.name"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input size="small" v-model="node.left" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input size="small" v-model="node.top" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="事件bean:">
          <el-select size="small" v-model="node.event_handler_bean"  placeholder="请选择">
            <el-option v-for="item in beanList" :key="item" :label="item" :value="item"> </el-option>
          </el-select>
        </el-form-item>
         <el-form-item label="选择表单:">
          <el-select size="small" v-model="node.feature" value-key="id" placeholder="请选择">
            <el-option v-for="item in formList" :key="item.id" :label="item.name" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理人:">
          <el-radio-group v-model="node.assignment_type" size="small">
          <el-radio-button label="ProcessPromoter">流程发起人</el-radio-button>
          <el-radio-button label="Assignee">指定参与者</el-radio-button>
        </el-radio-group>
        </el-form-item>
        <el-form-item label="选择角色:" v-show="node.assignment_type==='Assignee'">
        <el-select size="small" v-model="roleAssginee" value-key="id" placeholder="请选择" :disabled='userAssginee.length!=0' :clearable='true'>
            <el-option v-for="item in roleAssgineeList" :key="item.id" :label="item.name" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择用户:"  v-show="node.assignment_type==='Assignee'">
       <el-select size="small" v-model="userAssginee" multiple value-key="id" placeholder="请选择" :disabled='roleAssginee.id!=undefined' filterable allow-create clearable>
            <el-option v-for="item in userAssgineeList" :key="item.id" :label="item.name" :value="item"> </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="按钮权限:">
          <el-checkbox-group v-model="node.button_permission" style="height: 100%;" size="small">
            <el-checkbox-button label="通过"></el-checkbox-button>
            <el-checkbox-button label="拒绝"></el-checkbox-button>
            <el-checkbox-button label="退回"></el-checkbox-button>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="到期时间:" class="el-form-special">
          <el-input v-model="node.due.day" size="small"></el-input>天
          <el-input v-model="node.due.hour" size="small"></el-input>小时
          <el-input v-model="node.due.minute" size="small"></el-input>分钟
        </el-form-item>
        <!-- <el-form-item label="人员信息:" class="el-form-special">
          <el-input v-model="node.user_data.key" size="small"></el-input>key
          <el-input v-model="node.user_data.value" size="small"></el-input>value
        </el-form-item> -->
        <el-form-item label="节点状态:">
          <el-select size="small" v-model="node.state" placeholder="请选择">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 逻辑决策节点属性框 -->
      <el-form
        :model="node"
        label-width="78px"
        label-position="left"
        ref="dataForm"
        v-if="node.type === 'decision'"
      >
        <el-form-item label="节点类型:" style="display:none">
          <el-input v-model="node.type" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name" size="small"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="事件bean:">
          <el-select v-model="node.event_handler_bean" placeholder="请选择" size="small">
            <el-option v-for="item in beanList" :key="item" :label="item" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="条件表达式:">
            <el-button
              type="primary"
              size="mini"
              @click="addExpressionfunc()"
              style="margin-left: 20px"
              >添加表达式
            </el-button>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择" size="small">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>

      <!--结束节点属性框 -->
      <el-form
        :model="node"
        label-width="75px"
        label-position="left"
        ref="dataForm"
        v-if="node.type === 'end'"
      >
        <el-form-item label="节点类型:" style="display:none">
          <el-input v-model="node.type" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name" size="small"></el-input>
        </el-form-item>
        <el-form-item label="结束属性:">
          <el-input v-model="node.terminate" size="small"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择" size="small">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 长方形属性框 -->
      <el-form :model="node" label-width="75px" label-position="left" ref="dataForm" v-if="node.type === 'rectangle'">
        <el-form-item label="节点类型:" style="display:none">
          <el-input v-model="node.type" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name" size="small"></el-input>
        </el-form-item>
        <el-form-item label="功能类型:">
          <el-select v-model="node.feature" value-key="id" placeholder="请选择" size="small">
            <el-option v-for="item in featureList" :key="item.id" :label="item.type" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户角色:">
          <el-select v-model="node.role" multiple placeholder="请选择" size="small">
            <el-option v-for="item in roleList" :key="item.id" :label="item.roleName" :value="item.id"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择" size="small">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 菱形属性框 -->
      <el-form :model="node" label-width="75px" label-position="left" ref="dataForm" v-if="node.type === 'rhombus'">
        <el-form-item label="节点类型:" style="display:none">
          <el-input v-model="node.type" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name" size="small"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择" size="small">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 泳道图属性框 -->
      <el-form :model="node" label-width="75px" label-position="left" ref="dataForm" v-if="node.type === 'swinlane'">
        <el-form-item label="节点类型:" style="display:none">
          <el-input v-model="node.type" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name" size="small"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="width坐标:" v-show="false">
          <el-input v-model="node.width" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="height坐标:" v-show="false">
          <el-input v-model="node.height" :disabled="true" size="small"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择" size="small">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 连线属性框 -->
      <el-form :model="line" label-width="75px" label-position="left" ref="dataForm" v-if="type === 'line'">
        <el-form-item label="连线条件">
          <el-input v-model="line.label" size="small"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="expression" :rows="2"  type="textarea" disabled placeholder="请先填写路由决策表达式" />
        </el-form-item>
        <el-form-item>
          <el-select  v-model="lineRuleModel" placeholder="判断是否通过" size="small" clearable>
            <el-option v-for="item in ruleModel" :key="item.source" :label="item.name" :value="item.source"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <el-button class="form-button-save flex-col" @click="saveLine">
          <span class="form-button-save-text">保&nbsp;存</span>
        </el-button>
      </div>
        </el-form-item>
      </el-form>
      </div>
      
    </div>
    <div class="flow-form-line flex-col"></div>
    <div class="node-form-header flex-col" @click="attMenu2Open = !attMenu2Open">
      <div class="node-form-header-content flex-row">
        <img
          class="node-form-header-icon1"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng096494320fc014d16cfa3fa68c4ac231773f1364bcb1376625d7698d27f657d5"
        />
        <span class="node-form-header-text">表单属性</span>
        <img
          v-if="attMenu2Open"
          class="node-form-header-icon2"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc563fff86a5c839a00d798119c04b8bbe7a720e4283ba743a7c234d12efa85d0"
        />
        <img
          v-if="!attMenu2Open"
          class="node-form-header-icon2"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng7762e7a1e11d189f72dbcab4675e41ba40d5e2c4ae8fdabe98a30742914c97b5"
        />
      </div>
    </div>
    <div class="ef-node-form">
      <ul v-show="attMenu2Open" class="ef-node-menu-ul">
        <div class="attMenu2Div">
          <attribute-form :data="data" ref="attributesForm1" @changeOnline="changeOnline"> </attribute-form>
        </div>
      </ul>
    </div>
  </div>
  <el-dialog
    title="选择表达式"
    :visible.sync ="addExpressionDialogVisible"
    width="30%"
    @close ="addExpressionDialogClose"
  >
  <el-form
      :model="node.expressionNode"
      :rules="formRules"
      ref="if_expression"
      label-width="150px"
  >
  <!-- <div style="border-left: 4px solid #39a9ff;padding-left: 8px;line-height: initial;font-size: initial;">
				<h4>条件表达式</h4>
			</div> -->
			<el-row :gutter="10" v-for="(expression,index) in expressionNode" :key="index" style="margin-bottom:20px">
				<el-row :gutter="10">
					<el-col :span="6" prop="formId">
						<el-select  v-model="expression.formId" placeholder="请选择" size="small" clearable>
            <el-option v-for="item in formAllId" :key="item.id" :label="item.name" :value="item.id"> </el-option>
          </el-select>
					</el-col>
					<el-col :span="6" prop="symbol">
						<el-select v-model="expression.symbol" placeholder="请选择" size="small" clearable>
            <el-option v-for="item in symbolSelection" :key="item" :label="item" :value="item"> </el-option>
          </el-select>
					</el-col>
					<el-col :span="6" prop="textValue">
						<el-input v-model="expression.textValue"
              size="small"
              placeholder="请输入内容">
            </el-input>
					</el-col>
          <el-col :span="3">
					<el-button
            @click.prevent="removeCondition(index)"
            size="mini"
            type="danger"
            >删除条件</el-button
      >
        </el-col>
				</el-row>
			</el-row>
			<el-row :gutter="50">
				<el-col :span="3" >
					<el-form-item>
						<el-button size="mini" type="primary" @click="addCondition()"
            >新增条件</el-button>
					</el-form-item>
				</el-col>
			</el-row>
  </el-form>
  <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="addExpressionDialogVisible = false"
          >取 消
        </el-button>
        <el-button size="mini" type="primary" @click="addExpressionfunction"
          >确定
        </el-button>
      </span>
  </el-dialog>
  </div>
</template>

<script type='text/javascript'>
import { cloneDeep } from 'lodash'
import { getAction } from '@/api/manage'
import AttributeForm from './attributesForm'
import { nw_postAction, nw_postAction1 } from '@/api/newWorkApi'
import axios from 'axios'
import qs from 'qs'
import { t_postAction, t_getAction } from '@/api/tempApi'
export default {
  props: {
    data: Object,
  },
  data() {
    return {
      formAllId: [],
      formRules:{
        formId: [
          {
            required: true,
            message: "不能为空",
            trigger: "change",
          },
        ],
        symbol: [
          {
            required: true,
            message: "不能为空",
            trigger: "change",
          },
        ],
        textValue: [
          {
            required: true,
            message: "不能为空",
            trigger: "blur",
          },
        ],
      },
      expressionNode:[{ //路由选择表达式 显示数据
        formId:'',
        symbol:'',
        textValue:''
      }],
      ruleModel:[{ //判断是否通过选择框
        source: false,
        name: "否"
      },{
        source: true,
        name: "是"
      }
      ],
      symbolSelection: ['>', '=', '<'],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      formThreeId: { formDesignerId: '', onlineTableId: '', onlineDataId: '' },
      visible: true,
      // node 或 line
      type: '',
      lineRuleModel:'', //连线判断结果
      expression:'',//连线判断表达式
      lineResult:{},
      if_expression: [],//路由选择表达式 传值数据
      addExpression:false,//表达式
      addExpressionDialogVisible:false,//表达式
      node: {
        id: '',
        type: '',
        name: '',
        left: '',
        top: '',
        button_permission:[ "通过","拒绝","退回"],
        state: '',
        feature: {},
        formDesignerId: '',
        onlineTableId: '',
        onlineDataId: '',
        label: '', // 可以为空
        hander_bean: '',
        event_handler_bean: '',
        assignment_type: 'ProcessPromoter',
        assignee: [],
        due: {
          day: '',
          hour: '',
          minute: '',
        },
        user_data: {
          key: '',
          value: '',
        },
        terminate: '',
        width: 0,
        height: 0,
        role: [],
      },
      line: {},
      data: {},
      attMenu1Open: true,
      attMenu2Open: true,
      roleList: [],
      beanList: [],
      formList: [],
      roleAssginee: {},
      userAssginee: [],
      roleAssgineeList: [],
      userAssgineeList: [],
      featureList: [],
      companyList: [],
      stateList: [
        {
          state: 'Completed',
          label: '已完成',
        },
        {
          state: 'waiting',
          label: '未完成',
        },
        {
          state: 'running',
          label: '进行中',
        }
      ],
    }
  },
  components: {
    AttributeForm,
  },
  created() {
    this.getAllRole()
    this.getFeatureList()
    this.getCompanyList()
    this.getAllForm()
    this.getAllBean()
    this.getAllRoleAndUser()
  },
  mounted() {},
  methods: {
    addExpressionfunc: function () {
      this.addExpressionDialogVisible = true
    },
    addExpressionfunction(){
      this.if_expression = [];
      this.expression = '';
      for(var i = 0 ; i < this.expressionNode.length ; i++){
        if(this.expressionNode[i].textValue != '' && this.expressionNode[i].formId != '' && this.expressionNode[i].symbol != ''){
          this.if_expression.push({
            textValue: this.expressionNode[i].textValue,
            symbol: this.expressionNode[i].symbol,
            formId: this.expressionNode[i].formId
          })
          for(var j = 0 ; j < this.formAllId.length ; j++){
            if(this.formAllId[j].id == this.expressionNode[i].formId && i==0){
              this.expression = this.formAllId[j].name + this.expressionNode[i].symbol + this.expressionNode[i].textValue;
            }else if (this.formAllId[j].id == this.expressionNode[i].formId ){
              this.expression = this.expression + " && " + this.formAllId[j].name + this.expressionNode[i].symbol + this.expressionNode[i].textValue
            }
          }
        }
      }
      this.expressionNode = this.if_expression;
      this.addExpressionDialogVisible = false;
    },
    addExpressionDialogClose(){},
    removeCondition(index){
      if(this.expressionNode.length > 1){
          this.expressionNode.splice(index,1);
        }
    },
    addCondition(){
      this.expressionNode.push({
        formId:'',
        symbol:'',
        textValue:''
      });
    },
    //得到角色
    getAllRole() {
      getAction('/sys/role/list?pageSize=100000').then((res) => {
        this.roleList = res.result.records
      })
    },
    //得到逻辑节点的可选择属性，也就是上一个任务节点的表单绑定属性
    getTaskForm(data, decision) {
      var formAllId = new Array()
      var taskId = ''
      var formId = ''
      for (var i = 0; i < data.lineList.length; i++) {
        var line = data.lineList[i]
        if (line.to == decision.id) {
          taskId = line.from
        }
      }
      data.nodeList.filter((node) => {
        if (node.id == taskId) {
          formId = node.formDesignerId
          if (formId == '') {
            this.$message.info('请上一个节点选择表单')
          } else {
            t_getAction(`/admin/desform/${formId}/getConent`)
              .then((res) => {
                const formJson = JSON.parse(res.result)
                for (let i = 0; i < formJson.list.length; i++) {
                  if (formJson.list[i].type == 'grid') {
                    for (let j = 0; j < formJson.list[i].columns.length; j++) {
                      for (let z = 0; z < formJson.list[i].columns[j].list.length; z++) {
                        if (formJson.list[i].columns[j].list[z].tableCol != '') {
                          var form = {}
                          form['name'] = formJson.list[i].columns[j].list[z].name
                          form['id'] = formJson.list[i].columns[j].list[z].tableCol
                          formAllId.push(form)
                        }
                      }
                    }
                  } else if (formJson.list[i].type == 'divider') {
                  } else if (formJson.list[i].type == 'fileupload') {
                  } else if (formJson.list[i].type == 'table') {
                    for (let k = 0; k < formJson.list[i].tableColumns.length; k++) {
                      var form = {}
                      form['name'] = formJson.list[i].tableColumns[k].name
                      form['id'] = formJson.list[i].tableColumns[k].tableCol
                      formAllId.push(form)
                    }
                  } else {
                    if (formJson.list[i].tableCol != '') {
                      var form = {}
                      form['name'] = formJson.list[i].name
                      form['id'] = formJson.list[i].tableCol
                      formAllId.push(form)
                    }
                  }
                }
                this.$nextTick((res) => {
                  this.formAllId = formAllId
                })
              })
              .catch((err) => {
              })
          }
        }
      })
    },
    closeAttributesForm() {
      // this.$refs.attributesForm1.close()
    },
    //得到所有的表单
    getAllForm() {
      var data = new FormData()
      // data.append('current', 1)
      //data.append('size', 22)
      data.append('deleteFlag', 1)
      // data.append('encoding', 'Scheduling')
      data.append('encoding', '')
      t_postAction('/admin/desform/searchAll', data).then((res) => {
        this.formList = res.result.list
      })
    },
    //得到所有的角色和用户
    getAllRoleAndUser() {
      nw_postAction1(`/task/assign`, { pageIndex: '1' }).then((res) => {
        if (res.result.result != undefined && res.result.result.length > 0) {
          for (var i = 0; i < res.result.result.length; i++) {
            var type = res.result.result[i].providerId
            nw_postAction1(`/task/assign`, { pageIndex: '1', pageSize: 9999, providerId: type }).then((res) => {
              switch (res.result.result.providerId) {
                case 'roleAssigneeProvider':
                  this.roleAssgineeList = res.result.result.inheritAssignee
                  break
                case 'userAssigneeProvider':
                  this.userAssgineeList = res.result.result.inheritAssignee
                  break
              }
            })
          }
        }
      })
    },
    //得到事件bean的列表
    getAllBean() {
      axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
      //这里handlerType的参数还可以为NodeEventHandler（目前只有）、ProcessEventHandler、DecisionHandler、AssignmentHandler、ConditionHandler、ActionHandler、ForeachHandler、ReminderHandler、CountersignHandler、TaskListener
      nw_postAction1('/task/handlerList', qs.stringify({ handlerType: 'NodeEventHandler' })).then((res) => {
        this.beanList = res.result
      })
    },
    //得到所有的Task节点的功能列表，例如选择video，这个节点赋予视频的角色，拥有独有的表单属性
    getFeatureList() {
      let params = {
        column: 'createTime',
        order: 'desc',
        pageNo: 1,
        pageSize: 1000,
        superQueryMatchType: 'and',
      }
      getAction('/online/cgform/api/getData/6fd14b6968f6428096f588d080a1d749', params).then((res) => {
        if (res.success) {
          this.featureList = res.result.records
        }
      })
    },
    //得到所有的Company节点的功能列表
    getCompanyList(){
      
    },
    /**
     * 表单修改，这里可以根据传入的ID进行业务信息获取
     * @param data
     * @param id
     */
    nodeInit(data, id) {
      this.roleAssginee = {}
      this.userAssginee = []
      this.type = 'node'
      this.data = data
      data.nodeList.filter((node) => {
        if (node.id === id) {
          if (node.formDesignerId != '' && node.formDesignerId != undefined && node.type === 'rectangle') {
            this.showForm(node)
          }
          if (node.type === 'decision') {
            this.expressionNode = node.if_expression; //从路由决策节点获得表达式
            this.lineResult = node.result; //获得路由决策结果
            this.getTaskForm(data, node)
          }
          if (node.type === 'task') {
            for (var i = 0; i < node.assignee.length; i++) {
              var assign = node.assignee[i]
              if (assign.providerId == 'userAssigneeProvider') {
                this.userAssginee = this.userAssginee.concat(assign)
              }
              if (assign.providerId == 'roleAssigneeProvider') {
                this.roleAssginee = assign
                break
              }
            }
          }
          this.node = cloneDeep(node)
        }
      })
    },
    //展示高级属性（用表单配的）
    showForm(node) {
      console.log('showForm')
      this.formThreeId = { formDesignerId: '', onlineTableId: '', onlineDataId: '' }
      this.formThreeId.formDesignerId = node.formDesignerId //表单ID
      if (node.onlineTableId != '' && node.onlineDataId != '') {
        this.formThreeId.onlineTableId = node.onlineTableId
        this.formThreeId.onlineDataId = node.onlineDataId
      }
      this.$nextTick(() => {
        this.$refs.attributesForm1.openForm(this.formThreeId)
      })
    },
    //改变节点的onlineDataId和onlineTableId
    changeOnline(onlineTableId, onlineDataId) {
      this.data.nodeList.filter((node) => {
        if (node.id === this.node.id) {
          node.onlineTableId = onlineTableId
          node.onlineDataId = onlineDataId
        }
      })
    },
    lineInit(line) {
      this.node = {};
      this.type = 'line';
      this.line = line;
      this.if_expression = [];
      this.expression = '';
      for(var key in this.lineResult){ //将路由决策填入连线
        if(key == this.line.label ){
          if( this.lineResult[key] == true){
            this.lineRuleModel = true;
          }else if(this.lineResult[key] == false){
            this.lineRuleModel = false;
          }
        }
      }
      for(var i = 0 ; i < this.expressionNode.length ; i++){ //将表达式填入
        if(this.expressionNode[i].textValue != '' && this.expressionNode[i].formId != '' && this.expressionNode[i].symbol != ''){
          this.if_expression.push({
            textValue: this.expressionNode[i].textValue,
            symbol: this.expressionNode[i].symbol,
            formId: this.expressionNode[i].formId
          })
          for(var j = 0 ; j < this.formAllId.length ; j++){
            if(this.formAllId[j].id == this.expressionNode[i].formId && i==0){
              this.expression = this.formAllId[j].name + this.expressionNode[i].symbol + this.expressionNode[i].textValue;
            }else if (this.formAllId[j].id == this.expressionNode[i].formId ){
              this.expression = this.expression + " && " + this.formAllId[j].name + this.expressionNode[i].symbol + this.expressionNode[i].textValue
            }
          }
        }
      }
    },
    // 修改连线
    saveLine() {   
      for( var i =0 ; i< this.data.nodeList.length ; i++){ //保存路由决策结果
        if(this.data.nodeList[i].type == "decision" ){
          if(this.data.nodeList[i].result == undefined){
            this.data.nodeList[i].result = new Object;
            this.data.nodeList[i].result[this.line.label] = this.lineRuleModel;
          }else {          
            this.data.nodeList[i].result[this.line.label] = this.lineRuleModel;
          }
        }
      }
      this.$emit('setLineLabel', this.line.from, this.line.to, this.line.label)
    },
    save() {
      this.data.nodeList.filter((node) => {
        if (node.id === this.node.id) {
          let type = this.node.type
          switch (type) {
            case 'start':
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.state = this.node.state
              node.label = this.node.label
              node.event_handler_bean = this.node.event_handler_bean
              node.handler_bean = this.node.handler_bean
              // node.g = this.node.g
              break
            case 'task':


              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.button_permission = this.node.button_permission
              if (this.node.feature.id != undefined) {
                node.feature = this.node.feature
                node.formDesignerId = this.node.feature.id
              }
              node.onlineTableId = ''
              node.onlineDataId = ''
              node.state = this.node.state
              node.event_handler_bean = this.node.event_handler_bean
              node.assignment_type = this.node.assignment_type
              if (node.assignment_type == 'ProcessPromoter') {
                node.assignee = []
              } else {
                node.assignment_type == 'Assignee'
                node.assignee = this.userAssginee
                if(JSON.stringify(this.roleAssginee) !== "{}") {
                  node.assignee.push(this.roleAssginee)
                }
              }
              node.due = this.node.due
              node.user_data = this.node.user_data
              break
            case 'decision':
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.event_handler_bean = this.node.event_handler_bean
              node.state = this.node.state
              node.if_expression = this.if_expression;
              break
            case 'end':
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.terminate = this.node.terminate
              node.state = this.node.state
              break
            case 'rectangle':
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.role = this.node.role
              if (this.node.feature.form_id != undefined) {
                node.feature = this.node.feature
                node.formDesignerId = this.node.feature.form_id
                this.showForm(node)
              }
              node.onlineTableId = ''
              node.onlineDataId = ''
              node.state = this.node.state
              break
            case 'swinlane':
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.width = this.node.width
              node.height = this.node.height
              node.state = this.node.state
              break
            case 'rhombus':
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.state = this.node.state
              break
            default:
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.state = this.node.state
          }
          this.$message.success('保存成功')
          //this.$emit('repaintEverything')
        }
      })
    },
  },
}
</script>

<style scoped>
.flex-col {
  display: flex;
  flex-direction: column;
}
.flex-row {
  display: flex;
  flex-direction: row;
}
.el-node-form-tag {
  position: absolute;
  top: 50%;
  margin-left: -15px;
  height: 40px;
  width: 15px;
  background-color: #fbfbfb;
  border: 1px solid rgb(220, 227, 232);
  border-right: none;
}
.attMenu2Div {
  margin-top: 10px;
  margin-right: 10px;
}
.el-date-editor.el-input {
  width: 100%;
}
/*节点菜单*/
.ef-node-pmenu {
  cursor: pointer;
  height: 32px;
  line-height: 32px;
  width: 100%;
  display: block;
  font-weight: bold;
  color: #4a4a4a;
  padding-left: 5px;
}

.ef-node-pmenu:hover {
  background-color: #e0e0e0;
}
.ef-node-menu-ul {
  list-style: none;
  padding-left: 10px;
}
/*node-form*/
.ef-node-form-header {
  height: 32px;
  border-top: 1px solid #dce3e8;
  border-bottom: 1px solid #dce3e8;
  background: #f1f3f4;
  color: #000;
  line-height: 32px;
  padding-left: 12px;
  font-size: 14px;
}

.ef-node-form-body {
  margin-top: 10px;
  padding-right: 10px;
}
/*属性头部样式*/
.node-form-header {
  height: 48px;
  background-color: rgba(255, 255, 255, 1);
  width: 293px;
  justify-content: center;
  align-items: flex-start;
  padding-left: 24px;
}

.node-form-header-content {
  z-index: auto;
  width: 253px;
  height: 22px;
  justify-content: space-between;
}

.node-form-header-icon1 {
  width: 20px;
  height: 20px;
  margin-top: 1px;
}

.node-form-header-text {
  width: 64px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(51, 51, 51, 1);
  font-size: 16px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 22px;
  text-align: left;
  margin-left: 4px;
}

.node-form-header-icon2 {
  width: 16px;
  height: 16px;
  margin: 2px 0 0 170px;
}

/*属性头部样式结束*/
/*属性内容样式*/
.form-content {
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 1);
  width: 290px;
  justify-content: flex-start;
  padding-top: 24px;
  margin-left: 24px;
  max-height: 380px;
  overflow-y: scroll;
}
.form-content1 {
  margin-right: 20px;
}
.form-content-item {
  z-index: auto;
  width: 293px;
  height: 424px;
}

.ant-form-item-label {
  z-index: auto;
  width: 245px;
  height: 40px;
  margin-left: 24px;
  justify-content: space-between;
}

.form-content-item-text {
  width: 60px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(51, 51, 51, 1);
  font-size: 14px;
  white-space: nowrap;
  line-height: 20px;
  text-align: left;
  margin-top: 10px;
}

.form-content-item-input {
  height: 40px;
  border-radius: 4px;
  border-width: 1px;
  border: 1px solid rgba(240, 240, 240, 1);
  background-color: rgba(245, 247, 250, 1);
  width: 200px;
  justify-content: center;
  align-items: flex-start;
  padding-left: 12px;
  margin-left: 20px;
}
/*属性内容样式结束*/
/*按钮样式开始 */
.form-button {
  z-index: auto;
  width: 154px;
  height: 32px;
  justify-content: space-between;
}
.form-button-save {
  height: 32px;
  border-radius: 4px;
  border-width: 1px;
  border: 1px solid rgba(23, 145, 255, 1);
  background: #fbfbfb;
  width: 65px;
  justify-content: center;
  align-items: center;
}

.form-button-save-text {
  z-index: 82;
  width: 33px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(23, 145, 255, 1);
  font-size: 14px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}
.form-button-reset {
  z-index: 84;
  height: 32px;
  border-radius: 4px;
  background: #fbfbfb;
  border-width: 1px;
  border: 1px solid rgba(217, 217, 217, 1);
  width: 65px;
  justify-content: center;
  align-items: center;
}

.form-button-reset-text {
  z-index: 85;
  width: 33px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(102, 102, 102, 1);
  font-size: 14px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}

/*按钮样式结束 */
.flow-form-line {
  z-index: 135;
  width: 317px;
  height: 1px;
  background-color: rgba(240, 240, 240, 1);
}

.el-form-special >>> .el-input--small {
  width: 120px;
}
</style>
