import { axios } from '@/utils/request'
import api from './index'


//post
export function order_postAction(url, parameter) {
  return axios({
    url: url,
    method: 'post',
    data: parameter,
    baseURL: api.server_url + api.global_order_baseURL
    // baseURL: "/order"
  })
}
export function order_postAction2(url, parameter) {
  return axios({
    url: url,
    method: 'post',
    params: parameter,
    baseURL: api.server_url + api.global_order_baseURL
    // baseURL: "/order"
  })
}

//post method= {post | put}
export function order_httpAction(url, parameter, method) {
  return axios({
    url: url,
    method: method,
    data: parameter,
    baseURL: api.server_url + api.global_order_baseURL
  })
}

//put
export function order_putAction(url, parameter) {
  return axios({
    url: url,
    method: 'put',
    data: parameter,
    baseURL: api.server_url + api.global_order_baseURL
  })
}

//get
export function order_getAction(url, parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL: api.server_url + api.global_order_baseURL
    // baseURL: "/order"
  })
}

//deleteAction
export function order_deleteAction(url, parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL: api.server_url + api.global_order_baseURL
  })
}
