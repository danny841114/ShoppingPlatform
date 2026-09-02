-- 1. 建立基礎 User 帳號 (2 位賣家, 2 位買家)
INSERT INTO users (id, account, password, role, created_date) VALUES
(1, 'vendor01', 'pass123', 'ROLE_VENDOR', CURRENT_TIMESTAMP()),
(2, 'vendor02', 'pass123', 'ROLE_VENDOR', CURRENT_TIMESTAMP()),
(3, 'buyer01',  'pass123', 'ROLE_MEMBER', CURRENT_TIMESTAMP()),
(4, 'buyer02',  'pass123', 'ROLE_MEMBER', CURRENT_TIMESTAMP());

-- 2. 建立 Vendor Profile (賣家資料，關聯至 users)
INSERT INTO vendor (id, user_id, shop_name, description) VALUES
(1, 1, '極致數位館', '專業 3C 數碼周邊專賣店'),
(2, 2, '潮流服飾專賣', '提供高品質質感日常穿搭');

-- 3. 建立 Member Profile (買家資料，關聯至 users)
INSERT INTO member (id, user_id, name, birthdate, email, photo) VALUES
(1, 3, '陳小明', '1995-01-10', 'buyer01@example.com', NULL),
(2, 4, '林美玲', '1998-04-18', 'buyer02@example.com', NULL);

-- 4. 建立 Product (商品，vendor_id 對應 vendor 表的 id)
INSERT INTO product (id, name, description, price, quantity, date, vendor_id, photo) VALUES
-- Vendor 1 (極致數位館) 的商品
(1, '無線降噪耳機', '支援主動降噪，續航力長達 30 小時', 3500.00, 50, CURRENT_TIMESTAMP(), 1, NULL),
(2, '27吋 4K 顯示器', 'IPS 面板，高色域與廣視角', 8900.00, 20, CURRENT_TIMESTAMP(), 1, NULL),
(3, '機械式 RGB 鍵盤', '青軸手感，全鍵無衝與自訂燈光', 2200.00, 35, CURRENT_TIMESTAMP(), 1, NULL),
-- Vendor 2 (潮流服飾專賣) 的商品
(4, '純棉寬鬆 T 恤', '100% 重磅純棉，舒適透氣親膚', 490.00, 100, CURRENT_TIMESTAMP(), 2, NULL),
(5, '防風連帽外套', '防撥水面料，保暖耐磨適合戶外', 1580.00, 40, CURRENT_TIMESTAMP(), 2, NULL);

-- 5. 建立 Cart (購物車，member_id 對應 member 表的 id)
INSERT INTO cart (member_id, product_id, quantity, created_date) VALUES
-- 買家 1 (陳小明) 放入 Vendor 1 與 Vendor 2 的商品
(1, 1, 1, CURRENT_TIMESTAMP()), -- 無線降噪耳機 (Vendor 1)
(1, 4, 2, CURRENT_TIMESTAMP()), -- 純棉寬鬆 T 恤 (Vendor 2)
-- 買家 2 (林美玲) 購物車商品
(2, 2, 1, CURRENT_TIMESTAMP()), -- 27吋 4K 顯示器 (Vendor 1)
(2, 5, 1, CURRENT_TIMESTAMP()); -- 防風連帽外套 (Vendor 2)