<?php

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Test";

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

//if($ins=='m'){

//$value='c';
//$queryUpdate="UPDATE $tbl_name SET TestValue='".$value."'";
//mysql_query($queryUpdate) or die(Error2);
}

//echo "Success";
}



//echo "Connected";
//echo "a";


?>