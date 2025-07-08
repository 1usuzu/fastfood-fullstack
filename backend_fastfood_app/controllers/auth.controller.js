const bcrypt = require('bcryptjs');
const User = require('../models/user.model');

// Đăng ký
exports.register = async (req, res) => {
  const { phone, password } = req.body;
  try {
    const existingUser = await User.findOne({ where: { phone } });
    if (existingUser) return res.status(400).json({ message: "SĐT đã tồn tại" });

    const hash = await bcrypt.hash(password, 10);
    const newUser = await User.create({ phone, password: hash });
    res.json({ message: "Đăng ký thành công", user: { phone: newUser.phone } });
  } catch (err) {
    res.status(500).json({ message: "Lỗi server", error: err.message });
  }
};

// Đăng nhập
exports.login = async (req, res) => {
  const { phone, password } = req.body;
  try {
    const user = await User.findOne({ where: { phone } });
    if (!user) return res.status(400).json({ message: "SĐT không tồn tại" });

    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) return res.status(401).json({ message: "Sai mật khẩu" });

    res.json({ message: "Đăng nhập thành công", user: { phone: user.phone } });
  } catch (err) {
    res.status(500).json({ message: "Lỗi server", error: err.message });
  }
};

// Gửi OTP
exports.sendOtp = async (req, res) => {
  const { phone } = req.body;
  try {
    const user = await User.findOne({ where: { phone } });
    if (!user) return res.status(400).json({ message: "SĐT chưa đăng ký" });

    const otp = Math.floor(100000 + Math.random() * 900000).toString();
    user.otp = otp;
    await user.save();

    // Gửi OTP thực tế bằng SMS/Email ở đây nếu cần
    console.log("OTP gửi tới:", otp);

    res.json({ message: "Đã gửi OTP" });
  } catch (err) {
    res.status(500).json({ message: "Lỗi server", error: err.message });
  }
};

// Xác minh OTP
exports.verifyOtp = async (req, res) => {
  const { phone, otp } = req.body;
  try {
    const user = await User.findOne({ where: { phone, otp } });
    if (!user) return res.status(400).json({ message: "OTP không hợp lệ" });

    res.json({ message: "OTP chính xác" });
  } catch (err) {
    res.status(500).json({ message: "Lỗi server", error: err.message });
  }
};
// Đặt lại mật khẩu
exports.resetPassword = async (req, res) => {
  const { phone, newPassword } = req.body;
  try {
    const user = await User.findOne({ where: { phone } });
    if (!user) return res.status(400).json({ message: "Không tìm thấy người dùng" });

    const hash = await bcrypt.hash(newPassword, 10);
    user.password = hash;
    user.otp = null;
    await user.save();

    res.json({ message: "Đặt lại mật khẩu thành công" });
  } catch (err) {
    res.status(500).json({ message: "Lỗi server", error: err.message });
  }
};
