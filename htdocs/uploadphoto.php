<?php

require_once 'include/DB_Connect.php';
$db = new DB_Connect();
$conn = $db->connect();

 
if($_SERVER['REQUEST_METHOD'] == 'POST') {
    
    $ImageData = $_POST['image_data'];
    $ImageName = $_POST['image_name'];
    $ImagePath = "reportimg/".$ImageName.".jpeg";
    file_put_contents($ImagePath, base64_decode($ImageData));
    echo "Your Image Has Been Uploaded."; 

} else {
    echo "Not Uploaded";
}

    mysqli_close($conn);
?>