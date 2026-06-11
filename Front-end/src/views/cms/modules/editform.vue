<template>
  <a-drawer title="编辑" :visible="visible" width="800" @close="handleCancel">
    <div class="editForm">
      <div class="col-md"></div>
      <div class="col-lg">
        <el-tabs>
          <el-tab-pane label="基本设置">
            <div class="col-lg1">
              <div class="panel-body">
                <el-form ref="form" :model="form">
                  <div class="form-group form-control-medium">
                    <label for="formName">表单名：</label>
                    <el-input
                      v-model="form.formName"
                      id="formName"
                      name="formName"
                      type="text"
                      class="form-control"
                    ></el-input>
                  </div>
                  <div class="form-group form-control-medium">
                    <label for="tableName">附加表名：</label>
                    <el-input
                      v-model="form.tableName"
                      id="tableName"
                      name="tableName"
                      type="text"
                      class="form-control"
                    ></el-input>
                  </div>
                  <div class="form-group">
                    <label for="type">表单性质：</label>
                    <el-radio-group v-model="form.type">
                      <el-radio :label="1">自定义表单</el-radio>
                      <el-radio :label="0">系统表单</el-radio>
                    </el-radio-group>
                  </div>
                  <div
                    class="drawer-bootom-button"
                    style="text-align: right;"
                  >
                    <a-popconfirm
                      title="确定放弃编辑？"
                      @confirm="handleCancel"
                      okText="确定"
                      cancelText="取消"
                    >
                      <a-button style="margin-right: .8rem">取消</a-button>
                    </a-popconfirm>
                    <a-button @click="baocun" type="primary">提交</a-button>
                  </div>
                </el-form>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="字段管理">
            <div class="addfield">
              <a-button class="ant-btn" type="primary" icon="plus" @click="handleAdd()">新增</a-button>
            </div>
            <div class="panel-table">
              <div class="article-table">
                <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                  <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
                  <a>
                    <b>{{ selectedRowKeys.length }}</b>
                  </a>项
                  <a style="margin-left: 24px" @click="onClearSelected">清空</a>
                </div>
                <a-table
                  style="height:500px"
                  ref="table"
                  bordered
                  size="middle"
                  :columns="columns"
                  :dataSource="dataSource"
                  :pagination="ipagination"
                  :loading="loading"
                  :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                >
                  <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                      <a>删除</a>
                    </a-popconfirm>
                  </span>
                </a-table>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <addfield ref="modalform"></addfield>
    <editfield ref="modalform1"></editfield>
  </a-drawer>
</template>

