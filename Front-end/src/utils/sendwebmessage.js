import Vue from 'vue'
import axios from 'axios'
import { USER_INFO, USER_ID } from '@/store/mutation-types'
import api from '@/api/index'

//type为1时代表流程，为2时代表节点开启后传入需要的信息
const websocketmessage = (type, message) => {
  let params = {
    type,
    message
  }
  console.log('哈哈哈', Vue.ls.get(USER_ID));
  let login_info = Vue.ls.get(USER_INFO).username
  axios({
    // http://10.162.33.222:27134
    method: 'post',
    url: api.server_url+`/identitydisplay/scada/sendToScada/${login_info}`,
    data: params
  }).then((res) => {
    console.log('resgagg', res);
  }).catch((err) => {
    console.log('err', err);
  })
}
// const sendprocessId = (user,type, message) => {
//   let params = {
//     type,
//     message
//   }
//   axios({
//     method: 'post',
//     url: `http://10.162.33.222:27134/cict/processId/${user}`,
//     data: params
//   }).then((res) => {
//     console.log('resgagg', res);
//   }).catch((err) => {
//     console.log('err', err);
//   })
// }, sendprocessId 
export { websocketmessage}