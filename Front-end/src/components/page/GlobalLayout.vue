<template>
  <div>
    <a-layout class="layout" :class="[device]">
      <template v-if="layoutMode === 'sidemenu'">
        <a-drawer
          v-if="device === 'mobile'"
          :wrapClassName="'drawer-sider ' + navTheme"
          placement="left"
          @close="() => (this.collapsed = false)"
          :closable="false"
          :visible="collapsed"
          width="200px"
        >
          <side-menu
            mode="inline"
            v-if="device === 'mobile'"
            :menus="menus"
            @menuSelect="menuSelect"
            :theme="navTheme"
            :collapsed="false"
            :collapsible="true"
          ></side-menu>
        </a-drawer>

        <side-menu
          v-show="device === 'desktop'"
          mode="inline"
          :menus="menus"
          @menuSelect="myMenuSelect"
          :theme="navTheme"
          :collapsed="collapsed"
          :collapsible="true"
        ></side-menu>
      </template>
      <!-- 下次优化这些代码 -->
      <template v-else>
        <a-drawer
          v-if="device === 'mobile'"
          :wrapClassName="'drawer-sider ' + navTheme"
          placement="left"
          @close="() => (this.collapsed = false)"
          :closable="false"
          :visible="collapsed"
          width="200px"
        >
          <side-menu
            mode="inline"
            :menus="menus"
            @menuSelect="menuSelect"
            :theme="navTheme"
            :collapsed="false"
            :collapsible="true"
          ></side-menu>
        </a-drawer>
      </template>

      <a-layout
        :class="[layoutMode, `content-width-${contentWidth}`]"
        :style="{ paddingLeft: fixSiderbar && isDesktop() ? `${sidebarOpened ? 200 : 80}px` : '0' }"
      >
        <!-- layout header -->
        <global-header
          :mode="layoutMode"
          :menus="menus"
          :theme="navTheme"
          :collapsed="collapsed"
          :device="device"
          @toggle="toggle"
        />

        <!-- layout content -->
        <a-layout-content :style="{ height: '100%', paddingTop: fixedHeader ? '59px' : '0' }">
          <slot></slot>
          <!-- <div class="box-card-area" v-if="this.showBoxCard">
            <el-card class="box-card">
                <div class="box-icon">
                  <a-avatar class="box-avatar" size="large" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                </div>
                <div class="box-name" :src="getAvatar()">
                  {{nickname()}}
                </div>
                <div class="box-info">
                  <template>
                  <el-row>                    
                    <div>工号：{{this.boxNum}}</div>
                  </el-row>
                  <el-row>           
                    <div>部门：{{this.boxDepartment}}</div>
                  </el-row>
                  <el-row>
                    <div>角色：{{this.boxRole}}</div>
                  </el-row>
                  </template>
                </div>
                <div class="box-btn">
                  <template>
                  <el-row>                    
                    <el-button :disabled="percentage == 100" id="openPanelBtn" @click="openCourse()" plain>开启流程</el-button>
                  </el-row>
                  <el-row>                    
                    <el-button :disabled="percentage == 100" id="openPanelBtn" @click="clickNodePanel(0)"  plain>开启节点</el-button>
                  </el-row>
                  <el-row>
                    <el-button :disabled="percentage == 100 || is_open_node == '0'" id="openPanelBtn" @click="finishPanel()" plain>完成节点</el-button>
                  </el-row>
                  <el-row>
                    <el-button :disabled="percentage == 100" id="openPanelBtn"  @click="endCourse()" plain>结束流程</el-button>
                    <!-- <el-popconfirm 
                      title="确定结束课程吗？"
                      ref="ref"
                      @onConfirm="endCourse()"
                    >             
                      <el-button :disabled="percentage == 100" id="openPanelBtn"  plain slot="reference" >结束课程</el-button>
                    </el-popconfirm> -->
                  <!-- </el-row>
                  <el-row>
                    <a :href="downLoadFile.fileUrl" :download="downLoadFile.fileName">
                      <el-button id="uploadHandbook" @click="clickUploadHandbook()" plain>
                        下载操作手册
                      </el-button>  
                    </a>       
                  </el-row> 
                  <el-progress :percentage="percentage" id="percentage" :format="format"></el-progress>
                  </template>
                </div>
            </el-card> -->
          <!-- </div>  -->
          
          <!-- 导航栏 -->
          <el-popover
            placement="manual"
            width="750"
            trigger="manual"
            @show="show"
            :visible-arrow="false"
            popper-class="area-popover"
            v-model="showPopover"
            class="elPopover"
          >
            <div id="efContainer" ref="efContainer1" @mousemove.stop="mousemove" @mouseout="mouseout">
              <template v-for="(node, index) in data1.nodeList">
                <flow-node
                  :id="node.id"
                  :key="node.id"
                  :node="node"
                  :index="index"
                  type="navigation"
                  ref="flowNode"
                  :activeElement="activeElement"
                  @clickNode="clickNode"
                ></flow-node>
              </template>
            </div>
            <div class="mask">
              <div class="animation" ref="efContainer2">
                <template v-for="node in data2.nodeList">
                  <flow-node
                    :id="node.id"
                    :key="node.id"
                    :node="node"
                    :activeElement="activeElement"
                    @clickNode="clickNode"
                  ></flow-node>
                </template>
              </div>
            </div>
            
            <!-- <div
              slot="reference"
              class="navigation"
              id="navigatePopover"
              style="background:url('https://images.weserv.nl/?url=https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng2bcd29cfbc0a2bbc68be9227df9db5a6a3563626c82027e9fca34b93a27d4eca')"
            ></div> -->

            <!-- <div
              slot="reference"
              class="navigation"
              id="navigatePopover"
              style="background-size:60px 60px"
            >
              <img src="../../assets/image.png" width="60px" height="60px" />
            </div> -->
            <!-- <img
              slot="reference"
              class="navigation"
              referrerpolicy="no-referrer"
              src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng2bcd29cfbc0a2bbc68be9227df9db5a6a3563626c82027e9fca34b93a27d4eca"
            /> -->
            <!-- <el-button slot="reference" :circle="true" class="navigation">导</el-button> -->
          </el-popover>
          <el-dialog :visible.sync="videoDialogVisible" width="50%" center :before-close="handleCloseV">
            <span style="font-size: 20px; margin-left: 30%">“{{ videoName }}”节点的教学视频播放</span>
            <video-playback ref="videoPlay" :videoSrc="videoSrc" v-if="videoIF" @finishF="finishF"></video-playback>
          </el-dialog>
          <el-dialog :visible.sync="timeDialogVisible" width="50%" center :before-close="handleCloseT">
            <a-tabs default-active-key="1">
              <a-tab-pane key="1" tab="订单">
                <timeline v-if="Object.keys(orderList).length != 0" :timeline-list="orderList" type="order"></timeline>
                <a-result v-else status="warning" title="暂无数据"> </a-result>
              </a-tab-pane>
              <a-tab-pane key="2" tab="产品" force-render>
                <timeline v-if="Object.keys(modelList).length != 0" :timeline-list="modelList" type="model"></timeline>
                <a-result v-else status="warning" title="暂无数据"> </a-result>
              </a-tab-pane>
              <a-tab-pane key="3" tab="成品">
                <timeline
                  v-if="Object.keys(productList).length != 0"
                  :timeline-list="productList"
                  type="product"
                ></timeline>
                <a-result v-else status="warning" title="暂无数据"> </a-result>
              </a-tab-pane>
            </a-tabs>
          </el-dialog>
          <a-modal :visible="visible" @ok="handleOk" @cancel="handleCancel" :zIndex="9999999">
            <p>是否开启节点？</p>
          </a-modal>

          <el-dialog
            :title="infoDialogRole"
            :visible.sync="infoDialogVisible"
            width="30%"
            :before-close="handleClose">
            <template>
              <el-row>
                <span>进入模块:{{this.infoDialogName}}</span>
              </el-row>
            </template>
            <span slot="footer" class="dialog-footer">
              <el-button type="primary" @click="handleInfoOk">确认</el-button>
            </span>
          </el-dialog>
          <!-- 导航栏 -->
        </a-layout-content>

        <!-- layout footer -->
        <a-layout-footer style="padding: 0px">
          <global-footer />
        </a-layout-footer>
      </a-layout>

      <!-- update-start---- author:os_chengtgen -- date:20190830 --  for:issues/463 -编译主题颜色已生效，但还一直转圈，显示主题 正在编译 ---- -->
      <!--<setting-drawer></setting-drawer>-->
      <!-- update-end---- author:os_chengtgen -- date:20190830 --  for:issues/463 -编译主题颜色已生效，但还一直转圈，显示主题 正在编译 ---- -->
    </a-layout>

    <div>
      <el-dialog
        title="当前模型是否符合订单要求?"
        :visible.sync="demoShowFlag"
        :destroy-on-close="true"
        centered
        width="80%"
        :show-close="false"
      >
        <div>
          <div class="orderDetail">
            订单信息&nbsp;&nbsp;&nbsp;颜色:
            <div style="display: inline-block; color: green">{{ orderColor }}</div>
            &nbsp;&nbsp;形状:
            <div style="display: inline-block; color: green">{{ orderShape }}</div>
            &nbsp;&nbsp;性能指标:
            <div style="display: inline-block; color: green">{{ orderPowerSize }}</div>
            <el-divider></el-divider>
          </div>
          <div>
            <iframe ref="iframe" id="bdIframe" name="bdIframe" :src="bdTokenUrl" frameborder="0" scrolling="no">
            </iframe>
          </div>
          <div style="width: 100%; text-align: center; margin-top: 10px">
            <el-button type="danger" style="margin-right: 20px" @click="handelWrong()">不符合</el-button>
            <el-button type="success" @click="handelCorrect()">符合</el-button>
          </div>
        </div>
      </el-dialog>
    </div>
    <div>
      <offer-model ref="modalForm"></offer-model>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import videoPlayback from '@/components/VideoPlayer/videoPlayer'
