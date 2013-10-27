<?php

$hostname_localhost="www.vitulustech.com";
$database_localhost="vitulus_tech";
$username_localhost="vitulus";
$password_localhost="v!tulu$t3ch";
$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$username=$_POST['Username'];
$password=$_POST['Password'];
$query_search="select * from Credentials where Username = '".$username."' AND Password = '".$password. "'";
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