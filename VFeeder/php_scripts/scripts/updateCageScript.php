<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Cage";
$table2="UpdateCage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];
$food=$_POST['FoodAmount'];
$water=$_POST['WaterAmount'];
$time=$_POST['Time'];

$querySearch="select * from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error1);

if(mysql_num_rows($result))
{
//$querycageID="select CageID from $tbl_name where CageNum='".$cageNum."'";

//$cageID=mysql_query($querycageID) or die(Error);

$queryUpdate="UPDATE $tbl_name SET FoodAmount='".$food."', WaterAmount='".$water."' , Time='".$time."' WHERE CageNum='".$cageNum."'";
//WHERE CageID='".$cageID[0]."'";

mysql_query($queryUpdate) or die(Error2);

$queryUpdate2="INSERT INTO $table2(CageNum,FoodAmount,WaterAmount,Time) VALUES ('".$cageNum."','".$food."','".$water."','".$time."')";

mysql_query($queryUpdate2) or die(Error3);

echo "Success";
}
else
{
echo "Not Found";
}
?>