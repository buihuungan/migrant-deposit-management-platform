<template>
    <a-modal id="app" 
    :visible="visible" 
    width="80%"
      :centered="true"
      :zIndex="300"
      :closable="false"
      :destroyOnClose="true">
        <ckeditor :editor="editor" v-model="editorData" :config="editorConfig" @ready="onReady"></ckeditor>
        <span slot="footer" class="dialog-footer">
          <el-button @click="visible = false">取 消</el-button>
          <el-button type="primary" @click="saveWaresDesc()">确 定</el-button>
        </span>
    </a-modal>
</template>

<script>
    import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
    import {ce_postAction1 }from '@api/coursewareEngineApi'
    import MyUploadAdapter from '@/api/MyUploadAdapter.js';
    import qs from 'qs'
    export default {
        name: 'app',
        data() {
            return {
                visible:false,
                editor: ClassicEditor,
                wareId:'',
                editorData: '<p>Content of the editor.</p>',
                editorConfig: {
                    // The configuration of the editor.
                }
            };
        },
        methods:{
            openCourse(dataInitial,visible,wareId){
                console.log("gegee",dataInitial)
                this.visible = visible
                this.editorData = ` ${dataInitial}`
                this.wareId = wareId
            },
            onReady(editor) {
              // 自定义上传图片插件
              editor.plugins.get("FileRepository").createUploadAdapter = loader => {
                return new MyUploadAdapter(loader);
              }
            },
            saveWaresDesc(){
                ce_postAction1(`/business/saveWareDesc`, {description: this.editorData ,blobTempId:this.wareId})
            .then((res) => {
              if (res.success) {
                this.visible = false
                this.$message.success('保存成功')
              } else {
                  this.visible = false
                this.$message.error('保存失败')
              }
            })
            .catch((err) => {
              console.log(err)
            })       
            },
        },
    }
</script>

<style>
  .ck-editor__editable_inline {
    height: 500px !important;
}
</style>
<style scoped>
  .header-content-save {
  height: 32px;
  border-radius: 4px;
  background-color: rgba(23, 145, 255, 1);
  margin-left: 24px;
  width: 88px;
  justify-content: center;
  align-items: center;
}
.header-content-save-info {
    display: block;
    overflow-wrap: break-word;
    color: rgba(255, 255, 255, 1);
    font-size: 14px;
    font-family: PingFangSC-Medium;
    white-space: nowrap;
    line-height: 31px;
    text-align: center;
    margin-top: 20px;
}
</style>