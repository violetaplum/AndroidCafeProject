<?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

$link=mysqli_connect("localhost","root","root","project"); 
if (!$link)  
{ 
	   echo "MySQL 접속 에러 : ";
	      echo mysqli_connect_error();
	      exit();
}  


mysqli_set_charset($link,"utf8");  
$id=isset($_POST['id']) ? $_POST['id'] : '';  
$name=isset($_POST['name']) ? $_POST['name'] : '';  
$address=isset($_POST['address']) ? $_POST['address'] : '';  
$password=isset($_POST['password']) ? $_POST['password'] : ''; 
$passwordc=isset($_POST['passwordc']) ? $_POST['passwordc'] : ''; 
$gender=isset($_POST['gender']) ? $_POST['gender'] : '';  
$email=isset($_POST['email']) ? $_POST['email'] : '';  

if ($id !="" and $name !="" and $address !="" and $password !="" and $gender !="" and $email !=""){ 


	$sql="insert into Person(id,name,address,password,gender,email)values('$id','$name','$address','$password','$gender','$email')"; 
	
	$result=mysqli_query($link,$sql);  

	if($result){  
			      
		echo "
		<script>
		window.alert('회원가입 성공');
		location.href='login.php';
		</script>";
	}else{  
		echo "처리중 에러 발생 : "; 
		echo mysqli_error($link);
		} 
		 
}else {
	echo "회원님의 정보를 입력해주세요 ";
}


mysqli_close($link);

?>

<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>
   
         <form action="<?php $_PHP_SELF ?>" method="POST">
		    아이디: <input type="text" name="id"/><br/>
		    비밀번호: <input type="password" name="password"/><br/>
		    비밀번호확인: <input type="password" name="passwordc"/><br/>
	            이름: <input type = "text" name = "name" /><br/>
		    성별: <input type ="radio" name="gender" value="남">남
 			 <input type ="radio" name="gender" value="여">여<br/>
		    주소: <input type = "text" name = "address" /><br/>
		    이메일: <input type = "text" name = "email" /><br/>
		    <input type = "submit" value="확인" />
		     <input type ="reset" value="취소"/>
				          </form>
					     
   </body>
</html>
<?php
}
?>
