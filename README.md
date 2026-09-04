# 🛒 簡易電商購物平台 - 後端 API 服務 (Spring Boot)

本專案為電商購物平台的**後端 RESTful API 服務**（前後端分離架構）。提供完整的會員身份認證、買家購物車/訂單管理，以及賣家商品管理功能。

> 💡 **備註**：本 Repo 僅包含後端 API 程式碼。Vue.js 前端專案位於獨立的 Repository。

---

## 🚀 系統功能與權限

### 1. 使用者與權限管理 (User & Auth)
- **會員認證**：使用者註冊、登入與登出（採用 JWT + HTTP-only Cookie 安全機制）
- **雙身分切換**：支援買家 (`ROLE_MEMBER`) 與賣家 (`ROLE_VENDOR`) 身分開通與即時視角切換

### 2. 商品管理 (Products)
- **公開瀏覽**：所有使用者（含未登入訪客）可查詢商品列表、關鍵字搜尋與檢視商品詳情
- **賣家管理**：具備賣家身分者可進行商品的**上架**、**圖片上傳/修改**與**下架刪除**

### 3. 購物車管理 (Cart)
- **購物車操作**：買家可將商品加入購物車、調整商品數量及移除購物車項目

### 4. 訂單管理 (Orders)
- **訂單功能**：買家可進行結帳下單，並查詢個人歷史訂單明細

---

## 🛠️ 技術選型

- **核心語言與框架**：Java 21、Spring Boot 3
- **安全控管**：Spring Security（無狀態 JWT 認證、角色權限控制）
- **資料庫與持久層**：H2 Database（開發/測試環境）、Spring Data JPA
- **API 架構**：RESTful API 設計

---

## ⚙️ 快速啟動 (Local Development)

1. **環境需求**：JDK 21+
2. **啟動步驟**：
    - 複製本專案並使用 IDE (IntelliJ IDEA) 開啟。
    - 執行 `Application.java` 啟動服務（預設 Port: `8080`）。
3. **資料庫控制台 (H2 Console)**：
    - 存取路徑：`http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`