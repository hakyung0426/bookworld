<?php 

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE);



if(isset($_POST['isbn'])) {
    $isbn = $_POST['isbn'];
    
    $sql = "SELECT userid, date_sav, star, mood, content
            FROM report
            WHERE isbn = '" .$isbn."' 
                and is_pub = 1 
            ORDER BY date_sav DESC";
    $result = mysqli_query($conn, $sql);
    $data = array();

    if($result) {
        while($row = mysqli_fetch_array($result)) {
            array_push($data, array('userid' => $row['userid'], 'date_sav'=> $row['date_sav'],
                                'star' => $row['star'], 'mood' => $row['mood'], 'content' => $row['content']));
        }
        $response["error"] =  FALSE;
        $response["reviewlist"] = $data;
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