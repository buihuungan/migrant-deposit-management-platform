import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Element from 'element-ui'

import zhLocale from 'element-ui/lib/locale/lang/zh-CN'
import enLocale from 'element-ui/lib/locale/lang/en'

Vue.use(VueI18n)

const zh = require('./locale/zh')
const en = require('./locale/en')

const messages = {
  // 中文语言包
  'zh-CN': zh,
  // 英文语言包
  'en-US': en
}

const i18n = new VueI18n({
  // 语言标识
  locale: localStorage.getItem('lang') || 'zh_CN',
  //this.$i18n.locale // 通过切换locale的值来实现语言切换
  silentTranslationWarn: true,
  messages: messages
})
Vue.use(Element, {
  i18n: (key, value) => i18n.t(key, value)
})
// 暴露i18n
export default i18n
