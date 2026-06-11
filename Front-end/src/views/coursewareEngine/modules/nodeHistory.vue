<template>
  <div ref="node" :style="nodeContainerStyle" @click="clickNode" @mouseup="changeNodeSite">
    <div
      v-if="node.type != 'task' && node.type != 'rhombus' && node.type != 'swinlane' && node.state === 'waiting'"
      style="width: 100%"
    >
      <div class="menu-node flex-col">
        <div class="menu-node-content flex-row">
          <img class="menu-node-icon flex-col" referrerpolicy="no-referrer" :src="iconWaitingClass" />
          <span class="menu-node-text">{{ node.name }} - 未开始</span>
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
          <span class="menu-node-text menu-node-text-running"
            >{{ node.name }} - <span v-if="node.state == 'Created'">已创建</span>
            <span v-if="node.state == 'Withdraw'">已退回</span><span v-if="node.state == 'Canceled'">已拒绝</span></span
          >
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
          <span class="menu-node-text menu-node-text-success">{{ node.name }} - 已完成</span>
        </div>
      </div>
    </div>

    <div v-if="node.type === 'task' && node.state === 'waiting'" style="width: 100%">
      <div class="ef-node-menu-rectangle ef-node-menu-rectangle-waiting flex-col">
        <span class="ef-node-menu-rectangle-text ef-node-menu-rectangle-text-waiting">{{
          node.name
        }}</span>
      </div>
    </div>
    <div v-if="node.type === 'task' && node.state === 'running'" style="width: 100%">
      <div class="ef-node-menu-rectangle ef-node-menu-rectangle-running flex-col">
        <span class="ef-node-menu-rectangle-text ef-node-menu-rectangle-text-running">{{
          node.name
        }}</span>
      </div>
    </div>
    <div v-if="node.type === 'task' && node.state === 'Completed'" style="width: 100%">
      <div class="ef-node-menu-rectangle ef-node-menu-rectangle-success flex-col">
        <span class="ef-node-menu-rectangle-text ef-node-menu-rectangle-text-success">{{
          node.name
        }}</span>
      </div>
    </div>
    <div v-if="node.type === 'rhombus' && node.state === 'waiting'" style="width: 100%">
      <div class="ef-node-menu-rhombus-waiting flex-col">
        <span class="ef-node-menu-rhombus-text ef-node-menu-rhombus-text-waiting">{{ node.name }}</span>
      </div>
    </div>
    <div v-if="node.type === 'rhombus' && node.state === 'running'" style="width: 100%">
      <div class="ef-node-menu-rhombus-running flex-col">
        <span class="ef-node-menu-rhombus-text ef-node-menu-rhombus-text-running">{{ node.name }}</span>
      </div>
    </div>
    <div v-if="node.type === 'rhombus' && node.state === 'Completed'" style="width: 100%">
      <div class="ef-node-menu-rhombus-success flex-col">
        <span class="ef-node-menu-rhombus-text ef-node-menu-rhombus-text-success">{{ node.name }}</span>
      </div>
    </div>
    <div v-if="node.type === 'swinlane'">
      <vue-draggable-resizable
        :x="node.left"
        :y="node.top"
        :w="node.width"
        :h="node.height"
        :min-width="120"
        :min-height="160"
        @refLineParams="getRefLineParams"
        class="ef-node-menu-swinlane flex-col"
        @dragstop="(left, top) => onDragstop(left, top)"
        @resizing="(left, top, width, height) => onResizstop(left, top, width, height)"
      >
        <div
          class="swinlane-outer swinlane-outer-waiting flex-col"
        >
          <div class="swinlane-outer2 flex-row">
            <img
              class="swinlane-icon"
              referrerpolicy="no-referrer"
              src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngff7a840c5ae0f44f07be5cc9684f54db85dcc490e64d2d774df10ab73bd0b743"
            />
            <span class="swinlane-text swinlane-text-waiting">{{ node.name }}</span>
          </div>
        </div>
        <!-- <div
          class="swinlane-outer swinlane-outer-running flex-col"
          v-if="node.type === 'swinlane' && node.state === 'running'"
        >
          <div class="swinlane-outer2 flex-row">
            <img
              class="swinlane-icon"
              referrerpolicy="no-referrer"
              src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng5041d25566f3075b980ec85fcbce31df521e599cc14410da6311554023af08e5"
            />
            <span class="swinlane-text swinlane-text-running">{{ node.name }}</span>
          </div>
        </div>
        <div
          class="swinlane-outer swinlane-outer-success flex-col"
          v-if="node.type === 'swinlane' && node.state === 'Completed'"
        >
          <div class="swinlane-outer2 flex-row">
            <img
              class="swinlane-icon"
              referrerpolicy="no-referrer"
              src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngbbc1b5e6c289b5aa4bc41adff116f266685354d17d52b8b3ffda0ce57822221f"
            />
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
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngd227e5633cf673f0ef4db4980fa0a4b29fc1e8266b48f4d325cd6a5def8e9079'
        case 'people':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng5829105af2277ae1fcab2d300bb503694ebe945fd81c020d8e3ab1c20126e445'
        case 'subprocess':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng46d1a89cbca2c01ae852bbdec3f1c134cc24ae6cc0fc698a8a87d8eac94bbc3e'
        case 'decision':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngb13754238412773298bb039faccfbf39b0a4c1b5aeb3d534feedc947e0882adb'
        case 'fork':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng991184fdc05d8062b31f3f3a733241e326a55bb5875b7d3d067d2c00c6936c99'
        case 'join':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPnga741fca9807e0a44f64c48a1ee5e1df1dff786c75af98c934d84887be5297b1d'
        case 'end':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc3eed5da2d1421a7fd2ce4ad69eb6909c96c5fca7308029b03fe65fe7ae9bcf2'
      }
    },
    iconRunningClass() {
      const type = this.node.type
      switch (type) {
        case 'start':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc82ec43c96916e1824feb01ba163101c490679bc11626c9b8865820446b50e6b'
        case 'people':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng968621249f8fada20434f12baf1d1f9517a1747dc4813739c74d9ba181810c2f'
        case 'subprocess':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng2328e5d424deb7e52c26b65a28e766a47cf7cd24d95737e72a18419de5699a33'
        case 'decision':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngb168b7647237256c711c55f52f28e1a7e364391b745e3995e936b8fdd72e8f15'
        case 'fork':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng0dda8c03b12d166c0d72de568e78aed8627bff6a315673b3f45901e8b2e9376a'
        case 'join':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngcdcc7dd37482fa5db031c007919263ac7596997d8aadc5df599539321a944d71'
        case 'end':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngffb790f604a7a8d8dedb67c300d5fad40f912470d8d99c9640ed9c76a9914733'
      }
    },
    iconSuccessClass() {
      const type = this.node.type
      switch (type) {
        case 'start':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc7c55fca54825e200cdfda3d350c8085d7d67804986adde57a70e7daeb67cc25'
        case 'people':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngf02583be8f679c24dd7a66f64a7603a8462d0e9a8a426f5f1ddc59e63d5a343c'
        case 'subprocess':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng80a9c58fa0b1b02be2e2d7d7a3e3286a4147a5925abd7f48f9b620a2087c435e'
        case 'decision':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng84a20ddb356927c450f5c3a9a3ddb82302fdcc7af7728fd896c4839996c2da81'
        case 'fork':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng013b223bb941635be5fcf5169df8d3c60256d11e2878a7ccd559f053215f0463'
        case 'join':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng853a35170cf9d9a6d66298f8580c2eda75fc85afa7e07ccbe2ff80c8de155898'
        case 'end':
          return 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng17afa001ee97d334ac56ef422e390a3fdc6e262a219fa9339f863ab4868e81df'
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
/* .flow-node-drag {
  cursor: Crosshair;
} */

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
  /* cursor: move; */
  z-index: 1000;
  height: 36px;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 1);
  background-color: rgba(239, 249, 255, 1);
  align-self: center;
  width: 147px;
  justify-content: center;
  align-items: flex-start;
  padding-left: 13px;
  /* margin: 0 24px 8px 24px; */
}

