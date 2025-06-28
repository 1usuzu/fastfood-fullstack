const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

// Sử dụng dotenv để quản lý biến môi trường
require('dotenv').config();

const app = express();
app.use(cors());
app.use(express.json());

const PORT = 3000;

// ======================= PHẦN GỠ LỖI =======================
console.log("-----------------------------------------");
console.log("BẮT ĐẦU KIỂM TRA KẾT NỐI DATABASE...");

const dbUri = process.env.MONGODB_URI; // Lấy URI từ file .env

// KIỂM TRA 1: Server có đọc được file .env không?
if (!dbUri) {
  console.error('LỖI: Không tìm thấy biến MONGODB_URI. Hãy kiểm tra lại file .env của bạn.');
  process.exit(1);
}

// KIỂM TRA 2: Xem chính xác chuỗi kết nối đang được sử dụng là gì.
console.log('Server đang cố gắng kết nối tới URI:', dbUri);
console.log("-----------------------------------------");
// ==========================================================


// Kết nối tới MongoDB
mongoose.connect(dbUri)
  .then(() => console.log('>>> Đã kết nối thành công tới MongoDB! <<<'))
  .catch(err => console.error('Lỗi kết nối MongoDB:', err));

// Định nghĩa Schema (cấu trúc) cho món ăn trong MongoDB
const FoodSchema = new mongoose.Schema({
  name: {
    type: String,
    required: [true, 'Tên món ăn là bắt buộc'],
    trim: true
  },
  price: {
    type: Number,
    required: [true, 'Giá món ăn là bắt buộc'],
    min: [0, 'Giá không thể âm']
  },
  imageUrl: {
    type: String,
    trim: true,
    default: ''
  }
}, {
  timestamps: true
});

// Tạo Model từ Schema.
const Food = mongoose.model('Food', FoodSchema);

// GET: Lấy danh sách tất cả món ăn
app.get('/foods', async (req, res) => {
  try {
    console.log('Nhận được yêu cầu GET /foods. Bắt đầu tìm kiếm trong database...');
    const foods = await Food.find(); // Tìm tất cả các document trong collection 'foods'
    
    // KIỂM TRA 3: Xem đã tìm thấy bao nhiêu món ăn
    console.log(`Đã tìm thấy ${foods.length} món ăn. Đang gửi về cho client...`);

    res.json(foods); // Trả về danh sách món ăn dưới dạng JSON
  } catch (error) {
    console.error('Lỗi khi lấy danh sách món ăn:', error);
    res.status(500).json({ message: 'Lỗi máy chủ khi lấy danh sách món ăn', error: error.message });
  }
});


// POST: Thêm một món ăn mới
app.post('/foods', async (req, res) => {
  try {
    if (!req.body.name || req.body.price == null) {
      return res.status(400).json({ message: 'Tên và giá món ăn là bắt buộc.' });
    }
    if (typeof req.body.price !== 'number' || req.body.price < 0) {
        return res.status(400).json({ message: 'Giá món ăn phải là một số không âm.' });
    }
    const newFood = new Food(req.body);
    await newFood.save();
    res.status(201).json(newFood);
  } catch (error) {
    console.error('Lỗi khi thêm món ăn mới:', error);
    if (error.name === 'ValidationError') {
      return res.status(400).json({ message: 'Dữ liệu không hợp lệ', errors: error.errors });
    }
    res.status(500).json({ message: 'Lỗi máy chủ khi thêm món ăn mới', error: error.message });
  }
});


app.listen(PORT, () => console.log(`Backend đang chạy tại http://localhost:${PORT}`));