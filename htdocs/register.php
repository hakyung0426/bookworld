<?php

require_once 'include/DB_LoginFun.php';
$db = new DB_LoginFun();

$response = array("error" => FALSE);

if(isset($_POST['id']) && isset($_POST['password'])) {
    
    $id = $_POST['id'];
    $password = $_POST['password'];
    
    if($db->isUserExisted($id)) {//user이미 존재해서 등록실패
        $response["error"] = TRUE;
        $response["type"] = "exist";
        echo json_encode($response); 
    } else {                    //user 존재 안해서 등록시작
        $user = $db->storeUser($id, $password);
        if($user) {             //등록 성공
            $response["error"] = FALSE;
            $response["type"] = "NULL";
        }else {                 //그냥 오류로 등록 실패
            $response["error"] = TRUE;
            $response["type"] = "unknown";
        }
        echo json_encode($response);
    }
    
    
}else {
    $response["error"] = TRUE;
    $response["type"] = "parameter";
    echo json_encode($response);
}

?>