import SideMenu from '@/components/menu/SideMenu'
import GlobalHeader from '@/components/page/GlobalHeader'
import GlobalFooter from '@/components/page/GlobalFooter'
// update-start---- author:os_chengtgen -- date:20190830 --  for:issues/463 -编译主题颜色已生效，但还一直转圈，显示主题 正在编译 ------
// import SettingDrawer from '@/components/setting/SettingDrawer'
// 注释这个因为在个人设置模块已经加载了SettingDrawer页面
// update-end ---- author:os_chengtgen -- date:20190830 --  for:issues/463 -编译主题颜色已生效，但还一直转圈，显示主题 正在编译 ------

import { triggerWindowResizeEvent } from '@/utils/util'
import { mapState, mapActions } from 'vuex'
import { mixin, mixinDevice } from '@/utils/mixin.js'
import OfferModel from '@/components/page/offerModel'
import '@/views/coursewareEngine/easyflowModule/jsplumb' 
import { easyFlowMixin } from '@/views/coursewareEngine/easyflowModule/mixins'
import lodash from 'lodash'
import flowNode from '@/views/coursewareEngine/easyflowModule/node'
import { getAction } from '@/api/manage'
import { e_postAction } from '@/api/eduApi'
import { w_postAction } from '@/api/wmsApi'
import { ce_getWares } from '@api/coursewareEngineApi'
import { ce_getWares2 } from '@api/coursewareEngineApi'
import { ce_getPercentage } from '@api/coursewareEngineApi'
import { ce_postFinishWares } from '@api/coursewareEngineApi'
import { USER_ID, USER_INFO } from '@/store/mutation-types'
import { ACCESS_TOKEN ,TENANT_ID} from '@/store/mutation-types'
import Vue from 'vue'
import { putAction } from '../../api/manage'
import { order_postAction, order_getAction } from '@/api/orderApi'
import TimeLine from '../Timeline/Timeline'
import { o_getAction } from '@/api/onApi'
import { generateIndexRouter } from '@/utils/util'
import { websocketmessage } from '@/utils/sendwebmessage'
import { ce_getRoleId } from '@api/coursewareEngineApi'
import { mapGetters } from 'vuex'
import api from '@/api/index'

