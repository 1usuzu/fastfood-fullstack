const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

// Kết nối tới MongoDB
mongoose.connect('mongodb://localhost:27017/fastfood_app_db')
.then(() => console.log('Đã kết nối thành công tới MongoDB!'))
.catch(err => console.error('Lỗi kết nối MongoDB:', err));

// Định nghĩa Schema (cấu trúc) cho món ăn trong MongoDB
const FoodSchema = new mongoose.Schema({
name: {
    type: String,
    required: [true, 'Tên món ăn là bắt buộc'], // Thêm validation: tên là bắt buộc
    trim: true // Loại bỏ khoảng trắng thừa ở đầu và cuối
  },
  price: {
    type: Number,
    required: [true, 'Giá món ăn là bắt buộc'], // Thêm validation: giá là bắt buộc
    min: [0, 'Giá không thể âm'] // Thêm validation: giá không được âm
  },
  imageUrl: {
    type: String,
    trim: true,
    default: '' // Có thể đặt một URL ảnh mặc định nếu muốn
  }
}, {
  timestamps: true // Tự động thêm trường createdAt và updatedAt
});

// Tạo Model từ Schema. Model này sẽ dùng để tương tác với collection 'foods' trong database.
const Food = mongoose.model('Food', FoodSchema);

// API Routes (Các đường dẫn để client tương tác)

// GET: Lấy danh sách tất cả món ăn
app.get('/foods', async (req, res) => {
  try {
    const foods = await Food.find(); // Tìm tất cả các document trong collection 'foods'
    res.json(foods); // Trả về danh sách món ăn dưới dạng JSON
  } catch (error) {
    console.error('Lỗi khi lấy danh sách món ăn:', error);
    res.status(500).json({ message: 'Lỗi máy chủ khi lấy danh sách món ăn', error: error.message });
  }
});

// POST: Thêm một món ăn mới
app.post('/foods', async (req, res) => {
  // Dữ liệu món ăn mới sẽ nằm trong req.body (ví dụ: { "name": "Gà rán", "price": 30000, "imageUrl": "..." })
  try {
    // Kiểm tra dữ liệu đầu vào cơ bản
    if (!req.body.name || req.body.price == null) {
      return res.status(400).json({ message: 'Tên và giá món ăn là bắt buộc.' });
    }
    if (typeof req.body.price !== 'number' || req.body.price < 0) {
        return res.status(400).json({ message: 'Giá món ăn phải là một số không âm.' });
    }

    const newFood = new Food({
      name: req.body.name,
      price: req.body.price,
      imageUrl: req.body.imageUrl
    });

    await newFood.save(); // Lưu món ăn mới vào database
    res.status(201).json(newFood); // Trả về món ăn vừa tạo với mã trạng thái 201 (Created)
  } catch (error) {
    console.error('Lỗi khi thêm món ăn mới:', error);
    // Nếu lỗi là do validation của Mongoose
    if (error.name === 'ValidationError') {
      return res.status(400).json({ message: 'Dữ liệu không hợp lệ', errors: error.errors });
    }
    res.status(500).json({ message: 'Lỗi máy chủ khi thêm món ăn mới', error: error.message });
  }
});

const PORT = 3000; // Cổng mà server sẽ lắng nghe
app.listen(PORT, () => console.log(`Backend đang chạy tại http://localhost:${PORT}`));