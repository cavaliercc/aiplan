-- =====================================================
-- 排班管理系统 初始化数据
-- V2: 种子数据
-- 注意：admin 密码为 admin123（BCrypt 加密）
-- =====================================================

-- 启用 pgcrypto 扩展以便安全生成 BCrypt 密码（可选）
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 岗位数据
INSERT INTO positions (name, level) VALUES
    ('店长',       5),
    ('副店长',     4),
    ('资深员工',   3),
    ('普通员工',   2),
    ('实习员工',   1)
ON CONFLICT DO NOTHING;

-- 门店数据
INSERT INTO stores (id, name, city, province, address, phone, open_time, close_time, status) VALUES
    ('STORE001', '北京朝阳旗舰店', '北京', '北京市', '朝阳区建国路88号', '010-88888001', '08:00', '22:00', 'ACTIVE'),
    ('STORE002', '北京海淀店',     '北京', '北京市', '海淀区中关村大街1号', '010-88888002', '09:00', '21:00', 'ACTIVE'),
    ('STORE003', '上海浦东店',     '上海', '上海市', '浦东新区陆家嘴环路1000号', '021-88888003', '08:00', '22:00', 'ACTIVE')
ON CONFLICT DO NOTHING;

-- 员工数据
INSERT INTO employees (emp_no, name, phone, store_id, position_id, hire_date, status) VALUES
    ('EMP001', '张伟',   '13800138001', 'STORE001', 1, '2020-01-15', 'ACTIVE'),
    ('EMP002', '李娜',   '13800138002', 'STORE001', 2, '2020-03-20', 'ACTIVE'),
    ('EMP003', '王芳',   '13800138003', 'STORE001', 4, '2021-06-01', 'ACTIVE'),
    ('EMP004', '刘洋',   '13800138004', 'STORE002', 1, '2019-11-10', 'ACTIVE'),
    ('EMP005', '陈晓',   '13800138005', 'STORE002', 4, '2022-02-14', 'ACTIVE'),
    ('EMP006', '赵明',   '13800138006', 'STORE003', 1, '2020-07-22', 'ACTIVE')
ON CONFLICT DO NOTHING;

-- 更新门店经理
UPDATE stores SET manager_id = (SELECT id FROM employees WHERE emp_no = 'EMP001') WHERE id = 'STORE001';
UPDATE stores SET manager_id = (SELECT id FROM employees WHERE emp_no = 'EMP004') WHERE id = 'STORE002';
UPDATE stores SET manager_id = (SELECT id FROM employees WHERE emp_no = 'EMP006') WHERE id = 'STORE003';

-- 用户账号
-- 密码 admin123 的 BCrypt(10) 哈希值
-- 若需重新生成：new BCryptPasswordEncoder(10).encode("admin123")
INSERT INTO users (username, password, role, employee_id) VALUES
    ('admin',    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyNv68Qn', 'ADMIN',    NULL),
    ('zhangwei', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyNv68Qn', 'MANAGER',  (SELECT id FROM employees WHERE emp_no = 'EMP001')),
    ('liuna',    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyNv68Qn', 'EMPLOYEE', (SELECT id FROM employees WHERE emp_no = 'EMP002'))
ON CONFLICT DO NOTHING;

-- 班次类型
INSERT INTO shift_types (name, start_time, end_time, store_id) VALUES
    ('早班',   '08:00', '16:00', 'STORE001'),
    ('中班',   '12:00', '20:00', 'STORE001'),
    ('晚班',   '16:00', '24:00', 'STORE001'),
    ('早班',   '09:00', '17:00', 'STORE002'),
    ('晚班',   '13:00', '21:00', 'STORE002')
ON CONFLICT DO NOTHING;

-- 排班示例（本周）
INSERT INTO schedules (store_id, shift_date, shift_type_id, zone, status, created_by)
SELECT
    'STORE001',
    CURRENT_DATE,
    (SELECT id FROM shift_types WHERE name = '早班' AND store_id = 'STORE001' LIMIT 1),
    'A区',
    'PUBLISHED',
    (SELECT id FROM users WHERE username = 'admin' LIMIT 1)
ON CONFLICT DO NOTHING;

-- 通知示例
INSERT INTO notifications (type, title, content, related_type, target_user, is_read)
SELECT
    'INFO',
    '欢迎使用排班管理系统',
    '您的账号已创建，请及时修改默认密码。',
    'SYSTEM',
    id,
    FALSE
FROM users
ON CONFLICT DO NOTHING;
