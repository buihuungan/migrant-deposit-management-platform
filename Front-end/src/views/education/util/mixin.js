import { urls, columns } from './static'
import { getAction } from '@/api/manage'
import { shallowClone } from './util'
import { TENANT_ID } from '@/store/mutation-types'
import Vue from 'vue'
const getTree = {
  methods: {
    searchProfession(id) {
      let params = {
        order: 'desc',
        pageNo: 1,
        pageSize: 100000,
        tenant_id: Vue.ls.get(TENANT_ID),
        sys_org_code: id
      }
      const { search } = urls['profession']
      this.columns = columns['profession']
      return getAction(search, params)
    },
    searchClass(id, sys_org_code) {
      let params = {
        order: 'desc',
        pageNo: 1,
        pageSize: 100000,
        tenant_id: Vue.ls.get(TENANT_ID),
        sys_org_code: sys_org_code,
        subject_code: id
      }
      const { search } = urls['class']
      this.columns = columns['profession']
      return getAction(search, params)
    },
    get2Tree(axiosArr, result, message, type) {
      let children = result.length > 0 ? shallowClone(result[0]) : {}
      //根据机构获取专业
      Promise.all(axiosArr).then(resArr => {
        resArr.forEach((resItem, index) => {
          if (resItem && resItem.success) {
            children.chidren = null
            children.parentId = result[index].id
            let treeItems = []
            const { records } = resItem.result.data
            records.forEach(item => {
              let treeItem = shallowClone(children)
              treeItem.id = treeItem.key = treeItem.value = item.id
              treeItem.isLeaf = true
              treeItem.orgType = '2'
              treeItem.title = item.subject_name
              treeItem.departName = item.subject_name
              ;(treeItem.sys_org_code = item.sys_org_code),
                (treeItem.tenant_id = item.tenant_id),
                (treeItem.type = 'profession')
              treeItems.push(treeItem)
            })
            result[index].children = treeItems.length > 0 ? treeItems : null
            this.departTree.push(result[index])
            if (type === 'class') {
              const { children } = result[index]
              if (children) {
                children.forEach(item => {
                  this.departIdList.push(item.id)
                })
              }
            }
            if (type === 'teacher') {
              const { children } = result[index]
              if (children) {
                children.forEach(item => {
                  this.departIdList.push(item.id)
                })
              }
            }
            this.setThisExpandedKeys(result[index])
          }
        })
        this.userIdentity = message
        this.loading = false
      })
    },

    get3Tree(axiosArr, result, message) {
      // let children = result.length > 0 ? shallowClone(result[0]) : {};
      let classArr = []
      //根据机构获取专业
      Promise.all(axiosArr).then(resArr => {
        resArr.forEach((resItem, index) => {
          if (resItem && resItem.success) {
            let treeItems = []
            const { records } = resItem.result.data
            records.forEach(item => {
              let treeItem = {
                key: item.id,
                id: item.id,
                title: item.subject_name,
                sys_org_code: item.sys_org_code,
                tenant_id: item.tenant_id,
                children: null,
                parentId: result[index].id,
                isLeaf: true,
                type: 'profess'
              }
              classArr.push(this.searchClass(item.id, item.sys_org_code))
              treeItems.push(treeItem)
            })

            result[index].children = treeItems.length > 0 ? treeItems : null
            result[index].isLeaf = treeItems.length > 0 ? false : true
            this.setThisExpandedKeys(result[index])
          }
        })

        Promise.all(classArr).then(resItems => {
          resItems.forEach((res, index) => {
            if (res && res.success) {
              let subItems = []
              const { records } = res.result.data
              records.forEach(item => {
                let treeItem = {
                  key: item.id,
                  id: item.id,
                  title: item.class_name,
                  sys_org_code: item.sys_org_code,
                  subject_code: item.subject_code,
                  subject_name: item.subject_name,
                  tenant_id: item.tenant_id,
                  children: null,
                  parentId: item.subject_code,
                  isLeaf: true,
                  type: 'class'
                }
                this.departIdList.push(item.id)

                subItems.push(treeItem)
              })

              if (result && result.length && subItems.length) {
                for (let i = 0; i < result.length; i++) {
                  let flag = false
                  if (result[i].children && result[i].children.length) {
                    for (let j = 0; j < result[i].children.length; j++) {
                      if (result[i].children[j].id == subItems[0].subject_code) {
                        result[i].children[j].children = subItems
                        result[i].children[j].isLeaf = false
                        flag = true
                        break
                      }
                    }
                    if (flag) {
                      break
                    }
                  }
                }
              }
            }
          })

          this.departTree = result
        })
        this.userIdentity = message
        this.loading = false
      })
    }
  }
}
export { getTree }
