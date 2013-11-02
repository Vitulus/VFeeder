<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="CageHistory";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];
$date=$_POST['Date'];

$my_date=strtotime($date);
$newDate=date('y-m-d',$my_date);

$querySearch="select * from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error);

if(mysql_num_rows($result))
{
$queryFind="select Food, Water, Time from ".$tbl_name." where Date ='".$newDate."'";
$resultDispense=mysql_query($queryFind) or die(Error);
if(mysql_num_rows($resultDispense))
{
$readingArray=mysql_fetch_array($resultDispense);

$food="$readingArray[0]";
$water="$readingArray[1]";
$time="$readingArray[2]";

$newTime=strtotime($time);
$newFormat=date('H:i',$newTime);

echo "Success/".$food."/".$water."/".$newFormat."";
}
else
{
echo "Wrong Date";
}
}
else
{
echo "Not found";
}
?>