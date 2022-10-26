<?php

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE); 

if(isset($_POST['id'])) {
    
    $id = $_POST['id'];
    $sql = "SELECT ed_id FROM follow 
            WHERE ing_id = '".$id."' 
            ORDER BY date_flw DESC;";
    
    $result = mysqli_query($conn, $sql);
    $data = array();

    if($result) {
        while($row = mysqli_fetch_array($result)) {
            array_push($data, $row[0]);
        }
        $response["error"] =  FALSE;
        $response["followlist"] = $data; //REPORT 없으면 빈 행렬 리턴하는거임. 어케할지 고민 필요!
        
    }else {
        $response["error"] =  TRUE;
        $response["type"] = "mysqlerror";
    }
    
    
}else {
    $response["error"] = TRUE;
    $response["type"] = "parameter";
}


echo json_encode($response);
mysqli_close($conn);

    

    


?>