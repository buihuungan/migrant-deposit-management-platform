import { axios } from '@/utils/request'
import api from './index'
import Vue from 'vue'
import { USER_ID } from '@/store/mutation-types'
//post
export function w_postAction(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    params: parameter,
    baseURL:'/apilocal',
    headers: {
      'userId':  Vue.ls.get(USER_ID),
    },
  })
}

//post
export function w_postAction1(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL:'/apilocal',
    headers: {
      'userId':  Vue.ls.get(USER_ID),
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
export function w_getAction(url,parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL:'/apilocal',
    headers: {
      'userId':  Vue.ls.get(USER_ID),
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

