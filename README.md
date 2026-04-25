# 门店排班管理系统

面向连锁零售品牌的企业级人力资源与排班管理平台。

## 技术栈

### 前端
- Vue 3 + TypeScript + Vite
- Element Plus UI 组件库
- ECharts 图表
- Pinia 状态管理
- Vue Router 4
- Axios + Tailwind CSS

### 后端
- Java 17 + Spring Boot 3
- Spring Security + JWT 认证
- MyBatis-Plus ORM
- PostgreSQL 数据库
- SpringDoc OpenAPI (Swagger)

## 快速启动

### 前端
```bash
cd frontend
npm install
npm run dev
```
访问 http://localhost:5173

### 后端
```bash
# 1. 确保 PostgreSQL 已启动，创建数据库
createdb shift_management

# 2. 执行数据库迁移
psql -d shift_management -f backend/src/main/resources/db/V1__init_schema.sql
psql -d shift_management -f backend/src/main/resources/db/V2__seed_data.sql

# 3. 启动后端
cd backend
mvn spring-boot:run
```
访问 http://localhost:8080

### API 文档
http://localhost:8080/swagger-ui.html

### 默认账号
- 用户名: `admin`
- 密码: `admin123`

## 功能模块

- 📊 **仪表板** - KPI 概览、排班趋势图、今日提醒
- 🏪 **门店管理** - 门店信息、状态管理
- 👥 **员工管理** - 员工档案、排班偏好设置
- 📅 **排班计划** - 班次创建、员工分配
- 🔄 **换班审核** - 换班申请、审批流程
- 🔔 **通知中心** - 预警、提醒管理

## 数据库配置

编辑 `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shift_management
    username: postgres
    password: postgres
```
