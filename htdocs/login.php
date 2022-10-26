<?php
require_once 'include/DB_LoginFun.php';
$db = new DB_LoginFun();

$response = array("error" => FALSE);

if (isset($_POST['id']) && isset($_POST['password'])) {
    
    
    //receiving params
    $id = $_POST['id'];
    $password = $_POST['password'];
    
    //get id and password
    $user = $db->getUserByIdAndPassword($id, $password);
    
    if($user != false) {
        //user is found
        $response["error"] = FALSE;
        $response["user"]["id"] = $user["userid"];
        $response["user"]["password"] = $user["password"];
        echo json_encode($response);
        
    }else {
        //user is not found
        $response["error"] = TRUE;
        $response["error_msg"] = "wrong password or id";
        echo json_encode($response);
    }
    
}else {
    //required post params is missing
    $response["error"] = TRUE;
    echo json_encode($response);
}

?>