// import { axios } from '@/utils/request'
import axios from 'axios'
import Vue from 'vue'
import { ACCESS_TOKEN ,USER_ID ,TENANT_ID} from '@/store/mutation-types'


//原online的接口 需要用api2
//icim_baseURL: 'http://192.168.137.28:8082/identity',
//post  
export function im_postAction(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL:'/api2',
    headers: {
      'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
      'userId':  Vue.ls.get(USER_ID),
      'tenant_id': Vue.ls.get(TENANT_ID)
    },
  })
}
//downFile
export function im_downFile(url,parameter){
  return axios({
    url: url,
    params: parameter,
    method:'post' ,
    responseType: 'blob',
    baseURL:'/api2',
    headers: {
      'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
      'userId':  Vue.ls.get(USER_ID)
    },
  })
}
//post method= {post | put}
export function im_httpAction(url,parameter,method) {
  return axios({
    url: url,
    method:method ,
    data: parameter,
    baseURL:'/api2',
    headers: {
      'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
      'userId':  Vue.ls.get(USER_ID)
    },
  })
}

//put
export function im_putAction(url,parameter) {
  return axios({
    url: url,
    method:'put',
    data: parameter,
    baseURL:'/api2',
    headers: {
      'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
      'userId':  Vue.ls.get(USER_ID),
      'tenant_id': Vue.ls.get(TENANT_ID)
    },
  })
}

//get
export function im_getAction(url,parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL:'/api2',
    headers: {
      'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
      'userId':  Vue.ls.get(USER_ID)
    },
  })
}

//deleteAction
export function im_deleteAction(url,parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL:'/api2',
    headers: {
      'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
      'userId':  Vue.ls.get(USER_ID),
      'tenant_id': Vue.ls.get(TENANT_ID)
    },
  })
}

