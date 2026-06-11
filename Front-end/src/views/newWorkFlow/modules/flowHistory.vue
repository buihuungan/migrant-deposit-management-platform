<template>
  <a-modal :centered="true" :footer="null" title="查看历史" :visible="visible" :destroyOnClose="true" @cancel="handleCancel"
    width="75%" :zIndex="100">
    <div>
      <a-tabs default-active-key="1" tab-position="left" @change="tabChange">
        <a-tab-pane key="1" tab="历史列表">
          <a-table bordered :columns="columns" :dataSource="dataSource" rowKey="id">
            <span slot="nodeState" slot-scope="text, record, index">
              <span v-if="record.state == 'Created'">已创建</span>
              <span v-if="record.state == 'Withdraw'">已退回</span>
              <span v-if="record.state == 'Completed'">已通过</span>
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
                <a-tooltip :title="`${node.name} - ${getNodeState(node.state)}`">
                  <flow-node :key="node.id" :id="node.id" :node="node" type="design">
                    <span class="node-name">{{ node.name }}</span>
                  </flow-node>
                </a-tooltip>
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
              <a style="margin-right: 20px">创建日期：{{ item.createDateStr }}</a>
              <a>处理人：{{ item.submitter }}</a>
              <approve-task :formId="item.prevForm_designer_id" :tableId="item.prevOnline_table_id"
                :dataId="item.prevOnline_data_id" v-if="item.prevForm_designer_id != undefined"></approve-task>
            </a-timeline-item>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-modal>
</template>
<script>
import flowNode from './node'
import api from '@api'
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
          title: '处理人',
          align: 'center',
          dataIndex: 'submitter',
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
  mounted() {
    this.setContainerWidth();  // 初始设置容器宽度
    window.addEventListener('resize', this.setContainerWidth);  // 添加窗口大小变化监听
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.setContainerWidth);  // 清除事件监听
  },
  computed: {},
  methods: {
    // 设置流程图容器宽度
    setContainerWidth() {
      const modalWidth = document.querySelector('.ant-modal').offsetWidth;  // 获取弹框宽度
      const containerWidth = modalWidth * 0.9; // 使容器宽度为弹框宽度的 90%
      this.$refs.efContainer.style.width = `${containerWidth}px`;  // 更新容器宽度
    },
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
        .then(async (res) => {
          console.log('diagram/getByProcInstId  response', res)
          this.visible = true
          // 猜测是流程详情，后端接口没给这个数据
          this.dataSource = res.result.historyInfo

          //新增获取处理人姓名
          const promises = this.dataSource.map(async (item) => {
            if (item.assignee) {
              try {
                item.submitter = await this.getUserName(item.assignee);
              } catch (error) {
                console.error("获取处理人姓名失败:", error);
                item.submitter = "未知处理人";
              }
            } else {
              item.submitter = '';
            }
            return item;
          });

          // 等待所有处理完成后更新 dataSource，不然会导致处理人姓名展示不出来
          Promise.all(promises).then((updatedDataSource) => {
            this.$set(this, 'dataSource', updatedDataSource);
            console.log("最终更新后的 dataSource:", this.dataSource);

            const formArr = this.dataSource.map((item) => {
              const formObj = {
                taskName: item.nodeName,
                createDateStr: item.createDate,
                submitter: item.submitter, // 确保 submitter 已赋值
              };
              if (item.url != null) {
                formObj.prevForm_designer_id = item.url.substring(0, 32);
                formObj.prevOnline_table_id = item.url.substring(33, 65);
                formObj.prevOnline_data_id = item.url.substring(66, 87);
              }
              return formObj;
            });
            console.log('formArr:', formArr);
            this.formArr = formArr;
            this.returnJson = res.result.returnJson;
          });
        })
        .catch((error) => { })
    },
    //通过用户id(assignee)获取用户姓名
    async getUserName(assignee) {
      const code = "2c903614765217d301765217e94f0001";   //这是jeecg中的用户信息表的表单id
      const url = `${api.global_online_baseURL}/cgform/api/form/${code}/${assignee}`;
      try {
        const response = await this.axios.get(url);
        return response.result.realname;
      } catch (error) {
        console.error("请求失败:", error);
        return null;
      }
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
        this.jsPlumb.setZoom(0.8);  // 缩小到 80%
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
    getNodeState(state) {
      switch (state) {
        case 'Completed':
          return '已完成';
        case 'waiting':
          return '未开始';
        case 'Reserved':
          return '已创建';
        case 'Created':
          return '已创建';
        default:
          return '未知状态'; // 如果状态不是上述之一，显示未知状态
      }
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
  width: 100%;
  /* 可根据实际流程图的宽度进行调整 */
  overflow-x: auto;
  /* 开启水平滚动条 */
}

.flowPicture {
  width: 100%;
  height: 600px;
  /* pointer-events: none; */
}
</style>

<style>
html .ant-tooltip {
  max-width: 550px !important;
}

html .ant-tooltip-inner {
  background-color: #ffffff !important;
  color: rgba(0, 0, 0, 0.75) !important;
}
</style>
