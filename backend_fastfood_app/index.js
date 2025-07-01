const express = require('express');
const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const cors = require('cors');
require('dotenv').config();

// Import User model
const User = require('./models/User');

const app = express();

// Middleware
app.use(cors({ origin: '*' })); // Cho phép tất cả origin, thay bằng domain cụ thể trong production
app.use(express.json());

// Kết nối MongoDB
const dbUri = process.env.MONGODB_URI;
if (!dbUri) {
  console.error('Vui lòng đặt biến môi trường MONGODB_URI với URI kết nối MongoDB của bạn.');
  process.exit(1);
}

mongoose.connect(dbUri, {
  useNewUrlParser: true,
  useUnifiedTopology: true
})
.then(() => console.log('Đã kết nối thành công tới MongoDB!'))
.catch(err => console.error('Lỗi kết nối MongoDB:', err));

// Schema cho món ăn
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

const Food = mongoose.model('Food', FoodSchema);

// API Routes
// Lấy danh sách tất cả món ăn
app.get('/foods', async (req, res) => {
  try {
    const foods = await Food.find();
    res.json(foods);
  } catch (error) {
    console.error('Lỗi khi lấy danh sách món ăn:', error);
    res.status(500).json({ message: 'Lỗi máy chủ khi lấy danh sách món ăn', error: error.message });
  }
});

// Thêm món ăn mới
app.post('/foods', async (req, res) => {
  try {
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

// Đăng ký người dùng
app.post('/register', async (req, res) => {
  const { name, phone, password } = req.body;

  console.log('📦 Dữ liệu nhận được:', req.body);

  // Validation cơ bản
  if (!name || !phone || !password) {
    return res.status(400).json({ message: 'Vui lòng nhập đầy đủ thông tin.' });
  }

  // Validation nâng cao
  if (password.length < 6) {
    return res.status(400).json({ message: 'Mật khẩu phải có ít nhất 6 ký tự.' });
  }

  const phoneRegex = /^(0|\+84)[0-9]{9,10}$/;
  if (!phoneRegex.test(phone)) {
    return res.status(400).json({ message: 'Số điện thoại không hợp lệ.' });
  }

  try {
    const existingUser = await User.findOne({ phone });
    if (existingUser) {
      return res.status(409).json({ message: 'Số điện thoại đã được sử dụng.' });
    }

    const saltRounds = 10;
    const hashedPassword = await bcrypt.hash(password, saltRounds);

    const newUser = new User({
      name: name.trim(),
      phone,
      password: hashedPassword
    });

    await newUser.save();
    res.status(201).json({ message: 'Đăng ký thành công!' });
  } catch (error) {
    console.error('❌ Lỗi khi đăng ký:', error);
    res.status(500).json({ message: 'Lỗi khi đăng ký.', error: error.message });
  }
});

// Đăng nhập người dùng
app.post('/login', async (req, res) => {
  const { phone, password } = req.body;

  // Validation cơ bản
  if (!phone || !password) {
    return res.status(400).json({ message: 'Vui lòng nhập số điện thoại và mật khẩu.' });
  }

  try {
    const user = await User.findOne({ phone });
    if (!user) {
      return res.status(401).json({ message: 'Số điện thoại không tồn tại.' });
    }

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) {
      return res.status(401).json({ message: 'Mật khẩu không đúng.' });
    }

    // Tạo JWT token
    const token = jwt.sign(
      { id: user._id, phone: user.phone },
      process.env.JWT_SECRET || 'your_jwt_secret',
      { expiresIn: '1h' }
    );

    res.status(200).json({
      message: 'Đăng nhập thành công!',
      token,
      user: { id: user._id, name: user.name, phone: user.phone }
    });
  } catch (error) {
    console.error('❌ Lỗi khi đăng nhập:', error);
    res.status(500).json({ message: 'Lỗi máy chủ khi đăng nhập.', error: error.message });
  }
});

// Khởi động server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Backend đang chạy tại http://localhost:${PORT}`));
