<?php  

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");


//POST 값을 읽어온다.
$searchkey=isset($_POST['searchkey']) ? $_POST['searchkey'] : '';

if ($searchkey != '' ){ 

    $sql="SELECT title, author, company, date_rel, isbn from book WHERE title like '%$searchkey%'";
    $stmt = $conn->prepare($sql);
    $stmt->execute();

    $data = array(); 
    $stmt->bind_result($title, $author, $company, $date_rel, $isbn);

        while($stmt->fetch()){
                $temp =['title'=>$title,
                'author'=>$author,
                'company'=>$company,
                'date_rel'=>$date_rel,
                'isbn'=>$isbn];

                array_push($data, $temp);
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("response"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
        
    }


?>
