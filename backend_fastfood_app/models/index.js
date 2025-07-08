const sequelize = require('../config/database');
const User = require('./user.model');
const Order = require('./order');
const OrderItem = require('./orderItem');

// Thiết lập quan hệ giữa các bảng
User.hasMany(Order, { foreignKey: 'userId' });
Order.belongsTo(User, { foreignKey: 'userId' });

Order.hasMany(OrderItem, { foreignKey: 'orderId' });
OrderItem.belongsTo(Order, { foreignKey: 'orderId' });

// Đồng bộ CSDL
sequelize.sync()
  .then(() => console.log("✅ Đã kết nối & đồng bộ cơ sở dữ liệu"))
  .catch((err) => console.error("❌ Lỗi kết nối DB:", err));

const db = {
  sequelize,
  User,
  Order,
  OrderItem
};

module.exports = db;
