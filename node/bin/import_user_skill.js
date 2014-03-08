'use strict';

var fs    = require('fs');
var opts  = require('opts');
var Iconv = require('iconv').Iconv;
var csv   = require('csv');
var mongo = require('mongo');

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
  var user_skill = new mongo.UserSkill({
    user_id : record['ユーザーID'],
    level   : record['レベル　※ 1が低く、5が高い（自己申告）'],
    years   : record['年数　※5年以上は、5を選択'],
    skill   : record['name']
  });
  user_skill.save(function(err) {
    if (err) {
      console.log('user_sill.save failed');
    }
    done(err);
  });
}, {
  parallel: 1
}).on('error', function(err) {
  return process.exit(1);
}).on('end', function(cnt) {
  console.log('End.');
  return process.exit(0);
});