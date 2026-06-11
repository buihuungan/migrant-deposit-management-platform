<template>
  <div style="width: 100%">
    <el-table :data="modelTable" :span-method="objectSpanMethod" border v-show="modelTableShow">
      <el-table-column prop="algorithmNameZh" label="所属算法"> </el-table-column>
      <el-table-column prop="modelNameZh" label="模型名称"> </el-table-column>
      <el-table-column prop="description" label="描述"> </el-table-column>
      <el-table-column prop="createTime" label="创建时间"> </el-table-column>
      <el-table-column label="选择">
        <template slot-scope="scope">
          <el-checkbox v-model="modelTable[scope.$index].checked" @change="choseModel(scope.row)"></el-checkbox>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import api from '@/api/index'
import { axios } from '../../../utils/request'
import store from '@/store/store'
export default {
  data() {
    return {
      modelTableShow: true,
      modelTable: [
        // {
        //   algorithmId:"111",
        //   algorithmNameZh: "广义线性模型",
        //   modelId:'1',
        //   modelNameZh: '测试模型1',
        //   description: 'nothing',
        //   createTime: "2021-06-21 03:21:23"
        // },
      ],
      multipleSelection: [],
      mergeAlgorithmRows: [],
    }
  },
  mounted() {
    // axios.get(api.data_baseURL + '/model/doneTrainAndAlgorithm').then((res) => {
    //   if (res.status === 0) {
    //     this.modelTable = res.data
    //     this.mergeNums()
    //   }
    // })
    this.modelTable = [
      {
        algorithmId: '1416944332987981826',
        algorithmNameZh: '广义线性模型（GLM）',
        createTime: '2021-08-16 17:40:24',
        dataSourceId: '1426871948410556417',
        description: '二等',
        modelId: '1427203568241881089',
        modelNameZh: '二等',
      },
    ]
    this.mergeNums()
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row)
        })
      } else {
        this.$refs.multipleTable.clearSelection()
      }
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        const _row_1 = this.mergeAlgorithmRows[rowIndex]
        const _col_1 = _row_1 > 0 ? 1 : 0 //如果被合并了_row=0则它这个列需要取消
        return {
          rowspan: _row_1,
          colspan: _col_1,
        }
      }
    },
    mergeNums() {
      let modelTable = this.modelTable
      let start = 0 //源数据合并行的开始
      let count = 1 //合并行数统计
      for (let index = 1; index < modelTable.length; index++) {
        this.mergeAlgorithmRows[index] = 0
        if (modelTable[index].algorithmId === modelTable[index - 1].algorithmId) {
          count++
        } else {
          this.mergeAlgorithmRows[start] = count
          count = 1
          start = index
        }
      }
      this.mergeAlgorithmRows[start] = count
    },
    choseModel(row) {
      this.multipleSelection = []
      for (let index = 0; index < this.modelTable.length; index++) {
        const element = this.modelTable[index]
        if (element.checked === true) {
          this.multipleSelection.push(row)
        }
      }
      //临时存储选择的模型
      console.log(store)
      store.commit('setChosedModel', this.multipleSelection)
    },
  },
}
</script>
<style scoped>
</style>