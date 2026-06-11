<template>
  <div style="height: 800px" class="weekPlan">
    <el-container>
      <el-aside width="330px">
        <el-tree :data="list" :props="defaultProps" accordion @node-click="handleNodeClick"></el-tree>
      </el-aside>
      <el-container style="padding-left: 5px">
        <!-- 排产日历 -->
        <span class="orderList_title" style="margin-top: 0px">周排产量</span>
        <el-main>
          <div class="weeks">
            <el-card class="box-card" v-for="(item, index) in weekData" :key="item.order">
              <div slot="header" class="clearfix">
                <span>
                  第{{ item.week }}周
                  <span v-if="item.order == 2">(本周)</span>
                </span>
              </div>
              <a :class="{ weekNum: true, 'is-disabled': !dataSelectFlag }">
                <div @click="clickWeek(item, index)">周排产计划：{{ item.amount }}</div>
              </a>
            </el-card>
          </div>
          <el-row :gutter="20">
            <el-col :span="15">
              <div class="stock" style="margin-top: 40px">
                <span class="orderList_title">物料库存结余</span>
                <div style="margin-top: 20px">
                  <el-table :data="stockData" style="width: 100%">
                    <el-table-column prop="material" label="标识解析"></el-table-column>
                    <el-table-column prop="materialName" label="物料名称"></el-table-column>
                    <el-table-column prop="stock" label="库存"></el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
            <el-col :span="5">
              <!-- 仓库信息 -->
              <div class="wms" style="margin-top: 40px">
                <span class="orderList_title">产品仓库信息</span>
                <div style="margin-top: 20px">
                  <el-table :data="wareData" style="width: 100%">
                    <el-table-column prop="week" label="名称"></el-table-column>
                    <el-table-column prop="num" label="数量"></el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-main>
        <el-footer></el-footer>
      </el-container>
    </el-container>
    <el-dialog width="30%" title="请填写周排产量" :visible.sync="innerVisible" append-to-body @close="handleCancel">
      <el-input v-model="inputNum"></el-input>
      <el-button type="primary" @click="addWeek()" style="position: relative; top: 10px; left: 330px">确定</el-button>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { o_postAction, o_getAction, o_putAction } from '@/api/onApi.js'
