<script src="//unpkg.com/vue/dist/vue.js"></script>
<script src="//unpkg.com/element-ui@2.13.2/lib/index.js"></script>
<template>
  <a-drawer title="新增" :visible="visible" width="800" @close="handleCancel">
    <div class="addArticle">
      <div class="col-md">
        <!-- <h1 class="title">发布文章</h1> -->
      </div>
      <div class="col-lg">
        <section class="panel">
          <div class="panel-body">
            <el-form ref="form" :model="form">
              <div class="form-group form-control-medium">
                <label for="title">文章标题：</label>
                <el-input
                  v-model="form.title"
                  id="title"
                  name="title"
                  type="text"
                  class="form-control"
                ></el-input>
              </div>
              <div class="form-group form-control-small">
                <label for="weight">权重：</label>
                <el-input
                  v-model="form.weight"
                  id="weight"
                  name="weight"
                  type="text"
                  class="form-control"
                ></el-input>
              </div>
              <div class="form-group form-control-small">
                <label for="clicks">点击：</label>
                <el-input
                  v-model="form.clicks"
                  id="clicks"
                  name="clicks"
                  type="text"
                  class="form-control"
                ></el-input>
              </div>
              <div class="form-group form-control-medium">
                <label for="properties">自定义属性：</label>
                <el-checkbox-group v-model="properties">
                  <el-checkbox label="h">头条[h]</el-checkbox>
                  <el-checkbox label="b">加粗[b]</el-checkbox>
                  <el-checkbox label="f">幻灯[f]</el-checkbox>
                  <el-checkbox label="p">图片[p]</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="form-group form-control-medium">
                <label for="imagePath">缩略图：</label>
                <el-upload action="#" list-type="picture-card" :auto-upload="false">
                  <i slot="default" class="el-icon-plus"></i>
                  <div slot="file" slot-scope="{ file }">
                    <img class="el-upload-list__item-thumbnail" :src="file.url" alt />
                  </div>
                </el-upload>
                <el-dialog :visible.sync="dialogVisible">
                  <img width="100%" :src="form.imagePath" alt />
                </el-dialog>
              </div>
              <div class="form-group form-control-medium">
                <label for="categoryId">所属栏目：</label>
                <el-input
                  v-model="categoryId"
                  :disabled="true"
                  class="form-control"
                  readonly="readonly"
                ></el-input>
              </div>
              <div class="form-group form-control-medium">
                <label for="tag">标签：</label>
              </div>
              <div class="form-group">
                <label for="abstract">内容摘要：</label>
                <el-input type="textarea" v-model="form.description"></el-input>
              </div>
              <div class="form-group">
                <label for="文章内容">文章内容：</label>
                <!-- <vue-ueditor-wrap 
              class="editor"
              v-model="form.mdContent"            
                ></vue-ueditor-wrap>-->
              </div>
              <div class="form-group">
                <div class="col-lg-3">
                  <label for="comment">允许评论：</label>
                  <el-radio-group v-model="form.comment">
                    <el-radio :label="1">是</el-radio>
                    <el-radio :label="0">否</el-radio>
                  </el-radio-group>
                </div>
                <div class="col-lg-3">
                  <label for="subscribe">允许订阅：</label>
                  <el-radio-group v-model="form.subscribe">
                    <el-radio :label="1">是</el-radio>
                    <el-radio :label="0">否</el-radio>
                  </el-radio-group>
                </div>
              </div>
              <!-- <div class="form-btn-group-left">
                <el-button class="btn btn-info" @click="baocun">保存</el-button>
              </div>-->
            </el-form>
            <div class="drawer-bootom-button" v-show="!disableSubmit" style="text-align: right;">
              <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
                <a-button style="margin-right: .8rem">取消</a-button>
              </a-popconfirm>
              <a-button @click="baocun" type="primary">提交</a-button>
            </div>
          </div>
        </section>
      </div>
    </div>
  </a-drawer>
