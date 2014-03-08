'use strict';

mongo  = require 'mongo'
async  = require 'async'
moment = require 'moment'

from = moment('2013/01/01', 'YYYY/MM/DD').toDate()

random = (n) ->
  a = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("")
  s = ""
  for i in [1..n]
    s += a[Math.floor(Math.random() * a.length)]
  s

console.log 'Start.'
async.times 14, (n, done) ->
  start_at = moment(from).add('month', n).startOf('month').toDate()
  end_at   = moment(from).add('month', n).endOf('month').toDate()
  async.times 10000, (m, done) ->
    published_at = moment('2013/01/01', 'YYYY/MM/DD').add('days', Math.floor(Math.random() * 424)).toDate()
    key_phrase = new mongo.KeyPhrase
      id              : n * 10000 + m
      key_phrase      : random(3)
      category        : ['開発','デザイン','ライティング','事務'][Math.floor(Math.random() * 4)]
      score           : Math.floor(Math.random() * 100)
      published_at    : moment('2013/01/01', 'YYYY/MM/DD').add('days', Math.floor(Math.random() * 424)).toDate()
    key_phrase.save (err) ->
      if err
        console.log "kwy_phrase.save failed", err
        return done err
      done null
  , (err) ->
    return done err if err
    console.log "Done from #{start_at} to #{end_at}"
    done null
, (err) ->
  process.exit 1 if err
  console.log 'End.'
  process.exit 0