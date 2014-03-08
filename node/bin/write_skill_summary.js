'use strict';

var mongo  = require('mongo');

mongo.SkillSummary.find().sort({count:-1}).exec(function(err, summaries) {
  if (err) {
    console.log('SkillSummary.find failed', err);
    process.exit(1);
  }
  console.log("rank,skill,count,total_level,total_years");
  for (var i = 0; i < summaries.length; ++i) {
    var s = summaries[i];
    console.log("" + (i+1)  + "," + s.skill + "," + s.count + "," + s.total_level + "," + s.total_years);
  }
  process.exit(0);
});