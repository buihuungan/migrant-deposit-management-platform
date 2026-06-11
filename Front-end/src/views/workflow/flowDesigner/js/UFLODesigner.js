/**
 * Created by Jacky.Gao on 2017-07-05.
 */
import {
  Node,
  FlowDesigner,
  MsgBox
} from 'flowdesigner';
import BaseNode from './BaseNode.js';
//import EventSelectDialog from '../dialog/EventSelectDialog.js';
import UrlSelectDialog from '../dialog/UrlSelectDialog.js';
import {t_postAction,t_getAction} from '@/api/tempApi'

export default class UfloDesigner extends FlowDesigner {
  constructor(containerId) {
    super(containerId);
    this.setInfo('未保存文件');
  }
  toXML() { //生成当前流程的XML文档
    console.log("this.context.allFigures", this.context.allFigures)
    // if(!this.url){
    //     MsgBox.alert('当前流程url为空，请为当前流程设置Url？');
    //     return;
    // }
    let xml = '<?xml version="1.0" encoding="utf-8"?>';
    xml += `<uflo-process name="${this.name}"`;
    // if(this.key){
    //     xml+=` key="${this.key}"`
    // }
    //xml+=` start-process-url="formDesignerName:${this.formflag.url}#formDesignerId:${this.formflag.templateld}" `;
    // if(this.categoryId){
    //     xml+=` category-id="${this.categoryId}"`;
    // }
    // if(this.category){
    //     xml+=` category="${this.category}"`;
    // }
    // if(this.eventHandlerBean){
    //     xml+=` event-handler-bean="${this.eventHandlerBean}"`
    // }
    // if(this.effectDate){
    //     xml+=` effect-date="${this.effectDate}"`;
    // } 
    if (this.description) {
      xml += ` description="${this.description}"`;
    }
    xml += `>`;
    for (let figure of this.context.allFigures) { //遍历背景中所有的结点   
      if (!(figure instanceof BaseNode)) {
        continue;
      }
      console.log(figure); 
      xml += figure.toXML();
      console.log(xml); 
    }
    xml += '</uflo-process>';
    return xml;
  }
  fromJson(json) {
    this.name = json.name;
    //this.key=json.key;
    this.url=json.formflag;
    // this.eventHandlerBean=json.eventHandlerBean;
    // this.categoryId=json.categoryId;
    // this.category=json.category;
    // this.effectDate=json.effectDate;
    this.description = json.description;
    for (let nodeJson of json.nodes) {
      console.log(nodeJson);
      nodeJson.w = nodeJson.width;
      nodeJson.h = nodeJson.height;
      switch (nodeJson.type) {
        case "Action":
          nodeJson.type = '动作';
          break;
        case "Decision":
          nodeJson.type = '路由决策';
          break;
        case "Purchase":
          nodeJson.type = '物料采购';
          break;
        case "Task":
          nodeJson.type = '人工节点';
          //nodeJson.type = '人工任务';改变这个可以直接改变编辑界面打开的样子，是要人工节点还是别的节点
          break;
        case "CountersignTask":
          nodeJson.type = '会签任务';
          break;
        case "Foreach":
          nodeJson.type = '动态分支';
          break;
        case "Subprocess":
          nodeJson.type = '子流程';
          break;       
        case "End":
          nodeJson.type = '结束分支';
          break;
        case "Start":
          nodeJson.type = '开始';
          break;
        case "TerminalEnd":
          nodeJson.type = '结束流程';
          break;
        case "Fork":
          nodeJson.type = '分支';
          break;
        case "Join":
          nodeJson.type = '聚合';
          break;
      }
      console.log(nodeJson);
      nodeJson.connections = nodeJson.sequenceFlows || [];
      for (let conn of nodeJson.connections) {
        conn.to = conn.toNode;
      }
      this.addNode(nodeJson);
      console.log(nodeJson);
    }
    for (let figure of this.context.allFigures) {
      if (!(figure instanceof Node)) {
        continue;
      }
      figure._buildConnections();
    }
    console.log('uflofesigner');
  }

  buildTaskForm(target) {
    const group = $(`<div class="form-group uflo-group"></div>`);
    // const addButton = $(`<button class="btn btn-primary" type="button" style="margin-bottom: 3px;float: right">添加表单</button>`);
    // group.append(addButton);
    //表单名称下拉框
    const selectForm = $(`<div class="modal-body">表单名称:<select id="formName"></select></div>`)
    var data = new FormData()
    data.append('current', 1)
    data.append('size', 22)
    data.append('deleteFlag', 1)
    data.append('encoding', "Leave")
    t_postAction("/admin/desform/search", data).then(res => {
      for (let i = 0; i < res.result.list.length; i++) {
        $('#formName').append(`<option value = ${res.result.list[i].id}> ${res.result.list[i].name} </option>`)
      }
    })
    group.append(selectForm);
    return group;
  }

  getform(formName,target) { //获取表单json数据
    var selectedForm = ""; //tempatedId
    var selectedUrl = "";//url
    var isTable = 0;
    formName[0].onchange = function () {//下拉框改变值时，所执行的函数
      var index = formName[0].selectedIndex; // 选中索引
      selectedForm = formName[0].options[index].value;//tempatedId
      selectedUrl = formName[0].options[index].text;//
      if(selectedForm != ""){
        isTable = 1;
      }
      console.log(selectedUrl)
      target.formflag={url:selectedUrl,isTable:isTable,templateld:selectedForm}//添加到xml文件中

      t_getAction('/admin/desform/' + selectedForm + '/getConent').then(res => {
        selected = res.result;
        showForm(selected);
      })

    }
  }

