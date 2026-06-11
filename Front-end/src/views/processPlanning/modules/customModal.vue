<template>
  <a-modal
    :centered="true"
    title="需求定制"
    :visible="visible"
    @ok="handleOk1"
    @cancel="handleCancel"
    width="75%"
    :zIndex="100"
  >
    <div id="formContent">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="10" :sm="10">
            <a-space :size="43">
              <span></span>
              <a-form-item label="产品名:">
                <a-input placeholder="请输入产品名" v-model="form.tableTxt"></a-input> </a-form-item
            ></a-space>
          </a-col>
          <a-col :md="10" :sm="10">
            <a-form-item label="产品型号:">
              <a-input placeholder="请输入产品型号" v-model="form.model"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :md="10" :sm="10">
            <a-form-item label="生产企业名称:">
              <a-input placeholder="请输入生产企业名称" v-model="form.enterprise"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="10" :sm="10">
            <a-space :size="28">
              <span></span>
              <a-form-item label="产地:">
                <a-input placeholder="请输入产地" v-model="form.origin"></a-input>
              </a-form-item>
            </a-space>
          </a-col>
          <!-- <a-col :md="10" :sm="10">
            <a-form-item label="产品标识编码:">
              <a-input
                :addon-before="addonbefore"
                :disabled="true"
                placeholder="请输入产品标识编码"
                v-model="form.identity"
              ></a-input>
            </a-form-item>
          </a-col> -->
        </a-row>
        <!-- <a-row :gutter="24" style="margin-top:25px;">
          <a-col :md="8" :sm="8">
            <a-form-item label="产品标识编码：">
              <a-input placeholder="请输入关键字搜索" v-model="searchKey"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item>
              <a-button type="primary" @click="searchQuery">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
            </a-form-item>
          </a-col>
        </a-row> -->
        <a-row :gutter="24">
          <a-col :md="24" :sm="24">
            <a-form-item>
              <a-button type="primary" @click="addCustom">新增</a-button>
              <!-- <a-button type="primary" style="margin-left: 8px" v-show="hasSelected" @click="deleteCustom">删除</a-button>
              <a-button style="margin-left: 8px" v-show="hasSelected" @click="restCustom">清空选择</a-button>
              <span style="margin-left: 8px">
                <template v-if="hasSelected">
                  {{ `选择 ${selectedRowKeys.length} 个` }}
                </template>
              </span> -->
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <div class="materielTable">
        <a-table ref="table" size="middle" :columns="columns" :dataSource="dataSource" :pagination="ipagination">
          <span slot="action" slot-scope="text, record">
            <a-popconfirm title="确定删除吗?" cancelText="取消" okText="确定" @confirm="() => handleDelete(record)">
              <a-icon slot="icon" type="question-circle-o" style="color: red" />
              <a>删除</a>
            </a-popconfirm>
          </span>
        </a-table>
      </div>
    </div>
    <addModalNew ref="addForm" :dataSource="dataSource"></addModalNew>
  </a-modal>
</template>
<script>
import addModal from './addModal'
import addModalNew from './addModalNew'
import { im_postAction, im_getAction } from '@/api/icimApi.js'
import { tm_postAction } from '@/api/templateApi'
import { putAction, postAction } from '@/api/manage'
import { p_postAction, p_getAction } from '@/api/plmApi'
import Vue from 'vue'
import axios from 'axios'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'
//import api from '@/api/index'
import api from '../../../api/index.js'
import { Message } from 'element-ui'

