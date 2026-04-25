# 门店排班管理系统 — 设计文档

> 版本：v1.0 | 日期：2026-04-25 | 状态：设计阶段

---

## 1. 项目概述

### 1.1 项目名称

**门店排班管理系统**（Store Shift Scheduling Management System）

### 1.2 项目定位

面向连锁零售品牌（如九木杂物社）的企业级人力资源与排班管理 SaaS 平台，帮助总部人力资源部门统一管理多门店员工的排班计划、出勤数据与人力成本。系统以"企业管理终端"的形式运行，具备 Web 端后台管理界面。

### 1.3 目标用户

| 角色 | 描述 | 主要操作 |
|------|------|----------|
| HR 管理员 | 总部人力资源部门人员 | 全局排班、员工档案管理、数据报表 |
| 店长 | 各门店负责人 | 查看本店排班、审核换班申请、确认出勤 |
| 员工 | 各门店一线人员 | 查看个人排班、提交换班/调班申请 |
| 系统管理员 | IT 运维人员 | 门店配置、账号权限、系统设置 |

### 1.4 核心目标

- 实现多门店排班的集中可视化管理
- 降低人工排班的时间成本与冲突率
- 实时监控人力成本与出勤效率
- 支持员工排班偏好录入，提升排班合理性
- 提供及时的异常预警与待办提醒

---

## 2. 功能模块

### 2.1 仪表板（Dashboard）

核心 KPI 概览与运营趋势看板，是管理员进入系统后的首页。

**关键指标卡片（4 块）：**
- **本周总排班**：累计工时（与上周环比）
- **预估人力成本**：本周预估金额（与预算对比）
- **平均 P/H 效率**：单/人时（与目标值对比）
- **今日出勤率**：实到/应到人数与百分比，含进度条

**图表区：**
- 排班趋势预估图（未来7天柱状图），支持"预估工时 / 历史实际"双数据系列切换

**今日提醒面板（右侧）：**
- 换班申请待审核：含申请人、换班对象、日期班次，提供【批准/拒绝】快捷操作
- 人员配置不足预警：预警具体班次与缺员数量
- 考勤异常处理：标记未打卡员工，需管理员补签确认

### 2.2 门店位置（Store Distribution）

以卡片网格形式展示所有营业网点的实时状态与基础信息。

**功能点：**
- 门店状态标识：营业中（绿色）、装修中（橙色）、休业（灰色）
- 每张卡片包含：门店 ID、名称、地址、店长、联系电话、营业时间、预计恢复时间（装修中）
- 支持筛选（按状态/城市）与新增门店
- 店长待分配时显示"待分配"占位状态

### 2.3 员工管理（Employee Management）

跨门店员工档案的统一管理，支持筛选、查看与操作。

**功能点：**
- 多维度筛选：所属门店、职位、状态（在职/休假/离职）
- 表格字段：头像、姓名、工号、所属门店、职位、联系电话、入职日期、状态、操作
- 状态徽章：在职（绿）、休假（黄）、离职（灰）
- 支持数据导出（Excel/CSV）
- 分页展示，支持每页条目数配置
- 顶部「添加员工」入口

### 2.4 员工列表（Employee Directory）

在员工管理基础上，增加**排班偏好**维度的专项视图，为智能排班提供数据支撑。

**扩展字段：**
- 排班偏好摘要（无限制 / 仅白班 / 固定8小时 / 双休 / 未设置）
- 联系方式直接展示在员工列下方
- 每行提供「偏好设置」快捷入口与编辑操作

**筛选条件：**
- 所属门店、职位、在职状态、精确搜索（姓名或工号）

### 2.5 排班计划（Shift Scheduling）

新增班次的表单页，用于创建单次排班任务并分配资源。

**表单字段：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| 选择日期 | 日期选择器 | ✓ | 班次所在日期 |
| 班次时间 | 时间范围选择器 | ✓ | 上班时间 - 下班时间 |
| 选择门店 | 下拉单选 | ✓ | 绑定门店 |
| 分配区域 | 下拉单选 | — | 门店内工作区域 |
| 选择员工 | 多选标签输入 | ✓ | 支持姓名/工号搜索，已选员工以 Tag 形式展示 |
| 分配车辆 | 下拉单选 | — | 可选，默认"不分配车辆" |

### 2.6 排班偏好设置（Scheduling Preferences）

以弹窗形式针对单个员工配置默认排班规则与可用性约束，是智能排班算法的基础数据来源。

