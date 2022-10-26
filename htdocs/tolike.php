<?php 

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE);


if(isset($_POST['id']) && isset($_POST['isbn'])) {
    $id = $_POST['id'];
    $isbn = $_POST['isbn'];
    
    $sql = "SELECT userid
            FROM likebook
            WHERE userid = '".$id.
            "' and isbn = '".$isbn."'";
    
    $result = mysqli_query($conn, $sql);

    if($result) {
        
        if($row = mysqli_fetch_array($result)) {    //찜목록에 있는 상황에의 sql 설정
                $sql = "DELETE FROM likebook WHERE userid = '".$id."' and isbn = '".$isbn."'";
                $action = "delete";
        }else {                                     //찜목록에 없는 상황에의 sql 설정
            $sql = "INSERT INTO likebook(userid, isbn, date_fav) 
                    VALUES ('".$id."', '".$isbn."', now())";
            $action = "add";
        }
        
        
        $result = mysqli_query($conn, $sql);
        if($result) {
            $response["error"] = false;
            $response["result"] = $action;
        }else {                                     //delete or insert 실패 error
            $response["error"] = true;
            $response["type"] = "mysql";
        }
    
        
    }else {                                         //select 실패 error
        $response["error"] = TRUE;
        $response["type"] = "mysql";
    }
    
    
    
} else {
    $response["error"] = TRUE;
    $response["type"] = "parameter";
}




echo json_encode($response, JSON_UNESCAPED_UNICODE);
mysqli_close($conn);






?>