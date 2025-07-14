# Fastfood Backend API

## Mô tả
Đây là phần backend của ứng dụng Fastfood, xây dựng bằng Node.js với Express. Hệ thống hỗ trợ quản lý người dùng, xác thực JWT, quản lý món ăn, và kết nối cơ sở dữ liệu PostgreSQL (sử dụng Sequelize).

## Cài đặt

1. **Clone dự án:**
   ```bash
   git clone https://github.com/1usuzu/fastfood-fullstack.git
   cd backend_fastfood_app
   ```

2. **Cài đặt các package cần thiết:**
   ```bash
   npm install express sequelize pg pg-hstore bcryptjs jsonwebtoken cors dotenv moment qs
   ```
   - `express`: Framework web
   - `sequelize`: ORM cho Node.js
   - `pg` và `pg-hstore`: Driver cho PostgreSQL
   - `moment qs`: Cho vnpay
   - `bcryptjs`, `jsonwebtoken`, `cors`, `dotenv`: Các thư viện hỗ trợ khác

3. **Tạo file `.env`**
   Tạo file `.env` ở thư mục gốc với nội dung gồm:
   ```env
   DATABASE_URL
   VNP_TMNCODE
   VNP_HASHSECRET
   VNP_URL
   VNP_RETURNURL
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
- `config/database.js` — Kết nối cơ sở dữ liệu PostgreSQL (Sequelize)

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

## Ghi chú
- Đảm bảo đã cài Node.js >= 14
- Đảm bảo đã cấu hình đúng biến môi trường trong `.env`
- Đã tạo database PostgreSQL trước khi chạy

## License
MIT
