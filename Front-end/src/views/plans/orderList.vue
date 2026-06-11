<template>
  <div style="height: 800px" class="orderList">
    <el-container>
      <el-aside width="330px">
        <el-tree :data="list" :props="defaultProps" accordion @node-click="handleNodeClick"></el-tree>

        <!-- 订单详情 -->
        <div class="orderDetail">
          <span class="orderList_title">订单详情</span>
          <div class="detailForm">
            <el-form label-width="80px" :model="orderDetail">
              <!-- <el-form-item label="客户名称">
                <el-input v-model="orderDetail.receiver_name"></el-input>
              </el-form-item>-->
              <el-form-item label="计划日期">
                <el-input v-model="orderDetail.plan_date"></el-input>
              </el-form-item>
              <el-form-item label="数量">
                <el-input v-model="orderDetail.orderNum"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-aside>
      <el-container style="padding-left: 5px">
        <!-- 排产日历 -->
        <span class="orderList_title">日排产量</span>
        <el-button class="weekButton" @click="dialogVisible = true">点击查看周排产信息</el-button>
        <el-main>
          <calendar-plan
            ref="calendarPlan"
            :selectCapa="selectCapa"
            :code="code"
            :codeNum="codeNum"
            @event1="change"
            @getCapacity="getCapacity()"
            @getTree="getTree"
            @handleOk="handleOk"
          ></calendar-plan>

          <div class="plinecapacity">
            <span class="orderList_title">产线产能</span>
            <div class="detailTable">
              <el-table
                ref="multipleTable"
                :data="capacityData"
                tooltip-effect="dark"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                @select="selectChange"
                @row-click="handleRowClick"
                @select-all="selectAll"
              >
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="olinename" label="产线名称" width="180"></el-table-column>
                <el-table-column prop="factory" label="所属工厂" width="180"></el-table-column>
                <el-table-column prop="plinecapacity" label="总产能" width="180"></el-table-column>
                <el-table-column prop="surplus_capacity" label="剩余产能" width="180"></el-table-column>
                <el-table-column prop="materiel" label="物料" min-width="180">
                  <template slot-scope="scope">
                      <el-popover trigger="hover" placement="top"  width="300" :disabled="scope.row.materiel.length <= 110">
                          <p>{{scope.row.materiel}}</p>
                          <div slot="reference" style="width:100%;padding: 0px;display: -webkit-box;-webkit-line-clamp:3;overflow: hidden;text-overflow: ellipsis;-webkit-box-orient: vertical;white-space: normal;line-height:18px;">
                            <span class="ellipsis">{{scope.row.materiel}}</span>
                          </div>
                      </el-popover>
                  </template>
                  <!-- <template slot-scope="{row}">
                    <el-tooltip effect="dark" :content="row.roleNameList" placement="top">
                      <div
                        style="width:100%;padding: 0px;display: -webkit-box;-webkit-line-clamp:3;overflow: hidden;text-overflow: ellipsis;-webkit-box-orient: vertical;white-space: normal;line-height:18px;"
                      >{{row.materiel}}</div>
                    </el-tooltip>
                  </template> -->
                </el-table-column>
                <el-table-column prop="materiel_name" label="物料名称" min-width="180">
                  <template slot-scope="scope">
                      <el-popover trigger="hover" placement="top"  width="300" :disabled="scope.row.materiel_name.length <= 80">
                          <p>{{scope.row.materiel_name}}</p>
                          <div slot="reference" style="width:100%;padding: 0px;display: -webkit-box;-webkit-line-clamp:3;overflow: hidden;text-overflow: ellipsis;-webkit-box-orient: vertical;white-space: normal;line-height:18px;">
                            <span>{{scope.row.materiel_name}}</span>
                          </div>
                      </el-popover>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-main>
        <el-footer></el-footer>
      </el-container>
    </el-container>
    <el-dialog title="周排产" :visible.sync="dialogVisible" width="50%">
      <div class="weekInfo">
        <el-table :data="weekData" style="width: 100%">
          <el-table-column prop="prod_id" label="标识解析"></el-table-column>
          <el-table-column prop="week" label="周数" width="80px"></el-table-column>
          <el-table-column prop="start_date" label="开始日期"></el-table-column>
          <el-table-column prop="end_date" label="结束日期"></el-table-column>
          <el-table-column prop="amount" label="周排产数量"></el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import calendarPlan from './plan'
