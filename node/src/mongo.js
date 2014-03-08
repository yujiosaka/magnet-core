'use strict';

var mongoose = require('mongoose');
var config = require('ynconf');

var uri = [
  'mongodb://',
   config.mongodb.user,
   ':',
   config.mongodb.password,
   '@',
   config.mongodb.host,
   ':',
   config.mongodb.port,
   '/',
   config.mongodb.database
].join('');

var db = mongoose.createConnection(uri);

var user_skill = new mongoose.Schema({
  id   　　　: { type: Number, required: true },
  user_id   : { type: Number, required: true },
  level     : { type: Number, required: true },
  years     : { type: Number, required: true },
  skill     : { type: String, required: true },
  created_at: { type: Date, default: Date.now },
  updated_at: { type: Date, default: Date.now }
});

var job = new mongoose.Schema({
  id   　　    　: { type: Number, required: true },
  category      : { type: String, required: true, enum: ['開発', 'デザイン', 'ライティング', '事務'] },
  type          : { type: String, required: true, enum: ['固定報酬制', '時給制', 'コンペ'] },
  price         : { type: Number, required: true },
  task          : { type: Number },
  view_count    : { type: Number, required: true },
  proposal_count: { type: Number, required: true },
  user_id       : { type: Number },
  title         : { type: String, required: true },
  text          : { type: String, required: true },
  published_at  : { type: Date, required: true },
  created_at    : { type: Date, default: Date.now },
  updated_at    : { type: Date, default: Date.now }
});

exports.UserSkill = db.model('user_skill', user_skill);
exports.Job = db.model('job', job);
