
import {w_getAction} from '@/api/workapi'
import {MsgBox} from 'flowdesigner';
import axios from 'axios'
import Vue from 'vue'
import { ACCESS_TOKEN , TENANT_ID } from '@/store/mutation-types'
import "../css/node.css"
import { w_postAction } from '@/api/workapi'
import api from '../../../../api/index.js'
let selectName = "selectName";
let selectedItems= [];
let providerForSearch  = "";

export default class AssignProviderListDialog{
    constructor(){
        this.dialog=$(`<div class="modal fade createmodel" role="dialog" aria-hidden="true" style="z-index: 10000">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" id="selectclose" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            选择参与者
                        </h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>`);
        
    }

    createContent(){
        const body=this.dialog.find('.modal-body'),footer=this.dialog.find(".modal-footer");
        body.empty()
        footer.empty()
        this.initBody(body,footer);
    }
    buildTab(){
        body.append(group);
        const group=$(`<div class="form-group uflo-group"></div>`);
        const tab=(`<ul class="nav nav-tabs">
            <li><a href="#user_information" style="padding: 6px;10px" data-toggle="tab">用户信息</a></li>
            <li><a href="#department_role" style="padding: 6px;10px" data-toggle="tab">部门角色</a></li>
        </ul>`);
        group.append(tab);
        const tabContent=$(`<div class="tab-content"></div>`);
        group.append(tabContent);
        const taskAssign=$(`<div class="tab-pane fade in" id="user_information"></div>`);
        //taskAssign.append(this.buildTaskAssign(target));
        tabContent.append(taskAssign);

        const taskForm = $(`<div class="tab-pane fade in" id="department_role"></div>`);
        //taskForm.append(this.buildTaskForm(target));
        tabContent.append(taskForm);
        this.getform(taskForm.find("#formName"),target)

        return group;
    }    
    initBody(body,footer){
        body.empty()
        footer.empty()
        this.providerMap=new Map();
        //this.tip=$(`<span>当前应用中实现了com.bstek.uflo.process.assign.AssigneeProvider接口的的任务处理人提供者在下面的列表当中，选择一条继续</span>`);
        //body.append(this.tip);
        // body.append(`<div class="form-group"></div>`);

        // //const _this=this;
        // this.providerSelect=$(`<select class="form-control" id="selector" size="10"></select>`);
        // body.append(this.providerSelect);

        //切换
        this.providerSelect = $(`<div class="form-control" id="selector"></div>`);
        body.append(this.providerSelect);
           
 
        this.prevButton=$(`<button type="button" class="btn btn-default">上一步</button>`);
        //footer.append(this.prevButton);
        this.nextButton=$(`<button type="button" class="btn btn-default">下一步</button>`);
        //footer.append(this.nextButton);
        this.submitButton=$(`<button type="button" class="btn btn-default">提交</button>`)
        footer.append(this.submitButton);

        // this.tableContainer=$(`<div class="form-group"></div>`);
        // body.append(this.tableContainer);
        
        // this.pageGroup=$(`<div style="display:inline-block;margin-bottom:15px;"><label>翻页：</label></div>`);
        // this.tableContainer.append(this.pageGroup);

        this.searchGroup1=$(`<div style="display:inline-block;margin-bottom:15px;border:1px solid #ccc;border-radius:4px;margin-left:185px;"><input class="search1" autocomplete="off" type="text" style="display:inline-block;border:none;outline:none;height:34px;width:180px;" placeholder="输入用户名或真实姓名搜索"></div>`);
        //this.tableContainer.append(this.searchGroup1);
        this.searchButton1=$(`<button style="vertical-align:middle;border:none;height:34px;outline:none;margin-top:-3px;"><img src="@/../static/imgs/search_Grey.png" style="display:inline-block;width:15px;height:15px;margin:5px;"></button>`);
        //this.searchGroup1.append(this.searchButton1);

        this.searchGroup2=$(`<div style="display:inline-block;margin-bottom:15px;border:1px solid #ccc;border-radius:4px;margin-left:185px;"><input class="search2" autocomplete="off" type="text" style="display:inline-block;border:none;outline:none;height:34px;width:180px;" placeholder="输入用户类型搜索"></div>`);
        //this.tableContainer.append(this.searchGroup2);
        this.searchButton2=$(`<button style="vertical-align:middle;border:none;height:34px;outline:none;"><img src="@/../static/imgs/search_Grey.png" style="display:inline-block;width:15px;height:15px;margin:5px;"></button>`);
        //this.searchGroup2.append(this.searchButton2);

        this.searchGroup3=$(`<div style="display:inline-block;margin-bottom:15px;border:1px solid #ccc;border-radius:4px;margin-left:185px;"><input class="search3" autocomplete="off" type="text" style="display:inline-block;border:none;outline:none;height:34px;width:180px;" placeholder="输入用户名或真实姓名搜索"></div>`);
        //this.tableContainer.append(this.searchGroup3);
        this.searchButton3=$(`<button style="vertical-align:middle;border:none;height:34px;outline:none;margin-top:-3px;"><img src="@/../static/imgs/search_Grey.png" style="display:inline-block;width:15px;height:15px;margin:5px;"></button>`);
        this.searchGroup3.append(this.searchButton3);

        this.searchGroup4=$(`<div style="display:inline-block;margin-bottom:15px;border:1px solid #ccc;border-radius:4px;margin-left:185px;"><input class="search4" autocomplete="off" type="text" style="display:inline-block;border:none;outline:none;height:34px;width:180px;" placeholder="输入用户名或真实姓名搜索"></div>`);
        //this.tableContainer.append(this.searchGroup4);
        this.searchButton4=$(`<button style="vertical-align:middle;border:none;height:34px;outline:none;margin-top:-3px;"><img src="@/../static/imgs/search_Grey.png" style="display:inline-block;width:15px;height:15px;margin:5px;"></button>`);
        this.searchGroup4.append(this.searchButton4);

        this.searchGroup5=$(`<div style="display:inline-block;margin-bottom:15px;border:1px solid #ccc;border-radius:4px;margin-left:185px;"><input class="search5" autocomplete="off" type="text" style="display:inline-block;border:none;outline:none;height:34px;width:180px;" placeholder="输入名称搜索"></div>`);
        //this.tableContainer.append(this.searchGroup5);
        this.searchButton5=$(`<button style="vertical-align:middle;border:none;height:34px;outline:none;margin-top:-3px;"><img src="@/../static/imgs/search_Grey.png" style="display:inline-block;width:15px;height:15px;margin:5px;"></button>`);
        this.searchGroup5.append(this.searchButton5);

        this.searchGroup6=$(`<div style="display:inline-block;margin-bottom:15px;border:1px solid #ccc;border-radius:4px;margin-left:185px;"><input class="search6" autocomplete="off" type="text" style="display:inline-block;border:none;outline:none;height:34px;width:180px;" placeholder="输入专业Id或专业名称搜索"></div>`);
        //this.tableContainer.append(this.searchGroup6);
        this.searchButton6=$(`<button style="vertical-align:middle;border:none;height:34px;outline:none;margin-top:-3px;"><img src="@/../static/imgs/search_Grey.png" style="display:inline-block;width:15px;height:15px;margin:5px;"></button>`);
        this.searchGroup6.append(this.searchButton6);

        this.searchGroup7=$(`<div style="display:inline-block;margin-bottom:15px;border:1px solid #ccc;border-radius:4px;margin-left:185px;"><input class="search7" autocomplete="off" type="text" style="display:inline-block;border:none;outline:none;height:34px;width:180px;" placeholder="输入用户名或真实姓名搜索"></div>`);
        //this.tableContainer.append(this.searchGroup7);
        this.searchButton7=$(`<button style="vertical-align:middle;border:none;height:34px;outline:none;margin-top:-3px;"><img src="@/../static/imgs/search_Grey.png" style="display:inline-block;width:15px;height:15px;margin:5px;"></button>`);
        this.searchGroup7.append(this.searchButton7);

        // this.treeContainer=$(`<div class="tree" style="margin-left: 10px"></div>`);
        // body.append(this.treeContainer);
        // this.tree=$(`<ul style="padding-left: 10px;"></ul>`);
        // this.treeContainer.append(this.tree);
    }