export default {
  name: 'customModal',
  components: { addModal, addModalNew },
  data() {
    return {
      orderId: '',
      orderNum: '',
      formJson: {},
      visible: false,
      destroy: true,
      isAdd: false,
      searchKey: '',
      form: {
        tableTxt: '',
        model: '',
        identity: '',
        enterprise: '',
        origin: '',
        category: '',
      },
      addonbefore: '88.175.111/',
      columns: [
        {
          title: '标识',
          align: 'center',
          width: '40%',
          dataIndex: '标识编码',
        },
        {
          title: '物料名',
          align: 'center',
          width: '24%',
          dataIndex: '名称',
        },
        {
          title: '数量',
          align: 'center',
          width: '12%',
          dataIndex: '数量',
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '20%',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      dataSource: [],
      // selectedRowKeys: [],
      dataList: [],
      ipagination: {
        pageSize: 5,
        showTotal: (total, range) => {
          return '当前' + range[0] + '-' + range[1] + '条' + '\xa0\xa0共' + total + '条'
        },
      },
    }
  },
  methods: {
    openModal(record) {
      this.visible = true
      this.dataList = record
      this.searchQuery(record.process_id)
      console.log('record', record)
      this.orderNum = record.process_id
      this.orderId = record.id
      console.log('this.dataList', this.dataList)
    },
    //新提交
    handleOk1() {
      p_postAction(`/bom/saveCustomize`, {
        bomDetail: JSON.stringify(this.dataSource), //bom列表
        enterprise: this.form.enterprise, //生产企业名称
        name: this.form.tableTxt, //产品名
        orderNum: this.orderNum, //
        origin: this.form.origin, //产地
        specification: this.form.model, //产品型号
      })
        .then((res) => {
          if (res.success == true) {
            let id = res.result.upload.result.post
            let newIdentity = res.result.newIdentity
            this.insertCictOrder(id)
            // var postData = {
            //   buyer: '',
            //   identity: '',
            //   order_status: '创建订单',
            //   product_information: '',
            //   receive_address: '',
            //   seller: '虹信',
            //   send_address: '武汉市',
            //   send_date: '',
            //   user_id: '',
            // }
            // postData.buyer = this.dataList.seller_name
            // postData.identity = '88.343.000000/' + this.dataList.process_id
            // postData.product_information = newIdentity + '-' + this.form.tableTxt
            // postData.receive_address = this.dataList.delivery_party_addr
            // postData.send_date = this.dataList.plan_date
            // var userid = JSON.parse(window.localStorage.getItem('pro__Login_UserId'))
            // postData.user_id = userid.value
            // tm_postAction('/cgform/api/form/be35e28f48244943933d3484239de3a7', postData).then((res) => {
            //   console.log('生菜采购订单插入模板成功')
            // })
            let params = {
              baseCode: this.dataList.custormize_basic,
              newCode: newIdentity,
              newName: this.form.tableTxt,
            }
            p_postAction('/createProctline/create', params)
              .then((res) => {
                // this.$emit('close-annTask')
              })
              .catch((err) => {
                console.log('err', err)
              })
            this.$message.success('需求定制成功')
          } else {
            this.$message.error('需求定制失败')
          }
          this.visible = false
          this.form = {
            tableTxt: '',
            model: '',
            identity: '',
            enterprise: '',
            origin: '',
            category: '',
          }
          this.searchKey = ''
          this.dataSource = []
          this.$emit('loadData')
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //调增加接口
    insertCictOrder(id) {
      // let userData = JSON.parse(localStorage.getItem('pro__Login_Userinfo'))
      // let tenant_id = Vue.ls.get(TENANT_ID)
      // axios.defaults.headers.common['X-Access-Token'] = Vue.ls.get(ACCESS_TOKEN)
      // axios.defaults.headers.common['tenant_id'] = tenant_id
      // axios.defaults.headers.common['userid'] = userData.value.id
      // axios.post(`/plm/insertCictOrderDetail/insert`, {
      p_postAction(`/insertCictOrderDetail/insert`, {
        orderId: this.orderId,
        id: id,
      })
        .then((res) => {
          console.log('res', res)
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //调接口设置产品标识编码
    setIdentity() {
      this.form.identity
    },
    searchQuery(searchKey) {
      if (searchKey != '') {
        console.log(searchKey)
        p_getAction(`/bomDetail/get?orderNum=` + searchKey)
          .then((res) => {
            this.dataSource = JSON.parse(res.result)
            if (res.result == null) {
              this.dataSource = []
            }
            console.log(this.dataSource)
          })
          .catch((error) => {
            console.log(error)
          })
      } else {
        this.$message.warning('无基础产品')
      }
    },
    searchReset() {
      this.searchKey = ''
      this.dataSource = []
    },
    handleDelete(record) {
      let deleteIndex = ''
      this.dataSource.forEach((item, index) => {
        if (item.标识编码 === record.标识编码) {
          deleteIndex = index
          return
        }
      })
      this.dataSource.splice(deleteIndex, 1)
    },
    addCustom() {
      this.$refs.addForm.openModal()
    },

    async handleOk() {
      console.log('提交')
      await this.addTemplate()
      // 新增关系
      if (this.isAdd) {
        let data2 = []
        this.dataSource.forEach((item, index) => {
          data2.push({ modelId: item.modelId, num: item.modelNum })
        })
        let postData2 = { childrenModels: data2, parentId: this.form.identity }
        //加token
        const token = Vue.ls.get(ACCESS_TOKEN)
        if (token) {
          axios.defaults.headers.common['X-Access-Token'] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
        }
        var _this = this
        var prod_id = this.addonbefore + this.form.identity
        var prod_name = this.form.tableTxt
        axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'
        im_postAction('/modelRelation/addRelation', JSON.stringify(postData2))
          .then((res) => {
            if (res.data.success) {
              let data = []
              let obj = {}
              let params = _this.dataList
              obj.order_id = params.id
              obj.prod_id = prod_id
              obj.prod_name = prod_name
              obj.prod_amount = params.custormize_amount
              data.push(obj)
              params.custormize_flag = 1
              params.cict_order_detail = data
              putAction('/online/cgform/api/form/e8c9f23b81d74fc6a292c436d27faa70', params)
                .then((res) => {
                  let params = {
                    baseCode: _this.dataList.custormize_four,
                    newCode: obj.prod_id,
                    newName: obj.prod_name,
                  }
                  p_postAction('/createProctline/create', JSON.stringify(params))
                    .then((res) => {
                      this.$emit('close-annTask')
                    })
                    .catch((err) => {
                      console.log('err', err)
                    })
                })
                .catch((err) => {
                  console.log('err', err)
                })
              Message({
                type: 'success',
                showClose: true,
                message: '定制成功!',
              })

              this.isAdd = true
            }
          })
          .catch((err) => {
            console.log(err)
          })
        this.form = {}
        this.searchKey = ''
        this.dataSource = []
        this.isAdd = false
        this.visible = false
      } else {
        Message({
          type: 'false',
          showClose: true,
          message: '请先建表!',
        })
      }
    },
    async addTemplate() {
      // 新建模板
      console.log('this.form', this.form)
      if (this.form.tableTxt && this.form.identity) {
        let data = JSON.stringify({
          category: '',
          deleteFieldIds: [],
          deleteIndexIds: [],
          fields: [
            {
              converter: '',
              dbDefaultVal: '',
              dbFieldName: 'id',
              dbFieldTxt: '主键',
              dbIsKey: '1',
              dbIsNull: '0',
              dbLength: 36,
              dbPointLength: '0',
              dbType: 'String',
              dictField: '',
              dictTable: '',
              dictText: '',
              fieldDefaultValue: '',
              fieldExtendJson: '',
              fieldHref: '',
              fieldLength: '120',
              fieldMustInput: '0',
              fieldShowType: 'text',
              id: '16107041387410916993',
              isQuery: '0',
              isReadOnly: '1',
              isShowForm: '0',
              isShowList: '0',
              mainField: '',
              mainTable: '',
              orderNum: 1,
              queryMode: 'single',
              sortFlag: '0',
            },
            {
              converter: '',
              dbDefaultVal: '',
              dbFieldName: 'identity',
              dbFieldTxt: '标识编码',
              dbIsKey: '0',
              dbIsNull: '1',
              dbLength: '64',
              dbPointLength: '0',
              dbType: 'String',
              dictField: '',
              dictTable: '',
              dictText: '',
              fieldDefaultValue: '',
              fieldExtendJson: '',
              fieldHref: '',
              fieldLength: '120',
              fieldMustInput: '0',
              fieldShowType: 'text',
              fieldValidType: '',
              id: '16107041387411598757',
              isQuery: '0',
              isReadOnly: '0',
              isShowForm: '1',
              isShowList: '1',
              mainField: '',
              mainTable: '',
              orderNum: 2,
              queryMode: 'single',
              sortFlag: '0',
            },
            {
              converter: '',
              dbDefaultVal: '',
              dbFieldName: 'create_time',
              dbFieldTxt: '创建日期',
              dbIsKey: '0',
              dbIsNull: '1',
              dbLength: '20',
              dbPointLength: '0',
              dbType: 'Date',
              dictField: '',
              dictTable: '',
              dictText: '',
              fieldDefaultValue: '',
              fieldExtendJson: '',
              fieldHref: '',
              fieldLength: '120',
              fieldMustInput: '0',
              fieldShowType: 'datetime',
              fieldValidType: '',
              id: '16107041387411598757',
              isQuery: '0',
              isReadOnly: '0',
              isShowForm: '0',
              isShowList: '1',
              mainField: '',
              mainTable: '',
              orderNum: 3,
              queryMode: 'single',
              sortFlag: '0',
            },
            {
              converter: '',
              dbDefaultVal: '',
              dbFieldName: 'update_time',
              dbFieldTxt: '更新日期',
              dbIsKey: '0',
              dbIsNull: '1',
              dbLength: '20',
              dbPointLength: '0',
              dbType: 'Date',
              dictField: '',
              dictTable: '',
              dictText: '',
              fieldDefaultValue: '',
              fieldExtendJson: '',
              fieldHref: '',
              fieldLength: '120',
              fieldMustInput: '0',
              fieldShowType: 'datetime',
              fieldValidType: '',
              id: '16107041387411598757',
              isQuery: '0',
              isReadOnly: '0',
              isShowForm: '0',
              isShowList: '1',
              mainField: '',
              mainTable: '',
              orderNum: 4,
              queryMode: 'single',
              sortFlag: '0',
            },
            {
              converter: '',
              dbDefaultVal: '',
              dbFieldName: 'power',
              dbFieldTxt: '功率大小',
              dbIsKey: '0',
              dbIsNull: '1',
              dbLength: '64',
              dbPointLength: '0',
              dbType: 'String',
              dictField: '',
              dictTable: '',
              dictText: '',
              fieldDefaultValue: '',
              fieldExtendJson: '',
              fieldHref: '',
              fieldLength: '120',
              fieldMustInput: '0',
              fieldShowType: 'text',
              fieldValidType: '',
              id: '16107041387411598757',
              isQuery: '0',
              isReadOnly: '0',
              isShowForm: '1',
              isShowList: '1',
              mainField: '',
              mainTable: '',
              orderNum: 5,
              queryMode: 'single',
              sortFlag: '0',
            },
            {
              converter: '',
              dbDefaultVal: '',
              dbFieldName: 'shape',
              dbFieldTxt: '形状',
              dbIsKey: '0',
              dbIsNull: '1',
              dbLength: '64',
              dbPointLength: '0',
              dbType: 'String',
              dictField: '',
              dictTable: '',
              dictText: '',
              fieldDefaultValue: '',
              fieldExtendJson: '',
              fieldHref: '',
              fieldLength: '120',
              fieldMustInput: '0',
              fieldShowType: 'text',
              fieldValidType: '',
              id: '16107041387411598757',
              isQuery: '0',
              isReadOnly: '0',
              isShowForm: '1',
              isShowList: '1',
              mainField: '',
              mainTable: '',
              orderNum: 6,
              queryMode: 'single',
              sortFlag: '0',
            },
            {
              converter: '',
              dbDefaultVal: '',
              dbFieldName: 'color',
              dbFieldTxt: '颜色',
              dbIsKey: '0',
              dbIsNull: '1',
              dbLength: '64',
              dbPointLength: '0',
              dbType: 'String',
              dictField: '',
              dictTable: '',
              dictText: '',
              fieldDefaultValue: '',
              fieldExtendJson: '',
              fieldHref: '',
              fieldLength: '120',
              fieldMustInput: '0',
              fieldShowType: 'text',
              fieldValidType: '',
              id: '16107041387411598757',
              isQuery: '0',
              isReadOnly: '0',
              isShowForm: '1',
              isShowList: '1',
              mainField: '',
              mainTable: '',
              orderNum: 7,
              queryMode: 'single',
              sortFlag: '0',
            },
          ],
          head: {
            formCategory: 'identity',
            formTemplate: '1',
            idType: 'UUID',
            isCheckbox: 'Y',
            isPage: 'Y',
            isTree: 'N',
            queryMode: 'single',
            relationType: 1,
            scroll: 1,
            tableName: '',
            tableTxt: '',
            tableType: 1,
            themeTemplate: 'normal',
          },
          identity: '',
          indexs: [
            {
              indexField: 'identity',
              indexName: 'identity',
              indexType: 'unique',
            },
          ],
        })
        let postData = JSON.parse(data)
        postData.identity = this.addonbefore + this.form.identity
        postData.head.tableName = 'identity_' + new Date().getTime()
        postData.head.tableTxt = this.form.tableTxt
        postData.category = this.form.category
        console.log('postData', postData)
        axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'
        await tm_postAction('/cgform/api/addAll', JSON.stringify(postData))
          .then((res) => {
            if (res.success) {
              Message({
                type: 'success',
                showClose: true,
                message: '新增成功!',
              })
              this.isAdd = true
            }
          })
          .catch((err) => {
            console.log(err)
          })
      } else {
        Message({
          type: 'false',
          showClose: true,
          message: '请将信息填写完整!',
        })
      }
    },
    handleCancel() {
      this.searchKey = ''
      this.dataSource = []
      this.isAdd = false
      this.visible = false
      this.form = {}
    },
    randomString(e) {
      e = e || 5
      var t = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678',
        a = t.length,
        n = ''
      for (let i = 0; i < e; i++) n += t.charAt(Math.floor(Math.random() * a))
      return n
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
</style>
