<script src="//unpkg.com/vue/dist/vue.js"></script>
<script src="//unpkg.com/element-ui@2.13.2/lib/index.js"></script>
<template>
  <a-drawer title="编辑" :visible="visible" width="800" @close="handleCancel">
    <div class="editColumn">
      <!-- <div class="row"> -->
        <!--编辑栏目标题 -->
        <!-- <div class="editcolumn">
          <h1 class="panel-heading">编辑栏目</h1>
        </div> -->

        <!--编辑栏目路径显示开始-->
        <!-- <ul class="route">
          <li class="home-page-route">
            <a class="home-page-route-a" href="/admin/dashboard/index">首页</a>
          </li>
          <li class="editcolumn-route">/ 编辑栏目</li>
        </ul> -->
        <!--编辑栏目路径显示结束-->
      <!-- </div> -->

      <div class="row">
        <div class="col-lg-12">
          <section class="panel">
            <div class="addbody">
              <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
                <el-tab-pane label="基本设置" name="first">
                  <div class="tab-body">
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">栏目名称：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.cnname"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">栏目英文名称：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.enname"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="form_model">表单模型：</label>
                      </div>
                      <div class="input1">
                        <el-select class="moudelselect" v-model="form.formId" clearable>
                          <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="imagePath">栏目图片：</label>
                      </div>
                      <div class="input1">
                        <el-upload action="#" list-type="picture-card" :auto-upload="false">
                          <i slot="default" class="el-icon-plus"></i>
                          <div slot="file" slot-scope="{file}">
                            <img class="el-upload-list__item-thumbnail" :src="file.url" alt />
                          </div>
                        </el-upload>
                        <el-dialog :visible.sync="dialogVisible">
                          <img width="100%" :src="dialogImageUrl" alt />
                        </el-dialog>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">上级栏目：</label>
                      </div>
                      <div class="input1">
                        <el-select class="moudelselect" :disabled="true" v-model="input4" clearable>
                          <el-option
                            v-for="item in options4"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">分页大小：</label>
                      </div>
                      <div class="input1">
                        <el-input class="form-control" v-model="form.pageSize" clearable></el-input>
                      </div>
                    </div>
                    <div class="form-group radio_group1">
                      <div class="radio_group2">
                        <label for="title">显示/隐藏：</label>
                      </div>
                      <el-radio-group v-model="form.isShow">
                        <el-radio :label="1">显示</el-radio>
                        <el-radio :label="0">隐藏</el-radio>
                      </el-radio-group>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">栏目描述：</label>
                      </div>
                      <div class="input1">
                        <el-input type="textarea" v-model="form.description"></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">栏目内容：</label>
                      </div>
                      <div class="input1">
                        <!-- <vueUeditor ueditorPath="/static/utf8-jsp/" v-model="form.htmlContent"></vueUeditor> -->
                      </div>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="模板管理" name="second">
                  <div class="tab-body">
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">栏目模型：</label>
                      </div>
                      <div class="input1">
                        <el-select class="moudelselect" v-model="form.catModel" clearable>
                          <el-option
                            v-for="item in options5"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">访问地址：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.visitUrl"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">封面模板：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.coverTemp"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">列表模板：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.listTemp"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">内容页模板：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.articleTemp"
                          clearable
                        ></el-input>
                        <label for="文章内容">文章内容：</label>
                        <!-- <vueUeditor
                          class="editor"
                          ueditorPath="/static/utf8-jsp/"
                          v-model="form.mdContent"
                        ></vueUeditor> -->
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">链接地址：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.linkUrl"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="扩展" name="third">
                  <div class="tab-body">
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">扩展字段1：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.ext01"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">扩展字段2：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.ext02"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">扩展字段3：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.ext03"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">扩展字段4：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.ext04"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                    <div class="form-group form-control-medium">
                      <div class="name1">
                        <label for="title">扩展字段5：</label>
                      </div>
                      <div class="input1">
                        <el-input
                          placeholder="请输入内容"
                          class="form-control"
                          v-model="form.ext05"
                          clearable
                        ></el-input>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
            <div class="drawer-bootom-button" style="text-align: right;">
              <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
                <a-button style="margin-right: .8rem">取消</a-button>
              </a-popconfirm>
              <a-button @click="baocun" type="primary">提交</a-button>
            </div>
            <!-- <div class="panel-body table-responsive">
                        <table class="table table-hover">

                        </table>
            </div>-->
          </section>
        </div>
      </div>
    </div>
  </a-drawer>
</template>

