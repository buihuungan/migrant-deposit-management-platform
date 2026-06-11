/** init domain config */
import './config'


import Vue from 'vue'
import element from './elementUI.js'
import Antd, { version, message } from 'ant-design-vue'
import Storage from 'vue-ls'
import store from './store/'
import { VueAxios } from "@/utils/request"
import $ from 'jquery'
import '../node_modules/jquery/dist/jquery.min.js'

require('@jeecg/antd-online-mini')
require('@jeecg/antd-online-mini/dist/OnlineForm.css')

import echarts from 'echarts'
Vue.prototype.$echarts = echarts

// 可拖动组件
import VueDraggableResizable from 'vue-draggable-resizable'
// 可选择导入默认样式
import 'vue-draggable-resizable/dist/VueDraggableResizable.css'
Vue.component('vue-draggable-resizable', VueDraggableResizable)

import Viser from 'viser-vue'
import 'ant-design-vue/dist/antd.less';  // or 'ant-design-vue/dist/antd.less'
Vue.prototype.$message = message

import '@/permission' // permission control
import '@/utils/filter' // base filter
import Print from 'vue-print-nb-jeecg'
/*import '@babel/polyfill'*/
import preview from 'vue-photo-preview'
import 'vue-photo-preview/dist/skin.css'
import SSO from '@/cas/sso.js'
import {
  ACCESS_TOKEN,
  DEFAULT_COLOR,
  DEFAULT_THEME,
  DEFAULT_LAYOUT_MODE,
  DEFAULT_COLOR_WEAK,
  SIDEBAR_TYPE,
  DEFAULT_FIXED_HEADER,
  DEFAULT_FIXED_HEADER_HIDDEN,
  DEFAULT_FIXED_SIDEMENU,
  DEFAULT_CONTENT_WIDTH_TYPE,
  DEFAULT_MULTI_PAGE
} from "@/store/mutation-types"
import config from '@/defaultSettings'


import JDictSelectTag from './components/dict/index.js'
import hasPermission from '@/utils/hasPermission'
import vueBus from '@/utils/vueBus';
import JeecgComponents from '@/components/jeecg/index'
import '@/assets/less/JAreaLinkage.less'
import VueAreaLinkage from 'vue-area-linkage'
import '@/components/jeecg/JVxeTable/install'
import '@/components/JVxeCells/install'
import FormMaking from './components/FormMaking/index.js'
// import VueI18n from 'vue-i18n'
// import ElementUI, { Message } from 'element-ui'
import Board from './components/Board/index.js'//引入看板
import 'element-ui/lib/theme-chalk/index.css'

// import ElementUI from 'element-ui'
import VideoPlayer from 'vue-video-player'
require('video.js/dist/video-js.css')
require('vue-video-player/src/custom-theme.css')

import CKEditor from '@ckeditor/ckeditor5-vue2';
// import VueCoreVideoPlayer from 'vue-core-video-player'
// import i18n from './i18n/index'
import App from './App.vue'
import router from './router'

Vue.use(CKEditor);

// Vue.use(ElementUI,{
//   i18n:(key,value) => i18n.t(key,value)
// });

Vue.config.productionTip = false
Vue.use(FormMaking)
Vue.use(Board)
// Vue.use(VueCoreVideoPlayer)
Vue.use(Storage, config.storageOptions)
Vue.use(Antd)
Vue.use(VueAxios, router)
Vue.use(Viser)
Vue.use(hasPermission)
Vue.use(JDictSelectTag)
Vue.use(Print)
Vue.use(preview)
Vue.use(vueBus);
Vue.use(JeecgComponents);
Vue.use(VueAreaLinkage);
Vue.use(element)
// Vue.use(element,{
//   i18n:(key,value) => i18n.t(key,value)
// })
Vue.use(VideoPlayer)

// require('video.js/dist/video-js.css')
// require('vue-video-player/src/custom-theme.css')
// const hls = require('videojs-contrib-hls')	//我这里import不成功，所以只能使用这种方式
// Vue.use(hls)
// Vue.use(VideoPlayer)

SSO.init(() => {
  main()
})
function main() {
  new Vue({
    router,
    store,
    $,
    // i18n,
    mounted() {
      store.commit('SET_SIDEBAR_TYPE', Vue.ls.get(SIDEBAR_TYPE, true))
      store.commit('TOGGLE_THEME', Vue.ls.get(DEFAULT_THEME, config.navTheme))
      store.commit('TOGGLE_LAYOUT_MODE', Vue.ls.get(DEFAULT_LAYOUT_MODE, config.layout))
      store.commit('TOGGLE_FIXED_HEADER', Vue.ls.get(DEFAULT_FIXED_HEADER, config.fixedHeader))
      store.commit('TOGGLE_FIXED_SIDERBAR', Vue.ls.get(DEFAULT_FIXED_SIDEMENU, config.fixSiderbar))
      store.commit('TOGGLE_CONTENT_WIDTH', Vue.ls.get(DEFAULT_CONTENT_WIDTH_TYPE, config.contentWidth))
      store.commit('TOGGLE_FIXED_HEADER_HIDDEN', Vue.ls.get(DEFAULT_FIXED_HEADER_HIDDEN, config.autoHideHeader))
      store.commit('TOGGLE_WEAK', Vue.ls.get(DEFAULT_COLOR_WEAK, config.colorWeak))
      store.commit('TOGGLE_COLOR', Vue.ls.get(DEFAULT_COLOR, config.primaryColor))
      store.commit('SET_TOKEN', Vue.ls.get(ACCESS_TOKEN))
      store.commit('SET_MULTI_PAGE', Vue.ls.get(DEFAULT_MULTI_PAGE, config.multipage))
    },
    render: h => h(App)
  }).$mount('#app')
}
