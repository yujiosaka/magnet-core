'use strict'

mongo  = require 'mongo'
async  = require 'async'
moment = require 'moment'

start_at = moment('2013/01/01', 'YYYY/MM/DD').toDate()

console.log 'Start.'
async.times 14, (n, done) ->
  from = moment(start_at).add('month', n).startOf('month').toDate()
  to   = moment(start_at).add('month', n).endOf('month').toDate()
  mongo.KeyPhraseSummary.distinct 'key_phrase',
    start_at :
      $gte     : from
      $lt      : to
  , (err, key_phrases) ->
    if err
      console.log err
      return done err
    mongo.SkillSummary.distinct 'skill',
      skill :
        $in   : key_phrases
    , (err, skills) ->
      if err
        console.log err
        return done err
      mongo.KeyPhraseSummary.update
        key_phrase :
          $in        : skills
      ,
        is_skill     : true
      ,
        multi: true
      , (err, cnt) ->
        if err
          console.log err
          return done err
        console.log "Updated #{cnt} key_phrase_summaries from #{from} to #{to}"
        done null
, (err) ->
  process.exit 1 if err
  console.log 'End.'
  process.exit 0
