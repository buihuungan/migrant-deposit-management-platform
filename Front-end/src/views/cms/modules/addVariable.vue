<template>
<a-drawer title="新增" :visible="visible" width="800" @close="handleCancel">
  <div class="addVariable">
    <div class="col-md">
      </div>
      <div class="col-lg">
        <div class="panel-body">
          <form
            class="col s12"
            id="form1"
            action="/admin/forms/add"
            onsubmit="return validateForm();"
            method="post"
          >
            <div class="form-group form-control-medium">
              <label for="variableName">变量名:</label>
              <el-input v-model="form.itemName" placeholder="请输入变量名" class="form-control"></el-input>
            </div>
            <div class="form-group form-control-medium">
              <label for="describe">描述:</label>
              <el-input v-model="form.info" placeholder="请输入内容" class="form-control"></el-input>
            </div>
            <div class="form-group form-control-medium">
              <label for="group">组:</label>
              <el-input v-model="form.groupId" placeholder="请输入组名" class="form-control"></el-input>
            </div>
            <div class="form-group form-control-medium">
              <label for="group">变量值:</label>
              <el-input v-model="form.value" placeholder="请输入变量值" class="form-control"></el-input>
            </div>
            <div class="drawer-bootom-button" style="text-align: right;">
              <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
                <a-button style="margin-right: .8rem">取消</a-button>
              </a-popconfirm>
              <a-button @click="baocun" type="primary">提交</a-button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </a-drawer>
</template>

<script type="text/javascript" charset="utf-8" src="static/UEditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="static/UEditor/ueditor.all.min.js"></script>
<script>
// import VueUeditorWrap from 'vue-ueditor-wrap'
import { c_getAction, c_postAction, c_httpAction, c_putAction, c_deleteAction } from '@/api/cmsApi'
export default {
  components: {
    // VueUeditorWrap
  },
  name: 'App',
  data() {
    return {
      visible: false,
      form: {
        groupId: '',
        info: '',
        itemName: '',
        value: '',
        disableSubmit:false
      },
    }
  },

  methods: {
    handleOpen() {
      this.visible = true
    },
    handleCancel() {
      this.visible = false
    },
    baocun() {
      console.log(this.form)
      c_postAction('/admin/variable/add', this.form).then(
        function (res) {
          console.log(res)
        },
        function (err) {
          console.log(err)
        }
      )
      this.visible = false
      this.$emit('ok')
    },
  },
}
</script>

<style scoped>
.main {
  width: 100%;
}
.drawer-bootom-button {
    /* position: absolute; */
    margin-top: 10px;
    bottom: -8px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
.form-btn-group-left {
  text-align: left;
}
.content {
  text-align: left;
}
.el-input{
  display: contents !important;
}
.addVariable {
  /* float: right;
  width: 84%;
  height: 100%;
  border: 1px solid #000000; */
  overflow: auto;
  text-align: left;
  height: 740px;
  background-color: white;
}

.neirong {
  margin-bottom: 10px;
}
.anniu1 {
  display: inline-block;
}
.anniu {
  background: white;
  display: inline-block;
}
.col-md,
.col-lg {
  position: relative;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
}
.title {
  text-align: left;
  border-bottom: 1px solid #eee;
  background-color: white;
  padding: 10px 15px;
  border-top-right-radius: 3px;
  border-top-left-radius: 3px;
}
h1 {
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 10px;
}
/* .breadcrumb {
  text-align: left;
  padding: 8px 15px;
  list-style: none;
  background-color: white;
  border-radius: 4px;
  margin-top: 0px;
  margin-bottom: 20px;
}
=======
} */

.panel-body {
  padding: 15px;
  background-color: white;
}
a {
  text-decoration: none;
}
.index {
  color: #000;
}
.active {
  color: gray;
}
label {
  display: inline-block;
  margin-bottom: 5px;
  font-weight: bold;
}
form {
  display: block;
  margin-top: 0em;
}
.form-group {
  text-align: left;
  margin-bottom: 15px;
}
.form-control-medium {
  width: 500px !important;
}
.form-control {
  box-shadow: none;
  width: 100%;
  height: 34px;
  font-size: 14px;
  line-height: 1.428571429;
  color: #555;
  vertical-align: middle;
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
}
.savevariable {
  margin-top: 10px;
  font-size: 12px;
  padding-top: 5px;
  padding-right: 10px;
  padding-bottom: 5px;
  padding-left: 10px;
  border-radius: 4px;
  color: #fff;
  background-color: #5bc0de;
  border-color: #46b8da;
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