    buildTree(provider){
        w_postAction(`/uflo/assigneeProviderListServletHandlerImpl?providerId=`+provider.providerId)
            .then(res => {
                console.log(res);
                _this.tree.empty();
                for(let d of res.assignees || []){  
                    _this.buildTreeNode(_this.tree,d);
                }
            })
            .catch(error => {
              console.log(error)
            })
       
    }

    // buildTree(provider){
    //     const _this=this;
    //     const formTree = new FormData()
    //     formTree.append('providerId', provider.providerId)
    //     const token = Vue.ls.get(ACCESS_TOKEN)
    //     // if (token) {
    //     //     axios.defaults.headers.common['X-Access-Token'] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
    //     // }
    //     // axios.post('/api2/admin/designer/assign-providers',formTree)
    //     //axios.post(`${api.server_url+api.work_url}/uflo/assigneeProviderListServletHandlerImpl?providerId=`+provider.providerId)
    //     axios.post(`/apilocal/uflo/assigneeProviderListServletHandlerImpl?providerId=`+provider.providerId)
    //     //w_getAction('/designer/assignProviders?providerKey='+provider.providerId)
    //     //w_postAction('/admin/designer/assign-providers',formTree)
    //         .then(res => {
    //             console.log(res.data);
    //             _this.tree.empty();
    //             for(let d of res.data.assignees || []){  
    //                 _this.buildTreeNode(_this.tree,d);
    //             }
    //         })
    //         .catch(error => {
    //           console.log(error)
    //         })
    // }

