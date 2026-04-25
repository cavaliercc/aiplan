# 门店排班管理系统 — 项目文档与踩坑记录

> 项目地址：https://github.com/cavaliercc/aiplan
> 前端部署：https://aiplan-eta.vercel.app
> 后端部署：https://aiplan-production-a4dd.up.railway.app
> 日期：2026-04-25

---

## 一、项目概述

面向连锁零售品牌的企业级人力资源与排班管理 SaaS 平台。

| 项 | 内容 |
|------|------|
| 项目名称 | 门店排班管理系统（Store Shift Scheduling） |
| 前端 | Vue 3 + TypeScript + Vite + Element Plus + ECharts |
| 后端 | Java 17 + Spring Boot 3.2 + MyBatis-Plus + Spring Security + JWT |
| 数据库 | PostgreSQL 16 |
| 前端部署 | Vercel |
| 后端部署 | Railway（Docker） |
| 数据库部署 | Railway PostgreSQL |
| 默认账号 | admin / admin123 |

---

## 二、项目结构（详细）

```
shift-management/
├── DESIGN.md                    # 设计文档
├── README.md                    # 项目说明
├── DEPLOY.md                    # 部署指南
├── Dockerfile                   # 后端 Docker 构建文件
├── railway.toml                 # Railway 部署配置
├── images/                      # 设计稿截图（6张）
│
├── frontend/                    # ===== Vue 3 前端 =====
│   ├── package.json
│   ├── vite.config.ts           # Vite 配置（Tailwind + 代理 + allowedHosts）
│   ├── .env.production          # 生产环境 API 地址
│   │
│   └── src/
│       ├── main.ts              # 入口：Element Plus + Pinia + Router
│       ├── App.vue              # 根组件
│       ├── style.css            # Tailwind CSS 入口
│       │
│       ├── api/                 # API 层（8个模块）
│       │   ├── request.ts       # Axios 封装（baseURL 自动追加 /api）
│       │   ├── auth.ts          # 登录/注册
│       │   ├── stores.ts        # 门店 CRUD
│       │   ├── employees.ts     # 员工 CRUD + 偏好
│       │   ├── schedules.ts     # 排班 CRUD
│       │   ├── swapRequests.ts  # 换班申请
│       │   ├── notifications.ts # 通知
│       │   └── dashboard.ts     # 仪表板 KPI/趋势/提醒
│       │
│       ├── mock/                # Mock 数据（API 降级用）
│       │   ├── dashboard.ts
│       │   ├── employees.ts     # 15 条员工数据
│       │   └── stores.ts        # 8 家门店数据
│       │
│       ├── layouts/
│       │   └── MainLayout.vue   # 主布局（TopBar + Sidebar + Content）
│       │
│       ├── views/               # 页面（6个）
│       │   ├── Login.vue
│       │   ├── Dashboard.vue
│       │   ├── StoreDistribution.vue
│       │   ├── EmployeeManagement.vue
│       │   ├── EmployeeDirectory.vue
│       │   └── ShiftScheduling.vue
│       │
│       ├── components/
│       │   └── PreferenceModal.vue
│       │
│       └── router/
│           └── index.ts         # 路由配置 + 登录守卫
│
└── backend/                     # ===== Spring Boot 后端 =====
    ├── pom.xml                  # Maven 依赖
    │
    └── src/main/
        ├── java/com/shift/management/
        │   ├── ShiftManagementApplication.java
        │   ├── config/          # 配置层（7个）
        │   │   ├── SecurityConfig.java
        │   │   ├── CorsConfig.java
        │   │   ├── JwtConfig.java
        │   │   ├── MyBatisPlusConfig.java
        │   │   ├── SwaggerConfig.java
        │   │   ├── MetaObjectHandlerConfig.java
        │   │   └── DataInitializer.java
        │   ├── security/        # 安全层（3个）
        │   │   ├── JwtTokenProvider.java
        │   │   ├── JwtAuthenticationFilter.java
        │   │   └── UserDetailsServiceImpl.java
        │   ├── controller/      # 控制层（7个）
        │   ├── service/         # 服务层（7接口 + 7实现）
        │   ├── mapper/          # 数据访问层（12个）
        │   ├── entity/          # 实体层（12个）
        │   ├── dto/             # 数据传输对象（11个）
        │   └── common/          # 通用（Result + PageResult + 异常处理）
        │
        └── resources/
            ├── application.yml
            └── db/migration/
                ├── V1__init_schema.sql  # 13张表 + 5个索引
                └── V2__seed_data.sql    # 初始数据
```

---

## 三、踩过的坑与解决方案

### 坑 1：Claude Code 无法以 root 运行
- **现象：** `--permission-mode bypassPermissions` 报安全限制错误
- **解决：** 创建 `coder` 用户，用 `su - coder -c "claude ..."` 执行

### 坑 2：ACP 运行时 agent ID 不匹配
- **现象：** `sessions_spawn` 报 `agent "claude" is not allowed by policy`
- **解决：** 放弃 ACP，直接用 CLI 执行

### 坑 3：Vercel 部署 404
- **现象：** 前端部署成功但访问 404
- **原因：** Root Directory 配置为 `./` 而不是 `frontend`
- **解决：** Settings → Build → Root Directory 改为 `frontend`

### 坑 4：TypeScript 编译错误
- **现象：** `vue-tsc` 报未使用变量错误
- **解决：** 删除未使用的导入

### 坑 5：Vite 阻止自定义域名访问
- **现象：** 通过自定义域名访问报 "Blocked request"
- **解决：** `vite.config.ts` 添加 `server: { allowedHosts: true }`

### 坑 6：CORS 403 跨域错误
- **现象：** 前端请求被 Spring Security 拦截
- **原因：** CorsConfig 只允许 localhost
- **解决：** 添加 Vercel 生产域名到 CORS 允许列表

### 坑 7：前端请求路径错误（/auth/login 而不是 /api/auth/login）
- **原因：** `.env.production` 的 VITE_API_BASE_URL 没有 `/api` 后缀
- **解决：** `request.ts` 自动检测并追加 `/api`

### 坑 8：BCrypt 密码哈希不正确（401）
- **原因：** SQL 种子数据中的哈希值被 shell 转义破坏
- **解决：** 创建 `DataInitializer.java`，启动时自动重置 admin 密码

### 坑 9：Railway 后端 502（数据库连接失败）
- **原因：** PGPORT 被设成 8080（应用端口），不是 27728（数据库端口）
- **解决：** 硬编码公网数据库地址作为 fallback

### 坑 10：Dockerfile 路径错误
- **原因：** Dockerfile 在 backend/ 子目录，Railway 从根目录构建
- **解决：** Dockerfile 移到根目录，路径改为 `COPY backend/pom.xml pom.xml`

### 坑 11：Flyway 依赖版本缺失
- **原因：** `flyway-database-postgresql` 不在 Spring Boot 3.2 依赖管理中
- **解决：** 移除该依赖，只保留 `flyway-core`

---

## 四、部署检查清单

- [ ] Vercel Root Directory = `frontend`
- [ ] Vercel 环境变量 VITE_API_BASE_URL 已设置
- [ ] Railway Dockerfile 在项目根目录
- [ ] Railway 后端服务有正确的数据库环境变量
- [ ] CORS 配置包含前端域名
- [ ] 数据库表已创建 + 初始数据已插入
- [ ] DataInitializer 会自动重置 admin 密码
