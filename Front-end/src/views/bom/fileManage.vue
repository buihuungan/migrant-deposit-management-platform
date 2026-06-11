<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="产品名称">
          <a-input v-model="name" placeholder="请输入产品名称" />
        </a-form-item>
        <a-form-item label="产品标识解析">
          <a-input v-model="identity" placeholder="请输入产品标识解析" />
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
          <a-button type="primary" icon="plus" @click="plusFile" style="margin-right: 20px">新增</a-button>
          <a-button type="primary" icon="pic-center" :disabled="!hasSelected" :loading="loading" @click="deriveFile">
            派生
          </a-button>
        </a-button-group>
      </div>
      <div style="padding-top: 20px">
        <a-table
          :columns="columns"
          :dataSource="fileList"
          :pagination="ipagination"
          :row-selection="rowSelection"
          :rowKey="(record) => record.id"
        >
          <span slot="action" slot-scope="text, record, index">
            <a @click="editFile(record)">编辑</a>
            <a-divider type="vertical" />
            <a @click="deleteFile(record.id)">删除</a>
            <a-divider type="vertical" />
            <a @click="seeFile(record)">查看</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <div>
      <AnnFile ref="annFile" @closeFile="closeFile"> </AnnFile>
      <EditFile ref="editFile" @closeFile="closeFile"> </EditFile>
      <see-file ref="seeFile" @closeFile="closeFile" @ok="handleOk"></see-file>
      <derive-file ref="deriveFile" @closeFile="closeFile"></derive-file>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import AnnFile from './annFile'
import EditFile from './editFile'
import SeeFile from './seeFile'
import DeriveFile from './deriveFile'
import api from '@/api/index'
import { o_deleteAction } from '@/api/onApi'
import { getAction } from '@/api/manage'
import { p_postAction2 } from '@/api/plmApi'

export default {
  name: 'FileManage',
  components: { AnnFile, EditFile, SeeFile, DeriveFile },
  data() {
    return {
      loading: false,
      selectedRowKeys: [],
      name: '',
      identity: '',
      create_time: '',
      formId: 'a31285722bce40acb9383ce8a49f971c',
      onlineTableId: '8139e49550384b02adb8051b18a2f8c6',
      onlineDataId: '',
      fileList: [],
      columns: [
        {
          title: '产品名称',
          align: 'center',
          width: '10%',
          dataIndex: 'name',
        },
        {
          title: '产品标识解析',
          align: 'center',
          width: '10%',
          dataIndex: 'identity',
          ellipsis: true,
        },
        {
          title: '上传文件名称',
          align: 'center',
          width: '20%',
          dataIndex: 'docPath[0].fileName',
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
          title: '物料bom分类',
          align: 'center',
          width: '20%',
          dataIndex: 'materialBomCategory',
        },
        {
          title: '上传日期',
          width: '15%',
          align: 'center',
          dataIndex: 'createTime',
        },
        {
          title: '操作',
          width: '15%',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
        },
      ],
      ipagination: {
        pageSize: 10,
        showTotal: (total, range) => {
          return '当前' + range[0] + '-' + range[1] + '条' + '\xa0\xa0共' + total + '条'
        },
      },
    }
  },
  computed: {
    rowSelection() {
      const { selectedRowKeys } = this
      return {
        selectedRowKeys,
        onChange: this.onSelectChange,
        hideDefaultSelections: true,
        selections: [
          {
            key: 'all-data',
            text: 'Select All Data',
            onSelect: () => {
              this.selectedRowKeys = [...Array(46).keys()] // 0...45
            },
          },
        ],
        onSelect: this.onSelection,
      }
    },
    hasSelected() {
      return this.selectedRowKeys.length > 0
    },
  },
  mounted() {
    this.loadData()
  },
  methods: {
    onSelection() {
      if (this.selectedRowKeys.length > 1) {
        this.$message.info('只能选择一个')
      }
    },
    handleOk() {
      this.loadData()
    },
    //派生
    deriveFile() {
      console.log('到这里')
      var id = this.selectedRowKeys[0]
      var newFile = {}
      this.fileList.filter((file) => {
        if (file.id == id) {
          newFile = file
        }
      })
      this.$refs.deriveFile.openModal(newFile)
    },
    onSelectChange(selectedRowKeys) {
      console.log('selectedRowKeys changed: ', selectedRowKeys)
      console.log(this.ipagination)
      this.selectedRowKeys = selectedRowKeys
    },
    //重置查询条件
    reData() {
      this.name = ''
      this.identity = ''
    },
    //查看文件
    seeFile(record) {
      this.$refs.seeFile.openModal(record)
    },
    //编辑文件
    editFile(record) {
      this.onlineDataId = record.id
      this.create_time = record.createTime
      this.$refs.editFile.openModal(this.formId, this.onlineTableId, this.onlineDataId, this.create_time)
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
    //关闭文件新增编辑查看页面
    closeFile() {
      this.loadData()
    },
    //打开文件新增页面
    plusFile() {
      this.$refs.annFile.openModal(this.formId)
    },
    //加载所有的文件数据
    loadData() {
      this.fileList = []
      let requestJson = {
        name: this.name,
        identity: this.identity,
      }
      p_postAction2(`/bom/list`, JSON.stringify(requestJson))
        .then((res) => {
          console.log(res)
          if (res.result) {
            let records = res.result.records
            for (var i = 0; i < records.length; i++) {
              records[i].docPath = JSON.parse(records[i].docPath)
              records[i].key = i
            }
            this.fileList = records
            console.log(this.fileList)
          }
        })
        .catch((err) => {
          console.log(err)
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