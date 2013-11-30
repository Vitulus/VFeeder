<?php 

$hostname_localhost="localhost";
$database_localhost="vitulus_tech";
$username_localhost="vitulus_admin";
$password_localhost="abcd1234";
$tbl_name="Credentials";

$localhost=mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);

$email=$_POST['Email'];
$username=$_POST['Username'];

$protectedEmail=mysql_real_escape_string($email);
$protectedUsername=mysql_real_escape_string($username);


mysql_select_db($database_localhost,$localhost);


$query="select * from $tbl_name where Username = '".$protectedUsername."' AND Email = '".$protectedEmail. "'";
$result=mysql_query($query) or die(error);


if(mysql_num_rows($result))
{
$code=rand(100,999);

$password="select Password from $tbl_name where Username = '".$username."' AND Email = '".$email. "'";

$resultPassword=mysql_query($password) or die (error);
$passwordTxt=mysql_fetch_row($resultPassword);

$message="Here is your password: ".$passwordTxt[0];

mail($protectedEmail, "Here's your password", $message);

echo "User exists";
}
else
{
echo "No user exist with this email id";
}

?>