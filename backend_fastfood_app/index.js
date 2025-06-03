const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

mongoose.connect('mongodb://localhost:27017/fastfood_app', {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

const FoodSchema = new mongoose.Schema({
  name: String,
  price: Number,
  imageUrl: String,
});

const Food = mongoose.model('Food', FoodSchema);

// GET: Lấy danh sách món ăn
app.get('/foods', async (req, res) => {
  const foods = await Food.find();
  res.json(foods);
});

// POST: Thêm món ăn
app.post('/foods', async (req, res) => {
  const newFood = new Food(req.body);
  await newFood.save();
  res.json(newFood);
});

app.listen(3000, () => console.log('Backend running at http://localhost:3000'));