    buildTreeChildren(parent,providerId,id){
        const _this=this;
        w_postAction(`/uflo/assigneeProviderListServletHandlerImpl?providerId=`+providerId)
            .then(res => {
                console.log(res);
                for(let d of res.assignees || []){  
                    _this.buildTreeNode(parent,d);
                }
            })
            .catch(error => {
              console.log(error)
            })
    }
   
    
    buildAssignTable(keyWord,provider,assignTableBody,tableContainer,index){
        // this.assignTableBody.empty();
        // if(this.pageSelect){
        //     this.pageSelect.remove();
        // }

        const pageTitle = $(`<div style="display:inline-block;margin-bottom:15px;"></div>`)
        let pageInput=$(`<div style="float:left;margin-bottom:15px;border-radius:4px;">
            <label>搜索：</label>
            <input autocomplete="off" type="text" style="display:inline-block;border: 1px solid #ccc;outline:none;height:34px;width:180px;" placeholder="输入Id或名称搜索"></div>`);
        //this.tableContainer.append(this.searchGroup6);
        let searchButton=$(`<button id="search${index}" style="vertical-align:middle;border:none;height:34px;outline:none;margin-top:-3px;margin-left:15px">查询</button>`);
        pageInput.append(searchButton);






        let pageSelect=$(`<select class="form-control" style="display: inline-block;width: inherit;"></select>`);
        const pageGroup=$(`<div style="float:right;margin-bottom:15px;"><label>翻页：</label></div>`);
        pageGroup.append(pageSelect);



        tableContainer.prepend(pageGroup)
        tableContainer.prepend(pageInput)


        const _this=this;
        const pageSize=10;
        pageSelect.change(function(){  //这是根据页码选数据

            console.log('页面数据变化，多少页:',$(this).val())
            const pageIndex=$(this).val();
            _this.loadAssignTableData(keyWord,provider.providerId,pageIndex,pageSize,assignTableBody,pageSelect,index);
        });

        searchButton.on('click',function(){
            const searchWord = $(this).siblings('input').val()
            _this.loadAssignTableData(searchWord,provider.providerId,1,pageSize,assignTableBody,pageSelect,index);
        })

        this.loadAssignTableData(keyWord,provider.providerId,1,pageSize,assignTableBody,pageSelect,index);
    }

