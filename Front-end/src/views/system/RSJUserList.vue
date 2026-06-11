<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="角色">
              <a-select v-model="queryParam.roleIds" mode="multiple" placeholder="请选择角色">
                <a-select-option value="1872566080680017921">承办人员</a-select-option>
                <a-select-option value="1872566115782148097">业务分管领导</a-select-option>
                <a-select-option value="1872566152285175809">人社分管领导</a-select-option>
                <a-select-option value="1876096735837732866">人社局管理员</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="部门">
              <a-select v-model="queryParam.departName" placeholder="请选择部门">
                <a-select-option value="黄州区人社局">黄州区</a-select-option>
                <a-select-option value="团风县人社局">团风县</a-select-option>
                <a-select-option value="红安县人社局">红安县</a-select-option>
                <a-select-option value="罗田县人社局">罗田县</a-select-option>
                <a-select-option value="英山县人社局">英山县</a-select-option>
                <a-select-option value="浠水县人社局">浠水县</a-select-option>
                <a-select-option value="蕲春县人社局">蕲春县</a-select-option>
                <a-select-option value="黄梅县人社局">黄梅县</a-select-option>
                <a-select-option value="龙感湖管理区人社局">龙感湖管理区</a-select-option>
                <a-select-option value="麻城市人社局">麻城市</a-select-option>
                <a-select-option value="武穴市人社局">武穴市</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <!-- <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="真实名字">
                <a-input placeholder="请输入真实名字" v-model="queryParam.realname"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="手机号码">
                <a-input placeholder="请输入手机号码查询" v-model="queryParam.phone"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="用户状态">
                <a-select v-model="queryParam.status" placeholder="请选择">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option value="1">正常</a-select-option>
                  <a-select-option value="2">冻结</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template> -->

          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="border-top: 5px">
      <a-button @click="handleAdd" type="primary" icon="plus">添加人社局员工</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('用户信息')">导出</a-button> -->
      <!-- <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- <a-button type="primary" icon="hdd" @click="recycleBinVisible = true">回收站</a-button> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">
            <a-icon type="delete" @click="batchDel" />
            删除
          </a-menu-item>
          <a-menu-item key="2">
            <a-icon type="lock" @click="batchFrozen('2')" />
            冻结
          </a-menu-item>
          <a-menu-item key="3">
            <a-icon type="unlock" @click="batchFrozen('1')" />
            解冻
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
      <!-- <j-super-query :fieldList="superQueryFieldList" @handleSuperQuery="handleSuperQuery" /> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{
          selectedRowKeys.length
        }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table ref="table" bordered size="middle" rowKey="id" :columns="columns" :dataSource="dataSource"
        :pagination="ipagination" :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" @change="handleTableChange">
        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user" />
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a href="javascript:;" @click="handleDetail(record)">查看</a>
          <a-divider type="vertical" />

          <a-dropdown>
            <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
            <a-menu slot="overlay">

              <a-menu-item>
                <a href="javascript:;" @click="handleChangePassword(record.username)">密码</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>

              <!-- <a-menu-item v-if="record.status == 1">
                <a-popconfirm title="确定冻结吗?" @confirm="() => handleFrozen(record.id, 2, record.username)">
                  <a>冻结</a>
                </a-popconfirm>
              </a-menu-item> -->

              <a-menu-item v-if="record.status == 2">
                <a-popconfirm title="确定解冻吗?" @confirm="() => handleFrozen(record.id, 1, record.username)">
                  <a>解冻</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <RSJModal ref="modalForm" @ok="modalFormOk"></RSJModal>
    <password-modal ref="passwordmodal"></password-modal>

    <!-- 用户回收站 -->
    <user-recycle-bin-modal :visible.sync="recycleBinVisible" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import RSJModal from './modules/RSJModal' 
import PasswordModal from './modules/PasswordModal'
import { putAction, getFileAccessHttpUrl } from '@/api/manage'
import { frozenBatch } from '@/api/api'
import { JeecgListMixinRSJ } from '@/mixins/JeecgListMixinRSJ'
import SysUserAgentModal from './modules/SysUserAgentModal'
import JInput from '@/components/jeecg/JInput'
import UserRecycleBinModal from './modules/UserRecycleBinModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery'
import { getAction } from '@/api/manage'

