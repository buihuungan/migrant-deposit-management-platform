<template>
  <a-row :gutter="10">
    <a-col :md="6" :sm="24" style="background: #fff">
      <a-card :bordered="false">
        <div class="table-operator" :md="24" :sm="24" style="margin-bottom: 15px">
          <a-divider type="vertical" />
          <label style="margin-top: 16px;font-weight:600;">机构列表:</label>
        </div>
        <div style="background: #fff; padding-left: 16px; height: 100%; margin-top: 5px">
          <!-- 树-->
          <template v-if="userIdentity === '2' && departTree.length > 0">
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
          <div style="margin-top: 24px" v-else-if="userIdentity === '2' && departTree.length == 0">
            <h3><span>您暂无有效机构信息</span></h3>
          </div>
          <div style="margin-top: 24px" v-else><h3>普通员工暂无权限</h3></div>
        </div>
      </a-card>
    </a-col>
    <a-col :md="18" :sm="24">
      <Sys-Lesson-Info
        :managementType="managementType"
        :departIdList="departIdList"
        ref="SysLessonInfo"
        @clearSelectedDepartKeys="clearSelectedDepartKeys"
      ></Sys-Lesson-Info>
    </a-col>
  </a-row>
</template>
<script>
import SysLessonInfo from './modules/SysLessonInfo'
import { queryMyDepartTreeList, searchByKeywords } from '@/api/api'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
export default {
  name: 'lessonList',
  mixins: [JeecgListMixin],
  components: {
    SysLessonInfo,
  },
  data() {
    return {
      canAdd: false,
      managementType: 'profession',
      currentDeptId: '',
      iExpandedKeys: [],
      loading: false,
      autoExpandParent: true,
      currFlowId: '',
      currFlowName: '',
      disable: true,
      treeData: [],
      visible: false,
      departTree: [],
      departIdList: [],
      rightClickSelectedKey: '',
      hiding: true,
      model: {},
      dropTrigger: '',
      depart: {},
      disableSubmit: false,
      checkedKeys: [],
      selectedKeys: [],
      autoIncr: 1,
      currSelected: {},
      form: this.$form.createForm(this),
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      graphDatasource: {
        nodes: [],
        edges: [],
      },
      userIdentity: '',
    }
  },
  methods: {
    callback(key) {
      //console.log(key)
    },
    loadData() {
      this.refresh()
    },
    clearSelectedDepartKeys() {
      this.checkedKeys = []
      this.selectedKeys = []
      this.currentDeptId = ''
      this.$refs.SysLessonInfo.currentDeptId = ''
    },
    loadTree() {
      var that = this
      that.treeData = []
      that.departTree = []
      queryMyDepartTreeList().then((res) => {
        try {
          const { success, result } = res
          if (success && result) {
            const { length } = result
            for (let i = 0; i < length; i++) {
              let temp = result[i]
              temp.children = null
              temp.isLeaf = true
              that.treeData.push(temp)
              that.departTree.push(temp)
              that.departIdList.push(temp.id)
              that.setThisExpandedKeys(temp)
            }
            this.loading = false
          }

          that.userIdentity = res.message
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
      this.loadTree()
    },

    onExpand(expandedKeys) {
      this.iExpandedKeys = expandedKeys
      this.autoExpandParent = false
    },

    // onSearch(value) {
    //   let that = this
    //   if (value) {
    //     searchByKeywords({keyWord: value,myDeptSearch:'1'}).then((res) => {
    //       if (res.success) {
    //         that.departTree = []
    //         that.departIdList = []
    //         for (let i = 0; i < res.result.length; i++) {
    //           let temp = res.result[i]
    //           that.departTree.push(temp)
    //           that.departIdList.push(temp.id)
    //         }
    //       } else {
    //         that.$message.warning(res.message)
    //       }
    //     })
    //   } else {
    //     that.loadTree()
    //   }

    // },
    onCheck(checkedKeys, e) {
      let record = e.node.dataRef
      this.checkedKeys = []

      this.currentDeptId = record.id
      this.checkedKeys.push(record.id)

      this.$refs.SysLessonInfo.open(record)

      this.hiding = false
    },
    onSelect(selectedKeys, e) {
      console.log("e",e);
      if (this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]]
      }
      let record = e.node.dataRef

      this.checkedKeys.push(record.id)

      this.$refs.SysLessonInfo.onClearSelected()
      this.$refs.SysLessonInfo.open(record)
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
