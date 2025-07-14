Ứng dụng Đặt đồ ăn nhanh (Full-stack)

Frontend: Một ứng dụng Android gốc được phát triển bằng Java.

Backend: Một máy chủ RESTful API được xây dựng bằng Node.js và Express.

Ứng dụng cho phép người dùng đăng ký, đăng nhập, duyệt xem và lọc các món ăn, quản lý giỏ hàng, đặt hàng và theo dõi lịch sử đơn hàng của mình. Toàn bộ dữ liệu được đồng bộ hóa một cách an toàn giữa ứng dụng Android và máy chủ backend.

Tính năng chính
Xác thực người dùng: Đăng ký, đăng nhập, ghi nhớ đăng nhập và chức năng quên mật khẩu. Hệ thống sử dụng JWT (JSON Web Token) để bảo mật.

Quản lý món ăn: Xem danh sách món ăn, lọc theo danh mục, xem chi tiết từng món.

Giỏ hàng thông minh: Thêm món ăn vào giỏ, tùy chỉnh số lượng, thêm ghi chú cho món.

Quy trình đặt hàng: Tính toán tổng hóa đơn, tiến hành đặt hàng và lưu lại lịch sử.

Quản lý tài khoản: Người dùng có thể xem và chỉnh sửa thông tin cá nhân.

Thanh toán: Tích hợp cổng thanh toán VNPay (trong backend).

Công nghệ sử dụng
Backend:

Nền tảng: Node.js

Framework: Express.js

Cơ sở dữ liệu: MySQL

ORM: Sequelize

Xác thực: JSON Web Token (JWT), bcryptjs

Khác: CORS, Dotenv, Moment.js

Android:

Ngôn ngữ: Java

Kiến trúc: REST API Client

Thư viện: Retrofit (để gọi API), SessionManager (để quản lý phiên đăng nhập).

Cài đặt và Chạy dự án
Để chạy toàn bộ dự án, bạn cần cài đặt cả Backend và Frontend.

1. Clone Dự án
Đầu tiên, clone nhánh final của repository về máy:

``git clone -b final https://github.com/1usuzu/fastfood-fullstack.git
cd fastfood-fullstack``

2. Cài đặt Backend (Node.js)
Backend cần được chạy trước để Android có thể kết nối tới.

Di chuyển vào thư mục backend:

Bash

cd backend_fastfood_app
Cài đặt các thư viện cần thiết:

Bash

npm install express sequelize pg pg-hstore bcryptjs jsonwebtoken cors dotenv moment qs
Thiết lập môi trường (.env):
Tạo một file có tên .env trong thư mục backend_fastfood_app và điền các thông tin cần thiết.

Đoạn mã

# Chuỗi kết nối đến cơ sở dữ liệu PostgreSQL của bạn
DATABASE_URL="postgres://USER:PASSWORD@HOST:PORT/DATABASE_NAME"

# Thông tin VNPay (nếu có)
VNP_TMNCODE=...
VNP_HASHSECRET=...
VNP_URL=...
VNP_RETURNURL=...
Chạy máy chủ:

Bash

node index.js
Máy chủ sẽ bắt đầu chạy, thường là ở địa chỉ http://localhost:3000.

3. Cài đặt Frontend (Android)
Mở project bằng Android Studio:
Từ cửa sổ chào mừng của Android Studio, chọn "Open" và tìm đến thư mục android_fastfood_app trong repo bạn vừa clone.

Chỉnh sửa địa chỉ IP của Backend:
Mở file RetrofitClient.java và thay đổi BASE_URL để trỏ đến địa chỉ IP của máy tính đang chạy backend.

Nếu chạy trên máy ảo Android mặc định: IP 10.0.2.2 là alias cho localhost của máy tính.

Nếu chạy trên thiết bị thật: Bạn cần tìm địa chỉ IP trong mạng LAN của máy tính (ví dụ: 192.168.1.5) và đảm bảo điện thoại và máy tính kết nối cùng một mạng Wi-Fi.

Java

// file: app/src/main/java/com/example/fastfood/RetrofitClient.java

private static final String BASE_URL = "http://10.0.2.2:3000/"; // Sửa IP nếu cần
Build và Chạy ứng dụng:
Nhấn nút Run 'app' trong Android Studio để build và cài đặt ứng dụng lên máy ảo hoặc thiết bị thật của bạn.

🏗️ Cấu trúc thư mục
android_fastfood_app/: Chứa mã nguồn của ứng dụng Android.

backend_fastfood_app/: Chứa mã nguồn của máy chủ Node.js.

index.js: File chính khởi động server.

config/: Cấu hình kết nối cơ sở dữ liệu (Sequelize).

controllers/: Chứa logic xử lý cho các API endpoint.

models/: Định nghĩa các model dữ liệu (User, Food, Order...).

routes/: Định nghĩa các route (đường dẫn API).

🔌 API Endpoints chính
POST /register: Đăng ký tài khoản mới.

POST /login: Đăng nhập và nhận về JWT token.

GET /foods: Lấy danh sách tất cả món ăn.

POST /foods: Thêm một món ăn mới (yêu cầu xác thực).
