import { axios } from '@/utils/request'
import api from './index'

//post
export function erp_postAction(url, parameter) {
    return axios({
        url: url,
        method: 'post',
        data: parameter,
        baseURL: api.server_url + api.global_erp_baseURL,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
        },
    },
    )
}

//post method= {post | put}
export function erp_httpAction(url, parameter, method) {
    return axios({
        url: url,
        method: method,
        data: parameter,
        baseURL: api.server_url + api.global_erp_baseURL
    })
}

//put
export function erp_putAction(url, parameter) {
    return axios({
        url: url,
        method: 'put',
        data: parameter,
        baseURL: api.server_url + api.global_erp_baseURL
    })
}

//get
export function erp_getAction(url, parameter) {
    return axios({
        url: url,
        method: 'get',
        params: parameter,
        baseURL: api.server_url + api.global_erp_baseURL
    })
}

//deleteAction
export function erp_deleteAction(url, parameter) {
    return axios({
        url: url,
        method: 'delete',
        params: parameter,
        baseURL: api.server_url + api.global_erp_baseURL
    })
}

export function erp_postProAction(url, parameter) {
  return axios({
    url: url,
    method:'get',
    params: parameter,
    baseURL: api.server_url + api.global_erp_baseURL
  })
}

export function erp_getProAction(url, parameter) {
  return axios({
    url: url,
    method:'get',
    params: parameter,
    baseURL:api.server_url + api.global_erp_baseURL
  })
}