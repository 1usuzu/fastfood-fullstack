const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Order = sequelize.define('Order', {
  userId: { type: DataTypes.INTEGER, allowNull: true },
  customerName: { type: DataTypes.STRING, allowNull: false },
  customerAddress: { type: DataTypes.STRING, allowNull: false },
  customerPhone: { type: DataTypes.STRING, allowNull: false },
  totalPrice: { type: DataTypes.DOUBLE, allowNull: false },
  paymentMethod: {
    type: DataTypes.STRING,
    allowNull: false,
    defaultValue: 'Tiền mặt'
  },
  status: { type: DataTypes.STRING, defaultValue: 'Đã đặt' }
}, { tableName: 'orders' });

module.exports = Order;