<template>
  <a-modal :centered="true" title="物料添加" :visible="visible" @cancel="handleCancel" width="60%" :footer="false" :zIndex="100">
    <div id="formContent">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="9" :sm="9">
            <a-form-item label="产品名或标识：">
              <a-input placeholder="请输入关键字搜索" v-model="searchKey"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item>
              <a-button type="primary" @click="searchQuery">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <div class="materielTable">
        <a-table
          style="height: 200px;margin-top:25px;"
          ref="table"
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource2"
          :pagination="ipagination"
        >
          <span slot="modelNum" slot-scope="text, record">
                <a-input v-model="record.modelNum"></a-input>
          </span>
          <span slot="action" slot-scope="text, record">
            <a-popconfirm title="确定添加吗?" cancelText="取消" okText="确定" @confirm="() => handleAdd(record)">
              <a-icon slot="icon" type="question-circle-o" style="color: red" />
              <a>添加</a>
            </a-popconfirm>
          </span>
        </a-table>
      </div>
    </div>
  </a-modal>
</template>
<script>
import { im_postAction, im_getAction } from '@/api/icimApi.js'
import Vue from 'vue'
import axios from 'axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'
//import api from '@/api/index'
import api from '../../../api/index.js'
import { Message } from 'element-ui'
import qs from 'qs'

export default {
  name: 'addModal',
  props: ['dataSource'],
  data() {
    return {
      visible: false,
      searchKey: '',
      // modelNum: '',
      columns: [
        {
          title: '标识',
          align: 'center',
          width: '30%',
          dataIndex: 'identity',
        },
        {
          title: '物料名',
          align: 'center',
          width: '30%',
          dataIndex: 'modelName',
        },
        {
          title: '数量',
          dataIndex: 'modelNum',
          width: '15%',
          align: 'center',
          scopedSlots: { customRender: 'modelNum' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '20%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      dataSource2: [],
      pageNo: 1,
      pageSize: 6,
      ipagination: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        pageNo: 1,
        pageSize: 6,
        // onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
    }
  },
  computed: {},
  methods: {
    openModal() {
      this.visible = true
      this.loadData()
    },
    loadData() {
      console.log('加载数据')
      var that = this
      im_postAction(
        '/docking/modelList',
        qs.stringify({ category: 'materiel', pageNo: that.pageNo, pageSize: that.pageSize })
      )
        .then((res) => {
          console.log('res', res)
          this.dataSource2 = res.data.result.list
        })
        .catch((err) => {
          console.log(err)
        })
    },
    searchQuery() {
      console.log('查询') //2945780R1A
      axios
        .post(
          `${api.server_url}/identity/docking/modelList`,
          qs.stringify(
            { category: 'materiel', key: this.searchKey, pageNo: this.pageNo, pageSize: this.pageSize },
            {
              headers: {
                'X-Access-Token': Vue.ls.get(ACCESS_TOKEN),
                'Content-Type': 'application/x-www-form-urlencoded',
              },
            }
          )
        )
        .then((res) => {
          console.log('res', res)
          this.dataSource2 = res.data.result.list
        })
        .catch((err) => {
          console.log(err)
        })
    },

    searchReset() {
      console.log('重置')
      this.searchKey = ''
      this.loadData()
    },
    handleAdd(record) {
      console.log('添加',record)
      // if (this.modelNum) {
      //   record.modelNum = this.modelNum
      // }
      let flag = 1
      this.dataSource.forEach((item, index) => {
        if (item.modelId === record.modelId) {
          flag = 0
          return
        }
      })
      if (flag) {
        this.dataSource.push(record)
        this.handleCancel()
      } else {
        Message({
          type: 'false',
          showClose: true,
          message: '已存在，请勿重复添加!',
        })
      }
      console.log('dataSource', this.dataSource)
    },
    handleCancel() {
      this.dataSource2 = []
      this.searchKey = ''
      this.visible = false
    },
  },
}
</script>
<style scoped>
#formContent {
  height: 400px;
  /* padding: 5px; */
  background-color: #fff;
  /* border: 1px solid; */
  border-radius: 5px;
  margin: 0 auto;
}
.number > .ant-form-item > .ant-form-item-control-wrapper {
  width: 50%;
}
</style>