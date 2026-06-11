<template>
  <div class="echarts-box">
    <div class="echarts" id="echarts"
         v-loading="loadingShadow"
         element-loading-text="正在测试，请稍候......"></div>
  </div>
</template>

<script>
import api from '@/api/index'
import echarts from 'echarts'
import {axios} from '../../../utils/request'
import store from "@/store/store"
export default {
  data() {
    return {
      chart: null,
      // predictForm: {
      //     predictName: "",
      //     modelId: "",
      //     splitId: "",
      // },
      xAxisData: [],
      yAxisDataSource: [],
      yAxisDataPrediction: [],
      loadingShadow: false
    }
  },
  mounted() {
    // let params = this.$route.params;
    // if (params.modelId !== undefined) {
    //     this.predictForm.modelId = params.modelId;
    //     this.predictForm.modelName = params.modelName;
    // }
    // this.chart = echarts.init(document.getElementById('echarts'))
    // this.predictForm.predictName = "predict-" + this.guid();
    // axios.get('http://31789g74t4.wicp.vip/' + "/model/getPredictParams")
    // .then(res => {
    //     if (res.status === 0) {
    //         this.models = res.data.models;
    //         this.frames = res.data.frames;
    //     }else{
    //
    //     }
    // })
    this.predictTeacher()
  },
  methods: {
    predictTeacher(){
      this.loadingShadow = true;
      let modelPredict = store.getters.getModelPredict;
      console.log(modelPredict);
      let predictForm = {
        predictName: "prediction-" + this.guid(),
        modelId: "1427203568241881089",
        sourceId: modelPredict.dataId,
      }
      axios.post(api.data_baseURL + "/model/predictTeacher", predictForm)
        .then(res => {
          console.log(res)
          if (res.status === 0) {
            this.xAxisData = res.data.time;
            this.yAxisDataSource = res.data.source;
            this.yAxisDataPrediction = res.data.predict;
            this.chart = echarts.init(document.getElementById('echarts'))
            let option = this.getOption();
            this.chart.setOption(option)// 设置配置项
          }else{
          }
          this.loadingShadow = false;
        })
    },
    predict() {
      this.loadingShadow = true;
      let modelPredict = store.getters.getModelPredict;
      let predictForm = {
        predictName: "prediction-" + this.guid(),
        modelId: modelPredict.chosedModel[0].modelId,
        splitId: modelPredict.chosedPredictData[0].id,
      }
      axios.post(api.data_baseURL + "/model/predict", predictForm)
        .then(res => {
          if (res.status === 0) {
            this.xAxisData = res.data.time;
            this.yAxisDataSource = res.data.source;
            this.yAxisDataPrediction = res.data.predict;
            this.chart = echarts.init(document.getElementById('echarts'))
            let option = this.getOption();
            this.chart.setOption(option)// 设置配置项
          }else{
          }
          this.loadingShadow = false;
        })
    },
    getOption() {
      return {
        tooltip: {
          trigger: 'axis'
        },
        // grid: {
        //     left: '3%',
        //     right: '4%',
        //     bottom: '3%',
        //     containLabel: true
        // },
        legend: {
          data: ['真实值', '预测值']
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.xAxisData
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: "真实值",
            data: this.yAxisDataSource,
            type: 'line',
            // smooth: true
          }, {
            name: "预测值",
            data: this.yAxisDataPrediction,
            type: 'line',
            // smooth: true
          }
        ]
      };
    },

    // Generate four random hex digits.
    S4() {
      return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    },
    // GUID
    guid() {
      return (this.S4()+this.S4()+"-"+this.S4()+"-"+this.S4()+"-"+this.S4()+"-"+this.S4()+this.S4()+this.S4());
    }
  }

}
</script>

<style lang="scss" scoped>
.echarts-box {
  .echarts {
    margin-top: 50px;
    width: 98%;
    height: 550px;
  }
}
.selections {
  margin:10px auto;
  /* border:1px solid #000;  */
  text-align:center;
  // display: inline;
}
.selections-form{
  display: inline-block;
  width: 350px;
}
</style>
