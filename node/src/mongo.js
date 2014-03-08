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
  id   　　　: { type: Number, required: true }, // 連番
  user_id   : { type: Number, required: true }, // ユーザーID
  level     : { type: Number, required: true }, // レベル　※ 1が低く、5が高い（自己申告）
  years     : { type: Number, required: true }, // 年数　※5年以上は、5を選択
  skill     : { type: String, required: true }, // スキル名
  created_at: { type: Date, default: Date.now },
  updated_at: { type: Date, default: Date.now }
});

var job = new mongoose.Schema({
  id   　　    　: { type: Number, required: true }, // 連番
  category      : { type: String, required: true, enum: ['開発', 'デザイン', 'ライティング', '事務'] }, // カテゴリ
  type          : { type: String, required: true, enum: ['固定報酬制', '時給制', 'コンペ'] }, // タイプ
  price         : { type: Number, required: true }, // 固定報酬、時給、コンペまたはタスク
  view_count    : { type: Number, required: true }, // 閲覧数
  proposal_count: { type: Number, required: true }, // 提案数
  user_id       : { type: Number }, // ユーザーID（１人目に契約した人）
  title         : { type: String, required: true }, // タイトル
  text          : { type: String, required: true }, // 本文
  published_at  : { type: Date, required: true }, // 公開日
  created_at    : { type: Date, default: Date.now },
  updated_at    : { type: Date, default: Date.now }
});

exports.UserSkill = db.model('user_skill', user_skill);
exports.Job = db.model('job', job);
