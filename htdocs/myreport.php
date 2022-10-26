<?php 

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE);



if(isset($_POST['id']) && isset($_POST['isbn'])) {
    $id = $_POST['id'];
    $isbn = $_POST['isbn'];
    
    $sql = "SELECT date_sav, mood, is_pub, star, content
            FROM report
            WHERE userid = '" .$id.
            "' and isbn = " .$isbn;
    $result = mysqli_query($conn, $sql);

    if($result) {
        $row = mysqli_fetch_array($result);
        $response["error"] = FALSE;
        $response["date_sav"] = $row[0];
        $response["mood"] = $row[1];
        $response["is_pub"] = $row[2];
        $response["star"] = $row[3];
        $response["content"] = $row[4];
    }else {
        $response["error"] = TRUE;
    }
} else {
    $response["error"] = TRUE;
    $response["type"] = "parameter";
}




echo json_encode($response, JSON_UNESCAPED_UNICODE);
mysqli_close($conn);






?>