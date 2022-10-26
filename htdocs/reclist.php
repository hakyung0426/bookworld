<?php  

require_once 'include/DB_Connect.php';
$db = new DB_Connect();

$conn = $db->connect();
mysqli_set_charset($conn, "utf8");

$score = array();

if(isset($_POST['id'])) {
    $id= $_POST['id'];

    $i=1; //report.star에 따른 하위 카테고리 점수 부여
    while($i <= 5) { //report.star이 1일때부터 0.5씩 증가해서 5가 될 때까지 해당 하위 category select
        $sql = "SELECT category1, category2, category3 
                FROM book 
                WHERE book.isbn 
                    IN ( SELECT isbn FROM report WHERE report.userid = '".$id."' and report.star=".$i.")";
        $result = mysqli_query($conn, $sql); //연결된 객체인 $conn을 이용하여 $sql 쿼리를 실행

        if($result) { //$sql 쿼리를 실행했을 때

            while($row = mysqli_fetch_array($result)) {
                $score[$row[0]] = $score[$row[0]]+$i; //category1에 있는 하위카테고리가 속성이 되고 튜플에 점수부여
                $score[$row[1]] = $score[$row[1]]+$i; //category2에 있는 하위카테고리가 속성이 되고 튜플에 점수부여
                $score[$row[2]] = $score[$row[2]]+$i; //category3에 있는 하위카테고리가 속성이 되고 튜플에 점수부여
            }
        }
        $i=$i+0.5;
    }

   //report.date_sav 한 달 이내인 하위 카테고리에 점수(1점) 부여
   //report.date_sqv 한 달 이내인 하위 카테고리 select
    $sql_2 = "SELECT category1, category2, category3 
            FROM book 
            WHERE book.isbn 
                IN (SELECT isbn FROM report WHERE TIMESTAMPDIFF(MINUTE, report.date_sav, now())<43200 and userid = '".$id."')";
    $result2 = mysqli_query($conn, $sql_2); //연결된 객체인 $conn을 이용하여 $sql_2 쿼리를 실행
 
    if($result2) { //$sql_2 쿼리를 실행했을 때
        while($row2 = mysqli_fetch_array($result2)) {
            $score[$row2[0]] = $score[$row2[0]] + 1; //category1에 있는 거 점수 부여
            $score[$row2[1]] = $score[$row2[1]] + 1; //category2에 있는 거 점수 부여
            $score[$row2[2]] = $score[$row2[2]] + 1; //category3에 있는 거 점수 부여
        }
    }

   //찜목록에 있는 isbn에 해당하는 책의 하위 카테고리에 점수 부여
    $sql_3 = "SELECT category1, category2, category3
                FROM book
                WHERE book.isbn
                    IN (SELECT isbn FROM likebook WHERE userid = '".$id."')";

    $result3 = mysqli_query($conn, $sql_3);
    if($result3) {
        while($row3 = mysqli_fetch_array($result3)) {
            $score[$row3[0]] = $score[$row3[0]] + 1;
            $score[$row3[1]] = $score[$row3[1]] + 1;
            $score[$row3[2]] = $score[$row3[2]] + 1; 
        }
    }

   $score[""]=null;  //score로 정렬했을 때 가장 높은 점수에 해당하는 카테고리 2개 선정 //
   arsort($score); 

   $rec_cat1 = array_keys(array_slice($score, 0, 1)); //정렬된 것 중 앞에 2개 따로따로 잘라서 배열 지정
   $rec_cat2 = array_keys(array_slice($score, 1, 1));

   $str_cat1 = implode($rec_cat1); //첫 번째 카테고리 배열 string 변환
   $str_cat2 = implode($rec_cat2);

   //이제 sql로 책 뽑아내야 함

   //BS.isbn과 book.isbn 중 평균별점 4이상인 것 합집합, 찜리스트에서 뺀 것 중 카테고리에 맞는 것만 reclist로 뽑은 후 10개 랜덤제공

   $sql_4 = "SELECT title, author, company, date_rel, isbn
           FROM book
           WHERE ((((category1 = '$str_cat2') or (category1 = '$str_cat1') or (category2 = '$str_cat1') or (category2 = '$str_cat2') or (category3 = '$str_cat2') or (category3 = '$str_cat1'))
           and (star_av >= 4))
           or book.isbn IN (SELECT isbn from BS WHERE (book.category1 = '$str_cat2') or (book.category1 = '$str_cat1') or (book.category2 = '$str_cat1') or (book.category2 = '$str_cat2') or (book.category3 = '$str_cat2') or (book.category3 = '$str_cat1')))
           and NOT EXISTS (SELECT isbn FROM LIKEBOOK WHERE book.isbn = LIKEBOOK.isbn)
           and NOT EXISTS (SELECT isbn FROM report where (book.isbn = report.isbn) and (userid = '".$id."'))
           ORDER BY rand() Limit 10
           ";

   $result4 = mysqli_query($conn, $sql_4);

   $data = array();

   if($result4){
      while($rows = mysqli_fetch_array($result4)){
         array_push($data, array('title' => $rows[0], 'author'=>$rows[1], 'company'=>$rows[2], 'date_rel'=>$rows[3], 'isbn'=>$rows[4]));
      }
      $response["error"] = FALSE;
      $response["reclist"] = $data;
   }else{
      $response["error"] = TRUE;
      $response["type"]="mysqlerror";
      }
  }else{
     $response["error"]=TRUE;
     $response["type"]="parameter";   
   }

   
echo json_encode($response, JSON_UNESCAPED_UNICODE);
mysqli_close($conn);




?>


<html>
   <body>
   
      <form action="<?php $_PHP_SELF ?>" method="POST">
         id: <input type = "text" name = "id" />
         <input type = "submit" />
      </form>
   
   </body>
