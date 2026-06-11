<template>
  <div class="fm-upload-file">
    <el-upload
      class="upload-demo"
      action="http://121.199.72.67:80803/online/common/upload"
      name="multipartFile"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :on-success="handleSuccess"
      multiple
      :limit="3"
      :on-exceed="handleExceed"
    >
      <el-button size="small" type="primary">点击上传</el-button>
    </el-upload>
    <template v-if="value.length > 0">
      <template v-for="item in JSON.parse(value)">
        <el-tooltip effect="dark" :content="item.fileName" placement="top-start">
          <el-link
            type="info"
            class="file-download"
            :href="`http://121.199.72.67:80803/online/common/download${item.dbpath}`">
            {{item.fileName}}</el-link>
        </el-tooltip>
      </template>
    </template>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import api from '../../../../api/index'
require('viewerjs/dist/viewer.css')
export default {
  components: {
    Draggable
  },
  props: {
    value: {
      // type: Array,
      default: () => []
    },
    token: {
      type: String,
      default: ''
    },
    domain: {
      type: String,
      default: ''
    },
    multiple: {
      type: Boolean,
      default: false
    },
    limit: {
      type: Number,
      default: 9
    },
    isQiniu: {
      type: Boolean,
      default: false
    },
    min: {
      type: Number,
      default: 0
    },
    action: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    tip: {
      type: String,
      default: '123456'
    },
    headers: {
      type: Array,
      default: () => []
    },
    ui: {
      type: String,
      default: 'element'
    }
  },
  data() {
    return {
      serverUrl: api.server_url
    }
  },
  created() {
    console.log("this.value", this.value)
  },
  computed: {
    miniWidth() {
      if (this.width > this.height) {
        return this.height
      } else {
        return this.width
      }
    }
  },
  methods: {
    handleSuccess (response, file, fileList) {
      let list = []
      if (this.value.length > 0)
        list = JSON.parse(this.value)
      list.push(response.result)
      console.log("list", list)
      this.$emit('change', JSON.stringify(list))
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`
      )
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}?`)
    }
  }
}
</script>

<style lang="scss">
.fm-upload-file {
  .upload_input {
    display: none !important;
  }

  .file-download{
    width: 100%;
    .el-link--inner {
      display: inline-block !important;
      overflow: hidden !important;
      white-space: nowrap !important;
      text-overflow: ellipsis !important;
    }
  }

  .upload_tip {
    font-size: 12px;
    color: #606266;
    margin-top: 7px;
  }

  .upload-list {
    margin: 0;
    padding: 0;
    list-style: none;

    .list_item {
      font-size: 14px;
      color: #606266;
      line-height: 1.8;
      margin-top: 5px;
      position: relative;
      box-sizing: border-box;
      border-radius: 4px;
      width: 100%;

      &.is-disabled {
        // &::before{
        //   position: absolute;
        //   top:0;
        //   left: 0;
        //   bottom: 0;
        //   right: 0;
        //   z-index: 99;
        //   display: block;
        //   content: '';
        //   cursor: not-allowed;
        // }
        .icon-close {
          cursor: not-allowed;
        }
      }

      .el-progress,
      .ant-progress {
        position: absolute;
        top: 20px;
        width: 100%;
      }

      .el-progress__text,
      .ant-progress-text {
        position: absolute;
        right: 0;
        top: -13px;
      }

      .el-progress-bar,
      .ant-progress-outer {
        margin-right: 0;
        padding-right: 0;
      }

      &.is-success {
        .list_item-status-label {
          .icon-upload-success {
            display: inline-block;
            line-height: inherit;
          }
        }
      }

      .icon-close {
        display: none;
        position: absolute;
        top: 5px;
        right: 5px;
        cursor: pointer;
        color: #606266;
        font-weight: 400;
      }

      &:hover {
        background-color: #f5f7fa;

        .icon-close {
          display: inline-block;
        }

        .list_item-status-label {
          display: none;
        }

        .el-progress__text {
          display: none;
        }
      }

      .list_item-name {
        text-decoration: none;
        color: #606266;
        display: block;
        margin-right: 40px;
        overflow: hidden;
        padding-left: 4px;
        text-overflow: ellipsis;
        transition: color 0.3s;
        white-space: nowrap;

        &:hover {
          color: #409eff;
          cursor: pointer;
        }

        i {
          margin-right: 7px;
          color: #909399;
          height: 100%;
          line-height: inherit;
        }
      }

      .list_item-status-label {
        display: block;
        position: absolute;
        right: 5px;
        top: 0;
        line-height: inherit;

        .icon-upload-success {
          color: #67c23a;
          display: none;
        }
      }
    }
  }
}
</style>
