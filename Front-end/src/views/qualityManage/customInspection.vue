<template>
  <div>
    <el-dialog
      title="当前模型是否符合订单要求?"
      :visible.sync="demoShowFlag"
      :destroy-on-close="true"
      centered
      width="80%"
      :show-close="false"
    >
      <div>
        <div class="orderDetail">
          订单信息&nbsp;&nbsp;&nbsp;颜色:
          <div style="display: inline-block; color: green">{{ orderColor }}</div>
          &nbsp;&nbsp;形状:
          <div style="display: inline-block; color: green">{{ orderShape }}</div>
          &nbsp;&nbsp;性能指标:
          <div style="display: inline-block; color: green">{{ orderPowerSize }}</div>
          <el-divider></el-divider>
        </div>
        <div>
          <iframe
            ref="iframe"
            id="bdIframe"
            name="bdIframe"
            :src="bdTokenUrl"
            frameborder="0"
            scrolling="no"
          > </iframe>
        </div>
        <div style="width: 100%; text-align: center; margin-top: 10px">
          <el-button
            type="danger"
            style="margin-right: 20px"
            @click="handelWrong()"
          >不符合</el-button>
          <el-button
            type="success"
            @click="handelCorrect()"
          >符合</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { Message } from 'element-ui'
import { order_getAction, order_postAction } from '@/api/orderApi'
import axios from 'axios'
import { o_postAction, o_putAction } from '@/api/onApi.js'