    loadAssignTableData(keyWord,providerId,pageIndex,pageSize,assignTableBody,pageSelect,index){
        const _this=this;
        w_postAction(`/uflo/assigneeProviderListServletHandlerImpl?providerId=`+providerId+'&pageIndex='+pageIndex+'&pageSize='+pageSize)
            .then(res => {
                console.log("获取的表单数据+对应表",res);

                return _this.buildAssignTableData(res,pageIndex,assignTableBody,pageSelect,index);
            })
            .catch(error => {
                _this.$message({
                    type: 'error',
                    message: '加载数据失败！'
                  })
                console.log(error)
            })
    }

    buildAssignTableData(data,pageIndex,assignTableBody,pageSelect,index){
        const _this = this
        assignTableBody.empty();
        for(let d of data.inheritAssignee || []){
            if(data.name == "指定"){
                var tr=$(`<tr><td>${d.name}</td></tr>`);
            }else if(data.name == "普通用户" || data.name == "指定角色"){
                var tr=$(`<tr><td>${d.name}</td></tr>`);
            }else if( data.name == "指定用户"){
                console.log("解析",JSON.parse(d.addProperty))
                var addProperty1={}
                addProperty1=JSON.parse(d.addProperty)
                console.log("解析解析",addProperty1.orgCode);
                var tr=$(`<tr><td>${addProperty1.orgCode}</td><td>${addProperty1.username}</td><td>${addProperty1.realname}</td></tr>`);
            }else{
                var tr=$(`<tr><td>${d.id}</td><td>${d.realName}</td><td>${d.name}</td><td>${d.department}</td></tr>`);
            }
            const selectTD=$(`<td style="width: 60px"></td>`);
            tr.append((selectTD));
            // const selectIcon=$(`<i class="glyphicon glyphicon-hand-up" style="color: green;font-size: 14pt;cursor: pointer"></i>`);
            const selectIcon=$(`<input type="checkbox" class="selectoption${index}">`);
            selectTD.append(selectIcon);
            assignTableBody.append(tr);
            selectedItems[index].forEach(item=>{
                if(d.providerId === item.providerId && d.name === item.name && d.id === item.id){
                    selectIcon[0].checked=true;
                }
            })
            selectIcon[0].addEventListener('click',function(){ 
                let dom = $(`.selectoption${index}`)
                if(selectIcon[0].checked===true){
                    selectedItems[index].push({providerId:d.providerId,id:d.id,name:d.name,index:d.index,chosen:false})    
                    if(dom.length>1){
                        let flag = true
                        for(let i=0;i<dom.length;i++){
                            if(dom[i].checked == false){
                                flag = false
                            }
                        }

                        if(flag && $(`.selAll${index}`)){
                            $(`.selAll${index}`)[0].checked = true
                        }
                    }     
                }else{
                    let fn = function(target,targetIndex,selectedItems){
                        return target.provider==d.provider && target.id==d.id  && target.name==d.name
                    }
                    let location=selectedItems[index].findIndex(fn)
                    selectedItems[index].splice(location,1)

                    // console.log('来这里了'，$('.selAll'))
                    if($(`.selAll${index}`)){
                        $(`.selAll${index}`)[0].checked = false
                    }
                }

                if(selectedItems.length){
                    let isNull = true
                    selectedItems.forEach(item=>{
                        if(item.length){
                            isNull = false
                        }
                    })
                    if(!isNull){
                        _this.submitButton.attr("disabled",false)
                    }else{
                        _this.submitButton.attr("disabled",true)
                    }
                }
            })
        }
        if(data.name == "指定"){
            var tr=$(`<tr><td>全选</td></tr>`);
        }else if(data.name == "普通用户" || data.name == "指定角色"){
            var tr=$(`<tr><td colspan="1">全选</td></tr>`);
        }else if(data.name == "指定用户"){
            var tr=$(`<tr><td colspan="3">全选</td></tr>`);
        }else{
        var tr=$(`<tr><td colspan="4">全选</td></tr>`);
        }
        const selectD=$(`<td style="width: 60px"></td>`);
        selectD.empty()
        tr.append((selectD));
        // const selectIcon=$(`<i class="glyphicon glyphicon-hand-up" style="color: green;font-size: 14pt;cursor: pointer"></i>`);
        const selectAllBox=$(`<input type="checkbox" class="selAll${index}">`);
        selectAllBox[0].addEventListener('click',function(){ 
            let dom = $(`.selectoption${index}`)

            if(selectAllBox[0].checked===true){
                selectedItems[index] = []
                data.inheritAssignee.forEach(item=>{
                    selectedItems[index].push({
                        providerId:item.providerId,
                        id:item.id,
                        name:item.name,
                        index:item.index,
                        chosen:false})         
                })
                if(dom){
                    for(let i=0;i<dom.length;i++){
                        dom[i].checked = true
                    }
                }
            }else{
                selectedItems[index] = []
                if(dom){
                    for(let i=0;i<dom.length;i++){
                        dom[i].checked = false
                    }
                }
            }

            if(selectedItems.length){
                let isNull = true
                selectedItems.forEach(item=>{
                    if(item.length){
                        isNull = false
                    }
                })
                if(!isNull){
                    _this.submitButton.attr("disabled",false)
                }else{
                    _this.submitButton.attr("disabled",true)
                }
            }

        })
        selectD.append(selectAllBox);
        assignTableBody.append(tr);
        pageSelect.empty();
        const pageSize=10;
        const totalPage=parseInt(data.count/pageSize) + ((data.count % pageSize) > 0 ? 1 : 0);
        for(let i=1;i<=totalPage;i++){
            this.selectOption=$(`<option ${i==pageIndex ? 'selected' : ''}>${i}</option>`);
            pageSelect.append(this.selectOption);
        }
        // console.log(data.assignees)
    }

