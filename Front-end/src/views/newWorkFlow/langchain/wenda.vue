<template>
    <div class="full-screen">
        <div class="left">
            <div style="display: flex; justify-content: center;">
                <div class="circle "></div>
            </div>
            <h3 style="text-align: center;font-weight: 800;font-size: 18px;margin-top: 10px;">流程系统（大模型标题）</h3>
            <div class="module active" @click="changeModule($event.target.innerText)">流程问答</div>
            <div class="module" @click="changeModule($event.target.innerText)">案例检索</div>
            <div class="module" @click="changeModule($event.target.innerText)">价值评估</div>
        </div>
        <div class="right">
            <div class="rightbgk"></div>
            <div class="welcome">欢迎您，{{user}}</div>
            <div style="display: flex; justify-content: center; height: 30%;" v-if="!isStart">
                <div class="container">
                    <div class="example" style="color: black; position: relative;"><span class="promptword"></span><span class="try"><a href="javascript:void(0)" @click="handleTry($event.target.parentElement.parentElement.innerText)" :disabled="loading">试一下</a></span></div>
                    <div class="example" style="color: black; position: relative;"><span class="promptword"></span><span class="try"><a href="javascript:void(0)" @click="handleTry($event.target.parentElement.parentElement.innerText)">试一下</a></span></div>
                    <div class="example" style="color: black; position: relative;"><span class="promptword"></span><span class="try"><a href="javascript:void(0)" @click="handleTry($event.target.parentElement.parentElement.innerText)">试一下</a></span></div>
                </div>
            </div>
            <div style="display: flex; justify-content: center;" v-else>
                <div  class="title"><span class="name">执法流程系统</span><span class="mx-1" size="small" style="margin-left: 20px; font-size: 16px;">{{ active }}</span></div>
            </div>
            
            <div style="display: flex; justify-content: center; height: 60%;">
                <div class="qa-list" ref="scrollbar">
                    <ul class="infinite-list">
                        <li v-for="i in 0" :key="i" class="infinite-list-item">{{ i }}</li>
                        <li v-for="(item, index) in questions" :key="index" :class="(index % 2 === 0) ? 'infinite-list-item2' : 'infinite-list-item'">{{ item }}</li>
                    </ul>
                </div>
            </div>
            <div style="display: flex; justify-content: center;">
                <div class="input-container">
                    <div class="input-box">
                        <textarea v-model="textarea" id="textplaceholder" placeholder="请输入您的问题" @keyup.enter="handleEnter"></textarea>
                        <img src="../../../assets/jiantou.png" @click="handleEnter">
                    </div>
                </div>
            </div>
        </div>
    </div>
  </template>
  
  <script>
//   import axios from 'axios';
  import {ref} from 'vue'
