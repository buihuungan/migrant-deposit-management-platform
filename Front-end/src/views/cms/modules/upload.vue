<template>
<el-dialog
  title="提示"
  :visible="visible"
  width="30%"
  :before-close="handleClose">
    <div>
        <label class="labeltips">
        <strong>&nbsp;&nbsp;注意!</strong>{" "}
        上传成功后切记点击确定按钮，否则不会保存！
        </label>
        <el-upload
            class="upload-demo"
            action="/api1/cms/file/uploadFile "
            :on-success='handleSuccess'
        >
            <el-button size="small" class="selectfile" type="primary">
            选择文件
            </el-button>
        </el-upload>
    </div>
    <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="addattachment">确 定</el-button>
    </span>
</el-dialog>
</template>

<script>
import axios from "axios";
import { c_getAction,c_postAction,c_httpAction,c_putAction,c_deleteAction } from '@/api/cmsApi'
import api from '@api/index'

export default {
    props:{
        dialogVisible:{
            type:Boolean,
            default:false
        }
    },
    data(){
        return{
            visible:false,
            form : {
                fileName: "",
                filePath: "",
                fileSize: "",
                fileType: "",
            },
        }
    },
    methods:{
        handleSuccess(response, file, fileList){
            this.setFormData(response.data);
        },
        handleClose(){
            this.visible=false
        },
        handleOpen(){
            this.visible=true
        },
        setFormData(data){
            this.form.fileName=data.name;
            this.form.filePath=data.filepath;
            this.form.fileSize=data.filesize;
            this.form.fileType=data.filetype;
            console.log(this.form)
        },
        addattachment(){
            
            let self=this;
            c_postAction("/admin/attachment/add", this.form)
            .then(
            function (res) {
                self.$emit('update:data',res.filePath)
                // console.log(res);
                self.$emit('refreshData');
            },
            function (err) {
                console.log(err);
            }
            );
            this.handleClose();
        }
    }
}
</script>