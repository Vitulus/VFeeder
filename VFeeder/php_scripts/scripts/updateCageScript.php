<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Cage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];
$food=$_POST['FoodAmount'];
$water=$_POST['WaterAmount'];
$time=$_POST['Time'];

$querySearch="select * from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error);

if(mysql_num_rows($result))
{
$cageID="select CageID from $tbl_name where CageNum='".$cageNum."'";

$queryUpdate="UPDATE $tbl_name SET CageNum='".$cageNum."', FoodAmount='".$food."', WaterAmount='".$water."' , Time='".$time."'
WHERE CageID='".$cageID."'";

echo "Success";
}
else
{
echo "Not Found";
}
?>