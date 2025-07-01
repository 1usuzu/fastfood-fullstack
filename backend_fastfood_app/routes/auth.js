const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const User = require('../models/User');

router.post('/register', async (req, res) => {
  const { name, phone, password } = req.body;

  if (!name || !phone || !password)
    return res.status(400).json({ message: 'Thiếu thông tin.' });

  if (password.length < 6)
    return res.status(400).json({ message: 'Mật khẩu tối thiểu 6 ký tự.' });

  try {
    const exists = await User.findOne({ phone });
    if (exists) return res.status(409).json({ message: 'Số điện thoại đã tồn tại.' });

    const hash = await bcrypt.hash(password, 10);
    const user = new User({ name, phone, password: hash });
    await user.save();
    res.status(201).json({ message: 'Đăng ký thành công!' });
  } catch (err) {
    res.status(500).json({ message: 'Lỗi server', error: err.message });
  }
});

router.post('/login', async (req, res) => {
  const { phone, password } = req.body;

  if (!phone || !password)
    return res.status(400).json({ message: 'Thiếu số điện thoại hoặc mật khẩu.' });

  try {
    const user = await User.findOne({ phone });
    if (!user) return res.status(401).json({ message: 'Không tìm thấy người dùng.' });

    const match = await bcrypt.compare(password, user.password);
    if (!match) return res.status(401).json({ message: 'Sai mật khẩu.' });

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
  } catch (err) {
    res.status(500).json({ message: 'Lỗi server', error: err.message });
  }
});

module.exports = router;
