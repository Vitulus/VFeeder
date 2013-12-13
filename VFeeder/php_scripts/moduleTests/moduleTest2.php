<?php

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Test2";
$table2="Test";
$table3="RecentCage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$ins=$_POST['param1'];
$ins2=$_POST['param2'];

$queryAdd="INSERT INTO ".$tbl_name."(TestValue) VALUES('".$ins."')";
mysql_query($queryAdd) or die(Error1);

$queryAdd2="INSERT INTO ".$tbl_name."(TempTest) VALUES ('".$ins2."')";
mysql_query($queryAdd2) or die(Error2);

//$sub=subst($ins2,0);
if($ins2[0]=='t')
{
$queryDelete="DELETE from $table2";
mysql_query($queryDelete) or die(Error3);

$string=(string)$ins2;

$temp=substr($string,1,4);
$weight=substr($string,6,4);
$silo=substr($string,11,1);

$cageNum=2;

$queryInsert="UPDATE $table3 SET TempTest='".$temp."', FoodWeight='".$weight."',SiloLevel='".$silo."' WHERE CageNum='".$cageNum."'";
mysql_query($queryInsert) or die(Error4);
}

echo "Success";

?>