import { Message } from 'element-ui'
import { mapActions } from 'vuex'
import { axios } from '@/utils/request'

  
  export default {
    name:'LoginIndex',
    data() {
        return {
            questions: [],
            loading: false,
            textarea: '',
            queryInfo: {
                "query": "",
                "knowledge_base_name": "keti_1",
                "top_k": 3,
                "score_threshold": 1,
                "stream": false,
                "model_name": "chatglm3-6b",
                "temperature": 0.7,
                "max_tokens": 0,
                "prompt_name": "default"
            },
            // scrollbar: ref(),
            isStart: false,
            active: "流程问答",
            user:'',
            _token:'',
            promptWord: {
                    '流程问答':['请问农业种质涉假案件办案流程是什么？','流程问答提示词2（案例知识库的RAG大模型）','流程问答提示词3（提示模板直接调GLM）'],
                    '案例检索':['案例检索提示词1（基于执法流程语料库）','案例检索提示词2（案例知识库的RAG大模型）','案例检索提示词3（提示模板直接调GLM）'],
                    '价值评估':['价值评估提示词1（基于执法流程语料库）','价值评估提示词2（案例知识库的RAG大模型）','价值评估提示词3（提示模板直接调GLM）'],
                },
            queryType: {
                '流程问答': {
                    "knowledge_base_name": "keti_1",
                    "top_k": 3,
                    "score_threshold": 1,
                    "stream": false,
                    "model_name": "chatglm3-6b",
                    "temperature": 0.7,
                    "max_tokens": 0,
                    "prompt_name": "default"
                },
                '案例检索': {
                    "knowledge_base_name": "keti_4",
                    "top_k": 3,
                    "score_threshold": 1,
                    "stream": false,
                    "model_name": "chatglm3-6b",
                    "temperature": 0.7,
                    "max_tokens": 0,
                    "prompt_name": "default"
                },
                '价值评估': {
                    "knowledge_base_name": "keti_1",
                    "top_k": 3,
                    "score_threshold": 1,
                    "stream": false,
                    "model_name": "chatglm3-6b",
                    "temperature": 0.7,
                    "max_tokens": 0,
                    "prompt_name": "default"
                },
            }
        };
    },
    mounted() {
        this.init();
    },
    methods: {
        ...mapActions(['Login', 'Logout', 'PhoneLogin']),
        init() {
            this.validToken();
            this.setPromptWord(this.promptWord['流程问答']);
        },
        handleEnter() {
            if (this.loading) {
                Message({
                        showClose: true,
                        message: '正在思考中，请稍后...',
                        type: 'warning',
                        })
                return;
            }
            this.questions.push(this.textarea);
            this.queryInfo.query = this.textarea;
            this.query();
            this.textarea = '';
        },
        handleTry(val) {
            if (this.loading) return;
            let _question = val.replace("试一下", "").trim();
            this.questions.push(_question);
            this.queryInfo.query = _question;
            this.query();
        },
        query() {
            if (this.loading) {
                Message({
                        showClose: true,
                        message: '正在思考中，请稍后...',
                        type: 'warning',
                        })
                return;
            }
            this.loading = true;
            this.isStart = true;
            let _params = {
                params: JSON.stringify(this.queryInfo)
            };
            this.questions.push("正在思考...");
            setTimeout(() => {
                this.$refs.scrollbar.scrollTop = this.$refs.scrollbar.scrollHeight;
            }, 50);
            axios({
                method: 'POST',
                // url: "https://ai.wust.edu.cn/llm-kg/subject-one/newworkflow-stj/stj/llm/getFromChangJiang",
                url: "https://ai.wust.edu.cn/llm-kg/subject-one/jeecg-stj/stj/llm/getFromChangJiang",
                // url: "http://14.55.6.35:8082/stj/llm/getFromChangJiang",
                params: _params,
            }).then((response) => {
                const data = JSON.parse(response.result.replace("data: ", ""));
                const answer = data.answer;
                console.log('answer', answer);
                this.questions.pop();
                this.questions.push(answer);
  
                setTimeout(() => {
                    this.$refs.scrollbar.scrollTop = this.$refs.scrollbar.scrollHeight;
                }, 50);
                this.loading = false;
            }).catch((e) => {
                // location.reload();
            })
        },
        changeModule(type) {
            if (this.loading) {
                Message({
                        showClose: true,
                        message: '正在思考中，请稍后...',
                        type: 'warning',
                        })
                return;
            }
            this.isStart = false;

            setTimeout(() => {
                // 重新展示提示词
                this.setPromptWord(this.promptWord[type]);
                // 修改对应查询参数
                for (let key in this.queryType[type]) {
                    if (this.queryInfo.hasOwnProperty(key)) {
                        this.queryInfo[key] = this.queryType[type][key];
                    }
                }
            }, 100);
            this.active = type;
            // 改变选中元素的背景色
            let modules = document.querySelectorAll('.module');
            for (let el of modules) {
                el.style.backgroundColor = '#F5F5F5';
            }
            event.target.style.backgroundColor = '#CCD0FF';
            // 清空问答文本
            this.questions.length = 0;
        },
        setPromptWord(arr) {
            let examples = document.querySelectorAll('.promptword');
            arr.forEach((item, index) => {
                examples[index].innerHTML = item;
            });
            
        },
        validToken() {
            let urlHasToken = false;    // 用于判断url中是否存在token
            // 首先检查url是否有token
            const url = window.location.href;
            // const params = new URLSearchParams(new URL(url).search);
            // this._token = params.get('token');
            const regex = /token=([^&]+)/;
            const match = url.match(regex);

            if (match) {
                this._token = match[1];
                urlHasToken = true;
                history.pushState({}, '', url.replace("?token=" + this._token,""));
            }

            console.log("Token:", this._token); // 输出获取到的token值

            // z再检查本地是否有token
            if(!this._token) {
                let local_token = JSON.parse(localStorage.getItem("pro__Access-Token"));
                if(local_token && local_token.hasOwnProperty('value')) {
                    this._token = local_token.value;
                }
            }

            // 进行token验证，通过则显示界面，不通过则返回登陆页
            if(!this._token) {
                console.log('本地和Url中都没有token');
                // 本地和Url中都没有token就返回登陆页
                Message({
                showClose: true,
                message: '请重新登录!',
                type: 'warning',
                })
                this.$router.push('/user/login');
            } else {
                // 验证token是否有效
                axios({
                    method: 'POST',
                    url: "https://ai.wust.edu.cn/llm-kg/subject-one/jeecg-stj/stj/sys/getUserByToken",
                    // url: "http://14.55.6.35:8082/stj/sys/getUserByToken",
                    data: {
                        "token": this._token,
                    },
                    // headers: {
                    //     "X-Access-Token": this._token,
                    // }
                }).then((response) => {
                    // console.log("response",response);
                    
                    if(response.code == 200) {
                        this.user = response.result.realname;

                        // 验证成功后用这个token进行一次等于，否则登出接口会失败
                        if(urlHasToken) {
                            let loginParams = {
                            username:'',
                            password:'',
                            remember_me:'',
                            captcha:'',
                            checkKey:'',
                            tokenParam: this._token,
                        }
                        this.Login(loginParams)

                         // 创建一个Date对象表示当前时间
                        // const currentDate = new Date();
                        // // 获取当前时间的毫秒数（时间戳）
                        // const currentTimestamp = currentDate.getTime();
                        // // 创建一个新的Date对象，设置为当前时间
                        // const futureDate = new Date(currentTimestamp);
                        // // 将futureDate的小时数增加2小时
                        // futureDate.setHours(futureDate.getHours() + 2);
                        // // 获取未来时间的毫秒数（时间戳）
                        // const futureTimestamp = futureDate.getTime();

                        // let obj = {
                        //   "value":this._token,
                        //   "expire":futureTimestamp
                        // }
                        // localStorage.setItem("pro__Access-Token", JSON.stringify(obj));
                        }
                        
                    } else  {
                        Message({
                        showClose: true,
                        message: '请重新登录!',
                        type: 'warning',
                        })
                        this.$router.push('/user/login');
                    }
                });
            }
        },

    }
  };
  </script>
  <style scoped> 
    a {
      text-decoration: none; /* 去除默认的下划线 */
      outline: none; /* 去除旧版浏览器的点击后的外虚线框 */
      }
    .full-screen {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
    }
    .left {
        float: left;
        background-color: #FEFEFD;
        width: 20%;
        height: 100%;
    }
    .module {
        text-align: center;
        width: 80%;    
        height: 80px;
        background-color: #F5F5F5;
        line-height: 80px;
        margin-left: 10%;
        margin-top: 20px;
        border-radius: 10px;
    }
  .right {
    float: left;
    background-color: #F6F7FB;
    height: 100vh;
    width: 80%;
    position: relative;
  }
  .welcome {
    position: absolute;
    top: 10px;
    right: 10px;
  }
  .circle {
    width: 86px;
    height: 86px;
    border-radius: 50%;
    margin-top: 50px;
    background-image: url('../../../assets/hotel.jpeg');
    background-size: cover;
    background-position: center;
  }
  .container {
    margin-top: 70px;
    width: 910px;
    height: 170px;
    background-color: #fff;
    border-radius: 15px;
  }
  .example {
    float: left;
    width: 270px;
    height: 120px;
    padding-top: 10px;
    background-color: #F2F2FF;
    margin-left: 25px;
    margin-top: 20px;
    border-radius: 10px;
  }
  .title {
    margin-top: 70px;
    width: 75%;
    height: 50px;
    /* background-color: pink; */
    z-index: 1;
  }
  .title .name {
    display: inline-block;
    width: 200px;
    height: 35px;
    line-height: 35px;
    text-align: center;
    color: #fff;
    font-weight: 700;
    border-radius: 10px;
    background-color: #99C8F2;
  }
  .infinite-list {
    /* height: 500px; */
    width: 100%;
    padding: 0;
    margin: 0;
    margin-top: 30px;
    list-style: none;
    }
    .infinite-list .infinite-list-item {
        display: flex;
        float: left;
        width: 80%;
        height: auto;
        background: white;
        margin: 10px;
        color: black;
        border-top-right-radius: 20px;
        border-bottom-left-radius: 20px;
        border-bottom-right-radius: 20px;
        padding: 15px;
        white-space: pre-wrap;
        padding-left: 10px;
    }
    .infinite-list .infinite-list-item2 {
        float: right;
        display: flex;
        align-items: center;
        width: 50%;
        height: auto;
        background: #7365FF;
        margin: 10px;
        color: black;
        border-top-left-radius: 20px;
        border-top-right-radius: 20px;
        border-bottom-left-radius: 20px;
        padding: 5px;
        padding-left: 10px;
    }
  
    .infinite-list .infinite-list-item + .list-item {
        margin-top: 10px;
    }
    .infinite-list .infinite-list-item2 + .list-item {
        margin-top: 10px;
    }
    .try {
        width: 50px;color: rgb(0, 154, 250);
        position: absolute;bottom: 15px;
        right: 15px;width: 50px;
        color: rgb(0, 154, 250);
        position: absolute;
        bottom: 15px;right: 15px;
    }
    .qa-list {
        scroll-behavior: smooth;
        z-index: 1;
        overflow: scroll; 
        /* max-height: 39.37rem; 
        height: 39.37rem; */
        height: 100%;
        width: 75%;
    }
  
    .qa-list::-webkit-scrollbar {
        width: 0;
    }
  
    .qa-list::-webkit-scrollbar-track {
        background: transparent;
        /* 设置滚动条轨道背景为透明 */
    }
    .qa-list::-webkit-scrollbar-thumb {
        background: transparent;
        /* 设置滚动条拖动部分背景为透明 */
    }
  
    .input-container {
        font-size: 14.3624px;
        font-family: Avenir,Helvetica,Arial,sans-serif;
        -webkit-font-smoothing: antialiased;
        color: #2c3e50;
        border: 0;
        margin: 0;
        padding: 0;
        position: absolute;
        bottom: 3.72778rem;
        /* width: 67.98611rem; */
        width: 75%;
        height: 7.93889rem;

        background: #fff;
        border-radius: 1.04167rem;
        z-index: 2;
    }
    
    .input-box {
        font-size: 14.3624px;
        font-family: Avenir,Helvetica,Arial,sans-serif;
        -webkit-font-smoothing: antialiased;
        color: #2c3e50;
        border: 0;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: space-between;
        overflow: hidden;
    }
  
    .input-box img{
        font-size: 14.3624px;
        font-family: Avenir,Helvetica,Arial,sans-serif;
        -webkit-font-smoothing: antialiased;
        color: #2c3e50;
        border: 0;
        margin: 0;
        padding: 0;
        margin-top: 1.11111rem;
        margin-right: 1.45833rem;
        margin-left: .69444rem;
        width: 1.59722rem;
        height: 2.15278rem;
        cursor: pointer;
    }
  
    #textplaceholder {
        -webkit-font-smoothing: antialiased;
        padding: 0;
        width: 100%;
        height: 5.02222rem;
        flex: 1;
        margin: 1.45833rem;
        border: 0;
        outline: none;
        resize: none;
        font-family: Microsoft YaHei UI;
        font-weight: 400;
        font-size: 18px;
        color: #000;
    }
    #textplaceholder::-webkit-scrollbar {
        width: 0;
    }
  
    #textplaceholder::-webkit-scrollbar-track {
        background: transparent;
        /* 设置滚动条轨道背景为透明 */
    }
    #textplaceholder::-webkit-scrollbar-thumb {
        background: transparent;
        /* 设置滚动条拖动部分背景为透明 */
    }
  
    .rightbgk {
        top: 50px;
        left: 10%;
        width: 80%;
        height: 91%;
        background: #fff;
        border-radius: 2.08333rem;
        opacity: .42;
        position: absolute;
        z-index: 0.1;
    }

    .active {
        background-color: #CCD0FF;
    }
  </style>