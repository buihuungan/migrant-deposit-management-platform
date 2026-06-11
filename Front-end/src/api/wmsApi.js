import {
  axios
} from '@/utils/request'
import api from './index'


//post
export function w_postAction(url, parameter) {
  return axios({
    url: url,
    method: 'post',
    data: parameter,
    baseURL: api.server_url + api.global_wmsapij_baseURL,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
  },
  })
}

//post method= {post | put}
export function w_httpAction(url, parameter, method) {
  return axios({
    url: url,
    method: method,
    data: parameter,
    baseURL: api.server_url + api.global_wmsapij_baseURL
  })
}

//put
export function w_putAction(url, parameter) {
  return axios({
    url: url,
    method: 'put',
    data: parameter,
    baseURL: api.server_url + api.global_wmsapij_baseURL
  })
}

//get
export function w_getAction(url, parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL: api.server_url + api.global_wmsapij_baseURL
  })
}

//deleteAction
export function w_deleteAction(url, parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL: api.server_url + api.global_wmsapij_baseURL
  })
}