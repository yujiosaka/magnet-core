<?php
	
/**
 * Yahoo! JAPAN Web APIのご利用には、アプリケーションIDの登録が必要です。
 * あなたが登録したアプリケーションIDを $appid に設定してお使いください。
 * アプリケーションIDの登録URLは、こちらです↓
 * http://e.developer.yahoo.co.jp/webservices/register_application
 */
$appid = 'dj0zaiZpPXlvM0MyNzd2b0phMyZzPWNvbnN1bWVyc2VjcmV0Jng9ODg-'; // <-- ここにあなたのアプリケーションIDを設定してください。
$sentence = "今日はとても良い天気です";
echo "$sentence\n";

function escapestring($str) {
    return htmlspecialchars($str, ENT_QUOTES);
}

//if(isset($_REQUEST['sentence'])){
//  $sentence = mb_convert_encoding($_REQUEST['sentence'], 'utf-8', 'auto');
// }else{
//  $sentence = "";
//}

function show_keyphrase($appid, $sentence){
  $output = "xml";
  $request  = "http://jlp.yahooapis.jp/KeyphraseService/V1/extract?";
  $request .= "appid=".$appid."&sentence=".urlencode($sentence)."&output=".$output;
  echo "$request\n";
  
  $responsexml = simplexml_load_file($request);
  
  $result_num = count($responsexml->Result);

  if($result_num > 0){
    //echo "<table>";
    //echo "<tr><td><b>キーフレーズ</b></td><td><b>スコア</b></td></tr>";

    for($i = 0; $i < $result_num; $i++){
      $result = $responsexml->Result[$i];
      //echo "<tr><td>".escapestring($result->Keyphrase)."</td><td>".escapestring($result->Score)."</td></tr>";
      echo escapestring($result->Keyphrase).",".escapestring($result->Score)."\n";
    }
    //echo "</table>";
  }
}

show_keyphrase($appid, $sentence);

?>