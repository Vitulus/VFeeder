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

$protectedUsername=mysql_real_escape_string($username);
$protectedPassword=mysql_real_escape_string($password);

$query_search="select * from $tbl_name where Username = '".$protectedUsername."' AND Password = '".$protectedPassword. "'";
$query_exec = mysql_query($query_search) or die(mysql_error());
$rows = mysql_num_rows($query_exec);
//echo $rows
if($rows==0){
echo "No Such User Found";
}
else{
echo "User Found";
}
?>