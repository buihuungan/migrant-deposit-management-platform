// 添加自定义组件信息
// 基础字段
export const basicComponents = [
  {
    type: 'input',
    name:'单行文本',
    icon: 'icon-input',
    options: {
      width: '100%',
      defaultValue: '',
      required: false,
      requiredMessage: '',
      dataType: '',
      dataTypeCheck: false,
      dataTypeMessage: '',
      pattern: '',
      patternCheck: false,
      patternMessage: '',
      placeholder: '',
      customClass: '',
      disabled: false,
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      showPassword: false,
    }
  },
  {
    type: 'textarea',
    name:'多行文本',
    icon: 'icon-diy-com-textarea',
    options: {
      width: '100%',
      defaultValue: '',
      required: false,
      requiredMessage: '',
      disabled: false,
      pattern: '',
      patternMessage: '',
      placeholder: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'number',
    name:'计数器',
    icon: 'icon-number',
    options: {
      width: '',
      required: false,
      requiredMessage: '',
      defaultValue: 0,
      min: 0,
      max: 9,
      step: 1,
      disabled: false,
      controlsPosition: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'radio',
    name:'单选框组',
    icon: 'icon-radio-active',
    options: {
      inline: false,
      defaultValue: '',
      showLabel: false,
      options: [
        {
          value: 0,
          label: 'Option 1'
        },
        {
          value: 1,
          label: 'Option 2'
        },
        {
          value: 2,
          label: 'Option 3'
        }
      ],
      required: false,
      requiredMessage: '',
      width: '',
      remote: false,
      remoteType: 'option',
      remoteOption: '',
      remoteOptions: [],
      props: {
        value: 'value',
        label: 'label'
      },
      remoteFunc: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      disabled: false
    }
  },
  {
    type: 'checkbox',
    name:'多选框组',
    icon: 'icon-check-box',
    options: {
      inline: false,
      defaultValue: [],
      showLabel: false,
      options: [
        {
          value: 'Option 1'
        },
        {
          value: 'Option 2'
        },
        {
          value: 'Option 3'
        }
      ],
      required: false,
      requiredMessage: '',
      width: '',
      remote: false,
      remoteType: 'option',
      remoteOption: '',
      remoteOptions: [],
      props: {
        value: 'value',
        label: 'label'
      },
      remoteFunc: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      disabled: false
    }
  },
  {
    type: 'time',
    name:'时间选择器',
    icon: 'icon-time',
    options: {
      defaultValue: '',
      readonly: false,
      disabled: false,
      editable: true,
      clearable: true,
      placeholder: '',
      startPlaceholder: '',
      endPlaceholder: '',
      isRange: false,
      arrowControl: true,
      format: 'HH:mm:ss',
      required: false,
      requiredMessage: '',
      width: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'date',
    name:'日期选择器',
    icon: 'icon-date',
    options: {
      defaultValue: '',
      readonly: false,
      disabled: false,
      editable: true,
      clearable: true,
      placeholder: '',
      startPlaceholder: '',
      endPlaceholder: '',
      type: 'date',
      format: 'yyyy-MM-dd',
      timestamp: false,
      required: false,
      requiredMessage: '',
      width: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'rate',
    name:'评分',
    icon: 'icon-pingfen1',
    options: {
      defaultValue: null,
      max: 5,
      disabled: false,
      allowHalf: false,
      required: false,
      requiredMessage: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      showScore: false
    }
  },
  {
    type: 'color',
    name:'颜色选择器',
    icon: 'icon-color',
    options: {
      defaultValue: '',
      disabled: false,
      showAlpha: false,
      required: false,
      requiredMessage: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'select',
    name:'下拉选择框',
    icon: 'icon-select',
    options: {
      defaultValue: '',
      multiple: false,
      disabled: false,
      clearable: false,
      placeholder: '',
      required: false,
      requiredMessage: '',
      showLabel: false,
      width: '',
      options: [
        {
          value: 'Option 1'
        },
        {
          value: 'Option 2'
        },
        {
          value: 'Option 3'
        }
      ],
      remote: false,
      remoteType: 'option',
      remoteOption: '',
      filterable: false,
      remoteOptions: [],
      props: {
        value: 'value',
        label: 'label'
      },
      remoteFunc: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'switch',
    name:'开关',
    icon: 'icon-switch',
    options: {
      defaultValue: false,
      required: false,
      requiredMessage: '',
      disabled: false,
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'slider',
    name:'滑块',
    icon: 'icon-slider',
    options: {
      defaultValue: 0,
      disabled: false,
      required: false,
      requiredMessage: '',
      min: 0,
      max: 100,
      step: 1,
      showInput: false,
      range: false,
      width: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'text',
    name:'文字',
    icon: 'icon-wenzishezhi-',
    options: {
      defaultValue: 'This is a text',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'html',
    name:'HTML',
    icon: 'icon-html',
    options: {
      defaultValue: '<b style="color: red;">\n\tThis is a HTML5\n</b>',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'tag',
    name:'标签',
    icon: 'icon-html',
    options: {
      defaultValue: '标签',
      type: 'info'
    }
  }
]

// 高级字段
export const advanceComponents = [
  {
    type: 'blank',
    name:'自定义区域',
    icon: 'icon-zidingyishuju',
    options: {
      defaultType: 'String',
      customClass: '',
      width: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true
    }
  },
  {
    type: 'component',
    name:'自定义组件',
    icon: 'icon-component',
    options: {
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      template: '<p>自定义内容</p>',
      required: false,
    }
  },
  {
    type: 'fileupload',
    name:'文件上传',
    icon: 'icon-wenjianshangchuan',
    options: {
      defaultValue: [],
      width: '',
      tokenFunc: 'funcGetToken',
      token: '',
      domain: 'http://tcdn.form.xiaoyaoji.cn/',
      disabled: false,
      tip: '',
      action: 'http://tools-server.making.link/api/transfer',
      customClass: '',
      limit: 9,
      multiple: false,
      isQiniu: true,
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      headers: [],
      required: false,
    }
  },
  {
    type: 'imgupload',
    name:'图片上传',
    icon: 'icon-tupian',
    options: {
      defaultValue: [],
      size: {
        width: 100,
        height: 100,
      },
      width: '',
      tokenFunc: 'funcGetToken',
      token: '',
      domain: 'http://tcdn.form.xiaoyaoji.cn/',
      disabled: false,
      readonly: false,
      limit: 8,
      multiple: false,
      isQiniu: true,
      isDelete: false,
      min: 0,
      isEdit: false,
      action: 'http://tools-server.making.link/api/transfer',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      headers: [],
      required: false,
    }
  },
  {
    type: 'editor',
    name:'编辑器',
    icon: 'icon-fuwenbenkuang',
    options: {
      defaultValue: '',
      width: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      customToolbar: [
        ['bold', 'italic', 'underline', 'strike',
          { 'color': [] },
          { 'background': [] },
          { 'align': [] },
          { 'list': 'ordered' },
          { 'list': 'bullet' },
          { 'indent': '-1' },
          { 'indent': '+1' }
        ],
        [{ 'font': [] }, { 'header': [1, 2, 3, 4, 5, 6, false] }],
        [{ 'script': 'sub' }, { 'script': 'super' }],
        ['link', 'image', 'blockquote', 'code-block'],
        [{ 'direction': 'rtl' }],
        ['clean']
      ],
      disabled: false,
      required: false,
    }
  },
  {
    type: 'cascader',
    name:'级联选择器',
    icon: 'icon-jilianxuanze',
    options: {
      defaultValue: [],
      width: '',
      placeholder: '',
      disabled: false,
      clearable: false,
      remote: true,
      remoteType: 'option',
      remoteOption: '',
      remoteOptions: [],
      props: {
        value: 'value',
        label: 'label',
        children: 'children'
      },
      remoteFunc: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      required: false,
    }
  },
  {
    type: 'pcdcascader',
    name:'省市区级联',
    icon: 'icon-jilianxuanze',
    options: {
      defaultValue: [],
      width: '',
      placeholder: '',
      disabled: false,
      clearable: false,
      remote: true,
      remoteType: 'option',
      remoteOption: '',
      remoteOptions: [],
      props: {
        value: 'value',
        label: 'label',
        children: 'children'
      },
      remoteFunc: '',
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      required: false,
    }
  },
  {
    type: 'table',
    name:'子表单',
    icon: 'icon-table',
    options: {
      defaultValue: [],
      customClass: '',
      labelWidth: 100,
      isLabelWidth: false,
      hidden: false,
      dataBind: true,
      disabled: false,
      required: false,
    },
    tableColumns: []
  },
]

// 折线图
export const linechartComponents = [
  {
    type: 'baselinechart',
    name:'基础折线图',
    icon: 'icon-input',
    header: '基础折线图',
    bodyStyle: `{ padding: '20px' }`,
    shadow: 'always',
    options: {
      typeId: '1001',
      configure: [101, 201, 301],
      style: {
        width: '100%',//宽度默认100%,不要动
        height: '400px'//高度默认400px，不要修改，配置界面修改的高度就是这个值
      },
      defaultValue: {
        tooltip: {
          trigger: 'axis'
        },
        title: {
          text: '基础折线图',
          left: 'center',
        },
        xAxis: {
          type: 'category',
          data: [],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '',
            type: 'line',
            data: [],
          },
        ],
      },
      direction: {
        key: '',//配置的x轴
        value: '',//配置的y轴
      },
      surface: {
        dataColumns: [],//查询后的列表展示
        dataResources: [],//查询后的数据展示
      },
      sqlstr: '',//查询sql语句
      dataSourceId: '',//数据源id
      required: false,
      requiredMessage: '',
      dataType: '',
      dataTypeCheck: false,
      dataTypeMessage: '',
      pattern: '',
      patternCheck: false,
      patternMessage: '',
      placeholder: '',
      customClass: '',
      disabled: false,
      labelWidth: 100,//配置界面修改的宽度是这个值
      isLabelWidth: true,
      hidden: false,
      dataBind: true,
      showPassword: false,
    }
  },
  {
    type: 'serieslinechart',
    name:'多系列折线图',
    icon: 'icon-input',
    header: '多系列折线图',
    bodyStyle: `{ padding: '20px' }`,
    shadow: 'always',
    options: {
      typeId: '1002',
      configure: [101, 201, 303],
      style: {
        width: '100%',//宽度默认100%,不要动
        height: '400px'//高度默认400px，不要修改，配置界面修改的高度就是这个值
      },
      defaultValue: {
        tooltip: {
          trigger: 'item'
        },
        title: {
          text: '多系列折线图',
          left: 'center',
        },
        legend: {
          data: [],
          x: 'right',
          y: 'top',
        },
        xAxis: {
          type: 'category',
          data: [],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '',
            type: 'line',
            data: [],
          },
        ],
      },
      direction: {
        key: '',//配置的x轴
        value: '',//配置的y轴
        legend: '',//多系列参数
      },
      surface: {
        dataColumns: [],//查询后的列表展示
        dataResources: [],//查询后的数据展示
      },
      sqlstr: '',//查询sql语句
      dataSourceId: '',//数据源id
      required: false,
      requiredMessage: '',
      dataType: '',
      dataTypeCheck: false,
      dataTypeMessage: '',
      pattern: '',
      patternCheck: false,
      patternMessage: '',
      placeholder: '',
      customClass: '',
      disabled: false,
      labelWidth: 100,//配置界面修改的宽度是这个值
      isLabelWidth: true,
      hidden: false,
      dataBind: true,
      showPassword: false,
    }
  },
]

// 柱状图
export const columnchartComponents = [
  {
    type: 'basecolumnchart',
    name:'基础柱状图',
    icon: 'icon-input',
    header: '基础柱状图',
    bodyStyle: `{ padding: '20px' }`,
    shadow: 'always',
    options: {
      configure: [101, 201, 301],
      typeId: '2001',
      style: {
        width: '100%',//宽度默认100%,不要动
        height: '400px'//高度默认400px，不要修改，配置界面修改的高度就是这个值
      },
      defaultValue: {
        tooltip: {
          trigger: 'item'
        },
        title: {
          text: '基础柱状图',
          left: 'center',
        },
        xAxis: {
          type: 'category',
          data: [],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '',
            type: 'bar',
            data: [],
          },
        ],
      },
      direction: {
        key: '',//配置的x轴
        value: '',//配置的y轴
      },
      surface: {
        dataColumns: [],//查询后的列表展示
        dataResources: [],//查询后的数据展示
      },
      sqlstr: '',//查询sql语句
      dataSourceId: '',//数据源id
      required: false,
      requiredMessage: '',
      dataType: '',
      dataTypeCheck: false,
      dataTypeMessage: '',
      pattern: '',
      patternCheck: false,
      patternMessage: '',
      placeholder: '',
      customClass: '',
      disabled: false,
      labelWidth: 100,//配置界面修改的宽度是这个值
      isLabelWidth: true,
      hidden: false,
      dataBind: true,
      showPassword: false,
    }
  },
  {
    type: 'seriescolumnchart',
    name:'多系列柱状图',
    icon: 'icon-input',
    header: '多系列柱状图',
    bodyStyle: `{ padding: '20px' }`,
    shadow: 'always',
    options: {
      typeId: '2002',
      configure: [101, 201, 303],
      style: {
        width: '100%',//宽度默认100%,不要动
        height: '400px'//高度默认400px，不要修改，配置界面修改的高度就是这个值
      },
      defaultValue: {
        tooltip: {
          trigger: 'item'
        },
        title: {
          text: '多系列柱状图',
          left: 'center',
        },
        legend: {
          data: [],
          x: 'right',
          y: 'top',
        },
        xAxis: {
          type: 'category',
          data: [],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '',
            type: 'line',
            data: [],
          },
        ],
      },
      direction: {
        key: '',//配置的x轴
        value: '',//配置的y轴
        legend: '',//多系列参数
      },
      surface: {
        dataColumns: [],//查询后的列表展示
        dataResources: [],//查询后的数据展示
      },
      sqlstr: '',//查询sql语句
      dataSourceId: '',//数据源id
      required: false,
      requiredMessage: '',
      dataType: '',
      dataTypeCheck: false,
      dataTypeMessage: '',
      pattern: '',
      patternCheck: false,
      patternMessage: '',
      placeholder: '',
      customClass: '',
      disabled: false,
      labelWidth: 100,//配置界面修改的宽度是这个值
      isLabelWidth: true,
      hidden: false,
      dataBind: true,
      showPassword: false,
    }
  },
]

// 饼状图
export const piechartComponents = [
  {
    type: 'basepiechart',
    name:'基础饼状图',
    icon: 'icon-input',
    header: '基础饼状图',
    bodyStyle: `{ padding: '20px' }`,
    shadow: 'always',
    options: {
      typeId: '3001',
      configure: [101, 201, 302],
      style: {
        width: '100%',//宽度默认100%,不要动
        height: '400px'//高度默认400px，不要修改，配置界面修改的高度就是这个值
      },
      defaultValue: {
        tooltip: {
          trigger: 'item'
        },
        title: {
          text: '基础饼状图',
          left: 'center',
        },
        series: [
          {
            name: '',
            type: 'pie',
            data: [],
          },
        ],
      },
      direction: {
        key: '',//配置的x轴
        value: '',//配置的y轴
      },
      surface: {
        dataColumns: [],//查询后的列表展示
        dataResources: [],//查询后的数据展示
      },
      sqlstr: '',//查询sql语句
      dataSourceId: '',//数据源id
      required: false,
      requiredMessage: '',
      dataType: '',
      dataTypeCheck: false,
      dataTypeMessage: '',
      pattern: '',
      patternCheck: false,
      patternMessage: '',
      placeholder: '',
      customClass: '',
      disabled: false,
      labelWidth: 100,//配置界面修改的宽度是这个值
      isLabelWidth: true,
      hidden: false,
      dataBind: true,
      showPassword: false,
    }
  },
  {
    type: 'table_test',
    name:'表格测试',
    icon: 'icon-input',
    header: '表格',
    bodyStyle: `{ padding: '20px' }`,
    shadow: 'always',
    options: {
      typeId: '4001',
      configure: [101, 201, 401],
      style: {
        width: '100%',//宽度默认100%,不要动
        height: '400px'//高度默认400px，不要修改，配置界面修改的高度就是这个值
      },
      defaultValue: [],
      direction: {
        key: '',//配置的x轴
        value: '',//配置的y轴
      },
      surface: {
        dataColumns: [],//查询后的列表展示
        dataResources: [],//查询后的数据展示
      },
      sqlstr: '',//查询sql语句
      dataSourceId: '',//数据源id
      required: false,
      requiredMessage: '',
      dataType: '',
      dataTypeCheck: false,
      dataTypeMessage: '',
      pattern: '',
      patternCheck: false,
      patternMessage: '',
      placeholder: '',
      customClass: '',
      disabled: false,
      labelWidth: 100,//配置界面修改的宽度是这个值
      isLabelWidth: true,
      hidden: false,
      dataBind: true,
      showPassword: false,
    }
  },
]

// 布局字段
export const layoutComponents = [
  {
    type: 'grid',
    name:'栅格布局',
    icon: 'icon-RectangleCopy',
    columns: [
      {
        span: 12,
        xs: 12,
        sm: 12,
        md: 12,
        lg: 12,
        xl: 12,
        list: []
      },
      {
        span: 12,
        xs: 12,
        sm: 12,
        md: 12,
        lg: 12,
        xl: 12,
        list: []
      }
    ],
    options: {
      gutter: 0,
      justify: 'start',
      align: 'top',
      customClass: '',
      hidden: false,
      flex: true,
      responsive: false
    }
  },
  {
    type: 'tabs',
    name:'标签页',
    icon: 'icon-tabs',
    tabs: [
      {
        label: 'Tab 1',
        name: 'tab_1',
        list: []
      }
    ],
    options: {
      type: '',
      tabPosition: 'top',
      customClass: '',
      hidden: false,
    }
  },
  {
    type: 'divider',
    name:'分割线',
    icon: 'icon-fengexian',
    options: {
      hidden: false,
      contentPosition: 'left'
    }
  }
]