.menu-node-running {
  background-color: rgba(246, 247, 249, 1);
}
.menu-node-success {
  background-color: rgba(242, 242, 242, 1);
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
  color: rgba(23, 145, 255, 0.7);
  font-family: PingFangSC-Regular;
  font-size: 14px;
  white-space: nowrap;
  line-height: 20px;
  text-align: left;
}
/* .menu-node-text:hover {
  text-overflow: inherit;
  overflow: visible;
} */
.menu-node-text-running {
  color: #1791ff;
}
.menu-node-text-success {
  color: rgba(128, 128, 128, 1);
}
/*流程节点的样式结束*/
/*长方形的样式*/
.ef-node-menu-rectangle {
  /* cursor: move; */
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
  background-color: rgba(239, 249, 255, 1);
}

.ef-node-menu-rectangle-running {
  background-color: rgba(246, 247, 249, 1);
}
.ef-node-menu-rectangle-success {
  pointer-events: none;
  background-color: rgba(242, 242, 242, 1);
}

.ef-node-menu-rectangle-text {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100px;
  height: 20px;
  display: block;
  overflow-wrap: break-word;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #333333;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}

.ef-node-menu-rectangle-text-waiting {
  color: rgba(23, 145, 255, 0.7);
}

.ef-node-menu-rectangle-text-running {
  color: rgba(23, 145, 255, 1);
}

.ef-node-menu-rectangle-text-success {
  color: rgba(128, 128, 128, 1);
}
/*长方形的样式结束*/

/*菱形的样式*/
.ef-node-menu-rhombus-waiting {
  /* cursor: move; */
  z-index: 1000;
  width: 99px;
  height: 99px;
  background: #f6f7f9;
  border-radius: 8px;
  justify-content: center;
  align-items: center;
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng1815646066e3e3994e7425162f94a764a14d5c2d026cdf5c45f8b81f073175f2)
    100% no-repeat;
}
.ef-node-menu-rhombus-running {
  /* cursor: move; */
  z-index: 1000;
  width: 99px;
  height: 99px;
  background: #f6f7f9;
  border-radius: 8px;
  justify-content: center;
  align-items: center;
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPnged519fad8a4dd2086ec6d079deed4fd375989ad8692768b6e23150d975cb5920)
    100% no-repeat;
}
.ef-node-menu-rhombus-success {
  /* cursor: move; */
  z-index: 1000;
  width: 99px;
  height: 99px;
  background: #f6f7f9;
  border-radius: 8px;
  justify-content: center;
  align-items: center;
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng21a10c070c1e4df6a21424490c422a579c39c45c02e4d71f4cfa1d6df1890fac)
    100% no-repeat;
}
.ef-node-menu-rhombus-waiting:hover {
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng595c8d6aa65e73748f55d1c116a39cfbbe57ae39d91a21b3b7561acea20d02f1)
    100% no-repeat;
}

