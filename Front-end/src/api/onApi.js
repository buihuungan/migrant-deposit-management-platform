import { axios } from '@/utils/request'
import api from './index'


//post
export function o_postAction(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL:api.global_online_baseURL
  })
}

export function o_postAction1(url,parameter,registerToken) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL:api.global_online_baseURL,
    headers: {
      'x-access-token': registerToken,
    },
  })
}

export function o_postAction2(url,registerToken) {
  return axios({
    url: url,
    method:'get' ,
    baseURL:api.server_url+api.global_course_baseURL,
    headers: {
      'x-access-token': registerToken,
    },
  })
}

//post method= {post | put}
export function o_httpAction(url,parameter,method) {
  return axios({
    url: url,
    method:method ,
    data: parameter,
    baseURL:api.global_online_baseURL
  })
}

//put
export function o_putAction(url,parameter) {
  return axios({
    url: url,
    method:'put',
    data: parameter,
    baseURL:api.global_online_baseURL
  })
}

//get
export function o_getAction(url,parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL:api.global_online_baseURL
  })
}

//deleteAction
export function o_deleteAction(url,parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL:api.global_online_baseURL
  })
}




