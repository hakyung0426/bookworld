<?php


class DB_BookbugFun {
    
    private $conn;
    
    //생성자
    function __construct() {  
        require_once 'DB_Connect.php';
        $db = new DB_Connect();
        $this->conn = $db ->connect();
    }
    
    
    public function getUserById($id) {
        
    }
    
    
}




?>