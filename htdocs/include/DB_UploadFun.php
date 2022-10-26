<?php

class DB_UploadFun {
    
    private $conn;
    
    function __construct() {
        require_once 'DB_Connect.php';
        $db = new DB_Connect();
        $this -> conn = $db -> connect();
    }
    
    
    public function isReportExisted($isbn, $id) {
        $sql = "SELECT isbn, userid
                FROM report
                WHERE isbn = '".$isbn.
                "' and userid = '".$id."'";
        $result = mysqli_query($this->conn, $sql);
        
        if($result) {
            if($row = mysqli_fetch_array($result)) {
               return true; 
            } else return false;
        }else return false;
    }
    
    public function isLikebookExisted($isbn, $id) {
        $sql = "SELECT *
                FROM likebook
                WHERE isbn = '".$isbn.
                "' and userid = '".$id."'";
        $result = mysqli_query($this->conn, $sql);
        
        if($result) {
            
            if($row = mysqli_fetch_array($result)) { 
                $sql2 = "DELETE FROM likebook 
                        WHERE userid = '".$id."' and isbn = '".$isbn."'";
                $result2 = mysqli_query($this->conn, $sql2);
                
                if($result2) return true;  //likebook에 존재해서 delete 성공
                else return false;   //서버오류로 delete false
                
            }else return true;  //likebook에 존재하지 않음 true
            
        }else return false;  //서버 오류로 false
    }
    
    public function uploadReport($isbn, $id, $star, $is_pub, $mood, $content) {
        $sql = "INSERT INTO report (isbn, userid, date_sav, star, is_pub, mood, content) 
        VALUES ('".$isbn."', '".$id."', now(), ".$star.", ".$is_pub.", '".$mood."', '".$content."')";
        $result = mysqli_query($this->conn, $sql);
        if($result) {
            return true;
        } else return false;
    }
    
    
    public function modifyReport($isbn, $id, $star, $is_pub, $mood, $content) {
        $sql = "UPDATE report SET date_sav = now(), star = ".$star.", is_pub = ".$is_pub.
                ", mood = '".$mood."', content = '".$content."' WHERE isbn = '".$isbn."' and userid = '".$id."'";
        $result = mysqli_query($this->conn, $sql);
        if($result) {
            return true;
        }else return false;
    }
    
    
    
    
}