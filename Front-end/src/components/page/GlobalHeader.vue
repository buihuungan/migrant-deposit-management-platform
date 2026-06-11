<template>
  <div>
    <a-modal title="用户手册预览" :visible="previewVisible" width="80%" centered :footer="null"
      @cancel="previewVisible = false">
      <div style="height: 80vh">
        <iframe :src="pdfUrl" style="width: 100%; height: 100%" frameborder="0"></iframe>
      </div>
      <div style="text-align: center; margin-top: 16px">
        <a-button type="primary" @click="downloadManual">下载手册</a-button>
      </div>
    </a-modal>

    <a-modal title="视频教程" :visible="videoVisible" width="720px" @cancel="videoVisible = false" :footer="null"
      :destroyOnClose="true">
      <div style="position: relative; padding-top: 56.25%"> <!-- 16:9比例 -->
        <iframe :src="videoUrl" frameborder="0"
          allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen
          style="position: absolute; top: 0; left: 0; width: 100%; height: 100%"></iframe>
      </div>
    </a-modal>

    <!-- 原布局头部 -->
    <a-layout-header v-if="!headerBarFixed"
      :class="[fixedHeader && 'ant-header-fixedHeader', sidebarOpened ? 'ant-header-side-opened' : 'ant-header-side-closed',]"
      :style="{ padding: '0' }">

      <div v-if="mode === 'sidemenu'" class="header" :class="theme">
        <a-icon v-if="device === 'mobile'" class="trigger" :type="collapsed ? 'menu-fold' : 'menu-unfold'"
          @click="toggle"></a-icon>
        <a-icon v-else class="trigger" :type="collapsed ? 'menu-unfold' : 'menu-fold'" @click="toggle" />

        <div class="header-content">
          <div class="welcome-line">
            <span v-if="device === 'desktop'" class="welcome-text">欢迎进入保证金智慧管理服务平台</span>
            <span v-else class="welcome-text">保证金智慧管理服务平台</span>
          </div>
          <div class="button-line">
            <a-button type="link" class="user-manual-btn" @click="showPdfPreview">
              <a-icon type="book" />用户手册
            </a-button>
            <!-- <a-button type="link" class="user-manual-btn" @click="showVideoDialog">
              <a-icon type="video-camera" />视频教程
            </a-button> -->
          </div>
        </div>
        <div>
          <user-menu :theme="theme" />
        </div>
      </div>

      <!-- 顶部导航栏模式 -->
      <div v-else :class="['top-nav-header-index', theme]">
        <div class="header-index-wide">
          <div class="header-index-left" :style="topMenuStyle.headerIndexLeft">
            <logo class="top-nav-header" :show-title="device !== 'mobile'" :style="topMenuStyle.topNavHeader" />
            <div v-if="device !== 'mobile'" :style="topMenuStyle.topSmenuStyle">
              <s-menu mode="horizontal" :menu="menus" :theme="theme"></s-menu>
            </div>
            <a-icon v-else class="trigger" :type="collapsed ? 'menu-fold' : 'menu-unfold'" @click="toggle"></a-icon>
          </div>
          <user-menu class="header-index-right" :theme="theme" :style="topMenuStyle.headerIndexRight" />
        </div>
      </div>
    </a-layout-header>
  </div>
</template>

<script>
import UserMenu from '../tools/UserMenu'
// import SMenu from '../menu/'
import Logo from '../tools/Logo'

import { mixin } from '@/utils/mixin.js'
import { Button } from 'ant-design-vue'
import Vue from 'vue'
import { USER_INFO } from '@/store/mutation-types'
import { Modal } from 'bootstrap'
import axios from 'axios'

