import axios from "axios";
import api from './index'
/**
 * 自定义上传图片插件
 */
class MyUploadAdapter {
  constructor(loader) {
    this.loader = loader;
  }

  async upload() {
    const data = new FormData();
    // data.append("file", await this.loader.file);
    data.append("multipartFile", await this.loader.file);
    const res = await axios({
      url: api.server_url + api.global_plm_baseURL + '/common/upload/',
      method: "POST",
      data,
      withCredentials: true, // true 为不允许带 token, false 为允许
    });

    console.log(res.data);
    // 后台返回数据：
    // {"code":0,"msg":"success","data":{"url":"/upload/struts2.jpeg"}}

    // 方法返回数据格式： {default: "url"}
    return {
      // default: res.data.data.url,
      '800': api.server_url + api.global_plm_baseURL + '/common/static' + res.data.result.dbpath
    };
  }
}

export default MyUploadAdapter;