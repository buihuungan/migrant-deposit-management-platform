<template>
  <div class="background">
    <div class="add">
      <a-button type="primary" @click="add">新增</a-button>
    </div>
    <div class="table">
      <!-- :pagination="ipagination" -->
      <a-table :loading="tableLoading" :pagination="ipagination" :columns="columns" :data-source="formTableData">
        <span slot="action" slot-scope="text, record">
          <a-popconfirm
            title="确定开始生产吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => startProduction(record)"
          >
            <a-icon slot="icon" type="question-circle-o" style="color: green" />
            <a :disabled="record.productionStatus != 0">{{ sp_name(record.productionStatus) }}</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定取消生产吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => cancelProduction(record)"
          >
            <a-icon slot="icon" type="question-circle-o" style="color: red" />
            <a :disabled="record.productionStatus != 1">取消生产</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定生产完毕吗?"
            cancelText="取消"
            okText="确定"
            @confirm="() => productionCompleted(record)"
          >
            <a-icon slot="icon" type="question-circle-o" style="color: green" />
            <a :disabled="record.productionStatus != 1">生产完毕</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>
    <addProduct ref="modalform"></addProduct>
  </div>
</template>

<script>
import axios from 'axios'
import addProduct from './modules/addProduct'
import { order_postAction } from '@api/orderApi'
import { tm_postAction } from '@/api/templateApi'
import { erp_getAction } from '@/api/erpApi'
import { putAction } from '@api/manage'
import { Message } from 'element-ui'

export default {
  components: {
    addProduct,
  },
  data() {
    return {
      prod_identity: '',
      searchData: {
        prod_name: '',
      },
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        total: 0,
        showSizeChanger: true,
        showQuickJumper: true,
        showTotal: (total, range) => range + ' 共' + total + '条',
        onChange: this.onPageChange.bind(this),
        onShowSizeChange: (current, pageSize) => {
          this.ipagination.current = 1
          this.ipagination.pageSize = pageSize
          this.loadData()
        },
      },
      tableLoading: false,
      columns: [
        {
          title: '产品名称',
          width: '30%',
          dataIndex: 'prodName',
          key: 'prodName',
          align: 'center',
        },
        {
          title: '生产数量',
          width: '10%',
          dataIndex: 'prodNumber',
          key: 'prodNumber',
          align: 'center',
        },
        {
          title: '生产日期',
          width: '20%',
          dataIndex: 'prodDate',
          key: 'prodDate',
          align: 'center',
        },
        {
          title: '生产产线',
          width: '10%',
          dataIndex: 'prodProductionLine',
          key: 'prodProductionLine',
          align: 'center',
        },
        {
          title: '操作',
          dataIndex: 'action',
          key: 'action',
          width: '30%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      formTableData: [
        // {
        //   prod_name: 12123,
        //   prod_number: 12,
        //   prod_date: 12312,
        //   prod_productionLine: 12123213,
        //   productionStatus: 0,
        // },
      ],
    }
  },
  created() {
    this.loadData()
  },
  computed: {
    sp_name() {
      return function (status) {
        if (status == 1) {
          return '正在生产'
        } else {
          return '开始生产'
        }
      }
    },
  },
  methods: {
    loadData() {
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        exampleId: this.$store.state.exampleId
      }
      erp_getAction(
        `/ProductProduction/Query`,params
      ).then((res) => {
        console.log('res', res)
        this.ipagination.total = res.result.total
        this.formTableData = res.result.records
      })
    },

    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
    // 新增
    add() {
      this.$refs.modalform.openModal()
    },

    // 开始生产按钮
    startProduction(record) {
      console.log('record', record)
      // record.productionStatus = 1
      erp_getAction(`/ProductProduction/setStatus?id=${record.id}&status=1`).then((res) => {
        if (res.code == 200) {
          this.$message.success('开始生产成功')
          this.loadData()
        } else {
          this.$message.warning('物料库存量不足，开始生产失败')
          this.loadData()
        }
      })
    },

    // 取消生产按钮
    cancelProduction(record) {
      erp_getAction(`/ProductProduction/setStatus?id=${record.id}&status=0`).then((res) => {
        if (res.code == 200) {
          this.$message.success('取消生产成功')
          this.loadData()
        } else {
          this.$message.error('取消生产失败')
          this.loadData()
        }
      })
    },

    // 生产完毕按钮
    productionCompleted(record) {
      console.log('record', record)
      this.addProductNumber(record)
      this.addDockingModule(record)

      // 修改当前数据行状态
      erp_getAction(`/ProductProduction/setStatus?id=${record.id}&status=2`).then((res) => {
        if (res.code == 200) {
          this.$message.success('生产成功')
          this.loadData()
        } else {
          this.$message.error('生产失败')
          this.loadData()
        }
      })
    },

    // 添加到对接模块产品表
    async addDockingModule(record) {
      let model = { prefix: '' }
      await putAction('/sys/fillRule/executeRuleByCode/prod_identity_auto', model).then((res) => {
        if (res.success) {
          this.prod_identity = res.result
        }
      })
      let postData0 = {
        identity: '',
        prod_identity: '',
        prod_name: '',
        prod_line_id: '',
        user_id: '',
        prod_number: '',
        plan_id: '',
        lesson_id: '',
        teaching_mode: '',
      }
      let userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
      postData0.user_id = userid.value
      var process = JSON.parse(window.localStorage.getItem('process'))
      postData0.plan_id = process.planId
      postData0.lesson_id = process.lessonId
      postData0.teaching_mode = process.teachingMode
      postData0.identity = this.prod_identity
      postData0.prod_identity = ''
      postData0.prod_name = record.prodName
      postData0.prod_line_id = record.prodProductionLine
      postData0.prod_number = record.prodNumber
      tm_postAction('/cgform/api/form/d89dea4eff4a47a19c356777ff598714', postData0).then((res) => {
        if (res.success) {
          console.log('插入模板成功')
        }
      })
    },

    // 添加仓库成品库存
    addProductNumber(record) {
      let postData = {
        create_by: '',
        name: '',
        identity: '',
        sum: '',
      }
      let userId = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
      postData.create_by = userId.value
      postData.name = record.prodName.split('-')[0]
      postData.identity = record.prodName.split('-')[1].split('/')[1]
      postData.sum = record.prodNumber
      console.log('postData', postData)
      order_postAction('supplyChainInventory/addTotal', postData).then((res) => {})
    },
  },
}
</script>

<style scoped>
.background {
  background: #fff;
  padding: 24px;
}
.add {
  margin-bottom: 5px;
}
</style>
