import { axios } from '@/utils/request'
import api from './index'

//post
export function e_postAction(url, parameter) {
  return axios({
    url: url,
    method: 'post',
    data: parameter,
    baseURL: api.server_url + api.global_edu_baseURL,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 实训产线开启
export function e_postProAction(url, parameter) {
  return axios({
    url: url,
    method:'get',
    params: parameter,
    baseURL: api.server_url + api.global_edu_baseURL
  })
}
// 获取实训产线状态
export function e_getProAction(url, parameter) {
  return axios({
    url: url,
    method:'get',
    params: parameter,
    baseURL:api.server_url + api.global_edu_baseURL
  })
}

//post method= {post | put}
export function e_httpAction(url, parameter, method) {
  return axios({
    url: url,
    method: method,
    data: parameter,
    baseURL: api.server_url + api.global_edu_baseURL
  })
}

//put
export function e_putAction(url, parameter) {
  return axios({
    url: url,
    method: 'put',
    data: parameter,
    baseURL: api.server_url + api.global_edu_baseURL
    // baseURL: '/apitest'
  })
}

//get
export function e_getAction(url, parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL: api.server_url + api.global_edu_baseURL
  })
}

//deleteAction
export function e_deleteAction(url, parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL: api.server_url + api.global_edu_baseURL
  })
}
