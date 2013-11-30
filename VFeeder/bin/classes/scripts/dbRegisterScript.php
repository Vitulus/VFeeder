<?php

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Credentials";
$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$username=$_POST['Username'];
$password=$_POST['Password'];
$email=$_POST['Email'];

$protectedUsername=mysql_real_escape_string($username);
$protectedPassword=mysql_real_escape_string($password);
$protectedEmail=mysql_real_escape_string($email);

$query_write= "INSERT INTO $tbl_name(Username, Password, Email)VALUES('".$protectedUsername."', '".$protectedPassword."', '".$protectedEmail."')";

$query_exec=mysql_query($query_write) or die(mysql_error());

mysql_close($localhost);

echo "Success";
?>