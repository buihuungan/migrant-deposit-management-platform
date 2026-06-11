<template>
  <div :style="{ width: data.config ? data.config.width : 500 }">
    <el-form
      ref="generateForm"
      :key="formKey"
      :class="{
        [data.config.customClass]: data.config.customClass ? true : false,
        'no-label-form': data.config.labelWidth === 0,
      }"
      label-suffix=": "
      :size="data.config.size"
      :model="models"
      :rules="rules"
      :label-position="data.config.labelPosition"
      :disabled="!edit"
      :label-width="data.config.labelWidth + 'px'"
    >
      <template v-for="item in data.list">
        <generate-col-item
          v-if="item.type === 'grid'"
          :key="item.key"
          :model.sync="models"
          :rules="rules"
          :element="item"
          :remote="remote"
          :blanks="blanks"
          :display="displayFields"
          @input-change="onInputChange"
          :edit="edit"
          :remote-option="remoteOption"
        >
          <template v-slot:[blank.name]="scope" v-for="blank in blanks">
            <slot :name="blank.name" :model="scope.model" />
          </template>
        </generate-col-item>

        <generate-tab-item
          v-else-if="item.type === 'tabs'"
          :key="item.key"
          :model.sync="models"
          :rules="rules"
          :element="item"
          :remote="remote"
          :blanks="blanks"
          :display="displayFields"
          @input-change="onInputChange"
          :edit="edit"
          :add-or-edit="addOrEdit"
          :remote-option="remoteOption"
        >
          <template v-slot:[blank.name]="scope" v-for="blank in blanks">
            <slot :name="blank.name" :model="scope.model" />
          </template>
        </generate-tab-item>

        <generate-form-item
          v-else
          :key="item.key"
          :models.sync="models"
          :rules="rules"
          :widget="item"
          :remote="remote"
          :blanks="blanks"
          :display="displayFields"
          :edit="edit"
          :remote-option="addOrEdit"
          :add-or-edit="addOrEdit"
          @input-change="onInputChange"
        >
          <template v-slot:[blank.name]="scope" v-for="blank in blanks">
            <slot :name="blank.name" :model="scope.model" />
          </template>
        </generate-form-item>
      </template>
    </el-form>
  </div>
</template>

<script>
import GenerateFormItem from './GenerateFormItem'
import GenerateColItem from './GenerateColItem'
import GenerateTabItem from './GenerateTabItem'
import _ from 'lodash'

