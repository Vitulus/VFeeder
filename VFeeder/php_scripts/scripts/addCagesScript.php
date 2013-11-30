<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Cage";
$tbl_name1="RecentCage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];
$cageID=$_POST['CageID'];
$food=$_POST['FoodAmount'];
$water=$_POST['WaterAmount'];
$time=$_POST['Time'];

$status=true;

$protectedCageID=mysql_real_escape_string($cageID);

$querySearch="select * from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error);

if(mysql_num_rows($result))
{
echo "Repeated";
}

else
{

$query_write= "INSERT INTO $tbl_name(CageID, CageNum, FoodAmount, WaterAmount, Date, Time, Status)
VALUES('".$protectedCageID."', '".$cageNum."', '".$food."', '".$water."', CURDATE(), '".$time."', '".$status."')";

$query_recent="INSERT INTO $tbl_name1(CageNum, Status, DateActivated) VALUES ('".$cageNum."', '".$status."', CURDATE())";

$query_exec=mysql_query($query_write) or die(mysql_error());

$query_exec=mysql_query($query_recent) or die(mysql_error());

mysql_close($localhost);

echo "Success";
}
?>