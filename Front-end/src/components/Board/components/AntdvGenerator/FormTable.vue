<template>
  <div class="antd-form-table" :class="{'is-disabled': disabled}">
    <a-table :columns="tableColumns" :dataSource="tableData" bordered>
      <span slot="#" slot-scope="text, record, index">

        <div class="scope-index">
          <span>{{index + 1}}</span>
        </div>
        <div class="scope-action">
          <a-button :disabled="disabled" type="danger" shape="circle" icon="minus"  @click="handleRemove(index)" size="small"></a-button>
        </div>
      </span>

      <template
        v-for="col in columns"
        :slot="col.key"
        slot-scope="text, record, index"
      >
        <span :key="col.key">
          <form-table-item
            :key="col.key"
            :models.sync="tableData[index]"
            :rules="rules"
            :widget="col"
            :remote="remote"
            :blanks="blanks"
            :edit="!disabled" 
            :remote-option="remoteOption"
          >
            <template v-slot:[blank.name]="blankScope" v-for="blank in blanks">
              <slot :name="blank.name" :model="tableData[index]"></slot>
            </template>
          </form-table-item>
        </span>
      </template>
    </a-table>

    <!-- <a-button type="link" icon="plus"  @click="handleAddRow" v-if="!disabled">{{$t('fm.actions.add')}}</a-button> -->
    <a-button type="link" icon="plus"  @click="handleAddRow" v-if="!disabled">添加</a-button>
  </div>
</template>

<script>
import FormTableItem from './FormTableItem'

export default {
  components: {
    FormTableItem
  },
  props: ['columns', 'value', 'models', 'remote', 'blanks', 'disabled', 'rules', 'name', 'remoteOption'],
  data () {
    return {
      tableData: this.value,
      tableColumns: [{
        dataIndex: '#',
        title: '#',
        width: '80px',
        fixed: 'left',
        scopedSlots: { customRender: '#' },
      }]
    }
  },
  created () {
    this.tableColumns = this.tableColumns.concat(this.columns.map(item => ({
      dataIndex: item.model,
      key: item.key,
      title: item.name,
      scopedSlots: { customRender: item.key },
    })))
  },
  methods: {
    handleAddRow () {
      let item = {}
      
      for (let i = 0; i < this.columns.length; i++) {
        if (this.columns[i].type === 'blank') {
          item[this.columns[i].model] = this.columns[i].options.defaultType == 'String' ? '' : (this.columns[i].options.defaultType == 'Object' ? {} : [])
        } else {
          item[this.columns[i].model] = this.columns[i].options.defaultValue
        }
      }

      this.tableData.push({key: new Date().getTime(), ...item})
    },

    handleRemove (index) {
      this.tableData.splice(index, 1)
    }
  },
  watch: {
    value (val) {
      this.tableData = val
    }
  }
}
</script>

<style lang="scss">
.antd-form-table{
  .ant-form-item{
    margin-bottom: 0;
  }

  .scope-action{
    display: none;
  }

  .scope-index{
    display: block;
  }

  .ant-table-row-hover{
    .scope-action{
      display: block;
      .el-button{
        padding: 3px;
      }
    }

    .scope-index{
      display: none;
    }
  }

  .ant-table-pagination{
    display: none;
  }

  .ant-empty-normal{
    margin: 0;
  }
}
</style>

