# Fastfood Android

## Mô tả
Đây là phần adnroid của ứng dụng Fastfood, phát triển bằng Java. Ứng dụng cho phép người dùng đăng ký, đăng nhập, xem danh sách món ăn, thêm vào giỏ hàng, đặt món và xem lịch sử đơn hàng. Dữ liệu được đồng bộ với backend Node.js (Express) thông qua REST API.

## Tính năng chính
- Đăng ký, đăng nhập (JWT token + SessionManager)
- Ghi nhớ đăng nhập, quên mật khẩu
- Xem danh sách món ăn, lọc theo danh mục
- Chi tiết món ăn, ghi chú, chọn số lượng
- Thêm vào giỏ hàng, tính tổng hóa đơn
- Đặt hàng và xem lịch sử đặt hàng
- Chỉnh sửa thông tin cá nhân

## Cài đặt
1. **Clone dự án:**
   ```bash
   git clone -b final https://github.com/1usuzu/fastfood-fullstack.git
   cd android_fastfood_app
   ```

2. **Mở project bằng Android Studio**

3. **Chỉnh sửa địa chỉ IP trong RetrofitClient.java:**
```bash
private static final String BASE_URL = "http://10.0.2.2:3000/"; // Sửa IP nếu chạy trên thiết bị thật
```

4. **Build và Run ứng dụng trên máy ảo hoặc thiết bị thật**


# Fastfood Backend

## Mô tả
Đây là phần backend của ứng dụng Fastfood, xây dựng bằng Node.js với Express. Hệ thống hỗ trợ quản lý người dùng, xác thực JWT, quản lý món ăn, và kết nối cơ sở dữ liệu PostgreSQL (sử dụng Sequelize).

## Cài đặt

1. **Clone dự án:**
   ```bash
   git clone -b final https://github.com/1usuzu/fastfood-fullstack.git
   cd backend_fastfood_app
   ```

2. **Cài đặt các package cần thiết:**
   ```bash
   npm install express sequelize pg pg-hstore bcryptjs jsonwebtoken cors dotenv moment qs
   ```
   - `express`: Framework web
   - `sequelize`: ORM cho Node.js
   - `pg` và `pg-hstore`: Driver cho MySQL
   - `moment qs`: Thư viện cho VNPay
   - `bcryptjs`, `jsonwebtoken`, `cors`, `dotenv`: Các thư viện hỗ trợ khác

3. **Tạo file `.env`**
   Tạo file `.env` ở thư mục gốc với nội dung gồm:
   ```env
   DATABASE_URL=...
   VNP_TMNCODE=...
   VNP_HASHSECRET=...
   VNP_URL=...
   VNP_RETURNURL=...
   ```

4. **Chạy server:**
   ```bash
   node index.js
   ```

## Các API chính

### Người dùng
- `POST /register` — Đăng ký tài khoản
- `POST /login` — Đăng nhập, trả về JWT

### Món ăn
- `GET /foods` — Lấy danh sách món ăn
- `POST /foods` — Thêm món ăn mới

## Cấu trúc thư mục
- `index.js` — File chính khởi động server
- `models/` — Định nghĩa các model
- `routes/` — Định nghĩa các route
- `controllers/`: Logic xử lý API
- `config/database.js` — Kết nối cơ sở dữ liệu MySQL (Sequelize)

## Thư viện sử dụng
- express
- sequelize
- pg
- pg-hstore
- bcryptjs
- jsonwebtoken
- cors
- dotenv
- moment qs
