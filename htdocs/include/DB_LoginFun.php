<?php


class DB_LoginFun {
    
    private $conn;
    
    
    //생성자
    function __construct() {  
        require_once 'DB_Connect.php';
        $db = new DB_Connect();
        $this->conn = $db ->connect();
    }
    
    public function storeUser($id, $password) {
        $stmt = $this->conn->prepare("INSERT INTO user(userid, password) VALUES(?, ?)");
        $stmt->bind_param("ss", $id, $password);
        $result = $stmt->execute();
        $stmt->close();
        
        //successful stroe인지 체크
        if($result) {          
            return true;        //store 성공
        }else return false;         //store 실패
        
    }
    
    public function isUserExisted($id) {
        $stmt = $this->conn->prepare("SELECT userid from user WHERE userid = ?");
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $stmt->store_result();
        
        if($stmt->num_rows >0) { //user 존재
            $stmt->close();
            return true;
        }else {                 //user 존재하지않음
            $stmt->close();
            return false;
        }
        
    }
    
    //id와 password 통해 user 얻기
    public function getUserByIdAndPassword($id, $password) {
        
        $stmt = $this->conn->prepare("SELECT * FROM user WHERE userid = ?");
        
        $stmt->bind_param("s", $id);
        
        if($stmt->execute()) {
            $user = $stmt->get_result() ->fetch_assoc();
            $stmt->close();
            
            $userid = $user['userid'];
            $userpassword = $user['password'];
            if($password == $userpassword) {
                return $user;
            }
        } else {
            return FALSE;
        }
        
    }
    
    
    
}

?>