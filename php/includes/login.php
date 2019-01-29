<?php
require_once 'conffig.php';
require_once "connect.php";
if($_SERVER["REQUEST_METHOD"]==="POST"){
    $conect=new connect();
    //var_dump($conect);
    $u=$_POST["d_mobile"];
    $p=$_POST["d_password"];
	//exit('TRUE');
	$result_=$conect->select("donar_signup", "*", "`d_mobile`='$u'");
    if($result_->num_rows){//check user existance
        //var_dump($result);
        $result=$result_->fetch_array();
        //print_r($result);exit;
        if($result["d_password"]===$p) {
            
            $_SESSION["donar_id"]     = $result['d_id'];
			$_SESSION["donar_name"]   = $result['d_name'];
            $_SESSION["donar_fname"]  = $result['d_fathername']; 
            $_SESSION["donar_street"]  = $result['d_street']; 
            $_SESSION["donar_age"]  = $result['d_age']; 
            $_SESSION['donar_city']   = $result['d_city'];
            $_SESSION['donar_bg']     = $result['bg_id'];
            $_SESSION['donar_mobile'] = $result['d_mobile'];
            $_SESSION['donar_email'] = $result['d_email'];
            $_SESSION['donar_password'] = $result['d_password'];
            $_SESSION['d_latitude']   = $result['d_latitude'];
            $_SESSION['d_longitude']  = $result['d_longitude'];
            $_SESSION['dlast_donated']= $result['d_last_donated'];
            
             
          	header("Location:".BASE_URL."dashboard.php");
        }
		else{
            header("Location:".BASE_URL."/login.php?Invalid=WrongPass");
        }
    }else{
        	header("Location:".BASE_URL."/login.php?Invalid=NotExists");
    }
}