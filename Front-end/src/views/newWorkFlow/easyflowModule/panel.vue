<template>
  <div>
    <a-modal
      :visible="panelVisible"
      :footer="null"
      width="100%"
      :centered="true"
      :zIndex="300"
      :closable="false"
      :destroyOnClose="true"
    >
      <div v-if="easyFlowVisible" id="easyflow">
        <a-layout>
          <a-layout-sider width="200px">
            <div>
              <!--左侧节点列表-->
              <node-menu @addNode="addNode" ref="nodeMenu"></node-menu>
            </div>
          </a-layout-sider>
          <a-layout>
            <a-layout-header>
              <div class="header-content flex-col">
                <div class="header-content1 flex-row">
                  <div class="header-content-line flex-col"></div>
                  <span class="header-content-title">流程画板&nbsp&nbsp{{ data.name }}</span>
                  <div class="header-content-close flex-col" @click="closeEasyflow">
                    <span class="header-content-close-info">关闭画板</span>
                  </div>
                  <div class="header-content-resize flex-col">
                    <div class="header-content-resize-content flex-row">
                      <img
                        class="header-content-resize-icon"
                        referrerpolicy="no-referrer"
                        src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngb5918a658f15561742b01480d40d86ea095c1167d0488e1aeb34ac80ab504032"
                        @click="zoomAdd"
                      />
                      <!-- <span class="header-content-resize-text">60%</span> -->
                      <img
                        class="header-content-resize-icon"
                        referrerpolicy="no-referrer"
                        src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng52340645a6e083d09573d95e0d72e10e6cee2216bddc36565f280e7cfe41dda4"
                        @click="zoomSub"
                      />
                    </div>
                  </div>
                  <div class="header-content-delete flex-col">
                    <img
                      class="header-content-delete-icon"
                      referrerpolicy="no-referrer"
                      src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng4eca4d62c93f4a561c9afcffd5af328ea5de4dfbc9bb734ceae574e56eca7bae"
                      @click="deleteElement"
                    />
                  </div>
                  <div class="header-content-flow flex-col" @click="dataInfo">
                    <span class="header-content-flow-info">流程信息</span>
                  </div>
                  <div v-if="isNotDeploy" class="header-content-save flex-col" @click="saveFlow">
                    <span class="header-content-save-info">保存画板</span>
                  </div>
                </div>
              </div>
              <!--顶部工具菜单-->
              <!-- <el-button
                    type="text"
                    icon="el-icon-delete"
                    size="large"
                    @click="deleteElement"
                    :disabled="!this.activeElement.type"
                  ></el-button>
                  <el-button type="text" icon="el-icon-download" size="large" @click="downloadData"></el-button>
                    <el-button type="primary" plain round icon="el-icon-back" @click="back" size="mini">返回</el-button> -->
            </a-layout-header>
            <a-layout-content>
              <div id="efContainer" ref="efContainer" v-flowDrag>
                <flow-node
                  v-for="node in data.nodeList"
                  :id="node.id"
                  :key="node.id"
                  :node="node"
                  type="design"
                  :activeElement="activeElement"
                  @changeNodeSite="changeNodeSite"
                  @clickNode="clickNode"
                >
                </flow-node>
                <!-- 给画布一个默认的宽度和高度 -->
                <div style="position: absolute; top: 2000px; left: 2000px">&nbsp;</div>
              </div>
            </a-layout-content>
          </a-layout>
          <a-layout-sider width="317px">
            <div>
              <flow-node-form
                ref="nodeForm"
                @setLineLabel="setLineLabel"
                @repaintEverything="repaintEverything"
                :data="data"
              ></flow-node-form>
            </div>
          </a-layout-sider>
        </a-layout>
        <!-- 流程数据详情 -->
        <flow-info v-if="flowInfoVisible" ref="flowInfo" :data="data" :dataReload="dataReload"></flow-info>
        <flow-help v-if="flowHelpVisible" ref="flowHelp"></flow-help>
        <saveMessage ref="saveMessage" :data="data"></saveMessage>
      </div>
    </a-modal>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
