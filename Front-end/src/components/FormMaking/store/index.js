import Vue from 'vue'
import Vuex from 'vuex'

import app from './modules/app'
import user from './modules/user'
import permission from './modules/permission'
import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    permission
  },
  state: {
    dataBaseList:[],
    tableCols:[],
    
    chooseTablefromId:'',
    formMakingJson:[]
  },
  mutations: {
    changeDataBaseList(state,newList){
      state.dataBaseList=newList;
    },
    changeTableCols(state,newList){
      state.tableCols=newList;
    },
    changeChooseTablefromId(state,tablefromId){
      state.chooseTablefromId=tablefromId;
    },
    changeFormMakingJson(state,json){
      state.formMakingJson=json;
    },
    
  },
  actions: {

  },
  getters
})
