import { axios } from '@/utils/request'
import api from './index'


//post
export function p_postAction(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL: api.server_url+api.global_plm_baseURL
  })
}

//post
export function p_postAction2(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL: api.server_url+api.global_plm_baseURL,
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

//post method= {post | put}
export function p_httpAction(url,parameter,method) {
  return axios({
    url: url,
    method:method ,
    data: parameter,
    baseURL: api.server_url+api.global_plm_baseURL
  })
}

//put
export function p_putAction(url,parameter) {
  return axios({
    url: url,
    method:'put',
    data: parameter,
    baseURL:api.server_url+api.global_plm_baseURL
  })
}

//get
export function p_getAction(url,parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL:api.server_url+api.global_plm_baseURL
  })
}

//deleteAction
export function p_deleteAction(url,parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL:api.server_url+api.global_plm_baseURL
  })
}

