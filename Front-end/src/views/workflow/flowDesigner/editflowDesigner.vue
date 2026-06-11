
<template>
  <div id="editflowdesigner"></div>
</template>
<script>
import '../../../../node_modules/jquery/dist/jquery.min.js'
window.jQuery = require('jquery')
import '../../../../node_modules/bootstrap/dist/js/bootstrap.js'
import '../../../../node_modules/bootstrap/dist/css/bootstrap.min.css'
import './css/uflo.css'
import './css/iconfont.css'
import $ from 'jquery'
import axios from 'axios'
import Vue from 'vue'
import { ACCESS_TOKEN , TENANT_ID } from '@/store/mutation-types'
import { FlowDesigner } from 'flowdesigner'
import StartTool from './js/StartTool.js'
import TaskTool from './js/TaskTool.js'
import CountersignTaskTool from './js/CountersignTaskTool.js'
import ActionTool from './js/ActionTool.js'
import SubprocessTool from './js/SubprocessTool.js'
import DecisionTool from './js/DecisionTool.js'
import ForkTool from './js/ForkTool.js'
import ForeachTool from './js/ForeachTool.js'
import JoinTool from './js/JoinTool.js'
import EndTool from './js/EndTool.js'
import EndTerminalTool from './js/EndTerminalTool.js'
import UfloDesigner from './js/UFLODesigner.js'

import StartNode from './js/StartNode.js'
import EndNode from './js/EndTerminalNode.js'
import TaskNode from './js/TaskNode.js'
import BaseNode from './js/BaseNode.js'

import OpenDialog from './dialog/OpenDialog.js'
import SaveDialog from './dialog/SaveDialog.js'
import PurchaseNode from './js/antenna/PurchaseNode.js'
import PurchaseTool from './js/antenna/PurchaseTool.js'

import { getParameter } from './js/Utils.js'
import { MsgBox } from 'flowdesigner'
import { Message } from 'element-ui'
import { w_postAction, w_getAction } from '@/api/workapi'
import api from '../../../api/index.js'

