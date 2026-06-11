<template>
  <div id="userLayout" :class="['user-layout-wrapper', device]">
    <div class="container">
      <div class="top">
        <div class="header">
          <!-- <a href="/"> -->
          <!-- <img src="~@/assets/background.png" class="logo" alt="logo" /> -->
          <div class="header-placeholder">
            <span class="title" v-show="!hasToken">保证金智慧管理服务平台</span>
          </div> <!-- 新增的白色区域 -->
          <!-- </a> -->
        </div>
        <!-- <div class="desc">中信科移动</div> -->
      </div>

      <route-view :class="routeClass"></route-view>

      <div class="footer-placeholder">
      </div>
      <div class="footer">
      </div>
    </div>
  </div>
</template>

<script>
import RouteView from '@/components/layouts/RouteView'
import { mixinDevice } from '@/utils/mixin.js'

export default {
  name: 'UserLayout',
  components: { RouteView },
  mixins: [mixinDevice],
  data() {
    return {
      hasToken: false,
    }
  },
  created() {
    this.getToken()
  },
  mounted() {
    document.body.classList.add('userLayout')
  },
  beforeDestroy() {
    document.body.classList.remove('userLayout')
  },
  computed: {
    routeClass() {
      const currentRoute = this.$route.path
      console.log('currentRoute', currentRoute);
      if (currentRoute === '/user/entry-select') {
        return 'middle'
      } else if (currentRoute === '/user/login') {
        return ''
      }
      return ''
    },
  },
  methods: {
    // 解析token，存入localstore
    getToken() {
      const url = window.location.href
      const params = new URLSearchParams(new URL(url).search)
      const token = params.get('token')
      if (token) {
        this.hasToken = true
      }
    },
  },
}
</script>

<style lang="less" scoped>
#userLayout.user-layout-wrapper {
  height: 100%;

  &.mobile {
    .container {
      .main {
        max-width: 368px;
        width: 98%;
      }
    }
  }

  .container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 110px 0 144px;
    width: 100%;
    position: relative;
    width: 100%;
    min-height: 100%;
    background: #f0f2f5 url(~@/assets/background.svg) no-repeat 50%;
    background: url('~@/assets/background.png') no-repeat center center;
    background-size: 100%;
    // background-color: #007bFF;
    padding: 110px 0 144px;
    position: relative;

    a {
      text-decoration: none;
    }

    .top {
      text-align: center;

      .header {
        height: 44px;
        line-height: 44px;

        .badge {
          position: absolute;
          display: inline-block;
          line-height: 1;
          vertical-align: middle;
          margin-left: -12px;
          margin-top: -10px;
          opacity: 0.8;
        }

        .logo {
          height: 44px;
          vertical-align: top;
          margin-right: 16px;
          border-style: none;
        }

        .header-placeholder {
          height: 72px; // 设置高度
          background-color: white; // 白色背景
          display: flex; // 使用 flexbox
          align-items: center; // 垂直居中
          justify-content: center; // 水平居中
          position: absolute; // 绝对定位
          top: 0; // 在顶部
          left: 0;
          right: 0; // 填满宽度
          z-index: 1; // 确保在其他元素上面

          .title {
            font-size: 35px;
            color: #1890FF;
            font-family: 'Chinese Quote', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB',
              'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji',
              'Segoe UI Symbol';
            font-weight: 600;
            z-index: 2; // 确保标题在白色区域上方
          }
        }
      }

      .desc {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.45);
        margin-top: 12px;
        margin-bottom: 40px;
      }
    }

    .main {
      min-width: 260px;
      width: 368px;
      margin: 0 auto;
    }

    .footer {
      position: absolute;
      width: 100%;
      bottom: 0;
      padding: 0 16px;
      margin: 48px 0 24px;
      text-align: center;

      .links {
        margin-bottom: 8px;
        font-size: 14px;

        a {
          color: rgba(0, 0, 0, 0.45);
          transition: all 0.3s;

          &:not(:last-child) {
            margin-right: 40px;
          }
        }
      }

      .copyright {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
      }
    }

    .middle {
      position: absolute;
      width: 80%;
      // bottom: 0;
      padding: 0 16px;
      margin: 48px 0 24px;
      text-align: center;
      left: 50%;
      transform: translateX(-50.5%);
    }

    .footer-placeholder {
      width: 100%;
      background-color: white;
      // padding: 10px 0;
      text-align: center;
      position: absolute;
      bottom: 0;
      z-index: 2;

      .footer-text {
        font-size: 24px;
        color: #1890FF;
        font-weight: 500;
      }
    }


    // .footer {
    //   position: absolute;
    //   bottom: 50px;
    //   width: 100%;
    //   text-align: center;
    //   color: #478FCA;
    //   font-size: 30px;
    //   font-family: 'Arial', sans-serif;
    //   text-shadow:
    //     -1px -1px 0 #fff,
    //     1px -1px 0 #fff,
    //     -1px 1px 0 #fff,
    //     1px 1px 0 #fff;

    //   .footer-text {
    //     margin-top: 10px;
    //     font-weight: 500;
    //   }
    // }
  }
}
</style>