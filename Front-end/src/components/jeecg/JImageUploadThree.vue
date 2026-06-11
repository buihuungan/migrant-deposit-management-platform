<template>
  <div class="img">
    <a-upload name="file" listType="picture-card" :multiple="isMultiple" :action="uploadAction" :headers="headers"
      :data="{ biz: bizPath }" :fileList="fileList" :beforeUpload="beforeUpload" :disabled="disabled"
      :isMultiple="isMultiple" :showUploadList="isMultiple" @change="handleChange" @preview="handlePreview"
      :class="!isMultiple ? 'imgupload' : ''" :openFileDialogOnClick="isEdit || isAdd">
      <div :style="{ width: !isMultiple ? '104px' : 'auto', height: !isMultiple ? '104px' : 'auto' }"
        @click="handleClick">
        <img v-if="!isMultiple && picUrl" :src="getAvatarView()" style="width: 100%; height: 100%" />
        <div v-else class="iconp">
          <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
          <div class="ant-upload-text">{{ text }}</div>
        </div>
      </div>
      <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel()">
        <img alt="example" style="width: 100%" :src="previewImage" />
      </a-modal>
    </a-upload>
  </div>
</template>

<script>
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { getFileAccessHttpUrl } from '@/api/manage'
import Viewer from 'viewerjs'