**配置项：**

**基本偏好：**
- 首选班次（下拉：早班 08:00-16:00 / 中班 / 晚班等）
- 每日最大工时（数字输入，小时）

**不可用时间（Unavailability Blocks）：**
- 可添加多个时段
- 每个时段包含：日期、开始时间、结束时间
- 支持逐条删除

**休息日设定：**
- 按周选择固定休息日（周一至周日复选框）
- 适用于有固定周休需求的员工（如双休：周六+周日）

---

## 3. 页面结构

### 3.1 整体布局

```
┌─────────────────────────────────────────────────────────┐
│  TopBar：系统标题 / 搜索框 / 铃铛通知 / 设置 / 帮助 / 用户头像  │
├──────────────┬──────────────────────────────────────────┤
│              │                                          │
│   Sidebar    │            主内容区（Content Area）       │
│              │                                          │
│  品牌 Logo   │  页面标题 + 副标题                        │
│  部门名称    │                                          │
│              │  内容组件（表格 / 卡片 / 表单 / 图表）    │
│  [新增排班]  │                                          │
│  ─────────   │                                          │
│  仪表板      │                                          │
│  员工管理    │                                          │
│  排班计划    │                                          │
│  门店位置    │                                          │
│              │                                          │
└──────────────┴──────────────────────────────────────────┘
```

### 3.2 各页面组件清单

#### 仪表板

```
仪表板概览
├── PageHeader（日期 + 周次）
├── KPICardGroup
│   ├── KPICard（本周总排班）
│   ├── KPICard（预估人力成本）
│   ├── KPICard（平均P/H效率）
│   └── KPICard（今日出勤率 + ProgressBar）
├── ContentRow
│   ├── TrendChart（柱状图 + 图例切换）
│   └── AlertPanel
│       ├── AlertItem（换班申请 + 批准/拒绝按钮）
│       ├── AlertItem（配置不足预警）
│       ├── AlertItem（考勤异常）
│       └── Link（查看全部提醒）
```

#### 门店分布概览

```
门店分布概览
├── PageHeader + 操作栏（筛选 / 新增门店）
└── StoreCardGrid
    └── StoreCard × N
        ├── StatusBadge（营业中 / 装修中）
        ├── StoreID
        ├── StoreName
        ├── AddressRow（图标 + 文字）
        ├── ManagerRow（头像 + 姓名 + 职位标签）
        ├── PhoneRow
        └── HoursRow / EstimatedResumeRow
```

#### 员工管理

```
员工管理
├── PageHeader + 添加员工按钮
├── FilterBar（门店 / 职位 / 状态 下拉 + 筛选图标 + 导出图标）
└── DataTable
    ├── EmployeeRow × N
    │   ├── Avatar + 姓名
    │   ├── 工号
    │   ├── 所属门店
    │   ├── 职位
    │   ├── 联系电话
    │   ├── 入职日期
    │   ├── StatusBadge
    │   └── OperationGroup（查看 / 编辑 / 更多）
    └── Pagination
```

#### 员工列表（扩展视图）

```
员工列表
├── PageHeader + 导出按钮
├── FilterBar（门店 / 职位 / 状态 + 搜索框）
└── DataTable
    ├── EmployeeRow × N
    │   ├── Avatar + 姓名 + 联系方式（副文本）
    │   ├── 工号
    │   ├── 所属门店
    │   ├── 职位
    │   ├── PreferenceTags（班次限制标签 / "未设置"灰色文字）
    │   ├── StatusBadge
    │   └── OperationGroup（偏好设置 / 编辑图标）
    └── Pagination
```

#### 新增班次

```
新增班次（全页表单）
├── PageHeader + 副标题
└── FormCard
    ├── Row：DatePicker（选择日期）+ TimeRangePicker（班次时间）
    ├── Row：StoreSelect（选择门店）+ ZoneSelect（分配区域）
    ├── EmployeeMultiSelect（选择员工，TagInput + 搜索）
    ├── VehicleSelect（分配车辆，可选）
    └── FormActions（取消 / 保存）
```

#### 排班偏好设置（Modal）

```
Modal：排班偏好设置 - {员工姓名}
├── ModalHeader + 关闭按钮
├── Section：基本偏好
│   ├── PreferredShiftSelect（首选班次）
│   └── MaxHoursInput（每日最大工时）
├── Section：不可用时间
│   ├── UnavailabilityRow × N（日期 + 开始时间 + 结束时间 + 删除）
│   └── AddPeriodButton（+ 添加时段）
├── Section：休息日设定
│   └── WeekdayCheckboxGroup（周一 ~ 周日）
└── ModalFooter（取消 / 保存）
```

