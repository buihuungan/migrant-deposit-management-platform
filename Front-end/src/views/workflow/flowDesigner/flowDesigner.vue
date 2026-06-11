<template>
  <div>
    <div id="designer"></div>
    <!-- <form-content></form-content> -->
  </div>
</template>

<script>
import axios from 'axios'
import CusDialog from '../../../components/FormMaking/components/CusDialog'
// //import bootbox from '../../../../node_modules/bootbox/bootbox.js';
import '../../../../node_modules/jquery/dist/jquery.min.js'
window.jQuery = require('jquery')
// require('bootstrap')
import '../../../../node_modules/bootstrap/dist/js/bootstrap.js'
import '../../../../node_modules/bootstrap/dist/css/bootstrap.min.css'
import './css/uflo.css'
import './css/iconfont.css'
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

import OpenDialog from './dialog/OpenDialog.js'
import SaveDialog from './dialog/SaveDialog.js'

import PurchaseNode from './js/antenna/PurchaseNode.js'
import PurchaseTool from './js/antenna/PurchaseTool.js'
import { ACCESS_TOKEN , TENANT_ID } from '@/store/mutation-types'
import { getParameter } from './js/Utils.js'
import { MsgBox } from 'flowdesigner'
import { Message } from 'element-ui'
import { w_postAction } from '@/api/workapi'
import $ from 'jquery'
import api from '../../../api/index.js'

export default {
  name: 'flowDesigner',
  components: { CusDialog },
  data() {
    return {
    }
  },

  methods: {
    //关闭新增流程模板页面
    returnFlowDesign() {
      var _this = this
      this.$elementConfirm('是否关闭新增界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          _this.$emit('close-addflow')
        })
        .catch(() => {})
    },
  },
  mounted() {
    var _this = this
    window.returnFlowDesign = _this.returnFlowDesign
    const designer = new UfloDesigner('designer')
    designer.addTool(new StartTool())//开始
    designer.addTool(new TaskTool())//人工节点
    //designer.addTool(new PurchaseTool())
    //designer.addTool(new CountersignTaskTool());
    //designer.addTool(new ActionTool());
    designer.addTool(new SubprocessTool())//子流程
    designer.addTool(new DecisionTool()) //路由决策
    designer.addTool(new ForkTool())//分支
    //designer.addTool(new ForeachTool());
    designer.addTool(new JoinTool())//聚合
    //designer.addTool(new EndTool());
    designer.addTool(new EndTerminalTool()) //结束流程
    buildButtons(designer) //赋予点击事件
    designer.buildDesigner() //显示按钮
    //loadFile(designer)
    function loadFile(designer) {
      let name = getParameter('p') //返回流程的名字
      if (!name) {
        return
      }
      name = decodeURI(decodeURI(name))
      designer.setInfo(name)
      designer.processFile = name
      let url = 'apilocal/designer/open-file'
      $.ajax({
        url,
        data: { name, token: tokenval },
        success: (process) => {
          designer.fromJson(process)
        },
        error: (response) => {
          if (response && response.responseText) {
            MsgBox.alert('加载流程模版文件失败:' + response.responseText)
          } else {
            MsgBox.alert('加载流程模版文件失败')
          }
        },
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
        tip: '保存流程模版文件',
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
          if (!designer.validate()) {//调用super方法，判断各个节点的名称是否填写，节点之间是否连接
            return
          }
          if (!designer.name || designer.name.length < 1) {
            MsgBox.alert('流程名称不能为空!')
            return
          }
          for (let figure of designer.context.allFigures) {
            if (figure instanceof TaskNode) {
              if (figure.dueDefinition) {
                //if((!Number(figure.dueDefinition.day))  && (!Number(figure.dueDefinition.hour))  && (!Number(figure.dueDefinition.minute))){
                if (
                  figure.dueDefinition.day === '' ||
                  figure.dueDefinition.hour === '' ||
                  figure.dueDefinition.minute === ''
                ) {
                  console.log(figure.dueDefinition, '7007007070')
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
          //       var r = confirm(figure.name + '节点操作内容为空，是否确定提交？')
          //       if (r == false) {
          //         return
          //       }
          //     }
          //   }
          // }
          let content = designer.toXML()
          console.log('xml1结束', content)
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
            const fullName=designer.processFile;
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
        tip: '另存流程模版文件',
        click: function () {
          let content = designer.toXML()
          if (!content) {
            return
          }
          content = encodeURI(content)
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
#designer {
  border: 1px;
  width: 100%;
  height: 100%;
}
</style>