/**
 * Created by Jacky.Gao on 2017-07-14.
 */
import Vue from 'vue'
import {
  MsgBox
} from 'flowdesigner';
import {
  Message
} from 'element-ui';
import {
  w_postAction
} from '@/api/workapi'
import {
  ACCESS_TOKEN,
  TENANT_ID
} from "@/store/mutation-types"
import axios from 'axios'
import api from '../../../../api/index.js'


export default class SaveDialog {
  constructor() {
    this.dialog = $(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 10000">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            保存流程模板文件
                        </h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>`);
    this.providerMap = new Map();
    const body = this.dialog.find('.modal-body'),
      footer = this.dialog.find(".modal-footer");
    this.initBody(body, footer);
  }
  return () {
    window.returnFlowDesign();
  }
  initBody(body, footer) {
    const tokenval = Vue.ls.get(ACCESS_TOKEN)
    this.providers = [];
    this.files = [];
    const _this = this;

    const nameGroup = $(`<div class="form-group"><label>流程模板名称：</label></div>`);
    body.append(nameGroup);
    const nameEditor = $(`<input type="text" placeholder="输入流程模板文件名" class="form-control" style="display: inline-block;width: 360px;">`);
    nameGroup.append(nameEditor);

    // const providerGroup=$(`<div class="form-group"><label>模版文件来源：</label></div>`);
    // body.append(providerGroup);
    // this.providerSelect=$(`<select class="form-control" style="display: inline-block;width: 360px;"></select>`);
    // providerGroup.append(this.providerSelect);
    // this.providerSelect.change(function(){
    //     const value=$(this).val();
    //  let target=_this.loadTargetProvider(value);
    //  _this.loadFiles(target);
    //  });

    // const table=$(`<table class="table table-bordered">
    //     <thead>
    //         <tr style="background: #eeeeee">
    //             <td>文件名</td>
    //             <td style="width: 150px">修改日期</td>
    //             <td style="width: 50px">打开</td>
    //             <td style="width: 50px">删除</td>
    //         </tr>
    //     </thead>
    // </table>`);
    //
    //     const table=$(`<table class="table table-bordered">
    //     <thead>
    //         <tr style="background: #eeeeee">
    //             <td>文件名</td>
    //             <td style="width: 150px">修改日期</td>
    //             <td style="width: 50px">删除</td>
    //         </tr>
    //     </thead>
    // </table>`);
    //this.tableBody=$(`<tbody></tbody>`);
    //table.append(this.tableBody);
    //body.append(table);
    const saveButton = $(`<button type="button" class="btn btn-default"><i class="glyphicon glyphicon-floppy-disk"></i> 保存</button>`);
    footer.append(saveButton);
    saveButton.click(() => {
      //let exist=false;
      const name = nameEditor.val();
      if (!name) {
        MsgBox.alert('请输入流程模版名称！');
        return;
      }
      // const flag = -2;
      // const checkData = new FormData()
      // checkData.append('token', tokenval)
      // checkData.append('fileName', name)
      // w_postAction('/admin/designer/checkFileNameExist', checkData)
      //   .then((res) => {
      //     console.log(res.code)
      //     if (res.code == 1) {
      //       console.log(flag)
      //         if(confirm('流程文件已存在!是否选择覆盖？'))
      //           flag = 1;
      //         else
      //           flag = 2;
      //       return flag;
      //       // result = confirm('流程文件已存在!是否选择覆盖？')
      //       // return result;
      //     }
      //   })
      //   .catch((error) => {
      //     console.log(error)
      //     MsgBox.alert('查询流程文件失败!');
      //   })
      // console.log(flag)
      // if (flag == 2) {
      //   return;
      // }
      // for(let file of this.files){
      //     if(file.name===name){
      //         exist=true;
      //         break;
      //     }
      // }
      // if(exist){
      //     //MsgBox.alert(`流程模版文件[${name}]已存在!`);
      //     //return;
      // }
      //const providerName=this.providerSelect.val();
      const providerName = '默认文件系统';
      // if(!providerName){
      //     MsgBox.alert('请选择模版文件存储目的地！');
      //     return;
      // }
      //const content=encodeURI(this.designer.toXML());
      console.log("xml开始");
      const content = this.designer.toXML();
      console.log("xml结束");
      console.log(content);
      if (!content) {
        return;
      }
      const provider = this.providerMap.get(providerName);
      console.log(this.providerMap.get(providerName))
      let fullName = "file:" + name;
      //let fullName = provider.prefix + name;
       if(fullName.indexOf('.uflo.xml')===-1){
           fullName+='.uflo.xml';
       }
      // const saveData = new FormData()
      // saveData.append('fileName', fullName)
      // saveData.append('content', content)
      //   for (var [a, b] of saveData.entries()) {
      //     console.log(a, b);
      //  }
      axios.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest'
      w_postAction(`/uflo/designer/saveFile?fileName=`+fullName+'&content='+content)
        .then((res) => {
          if (this.callback) {
            this.callback.call(this, fullName);
            Message.success('保存成功！');
            //_this.return();
          }
          this.dialog.modal('hide');
        })
        .catch((error) => {
          console.log(error)
          Message.error('保存失败！');
        })
    });
  }
  loadTargetProvider(targetName) {
    let target = null;
    for (let p of this.providers) {
      if (p.name === targetName) {
        target = p;
        break;
      }
    }
    if (target == null) {
      throw `${targetName} provider not exist!`;
    }
    return target;
  }

  // loadFiles(provider){
  //     const _this=this;
  //     //this.tableBody.empty();
  //     const url=taskConUrl+'/admin/designer/load-process-provider-files';
  //     $.ajax({
  //         url,
  //         data:{name:provider.name,
  //               token: tokenval},
  //         type:'POST',
  //         success:function(files){
  //             const temp=JSON.parse(files.data);
  //             _this.files=temp.result;
  //             //_this.buildTable(temp.result,provider);
  //         },
  //         error:function(){
  //             MsgBox.alert(`加载${provider.name}中流程模版文件失败！`);
  //         }
  //     });
  // }

  // buildTable(files,provider){
  //     for(let file of files){
  //         const tr=$(`<tr><td>${file.name}</td><td>${file.updateDate}</td></tr>`);
  //         // const openTD=$(`<td></td>`);
  //         // tr.append(openTD);
  //         // const openIcon=$(`<i class="glyphicon glyphicon-folder-open" style="color: green;cursor: pointer"></i>`);
  //         // openTD.append(openIcon);
  //         // openIcon.click(function(){
  //         //     const fullName=provider.prefix+file.name;
  //         //     // const url=window._server+'/designer?_u='+fullName;
  //         //     const url=taskConUrl+'/uflo/designer?p='+fullName;
  //         //     window.open(url,'_blank');
  //         // });

  //         const delTD=$(`<td></td>`);
  //         tr.append(delTD);
  //         const delIcon=$(`<i class="glyphicon glyphicon-trash" style="color: red;cursor: pointer"></i>`);
  //         delTD.append(delIcon);
  //         const fullName=provider.prefix+file.name;
  //         delIcon.click(function(){
  //             MsgBox.confirm(`真的要删除文件[${file.name}]吗？`,function(){
  //                 const url=taskConUrl+'/admin/designer/delete-file';
  //                 //let url = taskConUrl + '/uflo/designer/deleteFile'
  //                 $.ajax({
  //                     url,
  //                     data:{fileName:fullName,token: tokenval},
  //                     type:'POST',
  //                     success:function(){
  //                         tr.remove();
  //                     },
  //                     error:function(){
  //                         MsgBox.alert('删除操作失败！');
  //                     }
  //                 });
  //             });
  //         });
  //         this.tableBody.append(tr);
  //     }
  // }

  show(designer, callback) {
    const tokenval = Vue.ls.get(ACCESS_TOKEN)
    this.designer = designer;
    this.callback = callback;
    this.dialog.modal('show');
    //this.providerSelect.empty();
    this.providerMap.clear();
    const _this = this;
    //let url = taskConUrl + '/admin/designer/load-process-providers';
    const filesName = new FormData()
    filesName.append('token', tokenval)
    filesName.append('name', "默认文件系统")

    // const token = Vue.ls.get(ACCESS_TOKEN)
    // let tenant_id=Vue.ls.get(TENANT_ID);
    // if (token) {
    //   axios.defaults.headers.common['X-Access-Token'] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
    //   axios.defaults.headers.common['tenant_id'] = tenant_id
    // }
    
    // axios.post('/api2/admin/designer/load-process-provider-files',filesName)
    // //w_postAction('/admin/designer/load-process-provider-files', filesName)
    //   .then((res) => {
    //     let index = 0;
    //     _this.providers = res.data;
    //     for (let item of res.data) {
    //       _this.providerMap.set(item.name, item);
    //       //_this.providerSelect.append(`<option ${index===0 ? 'checked' : ''}>${item.name}</option>`);
    //       // if(index===0){
    //       //     _this.loadFiles(item);
    //       // }
    //       index++;
    //     }

    //   })
    //   .catch((error) => {
    //     console.log(error)
    //     //MsgBox.alert('加载流程模版存储提供者列表失败！');
    //   })
  }
}