<?php

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Test2";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$ins=$_POST['param1'];

$queryAdd="INSERT INTO ".$tbl_name."(TestValue) VALUES('".$ins."')";
mysql_query($queryAdd) or die(Error3);

echo "Success";

?>
