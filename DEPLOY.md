# 部署指南

## 前端 (Vercel)

已部署：**https://aiplan-eta.vercel.app/**

配置：
- Root Directory: `frontend`
- Framework: Vue.js
- Build: `npm run build`
- Output: `dist`

## 后端 (Railway)

### 1. 在 Railway 创建项目

1. 打开 https://railway.app
2. 点 **"New Project"** → **"Deploy from GitHub repo"**
3. 选择 `cavaliercc/aiplan` 仓库

### 2. 配置环境变量

在 Railway 面板的 **Variables** 中添加：

| 变量名 | 值 |
|--------|-----|
| `PORT` | `8080` |
| `DATABASE_URL` | (PostgreSQL 连接字符串，见下一步) |
| `DATABASE_USERNAME` | `postgres` |
| `DATABASE_PASSWORD` | (你的数据库密码) |
| `JWT_SECRET` | `shift-management-jwt-secret-key-2026` |

### 3. 添加 PostgreSQL 数据库

1. 在 Railway 项目页点 **"New"** → **"Database"** → **"PostgreSQL"**
2. 数据库创建后，复制 `DATABASE_URL` 环境变量
3. Railway 会自动提供 `DATABASE_URL`、`PGUSER`、`PGPASSWORD` 等变量

### 4. 部署

Railway 会自动检测 Dockerfile 并构建。构建完成后会给你一个 URL，类似：
`https://xxx-production.up.railway.app`

### 5. 初始化数据库

首次部署后，需要执行 SQL 迁移：

```sql
-- 在 Railway PostgreSQL 控制台中执行
-- 复制 backend/src/main/resources/db/V1__init_schema.sql 内容
-- 复制 backend/src/main/resources/db/V2__seed_data.sql 内容
```

### 6. 配置前端 API 地址

在 Vercel 前端项目中添加环境变量：
- `VITE_API_BASE_URL` = `https://xxx-production.up.railway.app`

重新部署前端即可。

## 本地开发

### 前端
```bash
cd frontend
npm install
npm run dev
```

### 后端
```bash
# 需要本地 PostgreSQL
createdb shift_management
psql -d shift_management -f backend/src/main/resources/db/V1__init_schema.sql
psql -d shift_management -f backend/src/main/resources/db/V2__seed_data.sql

cd backend
mvn spring-boot:run
```

访问 http://localhost:8080/swagger-ui.html
