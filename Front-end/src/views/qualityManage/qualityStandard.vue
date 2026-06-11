<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="质量标准名称">
          <a-input v-model="quality_name" placeholder="请输入质量标准名称" />
        </a-form-item>
        <a-form-item label="质量标准类别">
          <a-select v-model="category" style="width: 200px" placeholder="请输入质量标准类别">
            <a-select-option v-for="item in categories" :key="item.id" :value="item.quality_category">
              {{ item.quality_category }}
            </a-select-option>
          </a-select>
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
  name: 'qualityStandard',
  components: { AnnFile, EditFile },
  data() {
    return {
      category: '',
      categories: [],
      quality_name: '',
      annFileName: '新增质量标准',
      editFileName: '编辑质量标准',
      formId: '3a27a1629acb40eea044510e0d7f2829',
      onlineTableId: '591ae07858eb4cf8ae50c375abc6520e',
      onlineDataId: '',
      fileList: [],
      columns: [
        {
          title: '质量标准名称',
          align: 'center',
          dataIndex: 'quality_standard_name',
        },
        {
          title: '质量标准类别',
          align: 'center',
          dataIndex: 'quality_standard_category',
        },
        {
          title: '上传文件名称',
          align: 'center',
          width: '30%',
          dataIndex: 'quality_standard_file[0].fileName',
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
          title: '上传日期',
          align: 'center',
          dataIndex: 'create_time',
        },
        {
          title: '上传人员',
          align: 'center',
          dataIndex: 'create_by',
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
  created() {
    this.getCategories()
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
      this.category = ''
      this.quality_name = ''
    },
    //得到所有的质量标准分类
    getCategories() {
      let params = {
        pageNo: 1,
        pageSize: -521,
      }
      o_getAction('/cgform/api/getData/cda92d069c81430b87e7b9884d8f1906', {
        params: params,
      }).then((res) => {
        if (res.success) {
          this.categories = res.result.records
        }
      })
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
      var queryDateInfo = [
        { rule: 'LIKE', type: 'text', val: '', field: 'quality_standard_name' },
        { rule: 'LIKE', type: 'text', val: '', field: 'quality_standard_category' },
      ]
      queryDateInfo[0].val = this.quality_name
      queryDateInfo[1].val = this.category
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
          for (let i = 0; i < records.length; i++) {
            //   if (records[i].quality_standard_file != '' && records[i].quality_standard_file[0].dbpath != undefined) {
            //     records[i].quality_standard_file[0].dbpath =
            //       api.server_url + `/plm/common/download` + records[i].quality_standard_file[0].dbpath
            //   }
          }
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