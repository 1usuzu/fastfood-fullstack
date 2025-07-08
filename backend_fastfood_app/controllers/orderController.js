const Order = require('../models/order');
const OrderItem = require('../models/orderItem');
const sequelize = require('../config/database');

exports.createOrder = async (req, res) => {
  const t = await sequelize.transaction();
  try {
    const { customerInfo, items, totalPrice } = req.body;

    // Kiểm tra dữ liệu
    if (!customerInfo || !items || !totalPrice || items.length === 0 || !customerInfo.userId) {
      return res.status(400).json({ message: 'Dữ liệu đơn hàng không hợp lệ.' });
    }

    // Tạo đơn hàng
    const newOrder = await Order.create({
      customerName: customerInfo.name,
      customerAddress: customerInfo.address,
      customerPhone: customerInfo.phone,
      totalPrice: totalPrice,
      userId: customerInfo.userId  // ✅ thêm userId
    }, { transaction: t });

    // Tạo từng món hàng
    for (const item of items) {
      await OrderItem.create({
        orderId: newOrder.id,
        foodName: item.name,
        quantity: item.quantity,
        price: item.price
      }, { transaction: t });
    }

    await t.commit();
    res.status(201).json({ message: 'Đặt hàng thành công!', orderId: newOrder.id });

  } catch (error) {
    await t.rollback();
    console.error('Lỗi khi xử lý đơn hàng:', error);
    res.status(500).json({ message: 'Lỗi máy chủ khi xử lý đơn hàng' });
  }
};
