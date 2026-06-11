<template>
  <a-modal :centered="true" title="物料添加" :visible="visible" :destroyOnClose="destroy" @cancel="handleCancel" width="40%" :footer="false" :zIndex="100">
    <div id="formContent">
      <a-form-model :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-form-model-item label="标识">
          <a-input v-model="form.标识编码" />
        </a-form-model-item>
        <a-form-model-item label="物料名">
          <a-input v-model="form.名称" />
        </a-form-model-item>
        <a-form-model-item label="数量">
          <a-input v-model="form.数量" />
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
          <a-button type="primary" @click="onSubmit"> 增加 </a-button>
          <a-button style="margin-left: 10px" @click="handleCancel"> 取消 </a-button>
        </a-form-model-item>
      </a-form-model>
    </div>
  </a-modal>
</template>

<script>
import { Message } from 'element-ui'
export default {
  name: 'addModalNew',
  props: ['dataSource'],
  data() {
    return {
      destroy: true,
      visible: false,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
      form: {
        标识编码: '',
        名称: '',
        数量: '',
      },
    }
  },
  methods: {
    onSubmit() {
      console.log('submit!', this.form)
      console.log(this.dataSource)
      let flag = 1
      this.dataSource.forEach((item, index) => {
        if (item.标识编码 === this.form.标识编码) {
          flag = 0
          return
        }
      })
      if (flag) {
        this.dataSource.push(this.form)
        this.handleCancel()
      } else {
        Message({
          type: 'false',
          showClose: true,
          message: '已存在，请勿重复添加!',
        })
      }
      console.log('dataSource', this.dataSource)
    },
    openModal() {
      this.visible = true;
      this.destroy=false;
      this.form= {
        标识编码: '',
        名称: '',
        数量: '',
      };
    },
    handleCancel(){
      this.destroy=true;
      this.visible=false;
    }
  },
}
</script>