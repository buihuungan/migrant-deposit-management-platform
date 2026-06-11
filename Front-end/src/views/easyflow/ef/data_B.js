let dataB ={
    "name": "流程",
    "nodeList": [
        {
            "id": "5yl18foezh",
            "type": "rectangle",
            "name": "销售订单",
            "left": "73.0077px",
            "top": "70.0115px",
            "role": [
                "1337369556699807745",
                "f6817f48af4fb3af11b9e8bf182f618b"
            ],
            "state": "waiting",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "",
            "onlineDataId": "",
            "feature": "task"
        },
        {
            "id": "3zthjoyue",
            "type": "rectangle",
            "name": "智能排产",
            "left": "295.008px",
            "top": "70.0115px",
            "role": [
                "1337369556699807745",
                "f6817f48af4fb3af11b9e8bf182f618b"
            ],
            "state": "waiting",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "",
            "onlineDataId": "",
            "feature": "task"
        },
        {
            "id": "tiu2okutpb",
            "type": "rhombus",
            "name": "物料是否充足",
            "state": "waiting",
            "left": "500.008px",
            "top": "101.396px"
        },
        {
            "id": "74kbgqdyc5",
            "type": "rectangle",
            "name": "采购管理",
            "left": "652.008px",
            "top": "70.0115px",
            "role": [
                "1337369556699807745",
                "f6817f48af4fb3af11b9e8bf182f618b"
            ],
            "state": "waiting",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "",
            "onlineDataId": "",
            "feature": "task"
        },
        {
            "id": "erkbpcmlnf",
            "type": "rectangle",
            "name": "生产任务",
            "left": "490.008px",
            "top": "254.873px",
            "role": [
                "1337369556699807745",
                "f6817f48af4fb3af11b9e8bf182f618b"
            ],
            "state": "waiting",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "",
            "onlineDataId": "",
            "feature": "task"
        },
        {
            "id": "rhvmz7i609",
            "type": "rectangle",
            "name": "物料配送",
            "left": "307px",
            "top": "255px",
            "role": [
                "1337369556699807745",
                "f6817f48af4fb3af11b9e8bf182f618b"
            ],
            "state": "waiting",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "",
            "onlineDataId": "",
            "feature": "task"
        },
        {
            "id": "49bjdcsanq",
            "type": "rectangle",
            "name": "产品生产",
            "left": "131.008px",
            "top": "254.873px",
            "role": [
                "1337369556699807745",
                "f6817f48af4fb3af11b9e8bf182f618b"
            ],
            "state": "waiting",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "",
            "onlineDataId": "",
            "feature": "task"
        }
    ],
    "lineList": [
        {
            "from": "5yl18foezh",
            "to": "3zthjoyue"
        },
        {
            "from": "3zthjoyue",
            "to": "tiu2okutpb"
        },
        {
            "from": "3zthjoyue",
            "to": "74kbgqdyc5"
        },
        {
            "from": "tiu2okutpb",
            "to": "74kbgqdyc5",
            "label": "否"
        },
        {
            "from": "tiu2okutpb",
            "to": "erkbpcmlnf",
            "label": "是"
        },
        {
            "from": "erkbpcmlnf",
            "to": "rhvmz7i609"
        },
        {
            "from": "rhvmz7i609",
            "to": "49bjdcsanq"
        }
    ]
}
export function getDataB () {
    return dataB
}
