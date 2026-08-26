INSERT INTO member (account, password, role, name, birthdate, email, photo) VALUES
('vendor01', 'pass123', 'VENDOR', '極致數位館', '1985-03-15', 'vendor01@example.com', NULL),
('vendor02', 'pass123', 'VENDOR', '潮流服飾專賣', '1990-07-22', 'vendor02@example.com', NULL),
('vendor03', 'pass123', 'VENDOR', '美味生活家', '1988-11-05', 'vendor03@example.com', NULL),
('buyer01', 'pass123', 'BUYER', '陳小明', '1995-01-10', 'buyer01@example.com', NULL),
('buyer02', 'pass123', 'BUYER', '林美玲', '1998-04-18', 'buyer02@example.com', NULL),
('buyer03', 'pass123', 'BUYER', '張大衛', '1992-09-30', 'buyer03@example.com', NULL),
('buyer04', 'pass123', 'BUYER', '黃雅婷', '2000-12-05', 'buyer04@example.com', NULL),
('buyer05', 'pass123', 'BUYER', '周杰倫', '1982-06-12', 'buyer05@example.com', NULL),
('buyer06', 'pass123', 'BUYER', '許小華', '1996-08-25', 'buyer06@example.com', NULL),
('buyer07', 'pass123', 'BUYER', '鄭家豪', '1994-02-14', 'buyer07@example.com', NULL);

INSERT INTO product (name, description, price, quantity, date, vendor_id, photo) VALUES
('無線降噪耳機', '支援主動降噪，續航力長達 30 小時', 3500.00, 50, CURRENT_TIMESTAMP(), 1, NULL),
('27吋 4K 顯示器', 'IPS 面板，高色域與廣視角', 8900.00, 20, CURRENT_TIMESTAMP(), 1, NULL),
('機械式 RGB 鍵盤', '青軸手感，全鍵無衝與自訂燈光', 2200.00, 35, CURRENT_TIMESTAMP(), 1, NULL),
('人體工學滑鼠', '多段 DPI 可調，有效緩解手腕疲勞', 1200.00, 60, CURRENT_TIMESTAMP(), 1, NULL),
('純棉寬鬆 T 恤', '100% 重磅純棉，舒適透氣親膚', 490.00, 100, CURRENT_TIMESTAMP(), 2, NULL),
('防風連帽外套', '防撥水面料，保暖耐磨適合戶外', 1580.00, 40, CURRENT_TIMESTAMP(), 2, NULL),
('經典牛仔褲', '修身剪裁，彈性面料活動自如', 1280.00, 45, CURRENT_TIMESTAMP(), 2, NULL),
('精品手沖咖啡豆', '耶加雪菲中淺烘焙，帶有果香風味', 450.00, 80, CURRENT_TIMESTAMP(), 3, NULL),
('不鏽鋼保溫杯 500ml', '雙層真空保溫，長效保溫保冷 12 小時', 680.00, 50, CURRENT_TIMESTAMP(), 3, NULL),
('微波爐專用氣炸鍋', '大容量多功能，油炸烘烤一機搞定', 2680.00, 15, CURRENT_TIMESTAMP(), 3, NULL);

INSERT INTO cart (member_id, product_id, quantity, created_date) VALUES
(4, 1, 1, CURRENT_TIMESTAMP()),
(4, 5, 2, CURRENT_TIMESTAMP()),
(5, 2, 1, CURRENT_TIMESTAMP()),
(5, 8, 3, CURRENT_TIMESTAMP()),
(6, 3, 1, CURRENT_TIMESTAMP()),
(7, 4, 2, CURRENT_TIMESTAMP()),
(7, 6, 1, CURRENT_TIMESTAMP()),
(8, 7, 1, CURRENT_TIMESTAMP()),
(9, 9, 2, CURRENT_TIMESTAMP()),
(10, 10, 1, CURRENT_TIMESTAMP());