import { o_getAction } from '@/api/onApi.js'
import { getAction } from '@/api/manage'
import { erp_getAction } from '@/api/erpApi.js'
export default {
  name: 'oderList',
  components: { calendarPlan },
  data() {
    return {
      list: [],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      orderDetail: {},
      weekData: [],
      capacityData: [],
      selectCapa: [],
      excesscapacity: [],
      dialogVisible: false,
      code: '',
      codeNum: 0,
    }
  },
  mounted() {
    this.getTree()
  },
  methods: {
    change(data) {
      this.capacityData.splice(0, 1, data)
    },
    //获得左侧树形目录得数据；
    getTree() {
      erp_getAction('/schedule/dailyTree',{
        exampleId:this.$store.state.exampleId
      })
        .then((res) => {
          let list = res.result
          this.list = list
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //点击左侧树型栏，数据联动处理
    handleNodeClick(data) {
      let vm = this
      //订单详情
      vm.orderDetail = {}
      this.getWeekData()
      this.getCapacity()
      if (data.children || data.label.indexOf('(') != -1) {
        //判断是否是第二级目录，第二级别目录才调取函数
        this.code = data.code
        this.codeNum = data.label.split('(')[1].split(')')[0]
        this.$refs.calendarPlan.getResDate(data.code)
      } else {
        o_getAction('/cgform/api/form/e8c9f23b81d74fc6a292c436d27faa70/' + data.code)
          .then((res) => {
            vm.orderDetail = {
              receiver_name: res.result.receiver_name,
              plan_date: res.result.plan_date,
              orderNum: res.result.cict_order_detail[0].prod_amount,
            }
          })
          .catch((err) => {
            console.log(err)
          })
      }
    },
    getWeekData() {
      let params = {
        pageSize: 10,
        pageNo: 1,
        column: 'createTime',
        order: 'desc',
      }
      params.superQueryMatchType = 'and'
      params.superQueryParams = escape(
        JSON.stringify([
          {
            rule: 'eq',
            type: 'text',
            val: this.code,
            field: 'prod_id',
          },
        ])
      )
      o_getAction(`cgform/api/getData/2170a40b71b84d4bba86de555bd4b178`, params)
        .then((res) => {
          this.weekData = res.result.records
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getCapacity() {
      let params = {
        pageSize: 10,
        pageNo: 1,
        column: 'createTime',
        order: 'desc',
      }
      params.superQueryMatchType = 'and'
      params.superQueryParams = escape(
        JSON.stringify([
          {
            rule: 'like',
            type: 'popup',
            popup: {
              code: 'model_identity',
              field: 'identity',
              orgFields: 'identity',
              destFields: 'materiel',
            },
            val: this.code,
            field: 'materiel',
          },
        ])
      )
      getAction(`/online/cgform/api/getData/19809bc666f34c6c841828f75e2376a3`, params)
        .then((res) => {
          this.capacityData = res.result.records
        })
        .catch((err) => {
          console.log(err)
        })
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
    handleSelectionChange(val) {
      this.selectCapa = val
    },
    selectChange(selection, row) {
      if (selection.length > 1) {
        const del_row = selection.shift()
        this.$refs.multipleTable.toggleRowSelection(del_row, false)
      }
    },
    // 点击行触发，选中或不选中复选框
    handleRowClick(row, column, event) {
      this.$refs.multipleTable.clearSelection()
      this.$refs.multipleTable.toggleRowSelection(row)
    },
    selectAll(selection) {
      if (selection.length > 1) {
        selection.length = 1
      }
    },
    handleOk() {
      console.log('触发了')
      this.capacityData = []
    },
  },
}
</script>

<style scoped>
.orderList_title {
  font-size: 1.5em;
  width: 100%;
  background-color: #fff;
  text-align: center;
}
.el-aside {
  color: #333;
  text-align: center;
  height: 100%;
}
.el-container {
  height: 105.6%;
}
.el-container .el-tree {
  height: 50%;
  overflow: scroll;
}
.el-main {
  background-color: #fff;
  color: #333;
  text-align: center;
  line-height: 13px;
  padding: 0px !important;
}
.orderDetail {
  width: 330px;
  height: 48%;
  display: inline-block;
  background-color: #fff;
  margin-top: 5px;
  border: 1px solid #0000000f;
  border-radius: 5px;
}
.wms {
  width: 310px;
  height: 57%;
  background-color: #fff;
}
.detailForm {
  width: 98%;
  margin-top: 10px;
}
.detailTable {
  margin-top: 15px;
}
.weekButton {
  position: relative;
  height: 20px;
  font-size: 13px;
  line-height: 0px;
  text-align: center;
}
</style>
<style>
.el-tooltip__popper{
  max-width:20%
}
.el-popover{
  height: 150px;
  overflow: auto;
}
</style>