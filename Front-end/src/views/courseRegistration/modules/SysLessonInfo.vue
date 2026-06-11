<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
    </div>
    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator" :md="24" :sm="24" style="margin-top: -15px">
      <a-button @click="handleAdd" type="primary" icon="plus" style="margin-top: 16px" :disabled="idNotDep"
        >新建课程</a-button
      >
    </div> -->
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <!-- <a @click="registrationDetails(record)">报名详情</a> -->
          <a @click="downloadTable(record)" > 导出excel表 </a>
        </span>
      </a-table>
    </div>
    <a-modal
      :title="title"
      :visible="visible"
      @ok="handleOK"
      @cancel="handleCancel">
      <a-table
        :dataSource="dataSource1"
        size="middle"
        rowKey="id"
        :columns="columns1"
        :pagination="ipagination1">
      </a-table>
    </a-modal>
    <!-- table区域-end -->
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction, postAction, deleteAction } from '@/api/manage'
import {erp_getAction} from  '@/api/erpApi'
import api from '@/api/index'
import axios from 'axios'
export default {
  name: 'LessonInfo',
  mixins: [JeecgListMixin],
  components: {
  },
  props: {
    departIdList: {
      type: Array,
      default: function () {
        return []
      },
    },
  },
  data() {
    return {
      title: '报名详情',
      visible: false,
      idNotDep: true,
      sys_org_code: '',
      currentDeptId: '',
      dataSource: [],
      dataSource1: [],
      // 表头
      columns: [
        {
          title: '课程名称',
          align: 'center',
          dataIndex: 'lesson_name',
        },
        {
          title: '课程描述',
          align: 'center',
          dataIndex: 'lesson_description',
        },
        {
          title: '课程类别',
          align: 'center',
          dataIndex: 'category',
        },
        {
          title: '对应教师',
          align: 'center',
          dataIndex: 'teacher_name',
        },
        {
          title: '仿真课件',
          align: 'center',
          dataIndex: 'ware_name',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 150,
        },
      ],
      columns1: [
        {
          title: '姓名',
          align: 'center',
          dataIndex: 'realname'
        },
        {
          title: '联系方式',
          align: 'center',
          dataIndex: 'telephone'
        }
      ],

      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['5', '10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: false,
        showSizeChanger: false,
        total: 0,
      },
      ipagination1: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['5', '10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: false,
        showSizeChanger: false,
        total: 0,
        onChange: this.onPageChange.bind(this),
      },
      record: {},
      selectUniversityName: '',
      url: {
        search: '/online/cgreport/api/getColumnsAndData/1356967172441178114',
        change: '/online/cgform/api/form/53349c93563748bf9bac7eec1ea09d3f',
        searchdetail: '/online/cgform/api/form/53349c93563748bf9bac7eec1ea09d3f',
      },
    }
  },
  methods: {
    onPageChange(page, pageSize) {
      // this.ipagination1.current = page
      // this.pageNo = page
      // this.pageSize = pageSize
      // if (this.inSearch) {
      //   //处于查询状态，分页数据不能再次重新查询
      //   this.searchQuery()
      // } else {
      //   this.getAllData()
      // }
    },
    downloadTable(record) {
      axios({
        url: api.server_url + api.global_erp_baseURL+ '/UserRegistration/downloadRegistration',
        params: { 
          lessonId:record.id,
          category: record.category,
          lessonDescription: record.lesson_description,
          wareName: record.ware_name,
          teacherName: record.teacher_name
        },
        responseType: 'blob', // 表明返回服务器返回的数据类型)
      })
      .then(res => {
        let blob = new Blob([res.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        // let fileName = '测试报告.xlsx'
        let fileName = record.lesson_name +  '报名表.xlsx'
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          var link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          //释放内存
          window.URL.revokeObjectURL(link.href)
        }
        
      })
    },
    handleCancel() {
      this.visible = false
    },
    handleOK() {
      this.visible = false
    },
    //报名详情
    registrationDetails(record) {
      let params = {
        lessonId: record.id
      } 
      erp_getAction('UserRegistration/getAllStudentByLessonId',params)
      .then(res =>{
        this.dataSource1 = res.result
        this.ipagination1.total = res.result.length
        this.visible = true
      })
    },
    loadData(arg) {
      this.dataSource = []
      this.ipagination.total = 0
      if (!this.departIdList || !this.departIdList.length) {
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      let params = {
        order: 'desc',
        pageNo: this.ipagination.current,
        pageSize: 10,
      }
      params.org_code = this.sys_org_code
      getAction(this.url.search, params).then((res) => {
        this.dataSource = res.result.data.records
        this.ipagination.total = res.result.data.total
      })
    },
    open(record) {
      this.sys_org_code = record.id
      this.idNotDep = false
      this.record = record
      this.currentDeptId = record.id
      this.loadData(1)
    },
  },
}
</script>
<style scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 3px;
}

.ant-card {
  margin-left: -30px;
  margin-right: -30px;
}

.table-page-search-wrapper {
  margin-top: -16px;
  margin-bottom: 16px;
}
</style>