// import { jsPlumb } from 'jsplumb'
// 使用修改后的jsplumb
import './jsplumb'
import { easyFlowMixin } from './mixins'
import flowNode from './node'
import nodeMenu from './node_menu'
import FlowInfo from './info'
import FlowNodeForm from './node_form'
import lodash from 'lodash'
import { getDataA } from './data_A'
import { getDataB } from './data_B'
import saveMessage from './saveMessage'
import { nw_postAction1 } from '@api/newWorkApi'
export default {
  data() {
    return {
      isNotDeploy: true,
      saveFlag: '',
      panelVisible: false,
      // jsPlumb 实例
      jsPlumb: null,
      // 控制画布销毁
      easyFlowVisible: true,
      // 控制流程数据显示与隐藏
      flowInfoVisible: false,
      // 是否加载完毕标志位
      loadEasyFlowFinish: false,
      flowHelpVisible: false,
      // 数据
      data: {},
      // 激活的元素、可能是节点、可能是连线
      activeElement: {
        // 可选值 node 、line
        type: undefined,
        // 节点ID
        nodeId: undefined,
        // 连线ID
        sourceId: undefined,
        targetId: undefined,
      },
      zoom: 1,
    }
  },
  // 一些基础配置移动该文件中
  mixins: [easyFlowMixin],
  // props: {
  //   isNotDeploy: {
  //     type: Boolean,
  //     default: true,
  //   },
  // },
  components: {
    draggable,
    flowNode,
    nodeMenu,
    FlowInfo,
    FlowNodeForm,
    saveMessage,
  },
  directives: {
    flowDrag: {
      bind(el, binding, vnode, oldNode) {
        if (!binding) {
          return
        }
        el.onmousedown = (e) => {
          if (e.button == 2) {
            // 右键不管
            return
          }
          //  鼠标按下，计算当前原始距离可视区的高度
          let disX = e.clientX
          let disY = e.clientY
          el.style.cursor = 'move'

          document.onmousemove = function (e) {
            // 移动时禁止默认事件
            e.preventDefault()
            const left = e.clientX - disX
            disX = e.clientX
            el.scrollLeft += -left

            const top = e.clientY - disY
            disY = e.clientY
            el.scrollTop += -top
          }

          document.onmouseup = function (e) {
            el.style.cursor = 'auto'
            document.onmousemove = null
            document.onmouseup = null
          }
        }
      },
    },
  },
  mounted() {
    // window.addEventListener('mousewheel', this.handleScroll, false)
  },
  methods: {
    // 返回唯一标识
    getUUID() {
      return Math.random().toString(36).substr(3, 10)
    },
    jsPlumbInit() {
      this.jsPlumb.ready(() => {
        // 导入默认配置
        this.jsPlumb.importDefaults(this.jsplumbSetting)
        // 会使整个jsPlumb立即重绘。
        this.jsPlumb.setSuspendDrawing(false, true)
        // 初始化节点
        this.loadEasyFlow()
        // 单点击了连接线, https://www.cnblogs.com/ysx215/p/7615677.html
        this.jsPlumb.bind('click', (conn, originalEvent) => {
          this.activeElement.type = 'line'
          this.activeElement.sourceId = conn.sourceId
          this.activeElement.targetId = conn.targetId
          this.$refs.nodeForm.lineInit({
            from: conn.sourceId,
            to: conn.targetId,
            label: conn.getLabel(),
          })
        })
        // 连线
        this.jsPlumb.bind('connection', (evt) => {
          let from = evt.source.id
          let to = evt.target.id
          if (this.loadEasyFlowFinish) {
            this.data.lineList.push({ from: from, to: to })
          }
        })

        // 删除连线回调
        this.jsPlumb.bind('connectionDetached', (evt) => {
          this.deleteLine(evt.sourceId, evt.targetId)
        })

        // 改变线的连接节点
        this.jsPlumb.bind('connectionMoved', (evt) => {
          this.changeLine(evt.originalSourceId, evt.originalTargetId)
        })

        // 连线右击
        this.jsPlumb.bind('contextmenu', (evt) => {
          console.log('contextmenu', evt)
        })

        // 连线
        this.jsPlumb.bind('beforeDrop', (evt) => {
          console.log(evt, this.data)
          let from = evt.sourceId
          let to = evt.targetId
          if (from === to) {
            this.$message.error('节点不支持连接自己')
            return false
          }
          if (this.hasLine(from, to)) {
            this.$message.error('该关系已存在,不允许重复创建')
            return false
          }
          if (this.hashOppositeLine(from, to)) {
            this.$message.error('不支持两个节点之间连线回环')
            return false
          }
          if (this.beTask(from, to)) {
            this.$message.error('逻辑节点的前节点必须是人工任务节点')
            return false
          }
          this.$message.success('连接成功')
          return true
        })

        // beforeDetach
        this.jsPlumb.bind('beforeDetach', (evt) => {
          console.log('beforeDetach', evt)
        })
        this.jsPlumb.setContainer(this.$refs.efContainer)
      })
    },
    // 加载流程图
    loadEasyFlow() {
      // 初始化节点
      this.data.nodeList.forEach((item) => {
        let node = item
        // 设置源点，可以拖出线连接其他节点
        console.log(node.id, lodash.merge(this.jsplumbSourceOptions, {}))
        this.jsPlumb.makeSource(node.id, lodash.merge(this.jsplumbSourceOptions, {}))
        // // 设置目标点，其他源点拖出的线可以连接该节点
        this.jsPlumb.makeTarget(node.id, this.jsplumbTargetOptions)
        if (!node.viewOnly) {
          this.jsPlumb.draggable(node.id, {
            containment: 'parent',
            stop: function (el) {
              // 拖拽节点结束后的对调
              console.log('拖拽结束: ', el)
            },
          })
        }
      })
      this.$nextTick(() => {
        // 初始化连线
        this.data.lineList.forEach((item) => {
          let line = item
          var connParam = {
            source: line.from,
            target: line.to,
            label: line.label ? line.label : '',
            connector: line.connector ? line.connector : '',
            anchors: line.anchors ? line.anchors : undefined,
            paintStyle: line.paintStyle ? line.paintStyle : undefined,
          }
          this.jsPlumb.connect(connParam, this.jsplumbConnectOptions)
        })
        this.$nextTick(function () {
          this.jsPlumb.repaintEverything()
          this.loadEasyFlowFinish = true
        })
      })
    },
    // 设置连线条件
    setLineLabel(from, to, label) {
      var conn = this.jsPlumb.getConnections({
        source: from,
        target: to,
      })[0]
      if (!label || label === '') {
        conn.removeClass('flowLabel')
        conn.addClass('emptyFlowLabel')
      } else {
        conn.addClass('flowLabel')
      }
      conn.setLabel({
        label: label,
      })
      this.data.lineList.forEach(function (line) {
        if (line.from == from && line.to == to) {
          line.label = label
        }
      })
      this.$message.success('保存成功')
    },
    // 删除激活的元素
    deleteElement() {
      if (this.activeElement.type === 'node') {
        this.deleteNode(this.activeElement.nodeId)
      } else if (this.activeElement.type === 'line') {
        this.$elementConfirm('确定删除所点击的线吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            var conn = this.jsPlumb.getConnections({
              source: this.activeElement.sourceId,
              target: this.activeElement.targetId,
            })[0]
            this.jsPlumb.deleteConnection(conn)
          })
          .catch(() => {})
      }
    },
    // 删除线
    deleteLine(from, to) {
      this.data.lineList = this.data.lineList.filter(function (line) {
        if (line.from == from && line.to == to) {
          return false
        }
        return true
      })
    },
    // 改变连线
    changeLine(oldFrom, oldTo) {
      this.deleteLine(oldFrom, oldTo)
    },
    // 改变节点的位置
    changeNodeSite(data) {
      console.log(data)
      for (var i = 0; i < this.data.nodeList.length; i++) {
        let node = this.data.nodeList[i]
        if (node.id === data.nodeId && node.type != 'swinlane') {
          node.left = data.left
          node.top = data.top
        }
        if (node.id === data.nodeId && node.type === 'swinlane') {
          node.left = data.left
          node.top = data.top
          console.log(data.width)
          if (data.width != undefined) {
            node.width = data.width
            node.height = data.height
          }
        }
      }
    },
    /**
     * 拖拽结束后添加新的节点
     * @param evt
     * @param nodeMenu 被添加的节点对象
     * @param mousePosition 鼠标拖拽结束的坐标
     */
    addNode(evt, nodeMenu, mousePosition) {
      console.log(nodeMenu)
      var screenX = evt.originalEvent.clientX,
        screenY = evt.originalEvent.clientY
      let efContainer = this.$refs.efContainer
      var containerRect = efContainer.getBoundingClientRect()
      var left = screenX,
        top = screenY
      // 计算是否拖入到容器中
      if (
        left < containerRect.x ||
        left > containerRect.width + containerRect.x ||
        top < containerRect.y ||
        containerRect.y > containerRect.y + containerRect.height
      ) {
        this.$message.error('请把节点拖入到画布中')
        return
      }
      left = left - containerRect.x + efContainer.scrollLeft
      top = top - containerRect.y + efContainer.scrollTop
      // 居中
      left -= 85
      top -= 16
      left = Math.round(left)
      top = Math.round(top)
      var nodeId = this.getUUID()
      // 动态生成名字
      var origName = nodeMenu.name
      var nodeName = origName
      var index = 1
      while (index < 10000) {
        var repeat = false
        console.log('this.data.nodeList', this.data.nodeList);
        for (var i = 0; i < this.data.nodeList.length; i++) {
          let node = this.data.nodeList[i]
          if (node.name === nodeName) {
            nodeName = origName + index
            repeat = true
          }
        }
        if (repeat) {
          index++
          continue
        }
        break
      }
      const type = nodeMenu.type
      switch (type) {
        case 'start':
          var node = {
            id: nodeId,
            type: nodeMenu.type,
            name: nodeName,
            left: left + 'px',
            top: top + 'px',
            state: nodeMenu.state, //状态
            label: '', // 可以为空
            event_handler_bean: '', // 可以选取事件
            // hander_bean: '', // 处理bean
            //g: '', //描述位置信息
          }
          break
        case 'task':
          var node = {
            id: nodeId,
            type: nodeMenu.type,
            name: nodeName,
            left: left + 'px',
            top: top + 'px',
            button_permission: ["通过","拒绝","退回"], //按钮权限
            state: nodeMenu.state,
            feature: {},
            formDesignerId: '', //表单id
            onlineTableId: '', //online表id
            onlineDataId: '', //online数据id
            task_type: 'Normal',
            event_handler_bean: '', //事件bean
            assignment_type: 'ProcessPromoter', // 如果需要制定参与者则需要做如下信息
            assignee: [], //参与者
            due: {
              // 如果需要设置到期时间等与时间相关的需要以下配置
              day: '',
              hour: '',
              minute: '',
            },
            user_data: {
              // 如果需要有人员信息的话需要添加如下信息
              key: '',
              value: '',
            },
          }
          break
        case 'decision':
          var node = {
            id: nodeId,
            type: nodeMenu.type,
            name: nodeName,
            event_handler_bean: '', //事件bean
            if_expression: [
              {
                formId: '',
                symbol: '',
                textValue: '',
              },
            ], //决策表达式
            result: {},
            left: left + 'px',
            top: top + 'px',
            state: nodeMenu.state,
          }
          break
        case 'end':
          var node = {
            id: nodeId,
            type: nodeMenu.type,
            name: nodeName,
            terminate: 'true',
            left: left + 'px',
            top: top + 'px',
            state: nodeMenu.state,
          }
          break
        case 'swinlane':
          var node = {
            id: nodeId,
            type: nodeMenu.type,
            name: nodeName,
            left: left,
            top: top,
            state: nodeMenu.state,
            width: 120,
            height: 160,
          }
          break
        case 'rectangle':
          var node = {
            id: nodeId,
            type: nodeMenu.type,
            name: nodeName,
            left: left + 'px',
            top: top + 'px',
            role: [],
            state: nodeMenu.state,
            feature: {},
            formDesignerId: '',
            onlineTableId: '',
            onlineDataId: '',
          }
          break
        case 'rhombus':
          var node = {
            id: nodeId,
            type: nodeMenu.type,
            name: nodeName,
            state: nodeMenu.state,
            left: left + 'px',
            top: top + 'px',
          }
          break
        default:
          var node = {
            id: nodeId,
            type: nodeMenu.type,
            name: nodeName,
            left: left + 'px',
            top: top + 'px',
            state: nodeMenu.state,
            feature: {},
            formDesignerId: '',
            onlineTableId: '',
            onlineDataId: '',
          }
      }

      console.log(node)
      /**
       * 这里可以进行业务判断、是否能够添加该节点
       */
      this.data.nodeList.push(node)
      this.$nextTick(function () {
        this.jsPlumb.makeSource(nodeId, this.jsplumbSourceOptions)
        this.jsPlumb.makeTarget(nodeId, this.jsplumbTargetOptions)
        this.jsPlumb.draggable(nodeId, {
          containment: 'parent',
          stop: function (el) {
            // 拖拽节点结束后的对调
            console.log('拖拽结束: ', el)
          },
        })
      })
    },
    /**
     * 删除节点
     * @param nodeId 被删除节点的ID
     */
    deleteNode(nodeId) {
      this.$elementConfirm('确定要删除节点' + nodeId + '?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        closeOnClickModal: false,
      })
        .then(() => {
          /**
           * 这里需要进行业务判断，是否可以删除
           */
          this.data.nodeList = this.data.nodeList.filter(function (node) {
            if (node.id === nodeId) {
              // 伪删除，将节点隐藏，否则会导致位置错位
              // node.show = false
              return false
            }
            return true
          })
          this.$nextTick(function () {
            this.jsPlumb.removeAllEndpoints(nodeId)
          })
        })
        .catch(() => {})
      return true
    },
    //clickNode做了改变，重写
    clickNode(node) {
      const nodeId = node.id
      this.activeElement.type = 'node'
      this.activeElement.nodeId = nodeId
      this.$refs.nodeForm.closeAttributesForm()
      this.$refs.nodeForm.nodeInit(this.data, nodeId) //nodeform的进入口
    },
    // 是否具有该线
    hasLine(from, to) {
      for (var i = 0; i < this.data.lineList.length; i++) {
        var line = this.data.lineList[i]
        if (line.from === from && line.to === to) {
          return true
        }
      }
      return false
    },
    //判断此to节点是不是逻辑节点，如果是from节点是不是人工任务节点，不是返回false，是的话返回true
    beTask(from, to) {
      var fromNode = {}
      var toNode = {}
      for (let i = 0; i < this.data.nodeList.length; i++) {
        let node = this.data.nodeList[i]
        if (node.id === from) {
          fromNode = node
        } else if (node.id === to) {
          toNode = node
        }
      }
      if (toNode.type === 'decision') {
        if (fromNode.type != 'task') {
          return true
        } else {
          return false
        }
      } else {
        return false
      }
    },
    // 是否含有相反的线
    hashOppositeLine(from, to) {
      return this.hasLine(to, from)
    },
    repaintEverything() {
      this.jsPlumb.repaint()
    },
    // 流程数据信息
    dataInfo() {
      console.log('dataInfo')
      this.flowInfoVisible = true
      this.$nextTick(function () {
        this.$refs.flowInfo.init()
      })
    },
    // 加载流程图
    dataReload(data) {
      // 确保 data 是一个对象
    if (typeof data === 'string') {
      data = JSON.parse(data);
    }
      console.log('data', data);
      this.easyFlowVisible = false
      this.data.nodeList = []
      this.data.lineList = []
      this.$nextTick(() => {
        data = lodash.cloneDeep(data)
        this.easyFlowVisible = true
        this.data = data
        this.$nextTick(() => {
          this.jsPlumb = jsPlumb.getInstance()
          this.$nextTick(() => {
            this.jsPlumbInit()
          })
        })
      })
      console.log('流程图加载成功');
    },
    // 模拟载入数据dataA
    dataReloadA() {
      this.dataReload(getDataA())
    },
    handleScroll(e) {
      var direction = e.deltaY > 0 ? 'down' : 'up'
      console.log(direction)
      if (direction == 'down') {
        console.log('11111')
        this.zoom = this.zoom + 0.05
        this.$refs.efContainer.style.transform = `scale(${this.zoom})`
      } else {
        this.zoom = this.zoom - 0.05
        this.$refs.efContainer.style.transform = `scale(${this.zoom})`
      }
    },
    zoomImg() {
      console.log(evt.wheelDelta)
      //       this.zoom+=evt.wheelDelta/12
      // this.$refs.efContainer.style.transform = `scale(${this.zoom})`
      return false
    },
    zoomAdd() {
      if (this.zoom >= 1) {
        return
      }
      this.zoom = this.zoom + 0.05
      this.$refs.efContainer.style.transform = `scale(${this.zoom})`

      this.jsPlumb.repaintEverything()
    },
    zoomSub() {
      if (this.zoom <= 0) {
        return
      }
      this.zoom = this.zoom - 0.05
      this.$refs.efContainer.style.transform = `scale(${this.zoom})`
      this.jsPlumb.repaintEverything()
    },
    // 下载数据
    downloadData() {
      this.$elementConfirm('确定要下载该流程数据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        closeOnClickModal: false,
      })
        .then(() => {
          var datastr = 'data:text/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(this.data, null, '\t'))
          var downloadAnchorNode = document.createElement('a')
          downloadAnchorNode.setAttribute('href', datastr)
          downloadAnchorNode.setAttribute('download', 'data.json')
          downloadAnchorNode.click()
          downloadAnchorNode.remove()
          this.$message.success('正在下载中,请稍后...')
        })
        .catch(() => {})
    },
    openHelp() {
      this.flowHelpVisible = true
      this.$nextTick(function () {
        this.$refs.flowHelp.init()
      })
    },
    //路由决策中加入新的键值对
    addDecisionType() {
      // 遍历 nodeList，找到 type 为 decision 的节点，并添加键值对
      this.data.nodeList.forEach((node) => {
        if (node.type === 'decision') {
          node.decision_type = 'Expression'
        }
      })
    },

    saveFlow() {
      console.log('变化前的data', this.data)
      this.addDecisionType()

      // 去除lineList中的重复项
      const uniqueLineList = this.data.lineList.filter((item, index, self) =>
          index === self.findIndex(t => t.from === item.from && t.to === item.to)
      );
      // 更新原数据
      this.data.lineList = uniqueLineList;
      console.log('变化后的data', this.data)
      //0:新增流程保存;1:编辑保存
      if (this.saveFlag == '0') {
        this.$refs.saveMessage.openModel()
      } else {
        console.log('保存的流程', this.data)
        nw_postAction1(`/design/saveFile`, {
          name: this.data.name,
          content: JSON.stringify(this.data),
          update: true,
        }).then((res) => {
          if (res.result) {
            this.$message.success('编辑流程保存成功')
            this.loading = false
          } else {
            this.$message.error('编辑流程保存失败')
            this.loading = false
          }
        })
      }
    },
    back() {
      this.$emit('back')
    },
    openModel(isNotDeploy,dataInitial, saveFlag) {
      this.isNotDeploy = isNotDeploy
      console.log('当前是不是没部署', this.isNotDeploy);
      console.log('编辑打开的流程', dataInitial)
      //saveFlag:0新增  1编辑
      this.saveFlag = saveFlag
      this.panelVisible = true
      this.jsPlumb = jsPlumb.getInstance()
      this.$nextTick(() => {
        // 默认加载流程A的数据、在这里可以根据具体的业务返回符合流程数据格式的数据即可
        this.dataReload(dataInitial)
        //this.dataReload(getDataA())
      })
    },
    //关闭画板
    closeEasyflow() {
      this.loadEasyFlowFinish = false
      this.jsPlumb.reset() //清除绘画缓存
      this.panelVisible = false
      this.data = {}
      // 激活的元素、可能是节点、可能是连线
      this.activeElement = {
        // 可选值 node 、line
        type: undefined,
        // 节点ID
        nodeId: undefined,
        // 连线ID
        sourceId: undefined,
        targetId: undefined,
      }
      this.$emit('close')
    },
  },
}
</script>
<style scoped>
.flex-col {
  display: flex;
  flex-direction: column;
}
.flex-row {
  display: flex;
  flex-direction: row;
}

