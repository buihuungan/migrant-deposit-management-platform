import {MsgBox} from 'flowdesigner';
import {t_postAction,t_getAction} from '@/api/tempApi'

export default class FormSelectDialog{
    constructor(title){
        this.dialog=$(`<div class="modal fade" role="dialog" aria-hidden="true" style="z-index: 10000">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">
                            ${title || '表单选择'}
                        </h4>
                    </div>
                    <div class="modal-body" style="max-height:400px;overflow-y:scroll"></div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>`);
        const body=this.dialog.find('.modal-body'),footer=this.dialog.find(".modal-footer");
        this.initBody(body,footer);
    }
    initBody(body,footer){
        const table=$(`<table class="table table-bordered"><thead><tr style="background: #eeeeee">
            <td>表单</td><td style="width: 50px;">选择</td>
        </tr></thead></table>`);
        body.append(table);
        this.tbody=$(`<tbody></tbody>`);
        table.append(this.tbody);
    }

    show(callback){
        this.dialog.modal('show');
        this.tbody.empty();
        const _this=this;
        var data = new FormData()
        data.append('current', 1)
        data.append('size', 22)
        data.append('deleteFlag', 1)
        data.append('encoding', "Scheduling")
        t_postAction("/admin/desform/search", data)
        .then(res => {
            for(let d of res.result.list){
                const tr=$(`<tr><td>${d.name}</td></tr>`);
                this.tbody.append(tr);
                const td=$(`<td></td>`);
                tr.append(td);
                const icon=$(`<i class="glyphicon glyphicon-hand-up" style="cursor: pointer;color: green;font-size: 12pt"></i>`);
                td.append(icon);
                icon.click(()=>{
                    callback.call(this,d);
                    _this.dialog.modal('hide');
                });
            }

            // let formNameDom = $('#formName')
            // console.log("5555555",formNameDom[0]);
            // if(formNameDom && formNameDom[0].options && formNameDom[0].options[0]){
            //     let selectedForm = formNameDom[0].options[0].value;//tempatedId
            //     let selectedUrl = formNameDom[0].options[0].text;//
            //     let isTable = 0
            //     if(selectedForm != ""){
            //       isTable = 1;
            //     }
            //     target.formflag={url:selectedUrl,isTable:isTable,templateld:selectedForm}//添加到xml文件中
            //     console.log("配置表单更改",target.formflag,"----------");
            // }
        })
    }
}