<script>
import addfield from './addfield'
import editfield from './editfield'
import { c_getAction, c_postAction, c_httpAction, c_putAction, c_deleteAction } from '@/api/cmsApi'
export default {
  components: {
    addfield,
    editfield,
  },
  data() {
    return {
      visible: false,
      dataSource: [],
      formid: '',
      columns: [
        {
          title: '表单提示字',
          align: 'center',
          dataIndex: 'fieldText',
        },
        {
          title: '数据字段名',
          align: 'center',
          dataIndex: 'fieldName',
        },
        {
          title: '数据类型',
          dataIndex: 'dataType',
          align: 'center',
          customRender: (dataType) => {
            if (dataType === 1) {
              return 'varchar'
            } else if (dataType === 2) {
              return 'char'
            } else if (dataType === 3) {
              return 'text'
            } else if (dataType === 4) {
              return 'HTML文本'
            } else if (dataType === 5) {
              return 'MARKDOWN文本'
            } else if (dataType === 6) {
              return '附件类型'
            } else if (dataType === 7) {
              return '时间类型'
            } else if (dataType === 8) {
              return 'RADIO'
            } else {
              return 'CHECKBOX'
            }
          },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      ipagination: {
        current: 0,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
        // showSizeChanger: true,
        total: 0,
      },
      selectedRowKeys: [],
      rightcolval: 0,
      loading: false,
      total: 10,
      queryInfo: {
        cid: '',
        page: 1,
        pageSize: 10,
      },
      queryParam: {},
    }
  },
  methods: {
    handleOpen(record) {
      this.visible = true
      console.log(record)
      this.form = record
      var _this = this
      c_getAction('/admin/forms/toEdit', { id: record.id }).then(
        function (res) {
          console.log(res)
          _this.dataSource = res.data.fields
          _this.formid = res.data.form.id
          console.log(res)
        },
        function (err) {
          console.log(err)
        }
      )
    },
    async handleDelete(record) {
      var _this = this
      // const res = await this.$confirm('此操作将删除该表单, 是否继续?', '提示', {
      //   confirmButtonText: '确定',
      //   cancelButtonText: '取消',
      //   type: 'warning'
      // }).catch(err => err)
      // if (res !== 'confirm') {
      //   return this.$message.info('取消删除')
      // }
      // var queryInfo = { id: record.id }
      var _this = this
      c_deleteAction('/admin/field/delete', { params: { id: record.id } }).then(
        function (res) {
          console.log(res)
        },
        function (err) {
          console.log(err)
        }
      )
    },
    handleAdd() {
      this.$refs.modalform.handleOpen(this.formid)
    },
    handleEdit(record) {
      this.$refs.modalform1.handleOpen(record)
    },
    handleCancel() {
      this.visible = false
    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      // this.Getform()
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      this.rightcolval = 1
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectionRows
      this.model = Object.assign({}, selectionRows[0])
      console.log(this.model)
      this.currentRoleId = selectedRowKeys[0]
      // this.loadData2()
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    baocun() {
      var itme = {
        formName: this.form.formName,
        tableName: this.form.tableName,
        type: this.form.type + '',
        id: this.form.id,
      }
      c_postAction('/admin/forms/edit', itme).then(
        function (res) {
          console.log(res)
        },
        function (err) {
          console.log(err)
        }
      )
      this.visible = false
      this.$emit('ok')
    },
  },
  created() {
    var MJsonStr = sessionStorage.getItem('message_form')
    console.log(MJsonStr)
    MJsonStr = JSON.parse(MJsonStr)
    this.form = MJsonStr
    console.log(this.form)
  },
}
</script>

<style scoped>
.editForm {
  /* float: right;
  width: 84%;
  height: 100%;
  border: 1px solid #000000; */
  overflow: auto;
  height: 720px;
  background-color: white;
  text-align: initial;
  padding-bottom: 20px;
}

.col-md,
.col-lg,
.col-lg1 {
  position: relative;
  min-height: 1px;
}
.col-md,
.col-lg {
  padding-right: 15px;
  padding-left: 15px;
}

.title {
  border-bottom: 1px solid #eee;
  background-color: white;
  padding: 10px 15px;
  border-top-right-radius: 3px;
  border-top-left-radius: 3px;
}
h1 {
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 10px;
}
.breadcrumb {
  padding: 8px 15px;
  list-style: none;
  background-color: white;
  border-radius: 4px;
  margin-top: 0px;
  margin-bottom: 20px;
}
.form-control-medium {
  display: contents;
}
.drawer-bootom-button {
    /* position: absolute; */
    bottom: -8px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
li {
  display: inline;
}
a {
  text-decoration: none;
}
.index {
  color: #000;
}

.form-group {
  margin-bottom: 15px;
}
.form-control-medium {
  width: 500px !important;
}
.form-control {
  box-shadow: none;
  width: 100%;
  font-size: 14px;
  line-height: 1.428571429;
  color: #555;
  vertical-align: middle;
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
}
.checkbox-inline {
  font-weight: normal;
}
.btn.btn-info {
  background-color: #23b7e5;
  border: none;
  -webkit-transition: all 0.3s ease;
  -moz-transition: all 0.3s ease;
  -ms-transition: all 0.3s ease;
  -o-transition: all 0.3s ease;
  transition: all 0.3s ease;
}
.btn {
  font-weight: 500;
  -webkit-border-radius: 3px;
  -moz-border-radius: 3px;
  border-radius: 3px;
  border: 1px solid transparent;
  display: inline-block;
  padding: 6px 12px;
  margin-bottom: 0;
  font-size: 14px;
  font-weight: normal;
  line-height: 1.428571429;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  cursor: pointer;
  background-image: none;
  border: 1px solid transparent;
  border-radius: 4px;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  -o-user-select: none;
  user-select: none;
}
.btn-info {
  color: #fff;
  background-color: #5bc0de;
  border-color: #46b8da;
}
.addzd {
  padding: 15px;
  background-color: #ffffff;
}
.adbtn {
  background-color: rgb(70, 151, 206);
  border: none;
  font-weight: 500;
  border-radius: 3px;
  padding: 5px 10px;
  font-size: 12px;
  line-height: 1.5;
  color: #fff;
  margin-bottom: 0;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  cursor: pointer;
  background-image: none;
  user-select: none;
}
label {
  display: inline-block;
  margin-bottom: 5px;
  font-weight: bold;
}
.addfield {
  padding: 15px;
  background-color: white;
  text-align: initial;
}
.addfield > a {
  background-color: #4697ce;
  border: #4697ce solid 1px;
  color: white;
  font-size: 12px;
  border-radius: 4px;
  padding: 5px;
  text-decoration: none;
}
.cell > a {
  background-color: #27c24c;
  font-weight: 500;
  border-radius: 3px;
  padding: 1px 5px;
  font-size: 12px;
  color: white;
  width: 24px;
  height: 12px;
  padding-left: 15px;
  padding-right: 15px;
  padding-top: 7px;
  padding-bottom: 7px;
}
.cell > a:hover {
  background-color: rgb(150, 224, 120);
}
</style>
