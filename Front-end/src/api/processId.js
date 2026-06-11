//保证金存缴的流程id列表和分类id
export const depositList = ['10111', '10113', '10115', '10117'];
export const depositCategoryId = '1847453055727501313';

// //保证金使用的流程id列表和分类id
export const useList = ['10111', '10113', '10115', '10117', '10120'];
export const useCategoryId = '1847453556447707137';

// //保证金补缴的流程id列表和分类id
// //如果部署了新的流程，记得改flowBackPay.vue里开启的对应的流程id
export const backpayList = ['10111', '10113', '10115', '10117', '10120', '10126', '10128', '10130', '10132'];
export const backpayCategoryId = '1860939985686949889';

// //保证金存缴方式变更的流程id列表和分类id
export const changeList = ['10111', '10113', '10115', '10117', '10138', '10140', '10142', '10144'];
export const changeCategoryId = '1860602147955077121';

// //保证金保函更换的流程id列表和分类id
// //如果部署了新的流程，记得改flowExtend.vue里开启的对应的流程id
export const extendList = ['10111', '10113', '10115', '10117', '10150', '10152', '10154', '10156'];
export const extendCategoryId = '1867119925569568769';

// //保证金返还的流程id列表和分类id
export const returnList = ['10111', '10113', '10115', '10117', '10159'];
export const returnCategoryId = '1867119977859956738';



// 正式
// 1.替换List 2.替换ip 3.替换注册的流程id
// export const depositList = ['5', '7', '9', '11'];
// export const useList = ['5', '7', '9', '11', '14'];
// export const backpayList = ['5', '7', '9', '11', '14', '21', '23', '25', '27'];
// export const changeList = ['5', '7', '9', '11', '33', '35', '37', '39'];
// export const extendList = ['5', '7', '9', '11', '45', '47', '49', '51'];
// export const returnList = ['5', '7', '9', '11', '54'];