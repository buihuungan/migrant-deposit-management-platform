<template>
  <div class="form-config-container">
    <el-form label-position="top" size="small">
      <!-- <el-form-item :label="$t('fm.config.form.dataBase')"> -->
      <el-form-item label="数据库绑定">
        <el-select
          v-model="data.dataBase"
          style="width: 100%"
          @change="dataBaseChange"
          :showSearch="true"
          :popper-append-to-body="false"
        >
          <el-option v-for="item in tabledata" :key="item.id" :value="item.tableName" @click.native="checkkey(item.id)">
            {{ item.tableTxt + '(' + item.tableName + ')' }}
          </el-option>
        </el-select>
        {{ data.tableId }}
      </el-form-item>
      
      <el-form-item label="UI">
        <el-radio-group v-model="data.ui">
          <el-radio-button label="element">Element</el-radio-button>
          <el-radio-button label="antd">Ant Design</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.form.width')"> -->
      <el-form-item label="表单宽度">
        <el-input v-model="data.width" clearable></el-input>
      </el-form-item>

      <template v-if="data.ui == 'antd'">
        <!-- <el-form-item :label="$t('fm.config.form.labelPosition.title')"> -->
        <el-form-item label="标签对齐方式">
          <el-radio-group v-model="data.layout">
            <!-- <el-radio-button label="horizontal">{{ $t('fm.config.form.labelPosition.right') }}</el-radio-button>
            <el-radio-button label="vertical">{{ $t('fm.config.form.labelPosition.top') }}</el-radio-button> -->
            <el-radio-button label="horizontal">右对齐</el-radio-button>
            <el-radio-button label="vertical">顶部对齐</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.form.labelWidth')"> -->
        <el-form-item label="表单标签宽度">
          <el-slider v-model="data.labelCol" :min="0" :max="24" :step="1"></el-slider>
        </el-form-item>
      </template>

      <template v-else>
        <!-- <el-form-item :label="$t('fm.config.form.labelPosition.title')"> -->
        <el-form-item label="标签对齐方式">
          <el-radio-group v-model="data.labelPosition" :key="data.labelPosition">
            <!-- <el-radio-button label="left">{{ $t('fm.config.form.labelPosition.left') }}</el-radio-button>
            <el-radio-button label="right">{{ $t('fm.config.form.labelPosition.right') }}</el-radio-button>
            <el-radio-button label="top">{{ $t('fm.config.form.labelPosition.top') }}</el-radio-button> -->
            <el-radio-button label="left">左对齐</el-radio-button>
            <el-radio-button label="right">右对齐</el-radio-button>
            <el-radio-button label="top">顶部对齐</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.form.labelWidth')"> -->
        <el-form-item label="表单标签宽度">
          <el-input-number v-model="data.labelWidth" :min="0" :max="200" :step="10"></el-input-number>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.labelWidth')" v-if="Object.keys(data.options).indexOf('labelWidth')>=0 && !data.options.tableColumn">
          <el-checkbox v-model="data.options.isLabelWidth" style="margin-right: 5px;">{{$t('fm.config.widget.custom')}}</el-checkbox>
          <el-input-number v-model="data.options.labelWidth" :disabled="!data.options.isLabelWidth" :min="0" :max="99999" :step="10"></el-input-number>
        </el-form-item> -->

        <!-- <el-form-item :label="$t('fm.config.form.size')"> -->
        <el-form-item label="组件尺寸">
          <el-radio-group v-model="data.size">
            <el-radio-button label="medium">medium</el-radio-button>
            <el-radio-button label="small">small</el-radio-button>
            <el-radio-button label="mini">mini</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </template>

      <!-- <el-form-item :label="$t('fm.config.form.customClass')"> -->
      <el-form-item label="自定义Class">
        <el-input v-model="data.customClass" clearable></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
// import { filterObj } from '@/utils/util';
import { o_getAction } from '@/api/onApi'