---

## 4. 交互流程

### 4.1 新增排班流程

```
管理员点击「新增排班」
    → 跳转「新增班次」页面
    → 填写日期、班次时间
    → 选择门店 → 联动加载该门店可用员工
    → 选择分配区域（可选）
    → 搜索并添加员工（多选，Tag 形式）
    → 选择分配车辆（可选）
    → 点击「保存」
        ├── 校验通过 → 写入排班计划 → 触发通知给被排班员工 → 返回排班计划列表
        └── 校验失败 → 表单内行内报错提示
```

### 4.2 换班申请审核流程

```
员工 A 提交换班申请（与员工 B 互换某班次）
    → 系统生成待审核通知
    → 仪表板「今日提醒」面板展示申请摘要
    → 管理员点击「批准」
        ├── 系统自动更新两个员工的排班记录
        ├── 通知双方员工
        └── 提醒条目标记为已处理
    → 管理员点击「拒绝」
        ├── 原排班不变
        └── 通知申请人拒绝原因（可选）
```

### 4.3 员工排班偏好设置流程

```
在「员工列表」页面，点击某行「偏好设置」
    → 弹出「排班偏好设置」Modal
    → 设置首选班次 + 每日最大工时
    → 添加/删除不可用时间段
    → 勾选固定休息日
    → 点击「保存」
        ├── 更新该员工偏好数据
        └── 后续排班算法在生成排班建议时将遵循这些约束
```

### 4.4 添加员工流程

```
点击「添加员工」按钮
    → 打开员工信息表单（Modal 或跳转页面）
    → 填写：姓名、工号（系统自动生成或手动）、所属门店、职位、联系电话、入职日期
    → 提交保存
    → 新员工出现在列表，排班偏好默认"未设置"
    → 可进一步设置排班偏好
```

### 4.5 人员配置不足预警处理流程

```
系统检测到某班次人员不足
    → 在仪表板「今日提醒」生成预警条目
    → 管理员查看预警详情（缺员班次、数量）
    → 跳转「新增排班」页面，预填班次信息
    → 手动补充人员
    → 保存后预警条目自动解除
```

---

## 5. 数据模型

### 5.1 门店表 `stores`

```sql
CREATE TABLE stores (
    id           VARCHAR(20) PRIMARY KEY,   -- e.g. STR-NJ-001
    name         VARCHAR(100) NOT NULL,
    city         VARCHAR(50),
    province     VARCHAR(50),
    address      VARCHAR(200),
    phone        VARCHAR(20),
    open_time    TIME,                      -- 10:00
    close_time   TIME,                      -- 22:00
    status       ENUM('open','renovation','closed') DEFAULT 'open',
    manager_id   INT REFERENCES employees(id),
    resume_date  DATE,                      -- 装修中时预计恢复日期
    created_at   TIMESTAMP DEFAULT NOW(),
    updated_at   TIMESTAMP DEFAULT NOW()
);
```

### 5.2 员工表 `employees`

```sql
CREATE TABLE employees (
    id           SERIAL PRIMARY KEY,
    emp_no       VARCHAR(20) UNIQUE NOT NULL,  -- EMP-2023-041
    name         VARCHAR(50) NOT NULL,
    avatar_url   VARCHAR(255),
    phone        VARCHAR(20),
    store_id     VARCHAR(20) REFERENCES stores(id),
    position_id  INT REFERENCES positions(id),
    hire_date    DATE,
    status       ENUM('active','on_leave','resigned') DEFAULT 'active',
    created_at   TIMESTAMP DEFAULT NOW(),
    updated_at   TIMESTAMP DEFAULT NOW()
);
```

### 5.3 职位表 `positions`

```sql
CREATE TABLE positions (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,  -- 收银员、理货员、导购、店长
    level        INT DEFAULT 1,
    created_at   TIMESTAMP DEFAULT NOW()
);
```

### 5.4 排班偏好表 `schedule_preferences`

```sql
CREATE TABLE schedule_preferences (
    id                  SERIAL PRIMARY KEY,
    employee_id         INT REFERENCES employees(id),
    preferred_shift_id  INT REFERENCES shift_types(id),  -- 首选班次
    max_daily_hours     INT DEFAULT 8,
    rest_days           JSON,    -- e.g. ["SAT","SUN"]
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW()
);
```

