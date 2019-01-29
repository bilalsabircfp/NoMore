<?php 
header('Content-type:application/json');
include 'includes/conffig.php';
require("includes/connect.php");
$conect = new connect();
//exit('True'); 
error_reporting(0);
?>

<?php

$operation = trim($_REQUEST['operation']);


//echo $d_city;
//////////////////////////////////////////////////////////

$name      	= trim($_REQUEST['name']);
$pass    	= trim($_REQUEST['pass']);
$number    	= trim($_REQUEST['number']);
$lat 			= trim($_REQUEST['lat']);
$lon 			= trim($_REQUEST['lon']);
$token 			= trim($_REQUEST['token']);


if(!isset($operation) || $operation == "") exit('There is no operation');





///////////////////////////////////////////////////////////
//firebase code
///////////////////////////////////////////////////////////
//firebase code
function send_notification($tokenid, $name, $lat, $lon){
	
//	print_r($tokenid);
	
	#API access key from Google API's Console
    define( 'API_ACCESS_KEY', 'AAAAO7wMs40:APA91bEVRYSXWPWVnbb42r20aecCyrkLIASmL28fVIY2SgUvhugmCoiV-ijxekoRh2jwibxAUFNrPzcoldtKD34LGVh66064rkekXOqaIfhZqnHordH-sUcgQNTq5fn7qjXa_rlVCG6y' );
  //  $registrationIds = 'fqbEmNRX7MQ:APA91bHccXDOA8q9BUeNT6ApbMpomcBG7deWEk4Zk-YDKk7DuV-HlxT3kPawR6W9nZrQ93_eWYQwz2aDlgFrce1J1A0OD4qbttrwmA6C5K5mRX3-MixndXfJdTiXHCa-YYGsP2C4eIZ7';
#prep the bundle
     $msg = array
          (
		'body' 	=> 'Click to see details.',
		'title'	=> 'Someone Needs Help!',
             	'icon'	=> 'myicon',/*Default Icon*/
              	'sound' => 'mySound',/*Default sound*/
				'vibrate' => 1,
				"click_action" => "ACTIVITY_DONOR",
          );
	$data = array
    (
         "name" => $name,
         "lat" => $lat,
         "lon" => $lon,
         
		 );

	$fields = array
			(
				'to'		=> $tokenid,
				'notification'	=> $msg,
				'data' => $data
			);
	
	
	$headers = array
			(
				'Authorization: key=' . API_ACCESS_KEY,
				'Content-Type: application/json'
			);
#Send Reponse To FireBase Server	
		$ch = curl_init();
		curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
		curl_setopt( $ch,CURLOPT_POST, true );
		curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
		curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
		curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
		curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
		$result = curl_exec($ch );
		curl_close( $ch );
#Echo Result Of FireBase Server
//echo $result;
	
	
}


/////////////////////////////////////////////////////////////



/////////////////////////////////////////////////////////

$i = explode(',',$treatment_ids);


////////////////////////////////////////////////////////////


//load list
if($operation && $operation=="list"){
    
    $select = "select * from users";

    $query 	= $conect->select_custom($select);
	
	if($query->num_rows > 0)
	{
		$JsonArray = array();
		$JsonArray[] = array('response'=>'1');
		while($num =  $query->fetch_assoc()){
		 $JsonArray[] = array(
			"name" =>$num['name'],
			"lat" =>$num['lat'],
			"lon" =>$num['lon'],
			"token" =>$num['token']
             
		);
        }
           
        echo json_encode(array('JsonData'=>$JsonArray), JSON_PRETTY_PRINT);
    }
        
	else
	{
		$JsonArray = array();
		$JsonArray[] = array('response'=>'0', 'response-text'=>'No Blood Group Matched Donor Found');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);	
	}
	
}




//register 
elseif($operation && $operation == 'register' && $name && $number && $lat && $lon && $token)
{ 

if($conect->insert("users","`name`,`number`,`lat`,`lon`,`token`",
"'$name','$number','$lat','$lon','$token' "))
	{

		$JsonArray   = array();
		$JsonArray[] = array('response'=>'1', 'response-text'=>'Registered successfully!');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
	}
	
		elseif(mysql_errno() == 1062)
		{
			$JsonArray   = array();
			$JsonArray[] = array('response'=>'0', 'response-text'=>'Mobile no already exists');
			echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
		} 
		else
		{
			$JsonArray   = array();
			$JsonArray[] = array('response'=>'0', 'response-text'=>mysql_error());
			echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
		}
}







//send notifications
elseif($operation == 'notify' && $token && $name && $lat && $lon)
{ 

	//print_r (explode(",",$token));

	$cars = explode(",",$token);
//	print_r ($token);
//print_r ($name);print_r ($lat);print_r ($lon);
	
	$total = count($cars);
	for ($i = 0; $i < $total; $i++) {
	print_r ($cars[$i]);
	send_notification($cars[$i], $name, $lat , $lon);
}
	
	/*$select = 'select * from donar_signup   ds join blood_group bg on ds.bg_id=bg.bg_id where ds.d_city="'.$d_city.'" AND ds.bg_id="'.$bg_id.'" ';

	$query 	= $conect->select_custom($select);	
	while($token = $query->fetch_assoc()){
		
	$bg_name = $token['bg_name'];
    $token_no = $token['token'];
	print_r($token_no);*/
	
	//oreach($cars as $value) {
 // print $value;
//}
	
	//send_notification($value,"kak","kaak","sdsd");
		
	//}

}










