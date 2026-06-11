<template>
  <a-drawer :title="title" :visible="visible" width="800" @close="handleCancel">
    <div class="sup_div2">
      <el-form style="width: 90%;" :model="info" :rules="rules" ref="ref">
        <el-form-item prop="demanderTitle">
          <label class="clearfix">
            <h3>
              需求主题
              <em>
                <font style="color:red">*</font>
              </em>
            </h3>
          </label>
          <el-input v-model="info.demanderTitle" placeholder="主题名称"></el-input>
        </el-form-item>

        <el-form-item prop="demanderName">
          <label class="clearfix">
            <h3>
              需求方名称
              <em>
                <font style="color:red">*</font>
              </em>
            </h3>
          </label>
          <el-input v-model="info.demanderName" placeholder="需求方名称"></el-input>
        </el-form-item>

        <el-form-item prop="area">
          <label class="clearfix">
            <h3>需求方地址</h3>
          </label>
          <el-cascader
            style="width: 100%;"
            v-model="place_value"
            :options="places"
            :props="{ expandTrigger: 'hover' }"
          ></el-cascader>
        </el-form-item>

        <el-form-item prop="intype">
          <label class="clearfix">
            <h3>行业类型</h3>
          </label>
          <el-cascader
            style="width: 100%;"
            v-model="type_value"
            :options="types3"
            :props="{ expandTrigger: 'hover' }"
          ></el-cascader>
        </el-form-item>

        <el-form-item prop="demanderType">
          <label class="clearfix">
            <h3>需求类型</h3>

            <el-select style="width: 100%;" v-model="info.demanderType" :popper-append-to-body="false" placeholder="请选择">
              <el-option
                style="width: 100%;"
                v-for="item in types1"
                :key="item.value"
                :label="item.label"
                :value="item.label"
              ></el-option>
            </el-select>
          </label>
        </el-form-item>

        <el-form-item prop="demanderProperty">
          <label class="clearfix">
            <h3>需求方单位属性</h3>
          </label>
          <el-select style="width: 100%;" v-model="info.demanderProperty" :popper-append-to-body="false" placeholder="请选择">
            <el-option
              style="width: 100%;"
              v-for="item in types"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="time">
          <label class="clearfix">
            <h3>发布时间</h3>
          </label>
          <el-input v-model="time"></el-input>
        </el-form-item>

        <el-form-item prop="demanderDescription">
          <label class="clearfix">
            <h3>
              内容描述
              <em>
                <font style="color:red">*</font>
              </em>
            </h3>
          </label>
          <el-input
            style="width: 100%;"
            type="textarea"
            :autosize="{ minRows: 4}"
            placeholder="请输入内容"
            v-model="info.demanderDescription"
          ></el-input>
        </el-form-item>

        <el-form-item prop="demanderCreditCode">
          <label class="clearfix">
            <h3>企业统一社会信用代码</h3>
          </label>
          <el-input v-model="info.demanderCreditCode" placeholder="请输入"></el-input>
        </el-form-item>

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
export default {
  data() {
    var checkNumber = (rule, value, cb) => {
      const regNumber = /^[0-9]*$/
      if (regNumber.test(value)) {
        return cb(new Error('不要输入全数字数据'))
      }
      return cb()
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
        demanderArea: '',
        demanderContact: '',
        demanderCreateTime: '',
        demanderCreditCode: '',
        demanderDescription: '',
        demanderIndustryType: '',
        demanderLogo: '',
        demanderName: '',
        demanderProperty: '',
        demanderTitle: '',
        demanderType: '',
      },
      rules: {
        demanderTitle: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { validator: checkNumber, trigger: 'blur' },
        ],
        demanderName: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { validator: checkNumber, trigger: 'blur' },
        ],
        demanderDescription: [
          { required: true, message: '请输入主要内容', trigger: 'blur' },
          { validator: checkNumber, trigger: 'blur' },
        ],
      },
      ref: '',
      disableSubmit: false,
      types1: [
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
      types3: [
        {
          value: '0',
          label: '原材料工业',
          children: [
            {
              value: '0',
              label: '钢铁',
            },
            {
              value: '1',
              label: '有色',
            },
            {
              value: '2',
              label: '石化化工',
            },
            {
              value: '3',
              label: '建材',
            },
            {
              value: '4',
              label: '稀土',
            },
          ],
        },
        {
          value: '1',
          label: '装备工业',
          children: [
            {
              value: '0',
              label: '机械',
            },
            {
              value: '1',
              label: '汽车',
            },
            {
              value: '2',
              label: '民用船舶',
            },
          ],
        },
        {
          value: '2',
          label: '消费品工业',
          children: [
            {
              value: '0',
              label: '轻工',
            },
            {
              value: '1',
              label: '纺织',
            },
            {
              value: '2',
              label: '食品',
            },
            {
              value: '3',
              label: '家电',
            },
          ],
        },
        {
          value: '3',
          label: '通信业',
          children: [
            {
              value: '0',
              label: '固定电话',
            },
            {
              value: '1',
              label: '移动通信业务统计数据',
            },
          ],
        },
        {
          value: '4',
          label: '电子信息',
          children: [
            {
              value: '0',
              label: '电子信息产品固定资产投资',
            },
            {
              value: '1',
              label: '制造',
            },
            {
              value: '2',
              label: '进出口统计数据',
            },
          ],
        },
        {
          value: '5',
          label: '软件业',
          children: [
            {
              value: '0',
              label: '软件和信息服务业经济运行统计数据',
            },
          ],
        },
      ],
      types: [
        {
          value: '0',
          label: '原材料工业',
        },
        {
          value: '1',
          label: '装备工业',
        },
        {
          value: '2',
          label: '通信业',
        },
        {
          value: '3',
          label: '电子信息',
        },
        {
          value: '4',
          label: '软件业',
        },
      ],
      place_value: [],
      type_value: [],
      visible: false,
      num: 0,
      contact: [{ p: '', num: '' }],
      contactrule: {
        p: [{ validator: checkname, trigger: 'blur' }],
        num: [{ validator: checkMoblie, trigger: 'blur' }],
      },
      time: '',
      places: [],
      login: '',
      user: '',
      addoredit: 0,
      title: '',
    }
  },
  created() {
    this.time = this.dateFormat()
    this.places = areaData
  },
  methods: {
    handleOpen() {
      this.visible = true
      this.addoredit = 1
      this.title = '新增'
    },
    handleCancel() {
      this.place_value = []
      this.type_value = []
      this.contact = []
      this.visible = false
      this.info = {}
    },
    editDemander(record) {
      this.addoredit = 2
      this.time = this.dateFormat()
      this.title = '编辑'
      this.place_value = []
      this.type_value = []
      this.contact = []
      this.visible = true
      this.info = record
      console.log(record)
      if (!record.demanderArea) {
        this.place_value = []
      } else {
        var place = record.demanderArea.split('/')
      }
      if (!record.demanderIndustryType) {
        this.type_value = []
      } else {
        var type = record.demanderIndustryType.split('/')
      }
      if (!record.demanderContact) {
        this.contact = []
      } else {
        var contact = record.demanderContact.split(/[\s\n]/)
      }
      if (record.demanderContact) {
        for (var i = 0; i < contact.length-1; i = i + 2) {
          this.contact[i / 2] = { p: '', num: '' }
          this.contact[i / 2].p = contact[i]
          this.contact[i / 2].num = contact[i + 1]
        }
      }

      if (record.demanderArea) {
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
      }

      if (record.demanderIndustryType) {
        for (var i = 0; i < this.types3.length; i++) {
          if (this.types3[i].label == type[0]) {
            this.type_value[0] = this.types3[i].value
            for (var j = 0; j < this.types3[i].children.length; j++) {
              if (this.types3[i].children[j].label == type[1]) {
                this.type_value[1] = this.types3[i].children[j].value
              }
            }
          }
        }
      }
    },
    dateFormat() {
      var date = new Date()
      var year = date.getFullYear()
      /* 在日期格式中，月份是从0开始的，因此要加0
       * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
       * */
      var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
      var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
      var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
      var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
      var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
      // 拼接
      return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
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
    getIndustryType(value) {
      var type = ''
      for (var i = 0; i < this.types3.length; i++) {
        if (this.types3[i].value == value[0]) {
          console.log(this.types3[i].label)
          type += this.types3[i].label
          for (var j = 0; j < this.types3[i].children.length; j++) {
            if (this.types3[i].children[j].value == value[1]) {
              type += '/' + this.types3[i].children[j].label
              return type
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
    handleSubmit() {
      this.$refs.ref.validate((vaild) => {
        if (!vaild) return
        var i
        console.log(this.place_value)
        this.info.demanderArea = this.getPlace(this.place_value)
        this.info.demanderContact = ''
        if (this.contact.length > 0) {
          for (i = 0; i < this.contact.length; i++) {
            this.info.demanderContact += this.contact[i].p + ' ' + this.contact[i].num + '\n'
          }
        }
        this.info.demanderIndustryType = this.getIndustryType(this.type_value)
        var date = new Date(this.time)
        this.info.demanderCreateTime = date.getTime()
        console.log(this.info)
        var _this = this
        if (this.addoredit === 1) {
          c_postAction('/user/demander/demanderAdd', this.info).then(
            function (res) {
              _this.time = _this.dateFormat()
              _this.info.demanderCreditCode = ''
              _this.info.demanderDescription = ''
              _this.info.demanderIndustryType = ''
              _this.info.demanderLogo = ''
              _this.info.demanderName = ''
              _this.info.demanderProperty = ''
              _this.info.demanderTitle = ''
              _this.info.demanderType = ''
              _this.place_value = ''
              _this.contact = [{ p: '', num: '' }]
              _this.type_value = ''
            },
            function (err) {
              console.log(err)
            }
          )
        } else {
          console.log(this.info)
          c_postAction('/user/demander/demanderUpdate', this.info).then(
            function (res) {
              _this.time = _this.dateFormat()
              _this.info.demanderCreditCode = ''
              _this.info.demanderDescription = ''
              _this.info.demanderIndustryType = ''
              _this.info.demanderLogo = ''
              _this.info.demanderName = ''
              _this.info.demanderProperty = ''
              _this.info.demanderTitle = ''
              _this.info.demanderType = ''
              _this.place_value = ''
              _this.contact = [{ p: '', num: '' }]
              _this.type_value = ''
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

