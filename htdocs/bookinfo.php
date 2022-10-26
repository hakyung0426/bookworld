<?php 

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$response = array("error" => FALSE);



if(isset($_POST['isbn']) && isset($_POST['id'])) {
    $isbn = $_POST['isbn'];
    $id = $_POST['id'];
    
    $sql = "SELECT *
            FROM book
            WHERE isbn = '" .$isbn."'";
    
    $sql_2 = "SELECT *
              FROM likebook
              WHERE userid = '".$id.
              "' and isbn = '".$isbn."'";
    
    $sql_3 = "SELECT userid 
            FROM report 
            WHERE userid = '".$id.
            "' and isbn = '".$isbn."'";
    
    $result = mysqli_query($conn, $sql);
    $result2 = mysqli_query($conn, $sql_2);
    $result3 = mysqli_query($conn, $sql_3);

    if($result && $result2 && $result3) {
        $mood_arr= array();
        if($row = mysqli_fetch_array($result)) {
            $i=13;
            while($i < 29) {
                array_push($mood_arr, $row[$i]);
                $i++;
            }
            $mood_max = array_keys($mood_arr, max($mood_arr));
            $mood = $mood_max[array_rand($mood_max, 1)];

            $response["error"] = FALSE;
            $response["title"] = $row["title"];
            $response["author"] = $row["author"];
            $response["company"] = $row["company"];
            $response["date_rel"] = $row["date_rel"];
            $response["category"] = $row["category1"];
            if($row["category2"] != null) {
                $response["category"] = $row["category1"].", ".$row["category2"];
            }
            if($row["category3"] != null) {
                $response["category"] = $row["category1"].", ".$row["category2"].", ".$row["category3"];
            }
            $response["intro"] = $row["intro"];
            $response["star_av"] = $row["star_av"];
            $response["rpt_num"] = $row["rpt_num"];
            $response["mood"] = $mood+1;
            
        }else {
            $response["error"] = true;
            $response["type"] = "existence";
            echo json_encode($response, JSON_UNESCAPED_UNICODE);
            mysqli_close($conn);
            return;
        }
        
        
        
        
        if($row2 = mysqli_fetch_array($result2)) {
            $response["like"] = true;
        }else $response["like"] = false;
        
        if($row3 = mysqli_fetch_array($result3)) {
            $response["isuploaded"] = true;            
        }else $response["isuploaded"] = false;
        
    }else {
        $response["error"] = TRUE;
        $response["type"] = "mysql";
    }
} else {
    $response["error"] = TRUE;
    $response["type"] = "parameter";
}




echo json_encode($response, JSON_UNESCAPED_UNICODE);
mysqli_close($conn);






?>