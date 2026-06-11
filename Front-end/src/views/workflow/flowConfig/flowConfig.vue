<template>
  <div>
    <div>
      <a-card :bordered="false">
        流程类别：
        <a-select
          placeholder="请选择流程类别"
          style="width: 200px"
          @change="getflowConfig"
          v-model="value"
          :defaultActiveFirstOption="true"
        >
          <a-select-option v-for="item in processCategories" :key="item.id" :value="item.id">
            {{ item.category_name }}
          </a-select-option>
        </a-select>
        <div class="flowConfig">
          <div style="padding-top: 20px">
            <a-table :columns="columns" bordered :dataSource="flowConfigData" :rowKey="(record) => record.id">
              <!-- :pagination="{pageSizeOptions:['10','20','30'],showSizeChanger:true,showQuickJumper:true,showTotal:(total, range)=>range+' 共'+total+'条'}" -->
              <span class="flowNameSpan" slot="name" slot-scope="text, record">
                <i class="iconfont icon-liucheng"></i>
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
      <AnnTask ref="modalform"> </AnnTask>
    </div>
  </div>
</template>
<script>
import '../../../../public/static/iconfont/iconfont.css'
import CusDialog from '../../../components/FormMaking/components/CusDialog'
import Vue from 'vue'
import axios from 'axios'
import { o_postAction, o_getAction } from '@/api/onApi.js'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
import { getAction } from '@/api/manage'
import { w_postAction, w_getAction } from '../../../api/workapi'
import api from '../../../api/index.js'
import AnnTask from './annTask'
export default {
  name: 'flowConfig',
  components: { AnnTask, CusDialog },
  data() {
    return {
      processCategory: '',
      value: '',
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
      processCategories: [{ category_name: '所有流程', functional_department: '武穴市机关服务中心', id: '' }],
    }
  },
  mounted() {
    this.getProcessCatagory()
  },
  methods: {
    // 获取流程类别
    getProcessCatagory() {
      let params = { pageNo: 1, pageSize: 9999 }
      o_getAction('/cgreport/api/getColumnsAndData/1346455081826521090',params).then((res) => {
        this.processCategories = this.processCategories.concat(res.result.data.records)
        this.getflowConfig()
      })
    },
    //得到所有的流程
    getflowConfig(x) {
      let url = '/process/loadProcess?categoryId=' + (x == undefined ? '' : x)
      w_postAction(url)
        .then((res) => {
          this.processCategory = x
          let flowConfigData = res
          let processCategories = this.processCategories
          for (var i = 0; i < flowConfigData.length; i++) {
            for (var j = 0; j < processCategories.length; j++) {
              if (flowConfigData[i].categoryId == processCategories[j].id) {
                flowConfigData[i].categoryName = processCategories[j].category_name
              }
            }
          }
        //  this.flowConfigData = flowConfigData  //两个重复了,好像没区别
        this.flowConfigData.push(flowConfigData[0])
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //开启流程
    startProcess(row) {
      let id = row.id
      let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
      axios.defaults.headers.common['userName'] = userData.value.username
      w_postAction(`/process/startProcess?processId=` + id)
        .then((res) => {
          this.$message.info('配置流程成功')
          var formDesignerId = res.result.formDesignerId //表单ID
          var taskId = res.result.firstTaskId //表单ID
          this.$refs.modalform.openModal(formDesignerId, taskId, '0')    //   0  代表普通的创建销售订单流程，  1  代表老师给课件预置销售订单数据
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