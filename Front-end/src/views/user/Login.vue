<template>
  <div class="main">
    <div element-loading-text="Loading..." v-loading.fullscreen.lock="hasToken"
      element-loading-background="rgba(255, 255, 255, 1)"></div>
    <a-form v-show="!hasToken" :form="form" class="user-layout-login" ref="formLogin" id="formLogin">
      <a-tabs :activeKey="customActiveKey" :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
        @change="handleTabClick">
        <a-tab-pane key="tab1" tab="账号密码登录">
          <a-form-item>
            <a-input size="large"
              v-decorator="['username', validatorRules.username, { validator: this.handleUsernameOrEmail }]" type="text"
              placeholder="请输入帐户名">
              <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-item>

          <a-form-item>
            <a-input v-decorator="['password', validatorRules.password]" size="large" type="password"
              autocomplete="false" placeholder="密码">
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-item>

          <a-row :gutter="0">
            <a-col :span="16">
              <a-form-item>
                <a-input v-decorator="['inputCode', validatorRules.inputCode]" size="large" type="text"
                  @change="inputCodeChange" placeholder="请输入验证码">
                  <a-icon slot="prefix" type="smile" :style="{ color: 'rgba(0,0,0,.25)' }" />
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8" style="text-align: right">
              <img v-if="requestCodeSuccess" style="margin-top: 2px" :src="randCodeImage"
                @click="handleChangeCheckCode" />
              <img v-else style="margin-top: 2px" src="../../assets/checkcode.png" @click="handleChangeCheckCode" />
            </a-col>
          </a-row>
        </a-tab-pane>
        <!-- <a-tab-pane key="tab2" tab="手机号登录">
          <a-form-item>
            <a-input v-decorator="['mobile', validatorRules.mobile]" size="large" type="text" placeholder="手机号">
              <a-icon slot="prefix" type="mobile" :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-item>

          <a-row :gutter="16">
            <a-col class="gutter-row" :span="16">
              <a-form-item>
                <a-input v-decorator="['captcha', validatorRules.captcha]" size="large" type="text"
                  placeholder="请输入验证码">
                  <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }" />
                </a-input>
              </a-form-item>
            </a-col>
            <a-col class="gutter-row" :span="8">
              <a-button class="getCaptcha" tabindex="-1" :disabled="state.smsSendBtn" @click.stop.prevent="getCaptcha"
                v-text="(!state.smsSendBtn && '获取验证码') || state.time + ' s'"></a-button>
            </a-col>
          </a-row>
        </a-tab-pane> -->
      </a-tabs>

      <!-- <a-form-item>
        <a-checkbox v-decorator="['rememberMe', {initialValue: true, valuePropName: 'checked'}]" >自动登录</a-checkbox>
        <router-link :to="{ name: 'alteration'}" class="forge-password" style="float: right;">
          忘记密码
        </router-link>
       <router-link :to="{ name: 'register'}" class="forge-password" style="float: right;margin-right: 10px" >
          注册账户
        </router-link>
      </a-form-item> -->

      <a-form-item style="margin-top: 24px">
        <a-button size="large" type="primary" htmlType="submit" class="login-button" :loading="loginBtn"
          @click.stop.prevent="handleSubmit" :disabled="loginBtn">登录</a-button>
        <div class="container">
          <!-- <a target="_blank" @click="downloadManual" style="display:flex;align-items: center;justify-content: center;">
            <span v-if="department === 'enterprise'">施工企业用户手册</span>
            <span v-else-if="department === 'brokerage'">经纪公司用户手册</span>
            <span v-else-if="department === 'mohrss'">人社局用户手册</span>
          </a> -->
          <a @click="showPdfPreview" style="display:flex;align-items: center;justify-content: center;">
            <span v-if="department === 'enterprise'"><a-icon type="book" />企业用户手册</span>
            <span v-else-if="department === 'brokerage'"><a-icon type="book" />经纪公司用户手册</span>
            <span v-else-if="department === 'mohrss'"><a-icon type="book" />工作人员手册</span>
          </a>
          <!-- <a @click="showVideoDialog" style="float: right; cursor: pointer">
            <a-icon type="video-camera" /> 视频教程
          </a> -->
        </div>
      </a-form-item>
    </a-form>

    <a-modal title="用户手册预览" :visible="previewVisible" width="80%" centered :footer="null"
      @cancel="previewVisible = false">
      <div style="height: 80vh">
        <iframe :src="pdfUrl" style="width: 100%; height: 100%" frameborder="0"></iframe>
      </div>
      <div style="text-align: center; margin-top: 16px">
        <a-button type="primary" @click="downloadManual">下载手册</a-button>
      </div>
    </a-modal>

    <two-step-captcha v-if="requiredTwoStepCaptcha" :visible="stepCaptchaVisible" @success="stepCaptchaSuccess"
      @cancel="stepCaptchaCancel" v-show="!hasToken"></two-step-captcha>
    <login-select-tenant v-show="!hasToken" ref="loginSelect" @success="loginSelectOk"></login-select-tenant>
    <!-- <third-login ref="thirdLogin"></third-login> -->

    <annTask ref="modalform"> </annTask>

    <a-modal title="视频教程" :visible="videoVisible" width="720px" @cancel="videoVisible = false" :footer="null"
      :destroyOnClose="true">
      <div style="position: relative; padding-top: 56.25%"> <!-- 16:9比例 -->
        <iframe :src="videoUrl" frameborder="0"
          allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen
          style="position: absolute; top: 0; left: 0; width: 100%; height: 100%"></iframe>
      </div>
    </a-modal>
  </div>
