<template>
  <div ref="tool">
    <div v-for="menuList in menuAllList.children">
      <div class="flow-menu flex-col" @click="menuList.open = !menuList.open">
        <div class="flow-menu-content flex-row">
          <img
            class="flow-menu-content-icon"
            referrerpolicy="no-referrer"
            src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPnge1474c581c4be5b56f28652b3ba5da8fb1f6c45bfb292975b00ede239f71c5b6"
          />
          <span class="flow-menu-content-text">{{ menuList.name }}</span>
          <img
            v-if="menuList.open"
            class="flow-menu-content-icon2"
            referrerpolicy="no-referrer"
            src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc563fff86a5c839a00d798119c04b8bbe7a720e4283ba743a7c234d12efa85d0"
          />
          <img
            v-if="!menuList.open"
            class="flow-menu-content-icon2"
            referrerpolicy="no-referrer"
            src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng7762e7a1e11d189f72dbcab4675e41ba40d5e2c4ae8fdabe98a30742914c97b5"
          />
        </div>
      </div>
      <div v-show="menuList.open">
        <draggable @end="end" @start="move" v-bind="draggableOptions">
          <div v-for="subMenu in menuList.children" class="menu-node flex-col" :id="subMenu.type" :key="subMenu.type">
            <div class="menu-node-content flex-row">
              <img class="menu-node-icon flex-col" referrerpolicy="no-referrer" :src="subMenu.ico" />
              <!-- <div class="menu-node-icon flex-col"></div> -->
              <span class="menu-node-text">{{ subMenu.name }}</span>
            </div>
          </div>
        </draggable>
      </div>
      <div class="flow-menu-line flex-col"></div>
    </div>
  </div>
</template>
<script>
import draggable from 'vuedraggable'

var mousePosition = {
  left: -1,
  top: -1,
}