export default {
  name: 'fm-generate-form',
  components: {
    GenerateFormItem,
    GenerateColItem,
    GenerateTabItem,
  },
  props: {
    data: {
      type: Object,
      default: () => ({
        /* 表单属性初始值 */ list: [],
        config: {
          labelWidth: 100,
          labelPosition: 'right',
          size: 'small',
          
          ui: 'element',
          layout: 'horizontal',
          labelCol: 3,
        },
      }),
    },
    remote: {
      type: Object,
      default: () => ({}),
    },
    value: {
      type: Object,
      default: () => ({}),
    },
    edit: {
      type: Boolean,
      default: true,
    },
    remoteOption: {
      type: Object,
      default: () => ({}),
    },
    addOrEdit: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      models: {},
      rules: {},
      blanks: [],
      displayFields: {},
      dataBindFields: [],
      generateShow: false,
      resetModels: {},
      formKey: new Date().getTime(),
      formValue: this.value,
    }
  },
  created() {
    this._initForm()
    if(!this.data.config){//点击新建this.data.config为未定义，导致上述引用控制台报错undefined
      this.data.config=''
    }
  },
  mounted() {
    // console.log('GenerateForm  models：', this.models)
    // console.log('GenerateForm  value：', this.value)
  },
  methods: {
    _initForm() {
      if (Object.keys(this.data).length) {
        this.generateModel(this.data.list)
      } else {
        this.generateModel([])
      }

      this.resetModels = _.cloneDeep(this.models)
    },

    generateModel1(genList) {
      for (let i = 0; i < genList.length; i++) {
        if (genList[i].type === 'grid') {
          this.displayFields[genList[i].model] = !genList[i].options.hidden
          genList[i].columns.forEach((item) => {
            this.generateModel(item.list)
          })
        } else if (genList[i].type === 'tabs') {
          genList[i].tabs.forEach((item) => {
            this.generateModel(item.list)
          })

          this.displayFields[genList[i].model] = !genList[i].options.hidden
          // console.log("generateModel函数打印this.displayFields[genList[i].model]",this.displayFields[genList[i].model]);
          // console.log("generateModel函数打印genList[i].options.hidden",genList[i].options.hidden);
        } else {
          if (Object.keys(this.formValue).indexOf(genList[i].model) >= 0) {
            // 处理老版本没有dataBind值的情况，默认绑定数据
            if (Object.keys(genList[i].options).indexOf('dataBind') < 0 || genList[i].options.dataBind) {
              this.models[genList[i].model] = this.formValue[genList[i].model]
              this.dataBindFields.push(genList[i].model)
            }

            this.displayFields[genList[i].model] = !genList[i].options.hidden

            if (genList[i].type === 'blank') {
              this.blanks.push({
                name: genList[i].model,
              })
            }
          } else {
            if (genList[i].type === 'blank') {
              // bound the default value
              if (Object.keys(genList[i].options).indexOf('dataBind') < 0 || genList[i].options.dataBind) {
                this.models[genList[i].model] =
                  genList[i].options.defaultType === 'String'
                    ? ''
                    : genList[i].options.defaultType === 'Object'
                    ? {}
                    : []
                this.dataBindFields.push(genList[i].model)
              }
              this.displayFields[genList[i].model] = !genList[i].options.hidden

              this.blanks.push({
                name: genList[i].model,
              })
            } else {
              if (Object.keys(genList[i].options).indexOf('dataBind') < 0 || genList[i].options.dataBind) {
                this.models[genList[i].model] = genList[i].type === 'table' ? [] : genList[i].options.defaultValue

                // 修复原程序缺陷：未对子表单的defaultValue赋值
                if (genList[i].type === 'table') {
                  this.models[genList[i].model] = genList[i].options.defaultValue
                }
                this.dataBindFields.push(genList[i].model)
              }
              this.displayFields[genList[i].model] = !genList[i].options.hidden
            }
          }

          genList[i].modelumns &&
            genList[i].modelumns.length &&
            genList[i].modelumns.forEach((item) => {
              if (item.type === 'blank') {
                this.blanks.push({
                  name: item.model,
                })
              }

              // 处理 rules
              if (this.rules[`${genList[i].model}.${item.model}`]) {
                this.rules[`${genList[i].model}.${item.model}`] = [
                  ...this.rules[`${genList[i].model}.${item.model}`],
                  ...item.rules.map((im) => {
                    if (im.pattern) {
                      return { ...im, pattern: eval(im.pattern) }
                    } else {
                      return { ...im }
                    }
                  }),
                ]
              } else {
                this.rules[`${genList[i].model}.${item.model}`] = [
                  ...item.rules.map((im) => {
                    if (im.pattern) {
                      return { ...im, pattern: eval(im.pattern) }
                    } else {
                      return { ...im }
                    }
                  }),
                ]
              }
            })

          if (this.rules[genList[i].model]) {
            this.rules[genList[i].model] = [
              ...this.rules[genList[i].model],
              ...genList[i].rules.map((item) => {
                if (item.pattern) {
                  return { ...item, pattern: eval(item.pattern) }
                } else {
                  return { ...item }
                }
              }),
            ]
          } else {
            this.rules[genList[i].model] = [
              ...genList[i].rules.map((item) => {
                if (item.pattern) {
                  return { ...item, pattern: eval(item.pattern) }
                } else {
                  return { ...item }
                }
              }),
            ]
          }
        }
      }
       //console.log("generateModel1",this.models);
    },

    generateModel(genList) {
      for (let i = 0; i < genList.length; i++) {
        if (genList[i].type === 'grid') {
          this.displayFields[genList[i].model] = !genList[i].options.hidden
          genList[i].columns.forEach((item) => {
            this.generateModel(item.list)
          })
        } else if (genList[i].type === 'tabs') {
          genList[i].tabs.forEach((item) => {
            this.generateModel(item.list)
          })
          this.displayFields[genList[i].model] = !genList[i].options.hidden
        } else {
          if (Object.keys(this.formValue).indexOf(genList[i].model) >= 0) {
            // 处理老版本没有dataBind值的情况，默认绑定数据
            if (Object.keys(genList[i].options).indexOf('dataBind') < 0 || genList[i].options.dataBind) {
              this.models[genList[i].model] = this.formValue[genList[i].model]
              this.dataBindFields.push(genList[i].model)
            }
            this.displayFields[genList[i].model] = !genList[i].options.hidden

            if (genList[i].type === 'blank') {
              this.blanks.push({
                name: genList[i].model,
              })
            }
          } else {
            if (genList[i].type === 'blank') {
              // bound the default value
              if (Object.keys(genList[i].options).indexOf('dataBind') < 0 || genList[i].options.dataBind) {
                this.models[genList[i].model] =
                  genList[i].options.defaultType === 'String'
                    ? ''
                    : genList[i].options.defaultType === 'Object'
                    ? {}
                    : []
                this.dataBindFields.push(genList[i].model)
              }
              this.displayFields[genList[i].model] = !genList[i].options.hidden

              this.blanks.push({
                name: genList[i].model,
              })
            } else {
              if (Object.keys(genList[i].options).indexOf('dataBind') < 0 || genList[i].options.dataBind) {
                this.models[genList[i].model] = genList[i].type === 'table' ? [] : genList[i].options.defaultValue

                // 修复原程序缺陷：未对子表单的defaultValue赋值
                if (genList[i].type === 'table') {
                  this.models[genList[i].model] = genList[i].options.defaultValue
                }
                this.dataBindFields.push(genList[i].model)
              }
              this.displayFields[genList[i].model] = !genList[i].options.hidden
            }
          }

          genList[i].modelumns &&
            genList[i].modelumns.length &&
            genList[i].modelumns.forEach((item) => {
              if (item.type === 'blank') {
                this.blanks.push({
                  name: item.model,
                })
              }

              // 处理 rules
              if (this.rules[`${genList[i].model}.${item.model}`]) {
                this.rules[`${genList[i].model}.${item.model}`] = [
                  ...this.rules[`${genList[i].model}.${item.model}`],
                  ...item.rules.map((im) => {
                    if (im.pattern) {
                      return { ...im, pattern: eval(im.pattern) }
                    } else {
                      return { ...im }
                    }
                  }),
                ]
              } else {
                this.rules[`${genList[i].model}.${item.model}`] = [
                  ...item.rules.map((im) => {
                    if (im.pattern) {
                      return { ...im, pattern: eval(im.pattern) }
                    } else {
                      return { ...im }
                    }
                  }),
                ]
              }
            })

          if (this.rules[genList[i].model]) {
            this.rules[genList[i].model] = [
              ...this.rules[genList[i].model],
              ...genList[i].rules.map((item) => {
                if (item.pattern) {
                  return { ...item, pattern: eval(item.pattern) }
                } else {
                  return { ...item }
                }
              }),
            ]
          } else {
            this.rules[genList[i].model] = [
              ...genList[i].rules.map((item) => {
                if (item.pattern) {
                  return { ...item, pattern: eval(item.pattern) }
                } else {
                  return { ...item }
                }
              }),
            ]
          }
        }
      }
     //console.log("generateModel",this.models);
      // console.log("aaaaaaaaaaaaaaaaaaaaathis.displayFields",this.displayFields);
    },

    _setDisabled(genList, fields, disabled) {
      for (let i = 0; i < genList.length; i++) {
        if (genList[i].type === 'grid') {
          /* 栅格布局 */
          genList[i].columns.forEach((item) => {
            this._setDisabled(item.list, fields, disabled)
          })
        } else if (genList[i].type === 'tabs') {
          /* 标签页 */
          genList[i].tabs.forEach((item) => {
            this._setDisabled(item.list, fields, disabled)
          })
        } else {
          if (fields.indexOf(genList[i].model) >= 0) {
            this.$set(genList[i].options, 'disabled', disabled)
          }
        }
      }
    },
    getData() {
      return new Promise((resolve, reject) => {
        this.$refs.generateForm.validate((valid) => {
          if (valid) {
            const resData = {}
            Object.keys(this.models).forEach((key) => {
              if (this.displayFields[key] && this.dataBindFields.indexOf(key) >= 0) {
                resData[key] = this.models[key]
              }
            })
            resolve(JSON.parse(JSON.stringify(resData)))
            resolve(resData)
          } else {
            // reject(new Error(this.$t('fm.message.validError')).message)
            reject(new Error('表单数据校验失败').message)
          }
        })
      })
    },
    reset() {
      this.setData(_.cloneDeep(this.resetModels))
      this.formKey = new Date().getTime()
    },
    onInputChange(value, field) {
      this.$emit('on-change', field, value, this.models)
      this.$emit(`on-${field}-change`, value)
    },
    display(fields) {
      Object.keys(this.displayFields).forEach((key) => {
        if (fields.indexOf(key) >= 0) {
          this.$set(this.displayFields, key, true)
        }
      })
      this.displayFields = { ...this.displayFields }
    },
    hide(fields) {
      Object.keys(this.displayFields).forEach((key) => {
        if (fields.indexOf(key) >= 0) {
          this.$set(this.displayFields, key, false)
        }
      })

      this.displayFields = { ...this.displayFields }
    },
    disabled(fields, disabled) {
      this._setDisabled(this.data.list, fields, disabled)
    },
    refresh() {},
    setData(value) {
      this.models = { ...this.models, ...value }
    },
  },
  watch: {
    data: {
      deep: true,
      handler(val) {
        this._initForm()
      },
    },

    models: {
      deep: true,
      handler(val) {    
        this.formValue = { ...val }   
      },
    },

    value: {
      deep: true,
      handler(val) {      
        // console.log('GenerateFrom界面监听value发生改变', val)
      },
    },
  },
}
</script>

<style lang="scss">
</style>
