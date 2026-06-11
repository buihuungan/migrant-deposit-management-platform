<template>
  <div>
    <a-card :bordered="false">
      <a-form layout="inline">
        <a-form-item label="标识编码">
          <a-input v-model="product_id" placeholder="请输入标识编码" />
        </a-form-item>
        <!-- <a-form-item label="标识编码名称">
          <a-input v-model="product_name" placeholder="请输入标识编码名称" />
        </a-form-item> -->
        <a-form-item>
          <a-button-group>
            <a-button type="primary" icon="search" @click="loadData()">查询</a-button>
            <a-button type="primary" icon="reload" @click="reData()">重置</a-button>
          </a-button-group>
        </a-form-item>
      </a-form>
      <div style="margin-top: 20px">
        <a-button-group>
          <a-button type="primary" icon="plus" @click="plusFile(0)" style="margin-right: 20px">新增</a-button>
          <a-button type="primary" icon="plus" @click="plusFile(1)">定制检测</a-button>
        </a-button-group>
      </div>
      <div style="padding-top: 20px">
        <a-table :columns="columns" :dataSource="fileList" rowKey="id" :scroll="{ x: 1000 }" :pagination="ipagination">
          <span slot="action" slot-scope="text, record, index">
            <a @click="editFile(record)">编辑</a>
            <a-divider type="vertical" />
            <!-- <a @click="deleteFile(record.id)">删除</a>
            <a-divider type="vertical" /> -->
            <a @click="inspection(record)" v-if="record.order_state !==3">检测</a>
            <a v-else disabled>已送检</a>
            <a-divider type="vertical" />
            <a @click="seeInspection(record)"  :disabled="record.data_id == null">查看检测结果</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <div>
      <AnnFile ref="annFile" @ok="handleOk"> </AnnFile>
      <EditFile ref="editFile" @ok="handleOk"> </EditFile>
      <InspectionFile ref="inspectionFile" @ok="handleOk"> </InspectionFile>
      <see-inspection ref="seeInspection"></see-inspection>
      <custom-inspection ref="customInspection"></custom-inspection>
      <a-modal
        title="定制检测结果查看"
        width="20%"
        :footer="null"
        :visible="visible"
        :centered="true"
        :confirm-loading="confirmLoading"
        @ok="handleOk"
        @cancel="handleOk"
      >
        <a-radio-group default-value="value1">
          <a-radio :style="radioStyle" value="value1" :disabled="true"> 合格 </a-radio>
          <a-radio :style="radioStyle" value="value2" :disabled="true"> 不合格 </a-radio>
        </a-radio-group>
      </a-modal>
    </div>
    
  </div>
</template>
<script>
import AnnFile from './annFile'
import EditFile from './editFile'
import InspectionFile from './inspectionFile'
import seeInspection from './seeInspection'
import customInspection from './customInspection'
import api from '@/api/index'
import { o_getAction, o_deleteAction } from '@/api/onApi'
import { getAction } from '@/api/manage'
import axios from 'axios'
import { USER_ID } from '@/store/mutation-types'
import Vue from 'vue'
import { ACCESS_TOKEN ,TENANT_ID} from '@/store/mutation-types'
import { Message } from "element-ui";

