<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="DispenseCage";
$table2="Cage";
$table3="Test";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

mysql_select_db($database_localhost,$localhost);

$active=1;
$querySearch="select CageNum from ".$table2." where Status = '".$active."'";

$result=mysql_query($querySearch) or die(Error1);

if(mysql_num_rows($result))
{

$column=array();
while($row = mysql_fetch_array($result))
{
    $column[] = $row[0];
}

$arrlength=count($column);

for($x=0;$x<$arrlength;$x++)
  {
  $cageNum=$column[$x];  
 $queryDispense="INSERT INTO $tbl_name(CageNum) VALUES('".$cageNum."')"; 
 mysql_query($queryDispense) or die(Error2);
  }
  
  $ins='a';
  $queryInsert="INSERT INTO $table3(TestValue) VALUES('".$ins."')";
  mysql_query($queryInsert) or die(Error3);
  
  echo "Success";
}
  else
  {
  echo "Not found";
  }
  
  ?>