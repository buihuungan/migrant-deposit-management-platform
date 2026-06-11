// import { axios } from '@/utils/request'
import axios from 'axios'
import api from './index'

//post
export function pm_postAction(url,parameter) {
  return axios({
    url: url,
    method:'post' ,
    data: parameter,
    baseURL:'/api2'
  })
}

//post method= {post | put}
export function pm_httpAction(url,parameter,method) {
  return axios({
    url: url,
    method:method ,
    data: parameter,
    baseURL:'/api2'
  })
}

//put
export function pm_putAction(url,parameter) {
  return axios({
    url: url,
    method:'put',
    data: parameter,
    baseURL:'/api2'
  })
}

//get
export function pm_getAction(url,parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL:'/api2'
  })
}

//deleteAction
export function pm_deleteAction(url,parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL:'/api2'
  })
}

