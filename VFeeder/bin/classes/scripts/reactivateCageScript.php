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

$querySearch="select * from ".$tbl_name." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error);

if(mysql_num_rows($result))
{
$status="select Status from ".$tbl_name." where CageNum='".$cageNum."'";
if($status[0]==1)
{
echo "Active";
}
else
{
$deactivate=0;
$queryReactivate="UPDATE $tbl_name SET Status='".$deactivate."' WHERE CageNum='".$cageNum."'";

echo "Success";
}
}
else
{
echo "Not found";
}
?>