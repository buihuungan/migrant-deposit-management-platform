<template>
  <a-modal
    :centered="true"
    :footer="null"
    title="查看历史"
    :visible="visible"
    :destroyOnClose="true"
    @cancel="handleCancel"
    width="75%"
    :zIndex="100"
  >
    <div>
      <a-tabs default-active-key="1" tab-position="left" @change="tabChange">
        <a-tab-pane key="1" tab="历史列表">
          <a-table bordered :columns="columns" :dataSource="dataSource" rowKey="id">
            <span slot="nodeState" slot-scope="text, record, index">
              <span v-if="record.state == 'Created'">已创建</span>
              <span v-if="record.state == 'Withdraw'">已退回</span>
              <span v-if="record.state == 'Completed'">已完成</span>
              <span v-if="record.state == 'Cancel'">已拒绝</span>
            </span>
            <span slot="endDate" slot-scope="text, record, index">
              <span v-if="record.endDate != null">{{ record.endDate }}</span>
              <span v-if="record.endDate == null">未完成</span>
            </span>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="2" tab="流程图">
          <div class="flowPicture">
            <div id="efContainer" ref="efContainer">
              <template v-for="node in data.nodeList">
                <flow-node :id="node.id" :key="node.id" :node="node" type="design"> </flow-node>
              </template>
              <!-- 给画布一个默认的宽度和高度 -->
              <div style="position: absolute; top: 600px; left: 700px">&nbsp;</div>
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane key="3" tab="流程详情">
          <div v-for="(item, index) in arrayForm" :key="index" style="margin-top: 20px">
            <a-timeline-item>
              <a style="margin-right: 20px">任务名称：{{ item.taskName }}</a>
              <a>创建日期：{{ item.createDateStr }}</a>
              <approve-task
                :formId="item.prevForm_designer_id"
                :tableId="item.prevOnline_table_id"
                :dataId="item.prevOnline_data_id"
                v-if="item.prevForm_designer_id != undefined"
              ></approve-task>
            </a-timeline-item>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-modal>
</template>
<script>
import flowNode from './nodeHistory'
import lodash from 'lodash'
import '../easyflowModule/jsplumb'
import Vue from 'vue'
import { easyFlowMixin } from '../easyflowModule/mixins'
import axios from 'axios'
import { nw_postAction, nw_postAction1, nw_getAction } from '@api/newWorkApi'
import ApproveTask from './approveTask'
export default {
  name: 'flowHistory',
  components: { ApproveTask, flowNode },
  mixins: [easyFlowMixin],
  data() {
    return {
      returnJson: {},
      jsPlumb: null,
      data: {},
      // 是否加载完毕标志位
      loadEasyFlowFinish: false,
      arrayForm: [],
      formArr: [],
      userData: {},
      processId: '',
      processInstanceId: '',
      processKey: '',
      visible: false,
      columns: [
        {
          title: '任务名称',
          align: 'center',
          dataIndex: 'nodeName',
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createDate',
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endDate',
          scopedSlots: { customRender: 'endDate' },
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'nodeState',
          scopedSlots: { customRender: 'nodeState' },
        },
      ],
      dataSource: [],
      ipagination: {
        pageSize: 10,
        showTotal: (total, range) => {
          return '当前' + range[0] + '-' + range[1] + '条' + '\xa0\xa0共' + total + '条'
        },
      },
    }
  },
  created() {
    this.userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
  },
  mounted() {},
  computed: {},
  methods: {
    tabChange(activeKey) {
      if (activeKey == '2') {
        this.dataReload(this.returnJson)
      }
      if (activeKey == '3') {
        this.arrayForm = this.formArr
      }
    },
    openModal(record) {
      nw_getAction(`/diagram/getByProcInstId?procInstId=` + record.processInstanceId)
        .then((res) => {
          console.log(res)
          this.visible = true
          this.dataSource = res.result.historyInfo
          var formArr = new Array()
          for (var i = 0; i < this.dataSource.length; i++) {
            var formObj = {}
            formObj.taskName = this.dataSource[i].nodeName
            formObj.createDateStr = this.dataSource[i].createDate
            if (this.dataSource[i].url != null) {
              formObj['prevForm_designer_id'] = this.dataSource[i].url.substring(0, 32)
              formObj['prevOnline_table_id'] = this.dataSource[i].url.substring(33, 65)
              formObj['prevOnline_data_id'] = this.dataSource[i].url.substring(66, 87)
            }
            formArr.push(formObj)
          }
          console.log(formArr)
          this.formArr = formArr
          this.returnJson = res.result.returnJson
        })
        .catch((error) => {})
    },
    handleCancel() {
      this.dataSource = []
      this.visible = false
      this.loadEasyFlowFinish = false
      this.jsPlumb.reset() //清除绘画缓存
      this.data = {}
    },
    handleOk() {
      this.dataSource = []
      this.visible = false
    },
    // 加载流程图
    dataReload(data) {
      console.log('data', data)
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
    // 加载流程图
    loadEasyFlow() {
      // 初始化节点
      for (var i = 0; i < this.data.nodeList.length; i++) {
        let node = this.data.nodeList[i]
        // 设置源点，可以拖出线连接其他节点
        console.log(node.id, lodash.merge(this.jsplumbSourceOptions, {}))
        this.jsPlumb.makeSource(node.id, lodash.merge(this.jsplumbSourceOptions, {}))
        // // 设置目标点，其他源点拖出的线可以连接该节点
        this.jsPlumb.makeTarget(node.id, this.jsplumbTargetOptions)
      }
      this.$nextTick(() => {
        // console.log(this.data.lineList.length);
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
          this.jsPlumb.repaintEverything()
          this.loadEasyFlowFinish = true
          console.log('loadEasyFlowFinish')
        })
      })
    },
  },
}
</script>
<style scoped>
.seeContent {
  margin: 7%;
}
#formContent {
  /* padding: 5px; */
  background-color: #fff;
  /* border: 1px solid; */
  border-radius: 5px;
  margin: 0 auto;
}
#efContainer {
  position: relative;
  flex: 1;
  height: 100%;
}
.flowPicture {
  width: 700px;
  height: 600px;
  /* pointer-events: none; */
}
</style>