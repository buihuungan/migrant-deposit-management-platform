<template>
  <div>
    <a-modal
      :centered="true"
      title="选择物料"
      :visible="visible"
      :destroyOnClose="true"
      @cancel="handleCancel"
      width="65%"
      :zIndex="200"
      :footer="null"
    >
      <div style="margin-top: 20px">
        <a-button-group>
          <a-button type="primary" icon="pic-center" :disabled="!hasSelected" :loading="loading" @click="chooseOk">
            选择
          </a-button>
        </a-button-group>
      </div>
      <div style="padding-top: 20px">
        <a-table :columns="columns" :data-source="materials" :pagination="ipagination" :row-selection="rowSelection1" :rowKey="record => record.id">
        </a-table>
      </div>
    </a-modal>
    <div></div>
  </div>
</template>
<script>
import axios from 'axios'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import api from '@/api/index'
import { o_deleteAction } from '@/api/onApi'
import { getAction } from '@/api/manage'
import { p_postAction2 } from '@/api/plmApi'
import chooseMaterial from './chooseMaterial'
import { o_getAction } from '@/api/onApi'

export default {
  name: 'chooseMaterial',
  components: { chooseMaterial },
  data() {
    return {
      visible: false,
      loading: false,
      selectedRowKeys: [],
      name: '',
      identity: '',
      create_time: '',
      formId: 'a31285722bce40acb9383ce8a49f971c',
      onlineTableId: '8139e49550384b02adb8051b18a2f8c6',
      onlineDataId: '',
      materials: [],
      columns: [
        {
          title: '物料标识',
          align: 'center',
          width: '50%',
          dataIndex: 'material_code',
          ellipsis: true,
        },
        {
          title: '物料名',
          align: 'center',
          width: '50%',
          dataIndex: 'material_name',
        },
      ],
      ipagination: {
        pageSize: 2,
        showTotal: (total, range) => {
          return '当前' + range[0] + '-' + range[1] + '条' + '\xa0\xa0共' + total + '条'
        },
      },
    }
  },
  computed: {
    rowSelection1() {
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
        onSelection: this.onSelection,
      }
    },
    hasSelected() {
      return this.selectedRowKeys.length > 0
    },
  },
  mounted() {},
  methods: {
    //得到所有的物料
    getAllMaterial() {
      let params = {
        pageNo: 1,
        pageSize: -521,
      }
      o_getAction('/cgform/api/getData/793d3d6d579b44aba24594437bffb040', {
        params: params,
      }).then((res) => {
        if (res.success) {
          this.materials = res.result.records
        }
      })
    },
    //派生
    chooseOk() {
      var id = this.selectedRowKeys[0]
      var newMaterial={}
      this.materials.filter((material) => {
        if (material.id == id) {
          newMaterial=material
        }
      })
      console.log(newMaterial)
      this.$emit('chooseOk1', newMaterial)
      this.handleCancel()
    },
    onSelectChange(selectedRowKeys) {
      console.log('selectedRowKeys changed: ', selectedRowKeys)
      console.log(this.ipagination)
      this.selectedRowKeys = selectedRowKeys
    },
    openModal() {
      this.visible = true
      this.getAllMaterial()
    },
    handleCancel() {
      this.materials = []
      this.visible = false
      this.selectedRowKeys = []
    },
  },
}
</script>