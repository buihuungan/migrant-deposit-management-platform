<template>
  <div>
    <!-- 筛选部分 -->
    <a-row v-if="hasFilterOptions" :gutter="24" style="margin-bottom: 20px">
      <!-- 普通筛选控件 -->
      <a-col v-for="column in configurationParameter.columnsData" :key="column.dataIndex" :md="8" :xxl="5"
        v-if="column.filterType && column.filterType !== 'mixedInput'" style="display: flex; align-items: center">
        <!-- 控件的左侧标签 -->
        <span style="width: 100px; text-align: right; margin-right: 8px; font-weight: bold">{{ column.title }}：</span>

        <!-- 根据 filterType 渲染不同的筛选控件,目前支持 input、select和日期选择器 -->
        <a-input v-if="column.filterType === 'input'" v-model="filterConditions[column.dataIndex]"
          :placeholder="`请输入${column.title}`" style="flex: 1; max-width: 200px" allowClear />

        <a-select v-if="column.filterType === 'select'" v-model="filterConditions[column.dataIndex]"
          :placeholder="`请选择${column.title}`" style="flex: 1; max-width: 200px">
          <a-select-option v-for="(item, index) in getSelectOptions(column.dataIndex)" :key="index" :value="item">
            {{ item }}
          </a-select-option>
        </a-select>

        <a-range-picker v-if="column.filterType === 'date'" v-model="filterConditions[column.dataIndex]"
          style="flex: 1; max-width: 300px" />
      </a-col>

      <!-- 混合筛选控件 -->
      <a-col v-if="mixedFilterColumns.length" style="display: flex; align-items: center" :md="8" :xxl="5">
        <span style="width: 150px; text-align: right; margin-right: 8px;  font-weight: bold">
          {{ mixedFilterTitles }}：
        </span>
        <a-input v-model="mixedFilterValue" placeholder="请输入关键字" style="flex: 1; max-width: 400px" allowClear />
      </a-col>

      <!-- 筛选按钮和清空按钮 -->
      <div v-if="hasFilterOptions" style="margin-bottom: 20px" :md="4" :xxl="5">
        <a-button type="primary" @click="handleFilter" style="margin-right: 8px">筛选</a-button>
        <a-button @click="clearFilters">清空筛选</a-button>
      </div>
    </a-row>

    <!-- 表格 -->
    <a-table bordered :columns="columnsList" :dataSource="filteredDataSourceList" rowKey="id">
      <span slot="flowWillAnnounceaction" slot-scope="text, record, index">
        <a @click="announceTask(record)">处理该任务</a>
        <a-divider type="vertical" />
        <a @click="seeHistory(record)">历史</a>
      </span>
      <span slot="flowHistoryaction" slot-scope="text, record, index">
        <a @click="seeHistory(record)">历史</a>
        <a-divider v-if="record.is_export === 'true'" type="vertical" />
        <a v-if="record.is_export === 'true'" @click="download(record)">下载文书</a>
      </span>
      <span slot="flowUseMoneycolumns" slot-scope="text, record, index">
        <a @click="startProcess(record)">申请使用</a>
      </span>
      <span slot="flowChangecolumns" slot-scope="text, record, index">
        <a @click="startFixedProcess(true, record)">申请存缴方式变更</a>
      </span>
      <span slot="flowBackPaycolumns" slot-scope="text, record, index">
        <a @click="urge(record)" v-if="['承办人员', '业务分管', '人社分管'].some(role => userInfo.roleNames.includes(role))">催缴</a>
        <a @click="startProcess(record)" v-if="['施工企业', '管理员'].some(role => userInfo.roleNames.includes(role))">补缴</a>
      </span>
      <span slot="flowExtendcolumns" slot-scope="text, record, index">
        <a @click="urge(record)" v-if="['承办人员', '业务分管', '人社分管'].some(role => userInfo.roleNames.includes(role))">预警</a>
        <a @click="startProcess(record)"
          v-if="['施工企业', '管理员'].some(role => userInfo.roleNames.includes(role))">更换保函/延长有效期</a>
      </span>
      <span slot="flowReturncolumns" slot-scope="text, record, index">
        <a @click="startProcess(record)">申请返还</a>
      </span>
    </a-table>
  </div>
