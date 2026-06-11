<template>
<div>
 <a-modal :centered="true" title="查看" :visible="visible" @cancel="handleCancel" width="75%" :zIndex="100">
    <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="24" :sm="24">
            <a-form-item label="产品标识解析:">
              <a-input v-model="identity" style="width: 300px"></a-input>
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
        <span>
          <a @click="() => chooseMaterial(record)">选择物料</a>
        </span>
      </div>
    </template>
          </span>
        </a-table>
      </div>
    </div>
    <template slot="footer">
      <a-button key="primary" @click="saveMaterial">
          保存
        </a-button>
        <a-button key="back" @click="handleCancel">
          返回
        </a-button>
      </template>
  </a-modal>
  <choose-material ref="chooseMaterial"   @chooseOk1="chooseOk1"></choose-material>
</div>
 
</template>
<script>
import { p_postAction2 } from '@/api/plmApi'
import axios from 'axios'
import Vue from 'vue'
import chooseMaterial from './chooseMaterial'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
import ChooseMaterial from './chooseMaterial.vue'
export default {
  name: 'seeFile',
  components: { chooseMaterial },
  data() {
    return {
      record: [],
      cacheData: [],
      editingKey: '',
      identity: '',
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
          width: '10%',
          dataIndex: '数量',
          scopedSlots: { customRender: '数量' },
        },
        {
          title: '操作',
          width: '20%',
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
      oldMaterial: {},
    }
  },
  mounted() {},
  computed: {},
  methods: {
    chooseOk1(newMaterial) {
      var oldMaterial = this.oldMaterial
      this.dataSource.filter((material) => {
        if (material['标识编码'] == oldMaterial['标识编码']) {
          oldMaterial['标识编码'] = newMaterial.material_code
          oldMaterial['名称'] = newMaterial.material_name
          oldMaterial['规格'] = newMaterial.material_specification
        }
      })
      this.cacheData = this.dataSource.map((item) => ({ ...item }))
    },
    openModal(record) {
      this.record = record
      this.visible = true
      this.identity = record.identity
      this.setData(record.bomDetail)
    },
    setData(bomDetail) {
      this.dataSource = JSON.parse(bomDetail)
      this.cacheData = this.dataSource.map((item) => ({ ...item }))
      console.log(this.dataSource)
    },
    handleCancel() {
      this.dataSource = []
      this.identity = ''
      this.visible = false
      this.cacheData = []
      this.editingKey = ''
      this.$emit('closeFile')
    },
    //保存材料
    saveMaterial() {
      let requestJson = {
        bom_detail: this.dataSource,
        fileName: this.record.docPath[0].fileName,
        status: '1',
        id: this.record.id,
      }
      p_postAction2(`/bom/save2Excel`, JSON.stringify(requestJson))
        .then((res) => {
          console.log(res)
          if (res.success) {
            this.$message.success('保存成功')
            this.$emit('ok')
          }
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
    chooseMaterial(record) {
      console.log(record)
      this.oldMaterial = record
      this.$refs.chooseMaterial.openModal()
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