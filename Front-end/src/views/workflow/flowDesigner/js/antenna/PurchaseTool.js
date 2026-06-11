/**
 * Created by Jacky.Gao on 2017-07-06.
 */
import BaseTool from '../BaseTool.js';
import PurchaseNode from './PurchaseNode.js';
import AssignProviderListDialog from '../../dialog/AssignProviderListDialog.js';
import EventSelectDialog from '../../dialog/EventSelectDialog.js';
import UrlSelectDialog from '../../dialog/UrlSelectDialog.js';
//import CalendarSelectDialog from '../../dialog/CalendarSelectDialog.js';
import CustomDataDialog from '../../dialog/CustomDataDialog.js';
import ComponentAuthorityDialog from '../../dialog/ComponentAuthorityDialog.js';
import introduceImage from "../../images/antenna/purchase1.jpg";
import "../../css/node.css"
import { t_postAction,t_getAction } from '../../../../../api/tempApi.js'
let selected = " "

export default class PurchaseTool extends BaseTool {
  getType() {
    return '人工任务';
  }
  getIcon() {
    return `<i class="uflo purchaseNode" style="color:#737383"></i>`
  }
  newNode() {
    return new PurchaseNode();
  }
  getConfigs() {
    return {
      in: -1,
      out: -1,
      single: false,
    };
  }
  buildTaskBasicConfig(target) {
    const group = $(`<fieldset style="padding: 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px">
        <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;">任务配置</legend></fieldset>`);
    // const taskNameGroup=$(`<div class="form-group uflo-group"><label>节点任务名称：</label></div>`);
    // const taskNameEditor=$(`<input type="text" class="form-control uflo-text-editor" value="${target.taskName || ''}" style="width:250px">`);
    // taskNameGroup.append(taskNameEditor);
    // group.append(taskNameGroup);
    // taskNameEditor.change(()=>{
    //     target.taskName=taskNameEditor.val();
    // });

    const urlGroup = $(`<div class="form-group uflo-group"><label>操作内容：</label></div>`);
    group.append(urlGroup);
    const tip = "提示"
    const urlText = $(`<input type="text" id="url-text" class="form-control uflo-text-editor" value="物料采购" style="width: 220px">`);
    urlGroup.append(urlText);
    //urlText.val(target.url);
    const urlSelectButton = $(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px;">选择</button>`);
    //urlGroup.append(urlSelectButton);
    // urlText.change(function(){
    //     target.url=$(this).val();
    // });
    target.url = "物料采购";
    const dialog = new UrlSelectDialog(`${tip}`);
    urlSelectButton.click(function () {
      dialog.show('ProcessUrlHandler', function (urlId) {
        urlText.val(urlId);
        target.url = urlId;
      });
    });
    // const listenerGroupContainer=$(`<div class="form-group uflo-group" style="margin-bottom: 0px !important;"><label>监听Bean：</label></div>`);
    // const listenerGroup=$(`<span class="input-group" style="display: inline-block;width: 270px;"></span>`);
    // listenerGroupContainer.append(listenerGroup);
    // const listenerEditor=$(`<input type="text" style="width:220px" value="${target.taskListenerBean || ''}" class="form-control uflo-text-editor" title="一个配置在Spring中，实现了com.bstek.uflo.process.listener.TaskListener接口的Bean的ID" placeholder="一个配置在Spring中，实现了com.bstek.uflo.process.listener.TaskListener接口的Bean的ID">`);
    // group.append(listenerEditor);
    // const inputGroup=$(`<span class="input-group-btn" style="vertical-align: middle;display: inline-block;"></span>`);
    // listenerGroup.append(inputGroup);
    // const selectListenerButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;">选择</button>`);
    // inputGroup.append(selectListenerButton);
    // const selectDialog=new EventSelectDialog('选择一个实现了TaskListener接口的Bean的ID');
    // selectListenerButton.click(function(){
    //     selectDialog.show('TaskListener',function(beanId){
    //         listenerEditor.val(beanId);
    //         target.taskListenerBean=beanId;
    //     });
    // });
    // group.append(listenerGroupContainer);
    // listenerEditor.change(()=>{
    //     target.taskListenerBean=listenerEditor.val();
    // });

    return group;
  }

  buildTaskAssign(target) {
    const group = $(`<div class="form-group uflo-group"></div>`);

    // const promoterGroup=$(`<div class="radio"></div>`);
    // group.append(promoterGroup);
    // const promoterRadio=$(`<label><input type="radio" name="task_assign_type_radio" ${target.assignmentType === 'ProcessPromoter' ? 'checked' : ''}>流程发起人</label>`);
    // promoterGroup.append(promoterRadio);
    // const specifyGroup=$(`<div class="radio"></div>`);
    // group.append(specifyGroup);
    // const specifyRadio=$(`<label><input type="radio" ${target.assignmentType === 'Assignee' ? 'checked' : ''} name="task_assign_type_radio">指定参与者</label>`);
    // specifyGroup.append(specifyRadio);
    // const specifyTableGroup=this.buildSpecifyAssignTable(target);
    // specifyGroup.append(specifyTableGroup);

    const promoterGroup = $(`<div class="radio"></div>`);
    group.append(promoterGroup);
    const promoterRadio = $(`<label><input type="radio" name="task_assign_type_radio" ${target.assignmentType === 'ProcessPromoter' ? 'checked' : ''}>流程发起人</label>`);
    promoterGroup.append(promoterRadio);

    const specifyGroup = $(`<div class="radio"></div>`);
    group.append(specifyGroup);
    const specifyRadio = $(`<label><input type="radio" name="task_assign_type_radio" ${target.assignmentType === 'Assignee' ? 'checked' : ''}>指定参与者</label>`);
    specifyGroup.append(specifyRadio);
    const specifyTableGroup = this.buildSpecifyAssignTable(target);
    specifyGroup.append(specifyTableGroup);


    // if(target.url === '采集'){
    //     target.assignmentType = 'Assignee';
    //     specifyRadio.children('input').attr('checked',true);
    // }else{
    //     target.assignmentType = 'ProcessPromoter';
    //     promoterRadio.children('input').attr('checked',true);
    // }

    // const elGroup=$(`<div class="radio"></div>`);
    // group.append(elGroup);
    // const elRadio=$(`<label><input type="radio" ${target.assignmentType === 'Expression' ? 'checked' : ''} name="task_assign_type_radio">EL表达式</label>`);
    // elGroup.append(elRadio);
    //const elEditor=$(`<input class="form-control uflo-text-editor" style="margin-left: 10px;width: 280px;">`);
    // elGroup.append(elEditor);
    // elEditor.val(target.expression || '');
    // elEditor.change(function(){
    //     target.expression=$(this).val();
    // });

    // const beanGroup=$(`<div class="radio"></div>`);
    // group.append(beanGroup);
    // const beanRadio=$(`<label><input type="radio" ${target.assignmentType === 'Handler' ? 'checked' : ''} name="task_assign_type_radio">提定Bean</label>`);
    // beanGroup.append(beanRadio);
    // const beanEditor=$(`<input type="text" title="一个实现了com.bstek.uflo.process.handler.AssignmentHandler接口配置在Spring中的Bean的id" class="form-control uflo-text-editor" value="${target.assignmentHandlerBean || ''}"  style="margin-left: 10px;width: 220px;">`);
    // beanGroup.append(beanEditor);
    // beanEditor.change(function(){
    //     target.assignmentHandlerBean=$(this).val();
    // });
    // const beanSelectButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px">选择</button>`);
    // beanGroup.append(beanSelectButton);
    // const beanSelectDialog=new EventSelectDialog('选择一个实现了com.bstek.uflo.process.handler.AssignmentHandler接口配置在Spring中的Bean的id');
    // beanSelectButton.click(function(){
    //     beanSelectDialog.show('AssignmentHandler',function(beanId){
    //         beanEditor.val(beanId);
    //         target.assignmentHandlerBean=beanId;
    //     });
    // });

    // const allowGroup=$(`<div class="form-group uflo-group"><label>允许上一节点为当前任务节点指定处理人：</label></div>`);
    // group.append(allowGroup);
    // const yesRadio=$(`<span><input type="radio" ${target.allowSpecifyAssignee ? 'checked' : ''} name="allow_specify_assign">是</span>`);
    // allowGroup.append(yesRadio);
    // const noRadio=$(`<span><input type="radio" ${!target.allowSpecifyAssignee ? 'checked' : ''} name="allow_specify_assign" style="margin-left: 10px">否</span>`);
    // allowGroup.append(noRadio);
    // yesRadio.children('input').click(function(){
    //     target.allowSpecifyAssignee=true;
    // });

    // noRadio.children('input').click(function(){
    //     target.allowSpecifyAssignee=false;
    // });

    switch (target.assignmentType) {
      case 'ProcessPromoter':
        specifyTableGroup.hide();
        //elEditor.attr('readOnly',true);
        //beanEditor.attr('readOnly',true);
        //beanSelectButton.attr('disabled',true);
        break;
      case 'Expression':
        specifyTableGroup.hide();
        //elEditor.attr('readOnly',false);
        //beanEditor.attr('readOnly',true);
        //beanSelectButton.attr('disabled',true);
        break;
      case 'Handler':
        specifyTableGroup.hide();
        //elEditor.attr('readOnly',true);
        //beanEditor.attr('readOnly',false);
        //beanSelectButton.attr('disabled',false);
        break;
      case 'Assignee':
        //elEditor.attr('readOnly',true);
        //beanEditor.attr('readOnly',true);
        //beanSelectButton.attr('disabled',true);
        specifyTableGroup.show();
        break;
    }

    promoterRadio.children('input').click(function () {
      specifyTableGroup.hide();
      //elEditor.attr('readOnly',true);
      //beanEditor.attr('readOnly',true);
      //beanSelectButton.attr('disabled',true);
      target.assignmentType = 'ProcessPromoter';
    });
    // elRadio.children('input').click(function(){
    //     specifyTableGroup.hide();
    //     //elEditor.attr('readOnly',false);
    //     //beanEditor.attr('readOnly',true);
    //     //beanSelectButton.attr('disabled',true);
    //     target.assignmentType='Expression';
    // });
    // beanRadio.children('input').click(function(){
    //     specifyTableGroup.hide();
    //     //elEditor.attr('readOnly',true);
    //     //beanEditor.attr('readOnly',false);
    //     //beanSelectButton.attr('disabled',false);
    //     target.assignmentType='Handler';
    // });

    specifyRadio.children('input').click(function () {
      //lEditor.attr('readOnly',true);
      //beanEditor.attr('readOnly',true);
      //beanSelectButton.attr('disabled',true);
      specifyTableGroup.show();
      target.assignmentType = 'Assignee';
    });
    return group;
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
        // selected = res.result;
        // showForm(selected);
      })

    }
  }

  buildSpecifyAssignTable(target) {
    const _this = this;
    const group = $(`<div class="form-group uflo-group" style="margin-top:10px;"></div>`);

    const totalGroup = $(`<div style="float:right;"><span style="display:inline-block;margin-top:6px;margin-left:15px;font-weight:400;cursor:pointer;">共计：</span></div>`);
    group.append(totalGroup);
    const totalButton = $(`<span style="display:inline-block;margin-right:5px;font-weight:400;cursor:pointer;">  0  条</span>`);
    totalGroup.append(totalButton);

    const pageGroup = $(`<div class="form-group" style="float:right;"><label>翻页：</label></div>`);
    group.append(pageGroup);
    const pageSelect = $(`<select class="form-control" style="display:inline-block;width:inherit;"></select>`);
    pageGroup.append(pageSelect);

    const addButton = $(`<button class="btn btn-primary" type="button" style="margin-bottom: 3px;float: right">添加参与者</button>`);
    group.append(addButton);
    const assignProviderDialog = new AssignProviderListDialog();
    assignProviderDialog.createContent()

    const table = $(`<table class="table table-bordered">
            <thead>
            <tr style="background: #fbfbfb"><td style="width: 120px">用户名或序号</td><td style="width: 120px">类型</td><td style="width: 40px">删除</td></tr>
            </thead>
        </table>`);
    group.append(table);
    const body = $(`<tbody></tbody>`);
    addButton.click(() => {
      assignProviderDialog.show(target.assignees, (data) => {
        body.empty();
        let assignees = target.assignees;
        if (!assignees) {
          assignees = [];
          target.assignees = assignees;
        }
        let len = assignees.length;
        for (let i = 0; i < len; i++) {
          assignees.pop();
        }
        for (var i = 0; i < data.length; i++) {
          assignees.push(data[i])
        }

        totalGroup.empty();
        totalGroup.append($(`<label style="display:inline-block;margin-top:6px;margin-left:10px;font-weight:400;cursor:pointer;">共计:  ${assignees.length}  条<label>`));

        assignees.forEach(assign => {
          console.log(assign);
          const tr = $(`<tr><td>${assign.id}</td><td>${assign.name}</td></tr>`);
          body.append(tr);
          const delTD = $(`<td></td>`);
          tr.append(delTD);
          const del = $(`<i class="glyphicon glyphicon-trash" style="color: red;font-size: 12pt;cursor: pointer"></i>`);
          delTD.append(del);
          del.click(function () {
            const index = assignees.indexOf(assign);
            assignees.splice(index, 1);
            tr.remove();
          });
          return true;
        });
        pageSelect.empty();
        const pageSize = 5;
        const totalPage = parseInt(assignees.length / pageSize) + ((assignees.length % pageSize) > 0 ? 1 : 0);

        for (let i = 1; i <= totalPage; i++) {
          pageSelect.append(`<option>${i}</option>`);
        }
        pageSelect.change(function () {
          const pageIndex = $(this).val();
          _this.buildPage(body, pageIndex, pageSize, assignees, totalButton, totalGroup)
        });
        _this.buildPage(body, 1, pageSize, assignees, totalButton, totalGroup)
      });
    });
    table.append(body);
    if (target.assignmentType === 'Assignee') {
      const assignees = target.assignees || [];
      totalGroup.empty();
      totalGroup.append($(`<label style="display:inline-block;margin-top:6px;margin-left:10px;font-weight:400;cursor:pointer;">共计:  ${assignees.length}  条<label>`));
      for (let assign of assignees) {
        const tr = $(`<tr>
                    <td>${assign.id}</td>
                    <td>${assign.name}</td>
                </tr>`);
        const delTD = $(`<td></td>`);
        tr.append(delTD);
        const del = $(`<i class="glyphicon glyphicon-trash" style="color: red;cursor: pointer"></i>`);
        del.click(() => {
          const index = assignees.indexOf(assign);
          assignees.splice(index, 1);
          tr.remove();
        });
        delTD.append(del);
        body.append(tr);
      }
      pageSelect.empty();
      const pageSize = 5;
      const totalPage = parseInt(assignees.length / pageSize) + ((assignees.length % pageSize) > 0 ? 1 : 0);

      for (let i = 1; i <= totalPage; i++) {
        pageSelect.append(`<option>${i}</option>`);
      }
      pageSelect.change(function () {
        const pageIndex = $(this).val();
        _this.buildPage(body, pageIndex, pageSize, assignees, totalButton, totalGroup);
      });
      _this.buildPage(body, 1, pageSize, assignees, totalButton, totalGroup);
    }
    return group;
  }
  buildPage(body, pageIndex, pageSize, assignees, totalButton, totalGroup) {
    body.empty();
    var len = assignees.length;
    var startRow = (pageIndex - 1) * pageSize + 1;
    var endRow = (pageIndex * pageSize > len ? len : pageIndex * pageSize);

    for (let i = 0; i < len; i++) {
      if (i >= (startRow - 1) && i <= (endRow - 1)) {
        const tr = $(`<tr><td>${assignees[i].id}</td><td>${assignees[i].name}</td></tr>`);
        body.append(tr);
        const delTD = $(`<td></td>`);
        tr.append(delTD);
        const del = $(`<i class="glyphicon glyphicon-trash" style="color: red;font-size: 12pt;cursor: pointer"></i>`);
        delTD.append(del);
        del.click(function () {
          const index = assignees.indexOf(assignees[i]);
          assignees.splice(index, 1);
          tr.remove();
          totalGroup.empty();
          totalGroup.append($(`<label style="display:inline-block;margin-top:6px;margin-left:15px;font-weight:400;cursor:pointer;">共计:  ${assignees.length}  条<label>`));
        });
      }
    }
    return body;
  }
  buildTab(target) {
    const group = $(`<div class="form-group uflo-group"></div>`);
    const introduce = $(`<span>采购（purchasing）是指企业在一定的条件下，从供应市场获取产品或服务作为企业资源， 以保证企业生产及经营活动正常开展的一项企业经营活动</span>`);
    const images = $(`<img width=100% src="${introduceImage}"/>`)
    const tab = (`<ul class="nav nav-tabs">
            <li class="active"><a href="#basic_task_config" style="padding: 6px;10px" data-toggle="tab">基本配置</a></li>
            <li><a href="#task_assign_config" style="padding: 6px;10px" data-toggle="tab">处理人</a></li>
            <li><a href="#task_form_config" style="padding: 6px;10px" data-toggle="tab">配置表单</a></li>
        </ul>`);
    //<li><a href="#task_due_config" style="padding: 6px;10px" data-toggle="tab">超时</a></li>

    group.append(introduce);
    group.append(images);
    group.append(tab);
    const tabContent = $(`<div class="tab-content"></div>`);
    group.append(tabContent);
    const taskBasic = $(`<div class="tab-pane fade in active" id="basic_task_config"></div>`);
    //taskBasic.append(this.getCommonProperties(target));
    taskBasic.append(this.buildTaskBasicConfig(target));
    tabContent.append(taskBasic);

    const taskAssign = $(`<div class="tab-pane fade in" id="task_assign_config"></div>`);
    taskAssign.append(this.buildTaskAssign(target));
    tabContent.append(taskAssign);

    const taskForm = $(`<div class="tab-pane fade in" id="task_form_config"></div>`);
    taskForm.append(this.buildTaskForm(target));
    tabContent.append(taskForm);
    this.getform(taskForm.find("#formName"),target)

    const dueConfig = $(`<div class="tab-pane fade in" id="task_due_config"></div>`);
    tabContent.append(dueConfig);

    const enableDueCheckbox = $(`<div class="checkbox"><label><input type="checkbox" ${target.dueDefinition ? 'checked' : ''}>启用任务超时功能</label></div>`);
    dueConfig.append(enableDueCheckbox);

    const dueTimeGroup = this.buildDueConfig(target);
    dueConfig.append(dueTimeGroup);

    enableDueCheckbox.find('input').change(function () {
      const value = $(this).prop('checked');
      if (value) {
        dueTimeGroup.show();
        target.dueDefinition = {};
      } else {
        dueTimeGroup.hide();
        target.dueDefinition = null;
      }
    });
    if (target.dueDefinition) {
      dueTimeGroup.show();
    } else {
      dueTimeGroup.hide();
    }

    const componentAuthority = $(`<div class="tab-pane fade in" id="component_authority"></div>`);
    tabContent.append(componentAuthority);
    componentAuthority.append(this.buildComponentAuthorityGroup(target));


    const customData = $(`<div class="tab-pane fade in" id="custom_data_config"></div>`);
    tabContent.append(customData);
    customData.append(this.buildCustomData(target));


    return group;
  }

  buildComponentAuthorityGroup(target) {
    const group = $(`<div class="form-group uflo-group"></div>`);
    const addButton = $(`<button type="button" class="btn btn-primary" style="float: right;margin-bottom: 3px;margin-top: 3px">添加</button>`);
    group.append(addButton);
    const table = $(`<table class="table table-bordered"><thead>
            <tr style="background: #f4f4f4"><td>组件名称</td><td>权限</td><td style="width: 70px">操作</td></tr>
        </thead></table>`);
    const body = $(`<tbody></tbody>`);
    table.append(body);
    const componentAuthorities = target.componentAuthorities || [];
    const dialog = new ComponentAuthorityDialog();
    for (let data of componentAuthorities) {
      this.buildComponentAuthority(componentAuthorities, data, body, dialog);
    }
    const _this = this;
    group.append(table);
    addButton.click(function () {
      dialog.show({}, function (data) {
        let componentAuthorities = [];
        if (target.componentAuthorities) {
          componentAuthorities = target.componentAuthorities;
        } else {
          target.componentAuthorities = componentAuthorities;
        }
        componentAuthorities.push(data);
        _this.buildComponentAuthority(componentAuthorities, data, body, dialog);
      });
    });
    return group;
  }

  buildCustomData(target) {
    const group = $(`<div class="form-group uflo-group"></div>`);
    const addButton = $(`<button type="button" class="btn btn-primary" style="float: right;margin-bottom: 3px;margin-top: 3px">添加</button>`);
    group.append(addButton);
    const dialog = new CustomDataDialog();
    const table = $(`<table class="table table-bordered"><thead>
            <tr style="background: #f4f4f4"><td>键</td><td>值</td><td style="width: 70px">操作</td></tr>
        </thead></table>`);
    const body = $(`<tbody></tbody>`);
    table.append(body);
    const userData = target.userData || [];
    for (let data of userData) {
      this.buildCustomDataBody(userData, data, body, dialog);
    }
    const _this = this;
    group.append(table);
    addButton.click(function () {
      dialog.show({}, function (data) {
        let userData = [];
        if (target.userData) {
          userData = target.userData;
        } else {
          target.userData = userData;
        }
        userData.push(data);
        _this.buildCustomDataBody(userData, data, body, dialog);
      });
    });
    return group;
  }

  buildComponentAuthority(componentAuthorities, data, body, dialog) {
    const tr = $(`<tr></tr>`);
    const nameTd = $(`<td>${data.component}</td>`);
    tr.append(nameTd);
    let auth = '只读';
    if (data.authority === 'ReadAndWrite') {
      auth = '可操作';
    } else if (data.authority === 'Hide') {
      auth = '不可见';
    }
    const authorityTd = $(`<td>${auth}</td>`);
    tr.append(authorityTd);
    body.append(tr);
    const td = $(`<td></td>`);
    tr.append(td);
    const del = $(`<i class="glyphicon glyphicon-trash" style="color: red;font-size: 12pt;cursor: pointer"></i>`);
    td.append(del);
    del.click(function () {
      tr.remove();
      const index = componentAuthorities.indexOf(data);
      componentAuthorities.splice(index, 1);
    });
    const edit = $(`<i class="glyphicon glyphicon-edit" style="color: #49a700;font-size: 12pt;cursor: pointer;margin-left: 10px"></i>`);
    td.append(edit);
    edit.click(function () {
      dialog.show(data, function (result) {
        nameTd.html(result.component);
        data.component = result.component;
        data.authority = result.authority;
        if (result.authority === 'ReadAndWrite') {
          authorityTd.html('可操作');
        } else if (result.authority === 'Hide') {
          authorityTd.html('不可见');
        }
      });
    });
  }

  buildCustomDataBody(userData, data, body, dialog) {
    const tr = $(`<tr></tr>`);
    const nameTd = $(`<td>${data.key}</td>`);
    tr.append(nameTd);
    const valueTd = $(`<td>${data.value}</td>`);
    tr.append(valueTd);
    body.append(tr);
    const td = $(`<td></td>`);
    tr.append(td);
    const del = $(`<i class="glyphicon glyphicon-trash" style="color: red;font-size: 12pt;cursor: pointer"></i>`);
    td.append(del);
    del.click(function () {
      tr.remove();
      const index = userData.indexOf(data);
      userData.splice(index, 1);
    });
    const edit = $(`<i class="glyphicon glyphicon-edit" style="color: #49a700;font-size: 12pt;cursor: pointer;margin-left: 10px"></i>`);
    td.append(edit);
    edit.click(function () {
      dialog.show(data, function (result) {
        nameTd.html(result.key);
        valueTd.html(result.value);
        data.key = result.key;
        data.value = result.value;
      });
    });
  }

  buildDueConfig(target) {
    const group = $(`<div class="form-group uflo-group"></div>`);
    const periodGroup = this.buildPeriodDate(target.dueDefinition || {}, '<label>超时时间：</label>', null, function () {
      // if(!target.dueDefinition){
      //     target.dueDefinition={};
      // }
      return target.dueDefinition;
    });
    group.append(periodGroup);
    let calendarInfos = [];
    if (target.dueDefinition && target.dueDefinition.calendarInfos) {
      calendarInfos = target.dueDefinition.calendarInfos;
    }
    // group.append(this.buildHolidayTable(calendarInfos,function(){
    //     if(!target.dueDefinition){
    //         target.dueDefinition={};
    //     }
    //     if(!target.dueDefinition.calendarInfos){
    //         target.dueDefinition.calendarInfos=[];
    //     }
    //     return target.dueDefinition.calendarInfos;
    // }));

    // <li class="active"><a href="#due_reminder_config" data-toggle="tab">过期提醒配置</a></li>
    // const tab=(`<ul class="nav nav-tabs">
    //     <li><a href="#due_action_config" data-toggle="tab">超时动作配置</a></li>
    // </ul>`);
    // const tab=(`<span>超时动作配置</span>`);
    // group.append(tab);

    //     const tabContent=$(`<div class="tab-content"></div>`);
    //     group.append(tabContent);
    //     const dueReminder=$(`<div class="tab-pane fade in active" id="due_reminder_config"></div>`);
    //     tabContent.append(dueReminder);

    //     //dueReminder.append(this.buildDueReminder(target));

    //     const dueAction=$(`<div class="tab-pane fade in" id="due_action_config"></div>`);
    //     tabContent.append(dueAction);
    //     dueAction.append(this.buildDueAction(target));
    //     return group;
    // }

    // buildDueReminder(target){
    //     const dueDefinition=target.dueDefinition || {reminder:{},dueAction:{}};
    //     const group=$(`<fieldset style="padding: 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px">
    //     <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;">任务过期提醒配置</legend></fieldset>`);
    //     const beanGroup=$(`<div class="form-group uflo-group"><label>提醒Bean：</label></div>`);
    //     group.append(beanGroup);
    //     let heanderBean='';
    //     if(dueDefinition.reminder && dueDefinition.reminder.handlerBean){
    //         heanderBean=dueDefinition.reminder.handlerBean;
    //     }
    //     const beanEditor=$(`<input type="text" class="form-control uflo-text-editor" value="${heanderBean}" title="一个实现了com.bstek.uflo.process.handler.ReminderHandler接口配置在Spring中的Bean的id" style="width: 220px">`);
    //     beanGroup.append(beanEditor);
    //     beanEditor.change(function(){
    //         if(!target.dueDefinition){
    //             target.dueDefinition=dueDefinition;
    //         }
    //         if(!target.dueDefinition.reminder){
    //             target.dueDefinition.reminder={};
    //         }
    //         dueDefinition.reminder.handlerBean=$(this).val();
    //     });
    //     const selectBeanButton=$(`<button type="button" class="btn btn-default" style="margin-top: 2px;padding-top: 2px;height: 26px;margin-left: 2px;">选择</button>`);
    //     beanGroup.append(selectBeanButton);
    //     const selectDialog=new EventSelectDialog('选择一个实现了com.bstek.uflo.process.handler.ReminderHandler接口配置在Spring中的Bean的id');
    //     selectBeanButton.click(function(){
    //         selectDialog.show('ReminderHandler',function(beanId){
    //             beanEditor.val(beanId);
    //             if(!target.dueDefinition){
    //                 target.dueDefinition=dueDefinition;
    //             }
    //             if(!target.dueDefinition.reminder){
    //                 target.dueDefinition.reminder={};
    //             }
    //             target.dueDefinition.reminder.handlerBean=beanId;
    //         })
    //     });

    //     let reminder={},reminderType='none';
    //     if(target.dueDefinition && target.dueDefinition.reminder){
    //         reminder=target.dueDefinition.reminder;
    //     }
    //     const remindTypeGroup=$(`<div class="form-group uflo-group"><label>提醒方式：</label></div>`);
    //     group.append(remindTypeGroup);
    //     const noneReminderRadio=$(`<span><input type="radio" name="reminder_type_radio" checked>不提醒</span>`);
    //     remindTypeGroup.append(noneReminderRadio);
    //     if(!target.dueDefinition || !target.dueDefinition.reminder){
    //         noneReminderRadio.children('input').prop('checked',true);
    //     }

    //     const onceReminderRadio=$(`<span style="margin-left: 10px;"><input type="radio" name="reminder_type_radio">一次性提醒</span>`);
    //     remindTypeGroup.append(onceReminderRadio);
    //     if(target.dueDefinition && target.dueDefinition.reminder){
    //         if(!target.dueDefinition.reminder.unit){
    //             onceReminderRadio.children('input').prop('checked',true);
    //             reminderType='once';
    //         }
    //     }

    //     const periodReminderRadio=$(`<span style="margin-left: 10px;"><input type="radio" name="reminder_type_radio">周期提醒</span>`);
    //     remindTypeGroup.append(periodReminderRadio);
    //     if(target.dueDefinition && target.dueDefinition.reminder) {
    //         if (target.dueDefinition.reminder.unit) {
    //             periodReminderRadio.children('input').prop('checked',true);
    //             reminderType='period';
    //         }
    //     }
    //     const _this=this;
    //     const periodGroup=$(`<div class="form-group uflo-group"><span style="margin-left: 70px">每隔</span></div>`);
    //     const periodEditor=$(`<input type="text" class="form-control uflo-text-editor" value="${reminder.repeat || ''}" style="width: 50px;margin-left: 2px">`);
    //     periodGroup.append(periodEditor);
    //     periodEditor.change(function(){
    //         const period=_this.buildPeriodReminder(target);
    //         period.repeat=$(this).val();
    //     });
    //     const dayRadio=$(`<span style="margin-left: 5px;"><input type="radio" name="period_reminder_unit">天</span>`);
    //     periodGroup.append(dayRadio);
    //     dayRadio.children('input').click(function(){
    //         const period=_this.buildPeriodReminder(target);
    //         period.unit='Day';
    //     });
    //     const hourRadio=$(`<span style="margin-left: 5px;"><input type="radio" name="period_reminder_unit">小时</span>`);
    //     periodGroup.append(hourRadio);
    //     hourRadio.children('input').click(function(){
    //         const period=_this.buildPeriodReminder(target);
    //         period.unit='Hour';
    //     });
    //     const minuteRadio=$(`<span style="margin-left: 5px;"><input type="radio" name="period_reminder_unit">分钟</span>`);
    //     periodGroup.append(minuteRadio);
    //     group.append(periodGroup);

    //     minuteRadio.children('input').click(function(){
    //         const period=_this.buildPeriodReminder(target);
    //         period.unit='Minute';
    //     });

    //     if(reminder.unit==='Day'){
    //         dayRadio.children('input').attr('checked',true);
    //     }
    //     if(reminder.unit==='Hour'){
    //         hourRadio.children('input').attr('checked',true);
    //     }
    //     if(reminder.unit==='Minute'){
    //         minuteRadio.children('input').attr('checked',true);
    //     }

    //     let calendarInfos=[];
    //     if(target.dueDefinition && target.dueDefinition.reminder && target.dueDefinition.reminder.calendarInfos){
    //         calendarInfos=target.dueDefinition.reminder.calendarInfos;
    //     }
    //     // const holidayGroup=this.buildHolidayTable(calendarInfos,function(){
    //     //     if(!target.dueDefinition){
    //     //         target.dueDefinition={};
    //     //     }
    //     //     if(!target.dueDefinition.reminder){
    //     //         target.dueDefinition.reminder={};
    //     //     }
    //     //     if(!target.dueDefinition.reminder.calendarInfos){
    //     //         target.dueDefinition.reminder.calendarInfos=[];
    //     //     }
    //     //     return target.dueDefinition.reminder.calendarInfos;
    //     // });
    //     // group.append(holidayGroup);

    //     //holidayGroup.hide();
    //     if(reminderType==='period'){
    //         periodGroup.show();
    //         //holidayGroup.show();
    //     }else{
    //         periodGroup.hide();
    //     }
    //     if(reminderType==='none'){
    //         selectBeanButton.prop('disabled',true);
    //         beanEditor.prop('readOnly',true);
    //     }

    //     noneReminderRadio.children('input').click(function(){
    //         periodGroup.hide();
    //         selectBeanButton.prop('disabled',true);
    //         beanEditor.prop('readOnly',true);
    //         //holidayGroup.hide();
    //         if(target.dueDefinition && target.dueDefinition.reminder){
    //             target.dueDefinition.reminder=null;
    //         }
    //     });

    //     onceReminderRadio.children('input').click(function(){
    //         periodGroup.hide();
    //         selectBeanButton.prop('disabled',false);
    //         beanEditor.prop('readOnly',false);
    //        // holidayGroup.hide();
    //         const period=_this.buildPeriodReminder(target);
    //         period.unit=null;
    //         period.repeat=null;
    //     });

    //     periodReminderRadio.children('input').click(function(){
    //         periodGroup.show();
    //         selectBeanButton.prop('disabled',false);
    //         beanEditor.prop('readOnly',false);
    //         //holidayGroup.show();
    //         const period=_this.buildPeriodReminder(target);
    //         period.unit='Day';
    //         period.repeat=1;
    //         dayRadio.children('input').attr('checked',true);
    //         periodEditor.val("1");
    //     });
    //     return group;
    // }

    // buildPeriodReminder(target){
    //     if(!target.dueDefinition){
    //         target.dueDefinition={};
    //     }
    //     if(!target.dueDefinition.reminder){
    //         target.dueDefinition.reminder={};
    //     }
    //     return target.dueDefinition.reminder;
    // }

    //buildDueAction(target){
    const group1 = $(`<fieldset style="padding: 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px">
        <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;">任务超时动作配置</legend></fieldset>`);
    group.append(group1);
    // const enableGroup=$(`<div class="form-group uflo-group"><label>是否启用超时动作：</label></div>`);
    // group1.append(enableGroup);
    const enableRadio = $(`<span><input type="radio" name="enable_due_action_radio" checked >启用</span>`);
    const disableRadio = $(`<span style="margin-left: 10px"><input type="radio" name="enable_due_action_radio">禁用</span>`);
    // enableGroup.append(enableRadio);
    // enableGroup.append(disableRadio);

    const container = $(`<div></div>`);
    group1.append(container);

    enableRadio.children('input').click(function () {
      container.show();
      if (!target.dueDefinition) {
        target.dueDefinition = {};
      }
      if (!target.dueDefinition.dueAction) {
        target.dueDefinition.dueAction = {};
      }
    });
    disableRadio.children('input').click(function () {
      container.hide();
      if (target.dueDefinition && target.dueDefinition.dueAction) {
        target.dueDefinition.dueAction = null;
      }
    });

    let dueAction = {};
    if (target.dueDefinition && target.dueDefinition.dueAction) {
      dueAction = target.dueDefinition.dueAction;
      //container.show();
      //enableRadio.children('input').attr('checked',true);
    } else {
      //container.hide();
      //disableRadio.children('input').attr('checked',true);
      container.show();
      enableRadio.children('input').attr('checked', true);
    }

    const beanGroup = $(`<div class="form-group uflo-group"><label>操作内容：</label></div>`);
    container.append(beanGroup);
    const beanEditor = $(`<input type="text" class="form-control uflo-text-editor"  value="${dueAction.handlerBean || ''}" title="一个实现了com.bstek.uflo.process.handler.ReminderHandler接口配置在Spring中的Bean的id" style="width: 220px">`);
    beanGroup.append(beanEditor);
    beanEditor.val(dueAction.handlerBean);
    beanEditor.change(function () {
      if (!target.dueDefinition) {
        target.dueDefinition = {};
      }
      if (!target.dueDefinition.dueAction) {
        target.dueDefinition.dueAction = {};
      }
      dueAction = target.dueDefinition.dueAction;
      dueAction.handlerBean = $(this).val();
    });
    const selectBeanButton = $(`<button type="button" class="btn btn-default" style="margin-top: 2px;padding-top: 2px;height: 26px;margin-left: 2px;">选择</button>`);
    beanGroup.append(selectBeanButton);
    const selectDialog = new EventSelectDialog('提示');
    selectBeanButton.click(function () {
      selectDialog.show('ReminderHandler', function (beanId) {
        if (!target.dueDefinition) {
          target.dueDefinition = {};
        }
        if (!target.dueDefinition.dueAction) {
          target.dueDefinition.dueAction = {};
        }
        dueAction = target.dueDefinition.dueAction;
        dueAction.handlerBean = beanId;
        beanEditor.val(beanId);
      });
    });

    // container.append(this.buildPeriodDate(dueAction,'<label>动作在过期后</label>','<span>后执行</span>',function(){
    //     if(!target.dueDefinition){
    //         target.dueDefinition={};
    //     }
    //     if(!target.dueDefinition.dueAction) {
    //         target.dueDefinition.dueAction={};
    //     }
    //     return target.dueDefinition.dueAction;
    // }));

    let calendars = [];
    if (target.dueDefinition && target.dueDefinition.dueAction && target.dueDefinition.dueAction.calendarInfos) {
      calendars = target.dueDefinition.dueAction.calendarInfos;
    }
    // container.append(this.buildHolidayTable(calendars,function(){
    //     if(!target.dueDefinition){
    //         target.dueDefinition={};
    //     }
    //     if(!target.dueDefinition.dueAction) {
    //         target.dueDefinition.dueAction={};
    //     }
    //     if(!target.dueDefinition.dueAction.calendarInfos){
    //         target.dueDefinition.dueAction.calendarInfos=[];
    //     }
    //     return target.dueDefinition.dueAction.calendarInfos;
    // }));

    return group;
  }

  buildPeriodDate(obj, title, tail, newObjectFun) {
    const periodGroup = $(`<div class="form-group uflo-group">${title}</div>`);
    //const periodDayEditor=$(`<input type="number" min="0" value="${obj.day || ''}" class="form-control uflo-text-editor" style="width: 40px">`);
    const periodDayEditor = $(`<input type="number" min="0" value="${obj.day > -1 ? obj.day : ''}" class="form-control uflo-text-editor" style="width: 40px">`);
    periodGroup.append(periodDayEditor);
    periodDayEditor.change(function () {
      const due = newObjectFun.call(this);
      due.day = $(this).val();
    });
    periodGroup.append(`<span style="margin-right: 3px">天</span>`);
    const periodHourEditor = $(`<input type="number" min="0" max="23" value="${obj.hour > -1 ? obj.hour :''}" class="form-control uflo-text-editor" style="width: 40px;margin-left: 5px">`);
    periodGroup.append(periodHourEditor);
    periodHourEditor.change(function () {
      const due = newObjectFun.call(this);
      due.hour = $(this).val();
    });
    periodGroup.append(`<span style="margin-right: 3px">小时</span>`);
    const periodMinuteEditor = $(`<input type="number" min="0" max="59" value="${obj.minute > -1 ? obj.minute :''}" class="form-control uflo-text-editor" style="width: 40px;margin-left: 5px">`);
    periodGroup.append(periodMinuteEditor);
    periodMinuteEditor.change(function () {
      const due = newObjectFun.call(this);
      due.minute = $(this).val();
    });
    periodGroup.append(`<span>分钟</span>`);
    if (tail) {
      periodGroup.append(tail);
    }
    return periodGroup;
  }

  // buildHolidayTable(target,newCalendarsFun){
  //     const group=$(`<div class="form-group uflo-group"></div>`);
  //     const addButton=$(`<button type="button" class="btn btn-primary" style="float: right">添加节假日</button>`);
  //     group.append(addButton);
  //     const table=$(`<table class="table table-bordered" style="font-size: 12px">
  //         <thead>
  //             <tr><td>节假日ID</td><td>节假日名称</td><td style="width: 50px">删除</td></tr>
  //         </thead>
  //     </table>`);
  //     group.append(table);
  //     const body=$(`<tbody></tbody>`);
  //     table.append(body);
  //     for(let data of target){
  //         const tr=$(`<tr><td>${data.id}</td><td>${data.name}</td></tr>`);
  //         body.append(tr);
  //         const td=$(`<td></td>`);
  //         tr.append(td);
  //         const del=$(`<i class="glyphicon glyphicon-trash" style="color: red;cursor: pointer;font-size: 12pt"></i>`);
  //         td.append(del);
  //         del.click(function(){
  //             const index=target.indexOf(data);
  //             target.splice(index,1);
  //             tr.remove();
  //         });
  //     }
  //     const dialog=new CalendarSelectDialog();
  //     addButton.click(()=>{
  //         dialog.show(function(data){
  //             const calendars=newCalendarsFun.call(this);
  //             for(let c of calendars){
  //                 if(c.id===data.id){
  //                     MsgBox.alert(`节假日[${data.name}]已添加！`);
  //                     return false;
  //                 }
  //             }
  //             calendars.push(data);
  //             const tr=$(`<tr><td>${data.id}</td><td>${data.name}</td></tr>`);
  //             body.append(tr);
  //             const td=$(`<td></td>`);
  //             tr.append(td);
  //             const del=$(`<i class="glyphicon glyphicon-trash" style="color: red;cursor: pointer;font-size: 12pt"></i>`);
  //             td.append(del);
  //             del.click(function(){
  //                 const index=calendars.indexOf(data);
  //                 calendars.splice(index,1);
  //                 tr.remove();
  //             });
  //             return true;
  //         });
  //     });
  //     return group;
  // }

  getPropertiesProducer() {
    const _this = this;
    return function () {
      const g = $(`<div></div>`);
      g.append(_this.buildTab(this));
      return g;
    }
  }

}
