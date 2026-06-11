/** init domain config */
import Vue from 'vue'
//设置全局API_BASE_URL
Vue.prototype.API_BASE_URL = process.env.VUE_APP_API_BASE_URL
Vue.prototype.API_DOCUMENT_URL = process.env.VUE_APP_COUESE_BASE_URL
window._CONFIG['domianURL'] = Vue.prototype.API_BASE_URL
window._CONFIG['DocumentURL'] = Vue.prototype.API_DOCUMENT_URL
//单点登录地址
window._CONFIG['casPrefixUrl'] = process.env.VUE_APP_CAS_BASE_URL
window._CONFIG['onlinePreviewDomainURL'] =  process.env.VUE_APP_ONLINE_BASE_URL
window._CONFIG['staticDomainURL'] = Vue.prototype.API_BASE_URL + '/sys/common/static'
window._CONFIG['staticDocumentURL'] = Vue.prototype.API_DOCUMENT_URL + '/file/static'
window._CONFIG['pdfDomainURL'] = Vue.prototype.API_BASE_URL+ '/sys/common/pdf/pdfPreviewIframe'