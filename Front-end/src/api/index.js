/*
 * @Description: 
 * @Version: 1.0
 * @Autor: LiQiu
 * @Date: 2020-11-09 15:15:05
 * @LastEditors: LiQiu
 * @LastEditTime: 2020-12-02 11:12:10
 */
const api = {
    
    // server_url: 'https://ii2.wust.edu.cn',
    // server_url: 'https://etraining.5gii.com.cn:10001',
    // server_url: 'https://scene.iiirsc.cn:10001',
    server_url: 'http://139.199.159.36',
    // server_url: 'https://ii1.wust.edu.cn',
    // server_url: 'https://10.162.32.225:8012',
    // server_url: 'http://14.55.6.35:37192',
    Login: '/sys/login',
    Logout: '/sys/logout',
    ForgePassword: '/auth/forge-password',
    Register: '/auth/register',
    SendSms: '/account/sms',
    UserInfo: '/user/info',
    user: '/api/user',
    role: '/api/role',
    service: '/api/service',
    permission: '/api/permission',
    permissionNoPager: '/api/permission/no-pager',
    global_ams_baseURL: '/api1/ams',
    global_cms_baseURL: '/cms',/* cms栏目接口公共url */
    // global_edu_baseURL: ':8012/edu',
    global_edu_baseURL: ':8012',
    global_workflow_baseURL: ':35367',/* 工作流接口公共url */
    // global_online_baseURL: '/stj-boot/stj/online',/* online表单接口公共url */
    global_online_baseURL: 'http://139.199.159.36:35367/stj/online',/* online表单接口公共url */
    // global_online_baseURL: 'http://14.55.6.35:8082/stj/online/',/* online表单接口公共url */
    global_erp_baseURL: '/erp',/* online表单接口公共url */
    global_ada_online_baseURL: '/Ada/online', /*报表配置接口*/
    global_plm_baseURL: '/plm',/*  */
    global_order_baseURL: '/order',/* 采购订单 */
    global_crm_baseURL: '/crm',/* 客户关系 */
    // global_fmaking_baseURL: 'http://14.55.6.35:38072',/* 表单设计器接口公共url */
    global_fmaking_baseURL: 'http://139.199.159.36:8025',/* 表单设计器接口公共url */

    global_workapij_baseURL: '/workflow',/* 工作流接口公共url */
    global_wmsapij_baseURL: '/wms',/* 工作流接口公共url */
    // global_course_baseURL: '/coursewareengine' ,/* 课件引擎接口公共url */
    // global_course_baseURL: '' ,/* 课件引擎接口公共url */
    global_course_baseURL: ':37192' ,/* 课件引擎接口公共url */
    usersystem:'/api1/usersystem',/* 用户体系接口公共url 调取验证码 */
    data_baseURL:'http://31789g74t4.wicp.vip',



    // server_url: 'http://121.199.72.67:10050/api1',/* 本地测试正真后台接口所在地址 */

    // server_url: 'http://47.96.254.28:8082',
    global_template_baseURL :'/identity/admin', //模板
    faketemplate_baseURL:'/fakenode/admin' ,//模拟模板
    icim_baseURL: '/identity', //标识解析模板
    ipcpm_baseURL: '/paramManage',

    fmaking_url: '/fmaking',
    online_url: '/online',
    work_url: '/workflow',
    cms_url: '/cms',
    roomUrl: '/api1/room',
    //modelUrl:'http://111.4.83.57:58026/ClassWebService.asmx'
}


module.exports = api