<template>
  <a-drawer :title="title" :visible="visible" width="800" @close="handleCancel">
    <div class="sup_div2">
      <el-form :model="info" :rules="infoRules" ref="ref" style="width: 90%;">
        <el-form-item prop="supplierProductName">
          <label class="clearfix">
            <h3>
              方案名称
              <em>
                <font style="color:red">*</font>
              </em>
            </h3>
          </label>
          <el-input v-model="info.supplierProductName" placeholder="方案名称"></el-input>
        </el-form-item>
        <el-form-item prop="supplierProductType">
          <label class="clearfix">
            <h3>
              方案类型
              <em>
                <font style="color:red">*</font>
              </em>
            </h3>
          </label>
          <el-select v-model="info.supplierProductType" placeholder="请选择" :popper-append-to-body="false" style="width: 100%;">
            <el-option
              v-for="item in types"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <label class="clearfix">
            <h3>所属地区</h3>
          </label>
          <el-cascader
            style="width: 100%;"
            v-model="place_value"
            :options="places"
            :props="{ expandTrigger: 'hover' }"
            @change="handleChange"
          ></el-cascader>
        </el-form-item>
        <el-form-item prop="supplierName">
          <label class="clearfix">
            <h3>供应商名称</h3>
          </label>
          <el-input v-model="info.supplierName" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item prop="supplierLink">
          <label class="clearfix">
            <h3>访问/下载链接</h3>
          </label>
          <el-input v-model="info.supplierLink" placeholder="访问/下载链接"></el-input>
        </el-form-item>
        <el-form-item>
          <label class="clearfix">
            <h3>操作演示文档</h3>
          </label>
          <el-upload
            class="upload-demo"
            drag
            action="https://jsonplaceholder.typicode.com/posts/"
            multiple
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或
              <em>点击上传</em>
            </div>
            <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>

        <el-form-item prop="supplierDescription">
          <label class="clearfix">
            <h3>
              主要功能
              <em>
                <font style="color:red">*</font>
              </em>
            </h3>
          </label>
          <el-input
            type="textarea"
            :autosize="{ minRows: 4}"
            placeholder="请输入内容"
            v-model="info.supplierDescription"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <label class="clearfix">
            <h3>联系人及联系方式</h3>
          </label>
          <div v-for="(item,index) in contact" :key="index" style="margin-bottom:20px;">
            <el-form :model="item" :rules="contactrule">
              <el-row :gutter="20">
                <el-col :span="6">
                  <el-form-item prop="p">
                    <el-input placeholder="请输入联系人" v-model="item.p"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="16">
                  <el-form-item prop="num">
                    <el-input placeholder="请输入联系方式" v-model="item.num"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="2">
                  <div class="button_contact">
                    <i
                      v-if="index+1 == contact.length"
                      style="font-size: 30px; color:#AEAEAE;"
                      class="el-icon-circle-plus-outline"
                      @click="Addcontcat()"
                    ></i>
                    <i
                      v-if="index+1 < contact.length"
                      style="font-size: 30px; color:#AEAEAE;"
                      class="el-icon-remove-outline"
                      @click="Deletecontcat(index)"
                    ></i>
                  </div>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-form-item>
      </el-form>
      <div class="drawer-bootom-button" v-show="!disableSubmit" style="text-align: right;">
        <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
          <a-button style="margin-right: .8rem">取消</a-button>
        </a-popconfirm>
        <a-button @click="handleSubmit" type="primary">提交</a-button>
      </div>
    </div>
  </a-drawer>
</template>

