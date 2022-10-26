<?php

require_once 'include/DB_BookbugFun.php';
$db = new DB_BookbugFun();

$response = array("error" => FALSE);

if(isset($_POST['id'])) {
    
    $id = $_POST['id'];
    
    $result = $db->feedById($id);
    if($result) {
        $response["error"] = FALSE;
        echo json_encode($response);
    }else {
        $response["error"] = TRUE;
        echo json_encode($response);  
    }
    
} else {
    $response["error"] = TRUE;
    echo json_encode($response); 
}
    

?>