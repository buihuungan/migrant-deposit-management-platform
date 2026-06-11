<template>
  <div class="productTraceability_sheep">
    <div class="table-page-search-wrapper" style="margin-left: 8px">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="销售订单标识编码：">
              <a-input placeholder="请输入销售订单标识编码" v-model="searchIdentity"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="2">
            <a-form-item>
              <a-button type="primary" @click="searchQuery">查询</a-button>
            </a-form-item>
          </a-col>
           <a-col :span="6">
            <a-form-item label="产品标识编码：">
              <a-input placeholder="请输入产品标识编码" v-model="searchIdentity"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="2">
            <a-form-item>
              <a-button type="primary" @click="searchQuery">查询</a-button>
            </a-form-item>
          </a-col>
        </a-row>
         
      </a-form>
    </div>
    <div id="main" style="width: 1200px; height: 700px"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import qs from 'qs'
import axios from "axios";
import $ from 'jquery'
import { im_postAction} from '@/api/icimApi'
import { o_putAction, o_getAction } from '../../api/onApi'
import { order_getAction } from '@/api/orderApi'
import { getAction } from '@/api/manage'
// import { im_postAction } from '@/api/icimApi'


export default {
  data() {
    return {
      searchIdentity: '',  //查询销售订单标识
      salesOrderIdData: [], //用来存销售订单
      globalData: [], //用来存放被收起的某节点的子节点
      mychart: {},
      option: {
        title: {
          text: '产品追溯',
        },
        tootip: {
          //弹窗
        },
        color:['#B4CCD3',
            '#96ADFF',
            '#B238BC',
            '#04FFFF',
            '#60FF86'],
        series: [
          {
            type: 'graph',
            layout: 'force', //图的布局，类型为力引导图
            coordinateSystem: null, //坐标系可选
            xAxisIndex: 0, //x轴坐标 有多种坐标系轴坐标选项
            yAxisIndex: 0, //y轴坐标
            force: {
                repulsion: 300,        //节点之间的斥力因子
                edgeLength: [100, 200],    //边的两个节点之间的距离
                gravity: 0.05,
                layoutAnimation: false,  //动画效果还是静态效果
            },
            legendHoverLink: true, //是否启用图例 hover(悬停) 时的联动高亮。
            roam: true, //是否开启鼠标缩放和平移漫游
            draggable: true, //节点是否可拖拽
            focusNodeAdjacency: true, //是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。
            edgeSymbol: ['none', 'arrow'], //边两端的标记类型
            symbol: 'circle',
            symbolSize: [60, 50], //图形大小
            edgeSymbolSize: 10, //边两端的标记大小，
            itemStyle: {
              //图形样式，有 normal 和 emphasis 两个状态。normal 是图形在默认状态下的样式；
              // color: '##fff2f2'
            },
            lineStyle: {
              //关系边的公用线条样式
              normal: {
                color: '#31354B',
                width: '1',
                type: 'solid', //线的类型 'solid'（实线）'dashed'（虚线）'dotted'（点线）
                curveness: 0, //线条的曲线程度，从0到1
                opacity: 1,
                // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。默认0.5
              },
              emphasis: {
                //高亮状态
              },
            },
            label: {
              //todo=============图形上的文本标签(图片名称)
              show: true, //是否显示标签
              // color: '2D2F3B'
              position: 'bottom', //标签的位置。['50%', '50%'] [x,y]   'inside'
              textStyle: {
                //标签的字体样式
                color: '#2D2F3B', //字体颜色
                fontStyle: 'normal', //文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                fontWeight: 'bolder', //'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                fontFamily: 'sans-serif', //文字的字体系列
                fontSize: 12, //字体大小
              },
              emphasis: {
                //高亮状态
              },
            },
            edgeLabel: {
              //==============线条的边缘标签
              normal: {
                show: false,
              },
              emphasis: {
                //高亮状态
              },
            },
            categories: [
              {category:0, symbolSize:60},
              {category:1, symbolSize:50},
              {category:2, symbolSize:40},
              {category:3, symbolSize:30},
              {category:4, symbolSize:20}
            ],
            data: [
              {
                name: '销售订单',
                id: 0+ '',
                category: 0,
                ignore: false,
                flag: true,
                parentId: 0+ ''
              },
              {
                name: '排产订单',
                id: 1+ '',
                category: 0,
                ignore: true,
                parentId:1+ ''
              },
              {
                name: '物料采购',
                id: 2+ '',
                category: 0,
                ignore: true,
                flag: true,
                parentId:2+ ''
              },
              {
                name: '生产任务',
                id: 3+ '',
                category: 0,
                ignore: true,
                flag: true,
                parentId:3+ ''
              },
              {
                name: '质量检测',
                id: 4+ '',
                category: 0,
                ignore: true,
                flag: true,
                parentId:4+ ''
              },
              {
                name: '发货',
                id: 5+ '',
                category: 0,
                ignore: true,
                parentId:5+ ''
              },
            ], //节点信息
            links: [
              {
                source: 0,
                target: 1,
              },
              {
                source: 1,
                target: 2,
                label: {
                    show: true,
                    formatter: '物料不足',
                    textStyle : { //标签的字体样式
                        color : '#2D2F3B', //字体颜色
                        fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                        fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                        fontFamily : 'sans-serif', //文字的字体系列
                        fontSize : 12, //字体大小
                    },
                }
              },
              {
                source: 2,
                target: 3,
                // label: {
                //     show: true,
                //     formatter: '经过一系列流程',
                //     textStyle : { //标签的字体样式
                //         color : '#2D2F3B', //字体颜色
                //         fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                //         fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                //         fontFamily : 'sans-serif', //文字的字体系列
                //         fontSize : 12, //字体大小
                //     },
                // }
              },
              {
                source: 3,
                target: 4
              },
              {
                source: 4,
                target: 5
              }
            ],
            lineStyle: {
              color: '#2f4554',
            },
          },
        ],
      },
      productData: [],
      ProductLinks: [],
    }
  },
  // mounted(){
  //   var that =this
  //   this.mychart = echarts.init(document.getElementById('main'));
  //   this.mychart.on('click', async function(params) {
  //     if (params.dataType == "node") {   //判断是否是点击节点
  //       if(params.data.flag){            //判断节点是否已展开
  //         if(params.data.name == '销售订单') //判断点击的时销售订单
  //         {
  //           console.log('ffffffffff',params.data.flag)
  //           await im_postAction(   //获取订单标识
  mounted() {
    var that = this
    this.mychart = echarts.init(document.getElementById('main'))
    this.mychart.on('click', async function (params) {
      if (params.dataType == 'node') {
        //判断是否是点击节点
        let seriesData = that.option.series[0].data
        if (params.data.flag) {
          //判断节点是否已展开
          if (params.data.name == '销售订单') {
            //判断点击的时销售订单
            console.log('ffffffffff', params.data.flag)
            await im_postAction(
              //获取订单标识
              `/identityManage/allIdentities`
            )
              .then((res) => {
                var idNum = that.option.series[0].data.length //当前已有ID数量
                for (var i = 0; i < 2; i++) {
                  var data1 = {
                    id: String(idNum),
                    name: res.data.result.list.records[i].identification,
                    category: 1,
                    flag: true,
                    salesOrderId: true,
                    parentId: 0+ ''
                  }
                  var links1 = {
                    source: params.data.id,
                    target: idNum+ '',
                    value: 50,
                    label: {
                      show: true,
                      formatter: '订单标识',
                    },
                  }
                  that.option.series[0].data.push(data1) //存入节点数据
                  that.option.series[0].links.push(links1) //存入连线数据
                  idNum = idNum + 1
                  var id = params.data.id //当前节点id
                  that.option.series[0].data[id].flag = false //flag改为false，表示该节点已展开
                }
              })
              .catch((err) => {
                console.log(err)
              })
            that.mychart.clear()
            that.mychart.setOption(that.option)
            // console.log('that.option',that.option)
          }

          if (params.data.salesOrderId) {
            //判断点击的是订单标识
            await im_postAction('/identityManage/identityResolve?identity=' + params.data.name)
              .then((res) => {
                var idNum = that.option.series[0].data.length //当前已有ID数量
                for (var keys in res.data.result.params[0]) {
                  var name = res.data.result.params[0][`${keys}`].name //取数组对象里的键值对
                  var key = res.data.result.params[0][`${keys}`].value
                  // var optionName = name + ':' + key
                  var data1 = {
                    id: String(idNum),
                    name: key,
                    category: 2,
                    parentId: params.data.id+ ''
                  }
                  var links1 = {
                    source: params.data.id,
                    target: idNum+ '',
                    label: {
                      show: true,
                      formatter: name,
                      textStyle: {
                        //标签的字体样式
                        color: '#2D2F3B', //字体颜色
                        fontStyle: 'normal', //文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                        fontWeight: 'bolder', //'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                        fontFamily: 'sans-serif', //文字的字体系列
                        fontSize: 12, //字体大小
                      },
                    },
                  }
                  that.option.series[0].data.push(data1)
                  that.option.series[0].links.push(links1)
                  idNum = idNum + 1
                  var id = params.data.id //当前节点id
                  that.option.series[0].data[id].flag = false
                  // console.log('that.optionthat.option',that.option)
                }
              })
              .catch((err) => {
                console.log(err)
              })
            that.mychart.clear()
            that.mychart.setOption(that.option)
          }

          if(params.data.name == '物料采购') //判断点击的是物料采购
          {
            await order_getAction(`/purchaseDemand/material`, {
            chooseSupplier: 3,
            pageNo: 1,
            pageSize: 10,
            })
            .then((res) => {
            console.log('rrrrrrrrrrrrrrrr',res)
            for( var i=0;i<3;i++){
              var idNum = that.option.series[0].data.length
              var data1 = {
                id: String(idNum),
                name: res.result.data[i].orderIdentity,
                category: 1,
                flag: true,
                orderId: res.result.data[i].id,
                parentId: params.data.id +''
              }
              var links1 = {
                source: params.data.id,
                target: String(idNum),
                label: {
                  show: true,
                  formatter: '采购订单标识',
                  textStyle : { //标签的字体样式
                    color : '#2D2F3B', //字体颜色
                    fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                    fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                    fontFamily : 'sans-serif', //文字的字体系列
                    fontSize : 12, //字体大小
                  },
                }
              }
              that.option.series[0].data.push(data1)
              that.option.series[0].links.push(links1)
              idNum = idNum +1
            }
            var id = params.data.id  //当前节点id
            that.option.series[0].data[id].flag = false
            that.mychart.clear()
            that.mychart.setOption(that.option)
            })
            .catch((err) => {
            console.log('err', err)
            })  
          }

          if(params.data.name.substring(0,5) == 'order') {  //判断点击的是采购订单标识
            var onlineId = '597983d3ade2449aa91338e05148332a'
            var dataId = params.data.orderId
            await o_getAction(`/cgform/api/form/${onlineId}/${dataId}`)
            .then((res) => {
              console.log('点击采购订单标识')
              var sourceId = params.data.id
              var currentId = that.option.series[0].data.length
              that.orderInformation(res, sourceId, currentId)  //渲染订单信息
            })
            .catch((err) => {
            console.log(err)
            })
          }

          if(params.data.name == '采购订单原料')   //判断点击的是采购订单原料
          {
            var sourceId = params.data.id
            console.log('lllllllllllllll',params.data.material_details)
            var material_details = params.data.material_details
            var currentId = that.option.series[0].data.length
            that.materialInformation(material_details, sourceId, currentId) //原料信息，父节点，子节点
          }

          if(params.data.materialId != null ) {    //判断点击的是具体原料，展示具体原料信息
            console.log('隔离调隔离条',params.data.materialId)
            var queryDateInfo = {}
            queryDateInfo.key = 'O3005015020821400004'
            queryDateInfo.pageNumber = 0
            queryDateInfo.pageSize = 9999
            await im_postAction('/identityManage/identitySearch', qs.stringify(queryDateInfo))
            .then((res) => {
              var record = res.data.result.list.records[0]
              console.log('dddddddddd',record)
              axios.defaults.headers.post['Content-Type'] = 'application/json'
              im_postAction('/identityManage/identityParam', JSON.stringify(record))
              .then((res) => {
                console.log("res",res);
                var materialList = res.data.result.list[0]
                var sourceId = params.data.id
                var currentId = that.option.series[0].data.length
                that.getMaterialDetail(materialList, sourceId, currentId)
              })
              .catch((err) => {
                console.log(err)
              })
            })
            .catch((err) => {
            console.log(err)
          })
          }

          //判断点击的是生产任务
          if(params.data.name == '生产任务') {
            var currentId = that.option.series[0].data.length
            var sourceId = params.data.id
            that.getProductionTask(currentId, sourceId)
          }

          //判断点击的是待发布生产任务
          if(params.data.name == '待发布生产任务') {
            var currentId = that.option.series[0].data.length
            var sourceId = params.data.id
            that.getPendingProductionTasks(currentId, sourceId)
          }
          
          //判断点击的是待发布任务的标识
          if(params.data.pendingProductionId) {
            var currentId = that.option.series[0].data.length
            var sourceId = params.data.id
            var pendingProductionData = params.data.taskInformation  //存储节点信息
            that.getPendingProductionTasksDetails(currentId, sourceId, pendingProductionData)
          }
   
          //判断点击的是已发布任务的标识
          // if(params.data) {

          // }

          //判断点击的是质量检测
          if(params.data.name ==  '质量检测') {
            var currentId = that.option.series[0].data.length
            var sourceId = params.data.id
            that.getQualityInspection(currentId, sourceId)
          }

          //判断点击的是质量检测的订单标识
          if(params.data.qualityInspectionData != null) {
            var currentId = that.option.series[0].data.length
            var sourceId = params.data.id
            var QualityInspectionData = params.data.qualityInspectionData
            console.log('tttttttttttttttttttt')
            that.getQualityInspectionData(currentId, sourceId, QualityInspectionData)
          }
        }    
        else if (params.data.flag == false) {
          console.log('进入', params)
          for (let i = that.option.series[0].data.length - 1; i >= 0; i--) {
            if(that.option.series[0].data[i].id == params.data.id){
              that.option.series[0].data[i].flag = true
            }
            if (that.option.series[0].data[i].parentId == params.data.id) {
              
              if (that.option.series[0].data[i].parentId != that.option.series[0].data[i].id) {
                for (let j = that.option.series[0].data.length - 1; j >= 0; j--) {
                  if (that.option.series[0].data[j].parentId == that.option.series[0].data[i].id) {
                    for (let k = that.option.series[0].data.length - 1; k >= 0; k--) {
                      if (that.option.series[0].data[k].parentId == that.option.series[0].data[j].id) {
                        for(let m = that.option.series[0].data.length - 1; m >= 0; m--) {
                          if(that.option.series[0].data[m].parentId == that.option.series[0].data[k].id) {
                            that.option.series[0].data.splice(m, 1)
                            that.option.series[0].links.splice(m-1, 1)
                          }
                        }
                        that.option.series[0].data.splice(k, 1)
                        that.option.series[0].links.splice(k-1, 1)
                      }
                    }
                    that.option.series[0].data.splice(j, 1)
                    that.option.series[0].links.splice(j-1, 1)
                  }
                }
                that.option.series[0].data.splice(i, 1)
                that.option.series[0].links.splice(i-1, 1)
              }
            }
          }
           that.mychart.clear()
           that.mychart.setOption(that.option)
        }
        console.log('ggggggggggg',that.option.series[0])
      }  
    })
    this.getProductTracing()
  },
  watch: {},
  methods: {
    getProductTracing() {
      this.mychart.clear()
      this.mychart.setOption(this.option)
      // console.log(this.mychart)
    },

    //渲染订单信息
    orderInformation(res, sourceId, currentId) {   
      console.log('orderInformation',res, sourceId,currentId)
      var idNum = currentId
      var linksName =['供应商标识编码', '供应商名称', '供应商评分', '零件名称', '合同名称']
      var dataName = [res.result.supplier_identity, res.result.supplier_name, res.result.supplier_rate, res.result.supplier_goods, res.result.contract_name]
      console.log('ssssssssssssss',linksName,dataName)
      for(var i=0;i<5;i++) {
        var data1 = {
          id: String(idNum),
          name: dataName[i],
          category: 2,
          flag: true,
          parentId: sourceId +''
        }
        var links1 = {
          source: sourceId,
          target: String(idNum),
          label: {
            show: true,
            formatter: linksName[i],
            textStyle : { //标签的字体样式
            color : '#2D2F3B', //字体颜色
            fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
            fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
            fontFamily : 'sans-serif', //文字的字体系列
            fontSize : 12, //字体大小
            },
          }
        }
        this.option.series[0].data.push(data1)
        this.option.series[0].links.push(links1)
        idNum = idNum +1
      }
      //原料存储信息
      var material_details = res.result.purchase_order_material_details
      var materialData = {
        id: String(idNum),
        name: '采购订单原料',
        category: 2,
        flag: true,
        material_details: material_details,
        parentId: sourceId+''
      }
      var materialLinks = {
        source: sourceId,
        target: String(idNum),
        label: {
          show: true,
          formatter: '原料',
        }
      }
      this.option.series[0].data.push(materialData)
      this.option.series[0].links.push(materialLinks)
      idNum = idNum +1
      this.option.series[0].data[sourceId].flag = false
      this.mychart.clear()
      this.mychart.setOption(this.option)
    },
    
    //渲染原料
    materialInformation(material_details, sourceId, currentId) { 
      console.log('materialInformation', material_details, sourceId, currentId)
      for(var i=0;i<material_details.length;i++) {
        var data1 = {
          id: String(currentId),
          name: material_details[i].name,
          category: 3,
          flag: true,
          materialId: material_details[i].identity,
          parentId: sourceId+''
        }
        var links1 = {
          source: sourceId,
          target: String(currentId),
        }
        this.option.series[0].data.push(data1)
        this.option.series[0].links.push(links1)
        currentId = currentId +1
      }
      this.option.series[0].data[sourceId].flag = false
      this.mychart.clear()
      this.mychart.setOption(this.option)
    },

    //渲染原料细节
    getMaterialDetail(materialList, sourceId, currentId) {
      console.log('渲染原料细节', materialList, sourceId, currentId)
      for(var keys in materialList){
        var name = materialList[`${keys}`].name   //取数组对象里的键值对
        var key = materialList[`${keys}`].value
        var data1 = {
          id: String(currentId),
          name: key,
          category: 4,
          parentId: String(sourceId)
        }
        var links1 ={
          source: String(sourceId),
          target: String(currentId),
          label: {
            show: true,
            formatter: name,
            textStyle : { //标签的字体样式
              color : '#2D2F3B', //字体颜色
              fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
              fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
              fontFamily : 'sans-serif', //文字的字体系列
              fontSize : 12, //字体大小
            },
          }
        }
        this.option.series[0].data.push(data1)
        this.option.series[0].links.push(links1)
        currentId = currentId +1
      }
      this.option.series[0].data[sourceId].flag = false
      this.mychart.clear()
      this.mychart.setOption(this.option)
    },
    
    //渲染生产任务
    getProductionTask(currentId, sourceId) {
      var name = ['待发布生产任务', '已发布生产任务']
      var linksName = ['待发布', '已发布']
      for(var i=0;i<2;i++){
        var data1 = {
          id: String(currentId),
          name: name[i],
          category: 1,
          flag: true,
          parentId:String(sourceId)
        }
        var links1 = {
          source: String(sourceId),
          target: String(currentId),
          label: {
            show: true,
            formatter: linksName[i],
            textStyle : { //标签的字体样式
              color : '#2D2F3B', //字体颜色
              fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
              fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
              fontFamily : 'sans-serif', //文字的字体系列
              fontSize : 12, //字体大小
              },
          }
        }
        this.option.series[0].data.push(data1)
        this.option.series[0].links.push(links1)
        currentId = currentId +1
      }
      this.option.series[0].data[sourceId].flag = false
      this.mychart.clear()
      this.mychart.setOption(this.option)
    },

    //渲染待发布生产任务
    getPendingProductionTasks(currentId, sourceId) {
      var params = {
        column: 'createTime',
        order: 'desc',
        pageNo: 1,
        pageSize: 10,
        superQueryMatchType: 'and'
      }
      getAction('online/cgform/api/getData/24e5e0a2c5d04bf3a0b99a9f8cccc2b4', params).then(res => {
        console.log('待发布生产任务',res)
        for(var i=0;i<2;i++) {
          var data1 = {
            id: String(currentId),
            name: res.result.records[i].prod_id,
            taskInformation: {
              prod_name: res.result.records[i].prod_name,
              scheduling_date: res.result.records[i].scheduling_date,
              amount: res.result.records[i].amount,
              production_line_id: res.result.records[i].production_line_id
            },
            category: 2,
            flag: true,
            pendingProductionId: true,
            parentId:String(sourceId)
          }
          var links1 = {
            source: String(sourceId),
            target: String(currentId),
            label: {
              show: true,
              formatter: '标识编码',
              textStyle : { //标签的字体样式
                color : '#2D2F3B', //字体颜色
                fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                fontFamily : 'sans-serif', //文字的字体系列
                fontSize : 12, //字体大小
              },
            }
          }
          this.option.series[0].data.push(data1)
          this.option.series[0].links.push(links1)
          currentId = currentId +1
        }
        this.option.series[0].data[sourceId].flag = false
        this.mychart.clear()
        this.mychart.setOption(this.option)
      })
    },

    //渲染待发布生产任务详情
    getPendingProductionTasksDetails(currentId, sourceId, pendingProductionData) {
      console.log('渲染待发布生产任务详情',currentId, sourceId, pendingProductionData)
      var links = ['名称', '排产日期', '数量', '产线']
      var names =[pendingProductionData.prod_name, pendingProductionData.scheduling_date, pendingProductionData.amount, pendingProductionData.production_line_id]
      for(var i=0;i<4;i++) {
        var data1 = {
          id: String(currentId),
          name: names[i],
          category: 3,
          parentId: String(sourceId)
        }
        var links1 = {
          source: String(sourceId),
          target: String(currentId),
          label: {
            show: true,
            formatter: links[i],
            textStyle : { //标签的字体样式
              color : '#2D2F3B', //字体颜色
              fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
              fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
              fontFamily : 'sans-serif', //文字的字体系列
              fontSize : 12, //字体大小
            },
          }
        }
        this.option.series[0].data.push(data1)
        this.option.series[0].links.push(links1)
        currentId = currentId +1
      }
      this.option.series[0].data[sourceId].flag = false
      this.mychart.clear()
      this.mychart.setOption(this.option)
    },

    //渲染质量检测信息
    getQualityInspection(currentId, sourceId) {
      let params = {
        column: 'createTime',
        order: 'desc',
        superQueryMatchType: 'and',
        pageNo: 1,
        pageSize: -521,
      }
      o_getAction('/cgform/api/getData/bba30b3a70924ac7aa65c220ee62cb30', params).then((res) => {
        console.log('渲染质量检测信息',res)
        for(var i=0;i<2;i++) {
          var data1 = {
            id: String(currentId),
            name: res.result.records[i].product_id,
            category: 1,
            qualityInspectionId: true,
            qualityInspectionData:res.result.records[i],
            flag: true,
            parentId: String(sourceId)
          }
          var links1 = {
            source: String(sourceId),
            target: String(currentId),
            label: {
              show: true,
              formatter: '质量检测标识',
              textStyle : { //标签的字体样式
                color : '#2D2F3B', //字体颜色
                fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                fontFamily : 'sans-serif', //文字的字体系列
                fontSize : 12, //字体大小
              },
            }
          }
          this.option.series[0].data.push(data1)
          this.option.series[0].links.push(links1)
          currentId = currentId +1
        }
        this.option.series[0].data[sourceId].flag = false
        this.mychart.clear()
        this.mychart.setOption(this.option)
      })
    },

    //渲染质量检测订单的信息
    getQualityInspectionData(currentId, sourceId, QualityInspectionData) {
      console.log('ssssssssssssssssssssssssss')
      var data = QualityInspectionData
      var names = [data.product_name, data.remark, data.product_data_file[0], data.categories, data.create_time]
      var linksName = ['标识编码名称', '备注', '数据采集文件上传路径', '检测分类', '上传日期']
      for(var i=0;i<names.length;i++) {
        var data1 = {
            id: String(currentId),
            name: names[i],
            category: 2,
            parentId: String(sourceId)
          }
          var links1 = {
            source: String(sourceId),
            target: String(currentId),
            label: {
              show: true,
              formatter: linksName[i],
              textStyle : { //标签的字体样式
                color : '#2D2F3B', //字体颜色
                fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                fontFamily : 'sans-serif', //文字的字体系列
                fontSize : 12, //字体大小
              },
            }
          }
          this.option.series[0].data.push(data1)
          this.option.series[0].links.push(links1)
          currentId = currentId +1
      }
      this.option.series[0].data[sourceId].flag = false
      this.mychart.clear()
      this.mychart.setOption(this.option)
    },

    //查询销售订单标识
    searchQuery() {
      if(!this.option.series[0].data.flag) {
        var rootId = 0
        this.foldNode(rootId)
      }
      var queryDateInfo = {}
      queryDateInfo.key = this.searchIdentity
      queryDateInfo.pageNumber = 0
      queryDateInfo.pageSize = 9999
      im_postAction('/identityManage/identitySearch', qs.stringify(queryDateInfo))
        .then((res) => {
          console.log(res)
          var currentId = this.option.series[0].data.length
          for(var i=0;i<res.data.result.list.records.length;i++) {
            var salesOrderIdentity = res.data.result.list.records[i].identification
            var data1 = {
              id: String(currentId),
              name: salesOrderIdentity,
              category: 1,
              parentId: '0',
              flag: true,
              salesOrderId: true
            }
            var links1 = {
              source: '0',
              target: String(currentId),
            }
            this.option.series[0].data.push(data1)
            this.option.series[0].links.push(links1)
            currentId = currentId +1
          }
          this.option.series[0].data[0].flag = false
          this.mychart.clear()
          this.mychart.setOption(this.option)  
      })
    },

    //折叠相应节点
    foldNode(rootId) {
      this.option.series[0].data[rootId].flag = true
      for (let i = this.option.series[0].data.length - 1; i >= 0; i--) {
        if (this.option.series[0].data[i].parentId == rootId) {
          if (this.option.series[0].data[i].parentId != this.option.series[0].data[i].id) {
            for (let j = this.option.series[0].data.length - 1; j >= 0; j--) {
              if (this.option.series[0].data[j].parentId == this.option.series[0].data[i].id) {
                for (let k = this.option.series[0].data.length - 1; k >= 0; k--) {
                  if (this.option.series[0].data[k].parentId == this.option.series[0].data[j].id) {
                    for(let m = this.option.series[0].data.length - 1; m >= 0; m--) {
                      if(this.option.series[0].data[m].parentId == this.option.series[0].data[k].id) {
                        this.option.series[0].data.splice(m, 1)
                        this.option.series[0].links.splice(m-1, 1)
                      }
                    }
                    this.option.series[0].data.splice(k, 1)
                    this.option.series[0].links.splice(k-1, 1)
                  }
                }
                this.option.series[0].data.splice(j, 1)
                this.option.series[0].links.splice(j-1, 1)
              }
            }
            this.option.series[0].data.splice(i, 1)
            this.option.series[0].links.splice(i-1, 1)
          }
        }
      }
    }
  }
}

</script>

<style scoped>
.productTraceability_sheep {
  background: #fff;
  padding: 8px;
}
</style>