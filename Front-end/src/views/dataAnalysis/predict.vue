<template>
  <div class="main">
    <el-steps :active="state.step" process-status="finish" finish-status="success" align-center style="height: 7%">
      <el-step title="选择模型" :description="step1Description" icon="el-icon-guide"></el-step>
      <el-step title="选择预测数据集" :description="step2Description" icon="el-icon-s-data"></el-step>
      <el-step title="预测结果" icon="el-icon-coin"></el-step>
    </el-steps>
    <div class="trainMain">
      <el-collapse-transition>
        <component class="component" v-bind:is="state.component" :ref="state.component"></component>
      </el-collapse-transition>
    </div>
    <div class="bottom">
      <el-button plain type="success" icon="el-icon-arrow-left" @click="previousStep">上一步</el-button>
      <el-button plain type="success" @click="netxStep" v-if="state.step < 2">
        <i class="el-icon-arrow-right el-icon--right">下一步</i>
        <!-- <span v-if="state.step === 3">开始训练</span> -->
      </el-button>
      <el-button plain type="success" @click="predict()" v-if="state.step == 2">
        <i class="el-icon-arrow-right el-icon--right">预测</i>
      </el-button>
    </div>
    <div>
      <div class="tagB"><a-button type="success"> 维修方案记录 </a-button></div>
      <a-table :columns="columns" :data-source="data" :scroll="{ x: 1000 }" bordered>
      </a-table>
    </div>
  </div>
</template>
<script>
import choseModel from './predictStep/choseModel.vue'
import choseData from './predictStep/choseData.vue'
import predictChart from './predictStep/predictChart.vue'
import store from '@/store/store'
import { getAction } from '@/api/manage'
export default {
  components: {
    choseModel,
    choseData,
    predictChart,
  },
  data() {
    return {
      state: {
        step: 0,
        component: 'choseModel',
      },
      step1Description: '',
      step2Description: '',
      columns: [
        {
          title: '设备标识',
          dataIndex: 'equipment_code',
          align: 'center',
          width: 200,
        },
        {
          title: '设备名称',
          dataIndex: 'equipment_name',
          align: 'center',
          width: 150,
        },
        {
          title: '缺陷时间',
          dataIndex: 'defect_time',
          align: 'center',
          width: 150,
        },
        {
          title: '缺陷上报人',
          dataIndex: 'defect_reporter',
          align: 'center',
          width: 150,
        },
        {
          title: '维修事件名称',
          dataIndex: 'repair_event_name',
          align: 'center',
          width: 150,
        },
        {
          title: '维修时间',
          dataIndex: 'repair_time',
          align: 'center',
          width: 150,
        },
        {
          title: '维修人',
          dataIndex: 'repair_man',
          align: 'center',
          width: 150,
        },
        {
          title: '维修方案及程序',
          dataIndex: 'maintenance_plan',
          align: 'center',
          width: 200,
        },
        {
          title: '需要设备配件',
          dataIndex: 'required_equipment_accessories',
          align: 'center',
          width: 200,
        },
        {
          title: '需要的材料',
          dataIndex: 'required_materials',
          align: 'center',
          width: 200,
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark',
          width: 200,
        },
      ],
      data: [],
    }
  },
  mounted() {
    let params = {
      pageNo: 1,
      pageSize: -521,
    }
    getAction('/online/cgform/api/getData/774943b3cf664da28d751b6700f9d668', params).then((res) => {
      if (res.success) {
        this.data = res.result.records
      }
    })
  },
  methods: {
    predict() {
      this.$refs.predictChart.predict()
    },
    netxStep() {
      let modelPredict = store.getters.getModelPredict
      console.log(store)
      switch (this.state.step) {
        case 0: {
          if (modelPredict.chosedModel == undefined) {
            this.$alert('请选择一个模型', '提示', {
              confirmButtonText: '确定',
              callback: (action) => {},
            })
            return
          } else {
            if (modelPredict.chosedModel.length === 1) {
              //检查是否选择一个算法
              this.state = { step: 1, component: 'choseData' }
              this.step1Description = modelPredict.chosedModel[0].modelNameZh
            } else if (modelPredict.chosedModel.length > 1) {
              this.$alert('只能选择一个模型', '提示', {
                confirmButtonText: '确定',
                callback: (action) => {},
              })
            } else if (modelPredict.chosedModel.length < 1) {
              this.$alert('请选择一个模型', '提示', {
                confirmButtonText: '确定',
                callback: (action) => {},
              })
            }
          }
          break
        }
        case 1: {
          // if (modelPredict.chosedPredictData == undefined ){
          //   this.$alert("请选择一个预测数据集", '提示', {
          //     confirmButtonText: '确定',
          //     callback: action => {
          //     }
          //   });
          //   return;
          // }else{
          //   if(modelPredict.chosedPredictData.length === 1) { //检查是否选择一个算法
          //     this.state = { step: 2, component: 'predictChart' };
          //     this.step2Description = modelPredict.chosedPredictData[0].splitName
          //   }else if(modelPredict.chosedPredictData.length > 1){
          //     this.$alert("只能选择一个模型", '提示', {
          //       confirmButtonText: '确定',
          //       callback: action => {
          //       }
          //     });
          //   }else if(modelPredict.chosedPredictData.length < 1){
          //     this.$alert("请选择一个模型", '提示', {
          //       confirmButtonText: '确定',
          //       callback: action => {
          //       }
          //     });
          //   }
          // }
          this.state = { step: 2, component: 'predictChart' }
          break
        }
        default: {
          break
        }
      }
    },
    previousStep() {
      switch (this.state.step) {
        case 2: {
          this.state = { step: 1, component: 'choseData' }
          break
        }
        case 1: {
          this.state = { step: 0, component: 'choseModel' }
          break
        }
      }
    },
  },
}
</script>
<style scoped>
.tagB{
  margin:20px 20px 20px 20px
}
.main {
  padding: 24px;
  background-color: #fff;
}
.trainMain {
  max-height: 600px;
  padding: 20px 0 50px 0;
}
.component {
}
.bottom {
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 100px;
  margin-top: 10px;
}
/* .btn {
  background-color: bisque;
  border-radius: 10px;
  height: 40px;
  width: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
} */
</style>