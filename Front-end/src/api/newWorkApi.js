import { axios } from '@/utils/request'
import api from './index'
import Vue from 'vue'
import { USER_ID } from '@/store/mutation-types'
//post
export function nw_postAction(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    params: parameter,
    // baseURL:'/newworkflow',
    baseURL:api.server_url+api.global_course_baseURL,
    headers: {
      'userId':  Vue.ls.get(USER_ID),
    },
  })
} 

//post
export function nw_postAction1(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    // baseURL:'/newworkflow',
    baseURL: api.server_url+api.global_course_baseURL,
    headers: {
      'userId': Vue.ls.get(USER_ID),
    },
  })
}

export function nw_postAction2(url,parameter,token) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    // baseURL:'/newworkflow',
    baseURL: api.server_url+api.global_course_baseURL,
    headers: {
      'userId': Vue.ls.get(USER_ID),
      'x-access-token': token,
    },
  })
}

export function nw_delete(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    // baseURL:'/newworkflow',
    baseURL: api.server_url+api.global_course_baseURL,
    headers: {
      'userId': Vue.ls.get(USER_ID),
    },
  })
}

//注册新用户时默认登录获取token
export function loginGetToken(url,parameter) {
  return axios({
    url: url,
    method:'post',
    data: parameter,
    baseURL: api.server_url+api.global_workflow_baseURL,
    // headers: {
    //   'userId': Vue.ls.get(USER_ID),
    // },
  })
}

//获取所有数据
export function nw_getAllData(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL: api.server_url+api.global_course_baseURL,
    headers: {
      'userId': Vue.ls.get(USER_ID),
    },
  })
}

//post method= {post | put}
export function w_httpAction(url,parameter,method) {
  return axios({
    url: url,
    method:method ,
    data: parameter,
    baseURL:api.global_workapij_baseURL
  })
}

//put
export function w_putAction(url,parameter) {
  return axios({
    url: url,
    method:'put',
    data: parameter,
    baseURL:api.global_workapij_baseURL
  })
}

//get
export function nw_getAction(url,parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    // baseURL:'/newworkflow',
    baseURL: api.server_url+api.global_course_baseURL,
    headers: {
      'userId': Vue.ls.get(USER_ID),
    },
  })
}

//deleteAction
export function w_deleteAction(url,parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL:api.global_workapij_baseURL
  })
}

export function getUserList(parameter) {
  return axios({
    url: api.user,
    method: 'get',
    params: parameter
  })
}

export function getRoleList(parameter) {
  return axios({
    url: api.role,
    method: 'get',
    params: parameter
  })
}

export function getServiceList(parameter) {
  return axios({
    url: api.service,
    method: 'get',
    params: parameter
  })
}

export function getPermissions(parameter) {
  return axios({
    url: api.permissionNoPager,
    method: 'get',
    params: parameter
  })
}

// id == 0 add     post
// id != 0 update  put
export function saveService(parameter) {
  return axios({
    url: api.service,
    method: parameter.id == 0 ? 'post' : 'put',
    data: parameter
  })
}

/**
 * 下载文件 用于excel导出
 * @param url
 * @param parameter
 * @returns {*}
 */
export function downFile(url,parameter){
  return axios({
    url: url,
    params: parameter,
    method:'get' ,
    responseType: 'blob'
  })
}