export default {
  name: 'editflowDesigner',
  props: ['editName'],
  data() {
    return {
    }
  },
  methods: {
    //关闭编辑流程模板页面
    returnFlowDesign() {
      var _this = this
      this.$elementConfirm('是否关闭编辑界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          _this.$emit('close-editflow')
        })
        .catch(() => {})
    },
  },
  mounted() {
    console.log('edit')
    console.log(this.editName)
    var editName = this.editName
    var _this = this
    name = this.$route.params.name
    $(document).ready(function () {
      const designer = new UfloDesigner('editflowdesigner')
      designer.addTool(new StartTool())//开始
      designer.addTool(new TaskTool())//人工节点
      //designer.addTool(new PurchaseTool())
      //designer.addTool(new CountersignTaskTool());
      //designer.addTool(new ActionTool());
      designer.addTool(new SubprocessTool())//子流程
      designer.addTool(new DecisionTool())//路由决策
      designer.addTool(new ForkTool())//分支
      //designer.addTool(new ForeachTool());
      designer.addTool(new JoinTool())//聚合
      //designer.addTool(new EndTool());
      designer.addTool(new EndTerminalTool())//结束流程
      buildButtons(designer) //赋予点击事件
      designer.buildDesigner() //显示按钮 渲染设计器
      loadFile(designer, editName)
    })
    //打开流程模板
    function loadFile(designer, editName) {
      if (!editName) {
        return
      }
      let name = decodeURI(editName)
      designer.setInfo(name)
      designer.processFile = name
      const fullName = name + '.uflo.xml'
      w_postAction(`/uflo/designer/openFile?name=` + fullName)
        .then((res) => {
          designer.fromJson(res)//formJson是super方法
        })
        .catch((error) => {
          _this.$message({
            type: 'error',
            message: '加载流程模版文件失败:',
          })
        })
    }
    function buildButtons(designer) {
     //增加返回按钮
      designer.addButton({
        icon: '<i class="uflo uflo-delete"></i>',
        tip: '返回',
        click: function () {
          _this.returnFlowDesign()
        },
      })
       //增加保存按钮
      const saveDialog = new SaveDialog()
      designer.addButton({
        icon: '<i class="uflo uflo-save"></i>',
        tip: '保存流程模板文件',
        click: function () {
          //判断开始和结束节点
          let startFlag = ''
          for (let figure of designer.context.allFigures) {
            if (figure instanceof StartNode) {
              startFlag = '1'
            }
          }
          if (!(startFlag == '1')) {
            MsgBox.alert('请设置开始节点')
            return
          }
          let endFlag = ''
          for (let figure of designer.context.allFigures) {
            if (figure instanceof EndNode) {
              endFlag = '1'
            }
          }
          if (!(endFlag == '1')) {
            MsgBox.alert('请设置结束节点')
            return
          }
          if (!designer.validate()) {
            //调用super方法，判断各个节点的名称是否填写，节点之间是否连接
            return
          }
          if (!designer.name || designer.name.length < 1) {
            MsgBox.alert('流程名称不能为空')
            return
          }
          for (var i = 0; i < designer.context.allFigures.length - 1; i++) {
            if (!(designer.context.allFigures[i] instanceof BaseNode)) {
              continue
            }
            for (var j = i + 1; j < designer.context.allFigures.length; j++) {
              if (!(designer.context.allFigures[j] instanceof BaseNode)) {
                continue
              }
              if (designer.context.allFigures[i].name === designer.context.allFigures[j].name) {
                MsgBox.alert('当前流程中新增的' + designer.context.allFigures[j].name + '节点名称已存在，请修改后保存')
                return
              }
            }
          }
          for (let figure of designer.context.allFigures) {
            if (figure instanceof TaskNode) {
              if (figure.dueDefinition) {
                if (
                  figure.dueDefinition.day === '' ||
                  figure.dueDefinition.hour === '' ||
                  figure.dueDefinition.minute === ''
                ) {
                  console.log(figure.dueDefinition, '030300')
                  MsgBox.alert('请为' + figure.name + '设置完整的超时时间')
                  return
                } else if (
                  figure.dueDefinition.day == 0 &&
                  figure.dueDefinition.hour == 0 &&
                  figure.dueDefinition.minute == 0
                ) {
                  MsgBox.alert(figure.name + '超时时间设置不合理，请重新设置')
                  return
                }
              }
            }
          }
          // for (let figure of designer.context.allFigures) {
          //   //遍历背景中所有的结点
          //   if (figure instanceof StartNode || figure instanceof TaskNode) {
          //     if (!figure.url) {
          //       var r = window.confirm(figure.name + '节点操作内容为空，是否确定提交？')
          //       if (r == false) {
          //         return
          //       }
          //     }
          //   }
          // }
          let content = designer.toXML()
          if (!content) {
            return
          }
          if (!designer.processFile) {//判断该模板有么有名称
            saveDialog.show(designer, function (fileName) {//没有名称就在saveDialog里面取名保存
            //回调
              designer.processFile = fileName
              designer.setInfo(fileName)
            })
          } else {//有就直接保存
            let fullName = designer.processFile
            //let fullName = provider.prefix + name;
            if (fullName.indexOf('.uflo.xml') === -1) {
              fullName += '.uflo.xml'
            }
            axios.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest'
            w_postAction(`/uflo/designer/saveFile?fileName=`+fullName+'&content='+content)
              .then((res) => {
                _this.$message({
                  type: 'success',
                  message: '保存成功',
                })
              })
              .catch((error) => {
                _this.$message({
                  type: 'error',
                  message: '保存失败',
                })
              })
          }
        },
      })
      //增加另存按钮
      designer.addButton({
        icon: '<i class="uflo uflo-saveas"></i>',
        tip: '另存流程模板文件',
        click: function () {
          let content = designer.toXML()
          if (!content) {
            return
          }
          content = encodeURIComponent(content)
          saveDialog.show(designer, function (fileName) {
            designer.processFile = fileName
            designer.setInfo(fileName)
          })
        },
      })
    }
  },
}
</script>
<style scoped>
.header {
  height: 50px;
  width: 100%;
  background-color: #409eff;
}
.header .header-span {
  color: #ffffff;
  display: block;
  line-height: 50px;
  text-align: center;
  font-size: 18px;
  padding-left: 10px;
}
</style>