const uidGenerator = () => {
  return '-' + parseInt(Math.random() * 10000 + 1, 10)
}
const getFileName = (path) => {
  if (path.lastIndexOf('\\') >= 0) {
    let reg = new RegExp('\\\\', 'g')
    path = path.replace(reg, '/')
  }
  return path.substring(path.lastIndexOf('/') + 1)
}
export default {
  name: 'JImageUpload',
  data() {
    return {
      // uploadAction:window._CONFIG['domianURL']+"/sys/common/upload",
      uploadAction: window._CONFIG['DocumentURL'] + '/file/upload',
      uploadLoading: false,
      picUrl: false,
      headers: {},
      fileList: [],
      previewImage: '',
      previewVisible: false,
    }
  },
  props: {
    text: {
      type: String,
      required: false,
      default: '上传',
    },
    /*这个属性用于控制文件上传的业务路径*/
    bizPath: {
      type: String,
      required: false,
      default: 'temp',
    },
    value: {
      type: [String, Array],
      required: false,
    },
    disabled: {
      type: Boolean,
      required: false,
      default: false,
    },
    isMultiple: {
      type: Boolean,
      required: false,
      default: false,
    },
    //update-begin-author:wangshuai date:20201021 for:LOWCOD-969 新增number属性，用于判断上传数量
    number: {
      type: Number,
      required: false,
      default: 0,
    },
    //update-end-author:wangshuai date:20201021 for:LOWCOD-969 新增number属性，用于判断上传数量
    isEdit: {
      type: Boolean,
      required: false,
      default: true, // 默认值为 true，表示编辑状态
    },
    isAdd: {
      type: Boolean,
      required: false,
      default: true, // 默认值为 true，表示新增状态
    },
  },
  watch: {
    value: {
      handler(val, oldValue) {
        if (val instanceof Array) {
          this.initFileList(val.join(','))
        } else {
          this.initFileList(val)
        }
        if (!val || val.length == 0) {
          this.picUrl = false
        }
      },
      //立刻执行handler
      immediate: true,
    },
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN)
    this.headers = { 'X-Access-Token': token }
  },
  methods: {
    handleClick(event) {
      // 只有当既不是编辑也不是新增状态时，才显示预览
      if (!this.isEdit && !this.isAdd) {
        event.stopPropagation()
        if (this.fileList.length > 0) {
          // this.previewImage = this.fileList[0].url
          // this.previewVisible = true
          const imageElement = document.createElement('img');
          imageElement.src = this.fileList[0].url;
          const viewer = new Viewer(imageElement, {
            // 可配置相关参数，比如显示导航栏、工具栏等，以下是一些常见配置示例
            inline: false, // 是否启用 inline 模式（这里设置为 false 表示弹出窗口查看）
            button: true, // 是否显示右上角的缩放按钮等操作按钮
            navbar: true, // 是否显示底部导航栏
            title: false, // 是否显示图片标题
            toolbar: true, // 是否显示顶部工具栏
            tooltip: true, // 是否显示提示信息
            movable: true, // 是否可以拖动图片
            zoomable: true, // 是否可以缩放图片
            rotatable: true, // 是否可以旋转图片
            scalable: true, // 是否可以通过鼠标滚轮缩放图片
            transition: true, // 是否启用过渡动画
          });
          viewer.show(); // 显示图片查看器
        }
      }
    },
    initFileList(paths) {
      if (!paths || paths.length == 0) {
        this.fileList = []
        return
      }
      this.picUrl = true
      let fileList = []
      let arr = paths.split(',')
      console.log('arr', arr)
      for (var a = 0; a < arr.length; a++) {
        let url = getFileAccessHttpUrl(arr[a])
        fileList.push({
          uid: uidGenerator(),
          name: getFileName(arr[a]),
          status: 'done',
          url: url,
          response: {
            status: 'history',
            message: arr[a],
          },
        })
      }
      this.fileList = fileList
    },
    beforeUpload: function (file) {
      var fileType = file.type
      if (fileType.indexOf('image') < 0) {
        this.$message.warning('请上传图片')
        return false
      }
    },
    handleChange(info) {
      console.log('info', info)
      this.picUrl = false
      let fileList = info.fileList
      //update-begin-author:wangshuai date:20201022 for:LOWCOD-969 判断number是否大于0和是否多选，返回选定的元素。
      if (this.number > 0 && this.isMultiple) {
        fileList = fileList.slice(-this.number)
      }
      //update-end-author:wangshuai date:20201022 for:LOWCOD-969 判断number是否大于0和是否多选，返回选定的元素。
      if (info.file.status === 'done') {
        if (info.file.response && info.file.response.success) {
          this.picUrl = true
          fileList = fileList.map((file) => {
            if (file.response && file.response.result && file.response.result.uplodadFile) {
              file.url = file.response.result.uplodadFile
            }
            return file
          })
        }
        //this.$message.success(`${info.file.name} 上传成功!`);
      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.name} 上传失败.`)
      } else if (info.file.status === 'removed') {
        this.handleDelete(info.file)
      }
      this.fileList = fileList
      if (info.file.status === 'done' || info.file.status === 'removed') {
        this.handlePathChange()
      }
    },
    // 预览
    handlePreview(file) {
      // 如果是编辑状态，直接执行上传功能
      if (!this.isEdit && !this.isAdd) {
        // this.previewImage = file.url || file.thumbUrl
        // this.previewVisible = true
        const viewer = new Viewer(imageElement, {
          // 配置参数同上述handleClick方法中的示例，可根据实际需求调整
          inline: false,
          button: true,
          navbar: true,
          title: false,
          toolbar: true,
          tooltip: true,
          movable: true,
          zoomable: true,
          rotatable: true,
          scalable: true,
          transition: true,
        });
        viewer.show();
      }
    },
    getAvatarView() {
      if (this.fileList.length > 0) {
        let url = this.fileList[0].url
        return getFileAccessHttpUrl(url)
      }
    },
    handlePathChange() {
      let uploadFiles = this.fileList
      let path = ''
      if (!uploadFiles || uploadFiles.length == 0) {
        path = ''
      }
      let arr = []
      if (!this.isMultiple) {
        arr.push(uploadFiles[uploadFiles.length - 1].response.result.uplodadFile)
      } else {
        for (let a = 0; a < uploadFiles.length; a++) {
          // update-begin-author:taoyan date:20200819 for:【开源问题z】上传图片组件 LOWCOD-783
          if (uploadFiles[a].status === 'done') {
            arr.push(uploadFiles[a].response.result.uplodadFile)
          } else {
            return
          }
          // update-end-author:taoyan date:20200819 for:【开源问题z】上传图片组件 LOWCOD-783
        }
      }
      if (arr.length > 0) {
        path = arr.join(',')
      }
      this.$emit('change', path)
    },
    handleDelete(file) {
      //如有需要新增 删除逻辑
      console.log(file)
    },
    handleCancel() {
      this.close()
      this.previewVisible = false
    },
    close() { },
  },
  model: {
    prop: 'value',
    event: 'change',
  },
}
</script>

<style scoped>
/* update--begin--autor:lvdandan-----date:20201016------for：j-image-upload图片组件单张图片详情回显空白
    * https://github.com/zhangdaiscott/jeecg-boot/issues/1810
    * https://github.com/zhangdaiscott/jeecg-boot/issues/1779
    */
::v-deep .imgupload .ant-upload-select {
  display: block;
}

::v-deep .imgupload .ant-upload.ant-upload-select-picture-card {
  width: 120px;
  height: 120px;
}

::v-deep .imgupload .iconp {
  padding: 32px;
}

/* update--end--autor:lvdandan-----date:20201016------for：j-image-upload图片组件单张图片详情回显空白*/
</style>