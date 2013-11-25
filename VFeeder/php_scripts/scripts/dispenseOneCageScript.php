<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="DispenseCage";
$table2="Cage";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$cageNum=$_POST['CageNum'];

$querySearch="select * from ".$table2." where CageNum = '".$cageNum."'";
$result=mysql_query($querySearch) or die(Error1);

if(mysql_num_rows($result))
{
$queryActive="Select Status from ".$table2." where CageNum='".$cageNum."'";
$queryResult=mysql_query($queryActive) or die(Error2);
$resultStatus=mysql_fetch_array($queryResult);
if($resultStatus[0]==0)
{
echo "Inactive";
}
else
{
$queryDispense="INSERT INTO $tbl_name(CageNum) VALUES('".$cageNum."')";
mysql_query($queryDispense) or die(Error3);

echo "Success";
}
}
else
{
echo "Not found";
}

?>