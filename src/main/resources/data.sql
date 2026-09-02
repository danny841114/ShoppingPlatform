INSERT INTO users (account, password, created_date) VALUES
('vendor01', 'pass123', CURRENT_TIMESTAMP()),
('vendor02', 'pass123', CURRENT_TIMESTAMP()),
('vendor03', 'pass123', CURRENT_TIMESTAMP()),
('buyer01',  'pass123', CURRENT_TIMESTAMP()),
('buyer02',  'pass123', CURRENT_TIMESTAMP()),
('buyer03',  'pass123', CURRENT_TIMESTAMP());

INSERT INTO vendor (user_id, shop_name, description) VALUES
(1, '極致數位館', '專業 3C 數碼周邊專賣店'),
(2, '潮流服飾專賣', '提供高品質質感日常穿搭'),
(3, '居家生活工坊', '打造舒適溫馨的質感居家空間');

INSERT INTO member (user_id, name, birthdate, email, photo) VALUES
(4, '陳小明', '1995-01-10', 'buyer01@example.com', NULL),
(5, '林美玲', '1998-04-18', 'buyer02@example.com', NULL),
(6, '張志豪', '2000-08-25', 'buyer03@example.com', NULL);

INSERT INTO product (name, description, price, quantity, date, vendor_id, photo) VALUES
('無線降噪耳機', '支援主動降噪，續航力長達 30 小時', 3500.00, 50, CURRENT_TIMESTAMP(), 1, NULL),
('27吋 4K 顯示器', 'IPS 面板，高色域與廣視角', 8900.00, 20, CURRENT_TIMESTAMP(), 1, NULL),
('機械式 RGB 鍵盤', '青軸手感，全鍵無衝與自訂燈光', 2200.00, 35, CURRENT_TIMESTAMP(), 1, NULL),
('多功能 USB-C 集線器', '支援 4K HDMI 與 100W 快充', 1280.00, 60, CURRENT_TIMESTAMP(), 1, NULL),
('純棉寬鬆 T 恤', '100% 重磅純棉，舒適透氣親膚', 490.00, 100, CURRENT_TIMESTAMP(), 2, NULL),
('防風連帽外套', '防撥水面料，保暖耐磨適合戶外', 1580.00, 40, CURRENT_TIMESTAMP(), 2, NULL),
('工裝九分休閒褲', '多口袋設計，修身耐磨彈性布料', 980.00, 75, CURRENT_TIMESTAMP(), 2, NULL),
('香氛造型水氧機', '超音波霧化，具備七彩夜燈功能', 790.00, 80, CURRENT_TIMESTAMP(), 3, NULL),
('人體工學記憶枕', '慢回彈材質，有效支撐頸椎健康', 1200.00, 30, CURRENT_TIMESTAMP(), 3, NULL),
('極簡陶瓷馬克杯', '350ml 容量，附手感木質杯墊', 320.00, 150, CURRENT_TIMESTAMP(), 3, NULL);

INSERT INTO cart (member_id, product_id, quantity, created_date) VALUES
(1, 1, 1, CURRENT_TIMESTAMP()),
(1, 4, 1, CURRENT_TIMESTAMP()),
(1, 5, 2, CURRENT_TIMESTAMP()),
(1, 8, 1, CURRENT_TIMESTAMP()),
(2, 2, 1, CURRENT_TIMESTAMP()),
(2, 6, 1, CURRENT_TIMESTAMP()),
(2, 9, 2, CURRENT_TIMESTAMP()),
(3, 3, 1, CURRENT_TIMESTAMP()),
(3, 7, 1, CURRENT_TIMESTAMP()),
(3, 10, 3, CURRENT_TIMESTAMP());