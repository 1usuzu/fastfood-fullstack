const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const User = sequelize.define('User', {
  name: {
    type: DataTypes.STRING,
    allowNull: false
  },
  phone: {
    type: DataTypes.STRING,
    allowNull: false,
    unique: true
  },
  password: {
    type: DataTypes.STRING,
    allowNull: false
  },  
  email: {
    type: DataTypes.STRING
  },
  address: {
    type: DataTypes.STRING
  },
  // **DÒNG THÊM MỚI**
  date: {
    type: DataTypes.STRING 
  }
  
}, {
  tableName: 'users',
  timestamps: true
});

module.exports = User;