#efContainer {
  position: relative;
  overflow: scroll;
  flex: 1;
  height: 100%;
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
  z-index: 300;
}

#easyflow {
  border: 1px;
  width: 100%;
  height: 760px;
}
#easyflow .ant-layout-header {
  width: 100%;
  height: 64px;
  background: #ffffff;
  border: solid 2px #e4e7ed;
  padding: 0;
}
#easyflow .ant-layout-content {
  width: 100%;
  height: 596px;
  background: #f6f7f9;
  border: solid 2px #e4e7ed;
}

#easyflow .ant-layout-sider {
  height: 760px;
  background: #ffffff;
  border: solid 2px #e4e7ed;
}
/*顶部工具栏样式*/
.header-content {
  height: 64px;
  background-color: rgba(255, 255, 255, 1);
  width: 100%;
  justify-content: center;
  align-items: center;
}

.header-content1 {
  z-index: auto;
  width: 95%;
  height: 32px;
}

.header-content-line {
  width: 4px;
  height: 12px;
  border-radius: 2px;
  background-color: rgba(23, 145, 255, 1);
  margin-top: 10px;
}

.header-content-title {
  width: 64px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(51, 51, 51, 1);
  font-size: 16px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 22px;
  text-align: center;
  margin: 5px 0 0 8px;
}

