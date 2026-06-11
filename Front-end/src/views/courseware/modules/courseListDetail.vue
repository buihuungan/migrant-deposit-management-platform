<template>
  <a-row :gutter="10">
    <!-- 详情区域 -->
    <a-col :md="12" :sm="24" style="padding: 10px 5px 5px 5px">
      <a-form :form="form" style="background: #fff; padding: 20px 50px 50px 20px; font-weight: 600; border-radius: 4px">
        <div style="margin-bottom: 20px">
          <a-divider type="vertical" style="width: 2px" />
          <label>仿真课件详情：</label>
        </div>
        <a-form-item label="案件名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['ware_name']" readOnly />
        </a-form-item>
        <a-form-item label="设计案件人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['teacher_name']" readOnly />
        </a-form-item>
        <a-form-item label="案件类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['ware_type_name']" readOnly />
        </a-form-item>
        <a-form-item label="案件领域" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['ware_domain_name']" readOnly />
        </a-form-item>
      </a-form>
    </a-col>
    <a-col :md="12" :sm="24" style="padding-top: 10px">
      <div style="background: #fff; padding: 20px 20px; font-weight: 600; border-radius: 4px 4px 0 0">
        <a-divider type="vertical" style="width: 2px" />
        <label>预览：</label>
        <div id="efContainer" ref="efContainer" class="efcontainer">
          <template v-for="node in data.nodeList">
            <flow-node :id="node.id" :key="node.id" :node="node" :activeElement="activeElement"> </flow-node>
          </template>
          <!-- 给画布一个默认的宽度和高度 -->
          <div style="position: absolute; top: 300px; left: 300px">&nbsp;</div>
        </div>
      </div>
    </a-col>
  </a-row>
</template>
<script>
import pick from 'lodash.pick'
import '../../easyflow/ef/jsplumb'
import { easyFlowMixin } from '@/views/easyflow/ef/mixins'
import lodash from 'lodash'
import flowNode from '@/views/easyflow/ef/node'

export default {
  name: 'courseListDetail',
  components: { flowNode },
  props: {
    detailrecords: {
      type: Object,
      default: function () {
        return {}
      },
    },
    editor: {
      type: Object,
      default: function () {
        return {}
      },
    },
  },
  mixins: [easyFlowMixin],
  watch: {
    detailrecords: {
      deep: true,
      immediate: true,
      handler: function (val) {
        let that = this
        this.form.resetFields()
        that.form.setFieldsValue(pick(val, 'ware_name', 'teacher_name', 'ware_type_name', 'ware_domain_name'))
        if (val.ware_content) {
          this.jsPlumb = jsPlumb.getInstance()
          this.dataReload(JSON.parse(val.ware_content))
        }
      },
    },
  },
  data() {
    return {
      form: this.$form.createForm(this),
      jsPlumb: null,
      data: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      // editor:null,
      visible: true,
      activeElement: {
        // 可选值 node 、line
        type: undefined,
        // 节点ID
        nodeId: undefined,
        // 连线ID
        sourceId: undefined,
        targetId: undefined,
      },
    }
  },
  methods: {
    dataReload(data) {
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
    },
    jsPlumbInit() {
      this.jsPlumb.ready(() => {
        // 导入默认配置
        this.jsPlumb.importDefaults(this.jsplumbSetting)
        // 会使整个jsPlumb立即重绘。
        this.jsPlumb.setSuspendDrawing(false, true)
        // 初始化节点
        this.loadEasyFlow()

        this.jsPlumb.setContainer(this.$refs.efContainer)
      })
    },
    loadEasyFlow() {
      // 初始化节点
      // for (var i = 0; i < this.data.nodeList.length; i++) {
      //   let node = this.data.nodeList[i]
      //   // 设置源点，可以拖出线连接其他节点
      //   // this.jsPlumb.makeSource(node.id, lodash.merge(this.jsplumbSourceOptions, {}))
      //   // // 设置目标点，其他源点拖出的线可以连接该节点
      //   // this.jsPlumb.makeTarget(node.id, this.jsplumbTargetOptions)
      // }
      // 初始化连线
      for (var i = 0; i < this.data.lineList.length; i++) {
        let line = this.data.lineList[i]
        var connParam = {
          source: line.from,
          target: line.to,
          label: line.label ? line.label : '',
          connector: line.connector ? line.connector : '',
          anchors: line.anchors ? line.anchors : undefined,
          paintStyle: line.paintStyle ? line.paintStyle : undefined,
        }
        this.jsPlumb.connect(connParam, this.jsplumbConnectOptions)
      }
      this.$nextTick(function () {
        this.loadEasyFlowFinish = true
      })
    },
  },
}
</script>
<style scoped>
#efContainer {
  height: 306px;
  position: relative;
  overflow: scroll;
  flex: 1;
}
</style>