    loadProviders(){
        const _this=this;
        w_postAction(`/uflo/assigneeProviderListServletHandlerImpl`)
            .then(res => {
                console.log("----------",res);
                var assignlist=[];
                assignlist=res
                console.log("ssssssssss",assignlist);
              _this.buildSelect(res);
            })
            .catch(error => {
              _this.$message({
                type: 'error',
                message: '加载任务参与者失败！'
              })
              console.log(error)
            })
    }

    async buildSelect(data){
        this.providerSelect.empty();
        this.providerMap.clear();

        let newTab = $(`<ul class="nav nav-tabs"></ul>`)
        const tabContent=$(`<div class="tab-content" style="padding-top:20px"></div>`);

        for(let i=0;i<data.length;i++){
            selectedItems[i] = []
            let provider = data[i]
            let newli = ''
            let contentTable = ''
            let random = Math.random()
            if(i==0){
                newli = $(`<li class="active"><a class="aclick" href="#tabcontent${i}" style="padding: 6px;10px" data-toggle="tab">${provider.name}</a></li>`)
                contentTable=$(`<div class="tab-pane fade in active" id="tabcontent${i}"></div>`);
            }else{
                newli = $(`<li><a class="aclick" href="#tabcontent${i}" style="padding: 6px;10px" data-toggle="tab">${provider.name}</a></li>`)
                contentTable=$(`<div class="tab-pane fade in" id="tabcontent${i}"></div>`);
            }
            this.providerMap.set(provider.providerId,provider);

            newli.find('a.aclick').on('click',function(){
                var contentdom = $(this).parents('div.form-control').find(`div.tab-pane`)
                if(contentdom.length>1){
                    for(let j=0;j<contentdom.length;j++){
                        //判断当前这个元素属性中是否包含active
                        let flag = contentdom[j].className.indexOf('active') != -1
                        if(j==i && !flag){
                            $(contentdom[j]).addClass('active')
                        }else if(j!=i && flag){
                            $(contentdom[j]).removeClass('active')
                        }
                    }
                }
               
            })


            const text = provider.name
            switch(text) {
                case "指定用户类型":
                  selectName = "序号";
                  contentTable.append(this.showSelectData(provider,selectName,text,i));
                  break;
                case "根据板块指定管理员":
                  selectName = "";
                  contentTable.append(this.showSelectData(provider,selectName,text,i));
                  break;
                case "根据专业指定用户":
                  selectName = "专业Id";
                  contentTable.append(this.showSelectData(provider,selectName,text,i));
                  break;
                default:
                  selectName = "id";
                  var dom = await this.showSelectData(provider,selectName,text,i)
                  contentTable.append(dom);
                  break;
              }
            tabContent.append(contentTable);
            newTab.append(newli)

          
        }

        const _this=this;
       
        this.providerSelect.append(newTab);  
        this.providerSelect.append(tabContent);
    }

