<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Cage";
$table2="UpdateCage";
$table3="Test";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];
$food=$_POST['FoodAmount'];
$time=$_POST['Time'];

$querySearch="select * from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error1);

if(mysql_num_rows($result))
{
//$querycageID="select CageID from $tbl_name where CageNum='".$cageNum."'";

//$cageID=mysql_query($querycageID) or die(Error);

$queryUpdate="UPDATE $tbl_name SET FoodAmount='".$food."', Time='".$time."' WHERE CageNum='".$cageNum."'";
//WHERE CageID='".$cageID[0]."'";

mysql_query($queryUpdate) or die(Error2);

$queryUpdate2="UPDATE $table2 SET FoodAmount='".$food."', Time='".$time."' WHERE CageNum='".$cageNum."'";

mysql_query($queryUpdate2) or die(Error3);

$ins='c';
$queryInsert="INSERT INTO $table3(TestValue) VALUES('".$ins."')";
mysql_query($queryInsert) or die(Error4);

echo "Success";
}
else
{
echo "Not Found";
}
?>