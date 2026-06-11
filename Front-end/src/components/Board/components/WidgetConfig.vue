<template>
  <div v-if="show">
    <el-form label-position="top" size="small">
      <!-- <el-form-item :label="$t('fm.config.widget.model')"> -->
      <el-form-item label="字段标识">
        <el-input v-model="data.model" :disabled="true"> </el-input>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.schedule')" v-if="data.type == 'table'"> -->
      <el-form-item label="附表绑定" v-if="data.type == 'table'">
        <el-select v-model="data.schedule" style="width: 100%" @change="scheduleChange" :showSearch="true" :popper-append-to-body="false">
          <!-- @click.native="scheduleClick" -->

          <el-option v-for="item in scheduleFilter" :key="item.id" :value="item.tableName" @click.native="checkkey(item.id)">
            {{ item.tableTxt + '(' + item.tableName + ')' }}
          </el-option>
        </el-select>
        {{ data.tableId }}
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.tableCol')" v-if="data.type != 'table'"> -->
      <el-form-item label="字段绑定" v-if="data.type != 'table'">
        <el-select v-model="data.tableCol" @change="tableColChange" filterable :popper-append-to-body="false" style="width: 100%">
          <el-option v-for="(item, index) in tableColsFilter" :key="index" :value="item.dbFieldName" :label="item.dbFieldTxt + '(' + item.dbFieldName + ')'" >
            <span v-show="bindingDisplay(item.dbFieldName)" style="color: red">(已绑定)</span>
            <!-- 档案id(archive_id)(String) -->
            <span>{{ item.dbFieldTxt + '(' + item.dbFieldName + ')' + '(' + item.dbType + ')' }}</span>
          </el-option>
        </el-select>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.isIdentified')"> -->
      <el-form-item label="是否标识">
        <el-switch v-model="data.isIdentified" active-text="是" inactive-text="否"> </el-switch>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.name')" v-if="data.type != 'grid' && data.type != 'tabs'"> -->
      <el-form-item label="标题" v-if="data.type != 'grid' && data.type != 'tabs'">
        <el-input v-model="data.name"></el-input>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.width')" v-if="Object.keys(data.options).indexOf('width') >= 0"> -->
      <el-form-item label="宽度" v-if="Object.keys(data.options).indexOf('width') >= 0">
        <el-input v-model="data.options.width"></el-input>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.height')" v-if="Object.keys(data.options).indexOf('height') >= 0"> -->
      <el-form-item label="高度" v-if="Object.keys(data.options).indexOf('height') >= 0">
        <el-input v-model="data.options.height"></el-input>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.size')" v-if="Object.keys(data.options).indexOf('size') >= 0"> -->
      <el-form-item label="大小" v-if="Object.keys(data.options).indexOf('size') >= 0">
        <!-- {{ $t('fm.config.widget.width') }} -->
        宽度
        <el-input style="width: 90px" type="number" v-model.number="data.options.size.width"></el-input>
        <!-- {{ $t('fm.config.widget.height') }} -->
        高度
        <el-input style="width: 90px" type="number" v-model.number="data.options.size.height"></el-input>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.echarttitle')" v-if="data.options.typeId&&data.options.typeId!='4001'"> -->
      <el-form-item label="组件名称" v-if="data.options.typeId&&data.options.typeId!='4001'">
        <el-input v-model="data.options.defaultValue.title.text"></el-input>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.labelWidth')" v-if="Object.keys(data.options).indexOf('labelWidth') >= 0 && !data.options.tableColumn"> -->
      <el-form-item label="标签宽度" v-if="Object.keys(data.options).indexOf('labelWidth') >= 0 && !data.options.tableColumn">
        <el-checkbox v-model="data.options.isLabelWidth" style="margin-right: 5px">
          <!-- {{ $t('fm.config.widget.custom') }} -->
          自定义
        </el-checkbox>
        <el-input-number v-model="data.options.labelWidth" :disabled="!data.options.isLabelWidth" :min="0" :max="99999" :step="10"></el-input-number>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.height')" v-if="data.options.typeId"> -->
      <el-form-item label="高度" v-if="data.options.typeId">
        <el-input v-model="data.options.style.height"></el-input>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.echartdataname')" v-if="data.options.typeId"> -->
      <el-form-item label="组件数据类型" v-if="data.options.typeId">
        <el-radio-group v-model="echartdataType">
          <el-radio :label="1"> 静态数据 </el-radio>
          <el-radio :label="2"> URL数据 </el-radio>
          <el-radio :label="3"> SQL数据 </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="数据值" v-if="echartdataType == 1&&data.options.typeId">
        <el-button>编辑</el-button>
      </el-form-item>

      <el-form-item label="接口地址" v-if="echartdataType == 2&&data.options.typeId">
        <el-input placeholder="请输入接口的url地址" type="textarea" />
      </el-form-item>

      <el-form-item label="接口方式" v-if="echartdataType == 2&&data.options.typeId">
        <el-radio-group v-model="urlType">
          <el-radio :label="1"> post </el-radio>
          <el-radio :label="2"> get </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="接口参数" v-if="echartdataType == 2&&data.options.typeId">
        <el-button>编辑</el-button>
      </el-form-item>

      <el-form-item label="SQL配置" v-if="echartdataType == 3&&data.options.typeId">
        <el-button @click="sqlDataV">编辑</el-button>
      </el-form-item>

      <el-form-item label="平滑" v-if="data.options.typeId=='1001'">
        <el-switch v-model="data.options.defaultValue.series[0].smooth">
        </el-switch>
      </el-form-item>
      <!-- <el-form-item label="面积" v-if="data.options.typeId=='1001'">

      </el-form-item> -->

      <!-- <el-form-item :label="$t('fm.config.widget.placeholder')" v-if="Object.keys(data.options).indexOf('placeholder') >= 0 && data.type != 'time' && data.type != 'date'"> -->
      <el-form-item label="占位内容" v-if="Object.keys(data.options).indexOf('placeholder') >= 0 && data.type != 'time' && data.type != 'date'">
        <el-input v-model="data.options.placeholder"></el-input>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.layout')" v-if="Object.keys(data.options).indexOf('inline') >= 0"> -->
      <el-form-item label="布局方式" v-if="Object.keys(data.options).indexOf('inline') >= 0">
        <el-radio-group v-model="data.options.inline">
          <!-- <el-radio-button :label="false">{{ $t('fm.config.widget.block') }}</el-radio-button>
          <el-radio-button :label="true">{{ $t('fm.config.widget.inline') }}</el-radio-button> -->
          <el-radio-button :label="false">块级</el-radio-button>
          <el-radio-button :label="true">行内</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.contentPosition')" v-if="Object.keys(data.options).indexOf('contentPosition') >= 0"> -->
      <el-form-item label="文案位置" v-if="Object.keys(data.options).indexOf('contentPosition') >= 0">
        <el-radio-group v-model="data.options.contentPosition">
          <!-- <el-radio-button label="left">{{ $t('fm.config.widget.left') }}</el-radio-button>
          <el-radio-button label="center">{{ $t('fm.config.widget.center') }}</el-radio-button>
          <el-radio-button label="right">{{ $t('fm.config.widget.right') }}</el-radio-button> -->
          <el-radio-button label="left">左侧</el-radio-button>
          <el-radio-button label="center">居中</el-radio-button>
          <el-radio-button label="right">右侧</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.showInput')" v-if="Object.keys(data.options).indexOf('showInput') >= 0"> -->
      <el-form-item label="显示输入框" v-if="Object.keys(data.options).indexOf('showInput') >= 0">
        <el-switch v-model="data.options.showInput"></el-switch>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.min')" v-if="Object.keys(data.options).indexOf('min') >= 0"> -->
      <el-form-item label="最小值" v-if="Object.keys(data.options).indexOf('min') >= 0">
        <el-input-number v-model="data.options.min" :min="-99999" :max="99999" :step="1"></el-input-number>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.max')" v-if="Object.keys(data.options).indexOf('max') >= 0"> -->
      <el-form-item label="最大值" v-if="Object.keys(data.options).indexOf('max') >= 0">
        <el-input-number v-model="data.options.max" :min="-99999" :max="99999" :step="1"></el-input-number>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.step')" v-if="Object.keys(data.options).indexOf('step') >= 0"> -->
      <el-form-item label="步长" v-if="Object.keys(data.options).indexOf('step') >= 0">
        <el-input-number v-model="data.options.step" :min="-99999" :max="99999" :step="1"></el-input-number>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.multiple')" v-if="data.type == 'select' || data.type == 'imgupload' || data.type == 'fileupload'"> -->
      <el-form-item label="是否多选" v-if="data.type == 'select' || data.type == 'imgupload' || data.type == 'fileupload'">
        <el-switch v-model="data.options.multiple" @change="handleSelectMuliple"></el-switch>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.filterable')" v-if="data.type == 'select'"> -->
      <el-form-item label="是否可搜索" v-if="data.type == 'select'">
        <el-switch v-model="data.options.filterable"></el-switch>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.allowHalf')" v-if="Object.keys(data.options).indexOf('allowHalf') >= 0"> -->
      <el-form-item label="允许半选" v-if="Object.keys(data.options).indexOf('allowHalf') >= 0">
        <el-switch v-model="data.options.allowHalf"> </el-switch>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.showAlpha')" v-if="Object.keys(data.options).indexOf('showAlpha') >= 0"> -->
      <el-form-item label="支持透明度选择" v-if="Object.keys(data.options).indexOf('showAlpha') >= 0">
        <el-switch v-model="data.options.showAlpha"> </el-switch>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.showLabel')" v-if="Object.keys(data.options).indexOf('showLabel') >= 0"> -->
      <el-form-item label="是否显示标签" v-if="Object.keys(data.options).indexOf('showLabel') >= 0">
        <el-switch v-model="data.options.showLabel"> </el-switch>
      </el-form-item>
      <!-- <el-form-item :label="$t('fm.config.widget.option')" v-if="Object.keys(data.options).indexOf('options') >= 0"> -->
      <el-form-item label="选项" v-if="Object.keys(data.options).indexOf('options') >= 0">
        <el-radio-group v-model="data.options.remote" size="mini" style="margin-bottom: 10px">
          <!-- <el-radio-button :label="false">{{ $t('fm.config.widget.staticData') }}</el-radio-button>
          <el-radio-button :label="true">{{ $t('fm.config.widget.remoteData') }}</el-radio-button> -->
          <el-radio-button :label="false">静态数据</el-radio-button>
          <el-radio-button :label="true">动态数据</el-radio-button>
        </el-radio-group>
        <template v-if="data.options.remote">
          <div>
            <el-radio-group v-model="data.options.remoteType">
              <!-- <el-radio label="option">{{ $t('fm.config.widget.remoteAssigned') }}</el-radio>
              <el-radio label="func">{{ $t('fm.config.widget.remoteFunc') }}</el-radio> -->
              <el-radio label="option">赋值变量</el-radio>
              <el-radio label="func">方法函数</el-radio>
            </el-radio-group>
            <el-input size="mini" v-if="data.options.remoteType == 'option'" v-model="data.options.remoteOption" style="margin-bottom: 5px">
            </el-input>
            <el-input size="mini" v-if="data.options.remoteType == 'func'" v-model="data.options.remoteFunc" style="margin-bottom: 5px">
            </el-input>
            <el-input size="mini" v-model="data.options.props.value">
              <!-- <div style="width: 30px" slot="prepend">{{ $t('fm.config.widget.value') }}</div> -->
              <div style="width: 30px" slot="prepend">值</div>
            </el-input>
            <el-input size="mini" v-model="data.options.props.label">
              <!-- <div slot="prepend" style="width: 30px">{{ $t('fm.config.widget.label') }}</div> -->
              <div slot="prepend" style="width: 30px">标签</div>
            </el-input>
          </div>
        </template>
        <template v-else>
          <template v-if="data.type == 'radio' || (data.type == 'select' && !data.options.multiple)">
            <el-radio-group v-model="data.options.defaultValue">
              <draggable tag="ul" :list="data.options.options" v-bind="{ group: { name: 'options' }, ghostClass: 'ghost', handle: '.drag-item' }" handle=".drag-item">
                <li v-for="(item, index) in data.options.options" :key="index">
                  <el-radio :label="item.value" style="margin-right: 5px">
                    <el-input :style="{ width: data.options.showLabel ? '90px' : '180px' }" size="mini" v-model="item.value"></el-input>
                    <el-input style="width: 90px" size="mini" v-if="data.options.showLabel" v-model="item.label"></el-input>
                    <!-- <input v-model="item.value"/> -->
                  </el-radio>
                  <i class="drag-item" style="font-size: 16px; margin: 0 5px; cursor: move"><i class="iconfont icon-icon_bars"></i></i>
                  <el-button @click="handleOptionsRemove(index)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px; margin-left: 5px"></el-button>
                </li>
              </draggable>
            </el-radio-group>
          </template>

          <template v-if="data.type == 'checkbox' || (data.type == 'select' && data.options.multiple)">
            <el-checkbox-group v-model="data.options.defaultValue">
              <draggable tag="ul" :list="data.options.options" v-bind="{ group: { name: 'options' }, ghostClass: 'ghost', handle: '.drag-item' }" handle=".drag-item">
                <li v-for="(item, index) in data.options.options" :key="index">
                  <el-checkbox :label="item.value" style="margin-right: 5px">
                    <el-input :style="{ width: data.options.showLabel ? '90px' : '180px' }" size="mini" v-model="item.value"></el-input>
                    <el-input style="width: 90px" size="mini" v-if="data.options.showLabel" v-model="item.label"></el-input>
                  </el-checkbox>
                  <i class="drag-item" style="font-size: 16px; margin: 0 5px; cursor: move"><i class="iconfont icon-icon_bars"></i></i>
                  <el-button @click="handleOptionsRemove(index)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px; margin-left: 5px"></el-button>
                </li>
              </draggable>
            </el-checkbox-group>
          </template>
          <div style="margin-left: 22px">
            <!-- <el-button type="text" @click="handleAddOption">{{ $t('fm.actions.addOption') }}</el-button> -->
            <el-button type="text" @click="handleAddOption">添加选项</el-button>
          </div>
        </template>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.remoteData')" v-if="data.type == 'cascader'"> -->
      <el-form-item label="动态数据" v-if="data.type == 'cascader'">
        <div>
          <el-radio-group v-model="data.options.remoteType">
            <!-- <el-radio label="option">{{ $t('fm.config.widget.remoteAssigned') }}</el-radio>
            <el-radio label="func">{{ $t('fm.config.widget.remoteFunc') }}</el-radio> -->
            <el-radio label="option">赋值变量</el-radio>
            <el-radio label="func">方法函数</el-radio>
          </el-radio-group>
          <el-input size="mini" v-if="data.options.remoteType == 'option'" v-model="data.options.remoteOption" style="margin-bottom: 5px">
          </el-input>
          <el-input size="mini" v-if="data.options.remoteType == 'func'" v-model="data.options.remoteFunc" style="margin-bottom: 5px">
          </el-input>
          <el-input size="mini" style="" v-model="data.options.props.value">
            <!-- <template slot="prepend">{{ $t('fm.config.widget.value') }}</template> -->
            <template slot="prepend">值</template>
          </el-input>
          <el-input size="mini" style="" v-model="data.options.props.label">
            <!-- <template slot="prepend">{{ $t('fm.config.widget.label') }}</template> -->
            <template slot="prepend">标签</template>
          </el-input>
          <el-input size="mini" style="" v-model="data.options.props.children">
            <!-- <template slot="prepend">{{ $t('fm.config.widget.childrenOption') }}</template> -->
            <template slot="prepend">子选项</template>
          </el-input>
        </div>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.remoteData')" v-if="data.type == 'pcdcascader'"> -->
      <el-form-item label="动态数据" v-if="data.type == 'pcdcascader'">
        <div>
          <el-radio-group v-model="data.options.remoteType">
            <!-- <el-radio label="option">{{ $t('fm.config.widget.remoteAssigned') }}</el-radio>
            <el-radio label="func">{{ $t('fm.config.widget.remoteFunc') }}</el-radio> -->
            <el-radio label="option">赋值变量</el-radio>
            <el-radio label="func">方法函数</el-radio>
          </el-radio-group>
          <el-input size="mini" v-if="data.options.remoteType == 'option'" v-model="data.options.remoteOption" style="margin-bottom: 5px">
          </el-input>
          <el-input size="mini" v-if="data.options.remoteType == 'func'" v-model="data.options.remoteFunc" style="margin-bottom: 5px">
          </el-input>
          <el-input size="mini" style="" v-model="data.options.props.value">
            <!-- <template slot="prepend">{{ $t('fm.config.widget.value') }}</template> -->
            <template slot="prepend">值</template>
          </el-input>
          <el-input size="mini" style="" v-model="data.options.props.label">
            <!-- <template slot="prepend">{{ $t('fm.config.widget.label') }}</template> -->
            <template slot="prepend">标签</template>
          </el-input>
          <el-input size="mini" style="" v-model="data.options.props.children">
            <!-- <template slot="prepend">{{ $t('fm.config.widget.childrenOption') }}</template> -->
            <template slot="prepend">子选项</template>
          </el-input>
        </div>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.defaultValue')" v-if=" -->
      <el-form-item label="默认值" v-if="
          Object.keys(data.options).indexOf('defaultValue') >= 0 &&
          (data.type == 'text' ||
            data.type == 'textarea' ||
            data.type == 'input' ||
            data.type == 'number' ||
            data.type == 'rate' ||
            data.type == 'color' ||
            data.type == 'switch' ||
            data.type == 'html')
        ">
        <el-input v-if="data.type == 'textarea'" type="textarea" :rows="5" v-model="data.options.defaultValue"></el-input>

        <template v-if="data.type == 'input' || data.type == 'text'">
          <template v-if="
              data.options.dataType == 'number' ||
              data.options.dataType == 'integer' ||
              data.options.dataType == 'float'
            ">
            <el-input type="number" v-model.number="data.options.defaultValue"></el-input>
          </template>
          <template v-else>
            <el-input v-model="data.options.defaultValue"></el-input>
          </template>
        </template>

        <el-rate v-if="data.type == 'rate'" style="display: inline-block; vertical-align: middle" :max="data.options.max" :allow-half="data.options.allowHalf" v-model="data.options.defaultValue"></el-rate>
        <!-- <el-button type="text" v-if="data.type == 'rate'" style="display: inline-block; vertical-align: middle; margin-left: 10px" @click="data.options.defaultValue = 0">{{ $t('fm.actions.clear') }}</el-button> -->
        <el-button type="text" v-if="data.type == 'rate'" style="display: inline-block; vertical-align: middle; margin-left: 10px" @click="data.options.defaultValue = 0">清空</el-button>
        <el-color-picker v-if="data.type == 'color'" v-model="data.options.defaultValue" :show-alpha="data.options.showAlpha"></el-color-picker>
        <el-switch v-if="data.type == 'switch'" v-model="data.options.defaultValue"></el-switch>
        <el-input-number v-if="data.type == 'number'" v-model="data.options.defaultValue" :step="data.options.step" :min="data.options.min" :max="data.options.max"></el-input-number>

        <template v-if="data.type == 'html'">
          <code-editor :key="data.key" v-model="data.options.defaultValue" height="200px"></code-editor>
        </template>
      </el-form-item>

      <template v-if="data.type == 'time' || data.type == 'date'">
        <!-- 时间选择器或者日期选择器 -->
        <!-- <el-form-item :label="$t('fm.config.widget.showType')" v-if="data.type == 'date'"> -->
        <el-form-item label="显示类型" v-if="data.type == 'date'">
          <el-select v-model="data.options.type">
            <el-option value="year"></el-option>
            <el-option value="month"></el-option>
            <el-option value="date"></el-option>
            <el-option value="dates"></el-option>
            <!-- <el-option value="week"></el-option> -->
            <el-option value="datetime"></el-option>
            <el-option value="datetimerange"></el-option>
            <el-option value="daterange"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.isRange')" v-if="data.type == 'time'"> -->
        <el-form-item label="是否为范围选择" v-if="data.type == 'time'">
          <!-- 是否为范围选择 -->
          <el-switch v-model="data.options.isRange"> </el-switch>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.isTimestamp')" v-if="data.type == 'date'"> -->
        <el-form-item label="是否获取时间戳" v-if="data.type == 'date'">
          <!-- 是否获取时间戳 -->
          <el-switch v-model="data.options.timestamp"> </el-switch>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.placeholder')" v-if=" -->
        <el-form-item label="占位内容" v-if="
            (!data.options.isRange && data.type == 'time') ||
            (data.type != 'time' && data.options.type != 'datetimerange' && data.options.type != 'daterange')
          ">
          <el-input v-model="data.options.placeholder"></el-input><!-- 占位内容 -->
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.startPlaceholder')" v-if="data.options.isRange || data.options.type == 'datetimerange' || data.options.type == 'daterange'"> -->
        <el-form-item label="开始时间占位内容" v-if="data.options.isRange || data.options.type == 'datetimerange' || data.options.type == 'daterange'">
          <el-input v-model="data.options.startPlaceholder"></el-input><!-- 开始时间占位内容 -->
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.endPlaceholder')" v-if="data.options.isRange || data.options.type == 'datetimerange' || data.options.type == 'daterange'"> -->
        <el-form-item label="结束时间占位内容" v-if="data.options.isRange || data.options.type == 'datetimerange' || data.options.type == 'daterange'">
          <el-input v-model="data.options.endPlaceholder"></el-input><!-- 结束时间占位内容 -->
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.format')"> -->
        <el-form-item label="格式">
          <!-- 格式 -->
          <el-input v-model="data.options.format"></el-input>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.defaultValue')" v-if="data.type == 'time' && Object.keys(data.options).indexOf('isRange') >= 0"> -->
        <el-form-item label="默认值" v-if="data.type == 'time' && Object.keys(data.options).indexOf('isRange') >= 0">
          <!-- 默认值 -->
          <!-- 时间选择器 -->
          <el-time-picker key="1" style="width: 100%" v-if="!data.options.isRange" v-model="data.options.defaultValue" :arrowControl="data.options.arrowControl" :value-format="data.options.format">
          </el-time-picker>
          <el-time-picker key="2" v-if="data.options.isRange" style="width: 100%" v-model="data.options.defaultValue" is-range :arrowControl="data.options.arrowControl" :value-format="data.options.format">
          </el-time-picker>
        </el-form-item>
      </template>

      <template v-if="data.type == 'imgupload' || data.type == 'fileupload'">
        <!-- <el-form-item :label="$t('fm.config.widget.limit')"> -->
        <el-form-item label="最大上传数">
          <el-input type="number" v-model.number="data.options.limit"></el-input>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.tip')" v-if="Object.keys(data.options).indexOf('tip') >= 0"> -->
        <el-form-item label="提示说明文字" v-if="Object.keys(data.options).indexOf('tip') >= 0">
          <el-input v-model="data.options.tip"></el-input>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.isQiniu')"> -->
        <el-form-item label="使用七牛上传">
          <el-switch v-model="data.options.isQiniu"></el-switch>
        </el-form-item>
        <template v-if="data.options.isQiniu">
          <el-form-item label="Domain" :required="true">
            <el-input v-model="data.options.domain"></el-input>
          </el-form-item>
          <!-- <el-form-item :label="$t('fm.config.widget.tokenFunc')" :required="true"> -->
          <el-form-item label="获取七牛Token方法" :required="true">
            <el-input v-model="data.options.tokenFunc"></el-input>
          </el-form-item>
        </template>
        <template v-else>
          <!-- <el-form-item :label="$t('fm.config.widget.action')" :required="true"> -->
          <el-form-item label="上传地址" :required="true">
            <el-input v-model="data.options.action"></el-input>
          </el-form-item>
          <!-- <el-form-item :label="$t('fm.config.widget.headers')"> -->
          <el-form-item label="设置上传的请求头部">
            <ul>
              <li v-for="(item, index) in data.options.headers" :key="index" style="margin-bottom: 5px">
                <el-input type="textarea" clearable :rows="1" placeholder="KEY" size="mini" style="width: 100px; margin-right: 5px" v-model="item.key"></el-input>

                <el-input type="textarea" clearable :rows="1" placeholder="VALUE" size="mini" style="width: 130px" v-model="item.value"></el-input>

                <el-button @click="handleOptionsRemove(index)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px; margin-left: 5px"></el-button>
              </li>
            </ul>
            <div>
              <!-- <el-button type="text" @click="handleAddHeader">{{ $t('fm.actions.add') }}</el-button> -->
              <el-button type="text" @click="handleAddHeader">添加</el-button>
            </div>
          </el-form-item>
        </template>
      </template>

      <template v-if="data.type == 'blank'">
        <!-- <el-form-item :label="$t('fm.config.widget.defaultType')"> -->
        <el-form-item label="$t('fm.config.widget.defaultType')">
          <el-select v-model="data.options.defaultType">
            <!-- <el-option value="String" :label="$t('fm.config.widget.string')"></el-option>
            <el-option value="Object" :label="$t('fm.config.widget.object')"></el-option>
            <el-option value="Array" :label="$t('fm.config.widget.array')"></el-option> -->
            <el-option value="String" label="字符串"></el-option>
            <el-option value="Object" label="对象"></el-option>
            <el-option value="Array" label="数组"></el-option>
          </el-select>
        </el-form-item>
      </template>

      <template v-if="data.type == 'component'">
        <!-- <el-form-item :label="$t('fm.config.widget.customTemplates')"> -->
        <el-form-item label="自定义模板">
          <!-- <code-editor :key="data.key" mode="html" v-model="data.options.template" height="200px"></code-editor> -->
          <!-- <el-button style="width: 100%" @click="handleSetTemplate">{{ $t('fm.config.widget.setting') }}</el-button> -->
          <el-button style="width: 100%" @click="handleSetTemplate">设置</el-button>
        </el-form-item>

        <code-dialog ref="codeDialog" mode="html" @on-confirm="handleTemplateConfirm"></code-dialog>
      </template>

      <template v-if="data.type == 'grid'">
        <!-- <el-form-item :label="$t('fm.config.widget.gutter')"> -->
        <el-form-item label="栅格间隔">
          <el-input type="number" v-model.number="data.options.gutter"></el-input>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.flex')"> -->
        <el-form-item label="Flex布局">
          <el-switch v-model="data.options.flex"></el-switch>
        </el-form-item>
        <!-- responsive -->
        <!-- <el-form-item :label="$t('fm.config.widget.responsive')"> -->
        <el-form-item label="响应式布局">
          <el-switch v-model="data.options.responsive"></el-switch>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.columnOption')"> -->
        <el-form-item label="列配置项">
          <draggable tag="ul" :list="data.columns" v-bind="{ group: { name: 'options' }, ghostClass: 'ghost', handle: '.drag-item' }" handle=".drag-item">
            <li v-for="(item, index) in data.columns" :key="index">
              <i class="drag-item" style="font-size: 16px; margin: 0 5px; cursor: move"><i class="iconfont icon-icon_bars"></i></i>

              <!-- <el-input v-if="!data.options.responsive" :placeholder="$t('fm.config.widget.span')" size="mini" style="width: 100px" type="number" v-model.number="item.span"></el-input> -->
              <el-input v-if="!data.options.responsive" placeholder="栅格值" size="mini" style="width: 100px" type="number" v-model.number="item.span"></el-input>

              <template v-if="data.options.responsive">
                <div style="display: inline-block">
                  <div style="display: flex; flex-direction: column; margin: 0 10px 10px 10px">
                    <span>
                      <span style="width: 25px; display: inline-block">xs: </span>
                      <el-input placeholder="xs" size="mini" style="width: 100px" type="number" v-model.number="item.xs"></el-input>
                      <pre style="width: 55px; display: inline; color: #999; margin-left: 5px" v-html="'<768px'"></pre>
                    </span>
                    <span>
                      <span style="width: 25px; display: inline-block">sm: </span>
                      <el-input placeholder="sm" size="mini" style="width: 100px" type="number" v-model.number="item.sm"></el-input>
                      <pre style="width: 55px; display: inline; color: #999; margin-left: 5px" v-html="'≥768px'"></pre>
                    </span>
                    <span>
                      <span style="width: 25px; display: inline-block">md: </span>
                      <el-input placeholder="md" size="mini" style="width: 100px" type="number" v-model.number="item.md"></el-input>
                      <pre style="width: 55px; display: inline; color: #999; margin-left: 5px" v-html="'≥992px'"></pre>
                    </span>
                    <span>
                      <span style="width: 25px; display: inline-block">lg: </span>
                      <el-input placeholder="lg" size="mini" style="width: 100px" type="number" v-model.number="item.lg"></el-input>
                      <pre style="width: 55px; display: inline; color: #999; margin-left: 5px" v-html="'≥1200px'"></pre>
                    </span>
                    <span>
                      <span style="width: 25px; display: inline-block">xl: </span>
                      <el-input placeholder="xl" size="mini" style="width: 100px" type="number" v-model.number="item.xl"></el-input>
                      <pre style="width: 55px; display: inline; color: #999; margin-left: 5px" v-html="'≥1920px'"></pre>
                    </span>
                  </div>
                </div>
              </template>

              <el-button @click="handleOptionsRemove(index)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px; margin-left: 5px"></el-button>
            </li>
          </draggable>
          <div style="margin-left: 22px">
            <!-- <el-button type="text" @click="handleAddColumn">{{ $t('fm.actions.addColumn') }}</el-button> -->
            <el-button type="text" @click="handleAddColumn">添加列</el-button>
          </div>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.justify')" v-if="data.options.flex"> -->
        <el-form-item label="水平排列方式" v-if="data.options.flex">
          <el-select v-model="data.options.justify">
            <!-- <el-option value="start" :label="$t('fm.config.widget.justifyStart')"></el-option> -->
            <!-- <el-option value="end" :label="$t('fm.config.widget.justifyEnd')"></el-option> -->
            <!-- <el-option value="center" :label="$t('fm.config.widget.justifyCenter')"></el-option> -->
            <!-- <el-option value="space-around" :label="$t('fm.config.widget.justifySpaceAround')"></el-option> -->
            <!-- <el-option value="space-between" :label="$t('fm.config.widget.justifySpaceBetween')"></el-option> -->
            <el-option value="start" label="左对齐"></el-option>
            <el-option value="end" label="右对齐"></el-option>
            <el-option value="center" label="居中"></el-option>
            <el-option value="space-around" label="两侧间隔相等"></el-option>
            <el-option value="space-between" label="两端对齐"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.align')" v-if="data.options.flex"> -->
        <el-form-item label="垂直排列方式" v-if="data.options.flex">
          <el-select v-model="data.options.align">
            <!-- <el-option value="top" :label="$t('fm.config.widget.alignTop')"></el-option>
            <el-option value="middle" :label="$t('fm.config.widget.alignMiddle')"></el-option>
            <el-option value="bottom" :label="$t('fm.config.widget.alignBottom')"></el-option> -->
            <el-option value="top" label="顶部对齐"></el-option>
            <el-option value="middle" label="居中"></el-option>
            <el-option value="bottom" label="底部对齐"></el-option>
          </el-select>
        </el-form-item>
      </template>

      <template v-if="data.type == 'tabs'">
        <!-- <el-form-item :label="$t('fm.config.widget.type')"> -->
        <el-form-item label="风格类型">
          <el-radio-group v-model="data.options.type">
            <!-- <el-radio-button label="">{{ $t('fm.config.widget.default') }}</el-radio-button>
            <el-radio-button label="card">{{ $t('fm.config.widget.card') }}</el-radio-button>
            <el-radio-button label="border-card">{{ $t('fm.config.widget.borderCard') }}</el-radio-button> -->
            <el-radio-button label="">默认</el-radio-button>
            <el-radio-button label="card">选项卡</el-radio-button>
            <el-radio-button label="border-card">卡片化</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.tabPosition')"> -->
        <el-form-item label="选项卡位置">
          <el-radio-group v-model="data.options.tabPosition">
            <!-- <el-radio-button label="top">{{ $t('fm.config.widget.top') }}</el-radio-button>
            <el-radio-button label="left">{{ $t('fm.config.widget.left') }}</el-radio-button>
            <el-radio-button label="right">{{ $t('fm.config.widget.right') }}</el-radio-button>
            <el-radio-button label="bottom">{{ $t('fm.config.widget.bottom') }}</el-radio-button> -->
            <el-radio-button label="top">顶部</el-radio-button>
            <el-radio-button label="left">左侧</el-radio-button>
            <el-radio-button label="right">右侧</el-radio-button>
            <el-radio-button label="bottom">底部</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item :label="$t('fm.config.widget.tabOption')"> -->
        <el-form-item label="标签配置项">
          <draggable tag="ul" :list="data.tabs" v-bind="{ group: { name: 'options' }, ghostClass: 'ghost', handle: '.drag-item' }" handle=".drag-item">
            <li v-for="(item, index) in data.tabs" :key="index">
              <i class="drag-item" style="font-size: 16px; margin: 0 5px; cursor: move"><i class="iconfont icon-icon_bars"></i></i>
              <!-- <el-input :placeholder="$t('fm.config.widget.tabName')" size="mini" style="width: 200px" v-model="item.label"></el-input> -->
              <el-input placeholder="标签名称" size="mini" style="width: 200px" v-model="item.label"></el-input>

              <el-button @click="handleOptionsRemove(index)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px; margin-left: 5px"></el-button>
            </li>
          </draggable>
          <div style="margin-left: 22px">
            <!-- <el-button type="text" @click="handleAddTab">{{ $t('fm.actions.addTab') }}</el-button> -->
            <el-button type="text" @click="handleAddTab">添加标签</el-button>
          </div>
        </el-form-item>
      </template>

      <!-- <el-form-item :label="$t('fm.config.widget.customClass')" v-if="Object.keys(data.options).includes('customClass')"> -->
      <el-form-item label="自定义Class" v-if="Object.keys(data.options).includes('customClass')">
        <el-input v-model="data.options.customClass" clearable></el-input>
      </el-form-item>

      <!-- <el-form-item :label="$t('fm.config.widget.attribute')"> -->
      <el-form-item label="操作属性">
        <!-- <el-checkbox v-model="data.options.dataBind" v-if="Object.keys(data.options).indexOf('dataBind') >= 0 && !data.options.tableColumn" :disabled="data.type != 'blank' && data.type != 'component'">{{ $t('fm.config.widget.dataBind') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.hidden" v-if="Object.keys(data.options).indexOf('hidden') >= 0 && !data.options.tableColumn">{{ $t('fm.config.widget.hidden') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.readonly" v-if="Object.keys(data.options).indexOf('readonly') >= 0">{{ $t('fm.config.widget.readonly') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.disabled" v-if="Object.keys(data.options).indexOf('disabled') >= 0">{{ $t('fm.config.widget.disabled') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.editable" v-if="Object.keys(data.options).indexOf('editable') >= 0">{{ $t('fm.config.widget.editable') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.clearable" v-if="Object.keys(data.options).indexOf('clearable') >= 0">{{ $t('fm.config.widget.clearable') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.arrowControl" v-if="Object.keys(data.options).indexOf('arrowControl') >= 0">{{ $t('fm.config.widget.arrowControl') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.isDelete" v-if="Object.keys(data.options).indexOf('isDelete') >= 0">{{ $t('fm.config.widget.isDelete') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.isEdit" v-if="Object.keys(data.options).indexOf('isEdit') >= 0">{{ $t('fm.config.widget.isEdit') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.showPassword" v-if="Object.keys(data.options).indexOf('showPassword') >= 0">{{ $t('fm.config.widget.showPassword') }}
        </el-checkbox>
        <el-checkbox v-model="data.options.showScore" v-if="Object.keys(data.options).indexOf('showScore') >= 0">{{ $t('fm.config.widget.showScore') }}
        </el-checkbox> -->
        <el-checkbox v-model="data.options.dataBind" v-if="Object.keys(data.options).indexOf('dataBind') >= 0 && !data.options.tableColumn" :disabled="data.type != 'blank' && data.type != 'component'">数据绑定
        </el-checkbox>
        <el-checkbox v-model="data.options.hidden" v-if="Object.keys(data.options).indexOf('hidden') >= 0 && !data.options.tableColumn">隐藏
        </el-checkbox>
        <el-checkbox v-model="data.options.readonly" v-if="Object.keys(data.options).indexOf('readonly') >= 0">完全只读
        </el-checkbox>
        <el-checkbox v-model="data.options.disabled" v-if="Object.keys(data.options).indexOf('disabled') >= 0">禁用
        </el-checkbox>
        <el-checkbox v-model="data.options.editable" v-if="Object.keys(data.options).indexOf('editable') >= 0">文本框可输入
        </el-checkbox>
        <el-checkbox v-model="data.options.clearable" v-if="Object.keys(data.options).indexOf('clearable') >= 0">显示清除按钮
        </el-checkbox>
        <el-checkbox v-model="data.options.arrowControl" v-if="Object.keys(data.options).indexOf('arrowControl') >= 0">使用箭头进行时间选择
        </el-checkbox>
        <el-checkbox v-model="data.options.isDelete" v-if="Object.keys(data.options).indexOf('isDelete') >= 0">可删除
        </el-checkbox>
        <el-checkbox v-model="data.options.isEdit" v-if="Object.keys(data.options).indexOf('isEdit') >= 0">可编辑
        </el-checkbox>
        <el-checkbox v-model="data.options.showPassword" v-if="Object.keys(data.options).indexOf('showPassword') >= 0">显示密码
        </el-checkbox>
        <el-checkbox v-model="data.options.showScore" v-if="Object.keys(data.options).indexOf('showScore') >= 0">显示分数
        </el-checkbox>
      </el-form-item>

      <template v-if="data.type != 'grid' && data.type != 'tabs'">
        <!-- <el-form-item :label="$t('fm.config.widget.validate')"> -->
        <el-form-item label="校验">
          <div class="validate-block" v-if="Object.keys(data.options).indexOf('required') >= 0">
            <!-- <el-checkbox v-model="data.options.required">{{ $t('fm.config.widget.required') }}</el-checkbox> -->
            <el-checkbox v-model="data.options.required">必填</el-checkbox>

            <!-- <el-input size="mini" class="message-input" clearable v-model="data.options.requiredMessage" v-if="data.options.required" :placeholder="$t('fm.message.errorTip')"></el-input> -->
            <el-input size="mini" class="message-input" clearable v-model="data.options.requiredMessage" v-if="data.options.required" placeholder="自定义错误提示"></el-input>
          </div>
          <div class="validate-block" v-if="Object.keys(data.options).indexOf('dataType') >= 0">
            <el-checkbox v-model="data.options.dataTypeCheck" style="margin-right: 10px"></el-checkbox>
            <el-select :popper-append-to-body="false" :disabled="!data.options.dataTypeCheck" v-if="Object.keys(data.options).indexOf('dataType') >= 0" v-model="data.options.dataType" size="mini">
              <!-- <el-option value="string" :label="$t('fm.config.widget.string')"></el-option> -->
              <!-- <el-option value="number" :label="$t('fm.config.widget.number')"></el-option> -->
              <!-- <el-option value="integer" :label="$t('fm.config.widget.integer')"></el-option> -->
              <!-- <el-option value="float" :label="$t('fm.config.widget.float')"></el-option> -->
              <!-- <el-option value="url" :label="$t('fm.config.widget.url')"></el-option> -->
              <!-- <el-option value="email" :label="$t('fm.config.widget.email')"></el-option> -->
              <!-- <el-option value="hex" :label="$t('fm.config.widget.hex')"></el-option> -->
              <el-option value="string" label="字符串"></el-option>
              <el-option value="number" label="数字"></el-option>
              <el-option value="integer" label="整数"></el-option>
              <el-option value="float" label="浮点数"></el-option>
              <el-option value="url" label="URL地址"></el-option>
              <el-option value="email" label="邮箱地址"></el-option>
              <el-option value="hex" label="十六进制"></el-option>
            </el-select>

            <!-- <el-input size="mini" class="message-input" clearable v-model="data.options.dataTypeMessage" v-if="data.options.dataTypeCheck" :placeholder="$t('fm.message.errorTip')"></el-input> -->
            <el-input size="mini" class="message-input" clearable v-model="data.options.dataTypeMessage" v-if="data.options.dataTypeCheck" placeholder="自定义错误提示"></el-input>
          </div>

          <div class="validate-block" v-if="Object.keys(data.options).indexOf('pattern') >= 0">
            <el-checkbox v-model="data.options.patternCheck" style="margin-right: 10px"></el-checkbox>
            <!-- <el-input :disabled="!data.options.patternCheck" size="mini" v-model.lazy="data.options.pattern" style="width: 240px" :placeholder="$t('fm.config.widget.patternPlaceholder')"></el-input> -->
            <!-- <el-input size="mini" class="message-input" clearable v-model="data.options.patternMessage" v-if="data.options.patternCheck" :placeholder="$t('fm.message.errorTip')"></el-input> -->
            <el-input :disabled="!data.options.patternCheck" size="mini" v-model.lazy="data.options.pattern" style="width: 240px" placeholder="填写正则表达式"></el-input>
            <el-input size="mini" class="message-input" clearable v-model="data.options.patternMessage" v-if="data.options.patternCheck" placeholder="自定义错误提示"></el-input>
          </div>
        </el-form-item>
      </template>
    </el-form>

    <!-- sql配置弹窗 -->
    <a-modal :width="900" v-model="sqlDataVisible" title="SQL配置" :closable="false" :footer="null" v-if="data.options.typeId">
      <a-tabs default-active-key="1">
        <a-tab-pane key="1" tab="数据">
          <a-form-model>
            <a-row>
              <a-col>
                <a-form-model-item label="数据源：">
                  <a-select style="width: 200px" v-model="data.options.dataSourceId">
                    <a-select-option v-for="(item, index) in dataSource" :key="index" :value="item.id">
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col>
                <a-form-model-item label="SQL语句：">
                  <a-textarea v-model="data.options.sqlstr" placeholder="请输入sql语句" />
                </a-form-model-item>
              </a-col>
              <a-col :offset="21">
                <a-button icon="search" @click="SqlSelect"> Search </a-button>
              </a-col>
              <a-col>
                <a-table :columns="data.options.surface.dataColumns" :data-source="data.options.surface.dataResources" :pagination="false" />
              </a-col>
            </a-row>
          </a-form-model>
        </a-tab-pane>
        <a-tab-pane key="2" tab="配置">
          <a-form-model>
            <!-- 301X轴Y轴（配置） -->
            <a-row v-if="judgeCF('301')">
              <a-col>
                <a-form-model-item label="X轴：">
                  <a-select style="width: 200px" v-model="data.options.direction.key">
                    <a-select-option v-for="(item, index) in data.options.surface.dataColumns" :key="index" :value="item.title">
                      {{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col>
                <a-form-model-item label="Y轴：">
                  <a-select style="width: 200px" v-model="data.options.direction.value">
                    <a-select-option v-for="(item, index) in data.options.surface.dataColumns" :key="index" :value="item.title">
                      {{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
            </a-row>
            <!-- 302属性值（配置） -->
            <a-row v-if="judgeCF('302')">
              <a-col>
                <a-form-model-item label="属性：">
                  <a-select style="width: 200px" v-model="data.options.direction.key">
                    <a-select-option v-for="(item, index) in data.options.surface.dataColumns" :key="index" :value="item.title">
                      {{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col>
                <a-form-model-item label="值：">
                  <a-select style="width: 200px" v-model="data.options.direction.value">
                    <a-select-option v-for="(item, index) in data.options.surface.dataColumns" :key="index" :value="item.title">
                      {{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
            </a-row>
            <!-- 303多系列属性值（配置） -->
            <a-row v-if="judgeCF('303')">
              <a-col>
                <a-form-model-item label="X轴：">
                  <a-select style="width: 200px" v-model="data.options.direction.key">
                    <a-select-option v-for="(item, index) in data.options.surface.dataColumns" :key="index" :value="item.title">
                      {{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col>
                <a-form-model-item label="Y轴：">
                  <a-select style="width: 200px" v-model="data.options.direction.value">
                    <a-select-option v-for="(item, index) in data.options.surface.dataColumns" :key="index" :value="item.title">
                      {{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col>
                <a-form-model-item label="多系列参数：">
                  <a-select style="width: 200px" v-model="data.options.direction.legend">
                    <a-select-option v-for="(item, index) in data.options.surface.dataColumns" :key="index" :value="item.title">
                      {{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-form-model>
        </a-tab-pane>
      </a-tabs>
      <br />
      <a-row>
        <a-col :span="2" :offset="22">
          <a-button @click="updateCom">确定</a-button>
        </a-col>
      </a-row>
    </a-modal>
  </div>
  <div v-else class="empty">
    <!-- {{ $t('fm.description.configEmpty') }} -->
    请添加字段
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import CodeEditor from '../components/CodeEditor'
import CodeDialog from './CodeDialog'
import compro from '../componentProperties'
import { axios } from '../../../utils/request'
import { getDataSource, SqlSelect, GetData } from '@api/boardapi'
import { Message } from 'element-ui'

export default {
  components: {
    Draggable,
    CodeEditor,
    CodeDialog,
  },
  props: ['widgetForm', 'data'],
  data() {
    return {
      // 定义data.tableCol
      validator: {
        type: null,
        required: null,
        pattern: null,
        range: null,
        length: null,
      },
      dataTableCols: [],
      currentColType: '',
      echartdataType: 3,
      urlType: 1,
      sqlDataVisible: false,
      dataSource: [], //sql配置数据源
      tableDataSource: [] //表单数据
    }
  },
  mounted() {},
  computed: {
    show() {
      return this.data && Object.keys(this.data).length > 0 && this.data.key
    },
    tableColsFilter() {
      return this.$store.state.tableCols.filter(
        (item) =>
          item.dbFieldName !== 'id' &&
          item.dbFieldName !== 'create_by' &&
          item.dbFieldName !== 'create_time' &&
          item.dbFieldName !== 'update_by' &&
          item.dbFieldName !== 'update_time'
      )
    },
    scheduleFilter() {
      return this.$store.state.dataBaseList.filter((item) => this.$store.state.secondTableCols.includes(item.tableName))
    },
  },
  created() {
    // 这个组件是右边面板的组件
    this.getTableCol()
    // console.log("666666666666",this.data.key)
  },
  methods: {
    judgeCF(key) {
      var arr = this.data.options.configure
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] == key) {
          return true
        }
      }
      return false
    },
    //大屏配置相关
    getDataSource() {
      getDataSource().then((res) => {
        // console.log('数据源', res)
        this.dataSource = res.data
        // this.SqlSelect()
      })
    },
    sqlDataV() {
      this.sqlDataVisible = true
      this.getDataSource()
    },
    // 搜索数据
    SqlSelect() {
      var getAPI = {
        datasourceId: this.data.options.dataSourceId,
        sql: this.data.options.sqlstr,
      }
      SqlSelect(getAPI)
        .then((res) => {
          if (res.status === 200) {
            for (var i = 0; i < res.data.columns.length; i++) {
              this.data.options.surface.dataColumns[i] = {
                title: res.data.columns[i].name,
                dataIndex: res.data.columns[i].name,
              }
            }
            var str = res.data.data
            this.data.options.surface.dataResources = JSON.parse(str)
            this.tableDataSource = JSON.parse(str)
            // console.log('表格数据', this.surface)
          } else {
            Message({
              showClose: true,
              message: '请填写正确的查询语句!',
              type: 'warning',
            })
          }
        })
        .catch(() => {
          Message({
            showClose: true,
            message: '请填写正确的查询语句!',
            type: 'warning',
          })
        })
    },
    updateCom() {
      if(this.data.type == 'table_test') {
        console.log('表格数据',this.tableDataSource)
        this.data.options.defaultValue = this.tableDataSource
        this.sqlDataVisible = false
        return
      }
      let params = {}
      params.dataSourceId = this.data.options.dataSourceId
      params.direction = JSON.stringify(this.data.options.direction)
      params.filterParameters = []
      params.options = JSON.stringify(this.data.options.defaultValue)
      params.sql = this.data.options.sqlstr
      params.typeId = this.data.options.typeId
      console.log('提交看板的数据', params)
      GetData(params).then((res) => {
        console.log('获取数据结果', res)
        if (res.status === 200) {
          let edata = res.data
          // this.data.options.defaultValue.xAxis.data = edata.xAxis.data
          // this.data.options.defaultValue.series[0].data = edata.series[0].data

          // this.data.options.defaultValue = {
          //   title: {
          //     text: '某站点用户访问来源',
          //     subtext: '纯属虚构',
          //     left: 'center',
          //   },
          //   tooltip: {
          //     trigger: 'item',
          //   },
          //   legend: {
          //     orient: 'vertical',
          //     left: 'left',
          //   },
          //   series: [
          //     {
          //       name: '访问来源',
          //       type: 'pie',
          //       radius: '50%',
          //       data: [
          //         { value: 1048, name: '搜索引擎' },
          //         { value: 735, name: '直接访问' },
          //         { value: 580, name: '邮件营销' },
          //         { value: 484, name: '联盟广告' },
          //         { value: 300, name: '视频广告' },
          //       ],
          //       emphasis: {
          //         itemStyle: {
          //           shadowBlur: 10,
          //           shadowOffsetX: 0,
          //           shadowColor: 'rgba(0, 0, 0, 0.5)',
          //         },
          //       },
          //     },
          //   ],
          // }

          this.data.options.defaultValue = edata
          this.sqlDataVisible = false
          console.log('????????', this.data.options.defaultValue)
          Message({
            showClose: true,
            message: '配置成功!',
            type: 'success',
          })
        } else {
          Message({
            showClose: true,
            message: '配置失败!',
            type: 'warning',
          })
        }
      })
    },

    async loadDataBase() {
      await axios({
        url: `/online/cgform/head/list`,
        method: 'get',
        data: { pageSize: 9999 },
      })
        .then((res) => {
          this.$store.commit('changeDataBaseList', res.result.records)
        })
        .catch((err) => {
          this.$message.error('请求数据库错误！')
        })
    },
    scheduleChange() {
      this.$forceUpdate()
    },
    // 给子表单绑定了附表的函数
    async checkkey(id) {
      // 获取所选附表对应的字段
      this.data.tableId = id
      if (id !== '') {
        // 获取附表的字段
        await axios({
          url: `/online/cgform/field/listByHeadId?headId=${id}`,
          method: 'get',
        })
          .then((res) => {
            this.$store.commit('changeTableCols', res.result)
          })
          .catch((err) => {
            console.log('表单属性界面获取数据表字段属性失败！')
          })
      } else {
        console.log('没有选择数据表，暂未加载数据表字段！')
      }
    },

    noSelect(itemdbType, itemdbFieldName) {
      let disableflag = false
      if (this.bindingDisplay(itemdbFieldName)) {
        return true
      }
      if (compro[this.data.type] && compro[this.data.type].toLowerCase == itemdbType.toLowerCase) {
        /* 如果当前所选组件的type值和字段的type值一样 */
        disableflag = false
      } else {
        disableflag = true
        return disableflag
      }
      return disableflag
    },
    // 需改进：bindingDisplay在判断是否禁用时调用过，在判断是否需要添加（已绑定）时最好不要再调用一次此函数
    bindingDisplay(itemdbFieldName) {
      var vm = this
      var recuFlag = false
      var bindedFlag = false
      //mainTableList用来存储当前主表已经绑定的字段
      // 因为绑定主表字段的组件可能在最外层，也可能在嵌套无数层的栅格内，所以需要递归完成后才能判断是否已经绑定过该字段
      var mainTableList = []
      // isBinded参数：选中元素绑定的子表单或主表的元素组成的list
      // isBinded功能：判断list里面是否有tableCol和itemdbFieldName一样的，即已经绑定了的
      var isBinded = function (list) {
        // 需要递归判断这个字段是否已经绑定
        for (var j = 0; j < list.length; j++) {
          if (list[j].tableCol == itemdbFieldName && vm.data.key != list[j].key) {
            bindedFlag = true
            return
          }
        }
      }
      // 递归查找
      var testRecu = function (obj) {
        if (recuFlag) return
        for (var k = 0; k < obj.list.length; k++) {
          if (obj.list[k].type === 'grid') {
            // 暂时只针对栅格布局的嵌套
            for (var j = 0; j < obj.list[k].columns.length; j++) {
              testRecu(obj.list[k].columns[j])
            }
          } else if (obj.list[k].type === 'table') {
            // 说明有子表单
            // 判断这个子表单是否是当前选中元素绑定的子表单
            // 判断依据：这个子表单里面有一个元素的key和当前选种元素的key一样，说明这个元素在这个子表单里
            // 此时再去判断这个子表单里面的元素有不有tableCol==itemdbFieldName且不是当前选中元素的
            for (var i = 0; i < obj.list[k].tableColumns.length; i++) {
              if (vm.data.key == obj.list[k].tableColumns[i].key) {
                // 这个子表单就是当前选中元素的绑定的子表单
                isBinded(obj.list[k].tableColumns)
                recuFlag = true
                break
              }
            }
          } else {
            // 可能在外面的元素（单独出现在主表内/栅格内）
            // 把所有绑定主表字段的组件所绑定的字段都存起来，key是为了调用isBinded()时判断是否为该组件本身绑定了这个字段
            mainTableList.push({
              tableCol: obj.list[k].tableCol,
              key: obj.list[k].key,
            })
          }
        }
      }
      testRecu(this.widgetForm)
      // 绑定在主表上的，没有在子表单，可能直接在栅格里
      if (!recuFlag) {
        recuFlag = true
        isBinded(mainTableList)
      }
      return bindedFlag
    },

    tableColChange(selectData) {
      this.data.tableColId =
        this.$store.state.tableCols[this.$store.state.tableCols.findIndex((item) => item.dbFieldName === selectData)].id
      this.$forceUpdate() // 解决v-model反显延迟
    },

    getTableCol() {
      /*
       *0、this.data存的是当前选中的组件的json，可能是表单、子表单、组件
       *1、如果位于子表单中，则获取子表单对应附表的字段，在为子表单绑定子表时获取了字段的，此时不需要获取
       *2、先使用递归查找选中组件是否在子表单内，若在则调用getSecondField()获取附表的字段
       *3、否则调用getMainField()获取主表的字段
       *4、利用this.data.key与this.widgetForm中的key判断是否是当前的选中元素
       */

      var flag = false
      var vm = this
      // 递归查找
      var testRecu = function (obj) {
        if (flag) return
        for (var k = 0; k < obj.list.length; k++) {
          if (obj.list[k].type === 'grid') {
            // 暂时只针对栅格布局的嵌套
            for (var j = 0; j < obj.list[k].columns.length; j++) {
              testRecu(obj.list[k].columns[j])
            }
          } else if (obj.list[k].type === 'table') {
            // 说明有子表单
            for (var i = 0; i < obj.list[k].tableColumns.length; i++) {
              if (vm.data.key == obj.list[k].tableColumns[i].key) {
                vm.getSecondField(obj.list[k].schedule == undefined ? '' : obj.list[k].schedule)
                flag = true
                break
              }
            }
          }
        }
      }
      testRecu(this.widgetForm)
      // 是单独的，没有子表单
      if (!flag) {
        flag = true
        vm.getMainField()
      }
    },
    // 获取附表的字段
    getSecondField(scheduleName) {
      this.$store.commit('changeTableCols', [])
      // 获取附表id
      let scheduleId = this.getScheduleIdByName(scheduleName)
      // 获取附表的字段
      axios({
        url: `/online/cgform/field/listByHeadId?headId=${scheduleId}`,
        method: 'get',
      })
        .then((res) => {
          this.$store.commit('changeTableCols', res.result)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    getScheduleIdByName(scheduleName) {
      let dbList = this.$store.state.dataBaseList
      for (let i = 0; i < dbList.length; i++) {
        if (dbList[i].tableName == scheduleName) {
          return dbList[i].id
        }
      }
    },
    // 获取主表的字段
    getMainField() {
      if (!this.widgetForm.config.tableId) {
        this.$store.commit('changeTableCols', [])
      } else {
        axios({
          url: `/online/cgform/field/listByHeadId?headId=${this.widgetForm.config.tableId}`,
          method: 'get',
        })
          .then((res) => {
            this.$store.commit('changeTableCols', res.result)
          })
          .catch((err) => {
            console.log('字段属性获取数据表字段属性失败！')
          })
      }
    },
    handleOptionsRemove(index) {
      if (this.data.type === 'grid') {
        this.data.columns.splice(index, 1)
      } else if (this.data.type === 'tabs') {
        this.data.tabs.splice(index, 1)
      } else if (this.data.type === 'imgupload' || this.data.type === 'fileupload') {
        this.data.options.headers.splice(index, 1)
      } else {
        if (!this.data.options.remote && this.data.options.options[index].value) {
          this.data.options.defaultValue = typeof this.data.options.defaultValue === 'string' ? '' : []
        }

        this.data.options.options.splice(index, 1)
      }
    },
    handleAddOption() {
      if (this.data.options.showLabel) {
        this.data.options.options.push({
          // value: this.$t('fm.config.widget.newOption'),
          // label: this.$t('fm.config.widget.newOption'),
          value: '新选项',
          label: '新选项',
        })
      } else {
        this.data.options.options.push({
          // value: this.$t('fm.config.widget.newOption'),
          // label: this.$t('fm.config.widget.newOption'),
          value: '新选项',
          label: '新选项',
        })
      }
    },
    handleAddColumn() {
      this.data.columns.push({
        span: '',
        list: [],
      })
    },
    handleAddTab() {
      let length = this.data.tabs.length

      this.data.tabs.push({
        // label: this.$t('fm.config.widget.tab') + (length + 1),
        label: '标签页' + (length + 1),
        name: 'tab_' + new Date().getTime(),
        list: [],
      })
    },
    handleAddHeader() {
      if ('headers' in this.data.options) {
        this.data.options.headers.push({
          key: '',
          value: '',
        })
      } else {
        this.$set(this.data.options, 'headers', [{ key: '', value: '' }])
      }
    },
    generateRule() {
      this.data.rules = []
      Object.keys(this.validator).forEach((key) => {
        if (this.validator[key]) {
          this.data.rules.push(this.validator[key])
        }
      })
    },
    handleSelectMuliple(value) {
      if (this.data.type == 'select') {
        if (value) {
          if (this.data.options.defaultValue) {
            this.data.options.defaultValue = [this.data.options.defaultValue]
          } else {
            this.data.options.defaultValue = []
          }
        } else {
          if (this.data.options.defaultValue.length > 0) {
            this.data.options.defaultValue = this.data.options.defaultValue[0]
          } else {
            this.data.options.defaultValue = ''
          }
        }
      }
    },

    handleSetTemplate() {
      this.$refs.codeDialog.open(this.data.options.template)
    },

    handleTemplateConfirm(value) {
      this.data.options.template = value

      this.$refs.codeDialog.close()
    },

    validateRequired(val) {
      if (val) {
        this.validator.required = {
          required: true,
          message: this.data.options.requiredMessage
            ? this.data.options.requiredMessage
            // : `${this.$t('fm.config.widget.validatorRequired')}`,
            : `${'必须填写'}`,
        }
      } else {
        this.validator.required = null
      }

      this.$nextTick(() => {
        this.generateRule()
      })
    },

    validateDataType(val) {
      if (!this.show) {
        return false
      }

      if (val && (this.data.options.dataTypeCheck || !Object.keys(this.data.options).includes('dataTypeCheck'))) {
        this.validator.type = {
          type: val,
          message: this.data.options.dataTypeMessage
            ? this.data.options.dataTypeMessage
            // : this.$t('fm.config.widget.validatorType'),
            : '格式不正确',
        }
      } else {
        this.validator.type = null
      }

      this.generateRule()
    },
    valiatePattern(val) {
      if (!this.show) {
        return false
      }

      if (val && (this.data.options.patternCheck || !Object.keys(this.data.options).includes('patternCheck'))) {
        this.validator.pattern = {
          pattern: val,
          message: this.data.options.patternMessage
            ? this.data.options.patternMessage
            // : this.$t('fm.config.widget.validatorPattern'),
            : '格式不匹配',
        }
      } else {
        this.validator.pattern = null
      }

      this.generateRule()
    },
  },
  watch: {
    'data.options.dataSourceId': function (val) {
      console.log('jafhajikfhaikjfh', val)
    },
    'data.options.isRange': function (val) {
      if (typeof val !== 'undefined') {
        if (val) {
          this.data.options.defaultValue = null
        } else {
          if (Object.keys(this.data.options).indexOf('defaultValue') >= 0) this.data.options.defaultValue = ''
        }
      }
    },
    'data.options.required': function (val) {
      /* 必填 */
      this.validateRequired(val)
    },
    'data.options.requiredMessage': function (val) {
      this.validateRequired(this.data && this.data.options ? this.data.options.required : false)
    },
    'data.options.dataType': function (val) {
      this.validateDataType(val)
    },
    'data.options.dataTypeCheck': function (val) {
      this.validateDataType(this.data && this.data.options ? this.data.options.dataType : '')
    },
    'data.options.dataTypeMessage': function (val) {
      this.validateDataType(this.data && this.data.options ? this.data.options.dataType : '')
    },
    'data.options.pattern': function (val) {
      this.valiatePattern(val)
    },
    'data.options.patternCheck': function (val) {
      this.valiatePattern(this.data && this.data.options ? this.data.options.pattern : '')
    },
    'data.options.patternMessage': function (val) {
      this.valiatePattern(this.data && this.data.options ? this.data.options.pattern : '')
    },
    tableCols: function (val) {
      console.log(!val.some((item) => item.dbFieldName === this.data.tableCol))
      if (!val.some((item) => item.dbFieldName === this.data.tableCol)) this.data.tableCol = ''
    },
    'data.tableCol': function (val) {},
  },
}
</script>