    // buildSelect(data){
    //     this.providerSelect.empty();
    //     this.providerMap.clear();
    //     for(let provider of data){
    //         let newOption=$(`<option value="${provider.providerId}">${provider.name}</option>`);
    //         this.providerSelect.append(newOption);
    //         this.providerMap.set(provider.providerId,provider);
    //     }

    //     const _this=this;
    //     $(document).ready(function(){
    //     //$('#selector').change(function(){//这里直接用change二次进入页面会不触发change事件，所以要改成on,另外还要绑定所操纵的对象（#selector）
    //        $('body').on('change','#selector',function(){
    //         var text = $(this).children('option:selected').text()
    //         switch(text) {
    //           case "指定用户类型":
    //             selectName = "序号";
    //             _this.showSelectData(selectName,text);
    //             break;
    //           case "根据板块指定管理员":
    //             selectName = "";
    //             _this.showSelectData(selectName,text);
    //             break;
    //             case "根据专业指定用户":
    //             selectName = "专业Id";
    //             _this.showSelectData(selectName,text);
    //             break;
    //           default:
    //             selectName = "id"
    //             _this.showSelectData(selectName,text);
    //             break;
    //         }
    //       });
    //   })
    // }
    showSelectData(provider,selectName,text,index) {
      let assignTable = ''
      if(text == "指定"){
        assignTable=$(`<table class="table table-bordered" style="margin-bottom:0px;"><thead>
            <tr style="background: #eeeeee"><td>名称</td><td style="50px">选择</td></tr></thead>
             </table>`);
        this.searchGroup1.hide();
        this.searchGroup2.hide();
        this.searchGroup3.hide();
        this.searchGroup4.hide();
        this.searchGroup5.show();
        this.searchGroup6.hide();
        this.searchGroup7.hide();
      }else if(text == "指定角色"){
        assignTable=$(`<table class="table table-bordered" style="margin-bottom:0px;"><thead>
            <tr style="background: #eeeeee"><td>角色类型</td><td style="50px">选择</td></tr></thead>
             </table>`);
        this.searchGroup1.hide();
        this.searchGroup2.show();
        this.searchGroup3.hide();
        this.searchGroup4.hide();
        this.searchGroup5.hide();
        this.searchGroup6.hide();
        this.searchGroup7.hide();
      }else if(text == "普通用户"||text == "指定用户"){
        assignTable=$(`<table class="table table-bordered" style="margin-bottom:0px;"><thead>
            <tr style="background: #eeeeee"><td>单位名称</td><td>用户名</td><td>用户姓名</td><td style="50px">选择</td></tr></thead>
             </table>`);
        this.searchGroup1.hide();
        this.searchGroup2.hide();
        this.searchGroup3.hide();
        this.searchGroup4.hide();
        this.searchGroup5.hide();
        this.searchGroup6.show();
        this.searchGroup7.hide();
      }else if(text == "指定现场实习生"){
        assignTable=$(`<table class="table table-bordered" style="margin-bottom:0px;"><thead>
            <tr style="background: #eeeeee"><td>` + selectName +`</td><td>真实姓名</td><td>用户类型</td><td style="50px">选择</td></tr></thead>
            </table>`);
        this.searchGroup1.hide();
        this.searchGroup2.hide();
        this.searchGroup3.show();
        this.searchGroup4.hide();
        this.searchGroup5.hide();
        this.searchGroup6.hide();
        this.searchGroup7.hide();
      }
      else if(text == "指定管理员"){
        assignTable=$(`<table class="table table-bordered" style="margin-bottom:0px;"><thead>
            <tr style="background: #eeeeee"><td>` + selectName +`</td><td>真实姓名</td><td>用户类型</td><td style="50px">选择</td></tr></thead>
            </table>`);
        this.searchGroup1.hide();
        this.searchGroup2.hide();
        this.searchGroup3.hide();
        this.searchGroup4.show();
        this.searchGroup5.hide();
        this.searchGroup6.hide();
        this.searchGroup7.hide();
      }
      else if(text == "指定普通用户"){
        assignTable=$(`<table class="table table-bordered" style="margin-bottom:0px;"><thead>
            <tr style="background: #eeeeee"><td>` + selectName +`</td><td>真实姓名</td><td>用户类型</td><td style="50px">选择</td></tr></thead>
            </table>`);
        this.searchGroup1.hide();
        this.searchGroup2.hide();
        this.searchGroup3.hide();
        this.searchGroup4.hide();
        this.searchGroup5.hide();
        this.searchGroup6.hide();
        this.searchGroup7.show();
      }else if(text == "指定公司员工"){
        assignTable=$(`<table class="table table-bordered company-table" style="margin-bottom:0px;"><thead>
            <tr style="background: #eeeeee"><td>` + selectName +`</td><td>真实姓名</td><td>用户类型</td><td>所属部门</td><td style="50px">选择</td></tr></thead>
            </table>`);
        this.searchGroup1.show();
        this.searchGroup2.hide();
        this.searchGroup3.hide();
        this.searchGroup4.hide();
        this.searchGroup5.hide();
        this.searchGroup6.hide();
        this.searchGroup7.hide();
      }
        const assignTableBody=$(`<tbody></tbody>`);
        assignTable.append(assignTableBody);
        const tableContainer=$(`<div class="form-group"></div>`);
        tableContainer.append(assignTable);

        this.buildAssignTable('',provider,assignTableBody,tableContainer,index)
        return tableContainer;
    }
    // init(assignees){
    //     selectedItems=[];
    //     if(assignees){
    //         assignees.forEach(assign=>{
    //             selectedItems.push(assign);
    //         })
    //     }
    // }
    show(assignees,callback){
      const _this = this
    //   this.init(assignees);
      this.dialog.modal('show');
      this.loadProviders();
    //   this.tableContainer.show();
    //   this.treeContainer.hide();
      this.providerSelect.show();
      this.prevButton.attr("disabled",true);
      this.nextButton.attr("disabled",true);
      this.submitButton.attr("disabled",true)
      this.callback=callback;



      this.submitButton.click(()=>{
        let newarray = []
        selectedItems.forEach(item=>{
            newarray = newarray.concat(item)
        })
        _this.callback.call(_this,newarray)
        _this.dialog.modal('hide')
            // for(let i = 0;i < 8; i++){
            // let  len = $(".search"+i).length
            // for(let  j = 0;j < len;j++){
            //     $(".search"+i)[j].value = ""
            // }
            // }
      })
    }
}
