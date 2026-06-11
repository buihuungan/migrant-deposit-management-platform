<template>
  <div ref="node" :style="nodeContainerStyle" @click="clickNode" @mouseup="changeNodeSite">
    <div
      v-if="node.type != 'task' && node.type != 'rhombus' && node.type != 'swinlane' && node.state === 'waiting'"
      style="width: 100%"
    >
      <div class="menu-node flex-col">
        <div class="menu-node-content flex-row">
          <img class="menu-node-icon flex-col" referrerpolicy="no-referrer" :src="iconWaitingClass" />
          <span class="menu-node-text flow-node-drag">{{ node.name }}</span>
        </div>
      </div>
    </div>
    <div
      v-if="
        node.type != 'task' &&
        node.type != 'rhombus' &&
        node.type != 'swinlane' &&
        (node.state === 'running' || node.state === 'Withdraw' || node.state === 'Created' || node.state === 'Canceled')
      "
      style="width: 100%"
    >
      <div class="menu-node menu-node-running flex-col">
        <div class="menu-node-content flex-row">
          <img class="menu-node-icon flex-col" referrerpolicy="no-referrer" :src="iconRunningClass" />
          <span class="menu-node-text menu-node-text-running flow-node-drag">{{ node.name }}</span>
        </div>
      </div>
    </div>
    <div
      v-if="node.type != 'task' && node.type != 'rhombus' && node.type != 'swinlane' && node.state === 'Completed'"
      style="width: 100%"
    >
      <div class="menu-node menu-node-success flex-col">
        <div class="menu-node-content flex-row">
          <img class="menu-node-icon flex-col" referrerpolicy="no-referrer" :src="iconSuccessClass" />
          <span class="menu-node-text menu-node-text-success flow-node-drag">{{ node.name }}</span>
        </div>
      </div>
    </div>

    <div v-if="node.type === 'task' && node.state === 'waiting'" style="width: 100%">
      <div class="ef-node-menu-rectangle ef-node-menu-rectangle-waiting flex-col">
        <span class="ef-node-menu-rectangle-text ef-node-menu-rectangle-text-waiting flow-node-drag">{{
          node.name
        }}</span>
      </div>
    </div>
    <div v-if="node.type === 'task' &&(node.state === 'running' || node.state === 'Withdraw' || node.state === 'Created' || node.state === 'Canceled')" style="width: 100%">
      <div class="ef-node-menu-rectangle ef-node-menu-rectangle-running flex-col">
        <span class="ef-node-menu-rectangle-text ef-node-menu-rectangle-text-running flow-node-drag">{{
          node.name
        }}</span>
      </div>
    </div>
    <div v-if="node.type === 'task' && node.state === 'Completed'" style="width: 100%">
      <div class="ef-node-menu-rectangle ef-node-menu-rectangle-success flex-col">
        <span class="ef-node-menu-rectangle-text ef-node-menu-rectangle-text-success flow-node-drag">{{
          node.name
        }}</span>
      </div>
    </div>
    <div v-if="node.type === 'rhombus' && node.state === 'waiting'" style="width: 100%">
      <div class="ef-node-menu-rhombus-waiting flex-col">
        <span class="ef-node-menu-rhombus-text ef-node-menu-rhombus-text-waiting flow-node-drag">{{ node.name }}</span>
      </div>
    </div>
    <div v-if="node.type === 'rhombus' && node.state === 'running'" style="width: 100%">
      <div class="ef-node-menu-rhombus-running flex-col">
        <span class="ef-node-menu-rhombus-text ef-node-menu-rhombus-text-running flow-node-drag">{{ node.name }}</span>
      </div>
    </div>
    <div v-if="node.type === 'rhombus' && node.state === 'Completed'" style="width: 100%">
      <div class="ef-node-menu-rhombus-success flex-col">
        <span class="ef-node-menu-rhombus-text ef-node-menu-rhombus-text-success flow-node-drag">{{ node.name }}</span>
      </div>
    </div>
    <div v-if="node.type === 'swinlane'">
      <vue-draggable-resizable
        :x="node.left"
        :y="node.top"
        :w="node.width"
        :h="node.height"
        :min-width="160"
        :min-height="200"
        @refLineParams="getRefLineParams"
        class="ef-node-menu-swinlane flex-col"
        @dragstop="(left, top) => onDragstop(left, top)"
        @resizing="(left, top, width, height) => onResizstop(left, top, width, height)"
      >
        <div
          class="swinlane-outer swinlane-outer-waiting flex-col"
         
        >
          <div class="swinlane-outer2 flex-row">
            <img class="swinlane-icon" referrerpolicy="no-referrer" :src="iconWaitingClass" />
            <span class="swinlane-text swinlane-text-waiting">{{ node.name }}</span>
          </div>
        </div>
        <!-- <div
          class="swinlane-outer swinlane-outer-running flex-col"
          v-if="node.type === 'swinlane' && node.state === 'running'"
        >
          <div class="swinlane-outer2 flex-row">
            <img class="swinlane-icon" referrerpolicy="no-referrer" :src="iconRunningClass" />
            <span class="swinlane-text swinlane-text-running">{{ node.name }}</span>
          </div>
        </div>
        <div
          class="swinlane-outer swinlane-outer-success flex-col"
          v-if="node.type === 'swinlane' && node.state === 'Completed'"
        >
          <div class="swinlane-outer2 flex-row">
            <img class="swinlane-icon" referrerpolicy="no-referrer" :src="iconSuccessClass" />
            <span class="swinlane-text swinlane-text-success">{{ node.name }}</span>
          </div>
        </div> -->
      </vue-draggable-resizable>
      <!--辅助线-->
      <span
        class="ref-line v-line"
        v-for="item in vLine"
        v-show="item.display"
        :style="{ left: item.position, top: item.origin, height: item.lineLength }"
      />
      <span
        class="ref-line h-line"
        v-for="item in hLine"
        v-show="item.display"
        :style="{ top: item.position, left: item.origin, width: item.lineLength }"
      />
      <!--辅助线END-->
    </div>
  </div>
