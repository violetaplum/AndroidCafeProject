<?php
session_start();
$id=$_POST['id'];  
$password=$_POST['password']; 

$link=mysqli_connect("localhost","root","root", "project" );

if (!$link)  
{  
    echo "MySQL 접속 에러 : ";
    echo mysqli_connect_error();
    exit();  
}else{

mysqli_set_charset($link,"utf8"); 


$sql  = "select id,password,name from Person where id='$id' and password='$password'";
$result=$link->query($sql);

$data = $result->fetch_array(MYSQLI_ASSOC);
$count=mysqli_num_rows($result);

if($count==1){
$_SESSION['id']=$id;
$_SESSION['name']=$name;

echo "1";


}else{
echo "0";

}
}
?>

 