<script>
// import vueUeditor from 'vue-ueditor'
import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
export default {
  components: {
    // vueUeditor,
  },
  data() {
    return {
      activeName: 'first',
      form: {},
      visible: false,
      options: [
        {
          value: '',
          label: '',
        },
      ],
      options2: [
        {
          value: '',
          label: '',
        },
      ],
      options4: [
        {
          value: '',
          label: '',
        },
      ],
      options5: [
        {
          value: 1,
          label: '封面',
        },
        {
          value: 2,
          label: '列表',
        },
        {
          value: 3,
          label: '外部链接',
        },
      ],
      options10: [
        {
          value: '选项1',
          label: '封面',
        },
        {
          value: '选项2',
          label: '列表',
        },
        {
          value: '选项3',
          label: '外部链接',
        },
      ],
      value: '',
      input0: '',
      input1: '',
      input2: '',
      input3: '',
      input4: '顶级栏目',
      input5: '',
      input6: '',
      input7: '1',
      input8: '',
      input9: '',
      input10: '',
      input11: '',
      input12: '',
      input13: '',
      input14: '',
      input15: '',
      input16: '',
      input17: '',
      input18: '',
      input19: '',
      input20: '',
      fileList: [{ name: '', url: '' }],
      radio: '1',
      textarea: '',
      dialogImageUrl: '',
      dialogVisible: false,
    }
  },
  created(){
    this.form = {}
  },  
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleCancel() {
      this.visible = false
    },
    handleOpen(record) {
      this.visible = true
      console.log(record)
      this.form = record
      console.log(this.form)
    },
    Getform() {
      var itme,
        k = {
          value: '',
          label: '',
        }
      var _this = this
      c_postAction('/admin/forms/list', {
          page: 1,
          pageSize: 100,
        })
        .then(
          function (res) {
            itme = res.data.list
            console.log(itme)
            for (var i = 0; i < itme.length; i++) {
              _this.options.push({
                value: itme[i].id,
                label: itme[i].formName,
              })
            }
          },
          function (err) {
            console.log(err)
          }
        )
    },
    baocun() {
      console.log(this.form)
      c_postAction('/admin/category/edit', this.form).then(
        function (res) {
          console.log(res)
        },
        function (err) {
          console.log(err)
        }
      )
      this.visible = false
    },
  },
  
  created() {
    this.Getform()
    var MJsonStr = sessionStorage.getItem('message_category')
    MJsonStr = JSON.parse(MJsonStr)
    this.form = MJsonStr
  },
}
</script>

<style>
/* @import url('//unpkg.com/element-ui@2.13.2/lib/theme-chalk/index.css'); */

/* .basic-body {
        background-color: #FFFFFF;
        margin-top: 0px;
        padding-top: 20px;
        padding-right: 10px;
        padding-bottom: 20px;
        padding-left: 10px;
    } */

.editColumn {
  /* float: right;
  width: 84%;
  height: 650px;
  border: 1px solid #000000; */
  overflow: auto;
  overflow-y: scroll;
  /* height: 740px; */
  text-align: left;
  background-color: white;
}
.row {
  padding-left: 15px;
  padding-right: 15px;
}

.drawer-bootom-button {
    /* position: absolute; */
    bottom: -8px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
  
.editcolumn {
  background: white;
  text-align: left;
}

.panel-heading {
  text-align: left;
  margin-top: 20px;
  margin-right: 0px;
  margin-bottom: 10px;
  margin-left: 0px;
  padding-top: 10px;
  padding-right: 15px;
  padding-bottom: 10px;
  padding-left: 15px;
}

ul.route {
  background: white;
  margin-top: 0px;
  margin-right: 0px;
  margin-bottom: 20px;
  margin-left: 0px;
  padding-top: 8px;
  padding-right: 15px;
  padding-bottom: 8px;
  padding-left: 15px;
  border-radius: 4px;
  text-align: left;
}

.home-page-route,
.editcolumn-route {
  display: inline;
  border-radius: 4px;
}

.home-page-route-a {
  text-decoration: none;
}

.panel {
  margin-bottom: 20px;
  background-color: white;
  border-radius: 4px;
}

.panel-body {
  padding-top: 15px;
  padding-right: 15px;
  padding-bottom: 15px;
  padding-left: 15px;
}

.panel > .panel-body + .table,
.panel > .panel-body + .table-responsive {
  border-top: 1px solid #ddd;
}

.tab-body {
  padding-top: 15px;
  padding-right: 15px;
  padding-bottom: 15px;
  padding-left: 15px;
  background-color: white;
}

.form-group {
  font-weight: bold;
}

.form-control-medium {
  margin-bottom: 15px;
}

.name1 {
  margin-bottom: 5px;
}

.input1 {
  padding-top: 6px;
  padding-right: 12px;
  padding-bottom: 6px;
}

.form-control {
  width: 500px;
  height: 34px;
}

.radio_group1 {
  margin-bottom: 15px;
}

.radio_group2 {
  display: inline-block;
}

.radio_group3 {
  margin-left: 20px;
  padding-left: 10px;
  display: inline;
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
  background-image: none;
  border: 1px solid transparent;
  border-radius: 4px;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  -o-user-select: none;
  user-select: none;
}

.btn-info {
  color: #fff;
  background-color: #5bc0de;
  border-color: #46b8da;
}
</style>




