# Ứng dụng Đặt Đồ Ăn Nhanh (FastFood Fullstack)

Một ứng dụng đặt đồ ăn nhanh full-stack với frontend là ứng dụng Android gốc (Java) và backend là API RESTful xây dựng bằng Node.js và Express. Ứng dụng cho phép người dùng đăng ký, đăng nhập, xem và lọc món ăn, quản lý giỏ hàng, đặt hàng và xem lịch sử đơn hàng. Dữ liệu được đồng bộ hóa an toàn giữa ứng dụng và máy chủ.

---

## Tính năng chính

### Xác thực người dùng
- Đăng ký và đăng nhập
- Quản lý phiên với JWT
- Quên mật khẩu
- Mã hóa mật khẩu bằng bcryptjs

###  Quản lý món ăn
- Xem danh sách món ăn
- Lọc theo danh mục
- Xem chi tiết món

### Giỏ hàng
- Thêm món vào giỏ
- Điều chỉnh số lượng
- Thêm ghi chú cho từng món

### Quy trình đặt hàng
- Tính tổng hóa đơn
- Đặt hàng
- Lưu lịch sử đơn hàng

### Quản lý tài khoản
- Xem và sửa thông tin cá nhân

### Thanh toán
- Tích hợp VNPay (ở backend)

---

## Công nghệ sử dụng

### Backend
- **Nền tảng**: Node.js
- **Framework**: Express.js
- **Cơ sở dữ liệu**: MySQL
- **ORM**: Sequelize
- **Xác thực**: JWT, bcryptjs
- **Khác**: CORS, dotenv, moment, qs

### Android
- **Ngôn ngữ**: Java
- **Kiến trúc**: REST API Client
- **Thư viện**: Retrofit, SessionManager

---

## 📦 Cấu trúc thư mục

```
fastfood-fullstack/
├── android_fastfood_app/       # Mã nguồn ứng dụng Android (Java)
│
├── backend_fastfood_app/       # Máy chủ Node.js Express
│   ├── config/                 # Cấu hình kết nối cơ sở dữ liệu (Sequelize)
│   ├── controllers/           # Logic xử lý các API
│   ├── models/                # Các mô hình dữ liệu Sequelize
│   ├── routes/                # Định nghĩa route API
│   ├── .env                   # Biến môi trường (tự tạo)
│   └── index.js               # File chính chạy server
```

---

## ⚙️ Cài đặt và chạy dự án

### 1. Clone repository

```bash
git clone -b final https://github.com/1usuzu/fastfood-fullstack.git
cd fastfood-fullstack
```

---

### 2. Cài đặt Backend

> Cần cài đặt trước Node.js và MySQL

```bash
cd backend_fastfood_app
```

Cài đặt thư viện:
```bash
npm install express sequelize pg pg-hstore bcryptjs jsonwebtoken cors dotenv moment qs
```

Tạo file `.env` và cấu hình như sau:

```env
DATABASE_URL=...

# Thông tin VNPay
VNP_TMNCODE=...
VNP_HASHSECRET=...
VNP_URL=...
VNP_RETURNURL=...
```

Chạy server:
```bash
node index.js
```

> ✅ Server chạy ở `http://localhost:3000`

---

### 3. Cài đặt Frontend (Android)

> 📱 Cần cài Android Studio

- Mở Android Studio → chọn "Open" → chọn thư mục `android_fastfood_app`

Cập nhật địa chỉ IP Backend:
```java
// File: app/src/main/java/com/example/fastfood/RetrofitClient.java
private static final String BASE_URL = "http://10.0.2.2:3000/"; // Thay đổi IP nếu cần
```

- `10.0.2.2`: Dùng cho máy ảo Android
- Dùng IP LAN thực tế cho thiết bị thật (ví dụ `192.168.1.x`)

Chạy ứng dụng:
- Nhấn nút **Run 'app'** trong Android Studio

---

## 🔌 Các API chính

| Phương thức | Endpoint      | Chức năng                    |
|-------------|----------------|------------------------------|
| POST        | `/register`    | Đăng ký tài khoản mới        |
| POST        | `/login`       | Đăng nhập, nhận token JWT    |
| GET         | `/foods`       | Lấy danh sách món ăn         |
| POST        | `/foods`       | Thêm món mới (cần xác thực)  |
| GET         | `/orders`      | Xem lịch sử đặt hàng         |
| POST        | `/orders`      | Đặt hàng                     |
