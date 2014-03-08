'use strict';

var fs     = require('fs');
var opts   = require('opts');
var Iconv  = require('iconv').Iconv;
var csv    = require('csv');
var mongo  = require('mongo');
var _      = require('underscore');
var async  = require('async');
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
}).to.array(function(records){
  var skill_obj = {};
  for (var i = 0; i < records.length; i++) {
    var record = records[i];
    var skill = record['name'];
    var level = parseInt(record['レベル　※ 1が低く、5が高い（自己申告）']);
    var years = parseInt(record['年数　※5年以上は、5を選択']);
    if (skill_obj[skill]) {
      skill_obj[skill].count += 1;
      skill_obj[skill].total_level += level;
      skill_obj[skill].total_years += years;
    } else {
      skill_obj[skill] = {
        skill: skill,
        count: 1,
        total_level: level,
        total_years: years
      };
    }
  }
  var skills = _.values(skill_obj);
  async.each(skills, function(skill, done) {
    var skill_summary = new mongo.SkillSummary(skill);
    skill_summary.start_at = moment('2013/03/08', 'YYYY/MM/DD').toDate();
    skill_summary.end_at = moment('2014/03/08', 'YYYY/MM/DD').toDate();
    skill_summary.save(function(err) {
      if (err) {
        console.log('user_sill.save failed');
      }
      done(err);
    });
  }, function(err) {
    if (err) {
      console.log(err);
      process.exit(1);
    } else {
      console.log('End.')
      process.exit(0);
    }
  });
});