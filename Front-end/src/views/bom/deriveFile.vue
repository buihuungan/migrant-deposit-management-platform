<template>
  <a-modal :centered="true" title="查看" :visible="visible" :destroyOnClose="true" @cancel="handleCancel" width="75%" :zIndex="100">
    <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="12" :sm="12">
            <a-form-item label="产品名称:">
              <a-input v-model="bom.name" style="width: 300px"></a-input>
            </a-form-item>
          </a-col>
           <a-col :md="12" :sm="12">
            <a-form-item label="产品标识解析:">
              <a-input v-model="bom.identity" style="width: 300px"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :md="24" :sm="24">
            <a-form-item label="保存文件名:">
              <a-input v-model="bom.fileName" style="width: 300px"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
    </a-form>
    <div id="formContent">
      <div class="materielTable">
        <a-table ref="table" size="middle" :columns="columns" :dataSource="dataSource" :pagination="ipagination">
           <template
      v-for="col in ['标识编码', '名称', '数量']"
      :slot="col"
      slot-scope="text, record, index"
    >
      <div :key="col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record['标识编码'], col)"
        />
        <template v-else>
          {{ text }}
        </template>
      </div>
    </template>
    <template slot="operation" slot-scope="text, record, index">
      <div class="editable-row-operations">
        <span v-if="record.editable">
          <a @click="() => save(record['标识编码'])">保存</a>
          <a-popconfirm title="Sure to cancel?" @confirm="() => cancel(record['标识编码'])">
            <a>取消</a>
          </a-popconfirm>
        </span>
        <span v-else>
          <a :disabled="editingKey !== ''" @click="() => edit(record['标识编码'])">编辑</a>
        </span>
      </div>
    </template>
          </span>
        </a-table>
      </div>
    </div>
    <template slot="footer">
      <a-button key="primary" @click="derive">
          派生
        </a-button>
      </template>
  </a-modal>
</template>
<script>
import { p_postAction2 } from '@/api/plmApi'
import axios from 'axios'
import Vue from 'vue'

import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
export default {
  name: 'deriveFile',
  data() {
    return {
      categories: [],
      bom: [],
      cacheData: [],
      editingKey: '',
      visible: false,
      destroy: true,
      columns: [
        {
          title: '标识',
          align: 'center',
          width: '35%',
          dataIndex: '标识编码',
          scopedSlots: { customRender: '标识编码' },
        },
        {
          title: '物料名',
          align: 'center',
          width: '35%',
          dataIndex: '名称',
          scopedSlots: { customRender: '名称' },
        },
        {
          title: '数量',
          align: 'center',
          width: '15%',
          dataIndex: '数量',
          scopedSlots: { customRender: '数量' },
        },
        {
          title: '操作',
          width: '15%',
          align: 'center',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
        },
      ],
      dataSource: [],
      // selectedRowKeys: [],
      ipagination: {
        pageSize: 5,
        showTotal: (total, range) => {
          return '当前' + range[0] + '-' + range[1] + '条' + '\xa0\xa0共' + total + '条'
        },
      },
    }
  },
  mounted() {},
  computed: {},
  methods: {
    //得到所有的分类
    getCategories() {
      let params = {
        pageNo: 1,
        pageSize: -521,
      }
      o_getAction('/cgform/api/getData/76a947fdca644734afc34deeabebc557', {
        params: params,
      }).then((res) => {
        if (res.success) {
          this.categories = res.result.records
        }
      })
    },
    openModal(record) {
      this.bom = record
      this.visible = true
      this.setData(record.bomDetail)
    },
    setData(bomDetail) {
      this.dataSource = JSON.parse(bomDetail)
      this.cacheData = this.dataSource.map((item) => ({ ...item }))
      console.log(this.dataSource)
    },
    handleCancel() {
      this.bom = []
      this.cacheData = []
      this.editingKey = ''
      this.dataSource = []
      this.visible = false
      this.$emit('closeFile')
    },
    //派生
    derive() {
      let requestJson = {
        bom_detail: this.dataSource,
        fileName: this.bom.fileName,
        identity: this.bom.identity,
        name: this.bom.name,
      }
      p_postAction2(`/bom/derive`, JSON.stringify(requestJson))
        .then((res) => {
          console.log(res)
          if (res.result) {
            this.$message.success('派生成功')
          } else {
            this.$message.success('派生失败')
          }
          this.handleCancel()
        })
        .catch((err) => {
          console.log(err)
        })
    },
    handleChange(value, key, column) {
      const newData = [...this.dataSource]
      const target = newData.filter((item) => key === item['标识编码'])[0]
      if (target) {
        target[column] = value
        this.dataSource = newData
      }
    },
    edit(key) {
      console.log(key)
      const newData = [...this.dataSource]
      const target = newData.filter((item) => key === item['标识编码'])[0]
      console.log(target)
      this.editingKey = key
      if (target) {
        target.editable = true
        this.dataSource = newData
      }
    },
    save(key) {
      const newData = [...this.dataSource]
      const newCacheData = [...this.cacheData]
      const target = newData.filter((item) => key === item['标识编码'])[0]
      const targetCache = newCacheData.filter((item) => key === item['标识编码'])[0]
      if (target && targetCache) {
        delete target.editable
        this.dataSource = newData
        Object.assign(targetCache, target)
        this.cacheData = newCacheData
      }
      this.editingKey = ''
      console.log(this.dataSource)
    },
    cancel(key) {
      const newData = [...this.dataSource]
      const target = newData.filter((item) => key === item['标识编码'])[0]
      this.editingKey = ''
      if (target) {
        Object.assign(target, this.cacheData.filter((item) => key === item['标识编码'])[0])
        delete target.editable
        this.dataSource = newData
      }
      console.log(this.dataSource)
    },
  },
}
</script>
<style scoped>
#formContent {
  /* padding: 5px; */
  background-color: #fff;
  /* border: 1px solid; */
  border-radius: 5px;
  margin: 0 auto;
}
.editable-row-operations a {
  margin-right: 8px;
}
</style>