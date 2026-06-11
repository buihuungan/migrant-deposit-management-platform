<template>
  <div>
    <div v-if="dataShowFlag">
      <a-button @click="display">开始学习</a-button>
    </div>
    <div v-if="demoShowFlag">
      <iframe
        ref="iframe"
        id="bdIframe"
        :src="bdTokenUrl"
        frameborder="0"
        scrolling="no"
      > </iframe>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      bdTokenUrl: './static/threejs3D/3DTest/obj.html',
      dataShowFlag: true,
      demoShowFlag: false,
    }
  },
  methods:{
    display(){
      this.dataShowFlag=false
      this.demoShowFlag=true
      this.$nextTick(() => {
        //页面宽高自适应
        const oIframe = document.getElementById('bdIframe')
        const deviceWidth = document.documentElement.clientWidth
        const deviceHeight = document.documentElement.clientHeight
        oIframe.style.width = Number(deviceWidth) - 220 + 'px' //数字是页面布局宽度差值
        oIframe.style.height = Number(deviceHeight) - 120 + 'px' //数字是页面布局高度差

      })
    }
  }
}
</script>

<style>
</style>