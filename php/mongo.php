<?php

$m = new Mongo("mongodb://user:testtest@tempest.mongohq.com:10045/magnet-large");
$db = $m->selectDB('magnet-large');
$col = $db->selectCollection("user_skills");
$cur = $col->findOne();

var_dump($cur);

?>