// import axios from 'axios'
export default {
  props: ['widgetForm', 'data'],
  data() {
    return {
      tabledata: [],
      tableTypeDictOptions: [],
      queryParam: {},
      fromconfigtableid: '',
    }
  },
  created() {
    this.loadDataBase();
  },

  methods: {
    dataBaseChange() {
      // 触发数据库绑定改变函数
      console.log("before",this.widgetForm);
      this.clearExistingInfo();
      console.log("after",this.widgetForm);

      console.log('dataBaseChange函数输出this.data值为', this.data)
      // this.data.tableId = ''
      // this.getScheduleData(this.data.tableId);
    },

    // 未做功能：需递归清空已绑信息  2020.10.26
    clearExistingInfo(){
      if (this.widgetForm) {
        for (let i = 0; i < this.widgetForm.list.length; i++) {
          if(this.widgetForm.list[i].type=='table'){
            for(let j = 0; j < this.widgetForm.list[i].tableColumns.length; j++){
               this.widgetForm.list[i].tableColumns[j].tableCol = '';
               this.widgetForm.list[i].tableColumns[j].tableColId = '';
            }
          }
          this.widgetForm.list[i].tableCol = '';
          this.widgetForm.list[i].schedule = ''
        }
      }
    },
    // 获取附表数据
    // getScheduleData(currentTd){
    //   o_getAction(`/cgform/field/listByHeadId?headId=${currentTd}`)
    //       .then((res) => {
    //         console.log("附表数据",res)
    //         // this.$store.commit('changeTableCols', res.result)
    //         // console.log(this.$store.state.tableCols)
    //       })
    //       .catch((err) => {
    //         console.log('表单属性界面获取数据表字段属性失败！')
    //       })
    // },
    getQueryParams() {
      //获取查询条件
      var param = Object.assign({}, this.queryParam, this.isorter, this.filters)
      param.pageNo = 1
      param.pageSize = 9999
      param.copyType = 0
      param.field = 'id,tableName,tableTxt'
      return param
    },
    async loadDataBase() {
      await o_getAction(`/cgform/head/list`, { pageSize: 9999 })
        .then((res) => {
          this.tabledata = res.result.records;
          this.$store.commit('changeDataBaseList', res.result.records);
          // 初始化附表下拉框数据
          this.queryByTableNames(this.data.dataBase);
        })
        .catch((err) => {
          console.log('请求数据库错误！')
        })
    },
    async checkkey(id) {
      console.log('checkkey函数所选表的id为=' + id, this.data.tableId)
      this.data.tableId = id
      if (id !== '') {
        /* if开始 */
        // 获取主表的字段
        await o_getAction(`/cgform/field/listByHeadId?headId=${id}`)
          .then((res) => {
            this.$store.commit('changeTableCols', res.result)
            // this.data.dataBase是当前选中的主表的名字
            this.queryByTableNames(this.data.dataBase)
          })
          .catch((err) => {
            console.log('表单属性界面获取数据表字段属性失败！')
          })
      } else {
        console.log('没有选择数据表，暂未加载数据表字段！')
      } /* if结束 */
    },
    queryByTableNames(currentTName) {
      this.$store.commit('changeSecondTableCols','')
      // console.log("queryByTableNames的参数值currentTName值为",currentTName)
      o_getAction(`/cgform/head/queryByTableNames?tableNames=${currentTName}`)
        .then((res) => {
          // console.log("queryByTableNames函数接口res",res)
          
          if (res.result.length > 0){
            if(res.result[0].subTableStr){
              this.$store.commit('changeSecondTableCols', res.result[0].subTableStr.split(','));

            }
            // else{
            //   console.log("该主表没有附表");
            // }

          }
            
        })
        .catch((err) => {
          console.log(err)
        })
    },
  },
  watch: {
    'data.layout': function (val) {
      if (this.data.ui == 'antd') {
        if (val == 'horizontal' || !val) {
          this.data.labelPosition = 'right'
        } else {
          this.data.labelPosition = 'top'
        }
      }
    },
    'data.labelPosition': function (val) {
      if (val == 'top') {
        this.data.layout = 'vertical'
        this.data.labelCol = 0
      } else {
        this.data.layout = 'horizontal'
        this.data.labelCol = 3
      }
    },
    'data.dataBase': function (val) {
      console.log('data.dataBase监听函数打印传入val值为：', val)
      console.log('监听到（数据库绑定）发生变化')

      //下面四条语句有bug一直输出为空
      // let self = this
      // console.log('watch监听输出this.data.tableId为：', this.data.tableId)
      // console.log('watch监听输出self.data.tableId为：', self.data.tableId)
      // console.log('watch监听输出this.widgetForm.config.tableId为：', self.widgetForm.config.tableId)
    },
    'data.button': function (val) {
      console.log('data.button监听函数打印传入val值为：', val)
      console.log('监听到（数据库绑定）发生变化')
    },
  },
}
</script>
<style>
/* .form-config-container .el-select-dropdown {
  position: absolute !important;
  top: 30px !important;
  left: 0px !important;
} */
</style>
