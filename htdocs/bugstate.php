<?php


require_once 'include/DB_BookbugFun.php';
$db = new DB_BookbugFun();

$response = array("error" => FALSE);

if(isset($_POST['id'])) {
    
    $id = $_POST['id'];
    
    $user = $db->getUserById($id);
    $date = $user["date_lfd"];
    $date = $db->getStateByLfd($date);
    $response["error"] = FALSE;
    $response["user"] ["fd_num"] = $user["fd_num"];
    $response["user"] ["time"] = $date["TIMESTAMPDIFF(SECOND, ?, now())"];
    echo json_encode($response); 
    
} else {
    $response["error"] = TRUE;
    echo json_encode($response);
}
    
    
    


?>