export default {
  data() {
    return {
      activeNames: '1',
      // draggable配置参数参考 https://www.cnblogs.com/weixin186/p/10108679.html
      draggableOptions: {
        preventOnFilter: false,
        sort: false,
        disabled: false,
        ghostClass: 'tt',
        // 不使用H5原生的配置
        forceFallback: true, 
        // 拖拽的时候样式
        // fallbackClass: 'flow-node-draggable'
      },
      menuAllList: {
        children: [
          {
            name: '流程节点',
            open: true,
            children: [
              {
                type: 'start',
                name: '开始',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng0d37f870e437e6ca233adeac562c3e5f73eb4aff91cc959ccfe45ea68221e2ae',
              },
              // {
              //   type: 'people',
              //   name: '人工节点',
              //   ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng5c2f08f7d588e21415a91827c313d61ba8b76fb7322d746d274c8d82184002e1',
              // },
              {
                type: 'subprocess',
                name: '子流程',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPnga8aa9f949697815c3426c18a907756051f3455de0db7d0fd7a2061597ef87ea3',
              },
              {
                type: 'decision',
                name: '路由决策',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc910d39e01f26f0f13b02769f4752cc26c85ca4ed17caae3be5d899355d7ec05',
              },
              {
                type: 'fork',
                name: '分支',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng4af059b1a64d487c20a6b811d454987d7ddc96a889cfa07e64922b22b508ba0d',
              },
              {
                type: 'join',
                name: '聚合',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng3442c58cdab5f5ed5e06c88fe5d698fd860649f6d19f4449eba8078e3f2d067a',
              },
              {
                type: 'end',
                name: '结束',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng92981a2d6acf28aa3eaff43e593176a9dc4823ba8db16a493f166966d366dc4a',
              },
            ],
          },
          {
            name: '课程节点',
            open: true,
            children: [
              {
                type: 'task',
                name: '课程节点',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngbccd7bcf9081f243d46bd704862703ae1a9e5fdcd4cfda0dc25fd02d6b3eadf3',
              },
              {
                type: 'rhombus',
                name: '选择节点',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngf2ac3aa31528daa92d0dc9c120ea9529833ba5d1ffa3c69e2040d3590e22620b',
              },
              {
                type: 'swinlane',
                name: '泳道图',
                ico: 'https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngaa877536b16a2643150f65189134311d9053650da3d808a9039e90758e3c0e7c',
              },
            ],
          },
        ],
      },

      nodeMenu: {},
    }
  },
  components: {
    draggable,
  },
  created() {
    /**
     * 以下是为了解决在火狐浏览器上推拽时弹出tab页到搜索问题
     * @param event
     */
    if (this.isFirefox()) {
      document.body.ondrop = function (event) {
        // 解决火狐浏览器无法获取鼠标拖拽结束的坐标问题
        mousePosition.left = event.layerX
        mousePosition.top = event.clientY - 50
        event.preventDefault()
        event.stopPropagation()
      }
    }
  },
  methods: {
    // 根据类型获取左侧菜单对象
    getMenuByType(type) {
      for (let i = 0; i < this.menuList1.length; i++) {
        let children = this.menuList1[i].children
        for (let j = 0; j < children.length; j++) {
          if (children[j].type === type) {
            return children[j]
          }
        }
      }
    },
    // startEnd(){
    //   if(sunMenu.name=='开始' || sunMenu.name=='结束'){
    //     this.startEnd = true
    //   }
    // },
    // 拖拽开始时触发
    move(evt, a, b, c) {
      this.nodeMenu = {}
      var type = evt.item.attributes.id.nodeValue
      switch (type) {
        case 'start':
          this.nodeMenu = {
            type: 'start',
            name: '开始',
            state: 'waiting',
          }
          break
        // case 'people':
        //   this.nodeMenu = {
        //     type: 'people',
        //     name: '人工节点',
        //     state: 'waiting',
        //   }
        //   break
        case 'subprocess':
          this.nodeMenu = {
            type: 'subprocess',
            name: '子流程',
            state: 'waiting',
          }
          break
        case 'decision':
          this.nodeMenu = {
            type: 'decision',
            name: '路由决策',
            state: 'waiting',
          }
          break
        case 'fork':
          this.nodeMenu = {
            type: 'fork',
            name: '分支',
            state: 'waiting',
          }
          break
        case 'join':
          this.nodeMenu = {
            type: 'join',
            name: '聚合',
            state: 'waiting',
          }
          break
        case 'end':
          this.nodeMenu = {
            type: 'end',
            name: '结束',
            state: 'waiting',
          }
          break
        case 'task':
          this.nodeMenu = {
            type: 'task',
            name: '课程节点',
            state: 'waiting',
          }
          break
        case 'rhombus':
          this.nodeMenu = {
            type: 'rhombus',
            name: '选择节点',
            state: 'waiting',
          }
          break
        case 'swinlane':
          this.nodeMenu = {
            type: 'swinlane',
            name: '泳道图',
           // state: 'waiting',
          }
          break
      }
    },
    // 拖拽结束时触发
    end(evt, e) {
      this.$emit('addNode', evt, this.nodeMenu, mousePosition)
    },
    // 是否是火狐浏览器
    isFirefox() {
      var userAgent = navigator.userAgent
      if (userAgent.indexOf('Firefox') > -1) {
        return true
      }
      return false
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
/*节点菜单头*/
.ef-node-pmenu {
  cursor: pointer;
  height: 48px;
  line-height: 32px;
  width: 100%;
  display: block;
  font-weight: bold;
  background: #ffffff;
  border-radius: 8px;
}

.flow-menu {
  z-index: 167;
  height: 48px;
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 1);
  width: 195px;
  justify-content: center;
  align-items: center;
}

.flow-menu-content {
  z-index: auto;
  width: 171px;
  height: 20px;
}

.flow-menu-content-icon {
  z-index: 169;
  width: 20px;
  height: 20px;
  margin-top: 2px;
}

.flow-menu-content-text {
  z-index: 4;
  width: 64px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(51, 51, 51, 1);
  font-size: 16px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 22px;
  text-align: left;
  margin-left: 4px;
}

.flow-menu-content-icon2 {
  z-index: 180;
  width: 16px;
  height: 16px;
  margin: 2px 0 0 76px;
}

.flow-menu-line {
  z-index: 55;
  width: 199px;
  height: 1px;
  background-color: rgba(240, 240, 240, 1);
  margin-top: 24px;
}
/*节点菜单头结束*/
/*节点样式开始*/
.menu-node {
  z-index: 163;
  height: 36px;
  border-radius: 8px;
  background-color: rgba(246, 247, 249, 1);
  margin-top: 8px;
  align-self: center;
  width: 147px;
  justify-content: center;
  align-items: flex-start;
  padding-left: 13px;
  margin: 0 24px 8px 24px;
}

.menu-node:hover {
  border-width: 1px;
  border: 2px dashed #1791ff;
}
.menu-node:active {
  border: 1px solid #1791ff;
}

.menu-node-content {
  z-index: auto;
  width: 56px;
  height: 20px;
  justify-content: space-between;
}

.menu-node-icon {
  z-index: 167;
  width: 16px;
  height: 16px;
  background: 0px 0px no-repeat;
  margin-top: 3px;
}
.menu-node-text {
  z-index: 164;
  width: 33px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(102, 102, 102, 1);
  font-size: 14px;
  white-space: nowrap;
  line-height: 20px;
  text-align: left;
}

/*节点样式结束*/
</style>