export default {
  name: 'qualityInspection',
  components: { AnnFile, EditFile, InspectionFile, seeInspection, customInspection },
  data() {
    return {
      visible: false,
      confirmLoading: false,
      record: [],
      product_name: '',
      product_id: '',
      annFileName: '新增标识编码数据采集',
      editFileName: '',
      formId: '',
      onlineTableId: 'bba30b3a70924ac7aa65c220ee62cb30',
      onlineDataId: '',
      fileList: [],
      radioStyle: {
        display: 'block',
        height: '30px',
        lineHeight: '30px',
      },
      columns: [
        {
          title: '标识编码',
          align: 'center',
          dataIndex: 'product_id',
          width: 200,
        },
        {
          title: '标识编码名称',
          align: 'center',
          dataIndex: 'product_name',
          width: 150,
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark',
          width: 150,
        },
        {
          title: '数据采集文件上传路径',
          align: 'center',
          dataIndex: 'product_data_file[0].fileName',
          customRender: (text) => (
            <a
              href={api.server_url + `/plm/common/download//` + text}
              download={api.server_url + `/plm/common/download//` + text}
            >
              {text}
            </a>
          ),
          width: 200,
        },
        {
          title: '检测分类',
          align: 'center',
          dataIndex: 'categories',
          width: 200,
        },
        // {
        //   title: '上传日期',
        //   align: 'center',
        //   dataIndex: 'create_time',
        //   width: 150,
        // },
        {
          title: '上传人员',
          align: 'center',
          dataIndex: 'create_by',
          width: 150,
        },
        {
          title: '操作',
          align: 'center',
          fixed: 'right',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          width: 280,
        },
      ],
      ipagination: {
        current: 1,
        pageSize: 10,
        showTotal: (total, range)=> {
          return range[0] + '-' + range[1] +  ' 共' + total + '条'
        },
        onChange: this.onPageChange.bind(this),
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    handleOk() {
      this.loadData()
    },
    //重置查询参数
    reData() {
      this.product_name = ''
      this.product_id = ''
    },
    handleOk() {
      this.visible = false
    },
    //查看检测结果
    seeInspection(record) {
      if (record.categories === '定制检测') {
        this.visible = true
      } else {
        this.$refs.seeInspection.openModal(record)
      }
    },
    //进行检测
    inspection(record) {
      if (record.categories === '定制检测') {
        this.$refs.customInspection.openModal(record)
      } else {
        this.annFileName = '标识编码质量检测'
        this.record = record
        this.$refs.inspectionFile.openModal(this.annFileName, record)
      }
    },
    //编辑文件
    editFile(record) {
      if (record.categories === '定制检测') {
        this.formId = '7d107de62a414878b8234c24d4a1ccad'
      } else {
        this.formId = '18034e4fffc34a04af3cc3350c16710c'
      }
      this.editFileName = '编辑标识编码数据采集'
      this.onlineDataId = record.id
      this.$refs.editFile.openModal(this.formId, this.onlineTableId, this.onlineDataId, this.editFileName)
    },
    //删除文件
    // deleteFile(id) {
    //   this.$elementConfirm('真的要删除这个文件吗 ?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning',
    //   }).then(() => {
    //     o_deleteAction('/cgform/api/form/' + this.onlineTableId + '/' + id).then((res) => {
    //       if (res.success) {
    //         this.$message.success(res.message)
    //         this.loadData()
    //       } else {
    //         this.$message.warning(res.message)
    //       }
    //     })
    //   })
    // },
    //打开文件新增页面
    plusFile(flag) {
      if (flag === 0) {
        var formId = 'ed8f8701098b40cf8e53b059daa7e641'
        var annFileName = '新增标识编码数据采集'
        this.$refs.annFile.openModal(formId, annFileName)
      } else {
        var formId = '94ce37cf019c403c84f0ed61c686c7b5'
        var annFileName = '新增标识编码数据采集'
        this.$refs.annFile.openModal(formId, annFileName)
      }
    },
    //加载所有的文件数据
    loadData() {
      this.fileList = []
      // var queryDateInfo = [
      //   { rule: 'LIKE', type: 'text', val: '', field: 'product_id' },
      //   //{ rule: 'LIKE', type: 'text', val: '', field: 'product_name' },
      // ]
      // queryDateInfo[0].val = this.product_id
      // //queryDateInfo[1].val = this.product_name
      // console.log('queryDateInfo', queryDateInfo)
      // let params = {
      //   column: 'createTime',
      //   order: 'desc',
      //   superQueryMatchType: 'and',
      //   pageNo: 1,
      //   pageSize: -521,
      //   superQueryParams: encodeURIComponent(JSON.stringify(queryDateInfo)),
      // }
      // o_getAction('/cgform/api/getData/' + this.onlineTableId, params).then((res) => {
      //   if (res.success) {
      //     var records = res.result.records
      //     for (let i = 0; i < records.length; i++) {
      //       if (records[i].inspection_result == '0') {
      //         records[i].inspection_result = '合格'
      //       }
      //       if (records[i].inspection_result == '1') {
      //         records[i].inspection_result = '不合格'
      //       }
      //     }
      //     this.fileList = records
      //   }
      // })
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        exampleId: this.$store.state.exampleId
      }
      axios({
        url: api.server_url + '/mes/productQuality/selectData',
        method: 'get',
        params: params,
        headers: {
          'userid':  Vue.ls.get(USER_ID),
          'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)
        },
      }).then(res => {
        console.log('res',res)

        res.data.result.list.records.forEach((item) => {
          let record = {}
          record.bdtokenurl = item.bdtokenurl
          record.categories = item.categories
          record.check_form_id = item.checkFormId
          record.create_by = item.createBy
          record.create_time = item.createTime
          record.data_id = item.dataId
          record.demo3d_all_data = item.demo3dAllData
          record.example_id = item.exampleId
          record.id = item.id
          record.product_data_file = item.productDataFile
          record.product_id = item.productId
          record.product_name = item.productName
          record.remark = item.remark
          record.sys_org_code = item.sysOrgCode
          record.true3d_data = item.true3dData
          record.update_by = item.updateBy
          record.update_time = item.updateTime
          this.fileList.push(record)

        })
        this.ipagination.total = res.data.result.list.total
      })

    },
    onPageChange(page, pageSize) {
      this.ipagination.current = page
      this.loadData()
    },
  },
}
</script>

<style>
/* .ant-form-inline .ant-form-item{
    display: inline-flex !important;
  } */
.desformspana a {
  color: #1890ff !important;
}
.xbutton {
  float: right;
  height: 30px;
  width: 40px;
  margin-top: 10px;
  margin-right: 10px;
}
</style>