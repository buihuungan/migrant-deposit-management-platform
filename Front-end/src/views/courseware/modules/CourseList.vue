<template>
  <a-row :gutter="10">
    <a-col :md="4" :sm="24" style="background: #fff; border-radius: 4px">
      <a-card :bordered="false">
        <div class="table-operator" :md="24" :sm="24" style="margin-bottom: 15px; font-weight: 600">
          <a-divider type="vertical" style="width: 2px" />
          <label style="margin-top: 16px">案件类型/领域:</label>
        </div>
        <div style="background: #fff; padding-left: 16px; height: 100%; margin-top: 5px">
          <!-- 树-->
          <template v-if="departTree.length > 0">
            <!--组织机构-->
            <a-tree
              showLine
              :selectedKeys="selectedKeys"
              :checkStrictly="true"
              @select="onSelect"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="departTree"
              :autoExpandParent="autoExpandParent"
              :expandedKeys="iExpandedKeys"
              @expand="onExpand"
            />
          </template>
          <div style="margin-top: 24px" v-else-if="departTree.length == 0">
            <h3><span>您的机构下暂无仿真课件信息</span></h3>
          </div>
          <!-- <div style="margin-top: 24px;" v-else><h3>普通员工暂此权限</h3></div> -->
        </div>
      </a-card>
    </a-col>
    <a-col :md="20" :sm="24">
      <Course-List-Table
        :departIdList="departIdList"
        :tableType="tableType"
        ref="courseInfo"
        :type="type"
        @changedetail="changeDetail"
        @clearSelectedDepartKeys="clearSelectedDepartKeys"
      ></Course-List-Table>
    </a-col>
  </a-row>
  <!-- <a-row >
    <course-list-detail :detailrecords="detailrecords" :editor="editor"></course-list-detail>
  </a-row> -->
</template>
<script>
import Vue from 'vue'
import { TENANT_ID } from '@/store/mutation-types'

import { getAction, putAction, deleteAction } from '@/api/manage'
import CourseListTable from './CourseListTable'
import courseListDetail from './courseListDetail'

import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'CourseList',
  mixins: [JeecgListMixin],
  components: {
    CourseListTable,
  },
  props: {
    type: {
      type: String,
      default: 'type',
    },
    editor: {
      type: Object,
      default: function () {
        return {}
      },
    },
  },
  data() {
    return {
      currentDeptId: '',
      iExpandedKeys: [],
      loading: false,
      autoExpandParent: true,

      disable: true,
      visible: false,
      departTree: [],
      departIdList: [],
      rightClickSelectedKey: '',
      hiding: true,
      model: {},
      tableType: '',
      disableSubmit: false,
      checkedKeys: [],
      selectedKeys: [],

      currSelected: {},
      form: this.$form.createForm(this),

      typeUrl: {
        searchtype: '/online/cgform/api/getData/474400feca3a4ac4a40549b57788b6ad',
      },
      domainUrl: {
        searchtype: '/online/cgform/api/getData/1464fd461cf84acfa700ca4b40294b1d',
      },
    }
  },
  methods: {
    changeDetail(val) {
      this.$emit('getdetailrecords', val)
    },
    loadData() {
      this.refresh()
    },
    clearSelectedDepartKeys() {
      this.checkedKeys = []
      this.selectedKeys = []
      this.currentDeptId = ''
      this.$refs.DeptClassInfo.currentDeptId = ''
    },
    loadTree() {
      var that = this
      that.departTree = []
      const url = this[`${that.type}Url`].searchtype
      //通过租户编码查询对应的类型、领域
      if (Vue.ls.get(TENANT_ID)) {
        var params = {
          superQueryParams: escape(
            JSON.stringify([
              {
                rule: 'eq',
                type: 'list',
                val: Vue.ls.get(TENANT_ID),
                field: 'sys_tenant_id',
              },
            ])
          ),
        }
      }

      getAction(url, params).then((res) => {
        try {
          if (res.success && res.result) {
            const { records } = res.result
            for (let i = 0; i < records.length; i++) {
              let leaf = Object.assign(records[i], {
                children: null,
                isLeaf: true,
                key: records[i].id,
                title: records[i][`ware_${that.type}_name`],
                value: records[i].id,
              })
              that.departTree.push(leaf)
              that.departIdList.push(leaf.id)
              that.setThisExpandedKeys(leaf)
            }
            this.loading = false
          }
        } catch (err) {
          console.log('树状图代码运行错误', err)
        }
      })
    },
    setThisExpandedKeys(node) {
      //只展开一级目录
      if (node.children && node.children.length > 0) {
        this.iExpandedKeys.push(node.key)
        //下方代码放开注释则默认展开所有节点
        /**
          for (let a = 0; a < node.children.length; a++) {
            this.setThisExpandedKeys(node.children[a])
          }
          */
      }
    },
    refresh() {
      this.loading = true
      this.loadTree();
      this.tableType = this.type;
    },

    onExpand(expandedKeys) {
      // or, you can remove all expanded children keys.
      this.iExpandedKeys = expandedKeys
      this.autoExpandParent = false
    },
    // onCheck(checkedKeys, e) {
    //   let record = e.node.dataRef
    //   this.checkedKeys = []
    //   this.currentDeptId = record.id
    //   this.checkedKeys.push(record.id)
    //   this.$refs.CourseInfo.open(record)
    //   this.hiding = false
    // },
    onSelect(selectedKeys, e) {
      if (this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]]
      }
      let record = e.node.dataRef
      this.checkedKeys.push(record.id)
      this.$refs.courseInfo.open(record)
    },
  },
  created() {
    this.currFlowId = this.$route.params.id
    this.currFlowName = this.$route.params.name
  },
}
</script>
<style lang="less" scoped>
@import '~@assets/less/common.less';
.ant-card {
  margin-left: 0;
  margin-right: 0;
}
/deep/ .ant-tree li {
  display: block;
  margin: 0;
  padding: 4px 0;
  white-space: nowrap;
  list-style: none;
  outline: 0;
}
</style>