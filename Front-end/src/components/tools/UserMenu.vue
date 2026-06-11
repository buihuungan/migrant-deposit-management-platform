<template>
  <div class="user-wrapper" :class="theme">
    <!-- update_begin author:zhaoxin date:20191129 for: 做头部菜单栏导航 -->
    <!-- update-begin author:sunjianlei date:20191@20 for: 解决全局样式冲突的问题 -->
    <!-- <span class="action" @click="handleJumpGateway" title="门户首页">
      <a-icon type="home" />
    </span> -->
    <!-- <span class="action" @click="handleJumpEcologyBigScreen">
      <a-icon type="pie-chart" />
    </span> -->
    <!-- <span class="action" @click="handleJump" title="标识解析大屏">
      <a-icon type="chrome" />
    </span> -->
    <!-- <span class="action" @click="showClick">
      <a-icon type="search"></a-icon>
    </span> -->
    <!-- update-begin author:sunjianlei date:20200219 for: 菜单搜索改为动态组件，在手机端呈现出弹出框 -->
    <component :is="searchMenuComp" v-show="searchMenuVisible || isMobile()" class="borders"
      :visible="searchMenuVisible" title="搜索菜单" :footer="null" @cancel="searchMenuVisible = false">
      <a-select class="search-input" showSearch :showArrow="false" placeholder="搜索菜单" optionFilterProp="children"
        :filterOption="filterOption" :open="isMobile() ? true : null" :getPopupContainer="(node) => node.parentNode"
        :style="isMobile() ? { width: '100%', marginBottom: '50px' } : {}" @change="searchMethods" @blur="hiddenClick">
        <a-select-option v-for="(site, index) in searchMenuOptions" :key="index" :value="site.id">{{
          site.meta.title
        }}</a-select-option>
      </a-select>
    </component>
    <div v-if="showRegister" style="position: relative; display: inline-block; margin-right: 3px">
      <div style="position: relative; display: inline-block;">
        <router-link :to="{ path: '/newWorkFlow/flowRegister', query: { tab: '1' } }" class="pending-link">施工企业注册待处理</router-link>
        <a-badge :count="registerTotal" :style="badgeStyle"/>
      </div>
    </div>
    <a-divider v-if="showRegister" type="vertical" />
    <div v-if="showDeposit" style="position: relative; display: inline-block; margin-right: 3px">
      <div style="position: relative; display: inline-block;">
        <router-link :to="{ path: '/newWorkFlow/flowDeposit', query: { tab: '1' } }" class="pending-link">存缴待处理</router-link>
        <a-badge :count="depositTotal" :style="badgeStyle"/>
      </div>
    </div>
    <a-divider v-if="showDeposit" type="vertical" />
    <div v-if="showUse" style="position: relative; display: inline-block; margin-right: 3px">
      <div style="position: relative; display: inline-block;">
        <router-link :to="{ path: '/newWorkFlow/flowUse', query: { tab: '1' } }" class="pending-link">使用待处理</router-link>
        <a-badge :count="useTotal" :style="badgeStyle"/>
      </div>
    </div>
    <a-divider v-if="showUse" type="vertical" />
    <div v-if="showBackpay" style="position: relative; display: inline-block; margin-right: 3px">
      <div style="position: relative; display: inline-block;">
        <router-link :to="{ path: '/newWorkFlow/flowBackPay', query: { tab: '1' } }" class="pending-link">补缴待处理</router-link>
        <a-badge :count="backpayTotal" :style="badgeStyle"/>
      </div>
    </div>
    <a-divider v-if="showBackpay" type="vertical" />
    <div v-if="showChange" style="position: relative; display: inline-block; margin-right: 3px">
      <div style="position: relative; display: inline-block;">
        <router-link :to="{ path: '/newWorkFlow/flowChange', query: { tab: '1' } }" class="pending-link">方式变更待处理</router-link>
        <a-badge :count="changeTotal" :style="badgeStyle"/>
      </div>
    </div>
    <a-divider v-if="showChange" type="vertical" />
    <div v-if="showExtend" style="position: relative; display: inline-block; margin-right: 3px">
      <div style="position: relative; display: inline-block;">
        <router-link :to="{ path: '/newWorkFlow/flowExtend', query: { tab: '1' } }" class="pending-link">更换延长待处理</router-link>
        <a-badge :count="extendTotal" :style="badgeStyle"/>
      </div>
    </div>
    <a-divider v-if="showExtend" type="vertical" />
    <div v-if="showReturn" style="position: relative; display: inline-block; margin-right: 3px">
      <div style="position: relative; display: inline-block;">
        <router-link :to="{ path: '/newWorkFlow/flowReturn', query: { tab: '1' } }" class="pending-link">返还待处理</router-link>
        <a-badge :count="returnTotal" :style="badgeStyle"/>
      </div>
    </div>
    <a-divider v-if="showReturn" type="vertical" style="margin-right: 3px" />
    <a-dropdown>
      <span class="action action-full ant-dropdown-link user-dropdown-menu">
        <!-- <a-avatar class="avatar" size="small" :src="getAvatar()" /> -->
        <span v-if="isDesktop()">欢迎您，{{ nickname() }}</span>
      </span>
      <a-menu slot="overlay" class="user-dropdown-menu-wrapper">
        <!-- <a-menu-item key="0">
          <router-link :to="{ name: 'account-center' }">
            <a-icon type="user" />
            <span>个人中心</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="1">
          <router-link :to="{ name: 'account-settings-base' }">
            <a-icon type="setting" />
            <span>账户设置</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="3" @click="systemSetting">
          <a-icon type="tool" />
          <span>系统设置</span>
        </a-menu-item> -->
        <a-menu-item key="4" @click="updatePassword">
          <a-icon type="setting" />
          <span>密码修改</span>
        </a-menu-item>
        <!-- <a-menu-item key="5" @click="updateCurrentDepart">
          <a-icon type="cluster" />
          <span>切换部门</span>
        </a-menu-item> -->
        <a-menu-item key="6" @click="clearCache">
          <a-icon type="sync" />
          <span>清理缓存</span>
        </a-menu-item>
      </a-menu>
    </a-dropdown>
    <span class="action">
      <a class="logout_title" href="javascript:;" @click="handleLogout">
        <a-icon type="logout" />
        <span v-if="isDesktop()">&nbsp;退出登录</span>
      </a>
    </span>
    <user-password ref="userPassword"></user-password>
    <depart-select ref="departSelect" :closable="true" title="部门切换"></depart-select>
    <setting-drawer ref="settingDrawer" :closable="true" title="系统设置"></setting-drawer>
  </div>
