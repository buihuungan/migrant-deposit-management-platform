import Vue from 'vue'
import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import notification from 'ant-design-vue/es/notification'
import { ACCESS_TOKEN, INDEX_MAIN_PAGE_PATH } from '@/store/mutation-types'
import { generateIndexRouter } from "@/utils/util"

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/user/register', '/user/register-result', '/user/alteration', '/user/entry-select'] // 添加 /user/entry-select

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  if (to.path === '/stjlangchain') {
    next();
  } else {
    if (Vue.ls.get(ACCESS_TOKEN)) {
      // 有 token 的情况下
      if (to.path === '/user/login') {
        next({ path: INDEX_MAIN_PAGE_PATH }) // 有 token 的情况下重定向到主页
        NProgress.done()
      } else {
        if (store.getters.permissionList.length === 0) {
          store.dispatch('GetPermissionList').then(res => {
            const menuData = res.result.menu;
            if (!menuData) return;
            let constRoutes = [];
            constRoutes = generateIndexRouter(menuData);
            // 添加主界面路由
            store.dispatch('UpdateAppRouter', { constRoutes }).then(() => {
              // 动态添加可访问路由表
              router.addRoutes(store.getters.addRouters)
              const redirect = decodeURIComponent(from.query.redirect || to.path)
              if (to.path === redirect) {
                next({ ...to, replace: true })
              } else {
                next({ path: redirect })
              }
            })
          }).catch(() => {
            store.dispatch('Logout').then(() => {
              next({ path: '/user/entry-select', query: { redirect: to.fullPath } })
              NProgress.done()
            })
          })
        } else {
          next()
        }
      }
    } else {
      // 没有 token 的情况下
      if (to.path === '/user/login' && from.path === '/user/entry-select') {
        // 防止从入口选择页面进入登录页面的循环
        next()
        console.log('跳转到登录，来源路径是入口选择:', to);
      } else if (to.path === '/user/login') {
        // 未登录用户不能直接访问登录页面，重定向到入口选择
        next({ path: '/user/entry-select', query: { redirect: to.fullPath } });
        console.log('未登录不能直接访问login', to);
      } else if ((whiteList.indexOf(to.path) !== -1)) {
        // 在白名单内的页面直接进入
        next()
        console.log('白名单', to);
      } 
      // else {
      //   // 这里添加一个标识
      //   if (from.path !== '/user/login') {
      //     next({ path: '/user/entry-select', query: { redirect: to.fullPath } })
      //     console.log('来源路径不是登录', to);
      //   } else {
      //     next(); // 已经在登录页，允许继续
      //   }
      // }
      NProgress.done() // 如果当前页面是 login 或 entry-select，则手动触发 NProgress 完成
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
