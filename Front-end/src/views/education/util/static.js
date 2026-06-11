const buttonDescription = {
    profession: "新建专业",
    class: "新建班级",
}
const columns = {
    class: [
        {
          title: '班级名称',
          align: "center",
          dataIndex: 'class_name'
        },
        {
          title: '所属专业',
          align: "center",
          dataIndex: 'subject_name'
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'},
          align: "center",
          width: 150
    }],
    profession: [
        {
          title: '专业名称',
          align: "center",
          dataIndex: 'subject_name'
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'},
          align: "center",
          width: 150
    }],
}
const urls = {
    class:{
        // search: "/online/cgform/api/getData/e1bb9376a3694a128e6abc7cd4496cb0",
        search: '/online/cgreport/api/getColumnsAndData/1356838975552684034',
        fix: "/online/cgform/api/form/e1bb9376a3694a128e6abc7cd4496cb0",
        delete: "/online/cgform/api/form/e1bb9376a3694a128e6abc7cd4496cb0"
    },
    profession: {
        // search: "/online/cgform/api/getData/d9bb8f8f54084b66ba1dc08c2b95d2fe",
        search: '/online/cgreport/api/getColumnsAndData/1356848251444723713',
        fix: "online/cgform/api/form/d9bb8f8f54084b66ba1dc08c2b95d2fe",
        delete: "/online/cgform/api/form/d9bb8f8f54084b66ba1dc08c2b95d2fe"
    },
}
const formItems = {
  profession: [{
    key: "subject_name",
    placeholder: "请输入专业名称",
    title: "专业名称"
  }],
  class: [{
    key: "class_name",
    placeholder: "请输入班级名称",
    title: "班级名称"
  }]
}
export { buttonDescription, columns, urls, formItems }