</template>

<script>
import VueDraggableResizable from 'vue-draggable-resizable'
import 'vue-draggable-resizable-gorkys/dist/VueDraggableResizable.css'
import iconStartWaiting from './svg/icon-start-waiting.svg'
import iconStartRunning from './svg/icon-start-running.svg'
import iconStartSuccess from './svg/icon-start-success.svg'
import iconTaskWaiting from './svg/icon-task-waiting.svg'
import iconTaskRunning from './svg/icon-task-running.svg'
import iconTaskSuccess from './svg/icon-task-success.svg'
import iconSubprocessWaiting from './svg/icon-subprocess-waiting.svg'
import iconSubprocessRunning from './svg/icon-subprocess-running.svg'
import iconSubprocessSuccess from './svg/icon-subprocess-success.svg'
import iconDecisionWaiting from './svg/icon-decision-waiting.svg'
import iconDecisionRunning from './svg/icon-decision-running.svg'
import iconDecisionSuccess from './svg/icon-decision-success.svg'
import iconForkWaiting from './svg/icon-fork-waiting.svg'
import iconForkRunning from './svg/icon-fork-running.svg'
import iconForkSuccess from './svg/icon-fork-success.svg'
import iconJoinWaiting from './svg/icon-join-waiting.svg'
import iconJoinRunning from './svg/icon-join-running.svg'
import iconJoinSuccess from './svg/icon-join-success.svg'
import iconEndWaiting from './svg/icon-end-waiting.svg'
import iconEndRunning from './svg/icon-end-running.svg'
import iconEndSuccess from './svg/icon-end-success.svg'
import iconSwinlaneWaiting from './svg/icon-swinlane-waiting.svg'
import iconSwinlaneRunning from './svg/icon-swinlane-running.svg'
import iconSwinlaneSuccess from './svg/icon-swinlane-success.svg'
export default {
  components: {
    VueDraggableResizable,
  },
  props: {
    node: Object,
    activeElement: Object,
    type: String,
    index: Number,
  },
  data() {
    return {
      vLine: [],
      hLine: [],
      isClicked: false,
    }
  },
  computed: {
    iconWaitingClass() {
      const type = this.node.type
      switch (type) {
        case 'start':
          return iconStartWaiting
        case 'people':
          return iconTaskWaiting
        case 'subprocess':
          return iconSubprocessWaiting
        case 'decision':
          return iconDecisionWaiting
        case 'fork':
          return iconForkWaiting
        case 'join':
          return iconJoinWaiting
        case 'end':
          return iconEndWaiting
        case 'swinlane':
          return iconSwinlaneWaiting
      }
    },
    iconRunningClass() {
      const type = this.node.type
      switch (type) {
        case 'start':
          return iconStartRunning
        case 'people':
          return iconTaskRunning
        case 'subprocess':
          return iconSubprocessRunning
        case 'decision':
          return iconDecisionRunning
        case 'fork':
          return iconForkRunning
        case 'join':
          return iconJoinRunning
        case 'end':
          return iconEndRunning
        case 'swinlane':
          return iconSwinlaneRunning
      }
    },
    iconSuccessClass() {
      const type = this.node.type
      switch (type) {
        case 'start':
          return iconStartSuccess
        case 'people':
          return iconTaskSuccess
        case 'subprocess':
          return iconSubprocessSuccess
        case 'decision':
          return iconDecisionSuccess
        case 'fork':
          return iconForkSuccess
        case 'join':
          return iconJoinSuccess
        case 'end':
          return iconEndSuccess
        case 'swinlane':
          return iconSwinlaneSuccess
      }
    },
    // 节点容器样式
    nodeContainerStyle() {
      if (this.node.type == 'swinlane') {
        return
      } else {
        return {
          top: this.node.top,
          left: this.node.left,
          position: 'absolute',
        }
      }
    },
  },
  methods: {
    // 点击节点
    clickNode() {
      this.$emit('clickNode', this.node, this.index)
    },
    // 鼠标移动后抬起
    changeNodeSite() {
      // 避免抖动
      if (this.node.left == this.$refs.node.style.left && this.node.top == this.$refs.node.style.top) {
        return
      }
      if (this.node.type == 'swinlane') {
        return
      } else {
        this.$emit('changeNodeSite', {
          nodeId: this.node.id,
          left: this.$refs.node.style.left,
          top: this.$refs.node.style.top,
        })
      }
    },
    // 辅助线回调事件
    getRefLineParams(params) {
      const { vLine, hLine } = params
      this.vLine = vLine
      this.hLine = hLine
    },
    onDragstop(left, top) {
      console.log(left, top)
      this.$emit('changeNodeSite', {
        nodeId: this.node.id,
        left: left,
        top: top,
      })
    },
    onResizstop(left, top, width, height) {
      console.log(left, top, width, height)
      this.$emit('changeNodeSite', {
        nodeId: this.node.id,
        left: left,
        top: top,
        width: width,
        height: height,
      })
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
.flow-node-drag {
  cursor: Crosshair;
}

.ref-line {
  position: absolute;
  background-color: rgb(255, 0, 204);
  z-index: 9999;
}
.v-line {
  width: 1px;
}
.h-line {
  height: 1px;
}
.is-clicked {
  background-color: #dddbdb;
}

/*流程节点的样式*/
.menu-node {
  cursor: move;
  z-index: 1000;
  height: 36px;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 1);
  background-color: rgba(239, 249, 255, 1);
  align-self: center;
  width: 90px;
  justify-content: center;
  align-items: flex-start;
  padding-left: 13px;
  /* margin: 0 24px 8px 24px; */
}

.menu-node-running {
  background-color: rgba(246, 247, 249, 1);
}
.menu-node-success {
  background-color: rgba(240, 239, 235, 1);
}
.menu-node:hover {
  border-width: 1px;
  border: 2px dashed #1791ff;
}
.menu-node:active {
  border: 1px solid #1791ff;
}

.menu-node-content {
  width: 120px;
  height: 20px;
  justify-content: space-between;
}

.menu-node-icon {
  width: 16px;
  height: 16px;
  background: 0px 0px no-repeat;
  margin-top: 3px;
}

.menu-node-text {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 95px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(29, 53, 87, 0.7);
  font-family: PingFangSC-Regular;
  font-size: 18px;
  font-weight: 600;
  white-space: nowrap;
  line-height: 20px;
  text-align: left;
}
/* .menu-node-text:hover {
  text-overflow: inherit;
  overflow: visible;
} */
.menu-node-text-running {
  color: #1d3557;
}
.menu-node-text-success {
  color: rgba(128, 128, 128, 1);
}
/*流程节点的样式结束*/
/*长方形的样式*/
.ef-node-menu-rectangle {
  cursor: move;
  z-index: 1000;
  height: 68px;
  border-radius: 8px;
  background-color: #eff9ff;
  width: 120px;
  justify-content: center;
  align-items: center;
  border: 1px solid #ffffff;
}

.ef-node-menu-rectangle:hover {
  border-width: 1px;
  border: 2px dashed #1791ff;
}
.ef-node-menu-rectangle:active {
  border: 1px solid #1791ff;
}

.ef-node-menu-rectangle-waiting {
  background-color: rgba(206, 229, 242, 0.7);
}

.ef-node-menu-rectangle-running {
  background-color: rgba(246, 247, 249, 1);
}
.ef-node-menu-rectangle-success {
  pointer-events: none;
  background-color: rgba(240, 239, 235, 1);
}

.ef-node-menu-rectangle-text {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100px;
  height: 20px;
  display: block;
  overflow-wrap: break-word;
  font-size: 18px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 600;
  color: #333333;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}

.ef-node-menu-rectangle-text-waiting {
  color: rgba(29, 53, 87, 0.7);
}

.ef-node-menu-rectangle-text-running {
  color: rgba(29, 53, 87, 1);
}

.ef-node-menu-rectangle-text-success {
  color: rgba(128, 128, 128, 1);
}
/*长方形的样式结束*/

/*菱形的样式*/
.ef-node-menu-rhombus-waiting {
  cursor: move;
  z-index: 1000;
  width: 99px;
  height: 99px;
  background: #f6f7f9;
  border-radius: 8px;
  justify-content: center;
  align-items: center;
  background: url(./svg/rhombus-waiting.svg) 100% no-repeat;
}
.ef-node-menu-rhombus-running {
  cursor: move;
  z-index: 1000;
  width: 99px;
  height: 99px;
  background: #f6f7f9;
  border-radius: 8px;
  justify-content: center;
  align-items: center;
  background: url(./svg/rhombus-running.svg) 100% no-repeat;
}
.ef-node-menu-rhombus-success {
  cursor: move;
  z-index: 1000;
  width: 99px;
  height: 99px;
  background: #f6f7f9;
  border-radius: 8px;
  justify-content: center;
  align-items: center;
  background: url(./svg/rhombus-success.svg) 100% no-repeat;
}
.ef-node-menu-rhombus-waiting:hover {
  background: url(./svg/rhombus-waiting-hover.svg) 100% no-repeat;
}

.ef-node-menu-rhombus-waiting:active {
  background: url(./svg/rhombus-waiting-active.svg) 100% no-repeat;
}
.ef-node-menu-rhombus-running:hover {
  background: url(./svg/rhombus-running-hover.svg) 100% no-repeat;
}
.ef-node-menu-rhombus-running:active {
  background: url(./svg/rhombus-running-active.svg) 100% no-repeat;
}
.ef-node-menu-rhombus-success:hover {
  background: url(./svg/rhombus-success-hover.svg) 100% no-repeat;
}
.ef-node-menu-rhombus-success:active {
  background: url(./svg/rhombus-success-active.svg) 100% no-repeat;
}

.ef-node-menu-rhombus-text {
  white-space: nowrap;
  overflow-wrap: break-word;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 80px;
  height: 17px;
  font-size: 12px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #333333;
  line-height: 17px;
  text-align: center;
}

.ef-node-menu-rhombus-text-waiting {
  color: rgba(23, 145, 255, 0.7);
}
.ef-node-menu-rhombus-text-running {
  color: rgba(23, 145, 255, 1);
}
.ef-node-menu-rhombus-text-success {
  color: rgba(128, 128, 128, 1);
}
/*菱形的样式结束*/

/*泳道图的样式*/
.ef-node-menu-swinlane {
  cursor: move;
  z-index: 500;
  width: 160px;
  height: 200px;
  background: #ffffff;
  border-width: 1px;
  border: 1px solid #f6f7f9;
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
}

.ef-node-menu-swinlane:hover {
  border-width: 1px;
  border: 2px dashed #1791ff;
}
.ef-node-menu-swinlane:active {
  border-width: 1px;
  border: 1px solid #1791ff;
}

.swinlane-outer {
  z-index: 500;
  width: 100%;
  height: 40px;
  background: #f0f0f0;
  border-radius: 2px;
  justify-content: center;
  align-items: center;
  position: absolute;
}

.swinlane-outer-waiting {
  background-color: rgba(239, 249, 255, 1);
}
.swinlane-outer-running {
  background-color: rgba(246, 247, 249, 1);
}
.swinlane-outer-success {
  background-color: rgba(242, 242, 242, 1);
}

.ef-node-menu-swinlane:hover .swinlane-outer {
  border-width: 1px;
  border-bottom: 2px dashed #1791ff;
}
.ef-node-menu-swinlane:active .swinlane-outer {
  border-bottom: 1px solid #1791ff;
}

.swinlane-outer2 {
  z-index: 500;
  margin-left: 10%;
  width: 80%;
  height: 20px;
  justify-content: space-between;
}

.swinlane-icon {
  width: 16px;
  height: 16px;
  margin-top: 2px;
}

.swinlane-text {
  white-space: nowrap;
  overflow-wrap: break-word;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 27%;
  width: 60%;
  display: block;
  overflow-wrap: break-word;
  font-size: 18px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #333333;
  line-height: 20px;
  text-align: center;
}

.swinlane-text-waiting {
  color: rgba(29, 53, 87, 0.7);
}
.swinlane-text-running {
  color: rgba(29, 53, 87, 1);
}
.swinlane-text-success {
  color: rgba(128, 128, 128, 1);
}
.handle {
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background: #f0f7ff;
  border: 1px solid #1879ff;
}
/*泳道图的样式结束*/
</style>
