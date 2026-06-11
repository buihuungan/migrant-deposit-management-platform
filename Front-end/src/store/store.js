import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isCollapse: false,
  },
  modelTraining: {
    chosedAlgorithm: [], //算法
    chosedDataCollection: [], //数据集
    chosedParamters: [], //参数
  },
  modelPredict: {
    chosedModel: [],
    chosedPredictData: [],
    dataId:'',
  },
  mutations: {
    switchCollapse (state) {
      state.isCollapse = !state.isCollapse
    },
    setChosedAlgorithm(modelTraining, chosedAlgorithm) {
      modelTraining.chosedAlgorithm = chosedAlgorithm;
    },
    setChosedDataCollection(modelTraining, chosedDataCollection) {
      modelTraining.chosedDataCollection = chosedDataCollection;
    },
    setChosedParamters(modelTraining, chosedParamters) {
      modelTraining.chosedParamters = chosedParamters;
    },
    setChosedModel(modelPredict, chosedModel) {
      console.log("sdfg")
      modelPredict.chosedModel = chosedModel;
    },
    setChosedPredictData(modelPredict, chosedPredictData) {
      modelPredict.chosedPredictData = chosedPredictData;
    },
    setFileId(modelPredict,fileId){
      console.log("sdfgthy");
      modelPredict.dataId=fileId
    }
  },
  actions: {

  },
  getters: {
    getModelTraining: (modelTraining, getters) => {
      return modelTraining;
    },
    getModelPredict: (modelPredict, getters) => {
      return modelPredict;
    }
  }
})
