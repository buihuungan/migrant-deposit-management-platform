<template>
  <div class="logo">
    <router-link :to="{ name: 'dashboard' }">
      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->
      <!-- <img v-if="navTheme === 'dark'" src="~@/assets/cict5.png" alt="logo" />
      <img v-else src="~@/assets/scene4.png" alt="logo" /> -->
      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->
      <img src="~@/assets/logo2.png" alt="logo" />
    </router-link>
  </div>
</template>

<script>
import { mixin } from '@/utils/mixin.js'

export default {
  name: 'Logo',
  mixins: [mixin],
  props: {
    title: {
      type: String,
      default: 'Jeecg-Boot Pro',
      required: false,
    },
    showTitle: {
      type: Boolean,
      default: true,
      required: false,
    },
  },
}
</script>
<style lang="less" scoped>
/*缩小首页布 局顶部的高度*/
@height: 59px;

.sider {
  box-shadow: none !important;

  .logo {
    height: @height !important;
    line-height: @height !important;
    box-shadow: none !important;
    transition: background 300ms;
    display: flex;
    /* 添加 flex 布局 */
    justify-content: center;
    /* 水平居中 */
    align-items: center;

    /* 垂直居中 */
    a {
      color: white;

      &:hover {
        color: rgba(255, 255, 255, 0.8);
      }
    }

    img {
      height: 50px;
      margin-left: -20px;
      margin-top: -3px;
    }
  }

  &.light .logo {
    background-color: @primary-color;
  }
}
</style>