import { axios } from '@/utils/request'
import api from './index'


//post
export function crm_postAction(url, parameter) {
  return axios({
    url: url,
    method: 'post',
    data: parameter,
    baseURL: api.server_url + api.global_crm_baseURL
  })
}

//post method= {post | put}
export function crm_httpAction(url, parameter, method) {
  return axios({
    url: url,
    method: method,
    data: parameter,
    baseURL: api.server_url + api.global_crm_baseURL
  })
}

//put
export function crm_putAction(url, parameter) {
  return axios({
    url: url,
    method: 'put',
    data: parameter,
    baseURL: api.server_url + api.global_crm_baseURL
  })
}

//get
export function crm_getAction(url, parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL: api.server_url + api.global_crm_baseURL
  })
}

//deleteAction
export function crm_deleteAction(url, parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL: api.server_url + api.global_crm_baseURL
  })
}

