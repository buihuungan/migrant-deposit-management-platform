<template>
  <div>
    <el-row>
      <el-col :span="18" :style="{ marginBottom: '24px' }">
        <el-row class="index-title">
          {{ indexTitle }}
        </el-row>
        <el-row class="index-content" v-html="indexContent">
          {{ indexContent }}
        </el-row>
        <!-- <el-row class="index-picture">
                <img src="../../assets/indexPageImg.png" width="100%" height="100%">
            </el-row>    -->
      </el-col>
      <el-col :span="6" :style="{ marginBottom: '24px' }"> </el-col>
    </el-row>
  </div>
</template>

<script>
import { ce_postFinishWares } from '@api/coursewareEngineApi'
export default {
  name: 'IndexPage',
  components: {},
  data() {
    return {
      indexTitle: '',
      indexContent: '',
    }
  },
  created() {
    this.initIndexPage()
    this.moveCard()
  },
  methods: {
    initIndexPage() {
      this.indexTitle = '动物源性流程介绍：'

      let id
      var process = JSON.parse(localStorage.getItem('process'))
      if (process) {
        id = process.processId
      } else {
        id = 135877
      }
      // var url = `/business/getWareDesc/${id}`
      // ce_postFinishWares(url, { processId: id }).then((res) => {
      //   this.indexContent = `<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp${res.result}`
      // })
      // this.indexContent=`&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp武汉凡谷电子技术股份有限公司（股票代码：002194）的前身是成立于1989年的武汉凡谷电子技术研究所。本公司是近年跻身国际一流的移动通信天馈系统射频器件独立供应商。<br>
      //                 &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp公司始终以“以不断满足客户需求为拉动力，以不断激发员工成功愿望为推动力”，“不断发展壮大、始终奉献社会”为理念。形成了以“技术创新，服务通信，核心业务，集中取胜，相邻业务，优选发展”宗旨的发展路线。<br>
      //                 &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp公司长期专注于发展移动通信天馈系统射频器件的核心技术，主要产品和解决方案有滤波器系列（双工器、合路器、塔顶放大器等）、介质材料、毫米波雷达系列（交通、安防、工业控制等）、行业网平台系列（公安、交通以及政府政务云等），凭借自有知识产权和强大的软件和硬件研发平台，快速响应客户定制产品需求。<br>
      //                 &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp公司具有从模具设计、压铸、机加、喷涂、电镀到装配、调试、检测的端到端的一站式生产制造体系。<br>
      //                 &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp凭借专业化产品研发平台、纵向集成的大规模生产能力、低成本结构以及优秀的客户协作能力，公司为全球领先的移动通信系统集成商提供基站天馈系统射频子系统和器件的定制产品及专业服务，通过其特有的技术、质量、成本管理、交付优势帮助客户服务了世界上约三分之一的移动用户。<br>
      //                 （1）计划：凡谷根据现有供需关系，对凡谷滤波器零部件制定采购计划，拟采购200套变压器、二极管；<br>
      //                 （2）采购：凡谷在收到需求商的凡谷滤波器需求订单之后，根据需求订单中凡谷滤波器产品相应的核心部件变压器和二极管对供应商下达采购需求，采购的变压器和二极管会根据数量给每个部件生成唯一标识编码，根据部件唯一标识编码可以查询到部件相关信息；<br>
      //                 （3）制造：凡谷在对变压器、二极管等部件收货入库之后，进行凡谷滤波器产品的制造，生产完成后生成凡谷滤波器产品唯一标识编码，根据凡谷滤波器产品唯一标识编码可以查询到凡谷滤波器产品相关信息；<br>
      //                 （4）物流：供应商在将变压器和二极管整理发货给凡谷时会生成唯一运单码，根据运单码可以查询到运单信息和物流状态；凡谷制造完凡谷滤波器产品发货时再生成凡谷滤波器产品的运单码，根据凡谷滤波器产品的运单码可以查询到运单信息和物流状态；<br>
      //                 （5）售后：需求商使用凡谷滤波器产品之后可通过维修服务商对凡谷滤波器产品进行故障反馈，维修服务商通过凡谷滤波器产品唯一标识编码查询到凡谷滤波器产品相关信息，再对凡谷进行质量数据反馈。`
      this.indexContent=`一.立案<br>
                      1.收到报案人报警后，明确涉案人员信息，受害人、嫌疑人，确定案发现场，做好立案<br>
                      2.向上级申请立案，获得现场勘查批准。<br><br>
                      
                      二.现场勘查：<br>
                      1.控制嫌疑人、让其指认所售卖的肉品种类<br>
                      2.取样：取中段肉，1斤左右，3-5块，编号、封装<br>
                      3.现场扣押，需要提前联系辖区有无冷库进行扣押肉品的储存，将肉品进行扣押<br>
                      4.迅速送至鉴定机构（提前联系鉴定机构，专人在机构等待办案单位送样，协助办理）<br>
                      <br>
                      三.检验鉴定<br>
                      1.将涉案物品进行扣押入库：扣押的物品在执法办案平台需要24小时内入库，入不了库的写明原因。开具扣押决定书、扣押物品清单。<br>
                      2.呈请鉴定：鉴定前将涉案物品出库，并在执法办案平台呈请鉴定。<br>
                      3.审批：警务支援大队案件部门主管领导审批通过后呈请到主管副局长进行审批。<br>
                      4.开具鉴定委托文书：开具鉴定聘请书、呈请鉴定书、鉴定委托书。公安机关需要出具正式的鉴定委托书，委托书中应明确鉴定的对象、目的、要求和相关法律依据等内容。鉴定聘请书写明鉴定要求（一式两份，一份交由鉴定机构、一份附卷）<br>
                      <br>
                      四.价值评估<br>
                      根据案情以及检验鉴定的结果，对案件做出评估结果，评估结果包含以下五个关键信息：市场中间价格、市场正品价值、违法所得、获利所得、货值金额。`
                   
    },
    moveCard() {
      $(window).bind('scroll', function () {
        let scrollTop = $(this).scrollTop()
        console.log(scrollTop) // 当前窗口的滚动距离
        $('.box-card').css({ top: scrollTop + 'px' })
      })
    },
  },
}
</script>

<style lang="less" scoped>
.index-title {
  margin-bottom: 6px;
  width: 250px;
  height: 37px;
  font-size: 26px;
  font-family: PingFang SC;
  font-weight: 500;
  line-height: 37px;
  color: #000000;
  opacity: 1;
}

.index-content {
  text-align: justify;
  margin-left: 10px;
  font-size: 16px;
  font-family: PingFang SC;
  font-weight: 300;
  line-height: 22px;
  color: #000000;
  opacity: 1;
}

.index-picture {
  margin-top: 28px;
  margin-left: 10px;
}
</style>