</template>

<script>
//import md5 from "md5"
import api from '@/api/index'
// import api from '@/api'
import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'
import Vue from 'vue'
import { ACCESS_TOKEN, ENCRYPTED_STRING } from '@/store/mutation-types'
import { putAction, postAction, getAction } from '@/api/manage'
import { encryption, getEncryptedString } from '@/utils/encryption/aesEncrypt'
import store from '@/store/'
import { USER_INFO } from '@/store/mutation-types'
// import ThirdLogin from './third/ThirdLogin'
import LoginSelectTenant from './LoginSelectTenant'
import axios from 'axios'
import { Message } from 'element-ui'
import { getRoleName, getRoleInfo } from '@/api/login'
import annTask from '@/views/newWorkFlow/modules/annTask'
import { nw_getAction, loginGetToken } from '@api/newWorkApi'

export default {
  components: {
    LoginSelectTenant,
    TwoStepCaptcha,
    annTask,
    // ThirdLogin
  },
  data() {
    return {
      department: '',
      customActiveKey: 'tab1',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      requiredTwoStepCaptcha: false,
      stepCaptchaVisible: false,
      form: this.$form.createForm(this),
      encryptedString: {
        key: '',
        iv: '',
      },
      state: {
        time: 60,
        smsSendBtn: false,
      },
      validatorRules: {
        username: { rules: [{ required: true, message: '请输入用户名!' }, { validator: this.handleUsernameOrEmail }] },
        password: { rules: [{ required: true, message: '请输入密码!', validator: 'click' }] },
        mobile: { rules: [{ validator: this.validateMobile }] },
        captcha: { rule: [{ required: true, message: '请输入验证码!' }] },
        inputCode: { rules: [{ required: true, message: '请输入验证码!' }] },
      },
      verifiedCode: '',
      inputCodeContent: '',
      inputCodeNull: true,
      currentUsername: '',
      currdatetime: '',
      randCodeImage: '',
      requestCodeSuccess: false,
      hasToken: false,
      _token: '',
      videoVisible: false,
      videoUrl: '', // 动态设置的视频URL
      previewVisible: false,
      pdfUrl: '',
    }
  },
  beforeCreate() {
    // localStorage.removeItem("pro__Access-Token")
    // this.getToken()
  },
  created() {
    this.currdatetime = new Date().getTime()
    Vue.ls.remove(ACCESS_TOKEN)
    this.getRouterData()
    this.handleChangeCheckCode()
    // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
    //this.getEncrypte();
    // update-end- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题

    // 在很多的GetPermissionList方法里都清除了token，应该会导致以后那个弹窗都不会弹出了
    // this.getToken()
  },
  mounted() {
    this.getToken()
    this.department = this.$route.query.department
    // this.$forceUpdate()
  },
  methods: {

    ...mapActions(['Login', 'Logout', 'PhoneLogin']),
    // 解析token，存入localstore
    getToken() {
      const url = window.location.href
      const params = new URLSearchParams(new URL(url).search)
      this._token = params.get('token')
      console.log('Token:', this._token) // 输出获取到的token值
      console.log('第一次' + this.$route.path)

      // 获取之后去除url中的token
      history.pushState({}, '', url.replace('token=' + this._token, ''))

      // // 创建一个Date对象表示当前时间
      // const currentDate = new Date();
      // // 获取当前时间的毫秒数（时间戳）
      // const currentTimestamp = currentDate.getTime();
      // // 创建一个新的Date对象，设置为当前时间
      // const futureDate = new Date(currentTimestamp);
      // // 将futureDate的小时数增加2小时
      // futureDate.setHours(futureDate.getHours() + 2);
      // // 获取未来时间的毫秒数（时间戳）
      // const futureTimestamp = futureDate.getTime();

      // let obj = {
      //   "value":this._token,
      //   "expire":futureTimestamp
      // }
      if (this._token) {
        // localStorage.setItem("pro__Access-Token", JSON.stringify(obj));
        this.hasToken = true

        this.validToken()

        // setTimeout(()=> {
        //   // location.reload();
        // },300)
        // location.reload();

        // if(path == 'langchain') {
        //   this.$router.push('/llm-kg/subject-one/stj-front#/langchain')
        // }
      }
    },
    validToken() {
      // 验证token是否有效
      axios({
        method: 'POST',
        url: 'https://ai.wust.edu.cn/llm-kg/subject-one/jeecg-stj/stj/sys/getUserByToken',
        // url: "http://14.55.6.35:8082/stj/sys/getUserByToken",
        data: {
          token: this._token,
        },
      }).then((response) => {
        if (response.data.code == 200) {
          // 验证成功后用这个token进行一次等于，否则登出接口会失败
          // localStorage.clear();
          this.handleSubmit()
        } else {
          Message({
            showClose: true,
            message: '请重新登录!',
            type: 'warning',
          })
          this.hasToken = false
        }
      })
    },

    // handler
    handleUsernameOrEmail(rule, value, callback) {
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        this.loginType = 0
      } else {
        this.loginType = 1
      }
      callback()
    },
    handleTabClick(key) {
      this.customActiveKey = key
      // this.form.resetFields()
    },
    handleSubmit() {
      let that = this
      let loginParams = {}
      that.loginBtn = true
      // 使用账户密码登录
      if (that.customActiveKey === 'tab1') {
        that.form.validateFields(
          ['username', 'password', 'inputCode', 'rememberMe'],
          { force: true },
          (err, values) => {
            if (!err || that.hasToken) {
              loginParams.username = values.username
              // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
              //loginParams.password = md5(values.password)
              //loginParams.password = encryption(values.password,that.encryptedString.key,that.encryptedString.iv)
              loginParams.password = values.password
              loginParams.remember_me = values.rememberMe
              // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
              loginParams.captcha = that.inputCodeContent
              loginParams.checkKey = that.currdatetime

              // 如果携带token，则直接登陆
              if (that.hasToken) {
                loginParams.tokenParam = that._token
              } else {
                loginParams.tokenParam = ''
              }

              localStorage.setItem('login_information', JSON.stringify(loginParams))
              window.sessionStorage.setItem('userName', loginParams.username)
              console.log('登录参数', loginParams)
              that
                .Login(loginParams)
                .then((res) => {
                  console.log('loginParams.username', loginParams.username)
                  if (res.code == '200') {
                    if (loginParams.username !== 'admin' && process.env.NODE_ENV === 'production') {
                      // setTimeout(() => {
                      //   window.location.href = api.server_url
                      // },10)
                    }
                    // if(this.hasToken) {
                    //   location.reload();
                    // }

                    // if(res.data.code != 200) {
                    //   that.hasToken = false;
                    // }
                  }
                  this.$refs.loginSelect.show(res.result) //去掉租户选择
                  // this.loginSuccess()
                })
                .catch((err) => {
                  that.requestFailed(err)
                  that.handleChangeCheckCode()
                })
            } else {
              that.loginBtn = false
              that.handleChangeCheckCode()
            }
          }
        )
        // 使用手机号登录
      } else {
        that.form.validateFields(['mobile', 'captcha', 'rememberMe'], { force: true }, (err, values) => {
          if (!err) {
            loginParams.mobile = values.mobile
            loginParams.captcha = values.captcha
            loginParams.remember_me = values.rememberMe
            that
              .PhoneLogin(loginParams)
              .then((res) => {
                console.log(res.result)
                this.$refs.loginSelect.show(res.result)
              })
              .catch((err) => {
                that.requestFailed(err)
              })
          }
        })
      }
    },
    getCaptcha(e) {
      e.preventDefault()
      let that = this
      this.form.validateFields(['mobile'], { force: true }, (err, values) => {
        if (!values.mobile) {
          that.cmsFailed('请输入手机号')
        } else if (!err) {
          this.state.smsSendBtn = true
          let interval = window.setInterval(() => {
            if (that.state.time-- <= 0) {
              that.state.time = 60
              that.state.smsSendBtn = false
              window.clearInterval(interval)
            }
          }, 1000)

          const hide = this.$message.loading('验证码发送中..', 0)
          let smsParams = {}
          smsParams.mobile = values.mobile
          smsParams.smsmode = '0'
          postAction('/sys/sms', smsParams)
            .then((res) => {
              if (!res.success) {
                setTimeout(hide, 0)
                this.cmsFailed(res.message)
              }
              console.log(res)
              setTimeout(hide, 500)
            })
            .catch((err) => {
              setTimeout(hide, 1)
              clearInterval(interval)
              that.state.time = 60
              that.state.smsSendBtn = false
              this.requestFailed(err)
            })
        }
      })
    },
    stepCaptchaSuccess() {
      console.log('成功调用了stepCaptchaSuccess()')
      this.loginSuccess()
    },
    stepCaptchaCancel() {
      this.Logout().then(() => {
        this.loginBtn = false
        this.stepCaptchaVisible = false
      })
    },
    handleChangeCheckCode() {
      this.currdatetime = new Date().getTime()
      getAction(`/sys/randomImage/${this.currdatetime}`)
        .then((res) => {
          if (res.success) {
            this.randCodeImage = res.result
            this.requestCodeSuccess = true
          } else {
            this.$message.error(res.message)
            this.requestCodeSuccess = false
          }
        })
        .catch(() => {
          this.requestCodeSuccess = false
        })
    },

    //获取登录用户的角色信息
    fetchUserRole() {
      return new Promise((resolve, reject) => {
        const userInfo = Vue.ls.get(USER_INFO) || {}
        const userId = userInfo.id
        if (!userId) {
          console.error('用户id不存在')
          reject('用户id不存在')
          return
        }
        // 获取角色信息
        getRoleName()
          .then((response) => {
            // 获取返回的角色信息列表
            const roleNameList = response.result
            // 获取角色 ID 列表
            getRoleInfo(userId)
              .then((roleInfoResponse) => {
                // 获取角色 ID 列表
                const roleIds = roleInfoResponse.result
                // 遍历角色 ID 列表，获取对应的 roleName
                const userRoleNames = roleIds.map((roleId) => {
                  // 在 roleNameList 中查找对应的 roleName
                  const role = roleNameList.find((role) => role.id === roleId)
                  return role ? role.roleName : null // 如果找到了对应的 roleName，就返回，否则返回 null
                })
                userInfo.roleNames = userRoleNames // 添加角色信息到 userInfo 中
                // 保存更新后的 userInfo 到 localStorage
                Vue.ls.set(USER_INFO, userInfo)
                console.log('用户信息已更新并存储:', userInfo)
                resolve()
              })
              .catch((error) => {
                console.error(error)
                reject(error)
              })
          })
          .catch((error) => {
            console.error(error)
            reject(error)
          })
      })
    },

    //获取登录用户的机构的位置信息
    getOrgInfo() {
      return new Promise((resolve, reject) => {
        const url = '/sys/user/getCurrentUserDeparts'
        getAction(url)
          .then((res) => {
            if (res.success && res.result && res.result.list) {
              const address = res.result.list.map((item) => item.address).filter((addr) => addr !== null);
              const userInfo = Vue.ls.get(USER_INFO) || {}
              userInfo.orgAddress = address
              Vue.ls.set(USER_INFO, userInfo)
              console.log('机构信息已更新并存储:', userInfo)
              resolve()
            } else {
              console.error('获取机构信息失败或数据不完整:', res.message)
              reject(new Error('获取机构信息失败'))
            }
          })
          .catch((error) => {
            console.error('获取机构信息接口调用失败:', error)
            reject(error)
          })
      })
    },

    loginSuccess() {
      console.log('登录成功，路由' + this.$route.path)
      //先获取用户机构信息
      this.getOrgInfo()
        .then(() => {
          // 再获取用户角色信息
          return this.fetchUserRole()
        })
        .then(() => {
          const userInfo = Vue.ls.get(USER_INFO) || {}
          const roleNames = userInfo.roleNames || {}
          // 假设通过判断角色名称里是否包含特定字符串来确定是否为施工企业角色，这里示例字符串为"construction_enterprise"，需根据实际调整
          const isCompany = roleNames.some((roleName) => roleName.includes('施工企业'))
          const targetPath = isCompany ? '/newWorkFlow/flowDeposit' : '/newWorkFlow/flowDeposit'
          this.$router.push({ path: targetPath }).catch(() => {
            console.log('登录跳转首页出错')
          })
        })
      this.$notification.success({
        message: '欢迎',
        description: `${timeFix()}，欢迎回来`,
      })
    },
    cmsFailed(err) {
      this.$notification['error']({
        message: '登录失败',
        description: err,
        duration: 4,
      })
    },
    requestFailed(err) {
      this.$notification['error']({
        message: '登录失败',
        description: ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试',
        duration: 4,
      })
      this.loginBtn = false
    },
    validateMobile(rule, value, callback) {
      if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)) {
        callback()
      } else {
        callback('您的手机号码格式不正确!')
      }
    },
    validateInputCode(rule, value, callback) {
      if (!value || this.verifiedCode == this.inputCodeContent) {
        callback()
      } else {
        callback('您输入的验证码不正确!')
      }
    },
    generateCode(value) {
      this.verifiedCode = value.toLowerCase()
    },
    inputCodeChange(e) {
      this.inputCodeContent = e.target.value
    },
    loginSelectOk() {
      console.log('loginSelectOk triggered, hasToken:', this.hasToken)
      console.log('成功调用了loginSelectOk() ')
      this.loginSuccess()
    },
    getRouterData() {
      this.$nextTick(() => {
        if (this.$route.params.username) {
          this.form.setFieldsValue({
            username: this.$route.params.username,
          })
        }
      })
    },
    //获取密码加密规则
    getEncrypte() {
      var encryptedString = Vue.ls.get(ENCRYPTED_STRING)
      if (encryptedString == null) {
        getEncryptedString().then((data) => {
          this.encryptedString = data
        })
      } else {
        this.encryptedString = encryptedString
      }
    },

    showPdfPreview() {
      // 根据部门设置预览PDF路径
      switch (this.department) {
        case 'enterprise':
          this.pdfUrl = 'http://139.199.159.36:37192/file/static/施工企业用户手册.pdf'; // 替换为实际PDF路径
          break;
        case 'brokerage':
          this.pdfUrl = 'http://139.199.159.36:37192/file/static/经纪公司用户手册.pdf';
          break;
        case 'mohrss':
          this.pdfUrl = 'http://139.199.159.36:37192/file/static/人社局用户手册.pdf';
          break;
      }
      console.log('pdfUrl:', this.pdfUrl)
      this.previewVisible = true;
    },
    downloadManual() {
      // 从 URL 获取 department 参数
      const department = this.$route.query.department;
      console.log('department:', department)
      const downloadTemplateApi = 'http://139.199.159.36:37192/file/download';

      let filePath, fileName;
      switch (department) {
        case 'enterprise':
          filePath = '/opt/UserManual/施工企业用户手册.pdf';
          fileName = '施工企业用户手册.pdf';
          break;
        case 'mohrss':
          filePath = '/opt/UserManual/人社局用户手册.pdf';
          fileName = '人社局用户手册.pdf';
          break;
        case 'brokerage':
          filePath = '/opt/UserManual/经纪公司用户手册.pdf';
          fileName = '经纪公司用户手册.pdf';
          break;
        default:
      }

      axios.post(downloadTemplateApi, null, {
        params: {
          filePath: filePath, // 将 filePath 作为查询参数
        },
        responseType: 'blob', // 设置响应类型为 blob，用于处理文件下载
      })
        .then((response) => {
          const blob = new Blob([response.data]);
          const downloadLink = document.createElement('a');
          const url = window.URL.createObjectURL(blob);
          downloadLink.href = url;
          downloadLink.download = fileName;
          downloadLink.click();
          window.URL.revokeObjectURL(url);
        })
        .catch((error) => {
          console.error('下载用户手册失败:', error);
          this.$message.error('下载用户手册失败');
        });
    },
    showVideoDialog() {
      // 设置不同的视频地址（
      switch (this.department) {
        case 'enterprise':
          this.videoUrl = '/videos/施工企业视频教程.mp4';
          break;
        case 'brokerage':
          this.videoUrl = '/videos/经纪公司视频教程.mp4';
          break;
        case 'mohrss':
          this.videoUrl = '/videos/人社局视频教程.mp4';
          break;
      }
      this.videoVisible = true;
    }
  },
}
</script>

<style lang="less" scoped>
.user-layout-login {
  background-color: white;
  // margin-top: 100px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}

.loading {
  height: 200px;
  width: 400px;
  margin: 200px auto;
}

.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.container a {
  text-decoration: none;
}

.video-container {
  position: relative;
  padding-top: 56.25%;
  /* 16:9比例 */
  height: 0;
  overflow: hidden;
}

video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>
<style>
.valid-error .ant-select-selection__placeholder {
  color: #f5222d;
}
</style>