export default {
  name: 'customInspection',
  components: { GenerateForm, AntdGenerateForm },
  //表单设计器id:formId
  data() {
    return {
      demoShowFlag: false,
      formId: '',
      onlineTableId: '',
      onlineDataId: '',
      editFileName: '查看最新检测结果',
      onlineJson: {},
      noDateFormJson: {},
      formJson: {},
      dataForm: {},
      // bdTokenUrl: './static/3d_model/model.html',
      bdTokenUrl: './static/threejs3D/3DTest/obj.html',
      demo_3d_data: {},
      judgeDemo_3D_data: {},
      demo_3d_all_data: [
        {
          objurl: '../../static/3d_model/sjh/baishuyuan.obj',
          mtlurl: '../../static/3d_model/sjh/baishuyuan.mtl',
          color: 1,
          shape: 0,
          powerSize: 0,
        },
        {
          objurl: '../../static/3d_model/sjh/baishufang.obj',
          mtlurl: '../../static/3d_model/sjh/baishufang.mtl',
          color: 1,
          shape: 1,
          powerSize: 0,
        },
        {
          objurl: '../../static/3d_model/sjh/baimiyuan.obj',
          mtlurl: '../../static/3d_model/sjh/baimiyuan.mtl',
          color: 1,
          shape: 0,
          powerSize: 1,
        },
        {
          objurl: '../../static/3d_model/sjh/baimifang.obj',
          mtlurl: '../../static/3d_model/sjh/baimifang.mtl',
          color: 1,
          shape: 1,
          powerSize: 1,
        },
        {
          objurl: '../../static/3d_model/sjh/heishuyuan.obj',
          mtlurl: '../../static/3d_model/sjh/heishuyuan.mtl',
          color: 0,
          shape: 0,
          powerSize: 0,
        },
        {
          objurl: '../../static/3d_model/sjh/heishufang.obj',
          mtlurl: '../../static/3d_model/sjh/heishufang.mtl',
          color: 0,
          shape: 1,
          powerSize: 0,
        },
        {
          objurl: '../../static/3d_model/sjh/heimiyuan.obj',
          mtlurl: '../../static/3d_model/sjh/heimiyuan.mtl',
          color: 0,
          shape: 0,
          powerSize: 1,
        },
        {
          objurl: '../../static/3d_model/sjh/heimifang.obj',
          mtlurl: '../../static/3d_model/sjh/heimifang.mtl',
          color: 0,
          shape: 1,
          powerSize: 1,
        },
      ],
      randomNumber: 0,
      judgeResult: false,
      globalObjurl: '',
      globalMtlurl: '',
      result: false,
      orderColor: '黑色',
      orderShape: '圆形',
      orderPowerSize: '15db',
      oIframe: '',
      record1:{},
    }
  },
  updated() {
    var that = this
    window.addEventListener(
      'message',
      function (e) {
        if (e.data == 'demoShowFlag') {
          that.demoShowFlag = false
          $('#bdIframe').attr('src', 'about:blank')
        }
        if (e.data == 'updata') {
          //刷新
          var newrow = {}
          that.demoShowFlag = false
          that.$nextTick(() => {
            newrow.objurl = that.globalObjurl
            newrow.mtlurl = that.globalMtlurl
            that.handleClick(newrow)
          })
        }
      },
      false
    )

    this.moveNavigation()
  },
  methods: {
    openModal(record) {
      this.record1=record
      console.log("检测传入record",record);
      this.demoShowFlag = true
      this.$nextTick(() => {
        this.oIframe = document.getElementById('bdIframe')
        console.log('this.oIframe', this.oIframe)
      })

      let that1 = this

      order_getAction(`/order/byProcessId`, { processId: record.product_id }).then((res) => {
        if (res.success) {
          if (res.result.color == 0) {
            this.orderColor = '黑色'
          } else {
            this.orderColor = '白色'
          }
          if (res.result.shape == 0) {
            this.orderShape = '圆形'
          } else {
            this.orderShape = '方形'
          }
          if (res.result.powerSize == 0) {
            this.orderPowerSize = '15db'
          } else {
            this.orderPowerSize = '20db'
          }
          $('#bdIframe').attr('src', this.bdTokenUrl)
          this.demoShowFlag = false
          this.loading3DModel()
        } else {
          Message({
            showClose: true,
            message: '订单数据错误，请检查订单信息！',
            type: 'error',
            customClass: 'zZindex',
          })
          that1.demoShowFlag = false
        }
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
    async handelWrong() {
      //学员点击不符合
      // console.log('学员点击不符合', this.judgeDemo_3D_data)
      await this.judgeRightOrWrong(this.judgeDemo_3D_data)

      if (!this.result) {
        Message({
          message: '判断正确。但模型本身错误，需要选出正确模型',
          type: 'success',
          customClass: 'zZindex',
        })
        this.demo_3d_all_data.splice(this.randomNumber, 1)
      } else {
        Message({ message: '判断错误！请再次判断！', type: 'warning', customClass: 'zZindex' })
      }
      this.$nextTick(() => {
        this.loading3DModel()
      })
      this.demoShowFlag = false

      $('#bdIframe').attr('src', 'about:blank')
    },
    async handelCorrect() {
      //学员点击符合
      // console.log('学员点击符合', this.judgeDemo_3D_data)

      await this.judgeRightOrWrong(this.judgeDemo_3D_data) //同步异步需要处理

      if (this.result) {
        Message({ message: '判断正确', type: 'success', customClass: 'zZindex' })
        let datajson = {
          amount: '',
          order_check: '',
          result: '合格',
        }
        o_postAction('/cgform/api/form/6e02a314b8b54c88bb12ff13fc28114c', datajson)
          .then((res) => {
            this.record1.data_id = res.result
            if (res.success == true) {
              o_putAction('/cgform/api/form/bba30b3a70924ac7aa65c220ee62cb30?tabletype=1', this.record1)
                .then((res) => {
                  this.$message.success('检测成功')
                })
                .catch((err) => {
                  this.$message.error('检测数据提交失败')
                  console.log(err)
                })
            } else {
              this.$message.error('提交失败')
            }
          })
          .catch((err) => {
            console.log(err)
          })
        // this.updateWare()
        // Message.success('判断正确')
      } else {
        Message({ message: '判断错误！请再次判断！', type: 'warning', customClass: 'zZindex' })
        // Message.warning('判断错误！请再次判断！')
        this.demo_3d_all_data.splice(this.randomNumber, 1)
        // console.log('删掉后的this.demo_3d_all_data', this.demo_3d_all_data)

        this.$nextTick(() => {
          this.loading3DModel()
        })
      }
      this.demoShowFlag = false

      $('#bdIframe').attr('src', 'about:blank')
    },

    async judgeRightOrWrong(data) {
      //判断用户的判断是否正确
      console.log('判断用户的判断是否正确data', data)
      axios.defaults.headers.post['Content-Type'] = 'application/json'
      await order_postAction(`/order/judgeModel?processId=${this.record1.product_id}`, JSON.stringify(data)).then((res) => {
        // console.log('接口调取成功')
        // console.log('JSON.stringify(data)', JSON.stringify(data))
        if (res.result) {
          this.result = res.result
        } else {
          this.result = true
        }

        if (typeof this.result != 'boolean') {
          Message({
            message: '数据错误，请创建订单或者刷新后重新检测！',
            type: 'warning',
            customClass: 'zZindex',
          })
          this.visible = false
        }
      })
      // const token = Vue.ls.get(ACCESS_TOKEN)
      // axios.defaults.headers.common['X-Access-Token'] = token
      // axios({
      //   url: `/cictOrder/judgeModel`,
      //   method: 'post',
      //   baseURL: 'http://10.162.34.242:27133',
      //   data: data,
      // }).then((res) => {
      //   console.log('接口调取成功')
      //   result=res.success
      // })
    },
    loading3DModel() {
      this.randomNumber = Math.round(Math.random() * (this.demo_3d_all_data.length - 1))
      if (this.randomNumber < 0) {
        this.randomNumber = 0
      }
      this.demo_3d_data = this.demo_3d_all_data[this.randomNumber]
      this.judgeDemo_3D_data.color = this.demo_3d_data.color
      this.judgeDemo_3D_data.shape = this.demo_3d_data.shape
      this.judgeDemo_3D_data.powerSize = this.demo_3d_data.powerSize
      this.handleClick(this.demo_3d_data)
    },

    handleClick(row) {
      // this.openFullScreen2()

      this.globalObjurl = row.objurl
      this.globalMtlurl = row.mtlurl
      this.demoShowFlag = true
      localStorage.setItem('objurl', row.objurl)
      localStorage.setItem('mtlurl', row.mtlurl)
    },
  },
}
</script>

<style lang="less" scoped>
//scoped 属性是一个布尔属性。如果使用该属性，则样式仅仅应用到 style 元素的父元素及其子元素。
//使用lang="less"，可在CSS界面写JS
.submitBtn {
  margin-top: 20px;
}
.submitBtn button {
  margin: 20px auto;
  left: 45%;
}
.abutton {
  margin-right: 20px;
}
//使用deep进行穿透，可以修改此界面的第三方组件但影响不到其他地方
/deep/ .tips-class > .el-form-item__label {
  font-size: 18px;
  color: red;
}
.zZindex {
  z-index: 9999 !important;
}
.el-popover.area-popover {
  z-index: 999999 !important;
  height: 175px;
  // transform: translateX(60px);
}
#bdIframe{
  width: 100%;
  height: 400px;
}
</style>