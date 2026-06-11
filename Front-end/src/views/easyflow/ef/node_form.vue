<template>
  <div>
    <div class="node-form-header flex-col" @click="attMenu1Open = !attMenu1Open">
      <div class="node-form-header-content flex-row">
        <img
          class="node-form-header-icon1"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng096494320fc014d16cfa3fa68c4ac231773f1364bcb1376625d7698d27f657d5"
        />
        <span class="node-form-header-text">基本属性</span>
        <img
          v-if="attMenu1Open"
          class="node-form-header-icon2"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc563fff86a5c839a00d798119c04b8bbe7a720e4283ba743a7c234d12efa85d0"
        />
        <img
          v-if="!attMenu1Open"
          class="node-form-header-icon2"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng7762e7a1e11d189f72dbcab4675e41ba40d5e2c4ae8fdabe98a30742914c97b5"
        />
      </div>
    </div>
    <div v-show="attMenu1Open" class="form-content flex-col">
      <!-- 流程节点属性框 -->
      <!-- v-show="type == 'node' && node.type != 'rectangle' && node.type != 'rhombus' && node.type != 'swinlane'" -->
      <el-form
        :model="node"
        label-width="75px"
        label-position="left"
        ref="dataForm"
        v-show="node.type === 'start'||node.type === 'task'||node.type === 'subprocess'||node.type === 'decision'||node.type === 'fork'||node.type === 'join'||node.type === 'end'"
      >
        <el-form-item label="节点类型:">
          <el-input v-model="node.type" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="功能类型:">
          <el-select v-model="feature1" value-key="id" placeholder="请选择">
            <el-option v-for="item in featureList" :key="item.id" :label="item.type" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 长方形属性框 -->
      <el-form :model="node" label-width="75px" label-position="left" ref="dataForm" v-show="node.type === 'rectangle'">
        <el-form-item label="节点类型:">
          <el-input v-model="node.type" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name"></el-input>
        </el-form-item>
        <el-form-item label="功能类型:">
          <el-select v-model="feature1" value-key="id" placeholder="请选择">
            <el-option v-for="item in featureList" :key="item.id" :label="item.type" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户角色:">
          <el-select v-model="node.role" multiple placeholder="请选择">
            <el-option v-for="item in roleList" :key="item.id" :label="item.roleName" :value="item.id"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 菱形属性框 -->
      <el-form :model="node" label-width="75px" label-position="left" ref="dataForm" v-show="node.type === 'rhombus'">
        <el-form-item label="节点类型:">
          <el-input v-model="node.type" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 泳道图属性框 -->
      <el-form :model="node" label-width="75px" label-position="left" ref="dataForm" v-show="node.type === 'swinlane'">
        <el-form-item label="节点类型:">
          <el-input v-model="node.type" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点名称:">
          <el-input v-model="node.name"></el-input>
        </el-form-item>
        <el-form-item label="left坐标:" v-show="false">
          <el-input v-model="node.left" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="top坐标:" v-show="false">
          <el-input v-model="node.top" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="width坐标:" v-show="false">
          <el-input v-model="node.width" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="height坐标:" v-show="false">
          <el-input v-model="node.height" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="节点状态:">
          <el-select v-model="node.state" placeholder="请选择">
            <el-option v-for="item in stateList" :key="item.state" :label="item.label" :value="item.state"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <button class="form-button-save flex-col" @click="save">
          <span class="form-button-save-text">保&nbsp;存</span>
        </button>
      </div>
        </el-form-item>
      </el-form>

      <!-- 连线属性框 -->
      <el-form :model="line" label-width="75px" label-position="left" ref="dataForm" v-show="type === 'line'">
        <el-form-item label="连线条件">
          <el-input v-model="line.label"></el-input>
        </el-form-item>
        <el-form-item>
          <div class="form-button flex-row">
        <button class="form-button-save flex-col" @click="saveLine">
          <span class="form-button-save-text">保&nbsp;存</span>
        </button>
      </div>
        </el-form-item>
      </el-form>
    </div>
    <div class="flow-form-line flex-col"></div>
    <div class="node-form-header flex-col" @click="attMenu2Open = !attMenu2Open">
      <div class="node-form-header-content flex-row">
        <img
          class="node-form-header-icon1"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng096494320fc014d16cfa3fa68c4ac231773f1364bcb1376625d7698d27f657d5"
        />
        <span class="node-form-header-text">表单属性</span></span>
        <img
          v-if="attMenu2Open"
          class="node-form-header-icon2"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPngc563fff86a5c839a00d798119c04b8bbe7a720e4283ba743a7c234d12efa85d0"
        />
        <img
          v-if="!attMenu2Open"
          class="node-form-header-icon2"
          referrerpolicy="no-referrer"
          src="https://lanhu.oss-cn-beijing.aliyuncs.com/SketchPng7762e7a1e11d189f72dbcab4675e41ba40d5e2c4ae8fdabe98a30742914c97b5"
        />
      </div>
    </div>
    <div class="ef-node-form">
      <ul v-show="attMenu2Open" class="ef-node-menu-ul">
        <div class="attMenu2Div">
          <attribute-form ref="attributesForm1" @changeOnline="changeOnline"> </attribute-form>
        </div>
      </ul>
    </div>
  </div>
