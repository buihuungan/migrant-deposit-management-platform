import { axios } from '@/utils/request'
import api from './index'
import Vue from 'vue'


//post
export function t_postAction(url, parameter) {
  return axios({
    url: url,
    method: 'post',
    data: parameter,
    baseURL: api.global_fmaking_baseURL
  })
}

//表单批量删除
export function batch_deletion(url, parameter) {
  return axios({
    url: url,
    method: 'delete',
    data: parameter,
    baseURL: api.global_fmaking_baseURL
  })
}

//post method= {post | put}
export function t_httpAction(url, parameter, method) {
  return axios({
    url: url,
    method: method,
    data: parameter,
    baseURL: api.global_fmaking_baseURL
  })
}

//put
export function t_putAction(url, parameter) {
  return axios({
    url: url,
    method: 'put',
    data: parameter,
    baseURL: api.global_fmaking_baseURL
  })
}

//get
export function t_getAction(url, parameter) {
  return axios({
    url: url,
    method: 'get',
    params: parameter,
    baseURL: api.global_fmaking_baseURL
  })
}

//deleteAction
export function t_deleteAction(url, parameter) {
  return axios({
    url: url,
    method: 'delete',
    params: parameter,
    baseURL: api.global_fmaking_baseURL
  })
}

