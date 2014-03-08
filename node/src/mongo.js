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
  type          : { type: String, required: true, enum: ['固定報酬制', '時給制', 'コンペ', 'タスク'] }, // タイプ
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

var skill_summary = new mongoose.Schema({
  skill      : { type: String, required: true }, // スキル名
  count      : { type: Number, default: 0 }, // 出現回数
  total_level: { type: Number, default: 0 }, // レベル合計
  total_years: { type: Number, default: 0 }, // 年数合計
  start_at   : { type: Date, required: true },
  end_at     : { type: Date, required: true }
});

var key_phrase = new mongoose.Schema({
  id              : { type: Number, required: true }, // 仕事情報ID
  category        : { type: String, required: true, enum: ['開発', 'デザイン', 'ライティング', '事務'] }, // カテゴリ
  key_phrase      : { type: String, required: true }, // キーフレーズ
  score           : { type: Number, required: true }, // キーフレーズの重要度
  published_at    : { type: Date }, // 公開日
  published_at_str: { type: String, required: true } // 公開日の文字列
});

var key_phrase_summary = new mongoose.Schema({
  category   : { type: String, required: true, enum: ['開発', 'デザイン', 'ライティング', '事務'] }, // カテゴリ
  key_phrase : { type: String, required: true }, // キーフレーズ
  total_score: { type: Number, required: true }, // キーフレーズの重要度合計
  total_count: { type: Number, required: true }, // 出現回数
  book_info  : {}, // 本の情報なんでも入れれる
  start_at   : { type: Date, required: true },
  end_at     : { type: Date, required: true }
});

exports.UserSkill = db.model('user_skill', user_skill);
exports.Job = db.model('job', job);
exports.SkillSummary = db.model('skill_summary', skill_summary);
exports.KeyPhrase = db.model('key_phrase', key_phrase);
exports.KeyPhraseSummary = db.model('key_phrase_summary', key_phrase_summary);
