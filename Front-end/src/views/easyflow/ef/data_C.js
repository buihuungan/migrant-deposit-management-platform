let dataC ={
    "name": "流程",
    "nodeList": [
        {
            "id": "3bqzct4h0k",
            "type": "rectangle",
            "name": "长方形1",
            "left": "114px",
            "top": "75px",
            "role": [],
            "state": "waiting",
            "feature": "vedio",
            "formDesignerId": "08277b6fe01644b6921a782daee85e46",
            "onlineTableId": "68e29388a0624e6fae085c7ca638021c",
            "onlineDataId": "1443132244495171586"
        },
        {
            "id": "tk1titglbg",
            "type": "rectangle",
            "name": "长方形2",
            "left": "389.008px",
            "top": "64.0115px",
            "role": [],
            "state": "waiting",
            "feature": "task",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "d0de97d9495b40b9a7537bfd3d3033bd",
            "onlineDataId": "1443132272160800770"
        },
        {
            "id": "a4bsh3tit",
            "type": "rectangle",
            "name": "长方形3",
            "left": "298.008px",
            "top": "241.012px",
            "role": [],
            "state": "waiting",
            "feature": "task",
            "formDesignerId": "eb1520b12e784f3cb711a5e61d2af5a2",
            "onlineTableId": "d0de97d9495b40b9a7537bfd3d3033bd",
            "onlineDataId": "1443132297393733634"
        }
    ],
    "lineList": [
        {
            "from": "3bqzct4h0k",
            "to": "tk1titglbg"
        },
        {
            "from": "tk1titglbg",
            "to": "a4bsh3tit"
        }
    ]
}
export function getDataC () {
    return dataC
}