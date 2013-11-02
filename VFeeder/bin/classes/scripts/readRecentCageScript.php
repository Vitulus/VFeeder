<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="RecentCage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];

$querySearch="select * from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error);

if(mysql_num_rows($result))
{
$queryRead="select Temp, Food, SiloLevel from ".$tbl_name." where CageNum= '".$cageNum."'";
$reading=mysql_query($queryRead) or die(Error);

$readingArray=mysql_fetch_array($reading);

$temp="$readingArray[0]";
$food="$readingArray[1]";
$silo="$readingArray[2]";

echo "Success/".$temp."/".$food."/".$silo."";
}
else
{
echo "Not found";
}