export default {
  name: 'UserList',
  mixins: [JeecgListMixinRSJ],
  components: {
    SysUserAgentModal,
    RSJModal,
    PasswordModal,
    JInput,
    UserRecycleBinModal,
    JSuperQuery,
  },
  data() {
    return {
      description: '这是用户管理页面',
      queryParam: {
        roleIds: [],
        departName: undefined
      },
      recycleBinVisible: false,
      columns: [
        /*{
              title: '#',
              dataIndex: '',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },*/
        {
          title: '员工账号',
          align: 'center',
          dataIndex: 'username',
          width: 120,
          sorter: true,
        },
        {
          title: '员工姓名',
          align: 'center',
          width: 100,
          dataIndex: 'realname',
        },
        // {
        //   title: '头像',
        //   align: "center",
        //   width: 120,
        //   dataIndex: 'avatar',
        //   scopedSlots: {customRender: "avatarslot"}
        // },

        // {
        //   title: '性别',
        //   align: "center",
        //   width: 80,
        //   dataIndex: 'sex_dictText',
        //   sorter: true
        // },
        // {
        //   title: '生日',
        //   align: "center",
        //   width: 100,
        //   dataIndex: 'birthday'
        // },
        {
          title: '手机号码',
          align: 'center',
          width: 100,
          dataIndex: 'phone',
        },
        {
          title: '部门',
          align: 'center',
          width: 180,
          dataIndex: 'departName',
        },
        // {
        //   title: '负责部门',
        //   align: "center",
        //   width: 180,
        //   dataIndex: 'departIds_dictText'
        // },
        {
          title: '状态',
          align: 'center',
          width: 80,
          dataIndex: 'status_dictText',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 170,
        },
      ],
      superQueryFieldList: [
        { type: 'input', value: 'username', text: '用户账号' },
        { type: 'input', value: 'realname', text: '用户姓名' },
        { type: 'select', value: 'sex', text: '性别', dictCode: 'sex' },
      ],
      url: {
        syncUser: '/act/process/extActProcess/doSyncUser',
        // list: '/sys/user/list',
        delete: '/sys/user/delete',
        deleteBatch: '/sys/user/deleteBatch',
        exportXlsUrl: '/sys/user/exportXls',
        importExcelUrl: 'sys/user/importExcel',
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    getAvatarView: function (avatar) {
      return getFileAccessHttpUrl(avatar)
    },

    batchFrozen: function (status) {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！')
        return false
      } else {
        let ids = ''
        let that = this
        let isAdmin = false
        that.selectionRows.forEach(function (row) {
          if (row.username == 'admin') {
            isAdmin = true
          }
        })
        if (isAdmin) {
          that.$message.warning('管理员账号不允许此操作,请重新选择！')
          return
        }
        that.selectedRowKeys.forEach(function (val) {
          ids += val + ','
        })
        that.$confirm({
          title: '确认操作',
          content: '是否' + (status == 1 ? '解冻' : '冻结') + '选中账号?',
          onOk: function () {
            frozenBatch({ ids: ids, status: status }).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.loadData()
                that.onClearSelected()
              } else {
                that.$message.warning(res.message)
              }
            })
          },
        })
      }
    },
    handleMenuClick(e) {
      if (e.key == 1) {
        this.batchDel()
      } else if (e.key == 2) {
        this.batchFrozen(2)
      } else if (e.key == 3) {
        this.batchFrozen(1)
      }
    },
    handleFrozen: function (id, status, username) {
      let that = this
      //TODO 后台校验管理员角色
      if ('admin' == username) {
        that.$message.warning('管理员账号不允许此操作！')
        return
      }
      frozenBatch({ ids: id, status: status }).then((res) => {
        if (res.success) {
          that.$message.success(res.message)
          that.loadData()
        } else {
          that.$message.warning(res.message)
        }
      })
    },
    handleChangePassword(username) {
      this.$refs.passwordmodal.show(username)
    },
    // fetchAllRoles() {
    //   const queryall = (params) => getAction("/sys/role/queryall", params);
    //   queryall({})
    //     .then(response => {
    //       const roles = response.result; // 从response的result属性获取角色数据数组
    //       if (roles && Array.isArray(roles)) {
    //         this.roleOptions = roles.map(role => ({
    //           id: role.id,
    //           name: role.roleName // 使用roleName字段作为下拉框显示的角色名称
    //         }));
    //       } else {
    //         console.warn('接口返回角色数据为空或者格式不正确');
    //         this.roleOptions = [];
    //       }
    //     })
    //     .catch(error => {
    //       console.error('获取角色列表失败', error);
    //     });
    // }
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>