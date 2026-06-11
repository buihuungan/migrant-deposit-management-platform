<template>
  <!-- 物料领取 -->
  <div class="material" style="display: grid">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item label="订单编号：" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
              <a-input placeholder="请输入订单编号搜索" v-model="searchIdentity"></a-input>
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
    </div>
    <!-- 物料领取逻辑上不允许自行新增订单信息，订单信息来源于物料配送单，如果后期需要新增功能，此时将下面三行注释取消即可 -->
    <!-- <div class="table-operator" style="margin: 5px 0 10px 2px">
      <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">新增</a-button>
    </div> -->
    <div class="article-table" style="white-space: nowrap;overflow:auto">
      <a-table
        :rowKey="record => record.id"
        style="height: 500px"
        ref="table"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
      >
        <span slot="action" slot-scope="text, record">
          <div>
            <a :disabled="record.wlStatus != 0" @click="materialReceiving(record)">领取物料</a>
            <a-divider type="vertical" />
            <a :disabled="record.wlStatus == 0" @click="materialReturn(record)">{{ wl_name(record.wlStatus) }}</a>
            <a-divider type="vertical" />
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link" @click="e => e.preventDefault()"
                >更多
                <a-icon type="down" />
              </a>
              <a-menu slot="overlay">
                <a-menu-item><a @click="handleDetail(record)">详情</a></a-menu-item>
                <a-menu-item>
                  <a-popconfirm
                    title="确定删除吗?"
                    cancelText="取消"
                    okText="确定"
                    @confirm="() => handleDelete(record)"
                  >
                    <a-icon slot="icon" type="question-circle-o" style="color: red" />
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </div>
        </span>
      </a-table>
    </div>
    <detail ref="modalform" @ok="handleOk"> </detail>
  </div>
</template>

