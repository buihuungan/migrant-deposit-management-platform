<!--
 * @Author: Lu
 * @Date: 2021-11-08 21:03:38
 * @LastEditTime: 2021-11-10 18:28:24
 * @LastEditors: Please set LastEditors
 * @Description: 节点注册
 * @FilePath: \simulation\src\views\identification\nodeRegistration.vue
-->
<template>
  <!-- 节点注册 -->
  <div class="purchase_demand_sheet" style="display: grid">
    <div class="table-operator" style="margin: 5px 0 10px 2px">
      <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">新增</a-button>
    </div>
    <div class="article-table" style="overflow: auto">
      <a-table
        style="height: 500px"
        ref="table"
        size="middle"
        :rowKey="(record) => record.nodePrefix"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" cancelText="取消" okText="确定" @confirm="() => handleDelete(record)">
            <a-icon slot="icon" type="question-circle-o" style="color: red" />
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>
    <a-modal v-model="visible" :title="title" :destroyOnClose="true" @ok="handleOk" @cancel="cancel">
      <a-form-model :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-form-model-item label="是二级节点" v-if="addOrEditFlag == '1'">
          <a-switch v-model="form.second_node" />
        </a-form-model-item>
        <a-form-model-item label="节点前缀">
          <a-input-group compact>
            <a-select v-if="form.second_node == false" @change="changePrefix" style="width: 30%">
              <a-select-option v-for="item in secondNodeList" :key="item"> {{ item }} </a-select-option>
            </a-select>
            <a-input style="width: 50%" v-model="form.nodePrefix" />
          </a-input-group>
        </a-form-model-item>
        <a-form-model-item label="节点名称">
          <a-input v-model="form.nodeName" style="marigin-left: 10px" />
        </a-form-model-item>
        <a-form-model-item label="节点地址">
          <a-input v-model="form.nodeAddress" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { tm_postAction } from '@/api/templateApi'
import { e_postAction, e_getAction, e_putAction, e_deleteAction } from '@/api/eduApi'
import axios from 'axios'
import { Message } from 'element-ui'
export default {
  components: {},
  data() {
    return {
      tableid: '',
      secondNodeList: [],
      visible: false,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
      nodeFrom: '',
      form: {
        nodePrefix: '',
        second_node: true,
        nodeName: '',
        nodeAddress: '',
      },
      title: '新增',
      onlineId: '597983d3ade2449aa91338e05148332a',
      addOrEditFlag: '1',
      columns: [
        {
          title: '节点前缀',
          width: '25%',
          dataIndex: 'nodePrefix',
        },
        {
          title: '节点名称',
          width: '25%',
          dataIndex: 'nodeName',
        },
        {
          title: '节点地址',
          width: '25%',
          dataIndex: 'nodeAddress',
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '25%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      pageNo: 1,
      pageSize: 10,
      dataSource: [],
      ipagination: {
        current: 1,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        pageSize: 10,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
      },
      inSearch: false,
      records: {},
    }
  },
  created() {
    this.getAllData()
  },
  methods: {
    changePrefix(e) {
      console.log('eee', e)
      this.nodeFrom = e
    },
    getAllData() {
      this.secondNodeList = []
      e_getAction(`/thirdNode/allnodes`).then((res) => {
        console.log('查询到的', res)
        this.dataSource = res.result
        res.result.forEach((item) => {
          this.secondNodeList.push(item.nodePrefix)
        })
      })
    },
    //新增
    handleAdd() {
      this.visible = true
      this.addOrEditFlag = '1'
    },
    //编辑
    handleEdit(record) {
      console.log('编辑的record', record)
      this.title = '编辑'
      this.visible = true
      this.form.nodePrefix = record.nodePrefix
      this.form.nodeName = record.nodeName
      this.form.nodeAddress = record.nodeAddress
      this.tableid = record.id
      this.addOrEditFlag = '2'
      if (record.nodeFrom) {
        this.nodeFrom = record.nodePrefix
      } else {
        this.nodeFrom = ''
      }
    },

    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.pageNo = page
      this.pageSize = pageSize
      if (this.inSearch) {
        //处于查询状态，分页数据不能再次重新查询
        this.searchQuery()
      } else {
        this.getAllData()
      }
    },
    //删除数据
    handleDelete(record) {
      let Url = ''
      if (record.nodeFrom) {
        Url = '/thirdNode'
      } else {
        Url = '/nodeRegistration'
      }
      e_deleteAction(`${Url}/${record.id}`)
        .then((res) => {
          if (res.success) {
            Message({
              showClose: true,
              message: '删除成功！',
              type: 'success',
            })
            this.getAllData()
          } else {
            Message({
              showClose: true,
              message: '删除失败！刷新后再次尝试！',
              type: 'error',
            })
          }
        })
        .catch((err) => {
          Message({
            showClose: true,
            message: '删除失败！',
            type: 'error',
          })
        })
    },
    cancel() {
      console.log('取消')
      this.form.nodePrefix = ''
      this.form.nodeName = ''
      this.form.nodeAddress = ''
    },
    handleOk() {
      let { nodePrefix, nodeName, nodeAddress } = this.form
      let params = { nodePrefix, nodeName, nodeAddress }
      if (!this.form.second_node) {
        params.nodeFrom = this.nodeFrom
        params.nodePrefix = this.nodeFrom + params.nodePrefix
      }
      let Eurl = this.nodeFrom == '' ? '/nodeRegistration' : '/thirdNode'
      if (this.addOrEditFlag === '1') {
        e_postAction(`${Eurl}`, params).then((res) => {
          Message({
            showClose: true,
            message: '新增成功！',
            type: 'success',
          })
        })
      } else {
        //编辑
        params.id = this.tableid
        e_putAction(`${Eurl}`, params).then((res) => {
          Message({
            showClose: true,
            message: '编辑成功！',
            type: 'success',
          })
        })
      }
      this.visible = false
      this.form.nodePrefix = ''
      this.form.nodeName = ''
      this.form.nodeAddress = ''
      this.getAllData()
    },
  },
}
</script>

<style scoped>
.ant-col-sm-24 {
  height: 40px;
  line-height: 40px;
}
.el-range-input {
  margin-left: 15px !important;
}
.purchase_demand_sheet {
  background: #fff;
  padding: 24px;
}
</style>
