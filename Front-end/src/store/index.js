import Vue from 'vue'
import Vuex from 'vuex'

import app from './modules/app'
import user from './modules/user'
import permission from './modules/permission'
import enhance from './modules/enhance'
import online from './modules/online'
import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    permission,
    enhance,
    online
  },
  state: {
    dataBaseList: [],
    tableCols: [],
    tableData: [],
    chooseTablefromId: '',
    formMakingJson: [],
    myValue:{}, //表单
    exampleId: '',
    isOpenNode: '0',//是否开启了节点，默认为0未开启，开启了则为1，完成了节点又重置为0

  },
  mutations: {
    changeDataBaseList(state, newList) {
      state.dataBaseList = newList;
    },
    changeTableCols(state, newList) {
      state.tableCols = newList;
    },
    changeChooseTablefromId(state, tablefromId) {
      state.chooseTablefromId = tablefromId;
    },
    changeFormMakingJson(state, json) {
      state.formMakingJson = json;
    },
    changeSecondTableCols(state, newList) {
      state.secondTableCols = newList;
    },
    changeTableData(state, newList) {
      state.tableData = newList;
    },
    cleanTableData(state, newList) {
      // console.log("触发了");
      // console.log("newList",newList);
      state.tableData = newList;
    },
    changFormValue(state,newList){
      state.myValue = newList
      //console.log("state.myValue",state.myValue)
    },
    changeExampleIdValue(state, newList) {
      state.exampleId = newList
      console.log('state.exampleId',state.exampleId)
    },
    changeisOpenNode(state, newList) {
      state.isOpenNode = newList
      console.log('state.isOpenNode',state.isOpenNode)
    }

  },
  actions: {

  },
  getters
})
