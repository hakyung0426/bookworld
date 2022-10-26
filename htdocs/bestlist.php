<?php

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE);   //json으로 내보낼 최종 결과변수

$sql = "SELECT title, author, company, date_rel, isbn

        FROM book

        WHERE book.isbn IN (SELECT isbn FROM BS)

        ORDER BY star_av DESC;";

$result = mysqli_query($conn, $sql);
$data = array();

if($result) {
     while($row = mysqli_fetch_array($result)) {
         array_push($data,
            array('title' => $row[0], 'author'=> $row[1],
                'company' => $row[2], 'date_rel' => $row[3],
                'isbn' => $row[4]));
        }
        $response["error"] =  FALSE;
        $response["bestlist"] = $data; 
        
    }else {
        $response["error"] =  TRUE;
        $response["type"] = "mysqlerror";
    }


echo json_encode($response, JSON_UNESCAPED_UNICODE);
mysqli_close($conn);

    

?>
