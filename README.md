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

## Cấu trúc thư mục

```
fastfood-fullstack/
├── android_fastfood_app/                 # Mã nguồn ứng dụng Android (Java)
│   ├── activity/                         # Các màn hình (Activity) chính
│   ├── adapter/                          # Các lớp Adapter để hiển thị danh sách (RecyclerView Adapter)
│   ├── fragment/                         # Các Fragment như trang chủ, giỏ hàng, chi tiết món ăn
│   ├── data/                             # Dữ liệu và kết nối API
│   │   ├── api/                          # Retrofit client và các interface để gọi API từ backend
│   │   ├── local/                        # Lưu trữ dữ liệu cục bộ (SharedPreferences, SessionManager)
│   │   ├── model/                        # Các lớp model biểu diễn dữ liệu như User, Food, Order
├── backend_fastfood_app/                 # Máy chủ Node.js Express
│   ├── config/                           # Cấu hình kết nối cơ sở dữ liệu (Sequelize)
│   ├── controllers/                      # Logic xử lý các API
│   ├── models/                           # Các mô hình dữ liệu Sequelize
│   ├── routes/                           # Định nghĩa route API
│   ├── .env                              # Biến môi trường (tự tạo)
│   └── index.js                          # File chính chạy server
```

---

## Cài đặt và chạy dự án

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

> Server chạy ở `http://localhost:3000`

---

### 3. Cài đặt Frontend (Android)

> Cần cài Android Studio

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

## Các API chính

| Phương thức | Endpoint                                | Chức năng                                      |
|-------------|-----------------------------------------|------------------------------------------------|
| POST        | `/register`                             | Đăng ký tài khoản mới                          |
| POST        | `/login`                                | Đăng nhập, nhận token JWT                      |
| GET         | `/foods`                                | Lấy danh sách món ăn                           |
| POST        | `/foods`                                | Thêm món mới (cần xác thực)                    |
| POST        | `/orders`                               | Đặt hàng                                       |
| GET         | `/orders/history/:userId`               | Xem lịch sử đặt hàng                           |
| GET         | `/api/users/:id`                        | Lấy thông tin chi tiết của một người dùng      |
| PUT         | `/api/users/:id`                        | Cập nhật thông tin của một người dùng          |
| POST        | `/api/users/change-password`            | Đổi mật khẩu khi đã đăng nhập                  |
| GET         | `/api/payment-account/:userPhone`       | Lấy danh sách thẻ thanh toán                   |
| POST        | `/api/payment-account/add`              | Thêm một thẻ thanh toán mới                    |
| DELETE      | `/api/payment-account/delete/:id`       | Xóa một thẻ thanh toán                         |
| POST        | `/api/payment/create_payment_url`       | Tạo URL để thanh toán qua cổng VNPay           |
| GET         | `/api/payment/vnpay_return`             | Nhận kết quả trả về từ cổng VNPay              |
| POST        | `/api/support-request`                  | Gửi yêu cầu hỗ trợ từ khách hàng               |

