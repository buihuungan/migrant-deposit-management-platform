<template>
  <div>
    <div>
      <generate-form
        style="margin: 0 auto"
        insite="true"
        :data="formJson"
        :remote="func1"
        ref="generateForm"
        v-if="formJson.config.ui == 'element'"
      >
        <template v-slot:blank="scope">
          Width
          <el-input v-model="scope.model.blank.width" style="width: 100px"></el-input>Height：
          <el-input v-model="scope.model.blank.height" style="width: 100px"></el-input>
        </template>
      </generate-form>

      <!-- <antd-generate-form
        style="margin: 0 auto"
        insite="true"
        v-if="formJson.config.ui == 'antd'"
        :data="formJson"
        ref="generateAntForm"
      >
        <template v-slot:blank="scope">
          <a-input
            v-decorator="[
              'blank',
              {
                initialValue: scope.model.blank,
              },
            ]"
            style="width: 100px"
          ></a-input>
        </template>
      </antd-generate-form> -->
    </div>
  </div>
</template>
<script>
import GenerateForm from '../../components/Board/components/GenerateForm'
import AntdGenerateForm from '../../components/Board/components/AntdvGenerator/GenerateForm'
import {axios} from "../../utils/request";
import {getOneScreen} from '@api/boardapi'

export default {
  name: 'AnnTask',
  components: { GenerateForm, AntdGenerateForm },
  props: ['formDesignerId', 'taskId'],
  data() {
    return {
      formJson: {},
      func1: {
      },
    }
  },
  methods: {
    //得到表单
    getForm() {
      var id = '9faab68e098f989f8641ef7f9f005945'
      getOneScreen(id).then((res) => {
        this.formJson = JSON.parse(res.data.content)
        console.log("获取的表单数据",this.formJson);
      }).catch((err) => {
        console.log(err)
      })
    },
  },
  mounted() {
    this.getForm()
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