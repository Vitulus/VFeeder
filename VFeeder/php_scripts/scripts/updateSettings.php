<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$table="UpdateCage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];


$querySearch="select * from ".$table." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error);

if(mysql_num_rows($result))
{
$queryRead="select FoodAmount, Time from ".$table." where CageNum= '".$cageNum."'";
$reading=mysql_query($queryRead) or die(Error2);

$readingArray=mysql_fetch_array($reading);

$food="$readingArray[0]";
$time="$readingArray[1]";

echo $food;
}
else
{
echo "Not found";
}
?>



