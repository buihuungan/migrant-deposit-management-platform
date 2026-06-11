<template>
<a-drawer title="新增" :visible="visible" width="800" @close="handleCancel">
  <div class="addForm">
    <div class="col-md">
      <!-- <h1 class="title">添加表单</h1>
      <ul class="breadcrumb">
        <li class="active">
          <a class="index"
             href>
            <i class="el-icon-s-home"></i>首页
          </a>
        </li>
        <li class="active">/ 添加表单</li>
      </ul> -->
    </div>
    <div class="col-lg">
      <div class="panel-body">
        <el-form ref="form"
                 :model="form">
          <div class="form-group form-control-medium">
            <label for="formName">表单名：</label>
            <el-input v-model="form.formName"
                      class="form-control"></el-input>
          </div>
          <div class="form-group form-control-medium">
            <label for="tableName">附加表名：</label>
            <el-input v-model="form.tableName"
                      class="form-control"></el-input>
          </div>
          <div class="form-group">
            <label for="type">表单性质：</label>
            <el-radio-group v-model="form.type">
              <el-radio label="1">自定义表单</el-radio>
              <el-radio label="0">系统表单</el-radio>

            </el-radio-group>
          </div>
          <div class="drawer-bootom-button"  style="text-align: right;">
              <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
                <a-button style="margin-right: .8rem">取消</a-button>
              </a-popconfirm>
              <a-button @click="baocun" type="primary">提交</a-button>
            </div>
        </el-form>
      </div>
    </div>
  </div>
</a-drawer>
</template>

<script>
  import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
export default {
  data () {
    return {
      visible:false,
      form: {
        formName: "",
        tableName: "",
        type: "1"
      }
    };
  },
  methods: {
    handleOpen(){
      this.visible= true
    },
    handleCancel(){
      this.visible = false
    },
    baocun () {
      console.log(this.form)
      c_postAction(
          "/admin/forms/add",
          this.form
        )
        .then(
          function (res) {
            console.log(res)
          },
          function (err) {
            console.log(err);
          }
        );
        this.visible = false
        this.$emit("ok")
    }
  }
};
</script>

<style scoped>
.addForm {
  /* float: right;
    width: 84%;
    height: 100%;
    border: 1px solid #000000; */
  overflow: auto;
  height: 740px;
  background-color: white;
  text-align: left;
}
.el-input{
  display:contents !important;
}
.el-input__inner{
  margin-bottom: 10px;
}
.col-md,
.col-lg {
  position: relative;
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
}
h1 {
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 10px;
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
.breadcrumb {
  padding: 8px 15px;
  list-style: none;
  background-color: white;
  border-radius: 4px;
  margin-top: 0px;
  margin-bottom: 20px;
}
li {
  display: inline;
}
.panel-body {
  padding: 15px;
  background-color: white;
  margin-bottom: 20px;
}
/* a {
  text-decoration: none;
} */
/* .index {
  color: #000;
} */

/* label {
  display: inline-block;
  margin-bottom: 5px;
  font-weight: bold;
} */
form {
  display: block;
  margin-top: 0em;
}
.form-group {
  margin-bottom: 15px;
}
.form-control-medium {
  width: 500px !important;
}
/* .form-control {
  box-shadow: none;
  width: 100%;
  font-size: 14px;
  line-height: 1.428571429;
  color: #555;
  vertical-align: middle;
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
} */
.checkbox-inline {
  font-weight: normal;
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
