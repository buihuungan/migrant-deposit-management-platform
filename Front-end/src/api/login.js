import api from './index'
import { axios } from '@/utils/request'
import Vue from 'vue'
import { USER_ID } from '@/store/mutation-types'
/**
 * login func
 * parameter: {
 *     username: '',
 *     password: '',
 *     remember_me: true,
 *     captcha: '12345'
 * }
 * @param parameter
 * @returns {*}
 */
export function login(parameter) {
  return axios({
    url: '/sys/login',
    method: 'post',
    data: parameter
  })
}

//获取登录用户的角色id信息
export function getRoleInfo(userid) {
  return axios({
    url: `/stj/sys/user/queryUserRole?&userid=${userid}` ,
    method:'get',
    baseURL: api.server_url+api.global_workflow_baseURL,
    headers: {
      'userId': Vue.ls.get(USER_ID),
    },
  })
}

//获取全部角色id和对应的角色名称
export function getRoleName() {
  return axios({
    url: '/stj/sys/role/queryall',
    method:'get',
    baseURL: api.server_url+api.global_workflow_baseURL,
    headers: {
      'userId': Vue.ls.get(USER_ID),
    },
  })
}

export function phoneLogin(parameter) {
  return axios({
    url: '/sys/phoneLogin',
    method: 'post',
    data: parameter
  })
}

export function getSmsCaptcha(parameter) {
  return axios({
    url: api.SendSms,
    method: 'post',
    data: parameter
  })
}

// export function getInfo() {
//   return axios({
//     url: '/api/user/info',
//     method: 'get',
//     headers: {
//       'Content-Type': 'application/json;charset=UTF-8'
//     }
//   })
// }

export function logout(logoutToken) {
  return axios({
    url: '/sys/logout',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'X-Access-Token':  logoutToken
    }
  })
}

/**
 * 第三方登录
 * @param token
 * @param thirdType
 * @returns {*}
 */
export function thirdLogin(token,thirdType) {
  return axios({
    url: `/sys/thirdLogin/getLoginUser/${token}/${thirdType}`,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}