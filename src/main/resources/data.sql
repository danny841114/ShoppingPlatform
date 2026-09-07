INSERT INTO users (account, password, created_date) VALUES
('vendor01', 'pass123', CURRENT_TIMESTAMP()),
('vendor02', 'pass123', CURRENT_TIMESTAMP()),
('vendor03', 'pass123', CURRENT_TIMESTAMP()),
('buyer01',  'pass123', CURRENT_TIMESTAMP()),
('buyer02',  'pass123', CURRENT_TIMESTAMP()),
('buyer03',  'pass123', CURRENT_TIMESTAMP());

INSERT INTO member (user_id, name, birthdate, email, photo) VALUES
(1, '王大衛', '1988-05-12', 'vendor01@example.com', NULL),
(2, '李佳玲', '1992-09-03', 'vendor02@example.com', NULL),
(3, '張建國', '1985-11-20', 'vendor03@example.com', NULL),
(4, '陳小明', '1995-01-10', 'buyer01@example.com', NULL),
(5, '林美玲', '1998-04-18', 'buyer02@example.com', NULL),
(6, '張志豪', '2000-08-25', 'buyer03@example.com', NULL);

INSERT INTO vendor (user_id, shop_name, description) VALUES
(1, '極致數位館', '專業 3C 數碼周邊專賣店'),
(2, '潮流服飾專賣', '提供高品質質感日常穿搭'),
(3, '居家生活工坊', '打造舒適溫馨的質感居家空間');

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

INSERT INTO cart_item (member_id, product_id, quantity, created_date) VALUES
(4, 1, 1, CURRENT_TIMESTAMP()),
(4, 4, 1, CURRENT_TIMESTAMP()),
(4, 5, 2, CURRENT_TIMESTAMP()),
(5, 2, 1, CURRENT_TIMESTAMP()),
(5, 6, 1, CURRENT_TIMESTAMP()),
(6, 3, 1, CURRENT_TIMESTAMP()),
(6, 10, 3, CURRENT_TIMESTAMP());

INSERT INTO orders
(order_number, total_amount, shipping_fee, status, receiver_name, receiver_phone, receiver_email, receiver_address, payment_method, note, created_date, member_id, vendor_id)
VALUES
('ORD20260301001', 4780.00, 60.00, 'COMPLETED', '陳小明', '0912345678', 'buyer01@example.com', '台北市信義區信義路五段7號', 'CREDIT_CARD', '請放置管理室', CURRENT_TIMESTAMP(), 4, 1),
('ORD20260301002', 1580.00, 60.00, 'SHIPPED',   '陳小明', '0912345678', 'buyer01@example.com', '台北市信義區信義路五段7號', 'LINE_PAY',    '發票請打統編', CURRENT_TIMESTAMP(), 4, 2),
('ORD20260302001', 8900.00,  0.00, 'PAID',      '林美玲', '0923456789', 'buyer02@example.com', '台中市西屯區台灣大道三段99號', 'CREDIT_CARD', NULL,         CURRENT_TIMESTAMP(), 5, 1),
('ORD20260302002',  790.00, 60.00, 'PENDING',   '林美玲', '0923456789', 'buyer02@example.com', '台中市西屯區台灣大道三段99號', 'COD',          '送達前請先電聯', CURRENT_TIMESTAMP(), 5, 3);

INSERT INTO order_item (order_id, product_id, product_name, price, quantity) VALUES
(1, 1, '無線降噪耳機', 3500.00, 1),
(1, 4, '多功能 USB-C 集線器', 1280.00, 1),
(2, 6, '防風連帽外套', 1580.00, 1),
(3, 2, '27吋 4K 顯示器', 8900.00, 1),
(4, 8, '香氛造型水氧機', 790.00, 1);