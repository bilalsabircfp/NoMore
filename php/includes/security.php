<?php 
if(isset($_SESSION["donar_id"]) && $_SESSION["donar_id"]!=''){
$_SESSION["donar_id"]  	= $_SESSION["donar_id"];
}
else{
?>	  <script>
		 		alert('Direct access not allowed ! Please enter credentails ?');
		</script>
<?php
	echo "<script>window.location='".BASE_URL.'login.php?Invalid=AccessNotAllowed'."';</script>";	
}
?>