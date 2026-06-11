/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import { filterObj } from '@/utils/util'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'
// import { o_deleteAction, o_getAction } from '@/api/onApi'
import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'

export const JeecgListMixin = {
  data() {
    return {
      //token header
      tokenHeader: { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) },
      /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
      queryParam: {},
      /* 数据源 */
      dataSource: [],
      /* 分页参数 */
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      /* 排序参数 */
      isorter: {
        column: 'createTime',
        order: 'desc'
      },
      /* 筛选参数 */
      filters: {},
      /* table加载状态 */
      loading: false,
      /* table选中keys*/
      selectedRowKeys: [],
      /* table选中records*/
      selectionRows: [],
      /* 查询折叠 */
      toggleSearchStatus: false,
      /* 高级查询条件生效状态 */
      superQueryFlag: false,
      /* 高级查询条件 */
      superQueryParams: '',
      /** 高级查询拼接方式 */
      superQueryMatchType: 'and'
    }
  },
  created() {
    if (!this.disableMixinCreated) {
      // console.log(' -- mixin created -- ')
      this.loadData()
      //初始化字典配置 在自己页面定义
      this.initDictConfig()
    }
  },
  methods: {
    loadData(arg, searchobj, ischildtable, isonlineUrl) {
      if (!this.url.list) {
        this.$message.error('请设置url.list属性!')
        return
      }
      console.log('arg', arg)
      console.log('searchobj', searchobj)
      console.log('ischildtable', ischildtable)
      console.log('isonlineUrl', isonlineUrl)
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      this.loading = true
      // console.log('ssss',this.url.list);

      if (ischildtable) {
        var url = `${this.url.list}/${searchobj}` //父子表
        let tenant_id = Vue.ls.get(TENANT_ID)

        if (isonlineUrl && tenant_id) {
          var params = {} //查询条件
          params.superQueryParams = escape(
            JSON.stringify([
              {
                rule: 'eq',
                type: 'text',
                val: tenant_id,
                field: 'tenant_id'
              }
            ])
          )
        }
      } else {
        var url = `${this.url.list}`
        if (isonlineUrl) {
          var params = this.getQueryParams(searchobj, isonlineUrl) //查询条件
        } else {
          var params = this.getQueryParams(searchobj) //查询条件
        }
      }

      
      // // username sex sex_dictText
      // console.log('用户账号查询' , this.queryParam.username)
      // console.log('用户性别查询' , this.queryParam.sex)
      // console.log('用户性别查询' , this.queryParam.realname)
      // console.log('用户性别查询' , this.queryParam.phone)
      // console.log('用户性别查询' , this.queryParam.status)
      // // roleName
      // console.log('角色查询' , this.queryParam.roleName)
      // // code  postRank postRank_dictText    name
      // console.log('职务编码查询' , this.queryParam.code )
      // console.log('职务职级查询' , this.queryParam.postRank)
      // console.log('职务名称查询' , this.queryParam.name)
      // // username
      // console.log('部门用户信息查询' , this.queryParam.username)
      // // titile
      // console.log('系统通告查询' , this.queryParam.titile)

      // 用户管理查询
      if(this.queryParam.username !== undefined){
        params.username = this.queryParam.username
      }
      if(this.queryParam.sex !== undefined){
        params.sex = this.queryParam.sex
      }
      if(this.queryParam.realname !== undefined){
        params.realname = this.queryParam.realname
      }
      if(this.queryParam.phone !== undefined){
        params.phone = this.queryParam.phone
      }
      if(this.queryParam.status !== undefined){
        params.status = this.queryParam.status
      }
      // 角色管理查询
      if(this.queryParam.roleName !== undefined){
        params.roleName = this.queryParam.roleName
      }
      // 职务查询
      if(this.queryParam.code !== undefined){
        params.code = this.queryParam.code
      }
      if(this.queryParam.postRank !== undefined){
        params.postRank = this.queryParam.postRank
      }
      if(this.queryParam.name !== undefined){
        params.name = this.queryParam.name
      }
      // 通告查询
      if(this.queryParam.titile !== undefined){
        params.titile =  this.queryParam.titile
      }
      // if (url.indexOf('/online/cgform/api/getData/') != -1 && !isonlineUrl) {
      //   return
      // }
      console.log('params',params)
      getAction(url, params).then(res => {
        if (res.success) {
          if (ischildtable) {
            this.dataSource = res.result[`${ischildtable}`]
          } else {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
        }
        if (res.code === 510) {
          this.$message.warning(res.message)
        }
        this.loading = false
      })
    },
    initDictConfig() {
      // console.log("--这是一个假的方法!")
    },
    handleSuperQuery(params, matchType) {
      //高级查询方法
      if (!params) {
        this.superQueryParams = ''
        this.superQueryFlag = false
      } else {
        this.superQueryFlag = true
        this.superQueryParams = JSON.stringify(params)
        this.superQueryMatchType = matchType
      }
      this.loadData(1)
    },
    getQueryParams(searchobj, isonlineUrl) {
      //获取查询条件
      let sqp = {} 
      if (this.superQueryFlag) {
        if (this.superQueryParams) {
          sqp['superQueryParams'] = encodeURI(this.superQueryParams)
        } else {
          let tenant_id = Vue.ls.get(TENANT_ID)
          let superQueryParams = []
          if (searchobj) {
            superQueryParams = [
              {
                rule: 'eq',
                type: 'text',
                val: 'lesson_id',
                field: searchobj
              }
            ]
          }
          if (isonlineUrl && tenant_id) {
            superQueryParams.push({
              rule: 'eq',
              type: 'text',
              val: tenant_id,
              field: 'tenant_id'
            })
          }
          if (this.queryParam[Object.keys(this.queryParam)[0]]) {
            superQueryParams.push({
              rule: 'eq',
              type: 'text',
              val: this.queryParam[Object.keys(this.queryParam)[0]],
              field: Object.keys(this.queryParam)[0]
            })
          }
          if (this.sys_org_code) {
            superQueryParams.push({
              rule: 'eq',
              type: 'text',
              val: this.sys_org_code,
              field: 'org_code'
            })
          }
          if (superQueryParams.length) {
            sqp['superQueryParams'] = encodeURI(JSON.stringify(superQueryParams))
          }
          // console.log('***********查询数据',superQueryParams)
        }

        if (searchobj) {
          sqp['superQueryMatchType'] = 'and'
        } else {
          sqp['superQueryMatchType'] = this.superQueryMatchType
        }
        sqp['superQueryFlag'] = true

        var param = Object.assign(sqp)
      } else {
        var param = Object.assign(sqp, this.queryParam, this.isorter, this.filters)
        param.field = this.getQueryField()
      }

      param.pageNo = this.ipagination.current
      param.pageSize = this.ipagination.pageSize
      return filterObj(param)
    },
    getQueryParams1() {
      //获取查询条件
      let sqp = {}
      // sqp['superQueryParams']=encodeURI(this.superQueryParams)
      sqp['superQueryMatchType'] = 'or'
      var param = Object.assign(sqp, this.queryParam, this.isorter, this.filters)
      // param.field = this.getQueryField();
      param.pageNo = this.ipagination.current
      param.pageSize = this.ipagination.pageSize
      return filterObj(param)
    },
    getQueryField() {
      //TODO 字段权限控制
      var str = 'id,'
      this.columns.forEach(function(value) {
        str += ',' + value.dataIndex
      })
      return str
    },

    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectionRows
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    searchQuery(isonlineUrl, searchobj, ischildtable) {
      console.log('username' , isonlineUrl)
      console.log('username' , searchobj)
      console.log('username' , ischildtable)
      console.log('username' , this.queryParam.username)
      // if (isonlineUrl && this.queryParam.username == undefined) {
      //   //高级查询，online查询
      //   this.superQueryFlag = true
      // }
      this.loadData(1, searchobj, ischildtable, isonlineUrl)
    },
    superQuery() {
      this.$refs.superQueryModal.show()
    },
    searchReset(isonlineUrl, searchobj, ischildtable) {
      // if (isonlineUrl) {
      //   //高级查询，online查询
      //   this.superQueryFlag = true
      // }
      this.queryParam = {}
      this.loadData(1, searchobj, ischildtable, isonlineUrl)
    },
    batchDel: function() {
      if (!this.url.deleteBatch) {
        this.$message.error('请设置url.deleteBatch属性!')
        return
      }
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！')
        return
      } else {
        var ids = ''
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ','
        }
        var that = this
        this.$confirm({
          title: '确认删除',
          content: '是否删除选中数据?',
          onOk: function() {
            that.loading = true
            deleteAction(that.url.deleteBatch, { ids: ids })
              .then(res => {
                if (res.success) {
                  that.$message.success(res.message)
                  that.loadData()
                  that.onClearSelected()
                } else {
                  that.$message.warning(res.message)
                }
              })
              .finally(() => {
                that.loading = false
              })
          }
        })
      }
    },
    handleDelete: function(id) {
      if (!this.url.delete) {
        this.$message.error('请设置url.delete属性!')
        return
      }
      var that = this
      deleteAction(that.url.delete, { id: id }).then(res => {
        if (res.success) {
          that.$message.success(res.message)
          that.loadData()
        } else {
          that.$message.warning(res.message)
        }
      })
    },
    handleEdit: function(record) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.disableSubmit = false
    },
    handleAdd: function() {
      this.$refs.modalForm.add()
      this.$refs.modalForm.title = '新增'
      this.$refs.modalForm.disableSubmit = false
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选

      // console.log('表单上的',pagination, filters, sorter)
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field
        this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
      }
      this.ipagination = pagination
      this.loadData()
    },
    handleToggleSearch() {
      this.toggleSearchStatus = !this.toggleSearchStatus
    },
    // 给popup查询使用(查询区域不支持回填多个字段，限制只返回一个字段)
    getPopupField(fields) {
      return fields.split(',')[0]
    },
    modalFormOk() {
      // 新增/修改 成功时，重载列表
      // console.log('mixin modalFormOk');
      this.loadData()
    },
    handleDetail: function(record) {
      this.$refs.modalForm.edit(record, true, true)
      this.$refs.modalForm.title = '详情'
      this.$refs.modalForm.disableSubmit = true
    },
    /* 导出 */
    handleExportXls2() {
      let paramsStr = encodeURI(JSON.stringify(this.getQueryParams()))
      let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?paramsStr=${paramsStr}`
      window.location.href = url
    },
    handleExportXls(fileName) {
      if (!fileName || typeof fileName != 'string') {
        fileName = '导出文件'
      }
      let param = { ...this.queryParam }
      if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
        param['selections'] = this.selectedRowKeys.join(',')
      }
      // console.log("导出参数",param)
      downFile(this.url.exportXlsUrl, param).then(data => {
        if (!data) {
          this.$message.warning('文件下载失败')
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
        } else {
          let url = window.URL.createObjectURL(new Blob([data], { type: 'application/vnd.ms-excel' }))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName + '.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        }
      })
    },
    /* 导入 */
    handleImportExcel(info) {
      if (info.file.status !== 'uploading') {
        // console.log(info.file, info.fileList);
      }
      if (info.file.status === 'done') {
        if (info.file.response.success) {
          // this.$message.success(`${info.file.name} 文件上传成功`);
          if (info.file.response.code === 201) {
            let {
              message,
              result: { msg, fileUrl, fileName }
            } = info.file.response
            let href = window._CONFIG['domianURL'] + fileUrl
            this.$warning({
              title: message,
              content: (
                <div>
                  <span>{msg}</span>
                  <br />
                  <span>
                    具体详情请{' '}
                    <a href={href} target="_blank" download={fileName}>
                      点击下载
                    </a>{' '}
                  </span>
                </div>
              )
            })
          } else {
            this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
          }
          this.loadData()
        } else {
          this.$message.error(`${info.file.name} ${info.file.response.message}.`)
        }
      } else if (info.file.status === 'error') {
        this.$message.error(`文件上传失败: ${info.file.msg} `)
      }
    },
    /* 图片预览 */
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','))
      }
      return getFileAccessHttpUrl(text)
    },
    /* 文件下载 */
    // update--autor:lvdandan-----date:20200630------for：修改下载文件方法名uploadFile改为downloadFile------
    downloadFile(text) {
      if (!text) {
        this.$message.warning('未知的文件')
        return
      }
      if (text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','))
      }
      let url = getFileAccessHttpUrl(text)
      window.open(url)
    }
  }
}
