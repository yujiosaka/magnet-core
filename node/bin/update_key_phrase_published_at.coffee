'use strict'

mongo  = require 'mongo'
async  = require 'async'
moment = require 'moment'

console.log "Start."
async.times 635, (n, done) ->
  from = 100 * n
  to   = 100 * n + 100
  mongo.KeyPhrase.find
    id   :
      $gte : from
      $lt  : to
  , (err, key_phrases) ->
    if err
      console.log err
      return done err
    async.each key_phrases, (key_phrase, done) ->
      key_phrase.published_at = moment(key_phrase.published_at_str, 'YYYY/MM/DD HH:mm:ss').toDate()
      key_phrase.save (err) ->
        if err
          console.log err
          return done err
        done null
    , done
, (err) ->
  process.exit 1 if err
  console.log "End."
  process.exit 0