.header-content-resize {
  height: 32px;
  border-radius: 16px;
  background-color: rgba(240, 240, 240, 0.8);
  margin-left: 24px;
  width: 127px;
  justify-content: center;
  align-items: center;
}

.header-content-resize-content {
  width: 79px;
  height: 17px;
  justify-content: space-between;
}

.header-content-resize-icon {
  width: 16px;
  height: 16px;
}

.header-content-resize-text {
  width: 27px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(102, 102, 102, 1);
  font-size: 12px;
  font-family: PingFangSC-Regular;
  white-space: nowrap;
  line-height: 17px;
  text-align: center;
}

.header-content-delete {
  height: 32px;
  border-radius: 16px;
  background-color: rgba(240, 240, 240, 0.8);
  margin-left: 24px;
  width: 32px;
  justify-content: center;
  align-items: center;
}

.header-content-delete-icon {
  width: 16px;
  height: 16px;
}
.header-content-close {
  height: 32px;
  border-radius: 4px;
  border-width: 1px;
  border: 1px solid rgb(192, 53, 88);
  background-color: rgba(255, 255, 255, 1);
  margin-left: 300px;
  width: 88px;
  justify-content: center;
  align-items: center;
}
.header-content-close-info {
  width: 56px;
  display: block;
  overflow-wrap: break-word;
  color: rgb(207, 56, 56);
  font-size: 14px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}
.header-content-flow {
  height: 32px;
  border-radius: 4px;
  border-width: 1px;
  border: 1px solid rgba(23, 145, 255, 1);
  background-color: rgba(255, 255, 255, 1);
  margin-left: 24px;
  width: 88px;
  justify-content: center;
  align-items: center;
}

.header-content-flow-info {
  width: 56px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(23, 145, 255, 1);
  font-size: 14px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}

.header-content-save {
  height: 32px;
  border-radius: 4px;
  background-color: rgba(23, 145, 255, 1);
  margin-left: 24px;
  width: 88px;
  justify-content: center;
  align-items: center;
}

.header-content-save-info {
  width: 56px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(255, 255, 255, 1);
  font-size: 14px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}
/*顶部工具栏样式结束*/
</style>
