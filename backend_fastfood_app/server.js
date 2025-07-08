const express = require('express');
const cors = require('cors');
require('dotenv').config();

const app = express();
const db = require('./models'); // models/index.js sẽ tự sync và load model liên quan

// Import routes
const authRoutes = require('./routes/auth.routes');
const orderRoutes = require('./routes/orderRoutes');
// const foodRoutes = require('./routes/foodRoutes'); // nếu có

// Middleware
app.use(cors());
app.use(express.json());

// Use routes
app.use('/', authRoutes);
app.use('/', orderRoutes);
// app.use('/', foodRoutes);

const PORT = process.env.PORT || 3000;

// Kết nối DB & khởi động server
db.sequelize.sync().then(() => {
  console.log("✅ Kết nối CSDL thành công");
  app.listen(PORT, () => console.log(`🚀 Server đang chạy tại http://localhost:${PORT}`));
}).catch(err => {
  console.error("❌ Lỗi kết nối CSDL:", err.message);
});
