<template>
  <div>
    <el-dialog
      title="3D模型详情"
      :visible.sync = "demoShowFlag"
      :destroy-on-close="true"
      centered
      width="80%"
      :show-close="false"
    >
    <div>
      <div>
        <iframe 
        ref="iframe"
        id="bdIframe"
        name="bdIframe"
        :src="bdTokenUrl"
        frameborder="0"
        scrolling="no"
        ></iframe>
      </div>
    </div>
    </el-dialog>
  </div>
</template>
<script>

export default{
  name: 'Model3d',
  components: {},
  data(){
    return {
      demoShowFlag: false,
      oIframe: '',
      bdTokenUrl: './static/threejs3D/3DTest/obj1.html',
    }
  },
  updated(){
    window.addEventListener(
      "message",
      function(e){
        console.log("eeeee",e);
      },
      false
    )
    this.moveNavigation()
  },
  methods:{
    openModel(record){
      this.demoShowFlag = true;
      this.$nextTick(() => {
        this.oIframe = document.getElementById('bdIframe')
        console.log('this.oIframe', this.oIframe)
        $('#bdIframe').attr('src', this.bdTokenUrl);
      })
      sessionStorage.setItem('model',"heishuyuan");
    },
    moveNavigation() {
      $('.navigation').mousedown(function (e) {
        var isFlag = true

        var me = $(this)

        var top = me.offset().top
        var left = me.offset().left

        var x = e.clientX
        var y = e.clientY
        $(document)
          .mousemove(function (e) {
            if (isFlag) {
              var xx = e.clientX
              var yy = e.clientY
              var xxx = window.pageXOffset
              var yyy = window.pageYOffset
              var t = yy + top - y - yyy
              var l = xx + left - x - xxx
              me.css({ left: l, top: t })
            }
          })
          .mouseup(function () {
            isFlag = false
          })
      })
    },
    close() {
      var _this = this
      this.$elementConfirm('是否关闭处理界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          _this.demoShowFlag = false
        })
        .catch(() => {})
    },
    
  },
}
</script>
<style lang="less" scoped>
#bdIframe{
  width: 100%;
  height: 400px;
}
</style>