</template>

<script>
import { cloneDeep } from 'lodash'
import { getAction } from '@/api/manage'
import AttributeForm from '@/views/easyflow/ef/attributesForm'

export default {
  data() {
    return {
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      formThreeId: { formDesignerId: '', onlineTableId: '', onlineDataId: '' },
      visible: true,
      // node 或 line
      type: '',
      node: {},
      line: {},
      data: {},
      feature1: {},
      attMenu1Open: true,
      attMenu2Open: true,
      roleList: [],
      featureList: [],
      stateList: [
        {
          state: 'success',
          label: '已完成',
        },
        {
          state: 'waiting',
          label: '未完成',
        },
        {
          state: 'running',
          label: '进行中',
        },
      ],
    }
  },
  components: {
    AttributeForm,
  },
  created() {
    getAction('/sys/role/list?pageSize=100000').then((res) => {
      this.roleList = res.result.records
    })
    this.getFeatureList()
  },
  methods: {
    closeAttributesForm() {
      this.$refs.attributesForm1.close()
    },
    //得到所有的Task节点的功能列表，例如选择video，这个节点赋予视频的角色，拥有独有的表单属性
    getFeatureList() {
      let params = {
        column: 'createTime',
        order: 'desc',
        pageNo: 1,
        pageSize: 1000,
        superQueryMatchType: 'and',
      }
      getAction('/online/cgform/api/getData/6fd14b6968f6428096f588d080a1d749', params).then((res) => {
        if (res.success) {
          let featureList = res.result.records
          this.featureList = featureList
          console.log(this.featureList)
        }
      })
    },
    /**
     * 表单修改，这里可以根据传入的ID进行业务信息获取
     * @param data
     * @param id
     */
    nodeInit(data, id) {
      console.log(data)
      this.feature1 = {}
      this.type = 'node'
      this.data = data
      data.nodeList.filter((node) => {
        if (node.id === id) {
          let featureList = this.featureList
          for (let i = 0; i < featureList.length; i++) {
            if (featureList[i].type == node.feature) {
              this.feature1 = featureList[i]
            }
          }
          if (node.formDesignerId != '' && node.type == 'rectangle') {
            this.showForm(node)
          }
          this.node = cloneDeep(node)
        }
      })
      console.log('type', this.type)
      console.log('node', this.node)
    },
    //展示高级属性（用表单配的）
    showForm(node) {
      this.formThreeId = { formDesignerId: '', onlineTableId: '', onlineDataId: '' }
      this.formThreeId.formDesignerId = node.formDesignerId //表单ID
      if (node.onlineTableId != '' && node.onlineDataId != '') {
        this.formThreeId.onlineTableId = node.onlineTableId
        this.formThreeId.onlineDataId = node.onlineDataId
      }
      this.$nextTick(() => {
        console.log('this.formThreeId', this.formThreeId)
        this.$refs.attributesForm1.openForm(this.formThreeId)
      })
    },
    //改变节点的onlineDataId和onlineTableId
    changeOnline(onlineTableId, onlineDataId) {
      this.data.nodeList.filter((node) => {
        if (node.id === this.node.id) {
          node.onlineTableId = onlineTableId
          node.onlineDataId = onlineDataId
        }
      })
    },
    lineInit(line) {
      this.node = {}
      this.type = 'line'
      this.line = line
      console.log('this.line', this.line)
    },
    // 修改连线
    saveLine() {
      this.$emit('setLineLabel', this.line.from, this.line.to, this.line.label)
    },
    save() {
      this.data.nodeList.filter((node) => {
        if (node.id === this.node.id) {
          let type = this.node.type
          switch (type) {
            case 'rectangle':
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.role = this.node.role
              node.feature = this.feature1.type
              if (node.feature != '' && node.feature != undefined) {
                node.formDesignerId = this.feature1.form_id
              }
              node.onlineTableId = ''
              node.onlineDataId = ''
              node.state = this.node.state
              if (node.formDesignerId != '' && node.type == 'rectangle') {
                this.showForm(node)
              }
              break
            case 'swinlane':
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.width = this.node.width
              node.height = this.node.height
              node.state = this.node.state
              break
            case 'rhombus':
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.state = this.node.state
              break
            default:
              console.log(node)
              node.name = this.node.name
              node.left = this.node.left
              node.top = this.node.top
              node.state = this.node.state
          }
          this.$message.success('保存成功')
          this.$emit('repaintEverything')
        }
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
.el-node-form-tag {
  position: absolute;
  top: 50%;
  margin-left: -15px;
  height: 40px;
  width: 15px;
  background-color: #fbfbfb;
  border: 1px solid rgb(220, 227, 232);
  border-right: none;
}
.attMenu2Div {
  margin-top: 10px;
  margin-right: 10px;
}
.el-date-editor.el-input {
  width: 100%;
}
/*节点菜单*/
.ef-node-pmenu {
  cursor: pointer;
  height: 32px;
  line-height: 32px;
  width: 100%;
  display: block;
  font-weight: bold;
  color: #4a4a4a;
  padding-left: 5px;
}

.ef-node-pmenu:hover {
  background-color: #e0e0e0;
}
.ef-node-menu-ul {
  list-style: none;
  padding-left: 10px;
}
/*node-form*/
.ef-node-form-header {
  height: 32px;
  border-top: 1px solid #dce3e8;
  border-bottom: 1px solid #dce3e8;
  background: #f1f3f4;
  color: #000;
  line-height: 32px;
  padding-left: 12px;
  font-size: 14px;
}

.ef-node-form-body {
  margin-top: 10px;
  padding-right: 10px;
}
/*属性头部样式*/
.node-form-header {
  height: 48px;
  background-color: rgba(255, 255, 255, 1);
  width: 293px;
  justify-content: center;
  align-items: flex-start;
  padding-left: 24px;
}

.node-form-header-content {
  z-index: auto;
  width: 253px;
  height: 22px;
  justify-content: space-between;
}

.node-form-header-icon1 {
  width: 20px;
  height: 20px;
  margin-top: 1px;
}

.node-form-header-text {
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

.node-form-header-icon2 {
  width: 16px;
  height: 16px;
  margin: 2px 0 0 170px;
}

/*属性头部样式结束*/
/*属性内容样式*/
.form-content {
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 1);
  width: 265px;
  justify-content: flex-start;
  padding-top: 24px;
  margin-left: 24px;
}

.form-content-item {
  z-index: auto;
  width: 293px;
  height: 424px;
}

.ant-form-item-label {
  z-index: auto;
  width: 245px;
  height: 40px;
  margin-left: 24px;
  justify-content: space-between;
}

.form-content-item-text {
  width: 60px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(51, 51, 51, 1);
  font-size: 14px;
  white-space: nowrap;
  line-height: 20px;
  text-align: left;
  margin-top: 10px;
}

.form-content-item-input {
  height: 40px;
  border-radius: 4px;
  border-width: 1px;
  border: 1px solid rgba(240, 240, 240, 1);
  background-color: rgba(245, 247, 250, 1);
  width: 200px;
  justify-content: center;
  align-items: flex-start;
  padding-left: 12px;
  margin-left: 20px;
}
/*属性内容样式结束*/
/*按钮样式开始 */
.form-button {
  z-index: auto;
  width: 154px;
  height: 32px;
  justify-content: space-between;
}
.form-button-save {
  height: 32px;
  border-radius: 4px;
  border-width: 1px;
  border: 1px solid rgba(23, 145, 255, 1);
  background: #fbfbfb;
  width: 65px;
  justify-content: center;
  align-items: center;
}

.form-button-save-text {
  z-index: 82;
  width: 33px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(23, 145, 255, 1);
  font-size: 14px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}
.form-button-reset {
  z-index: 84;
  height: 32px;
  border-radius: 4px;
  background: #fbfbfb;
  border-width: 1px;
  border: 1px solid rgba(217, 217, 217, 1);
  width: 65px;
  justify-content: center;
  align-items: center;
}

.form-button-reset-text {
  z-index: 85;
  width: 33px;
  display: block;
  overflow-wrap: break-word;
  color: rgba(102, 102, 102, 1);
  font-size: 14px;
  font-family: PingFangSC-Medium;
  white-space: nowrap;
  line-height: 20px;
  text-align: center;
}

/*按钮样式结束 */
.flow-form-line {
  z-index: 135;
  width: 317px;
  height: 1px;
  background-color: rgba(240, 240, 240, 1);
  margin-top: 24px;
}
</style>
