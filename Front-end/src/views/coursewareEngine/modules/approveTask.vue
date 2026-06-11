<template>
  <div>
    <div id="approveTaskContent">
      <div class="formbody">
        <div>
          <generate-form
            style="margin: 0 auto"
            insite="true"
            :data="dataForm"
            ref="generateForm"
            :remote="func1"
            :edit="false"
          >
            <template v-slot:blank="scope">
              Width
              <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
              <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
            </template>
          </generate-form>
        </div>
      </div>
    </div> 
    </div>
  </div>
</template>
<script>
import GenerateForm from '@/components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '@/components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction } from '@/api/onApi.js'

export default {
  name: 'ApproveTask',
  components: { GenerateForm, AntdGenerateForm },
  props: ['formId', 'dataId', 'tableId'],
  data() {
    return {
      dataForm: {},
      func1: {},
    }
  },
  updated() {},
  methods: {
    //得到一个表单
    getForm() {
      var preid = this.formId
      t_getAction('/admin/desform/' + preid + '/getConent')
        .then((res) => {
          if (res.success) {
            var formJson = JSON.parse(res.result)
            if (this.tableId != '' && this.dataId != '') {
              this.getOnlineJson(this.tableId, this.dataId, formJson)
            } else {
              this.dataForm = formJson
            }
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //得到online的json
    getOnlineJson(tableId, dataId, formJson) {
      o_getAction('/cgform/api/form/' + tableId + '/' + dataId)
        .then((res) => {
          var onlineJson = res.result
          if (onlineJson != null && onlineJson != '') {
            this.connData(onlineJson, formJson)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //拼接online键值配对
    connData(onlineJson, formJson) {
      for (let i = 0; i < formJson.list.length; i++) {
        if (formJson.list[i].type == 'grid') {
          for (let j = 0; j < formJson.list[i].columns.length; j++) {
            for (let z = 0; z < formJson.list[i].columns[j].list.length; z++) {
              if (formJson.list[i].columns[j].list[z].tableCol != '') {
                formJson.list[i].columns[j].list[z].options.defaultValue =
                  onlineJson[formJson.list[i].columns[j].list[z].tableCol]
              }
            }
          }
        } else if (formJson.list[i].type == 'table') {
          // for (let h = 0; h < formJson.list[i].tableColumns.length; h++) {
          //  formJson.list[i].tableColumns[h].options.defaultValue =
          //     onlineJson[formJson.list[i].tableColumns[h].tableCol]
          // }
          for (let k = 0; k < onlineJson[formJson.list[i].schedule].length; k++) {
            for (let h = 0; h < formJson.list[i].tableColumns.length; h++) {
              formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].schedule]
            }
          }
        } else {
          if (formJson.list[i].tableCol != '') {
            formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].tableCol]
            // if (formJson.list[i].type == 'fileupload') {
            //   var fileupload=JSON.parse(formJson.list[i].options.defaultValue)
            //   fileupload[0].dbpath="https://git.data.wust.edu.cn/api1/online/common/download"+fileupload[0].dbpath
            //   formJson.list[i].options.defaultValue=JSON.stringify(fileupload)
            // }
          }
        }
      }
      this.dataForm = formJson
    },
  },
  mounted() {
    this.getForm()
  },
}
</script>
<style scoped>
#approveTaskContent {
  display: flex;
  justify-content: center;
  padding: 5px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin: 0 auto;
  cursor: pointer;
  z-index: 9999;
  /* pointer-events: none; */
  user-select: none;
}
/* .formbody .el-input__suffix-inner {
  pointer-events: none;
} */
.formTable {
  width: 90%;
  border: 1px solid #000000;
  margin: 0 auto;
}
.title {
  width: 100%;
  height: 50px;
  text-align: center;
}
.formbody {
  margin-top: 20px;
  width: 100%;
}
.submitBtn {
  margin-top: 20px;
}
.submitBtn button {
  margin: 20px auto;
  left: 46%;
}
.buttonstyle {
  margin-left: 670px;
}
</style>