<?php

session_start();
include(‘dbConnectScript.php’);

$username=$_POST[‘Username’];
$password=$_POST[‘Password’];
$email=$_POST[‘Email’];

mysql_query(“INSERT INTO Credentials(username, password, email)VALUES(‘$username’, ‘$password’, ‘$email’)”);

mysql_close($con);

echo “Success”
?>