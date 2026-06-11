<script src="//unpkg.com/vue/dist/vue.js"></script>
<script src="//unpkg.com/element-ui@2.13.2/lib/index.js"></script>
<template>
  <div class="systemConf">
    <div class="systemcol-ld">
      <div class="article-table">
        <div class="systemtab-body">
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title1">网站地址：</label>
            </div>
            <div class="systeminput">
              <el-input
                id="title1"
                placeholder="请输入内容"
                class="form-control"
                v-model="form.website"
                clearable
              ></el-input>
            </div>
          </div>
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title2">站点标题：</label>
            </div>
            <div class="systeminput">
              <el-input
                id="title2"
                placeholder="请输入内容"
                class="form-control"
                v-model="form.title"
                clearable
              ></el-input>
            </div>
          </div>
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title3">备案号：</label>
            </div>
            <div class="systeminput">
              <el-input
                id="title3"
                placeholder="请输入内容"
                class="form-control"
                v-model="form.icp"
                clearable
              ></el-input>
            </div>
          </div>
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title4">默认上传目录：</label>
            </div>
            <div class="systeminput">
              <el-input
                id="title4"
                placeholder="请输入内容"
                class="form-control"
                v-model="form.uploaddir"
                clearable
              ></el-input>
            </div>
          </div>
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title5">站点版权：</label>
            </div>
            <div class="systeminput">
              <el-input
                id="title5"
                placeholder="请输入内容"
                class="form-control"
                v-model="form.copyright"
                clearable
              ></el-input>
            </div>
          </div>
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title6">站点关键字：</label>
            </div>
            <div class="systeminput">
              <el-input
                id="title6"
                placeholder="请输入内容"
                class="form-control"
                v-model="form.keywords"
                clearable
              ></el-input>
            </div>
          </div>
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title7">畅言AppID：</label>
              <span style="font-size:12px">如还没有畅言，请点击
                <a
                  href="http://www.baidu.com"
                  style="color:bule"
                >畅言官网</a>
                进行注册。</span>
            </div>
            <div class="systeminput">
              <el-input
                id="title7"
                placeholder="请输入内容"
                class="form-control"
                v-model="form.appid"
                clearable
              ></el-input>
            </div>
          </div>
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title8">畅言AppKey：</label>
            </div>
            <div class="systeminput">
              <el-input
                id="title8"
                placeholder="请输入内容"
                class="form-control"
                v-model="form.appkey"
                clearable
              ></el-input>
            </div>
          </div>
          <div class="form-group form-control-medium">
            <div class="name1">
              <label for="title9">站点描述：</label>
            </div>
            <div class="systeminput">
              <el-input
                id="title9"
                placeholder="请输入内容"
                class="form-control"
                type="textarea"
                v-model="form.describe"
                clearable
              ></el-input>
            </div>
          </div>
          <div>
            <el-button
              @click="saveSettings(form)"
              type="primary"
            >保存设置</el-button>
            <el-button
              @click="canaelSettings"
              type="primary"
            >还原设置</el-button>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { c_getAction, c_postAction } from '@/api/cmsApi'
import moment from 'moment'
import { Message } from 'element-ui'

export default {
  name: 'vSystemConf',
  components: {
    moment,
  },
  data: function () {
    return {
      form: {
        id: '',
        website: '',
        title: '',
        icp: '',
        uploaddir: '',
        copyright: '',
        keywords: '',
        appid: '',
        appkey: '',
        describe: '',
      },
    }
  },
  created() {
    this.loaddata()
  },
  methods: {
    async loaddata(arg) {
      var _this = this
      await c_getAction('/admin/system/list')
        .then((res) => {
          console.log(res)
          console.log('success')
          this.$set(this.form, 'id', res.data.id)
          this.$set(this.form, 'website', res.data.website)
          this.$set(this.form, 'title', res.data.title)
          this.$set(this.form, 'icp', res.data.icp)
          this.$set(this.form, 'uploaddir', res.data.uploaddir)
          this.$set(this.form, 'copyright', res.data.copyright)
          this.$set(this.form, 'keywords', res.data.keywords)
          this.$set(this.form, 'appid', res.data.appid)
          this.$set(this.form, 'appkey', res.data.appkey)
          this.$set(this.form, 'describe', res.data.describe)
        })
        .catch((err) => {
          console.log('error')
        })
    },
    saveSettings(info) {
      console.log(info)
      c_postAction('/admin/system/update', info).then((res) => {
      console.log(res)
      if(res.data){
        Message({
          showClose: true,
          message: '保存设置成功',
          type: 'success',
        })
      }else{
        Message({
          showClose: true,
          message: '保存设置失败',
          type: 'error',
        })
      }
      })
    },
    canaelSettings(){
      this.loaddata()
    },
  },
}
</script>

<style>
/* @import url('//unpkg.com/element-ui@2.13.2/lib/theme-chalk/index.css'); */

.systemcol-ld {
  padding: 24px;
  background-color: white;
  position: relative;
  min-height: 1px;
}

.systemtab-body {
  padding-top: 15px;
  padding-right: 15px;
  padding-bottom: 15px;
  padding-left: 15px;
  background-color: white;
  text-align: left;
}

.form-group {
  font-weight: bold;
}

.form-control-medium {
  margin-bottom: 15px;
}

.systeminput {
  padding-top: 5px;
}
</style>