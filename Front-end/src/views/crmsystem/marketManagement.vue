<template>
  <div class="market_management" style="display: grid">
    <!-- 上半部分 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item label="营销活动名：" :labelCol="{ span: 6 }" :wrapperCol="{ span: 17, push: 1 }">
              <a-input
                placeholder="请输入营销活动名搜索"
                v-model="searchIdentity"
                @keyup.enter.native="searchQuery"
              ></a-input>
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

    <div class="table-operator" style="margin: 5px 0 10px 2px">
      <a-button class="ant-btn add" type="primary" icon="plus" @click="handleActivityAdd">新增</a-button>
      <!-- <a-button class="ant-btn" type="primary" icon="vertical-align-bottom" @click="handleAdd()">市场情报模板</a-button> -->
      <a-button
        type="primary"
        :href="`${serverUrl}/plm/common/download${marketInformationExample_url}`"
        ><a-icon type="download" />市场情报模板</a-button
      >
      <a-button type="primary" icon="arrow-download" :href="`${serverUrl}/plm/common/download${activityExample_url}`"
        ><a-icon type="download" />营销活动模板</a-button
      >
      <a-button type="primary" icon="arrow-download" :href="`${serverUrl}/plm/common/download${customerExample_url}`"
        ><a-icon type="download" />具体客户模板</a-button
      >
      <a-button type="primary" icon="arrow-download" :href="`${serverUrl}/plm/common/download${marketPlanExample_url}`"
        ><a-icon type="download" />市场计划模板</a-button
      >
    </div>

    <!-- 表格部分 -->
    <div class="article-table">
      <a-table
        style="height: 500px"
        ref="table"
        size="middle"
        row-key="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
      >
        <template slot="createTime" slot-scope="text, record">
          <span>{{ record.ma_create_time.slice(0, 11) }}</span>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleActivityEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link" @click="(e) => e.preventDefault()"> 更多 <a-icon type="down" /> </a>
            <a-menu slot="overlay">
              <a-menu-item><a @click="handleActivityDetail(record)">详情</a></a-menu-item>
              <a-menu-item>
                <a-popconfirm
                  title="确定删除吗?"
                  cancelText="取消"
                  okText="确定"
                  @confirm="() => handleDeleteActivity(record)"
                >
                  <a-icon slot="icon" type="question-circle-o" style="color: red" />
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <!-- 添加/编辑/详情活动组件 -->
    <activityAddAndEditPDS ref="activityModalform" @ok="handleOk"> </activityAddAndEditPDS>
  </div>
</template>

<script>
import activityAddAndEditPDS from './modules/activityAddAndEditPDS.vue'
import { o_getAction, o_deleteAction } from '@/api/onApi'
import { Message } from 'element-ui'
import api from '@/api/index'
export default {
  components: {
    activityAddAndEditPDS,
  },
  data() {
    return {
      serverUrl: api.server_url,
      marketInformationExample_url: '/市场情报模板_1627267695922.doc',
      activityExample_url: '/营销活动模板_1627279715762.doc',
      customerExample_url: '/客户模板_1627432159517.xls',
      marketPlanExample_url: '/市场计划模板_1627281758490.doc',
      onlineId: 'e223ffdd6b2346c7a71d7435393fa4ea',
      searchIdentity: '',
      inSearch: false,
      columns: [
        {
          title: '营销活动名',
          align: 'center',
          width: '10%',
          dataIndex: 'ma_name',
        },
        {
          title: '市场情报',
          align: 'center',
          width: '15%',
          dataIndex: 'ma_information_file',
          customRender: (text) => (
            <a
              href={api.server_url + `/plm/common/download//` + text}
              download={api.server_url + `/plm/common/download//` + text}
            >
              {text}
            </a>
          ),
        },
        {
          title: '创建时间',
          align: 'center',
          width: '10%',
          dataIndex: 'ma_create_time',
          scopedSlots: { customRender: 'createTime' },
        },
        {
          title: '活动文件',
          align: 'center',
          width: '15%',
          dataIndex: 'ma_file',
          customRender: (text) => (
            <a
              href={api.server_url + `/plm/common/download//` + text}
              download={api.server_url + `/plm/common/download//` + text}
            >
              {text}
            </a>
          ),
        },
        {
          title: '客户文件',
          align: 'center',
          width: '15%',
          dataIndex: 'customer_file',
          customRender: (text) => (
            <a
              href={api.server_url + `/plm/common/download//` + text}
              download={api.server_url + `/plm/common/download//` + text}
            >
              {text}
            </a>
          ),
        },
        {
          title: '市场计划',
          align: 'center',
          width: '15%',
          dataIndex: 'ma_plan_file',
          customRender: (text) => (
            <a
              href={api.server_url + `/plm/common/download//` + text}
              download={api.server_url + `/plm/common/download//` + text}
            >
              {text}
            </a>
          ),
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '15%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      dataSource: [],
      ipagination: {
        current: 1,
        pageSize: 10,
        total: 0,
        showQuickJumper: true,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        onChange: this.activityOnPageChange.bind(this),
      },
      onlyOne: false
    }
  },
  created() {
    this.getData()
  },
  methods: {
    getData() {
      this.onlyOne = false
      let params = {
        column: 'ma_create_time',
        order: 'desc',
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        superQueryMatchType: 'and',
      }
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records
          if(this.dataSource.length === 1) this.onlyOne = true
          this.ipagination.total = res.result.total
        } else {
          Message({
            showClose: true,
            message: '获取活动失败!',
            type: 'error',
          })
        }
      })
    },
    activityOnPageChange(page) {
      this.ipagination.current = page
      if (!this.inSearch) {
        this.getData()
      } else {
        this.searchQuery()
      }
    },
    handleDeleteActivity(record) {
      o_deleteAction(`/cgform/api/form/${this.onlineId}/${record.id}`).then((res) => {
        if(this.onlyOne){
          this.ipagination.current--
        } 
        this.getData()
        Message({
          showClose: true,
          message: '删除成功!',
          type: 'success',
        })
      })
    },
    handleActivityAdd() {
      this.$refs.activityModalform.setFlag(1)
      this.$refs.activityModalform.show()
    },
    handleActivityDetail(record) {
      this.$refs.activityModalform.setFlag(3)
      this.$refs.activityModalform.setForm(record)
      this.$refs.activityModalform.show()
    },
    handleActivityEdit(record) {
      this.$refs.activityModalform.setFlag(2)
      this.$refs.activityModalform.setForm(record)
      this.$refs.activityModalform.show()
    },
    searchQuery() {
      if (!this.inSearch) {
        this.inSearch = true
        this.ipagination.current = 1
      }
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        column: 'createTime',
        order: 'asc',
      }
      params.superQueryMatchType = 'and'
      params.superQueryParams = encodeURIComponent(
        JSON.stringify([
          {
            rule: 'like',
            type: 'text',
            val: this.searchIdentity,
            field: 'ma_name',
          },
        ])
      )
      o_getAction(`/cgform/api/getData/${this.onlineId}`, params)
        .then((res) => {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
          // this.ipagination.current = 1
        })
        .catch((err) => {
          Message({
            showClose: true,
            message: '数据获取失败！',
            type: 'error',
          })
        })
    },
    searchReset() {
      this.inSearch = false
      this.searchIdentity = ''
      this.ipagination.current = 1
      this.getData()
    },
    handleOk() {
      this.getData()
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
.market_management {
  background: #fff;
  padding: 24px;
}

</style>



