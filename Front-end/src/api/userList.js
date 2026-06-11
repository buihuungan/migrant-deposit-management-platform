import { axios } from '@/utils/request'
import api from './index'
import { nw_getAction, nw_getAllData } from '@api/newWorkApi'

//人社局员工列表获取
export function getRSJUser(data) {
    return axios({
        url: '/stj/sys/user/humanResourcesList',
        method: 'post',
        baseURL: api.server_url + api.global_workflow_baseURL,
        data: data,
        // headers: {
        //   'userId': Vue.ls.get(USER_ID),
        // },
    })
}

//施工单位用户列表获取
export function getQYUser(data) {
    return axios({
        url: '/stj/sys/user/constructionEnterprisesList',
        method: 'post',
        baseURL: api.server_url + api.global_workflow_baseURL,
        data: data,
        // headers: {
        //   'userId': Vue.ls.get(USER_ID),
        // },
    })
}

//施工单位登录用户数据查看
export function getSingleQYUser(userId) {
    return axios({
        url: '/stj/sys/user/constructionEnterprisesById',
        method: 'get',
        baseURL: api.server_url + api.global_workflow_baseURL,
        params: {
            userId: userId
        }
        // headers: {
        //   'userId': Vue.ls.get(USER_ID),
        // },
    })
}

//通过企业名称获取企业基本信息
export function getBusinessByCompanyName(companyName) {
    return axios({
        url: '/stj/sys/user/getBusinessByCompanyName',
        method: 'post',
        baseURL: api.server_url + api.global_workflow_baseURL,
        data: {
            companyName: companyName
        }
    });
}

//新增施工单位用户
export function addQYUser(data) {
    return axios({
        url: '/stj/sys/user/addConstructionEnterprises',
        method: 'post',
        baseURL: api.server_url + api.global_workflow_baseURL,
        data: data,
        // headers: {
        //   'userId': Vue.ls.get(USER_ID),
        // },
    })
}

//编辑施工单位用户
export function editQYUser(data) {
    return axios({
        url: '/stj/sys/user/editConstructionEnterprises',
        method: 'put',
        baseURL: api.server_url + api.global_workflow_baseURL,
        data: data,
        // headers: {
        //   'userId': Vue.ls.get(USER_ID),
        // },
    })
}


//下载文书接口
// export function downloadDocument(record) {
//     console.log('下载文书参数:', record);
//     return axios({
//         url: `/file/static${record.export_path}`,
//         // url: '/file/static/opt/export/pdfs/1876948644514078721-output.pdf',
//         method: 'get',
//         baseURL: api.server_url + api.global_course_baseURL,
//         responseType: 'blob',
//         headers: {
//             'Content-Type': 'application/json',
//             'Accept': 'application/pdf,application/octet-stream'
//         }
//     }).then(response => {
//         console.log('文件下载成功:', response);
//         // 根据文件扩展名判断文件类型
//         const fileName = record.export_path.split('/').pop() || 'downloaded-file.pdf';
//         const fileExtension = fileName.split('.').pop().toLowerCase();

//         // 设置正确的 MIME 类型
//         let mimeType = 'application/octet-stream'; // 默认类型
//         if (fileExtension === 'pdf') {
//             mimeType = 'application/pdf';
//         } else if (fileExtension === 'docx') {
//             mimeType = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
//         } else if (fileExtension === 'xlsx') {
//             mimeType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
//         }

//         // 创建 Blob 对象，直接使用 response.data
//         const blob = new Blob([response.data], { type: mimeType });
//         const downloadUrl = window.URL.createObjectURL(blob);

//         // 创建下载链接
//         const link = document.createElement('a');
//         link.href = downloadUrl;
//         link.setAttribute('download', fileName);

//         // 触发下载
//         document.body.appendChild(link);
//         link.click();

//         // 清理
//         document.body.removeChild(link);
//         window.URL.revokeObjectURL(downloadUrl);
//     }).catch(error => {
//         console.error('文件下载失败:', error);
//     });
// }

export function downloadDocument(record) {
    const url = api.server_url + api.global_course_baseURL + `/file/static${record.export_path}`;
    window.open(url, '_blank');
}

// export function downloadDocument(record) {
//     const url = api.server_url + api.global_course_baseURL + `/file/static${record.export_path}`;
//     const a = document.createElement('a');
//     a.href = url;
//     a.download = '';  // 可以指定文件名，如果为空，浏览器会使用文件的默认名称
//     document.body.appendChild(a);
//     a.click();
//     document.body.removeChild(a);
// }

//认领接口
function claimTask(record) {
    return nw_getAction(`/task/claimTask/` + record.taskId)
        .then((res) => {
            if (res.result) {
                console.log('认领成功', record)
                return true // 认领成功返回 true
            } else {
                console.error('认领失败')
                return false // 认领失败返回 false
            }
        })
        .catch((error) => {
            console.log(error)
            return false // 出现错误时返回 false
        })
}


//登录用户自动认领流程接口
export async function AutoClaim(processIdList, categoryId, userInfo) {
    let params = {
        processIdList: processIdList,
        applyState: ['claim'],
        pageSize: 1000,
        pageNum: 1,
        categoryId: categoryId,
    };

    try {
        const res = await nw_getAllData(`/generalList/getAllList`, params);
        console.log('获取未认领的返回数据:', res.result.dataList);
        const loadClaimData = res.result.dataList;

        if (loadClaimData.length > 0) {
            const claimPromises = []; // 用于存储所有认领任务的 Promise
            for (let i = 0; i < loadClaimData.length; i++) {
                const projectAddress = loadClaimData[i].allData.main_payment.project_address;
                console.log('项目地址:', userInfo);
                // 通过用户的部门地址和项目的地址进行匹配来自动认领
                if (userInfo.orgAddress.some((addr) => addr === projectAddress)) {
                    const promise = claimTask(loadClaimData[i]);
                    claimPromises.push(promise);
                }
            }
            // 等待所有认领任务完成后再继续
            await Promise.all(claimPromises);
            console.log('所有认领任务完成');
        }
    } catch (error) {
        console.error('AutoClaim 失败:', error);
    }
}


//获取代办事项数量
export function getPendingTotal(processIdList, categoryId) {
    return new Promise((resolve, reject) => {
        let params = {
            processIdList: processIdList,
            applyState: ['pending'],
            categoryId: categoryId,
        }
        nw_getAllData('generalList/getAllList', params)
            .then((res) => {
                const validDataList = res.result.dataList.filter((dataItem) => {
                    return dataItem.allData && Object.keys(dataItem.allData).length > 0;
                })
                const total = validDataList.length;
                resolve(total);
            }).catch((err) => {
                console.log(err);
                reject(err);
            });
    });
}