export default {
  name: 'GlobalHeader',
  components: {
    UserMenu,
    // SMenu,
    Logo,
    Button,
    Modal
  },
  mixins: [mixin],
  props: {
    mode: {
      type: String,
      // sidemenu, topmenu
      default: 'sidemenu'
    },
    menus: {
      type: Array,
      required: true
    },
    theme: {
      type: String,
      required: false,
      default: 'dark'
    },
    collapsed: {
      type: Boolean,
      required: false,
      default: false
    },
    device: {
      type: String,
      required: false,
      default: 'desktop'
    }
  },
  data() {
    return {
      headerBarFixed: false,
      //update-begin--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
      topMenuStyle: {
        headerIndexLeft: {},
        topNavHeader: {},
        headerIndexRight: {},
        topSmenuStyle: {}
      },
      chatStatus: '',
      videoVisible: false,
      videoUrl: '', // 动态设置的视频URL
      previewVisible: false,
      pdfUrl: '',
    }
  },
  watch: {
    /** 监听设备变化 */
    device() {
      if (this.mode === 'topmenu') {
        this.buildTopMenuStyle()
      }
    },
    /** 监听导航栏模式变化 */
    mode(newVal) {
      if (newVal === 'topmenu') {
        this.buildTopMenuStyle()
      }
    }
  },
  computed: {
    userInfo() {
      // 从 Vue.ls 中获取 USER_INFO
      return Vue.ls.get(USER_INFO) || {}
    },
    // 新增角色判断计算属性
    hasEnterpriseRole() {
      return this.userInfo.roleNames.some(role =>
        ['施工企业'].includes(role)
      )
    },
    hasBrokerageRole() {
      return this.userInfo.roleNames.some(role =>
        ['经纪公司'].includes(role)
      )
    },
    hasMOHRSSRole() {
      return this.userInfo.roleNames.some(role =>
        ['承办人员', '业务分管', '人社分管', '人社局管理员'].includes(role)
      )
    }
  },
  //update-end--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
    //update-begin--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
    if (this.mode === 'topmenu') {
      this.buildTopMenuStyle()
    }
    //update-end--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
  },
  methods: {
    handleScroll() {
      if (this.autoHideHeader) {
        let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
        if (scrollTop > 100) {
          this.headerBarFixed = true
        } else {
          this.headerBarFixed = false
        }
      } else {
        this.headerBarFixed = false
      }
    },
    toggle() {
      this.$emit('toggle')
    },
    //update-begin--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
    buildTopMenuStyle() {
      if (this.mode === 'topmenu') {
        if (this.device === 'mobile') {
          // 手机端需要清空样式，否则显示会错乱
          this.topMenuStyle.topNavHeader = {}
          this.topMenuStyle.topSmenuStyle = {}
          this.topMenuStyle.headerIndexRight = {}
          this.topMenuStyle.headerIndexLeft = {}
        } else {
          let rightWidth = '360px'
          this.topMenuStyle.topNavHeader = { 'min-width': '165px' }
          this.topMenuStyle.topSmenuStyle = { 'width': 'calc(100% - 165px)' }
          this.topMenuStyle.headerIndexRight = { 'min-width': rightWidth }
          this.topMenuStyle.headerIndexLeft = { 'width': `calc(100% - ${rightWidth})` }
        }
      }
    },
    handleUserManual() {
      // 按钮点击事件处理逻辑
      console.log('用户手册按钮被点击')
      // 可添加跳转路由或其他操作
    },
    //update-begin--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
    showPdfPreview() {
      let pdfUrl = ''
      if (this.hasEnterpriseRole) {
        pdfUrl = 'http://139.199.159.36:37192/file/static/施工企业用户手册.pdf'
        console.log('pdfUrl:', pdfUrl)
      } else if (this.hasBrokerageRole) {
        pdfUrl = 'http://139.199.159.36:37192/file/static/经纪公司用户手册.pdf'
      } else if (this.hasMOHRSSRole) {
        pdfUrl = 'http://139.199.159.36:37192/file/static/人社局用户手册.pdf'
      }
      if (pdfUrl) {
        this.pdfUrl = pdfUrl
        this.previewVisible = true
      }
    },
    downloadManual() {
      const downloadTemplateApi = 'http://139.199.159.36:37192/file/download';

      let filePath, fileName;
      if (this.hasEnterpriseRole) {
        filePath = '/opt/UserManual/施工企业用户手册.pdf';
        fileName = '施工企业用户手册.pdf';
      } else if (this.hasBrokerageRole) {
        filePath = '/opt/UserManual/经纪公司用户手册.pdf';
        fileName = '经纪公司用户手册.pdf';
      } else if (this.hasMOHRSSRole) {
        filePath = '/opt/UserManual/人社局用户手册.pdf';
        fileName = '人社局用户手册.pdf';
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
      let videoUrl = ''
      if (this.hasEnterpriseRole) {
        videoUrl = '/videos/施工企业视频教程.mp4'
      } else if (this.hasBrokerageRole) {
        videoUrl = '/videos/经纪公司视频教程.mp4'
      } else if (this.hasMOHRSSRole) {
        videoUrl = '/videos/人社局视频教程.mp4'
      }

      if (videoUrl) {
        this.videoUrl = videoUrl
        this.videoVisible = true
      }
    }
  }
}
</script>

<style lang="less" scoped>
/* update_begin author:scott date:20190220 for: 缩小首页布局顶部的高度*/

@header-height: 59px;

.ant-layout-header {
  height: @header-height;
  line-height: normal;
}

.header {
  display: flex;
  align-items: center;
  background-color: #1890ff !important;
  color: white !important;
  height: 59px;
  .trigger {
    font-size: 18px;
    margin-right: 16px;
  }

  .header-content {
    flex: 1;
    min-width: 0; // 防止内容溢出
    margin-right: 16px;

    .welcome-line {
      line-height: 20px;
      font-size: 13px;
      white-space: nowrap;
    }

    .button-line {
      display: flex;
      align-items: center;
      gap: 8px;
      line-height: 20px;

      .user-manual-btn {
        padding: 0;
        height: auto;
        color: inherit !important;
        font-size: 13px;

        &:hover {
          opacity: 0.8;
        }

        .anticon {
          margin-right: 4px;
        }
      }
    }
  }

  // 暗色主题适配
  &.dark {
    .header-content {
      .welcome-line {
        color: rgba(255, 255, 255, 0.85);
      }

      .button-line {
        .user-manual-btn {
          color: rgba(255, 255, 255, 0.85) !important;
        }
      }
    }
  }

  &.light {
    height: 59px !important; // 针对light主题强制覆盖
  }
}

// 移动端适配
@media screen and (max-width: 768px) {
  .header {
    .header-content {
      .welcome-line {
        font-size: 12px;
      }

      .button-line {
        flex-wrap: wrap;
        gap: 4px;

        .user-manual-btn {
          font-size: 12px;
        }
      }
    }
  }
}

/* update_end author:scott date:20190220 for: 缩小首页布局顶部的高度*/
</style>