</template>

<script>
import { nw_getAllData } from '@api/newWorkApi'
import { taskStateMapping } from '../taskStateMapping'

export default {
  name: 'commonTable',
  props: {
    configurationParameter: {
      type: Object,
      default: () => { },
    },
    announceTask: {
      type: Function,
      default: () => { },
    },
    seeHistory: {
      type: Function,
      default: () => { },
    },
    startProcess: {
      type: Function,
      default: () => { },
    },
    startFixedProcess: {
      type: Function,
      default: () => { },
    },
    download: {
      type: Function,
      default: () => { },
    },
    userInfo: {
      type: Object,
      default: () => { },
    },
    urge: {
      type: Function,
      default: () => { },
    },
  },
  data() {
    return {
      dataSourceList: [], // 原始数据
      filteredDataSourceList: [], // 筛选后的数据
      columnsList: [], // 表格列配置
      filterConditions: {}, // 存储筛选条件
      mixedFilterValue: '', // 混合筛选的输入框值
    }
  },
  computed: {
    // 判断是否有筛选项
    hasFilterOptions() {
      return this.configurationParameter.columnsData.some((column) => column.filterType)
    },
    // 筛选出 filterType 为 mixedInput 的列
    mixedFilterColumns() {
      return this.configurationParameter.columnsData.filter((column) => column.filterType === 'mixedInput')
    },
    // 拼接混合筛选框的标题
    mixedFilterTitles() {
      return this.mixedFilterColumns.map((column) => column.title).join('/')
    },
  },
  methods: {
    // 获取表格数据并初始化
    async getAllList() {
      let params = {
        ...this.configurationParameter.inquire,
      }
      try {
        const res = await nw_getAllData('generalList/getAllList', params)

        // 过滤掉 allData 为空的脏数据
        const validDataList = res.result.dataList.filter((dataItem) => {
          return dataItem.allData && Object.keys(dataItem.allData).length > 0 // 只有 allData 不为空对象时才保留
        })
        console.log('过滤之后的数据', validDataList)

        // 遍历 dataList 生成 dataSourceList
        const dataSourceList = validDataList.map((dataItem) => {
          const item = {}
          // 流程基本值
          item.processName = dataItem.processName
          item.taskId = dataItem.taskId
          item.processId = dataItem.processId
          item.processInstanceId = dataItem.processInstanceId
          item.processHisInstanceId = dataItem.processHisInstanceId
          // 某些流程需要取下面这些值
          if (dataItem.allData.main_payment) { // 确保main_payment存在
            item.depositWay = dataItem.allData.main_payment.deposit_way // 保证金存缴方式
          }
          item.flag = dataItem.flag // 用于判断是否是撤回的任务

          // 处理前置流程 id
          const processMainTable = dataItem.processMainTable
          const processMainKey = dataItem.processMainKey
          item.frontId = dataItem.allData?.[processMainTable]?.[processMainKey] || null // 该流程的前置流程 id

          // 遍历 columnsData 来生成 dataSource
          this.configurationParameter.columnsData.forEach((column) => {
            if (column.dataLocation) {
              // 判断 dataLocation 是否存在，并提取数据
              const keys = column.dataLocation.split('.')
              let value = dataItem
              keys.forEach((key) => {
                value = (value && value[key] !== undefined) ? value[key] : null
              })
              // 特殊处理 nodeName
              if (column.dataLocation === 'nodeName') {
                const dataType = dataItem.dataType // 获取 dataType 值,来判断流程是否已完成或者已终止
                if (dataType === 'complete') {
                  value = '已完成'
                } else if (dataType === 'cancel') {
                  value = '已终止'
                } else {
                  // 映射 nodeName 值
                  value = taskStateMapping[value] || value
                }
              }
              item[column.dataIndex] = value
            }
          })
          return item
        })

        // 更新 dataSourceList
        this.dataSourceList = dataSourceList
        this.filteredDataSourceList = [...dataSourceList]

        // 去掉columnsData中的无用字段,生成columnsList,只有show为true的列才会显示
        const columnsList = this.configurationParameter.columnsData
          .filter((column) => column.show)
          .map((column) => {
            const { dataLocation, show, filterType, ...rest } = column
            return rest
          })
        // 更新 columnsList
        this.columnsList = columnsList
        console.log('dataSourceList:', this.dataSourceList)
        console.log('columnsList:', this.columnsList)

        // 生成 dataSourceList 后添加以下代码
        if (this.configurationParameter.filterFunction) {
          this.dataSourceList = this.configurationParameter.filterFunction(this.dataSourceList);
        }

        // 更新 filteredDataSourceList
        this.filteredDataSourceList = [...this.dataSourceList];
      } catch (error) {
        console.error('获取数据失败：', error)
      }
    },

    // 获取 select 下拉选项的方法
    getSelectOptions(dataIndex) {
      const column = this.columnsList.find(col => col.dataIndex === dataIndex);
      // 如果列标题是“状态”，返回固定选项
      if (column && column.title === '状态') {
        return ['全部', '进行中', '已完成', '已终止'];
      }
      // 否则动态生成选项
      const uniqueValues = new Set(this.dataSourceList.map((item) => item[dataIndex]));
      return Array.from(uniqueValues).filter((value) => value !== undefined && value !== null);
    },

    // 筛选方法
    async handleFilter() {
      try {
        await this.getAllList()
        // 在数据更新后执行筛选逻辑
        this.filteredDataSourceList = this.dataSourceList.filter((item) => {
          const basicFilter = Object.keys(this.filterConditions).every((key) => {
            const filterValue = this.filterConditions[key]
            if (!filterValue) return true // 如果没有筛选条件，默认不过滤

            const column = this.configurationParameter.columnsData.find((col) => col.dataIndex === key)
            const itemValue = item[key]

            if (column.filterType === 'input') {
              // 输入框模糊匹配
              return itemValue && itemValue.toString().includes(filterValue.toString())
            } else if (column.filterType === 'select') {
              // 当列标题为状态时特殊处理
              if (column.title && column.title === '状态') {
                if (filterValue === '已完成') {
                  return itemValue === '已完成';
                } else if (filterValue === '已终止') {
                  return itemValue === '已终止';
                } else if (filterValue === '全部') {
                  return true;
                } else if (filterValue === '进行中') {
                  return itemValue !== '已完成' && itemValue !== '已终止';
                }
              }
              // 下拉框完全匹配（非状态列的处理）
              return itemValue === filterValue
            } else if (column.filterType === 'date') {
              // 日期范围筛选
              if (!Array.isArray(filterValue) || filterValue.length !== 2) return true
              const [startDate, endDate] = filterValue
              const itemDate = new Date(itemValue).getTime()
              return itemDate >= new Date(startDate).getTime() && itemDate <= new Date(endDate).getTime()
            }

            return true // 如果没有匹配到类型，则默认不过滤
          })

          // 混合条件筛选
          const mixedFilter = this.mixedFilterColumns.some((column) => {
            const itemValue = item[column.dataIndex]
            return itemValue && itemValue.toString().toLowerCase().includes(this.mixedFilterValue.toLowerCase())
          })

          // 过滤出同时满足普通条件和混合筛选条件的数据
          return basicFilter && (!this.mixedFilterValue || mixedFilter)
        })

        if (this.configurationParameter.filterFunction) {
          this.filteredDataSourceList = this.configurationParameter.filterFunction(this.filteredDataSourceList);
        }

        console.log('筛选后的数据：', this.filteredDataSourceList)
      } catch (error) {
        console.error('获取数据失败：', error)
      }
    },
    // 清空筛选条件
    clearFilters() {
      this.filterConditions = {}
      this.mixedFilterValue = ''
      this.getAllList()
    },
  },

  mounted() {
    this.getAllList()
  },
}
</script>

<style scoped></style>