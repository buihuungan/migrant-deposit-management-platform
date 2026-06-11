<template>
  <div id="formContent" class="formContent">
    <div v-if="dataShowFlag">
      <el-table :data="tableData" border style="width: 100%" :header-cell-style="{ 'text-align': 'center' }">
        <el-table-column fixed prop="date" label="日期" min-width="20%" align="center"> </el-table-column>
        <el-table-column prop="name" label="名称" min-width="20%" align="center"> </el-table-column>
        <el-table-column prop="province" label="类型" min-width="20%" align="center"> </el-table-column>
        <el-table-column prop="zip" label="模型名称" min-width="20%" align="center"> </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="20%" align="center">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-if="demoShowFlag">
      <iframe ref="iframe" id="bdIframe" :src="bdTokenUrl" frameborder="0" scrolling="no"> </iframe>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      bdTokenUrl: './static/3d_model/model.html',
      dataShowFlag: true,
      demoShowFlag: false,
      globalObjurl: '',
      globalMtlurl: '',

      tableData: [
        // {
        //   date: '2016-05-02',
        //   name: '天线定制',
        //   province: '3D模型',
        //   zip: '高频天线',
        //   objurl: '../../static/3d_model/gaoping/gpobj.obj',
        //   mtlurl: '../../static/3d_model/gaoping/gpobj.mtl',
        // },
        // {
        //   date: '2016-05-04',
        //   name: '熔炉教学',
        //   province: '3D模型',
        //   zip: '凡谷示波器',
        //   objurl: '../../static/3d_model/gaoping/gpobj2.obj',
        //   mtlurl: '../../static/3d_model/gaoping/gpobj2.mtl',
        // },
        {
          date: '2016-05-01',
          name: '射频天线',
          province: '白疏圆',
          zip: '双极化射频天线',
          objurl: '../../static/3d_model/sjh/baishuyuan.obj',
          mtlurl: '../../static/3d_model/sjh/baishuyuan.mtl',
        },
        
        {
          date: '2016-05-03',
          name: '减速机教学',
          province: '白密圆',
          zip: '减速机',
          objurl: '../../static/3d_model/sjh/baimiyuan.obj',
          mtlurl: '../../static/3d_model/sjh/baimiyuan.mtl',
        },
        {
          date: '2016-05-05',
          name: '减速机教学',
          province: '黑疏圆',
          zip: '减速机',
          objurl: '../../static/3d_model/sjh/heishuyuan.obj',
          mtlurl: '../../static/3d_model/sjh/heishuyuan.mtl',
        },
        {
          date: '2016-05-07',
          name: '减速机教学',
          province: '黑密圆',
          zip: '减速机',
          objurl: '../../static/3d_model/sjh/heimiyuan.obj',
          mtlurl: '../../static/3d_model/sjh/heimiyuan.mtl',
        },      
        {
          date: '2016-05-04',
          name: '减速机教学',
          province: '白密方',
          zip: '减速机',
          objurl: '../../static/3d_model/sjh/baimifang.obj',
          mtlurl: '../../static/3d_model/sjh/baimifang.mtl',
        },
        {
          date: '2016-05-02',
          name: '减速机教学',
          province: '白疏方',
          zip: '减速机',
          objurl: '../../static/3d_model/sjh/baishufang.obj',
          mtlurl: '../../static/3d_model/sjh/baishufang.mtl',
        }, 
        {
          date: '2016-05-08',
          name: '减速机教学',
          province: '黑密方',
          zip: '减速机',
          objurl: '../../static/3d_model/sjh/heimifang.obj',
          mtlurl: '../../static/3d_model/sjh/heimifang.mtl',
        },
        {
          date: '2016-05-06',
          name: '减速机教学',
          province: '黑疏方',
          zip: '减速机',
          objurl: '../../static/3d_model/sjh/heishufang.obj',
          mtlurl: '../../static/3d_model/sjh/heishufang.mtl',
        },
      ],   
    }
  },
  mounted() {

    /**
     * iframe-宽高自适应显示
     */
    var that = this
    window.addEventListener('message', function (e) {
      if (e.data == 'demoShowFlag') {
        that.$nextTick(() => {
          that.demoShowFlag = false
          that.dataShowFlag = true
        })
      }
      if (e.data == 'updata') {
        var newrow = {}
        that.demoShowFlag = false
        that.$nextTick(() => {
          newrow.objurl = that.globalObjurl
          newrow.mtlurl = that.globalMtlurl
          that.handleClick(newrow)
        })
      }
    })
  },
  methods: {
    handleClick(row) {
      this.openFullScreen2()
      this.globalObjurl = row.objurl
      this.globalMtlurl = row.mtlurl

      this.dataShowFlag = false
      this.demoShowFlag = true

      this.$nextTick(() => {
        //页面宽高自适应
        const oIframe = document.getElementById('bdIframe')
        const deviceWidth = document.documentElement.clientWidth
        const deviceHeight = document.documentElement.clientHeight
        oIframe.style.width = Number(deviceWidth) - 220 + 'px' //数字是页面布局宽度差值
        oIframe.style.height = Number(deviceHeight) - 120 + 'px' //数字是页面布局高度差

        oIframe.contentWindow.objurl = row.objurl //将obj和mtl地址路径传到iframe里面
        oIframe.contentWindow.mtlurl = row.mtlurl
      })
    },
    openFullScreen2() {
      const loading = this.$loading({
        lock: true,
        text: '模型正在渲染，可以点击模型时即渲染完毕。请稍等片刻。',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })
      setTimeout(() => {
        loading.close()
      }, 6000)
    },
  },
}
</script>

<style>

</style>