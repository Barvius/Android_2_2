<?php
include 'db.php';
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: GET, POST');


if (isset($_GET['getRandomItems'])) {
  echo json_encode(DB::getInstance()->getRandomItems(),JSON_NUMERIC_CHECK);
  return;
}

if (isset($_GET['getRandomSet']) && isset($_GET['masterId']) && isset($_GET['setSize'])) {
  echo json_encode(DB::getInstance()->getRandomSet($_GET['masterId'],$_GET['setSize']),JSON_NUMERIC_CHECK);
  return;
}

if (isset($_GET['testIsAvailable'])) {
  echo json_encode(DB::getInstance()->testIsAvailable(),JSON_NUMERIC_CHECK);
  return;
}

if (isset($_GET['dictionaryIsAvailable'])) {
  echo json_encode(DB::getInstance()->dictionaryIsAvailable(),JSON_NUMERIC_CHECK);
  return;
}

if (isset($_GET['moveToArchive']) && isset($_GET['dictionaryId'])) {
  echo json_encode(DB::getInstance()->moveToArchive($_GET['dictionaryId']),JSON_NUMERIC_CHECK);
  return;
}

if (isset($_GET['truncateArchive'])) {
  echo json_encode(DB::getInstance()->truncateArchive(),JSON_NUMERIC_CHECK);
  return;
}

if (isset($_GET['addDictionaryItems']) ) {
  if (isset($_POST['ru']) && isset($_POST['en'])){
    echo json_encode(DB::getInstance()->addDictionaryItems($_POST['ru'],$_POST['en']),JSON_NUMERIC_CHECK);
  }
  return;
}

 ?>
