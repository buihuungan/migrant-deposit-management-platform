<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="工序名称">
          <a-input v-model="process_name" placeholder="请输入工序名称" />
        </a-form-item>
        <a-form-item>
          <a-button-group>
            <a-button type="primary" icon="search" @click="loadData()">查询</a-button>
            <a-button type="primary" icon="reload" @click="reData()">重置</a-button>
          </a-button-group>
        </a-form-item>
      </a-form>
      <div style="margin-top: 20px">
        <a-button-group>
          <a-button type="primary" icon="plus" @click="plusFile">新增</a-button>
        </a-button-group>
      </div>
      <div style="padding-top: 20px">
        <a-table :columns="columns" :dataSource="fileList" rowKey="id">
          <span slot="action" slot-scope="text, record, index">
            <a @click="editFile(record)">编辑</a>
            <a-divider type="vertical" />
            <a @click="deleteFile(record.id)">删除</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <div>
      <AnnFile ref="annFile" @ok="handleOk"> </AnnFile>
      <EditFile ref="editFile" @ok="handleOk"> </EditFile>
    </div>
  </div>
</template>
<script>
import AnnFile from './annFile'
import EditFile from './editFile'
import api from '@/api/index'
import { o_deleteAction, o_getAction } from '@/api/onApi'
import { getAction } from '@/api/manage'

export default {
  name: 'productProcess',
  components: { AnnFile, EditFile },
  data() {
    return {
      category: '',
      process_name: '',
      annFileName: '新增工序',
      editFileName: '编辑工序',
      formId: '06d2e9575e414c6ab057784c1fe365a5',
      onlineTableId: 'cd415454f5a748d19680ce3270587892',
      onlineDataId: '',
      fileList: [],
      columns: [
        {
          title: '工序名称',
          align: 'center',
          dataIndex: 'process_name',
        },
        {
          title: '产品名称',
          align: 'center',
          dataIndex: 'product_name',
        },
        {
          title: '工序文件',
          align: 'center',
          width: '30%',
          dataIndex: 'file[0].fileName',
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
          title: '责任人',
          align: 'center',
          dataIndex: 'person',
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    handleOk() {
      this.loadData()
    },
    //重置查询参数
    reData() {
      this.process_name = ''
    },
    //编辑文件
    editFile(record) {
      this.onlineDataId = record.id
      this.$refs.editFile.openModal(this.formId, this.onlineTableId, this.onlineDataId, this.editFileName)
    },
    //删除文件
    deleteFile(id) {
      this.$elementConfirm('真的要删除这个文件吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        o_deleteAction('/cgform/api/form/' + this.onlineTableId + '/' + id).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.loadData()
          } else {
            this.$message.warning(res.message)
          }
        })
      })
    },
    //打开文件新增页面
    plusFile() {
      this.$refs.annFile.openModal(this.formId, this.annFileName)
    },
    //加载所有的文件数据
    loadData() {
      this.fileList = []
      var queryDateInfo = [{ rule: 'LIKE', type: 'text', val: '', field: 'process_name' }]
      queryDateInfo[0].val = this.process_name
      console.log('queryDateInfo', queryDateInfo)
      let params = {
        column: 'createTime',
        order: 'desc',
        superQueryMatchType: 'and',
        pageNo: 1,
        pageSize: -521,
        superQueryParams: encodeURIComponent(JSON.stringify(queryDateInfo)),
      }
      getAction('/online/cgform/api/getData/' + this.onlineTableId, params).then((res) => {
        if (res.success) {
          let records = res.result.records
          console.log(records)
          this.fileList = res.result.records
        }
      })
    },
  },
}
</script>

<style>
/* .ant-form-inline .ant-form-item{
    display: inline-flex !important;
  } */
.desformspana a {
  color: #1890ff !important;
}
.xbutton {
  float: right;
  height: 30px;
  width: 40px;
  margin-top: 10px;
  margin-right: 10px;
}
</style>