</template>

<script>
import HeaderNotice from './HeaderNotice'
import UserPassword from './UserPassword'
import SettingDrawer from '@/components/setting/SettingDrawer'
import DepartSelect from './DepartSelect'
import { mapActions, mapGetters, mapState } from 'vuex'
import { mixinDevice } from '@/utils/mixin.js'
import { getFileAccessHttpUrl, getAction } from '@/api/manage'
import { USER_INFO } from '@/store/mutation-types'
import Vue from 'vue'
import { USER_ID } from '@/store/mutation-types'
import { UI_CACHE_DB_DICT_DATA } from '@/store/mutation-types'
import api from '@/api/index'
import { getPendingTotal, AutoClaim } from '@/api/userList'
import { depositList, depositCategoryId, useList, useCategoryId, backpayList, backpayCategoryId, changeList, changeCategoryId, extendList, extendCategoryId, returnList, returnCategoryId, registerList, registerCategoryId } from '@/api/processId'
import { extend } from 'vuedraggable'
export default {
  name: 'UserMenu',
  mixins: [mixinDevice],
  data() {
    return {
      showRegister: false,
      showDeposit: false,// 是否显示保证金存缴待处理
      showUse: false,// 是否显示保证金使用待处理
      showBackpay: false,// 是否显示保证金补缴待处理
      showChange: false,// 是否显示保证金补缴待处理
      showExtend: false,// 是否显示保证金更换待处理
      showReturn: false,// 是否显示保证金返还待处理
      registerTotal: 0,
      depositTotal: 0,    // 保证金存缴待处理数量
      useTotal: 0,    // 保证金使用待处理数量
      backpayTotal: 0, //保证金补缴待处理数量
      changeTotal: 0,    // 保证金存缴方式变更待处理数量
      extendTotal: 0,    // 保证金存缴方式变更待处理数量
      returnTotal: 0,    // 保证金返还待处理数量
      intervalId: null,
      // update-begin author:sunjianlei date:20200219 for: 头部菜单搜索规范命名 --------------
      searchMenuOptions: [],
      searchMenuComp: 'span',
      searchMenuVisible: false,
      // update-begin author:sunjianlei date:20200219 for: 头部菜单搜索规范命名 --------------
    }
  },
  components: {
    HeaderNotice,
    UserPassword,
    DepartSelect,
    SettingDrawer,
  },
  props: {
    theme: {
      type: String,
      required: false,
      default: 'dark',
    },
  },
  /* update_begin author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
  created() {
    let lists = []
    this.searchMenus(lists, this.permissionMenuList)
    console.log('this.permissionMenuList', this.permissionMenuList)
    this.searchMenuOptions = [...lists]
    // 获取路由的查询参数，并根据参数设置选中的标签页
    const tab = this.$route.query.tab;
    if (tab !== undefined) {
      this.taskTab.tabKey = tab; // 如果传入了tab参数，设置activeTab
    }
  },
  mounted() {
    this.startInterval()
    this.getPermission()
    //使用事件总线监听事件
    this.$bus.$on('callGetTotal', () => {
      this.getTotal()
    })

    //如果是单点登录模式
    if (process.env.VUE_APP_SSO == 'true') {
      let depart = this.userInfo().orgCode
      if (!depart) {
        this.updateCurrentDepart()
      }
    }
  },
  computed: {
    ...mapState({
      // 后台菜单
      permissionMenuList: (state) => state.user.permissionList,
    }),
    getUserInfo() {
      return Vue.ls.get(USER_INFO) || {}
    },
    badgeStyle() {
      return {
        position: 'absolute',
        top: '-12px',
        right: '-15px',
        transform: 'scale(0.8)',
        transformOrigin: 'center',
        color: '#fff',
        zIndex: 1
      }
    },
  },
  /* update_end author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
  watch: {
    // update-begin author:sunjianlei date:20200219 for: 菜单搜索改为动态组件，在手机端呈现出弹出框
    device: {
      immediate: true,
      handler() {
        this.searchMenuVisible = false
        this.searchMenuComp = this.isMobile() ? 'a-modal' : 'span'
      },
    },
    // update-end author:sunjianlei date:20200219 for: 菜单搜索改为动态组件，在手机端呈现出弹出框
  },
  methods: {
    // 获取菜单权限判断是否展示待处理数量
    getPermission() {
      let permission = this.permissionMenuList;
      console.log('菜单权限', permission);
      permission.forEach((item) => {
        if (item.component === 'newWorkFlow/flowDeposit') {
          this.showDeposit = true;
          return; // 继续检查其他项
        }
        // 检查保证金使用
        if (item.children && item.children.length > 0 && !this.showUse) {
          this.checkChildren(item.children, 'newWorkFlow/flowUse', (found) => {
            this.showUse = found;
          });
        }
        // 检查保证金补缴
        if (item.children && item.children.length > 0 && !this.showBackpay) {
          this.checkChildren(item.children, 'newWorkFlow/flowBackPay', (found) => {
            this.showBackpay = found;
          });
        }
        if (item.component === 'newWorkFlow/flowChange') {
          this.showChange = true;
          return;
        }
        if (item.component === 'newWorkFlow/flowExtend') {
          this.showExtend = true;
          return;
        }
        if (item.component === 'newWorkFlow/flowReturn') {
          this.showReturn = true;
          return;
        }
        if (item.component === 'newWorkFlow/flowRegister') {
          this.showRegister = true;
          return;
        }
      });
    },

    // 新增递归函数
    checkChildren(children, targetComponent, callback) {
      for (const child of children) {
        if (child.component === targetComponent) {
          callback(true);
          console.log('找到目标组件：', child.component);
          return true; // 终止递归
        }
        if (child.children && child.children.length > 0) {
          const found = this.checkChildren(child.children, targetComponent, callback);
          if (found) return true; // 子节点找到后终止上层递归
        }
      }
      return false;
    },
    // 获取所有办事项的总数
    getTotal() {
      // 获取保证金存缴待办事项总数
      getPendingTotal(depositList, depositCategoryId)
        .then((total) => {
          this.depositTotal = total
          console.log('depositTotal', this.depositTotal)
        })
        .catch((error) => {
          console.error('获取待办事项总数失败:', error)
        })
      // 获取保证金使用待办事项总数
      getPendingTotal(useList, useCategoryId)
        .then((total) => {
          this.useTotal = total
          console.log('useTotal', this.useTotal)
        })
        .catch((error) => {
          console.error('获取待办事项总数失败:', error)
        })
      // 获取保证金补缴待办事项总数
      getPendingTotal(backpayList, backpayCategoryId)
        .then((total) => {
          this.backpayTotal = total
          console.log('backpayTotal', this.backpayTotal)
        })
        .catch((error) => {
          console.error('获取待办事项总数失败:', error)
        })
      // 获取保证金方式变更待办事项总数
      getPendingTotal(changeList, changeCategoryId)
        .then((total) => {
          this.changeTotal = total
          console.log('changeTotal', this.changeTotal)
        })
        .catch((error) => {
          console.error('获取待办事项总数失败:', error)
        })
      // 获取保证金保函更换待办事项总数
      getPendingTotal(extendList, extendCategoryId)
        .then((total) => {
          this.extendTotal = total
          console.log('extendTotal', this.extendTotal)
        })
        .catch((error) => {
          console.error('获取待办事项总数失败:', error)
        })
      // 获取保证金返还待办事项总数
      getPendingTotal(returnList, returnCategoryId)
        .then((total) => {
          this.returnTotal = total
          console.log('returnTotal', this.returnTotal)
        })
        .catch((error) => {
          console.error('获取待办事项总数失败:', error)
        })
      getPendingTotal(registerList, registerCategoryId)
        .then((total) => {
          this.registerTotal = total
        })
        .catch((error) => {
          console.error('获取待办事项总数失败:', error)
        })
    },

    // 开启定时器
    async startInterval() {
      await AutoClaim(depositList, depositCategoryId, this.getUserInfo) // 自动认领该用户的保证金存缴的流程
      await AutoClaim(useList, useCategoryId, this.getUserInfo) // 自动认领该用户的保证金使用的流程
      await AutoClaim(backpayList, backpayCategoryId, this.getUserInfo) // 自动认领该用户的保证金补缴的流程
      await AutoClaim(changeList, changeCategoryId, this.getUserInfo) // 自动认领该用户的保证金存缴方式变更的流程
      await AutoClaim(extendList, extendCategoryId, this.getUserInfo) // 自动认领该用户的保证金保函更换的流程
      await AutoClaim(returnList, returnCategoryId, this.getUserInfo) // 自动认领该用户的保证金返还的流程
      await AutoClaim(registerList, registerCategoryId, this.getUserInfo) // 自动认领该用户的保证金返还的流程
      console.log('开启保证金存缴的自动认领')
      this.getTotal()
      this.intervalId = setInterval(async () => {
        await AutoClaim(depositList, depositCategoryId)
        this.getTotal()
      }, 180000)
      this.intervalId = setInterval(async () => {
        await AutoClaim(useList, useCategoryId)
        this.getTotal()
      }, 180000)
      this.intervalId = setInterval(async () => {
        await AutoClaim(backpayList, backpayCategoryId)
        this.getTotal()
      }, 180000)
      this.intervalId = setInterval(async () => {
        await AutoClaim(changeList, changeCategoryId)
        this.getTotal()
      }, 180000)
      this.intervalId = setInterval(async () => {
        await AutoClaim(extendList, extendCategoryId)
        this.getTotal()
      }, 180000)
      this.intervalId = setInterval(async () => {
        await AutoClaim(returnList, returnCategoryId)
        this.getTotal()
      }, 180000)
      this.intervalId = setInterval(async () => {
        await AutoClaim(registerList, registerCategoryId)
        this.getTotal()
      }, 180000)
    },

    /* update_begin author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
    showClick() {
      this.searchMenuVisible = true
    },
    hiddenClick() {
      this.shows = false
    },
    /* update_end author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
    ...mapActions(['Logout']),
    ...mapGetters(['nickname', 'avatar', 'userInfo']),
    getAvatar() {
      return getFileAccessHttpUrl(this.avatar())
    },
    handleLogout() {
      const that = this
      this.$elementConfirm('真的要注销登录吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          return that
            .Logout({})
            .then(() => {
              // update-begin author:wangshuai date:20200601 for: 退出登录跳转登录页面
              that.$router.push({ path: '/user/entry-select' })
              // update-end author:wangshuai date:20200601 for: 退出登录跳转登录页面
              //window.location.reload()
            })
            .catch((err) => {
              that.$message.error({
                title: '错误',
                description: err.message,
              })
            })
        })
        .catch(() => { })
    },
    updatePassword() {
      let username = this.userInfo().username
      this.$refs.userPassword.show(username)
    },
    updateCurrentDepart() {
      this.$refs.departSelect.show()
    },
    systemSetting() {
      this.$refs.settingDrawer.showDrawer()
    },
    /* update_begin author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
    searchMenus(arr, menus) {
      for (let i of menus) {
        if (!i.hidden && 'layouts/RouteView' !== i.component) {
          arr.push(i)
        }
        if (i.children && i.children.length > 0) {
          this.searchMenus(arr, i.children)
        }
      }
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
    },
    // update_begin author:sunjianlei date:20191230 for: 解决外部链接打开失败的问题
    searchMethods(value) {
      let route = this.searchMenuOptions.filter((item) => item.id === value)[0]
      if (route.meta.internalOrExternal === true || route.component.includes('layouts/IframePageView')) {
        window.open(route.meta.url, '_blank')
      } else {
        this.$router.push({ path: route.path })
      }
      this.searchMenuVisible = false
    },
    // update_end author:sunjianlei date:20191230 for: 解决外部链接打开失败的问题
    /*update_end author:zhaoxin date:20191129 for: 做头部菜单栏导航*/
    /*update_begin author:liushaoqian date:20200507 for: 刷新缓存*/
    clearCache() {
      getAction('sys/dict/refleshCache')
        .then((res) => {
          if (res.success) {
            //重新加载缓存
            getAction('sys/dict/queryAllDictItems').then((res) => {
              if (res.success) {
                Vue.ls.remove(UI_CACHE_DB_DICT_DATA)
                Vue.ls.set(UI_CACHE_DB_DICT_DATA, res.result, 7 * 24 * 60 * 60 * 1000)
              }
            })
            this.$message.success('刷新缓存完成！')
          }
        })
        .catch((e) => {
          this.$message.warn('刷新缓存失败！')
          console.log('刷新失败', e)
        })
    },
  },
  beforeDestroy() {
    clearInterval(this.intervalId)
    console.log('关闭获取待办事项数量的定时器')
    this.$bus.$off('callGetTotal')
    console.log('关闭事件总线监听')
  },
}
</script>

<style lang="less" scoped>
/* update_begin author:zhaoxin date:20191129 for: 让搜索框颜色能随主题颜色变换*/
/* update-begin author:sunjianlei date:20191220 for: 解决全局样式冲突问题 */
.user-wrapper .search-input {
  width: 180px;
  color: inherit;

  /deep/ .ant-select-selection {
    background-color: inherit;
    border: 0;
    border-bottom: 1px solid white;

    &__placeholder,
    &__field__placeholder {
      color: inherit;
    }
  }
}

/* update-end author:sunjianlei date:20191220 for: 解决全局样式冲突问题 */
/* update_end author:zhaoxin date:20191129 for: 让搜索框颜色能随主题颜色变换*/

.pending-link {
  font-size: 13px;
  color: #fff;
  text-decoration: none;
  transition: opacity 0.3s ease;

  &:hover {
    opacity: 0.8;
  }
}
</style>

<style scoped>
.logout_title {
  color: inherit;
  text-decoration: none;
}
</style>
