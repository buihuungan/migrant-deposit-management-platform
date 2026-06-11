<template>
  <div class="simulation_factory">
    <el-container>
      <el-header>
        <el-button type="primary" @click="startTraining()" v-if="nowStatus == 'on'">已开启</el-button>
        <el-button type="primary" @click="startTraining()" v-if="nowStatus == 'off'">已关闭</el-button>
        <el-button type="primary" @click="viewStatus()">查看实训产线状态</el-button>
      </el-header>
      <el-main>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="售后看板" name="first" v-if="proLineStatus">
            <div>
              <iframe
                id="content"
                src="https://fh.trumbai.cn/page/hBI.html"
                style="width: 100%; height: 700px"
              ></iframe>
            </div>
          </el-tab-pane>
          <el-tab-pane label="监控看板" name="second" v-if="proLineStatus">
            <div>
              <iframe
                id="content"
                src="https://fh.trumbai.cn/page/BIvideo.html"
                scrolling="no"
                style="width: 100%; height: 1000px"
                v-if="activeName == 'second' || isFirstInto"
              ></iframe>
            </div>

            <!-- <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests"> -->
          </el-tab-pane>
          <el-tab-pane label="讲解看板" name="third" v-if="proLineStatus">
            <div>
              <iframe
                id="content"
                src="https://fh.trumbai.cn/page/BIPicture.html"
                style="width: 100%; height: 800px"
              ></iframe>
            </div>

            <!-- <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests"> -->
          </el-tab-pane>
          <el-tab-pane label="生产看板" name="fourth" v-if="proLineStatus">
            <div>
              <iframe
                id="content"
                src="https://fh.trumbai.cn/Page/BISCSJ.html"
                style="width: 100%; height: 800px"
              ></iframe>
            </div>

            <!-- <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests"> -->
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import axios from 'axios'
import { e_getAction } from '@/api/eduApi'
import { erp_postProAction, erp_getProAction } from '@/api/erpApi'
import { e_postProAction, e_getProAction } from '@/api/eduApi'
import { Message } from 'element-ui'
export default {
  data() {
    return {
      proLineStatus: true,
      isFirstInto: false,
      nowTabs:'',
      activeName: 'first',
      flag: 'off', //初始状态未开启
      nowStatus: 'off', //初始状态未开启
      datas: {
        Command: '',
      },
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
      // if(this.flag == 'off'){
      //   this.$message.error('实训产线系统未开启！')
      // }
      if(this.activeName == 'first') {
        // window.open("http://101.36.181.105:1005/page/hBI.html")
        this.isFirstInto = true
        console.log('111', this.isFirstInto);
        console.log('222', this.activeName);
      }
      else if(this.activeName == 'second') {
        // window.open("http://101.36.181.105:1005/page/BIvideo.html")
        this.isFirstInto = true
        console.log('111', this.isFirstInto);
        console.log('222', this.activeName);
      }
      else if(this.activeName == 'third') {
        // window.open("http://101.36.181.105:1005/page/BIPicture.html")
        this.isFirstInto = true
        console.log('111', this.isFirstInto);
        console.log('222', this.activeName);
      }
      else if(this.activeName == 'fourth') {
        // window.open("http://101.36.181.105:1005/Page/BISCSJ.html")
        this.isFirstInto = true
        console.log('111', this.isFirstInto);
        console.log('222', this.activeName);
      }
    },
    viewStatus() {
      // e_getProAction(`/receiveProcessFlag/getAbuttingJointData`).then((res) => {
      //   console.log('res',res)
      //   if(res.result.message == 'on'){
      //     this.$message.success('实训产线系统已启动')
      //   }else{
      //     this.$message.error('实训产线未启动')
      //   }
      // })
      axios({
        url: 'https://fh.trumbai.cn/api/dataapi/GetDeviceStatus.html',
        method: 'get',
      }).then((res) => {
        console.log('当前实训产线状态', res.data)
        if (res.data.Message == 'on') {
          this.nowStatus = 'on'
          Message.success({
            message: '当前实训产线状态为开启',
            center: true,
          })
          console.log('当前flag' ,this.flag)
        } else if (res.data.Message == 'off') {
          this.nowStatus = 'off'
          Message.error({
            message: '当前实训产线状态为关闭',
            center: true,
          })
          console.log('当前flag' ,this.flag)
        }
      })
    },
    startTraining() {
      console.log('111111111', this.flag)
      // this.proLineStatus=true
      // $.ajax({
      //     url: "https://fh.trumbai.cn/api/dataapi/SetCommand.html",
      //     type: "POST",
      //     data: JSON.stringify(data),
      //     dataType: "json",
      //     success: function (data) {
      //     },
      //     error: function (err) {
      //     }
      // });
      let that = this
      if (this.flag == 'off') {
        this.flag = 'on'
        // this.data.datas = this.flag
        this.datas.Command = this.flag
        console.log('当前flag1' ,this.flag)
        $.ajax({
          url: 'https://fh.trumbai.cn/api/dataapi/SetCommand.html',
          type: 'POST',
          data: JSON.stringify(this.datas),
          dataType: 'json',
          success: function (data) {
            console.log('开启接口', data.Message)
            if (data.Message == '产线未准备就绪，无法启动！') {
              that.nowStatus = 'off'
              that.flag = 'off'
              Message.warning({
                message: '产线未准备就绪，无法启动！',
                center: true,
              })
            }else if (data.Message == '指令[on]已成功下达！'){
              that.nowStatus = 'on'
              Message.success({
                message: '开启实训产线成功',
                center: true,
              })
            }
          },
          error: function (err) {
            this.flag = 'off'
            console.log('开启失败', err)
          },
        })
      } else if (this.flag == 'on') {
        this.flag = 'off'
        this.datas.Command = this.flag
        $.ajax({
          url: 'https://fh.trumbai.cn/api/dataapi/SetCommand.html',
          type: 'POST',
          data: JSON.stringify(this.datas),
          dataType: 'json',
          success: function (data) {
            if (data.Message == '指令[off]已成功下达！') {
              that.nowStatus = 'off'
              Message.warning({
                message: '关闭实训产线成功。',
                center: true,
              })
            }else if (data.Message == '产线已经停止运行，无需再停止。'){
              that.nowStatus = 'off'
              Message.success({
                message: '产线已经停止运行，无需再停止。',
                center: true,
              })
            }
            console.log('关闭实训产线', data)
          },
          error: function (err) {
            console.log('关闭失败', err)
            that.flag = 'on'
          },
        })
      }
    },
  },
  mounted() {},
}
</script>

<style scoped>
.simulation_factory {
  background: #fff;
  padding: 24px;
}
</style>