//load single profile
elseif($operation=="load_profile" && $d_mobile){
  
    $select = 'select * from donar_signup  ds join blood_group bg on ds.bg_id=bg.bg_id  where ds.d_mobile="'.$d_mobile.'"';
    $query =$conect->select_custom($select);   
    
	if($query->num_rows > 0)
	{
		$JsonArray = array();
		$JsonArray[] = array('response'=>'1');
		while($num = $query->fetch_assoc()){
		$JsonArray[] = array(
                        "d_name" =>$num['d_name'],
			"d_latitude" =>$num['d_latitude'],
			"d_longitude" =>$num['d_longitude'],
                        "d_city" =>$num['d_city'],
			"d_mobile" =>$num['d_mobile'],
			"d_street" =>$num['d_street'],
			"d_age" =>$num['d_age'],
            "d_status" =>$num['d_status'],
            "bg_name" =>$num['bg_name'],
            
        );
        }
           
        echo json_encode(array('JsonData'=>$JsonArray), JSON_PRETTY_PRINT);
    }
        
	else
	{
		$JsonArray = array();
		$JsonArray[] = array('response'=>'0', 'response-text'=>'Record not found');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);	
	}
	
}


//update token

elseif($operation && $operation == 'app_token' && $token && $d_mobile)
{ 
	if($conect->update("donar_signup",
    	"`token`='$token' " , "`d_mobile`='$d_mobile' ")){

		$JsonArray = array();
		$JsonArray[] = array('response'=>'1', 'response-text'=>'Welcome!');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT); 
	}
	else{
		$JsonArray = array();
		$JsonArray[] = array('response'=>'0', 'response-text'=>'Update Unsuccessfully!');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
	}
		
}








//update profile
elseif($operation && $operation == 'update' && $d_id && $d_last_donated)
{ 


	if($conect->update("donar_signup",
    	"`d_last_donated`='$d_last_donated' "  , "`d_id`='$d_id'"  )){
		$JsonArray = array();
		$JsonArray[] = array('response'=>'1', 'response-text'=>'Profile updated succesfully');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
	}
	else{
		$JsonArray = array();
		$JsonArray[] = array('response'=>'0', 'response-text'=>'Update Unsuccessfully!');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
	}
}



//update status
elseif($operation && $operation == 'update_status' && $d_id && $d_status)
{ 


	if($conect->update("donar_signup",
    	"`d_status`='$d_status' "  , "`d_id`='$d_id'"  )){
		$JsonArray = array();
		$JsonArray[] = array('response'=>'1', 'response-text'=>'Profile updated succesfully');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
	}
	else{
		$JsonArray = array();
		$JsonArray[] = array('response'=>'0', 'response-text'=>'Update Unsuccessfully!');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
	}
}






//recover password
elseif($operation == 'recover' && $d_email)
{ 

	$sel_password = "SELECT * FROM `donar_signup` where d_email = '".$d_email."' ";

	$accept_res   = $conect->select_custom($sel_password);
    $fet_accept   = $accept_res->fetch_assoc();

   	$password = $fet_accept['d_password'];
	

	$subject = "Your Recovered Password";
 
	$message = "Please use this password to login " . $password;
	$headers = 'From: webmaster@example.com' . "\r\n" .
    'Reply-To: webmaster@example.com' . "\r\n" .
    'X-Mailer: PHP/' . phpversion();

	if(mail($d_email, $subject, $message, $headers)){

           
		echo "Your Password has been sent to your email id!";
	}else{

		echo "Failed to Recover your password, try again";
	}
	
	
}



//web notifications 
elseif($operation && $operation == 'web' && $acceptor_name && $acceptor_city && $acceptor_mobile 
			 && $lat && $lon && $bg_id)
{ 


if($conect->insert("blood_accept","`acceptor_name`,`acceptor_city`,`acceptor_mobile`,`lat`,`lon`,`bg_id`",
"'$acceptor_name','$acceptor_city','$acceptor_mobile','$lat','$lon', '$bg_id' "))
	{

		$JsonArray   = array();
		$JsonArray[] = array('response'=>'1', 'response-text'=>'Registered successfully!');
		echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
	}
	
		elseif(mysql_errno() == 1062)
		{
			$JsonArray   = array();
			$JsonArray[] = array('response'=>'0', 'response-text'=>'Mobile no already exists');
			echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
		} 
		else
		{
			$JsonArray   = array();
			$JsonArray[] = array('response'=>'0', 'response-text'=>mysql_error());
			echo json_encode(array('JsonData'=>$JsonArray),JSON_PRETTY_PRINT);
		}
}







	
	
?>
	