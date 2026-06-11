<template>
  <div>
    <a-tabs defaultActiveKey="1" @change="callback">
      <a-tab-pane tab="案件类型" key="1">
        <course-list type="type" v-if="key == 1" @getdetailrecords="getDetailRecords"></course-list>
      </a-tab-pane>
      <a-tab-pane tab="案件领域" key="2">
        <course-list type="domain" v-if="key == 2" @getdetailrecords="getDetailRecords"></course-list>
      </a-tab-pane>
    </a-tabs>
    <a-row>
      <course-list-detail v-show="visible" :detailrecords="detailrecords" :editor="editor"></course-list-detail>
    </a-row>
  </div>
</template>
<script>
import { getAction, putAction, deleteAction } from '@/api/manage'
import CourseList from './modules/CourseList'
import courseListDetail from './modules/courseListDetail'

export default {
  components: { CourseList, courseListDetail },
  name: 'courseDetail',
  data() {
    return {
      treeData: [],
      dataSource: [],
      // columns: columns,
      selectedKeys: [],
      selectedId: null,
      courseIdInfo: [], // 课件id表
      drawerModel: {
        visible: false,
        title: '',
        drawerForm: {},
        isEdit: false,
      },
      url: {
        getCourseType: '/online/cgform/api/getData/474400feca3a4ac4a40549b57788b6ad',
        getCourseIdInfo: '/online/cgform/api/getData/ed413176867946fbb8e7ffeab9d13817', // 获取课件 id
        getCourse: '/online/cgform/api/getData/22f5f24e82444c3db7228f2c2412654d',
        editCourse: '/online/cgform/api/form/22f5f24e82444c3db7228f2c2412654d', // 修改
        deleteCourse: '/online/cgform/api/form/22f5f24e82444c3db7228f2c2412654d', // 删除
      },
      editor: null,
      key: 1,
      detailrecords: {},
      visible: false,
    }
  },
  mounted() {
    this.editor = createEditor('/static/examples/editors/config/showmxgraph.xml')

    this.editor.graph.setEnabled(false)
    this.editor.graph.setPanning(true)
    this.editor.graph.setTooltips(true)
  },
  watch: {
    selectedId(newVal, oldVal) {
      if (!newVal) {
        this.getCourseByTypeId()
        return
      }
      const params = {}
      params.superQueryMatchType = 'or'
      params.superQueryParams = escape(
        JSON.stringify([
          {
            rule: 'eq',
            type: 'text',
            val: newVal,
            field: 'ware_type_id',
          },
        ])
      )
      this.getCourseByTypeId(params)
    },
  },
  methods: {
    getDetailRecords(val) {
      this.detailrecords = val
      this.visible = true
    },
    callback(key) {
      this.key = key
      this.visible = false
    },
    /**
     * 获取课件类型
     */
    async getCourseType() {
      let res = await getAction(this.url.getCourseType)

      if (!res.success) {
        // 出错了，加载失败
        this.$message.error(res.message)
        return
      }

      const { total, records } = res.result
      // console.log(records);
      // 无数据
      if (total === 0) return
      console.log('树的数据', records)

      records.map((val, index) => {
        this.treeData.push({
          title: val.ware_type_name,
          key: val.id,
        })
      })
    },
    onSelect(selectedKeys, e) {
      console.log('点击的key', selectedKeys)

      this.selectedKeys = selectedKeys
      if (selectedKeys.length === 0) {
        this.selectedId = null
        return
      }
      this.selectedId = selectedKeys[0]
    },
    /**
     * 根据课件类型查询课件 id
     */
    async getCourseByTypeId(params = []) {
      // 初始化
      this.dataSource = []

      let res
      if (params.length === 0) {
        // 查询所有课件信息
        res = await getAction(this.url.getCourseIdInfo)
      } else {
        res = await getAction(this.url.getCourseIdInfo, params)
      }
      // console.log('getCourseByTypeId', res);
      if (!res.success) {
        // 出错了，加载失败
        this.$message.error(res.message)
        return
      }
      // 关系表
      const courseIdInfo = res.result

      // console.log('**********所有的id数据', courseIdInfo.total, courseIdInfo.records);

      // 无数据
      if (courseIdInfo.total === 0) return

      let query = {}
      query.superQueryMatchType = 'or'
      let superQueryParams = []
      for (let i = 0; i < courseIdInfo.records.length; i++) {
        const element = courseIdInfo.records[i]
        // const ware_info = await this.getCourseInfoById(element.ware_id) // 获取
        //获取所有的查询条件，
        superQueryParams.push({
          rule: 'eq',
          type: 'text',
          val: element.ware_id,
          field: 'id',
        })
      }
      query.superQueryParams = escape(JSON.stringify(superQueryParams))

      this.getCourseInfoById(query)
    },
    /**
     * 根据课件 id 查询课件内容
     */
    async getCourseInfoById(params) {
      const res = await getAction(this.url.getCourse, params)
      if (!res.success || !res.result) return

      const { records } = res.result
      records.forEach((item) => {
        this.dataSource.push({
          course_name: item.ware_name,
          course_teacher: item.teacher_name,
          course_teacher_id: item.teacher_work_no,
          id: item.id,
          key: item.id,
          ware_content: item.ware_content,
        })
      })
      console.log('this.dataSource', this.dataSource)
    },
    onClose() {
      this.drawerModel.visible = false
    },
    operateInfo(info, flag) {
      // console.log(info)
      switch (flag) {
        case 'delete':
          this.deleteCourse(info)
          break

        case 'edit':
          this.drawerModel.visible = true
          this.drawerModel.drawerForm = info
          this.drawerModel.isEdit = false
          break

        case 'select':
          ;(this.$refs.modalForm.visible = true), this.$refs.modalForm.open(info)
          // this.drawerModel.visible = true
          this.drawerModel.drawerForm = info
          // this.drawerModel.isEdit = true

          break
      }
    },
    submitData(info) {
      // id: "1341316591547281410"
      // sys_org_code: "1111"
      // tenant_id: "3"
      // teacher_id: "1111"
      // teacher_name: "1111"
      // teacher_work_no: "1111"
      // ware_content: "1111"
      // ware_name: "33333333333333333333333333333333333"
      // 修改数据
      console.log('修改数据', info)
      putAction(this.url.editCourse, info).then(
        (res) => {
          console.log(res)
        },
        (err) => {}
      )
    },
    async deleteCourse(info) {
      // console.log(`${this.url.deleteCourse}/${info.id}`);
      const res = await deleteAction(`${this.url.deleteCourse}/${info.id}`)

      if (!res.success) {
        this.$message.error('删除失败')
        return
      }
      this.$message.success(res.message)

      console.log('删除log', this.selectedId, res)
      // 没有选中类型
      if (this.selectedId == null) {
        this.selectedKeys = []
        this.getCourseByTypeId()
        return
      }
      // 选中类型
      const params = {}
      params.superQueryMatchType = 'or'
      params.superQueryParams = escape(
        JSON.stringify([
          {
            rule: 'eq',
            type: 'text',
            val: this.selectedId,
            field: 'ware_id',
          },
        ])
      )
      this.getCourseByTypeId(params)
    },
  },
  beforeDestroy() {
    this.editor && this.editor.destroy && this.editor.destroy()
    this.editor = null
  },
}
</script>

<style scoped>
</style>