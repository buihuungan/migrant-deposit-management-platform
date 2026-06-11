# 农民工工资保证金管理系统

> 政务类管理系统标准技术架构，基于 Spring Cloud + Vue + uflo 工作流实现

## 项目简介

本系统是一套完整的农民工工资保证金管理解决方案，支持保证金存缴、差异化计算、在线审批、工作流驱动等功能。

## 技术栈

| 技术 | 说明 |
|------|------|
| 前端 | Vue.js + Element UI + axios |
| 后端 | Spring Boot + Spring Cloud |
| 数据库 | MySQL + Redis |
| 工作流 | uflo 轻量级工作流引擎 |
| 认证 | Shiro + JWT |
| 服务治理 | Nacos + Gateway |
| 定时任务 | Quartz |

## 项目结构

```
├── Front-end/                 # Vue.js 前端项目
├── Workflow engine workflow/  # 后端服务
│   ├── STJBoot/              # 主业务系统
│   ├── NewWorkflow/          # 工作流服务
│   ├── BusinessBack/         # 业务后端
│   ├── Eureka/               # 服务注册中心
│   └── sql/                  # 数据库脚本
└── docs/                     # 项目文档
```

## 快速开始

### 1. 环境要求

- JDK 1.8+
- Node.js 14+
- MySQL 5.7+ / 8.0
- Redis 3.0+
- Maven 3.6+

### 2. 数据库配置

1. 创建数据库
```sql
CREATE DATABASE migrant_deposit_db DEFAULT CHARACTER SET utf8mb4;
```

2. 导入数据库脚本（参考 `sql/` 目录下的文件）

3. 修改配置文件 `application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/migrant_deposit_db
    username: your_username
    password: your_password
```

### 3. 后端启动

```bash
cd "Workflow engine workflow"

# 启动 Eureka 服务注册中心
cd Eureka
mvn spring-boot:run

# 启动其他微服务（按需启动）
cd ../NewWorkflow
mvn spring-boot:run
```

### 4. 前端启动

```bash
cd Front-end

# 安装依赖
npm install

# 开发环境启动
npm run serve

# 生产环境打包
npm run build
```

### 5. 访问系统

- 前端地址：http://localhost:8080
- 后端接口：http://localhost:8081

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |

## 功能模块

- [x] 用户权限管理
- [x] 保证金存缴登记
- [x] 差异化存缴计算
- [x] 保证金使用申请
- [x] 工作流审批
- [x] 存缴方式变更
- [x] 数据字典管理
- [x] 系统公告

## 许可证

MIT License
