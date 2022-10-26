<?php

require_once 'include/DB_UploadFun.php';
$db = new DB_UploadFun();
mysqli_set_charset($db->$conn, "utf8");

$response = array("error" => FALSE);   //json으로 내보낼 최종 결과변수


if(isset($_POST['isbn']) && isset($_POST['id']) && isset($_POST['star']) && isset($_POST['is_pub']) && isset($_POST['mood']) && isset($_POST['content'])) {
    
    
    $isbn = $_POST['isbn'];
    $id = $_POST['id'];
    $star = $_POST['star'];
    $is_pub = $_POST['is_pub'];
    $mood = $_POST['mood'];
    $content = $_POST['content'];
    
    $isexisted = $db->isReportExisted($isbn, $id);

    if($isexisted) {
        if($db->modifyReport($isbn, $id, $star, $is_pub, $mood, $content)) {
            $response["error"] = false;
        }else  {
            $response["error"] = true;
            $response["type"] = "server1";
        }
    } else {
        if($db->uploadReport($isbn, $id, $star, $is_pub, $mood, $content)) {
            $islikebookexisted = $db->isLikebookExisted($isbn, $id);
            if($islikebookexisted) {
                $response["error"] = false;
            } else {
                $response["error"] = true;
                $response["type"] = "server2";
            }
        }else  {
            $response["error"] = true;
            $response["type"] = "server3";
        }
    }
    

    
    
    
}else {
    $response["error"] = TRUE;
    $response["type"] = "parameter";
}


echo json_encode($response, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
mysqli_close($db->$conn);







?>