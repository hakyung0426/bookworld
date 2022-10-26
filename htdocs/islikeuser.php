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
        
        if($row = mysqli_fetch_array($result)) {  
            $response["error"] = FALSE;
            $response["result"] = true;
        }else {            
            $response["error"] = FALSE;
            $response["result"] =false;
        }
    }else {
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