<template>
  <a-row :gutter="10">
    <a-col :md="6" :sm="24" >
      <a-card :bordered="false">
        <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
          <!-- <a-input-search @search="onSearch" style="width:100%;margin-top: 10px" placeholder="请输入部门名称"/> -->
          <!-- 树-->
          <template v-if="userIdentity === '2' && departTree.length>0">
            <!--组织机构-->
            <a-tree
              showLine
              :selectedKeys="selectedKeys"
              :checkStrictly="true"
              @select="onSelect"
              :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
              :treeData="departTree"
              :autoExpandParent="autoExpandParent"
              :expandedKeys="iExpandedKeys"
              @expand="onExpand"
            />

          </template>
          <div style="margin-top: 24px;" v-else-if="userIdentity === '2' && departTree.length==0">
            <h3><span>您的部门下暂无有效部门信息</span></h3>
          </div>
          <div style="margin-top: 24px;" v-else><h3>普通员工暂无权限</h3></div>
        </div>
      </a-card>
    </a-col>
    <a-col :md="18" :sm="24">
       <Dept-Class-Info :canAdd="canAdd" :managementType="managementType" :departIdList="departIdList" ref="DeptClassInfo" @clearSelectedDepartKeys="clearSelectedDepartKeys"></Dept-Class-Info>
    </a-col>
  </a-row>
</template>
<script>
  import DeptClassInfo from './modules/DeptClassInfo'
  import {queryMyDepartTreeList, searchByKeywords} from '@/api/api'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'

  export default {
    name: 'professionList',
    mixins: [JeecgListMixin],
    components: {
      DeptClassInfo,
    },
    data() {
      return {
        canAdd: false,
        managementType: "profession",
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
        departIdList:[],
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
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        graphDatasource: {
          nodes: [],
          edges: []
        },
        userIdentity:"",
      }
    },
    methods: {
      callback(key) {
        //console.log(key)
      },
      loadData() {
        this.refresh();
      },
      clearSelectedDepartKeys() {
        this.checkedKeys = [];
        this.selectedKeys = [];
        this.currentDeptId = '';
        this.$refs.DeptClassInfo.currentDeptId='';
      },
      loadTree() {
        var that = this
        that.treeData = []
        that.departTree = []
        queryMyDepartTreeList().then((res) => {
          try {
            const { success, result } = res;
            if (success && result ) {
              const { length } = result;
              for (let i = 0; i < length; i++) {
                let temp = result[i];
                temp.children = null;
                temp.isLeaf = true;
                that.treeData.push(temp);
                that.departTree.push(temp);
                that.departIdList.push(temp.id);
                that.setThisExpandedKeys(temp);
              }
              this.loading = false
            }
            console.log('拿到树状图',res)
            that.userIdentity = res.message
          } catch(err) {
            console.log('树状图代码运行错误', err);
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
        // console.log('onExpand', expandedKeys)
        // if not set autoExpandParent to false, if children expanded, parent can not collapse.
        // or, you can remove all expanded children keys.
        this.iExpandedKeys = expandedKeys
        this.autoExpandParent = false
      },

      onSearch(value) {
        let that = this
        if (value) {
          searchByKeywords({keyWord: value,myDeptSearch:'1'}).then((res) => {
            if (res.success) {
              that.departTree = []
              that.departIdList = []
              for (let i = 0; i < res.result.length; i++) {
                let temp = res.result[i]
                that.departTree.push(temp)
                that.departIdList.push(temp.id)
              }
            } else {
              that.$message.warning(res.message)
            }
          })
        } else {
          that.loadTree()
        }

      },
      onCheck(checkedKeys, e) {
        let record = e.node.dataRef;
        this.checkedKeys = [];
        this.currentDeptId = record.id;
        this.checkedKeys.push(record.id);
        this.$refs.DeptClassInfo.open(record);
        this.hiding = false;
      },
      onSelect(selectedKeys, e) {
        this.canAdd = true;
        if (this.selectedKeys[0] !== selectedKeys[0]) {
          this.selectedKeys = [selectedKeys[0]];
        }
        let record = e.node.dataRef;
        this.checkedKeys.push(record.id);
        // this.$refs.DeptBaseInfo.open(record);
        this.$refs.DeptClassInfo.onClearSelected();
        this.$refs.DeptClassInfo.open(record,false);
        // this.$refs.DeptRoleInfo.onClearSelected();
        // this.$refs.DeptRoleInfo.open(record);
      },
    },
    created() {
      this.currFlowId = this.$route.params.id
      this.currFlowName = this.$route.params.name
      // this.loadTree()
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