<script>
import detail from './modules/detail'
import { Message } from 'element-ui'
import { order_getAction } from '@/api/orderApi'
import { o_getAction, o_deleteAction, o_postAction, o_putAction } from '@/api/onApi'
import axios from 'axios'
import { USER_ID } from '@/store/mutation-types'
import Vue from 'vue'
import { ACCESS_TOKEN ,TENANT_ID} from '@/store/mutation-types'
import api from '@/api/index'
export default {
  components: {
    detail
  },
  data() {
    return {
      onlineId: '2c94f38a76dc81a50176dc81a50b0000',//物料配送单id
      addOrEditFlag: 1,
      columns: [
        {
          title: '工单编号',
          align: 'center',
          width: '25%',
          dataIndex: 'orderId'
        },
        {
          title: '创建日期',
          align: 'center',
          width: '15%',
          dataIndex: 'createTime'
        },
        {
          title: '需求工厂',
          align: 'center',
          width: '10%',
          dataIndex: 'productionPlantId'
        },
        {
          title: '产线',
          align: 'center',
          width: '10%',
          dataIndex: 'produtionLineId'
        },
        {
          title: '备注',
          align: 'center',
          width: '20%',
          dataIndex: 'remark'
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '20%',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      pageNo: 1,
      pageSize: 10,
      dataSource: [],
      ipagination: {
        current: 0,
        total: 0,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        pageSize: 10,
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true
      },
      searchIdentity: '',
      inSearch: false
    }
  },
  computed: {
    wl_name() {
      return function(status) {
        if (status == 2) {
          return '物料已退回'
        } else {
          return '物料退回'
        }
      }
    }
  },
  created() {
    this.getAllData()
  },
  methods: {
    toHump(str) {
      // 将字符串通过 下划线 拆分成多段 形成一个数组
      var strArr = str.split('_');

      // 将数组中每个元素的第一个字母修改成大写形式
      // charAt(0) 返回该元素的第一个字母 user -> u
      // substring(1) 将字符串从第一个字母开始截取 user -> ser
      for (let i = 1; i < strArr.length; i++) {
        strArr[i] = strArr[i].charAt(0).toUpperCase() + strArr[i].substring(1);
      }
      // 数组转换成字符串
      return strArr.join('');
    },

    transData(souceData) {
      return souceData.map(item => {
      // 准备最后返回的对象
      let obj = {}
      Object.keys(item).forEach(key => {
        if (/\_(\w)/.test(key)) {
        // 如果需要转化 则进行驼峰转化
        let _key = this.toHump(key)
        obj[_key] = item[key]
        } else {
        // 如果不需要直接赋值
        obj[key] = item[key]
        }
      })
      this.dataSource.push(obj);
      return obj
      })
    },


    //查询操作
    searchQuery() {
      this.inSearch = true
      let params = {
        pageSize: this.pageSize,
        pageNo: this.pageNo,
        column: 'createTime',
        order: 'desc'
      }
      params.superQueryMatchType = 'and'
      params.superQueryParams = escape(
        JSON.stringify([
          {
            rule: 'like',
            type: 'text',
            val: this.searchIdentity,
            field: 'order_id'
          }
        ])
      )
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params)
        .then(res => {
          console.log( 'res',res)
          this.dataSource = []
          this.transData(res.result.records)
          this.ipagination.current = 1
          this.ipagination.total = res.result.total
          // this.dataSource = res.result.records
        })
        .catch(err => {
          Message({
            showClose: true,
            message: '数据获取失败！',
            type: 'error'
          })
        })
    },
    searchReset() {
      this.inSearch = false
      this.searchIdentity = ''
      this.pageSize = 10
      this.pageNo = 1
      this.getAllData()
    },

    getAllData() {
      // let params = {
      //   pageSize: this.pageSize,
      //   pageNo: this.pageNo,
      //   column: 'createTime',
      //   order: 'desc'
      // }
      // params.superQueryMatchType = 'and'
      // o_getAction(`/cgform/api/getData/` + this.onlineId, params)
      //   .then(res => {
      //     this.dataSource = res.result.records
      //     this.ipagination.total = res.result.total
      //   })
      //   .catch(err => {
      //     Message({
      //       showClose: true,
      //       message: '数据获取失败！',
      //       type: 'error'
      //     })
      //   })
      let params = {
        pageNo: this.pageNo,
        pageSize: this.pageSize,
        exampleId: this.$store.state.exampleId
      }
      axios({
        url: api.server_url + api.global_erp_baseURL + '/materialOutBill/selectData',
        method: 'get',
        params: params,
        headers: {
          'userid':  Vue.ls.get(USER_ID),
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },
      }).then(res => {
        console.log('res',res)
        this.dataSource = res.data.result.list.records
        this.ipagination.total = res.data.result.list.total
        console.log('dataSource',this.dataSource)
      })
    },
    //新增
    handleAdd() {
      this.addOrEditFlag = 1
      this.$refs.modalform.openModal('', this.addOrEditFlag)
    },
    //编辑
    handleEdit(record) {
      this.addOrEditFlag = 2
      this.$refs.modalform.openModal(record, this.addOrEditFlag)
    },
    //详情
    handleDetail(record) {
      this.addOrEditFlag = 3
      this.$refs.modalform.openModal(record, this.addOrEditFlag)
    },
    //领取物料
    materialReceiving(record) {
      this.addOrEditFlag = 4
      this.$refs.modalform.openModal(record, this.addOrEditFlag)
    },
    //物料退回
    materialReturn(record) {
      this.addOrEditFlag = 5
      this.$refs.modalform.openModal(record, this.addOrEditFlag)
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
      o_deleteAction(`/cgform/api/form/${this.onlineId}/${record.id}`)
        .then(res => {
          Message({
            showClose: true,
            message: '删除成功！',
            type: 'success'
          })
          this.getAllData()
        })
        .catch(err => {
          Message({
            showClose: true,
            message: '删除失败！',
            type: 'error'
          })
        })
    },
    handleOk() {
      this.getAllData()
    }
  }
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
.material {
  background: #fff;
  padding: 24px;
}
</style>