### 5.5 不可用时间表 `unavailability_blocks`

```sql
CREATE TABLE unavailability_blocks (
    id           SERIAL PRIMARY KEY,
    employee_id  INT REFERENCES employees(id),
    date         DATE NOT NULL,
    start_time   TIME NOT NULL,
    end_time     TIME NOT NULL,
    reason       VARCHAR(200),
    created_at   TIMESTAMP DEFAULT NOW()
);
```

### 5.6 班次类型表 `shift_types`

```sql
CREATE TABLE shift_types (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,   -- 早班、中班、晚班
    start_time   TIME NOT NULL,
    end_time     TIME NOT NULL,
    store_id     VARCHAR(20) REFERENCES stores(id),  -- NULL 表示全局通用
    created_at   TIMESTAMP DEFAULT NOW()
);
```

### 5.7 排班计划表 `schedules`

```sql
CREATE TABLE schedules (
    id            SERIAL PRIMARY KEY,
    store_id      VARCHAR(20) REFERENCES stores(id),
    shift_date    DATE NOT NULL,
    shift_type_id INT REFERENCES shift_types(id),
    zone          VARCHAR(50),       -- 分配区域
    vehicle_id    INT REFERENCES vehicles(id),
    status        ENUM('draft','published','completed','cancelled') DEFAULT 'draft',
    created_by    INT REFERENCES users(id),
    created_at    TIMESTAMP DEFAULT NOW(),
    updated_at    TIMESTAMP DEFAULT NOW()
);
```

### 5.8 排班分配表 `schedule_assignments`

```sql
CREATE TABLE schedule_assignments (
    id           SERIAL PRIMARY KEY,
    schedule_id  INT REFERENCES schedules(id),
    employee_id  INT REFERENCES employees(id),
    checked_in   TIMESTAMP,        -- 实际打卡上班时间
    checked_out  TIMESTAMP,        -- 实际打卡下班时间
    status       ENUM('assigned','confirmed','absent','late') DEFAULT 'assigned',
    created_at   TIMESTAMP DEFAULT NOW()
);
```

### 5.9 换班申请表 `shift_swap_requests`

```sql
CREATE TABLE shift_swap_requests (
    id                  SERIAL PRIMARY KEY,
    requester_id        INT REFERENCES employees(id),
    target_id           INT REFERENCES employees(id),
    requester_sched_id  INT REFERENCES schedule_assignments(id),
    target_sched_id     INT REFERENCES schedule_assignments(id),
    reason              VARCHAR(200),
    status              ENUM('pending','approved','rejected') DEFAULT 'pending',
    reviewed_by         INT REFERENCES users(id),
    reviewed_at         TIMESTAMP,
    created_at          TIMESTAMP DEFAULT NOW()
);
```

### 5.10 提醒/通知表 `notifications`

```sql
CREATE TABLE notifications (
    id           SERIAL PRIMARY KEY,
    type         ENUM('swap_request','understaffing','attendance_anomaly','system'),
    title        VARCHAR(100),
    content      TEXT,
    related_id   INT,              -- 关联资源 ID（如换班申请 ID）
    related_type VARCHAR(50),
    target_user  INT REFERENCES users(id),
    is_read      BOOLEAN DEFAULT FALSE,
    created_at   TIMESTAMP DEFAULT NOW()
);
```

### 5.11 车辆表 `vehicles`（可选）

```sql
CREATE TABLE vehicles (
    id           SERIAL PRIMARY KEY,
    plate_no     VARCHAR(20) UNIQUE,
    store_id     VARCHAR(20) REFERENCES stores(id),
    status       ENUM('available','in_use','maintenance') DEFAULT 'available',
    created_at   TIMESTAMP DEFAULT NOW()
);
```

---

## 6. 技术栈建议

### 6.1 前端

| 技术 | 选型 | 说明 |
|------|------|------|
| 框架 | **React 18 + TypeScript** | 组件化开发，类型安全 |
| 构建工具 | **Vite** | 快速冷启动，HMR 体验好 |
| UI 组件库 | **Ant Design 5.x** | 丰富的企业级组件，适配本系统 Table / Form / Modal 需求 |
| 图表库 | **ECharts** 或 **Recharts** | 仪表板柱状图、趋势图 |
| 状态管理 | **Zustand** | 轻量，适合中型管理后台 |
| 路由 | **React Router v6** | 标准 SPA 路由 |
| 请求封装 | **Axios + React Query** | 请求缓存、自动刷新，减少样板代码 |
| 样式方案 | **CSS Modules + Tailwind CSS** | 局部样式隔离 + 通用工具类 |
| 日期处理 | **Day.js** | 轻量替代 Moment.js |

