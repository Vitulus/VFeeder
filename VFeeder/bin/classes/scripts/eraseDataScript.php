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

$cageNum=$_POST["CageNum"];

$querySearch="select Status from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error1);

if(mysql_num_rows($result))
{
$queryDelete="DELETE FROM ".$tbl_name." WHERE CageNum='".$cageNum."'";
mysql_query($queryDelete) or die(Error2);
echo "Success";
}
else
{
echo "Not found";
}

?>