-- 插入角色
INSERT INTO roles (name, permissions) VALUES
                                          ('Admin', 'all'),
                                          ('Manager', 'manage_parking,manage_users'),
                                          ('User', 'view_only');

-- 插入用户
INSERT INTO users (username, password, name, email, role_id) VALUES
                                                                 ('admin', 'password', 'Admin User', 'admin@example.com', 1),
                                                                 ('manager', 'password', 'Manager User', 'manager@example.com', 2),
                                                                 ('user', 'password', 'Regular User', 'user@example.com', 3);

-- 插入客户
INSERT INTO customers (name, contactInfo) VALUES
                                              ('John Doe', 'john@example.com'),
                                              ('Jane Smith', 'jane@example.com');

-- 插入车辆
INSERT INTO vehicles (licensePlate, make, model, customer_id) VALUES
                                                                  ('ABC123', 'Toyota', 'Camry', 1),
                                                                  ('DEF456', 'Honda', 'Civic', 2);

-- 插入停车场
INSERT INTO parking_lots (name, address, capacity) VALUES
                                                       ('Main Parking Lot', '123 Main St', 100),
                                                       ('Secondary Parking Lot', '456 Second St', 50);

-- 插入停车位（示例：为 Main Parking Lot 插入 2 个停车位）
INSERT INTO parking_spaces (parkingLot_id, spaceNumber, status) VALUES
                                                                    (1, 'A1', 'free'),
                                                                    (1, 'A2', 'free'),
                                                                    (2, 'B1', 'free'),
                                                                    (2, 'B2', 'free');

-- 插入停车记录（示例：车辆 ABC123 于昨天停到今天）
INSERT INTO parking_records (vehicle_id, parkingSpace_id, entryTime, exitTime, feePaid) VALUES
    (1, 1, '2025-03-09 08:00:00', '2025-03-10 08:00:00', 10.00);

-- 插入财务交易
INSERT INTO financial_transactions (parkingRecord_id, amount, paymentMethod, transactionTime) VALUES
    (1, 10.00, 'cash', '2025-03-10 08:00:00');

-- 插入合作单位
INSERT INTO partners (name, contactInfo, relationshipType) VALUES
    ('Security Company', 'security@example.com', 'security');

-- 插入系统日志
INSERT INTO system_logs (user_id, actionType, details, timestamp) VALUES
    (1, 'login', 'Admin logged in', '2025-03-10 14:00:00');

-- 插入全局配置
INSERT INTO global_configurations (key, value) VALUES
                                                   ('parkingFeePerHour', '2.00'),
                                                   ('operatingHoursStart', '08:00'),
                                                   ('operatingHoursEnd', '20:00');

-- 插入菜单
INSERT INTO menus (name, url, icon, parent_id) VALUES
                                                   ('Dashboard', '/dashboard', 'home', NULL),
                                                   ('Parking Management', '/parking-lots', 'parking', NULL),
                                                   ('Vehicle Management', '/vehicles', 'car', NULL),
                                                   ('User Management', '/users', 'users', NULL),
                                                   ('Reports', '/reports', 'chart', NULL);

-- 插入角色-菜单映射（示例：为 Admin 角色分配所有菜单）
INSERT INTO role_menu (role_id, menu_id) VALUES
                                             (1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
                                             (2, 1), (2, 2), (2, 3),
                                             (3, 1);