</template>
<script>
import { c_getAction, c_postAction, c_httpAction, c_putAction, c_deleteAction } from '@/api/cmsApi'
// import VueUeditorWrap from 'vue-ueditor-wrap'
export default {
  name: 'article',
  components: {
    // VueUeditorWrap
  },
  data() {
    return {
      visible: false,
      disableSubmit: false,
      form1: {},
      form: {},
      properties: [],
      tag: [],
      inputValue: '',
      inputVisible: false,
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      categoryId: '顶级栏目',
    }
  },
  methods: {
    baocun() {
      var i
      for (i = 0; i < this.properties.length - 1; i++) {
        this.form.properties += this.properties[i] + ','
      }
      this.form.properties += this.properties[i]

      c_postAction('/admin/archives/add', this.form).then(
        function (res) {
          console.log(res)
        },
        function (err) {
          console.log(err)
        }
      )
      this.visible = false
    },
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
    },

    showInput() {
      this.inputVisible = true
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleOpen() {
      this.visible = true
    },
    handleInputConfirm() {
      let inputValue = this.inputValue
      if (inputValue) {
        this.dynamicTags.push(inputValue)
      }
      this.inputVisible = false
      this.inputValue = ''
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleOk() {},
    handleCancel() {
      this.visible = false
    },
  },
}
</script>
<style scoped>
/* @import url('//unpkg.com/element-ui@2.13.2/lib/theme-chalk/index.css'); */

.addArticle {
  /* float: right;
  width: 84%;
  height: 650px;
  border: 1px solid #000000; */
  overflow: auto;
  background-color: white;
  text-align: left;
  overflow-y: scroll;
  height: 740px;
}
.el-upload__input{
  display: none;
}
.avatar-uploader > .ant-upload {
  width: 104px;
  height: 104px;
}
.ant-upload-select-picture-card i {
  font-size: 49px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}

.ant-table-tbody .ant-table-row td {
  padding-top: 10px;
  padding-bottom: 10px;
}
.drawer-bootom-button {
  bottom: -8px;
  width: 100%;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  left: 0;
  background: #fff;
  border-radius: 0 0 2px 2px;
}
.col-md,
.col-lg {
  /* position: relative; */
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
}
.title {
  border-bottom: 1px solid #eee;
  background-color: white;
  padding: 10px 15px;
  border-top-right-radius: 3px;
  border-top-left-radius: 3px;
  text-align: initial;
}
h1 {
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 10px;
}
.breadcrumb {
  padding: 8px 15px;
  list-style: none;
  background-color: white;
  border-radius: 4px;
  margin-top: 0px;
  margin-bottom: 20px;
  text-align: initial;
}
li {
  display: inline;
}
a {
  text-decoration: none;
}
.panel-body {
  padding: 15px;
  background-color: white;
}
form {
  display: block;
  margin-top: 0em;
}
title {
  width: 500px !important;
  margin-bottom: 15px;
}
.col-lg {
  position: relative;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
}
label {
  display: inline-block;
  margin-bottom: 5px;
  font-weight: bold;
  text-align: initial;
}

.form-group {
  margin-bottom: 15px;
  width: 100%;
  text-align: left;
}
.form-control-medium {
  width: 500px !important;
}
.form-control-small {
  width: 200px !important;
}
.form-control {
  box-shadow: none;
  width: 100%;
  font-size: 14px;
  color: #555;
  vertical-align: middle;
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
}
.picker {
  background-color: blue;
}
.col-lg-3 {
  position: inherit;
  min-height: 1px;
  padding-right: 15px;
  /* padding-left: 15px; */
  display: inline-block;
}
.form-group > .form-control {
  width: 98%;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.btn.btn-info {
  background-color: #23b7e5;
  border: none;
  -webkit-transition: all 0.3s ease;
  -moz-transition: all 0.3s ease;
  -ms-transition: all 0.3s ease;
  -o-transition: all 0.3s ease;
  transition: all 0.3s ease;
}
.btn {
  font-weight: 500;
  -webkit-border-radius: 3px;
  -moz-border-radius: 3px;
  border-radius: 3px;
  border: 1px solid transparent;
  display: inline-block;
  padding: 6px 12px;
  margin-bottom: 0;
  font-size: 14px;
  font-weight: normal;
  line-height: 1.428571429;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  cursor: pointer;
  border: 1px solid transparent;
  border-radius: 4px;
}
.btn-info {
  color: #fff;
  background-color: #5bc0de;
  border-color: #46b8da;
}
.form-btn-group-left {
  text-align: initial;
}
</style>