export default {
  name: 'GlobalLayout',
  components: {
    OfferModel,
    SideMenu,
    GlobalHeader,
    GlobalFooter,
    flowNode,
    videoPlayback,
    TimeLine,
    // update-start---- author:os_chengtgen -- date:20190830 --  for:issues/463 -编译主题颜色已生效，但还一直转圈，显示主题 正在编译 ------
    // // SettingDrawer
    // 注释这个因为在个人设置模块已经加载了SettingDrawer页面
    // update-end ---- author:os_chengtgen -- date:20190830 --  for:issues/463 -编译主题颜色已生效，但还一直转圈，显示主题 正在编译 ------
  },
  mixins: [mixin, mixinDevice, easyFlowMixin],
  data() {
    return {
      // is_open_course: '0',  //是否开启课程
      is_open_node: '0',    //是否开启节点
      lessonId: '',
      teachingMode: '',
      planId: '',
      processId: '',
      wareId:'',
      downLoadFile:{
        fileUrl:'',
        fileName: '操作手册.docx',
      },
      flowId: '742068bdf6a44fc7bfcb25a909a180ac',
      bdTokenUrl: './static/3d_model/model.html',
      demoShowFlag: false,
      demo_3d_data: {},
      percentage: 0,
      judgeDemo_3D_data: {},
      demo_3d_all_data: [
        {
          objurl: '../../../static/3d_model/sjh/baishuyuan.obj',
          mtlurl: '../../../static/3d_model/sjh/baishuyuan.mtl',
          color: 1,
          shape: 0,
          powerSize: 0,
        },
        {
          objurl: '../../../static/3d_model/sjh/baishufang.obj',
          mtlurl: '../../../static/3d_model/sjh/baishufang.mtl',
          color: 1,
          shape: 1,
          powerSize: 0,
        },
        {
          objurl: '../../../static/3d_model/sjh/baimiyuan.obj',
          mtlurl: '../../../static/3d_model/sjh/baimiyuan.mtl',
          color: 1,
          shape: 0,
          powerSize: 1,
        },
        {
          objurl: '../../../static/3d_model/sjh/baimifang.obj',
          mtlurl: '../../../static/3d_model/sjh/baimifang.mtl',
          color: 1,
          shape: 1,
          powerSize: 1,
        },
        {
          objurl: '../../../static/3d_model/sjh/heishuyuan.obj',
          mtlurl: '../../../static/3d_model/sjh/heishuyuan.mtl',
          color: 0,
          shape: 0,
          powerSize: 0,
        },
        {
          objurl: '../../../static/3d_model/sjh/heishufang.obj',
          mtlurl: '../../../static/3d_model/sjh/heishufang.mtl',
          color: 0,
          shape: 1,
          powerSize: 0,
        },
        {
          objurl: '../../../static/3d_model/sjh/heimiyuan.obj',
          mtlurl: '../../../static/3d_model/sjh/heimiyuan.mtl',
          color: 0,
          shape: 0,
          powerSize: 1,
        },
        {
          objurl: '../../../static/3d_model/sjh/heimifang.obj',
          mtlurl: '../../../static/3d_model/sjh/heimifang.mtl',
          color: 0,
          shape: 1,
          powerSize: 1,
        },
      ],
      randomNumber: 0,
      judgeResult: false,
      globalObjurl: '',
      globalMtlurl: '',
      result: false,
      orderColor: '黑色',
      orderShape: '圆形',
      orderPowerSize: '15db',
      collapsed: false,
      activeMenu: {},
      menus: [],
      editor: {},
      //导航data
      oXArr: [],
      oYArr: [],
      leftArr: [],
      topArr: [],
      // 是否加载完毕标志位
      loadEasyFlowFinish: false,
      //jsPlumb修改
      jsPlumb1: null,
      jsPlumb2: null,
      data1: {},
      data2: {},
      activeElement: {
        // 可选值 node 、line
        type: undefined,
        // 节点ID
        nodeId: undefined,
        // 连线ID
        sourceId: undefined,
        targetId: undefined,
      },
      ware_content: '',
      navigationUrl: {
        searchUrl: '/cgform/api/getData/a0a23a86799d43f5b019d3cadf2261a8',
        updateUrl: '/online/cgform/api/form/a0a23a86799d43f5b019d3cadf2261a8',
      },
      ware: {},
      //jsPlumb修改
      //播放视频相关变量
      videoName: '', //播放教学视频上方的节点名称
      videoIF: false, //用来视频组件销毁
      videoSrc: '', //节点教学视频播放地址
      videoDialogVisible: false, //视频弹窗
      finishFlag: 3, //视频是否播放完
      visible: false, //是否开启节点弹框
      nodeList: {}, //节点信息
      index: 0, //节点索引
      timeDialogVisible: false, //时间轴组件
      orderList: [], //时间轴数据
      modelList: [], //时间轴数据
      productList: [], //时间轴数据
      infoDialogVisible: false, //信息对话框是否显示
      infoDialogName: "", //信息对话框内容
      infoDialogRole: "", //显示绑定用户
      boxIcon: "",
      boxName: "",
      boxNum: "",
      boxDepartment: "待定",
      boxRole: "待定",
      showBoxCard: true,
      showPopover: false,
      hasInitWareContent: false,
      roleList: [], //角色列表
    }
  },
  computed: {
    ...mapState({
      // 主路由
      mainRouters: (state) => state.permission.addRouters,
      // 后台菜单
      permissionMenuList: (state) => state.user.permissionList,
    }),
  },
  watch: {
    sidebarOpened(val) {
      this.collapsed = !val
    },
  },
  created() {
    if (navigator.keyboard && navigator.keyboard.lock) {
        navigator.keyboard.lock(['Escape']);
        console.log('已启用');
      } else {
        // alert('您的浏览器上不支持该功能')
      }
    //--update-begin----author:scott---date:20190320------for:根据后台菜单配置，判断是否路由菜单字段，动态选择是否生成路由（为了支持参数URL菜单）------
    //this.menus = this.mainRouters.find((item) => item.path === '/').children;
    this.menus = this.permissionMenuList
    // 根据后台配置菜单，重新排序加载路由信息
    //console.log('----加载菜单逻辑----')
    //console.log(this.mainRouters)
    //console.log(this.permissionMenuList)
    //console.log('----navTheme------'+this.navTheme)
    //--update-end----author:scott---date:20190320------for:根据后台菜单配置，判断是否路由菜单字段，动态选择是否生成路由（为了支持参数URL菜单）------
    // this.loadWareContent();
    getAction('/sys/role/list?pageSize=100000').then((res) => {
      this.roleList = res.result.records
      
    console.log("roleList",this.roleList)
    })
  },
  mounted() {
    //this.percentageNumber()
    let vm=this
    $("#navigatePopover").on("click",function(){ vm.clickNodePanel(1) });
    $(document).on("click",function(e){
      if($(e.target).attr('class')
        &&!$(e.target).attr('class').match("el-popover")
        &&!$(e.target).attr('class').match("menu-node")
        &&!$(e.target).attr('class').match("node-menu")
        &&!$(e.target).attr('class').match("swinlane")){
        vm.closeNodePanel()
      }
    });
  },
  updated() {
    var that = this
    window.addEventListener(
      'message',
      function (e) {
        if (e.data == 'demoShowFlag') {
          that.demoShowFlag = false
          $('#bdIframe').attr('src', 'about:blank')
        }
        if (e.data == 'updata') {
          //刷新
          var newrow = {}
          that.demoShowFlag = false
          that.$nextTick(() => {
            newrow.objurl = that.globalObjurl
            newrow.mtlurl = that.globalMtlurl
            that.handleClick(newrow)
          })
        }
      },
      false
    )
    this.moveEfcontainer()
    this.moveNavigation()
    this.isShowBoxCard()
  },
  methods: {
    //结束课程
    endCourse() {
      console.log('222222222')
      var params = {
        processId: this.processId,
        planId: this.planId,
        lessonId: this.lessonId,
        teachingMode: this.teachingMode
      }
      console.log('1111111111111111')
      ce_getWares2('/business/shutDownTask/ByStudent',params).then((res)=>{
        console.log('res',res)
        if(res.success) {
          this.$message.success('结束课程成功！')
          this.percentage = 0
        }
      })
    },
    //更新节点开启状态
    updateNodeStatus(value) {
      this.$store.commit('changeisOpenNode',value)
      this.is_open_node = this.$store.state.isOpenNode
    },
    //获取角色id
    getRoleId1() {
      // ce_getRoleId('/business/getRoleId').then((res)=> {
      // this.loadWareContent();
      var url = '/business/getDefaultInformation';
      var params = {
        processId: this.processId,
        planId: this.planId,
        lessonId: this.lessonId,
        teachingMode: this.teachingMode
      }
      ce_getWares2(url,params).then((res)=>{  
        this.processInstanceId = res.result.processInstanceId
        ce_getRoleId(`/business/getRoleId/${this.processInstanceId}`).then((res)=> {
        var userMessage = Vue.ls.get(USER_INFO)
        let params = {
          userId: '',
          roleIds: []
        }
        params.userId = userMessage.id
        params.roleIds[0] = res.result.roleId
        let rolename=""
        for(let i=0;i<this.roleList.length;i++){
          if( res.result.roleId == this.roleList[i].id){
            rolename=this.roleList[i].roleName
          }
        }
        this.boxRole = rolename
        this.boxDepartment = rolename.substring(0,rolename.length-1) + '部'
        e_postAction('/manager/grantRole', params).then((res) => {
          this.GetPermissionList().then((res) => {
            this.menus = res.result.menu
            let firstPath=this.menus[0].path
            console.log(this.menus)
            console.log(firstPath)
            if(this.menus.length>1){
              let component=this.menus[1];
              while(component.children && component.children.length>0){
                component=component.children[0]
              }
              firstPath=component.path
            }
            console.log(firstPath)
            console.log("aaaa")
            let constRoutes = []
            constRoutes = generateIndexRouter(this.menus)
            // 添加主界面路由
            this.$store.dispatch('UpdateAppRouter', { constRoutes }).then(() => {
              this.updateNodeStatus('1') //开启节点成功，更新是否开启节点状态
              console.log('this.$store.getters.addRouters', this.$store.getters.addRouters)
              this.$router.addRoutes(this.$store.getters.addRouters)
              this.$router.replace({ path: firstPath }).catch((err) => err)
              this.$emit('clearPageList')
            })
          })
        })
      }
      )
      })
    },

    getRoleId2() {
      // ce_getRoleId('/business/getRoleId').then((res)=> {
      // this.loadWareContent();
      var url = '/business/getDefaultInformation';
      var params = {
        processId: this.processId,
        planId: this.planId,
        lessonId: this.lessonId,
        teachingMode: this.teachingMode
      }
      ce_getWares2(url,params).then((res)=>{  
        this.processInstanceId = res.result.processInstanceId
        ce_getRoleId(`/business/getRoleId/${this.processInstanceId}`).then((res)=> {
        var userMessage = Vue.ls.get(USER_INFO)
        let params = {
          userId: '', 
          roleIds: []
        }
        params.userId = userMessage.id
        params.roleIds[0] = res.result.roleId
        let rolename=""
        for(let i=0;i<this.roleList.length;i++){
          if( res.result.roleId == this.roleList[i].id){
            rolename=this.roleList[i].roleName
          }
        }
        this.boxRole = rolename
        this.boxDepartment = rolename.substring(0,rolename.length-1) + '部'
        e_postAction('/manager/grantRole', params).then((res) => {
          this.GetPermissionList().then((res) => {
            this.menus = res.result.menu
            let firstPath=this.menus[0].path
            console.log(this.menus)
            console.log(firstPath)
            if(this.menus.length>1){
              let component=this.menus[1];
              while(component.children && component.children.length>0){
                component=component.children[0]
              }
              firstPath=component.path
            }
            console.log(firstPath)
            console.log("aaaa")
            // let constRoutes = []
            // constRoutes = generateIndexRouter(this.menus)
            // // 添加主界面路由
            // this.$store.dispatch('UpdateAppRouter', { constRoutes }).then(() => {
            //   this.updateNodeStatus('1') //开启节点成功，更新是否开启节点状态
            //   console.log('this.$store.getters.addRouters', this.$store.getters.addRouters)
            //   this.$router.addRoutes(this.$store.getters.addRouters)
            //   this.$router.replace({ path: firstPath }).catch((err) => err)
            //   this.$emit('clearPageList')
            // })
          })
        })
      }
      )
      })
    },
    //显示进度条
    percentageNumber(){  
      if(this.processInstanceId != undefined)  {
      var url = `/business/percentage/${this.processInstanceId}`;
      ce_getPercentage(url).then((res)=>{
        this.percentage = res.result
      })
      }else{
        return
      }
    },
    format(percentage){
      console.log("this.percentage",this.percentage)
      console.log("percentage",percentage)
      return this.percentage === 100 ?  '完成课程':`${percentage}%`
    },
    ...mapActions(['setSidebar', 'GetPermissionList']),
    moveNavigation() {
      $('.navigation').mousedown(function (e) {
        var isFlag = true

        var me = $(this)

        var top = me.offset().top
        var left = me.offset().left

        var x = e.clientX
        var y = e.clientY
        $(document)
          .mousemove(function (e) {
            if (isFlag) {
              var xx = e.clientX
              var yy = e.clientY
              var xxx = window.pageXOffset
              var yyy = window.pageYOffset
              var t = yy + top - y - yyy
              var l = xx + left - x - xxx
              me.css({ left: l, top: t })
              console.log('触发了')
            }
          })
          .mouseup(function () {
            isFlag = false
          })
      })
    },
    moveEfcontainer() {
      $(window).bind('scroll', function () {
        let scrollTop = $(this).scrollTop()
        $('.el-popover').css({ top: scrollTop + 'px' })
        $('.area-popover').css({ top: scrollTop + 'px' })
      })
    },
    async loadWareContent() { //默认加载
      let id
      var process = JSON.parse(localStorage.getItem('process'));
      if(process){
        //  id = process.processId;
         this.processId = process.processId;
         this.planId = process.planId
         this.teachingMode = process.teachingMode
         this.lessonId = process.lessonId
         this.wareId = process.wareId
      }else {
        // id = 135877;
        // this.processId = '265371';
        // this.planId =  '1538414484287156225';
        // this.teachingMode = '1'
        // this.lessonId = '1527201539892998145'
        // this.wareId = '265372'
        this.processId = '390055';
        this.planId =  '1777245341471080450';
        this.teachingMode = '1'
        this.lessonId = '1777244966500302849'
        this.wareId = '390056'
      }
      // }else {
      //   // id = 135877;
      //   this.processId = '140111';
      //   this.planId =  '1497398966647132162';
      //   this.teachingMode = '1'
      //   this.lessonId = '1496093345993351170'
      //   this.wareId = '140112'
      // }
      this.downLoadFile.fileUrl = api.server_url + '/coursewareengine/business/downLoadLessonFile?processId=' + this.processId
      // document.documentElement.webkitRequestFullScreen()    //进入首页即默认全屏
      // var url = `/business/getDefaultInformation/${id}`;
      var url = '/business/getDefaultInformation';
      var params = {
        processId: this.processId,
        planId: this.planId,
        lessonId: this.lessonId,
        teachingMode: this.teachingMode
      }
      this.data1.lineList = [];
      //this.jsPlumb1.reset();
      await ce_getWares2(url,params).then((res)=>{
        var wareJson = res.result.ware.returnJson;    
        this.processInstanceId = res.result.processInstanceId
        this.ware_content=JSON.stringify(wareJson);
        console.log("processInstanceId",this.processInstanceId);
        this.percentageNumber();//加载进度条
      })
      
      // let params = {
      //   column: 'createTime',
      //   order: 'desc',
      //   superQueryMatchType: 'and',
      // }
      // let searchParams = []
      // searchParams.push({ rule: 'eq', type: 'text', val: Vue.ls.get(USER_ID), field: 'user_id' })
      // params.superQueryParams = encodeURI(JSON.stringify(searchParams))
      // o_getAction(this.navigationUrl.searchUrl, params).then((res) => {
      //   if (res.result.records.length > 0) {
      //     let ware_temp = res.result.records[0]
      //     let ware_content_temp = res.result.records[0].plan_content
      //     for(let i=0;i<res.result.records.length;i++)
      //     {
      //       if(res.result.records[i].plan_id==sessionStorage.getItem("planId")){
      //         ware_temp = res.result.records[i]
      //         ware_content_temp = res.result.records[i].plan_content
      //         sessionStorage.removeItem("planId")
      //         break;
      //       }
      //     }
      //     this.ware=ware_temp
      //     this.ware_content=ware_content_temp
      //     if(!this.hasInitWareContent){
      //       this.hasInitWareContent=true
      //       let wc=JSON.parse(this.ware_content);
      //       for(let i=0;i<wc.nodeList.length;i++){
      //         if(wc.nodeList[i].hasClicked){
      //           wc.nodeList[i].hasClicked=false
      //         }
      //       }
      //       this.ware_content=JSON.stringify(wc)
      //     }   
      //     this.dataReload2(JSON.parse(this.ware_content))
      //   }
      // })
    },
    finishPanel(){
      // let id
      // var process = JSON.parse(localStorage.getItem('process'));
      // if(process){
      //    id = process.processId;
      // }else {
      //    id = 135877;
      // }
      var taskId = "";
      var params = {
        processId: this.processId,
        planId: this.planId,
        lessonId: this.lessonId,
        teachingMode: this.teachingMode
      }
      // var url1 = `/business/getNewListTaskId/${id}`;
      var url1 = '/business/getNewListTaskId';
      ce_getWares2(url1,params).then((res)=>{
        var postData = {
            planId: this.planId,
            lessonId: this.lessonId,
            userId: '',
            roleId: '',
          }
          var userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
          postData.userId = userid.value
          ce_getRoleId(`/business/getRoleId/${this.processInstanceId}`).then((res)=> {
            postData.roleId = res.result.roleId
            axios({
              url: api.server_url + api.global_edu_baseURL + '/eduStatistics/statisticsNum',
              method: 'post',
              data: postData
            }).then( res => {
              console.log('res',res)
          })
          })
        console.log("res",res.result);
        taskId = res.result;
        var url = `/task/complete`;
      // var finish = JSON.stringify(this.jsonObjectParam)
        ce_postFinishWares(url,{taskId:taskId}).then((res)=>{
          if(res.result.processInstanceId){
            var wareJson = res.result.diagram.returnJson;
            this.ware_content=JSON.stringify(wareJson);
            this.$message.success('节点已完成');
            this.percentageNumber()
            this.updateNodeStatus('0')
          }else{
            this.$message.success('该课程已全部完成，进度条会清零，不用担心，可以重复学习而已,次数累加，完成一次就够啦！',60);
            this.percentage = 100
          }
        })
      })
    },

    openCourse(){
      // if (navigator.keyboard && navigator.keyboard.lock) {
      //   navigator.keyboard.lock(['Escape']);
      //   console.log('已启用');
      // } else {
      //   alert('您的浏览器上不支持该功能')
      // }

      // document.documentElement.webkitRequestFullScreen()    //开启课程进入全屏
      // let id
      // var process = JSON.parse(localStorage.getItem('process'));
      // if(process){
      //    id = process.processId;
      // }else {
      //    id = 135877;
      // }
      // var url = `/business/startClass/${id}`;
      var data = {
        planId: this.planId,
        lessonId: this.lessonId,
      }
      axios({
        url: api.server_url + api.global_edu_baseURL + '/EduCreditStatistics/insertStatisticNewStudent',
        method: 'post',
        params: data,
        headers: {
          'userid': Vue.ls.get(USER_ID),
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        }
      }).then((res) => {
        
      })
      var url = `/business/startClass/${this.wareId}`;
      var params = {
        processId: this.processId,
        planId: this.planId,
        lessonId: this.lessonId,
        teachingMode: this.teachingMode
      }
      this.data1.lineList = [];
      //this.jsPlumb1.reset();
      ce_getWares2(url,params).then((res)=>{
        this.$store.commit('changeExampleIdValue',res.result.exampleId) //存储实例ID
        var wareJson = res.result.ware.returnJson;        
        this.processInstanceId = res.result.processInstanceId
        if(res.result.startProcessVO){
          if(res.result.startProcessVO.currentTask == "视频"){
            var myValue = {formDesignerId: '', onlineTableId: '', onlineDataId: ''}
            myValue.formDesignerId = res.result.startProcessVO.formDesignerId,
            myValue.onlineTableId = res.result.startProcessVO.onlineTableId,
            myValue.onlineDataId = res.result.startProcessVO.onlineDataId,
            this.$store.commit('changFormValue', myValue)
          }
        };
        if(res.result.ware.startProcessVO){
          var i = 0;
          console.log("121")
          for(i=0;i<res.result.ware.startProcessVO.length;i++){
            console.log("2222")
          if(res.result.ware.startProcessVO[i].formDesignerId != null || res.result.ware.startProcessVO[i].formDesignerId != undefined){
            var myValue = {formDesignerId: '', onlineTableId: '', onlineDataId: ''}
            myValue.formDesignerId = res.result.ware.startProcessVO[i].formDesignerId,
            myValue.onlineTableId = res.result.ware.startProcessVO[i].onlineTableId,
            myValue.onlineDataId = res.result.ware.startProcessVO[i].onlineDataId,
            this.$store.commit('changFormValue', myValue)
          }}
        };
        let flag = res.result.flag;
        this.percentageNumber();
        if(flag == 1 ){
          this.$message.success('继续学习');
        }else if (flag == 0){
          this.$message.success('开始学习');
        }
        this.ware_content=JSON.stringify(wareJson);
        document.documentElement.webkitRequestFullScreen()    //开启课程进入全屏
        // this.loadWareContent()
        // this.getRoleId2()
        // this.showPopover=!this.showPopover;
      })

    },
    //开启节点
    clickNodePanel(flag) {
      if(flag == 0){
        // this.loadWareContent()
        // this.getRoleId1()
        // location.reload(); //刷新页面
      }
      this.showPopover=!this.showPopover;
    },
    closeNodePanel() {
      if(this.showPopover){
       this.showPopover=false;
      }
    },
    handleOk() {
      let vm = this
      websocketmessage('1', this.nodeList.id)
      let params = {}
      var formData = new FormData()
      formData.append('userId', Vue.ls.get(USER_ID))
      formData.append('status', this.nodeList.name + '中')
      // order_postAction('/orderIdentity/updateByUser', formData)
      //   .then((res) => {
      if (this.nodeList.role) {
        let userMessage = Vue.ls.get(USER_INFO)
        let params = {}
        params.userId = userMessage.id
        params.roleIds = this.nodeList.role
        let rolename=""
        for(let i=0;i<this.roleList.length;i++){
          if(this.nodeList.role == this.roleList[i].id){
            rolename=this.roleList[i].roleName
          }
        }
        this.infoDialogRole = "您当前的角色是："+rolename
        this.boxRole = rolename
        e_postAction('/manager/grantRole', params).then((res) => {
          this.GetPermissionList().then((res) => {
            this.menus = res.result.menu
            let firstPath=this.menus[0].path
            console.log(this.menus)
            console.log(firstPath)
            if(this.menus.length>1){
              let component=this.menus[1];
              while(component.children && component.children.length>0){
                component=component.children[0]
              }
              firstPath=component.path
            }
            console.log(firstPath)
            console.log("aaaa")
            let constRoutes = []
            constRoutes = generateIndexRouter(this.menus)
            // 添加主界面路由
            this.$store.dispatch('UpdateAppRouter', { constRoutes }).then(() => {
              console.log('this.$store.getters.addRouters', this.$store.getters.addRouters)
              this.$router.addRoutes(this.$store.getters.addRouters)
              this.$router.replace({ path: firstPath }).catch((err) => err)
              this.$emit('clearPageList')
            })
            
            this.infoDialogName=this.nodeList.name
            this.boxDepartment=this.nodeList.name
            switch (this.nodeList.name) {
              case '检测':
                w_postAction('/cictOrder/currModel').then((res) => {
                  if (res.success) {
                    if (res.result.color == 0) {
                      this.orderColor = '黑色'
                    } else {
                      this.orderColor = '白色'
                    }
                    if (res.result.shape == 0) {
                      this.orderShape = '圆形'
                    } else {
                      this.orderShape = '方形'
                    }
                    if (res.result.powerSize == 0) {
                      this.orderPowerSize = '15db'
                    } else {
                      this.orderPowerSize = '20db'
                    }
                    $('#bdIframe').attr('src', this.bdTokenUrl)
                    this.demoShowFlag = false
                    this.loading3DModel()
                  } else {
                    this.$message({
                      message: '订单数据错误，请检查订单信息！',
                      type: 'error',
                      customClass: 'zZindex',
                    })
                    this.demoShowFlag = false
                  }

                  // this.$message({ message: '请创建订单后再检测！', type: 'warning', customClass: 'zZindex' })
                })

                break
              case '发货':
                this.$refs.modalForm.getForm(this.flowId)
                break
              case '入库':
                w_postAction('/wmsInBill/autoInBill')
                  .then((res) => {
                    if (res.code === 200) {
                      // this.$message.success('入库成功')
                      this.updateWare()
                    } else {
                      // this.$message.warning('入库失败')
                    }
                  })
                  .catch((err) => {
                    console.log('err', err)
                  })
                break
              default:
                if (this.nodeList.feature != 'vedio') {
                  this.updateWare()
                }
                break
            }
          })
        })
      }
      if (this.nodeList.feature === 'vedio') {
        let onlineTableId = vm.nodeList.onlineTableId
        let onlineDataId = vm.nodeList.onlineDataId
        o_getAction('/cgform/api/form/' + onlineTableId + '/' + onlineDataId)
          .then((res) => {
            this.videoSrc = res.result.video
            //this.videoSrc = 'https://mark12581.oss-cn-beijing.aliyuncs.com/01-01%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4'
            this.videoName = this.nodeList.name
            this.videoDialogVisible = true
            this.videoIF = true
          })
          .catch((err) => {
            console.log(err)
          })
      }
      this.visible = false
      this.infoDialogVisible= true
      this.showPopover=false
      this.repaintEverything()
      // })
      // .catch((err) => {
      //   console.log('err', err)
      // })
    },
    handleInfoOk() {
      this.infoDialogVisible = false
      this.infoDialogName = ""
      this.infoDialogRole = ""
    },
    updateWare() {
      this.nodeList.isClicked = true
      this.nodeList.status = 'success'
      this.data1.nodeList[this.index] = this.nodeList
      let params = this.ware
      params.plan_content = JSON.stringify(this.data1)
      putAction(this.navigationUrl.updateUrl, params).then((res) => {
        // this.loadWareContent()
      })
    },
    async handelWrong() {
      //学员点击不符合
      // console.log('学员点击不符合', this.judgeDemo_3D_data)
      await this.judgeRightOrWrong(this.judgeDemo_3D_data)

      if (!this.result) {
        this.$message({
          message: '判断正确。但模型本身错误，需要选出正确模型',
          type: 'success',
          customClass: 'zZindex',
        })
        this.demo_3d_all_data.splice(this.randomNumber, 1)
      } else {
        this.$message({ message: '判断错误！请再次判断！', type: 'warning', customClass: 'zZindex' })
      }
      this.$nextTick(() => {
        this.loading3DModel()
      })
      this.demoShowFlag = false

      $('#bdIframe').attr('src', 'about:blank')
    },
    async handelCorrect() {
      //学员点击符合
      // console.log('学员点击符合', this.judgeDemo_3D_data)

      await this.judgeRightOrWrong(this.judgeDemo_3D_data) //同步异步需要处理

      if (this.result) {
        this.$message({ message: '判断正确', type: 'success', customClass: 'zZindex' })
        this.updateWare()
        // this.$message.success('判断正确')
      } else {
        this.$message({ message: '判断错误！请再次判断！', type: 'warning', customClass: 'zZindex' })
        // this.$message.warning('判断错误！请再次判断！')
        this.demo_3d_all_data.splice(this.randomNumber, 1)
        // console.log('删掉后的this.demo_3d_all_data', this.demo_3d_all_data)

        this.$nextTick(() => {
          this.loading3DModel()
        })
      }
      this.demoShowFlag = false

      $('#bdIframe').attr('src', 'about:blank')
    },

    async judgeRightOrWrong(data) {
      //判断用户的判断是否正确

      await w_postAction('/cictOrder/judgeModel', JSON.stringify(data)).then((res) => {
        // console.log('接口调取成功')
        // console.log('JSON.stringify(data)', JSON.stringify(data))
        this.result = res.result
        if (typeof this.result != 'boolean') {
          this.$message({
            message: '数据错误，请创建订单或者刷新后重新检测！',
            type: 'warning',
            customClass: 'zZindex',
          })
          this.visible = false
        }
      })
      // const token = Vue.ls.get(ACCESS_TOKEN)
      // axios.defaults.headers.common['X-Access-Token'] = token
      // axios({
      //   url: `/cictOrder/judgeModel`,
      //   method: 'post',
      //   baseURL: 'http://10.162.34.242:27133',
      //   data: data,
      // }).then((res) => {
      //   console.log('接口调取成功')
      //   result=res.success
      // })
    },
    loading3DModel() {
      this.randomNumber = Math.round(Math.random() * (this.demo_3d_all_data.length - 1))
      if (this.randomNumber < 0) {
        this.randomNumber = 0
      }
      console.log('this.randomNumber', this.randomNumber)
      this.demo_3d_data = this.demo_3d_all_data[this.randomNumber]
      this.judgeDemo_3D_data.color = this.demo_3d_data.color
      this.judgeDemo_3D_data.shape = this.demo_3d_data.shape
      this.judgeDemo_3D_data.powerSize = this.demo_3d_data.powerSize
      this.handleClick(this.demo_3d_data)
    },

    handleClick(row) {
      // this.openFullScreen2()

      this.globalObjurl = row.objurl
      this.globalMtlurl = row.mtlurl
      this.demoShowFlag = true
      setTimeout(() => {
        //页面宽高自适应
        const oIframe = document.getElementById('bdIframe')
        const deviceWidth = document.documentElement.clientWidth
        const deviceHeight = document.documentElement.clientHeight
        oIframe.style.width = Number(deviceWidth) * 0.8 - 40 + 'px' //数字是页面布局宽度差值
        oIframe.style.height = Number(deviceHeight) * 0.7 - 120 + 'px' //数字是页面布局高度差
        console.log('handleClick objurl', row.objurl)
        console.log('handleClick mtlurl', row.mtlurl)

        oIframe.contentWindow.objurl = row.objurl //将obj和mtl地址路径传到iframe里面
        oIframe.contentWindow.mtlurl = row.mtlurl

        // oIframe.contentWindow.location.reload(true)
        // document.getElementById('bdIframe').location.reload()
        // console.log("oIframe.contentWindow.objurl",oIframe.contentWindow.objurl);
        // oIframe.contentWindow.init(row.objurl, row.mtlurl)
      })
    },
    // openFullScreen2() {
    //   const loading = this.$loading({
    //     lock: true,
    //     text: '模型正在渲染，可以点击模型时即渲染完毕。请稍等片刻。',
    //     spinner: 'el-icon-loading',
    //     background: 'rgba(0, 0, 0, 0.7)',
    //   })
    //   setTimeout(() => {
    //     loading.close()
    //   }, 6000)
    // },
    handleCancel() {
      this.visible = false
    },
    handleClose() {
     
    },
    handleCloseT() {
      this.timeDialogVisible = false
    },
    //点击节点
    clickNode(node, index) {
      console.log('触发了tttt', node)
      this.index = index
      if (node.feature === 'task') {
        if (node.state === 'waiting') {
          this.visible = true
          this.nodeList = node
        } else {
          let params = {}
          params.userId = Vue.ls.get(USER_ID)
          order_getAction('/identityTraceability/get', params)
            .then((res) => {
              const { model, order, product } = res.result

              this.timeDialogVisible = true
              this.orderList = order
              this.modelList = model
              this.productList = product
            })
            .catch((err) => {
              console.log('err', err)
            })
        }
      }
    },
    finishF(flag) {
      this.finishFlag = flag
    },
    handleCloseV(done) {
      if (this.finishFlag == 0) {
        this.$message({
          type: 'warning',
          message: '请完整观看视频！',
        })
      } else if (this.finishFlag == 1) {
        this.$elementConfirm('确认关闭教学视频吗？')
          .then((_) => {
            done()
            this.videoIF = false
            this.updateWare()
            this.finishFlag = 3
          })
          .catch((_) => {})
      } else {
        this.$elementConfirm('确认关闭教学视频吗？')
          .then((_) => {
            done()
            this.videoIF = false
            this.finishFlag = 3
          })
          .catch((_) => {})
      }
    },

    toggle() {
      this.collapsed = !this.collapsed
      this.setSidebar(!this.collapsed)
      triggerWindowResizeEvent()
    },
    menuSelect() {
      if (!this.isDesktop()) {
        this.collapsed = false
      }
    },
    //update-begin-author:taoyan date:20190430 for:动态路由title显示配置的菜单title而不是其对应路由的title
    myMenuSelect(value) {
      //此处触发动态路由被点击事件
      this.findMenuBykey(this.menus, value.key)
      this.$emit('dynamicRouterShow', value.key, this.activeMenu.meta.title)
      // update-begin-author:sunjianlei date:20191223 for: 修复刷新后菜单Tab名字显示异常
      let storeKey = 'route:title:' + this.activeMenu.path
      this.$ls.set(storeKey, this.activeMenu.meta.title)
      // update-end-author:sunjianlei date:20191223 for: 修复刷新后菜单Tab名字显示异常
    },
    findMenuBykey(menus, key) {
      for (let i of menus) {
        if (i.path == key) {
          this.activeMenu = { ...i }
        } else if (i.children && i.children.length > 0) {
          this.findMenuBykey(i.children, key)
        }
      }
    },
    //update-end-author:taoyan date:20190430 for:动态路由title显示配置的菜单title而不是其对应路由的title

    // jsPlumb修改
    async show() {
      // // if (this.ware_content) {
      // //   console.log("ware_content:"+JSON.parse(this.ware_content))
      // //   this.dataReload1(JSON.parse(this.ware_content))
      // // } else {
      //   // this.$message.warning('暂无课件信息')
      //   var url = '/business/getDefaultInformation';
      //   var params = {
      //     processId: this.processId,
      //     planId: this.planId,
      //     lessonId: this.lessonId,
      //     teachingMode: this.teachingMode
      //   }
      //   this.data1.lineList = [];
      //   //this.jsPlumb1.reset();
      //   await ce_getWares2(url,params).then((res)=>{
      //     var wareJson = res.result.ware.returnJson;    
      //     this.ware_content=JSON.stringify(wareJson);
      //     this.dataReload1(JSON.parse(this.ware_content))
      //   })
      // // }
    },
    dataReload1(data) {
      var data1 = {}
      this.data1.nodeList = []
      this.data1.lineList = []
      this.$nextTick(() => {
        data1 = lodash.cloneDeep(data)
        this.data1 = data1
        this.$nextTick(() => {
          this.jsPlumb1 = jsPlumb.getInstance()
          this.$nextTick(() => {
            this.jsPlumbInit(this.jsPlumb1, this.data1, '1')
          })
        })
      })
    },
    dataReload2(data) {
      var data2 = {}
      data2 = JSON.parse(JSON.stringify(data))
      this.data2.nodeList = []
      this.data2.lineList = []
      for (var i = 0; i < data2.nodeList.length; i++) {
        data2.nodeList[i].id = data2.nodeList[i].id + '1'
      }
      for (var i = 0; i < data2.lineList.length; i++) {
        data2.lineList[i].from = data2.lineList[i].from + '1'
        data2.lineList[i].to = data2.lineList[i].to + '1'
      }
      this.$nextTick(() => {
        data2 = lodash.cloneDeep(data2)
        this.data2 = data2
        this.$nextTick(() => {
          this.jsPlumb2 = jsPlumb.getInstance()
          this.$nextTick(() => {
            this.jsPlumbInit(this.jsPlumb2, this.data2, '2')
          })
        })
      })
    },
    jsPlumbInit(jsPlumb, data, n) {
      jsPlumb.ready(() => {
        // 导入默认配置
        jsPlumb.importDefaults(this.jsplumbSetting)
        // 会使整个jsPlumb立即重绘。
        jsPlumb.setSuspendDrawing(false, true)
        // 初始化节点
        this.loadEasyFlow(jsPlumb, data)
        if (n == '1') {
          jsPlumb.setContainer(this.$refs.efContainer1)
        } else {
          this.jsPlumb2.setContainer(this.$refs.efContainer2)
        }
      })
    },
    loadEasyFlow(jsPlumb, data) {
      console.log(data)
      // 初始化节点
      for (var i = 0; i < data.nodeList.length; i++) {
        let node = data.nodeList[i]
        // 设置源点，可以拖出线连接其他节点
        jsPlumb.makeSource(node.id, lodash.merge(this.jsplumbSourceOptions, {}))
        // // 设置目标点，其他源点拖出的线可以连接该节点
        jsPlumb.makeTarget(node.id, this.jsplumbTargetOptions)
        if (!node.viewOnly) {
          jsPlumb.draggable(node.id, {
            containment: 'parent',
            stop: function (el) {
              // 拖拽节点结束后的对调
              console.log('拖拽结束: ', el)
            },
          })
        }
      }
      this.$nextTick(() => {
        // 初始化连线
        for (var i = 0; i < data.lineList.length; i++) {
          let line = data.lineList[i]
          var connParam = {
            source: line.from,
            target: line.to,
            label: line.label ? line.label : '',
            connector: line.connector ? line.connector : '',
            anchors: line.anchors ? line.anchors : undefined,
            paintStyle: {
              // 线的颜色
              stroke: '#CCCCCC',
              // 线的粗细，值越大线越粗
              strokeWidth: 1,
              // 设置外边线的颜色，默认设置透明，这样别人就看不见了，点击线的时候可以不用精确点击，参考 https://blog.csdn.net/roymno2/article/details/72717101
              outlineStroke: 'transparent',
              // 线外边的宽，值越大，线的点击范围越大
              outlineWidth: 10,
            },
          }
          jsPlumb.connect(connParam, this.jsplumbConnectOptions)
        }
        this.$nextTick(function () {
          jsPlumb.repaintEverything()
          this.loadEasyFlowFinish = true
        })
      })
    },
    fun1(ev) {
      var height = $('.mask').height() / 2.5
      var width = $('.mask').width() / 2.5
      var maskOffsetHeight = $('.mask')[0].offsetHeight / 2.5
      var maskOffsetWidth = $('.mask')[0].offsetWidth / 2.5
      $('.animation').css({
        left: `${-(ev.pageX - $('.el-popover').offset().left) * 5.25 + maskOffsetWidth + height + 'px'}`,
        top: `${-(ev.pageY - $('.el-popover').offset().top) * 4.8 + maskOffsetHeight + width + 'px'}`,
      })
    },
    repaintEverything() {
      this.jsPlumb2.repaintEverything();
      // this.$nextTick(()=>{
      //   this.jsPlumb2.repaintEverything();
      // })
      
    },
    mousemove(ev) {
      if (ev.srcElement.childNodes.length != 0) {
        $('.mask').css({ display: 'block' })
        this.fun1(ev)
        this.repaintEverything()
      }
    },
    mouseout() {
      $('.mask').css({ display: 'none' })
      this.leftArr = []
      this.topArr = []
    },
    // jsPlumb修改
    ...mapGetters(['nickname', 'avatar', 'userInfo']),
    getAvatar() {
      this.boxNum=this.userInfo().workNo
      return "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
    },
    isShowBoxCard() {
      let url = this.$route.path;
			url = url.split('/');
      if(url[url.length-1]=="analysis"){
        this.showBoxCard=true
      }else{
        this.showBoxCard=false
      }
    },

    clickUploadHandbook() {
      // let params ={
      //   lessonId: '140402'
      // }
      // axios({
      //   url:'https://ii2.wust.edu.cn/coursewareengine/business/downLoadLessonFile',
      //   params:params,
      //   method: 'get'
      // }).then(res => {
        
      // })
    }
  },
}
</script>

