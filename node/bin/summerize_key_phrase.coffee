'use strict'

mongo  = require 'mongo'
async  = require 'async'
moment = require 'moment'

start_at = moment('2013/01/01', 'YYYY/MM/DD').toDate()

console.log 'Start.'
async.times 14, (n, done) ->
  from = moment(start_at).add('month', n).startOf('month').toDate()
  to   = moment(start_at).add('month', n).endOf('month').toDate()
  mongo.KeyPhrase.aggregate [
    $match       :
      published_at :
        $gte         : from
        $lt          : to
  ,
    $group       :
      _id          :
        category     : '$category'
        key_phrase   : '$key_phrase'
      total_count  :
        $sum         : 1
      total_score  :
        $sum         : '$score'
  ], (err, summaries) ->
    if err
      console.log err
      return done err
    for summary in summaries
      summary.category = summary._id.category
      summary.key_phrase = summary._id.key_phrase
      delete summary._id
    async.each summaries, (summary, done) ->
      key_phrase_summary = mongo.KeyPhraseSummary summary
      key_phrase_summary.save (err) ->
        if err
          console.log err
          return done err
        done null
    , (err) ->
      return done err if err
      console.log "Saved #{summaries.length} key_phrase_summaries from #{from} to #{to}."
      done null
, (err) ->
  process.exit 1 if err
  console.log 'End.'
  process.exit 0
