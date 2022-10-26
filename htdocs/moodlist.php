<?php  

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE); 


if (isset($_POST['mood']) && isset($_POST['category'])){ 

    $mood = $_POST['mood'];
    $category = $_POST['category'];

    if($category=="분야 없음")
        $sql="SELECT title, author, company, date_rel, isbn from book ORDER BY $mood DESC";
    else
        $sql="SELECT title, author, company, date_rel, isbn from book WHERE category_upper='$category' ORDER BY $mood DESC";

    $result = $conn->prepare($sql);
    $result->execute();

    if($result) {

        $data = array(); 
        $result->bind_result($title, $author, $company, $date_rel, $isbn);

            while($result->fetch()){
                    $temp =['title'=>$title,
                    'author'=>$author,
                    'company'=>$company,
                    'date_rel'=>$date_rel,
                    'isbn'=>$isbn];

                    array_push($data, $temp);
                    
            }

            if($data){
                $response["error"] = FALSE;
                $response["moodlist"] = $data;

            }
            
    }else {
        $response["error"] = TRUE;
        $response["type"] = "mysql";
    }
   
}else {
    $response["error"] = TRUE;
    $response["type"] = "parameter";
}


echo json_encode($response);
mysqli_close($conn);


?>