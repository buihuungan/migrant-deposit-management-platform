const path = require('path')
const CompressionPlugin = require('compression-webpack-plugin')
const api = require('./src/api')

function resolve(dir) {
  return path.join(__dirname, dir)
}

// vue.config.js
module.exports = {
  assetsDir: 'static',
  /*
    Vue-cli3:
    Crashed when using Webpack `import()` #2463
    https://github.com/vuejs/vue-cli/issues/2463
   */
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  // 多入口配置
  // pages: {
  //   index: {
  //     entry: 'src/main.js',
  //     template: 'public/index.html',
  //     filename: 'index.html',
  //   }
  // },

  // 打包app时放开该配置
  // publicPath:'././',
  // publicPath:'/llm-kg/subject-one/stj-front',
  // publicPath:'./llm-kg/subject-one/stj-front/',
  // publicPath:'././llm-kg/subject-one/stj-front/',
  // publicPath:'/admin',
  configureWebpack: config => {
    // 生产环境取消 console.log
    if (process.env.NODE_ENV === 'production') {
      config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true
    }
  },
  chainWebpack: (config) => {
    config.resolve.alias
      .set('@$', resolve('src'))
      .set('@api', resolve('src/api'))
      .set('@assets', resolve('src/assets'))
      .set('@comp', resolve('src/components'))
      .set('@views', resolve('src/views'))
      .set('@layout', resolve('src/layout'))
      .set('@static', resolve('src/static'))
      .set('@mobile', resolve('src/modules/mobile'))

    // 生产环境，开启js\css压缩
    if (process.env.NODE_ENV === 'production') {
      config.plugin('compressionPlugin').use(new CompressionPlugin({
        test: /\.(js|css|less)$/, // 匹配文件名
        threshold: 10240, // 对超过10k的数据压缩
        deleteOriginalAssets: false // 不删除源文件
      }))
    }

    // 配置 webpack 识别 markdown 为普通的文件
    config.module
      .rule('markdown')
      .test(/\.md$/)
      .use()
      .loader('file-loader')
      .end()

    // 编译vxe-table包里的es6代码，解决IE11兼容问题
    config.module
      .rule('vxe')
      .test(/\.js$/)
      .include
      .add(resolve('node_modules/vxe-table'))
      .add(resolve('node_modules/vxe-table-plugin-antd'))
      .end()
      .use()
      .loader('babel-loader')
      .end()

    config.module
      .rule('')
      .test(/mxClient\.js$/)
      .use('exports-loader')
      .loader(
        'exports-loader?mxClient,mxToolbar,mxConnectionHandler,mxEllipse,mxConnectionConstraint,mxWindow,' +
        'mxObjectCodec,mxGraphModel,mxActor,mxPopupMenu,mxShape,mxEventObject,mxGraph,mxPopupMenuHandler,mxPrintPreview,' +
        'mxEventSource,mxRectangle,mxVertexHandler,mxMouseEvent,mxGraphView,mxCodecRegistry,mxImage,mxGeometry,' +
        'mxRubberband,mxConstraintHandler,mxKeyHandler,mxDragSource,mxGraphModel,mxEvent,mxUtils,mxEvent,mxCodec,mxCell,' +
        'mxConstants,mxPoint,mxGraphHandler,mxCylinder,mxCellRenderer,mxEvent,mxUndoManager'
      )
      .end()
  },

  css: {
    // 禁用css提取
    extract: false,
    loaderOptions: {
      css: {
        // 禁用css压缩
        minimize: false
      },
      less: {
        modifyVars: {
          /* less 变量覆盖，用于自定义 ant design 主题 */
          'primary-color': '#1890FF',
          'link-color': '#1890FF',
          'border-radius-base': '4px'
        },
        javascriptEnabled: true
      }
    }
  },

  devServer: {
    port: 3000,
    proxy: {
      '/apilocal': {
        target: api.server_url + '/apilocal',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/apilocal': ''
        }
      },
      '/edu': {
        target: api.server_url + '/edu',
        // target: 'http://10.162.34.242:8012',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/edu': ''
        }
      },
      '/poli': {
        target: api.server_url + '/poli',
        // target: 'http://10.162.34.242:8012',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/poli': ''
        }
      },
      '/apiUpload': {
        target: api.server_url,
        // target: 'http://10.162.34.242:8012',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/apiUpload': ''
        }
      },
      /* '/api': {
         target: 'https://mock.ihx.me/mock/5baf3052f7da7e07e04a5116/antd-pro', //mock API接口系统
         ws: false,
         changeOrigin: true,
         pathRewrite: {
           '/jeecg-boot': ''  //默认所有请求都加了jeecg-boot前缀，需要去掉
         }
       }, */
      '/jeecg-boot': {
        target: 'http://121.199.72.67:10050/stj', // 请求本地 需要jeecg-boot后台项目
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/jeecg-boot': ''
        }
      },
      '/jeecg-stj': {
        target: 'https://ai.wust.edu.cn', // 请求本地 需要jeecg-boot后台项目
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/jeecg-stj': ''
        }
      },
      '/api2': {
        target: api.server_url + '/identity',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/api2': ''
        }
      },
      '/plm': {
        target: api.server_url + '/plm',
        //target:'http://192.168.137.140:37039',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/plm': ''
        }
      },
      '/order': {
        target: api.server_url + '/order',
        //target:'http://10.162.35.2:41684',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/order': ''
        }
      },
      '/crm': {
        target: api.server_url + '/crm',
        //target:'http://10.162.35.2:41684',
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/crm': ''
        }
      },
      // '/newworkflow': {
      //   target: 'http://202.114.252.2:37191',
      //   //target:'http://10.162.35.2:41684',
      //   ws: false,
      //   changeOrigin: true,
      //   pathRewrite: {
      //     '/newworkflow': ''
      //   }
      // },
      // '/coursewareengine': {
      //   target: api.server_url+'/coursewareengine',
      //   //target:'http://10.162.35.2:41684',
      //   ws: false,
      //   changeOrigin: true,
      //   pathRewrite: {
      //     '/coursewareengine': ''
      //   }
      // },
      '/ada-api': {
        target: api.server_url + '/ada-api',
        //target:'http://10.162.35.2:41684',.
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/ada-api': ''
        }
      },
      // '/dataapi': {
      //   target: 'http://fh.trumbai.cn/api/dataapi',
      //   changeOrigin: true,
      //   ws: false,
      //   pathRewrite: {
      //     '/dataapi': ''
      //   }
      // },
      '/wareFilter': {
        target: 'http://223.76.124.14:58026/ClassWebService.asmx',
        changeOrigin: true,
        ws: false,
        pathRewrite: {
          '/wareFilter': ''
        }
      },
    }
  },

  lintOnSave: false
}
