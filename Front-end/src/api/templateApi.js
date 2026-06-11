import Vue from 'vue'
import { axios } from '@/utils/request'
import api from './index'
import { ACCESS_TOKEN ,TENANT_ID} from '@/store/mutation-types'


//post
export function tm_postAction(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL:api.server_url+api.global_template_baseURL
  })
}

//模拟的新建模板接口
export function tm_fakepostAction(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL:api.server_url+api.faketemplate_baseURL
  })
}

//post method= {post | put}
export function tm_httpAction(url,parameter,method) {
  return axios({
    url: url,
    method:method ,
    data: parameter,
    baseURL:api.server_url+api.global_template_baseURL
  })
}

//put
export function tm_putAction(url,parameter) {
  return axios({
    url: url,
    method:'put',
    data: parameter,
    baseURL:api.server_url+api.global_template_baseURL
  })
}

//模拟getaction
export function tm_fakegetAction(url,parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL:api.server_url+api.faketemplate_baseURL,
    // headers: {
    //   'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
    //   'TENANT_ID': Vue.ls.get(TENANT_ID)
    // }
  })
}

//get
export function tm_getAction(url,parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL:api.server_url+api.global_template_baseURL,
    // headers: {
    //   'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
    //   'TENANT_ID': Vue.ls.get(TENANT_ID)
    // }
  })
}

//deleteAction
export function tm_deleteAction(url,parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL:api.server_url+api.global_template_baseURL
  })
}




