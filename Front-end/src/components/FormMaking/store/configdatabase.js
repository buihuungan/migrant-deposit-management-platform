import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state:{
        dataBaseList:[],
        tableCols:[],
        secondTableCols:[]
    },
    mutations: {
        changeDataBaseList(state,newList){
          state.dataBaseList=newList;
        },
        changeTableCols(state,newList){
          state.tableCols=newList;
        },
        changeSecondTableCols(state,newList){
          state.secondTableCols = newList;
        },
    }
})