<?php

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE);   //json으로 내보낼 최종 결과변수



if(isset($_POST['id'])) {
    
    $id = $_POST['id'];
    $sql = "SELECT title, author, company, date_rel, report.isbn

            FROM report

            JOIN book ON book.isbn = report.isbn 

            WHERE userid = '" .$id.
        
            "'  and is_pub = 1
            ORDER BY date_sav DESC;";
    
    $result = mysqli_query($conn, $sql);
    $data = array();

    if($result) {
        while($row = mysqli_fetch_array($result)) {
            array_push($data, array('title' => $row[0], 'author'=> $row[1],
                                'company' => $row[2], 'date_rel' => $row[3], 'isbn' => $row[4]));
        }
        $response["error"] =  FALSE;
        $response["booklist"] = $data; //REPORT 없으면 빈 행렬 리턴하는거임. 어케할지 고민 필요!
        
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