<style lang="less">
.zZindex {
  z-index: 9999 !important;
}
.el-popover.area-popover {
  z-index: 999999 !important;
  height: 100%;
  right: 0px;
  top: 0px;
  // transform: translateX(60px);
}
body {
  // 打开滚动条固定显示
  overflow-y: scroll;

  &.colorWeak {
    filter: invert(80%);
  }
}

.layout {
  min-height: 100vh !important;
  overflow-x: hidden;

  &.mobile {
    .ant-layout-content {
      .content {
        margin: 24px 0 0;
      }
    }

    /**
       * ant-table-wrapper
       * 覆盖的表格手机模式样式，如果想修改在手机上表格最低宽度，可以在这里改动
       */
    .ant-table-wrapper {
      .ant-table-content {
        overflow-y: auto;
      }
      .ant-table-body {
        min-width: 800px;
      }
    }
    .sidemenu {
      .ant-header-fixedHeader {
        &.ant-header-side-opened,
        &.ant-header-side-closed {
          width: 100%;
        }
      }
    }

    .topmenu {
      /* 必须为 topmenu  才能启用流式布局 */
      &.content-width-Fluid {
        .header-index-wide {
          margin-left: 0;
        }
      }
    }
    .header,
    .top-nav-header-index {
      .user-wrapper .action {
        padding: 0 12px;
      }
    }
  }

  &.ant-layout-has-sider {
    flex-direction: row;
  }

  .trigger {
    font-size: 22px;
    line-height: 42px;
    padding: 0 18px;
    cursor: pointer;
    transition: color 300ms, background 300ms;

    &:hover {
      background: rgba(255, 255, 255, 0.3);
    }
  }

  .topmenu {
    .ant-header-fixedHeader {
      position: fixed;
      top: 0;
      right: 0;
      z-index: 9;
      width: 100%;
      transition: width 0.2s;

      &.ant-header-side-opened {
        width: 100%;
      }

      &.ant-header-side-closed {
        width: 100%;
      }
    }
    /* 必须为 topmenu  才能启用流式布局 */
    &.content-width-Fluid {
      .header-index-wide {
        max-width: unset;
        margin-left: 24px;
      }

      .page-header-index-wide {
        max-width: unset;
      }
    }
  }

  .sidemenu {
    .ant-header-fixedHeader {
      position: fixed;
      top: 0;
      right: 0;
      z-index: 9;
      width: 100%;
      transition: width 0.2s;

      &.ant-header-side-opened {
        width: calc(100% - 200px);
      }

      &.ant-header-side-closed {
        width: calc(100% - 80px);
      }
    }
  }

  .header {
    height: 64px;
    padding: 0 12px 0 0;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    position: relative;
  }

  .header,
  .top-nav-header-index {
    .user-wrapper {
      float: right;
      height: 100%;

      .action {
        cursor: pointer;
        padding: 0 14px;
        display: inline-block;
        transition: all 0.3s;

        height: 70%;
        line-height: 46px;

        &.action-full {
          height: 100%;
        }

        &:hover {
          background: rgba(255, 255, 255, 0.3);
        }

        .avatar {
          margin: 20px 10px 20px 0;
          color: #1890ff;
          background: hsla(0, 0%, 100%, 0.85);
          vertical-align: middle;
        }

        .icon {
          font-size: 16px;
          padding: 4px;
        }

        .anticon {
          color: inherit;
        }
      }
    }

    &.dark {
      .user-wrapper {
        .action {
          color: black;

          &:hover {
            background: rgba(0, 0, 0, 0.05);
          }

          .anticon {
            color: inherit;
          }
        }
      }
    }
  }

  &.mobile {
    .top-nav-header-index {
      .header-index-wide {
        .header-index-left {
          .trigger {
            color: rgba(255, 255, 255, 0.85);
            padding: 0 12px;
          }

          .logo.top-nav-header {
            text-align: center;
            width: 56px;
            line-height: 58px;
          }
        }
      }

      .user-wrapper .action .avatar {
        margin: 20px 0;
      }

      &.light {
        .header-index-wide {
          .header-index-left {
            .trigger {
              color: rgba(0, 0, 0, 0.65);
            }
          }
        }
        //
      }
    }
  }

  &.tablet {
    // overflow: hidden; text-overflow:ellipsis; white-space: nowrap;
    .top-nav-header-index {
      .header-index-wide {
        .header-index-left {
          .logo > a {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
      }
    }
  }

  .top-nav-header-index {
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    position: relative;
    transition: background 0.3s, width 0.2s;

    .header-index-wide {
      width: 100%;
      margin: auto;
      padding: 0 20px 0 0;
      display: flex;
      height: 59px;

      .ant-menu.ant-menu-horizontal {
        border: none;
        height: 64px;
        line-height: 64px;
      }

      .header-index-left {
        flex: 1 1;
        display: flex;

        .logo.top-nav-header {
          width: 165px;
          height: 64px;
          position: relative;
          line-height: 64px;
          transition: all 0.3s;
          overflow: hidden;

          img {
            display: inline-block;
            vertical-align: middle;
            height: 32px;
          }

          h1 {
            color: #fff;
            display: inline-block;
            vertical-align: top;
            font-size: 16px;
            margin: 0 0 0 12px;
            font-weight: 400;
          }
        }
      }

      .header-index-right {
        float: right;
        height: 59px;
        overflow: hidden;
        .action:hover {
          background-color: rgba(0, 0, 0, 0.05);
        }
      }
    }

    &.light {
      background-color: #fff;

      .header-index-wide {
        .header-index-left {
          .logo {
            h1 {
              color: #002140;
            }
          }
        }
      }
    }

    &.dark {
      .user-wrapper {
        .action {
          color: white;

          &:hover {
            background: rgba(255, 255, 255, 0.3);
          }
        }
      }
      .header-index-wide .header-index-left .trigger:hover {
        background: rgba(255, 255, 255, 0.3);
      }
    }
  }

  // 内容区
  .layout-content {
    margin: 24px 24px 0px;
    height: 64px;
    padding: 0 12px 0 0;
  }
}

.topmenu {
  .page-header-index-wide {
    margin: 0 auto;
    width: 100%;
  }
  /deep/ .ant-menu-dark {
    background-color: rgb(48, 65, 86);
    /deep/ .ant-menu-submenu:hover {
      background-color: #263445;
    }
    /deep/ .ant-menu-item:hover {
      background-color: #263445;
    }
  }
  /deep/ .ant-menu.ant-menu-dark .ant-menu-item-selected {
    background-color: #263445;
  }
  /deep/ .ant-menu.ant-menu-dark .ant-menu-item-selected i {
    color: rgb(24, 144, 255);
  }
  /deep/ .ant-menu.ant-menu-dark .ant-menu-item-selected span {
    color: rgb(24, 144, 255);
  }
  /deep/ .ant-menu-dark .ant-menu-submenu-active {
    color: #ffffff !important;
  }
}
.dark.header-index-right {
  background-color: rgb(48, 65, 86) !important;
}
.layout .top-nav-header-index.dark .user-wrapper .action:hover {
  background-color: #263445 !important;
}
.layout .top-nav-header-index .dark .user-wrapper .action i {
  color: #ffffff !important;
}
.layout .top-nav-header-index .user-wrapper .action .anticon {
  color: inherit !important;
}
.dark.ant-dropdown-menu {
  background-color: #999999;
}
// drawer-sider 自定义
.ant-drawer.drawer-sider {
  .sider {
    box-shadow: none;
  }

  &.dark {
    .ant-drawer-content {
      background-color: rgb(0, 21, 41);
    }
  }
  &.light {
    box-shadow: none;
    .ant-drawer-content {
      background-color: #fff;
    }
  }

  .ant-drawer-body {
    padding: 0;
  }
}

// 菜单样式
.sider {
  box-shadow: 2px 116px 6px 0 rgba(0, 21, 41, 0.35);
  position: relative;
  z-index: 10;

  &.ant-fixed-sidemenu {
    position: fixed;
    height: 100%;
  }

  .logo {
    height: 64px;
    position: relative;
    line-height: 64px;
    padding-left: 24px;
    -webkit-transition: all 0.3s;
    transition: all 0.3s;
    background: #002140;
    overflow: hidden;

    img,
    h1 {
      display: inline-block;
      vertical-align: middle;
    }

    img {
      height: 32px;
    }

    h1 {
      color: #fff;
      font-size: 18px;
      margin: 0 0 0 8px;
      font-family: 'Chinese Quote', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB',
        'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji',
        'Segoe UI Symbol';
      font-weight: 600;
    }
  }

  &.light {
    background-color: #fff;
    box-shadow: 2px 116px 8px 0 rgba(29, 35, 41, 0.05);

    .logo {
      background: #fff;
      box-shadow: 1px 1px 0 0 #e8e8e8;

      h1 {
        color: unset;
      }
    }

    .ant-menu-light {
      border-right-color: transparent;
    }
  }
}

// 外置的样式控制
.user-dropdown-menu-wrapper.ant-dropdown-menu {
  padding: 4px 0;

  .ant-dropdown-menu-item {
    width: 160px;
  }

  .ant-dropdown-menu-item > .anticon:first-child,
  .ant-dropdown-menu-item > a > .anticon:first-child,
  .ant-dropdown-menu-submenu-title > .anticon:first-child .ant-dropdown-menu-submenu-title > a > .anticon:first-child {
    min-width: 12px;
    margin-right: 8px;
  }
}

// 数据列表 样式
.table-alert {
  margin-bottom: 16px;
}

.table-page-search-wrapper {
  .ant-form-inline {
    .ant-form-item {
      display: flex;
      margin-bottom: 24px;
      margin-right: 0;

      .ant-form-item-control-wrapper {
        flex: 1 1;
        display: inline-block;
        vertical-align: middle;
      }

      > .ant-form-item-label {
        line-height: 32px;
        padding-right: 8px;
        width: auto;
      }
      .ant-form-item-control {
        height: 32px;
        line-height: 32px;
      }
    }
  }

  .table-page-search-submitButtons {
    display: block;
    margin-bottom: 24px;
    white-space: nowrap;
  }
}

.content {
  .table-operator {
    margin-bottom: 18px;

    button {
      margin-right: 8px;
    }
  }
}
.ant-menu-dark .ant-menu-vertical.ant-menu-sub li:hover {
  background-color: #001528;
}
.ant-menu-submenu-popup.ant-menu-dark .ant-menu-item-selected {
  background-color: #001528 !important;
}
.ant-menu-submenu-popup.ant-menu-dark .ant-menu-item-selected span {
  color: rgb(24, 144, 255);
}
.ant-menu-submenu-popup.ant-menu-dark .ant-menu-item-selected i {
  color: rgb(24, 144, 255);
}
.ant-menu-dark .ant-menu-sub {
  background: #1f2d3d !important;
  .ant-menu-submenu-open,
  .ant-menu-submenu-active {
    color: #ffffff !important;
  }
}
</style>
<style scoped>
#efContainer {
  transform: scale(0.8) translate(-12%, 10%);
  text-align: center;
  left: 0;
  top: 0;
  margin-top: 8% !important;
  overflow: visible;
  cursor: pointer;
}
.mask {
  transform: scale(2.5);
  position: absolute;
  left: -310%;
  top: -185%;
  overflow: hidden;
  background-color: white;
  height: 90%;
  width: 90%;
  border-radius: 50%;
  display: none;
  box-shadow: gray 5px 5px 30px 5px;
}
.animation {
  position: absolute;
  top: 0;
  left: 0;
}
.navigation {
  position: fixed;
  left: 80%;
  top: 85%;
  z-index: 9999999;
  width: 60px;
  height: 60px;
}
.jtk-overlay {
  cursor: pointer;
  color: #4a4a4a;
}

/* 连线中的label 样式*/
.jtk-overlay.flowLabel:not(.aLabel) {
  padding: 4px 10px;
  background-color: white;
  color: #565758 !important;
  border: 1px solid #e0e3e7;
  border-radius: 5px;
  z-index: 3500;
}
/* .el-dialog {
  overflow: hidden !important;

} */
.orderDetail {
  text-align: center;
  font-size: 24px;
  font-family: 'Microsoft YaHei', 微软雅黑, 'MicrosoftJhengHei', 华文细黑, STHeiti, MingLiu;
}
.el-divider--horizontal {
  margin: 8px 0;
  background: 0 0;
  border-top: 1px dashed #111111;
}

/* .el-dialog__body{
   padding: 10px 20px !important;
 } */

 
.box-card-area {
  position: absolute;
  right:  0;
  top: 0;
  /* width: 336px; */
   width: 20%;
  margin-top: 111px;
 }
.box-card {
  width: 70%;
  margin-left: 20%;
  margin-right: 10%;
}
.box-name {
  text-align: center;
  font-size: 24px;
  font-family: Roboto;
  font-weight: bold;
  line-height: 72px;
  color: #333333;
  opacity: 1;
}

.box-avatar {
  left: 49px;
  right: 49px;
  width: 95px;
  height: 95px;
  background: rgba(0, 0, 0, 0);
  border: 4px solid rgba(94, 157, 248, 0.4470588235294118);
}

.box-info {
  font-size: 20px;
  font-family: Roboto;
  font-weight: 500;
  line-height: 32px;
  color: #333333;
  opacity: 1;
}

.box-btn {
  margin:10% 20% 0 20%;
}

#finishPanelBtn{
  /* width: 100%; */
  font-size: 16px;
  font-family: Roboto-Medium;
  line-height: 21px;
  text-align: center;
  color: #5B7CFD;
  opacity: 1;
  margin-bottom: 10%;
}
#percentage{
  width: 100%;
  font-size: 16px;
  font-family: Roboto-Medium;
  line-height: 21px;
  text-align: center;
  color: #5B7CFD;
  opacity: 1;
  margin-bottom: 10%;
}
#openPanelBtn {
  /* width: 100%; */
  font-size: 16px;
  font-family: Roboto-Medium;
  line-height: 21px;
  text-align: center;
  color: #5B7CFD;
  opacity: 1;
  margin-bottom: 10%;
}

#uploadHandbook {
  /* width: 100%; */
  font-size: 12px;
  text-align: center;
  font-family: Roboto-Medium;
  line-height: 21px;
  color: #5B7CFD;
  opacity: 1;
  margin-bottom: 10%;
}

#elPopover {
  /* width: */
}

</style>
