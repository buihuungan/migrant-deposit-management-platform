// const api_domain = "https://ii2.wust.edu.cn/poli"
import { ACCESS_TOKEN ,USER_ID ,TENANT_ID} from '@/store/mutation-types'
import Vue from 'vue'

import axios from 'axios'

//创建axios实例
var service = axios.create({
    baseURL: '/poli',
    timeout: 5000,
})

    // 创建大屏
    export function createScreen(data) {
      return service({
          url: '/screen/add',
          method: 'post',
          data: data,
      })
    }

    // 分页获取大屏
    export function getScreen(data) {
      return service({
          url: '/screen/page',
          method: 'get',
          params: data,
      })
    }

    // 获取一个大屏
    export function getOneScreen(id) {
      return service({
        url: '/screen/'+id+'/one',
        method: 'get',
      })
    }
    // 修改一个大屏
    export function changeScreen(data) {
      return service({
          url: '/screen/update',
          method: 'post',
          data: data,
      })
    }
    // 删除一个大屏
    export function deleteScreen(id) {
      return service({
          url: '/screen/'+id+'/delete',
          method: 'get',
      })
    }
    // 批量删除大屏
    export function deleteBatchScreen(ids) {
      return service({
          url: `/screen/deleteBatch?ids=${ids}`,
          method: 'get',
      })
    }
    // 获取所有数据源
    export function getDataSource() {
      return service({
        url: '/datasource/all',
        method: 'get',
      })
    }
    // 执行SQL查询
    export function SqlSelect(data) {
      return service({
        url: '/sql/query',
        method: 'get',
        params: data,
      })
    }
    // 通过Sql获取数据
    export function GetData(data) {
      return service({
        url: '/sql/query',
        method: 'post',
        data: data,
      })
    }
    // 为一个大屏创建一个组件
    // export function createComForSc(data) {
    //   return service({
    //     url: '/screen/component/add',
    //     method: 'post',
    //     data: data,
    //   })
    // }
    // 更新组件数据配置
    // export function updateCom(data) {
    //   return service({
    //     url: '/screen/component/update',
    //     method: 'post',
    //     data: data,
    //   })
    // }
    // 获取所有组件
    // export function getAllCom(data) {
    //   return service({
    //     url: '/screen/component/'+data.screenId+'/all',
    //     method: 'get',
    //   })
    // }
    // 删除一个组件
    // export function deleteCom(data) {
    //   return service({
    //     url: '/screen/component/'+data.id+'/delete',
    //     method: 'get',
    //   })
    // }
    // 获取大屏组件的数据
    export function getAllComData(data) {
      return service({
        url: '/component/data/'+data.id+'/one',
        method: 'get',
      })
    }
    // 获取组件的数据
    // export function getComData(data) {
    //   return service({
    //     url: '/sql/component/'+data.id,
    //     method: 'get',
    //   })
    // }
    // 保存、更新组件数据
    // export function saveComData(data) {
    //   return service({
    //     url: '/component/data/addOrUpdate',
    //     method: 'post',
    //     data: data,
    //   })
    // }
    export function get(url, params){    
        return new Promise((resolve, reject) =>{        
            axios.get(url, {
                params: params        
            }).then(res => {
                resolve(res.data);
            }).catch(err =>{
                reject(err.data)        
        })    
    })
    }
    export function getJson(method) {
        return new Promise((resolve, reject) => {
          axios({
            baseURL:'',
            method: 'get',
            url: method,
            dataType: "json",
            crossDomain: true,
            cache: false
          }).then(res => {
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        })
    }
    export function post(url, params) {
        return new Promise((resolve, reject) => {
             axios.post(url, QS.stringify(params))
            .then(res => {
                resolve(res.data);
            })
            .catch(err =>{
                reject(err.data)
            })
        });
    }
    // ---------------------------------------------------------------
    // 测试用
    export function postaaa(data) {
      return service({
          url: '/datasource/update',
          method: 'post',
          data: data,
          headers: {
            'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
            'userId':  Vue.ls.get(USER_ID),
            'tenant_id': Vue.ls.get(TENANT_ID)
          },
      })
    }
    // ---------------------------------------------------------------


