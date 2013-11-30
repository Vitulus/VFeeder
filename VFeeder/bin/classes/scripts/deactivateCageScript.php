<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Cage";
$tbl_name2="RecentCage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];

$querySearch="select * from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error);

if(mysql_num_rows($result))
{
$status="select Status from ".$tbl_name." where CageNum='".$cageNum."'";
$queryResult=mysql_query($status) or die(Error2);
$resultStatus=mysql_fetch_array($queryResult);

if($resultStatus[0]==0)
{
echo "Inactive";
}
else
{
$deactivate=0;
$queryDeactivate="UPDATE $tbl_name SET Status='".$deactivate."' WHERE CageNum='".$cageNum."'";
mysql_query($queryDeactivate) or die(Error);

$queryRecent="UPDATE $tbl_name2 SET Status='".$deactivate."' WHERE CageNum='".$cageNum."'";
mysql_query($queryRecent) or die(Error);

echo "Success";
}
}
else
{
echo "Not found";
}
?>