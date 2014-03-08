'use strict';

var fs     = require('fs');
var opts   = require('opts');
var Iconv  = require('iconv').Iconv;
var csv    = require('csv');
var mongo  = require('mongo');
var moment = require('moment');

opts.parse([
  {
    short: 'f',
    long: 'file',
    description: 'Input CSV file',
    value: true,
    required: true
  }
]);

var file = opts.get('file');
var iconv = new Iconv('SHIFT_JIS', 'UTF-8//TRANSLIT//IGNORE');

console.log('Start.');
csv().from.stream(fs.createReadStream(file).pipe(iconv), {
  columns: true
}).transform(function(record, i, done) {
  var type = record["タイプ"];
  var price;
  switch(type) {
    case "固定報酬制":
      price = record["固定報酬"];
      break;
    case "時給制":
      price = record["時給"];
      break;
    case "コンペ":
      price = record["コンペ"];
      break;
    case "タスク":
      price = record["タスク"];
      break;
    default:
      price = 0;
      break;
  }
  if (price === "NULL") {
    price = 0;
  }
  var user_id;
  if (record["ユーザーID（１人目に契約した人）"] != "NULL") {
    user_id = record["ユーザーID（１人目に契約した人）"];
  }
  var job = new mongo.Job({
    id            : record['id'],
    type          : type,
    category      : record['カテゴリ'],
    price         : price,
    published_at  : moment(record['公開日'], 'YYYY/M/D H:mm').toDate(),
    view_count    : record['閲覧数'],
    proposal_count: record['提案数'],
    user_id       : user_id,
    title         : record['タイトル'],
    text          : record["本文"].replace(/<("[^"]*"|'[^']*'|[^'">])*>/g,'')
  });
  job.save(function(err) {
    if (err) {
      console.log('job.save failed');
    }
    done(err);
  });
}, {
  parallel: 1
}).on('error', function(err) {
  console.log(err);l
  return process.exit(1);
}).on('end', function(cnt) {
  console.log('End.');
  return process.exit(0);
});