  buildProperties(target) {
    const g = $('<div></div>');
    const flowNameGroup=$(`<div class="form-group"><label>流程名称：</label></div>`);
    g.append(flowNameGroup);
    const flowNameText=$(`<input type="text" class="form-control uflo-text-editor" style="width: 300px">`);
    flowNameGroup.append(flowNameText);
    flowNameText.val(target.name);
    flowNameText.change(function(){
        target.name=$(this).val();
    });

    // const keyGroup=$(`<div class="form-group"><label>关键字：</label></div>`);
    // g.append(keyGroup);
    // const keyText=$(`<input type="text" class="form-control uflo-text-editor" style="width: 315px">`);
    // keyGroup.append(keyText);
    // keyText.val(target.key);
    // keyText.change(function(){
    //    target.key=$(this).val();
    // });

    //const urlGroup=$(`<div class="form-group uflo-group"><label>URL：</label></div>`);
    //g.append(this.buildTaskForm(target));
    const taskForm = $(`<div class="form-group uflo-group"></div>`);
    taskForm.append(this.buildTaskForm(target));
    //g.append(taskForm);
    this.getform(taskForm.find("#formName"),target)
    console.log(this.formflag)
    //this.getform(taskForm.find("#formName"),target)
    //  const urlGroup=$(`<div class="form-group uflo-group"><label>URL：</label></div>`);
    //  g.append(urlGroup);
    //  const tip="请为当前流程设置url";
    //  const urlText=$(`<input type="text" class="form-control uflo-text-editor" title="${tip}" value="${target.startProcessUrl || ''}" placeholder="请为当前节点设置url" style="width: 240px">`);
    //  urlGroup.append(urlText);
    // urlText.val(target.startProcessUrl);
    // const urlSelectButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px;">选择</button>`);
    // urlGroup.append(urlSelectButton);
    // urlText.change(function(){
    //     target.startProcessUrl=$(this).val();
    // });
    // const dialog1=new UrlSelectDialog(`${tip}`);
    // urlSelectButton.click(function(){
    //     dialog1.show('ProcessUrlHandler',function(urlId){
    //         urlText.val(urlId);
    //         target.startProcessUrl=urlId;                
    //     });
    // }); 
    //g.append(this.buildTaskForm(target));
    //this.getform(taskForm.find("#formName"),target)
    // const eventGroup=$(`<div class="form-group"><label>事件Bean：</label></div>`);
    // g.append(eventGroup);
    // const tip="一个实现了com.bstek.uflo.process.handler.ProcessEventHandler接口并配置到Spring中的Bean ID"
    // const eventText=$(`<input type="text" class="form-control uflo-text-editor" title="${tip}" style="width: 240px">`);
    // eventGroup.append(eventText);
    // eventText.val(target.eventHandlerBean);
    // const eventSelectButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px;">选择</button>`);
    // eventGroup.append(eventSelectButton);
    // eventText.change(function(){
    //     target.eventHandlerBean=$(this).val();
    // });
    // const dialog=new EventSelectDialog(`选择${tip}`);
    // eventSelectButton.click(function(){
    //     dialog.show('ProcessEventHandler',function(beanId){
    //         target.eventHandlerBean=beanId;
    //         eventText.val(beanId);
    //     });
    // });

    // const categoryIdGroup=$(`<div class="form-group"><label>分类ID：</label></div>`);
    // g.append(categoryIdGroup);
    // const categoryIdText=$(`<input type="text" class="form-control uflo-text-editor" style="width: 315px">`);
    // categoryIdGroup.append(categoryIdText);
    // categoryIdText.val(target.categoryId);
    // categoryIdText.change(function(){
    //     target.categoryId=$(this).val();
    // });

    // if(window._categories.length>0){
    //     const categoryGroup=$(`<div class="form-group"><label>业务分类：</label></div>`);
    //     g.append(categoryGroup);
    //     const categorySelect=$(`<select class="form-control uflo-text-editor" style="width: 300px"></select>`);
    //     for(let category of window._categories){
    //         categorySelect.append(`<option value="${category}">${category}</option>`);
    //     }
    //     categoryGroup.append(categorySelect);
    //     categorySelect.val(target.category);
    //     categorySelect.change(function(){
    //         target.category=$(this).val();
    //     });
    // }

    // const effectDateGroup=$(`<div class="form-group"><label>生效日期：</label></div>`);
    // g.append(effectDateGroup);
    // const effectDateText=$(`<input type="datetime" placeholder="日期格式为yyyy-MM-dd HH:mm:ss" title="日期格式为yyyy-MM-dd HH:mm:ss" class="form-control uflo-text-editor" style="width: 300px">`);
    // effectDateGroup.append(effectDateText);
    // effectDateText.val(target.effectDate);
    // effectDateText.change(function(){
    //     target.effectDate=$(this).val();
    // });

    const descGroup = $(`<div class="form-group"><label style="vertical-align: top">描述：</label></div>`);
    g.append(descGroup);
    const descText = $(`<textarea rows="5" class="form-control uflo-text-editor" placeholder="描述内容修改后请点击属性面板空白处保存" style="width: 330px;height:80px !important"></textarea>`);
    descGroup.append(descText);
    descText.val(target.description);
    var timer = setTimeout(() => { //定时器用来解决对象异步加载，target.description的值取不到的问题
      descText.val(target.description);
    }, 500);
    descText.change(function () {
      target.description = $(this).val();
    });

    return g;
  }

  getPropertiesProducer() {
    const _this = this;
    return function () {
      const g = $('<div></div>');
      g.append(_this.buildProperties(this));
      return g;
    }
  }
}