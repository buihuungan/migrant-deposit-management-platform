<script src="//unpkg.com/vue/dist/vue.js"></script>
<script src="//unpkg.com/element-ui@2.13.2/lib/index.js"></script>
<template>
<a-modal
      title="新增"
      :visible="visible"
      width="800"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
    >
  <div class="editField">
    <div class="col-md">
    </div>
    <div class="col-lg">
      <section class="panel">
        <div class="panel-body">
          <el-form ref="form" :model="form">
            <div class="form-group form-control-medium">
              <label for="fieldText">表单提示字：</label>
              <el-input v-model="form.fieldText" id="fieldText" name="fieldText" type="text" class="form-control"></el-input>
            </div>
            <div class="form-group form-control-medium">
              <label for="field">字段名称：</label>
              <el-input
                v-model="form.fieldName"
                id="field"
                name="field"
                type="text"
                class="form-control"
              ></el-input>
            </div>
            <div class="form-group form-control-medium">
              <label for="datatype">数据类型:</label>
              <br />
              <div class="row">
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="1">单行文本（VARCHAR）</el-radio>
                </div>
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="2">单行文本（CHAR）</el-radio>
                </div>
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="3">多行文本（TEXT)</el-radio>
                </div>
              </div>
              <div class="row">
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="4">HTML文本</el-radio>
                </div>
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="5">MARKDOWN文本</el-radio>
                </div>
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="6">附件类型</el-radio>
                </div>
              </div>
              <div class="row">
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="7">时间类型</el-radio>
                </div>
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="8">单选（RADIO）</el-radio>
                </div>
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="9">多选（CHECKBOX）</el-radio>
                </div>
              </div>
              <div class="row">
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="10">下拉选（SELECT）</el-radio>
                </div>
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="11">图片类型（IMAGE）</el-radio>
                </div>
                <div class="checkbox-item">
                  <el-radio v-model="form.dataType" label="12">图集（IMAGESET）</el-radio>
                </div>
              </div>
            </div>
            <div class="form-group">
              <label for="description">默认值：</label>
              <el-input type="textarea" v-model="form.defaultValue"></el-input>
              <p for="tip">如果定义数据类型为select、radio、checkbox时，此处填写被选择的项目(用“,”分开，如“男,女,人妖”)</p>
            </div>
            <div class="form-group form-control-small">
              <label for="clicks">字段长度：</label>
              <el-input
                v-model="form.maxLength"
                id="clicks"
                name="clicks"
                type="text"
                class="form-control"
              ></el-input>
            </div>
          </el-form>
        </div>
      </section>
    </div>
  </div>
</a-modal>
</template>

<script>
  import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
export default {
  data() {
    return {
      visible:false,
      confirmLoading:false,
      form: {}
    };
  },
  methods:{
    handleOpen(record){
      console.log(record)
      this.visible=true
      this.form = record
    },
    handleOk(){
      c_postAction('/admin/field/edit', this.form).then(
        
        function (res) {
          console.log(res)
        },
        function (err) {
          console.log(err)
        }
      )
      this.visible= false
    },
    handleCancel(){
      this.visible=false
    }
  }
};
</script>

<style scoped>
/* .body {
  background-color: inherit;
  padding-top: 20px;
  padding-left: 15px;
  padding-right: 15px;
} */
.editField {
  /* float: right;
  width: 84%;
  height: 100%;
  border: 1px solid #000000; */
  overflow: auto;
  overflow: scroll;
  height: 740px;
  background-color: white;
  text-align: left;
}
/* .el-form{
  display: grid;
} */
.col-md,
.col-lg {
  position: relative;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
}
.index {
  color: #000;
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
a {
  text-decoration: none;
}

.panel-body {
  padding: 15px;
  background-color: white;
}
label {
  display: inline-block;
  margin-bottom: 5px;
  font-weight: bold;
}
.form-control {
  box-shadow: none;
  width: 100%;
  font-size: 14px;
  line-height: 1.428571429;
  color: #555;
  vertical-align: middle;
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
}
.form-control-medium {
  width: 500px !important;
}
.form-group {
  margin-bottom: 15px;
}
.form-group > .form-control {
  width: 98%;
}
.form-control-small {
  width: 200px !important;
}
.row {
  padding-left: 15px;
  padding-right: 15px;
}
.checkbox-item {
  width: 200px;
  float: left;
  line-height: 40px;
}
p {
  margin: 0px;
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