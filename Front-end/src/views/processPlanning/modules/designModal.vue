<template>
  <a-modal
    :centered="true"
    :visible="visible"
    :footer="null"
    width="95%"
    :zIndex="100"
    :closable="false"
    :destroyOnClose="destroy"
  >
    <div id="formContent">
      <div class="formbody">
        <generate-form
          style="margin: 0 auto"
          insite="true"
          :data="formJson"
          ref="generateForm"
          :edit="false"
        >
          <template v-slot:blank="scope">
            Width
            <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
            <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
          </template>
        </generate-form>
      </div>
      <div class="submitBtn">
        <a-button type="primary" @click="close()">返回</a-button>
      </div>
    </div>
  </a-modal>
</template>
<script>
import GenerateForm from '../../../components/FormMaking/components/GenerateForm'
import AntdGenerateForm from '../../../components/FormMaking/components/AntdvGenerator/GenerateForm'
import { t_postAction, t_getAction } from '@/api/tempApi.js'
import { o_postAction, o_getAction, o_putAction } from '@/api/onApi.js'
import { w_postAction, w_getAction } from '../../../api/workapi'
import { ACCESS_TOKEN } from '@/store/mutation-types'
//import api from '@/api/index'
import api from '../../../api/index.js'
import { Message } from 'element-ui'

export default {
  name: 'salesModal',
  components: { GenerateForm, AntdGenerateForm },
  props: ['flowId'],
  data() {
    return {
      formJson: {},
      visible: false,
      destroy: true,
    }
  },
  methods: {
    close() {
      var _this = this
      this.$elementConfirm('是否关闭界面 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          _this.visible = false
          _this.$emit('close-annTask')
        })
        .catch(() => {})
    },
    getForm(record) {
      var id = this.flowId
      this.dataId = record.id
      t_getAction('/admin/desform/' + id + '/getConent')
        .then((res) => {
          this.getOnlineJson(res.result, this.dataId)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getOnlineJson(formJson, predataId) {
      //得到online的json
      o_getAction('/cgform/api/form/e8c9f23b81d74fc6a292c436d27faa70/' + predataId)
        .then((res) => {
          var onlineJson = res.result
          this.connData(onlineJson, JSON.parse(formJson))
        })
        .catch((err) => {
          console.log(err)
        })
    },
    connData(onlineJson, formJson) {
      //拼接online键值配对
      for (let i = 0; i < formJson.list.length; i++) {
        console.log(formJson.list[i])
        console.log(formJson.list[i].type)
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
          console.log(onlineJson[formJson.list[i].schedule])
          for (let k = 0; k < onlineJson[formJson.list[i].schedule].length; k++) {
            console.log(onlineJson[formJson.list[i].schedule][k])
            let tableValue = onlineJson[formJson.list[i].schedule][k]
            if (tableValue != undefined) {
              for (let h = 0; h < formJson.list[i].tableColumns.length; h++) {
                formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].schedule]
              }
            }
          }
        } else {
          if (formJson.list[i].tableCol != '') {
            formJson.list[i].options.defaultValue = onlineJson[formJson.list[i].tableCol]
          }
        }
      }
      this.formJson = formJson
      this.visible = true

      this.$nextTick(() => {
        $('.is_customized')[0].style.display = 'none'
        if ($('.is_customized').find('input[type=radio]:checked').val() === '0') {
          $('.childTable')[0].style.display = 'block'
          $('.customized_desc')[0].style.display = 'none'
        } else {
          $('.childTable')[0].style.display = 'none'
          $('.customized_desc')[0].style.display = 'block'
        }
      })
    },
  },
}
</script>
<style scoped>
#formContent {
  padding: 5px;
  background-color: #fff;
  border: 1px solid;
  border-radius: 5px;
  margin: 0 auto;
}
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
  left: 45%;
}
.abutton {
  margin-right: 20px;
}
</style>