<?php 

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE);


if(isset($_POST['ing_id']) && isset($_POST['ed_id'])) {
    $ing_id = $_POST['ing_id'];
    $ed_id = $_POST['ed_id'];
    
    $sql = "SELECT *
            FROM follow
            WHERE ing_id = '".$ing_id.
            "' and ed_id = '".$ed_id."'";
    
    $result = mysqli_query($conn, $sql);

    if($result) {
        
        if($row = mysqli_fetch_array($result)) {    //팔로우 목록에 있는 상황에의 sql 설정
                $sql = "DELETE FROM follow WHERE ing_id = '".$ing_id."' and ed_id = '".$ed_id."'";
                $action = "delete";
        }else {                                     //팔로우 목록에 없는 상황에의 sql 설정
            $sql = "INSERT INTO follow(ing_id, ed_id, date_flw) 
                    VALUES ('".$ing_id."', '".$ed_id."', now())";
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