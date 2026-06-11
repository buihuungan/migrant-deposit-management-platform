let dataA ={
    "name": "流程",
    "event-handler-bean": "test",
    "start-process-url": "test",
    "key": "test",
    "category-id": "test",
    "effect-date": "2020-12-12 00:00:00",
    "description": "test",
    "type": "process", // 要加上这个属性，不然我程序不好判断name是节点的还是程序的
    "nodeList": [
        {
            "id": "r46fkfg2v",
            "type": "start",
            "name": "开始",
            "label": "test",   // 可以为空
            "event-handler-bean": "test", // 可以选取事件
            "handler-bean": "test", // 处理bean
            //"g": "test", // 在这个里面可以保存x，y轴 width、height
            "icon": "el-icon-caret-right",
            "class": "ef-node-menu-li",
            "left": "88",
            "top": "43",
            "state": "waiting",
            "formDesignerId": "",
            "onlineTableId": "",
            "onlineDataId": ""
        },
        {
            "id": "kqsgchh2",
            "type": "people",
            "name": "人工节点",
            "ico": "el-icon-s-custom",
            "class": "ef-node-menu-li",
            "left": "86",
            "top": "117",
            "state": "waiting",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "d0de97d9495b40b9a7537bfd3d3033bd",
            "onlineDataId": "1419484619250720770",
            "event-handler-bean":"", //事件bean
            // 如果需要制定参与者则需要做如下信息
            "assignment-type":"Assignee", //处理人
            "assignee" : [{
                "provider-id":"roleAssigneeProvider",
                "name":"部门主管",
                "id":"1346466939702743042"
            }],
            // 如果需要设置到期时间等与时间相关的需要以下配置
            "due":{
                "day":"",
                "hour":"",
                "minute":"",
            },
            // 如果需要有人员信息的话需要添加如下信息
            "user-data": {
                "key":"",
                "value":""
            } 
        },
        {
            "id": "h7dy17uebr",
            "type": "end",
            "name": "结束",
            "terminate": "test", // 结束节点的属性， 默认写为true吧，具体的区别我还没研究出来
            "ico": "el-icon-video-play",
            "class": "ef-node-menu-li",
            "left": "91",
            "top": "221",
            "state": "waiting",
            "formDesignerId": "",
            "onlineTableId": "",
            "onlineDataId": ""
        }
    ],
    "lineList": [
        {
            "from": "r46fkfg2v",
            "to": "kqsgchh2"
        },
        {
            "from": "kqsgchh2",
            "to": "h7dy17uebr"
        }
    ]
}
export function getDataA () {
    return dataA
}

