<div align="center">

# 🏛️ 农民工工资保证金管理系统

**政务类管理平台 · 标准化微服务架构实践作品**

[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Hoxton-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Vue](https://img.shields.io/badge/Vue-2.x-4FC08D?logo=vue.js&logoColor=white)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-5.7%20%2F%208.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](#许可证)
[![Status](https://img.shields.io/badge/Status-Personal%20Showcase-orange.svg)](#项目说明)

> 一套基于 **Spring Cloud 微服务架构** + **Vue.js** + **轻量级工作流引擎** 的政务类管理系统解决方案，涵盖保证金存缴、差异化计算、在线审批、流程驱动等核心业务场景。

</div>

---

## 📌 项目说明

本项目是个人**学习与作品展示**性质的开源项目，完整实现了农民工工资保证金管理业务的全流程数字化（存缴登记 → 差异化计算 → 审批 → 退付 → 监管），旨在展示标准的政务类系统技术架构与工程化实践。

> ⚠️ **本项目为个人作品展示，并非商业产品**。代码仅作技术交流，不提供完整运行环境、数据库脚本与生产级部署支持。

---

## 🏗️ 技术架构

### 后端技术栈

| 类别 | 技术 | 说明 |
|------|------|------|
| 微服务框架 | Spring Cloud Hoxton | 服务注册、负载均衡、熔断降级 |
| 服务治理 | Eureka / Nacos | 服务注册与发现中心 |
| API 网关 | Spring Cloud Gateway | 统一入口、路由转发、鉴权 |
| 持久层 | MyBatis-Plus + MySQL 5.7/8.0 | ORM 与关系型数据库 |
| 缓存 | Redis | 分布式缓存、Session 共享 |
| 权限认证 | Shiro + JWT | RBAC 权限模型 + Token 鉴权 |
| 工作流 | uflo / Flowable | 轻量级国产工作流引擎 |
| 定时任务 | Quartz + XXL-Job | 分布式任务调度 |
| 文件存储 | MinIO / 本地存储 | 分布式文件服务 |
| 服务监控 | Spring Boot Admin | 应用健康监控 |

### 前端技术栈

| 类别 | 技术 | 说明 |
|------|------|------|
| 核心框架 | Vue 2.x + Vue Router + Vuex | 渐进式前端框架 |
| UI 组件库 | Ant Design Vue | 企业级中后台 UI 方案 |
| HTTP 客户端 | Axios | 异步请求与拦截器 |
| 构建工具 | Webpack 4 + Vue CLI 3 | 模块化打包 |
| 可视化 | ECharts | 数据报表与图表展示 |
| 富文本 | TinyMCE / wangEditor | 业务表单与公告编辑 |

### 工程化能力

- ✅ 微服务拆分（注册中心 / 网关 / 鉴权 / 业务 / 工作流 独立部署）
- ✅ 统一异常处理 + 全局日志 + 接口签名校验
- ✅ 多租户 / 多部门数据隔离
- ✅ Swagger / Knife4j 在线 API 文档
- ✅ Docker Compose 一键启动开发环境
- ✅ 前后端分离 + Token 无状态认证

---

## 📂 项目结构

```
migrant_deposit_management_platform/
│
├── Front-end/                    # 🎨 前端项目（Vue 2.x）
│   ├── src/
│   │   ├── views/                # 业务页面（保证金、审批、工作流等）
│   │   ├── components/           # 公共组件（图表、上传、表格等）
│   │   ├── api/                  # 接口请求层
│   │   ├── store/                # Vuex 状态管理
│   │   └── router/               # 路由配置
│   └── package.json
│
├── Workflow engine workflow/     # ⚙️ 后端微服务集群
│   ├── STJBoot/                  # 主业务系统（jeecg-boot 改造）
│   ├── NewWorkflow/              # 工作流服务（uflo 引擎）
│   ├── BusinessBack/             # 业务后端（保证金核心）
│   ├── Eureka/                   # 服务注册中心
│   └── opt/                      # 部署文件 / 静态资源
│
├── docs/                         # 📖 项目文档（本地复习版）
│
├── 示例配置文件/                  # 📝 配置模板（脱敏示例）
│   └── application-example.yml
│
├── .gitignore
└── README.md
```

---

## 🚀 核心功能模块

| 模块 | 说明 | 状态 |
|------|------|------|
| 🔐 用户权限管理 | 用户 / 角色 / 部门 / 菜单 / 数据权限 | ✅ |
| 💰 保证金存缴登记 | 工程项目登记、保证金台账管理 | ✅ |
| 📊 差异化存缴计算 | 信用等级、违法记录差异化比例自动核算 | ✅ |
| 📝 保证金使用申请 | 工资支付、退付、抵扣申请 | ✅ |
| 🔄 工作流审批 | 自定义流程图、多级审批、会签/或签 | ✅ |
| 🔁 存缴方式变更 | 保函 ↔ 现金 ↔ 银行授信方式转换 | ✅ |
| 📋 数据字典 | 业务字典统一管理与缓存 | ✅ |
| 📢 系统公告 | 富文本公告发布与推送 | ✅ |
| 📈 监管大屏 | ECharts 实时数据可视化 | ✅ |

---

## 🛠️ 本地开发环境

### 环境要求

| 工具 | 版本 |
|------|------|
| JDK | 1.8+ |
| Maven | 3.6+ |
| Node.js | 14+ |
| MySQL | 5.7+ / 8.0 |
| Redis | 3.0+ |
| IDE | IntelliJ IDEA / VS Code |

### 启动顺序

```bash
# 1. 启动基础设施
mysql & redis-server

# 2. 启动后端服务（按顺序）
cd "Workflow engine workflow/Eureka"      && mvn spring-boot:run
cd "Workflow engine workflow/STJBoot"     && mvn spring-boot:run
cd "Workflow engine workflow/NewWorkflow" && mvn spring-boot:run

# 3. 启动前端
cd Front-end
npm install
npm run serve
```

> 📌 配置文件位于各模块的 `src/main/resources/application.yml`，**请参考 `示例配置文件/application-example.yml` 进行本地配置**（生产环境密码、Redis 地址、Nacos 地址等已脱敏）。

---

## 🔒 隐私与安全说明

本仓库已对以下敏感信息进行脱敏处理：

- ❌ **不包含**真实数据库脚本、SQL 表结构与业务数据
- ❌ **不包含**真实服务器 IP、域名、生产环境配置
- ❌ **不包含**默认管理员账号与密码
- ❌ **不包含**第三方支付、银行 API 等真实密钥
- ❌ **不包含**任何政府单位真实业务数据

✅ `示例配置文件/application-example.yml` 仅作为配置格式参考，**所有密钥、密码、地址均为占位符**，请勿直接用于生产环境。

---

## ⚖️ 版权与许可证

```
MIT License — 仅供学习与个人作品展示使用
```

Copyright (c) 2024-present

**使用条款：**

1. ✅ 允许个人学习、研究、二次开发
2. ✅ 允许在简历 / 作品集中**注明出处**后展示
3. ❌ **禁止**用于任何商业用途（销售、SaaS 服务、政府采购投标等）
4. ❌ **禁止**去除版权信息后二次发布到其他平台
5. ❌ **禁止**用于任何违法、违规的政务系统搭建

> 💡 本项目代码遵循 MIT 协议开源，但**项目涉及的业务流程参考了部分政务行业实践**，未经作者授权不得用于实际政务系统交付。如需商业合作请联系作者。

---

## 🙋‍♂️ 关于作者

**SirLS** — 后端开发工程师 / Java 全栈

- 🛠️ 技术方向：Spring Cloud 微服务、政务类系统、工作流引擎、企业级中后台
- 💼 本项目用于展示**独立完成微服务架构设计 + 全栈开发**的能力
- 📫 联系方式：详见个人 GitHub Profile

> 如果你觉得本项目对你的学习有帮助，欢迎点个 ⭐ Star 支持一下！

---

<div align="center">

**Made with ❤️ by SirLS · 个人作品展示项目**

</div>
