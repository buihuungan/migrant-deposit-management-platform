<template>
  <div class="calendarPlan">
    <el-calendar v-model="calendarData">
      <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
      <template slot="dateCell" slot-scope="{ data }">
        <div
          :class="{
            today: data.day === todayDate,
            notToday: data.day !== todayDate,
            isnextWeek: nextWeek.includes(data.day),
            isNextWeek: nextWeek2.includes(data.day),
          }"
          @click="selectAdd(data)"
        >
          <p>{{ data.day.split('-').slice(2).join('-') }}</p>
          <p>日排产量：{{ dealMyDate(data.day) }}</p>
        </div>
      </template>
    </el-calendar>
    <el-dialog width="30%" title="请填写日排产量" :visible.sync="innerVisible" append-to-body>
      <span v-if="this.capaData.length != 0" style="position: relative; top: -21px"
        >当前选择产线名称 {{ selectCapa[0].olinename }} ，此产能剩余产能为：{{ selectCapa[0].surplus_capacity }}</span
      >
      <el-form  label-width="100px">
        <el-form-item label="排产量:">
          <el-input v-model="inputNum" style="width: 240px"></el-input>
        </el-form-item>

      </el-form>
        <el-button style="margin-left:40%" type="primary" @click="addNumber()">确定</el-button>
      </el-row>
    </el-dialog>
    <!-- <addAndEditPDS ref="modalform"></addAndEditPDS> -->
    <!-- 调采购需求单新增页面，数据处理较为麻烦，建议后期需求重新写一个页面，此处该功能直接调新增接口增加数据，不显示页面 -->
  </div>
</template>
<script>
// import addAndEditPDS from '../procurementPlan/modules/addAndEditPDS.vue'
import { o_postAction, o_getAction, o_putAction } from '@/api/onApi.js'
import { erp_getAction, erp_putAction, erp_postAction } from '@/api/erpApi.js'
import { w_getAction } from '@api/wmsApi'
import { putAction } from '@api/manage'

