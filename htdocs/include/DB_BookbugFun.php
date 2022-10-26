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
        $stmt = $this->conn->prepare("SELECT * FROM user WHERE userid = ?");
        $stmt->bind_param("s", $id);
        
        if($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        }else {
            return NULL;
        }
    }
    
    public function getStateByLfd($date) {
        $stmt = $this->conn->prepare("SELECT TIMESTAMPDIFF(SECOND, ?, now())");
        $stmt -> bind_param("s", $date);
        
        if($stmt->execute()) {
            $date = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $date;
        }else {
            return NULL;
        }
        
        
    }
    
    public function feedById($id) {

        $stmt = $this->conn->prepare("UPDATE user SET fd_num = fd_num-1, date_lfd=now() WHERE userid = ?");
        $stmt->bind_param("s", $id);
        $result = $stmt->execute();
        $stmt->close();
        
        if($result) {          
            return true;       
        }else return false; 
    
    }
}




?>