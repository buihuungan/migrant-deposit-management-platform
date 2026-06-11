<template>
  <div>
    <div>
      <a-card :bordered="false">
        流程类别：
        <a-select
          placeholder="请选择流程类别"
          style="width: 200px"
          @change="getflowConfig"
          :defaultActiveFirstOption="true"
        >
          <a-select-option v-for="item in processCategories" :key="item.id" :value="item.processId">
            {{ item.category_name }}
          </a-select-option>
        </a-select>
        <div class="flowConfig">
          <div style="padding-top: 20px">
            <a-table :columns="columns" bordered :dataSource="flowConfigData" rowKey="processId">
              <span class="flowNameSpan" slot="name" slot-scope="text, record">
                <a-icon type="cluster" />
                {{ record.name }}
              </span>
              <span slot="action" slot-scope="text, record">
                <a @click="startProcess(record)">发起流程</a>
              </span>
            </a-table>
          </div>
        </div>
      </a-card>
    </div>
    <div>
      <annTask ref="modalform"> </annTask>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
import annTask from './modules/annTask'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import { nw_postAction, nw_postAction1, nw_getAction } from '@api/newWorkApi'
export default {
  name: 'flowConfig',
  components: {
    annTask
  },
  data() {
    return {
      form: '',
      name: '',
      id: '',
      flowConfigData: [],
      columns: [
        {
          title: '流程名称',
          align: 'center',
          dataIndex: 'name',
          scopedSlots: { customRender: 'name' },
        },
        {
          title: '流程类别',
          align: 'center',
          dataIndex: 'categoryName',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      processCategories: [{ category_name: '所有流程', functional_department: '', id: '' }],
    }
  },
  mounted() {
    this.getProcessCatagory()
  },
  methods: {
    // 获取流程类别
    getProcessCatagory() {
      let params = { pageNo: 1, pageSize: 9999 }
      o_getAction('/cgreport/api/getColumnsAndData/1346455081826521090', params).then((res) => {
        this.processCategories = this.processCategories.concat(res.result.data.records)
        this.getflowConfig()
      })
    },
    //得到所有的流程
    getflowConfig(x) {
      let url = '/process/processList/{categoryId}?categoryId' + (x == undefined ? '' : x)
      nw_getAction(url)
        .then((res) => {
          if (res.success) {
            let flowConfigData = res.result
            let processCategories = this.processCategories
            for (var i = 0; i < flowConfigData.length; i++) {
              for (var j = 0; j < processCategories.length; j++) {
                if (flowConfigData[i].categoryId == processCategories[j].id) {
                  flowConfigData[i].categoryName = processCategories[j].category_name
                }
              }
            }
            this.flowConfigData = flowConfigData
          } else {
            this.$message.error('查询可开启的流程失败')
          }
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //开启流程
    startProcess(record) {
      let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
      axios.defaults.headers.common['userName'] = userData.value.username
      nw_getAction(`/process/startProcess/{processId}?processId=` + record.processId)
        .then((res) => {
          console.log(res)
          if (res.success) {
            this.$message.success('开启流程成功')
            var formDesignerId = res.result.startProcessVO.formDesignerId
            var onlineDataId = res.result.startProcessVO.onlineDataId
            var onlineTableId = res.result.startProcessVO.onlineTableId
            var taskId = res.result.fistTaskId
            this.$refs.modalform.openModal(formDesignerId, onlineDataId, onlineTableId, taskId)
          } else {
            this.$message.error('开启流程失败')
          }
        })
        .catch((error) => {
          console.log(error)
        })
    },
  },
}
</script>
<style scoped>
.flowNameSpan {
  display: inline-block;
  width: 100%;
}
.iconfont {
  font-size: 20px;
}
.iconfont .icon-liucheng {
  float: left;
}
</style>