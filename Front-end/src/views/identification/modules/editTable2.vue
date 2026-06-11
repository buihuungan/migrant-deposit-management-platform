<template>
  <div>
    <el-table size="mini" :data="master_user.data" border>
      <el-table-column type="index" width="60" :resizable="false"></el-table-column>

      <el-table-column
        :resizable="false"
        v-for="(v, index) in master_user.columns"
        :prop="v.field"
        :label="v.title"
        :width="v.width"
        :key="index"
      >

      </el-table-column>

      <el-table-column label="属性值" width="230" :resizable="false">
        <template slot-scope="scope">
          <el-input size="mini" v-model="scope.row.paramValue"></el-input>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from 'axios'
import { pm_postAction, pm_deleteAction, pm_getAction, pm_putAction } from '@/api/icipmApi'
export default {
  props: ['identity', 'master_userData', 'addOrEditFlag1', 'identityId', 'listData'],
  data() {
    return {
      master_user: {
        sel: null, //选中行
        columns: [
          { field: 'paramName', title: '属性名', width: 230 },
          { field: 'paramType', title: '属性类别', width: 230 },
        ],
        data: [],
      },
      tableData: [],
    }
  },
  watch:{
    'master_userData':function(){
      this.master_user.data = this.master_userData
      this.master_user.data = this.master_user.data.filter((item) => {
        return item.paramName != 'id' && item.paramName != 'identity' && item.paramName !='create_time'&& item.paramName !='update_time'
      })
    }
  },
  created() {
  },
  mounted() {},
  methods: {
    getList(listData) {
      for (let index = 0; index < listData.length; index++) {
        var data = { paramName: '', paramType: '', paramValue: '' }
        this.$set(data, 'paramName', listData[index].dbFieldName)
        this.$set(data, 'paramType', listData[index].dbFieldTxt)
        this.$set(data, 'paramValue', '')
        this.master_user.data.push(data)
      }
      this.master_user.data = this.master_user.data.filter((item) => {
        return item.paramName != 'id' && item.paramName != 'identity' && item.paramName !='create_time'&& item.paramName !='update_time'
      })
    },
    editTableStart() {
      if (this.addOrEditFlag1 == 1) {
        this.master_user.data = []
      } else if (this.addOrEditFlag1 == 2) {
      }
    },
  },
}
</script>

<style lang="less" scoped>
.cell {
  text-align: center;
}
/deep/ .ant-btn-primary {
  // border-radius: 50%;
  margin-top: 10px;
  margin-left: calc(50% - 38px);
}
</style>