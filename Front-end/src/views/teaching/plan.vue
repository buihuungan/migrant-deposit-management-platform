<template>
  <a-row :gutter="10">
    <a-col :md="6" :sm="24">
      <a-card :bordered="false">
        <div style="background: #fff; padding-left: 16px; height: 100%; margin-top: 5px" v-if="userIdentity.length > 0">
          <div v-if="userIdentity === '2' && departTree.length">
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
            <div style="margin-top: 24px" v-else>
              <h3><span>您的部门下暂无教学计划信息</span></h3>
            </div>
          </div>
          <div style="margin-top: 24px" v-if="userIdentity != '2'"><h3>普通员工暂此权限</h3></div>
        </div>
      </a-card>
    </a-col>
    <a-col :md="18" :sm="24">
      <div>
        <Sys-Plan-Info
          :departIdList="departIdList"
          ref="SysPlanInfo"
          @clearSelectedDepartKeys="clearSelectedDepartKeys"
        ></Sys-Plan-Info>
      </div>
    </a-col>
  </a-row>
</template>
<script>
import SysPlanInfo from './modules/SysPlanInfo'
import { queryMyDepartTreeList, searchByKeywords } from '@/api/api'
import { getAction } from '@/api/manage'

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getTree } from '../education/util/mixin'

export default {
  name: 'SysPlanList',
  mixins: [JeecgListMixin, getTree],
  components: {
    SysPlanInfo,
  },
  data() {
    return {
      url: {
        searchprofess: '/online/cgform/api/getData/d9bb8f8f54084b66ba1dc08c2b95d2fe',
        searcgclass: '/online/cgform/api/getData/e1bb9376a3694a128e6abc7cd4496cb0',
      },
      currentDeptId: '',
      iExpandedKeys: [],
      loading: false,
      autoExpandParent: true,
      currFlowId: '',
      currFlowName: '',
      disable: true,
      treeData: [],
      departTree: [],
      departIdList: [],
      hiding: true,
      depart: {},
      checkedKeys: [],
      selectedKeys: [],
      userIdentity: '',
    }
  },
  methods: {
    callback(key) {},
    loadData() {
      this.refresh()
    },
    clearSelectedDepartKeys() {
      this.checkedKeys = []
      this.selectedKeys = []
      this.currentDeptId = ''
      this.$refs.SysPlanInfo.currentDeptId = ''
    },
    loadProfess(node) {
      let that = this
      let params = {}
      params.superQueryMatchType = 'or'
      params.superQueryParams = escape(
        JSON.stringify([
          {
            rule: 'eq',
            type: 'text',
            val: node.id,
            field: 'sys_org_code',
          },
        ])
      )
      getAction(this.url.searchprofess, params).then((res) => {
        if (res.success && res.result) {
          const { records } = res.result
          console.log('拿到专业数据', records)
          if (records.length) {
            node.children = []
            node.isLeaf = false
            for (let i = 0; i < records.length; i++) {
              let leaf = {
                key: records[i].id,
                id: records[i].id,
                title: records[i].subject_name,
                sys_org_code: records[i].sys_org_code,
                tenant_id: records[i].tenant_id,
                children: null,
                parentId: node.id,
                isLeaf: true,
                type: 'profess',
              }

              that.loadClass(leaf)
              node.children.push(leaf)
            }
          }
          that.departIdList.push(node.id)
          that.treeData.push(node)
          that.departTree.push(node)
          that.setThisExpandedKeys(node)
        }
      })
    },
    loadClass(node) {
      let that = this
      let params = {}
      params.superQueryMatchType = 'or'
      params.superQueryParams = escape(
        JSON.stringify([
          {
            rule: 'eq',
            type: 'text',
            val: node.id,
            field: 'subject_code',
          },
        ])
      )
      getAction(this.url.searcgclass, params).then((res) => {
        if (res.success && res.result) {
          let data = res.result.records
          // console.log('拿到班级数据',data,node)
          if (data.length) {
            node.children = []
            node.isLeaf = false
            data.forEach((item) => {
              let leaf = {
                key: item.id,
                id: item.id,
                title: item.class_name,
                sys_org_code: item.sys_org_code,
                subject_code: item.subject_code,
                tenant_id: item.tenant_id,
                children: [],
                parentId: item.subject_code,
                isLeaf: true,
                type: 'class',
              }
              node.children.push(leaf)
            })
          }
        }
      })
    },
    async loadTree() {
      var that = this
      let res = await queryMyDepartTreeList().then()
      const { success, result, message } = res

      if (success && result) {
        const { length } = result
        let axiosArr = []
        for (let i = 0; i < length; i++) {
          axiosArr.push(this.searchProfession(result[i].id))
        }
        this.get3Tree(axiosArr, result, message, 'plan')
      }
      that.userIdentity = message
      console.log('userIdentity', that.userIdentity)
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
      this.loadTree()
    },

    onExpand(expandedKeys) {
      this.iExpandedKeys = expandedKeys
      this.autoExpandParent = false
    },
    onCheck(checkedKeys, e) {
      let record = e.node.dataRef

      this.checkedKeys = []

      this.currentDeptId = record.id
      this.checkedKeys.push(record.id)

      this.$refs.SysPlanInfo.open(record)
      this.hiding = false
    },
    onSelect(selectedKeys, e) {
      if (this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]]
      }
      let record = e.node.dataRef
      this.checkedKeys.push(record.id)

      this.$refs.SysPlanInfo.open(record)
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