### 6.2 后端

| 技术 | 选型 | 说明 |
|------|------|------|
| 运行时 | **Node.js 20 LTS** | 生态成熟，与前端同语言降低切换成本 |
| Web 框架 | **NestJS** | 企业级 Node.js 框架，支持 DI、装饰器、模块化 |
| API 风格 | **RESTful API** | 标准化资源路径，配合 Swagger 自动生成文档 |
| ORM | **Prisma** | 类型安全的 ORM，schema 驱动，迁移管理完善 |
| 认证授权 | **JWT + RBAC** | 多角色权限（HR 管理员 / 店长 / 员工）|
| 任务调度 | **Bull + Redis** | 定时检测人员配置不足、生成每日提醒 |
| 实时推送 | **WebSocket (Socket.io)** | 换班申请审核结果、配置预警实时推送 |
| 文件导出 | **ExcelJS** | 员工列表导出为 Excel |

### 6.3 数据库

| 用途 | 技术 | 说明 |
|------|------|------|
| 主数据库 | **PostgreSQL 16** | 支持 JSON 字段（休息日等），事务完整性强 |
| 缓存/队列 | **Redis 7** | Session 缓存、Bull 队列、高频 KPI 数据缓存 |
| 搜索增强 | **内置 PostgreSQL 全文索引** | 员工姓名/工号搜索，无需引入 ES |

### 6.4 基础设施

| 类别 | 建议 | 说明 |
|------|------|------|
| 容器化 | **Docker + Docker Compose** | 本地开发与生产环境一致 |
| 部署平台 | **阿里云 ECS / 腾讯云 CVM** | 国内合规，延迟低 |
| 反向代理 | **Nginx** | 静态资源托管 + API 反代 |
| CI/CD | **GitHub Actions** | 自动化测试、构建、部署 |
| 监控告警 | **Prometheus + Grafana** | 服务健康监控、慢查询告警 |
| 日志 | **Winston + 阿里云 SLS** | 结构化日志收集与查询 |

### 6.5 目录结构（前端）

```
src/
├── api/              # Axios 请求封装（按模块划分）
├── components/       # 通用 UI 组件（KPICard, StatusBadge, etc.）
├── pages/
│   ├── dashboard/    # 仪表板
│   ├── stores/       # 门店分布概览
│   ├── employees/    # 员工管理 & 员工列表
│   ├── schedules/    # 排班计划 & 新增班次
│   └── settings/     # 系统设置
├── store/            # Zustand 全局状态
├── hooks/            # 自定义 Hook
├── utils/            # 工具函数（日期格式化、权限判断等）
├── types/            # TypeScript 全局类型定义
└── router/           # 路由配置与权限守卫
```

### 6.6 目录结构（后端）

```
src/
├── modules/
│   ├── auth/         # JWT 认证、登录注销
│   ├── users/        # 系统用户管理
│   ├── stores/       # 门店 CRUD
│   ├── employees/    # 员工档案、偏好设置
│   ├── schedules/    # 排班计划、班次分配
│   ├── swap-requests/# 换班申请审核
│   ├── notifications/# 消息通知
│   └── reports/      # 数据报表、KPI 计算
├── common/
│   ├── guards/       # 权限守卫（RBAC）
│   ├── interceptors/ # 请求/响应拦截器
│   ├── filters/      # 全局异常过滤器
│   └── decorators/   # 自定义装饰器
├── prisma/           # Prisma Schema & 迁移文件
└── main.ts
```

---

## 7. 非功能性需求

| 维度 | 要求 |
|------|------|
| 性能 | 列表页首屏加载 < 2s；仪表板 KPI 计算接口响应 < 500ms |
| 并发 | 支持 200+ 同时在线用户（多门店 HR + 店长） |
| 权限 | 基于角色的细粒度权限控制，员工只能查看本人排班 |
| 安全 | HTTPS 全站；敏感操作记录操作日志；手机号脱敏展示 |
| 响应式 | 主要针对 PC 端 1280px+ 宽屏，兼容 1024px 平板 |
| 国际化 | 当前仅支持简体中文，预留 i18n 架构 |
| 可维护性 | 关键业务逻辑覆盖单元测试，核心接口覆盖 E2E 测试 |
