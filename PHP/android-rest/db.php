<?php
include './config.php';

class DB {
  private static $instance;
  private $pdo;

  public static function getInstance(): DB{
    if (null === static::$instance) {
      static::$instance = new static();
    }
    return static::$instance;
  }

  public function __construct() {
    $this->pdo = new PDO("mysql:host=localhost;dbname=myDB;charset=utf8", DBUser, DBPassword);
  }

  public function __destruct(){
    $this->pdo = null;
  }

  public function getRandomItems() {
    $res = $this->pdo->query("SELECT * FROM dictionary WHERE IFNULL((SELECT id FROM archive WHERE archive.dictionary_id = dictionary.id),0) = 0 ORDER BY RAND() LIMIT 1")->fetch(PDO::FETCH_ASSOC);
    return $res;
  }

  public function getRandomSet($masterId, $setSize) {
    if (ctype_digit($masterId) && ctype_digit($setSize)) {
        $res = $this->pdo->prepare("SELECT * FROM dictionary WHERE id != $masterId ORDER BY RAND() LIMIT $setSize");
        $res->execute();
        return $res->fetchAll(PDO::FETCH_ASSOC);
    }
    return array(
       'error' => 'bad argument'
    );
  }

  public function testIsAvailable() {
    $res = $this->pdo->query("SELECT * FROM dictionary WHERE IFNULL((SELECT id FROM archive WHERE archive.dictionary_id = dictionary.id),0) = 0 LIMIT 1")->fetch(PDO::FETCH_ASSOC);
// var_dump($res);
    return array(
       'testIsAvailable' => $res == false ? "false":"true"
    );
  }

  public function dictionaryIsAvailable() {
    $res = $this->pdo->query("SELECT * FROM dictionary LIMIT 1")->fetch(PDO::FETCH_ASSOC);
    return array(
       'dictionaryIsAvailable' => $res == false ? "false":"true"
    );
  }

  public function moveToArchive($dictionaryId){
    $res = $this->pdo->prepare("INSERT INTO archive (dictionary_id) VALUES (:dictionary_id)");
    $res->bindParam(':dictionary_id', $dictionaryId, PDO::PARAM_INT);
    $res->execute();
    return array(
       'op' => "true"
    );
  }

  public function truncateArchive() {
    $res = $this->pdo->query("TRUNCATE archive")->fetch(PDO::FETCH_ASSOC);
    return array(
       'op' => "true"
    );
  }

  public function addDictionaryItems($ru, $en){
    $res = $this->pdo->prepare("INSERT INTO dictionary (ru, en) VALUES (:ru, :en)");
    $res->bindParam(':ru', $ru, PDO::PARAM_STR);
    $res->bindParam(':en', $en, PDO::PARAM_STR);
    $res->execute();
    return array(
       'op' => "true"
    );
  }
}

 ?>
