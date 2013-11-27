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

  $ins='f';
  $queryInsert="INSERT INTO $tbl_name(TestValue) VALUES('".$ins."')";
  mysql_query($queryInsert) or die(Error3);
  
  $ins2=$_POST['param1'];
  
  if($ins2=="m")
  {  
  echo "Success";
  }
  
  else
  {
  echo "Error";
  }
  
  ?>