<script>
import areaData from '../areaData.json'
import { c_getAction, c_postAction, c_httpAction, c_putAction, c_deleteAction } from '@/api/cmsApi'
import { Message } from 'element-ui';
export default {
  data() {
    var checkNumber = (rule, value, cb) => {
      const regNumber = /^[0-9]*$/
      if (regNumber.test(value)) {
        return cb(new Error('不要输入全数字数据'))
      }
      return cb()
    }
    var checklink = (rule, value, cb) => {
      const reglink = /(https?|ftp|file):\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]/
      if (reglink.test(value)) {
        return cb()
      }
      return cb(new Error('请输入正确的域名'))
    }
    var checkname = (rule, value, cb) => {
      const regNumber = /^[\u4e00-\u9fa5]{0,}$/
      if (regNumber.test(value)) {
        return cb()
      }
      return cb(new Error('请输入正确的姓名'))
    }
    var checkMoblie = (rule, value, cb) => {
      // const reglink = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/
      const reglink = /^1[3456789]\d{9}$/
      if (reglink.test(value)) {
        return cb()
      }
      return cb(new Error('请输入正确的电话号码'))
    }
    return {
      info: {
        supplierArea: '',
        supplierContact: '',
        supplierDescription: '',
        supplierDocument: '',
        supplierLink: '',
        supplierLogo: '',
        supplierName: '',
        supplierProductName: '',
        supplierProductType: '',
      },
      infoRules: {
        supplierProductName: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { validator: checkNumber, trigger: 'blur' },
        ],
        supplierProductType: [{ required: true, message: '请选择类型', trigger: 'change' }],
        supplierDescription: [
          { required: true, message: '请输入主要内容', trigger: 'blur' },
          { validator: checkNumber, trigger: 'blur' },
        ],
        supplierLink: [{ validator: checklink, trigger: 'blur' }],
      },
      ref: '',
      title: '',
      place_value: [],
      types: [
        {
          value: '0',
          label: '企业咨询',
        },
        {
          value: '1',
          label: '研发对接',
        },
        {
          value: '2',
          label: '技术服务',
        },
        {
          value: '3',
          label: '检测认证',
        },
        {
          value: '4',
          label: '设备共享',
        },
        {
          value: '5',
          label: '人才评价',
        },
        {
          value: '6',
          label: '市场服务',
        },
        {
          value: '7',
          label: '投融资供需对接',
        },
      ],
      contact: [{ p: '', num: '' }],
      contactrule: {
        p: [{ validator: checkname, trigger: 'blur' }],
        num: [{ validator: checkMoblie, trigger: 'blur' }],
      },
      places: [],
      visible: false,
      disableSubmit: false,
      login: '',
      addoredit: 0,
      user: '',
    }
  },
  created() {
    this.places = areaData
  },
  methods: {
    handleOpen() {
      this.visible = true
      this.addoredit = 1
      this.title = '新增'
    },
    handleCancel() {
      this.info = {}
      this.visible = false
    },
    editSupplier(record) {
      this.addoredit = 2
      this.visible = true
      this.title = '编辑'
      this.place_value = []
      this.info = record
      console.log(record)
      var place = record.supplierArea.split('/')
      for (var i = 0; i < this.places.length; i++) {
        if (this.places[i].label == place[0]) {
          this.place_value[0] = this.places[i].value
          for (var j = 0; j < this.places[i].children.length; j++) {
            if (this.places[i].children[j].label == place[1]) {
              this.place_value[1] = this.places[i].children[j].value
              for (var k = 0; k < this.places[i].children[j].children.length; k++) {
                if (this.places[i].children[j].children[k].label == place[2]) {
                  this.place_value[2] = this.places[i].children[j].children[k].value
                }
              }
            }
          }
        }
      }
    },
    Addcontcat() {
      console.log(this.contact)
      this.contact.push({ p: '', num: '' })
    },
    Deletecontcat(index) {
      this.contact.splice(index, 1)
    },
    getPlace(value) {
      var place = ''
      for (var i = 0; i < this.places.length; i++) {
        if (this.places[i].value == value[0]) {
          place += this.places[i].label
          for (var j = 0; j < this.places[i].children.length; j++) {
            if (this.places[i].children[j].value == value[1]) {
              place += '/' + this.places[i].children[j].label
              for (var k = 0; k < this.places[i].children[j].children.length; k++) {
                if (this.places[i].children[j].children[k].value == value[2]) {
                  place += '/' + this.places[i].children[j].children[k].label
                  console.log(place)
                  return place
                }
              }
            }
          }
        }
      }
    },
    handleSubmit() {
      this.$refs.ref.validate((vaild) => {
        if (!vaild) return
        var i
        this.info.supplierArea = this.handleChange(this.place_value)
        this.info.supplierContact = ''
        if (this.contact.length > 0) {
          for (i = 0; i < this.contact.length; i++) {
            this.info.supplierContact += this.contact[i].p + ' ' + this.contact[i].num + '\n'
          }
        }
        var _this = this
        if (this.addoredit === 1) {
          c_postAction('/user/supplier/supplierAdd', this.info).then(
            function (res) {
              _this.info = {
                supplierArea: '',
                supplierContact: '',
                supplierDescription: '',
                supplierDocument: '',
                supplierLink: '',
                supplierLogo: '',
                supplierName: '',
                supplierProductName: '',
                supplierProductType: '',
              }
              _this.place_value = ''
              _this.contact = [{ p: '', num: '' }]
            },
            function (err) {
              console.log(err)
            }
          )
        } else {
          c_postAction('/user/supplier/supplierUpdate', this.info).then(
            function (res) {
              _this.info = {
                supplierArea: '',
                supplierContact: '',
                supplierDescription: '',
                supplierDocument: '',
                supplierLink: '',
                supplierLogo: '',
                supplierName: '',
                supplierProductName: '',
                supplierProductType: '',
              }
              _this.place_value = ''
              _this.contact = [{ p: '', num: '' }]
            },
            function (err) {
              console.log(err)
            }
          )
        }
      })
      this.info = {}
      this.visible = false
      this.$emit('ok')
    },
    handleChange(value) {
      var place = ''
      for (var i = 0; i < this.places.length; i++) {
        if (this.places[i].value == value[0]) {
          place += this.places[i].label
          for (var j = 0; j < this.places[i].children.length; j++) {
            if (this.places[i].children[j].value == value[1]) {
              place += '/' + this.places[i].children[j].label
              for (var k = 0; k < this.places[i].children[j].children.length; k++) {
                if (this.places[i].children[j].children[k].value == value[2]) {
                  place += '/' + this.places[i].children[j].children[k].label
                  console.log(place)
                  return place
                }
              }
            }
          }
        }
      }
    },
  },
}
</script>

<style scoped>
.el-popper {
  z-index: 99999999999999 !important;
}
.el-select-dropdown__list{
  display:grid !important;
}
.el-select-dropdown{
  position: absolute !important;
  top:30px !important;
  left: 0px !important;
}
</style>