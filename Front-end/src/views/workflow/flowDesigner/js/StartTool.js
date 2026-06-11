/**
 * Created by jacky on 2016/7/18.
 */
// import $ from 'jquery'
import '../../../../../node_modules/jquery/dist/jquery.min.js';
import BaseTool from './BaseTool.js';
import StartNode from './StartNode.js';
import UrlSelectDialog from '../dialog/UrlSelectDialog.js';

export default class StartTool extends BaseTool{
    getType(){
        return '开始';
    }
    getIcon(){
        return `<i class="uflo uflo-start" style="color:#737383"></i>`
        // return `<img src="../images/t.png" />`
    }
    newNode(){
        return new StartNode();
    }
    getConfigs(){
        return {
            in:0,
            out:1,
            single:true
        };
    }
    buildTaskConfig(target){
        const group=$(`<fieldset style="padding: 10px;border:solid 1px #dddddd;border-radius: 8px;margin-bottom: 10px;margin-top: 10px;">
        <legend style="width: auto;margin-bottom: 1px;border-bottom:none;font-size: inherit;color: #4b4b4b;">基本属性</legend></fieldset>`);
        //  const taskNameGroup=$(`<div class="form-group uflo-group"><label>节点任务名称：</label></div>`);
        // const taskNameEditor=$(`<input type="text" class="form-control uflo-text-editor" value="${target.taskName || ''}" style="width:250px">`);
        // taskNameGroup.append(taskNameEditor);
        // group.append(taskNameGroup);
        // taskNameEditor.change(function(){
        //     const value=$(this).val();
        //     target.taskName=value;
        // });

        // const urlGroup=$(`<div class="form-group uflo-group"><label>操作内容：</label></div>`);
        // group.append(urlGroup);
        // const tip="提示";
        // const urlText=$(`<input type="text" class="form-control uflo-text-editor" title="${tip}" value="待发布" style="width: 220px">`);
        // urlGroup.append(urlText);
        // //urlText.val(target.url);
        // const urlSelectButton=$(`<button type="button" class="btn btn-default" style="height: 26px;padding-top: 2px;margin-left: 2px;">选择</button>`);
        // //urlGroup.append(urlSelectButton);
        // urlText.change(function(){
        //     target.url=$(this).value();
        // });
        // const dialog=new UrlSelectDialog(`${tip}`);
        // urlSelectButton.click(function(){
        //     dialog.show('ProcessUrlHandler',function(urlId){
        //         urlText.val(urlId);
        //         target.url=urlId;               
        //     });
        // });
        const descGroup = $(`<div class="form-group"><label style="vertical-align: top">描述：</label></div>`);
        group.append(descGroup);
        const descText = $(`<textarea rows="5" class="form-control uflo-text-editor" placeholder="描述内容修改后请点击属性面板空白处保存" style="width: 300px;height:80px !important"></textarea>`);
        descGroup.append(descText);
        descText.val(target.description);
        var timer = setTimeout(() => { //定时器用来解决对象异步加载，target.description的值取不到的问题
        descText.val(target.description);
          }, 500);
        descText.change(function () {
            target.description = $(this).val();
         });

        return group;
    }

    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            //g.append(_this.getCommonProperties(this));
            g.append(_this.buildTaskConfig(this));
            return g;
        }
    }
}