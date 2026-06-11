<template>
  <div>
    <div>
      <a-row>
        <a-col :span="10"> </a-col>
        <a-col :span="4">
          <el-upload
            class="upload-demo"
            :data="{ username: 'admin' }"
            :action="importFileUrl"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-success="handleSuccess"
            :limit="1"
            :on-exceed="handleExceed"
            :beforeUpload="beforeAvatarUpload"
            :file-list="fileList"
          >
            <el-button size="small" type="primary">点击上传数据文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传1个文件,且不超过100M</div>
          </el-upload>
        </a-col>
        <a-col :span="10"> </a-col>
      </a-row>
    </div>
    <!-- <div style="">
      <el-table
        :data="predictDataTable"
        :span-method="objectSpanMethod"
        border
        height="550"
        v-show="predictDataTableShow"
      >
        <el-table-column prop="sourceName" label="数据集"> </el-table-column>
        <el-table-column prop="groupName" label="分组"> </el-table-column>
        <el-table-column prop="splitName" label="分片"> </el-table-column>
        <el-table-column prop="weight" label="比重"> </el-table-column>
        <el-table-column prop="description" label="描述"> </el-table-column>
        <el-table-column prop="createTime" label="创建时间"> </el-table-column>
        <el-table-column label="选择">
          <template slot-scope="scope">
            <el-checkbox v-model="predictDataTable[scope.$index].checked" @change="choseSplit(scope.row)"></el-checkbox>
          </template>
        </el-table-column>
      </el-table>
    </div> -->
  </div>
</template>

<script>
import { axios } from '../../../utils/request'
import store from '@/store/store'
import api from '@/api/index'
let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
export default {
  data() {
    return {
      importFileUrl: api.data_baseURL + '/data/importFile',
      fileList: [],
      predictDataTable: [
        // {
        //   splitId:'1',
        //   sourceId:'001',
        //   groupId: '001',
        //   sourceName: '数据1',
        //   groupName: '组1',
        //   splitName: 'a',
        //   weight: '75%',
        //   description: '用于训练',
        //   createTime: '2021-06-05 21:30:26'
        // },
      ],
      mergeSourceRows: [], //合并方法,源数据集合并的行， 1-5行合并，6-7行合并
      mergeGroupRows: [],
      multipleSelection: [],
      predictDataTableShow: false,
    }
  },
  mounted() {
    // let modelPredict = store.getters.getModelPredict
    // let paramForm = {
    //   sourceId: modelPredict.chosedModel[0].dataSourceId,
    // }
    // // axios.get(this.BIGDATA_BASE_URL + "/data/getAllSplitData")
    // axios({
    //   url: '/data/getSplitDataBySourceId',
    //   method: 'get',
    //   baseURL: 'http://31789g74t4.wicp.vip/',
    //   params: paramForm,
    // }).then((res) => {
    //   this.predictDataTable = res.data
    //   this.mergeRowNums()
    //   this.predictDataTableShow = true
    // })
  },
  methods: {
    handleSuccess(fileList) {
      var fileId = JSON.parse(fileList.data).fileId
      store.commit('setFileId', fileId)
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 100
      if (!isLt2M) {
        this.$message({
          message: '上传文件大小不能超过 100MB!',
          type: 'error',
        })
      }
      return isLt2M
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleExceed(files, fileList) {
      console.log(fileList)
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`
      )
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row)
        })
      } else {
        this.$refs.multipleTable.clearSelection()
      }
    },
    choseSplit(row) {
      this.multipleSelection = []
      for (let index = 0; index < this.predictDataTable.length; index++) {
        const element = this.predictDataTable[index]
        if (element.checked == true) {
          this.multipleSelection.push(row)
        }
      }
      //临时存储选择的数据集
      store.commit('setChosedPredictData', this.multipleSelection)
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        const _row_1 = this.mergeSourceRows[rowIndex]
        const _col_1 = _row_1 > 0 ? 1 : 0 //如果被合并了_row=0则它这个列需要取消
        return {
          rowspan: _row_1,
          colspan: _col_1,
        }
      } else if (columnIndex === 1) {
        const _row_1 = this.mergeGroupRows[rowIndex]
        const _col_1 = _row_1 > 0 ? 1 : 0 //如果被合并了_row=0则它这个列需要取消
        return {
          rowspan: _row_1,
          colspan: _col_1,
        }
      }
    },
    mergeRowNums() {
      let dataSplit = this.predictDataTable
      let sourceStart = 0 //源数据合并行的开始
      let groupStart = 0 //分组合并行的开始
      let mergeSourceCount = 1 //合并行数统计
      let mergeGroupCount = 1
      for (let index = 1; index < dataSplit.length; index++) {
        this.mergeSourceRows[index] = 0 //虽然从1开始，但是start是从0开始，所以0位置无需填充
        this.mergeGroupRows[index] = 0 //虽然从1开始，但是start是从0开始，所以0位置无需填充
        if (dataSplit[index].sourceId === dataSplit[index - 1].sourceId) {
          mergeSourceCount++
          //当前数据源切片分组合并行
          if (dataSplit[index].groupId === dataSplit[index - 1].groupId) {
            mergeGroupCount++
          } else {
            this.mergeGroupRows[groupStart] = mergeGroupCount //切片分组合并行数
            groupStart = index
            mergeGroupCount = 1
          }
        } else {
          //切换数据源
          this.mergeSourceRows[sourceStart] = mergeSourceCount //元数据合并行数
          sourceStart = index
          mergeSourceCount = 1
          this.mergeGroupRows[groupStart] = mergeGroupCount //切片分组合并行数
          groupStart = index
          mergeGroupCount = 1
        }
      }
      this.mergeSourceRows[sourceStart] = mergeSourceCount
      this.mergeGroupRows[groupStart] = mergeGroupCount //切片分组合并行数
    },
  },
}
</script>