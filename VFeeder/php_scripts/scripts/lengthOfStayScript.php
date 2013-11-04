<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="RecentCage";
include_once("dateDiffScript.php");

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST["CageNum"];

$querySearch="select Status from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error1);

if(mysql_num_rows($result))
{
$temp1=mysql_fetch_array($result);

if($temp1[0]==1)
{
$queryFind="select DateActivated from ".$tbl_name." where CageNum ='".$cageNum."'";
$resultDate=mysql_query($queryFind) or die(Error2);

$temp2=mysql_fetch_array($resultDate);
//$tempThen=$temp2[0];
//$tempNow= date('Y-m-d');

//$queryDiff="select DATEDIFF(day,'".$tempThen."','".$tempNow."')AS DiffDate";
//$resultDiff=mysql_query($queryDiff) or die(Error3);

//echo "Success/".$resultDiff."";

$date1=date('Y-m-d');
$date2=$temp2[0];
//echo $date2;

//$finalDate = date_diff($date1, $date2);

echo "Success/".$date1."/".$date2."";
}
else
{
echo "Inactive";
}
}
else
{
echo "Not Found";
}
?>