.ef-node-menu-rhombus-waiting:active {
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngf348a3944447b1b57f084af4bc9d9274e393895867c45dc9924cf8f70e55294e)
    100% no-repeat;
}
.ef-node-menu-rhombus-running:hover {
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng7bc74d5e372e42ab1dd83fec5fff54d8e530027947636ee9df15ffd778c41a68)
    100% no-repeat;
}
.ef-node-menu-rhombus-running:active {
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPnge12de72899cf951763491889707c1f82a3bd73949a39f2c1a6a8181b5677dc10)
    100% no-repeat;
}
.ef-node-menu-rhombus-success:hover {
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngbd6b7412029613ca41becc256655d20cfed79b1fb0c3ba3430371ed5618426ce)
    100% no-repeat;
}
.ef-node-menu-rhombus-success:active {
  background: url(https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng1a7e4bef19af628d44492347dd57b9ad184ae194d7090ac7d94b8da2ab4f888a)
    100% no-repeat;
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
  /* cursor: move; */
  z-index: 500;
  width: 120px;
  height: 160px;
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
  font-size: 14px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #333333;
  line-height: 20px;
  text-align: center;
}

.swinlane-text-waiting {
  color: rgba(23, 145, 255, 0.7);
}
.swinlane-text-running {
  color: rgba(23, 145, 255, 1);
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
