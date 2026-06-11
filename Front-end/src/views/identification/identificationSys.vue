<template>
  <div class="IdentificationSys">
    <a-button type="primary" :disabled="this.openState != 0" class="openDocking" @click="openDocking">{{
      open_name(openState)
    }}</a-button>
    <br />
    <a-button type="primary" :disabled="this.openState != 1" class="closeDocking" @click="closeDocking">{{
      close_name(openState)
    }}</a-button>
  </div>
</template>

<script>
import { axios } from '../../utils/request'
import { im_postAction, im_getAction } from '@/api/icimApi'
export default {
  data() {
    return {
      openState: '',
    }
  },
  computed: {
    open_name() {
      return function (status) {
        return '开 启 对 接'
      }
    },
    close_name() {
      return function (status) {
        return '关 闭 对 接'
      }
    },
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      im_getAction(`/docking/isStatue`).then((res) => {
        this.openState = res.data.result.result
      })
    },
    openDocking() {
      im_getAction(`/docking/isSend?statue=1`).then((res) => {
        if (res.data.result.result == true) {
          this.$message.success('开启对接成功')
          this.loadData()
        } else {
          this.$message.error('开启对接失败')
        }
      })
    },
    closeDocking() {
      im_getAction(`/docking/isSend?statue=0`).then((res) => {
        if (res.data.result.result == true) {
          this.$message.success('关闭对接成功')
          this.loadData()
        } else {
          this.$message.error('关闭对接失败')
        }
      })
    },
  },
}
</script>

<style>
.IdentificationSys {
  background: #fff;
  /* margin:24px 16px; */
  padding: 24px;
  height: 500px;
}
.IdentificationSys .openDocking {
  width: 120px;
  left: 50%;
  top: 45%;
  transform: translate(-50%, -50%);
}
.IdentificationSys .closeDocking {
  width: 120px;
  margin-top: 20px;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
</style>