import { w_getAction, w_postAction } from '@/api/wmsApi.js'
import { erp_getAction, erp_postAction } from '@/api/erpApi.js'
export default {
  name: 'oderList',
  data() {
    return {
      list: [],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      prod_id: '', //当前目录选中的标识解析
      wareData: [],
      stockData: [],
      weekData: [], //获取当前周数及其日期
      innerVisible: false,
      inputNum: '',
      selectWeek: '', //选中更改的周数
      startDate: '',
      endDate: '',
      dataSelectFlag: false, //判断左边树是否点击
      codeNum: '',
    }
  },
  mounted() {
    this.getWeeklyTree()
    this.getWeeks()
  },
  updated(){
    $('.ant-message').css('z-index', '9999999')
  },
  methods: {
    //获得当前周的附近四周及其日期
    getWeeks() {
      erp_getAction('/schedule/weeks')
        .then((res) => {
          this.weekData = res.result
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //获得左侧树形目录得数据；
    getWeeklyTree() {
      erp_getAction('/schedule/weeklyTree',{
        exampleId: this.$store.state.exampleId
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
      //仓库信息
      if (data.children || data.label.indexOf('(') != -1) {
        //判断是否是第二级目录，第二级别目录才调取函数
        this.getWareData(data.code)
        this.getWeekDate(data.code)
        this.getStockData(data.code)
        this.prod_id = data.code
        this.dataSelectFlag = true
        this.codeNum = data.label.split('(')[1].split(')')[0]
      }
      // //此处可能有bug
      // if (data.children && data.children[0].children) {
      //   //一级目录
      //   this.wareData = []
      // }
    },
    getWareData(code) {
      let params = {
        modelId: code,
      }
      erp_getAction('/schedule/warehouse', params)
        .then((res) => {
          if (res.success) {
            this.wareData = res.result
          } else {
            this.$message.error(res.message)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //获得库存信息
    getStockData(code) {
      let params = {
        code: code,
      }
      erp_getAction('/schedule/rawMaterial', params)
        .then((res) => {
          if (res.success) {
            this.stockData = res.result
          } else {
            this.$message.error(res.message)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //获取已填写的周排产计划数据
    getWeekDate(code) {
      let params = { prodId: code }
      erp_getAction('/schedule/weekSchedule', params)
        .then((res) => {
          let data = res.result
          data.forEach((item) => {
            this.weekData.forEach((ele, index) => {
              if (item.week === ele.week) {
                this.$set(this.weekData[index], 'amount', item.amount)
              }
            })
          })
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    //选择周
    clickWeek(item, index) {
      if (index === 3) {
        this.innerVisible = true
        this.selectWeek = item.week
        this.startDate = item.dates[0]
        this.endDate = item.dates[6]
      } else {
        this.$message.warning('请对本周的下下周进行排产!')
      }
    },
    //弹框确认，修改绑定数据并渲染
    addWeek() {
      //调取接口，将数据存入online中
      let vm = this
      let flag = 0 //标记是新增还是编辑，0为新增，1为编辑
      for (let i = 0; i < vm.weekData.length; i++) {
        if (vm.selectWeek == vm.weekData[i].week && vm.weekData[i].amount != undefined && vm.weekData[i].amount != '') {
          flag = 1
        }
      }
      if (this.prod_id == '') {
        this.$message.warning('请在左侧选择标识解析！')
      } else if (Number(this.inputNum) > Number(this.codeNum)) {
        this.$message.warning('超过需要排产量!')
      } else {
        let params = {
          week: this.selectWeek,
          amount: this.weekData[this.weekData.length - 1].amount
            ? Number(this.inputNum) + this.weekData[this.weekData.length - 1].amount
            : Number(this.inputNum),
          prodId: this.prod_id,
          startDate: this.startDate,
          endDate: this.endDate,
        }
        if (flag == 1) {
          let paramsData = {
            pageSize: 10,
            pageNo: 1,
            column: 'createTime',
            order: 'desc',
          }
          paramsData.superQueryMatchType = 'and'
          paramsData.superQueryParams = escape(
            JSON.stringify([
              {
                rule: 'eq',
                type: 'date',
                val: this.selectWeek,
                field: 'week',
              },
            ])
          )
          o_getAction(`cgform/api/getData/2170a40b71b84d4bba86de555bd4b178`, paramsData)
            .then((res) => {
              params.id = res.result.records[0].id //获得该条编辑数据的ID
              erp_postAction('/schedule/weekSchedule', params)
                .then((res) => {
                  console.log('res', res)
                  this.getWeekDate(vm.prod_id) //重新获得数据并渲染
                  this.getWeeklyTree()
                  this.innerVisible = false
                  this.inputNum = '' //初始化填入的排产量
                })
                .catch((err) => {
                  console.log('err', err)
                })
            })
            .catch((err) => {
              console.log(err)
            })
        } else {
          erp_postAction('/schedule/weekSchedule', params)
            .then((res) => {
              console.log('res', res)
              this.getWeekDate(vm.prod_id) //重新获得数据并渲染
              this.getWeeklyTree()
              this.innerVisible = false
              this.inputNum = '' //初始化填入的排产量
            })
            .catch((err) => {
              console.log('err', err)
            })
        }
      }
    },
    handleCancel() {
      this.innerVisible = false
      this.inputNum = ''
    },
  },
}
</script>

<style scoped>
.weekPlan .el-container .el-tree {
  height: 100%;
}
.orderList_title {
  font-size: 1.5em;
  width: 100%;
  background-color: #fff;
  text-align: center;
  margin-top: 40px;
}
.el-aside {
  color: #333;
  text-align: center;
  height: 100%;
}
.el-container {
  height: 105%;
}
.el-container .el-tree {
  height: 35%;
}
.el-main {
  background-color: #fff;
  color: #333;
  text-align: center;
  line-height: 13px;
  padding: 0px !important;
}

.wms {
  width: 330px;
  height: 30%;
  background-color: #fff;
}
.weeks {
  margin-top: 20px;
}
.el-card {
  display: inline-block;
  width: 25%;
}
.stock .cell {
  text-align: center;
}
.el-row {
  margin-left: 0px !important;
  margin-right: 0px !important;
}
.is-disabled {
  pointer-events: none;
  cursor: default;
}
</style>