export default {
  name: 'calendarPlan',
  // components: {
  //   addAndEditPDS,
  // },
  props: ['selectCapa', 'code', 'codeNum'], //"code":点击的左侧的标识解析编码
  watch: {
    selectCapa(newV, oldV) {
      //当前所选择产能
      console.log("产能newV", newV);
      this.capaData = newV
    },
  },
  data() {
    return {

      onlineId: '597983d3ade2449aa91338e05148332a', //采购需求单
      url: {
        rule: {
          orderNum: '/sys/fillRule/executeRuleByCode/purchase_order_autoCode',
        },
      },
      res1: {}, //存判断物料充足接口返回数据
      resDate: [],
      calendarData: new Date(),
      innerVisible: false,
      inputNum: '',
      selectData: {},
      capaData: [],
      selDate: '', //记录选中的日期
      num: '', //标记选中的这边是否已经有排产计划
      nextWeek: [],
      nextWeek2: [],
      weekDate: [], //可填写日期
      todayDate: '',
    }
  },
  mounted() {
    this.todayDate = new Date().toISOString().substring(0, 10)
    //为当前的下一周添加背景色
    $('.is-today').parent().next().css('background-color', '#e6f7ff')
    //为当前的下一周添加背景色
    $('.is-today').parent().next().next().css('background-color', 'rgb(204, 241, 247)')
    this.changeNextColor()
    //获得当前时间下一周日期数组
    this.getNextWeek2()
    //初始化数据

    $('.el-calendar__title').after($('.weekButton'))
  },
  updated() {
    $('.ant-message').css('z-index', '9999999')
    this.$nextTick(() => {
      $('.current').removeClass('is-selected')
      $('.is-today').addClass('is-selected')

      $('.current').removeClass('nextWeek')
      $('.current').removeClass('nextWeek2')
      $('.next').removeClass('nextWeek')
      $('.next').removeClass('nextWeek2')
      $('.prev').removeClass('nextWeek')
      $('.prev').removeClass('nextWeek2')
      $('.isnextWeek').parent().parent().addClass('nextWeek')
      $('.isNextWeek').parent().parent().addClass('nextWeek2')
    })
  },
  methods: {
    changeNextColor() {
      this.$nextTick(() => {
        // 点击前一个月
        let prevBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:nth-child(1)')
        let todayBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:nth-child(2)')
        let nextBtn = document.querySelector('.el-calendar__button-group .el-button-group>button:nth-child(3)')
        prevBtn.addEventListener('click', () => {
          $('.notToday').parent().parent().removeClass('is-today')
          $('.today').parent().parent().addClass('is-today')
          this.changeColor()
        })
        todayBtn.addEventListener('click', () => {
          $('.notToday').parent().parent().removeClass('is-today')
          this.changeColor()
        })
        nextBtn.addEventListener('click', () => {
          $('.notToday').parent().parent().removeClass('is-today')
          $('.today').parent().parent().addClass('is-today')
          this.changeColor()
        })
      })
    },
    //渲染背景色
    changeColor() {
      // $('.is-today p').css("color",'#409EFF')
      console.log('this.todayDate', this.todayDate)
      $('.el-calendar-day').parent().parent().css('background-color', '#fff')
      $('.is-today').parent().next().css('background-color', '#e6f7ff')
      $('.is-today').parent().next().next().css('background-color', 'rgb(204, 241, 247)')
    },
    //获得初始化数据
    getResDate(code) {
      return new Promise((resolve, reject) => {
        let params = {
          prodId: code,
        }
        erp_getAction('/schedule/dailySchedule', params)
          .then((res) => {
            let data = res.result
            for (let i = 0; i < data.length; i++) {
              data[i].create_date = data[i].schedulingDate
            }
            this.resDate = data
            resolve(this.resDate)
          })
          .catch((err) => {
            console.log('err', err)
          })
      })
    },
    //获得下下一周日期范围
    getNextWeek2() {
      erp_getAction('/schedule/weeks')
        .then((res) => {
          this.nextWeek = res.result[2].dates
          this.nextWeek2 = res.result[3].dates
        })
        .catch((err) => {
          console.log(err)
        })
    },
    //添加日排产量数据渲染
    dealMyDate(v) {
      let len = this.resDate.length
      let res = ''
      let num = 0
      for (let i = 0; i < len; i++) {
        if (this.resDate[i].create_date == v) {
          num += this.resDate[i].amount - 0
        }
      }
      res = num.toString()
      return res
    },
    selectAdd1(data) {
      let len = this.resDate.length
      let num = 0
      for (let i = 0; i < len; i++) {
        if (this.resDate[i].create_date == data.day) {
          num += this.resDate[i].prod_amount - 0
        }
      }
      this.$emit('event1', this.selectCapa[0])
    },

    getOrderNum() {
      var model = { prefix: '' }
      return putAction(this.url.rule.orderNum, model).then((res) => {
        if (res.success) {
          return res.result
        }
      })
    },

    //点击更改数据
    selectAdd(data) {
      let flag = 0 //标记选中的日期是否为下下一周的日期，1是  0不是
      for (let i = 0; i < this.nextWeek2.length; i++) {
        if (data.day == this.nextWeek2[i]) flag = 1
      }
      if (flag == 1) {
        this.innerVisible = true
        this.selectData = data
        this.num = this.dealMyDate(this.selectData.day) //得到当前日期的排产数量
      } else {
        this.$message.warning('请对当前周的下下一周做排产计划！')
      }
    },

    //弹框确认，修改绑定数据并渲染
    addNumber() {
      var that = this
      //调取接口，将数据存入online中
      if (this.inputNum == '') {
        this.$message.warning('请填写日排产量！')
      } else if (this.capaData.length == 0) {
        this.$message.warning('请选择产能产线！')
      } else {
        if (this.selectCapa[0].surplus_capacity < this.inputNum) {
          this.$message.warning('排产量大于所选择产线的剩余产能，请重新排产！')
        } else if (Number(this.inputNum) > Number(this.codeNum)) {
          this.$message.warning('超过需要排产量!')
        } else {
          let prodName = this.capaData[0].materiel_name.split(',')[this.capaData[0].materiel.split(',').indexOf(this.code)]
          if (!prodName) {
            prodName = ''//产品名称获取失败
          }

          let params = {
            schedulingDate: this.selectData.day,
            amount: Number(this.inputNum),
            productionLineId: this.capaData[0].olinename,
            prodId: this.code,
            prodName: this.capaData[0].materiel_name.split(',')[this.capaData[0].materiel.split(',').indexOf(this.code)],
            exampleId: this.$store.state.exampleId
          }
          //判断物料是否充足
          let params1 = {
            identity: this.code,
            num: Number(this.inputNum),
            // num: 1000000,//用作测试物料不够情况
          }
          w_getAction('/material/enough', params1).then((res) => {
            this.res1 = res
            if (res.result.enough) {
              //物料充足，正常排产
              erp_postAction('/schedule/dailySchedule', params).then((res) => {
                that.getResDate(that.capaData[0].materiel).then((data) => {
                  that.innerVisible = false
                  that.inputNum = '' //初始化填入的排产量
                  let data1 = {}
                  data1.day = params.create_date
                  that.selectAdd1(data1)
                }) //重新获得数据并渲染日历
                //刷新左侧数据
                that.$emit('getTree')
                //修改剩余产能
                let surplusCapacityModel = {
                  lineId: that.capaData[0].id,
                  scheduling: that.inputNum,
                }
                erp_putAction('/schedule/surplusCapacity', surplusCapacityModel)
                  .then((res) => {
                    // this.$emit('getCapacity')
                    this.$emit('handleOk')
                  })
                  .catch((err) => {
                    console.log(err)
                  })
                // console.log('a', this.codeNum);
                // console.log('b', this.inputNum);
                //物料产完，删除物料
                if (Number(this.codeNum) == Number(this.inputNum)) {
                  erp_postAction(`/productionLine/delMateriel?materiel=${this.code}&onlineName=${this.selectCapa[0].olinename}`)
                    .then((res) => {
                      console.log('aaa', this.code);
                    })
                }
              })
            } else {
              //物料不足，需要新增采购需求单
              this.$elementConfirm('排产的标识所需物料不足，是否自动下达物料采购单?', '物料不足提示', {
                confirmButtonText: '下达',
                cancelButtonText: '取消',
                type: 'warning',
              })
                .then(() => {
                  this.innerVisible = false
                  var cgxqddata = {}
                  this.getOrderNum().then((res) => {
                    cgxqddata.order_identity = res
                    cgxqddata.purchase_order_material_details = this.res1.result.demandMaterials
                    cgxqddata.example_id = this.$store.state.exampleId
                    //需要传入数据为order_identity purchase_order_material_details:[identity,model,name,number,unit]
                    //排产点击数量时，物料不够需要根据后台返回结果去采购需求单里面采购物料。
                    o_postAction('/cgform/api/form/' + this.onlineId, cgxqddata)
                      .then((res) => {
                        this.$message.success('新增成功')
                        this.$emit('ok')
                      })
                      .catch((err) => {
                        this.$message.error('新增失败')
                        console.log(err)
                      })
                  })
                  //物料不足自动下采购单后也要下达生产任务
                  erp_postAction('/schedule/dailySchedule', params).then((res) => {
                    that.getResDate(that.capaData[0].materiel).then((data) => {
                      that.innerVisible = false
                      that.inputNum = '' //初始化填入的排产量
                      let data1 = {}
                      data1.day = params.create_date
                      that.selectAdd1(data1)
                    }) //重新获得数据并渲染日历
                    //刷新左侧数据
                    that.$emit('getTree')
                    //修改剩余产能
                    let surplusCapacityModel = {
                      lineId: that.capaData[0].id,
                      scheduling: that.inputNum,
                    }
                    erp_putAction('/schedule/surplusCapacity', surplusCapacityModel)
                      .then((res) => {
                        // this.$emit('getCapacity')
                        this.$emit('handleOk')
                      })
                      .catch((err) => {
                        console.log(err)
                      })
                    // console.log('a', this.codeNum);
                    // console.log('b', this.inputNum);
                    //物料产完，删除物料
                    if (Number(this.codeNum) == Number(this.inputNum)) {
                      erp_postAction(`/productionLine/delMateriel?materiel=${this.code}&onlineName=${this.selectCapa[0].olinename}`)
                        .then((res) => {
                          console.log('aaa', this.code);
                        })
                    }
                  })
                })
                .catch((e) => {
                  console.log('点击取消 e', e)
                })
            }
          })
        }
      }
    },
  },
}
</script>

<style lang="less" scoped>
.calendarPlan .el-calendar-table .el-calendar-day {
  height: 65px !important;
}
/deep/ .el-calendar-table:not(.is-range) td.next,
/deep/ .el-calendar-table:not(.is-range) td.prev {
  pointer-events: none;
}
/deep/ .is-today {
  color: #409eff !important;
}
/deep/ .el-calendar__title {
  text-align: left;
  width: 15%;
}
/deep/ .nextWeek {
  background-color: #e6f7ff;
}
/deep/ .nextWeek2 {
  background-color: rgb(204, 241, 247);
}
</style>

