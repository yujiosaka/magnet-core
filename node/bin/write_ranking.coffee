'use strict'

mongo  = require 'mongo'
async  = require 'async'
moment = require 'moment'
opts   = require 'opts'

opts.parse [
  short: 'c',
  long: 'count',
  description: 'Count',
  value: true
]

from  = moment('2013/01/01', 'YYYY/MM/DD').toDate()
count = parseInt opts.get('count') || 10
is_skill = true

console.log "category,rank,key_phrase,total_score,start_at"
async.eachSeries ['開発', 'デザイン', 'ライティング', '事務'], (category, done) ->
  async.timesSeries 14, (n, done) ->
    start_at = moment(from).add('month', n).toDate()
    mongo.KeyPhraseSummary.find {start_at,is_skill,category}
    .sort total_score : -1
    .limit count
    .exec (err, summaries) ->
      if err
        console.log err
        return done err
      for s,i in summaries
        console.log "#{category},#{i+1},#{s.key_phrase},#{s.total_score},#{moment(start_at).format('YYYY-MM-DD')}"
      done null
  , done
, (err) ->
  process.exit 1 if err
  process.exit 0
