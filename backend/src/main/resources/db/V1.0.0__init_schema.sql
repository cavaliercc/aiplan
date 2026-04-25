-- =====================================================
-- 排班管理系统 数据库初始化脚本
-- V1: 建表
-- =====================================================

-- 岗位表
CREATE TABLE IF NOT EXISTS positions (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    level       INTEGER      NOT NULL DEFAULT 1,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 门店表
CREATE TABLE IF NOT EXISTS stores (
    id          VARCHAR(20)  PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    city        VARCHAR(50),
    province    VARCHAR(50),
    address     VARCHAR(200),
    phone       VARCHAR(20),
    open_time   TIME,
    close_time  TIME,
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    manager_id  INTEGER,
    resume_date DATE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 员工表
CREATE TABLE IF NOT EXISTS employees (
    id          SERIAL PRIMARY KEY,
    emp_no      VARCHAR(20)  UNIQUE NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    avatar_url  VARCHAR(500),
    phone       VARCHAR(20),
    store_id    VARCHAR(20)  REFERENCES stores(id),
    position_id INTEGER      REFERENCES positions(id),
    hire_date   DATE,
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 门店经理外键（延迟约束）
ALTER TABLE stores
    ADD CONSTRAINT fk_stores_manager
    FOREIGN KEY (manager_id) REFERENCES employees(id)
    DEFERRABLE INITIALLY DEFERRED;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id          SERIAL PRIMARY KEY,
    username    VARCHAR(50)  UNIQUE NOT NULL,
    password    VARCHAR(200) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'EMPLOYEE',
    employee_id INTEGER      REFERENCES employees(id),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 车辆表
CREATE TABLE IF NOT EXISTS vehicles (
    id          SERIAL PRIMARY KEY,
    plate_no    VARCHAR(20)  NOT NULL,
    store_id    VARCHAR(20)  REFERENCES stores(id),
    status      VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 班次类型表
CREATE TABLE IF NOT EXISTS shift_types (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    start_time  TIME         NOT NULL,
    end_time    TIME         NOT NULL,
    store_id    VARCHAR(20)  REFERENCES stores(id),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 排班表
CREATE TABLE IF NOT EXISTS schedules (
    id            SERIAL PRIMARY KEY,
    store_id      VARCHAR(20)  NOT NULL REFERENCES stores(id),
    shift_date    DATE         NOT NULL,
    shift_type_id INTEGER      REFERENCES shift_types(id),
    zone          VARCHAR(50),
    vehicle_id    INTEGER      REFERENCES vehicles(id),
    status        VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    created_by    INTEGER      REFERENCES users(id),
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 排班人员分配表
CREATE TABLE IF NOT EXISTS schedule_assignments (
    id          SERIAL PRIMARY KEY,
    schedule_id INTEGER      NOT NULL REFERENCES schedules(id),
    employee_id INTEGER      NOT NULL REFERENCES employees(id),
    checked_in  TIMESTAMP,
    checked_out TIMESTAMP,
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 排班偏好表
CREATE TABLE IF NOT EXISTS schedule_preferences (
    id                 SERIAL PRIMARY KEY,
    employee_id        INTEGER      NOT NULL UNIQUE REFERENCES employees(id),
    preferred_shift_id INTEGER      REFERENCES shift_types(id),
    max_daily_hours    INTEGER,
    rest_days          VARCHAR(50),
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 不可用时间块表
CREATE TABLE IF NOT EXISTS unavailability_blocks (
    id          SERIAL PRIMARY KEY,
    employee_id INTEGER      NOT NULL REFERENCES employees(id),
    date        DATE         NOT NULL,
    start_time  TIME         NOT NULL,
    end_time    TIME         NOT NULL,
    reason      VARCHAR(200),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 换班申请表
CREATE TABLE IF NOT EXISTS shift_swap_requests (
    id                 SERIAL PRIMARY KEY,
    requester_id       INTEGER      NOT NULL REFERENCES employees(id),
    target_id          INTEGER      NOT NULL REFERENCES employees(id),
    requester_sched_id INTEGER      NOT NULL REFERENCES schedules(id),
    target_sched_id    INTEGER      NOT NULL REFERENCES schedules(id),
    reason             TEXT,
    status             VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    reviewed_by        INTEGER      REFERENCES users(id),
    reviewed_at        TIMESTAMP,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 通知表
CREATE TABLE IF NOT EXISTS notifications (
    id           SERIAL PRIMARY KEY,
    type         VARCHAR(20)  NOT NULL,
    title        VARCHAR(100) NOT NULL,
    content      TEXT,
    related_id   INTEGER,
    related_type VARCHAR(50),
    target_user  INTEGER      NOT NULL REFERENCES users(id),
    is_read      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_employees_store_id   ON employees(store_id);
CREATE INDEX IF NOT EXISTS idx_schedules_store_date ON schedules(store_id, shift_date);
CREATE INDEX IF NOT EXISTS idx_assignments_schedule ON schedule_assignments(schedule_id);
CREATE INDEX IF NOT EXISTS idx_notifications_user   ON notifications(target_user);
CREATE INDEX IF NOT EXISTS idx_swap_status          ON shift_swap_requests(status);
