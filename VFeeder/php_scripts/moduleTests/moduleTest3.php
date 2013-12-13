<?php

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Test";
$table2="DispenseCage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$ins=$_POST['param1'];

$querySearch="select * from $tbl_name";
$result=mysql_query($querySearch) or die(Error1);

if(mysql_num_rows($result))
{
$resultIns=mysql_fetch_array($result);

echo $resultIns[0];


$queryDelete="DELETE FROM $tbl_name";

mysql_query($queryDelete) or die(Error2);

$queryDelete2="DELETE FROM $table2";
mysql_query($queryDelete2) or die(Error3);

//echo "Success";
}

?>