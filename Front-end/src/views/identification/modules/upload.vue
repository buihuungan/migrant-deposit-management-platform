<template>
  <a-modal title="上传" :visible="visible" :footer="null" @cancel="handleCancel" :destroyOnClose="true" :width="700">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="Excel文件上传">
        <div style="display: flex; flex-direction: column">
          <a-upload
            name="file"
            :headers="headers"
            action= "actionUrl1"
            :multiple="false"
            @change="handleChange"
          >
            <a-button type="primary">文件上传</a-button>
          </a-upload>
          <br />
          文件上传帮助
          <br />
          1、在模板管理处导出您想选择的模板进行编辑。
          <br />
          2、点击【上传】按钮，上传EXCEL文件。 文件上传记录
          <br />
          <h3>总共:{{ filenum.dataNum }}条，成功:{{ filenum.successNum }}条，失败:{{ filenum.failureNum }}条</h3>
          <h3 style="color: red">{{ fileerror }}</h3>
          <a-table :columns="columns" :data-source="failurefilelist" size="middle" :scroll="{ x: true }" />

          <!-- 上传失败的标识:
          <h3 v-for="(item,key) in failurefilelist" v-bind:key="key">{{item.identity}} ———— {{item.failureReason}}
          </h3> -->
        </div>
      </a-tab-pane>
      <a-tab-pane key="2" tab="XML文件上传">
        <div style="display: flex; flex-direction: column">
          文件上传帮助
          <br />
          1、1个文件中，只能包含1个Handle；
          <br />
          2、点击【上传】按钮，上传XML文件。
          <a-upload
            name="file"
            action="actionUrl2"
            :multiple="false"
            @change="handleChange"
          >
            <a-button type="primary">文件上传</a-button>
          </a-upload>
        </div>
      </a-tab-pane>
    </a-tabs>
  </a-modal>
</template>

<script>
import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
import api from '@/api/index'
export default {
  data() {
    return {
      columns: [],
      fileerror: '',
      visible: false,
      failurefilelist: [],
      filenum: {
        dataNum: 0,
        successNum: 0,
        failureNum: 0,
      },
      headers: {
        'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
        tenant_id: Vue.ls.get(TENANT_ID),
      },
      actionUrl1: api.server_url + '/identity/docking/excelImport',
      actionUrl2: api.server_url + '/identity/docking/xmlImport'
    }
  },
  methods: {
    openUpload() {
      this.visible = true
    },
    handleCancel() {
      this.visible = false
      this.failurefilelist = []
      this.filenum.dataNum = 0
      this.filenum.successNum = 0
      this.filenum.failureNum = 0
      this.fileerror = ''
      this.$emit('ok')
    },
    handleChange(info) {
      console.log('info', info)
      if (info.file.status !== 'uploading') {
        // console.log(info.file, info.fileList)
      }
      if (info.file.status === 'done') {
        if (info.file.response.success === true && info.file.response.code == 0 && info.file.response.result.failureNum == 0) {
          this.$message.success(`${info.file.name} 上传成功`)
          let fileresult = info.file.response.result
          this.filenum.dataNum = fileresult.dataNum
          this.filenum.successNum = fileresult.successNum
          this.filenum.failureNum = fileresult.failureNum
          this.failurefilelist = fileresult.failDataList
        }
        //但部分标识被更新
        else if (info.file.response.code === 0 && info.file.response.success === true && info.file.response.result.failureNum>0) {
          this.$message.success(`${info.file.name} 上传成功`)
          this.columns = []
          let fileresult = info.file.response.result
          this.filenum.dataNum = fileresult.dataNum
          this.filenum.successNum = fileresult.successNum
          this.filenum.failureNum = fileresult.failureNum
          if (fileresult.failDataList.length > 0) {
            this.failurefilelist = fileresult.failDataList
            let tempcolumns = Object.keys(this.failurefilelist[0])
            for (let i = 0; i < tempcolumns.length; i++) {
              this.columns.push({
                title: tempcolumns[i],
                dataIndex: tempcolumns[i],
              })
            }
          }
        } else if (info.file.response.message === '校验失败') {
          this.$message.info(`${info.file.name} 上传失败校验不通过`)
          this.columns = []
          let fileresult = info.file.response.result
          // this.filenum.dataNum = fileresult.dataNum
          // this.filenum.successNum = fileresult.successNum
          // this.filenum.failureNum = fileresult.failureNum
          this.failurefilelist = fileresult.failDataList
          let tempcolumns = Object.keys(this.failurefilelist[0])
          let tempcounts = Object.values(fileresult.repeatCount)
          console.log('failurefilelist', this.failurefilelist)
          console.log('tempcountstempcountstempcounts', tempcounts)
          for (let i = 0; i < tempcolumns.length; i++) {
            this.columns.push({
              title: tempcolumns[i],
              dataIndex: tempcolumns[i],
            })
          }
          for (let i = 0; i < tempcounts.length; i++) {
            this.failurefilelist[i].failureReason =
              this.failurefilelist[i].failureReason + '(' + tempcounts[i].toString() + '次)'
          }
        } else if (info.file.response.code === 500) {
          this.$message.info(`${info.file.name} 上传失败校验不通过`)
          let tempsearch = info.file.response.message.indexOf(':')
          this.fileerror = info.file.response.message.slice(tempsearch + 1)
        }
      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.name} 上传失败.`)
      }
    },
  },
}
</script>

<style>
</style>